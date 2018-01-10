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
package org.mobicents.protocols.ss7.oam.common.sccp;

import java.util.Calendar;
import java.util.Map;


import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
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
import org.mobicents.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.RemoteSubSystem;
import org.mobicents.protocols.ss7.sccp.Router;
import org.mobicents.protocols.ss7.sccp.SccpCongestionControlAlgo;
import org.mobicents.protocols.ss7.sccp.SccpManagementEventListener;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpResource;
import org.mobicents.protocols.ss7.sccp.SccpStack;

/**
 * @author Amit Bhayani
 *
 */
public class SccpManagementJmx implements SccpManagementJmxMBean, SccpManagementEventListener, AlarmMediator {

    private final MBeanHost ss7Management;
    private final SccpStack wrappedSccpStack;

    private AlarmListenerCollection alc = new AlarmListenerCollection();

    public SccpManagementJmx(MBeanHost ss7Management, SccpStack wrappedSccpStack) {
        this.ss7Management = ss7Management;
        this.wrappedSccpStack = wrappedSccpStack;
    }

    /**
     * methods - bean life-cycle
     */

    public void start() {
        // TODO : Check if these MBeans already registered, if they are don't do it again

        this.ss7Management.registerMBean(Ss7Layer.SCCP, SccpManagementType.MANAGEMENT, this.getName(), this);

        SccpRouterJmx sccpRouterJmx = new SccpRouterJmx(this.wrappedSccpStack.getRouter(),this.wrappedSccpStack.getSccpProvider());
        this.ss7Management.registerMBean(Ss7Layer.SCCP, SccpManagementType.ROUTER, this.getName(), sccpRouterJmx);

        SccpResourceJmx sccpResourceJmx = new SccpResourceJmx(this.wrappedSccpStack.getSccpResource());
        this.ss7Management.registerMBean(Ss7Layer.SCCP, SccpManagementType.RESOURCE, this.getName(), sccpResourceJmx);

        this.wrappedSccpStack.getSccpProvider().registerManagementEventListener(this);
    }

    public void stop() {

    }

    @Override
    public int getMaxDataMessage() {
        return this.wrappedSccpStack.getMaxDataMessage();
    }

    @Override
    public int getPeriodOfLogging() {
        return this.wrappedSccpStack.getPeriodOfLogging();
    }

    @Override
    public Mtp3UserPart getMtp3UserPart(int id) {
        return this.wrappedSccpStack.getMtp3UserPart(id);
    }

    @Override
    public Map<Integer, Mtp3UserPart> getMtp3UserParts() {
        return this.wrappedSccpStack.getMtp3UserParts();
    }

    @Override
    public String getName() {
        return this.wrappedSccpStack.getName();
    }

    @Override
    public String getPersistDir() {
        return this.wrappedSccpStack.getPersistDir();
    }

    @Override
    public int getReassemblyTimerDelay() {
        return this.wrappedSccpStack.getReassemblyTimerDelay();
    }

    @Override
    public Router getRouter() {
        return this.wrappedSccpStack.getRouter();
    }

    @Override
    public SccpProvider getSccpProvider() {
        return null;
    }

    @Override
    public SccpResource getSccpResource() {
        return this.wrappedSccpStack.getSccpResource();
    }

    @Override
    public double getSstTimerDuration_IncreaseFactor() {
        return this.wrappedSccpStack.getSstTimerDuration_IncreaseFactor();
    }

    @Override
    public int getSstTimerDuration_Max() {
        return this.wrappedSccpStack.getSstTimerDuration_Max();
    }

    @Override
    public int getSstTimerDuration_Min() {
        return this.wrappedSccpStack.getSstTimerDuration_Min();
    }

    @Override
    public int getZMarginXudtMessage() {
        return this.wrappedSccpStack.getZMarginXudtMessage();
    }

    @Override
    public boolean isRemoveSpc() {
        return this.wrappedSccpStack.isRemoveSpc();
    }

    @Override
    public void setPersistDir(String persistDir) {
        this.wrappedSccpStack.setPersistDir(persistDir);
    }

    @Override
    public void setRemoveSpc(boolean removeSpc) throws Exception {
        this.wrappedSccpStack.setRemoveSpc(removeSpc);
    }

    @Override
    public void setPreviewMode(boolean previewMode) throws Exception {
        this.wrappedSccpStack.setPreviewMode(previewMode);
    }

    @Override
    public boolean isPreviewMode() {
        return this.wrappedSccpStack.isPreviewMode();
    }

    @Override
    public void setSstTimerDuration_Min(int sstTimerDuration_Min) throws Exception {
        this.wrappedSccpStack.setSstTimerDuration_Min(sstTimerDuration_Min);
    }

    @Override
    public void setSstTimerDuration_Max(int sstTimerDuration_Max) throws Exception {
        this.wrappedSccpStack.setSstTimerDuration_Max(sstTimerDuration_Max);
    }

    @Override
    public void setSstTimerDuration_IncreaseFactor(double sstTimerDuration_IncreaseFactor) throws Exception {
        this.wrappedSccpStack.setSstTimerDuration_IncreaseFactor(sstTimerDuration_IncreaseFactor);
    }

    @Override
    public void setZMarginXudtMessage(int zMarginXudtMessage) throws Exception {
        this.wrappedSccpStack.setZMarginXudtMessage(zMarginXudtMessage);
    }

    @Override
    public void setMaxDataMessage(int maxDataMessage) throws Exception {
        this.wrappedSccpStack.setMaxDataMessage(maxDataMessage);
    }

    @Override
    public void setPeriodOfLogging(int periodOfLogging) throws Exception {
        this.wrappedSccpStack.setPeriodOfLogging(periodOfLogging);
    }

    @Override
    public void setReassemblyTimerDelay(int reassemblyTimerDelay) throws Exception {
        this.wrappedSccpStack.setReassemblyTimerDelay(reassemblyTimerDelay);
    }

    @Override
    public void setMtp3UserParts(Map<Integer, Mtp3UserPart> mtp3UserPartsTemp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSccpProtocolVersion(SccpProtocolVersion sccpProtocolVersion) throws Exception {
        this.wrappedSccpStack.setSccpProtocolVersion(sccpProtocolVersion);
    }

    @Override
    public SccpProtocolVersion getSccpProtocolVersion() {
        return this.wrappedSccpStack.getSccpProtocolVersion();
    }

    @Override
    public int getCongControlTIMER_A() {
        return this.wrappedSccpStack.getCongControlTIMER_A();
    }

    @Override
    public void setCongControlTIMER_A(int value) throws Exception {
        this.wrappedSccpStack.setCongControlTIMER_A(value);
    }

    @Override
    public int getCongControlTIMER_D() {
        return this.wrappedSccpStack.getCongControlTIMER_D();
    }

    @Override
    public void setCongControlTIMER_D(int value) throws Exception {
        this.wrappedSccpStack.setCongControlTIMER_D(value);
    }

    @Override
    public SccpCongestionControlAlgo getCongControl_Algo() {
        return this.wrappedSccpStack.getCongControl_Algo();
    }

    @Override
    public void setCongControl_Algo(SccpCongestionControlAlgo value) throws Exception {
        this.wrappedSccpStack.setCongControl_Algo(value);
    }

    @Override
    public boolean isCongControl_blockingOutgoungSccpMessages() {
        return this.wrappedSccpStack.isCongControl_blockingOutgoungSccpMessages();
    }

    @Override
    public void setCongControl_blockingOutgoungSccpMessages(boolean value) throws Exception {
        this.wrappedSccpStack.setCongControl_blockingOutgoungSccpMessages(value);
    }

    @Override
    public boolean isStarted() {
        return this.wrappedSccpStack.isStarted();
    }

    @Override
    public void onServiceStarted() {

    }

    @Override
    public void onServiceStopped() {

    }

    @Override
    public void onRemoveAllResources() {

    }

    @Override
    public void onRemoteSubSystemUp(RemoteSubSystem rss) {
        //do we need to check previous state?
        AlarmMessage alm = this.generateRemoteSubSystemAlarm(rss, true);
        this.alc.onAlarm(alm);

    }

    @Override
    public void onRemoteSubSystemDown(RemoteSubSystem rss) {
        AlarmMessage alm = this.generateRemoteSubSystemAlarm(rss, false);
        this.alc.onAlarm(alm);
    }

    @Override
    public void onRemoteSpcUp(RemoteSignalingPointCode remoteSpc) {
        AlarmMessage alm = this.generateRemoteSpcAlarm(remoteSpc, true);
        this.alc.onAlarm(alm);
    }

    @Override
    public void onRemoteSpcDown(RemoteSignalingPointCode remoteSpc) {
        AlarmMessage alm = this.generateRemoteSpcAlarm(remoteSpc, false);
        this.alc.onAlarm(alm);
    }

    @Override
    public void onRemoteSccpUp(RemoteSignalingPointCode remoteSpc) {
        AlarmMessage alm = this.generateRemoteSccpAlarm(remoteSpc, true);
        this.alc.onAlarm(alm);

    }

    @Override
    public void onRemoteSccpDown(RemoteSignalingPointCode remoteSpc) {
        AlarmMessage alm = this.generateRemoteSccpAlarm(remoteSpc, false);
        this.alc.onAlarm(alm);

    }

    @Override
    public void registerAlarmListener(AlarmListener al) {
        this.alc.registerAlarmListener(al);

    }

    @Override
    public void unregisterAlarmListener(AlarmListener al) {
        this.alc.unregisterAlarmListener(al);

    }

    @Override
    public CurrentAlarmList getCurrentAlarmList() {
        CurrentAlarmListImpl all = new CurrentAlarmListImpl();
        //check ssns
        Map<Integer, RemoteSubSystem> remoteSsns = wrappedSccpStack.getSccpResource().getRemoteSsns();
        for (RemoteSubSystem rss : remoteSsns.values()) {
            if(rss.isRemoteSsnProhibited()) {
                AlarmMessage alm = this.generateRemoteSubSystemAlarm(rss, false);
                all.addAlarm(alm);
            }
        }
        //Check spcs
        Map<Integer, RemoteSignalingPointCode> remoteSpcs = wrappedSccpStack.getSccpResource().getRemoteSpcs();
        for (RemoteSignalingPointCode rspc : remoteSpcs.values()) {
            if(rspc.isRemoteSpcProhibited()) {
                AlarmMessage alm = this.generateRemoteSpcAlarm(rspc, false);
                all.addAlarm(alm);
            }
            if(rspc.isRemoteSccpProhibited()) {
                AlarmMessage alm = this.generateRemoteSccpAlarm(rspc, false);
                all.addAlarm(alm);
            }
        }

        return all;
    }

    @Override
    public String getAlarmProviderObjectPath() {
        return this.alc.getAlarmProviderObjectPath();
    }

    @Override
    public void setAlarmProviderObjectPath(String value) {
        this.alc.setAlarmProviderObjectPath(value);

    }

    private AlarmMessage generateRemoteSubSystemAlarm(RemoteSubSystem rss, boolean isCleared) {

        AlarmMessageImpl alm = new AlarmMessageImpl();
        alm.setIsCleared(isCleared);
        alm.setAlarmSeverity(AlarmSeverity.major);
        alm.setAlarmSource("SS7_SCCP_" + this.getName());
        alm.setObjectName("RemoteSubSystem: " + rss.getRemoteSsn());
        alm.setObjectPath("/Sccp:" + this.getName() + "/Rss:" + rss.getRemoteSsn());
        alm.setProblemName("Remote subsystem state is down");
        alm.setTimeAlarm(Calendar.getInstance());

        return alm;
    }

    private AlarmMessage generateRemoteSpcAlarm(RemoteSignalingPointCode rspc, boolean isCleared) {

        AlarmMessageImpl alm = new AlarmMessageImpl();
        alm.setIsCleared(isCleared);
        alm.setAlarmSeverity(AlarmSeverity.major);
        alm.setAlarmSource("SS7_SCCP_" + this.getName());
        alm.setObjectName("RemoteSpc: " + rspc.getRemoteSpc());
        alm.setObjectPath("/Sccp:" + this.getName() + "/Rspc:" + rspc.getRemoteSpc());
        alm.setProblemName("Remote signaling point code state is down");
        alm.setTimeAlarm(Calendar.getInstance());

        return alm;
    }

    private AlarmMessage generateRemoteSccpAlarm(RemoteSignalingPointCode rspc, boolean isCleared) {

        AlarmMessageImpl alm = new AlarmMessageImpl();
        alm.setIsCleared(isCleared);
        alm.setAlarmSeverity(AlarmSeverity.major);
        alm.setAlarmSource("SS7_SCCP_" + this.getName());
        alm.setObjectName("RemoteSccp: " + rspc.getRemoteSpc());
        alm.setObjectPath("/Sccp:" + this.getName() + "/Rsccp:" + rspc.getRemoteSpc());
        alm.setProblemName("Remote SCCP state is down");
        alm.setTimeAlarm(Calendar.getInstance());

        return alm;
    }

}
