package org.restcomm.protocols.ss7.map.load;

import org.apache.log4j.Logger;
import org.mobicents.protocols.api.IpChannelType;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.restcomm.protocols.ss7.map.api.smstpdu.AbsoluteTimeStamp;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressField;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserData;
import org.restcomm.protocols.ss7.map.smstpdu.AbsoluteTimeStampImpl;
import org.restcomm.protocols.ss7.map.smstpdu.AddressFieldImpl;
import org.restcomm.protocols.ss7.map.smstpdu.DataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsDeliverTpduImpl;
import org.restcomm.protocols.ss7.map.smstpdu.UserDataImpl;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicLong;


public class SriWithMTFsmClient extends SriClient {
    private static final Logger logger = Logger.getLogger(SriWithMTFsmClient.class);

    private AtomicLong successMTfsm = new AtomicLong(0);
    private AtomicLong mtMessageCounter = new AtomicLong(0);

    public static void main(String[] args) {
        try {
            IpChannelType ipChannelType = collectClientArgs(args);

            final SriWithMTFsmClient client = new SriWithMTFsmClient();
            client.initializeStackNoReport(ipChannelType);
            client.targetGT = "55";
            client.start();
            client.waitFinishAndPrintReport();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed: " + e.toString());
        }
    }

    private void waitFinishAndPrintReport() throws InterruptedException {
        while (true) {
            if (this.callsFinished.get() == NDIALOGS * 2) {
                this.shutdown();
                break;
            }
            Thread.sleep(20000);
        }
        this.printReport();
        System.exit(0);
    }

    @Override
    void printReport() {
        super.printReport();
        logger.info("Success MT forward messages: " + this.successMTfsm.get());
    }

    @Override
    public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMRespInd) {
        try {
            rateLimiterObj.acquire();
            super.onSendRoutingInfoForSMResponse(sendRoutingInfoForSMRespInd);

            MAPParameterFactory mapParameterFactory = mapProvider.getMAPParameterFactory();
            AddressString origRef = mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, originGT);
            AddressString destRef = sendRoutingInfoForSMRespInd.getLocationInfoWithLMSI().getNetworkNodeNumber();

            MAPDialogSms mapDialogSms = getMapDialogSms(origRef, destRef, MAPApplicationContextName.shortMsgMTRelayContext);

            IMSI imsi = sendRoutingInfoForSMRespInd.getIMSI();
            SM_RP_DA sm_RP_DA = mapParameterFactory.createSM_RP_DA(imsi);
            SM_RP_OA sm_RP_OA = mapParameterFactory.createSM_RP_OA_ServiceCentreAddressOA(origRef);

            ProtocolIdentifier pi = new ProtocolIdentifierImpl(0);

            AbsoluteTimeStamp serviceCentreTimeStamp = getAbsoluteTimeStamp();
            String message = String.format("msg_number_%d", this.mtMessageCounter.getAndIncrement());
            final int gsm7 = 0;
            DataCodingScheme dcs = new DataCodingSchemeImpl(gsm7);
            UserData userData = new UserDataImpl(message, dcs, null, null);

            AddressField originAddressField = new AddressFieldImpl(TypeOfNumber.InternationalNumber, NumberingPlanIdentification.ISDNTelephoneNumberingPlan, originGT);

            SmsDeliverTpduImpl tpdu = new SmsDeliverTpduImpl(false, false, false, false, originAddressField, pi, serviceCentreTimeStamp, userData);

            SmsSignalInfo sm_rp_ui = mapParameterFactory.createSmsSignalInfo(tpdu, null);

            mapDialogSms.addMtForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_rp_ui, false, null);
            mapDialogSms.send();
        } catch (MAPException e) {
            e.printStackTrace();
            logger.error(e.toString());
            this.mapExceptions.incrementAndGet();
        }
    }

    private AbsoluteTimeStamp getAbsoluteTimeStamp() {
        Calendar cld = new GregorianCalendar();
        int year = cld.get(Calendar.YEAR);
        int mon = cld.get(Calendar.MONTH);
        int day = cld.get(Calendar.DAY_OF_MONTH);
        int h = cld.get(Calendar.HOUR);
        int m = cld.get(Calendar.MINUTE);
        int s = cld.get(Calendar.SECOND);
        int tz = cld.get(Calendar.ZONE_OFFSET);

        return new AbsoluteTimeStampImpl(year - 2000, mon, day, h, m, s, tz / 1000 / 60 / 15);
    }

    @Override
    public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwSmInd) {
        logger.info("onMtForwardShortMessageRequest");
    }

    @Override
    public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwSmRespInd) {
        successMTfsm.incrementAndGet();
    }
}
