package org.mobicents.protocols.ss7.map.load.mapp;

import org.mobicents.protocols.ss7.map.load.mapp.MAPpContext;
import org.mobicents.protocols.ss7.map.load.mapp.StackInitializer;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.BCDEvenEncodingScheme;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.EncodingScheme;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public class SCCPInitializer implements StackInitializer {

    // MTP Details
    protected static int CLIENT_SPC = 1;
    protected static int SERVET_SPC = 2;
    protected static int NETWORK_INDICATOR = 2;
    protected static int SERVICE_INIDCATOR = 3; // SCCP
    protected static int SSN = 8;

    static final String STACK_ID = "sccpStack";

    @Override
    public String getStackProtocol() {
        return STACK_ID;
    }

    @Override
    public void init(MAPpContext ctx) throws Exception {
        M3UAManagementImpl clientM3UAMgmt = (M3UAManagementImpl) ctx.data.get(M3UAInitializer.STACK_ID);
        SccpStackImpl sccpStack = new SccpStackImpl("MapLoadClientSccpStack");
        sccpStack.setMtp3UserPart(1, clientM3UAMgmt);

//        this.sccpStack.setCongControl_Algo(SccpCongestionControlAlgo.levelDepended);
        sccpStack.start();
        sccpStack.removeAllResourses();

        sccpStack.getSccpResource().addRemoteSpc(0, SERVET_SPC, 0, 0);
        sccpStack.getSccpResource().addRemoteSsn(0, SERVET_SPC, SSN, 0, false);

        sccpStack.getRouter().addMtp3ServiceAccessPoint(1, 1, CLIENT_SPC, NETWORK_INDICATOR, 0);
        sccpStack.getRouter().addMtp3Destination(1, 1, SERVET_SPC, SERVET_SPC, 0, 255, 255);

        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        EncodingScheme ec = new BCDEvenEncodingScheme();
        GlobalTitle gt1 = fact.createGlobalTitle("-", 0, org.mobicents.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                ec, NatureOfAddress.INTERNATIONAL);
        GlobalTitle gt2 = fact.createGlobalTitle("-", 0, org.mobicents.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                ec, NatureOfAddress.INTERNATIONAL);
        SccpAddress localAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, CLIENT_SPC, 0);
        sccpStack.getRouter().addRoutingAddress(1, localAddress);
        SccpAddress remoteAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, SERVET_SPC, 0);
        sccpStack.getRouter().addRoutingAddress(2, remoteAddress);

        GlobalTitle gt = fact.createGlobalTitle("*", 0, org.mobicents.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, ec,
                NatureOfAddress.INTERNATIONAL);
        SccpAddress pattern = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 0);
        sccpStack.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.REMOTE, pattern,
                "K", 1, -1, null, 0);
        sccpStack.getRouter().addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern,
                "K", 2, -1, null, 0);
        ctx.data.put(STACK_ID, sccpStack);
    }
}
