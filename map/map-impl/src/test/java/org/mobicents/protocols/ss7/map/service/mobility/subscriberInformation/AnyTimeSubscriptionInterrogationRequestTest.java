package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSForBSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TeleserviceCodeImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSForBSCodeImpl;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author vadim subbotin
 */
public class AnyTimeSubscriptionInterrogationRequestTest {
    private byte[] data = {48, 91, -96, 9, -127, 7, -111, -105, 2, 33, 67, 101, -9, -95, 26, -95, 6, 4, 1, 112, -125, 1,
            0, -126, 0, -125, 1, 0, -124, 0, -121, 1, 2, -120, 0, -118, 0, -116, 0, -114, 0, -126, 7, -111, -105, 2, 103,
            69, 35, -15, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6,
            3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -124, 0};

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {
        AsnInputStream ansIS = new AsnInputStream(data);
        int tag = ansIS.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        AnyTimeSubscriptionInterrogationRequestImpl request = new AnyTimeSubscriptionInterrogationRequestImpl();
        request.decodeAll(ansIS);

        assertNotNull(request.getSubscriberIdentity());
        assertNotNull(request.getGsmScfAddress());
        assertNotNull(request.getRequestedSubscriptionInfo());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(request.getExtensionContainer()));
        assertTrue(request.getLongFTNSupported());
        assertEquals(request.getGsmScfAddress().getAddress(), "79207654321");

        ISDNAddressString subscriberMsisdn = request.getSubscriberIdentity().getMSISDN();
        assertEquals(subscriberMsisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(subscriberMsisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(subscriberMsisdn.getAddress(), "79201234567");

        RequestedSubscriptionInfo subscriptionInfo = request.getRequestedSubscriptionInfo();
        assertEquals(subscriptionInfo.getRequestedSSInfo().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allChargingSS);
        assertEquals(subscriptionInfo.getRequestedSSInfo().getBasicService().getTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);
        assertFalse(subscriptionInfo.getRequestedSSInfo().getLongFtnSupported());
        assertTrue(subscriptionInfo.getOdb());
        assertEquals(subscriptionInfo.getRequestedCAMELSubscriptionInfo(), RequestedCAMELSubscriptionInfo.oCSI);
        assertTrue(subscriptionInfo.getSupportedVlrCamelPhases());
        assertFalse(subscriptionInfo.getSupportedSgsnCamelPhases());
        assertEquals(subscriptionInfo.getAdditionalRequestedCamelSubscriptionInfo(), AdditionalRequestedCAMELSubscriptionInfo.oImCSI);
        assertTrue(subscriptionInfo.getMsisdnBsList());
        assertFalse(subscriptionInfo.getCsgSubscriptionDataRequested());
        assertTrue(subscriptionInfo.getCwInfo());
        assertFalse(subscriptionInfo.getClipInfo());
        assertTrue(subscriptionInfo.getClirInfo());
        assertFalse(subscriptionInfo.getHoldInfo());
        assertTrue(subscriptionInfo.getEctInfo());
    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {
        ISDNAddressStringImpl subscriberMsisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "79201234567");
        SubscriberIdentity subscriberIdentity = new SubscriberIdentityImpl(subscriberMsisdn);

        SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.allChargingSS);
        BasicServiceCode basicServiceCode = new BasicServiceCodeImpl(new TeleserviceCodeImpl(TeleserviceCodeValue.allTeleservices));
        SSForBSCode ssForBSCode = new SSForBSCodeImpl(ssCode, basicServiceCode, false);
        RequestedSubscriptionInfo requestedSubscriptionInfo = new RequestedSubscriptionInfoImpl(ssForBSCode, true, RequestedCAMELSubscriptionInfo.oCSI,
                true, false, null, AdditionalRequestedCAMELSubscriptionInfo.oImCSI, true, false, true,
                false, true, false, true);

        ISDNAddressString gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "79207654321");

        AnyTimeSubscriptionInterrogationRequestImpl request = new AnyTimeSubscriptionInterrogationRequestImpl(subscriberIdentity, requestedSubscriptionInfo,
                gsmSCFAddress, MAPExtensionContainerTest.GetTestExtensionContainer(), true);

        AsnOutputStream asnOS = new AsnOutputStream();
        request.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));
    }
}
