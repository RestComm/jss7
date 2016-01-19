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
package org.mobicents.protocols.ss7.oam.common.m3ua;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javolution.util.FastList;

import org.mobicents.protocols.ss7.m3ua.As;
import org.mobicents.protocols.ss7.m3ua.Asp;
import org.mobicents.protocols.ss7.m3ua.AspFactory;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.M3UAManagement;
import org.mobicents.protocols.ss7.m3ua.M3UAManagementEventListener;
import org.mobicents.protocols.ss7.m3ua.RouteAs;
import org.mobicents.protocols.ss7.m3ua.State;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmListener;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmListenerCollection;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmMediator;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmMessage;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmMessageImpl;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmSeverity;
import org.mobicents.protocols.ss7.oam.common.alarm.CurrentAlarmList;
import org.mobicents.protocols.ss7.oam.common.alarm.CurrentAlarmListImpl;
import org.mobicents.protocols.ss7.oam.common.jmx.MBeanHost;
import org.mobicents.protocols.ss7.oam.common.jmxss7.Ss7Layer;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class M3uaManagementJmx implements M3uaManagementJmxMBean, M3UAManagementEventListener, AlarmMediator {

    private final MBeanHost ss7Management;
    private final M3UAManagement wrappedM3UAManagement;

    protected FastList<As> appServers = new FastList<As>();
    protected FastList<AspFactory> aspfactories = new FastList<AspFactory>();
    private AlarmListenerCollection alc = new AlarmListenerCollection();

    private static final ParameterFactory parameterFactory = new ParameterFactoryImpl();


    public M3uaManagementJmx(MBeanHost ss7Management, M3UAManagement wrappedM3UAManagement) {
        this.ss7Management = ss7Management;
        this.wrappedM3UAManagement = wrappedM3UAManagement;
    }

    /**
     * methods - bean life-cycle
     */

    public void start() {

        synchronized (this.appServers) {
            appServers.clear();
        }

        synchronized (this.aspfactories) {
            aspfactories.clear();
        }

        this.ss7Management.registerMBean(Ss7Layer.M3UA, M3UAManagementType.MANAGEMENT, this.getName(), this);
        this.wrappedM3UAManagement.addM3UAManagementEventListener(this);

        List<AspFactory> aspFactories = this.wrappedM3UAManagement.getAspfactories();
        for (AspFactory aspFactory : aspFactories) {
            this.addAspFactoryToManagement(aspFactory);
        }

        List<As> appServers = this.wrappedM3UAManagement.getAppServers();
        for (As as : appServers) {
            this.addAsToManagement(as);

            List<Asp> asps = as.getAspList();

            for (Asp asp : asps) {
                this.onAspAssignedToAs(as, asp);
            }
        }
    }

    public void stop() {
        this.removeAllResourcesFromManagement();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean#getName ()
     */
    @Override
    public String getName() {
        return this.wrappedM3UAManagement.getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean# getPersistDir()
     */
    @Override
    public String getPersistDir() {
        return this.wrappedM3UAManagement.getPersistDir();
    }

    @Override
    public void setPersistDir(String persistDir) {
        this.wrappedM3UAManagement.setPersistDir(persistDir);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean# getMaxSequenceNumber()
     */
    @Override
    public int getMaxSequenceNumber() {
        return this.wrappedM3UAManagement.getMaxSequenceNumber();
    }

    @Override
    public void setMaxSequenceNumber(int maxSequenceNumber) throws Exception {
        this.wrappedM3UAManagement.setMaxSequenceNumber(maxSequenceNumber);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean# getMaxAsForRoute()
     */
    @Override
    public int getMaxAsForRoute() {
        return this.wrappedM3UAManagement.getMaxAsForRoute();
    }

    @Override
    public void setMaxAsForRoute(int maxAsForRoute) throws Exception {
        this.wrappedM3UAManagement.setMaxAsForRoute(maxAsForRoute);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean# getAppServers()
     */
    @Override
    public List<As> getAppServers() {
        return this.appServers.unmodifiable();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean# getAspfactories()
     */
    @Override
    public List<AspFactory> getAspfactories() {
        return this.aspfactories.unmodifiable();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean#createAs (java.lang.String,
     * org.mobicents.protocols.ss7.m3ua.Functionality, org.mobicents.protocols.ss7.m3ua.ExchangeType,
     * org.mobicents.protocols.ss7.m3ua.IPSPType, org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext,
     * org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType, org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance)
     */
    @Override
    public As createAs(String asName, Functionality functionality, ExchangeType exchangeType, IPSPType ipspType,
            RoutingContext rc, TrafficModeType trafficMode, int minAspActive, NetworkAppearance na) throws Exception {
        As as = this.wrappedM3UAManagement.createAs(asName, functionality, exchangeType, ipspType, rc, trafficMode,
                minAspActive, na);
        return null;
    }

    @Override
    public As createAppServer(String asName, Functionality functionality, ExchangeType exchangeType, IPSPType ipspType,
            String rc, int trafficMode, int minAspActive, String na) throws Exception {

        NetworkAppearance networkAppearance = null;
        if (na != null && !na.trim().equals("")) {
            long naLong = Long.parseLong(na);
            networkAppearance = parameterFactory.createNetworkAppearance(naLong);
        }

        RoutingContext routingContext = null;
        if (rc != null && !rc.trim().equals("")) {
            long rcLong = Long.parseLong(rc.trim());
            routingContext = parameterFactory.createRoutingContext(new long[] { rcLong });
        }

        TrafficModeType trafficModeType = parameterFactory.createTrafficModeType(trafficMode);
        As as = this.wrappedM3UAManagement.createAs(asName, functionality, exchangeType, ipspType, routingContext,
                trafficModeType, minAspActive, networkAppearance);

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean#destroyAs (java.lang.String)
     */
    @Override
    public As destroyAs(String asName) throws Exception {
        this.wrappedM3UAManagement.destroyAs(asName);
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean# createAspFactory(java.lang.String,
     * java.lang.String)
     */
    @Override
    public AspFactory createAspFactory(String aspName, String associationName) throws Exception {
        this.wrappedM3UAManagement.createAspFactory(aspName, associationName);
        return null;
    }

    @Override
    public AspFactory createAspFactory(String aspName, String associationName, boolean isHeartBeatEnabled) throws Exception {
        this.wrappedM3UAManagement.createAspFactory(aspName, associationName, isHeartBeatEnabled);
        return null;
    }

    @Override
    public int getHeartbeatTime() {
        return this.wrappedM3UAManagement.getHeartbeatTime();
    }

    @Override
    public void setHeartbeatTime(int timeBetweenHeartbeat) throws Exception {
        this.wrappedM3UAManagement.setHeartbeatTime(timeBetweenHeartbeat);
    }

    @Override
    public boolean isStarted() {
        return this.wrappedM3UAManagement.isStarted();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean# createAspFactory(java.lang.String,
     * java.lang.String, long)
     */
    @Override
    public AspFactory createAspFactory(String aspName, String associationName, long aspid, boolean isHeartBeatEnabled)
            throws Exception {
        this.wrappedM3UAManagement.createAspFactory(aspName, associationName, aspid, isHeartBeatEnabled);
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean# destroyAspFactory(java.lang.String)
     */
    @Override
    public AspFactory destroyAspFactory(String aspName) throws Exception {
        this.wrappedM3UAManagement.destroyAspFactory(aspName);
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean# assignAspToAs(java.lang.String, java.lang.String)
     */
    @Override
    public Asp assignAspToAs(String asName, String aspName) throws Exception {
        this.wrappedM3UAManagement.assignAspToAs(asName, aspName);
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean# unassignAspFromAs(java.lang.String,
     * java.lang.String)
     */
    @Override
    public Asp unassignAspFromAs(String asName, String aspName) throws Exception {
        this.wrappedM3UAManagement.unassignAspFromAs(asName, aspName);
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean#startAsp (java.lang.String)
     */
    @Override
    public void startAsp(String aspName) throws Exception {
        this.wrappedM3UAManagement.startAsp(aspName);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean#stopAsp (java.lang.String)
     */
    @Override
    public void stopAsp(String aspName) throws Exception {
        this.wrappedM3UAManagement.stopAsp(aspName);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean#addRoute (int, int, int, java.lang.String)
     */
    @Override
    @Deprecated
    public void addRoute(int dpc, int opc, int si, String asName) throws Exception {
        this.wrappedM3UAManagement.addRoute(dpc, opc, si, asName);
    }

    @Override
    public void addRoute(int dpc, int opc, int si, String asName, int trafficModeType) throws Exception {
        this.wrappedM3UAManagement.addRoute(dpc, opc, si, asName, trafficModeType);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean#removeRoute (int, int, int, java.lang.String)
     */
    @Override
    public void removeRoute(int dpc, int opc, int si, String asName) throws Exception {
        this.wrappedM3UAManagement.removeRoute(dpc, opc, si, asName);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmxMBean# removeAllResourses()
     */
    @Override
    public void removeAllResourses() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * M3UAManagementEventListener callbacks
     */

    @Override
    public void onAsCreated(As as) {
        this.addAsToManagement(as);

        // if (wrappedM3UAManagement.isStarted()) {
        if (!(as.getState().getName().equals(State.STATE_ACTIVE))) {
            AlarmMessage alm = this.generateAsAlarm(as, false);
            this.alc.onAlarm(alm);
        }
        // }
    }

    @Override
    public void onAsDestroyed(As as) {
        this.removeAsFromManagement(as);

        // if (wrappedM3UAManagement.isStarted()) {
        if (!(as.getState().getName().equals(State.STATE_ACTIVE))) {
            AlarmMessage alm = this.generateAsAlarm(as, true);
            this.alc.onAlarm(alm);
        }
        // }
    }

    @Override
    public void onAsDown(As as, State oldState) {
        // if (wrappedM3UAManagement.isStarted()) {
        AlarmMessage alm = this.generateAsAlarm(as, false);
        this.alc.onAlarm(alm);
        // }
    }

    @Override
    public void onAsInactive(As as, State oldState) {
        // if (wrappedM3UAManagement.isStarted()) {
        AlarmMessage alm = this.generateAsAlarm(as, false);
        this.alc.onAlarm(alm);
        // }
    }

    @Override
    public void onAsActive(As as, State oldState) {
        // if (wrappedM3UAManagement.isStarted()) {
        AlarmMessage alm = this.generateAsAlarm(as, true);
        this.alc.onAlarm(alm);
        // }
    }

    @Override
    public void onAsPending(As as, State oldState) {
        // if (wrappedM3UAManagement.isStarted()) {
        AlarmMessage alm = this.generateAsAlarm(as, false);
        this.alc.onAlarm(alm);
        // }
    }

    @Override
    public void onAspInactive(Asp asp, State oldState) {
        // if (wrappedM3UAManagement.isStarted()) {
        // AspFactory aspFact = asp.getAspFactory();
        // if (aspFact.getStatus()) {
        AlarmMessage alm = this.generateAspAlarm(asp, false);
        this.alc.onAlarm(alm);
        // }
        // }
    }

    @Override
    public void onAspActive(Asp asp, State oldState) {
        // if (wrappedM3UAManagement.isStarted()) {
        // AspFactory aspFact = asp.getAspFactory();
        // if (aspFact.getStatus()) {
        AlarmMessage alm = this.generateAspAlarm(asp, true);
        this.alc.onAlarm(alm);
        // }
        // }
    }

    @Override
    public void onAspDown(Asp asp, State oldState) {
        // if (wrappedM3UAManagement.isStarted()) {
        // AspFactory aspFact = asp.getAspFactory();
        // if (aspFact.getStatus()) {
        AlarmMessage alm = this.generateAspAlarm(asp, false);
        this.alc.onAlarm(alm);
        // }
        // }
    }

    // @Override
    public void onStarted() {
        CurrentAlarmListImpl all = this.checkAllAlarms(false);
        for (AlarmMessage alm : all.getCurrentAlarmList()) {
            this.alc.onAlarm(alm);
        }

        AlarmMessage alm2 = generateStoppedAlarm(true);
        this.alc.onAlarm(alm2);
    }

    // @Override
    public void onStopped() {
        CurrentAlarmListImpl all = this.checkAllAlarms(true);
        for (AlarmMessage alm : all.getCurrentAlarmList()) {
            this.alc.onAlarm(alm);
        }

        AlarmMessage alm2 = generateStoppedAlarm(false);
        this.alc.onAlarm(alm2);
    }

    @Override
    public void onAspFactoryCreated(AspFactory aspFactory) {
        this.addAspFactoryToManagement(aspFactory);
    }

    @Override
    public void onAspFactoryDestroyed(AspFactory aspFactory) {
        this.removeAspFactoryFromManagement(aspFactory);
    }

    @Override
    public void onAspFactoryStarted(AspFactory aspFact) {
        // if (wrappedM3UAManagement.isStarted()) {
        List<Asp> lstAsp = aspFact.getAspList();
        for (Asp asp : lstAsp) {
            if (!asp.getState().getName().equals(State.STATE_ACTIVE)) {
                AlarmMessage alm = this.generateAspAlarm(asp, false);
                this.alc.onAlarm(alm);
            }
        }
        // }
    }

    @Override
    public void onAspFactoryStopped(AspFactory aspFact) {
        // if (wrappedM3UAManagement.isStarted()) {
        List<Asp> lstAsp = aspFact.getAspList();
        for (Asp asp : lstAsp) {
            if (!asp.getState().getName().equals(State.STATE_ACTIVE)) {
                AlarmMessage alm = this.generateAspAlarm(asp, true);
                this.alc.onAlarm(alm);
            }
        }
        // }
    }

    @Override
    public void onAspAssignedToAs(As as, Asp asp) {
        AsJmx asJmx = null;
        AspFactoryJmx aspFactoryJmx = null;

        synchronized (this.appServers) {
            for (FastList.Node<As> n = this.appServers.head(), end = this.appServers.tail(); (n = n.getNext()) != end;) {
                As asTemp = n.getValue();
                if (asTemp.getName().equals(as.getName())) {
                    asJmx = (AsJmx) asTemp;
                    break;
                }
            }// for loop
        }

        synchronized (this.aspfactories) {
            for (FastList.Node<AspFactory> n = this.aspfactories.head(), end = this.aspfactories.tail(); (n = n.getNext()) != end;) {
                AspFactory aspFactoryJmxTmp = n.getValue();
                if (aspFactoryJmxTmp.getName().equals(asp.getName())) {
                    aspFactoryJmx = (AspFactoryJmx) aspFactoryJmxTmp;
                    break;
                }
            }// for loop
        }

        this.addAspToManagement(asJmx, aspFactoryJmx, asp);
    }

    @Override
    public void onAspUnassignedFromAs(As as, Asp asp) {
        AsJmx asJmx = null;
        AspFactoryJmx aspFactoryJmx = null;

        synchronized (this.appServers) {
            for (FastList.Node<As> n = this.appServers.head(), end = this.appServers.tail(); (n = n.getNext()) != end;) {
                As asTemp = n.getValue();
                if (asTemp.getName().equals(as.getName())) {
                    asJmx = (AsJmx) asTemp;
                    break;
                }
            }// for loop
        }

        synchronized (this.aspfactories) {
            for (FastList.Node<AspFactory> n = this.aspfactories.head(), end = this.aspfactories.tail(); (n = n.getNext()) != end;) {
                AspFactory aspFactoryJmxTmp = n.getValue();
                if (aspFactoryJmxTmp.getName().equals(asp.getName())) {
                    aspFactoryJmx = (AspFactoryJmx) aspFactoryJmxTmp;
                    break;
                }
            }// for loop
        }

        this.removeAspFromManagement(asJmx, aspFactoryJmx, asp);
    }

    @Override
    public void onRemoveAllResources() {
        this.removeAllResourcesFromManagement();
    }

    /**
     * methods - private
     */
    private void addAsToManagement(As as) {
        synchronized (this.appServers) {
            AsJmx asJmx = new AsJmx(as);
            this.ss7Management.registerMBean(Ss7Layer.M3UA, M3UAManagementType.AS, as.getName(), asJmx);
            this.appServers.add(asJmx);
        }
    }

    private void removeAsFromManagement(As as) {
        synchronized (this.appServers) {
            As asJmx = null;
            for (FastList.Node<As> n = this.appServers.head(), end = this.appServers.tail(); (n = n.getNext()) != end;) {
                As asTemp = n.getValue();
                if (asTemp.getName().equals(as.getName())) {
                    asJmx = asTemp;
                    break;
                }
            }// for

            this.appServers.remove(asJmx);

            this.ss7Management.unregisterMBean(Ss7Layer.M3UA, M3UAManagementType.AS, as.getName());
        }
    }

    private void addAspFactoryToManagement(AspFactory aspFactory) {
        synchronized (this.aspfactories) {
            AspFactoryJmx aspFactoryJmx = new AspFactoryJmx(aspFactory);
            this.ss7Management.registerMBean(Ss7Layer.M3UA, M3UAManagementType.ASPFACTORY, aspFactory.getName(), aspFactoryJmx);
            this.aspfactories.add(aspFactoryJmx);
        }
    }

    private void removeAspFactoryFromManagement(AspFactory aspFactory) {
        synchronized (this.aspfactories) {
            AspFactory aspFactoryJmx = null;
            for (FastList.Node<AspFactory> n = this.aspfactories.head(), end = this.aspfactories.tail(); (n = n.getNext()) != end;) {
                AspFactory aspFactoryJmxTmp = n.getValue();
                if (aspFactoryJmxTmp.getName().equals(aspFactory.getName())) {
                    aspFactoryJmx = aspFactoryJmxTmp;
                    break;
                }
            }

            this.aspfactories.remove(aspFactoryJmx);

            this.ss7Management.unregisterMBean(Ss7Layer.M3UA, M3UAManagementType.ASPFACTORY, aspFactoryJmx.getName());
        }
    }

    private void addAspToManagement(AsJmx asJmx, AspFactoryJmx aspFactoryJmx, Asp asp) {
        AspJmx aspJmx = new AspJmx(asp, asJmx, aspFactoryJmx);

        asJmx.addAppServerProcess(aspJmx);
        aspFactoryJmx.addAppServerProcess(aspJmx);

        this.ss7Management.registerMBean(Ss7Layer.M3UA, M3UAManagementType.ASP, aspJmx.getName(), aspJmx);
    }

    private void removeAspFromManagement(AsJmx asJmx, AspFactoryJmx aspFactoryJmx, Asp asp) {
        AspJmx aspJmx = asJmx.removeAppServerProcess(asp.getName());
        aspFactoryJmx.removeAppServerProcess(aspJmx);

        this.ss7Management.unregisterMBean(Ss7Layer.M3UA, M3UAManagementType.ASP, asp.getName());
    }

    private void removeAllResourcesFromManagement() {

        List<As> appServers = this.wrappedM3UAManagement.getAppServers();
        for (As as : appServers) {

            List<Asp> asps = as.getAspList();

            for (Asp asp : asps) {
                this.onAspUnassignedFromAs(as, asp);
            }

            this.removeAsFromManagement(as);
        }

        List<AspFactory> aspFactories = this.wrappedM3UAManagement.getAspfactories();
        for (AspFactory aspFactory : aspFactories) {
            this.removeAspFactoryFromManagement(aspFactory);
        }
    }

    @Override
    public String getAlarmProviderObjectPath() {
        return this.alc.getAlarmProviderObjectPath();
    }

    @Override
    public CurrentAlarmList getCurrentAlarmList() {
        CurrentAlarmListImpl all = new CurrentAlarmListImpl();

        // if (wrappedM3UAManagement.isStarted()) {
        if (true) {
            all = this.checkAllAlarms(false);
        } else {
            AlarmMessage alm = generateStoppedAlarm(false);
            all.addAlarm(alm);
        }

        return all;
    }

    @Override
    public void registerAlarmListener(AlarmListener al) {
        this.alc.registerAlarmListener(al);
    }

    @Override
    public void setAlarmProviderObjectPath(String value) {
        this.alc.setAlarmProviderObjectPath(value);
    }

    @Override
    public void unregisterAlarmListener(AlarmListener al) {
        this.alc.unregisterAlarmListener(al);
    }

    private AlarmMessage generateAsAlarm(As ass, boolean isCleared) {
        AlarmMessageImpl alm = new AlarmMessageImpl();

        alm.setIsCleared(isCleared);
        alm.setAlarmSeverity(AlarmSeverity.major);
        alm.setAlarmSource("SS7_M3UA_" + this.getName());
        alm.setObjectName("AS: " + ass.getName());
        alm.setObjectPath("/M3ua:" + this.getName() + "/Ass/As:" + ass.getName());
        alm.setProblemName("AS state is not active");
        alm.setTimeAlarm(Calendar.getInstance());

        return alm;
    }

    private AlarmMessage generateAspAlarm(Asp asp, boolean isCleared) {
        AlarmMessageImpl alm = new AlarmMessageImpl();

        alm.setIsCleared(isCleared);
        alm.setAlarmSeverity(AlarmSeverity.minor);
        alm.setAlarmSource("SS7_M3UA_" + this.getName());
        alm.setObjectName("ASP: " + asp.getName());
        alm.setObjectPath("/M3ua:" + this.getName() + "/Asps/Asp:" + asp.getName());
        alm.setProblemName("ASP state is not active");
        alm.setTimeAlarm(Calendar.getInstance());
        return alm;
    }

    private AlarmMessage generateStoppedAlarm(boolean isCleared) {
        AlarmMessageImpl alm = new AlarmMessageImpl();

        alm.setIsCleared(isCleared);
        alm.setAlarmSeverity(AlarmSeverity.critical);
        alm.setAlarmSource("SS7_M3UA_" + this.getName());
        alm.setObjectName("M3UA");
        alm.setObjectPath("/M3ua:" + this.getName());
        alm.setProblemName("M3UA server is stopped");
        alm.setTimeAlarm(Calendar.getInstance());

        return alm;
    }

    private CurrentAlarmListImpl checkAllAlarms(boolean isCleared) {
        CurrentAlarmListImpl all = new CurrentAlarmListImpl();

        List<AspFactory> lstAspFact = this.wrappedM3UAManagement.getAspfactories();
        for (AspFactory aspFact : lstAspFact) {
            if (aspFact.getStatus()) {
                List<Asp> lstAsp = aspFact.getAspList();
                for (Asp asp : lstAsp) {
                    if (!asp.getState().getName().equals(State.STATE_ACTIVE)) {
                        AlarmMessage alm = this.generateAspAlarm(asp, isCleared);
                        all.addAlarm(alm);
                    }
                }
            }
        }
        List<As> lstAs = this.wrappedM3UAManagement.getAppServers();
        for (As as : lstAs) {
            if (!as.getState().getName().equals(State.STATE_ACTIVE)) {
                AlarmMessage alm = this.generateAsAlarm(as, isCleared);
                all.addAlarm(alm);
            }
        }

        return all;
    }

    @Override
    public void onServiceStarted() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onServiceStopped() {
        // TODO Auto-generated method stub

    }

    @Override
    public void addM3UAManagementEventListener(M3UAManagementEventListener arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeM3UAManagementEventListener(M3UAManagementEventListener arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, RouteAs> getRoute() {
        return this.wrappedM3UAManagement.getRoute();
    }
}
