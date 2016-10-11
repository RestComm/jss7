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

package org.mobicents.protocols.ss7.map.smstpdu;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Map;

import org.mobicents.protocols.ss7.map.api.datacoding.NationalLanguageIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.ConcatenatedShortMessagesIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.NationalLanguageLockingShiftIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.NationalLanguageSingleShiftIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserDataHeaderElement;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class UserDataHeaderTest {

    public byte[] getData1() {
        return new byte[] { 6, 8, 4, 0, -47, 3, 2 };
    }

    public byte[] getData11() {
        return new byte[] { 6, 8, 4, 10, 1, 3, 2 };
    }

    public byte[] getData2() {
        return new byte[] { 5, 0, 3, -116, 2, 1 };
    }

    public byte[] getDataA1() {
        return new byte[] { 3, 37, 1, 2 };
    }

    public byte[] getDataA2() {
        return new byte[] { 3, 36, 1, 3 };
    }

    public byte[] getData3() {
        return new byte[] { 4, 13, 2, 20, 6 };
    }

    public byte[] getData4() {
        return new byte[] { 40, 0, 3, 40, 3, 2, 17, 33, 48, -1, -1, -128, 49, -128, 49, -113, -15, -97, -7, -104, 25, -104, 25,
                -104, 25, -97, -7, -97, -7, -103, -103, -97, -7, -97, -7, -103, -103, -97, -7, -1, -1 };
    }

    public byte[] getData5() {
        return new byte[] { 9, 0, 3, 40, 3, 3, 11, 2, 0, 0 };
    }

    public byte[] getData6() {
        return new byte[] { 9, 10, 3, 0, 32, 4, 11, 2, 32, 5 };
    }

    public byte[] getData7() {
        return new byte[] { 6, 5, 4, 35, -12, 0, 0 };
    }

    public byte[] getData8() {
        return new byte[] { 2, 112, 0 };
    }

    @Test(groups = { "functional.decode", "smstpdu" })
    public void testDecode() throws Exception {

        UserDataHeaderImpl impl = new UserDataHeaderImpl(this.getData1());
        Map<Integer, byte[]> mp = impl.getAllData();
        assertEquals(impl.getAllData().size(), 1);
        assertTrue(Arrays.equals(mp.get(8), new byte[] { 0, -47, 3, 2 }));
        ConcatenatedShortMessagesIdentifier conc = impl.getConcatenatedShortMessagesIdentifier();
        assertNotNull(conc);
        assertEquals(conc.getReferenceIs16bit(), true);
        assertEquals(conc.getReference(), 53504);
        assertEquals(conc.getMesageSegmentCount(), 3);
        assertEquals(conc.getMesageSegmentNumber(), 2);

        impl = new UserDataHeaderImpl(this.getData11());
        mp = impl.getAllData();
        assertEquals(impl.getAllData().size(), 1);
        assertTrue(Arrays.equals(mp.get(8), new byte[] { 10, 1, 3, 2 }));
        conc = impl.getConcatenatedShortMessagesIdentifier();
        assertNotNull(conc);
        assertEquals(conc.getReferenceIs16bit(), true);
        assertEquals(conc.getReference(), 266);
        assertEquals(conc.getMesageSegmentCount(), 3);
        assertEquals(conc.getMesageSegmentNumber(), 2);

        impl = new UserDataHeaderImpl(this.getData2());
        mp = impl.getAllData();
        assertEquals(impl.getAllData().size(), 1);
        assertTrue(Arrays.equals(mp.get(0), new byte[] { -116, 2, 1 }));
        conc = impl.getConcatenatedShortMessagesIdentifier();
        assertNotNull(conc);
        assertEquals(conc.getReferenceIs16bit(), false);
        assertEquals(conc.getReference(), 140);
        assertEquals(conc.getMesageSegmentCount(), 2);
        assertEquals(conc.getMesageSegmentNumber(), 1);

        impl = new UserDataHeaderImpl(this.getDataA1());
        mp = impl.getAllData();
        assertEquals(impl.getAllData().size(), 1);
        assertTrue(Arrays.equals(mp.get(37), new byte[] { 2 }));
        NationalLanguageLockingShiftIdentifier nls = impl.getNationalLanguageLockingShift();
        assertNotNull(nls);
        assertEquals(nls.getNationalLanguageIdentifier(), NationalLanguageIdentifier.Spanish);

        impl = new UserDataHeaderImpl(this.getDataA2());
        mp = impl.getAllData();
        assertEquals(impl.getAllData().size(), 1);
        assertTrue(Arrays.equals(mp.get(36), new byte[] { 3 }));
        NationalLanguageSingleShiftIdentifier nss = impl.getNationalLanguageSingleShift();
        assertNotNull(nss);
        assertEquals(nss.getNationalLanguageIdentifier(), NationalLanguageIdentifier.Portuguese);

        // TODO: implement getData3()-getData7() decoding

        impl = new UserDataHeaderImpl(this.getData8());
        mp = impl.getAllData();
        assertEquals(impl.getAllData().size(), 1);
        assertTrue(Arrays.equals(mp.get(112), new byte[] { }));
    }

    @Test(groups = { "functional.encode", "smstpdu" })
    public void testEncode() throws Exception {

        UserDataHeaderImpl impl = new UserDataHeaderImpl();
        UserDataHeaderElement ie = new ConcatenatedShortMessagesIdentifierImpl(true, 53504, 3, 2);
        impl.addInformationElement(ie);
        assertTrue(Arrays.equals(impl.getEncodedData(), this.getData1()));

        impl = new UserDataHeaderImpl();
        ie = new ConcatenatedShortMessagesIdentifierImpl(true, 266, 3, 2);
        impl.addInformationElement(ie);
        assertTrue(Arrays.equals(impl.getEncodedData(), this.getData11()));

        impl = new UserDataHeaderImpl();
        ie = new ConcatenatedShortMessagesIdentifierImpl(false, 140, 2, 1);
        impl.addInformationElement(ie);
        assertTrue(Arrays.equals(impl.getEncodedData(), this.getData2()));

        impl = new UserDataHeaderImpl();
        ie = new NationalLanguageLockingShiftIdentifierImpl(NationalLanguageIdentifier.Spanish);
        impl.addInformationElement(ie);
        assertTrue(Arrays.equals(impl.getEncodedData(), this.getDataA1()));

        impl = new UserDataHeaderImpl();
        ie = new NationalLanguageSingleShiftIdentifierImpl(NationalLanguageIdentifier.Portuguese);
        impl.addInformationElement(ie);
        assertTrue(Arrays.equals(impl.getEncodedData(), this.getDataA2()));

        // TODO: implement getData3()-getData7() encoding

        impl = new UserDataHeaderImpl();
        ie = new ConcatenatedShortMessagesIdentifierImpl(false, 140, 2, 1);
        impl.addInformationElement(112, new byte[] {});
        assertTrue(Arrays.equals(impl.getEncodedData(), this.getData8()));
    }
}
