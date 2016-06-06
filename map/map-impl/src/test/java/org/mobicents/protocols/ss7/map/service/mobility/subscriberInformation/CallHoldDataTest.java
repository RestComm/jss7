package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author vadim subbotin
 */
public class CallHoldDataTest {
    private byte[] data = {48, 5, -127, 1, 9, -126, 0};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
        AsnInputStream asn = new AsnInputStream(data);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        CallHoldDataImpl callHoldData = new CallHoldDataImpl();
        callHoldData.decodeAll(asn);
        assertNotNull(callHoldData.getSsStatus());
        assertTrue(callHoldData.getSsStatus().getBitQ());
        assertFalse(callHoldData.getSsStatus().getBitP());
        assertFalse(callHoldData.getSsStatus().getBitR());
        assertTrue(callHoldData.getSsStatus().getBitA());
        assertTrue(callHoldData.getNotificationToCSE());
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
        ExtSSStatus extSSStatus = new ExtSSStatusImpl(true, false, false, true);
        CallHoldDataImpl callHoldData = new CallHoldDataImpl(extSSStatus, true);

        AsnOutputStream asnOS = new AsnOutputStream();
        callHoldData.encodeAll(asnOS);
        byte[] raw = asnOS.toByteArray();
        assertTrue(Arrays.equals(raw, data));
    }
}
