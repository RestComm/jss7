/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.mobicents.protocols.ss7.cap.service.sms.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.TPValidityPeriodImpl;
import org.mobicents.protocols.ss7.map.api.smstpdu.AbsoluteTimeStamp;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityPeriod;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityPeriodFormat;
import org.mobicents.protocols.ss7.map.smstpdu.AbsoluteTimeStampImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class TPValidityPeriodTest {

    public byte[] getData() {
        return new byte[] { 4, 1, 4 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 7, 49, 48, 16, 17, 2, 69, 0 };
    };
	
	public byte[] getTPValidityPeriod() {
		return new byte[] { 4 };
	};
	
	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		byte[] data = this.getData();
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		TPValidityPeriodImpl prim = new TPValidityPeriodImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, Tag.STRING_OCTET);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
		
        assertTrue(Arrays.equals(prim.getData(), getTPValidityPeriod()));
        ValidityPeriod vp = prim.getValidityPeriod();
        assertEquals(vp.getValidityPeriodFormat(), ValidityPeriodFormat.fieldPresentRelativeFormat);
        assertEquals((int)vp.getRelativeFormatValue(), 4);


        data = this.getData2();
        asn = new AsnInputStream(data);
        asn.readTag();
        prim = new TPValidityPeriodImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        vp = prim.getValidityPeriod();
        assertEquals(vp.getValidityPeriodFormat(), ValidityPeriodFormat.fieldPresentAbsoluteFormat);
        AbsoluteTimeStamp afv = vp.getAbsoluteFormatValue();
        assertEquals(afv.getYear(), 13);
        assertEquals(afv.getMonth(), 3);
        assertEquals(afv.getDay(), 1);
        assertEquals(afv.getHour(), 11);
        assertEquals(afv.getMinute(), 20);
        assertEquals(afv.getSecond(), 54);
	}

	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {
        TPValidityPeriodImpl prim = new TPValidityPeriodImpl(4);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));


        AbsoluteTimeStamp absoluteFormatValue = new AbsoluteTimeStampImpl(13, 3, 1, 11, 20, 54, 0);
        prim = new TPValidityPeriodImpl(absoluteFormatValue);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));
	}

}
