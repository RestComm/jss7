package org.mobicents.protocols.ss7.map.load.mapp;

import org.mobicents.protocols.ss7.map.load.mapp.MAPpContext;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.sccp.NetworkIdState;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public class USSDScenario extends AbstractScenario {

    // MTP Details
    protected static int CLIENT_SPC = 1;
    protected static int SERVET_SPC = 2;
    protected static int NETWORK_INDICATOR = 2;
    protected static int SERVICE_INIDCATOR = 3; // SCCP
    protected static int SSN = 8;

    protected final SccpAddress SCCP_CLIENT_ADDRESS = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
            null, CLIENT_SPC, SSN);
    protected final SccpAddress SCCP_SERVER_ADDRESS = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
            null, SERVET_SPC, SSN);

    @Override
    public void createDialog(MAPpContext ctx) throws Exception {
        MAPStack mapStack = (MAPStack) ctx.data.get(MAPInitializer.STACK_ID);
        MAPProvider mapProvider = mapStack.getMAPProvider();

        // First create Dialog
        AddressString origRef = mapProvider.getMAPParameterFactory().createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "12345");
        AddressString destRef = mapProvider.getMAPParameterFactory().createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "67890");
        MAPDialogSupplementary mapDialog = mapProvider.getMAPServiceSupplementary().createNewDialog(
                MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2),
                SCCP_CLIENT_ADDRESS, origRef, SCCP_SERVER_ADDRESS, destRef);

        CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);

        // USSD String: *125*+31628839999#
        // The Charset is null, here we let system use default Charset (UTF-7 as
        // explained in GSM 03.38. However if MAP User wants, it can set its own
        // impl of Charset
        USSDString ussdString = mapProvider.getMAPParameterFactory().createUSSDString("*125*+31628839999#", null, null);

        ISDNAddressString msisdn = mapProvider.getMAPParameterFactory().createISDNAddressString(
                AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

        mapDialog.addProcessUnstructuredSSRequest(ussdDataCodingScheme, ussdString, null, msisdn);

        //nbConcurrentDialogs.incrementAndGet();
        // This will initiate the TC-BEGIN with INVOKE component
        mapDialog.send();
    }


    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.supplementary. MAPServiceSupplementaryListener
     * #onUnstructuredSSRequestIndication(org.mobicents .protocols.ss7.map.api.service
     * .supplementary.UnstructuredSSRequestIndication)
     */
    @Override
    public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
        super.onUnstructuredSSRequest(unstrReqInd);
        MAPDialogSupplementary mapDialog = unstrReqInd.getMAPDialog();

        try {
            CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);

            MAPStack mapStack = (MAPStack) ctx.data.get(MAPInitializer.STACK_ID);
            MAPProvider mapProvider = mapStack.getMAPProvider();

            USSDString ussdString = mapProvider.getMAPParameterFactory().createUSSDString("1", null, null);

            mapDialog.addUnstructuredSSResponse(unstrReqInd.getInvokeId(), ussdDataCodingScheme, ussdString);
            mapDialog.send();

        } catch (MAPException e) {
            ctx.logger.error(String.format("Error while sending UnstructuredSSResponse for Dialog=%d", mapDialog.getLocalDialogId()));
        }
    }
}
