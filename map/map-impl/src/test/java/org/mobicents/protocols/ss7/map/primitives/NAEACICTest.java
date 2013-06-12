package org.mobicents.protocols.ss7.map.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.NetworkIdentificationPlanValue;
import org.mobicents.protocols.ss7.map.api.primitives.NetworkIdentificationTypeValue;
import org.testng.annotations.Test;

public class NAEACICTest {

    public byte[] getData() {
        return new byte[] { 4, 3, 34, 33, 67 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 3, 33, 33, 3 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        NAEACICImpl prim = new NAEACICImpl();
        prim.decodeAll(asn);
        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertTrue(prim.getCarrierCode().equals("1234"));
        assertEquals(prim.getNetworkIdentificationPlanValue(), NetworkIdentificationPlanValue.fourDigitCarrierIdentification);
        assertEquals(prim.getNetworkIdentificationTypeValue(), NetworkIdentificationTypeValue.nationalNetworkIdentification);

        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new NAEACICImpl();
        prim.decodeAll(asn);
        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertTrue(prim.getCarrierCode().equals("123"));
        assertEquals(prim.getNetworkIdentificationPlanValue(), NetworkIdentificationPlanValue.threeDigitCarrierIdentification);
        assertEquals(prim.getNetworkIdentificationTypeValue(), NetworkIdentificationTypeValue.nationalNetworkIdentification);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        // option 1
        NAEACICImpl prim = new NAEACICImpl("1234", NetworkIdentificationPlanValue.fourDigitCarrierIdentification,
                NetworkIdentificationTypeValue.nationalNetworkIdentification);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

        // option 2
        prim = new NAEACICImpl("123", NetworkIdentificationPlanValue.threeDigitCarrierIdentification,
                NetworkIdentificationTypeValue.nationalNetworkIdentification);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));
    }

}
