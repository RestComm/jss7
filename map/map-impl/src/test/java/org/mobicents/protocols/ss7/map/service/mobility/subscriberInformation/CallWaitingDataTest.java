package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCwFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
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
public class CallWaitingDataTest {
    private byte[] data = {48, 14, -95, 10, 48, 8, -95, 3, -125, 1, 0, -126, 1, 15, -126, 0};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
        AsnInputStream asn = new AsnInputStream(data);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        CallWaitingDataImpl callWaitingData = new CallWaitingDataImpl();
        callWaitingData.decodeAll(asn);

        assertNotNull(callWaitingData.getCwFeatureList());
        assertEquals(callWaitingData.getCwFeatureList().size(), 1);
        assertTrue(callWaitingData.getNotificationToCSE());

        ExtCwFeature extCwFeature = callWaitingData.getCwFeatureList().get(0);
        assertNotNull(extCwFeature.getSsStatus());
        assertTrue(extCwFeature.getSsStatus().getBitQ());
        assertTrue(extCwFeature.getSsStatus().getBitP());
        assertTrue(extCwFeature.getSsStatus().getBitR());
        assertTrue(extCwFeature.getSsStatus().getBitA());
        assertEquals(extCwFeature.getBasicService().getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
        ExtBasicServiceCode extBasicServiceCode = new ExtBasicServiceCodeImpl(new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allTeleservices));
        final ExtCwFeatureImpl extCwFeature = new ExtCwFeatureImpl(extBasicServiceCode, new ExtSSStatusImpl(true, true, true, true));
        CallWaitingDataImpl callWaitingData = new CallWaitingDataImpl(new ArrayList<ExtCwFeature>(){{add(extCwFeature);}}, true);

        AsnOutputStream asnOS = new AsnOutputStream();
        callWaitingData.encodeAll(asnOS);
        byte[] raw = asnOS.toByteArray();
        assertTrue(Arrays.equals(raw, data));
    }
}
