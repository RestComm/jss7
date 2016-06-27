package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author vadim subbotin
 */
public class ExtCwFeatureTest {
    private byte[] data = {48, 8, -95, 3, -125, 1, 32, -126, 1, 12};

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {
        AsnInputStream ansIS = new AsnInputStream(data);
        int tag = ansIS.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        ExtCwFeatureImpl extCwFeature = new ExtCwFeatureImpl();
        extCwFeature.decodeAll(ansIS);

        ExtBasicServiceCode extBasicServiceCode = extCwFeature.getBasicService();
        assertEquals(extBasicServiceCode.getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allShortMessageServices);

        ExtSSStatus extSSStatus = extCwFeature.getSsStatus();
        assertTrue(extSSStatus.getBitQ());
        assertTrue(extSSStatus.getBitP());
        assertFalse(extSSStatus.getBitR());
        assertFalse(extSSStatus.getBitA());
    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {
        ExtBasicServiceCode extBasicServiceCode = new ExtBasicServiceCodeImpl(new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allShortMessageServices));
        ExtCwFeatureImpl extCwFeature = new ExtCwFeatureImpl(extBasicServiceCode, new ExtSSStatusImpl(true, true, false, false));

        AsnOutputStream asnOS = new AsnOutputStream();
        extCwFeature.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));
    }
}
