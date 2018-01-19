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

package org.mobicents.protocols.ss7.oam.common.sctp;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.CongestionListener;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.api.ManagementEventListener;
import org.mobicents.protocols.api.Server;
import org.mobicents.protocols.api.ServerListener;
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
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 *
 */
public class SctpManagementJmx implements SctpManagementJmxMBean, ManagementEventListener, AlarmMediator, CongestionListener {

    private final MBeanHost ss7Management;
    private final Management wrappedSctpManagement;
    private FastList<Server> lstServers = new FastList<Server>();
    private FastMap<String, Association> lstAssociations = new FastMap<String, Association>();
    private AlarmListenerCollection alc = new AlarmListenerCollection();

    public SctpManagementJmx(MBeanHost ss7Management, Management wrappedSctpManagement) {
        super();
        this.ss7Management = ss7Management;
        this.wrappedSctpManagement = wrappedSctpManagement;
    }

    /**
     * SctpManagementJmxMBean Methods
     */

    @Override
    public Association addAssociation(String hostAddress, int hostPort, String peerAddress, int peerPort, String assocName)
            throws Exception {
        this.wrappedSctpManagement.addAssociation(hostAddress, hostPort, peerAddress, peerPort, assocName);
        return null;
    }

    @Override
    public Association addAssociation(String hostAddress, int hostPort, String peerAddress, int peerPort, String assocName,
            IpChannelType ipChannelType, String[] extraHostAddresses) throws Exception {
        this.wrappedSctpManagement.addAssociation(hostAddress, hostPort, peerAddress, peerPort, assocName, ipChannelType,
                extraHostAddresses);
        return null;
    }

    @Override
    public Association addSctpAssociation(String hostAddress, int hostPort, String peerAddress, int peerPort, String assocName,
            String ipChannelType, String extraHostAddresses) throws Exception {
        this.wrappedSctpManagement.addAssociation(hostAddress, hostPort, peerAddress, peerPort, assocName, IpChannelType.valueOf(ipChannelType.toUpperCase()),
                (extraHostAddresses != null && !extraHostAddresses.isEmpty()) ? extraHostAddresses.split(",") : null);
        return null;
    }

    @Override
    public Association modifySctpAssociation(String hostAddress, String hostPort, String peerAddress, String peerPort,
            String assocName, String ipChannelType, String extraHostAddresses) throws Exception {
        String currHostAddress = null;
        if(hostAddress!=null && !hostAddress.isEmpty())
            currHostAddress = hostAddress;
        Integer currHostPort = null;
        if(hostPort!=null && !hostPort.isEmpty())
            currHostPort = Integer.valueOf(hostPort);
        String currPeerAddress = null;
        if(peerAddress!=null && !peerAddress.isEmpty())
            currPeerAddress = peerAddress;
        Integer currPeerPort = null;
        if(peerPort!=null && !peerPort.isEmpty())
            currPeerPort = Integer.valueOf(peerPort);
        IpChannelType currIpChannelType = null;
        if(ipChannelType!=null && !ipChannelType.isEmpty())
            currIpChannelType = IpChannelType.valueOf(ipChannelType.toUpperCase());
        String[] currExtraHostAddresses = null;
        if(extraHostAddresses != null && !extraHostAddresses.isEmpty())
            currExtraHostAddresses = extraHostAddresses.split(",");
        this.wrappedSctpManagement.modifyAssociation(currHostAddress, currHostPort, currPeerAddress, currPeerPort, assocName, currIpChannelType, currExtraHostAddresses);
        return null;
    }

    @Override
    public void addManagementEventListener(ManagementEventListener listener) {
        // throw new OperationNotSupportedException();
        // TODO ?
    }

    @Override
    public Server addServer(String serverName, String hostAddress, int port) throws Exception {
        this.wrappedSctpManagement.addServer(serverName, hostAddress, port);
        return null;
    }

    @Override
    public Server addServer(String serverName, String hostAddress, int port, IpChannelType ipChannelType,
            String[] extraHostAddresses) throws Exception {
        this.wrappedSctpManagement.addServer(serverName, hostAddress, port, ipChannelType, extraHostAddresses);
        return null;
    }

    @Override
    public Server addServer(String serverName, String hostAddress, int port, IpChannelType ipChannelType,
            boolean acceptAnonymousConnections, int maxConcurrentConnectionsCount, String[] extraHostAddresses)
            throws Exception {
        this.wrappedSctpManagement.addServer(serverName, hostAddress, port, ipChannelType, acceptAnonymousConnections,
                maxConcurrentConnectionsCount, extraHostAddresses);
        return null;
    }

//    @Override
//    public Server addSctpServer(String serverName, String hostAddress, int port, IpChannelType ipChannelType,
//            boolean acceptAnonymousConnections, int maxConcurrentConnectionsCount, String extraHostAddresses) throws Exception {
//        this.wrappedSctpManagement.addServer(serverName, hostAddress, port, ipChannelType, acceptAnonymousConnections,
//                maxConcurrentConnectionsCount,
//                (extraHostAddresses != null && !extraHostAddresses.isEmpty()) ? extraHostAddresses.split(",") : null);
//        return null;
//    }
    @Override
    public Server addSctpServer(String serverName, String hostAddress, int port, String ipChannelType,
       boolean acceptAnonymousConnections, int maxConcurrentConnectionsCount, String extraHostAddresses)
       throws Exception {
    this.wrappedSctpManagement.addServer(serverName, hostAddress, port, IpChannelType.valueOf(ipChannelType.toUpperCase()), acceptAnonymousConnections,
                maxConcurrentConnectionsCount,
                (extraHostAddresses != null && !extraHostAddresses.isEmpty()) ? extraHostAddresses.split(",") : null);
        return null;
    }

    @Override
    public Server modifySctpServer(String serverName, String hostAddress, String port, String ipChannelType,
            String acceptAnonymousConnections, String maxConcurrentConnectionsCount, String extraHostAddresses) throws Exception {

        String currHostAddress = null;
        if(hostAddress!=null && !hostAddress.isEmpty())
            currHostAddress = hostAddress;
        Integer currPort = null;
        if(port!=null && !port.isEmpty())
            currPort = Integer.valueOf(port);
        IpChannelType currIpChannelType = null;
        if(ipChannelType!=null && !ipChannelType.isEmpty())
            currIpChannelType = IpChannelType.valueOf(ipChannelType.toUpperCase());
        Boolean currAcceptAnonymousConnections = null;
        if(acceptAnonymousConnections!=null && !acceptAnonymousConnections.isEmpty())
            currAcceptAnonymousConnections = Boolean.valueOf(acceptAnonymousConnections);
        Integer currMaxConcurrentConnectionsCount = null;
        if(maxConcurrentConnectionsCount!=null && !maxConcurrentConnectionsCount.isEmpty())
            currMaxConcurrentConnectionsCount = Integer.valueOf(maxConcurrentConnectionsCount);
        String[] currExtraHostAddresses = null;
        if(extraHostAddresses != null && !extraHostAddresses.isEmpty())
            currExtraHostAddresses = extraHostAddresses.split(",");

        this.wrappedSctpManagement.modifyServer(serverName, currHostAddress, currPort, currIpChannelType, currAcceptAnonymousConnections,
                currMaxConcurrentConnectionsCount, currExtraHostAddresses);
        return null;
    }

    @Override
    public Association addServerAssociation(String peerAddress, int peerPort, String serverName, String assocName)
            throws Exception {
        this.wrappedSctpManagement.addServerAssociation(peerAddress, peerPort, serverName, assocName);
        return null;
    }
    @Override
    public Association addServerAssociation(String peerAddress, int peerPort, String serverName, String assocName,
            IpChannelType ipChannelType) throws Exception {
        this.wrappedSctpManagement.addServerAssociation(peerAddress, peerPort, serverName, assocName, ipChannelType);
        return null;
    }

    @Override
    public Association addSctpServerAssociation(String peerAddress, int peerPort, String serverName, String assocName,
            String ipChannelType) throws Exception {
        this.wrappedSctpManagement.addServerAssociation(peerAddress, peerPort, serverName, assocName, IpChannelType.valueOf(ipChannelType.toUpperCase()));
        return null;
    }

    @Override
    public Association modifySctpServerAssociation(String peerAddress, String peerPort, String serverName, String assocName,
            String ipChannelType) throws Exception {
        String currPeerAddress = null;
        if(peerAddress!=null && !peerAddress.isEmpty())
            currPeerAddress = peerAddress;
        Integer currPeerPort = null;
        if(peerPort!=null && !peerPort.isEmpty())
            currPeerPort = Integer.valueOf(peerPort);
        String currServerName = null;
        if(serverName!=null && !serverName.isEmpty())
            currServerName = serverName;
        IpChannelType currIpChannelType = null;
        if(ipChannelType!=null && !ipChannelType.isEmpty())
            currIpChannelType = IpChannelType.valueOf(ipChannelType.toUpperCase());
        this.wrappedSctpManagement.modifyServerAssociation(assocName, currPeerAddress, currPeerPort, currServerName, currIpChannelType);
        return null;
    }

    @Override
    public Association getAssociation(String assocName) throws Exception {
        for (FastMap.Entry<String, Association> n = this.lstAssociations.head(), end = this.lstAssociations.tail(); (n = n
                .getNext()) != end;) {
            String key = n.getKey();
            if (key.equals(assocName)) {
                return n.getValue();
            }
        }
        return null;
    }

    @Override
    public Map<String, Association> getAssociations() {
        Map<String, Association> lstAssociationsTmp = new HashMap<String, Association>();
        lstAssociationsTmp.putAll(this.lstAssociations);
        return lstAssociationsTmp;
    }

    @Override
    public int getConnectDelay() {
        return this.wrappedSctpManagement.getConnectDelay();
    }

    @Override
    public String getName() {
        return this.wrappedSctpManagement.getName();
    }

    @Override
    public String getPersistDir() {
        return this.wrappedSctpManagement.getPersistDir();
    }

    @Override
    public ServerListener getServerListener() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Server> getServers() {
        return this.lstServers.unmodifiable();
    }

    @Override
    public int getWorkerThreads() {
        return this.wrappedSctpManagement.getWorkerThreads();
    }

    @Override
    public int getBufferSize() {
        return this.wrappedSctpManagement.getBufferSize();
    }

    @Override
    public boolean isSingleThread() {
        return this.wrappedSctpManagement.isSingleThread();
    }

    @Override
    public boolean isStarted() {
        return this.wrappedSctpManagement.isStarted();
    }

    @Override
    public void removeAllResourses() throws Exception {
        this.wrappedSctpManagement.removeAllResourses();
    }

    @Override
    public void removeAssociation(String assocName) throws Exception {
        this.wrappedSctpManagement.removeAssociation(assocName);
    }

    @Override
    public void removeManagementEventListener(ManagementEventListener arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeServer(String serverName) throws Exception {
        this.wrappedSctpManagement.removeServer(serverName);
    }

    @Override
    public void setConnectDelay(int connectDelay) throws Exception {
        this.wrappedSctpManagement.setConnectDelay(connectDelay);
    }

    @Override
    public void setPersistDir(String persistDir) {
        this.wrappedSctpManagement.setPersistDir(persistDir);
    }

    @Override
    public double getCongControl_BackToNormalDelayThreshold_1() {
        return this.wrappedSctpManagement.getCongControl_BackToNormalDelayThreshold_1();
    }

    @Override
    public double getCongControl_BackToNormalDelayThreshold_2() {
        return this.wrappedSctpManagement.getCongControl_BackToNormalDelayThreshold_2();
    }

    @Override
    public double getCongControl_BackToNormalDelayThreshold_3() {
        return this.wrappedSctpManagement.getCongControl_BackToNormalDelayThreshold_3();
    }

    @Override
    public double getCongControl_DelayThreshold_1() {
        return this.wrappedSctpManagement.getCongControl_DelayThreshold_1();
    }

    @Override
    public double getCongControl_DelayThreshold_2() {
        return this.wrappedSctpManagement.getCongControl_DelayThreshold_2();
    }

    @Override
    public double getCongControl_DelayThreshold_3() {
        return this.wrappedSctpManagement.getCongControl_DelayThreshold_3();
    }

    @Override
    public void setCongControl_BackToNormalDelayThreshold_1(double val) throws Exception {
        this.wrappedSctpManagement.setCongControl_BackToNormalDelayThreshold_1(val);
    }

    @Override
    public void setCongControl_BackToNormalDelayThreshold_2(double val) throws Exception {
        this.wrappedSctpManagement.setCongControl_BackToNormalDelayThreshold_2(val);
    }

    @Override
    public void setCongControl_BackToNormalDelayThreshold_3(double val) throws Exception {
        this.wrappedSctpManagement.setCongControl_BackToNormalDelayThreshold_3(val);
    }

    @Override
    public void setCongControl_DelayThreshold_1(double val) throws Exception {
        this.wrappedSctpManagement.setCongControl_DelayThreshold_1(val);
    }

    @Override
    public void setCongControl_DelayThreshold_2(double val) throws Exception {
        this.wrappedSctpManagement.setCongControl_DelayThreshold_2(val);
    }

    @Override
    public void setCongControl_DelayThreshold_3(double val) throws Exception {
        this.wrappedSctpManagement.setCongControl_DelayThreshold_3(val);
    }

    @Override
    public void setServerListener(ServerListener arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSingleThread(boolean singleThread) throws Exception {
        this.wrappedSctpManagement.setSingleThread(singleThread);
    }

    @Override
    public void setWorkerThreads(int workerThreads) throws Exception {
        this.wrappedSctpManagement.setWorkerThreads(workerThreads);
    }

    @Override
    public void setBufferSize(int bufferSize) throws Exception {
        this.wrappedSctpManagement.setBufferSize(bufferSize);
    }

    @Override
    public Boolean getOptionSctpDisableFragments() {
        return this.wrappedSctpManagement.getOptionSctpDisableFragments();
    }

    @Override
    public Integer getOptionSctpFragmentInterleave() {
        return this.wrappedSctpManagement.getOptionSctpFragmentInterleave();
    }

    @Override
    public Boolean getOptionSctpNodelay() {
        return this.wrappedSctpManagement.getOptionSctpNodelay();
    }

    @Override
    public Integer getOptionSoLinger() {
        return this.wrappedSctpManagement.getOptionSoLinger();
    }

    @Override
    public Integer getOptionSoRcvbuf() {
        return this.wrappedSctpManagement.getOptionSoRcvbuf();
    }

    @Override
    public Integer getOptionSoSndbuf() {
        return this.wrappedSctpManagement.getOptionSoSndbuf();
    }

    @Override
    public void setOptionSctpDisableFragments(Boolean val) {
        this.wrappedSctpManagement.setOptionSctpDisableFragments(val);
    }

    @Override
    public void setOptionSctpFragmentInterleave(Integer val) {
        this.wrappedSctpManagement.setOptionSctpFragmentInterleave(val);
    }

    @Override
    public void setOptionSctpNodelay(Boolean val) {
        this.wrappedSctpManagement.setOptionSctpNodelay(val);
    }

    @Override
    public void setOptionSoLinger(Integer val) {
        this.wrappedSctpManagement.setOptionSoLinger(val);
    }

    @Override
    public void setOptionSoRcvbuf(Integer val) {
        this.wrappedSctpManagement.setOptionSoRcvbuf(val);
    }

    @Override
    public void setOptionSoSndbuf(Integer val) {
        this.wrappedSctpManagement.setOptionSoSndbuf(val);
    }

    @Override
    public Integer getOptionSctpInitMaxstreams_MaxInStreams() {
        return this.wrappedSctpManagement.getOptionSctpInitMaxstreams_MaxInStreams();
    }

    @Override
    public Integer getOptionSctpInitMaxstreams_MaxOutStreams() {
        return this.wrappedSctpManagement.getOptionSctpInitMaxstreams_MaxOutStreams();
    }

    @Override
    public void setOptionSctpInitMaxstreams_MaxInStreams(Integer val) {
        this.wrappedSctpManagement.setOptionSctpInitMaxstreams_MaxInStreams(val);
    }

    @Override
    public void setOptionSctpInitMaxstreams_MaxOutStreams(Integer val) {
        this.wrappedSctpManagement.setOptionSctpInitMaxstreams_MaxOutStreams(val);
    }

    @Override
    public void start() throws Exception {
        synchronized (this) {
            lstServers.clear();
            lstAssociations.clear();

            this.ss7Management.registerMBean(Ss7Layer.SCTP, SctpManagementType.MANAGEMENT, this.getName(), this);
            this.wrappedSctpManagement.addManagementEventListener(this);
            this.wrappedSctpManagement.addCongestionListener(this);

            List<Server> lstSrv = wrappedSctpManagement.getServers();
            for (Server srv : lstSrv) {
                this.addServerToManagement(srv);
            }

            Map<String, Association> lstAss = wrappedSctpManagement.getAssociations();
            for (String s : lstAss.keySet()) {
                Association asso = lstAss.get(s);
                this.addAssociationToManagement(asso);
            }

        }
    }

    @Override
    public void startAssociation(String assocName) throws Exception {
        this.wrappedSctpManagement.startAssociation(assocName);
    }

    @Override
    public void startServer(String serverName) throws Exception {
        this.wrappedSctpManagement.startServer(serverName);
    }

    @Override
    public void stop() throws Exception {
        this.removeAllResourcesFromManagement();
    }

    @Override
    public void stopAssociation(String assocName) throws Exception {
        this.wrappedSctpManagement.stopAssociation(assocName);
    }

    @Override
    public void stopServer(String serverName) throws Exception {
        this.wrappedSctpManagement.stopServer(serverName);
    }

    /**
     * ManagementEventListener methods
     */

    @Override
    public void onAssociationAdded(Association asso) {
        this.addAssociationToManagement(asso);
    }

    @Override
    public void onAssociationDown(Association asso) {
        if (asso.isStarted()) {
            AlarmMessage alm = this.generateAssociationAlarm(asso, false, false, "onAssociationDown");
            this.alc.onAlarm(alm);
        }
    }

    @Override
    public void onAssociationRemoved(Association asso) {
        removeAssociationFromManagement(asso);
    }

    @Override
    public void onAssociationStarted(Association ass) {
        if (!ass.isUp()) {
            AlarmMessage alm = this.generateAssociationAlarm(ass, false, false, "onAssociationStarted");
            this.alc.onAlarm(alm);
        }
    }

    @Override
    public void onAssociationStopped(Association ass) {
        if (!ass.isUp()) {
            AlarmMessage alm = this.generateAssociationAlarm(ass, true, false, "onAssociationStopped");
            this.alc.onAlarm(alm);
        }
    }

    @Override
    public void onAssociationUp(Association ass) {
        if (ass.isStarted()) {
            AlarmMessage alm = this.generateAssociationAlarm(ass, true, false, "onAssociationUp");
            this.alc.onAlarm(alm);
        }
    }

    @Override
    public void onRemoveAllResources() {
        this.removeAllResourcesFromManagement();
    }

    @Override
    public void onServerAdded(Server srv) {
        this.addServerToManagement(srv);
    }

    @Override
    public void onServerRemoved(Server srv) {
        this.removeServerFromManagement(srv);
    }

    @Override
    public void onServiceStarted() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onServiceStopped() {
        // TODO Auto-generated method stub

    }

    /**
     * AlarmMediator methods
     */

    @Override
    public String getAlarmProviderObjectPath() {
        return this.alc.getAlarmProviderObjectPath();
    }

    @Override
    public CurrentAlarmList getCurrentAlarmList() {
        CurrentAlarmListImpl al = new CurrentAlarmListImpl();
        if (wrappedSctpManagement.isStarted()) {
            Map<String, Association> lstAss = wrappedSctpManagement.getAssociations();
            for (Association ass : lstAss.values()) {
                if (ass.isStarted()) {
                    if (!ass.isConnected()) {
                        AlarmMessage alm = this.generateAssociationAlarm(ass, false, true, "");
                        this.alc.prepareAlarm(alm);

                        al.addAlarm(alm);
                    }
                    int congLevel = ass.getCongestionLevel();
                    if (congLevel > 0) {
                        AlarmMessage alm = this.generateCongestionAlarm(ass, congLevel, false, true, "");
                        this.alc.prepareAlarm(alm);

                        al.addAlarm(alm);
                    }
                }
            }
        }

        return al;
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

    /**
     * methods - private
     */
    private void addAssociationToManagement(Association asso) {
        synchronized (this) {
            Association assBean = new SctpAssociationJmx(this, asso);
            this.ss7Management.registerMBean(Ss7Layer.SCTP, SctpManagementType.ASSOCIATION, asso.getName(), assBean);
            lstAssociations.put(asso.getName(), assBean);
        }
    }

    private AlarmMessage generateAssociationAlarm(Association ass, boolean isCleared, boolean isCurrentAlarmList, String event) {
        AlarmMessageImpl alm = new AlarmMessageImpl();

        alm.setIsCleared(isCleared);
        alm.setAlarmSeverity(AlarmSeverity.minor);
        alm.setAlarmSource("SS7_SCTP_" + this.getName());
        alm.setObjectName("Association: " + ass.getName());
        alm.setObjectPath("/Sctp:" + this.getName() + "/Associations/Association:" + ass.getName());
        alm.setProblemName("SCTP association is down");
        alm.setCause(event);
        alm.setTimeAlarm(Calendar.getInstance());

        if (!isCurrentAlarmList) {
            if (isCleared)
                alm.setCurentTimeClear();
            else
                alm.setCurentTimeAlarm();
        }

        return alm;
    }

    private AlarmMessage generateCongestionAlarm(Association ass, int congLevel, boolean isCleared, boolean isCurrentAlarmList,
            String event) {
        if (congLevel < 1 || congLevel > 3)
            return null;

        AlarmMessageImpl alm = new AlarmMessageImpl();

        alm.setIsCleared(isCleared);
        switch (congLevel) {
            case 1:
                alm.setAlarmSeverity(AlarmSeverity.minor);
                alm.setProblemName("SCTP association congestion level 1 (minor)");
                break;
            case 2:
                alm.setAlarmSeverity(AlarmSeverity.major);
                alm.setProblemName("SCTP association congestion level 2 (major)");
                break;
            case 3:
                alm.setAlarmSeverity(AlarmSeverity.critical);
                alm.setProblemName("SCTP association congestion level 3 (critical)");
                break;
        }
        alm.setAlarmSource("SS7_SCTP_" + this.getName());
        alm.setObjectName("Association: " + ass.getName());
        alm.setObjectPath("/Sctp:" + this.getName() + "/Associations/Association:" + ass.getName());
        alm.setCause(event);
        alm.setTimeAlarm(Calendar.getInstance());

        if (!isCurrentAlarmList) {
            if (isCleared)
                alm.setCurentTimeClear();
            else
                alm.setCurentTimeAlarm();
        }

        return alm;
    }

    private void removeAssociationFromManagement(Association asso) {
        synchronized (this) {
            lstAssociations.remove(asso.getName());
            this.ss7Management.unregisterMBean(Ss7Layer.SCTP, SctpManagementType.ASSOCIATION, asso.getName());
        }
    }

    private void removeAllResourcesFromManagement() {
        // TODO Logic is bad here. wrappedSctpManagement will no more have list
        // of server and associations any more
        synchronized (this) {
            Map<String, Association> lstAss = wrappedSctpManagement.getAssociations();
            for (String s : lstAss.keySet()) {
                Association asso = lstAss.get(s);
                this.removeAssociationFromManagement(asso);
            }
            List<Server> lstServ = wrappedSctpManagement.getServers();
            for (Server srv : lstServ) {
                this.removeServerFromManagement(srv);
            }
        }
    }

    private void removeServerFromManagement(Server srv) {
        synchronized (this) {
            Server srvBeanX = null;
            for (Server srvBean : lstServers) {
                if (srvBean.getName().equals(srv.getName())) {
                    srvBeanX = srvBean;
                    break;
                }
            }

            lstServers.remove(srvBeanX);
            this.ss7Management.unregisterMBean(Ss7Layer.SCTP, SctpManagementType.SERVER, srv.getName());
        }
    }

    private void addServerToManagement(Server srv) {
        synchronized (this) {
            SctpServerJmx srvBean = new SctpServerJmx(this, srv);
            this.ss7Management.registerMBean(Ss7Layer.SCTP, SctpManagementType.SERVER, srv.getName(), srvBean);
            lstServers.add(srvBean);
        }
    }

    @Override
    public void addCongestionListener(CongestionListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeCongestionListener(CongestionListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCongLevelChanged(Association association, int oldCongLevel, int newCongLevel) {
        if (association.isStarted()) {
            if (oldCongLevel > 0) {
                AlarmMessage alm = this.generateCongestionAlarm(association, oldCongLevel, true, false, "onCongLevelChanged");
                this.alc.onAlarm(alm);
            }

            if (newCongLevel > 0) {
                AlarmMessage alm = this.generateCongestionAlarm(association, newCongLevel, false, false, "onCongLevelChanged");
                this.alc.onAlarm(alm);
            }
        }
    }

    @Override
    public void modifyServer(String serverName, String hostAddress, Integer port, IpChannelType ipChannelType,
            Boolean acceptAnonymousConnections, Integer maxConcurrentConnectionsCount, String[] extraHostAddresses)
            throws Exception {
        this.wrappedSctpManagement.modifyServer(serverName, hostAddress, port, ipChannelType, acceptAnonymousConnections, maxConcurrentConnectionsCount, extraHostAddresses);
    }

    @Override
    public void modifyServerAssociation(String assocName, String peerAddress, Integer peerPort, String serverName,
            IpChannelType ipChannelType) throws Exception {
        this.wrappedSctpManagement.modifyServerAssociation(assocName, peerAddress, peerPort, serverName, ipChannelType);

    }

    @Override
    public void modifyAssociation(String hostAddress, Integer hostPort, String peerAddress, Integer peerPort, String assocName,
            IpChannelType ipChannelType, String[] extraHostAddresses) throws Exception {
        this.wrappedSctpManagement.modifyAssociation(hostAddress, hostPort, peerAddress, peerPort, assocName, ipChannelType, extraHostAddresses);

    }

    @Override
    public void onServerModified(Server removeServer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAssociationModified(Association association) {
        // TODO Auto-generated method stub
    }

}
