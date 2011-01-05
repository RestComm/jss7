import javax.naming.NamingException;

import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.dialog.AddressNature;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAcceptInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPCloseInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPOpenInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderAbortInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortInfo;
import org.mobicents.protocols.ss7.map.api.dialog.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSIndication;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public class MAPExample implements MAPDialogListener, MAPServiceListener {

    private MAPStack mapStack;
    private MAPProvider mapProvider;

    MapServiceFactory servFact;

    SccpAddress destAddress = null;

    // The address created by passing the AddressNature, NumberingPlan and
    // actual address
    AddressString destReference = servFact.createAddressString(
            AddressNature.international_number, NumberingPlan.land_mobile,
            "204208300008002");

    SccpAddress origAddress = null;

    AddressString origReference = servFact.createAddressString(
            AddressNature.international_number, NumberingPlan.ISDN,
            "31628968300");

    MAPExample(SccpProvider sccpPprovider, SccpAddress address,
            SccpAddress remoteAddress) {
        origAddress = address;
        destAddress = remoteAddress;

        mapStack = new MAPStackImpl(sccpPprovider, origAddress);
        mapProvider = mapStack.getMAPProvider();
        servFact = mapProvider.getMapServiceFactory();

        mapProvider.addMAPDialogListener(this);
        mapProvider.addMAPServiceListener(this);
    }

    private static SccpProvider getSccpProvider()

    throws NamingException

    {

//        // no arg is ok, if we run in JBoss
//
//        InitialContext ctx = new InitialContext();
//
//        try {
//
//            String adapterName = "jmx/invoker/RMIAdaptor";
//
//            Object obj = ctx.lookup(adapterName);
//
//            if (!(obj instanceof RMIAdaptor))
//
//            {
//
//                throw new ClassCastException
//
//                ("Object not of type: RMIAdaptorImpl, but: " +
//
//                (obj == null ? "not found" : obj.getClass().getName()));
//
//            }
//
//            // SS7Service is JMX bean, lets use it to get
//
//            MBeanServerConnection con = createMBeanServerConnection();
//
//            ObjectName on = new ObjectName(
//                    "org.mobicents.ss7:service=SS7Service");
//
//            String providerJndiName = con.invoke(on, "getJndiName", null, null);
//
//            InitialContext ctx = new InitialContext();
//
//            return (SccpProvider) ctx.lookup(providerJndiName);
//
//        } finally
//
//        {
//
//            ctx.close();
//
//        }
        return null;

    }

    public void run() throws Exception {

        // First create Dialog
        MAPDialog mapDialog = mapProvider.createNewDialog(
                MAPApplicationContext.networkUnstructuredSsContextV2,
                destAddress, destReference, origAddress, origReference);

        // The dataCodingScheme is still byte, as I am not exactly getting how
        // to encode/decode this.
        byte ussdDataCodingScheme = 0x0f;

        // USSD String: *125*+31628839999#
        // The Charset is null, here we let system use default Charset (UTF-7 as
        // explained in GSM 03.38. However if MAP User wants, it can set its own
        // impl of Charset
        USSDString ussdString = servFact.createUSSDString("*125*+31628839999#",
                null);

        AddressString msisdn = this.servFact.createAddressString(
                AddressNature.international_number, NumberingPlan.ISDN,
                "31628838002");

        mapDialog.addProcessUnstructuredSSRequest(ussdDataCodingScheme,
                ussdString, msisdn);

        // This will initiate the TC-BEGIN with INVOKE component
        mapDialog.send();
    }

    public void onMAPAcceptInfo(MAPAcceptInfo mapAccptInfo) {
        // TODO Auto-generated method stub

    }

    public void onMAPCloseInfo(MAPCloseInfo mapCloseInfo) {
        // TODO Auto-generated method stub

    }

    public void onMAPOpenInfo(MAPOpenInfo mapOpenInfo) {
        // TODO Auto-generated method stub

    }

    public void onMAPProviderAbortInfo(MAPProviderAbortInfo mapProviderAbortInfo) {
        // TODO Auto-generated method stub

    }

    public void onMAPRefuseInfo(MAPRefuseInfo mapRefuseInfo) {
        // TODO Auto-generated method stub

    }

    public void onMAPUserAbortInfo(MAPUserAbortInfo mapUserAbortInfo) {
        // TODO Auto-generated method stub

    }

    public void onProcessUnstructuredSSIndication(
            ProcessUnstructuredSSIndication procUnstrInd) {
        // TODO Auto-generated method stub

    }

    public void onUnstructuredSSIndication(UnstructuredSSIndication unstrInd) {
        // TODO Auto-generated method stub

    }

    public static void main(String[] args) {

        int translationType = 0;

        int subSystemNumber = 0;

//        GlobalTitle gt = GlobalTitle.getInstance(translationType,
//                NumberingPlan.ISDN_MOBILE, NatureOfAddress.NATIONAL, "1234");
//
//        SccpAddress localAddress = new SccpAddress(gt, 0);
//
//        gt = GlobalTitle.getInstance(translationType,
//                NumberingPlan.ISDN_MOBILE, NatureOfAddress.NATIONAL, "1572582");
//
//        SccpAddress remoteAddress = new SccpAddress(gt, 0);
//
//        MAPExample c = new MAPExample(getSccpProvider(), localAddress, remoteAddress);

    }

}
