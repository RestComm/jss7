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
public class SctpManagementJmx implements SctpManagementJmxMBean, ManagementEventListener, AlarmMediator {

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
            IpChannelType ipChannelType, String extraHostAddresses) throws Exception {
        this.wrappedSctpManagement.addAssociation(hostAddress, hostPort, peerAddress, peerPort, assocName, ipChannelType,
                (extraHostAddresses != null && !extraHostAddresses.isEmpty()) ? extraHostAddresses.split(",") : null);
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

    @Override
    public Server addSctpServer(String serverName, String hostAddress, int port, IpChannelType ipChannelType,
            boolean acceptAnonymousConnections, int maxConcurrentConnectionsCount, String extraHostAddresses) throws Exception {
        this.wrappedSctpManagement.addServer(serverName, hostAddress, port, ipChannelType, acceptAnonymousConnections,
                maxConcurrentConnectionsCount,
                (extraHostAddresses != null && !extraHostAddresses.isEmpty()) ? extraHostAddresses.split(",") : null);
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
            IpChannelType ipChannelType) throws Exception {
        this.wrappedSctpManagement.addServerAssociation(peerAddress, peerPort, serverName, assocName, ipChannelType);
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
        if (!ass.isConnected()) {
            AlarmMessage alm = this.generateAssociationAlarm(ass, false, false, "onAssociationStarted");
            this.alc.onAlarm(alm);
        }
    }

    @Override
    public void onAssociationStopped(Association ass) {
        if (!ass.isConnected()) {
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
                if (ass.isStarted() && !ass.isConnected()) {
                    AlarmMessage alm = this.generateAssociationAlarm(ass, false, true, "");
                    this.alc.prepareAlarm(alm);

                    al.addAlarm(alm);
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
}
