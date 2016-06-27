package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author vadim subbotin
 */
public class MSISDNBSTest {
    private byte[] data = {48, 54, 4, 6, -111, 17, 33, 34, 51, -13, -96, 3, -125, 1, 96, -95, 39, -96, 32, 48, 10, 6,
            3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95,
            3, 31, 32, 33};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
        AsnInputStream asn = new AsnInputStream(data);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        MSISDNBSImpl msisdnbs = new MSISDNBSImpl();
        msisdnbs.decodeAll(asn);

        assertNotNull(msisdnbs.getMsisdn());
        assertNotNull(msisdnbs.getBasicServiceList());
        assertEquals(msisdnbs.getBasicServiceList().size(), 1);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(msisdnbs.getExtensionContainer()));

        ExtBasicServiceCode extBasicServiceCode = msisdnbs.getBasicServiceList().get(0);
        assertEquals(extBasicServiceCode.getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.allFacsimileTransmissionServices);

        ISDNAddressString msisdn = msisdnbs.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "111222333");
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        final ExtBasicServiceCodeImpl extBasicServiceCode = new ExtBasicServiceCodeImpl(
                new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allFacsimileTransmissionServices));
        MSISDNBSImpl msisdnbs = new MSISDNBSImpl(msisdn, new ArrayList<ExtBasicServiceCode>(){{add(extBasicServiceCode);}},
                MAPExtensionContainerTest.GetTestExtensionContainer());

        AsnOutputStream asnOS = new AsnOutputStream();
        msisdnbs.encodeAll(asnOS);
        byte[] raw = asnOS.toByteArray();
        assertTrue(Arrays.equals(raw, data));
    }
}
