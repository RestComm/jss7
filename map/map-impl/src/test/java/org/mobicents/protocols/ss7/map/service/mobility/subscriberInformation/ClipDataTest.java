package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.supplementary.OverrideCategory;
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
public class ClipDataTest {
    private byte[] data = {48, 8, -127, 1, 0, -126, 1, 0, -125, 0};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
        AsnInputStream asn = new AsnInputStream(data);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ClipDataImpl clipData = new ClipDataImpl();
        clipData.decodeAll(asn);

        assertNotNull(clipData.getSsStatus());
        assertFalse(clipData.getSsStatus().getBitA());
        assertFalse(clipData.getSsStatus().getBitP());
        assertFalse(clipData.getSsStatus().getBitR());
        assertFalse(clipData.getSsStatus().getBitQ());
        assertEquals(clipData.getOverrideCategory(), OverrideCategory.overrideEnabled);
        assertTrue(clipData.getNotificationToCSE());
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
        ClipDataImpl clipData = new ClipDataImpl(new ExtSSStatusImpl(false, false, false, false), OverrideCategory.overrideEnabled, true);

        AsnOutputStream asnOS = new AsnOutputStream();
        clipData.encodeAll(asnOS);
        byte[] raw = asnOS.toByteArray();
        assertTrue(Arrays.equals(raw, data));
    }
}
