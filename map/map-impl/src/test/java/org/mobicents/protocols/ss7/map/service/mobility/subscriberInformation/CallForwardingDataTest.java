package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.FTNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtForwFeatureImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * @author vadim subbotin
 */
public class CallForwardingDataTest {
    private byte[] data = {48, 67, 48, 22, 48, 20, -124, 1, 5, -123, 6, -111, -119, 103, 69, 35, -15, -121, 1, 5, -118,
            4, -111, 33, 67, -11, 5, 0, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
            3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
        AsnInputStream asn = new AsnInputStream(data);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        CallForwardingDataImpl callForwardingData = new CallForwardingDataImpl();
        callForwardingData.decodeAll(asn);

        assertNotNull(callForwardingData.getForwardingFeatureList());
        assertEquals(callForwardingData.getForwardingFeatureList().size(), 1);
        assertTrue(callForwardingData.getNotificationToCSE());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(callForwardingData.getExtensionContainer()));

        ExtForwFeature extForwFeature = callForwardingData.getForwardingFeatureList().get(0);
        ISDNAddressString forwardedToNumber = extForwFeature.getForwardedToNumber();
        ExtSSStatus extSSStatus = extForwFeature.getSsStatus();
        FTNAddressString longForwardedToNumber = extForwFeature.getLongForwardedToNumber();
        assertNull(extForwFeature.getBasicService());
        assertNull(extForwFeature.getForwardedToSubaddress());
        assertNull(extForwFeature.getForwardingOptions());
        assertNull(extForwFeature.getExtensionContainer());
        assertEquals(extForwFeature.getNoReplyConditionTime().intValue(), 5);
        assertEquals(forwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(forwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(forwardedToNumber.getAddress(), "987654321");
        assertFalse(extSSStatus.getBitQ());
        assertTrue(extSSStatus.getBitP());
        assertFalse(extSSStatus.getBitR());
        assertTrue(extSSStatus.getBitA());
        assertEquals(longForwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(longForwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(longForwardedToNumber.getAddress(), "12345");
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
        ISDNAddressStringImpl forwardedToNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "987654321");
        FTNAddressStringImpl longForwardedToNumber = new FTNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "12345");
        final ExtForwFeatureImpl extForwFeature = new ExtForwFeatureImpl(null, new ExtSSStatusImpl(false, true, false, true), forwardedToNumber, null,
                null, 5, null, longForwardedToNumber);
        CallForwardingDataImpl callForwardingData = new CallForwardingDataImpl(new ArrayList<ExtForwFeature>(){{add(extForwFeature);}}, true,
                MAPExtensionContainerTest.GetTestExtensionContainer());

        AsnOutputStream asnOS = new AsnOutputStream();
        callForwardingData.encodeAll(asnOS);
        byte[] raw = asnOS.toByteArray();
        assertTrue(Arrays.equals(raw, data));
    }
}
