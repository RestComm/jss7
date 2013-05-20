/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ServingNodeAddressTest {

    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

    public byte[] getDataMsc() {
        return new byte[] { -128, 5, -111, 49, 117, 9, 0 };
    }

    public byte[] getDataSgsn() {
        return new byte[] { -127, 5, -111, 49, 117, 9, 0 };
    }

    public byte[] getDataMme() {
        return new byte[] { -126, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
    }

    public byte[] getDataDiameterIdentity() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
        byte[] data = getDataMsc();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, ServingNodeAddressImpl._TAG_mscNumber);

        ServingNodeAddressImpl impl = new ServingNodeAddressImpl();
        impl.decodeAll(asn);

        ISDNAddressString isdnAdd = impl.getMscNumber();
        assertEquals(isdnAdd.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(isdnAdd.getAddress().equals("13579000"));
        assertNull(impl.getSgsnNumber());
        assertNull(impl.getMmeNumber());

        data = getDataSgsn();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, ServingNodeAddressImpl._TAG_sgsnNumber);

        impl = new ServingNodeAddressImpl();
        impl.decodeAll(asn);

        isdnAdd = impl.getSgsnNumber();
        assertEquals(isdnAdd.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(isdnAdd.getAddress().equals("13579000"));
        assertNull(impl.getMscNumber());
        assertNull(impl.getMmeNumber());

        data = getDataMme();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, ServingNodeAddressImpl._TAG_mmeNumber);

        impl = new ServingNodeAddressImpl();
        impl.decodeAll(asn);

        DiameterIdentity di = impl.getMmeNumber();
        assertTrue(Arrays.equals(di.getData(), getDataDiameterIdentity()));
        assertNull(impl.getMscNumber());
        assertNull(impl.getSgsnNumber());
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
        byte[] data = getDataMsc();

        ISDNAddressString isdnAdd = MAPParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "13579000");
        ServingNodeAddressImpl impl = new ServingNodeAddressImpl(isdnAdd, true);

        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));

        data = getDataSgsn();

        impl = new ServingNodeAddressImpl(isdnAdd, false);

        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));

        data = getDataMme();

        DiameterIdentity di = new DiameterIdentityImpl(getDataDiameterIdentity());
        impl = new ServingNodeAddressImpl(di);

        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));
    }
}
