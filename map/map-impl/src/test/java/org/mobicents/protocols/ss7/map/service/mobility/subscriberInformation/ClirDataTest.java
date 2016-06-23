package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
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
public class ClirDataTest {
    private byte[] data = {48, 8, -127, 1, 10, -126, 1, 0, -125, 0};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
        AsnInputStream asn = new AsnInputStream(data);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ClirDataImpl clirData = new ClirDataImpl();
        clirData.decodeAll(asn);

        assertNotNull(clirData.getSsStatus());
        assertTrue(clirData.getSsStatus().getBitQ());
        assertFalse(clirData.getSsStatus().getBitP());
        assertTrue(clirData.getSsStatus().getBitR());
        assertFalse(clirData.getSsStatus().getBitA());
        assertEquals(clirData.getCliRestrictionOption(), CliRestrictionOption.permanent);
        assertTrue(clirData.getNotificationToCSE());
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
        ClirDataImpl clirData = new ClirDataImpl(new ExtSSStatusImpl(true, false, true, false), CliRestrictionOption.permanent, true);

        AsnOutputStream asnOS = new AsnOutputStream();
        clirData.encodeAll(asnOS);
        byte[] raw = asnOS.toByteArray();
        assertTrue(Arrays.equals(raw, data));
    }
}
