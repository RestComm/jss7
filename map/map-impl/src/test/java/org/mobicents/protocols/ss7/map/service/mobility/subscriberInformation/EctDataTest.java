package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
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
public class EctDataTest {
    private byte[] data = {48, 5, -127, 1, 14, -126, 0};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
        AsnInputStream asn = new AsnInputStream(data);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        EctDataImpl ectData = new EctDataImpl();
        ectData.decodeAll(asn);

        assertNotNull(ectData.getSsStatus());
        assertTrue(ectData.getSsStatus().getBitQ());
        assertTrue(ectData.getSsStatus().getBitP());
        assertTrue(ectData.getSsStatus().getBitR());
        assertFalse(ectData.getSsStatus().getBitA());
        assertTrue(ectData.getNotificationToCSE());
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
        EctDataImpl ectData = new EctDataImpl(new ExtSSStatusImpl(true, true, true, false), true);

        AsnOutputStream asnOS = new AsnOutputStream();
        ectData.encodeAll(asnOS);
        byte[] raw = asnOS.toByteArray();
        assertTrue(Arrays.equals(raw, data));
    }
}
