package org.mobicents.protocols.ss7.sccp.impl;

import java.io.IOException;

import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.NetworkIdState;
import org.mobicents.protocols.ss7.sccp.RemoteSccpStatus;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SignallingPointStatus;
import org.mobicents.protocols.ss7.sccp.impl.parameter.BCDEvenEncodingScheme;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0100Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public class Test implements SccpListener {
    private SccpProvider sccpProvider;
    private SccpAddress localAddress;
    private int localSsn = 8;

    private static SccpProvider getSccpProvider() {
        Mtp3UserPartImpl mtp3UserPart1 = null;
        // ......
        // ......
        SccpStackImpl sccpStack1 = new SccpStackImpl("testSccpStack");
        sccpStack1.setMtp3UserPart(1, mtp3UserPart1);
        sccpStack1.start();
        return sccpStack1.getSccpProvider();
    }

    public void start() throws Exception {
        this.sccpProvider = getSccpProvider();
        int translationType = 0;
        GlobalTitle gt = new GlobalTitle0100Impl("1234",translationType, BCDEvenEncodingScheme.INSTANCE,NumberingPlan.ISDN_MOBILE, NatureOfAddress.NATIONAL);
        localAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt,-1,  0);
        this.sccpProvider.registerSccpListener(this.localSsn, this);
    }

    public void stop() {
        this.sccpProvider.deregisterSccpListener(this.localSsn);
    }

    @Override
    public void onMessage(SccpDataMessage message) {
        localAddress = message.getCalledPartyAddress();
        SccpAddress remoteAddress = message.getCallingPartyAddress();
        // now decode content
        byte[] data = message.getData();
        // processing a request
        byte[] answerData = new byte[10];
        // put custom executing code here and fill answerData
        HopCounter hc = this.sccpProvider.getParameterFactory().createHopCounter(5);
        SccpDataMessage sccpAnswer = this.sccpProvider.getMessageFactory().createDataMessageClass1(remoteAddress, localAddress,
                answerData, message.getSls(), localSsn, false, hc, null);
        try {
            this.sccpProvider.send(sccpAnswer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onNotice(SccpNoticeMessage message) {
    }

    @Override
    public void onPcState(int dpc, SignallingPointStatus status, Integer restrictedImportanceLevel,
            RemoteSccpStatus remoteSccpStatus) {
    }

    @Override
    public void onCoordResponse(int ssn, int multiplicityIndicator) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onState(int dpc, int ssn, boolean inService, int multiplicityIndicator) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onNetworkIdState(int networkId, NetworkIdState networkIdState) {
        // TODO Auto-generated method stub
        
    }

}
