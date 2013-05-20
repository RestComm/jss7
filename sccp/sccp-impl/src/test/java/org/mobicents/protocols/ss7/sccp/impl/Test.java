package org.mobicents.protocols.ss7.sccp.impl;

import java.io.IOException;

import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.RemoteSccpStatus;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SignallingPointStatus;
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
        GlobalTitle gt = GlobalTitle.getInstance(translationType, NumberingPlan.ISDN_MOBILE, NatureOfAddress.NATIONAL, "1234");
        localAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, -1, gt, 0);
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

    public void onCoordRequest(int dpc, int ssn, int multiplicityIndicator) {
    }

    public void onCoordResponse(int dpc, int ssn, int multiplicityIndicator) {
    }

    public void onState(int dpc, int ssn, boolean inService, int multiplicityIndicator) {
    }

    @Override
    public void onPcState(int dpc, SignallingPointStatus status, int restrictedImportanceLevel,
            RemoteSccpStatus remoteSccpStatus) {
    }

}
