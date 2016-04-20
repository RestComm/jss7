/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.m3ua.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javolution.text.TextBuilder;
import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.ss7.m3ua.As;
import org.mobicents.protocols.ss7.m3ua.Asp;
import org.mobicents.protocols.ss7.m3ua.AspFactory;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.M3UAManagement;
import org.mobicents.protocols.ss7.m3ua.M3UAManagementEventListener;
import org.mobicents.protocols.ss7.m3ua.RouteAs;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.oam.M3UAOAMMessages;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.scheduler.M3UAScheduler;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.mtp.Mtp3EndCongestionPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartBaseImpl;
import org.mobicents.protocols.ss7.mtp.RoutingLabelFormat;

/**
 * @author amit bhayani
 */
public class M3UAManagementImpl extends Mtp3UserPartBaseImpl implements M3UAManagement {
    private static final Logger logger = Logger.getLogger(M3UAManagementImpl.class);
    private static final String AS_LIST = "asList";
    private static final String ASP_FACTORY_LIST = "aspFactoryList";
    private static final String DPC_VS_AS_LIST = "route";

    private static final String MAX_AS_FOR_ROUTE_PROP = "maxasforroute";
    private static final String MAX_SEQUENCE_NUMBER_PROP = "maxsequencenumber";
    private static final String HEART_BEAT_TIME_PROP = "heartbeattime";

    private static final String M3UA_PERSIST_DIR_KEY = "m3ua.persist.dir";
    private static final String USER_DIR_KEY = "user.dir";
    private static final String PERSIST_FILE_NAME = "m3ua1.xml";

    private static final M3UAXMLBinding binding = new M3UAXMLBinding();
    private static final String TAB_INDENT = "\t";
    private static final String CLASS_ATTRIBUTE = "type";

    protected static final int MAX_SEQUENCE_NUMBER = 256;

    protected FastList<As> appServers = new FastList<As>();
    protected FastList<AspFactory> aspfactories = new FastList<AspFactory>();

    protected M3UAScheduler m3uaScheduler = new M3UAScheduler();

    private final TextBuilder persistFile = TextBuilder.newInstance();

    private final String name;

    private String persistDir = null;

    protected ParameterFactory parameterFactory = new ParameterFactoryImpl();
    protected MessageFactory messageFactory = new MessageFactoryImpl();

    protected Management transportManagement = null;
    protected boolean sctpLibNettySupport = false;

    private ScheduledExecutorService fsmTicker;

    protected int maxAsForRoute = 2;

    protected int timeBetweenHeartbeat = 10000; // 10 sec default

    private M3UARouteManagement routeManagement = null;

    protected FastList<M3UAManagementEventListener> managementEventListeners = new FastList<M3UAManagementEventListener>();

    /**
     * Maximum sequence number received from SCCTP user. If SCCTP users sends seq number greater than max, packet will be
     * dropped and error message will be logged
     */
    private int maxSequenceNumber = MAX_SEQUENCE_NUMBER;

    public M3UAManagementImpl(String name, String productName) {
        super(productName);
        this.name = name;
        binding.setClassAttribute(CLASS_ATTRIBUTE);
        binding.setAlias(AspFactoryImpl.class, "aspFactory");
        binding.setAlias(AsImpl.class, "as");
        binding.setAlias(AspImpl.class, "asp");

        this.routeManagement = new M3UARouteManagement(this);

    }

    public String getName() {
        return name;
    }

    public int getMaxSequenceNumber() {
        return maxSequenceNumber;
    }

    /**
     * Set the maximum SLS that can be used by SCTP. Internally SLS vs SCTP Stream Sequence Number is maintained. Stream Seq
     * Number 0 is for management.
     *
     * @param maxSls the maxSls to set
     */
    public void setMaxSequenceNumber(int maxSequenceNumber) throws Exception {
        if (this.isStarted)
            throw new Exception("MaxSequenceNumber parameter can be updated only when M3UA stack is NOT running");

        if (maxSequenceNumber < 1) {
            maxSequenceNumber = 1;
        } else if (maxSequenceNumber > MAX_SEQUENCE_NUMBER) {
            maxSequenceNumber = MAX_SEQUENCE_NUMBER;
        }
        this.maxSequenceNumber = maxSequenceNumber;
    }

    public String getPersistDir() {
        return persistDir;
    }

    public void setPersistDir(String persistDir) {
        this.persistDir = persistDir;
        this.persistFile.clear();
    }

    public int getMaxAsForRoute() {
        return maxAsForRoute;
    }

    public void setMaxAsForRoute(int maxAsForRoute) throws Exception {
        if (this.isStarted)
            throw new Exception("MaxAsForRoute parameter can be updated only when M3UA stack is NOT running");

        if (maxAsForRoute < 1) {
            maxAsForRoute = 1;
        } else if (maxAsForRoute > 4) {
            maxAsForRoute = 4;
        }

        this.maxAsForRoute = maxAsForRoute;
    }

    public int getHeartbeatTime() {
        return this.timeBetweenHeartbeat;
    }

    public void setHeartbeatTime(int timeBetweenHeartbeat) throws Exception {
        if (!this.isStarted)
            throw new Exception("HeartbeatTime parameter can be updated only when M3UA stack is running");

        if (timeBetweenHeartbeat < 1000) {
            // minimum 1 sec is reasonable?
            timeBetweenHeartbeat = 1000;
        }

        this.timeBetweenHeartbeat = timeBetweenHeartbeat;

        this.store();
    }

    @Override
    public void setUseLsbForLinksetSelection(boolean useLsbForLinksetSelection) throws Exception {
        if (!this.isStarted)
            throw new Exception("UseLsbForLinksetSelection parameter can be updated only when M3UA stack is running");

        super.setUseLsbForLinksetSelection(useLsbForLinksetSelection);

        this.store();
    }

    @Override
    public void setRoutingLabelFormat(RoutingLabelFormat routingLabelFormat) throws Exception {
        if (this.isStarted)
            throw new Exception("RoutingLabelFormat parameter can be updated only when M3UA stack is NOT running");

        super.setRoutingLabelFormat(routingLabelFormat);
    }

    @Override
    public void setDeliveryMessageThreadCount(int deliveryMessageThreadCount) throws Exception {
        if (this.isStarted)
            throw new Exception("DeliveryMessageThreadCount parameter can be updated only when M3UA stack is NOT running");

        super.setDeliveryMessageThreadCount(deliveryMessageThreadCount);
    }

    public Management getTransportManagement() {
        return transportManagement;
    }

    public void setTransportManagement(Management transportManagement) {
        this.transportManagement = transportManagement;
        if (transportManagement != null && transportManagement.getClass().getSimpleName().contains("Netty"))
            sctpLibNettySupport = true;
    }

    public boolean isSctpLibNettySupport() {
        return sctpLibNettySupport;
    }

    public void start() throws Exception {

        if (this.transportManagement == null) {
            throw new NullPointerException("TransportManagement is null");
        }

        if (maxAsForRoute < 1 || maxAsForRoute > 4) {
            throw new Exception("Max AS for a route can be minimum 1 or maximum 4");
        }

        super.start();

        this.preparePersistFile();
        logger.info(String.format("M3UA configuration file path %s", persistFile.toString()));

        binding.setM3uaManagement(this);

        try {
            this.load();
        } catch (FileNotFoundException e) {
            logger.warn(String.format("Failed to load the SS7 configuration file. \n%s", e.getMessage()));
        }

        fsmTicker = Executors.newSingleThreadScheduledExecutor();
        fsmTicker.scheduleAtFixedRate(m3uaScheduler, 500, 500, TimeUnit.MILLISECONDS);

        for (FastList.Node<M3UAManagementEventListener> n = this.managementEventListeners.head(), end = this.managementEventListeners
                .tail(); (n = n.getNext()) != end;) {
            M3UAManagementEventListener m3uaManagementEventListener = n.getValue();
            try {
                m3uaManagementEventListener.onServiceStarted();
            } catch (Throwable ee) {
                logger.error("Exception while invoking M3UAManagementEventListener.onServiceStarted", ee);
            }
        }

        logger.info("Started M3UAManagement");
    }

    private void preparePersistFile() {
        if (this.persistFile.length() > 0)
            return;

        if (persistDir != null) {
            this.persistFile.append(persistDir).append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
        } else {
            persistFile.append(System.getProperty(M3UA_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY)))
                    .append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
        }
    }

    public void stop() throws Exception {

        for (FastList.Node<M3UAManagementEventListener> n = this.managementEventListeners.head(), end = this.managementEventListeners
                .tail(); (n = n.getNext()) != end;) {
            M3UAManagementEventListener m3uaManagementEventListener = n.getValue();
            try {
                m3uaManagementEventListener.onServiceStopped();
            } catch (Throwable ee) {
                logger.error("Exception while invoking onServiceStopped", ee);
            }
        }

        this.store();

        this.stopFactories();
        super.stop();

        fsmTicker.shutdown();
    }

    @Override
    public boolean isStarted() {
        return this.isStarted;
    }

    @Override
    public void addM3UAManagementEventListener(M3UAManagementEventListener m3uaManagementEventListener) {
        synchronized (this) {
            if (this.managementEventListeners.contains(m3uaManagementEventListener))
                return;

            FastList<M3UAManagementEventListener> newManagementEventListeners = new FastList<M3UAManagementEventListener>();
            newManagementEventListeners.addAll(this.managementEventListeners);
            newManagementEventListeners.add(m3uaManagementEventListener);
            this.managementEventListeners = newManagementEventListeners;
        }
    }

    @Override
    public void removeM3UAManagementEventListener(M3UAManagementEventListener m3uaManagementEventListener) {
        synchronized (this) {
            if (!this.managementEventListeners.contains(m3uaManagementEventListener))
                return;

            FastList<M3UAManagementEventListener> newManagementEventListeners = new FastList<M3UAManagementEventListener>();
            newManagementEventListeners.addAll(this.managementEventListeners);
            newManagementEventListeners.remove(m3uaManagementEventListener);
            this.managementEventListeners = newManagementEventListeners;
        }
    }

    public List<As> getAppServers() {
        FastList<As> appServersTmp = new FastList<As>();
        appServersTmp.addAll(this.appServers);
        return appServersTmp;
    }

    public List<AspFactory> getAspfactories() {
        FastList<AspFactory> aspfactoriesTmpe = new FastList<AspFactory>();
        aspfactoriesTmpe.addAll(this.aspfactories);
        return aspfactoriesTmpe;
    }

    public Map<String, RouteAs> getRoute() {
        Map<String, RouteAs> routeTmp = new HashMap<String, RouteAs>();
        routeTmp.putAll(this.routeManagement.route);
        return routeTmp;
    }

    protected As getAs(String asName) {
        for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
            As as = n.getValue();
            if (as.getName().equals(asName)) {
                return as;
            }
        }
        return null;
    }

    /**
     * <p>
     * Create new {@link AsImpl}
     * </p>
     * <p>
     * Command is m3ua as create <as-name> <AS | SGW | IPSP> mode <SE | DE> ipspType <client | server > rc <routing-context>
     * traffic-mode <traffic mode> min-asp <minimum asp active for TrafficModeType.Loadshare> network-appearance <network
     * appearance>
     * </p>
     * <p>
     * where mode is optional, by default SE. ipspType should be specified if type is IPSP. rc is optional and traffi-mode is
     * also optional, default is Loadshare
     * </p>
     *
     * @param args
     * @return
     * @throws Exception
     */
    public As createAs(String asName, Functionality functionality, ExchangeType exchangeType, IPSPType ipspType,
            RoutingContext rc, TrafficModeType trafficMode, int minAspActiveForLoadbalance, NetworkAppearance na)
            throws Exception {

        As as = this.getAs(asName);
        if (as != null) {
            throw new Exception(String.format(M3UAOAMMessages.CREATE_AS_FAIL_NAME_EXIST, asName));
        }

        // TODO check if RC is already taken?

        if (exchangeType == null) {
            exchangeType = ExchangeType.SE;
        }

        if (functionality == Functionality.IPSP && ipspType == null) {
            ipspType = IPSPType.CLIENT;
        }

        as = new AsImpl(asName, rc, trafficMode, minAspActiveForLoadbalance, functionality, exchangeType, ipspType, na);
        ((AsImpl) as).setM3UAManagement(this);
        FSM localFSM = ((AsImpl) as).getLocalFSM();
        this.m3uaScheduler.execute(localFSM);

        FSM peerFSM = ((AsImpl) as).getPeerFSM();
        this.m3uaScheduler.execute(peerFSM);

        appServers.add(as);

        this.store();

        for (FastList.Node<M3UAManagementEventListener> n = this.managementEventListeners.head(), end = this.managementEventListeners
                .tail(); (n = n.getNext()) != end;) {
            M3UAManagementEventListener m3uaManagementEventListener = n.getValue();
            try {
                m3uaManagementEventListener.onAsCreated(as);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAsCreated", ee);
            }

        }

        return as;
    }

    public AsImpl destroyAs(String asName) throws Exception {
        AsImpl as = (AsImpl) this.getAs(asName);
        if (as == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_AS_FOUND, asName));
        }

        if (as.appServerProcs.size() != 0) {
            throw new Exception(String.format(M3UAOAMMessages.DESTROY_AS_FAILED_ASP_ASSIGNED, asName));
        }

        for (FastMap.Entry<String, RouteAsImpl> e = this.routeManagement.route.head(), end = this.routeManagement.route.tail(); (e = e
                .getNext()) != end;) {
            RouteAsImpl asList = e.getValue();
            if(asList.hasAs(as)){
                throw new Exception(String.format(M3UAOAMMessages.AS_USED_IN_ROUTE_ERROR, asName, e.getKey()));
            }
        }

        FSM asLocalFSM = as.getLocalFSM();
        if (asLocalFSM != null) {
            asLocalFSM.cancel();
        }

        FSM asPeerFSM = as.getPeerFSM();
        if (asPeerFSM != null) {
            asPeerFSM.cancel();
        }

        appServers.remove(as);

        this.store();

        for (FastList.Node<M3UAManagementEventListener> n = this.managementEventListeners.head(), end = this.managementEventListeners
                .tail(); (n = n.getNext()) != end;) {
            M3UAManagementEventListener m3uaManagementEventListener = n.getValue();
            try {
                m3uaManagementEventListener.onAsDestroyed(as);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAsDestroyed", ee);
            }

        }

        return as;
    }

    /**
     * Create new {@link AspFactoryImpl} without passing optional aspid and heartbeat is false
     *
     * @param aspName
     * @param associationName
     * @return
     * @throws Exception
     */
    public AspFactory createAspFactory(String aspName, String associationName) throws Exception {
        return this.createAspFactory(aspName, associationName, false);
    }

    /**
     * Create new {@link AspFactoryImpl} without passing optional aspid
     *
     * @param aspName
     * @param associationName
     * @return
     * @throws Exception
     */
    public AspFactory createAspFactory(String aspName, String associationName, boolean isHeartBeatEnabled) throws Exception {
        long aspid = 0L;
        boolean regenerateFlag = true;

        while (regenerateFlag) {
            aspid = AspFactoryImpl.generateId();
            if (aspfactories.size() == 0) {
                // Special case where this is first Asp added
                break;
            }

            for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
                AspFactoryImpl aspFactoryImpl = (AspFactoryImpl) n.getValue();
                if (aspid == aspFactoryImpl.getAspid().getAspId()) {
                    regenerateFlag = true;
                    break;
                } else {
                    regenerateFlag = false;
                }
            }// for
        }// while

        return this.createAspFactory(aspName, associationName, aspid, isHeartBeatEnabled);
    }

    /**
     * <p>
     * Create new {@link AspFactoryImpl}
     * </p>
     * <p>
     * Command is m3ua asp create <asp-name> <sctp-association> aspid <aspid> heartbeat <true|false>
     * </p>
     * <p>
     * asp-name and sctp-association is mandatory where as aspid is optional. If aspid is not passed, next available aspid wil
     * be used
     * </p>
     *
     * @param aspName
     * @param associationName
     * @param aspid
     * @return
     * @throws Exception
     */
    public AspFactory createAspFactory(String aspName, String associationName, long aspid, boolean isHeartBeatEnabled)
            throws Exception {
        AspFactoryImpl factory = this.getAspFactory(aspName);

        if (factory != null) {
            throw new Exception(String.format(M3UAOAMMessages.CREATE_ASP_FAIL_NAME_EXIST, aspName));
        }

        Association association = this.transportManagement.getAssociation(associationName);
        if (association == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_ASSOCIATION_FOUND, associationName));
        }

        if (association.isStarted()) {
            throw new Exception(String.format(M3UAOAMMessages.ASSOCIATION_IS_STARTED, associationName));
        }

        if (association.getAssociationListener() != null) {
            throw new Exception(String.format(M3UAOAMMessages.ASSOCIATION_IS_ASSOCIATED, associationName));
        }

        for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
            AspFactoryImpl aspFactoryImpl = (AspFactoryImpl) n.getValue();
            if (aspid == aspFactoryImpl.getAspid().getAspId()) {
                throw new Exception(String.format(M3UAOAMMessages.ASP_ID_TAKEN, aspid));
            }
        }

        factory = new AspFactoryImpl(aspName, this.getMaxSequenceNumber(), aspid, isHeartBeatEnabled);
        factory.setM3UAManagement(this);
        factory.setAssociation(association);
        factory.setTransportManagement(this.transportManagement);

        aspfactories.add(factory);

        this.store();

        for (FastList.Node<M3UAManagementEventListener> n = this.managementEventListeners.head(), end = this.managementEventListeners
                .tail(); (n = n.getNext()) != end;) {
            M3UAManagementEventListener m3uaManagementEventListener = n.getValue();
            try {
                m3uaManagementEventListener.onAspFactoryCreated(factory);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAspFactoryCreated", ee);
            }
        }

        return factory;
    }

    public AspFactoryImpl destroyAspFactory(String aspName) throws Exception {
        AspFactoryImpl aspFactroy = this.getAspFactory(aspName);
        if (aspFactroy == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_ASP_FOUND, aspName));
        }

        if (aspFactroy.aspList.size() != 0) {
            throw new Exception("Asp are still assigned to As. Unassign all");
        }
        aspFactroy.unsetAssociation();
        this.aspfactories.remove(aspFactroy);
        this.store();

        for (FastList.Node<M3UAManagementEventListener> n = this.managementEventListeners.head(), end = this.managementEventListeners
                .tail(); (n = n.getNext()) != end;) {
            M3UAManagementEventListener m3uaManagementEventListener = n.getValue();
            try {
                m3uaManagementEventListener.onAspFactoryDestroyed(aspFactroy);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAspFactoryDestroyed", ee);
            }
        }

        return aspFactroy;
    }

    /**
     * Associate {@link AspImpl} to {@link AsImpl}
     *
     * @param asName
     * @param aspName
     * @return
     * @throws Exception
     */
    public AspImpl assignAspToAs(String asName, String aspName) throws Exception {
        // check ASP and AS exist with given name
        AsImpl asImpl = (AsImpl) this.getAs(asName);

        if (asImpl == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_AS_FOUND, asName));
        }

        AspFactoryImpl aspFactroy = this.getAspFactory(aspName);

        if (aspFactroy == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_ASP_FOUND, aspName));
        }

        // If ASP already assigned to AS we don't want to re-assign
        for (FastList.Node<Asp> n = asImpl.appServerProcs.head(), end = asImpl.appServerProcs.tail(); (n = n.getNext()) != end;) {
            AspImpl aspImpl = (AspImpl) n.getValue();
            if (aspImpl.getName().equals(aspName)) {
                throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_THIS_AS, aspName,
                        asName));
            }
        }

        FastList<Asp> aspImpls = aspFactroy.aspList;

        // Checks for RoutingContext. We know that for null RC there will always
        // be a single ASP assigned to AS and ASP cannot be shared
        if (asImpl.getRoutingContext() == null) {
            // If AS has Null RC, this should be the first assignment of ASP to
            // AS
            if (aspImpls.size() != 0) {
                throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS, aspName,
                        asName));
            }
        } else if (aspImpls.size() > 0) {
            // RoutingContext is not null, make sure there is no ASP that is
            // assigned to AS with null RC
            Asp asp = aspImpls.get(0);
            if (asp != null && asp.getAs().getRoutingContext() == null) {
                throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS_WITH_NULL_RC,
                        aspName, asName));
            }
        }

        if (aspFactroy.getFunctionality() != null && aspFactroy.getFunctionality() != asImpl.getFunctionality()) {
            throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS_TYPE, aspName,
                    asName, aspFactroy.getFunctionality()));
        }

        if (aspFactroy.getExchangeType() != null && aspFactroy.getExchangeType() != asImpl.getExchangeType()) {
            throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS_EXCHANGETYPE,
                    aspName, asName, aspFactroy.getExchangeType()));
        }

        if (aspFactroy.getIpspType() != null && aspFactroy.getIpspType() != asImpl.getIpspType()) {
            throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_IPSP_TYPE, aspName,
                    asName, aspFactroy.getIpspType()));
        }

        aspFactroy.setExchangeType(asImpl.getExchangeType());
        aspFactroy.setFunctionality(asImpl.getFunctionality());
        aspFactroy.setIpspType(asImpl.getIpspType());

        AspImpl aspImpl = aspFactroy.createAsp();
        FSM aspLocalFSM = aspImpl.getLocalFSM();
        m3uaScheduler.execute(aspLocalFSM);

        FSM aspPeerFSM = aspImpl.getPeerFSM();
        m3uaScheduler.execute(aspPeerFSM);
        asImpl.addAppServerProcess(aspImpl);

        this.store();

        for (FastList.Node<M3UAManagementEventListener> n = this.managementEventListeners.head(), end = this.managementEventListeners
                .tail(); (n = n.getNext()) != end;) {
            M3UAManagementEventListener m3uaManagementEventListener = n.getValue();
            try {
                m3uaManagementEventListener.onAspAssignedToAs(asImpl, aspImpl);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAspAssignedToAs", ee);
            }
        }

        return aspImpl;
    }

    public Asp unassignAspFromAs(String asName, String aspName) throws Exception {
        // check ASP and AS exist with given name
        AsImpl asImpl = (AsImpl) this.getAs(asName);

        if (asImpl == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_AS_FOUND, asName));
        }

        AspImpl aspImpl = asImpl.removeAppServerProcess(aspName);
        aspImpl.getAspFactory().destroyAsp(aspImpl);
        this.store();

        for (FastList.Node<M3UAManagementEventListener> n = this.managementEventListeners.head(), end = this.managementEventListeners
                .tail(); (n = n.getNext()) != end;) {
            M3UAManagementEventListener m3uaManagementEventListener = n.getValue();
            try {
                m3uaManagementEventListener.onAspUnassignedFromAs(asImpl, aspImpl);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAspUnassignedFromAs", ee);
            }
        }

        return aspImpl;
    }

    /**
     * This method should be called by management to start the ASP
     *
     * @param aspName The name of the ASP to be started
     * @throws Exception
     */
    public void startAsp(String aspName) throws Exception {
        AspFactoryImpl aspFactoryImpl = this.getAspFactory(aspName);

        if (aspFactoryImpl == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_ASP_FOUND, aspName));
        }

        if (aspFactoryImpl.getStatus()) {
            throw new Exception(String.format(M3UAOAMMessages.ASP_ALREADY_STARTED, aspName));
        }

        if (aspFactoryImpl.aspList.size() == 0) {
            throw new Exception(String.format(M3UAOAMMessages.ASP_NOT_ASSIGNED_TO_AS, aspName));
        }

        aspFactoryImpl.start();
        this.store();

        for (FastList.Node<M3UAManagementEventListener> n = this.managementEventListeners.head(), end = this.managementEventListeners
                .tail(); (n = n.getNext()) != end;) {
            M3UAManagementEventListener m3uaManagementEventListener = n.getValue();
            try {
                m3uaManagementEventListener.onAspFactoryStarted(aspFactoryImpl);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAspFactoryStarted", ee);
            }

        }
    }

    /**
     * This method should be called by management to stop the ASP
     *
     * @param aspName The name of the ASP to be stopped
     * @throws Exception
     */
    public void stopAsp(String aspName) throws Exception {

        this.doStopAsp(aspName, true);
    }

    private void doStopAsp(String aspName, boolean needStore) throws Exception {
        AspFactoryImpl aspFactoryImpl = this.getAspFactory(aspName);

        if (aspFactoryImpl == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_ASP_FOUND, aspName));
        }

        if (!aspFactoryImpl.getStatus()) {
            throw new Exception(String.format(M3UAOAMMessages.ASP_ALREADY_STOPPED, aspName));
        }

        aspFactoryImpl.stop();

        if (needStore)
            this.store();

        // TODO : Should calling
        // m3uaManagementEventListener.onAspFactoryStopped() be before actual
        // stop of aspFactory? The problem is ASP_DOWN and AS_INACTIV callbacks
        // are before AspFactoryStopped. Is it ok?
        for (FastList.Node<M3UAManagementEventListener> n = this.managementEventListeners.head(), end = this.managementEventListeners
                .tail(); (n = n.getNext()) != end;) {
            M3UAManagementEventListener m3uaManagementEventListener = n.getValue();
            try {
                m3uaManagementEventListener.onAspFactoryStopped(aspFactoryImpl);
            } catch (Throwable ee) {
                logger.error("Exception while invoking onAspFactoryStopped", ee);
            }
        }
    }

    public void addRoute(int dpc, int opc, int si, String asName) throws Exception {
        this.routeManagement.addRoute(dpc, opc, si, asName, TrafficModeType.Loadshare);
    }

    public void addRoute(int dpc, int opc, int si, String asName, int trafficModeType) throws Exception {
        this.routeManagement.addRoute(dpc, opc, si, asName, trafficModeType);
    }

    public void removeRoute(int dpc, int opc, int si, String asName) throws Exception {
        this.routeManagement.removeRoute(dpc, opc, si, asName);
    }

    public void removeAllResourses() throws Exception {

        if (!this.isStarted) {
            throw new Exception(String.format("Management=%s not started", this.name));
        }

        if (this.appServers.size() == 0 && this.aspfactories.size() == 0)
            // no resources allocated - nothing to do
            return;

        if (logger.isInfoEnabled()) {
            logger.info(String.format("Removing allocated resources: AppServers=%d, AspFactories=%d", this.appServers.size(),
                    this.aspfactories.size()));
        }

        this.stopFactories();

        // Remove routes
        this.routeManagement.removeAllResourses();

        // Unassign asp from as
        FastMap<String, FastList<String>> lstAsAsp = new FastMap<String, FastList<String>>();

        for (As as : this.appServers) {
            AsImpl asImpl = (AsImpl) as;
            FastList<String> lstAsps = new FastList<String>();

            for (FastList.Node<Asp> n = asImpl.appServerProcs.head(), end = asImpl.appServerProcs.tail(); (n = n.getNext()) != end;) {
                AspImpl aspImpl = (AspImpl) n.getValue();
                lstAsps.add(aspImpl.getName());
            }
            lstAsAsp.put(asImpl.getName(), lstAsps);
        }

        for (FastMap.Entry<String, FastList<String>> e = lstAsAsp.head(), end = lstAsAsp.tail(); (e = e.getNext()) != end;) {
            String asName = e.getKey();
            FastList<String> lstAsps = e.getValue();

            for (FastList.Node<String> n = lstAsps.head(), end1 = lstAsps.tail(); (n = n.getNext()) != end1;) {
                String aspName = n.getValue();
                this.unassignAspFromAs(asName, aspName);
            }

        }

        // Remove all AspFactories
        ArrayList<AspFactory> lstAspFactory = new ArrayList<AspFactory>();
        for (AspFactory aspFact : this.aspfactories) {
            lstAspFactory.add(aspFact);
        }
        for (AspFactory aspFact : lstAspFactory) {
            this.destroyAspFactory(aspFact.getName());
        }

        // Remove all AppServers
        ArrayList<String> lst = new ArrayList<String>();
        for (As asImpl : this.appServers) {
            lst.add(asImpl.getName());
        }
        for (String n : lst) {
            this.destroyAs(n);
        }

        // We store the cleared state
        this.store();

        for (FastList.Node<M3UAManagementEventListener> n = this.managementEventListeners.head(), end = this.managementEventListeners
                .tail(); (n = n.getNext()) != end;) {
            M3UAManagementEventListener m3uaManagementEventListener = n.getValue();
            try {
                m3uaManagementEventListener.onRemoveAllResources();
            } catch (Throwable ee) {
                logger.error("Exception while invoking onRemoveAllResources", ee);
            }
        }
    }

    private void stopFactories() throws Exception {
        // Stopping asp factories
        boolean someFactoriesIsStopped = false;
        for (AspFactory aspFact : this.aspfactories) {
            AspFactoryImpl aspFactImpl = (AspFactoryImpl) aspFact;
            if (aspFactImpl.started) {
                someFactoriesIsStopped = true;
                this.doStopAsp(aspFact.getName(), false);
            }
        }
        // waiting 5 seconds till stopping factories
        if (someFactoriesIsStopped) {
            for (int step = 1; step < 50; step++) {
                boolean allStopped = true;
                for (AspFactory aspFact : this.aspfactories) {
                    if (aspFact.getAssociation() != null && aspFact.getAssociation().isConnected()) {
                        allStopped = false;
                        break;
                    }
                }
                if (allStopped)
                    break;

                Thread.sleep(100);
            }
        }
    }

    public void sendTransferMessageToLocalUser(Mtp3TransferPrimitive msg, int seqControl) {
        super.sendTransferMessageToLocalUser(msg, seqControl);
    }

    public void sendPauseMessageToLocalUser(Mtp3PausePrimitive msg) {
        super.sendPauseMessageToLocalUser(msg);
    }

    public void sendResumeMessageToLocalUser(Mtp3ResumePrimitive msg) {
        super.sendResumeMessageToLocalUser(msg);
    }

    public void sendStatusMessageToLocalUser(Mtp3StatusPrimitive msg) {
        super.sendStatusMessageToLocalUser(msg);
    }

    public void sendEndCongestionMessageToLocalUser(Mtp3EndCongestionPrimitive msg) {
        super.sendEndCongestionMessageToLocalUser(msg);
    }

    private AspFactoryImpl getAspFactory(String aspName) {
        for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
            AspFactoryImpl aspFactoryImpl = (AspFactoryImpl) n.getValue();
            if (aspFactoryImpl.getName().equals(aspName)) {
                return aspFactoryImpl;
            }
        }
        return null;
    }

    /**
     * Persist
     */
    public void store() {

        // TODO : Should we keep reference to Objects rather than recreating
        // everytime?
        try {
            this.preparePersistFile();
            XMLObjectWriter writer = XMLObjectWriter.newInstance(new FileOutputStream(persistFile.toString()));
            writer.setBinding(binding);
            // Enables cross-references.
            // writer.setReferenceResolver(new XMLReferenceResolver());
            writer.setIndentation(TAB_INDENT);

            writer.write(this.timeBetweenHeartbeat, HEART_BEAT_TIME_PROP, Integer.class);
            writer.write(this.isUseLsbForLinksetSelection(), USE_LSB_FOR_LINKSET_SELECTION, Boolean.class);

            writer.write(aspfactories, ASP_FACTORY_LIST, FastList.class);
            writer.write(appServers, AS_LIST, FastList.class);
            writer.write(this.routeManagement.route, DPC_VS_AS_LIST, RouteMap.class);

            writer.close();
        } catch (Exception e) {
            logger.error("Error while persisting the Rule state in file", e);
        }
    }

    /**
     * Load and create LinkSets and Link from persisted file
     *
     * @throws Exception
     */
    public void load() throws FileNotFoundException {

        XMLObjectReader reader = null;
        try {
            this.preparePersistFile();

            File f = new File(persistFile.toString());
            if (f.exists()) {
                // we have V2 config
                loadVer2(persistFile.toString());
            } else {
                String s1 = persistFile.toString().replace("1.xml", ".xml");
                f = new File(s1);

                if (f.exists()) {
                    loadVer1(s1);
                }

                this.store();
                f.delete();
            }
        } catch (XMLStreamException ex) {
            logger.error(String.format("Failed to load the M3UA configuration file. \n%s", ex.getMessage()));
        } catch (FileNotFoundException e) {
            logger.warn(String.format("Failed to load the M3UA configuration file. \n%s", e.getMessage()));
        } catch (IOException e) {
            logger.error(String.format("Failed to load the M3UA configuration file. \n%s", e.getMessage()));
        }
    }

    private void loadActualData(XMLObjectReader reader ) throws XMLStreamException, IOException{
        try {
            Integer vali = reader.read(MAX_SEQUENCE_NUMBER_PROP, Integer.class);
            vali = reader.read(MAX_AS_FOR_ROUTE_PROP, Integer.class);

            this.timeBetweenHeartbeat = reader.read(HEART_BEAT_TIME_PROP, Integer.class);
        } catch (java.lang.Exception e) {
            // ignore.
            // For backward compatibility we can ignore if these values are not defined
            logger.error("Errro while reading attribute", e);
        }

        String vals = reader.read(ROUTING_LABEL_FORMAT, String.class); // we do not store it - for backup compatibility
        Boolean valb = reader.read(USE_LSB_FOR_LINKSET_SELECTION, Boolean.class);
        if (valb != null) {
            try {
                super.setUseLsbForLinksetSelection(valb);
            } catch (Exception e) {
            }
        }

        aspfactories = reader.read(ASP_FACTORY_LIST, FastList.class);
        appServers = reader.read(AS_LIST, FastList.class);
        this.routeManagement.route = reader.read(DPC_VS_AS_LIST, RouteMap.class);

        this.routeManagement.reset();

        // Create Asp's and assign to each of the AS. Schedule the FSM's
        for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
            AsImpl asImpl = (AsImpl) n.getValue();
            asImpl.setM3UAManagement(this);
            FSM asLocalFSM = asImpl.getLocalFSM();
            m3uaScheduler.execute(asLocalFSM);

            FSM asPeerFSM = asImpl.getPeerFSM();
            m3uaScheduler.execute(asPeerFSM);

            // All the Asp's for this As added in temp list
            FastList<Asp> tempAsp = new FastList<Asp>();
            tempAsp.addAll(asImpl.appServerProcs);

            // Claer Asp's from this As
            asImpl.appServerProcs.clear();

            for (FastList.Node<Asp> n1 = tempAsp.head(), end1 = tempAsp.tail(); (n1 = n1.getNext()) != end1;) {
                AspImpl aspImpl = (AspImpl) n1.getValue();

                try {
                    // Now let the Asp's be created from respective
                    // AspFactory and added to As
                    this.assignAspToAs(asImpl.getName(), aspImpl.getName());
                } catch (Exception e) {
                    logger.error("Error while assigning Asp to As on loading from xml file", e);
                }
            }
        }

        // Set the transportManagement
        for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
            AspFactoryImpl factory = (AspFactoryImpl) n.getValue();
            factory.setTransportManagement(this.transportManagement);
            factory.setM3UAManagement(this);
            try {
                factory.setAssociation(this.transportManagement.getAssociation(factory.associationName));
            } catch (Throwable e1) {
                logger.error(String.format("Error setting Assciation=%s for the AspFactory=%s while loading from XML",
                        factory.associationName, factory.getName()), e1);
            }

            if (factory.getStatus()) {
                try {
                    factory.start();
                } catch (Exception e) {
                    logger.error(String.format("Error starting the AspFactory=%s while loading from XML", factory.getName()), e);
                }
            }
        }
    }

    private void loadVer1(String fn) throws XMLStreamException, IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fn)));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String s1 = br.readLine();
            if (s1 == null)
                break;
            sb.append(s1);
            sb.append("\n");
        }
        br.close();
        String s2 = sb.toString();
        s2 = s2.replace("<value value=", "<routeAs trafficModeType=\"2\"  as=");
        logger.error("new XML \n"+s2.toString());
        StringReader sr = new StringReader(s2);
        XMLObjectReader reader = XMLObjectReader.newInstance(sr);
        reader.setBinding(binding);
        this.loadActualData(reader);
        reader.close();
    }

    private void loadVer2(String fn) throws XMLStreamException, IOException{
        XMLObjectReader reader = XMLObjectReader.newInstance(new FileInputStream(fn));
        reader.setBinding(binding);
        this.loadActualData(reader);
        reader.close();
    }

    @Override
    public void sendMessage(Mtp3TransferPrimitive mtp3TransferPrimitive) throws IOException {
        ProtocolData data = this.parameterFactory.createProtocolData(mtp3TransferPrimitive.getOpc(),
                mtp3TransferPrimitive.getDpc(), mtp3TransferPrimitive.getSi(), mtp3TransferPrimitive.getNi(),
                mtp3TransferPrimitive.getMp(), mtp3TransferPrimitive.getSls(), mtp3TransferPrimitive.getData());

        PayloadData payload = (PayloadData) messageFactory.createMessage(MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD);
        payload.setData(data);

        AsImpl asImpl = this.routeManagement.getAsForRoute(data.getDpc(), data.getOpc(), data.getSI(), data.getSLS());
        if (asImpl == null) {
            logger.error(String.format("Tx : No AS found for routing message %s", payload));
            return;
        }
        payload.setNetworkAppearance(asImpl.getNetworkAppearance());
        payload.setRoutingContext(asImpl.getRoutingContext());
        asImpl.write(payload);
    }
}
