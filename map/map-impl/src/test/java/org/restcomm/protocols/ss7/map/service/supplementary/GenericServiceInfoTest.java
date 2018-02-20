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

package org.restcomm.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CCBSFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.restcomm.protocols.ss7.map.service.supplementary.CCBSFeatureImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.GenericServiceInfoImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSStatusImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class GenericServiceInfoTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 3, 4, 1, 2 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 28, 4, 1, 2, 10, 1, 2, (byte) 128, 1, 3, (byte) 129, 1, 4, (byte) 162, 5, 48, 3, (byte) 128, 1, 2, (byte) 131, 1, 11,
                (byte) 132, 1, 12, (byte) 133, 1, 13 };
    }

    @Test(groups = { "functional.decode", "service.supplementary" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        GenericServiceInfoImpl impl = new GenericServiceInfoImpl();
        impl.decodeAll(asn);

        assertFalse(impl.getSsStatus().getABit());
        assertFalse(impl.getSsStatus().getPBit());
        assertFalse(impl.getSsStatus().getQBit());
        assertTrue(impl.getSsStatus().getRBit());

        assertNull(impl.getCliRestrictionOption());
        assertNull(impl.getMaximumEntitledPriority());
        assertNull(impl.getDefaultPriority());
        assertNull(impl.getCcbsFeatureList());
        assertNull(impl.getNbrSB());
        assertNull(impl.getNbrUser());
        assertNull(impl.getNbrSN());


        rawData = getEncodedData2();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        impl = new GenericServiceInfoImpl();
        impl.decodeAll(asn);

        assertFalse(impl.getSsStatus().getABit());
        assertFalse(impl.getSsStatus().getPBit());
        assertFalse(impl.getSsStatus().getQBit());
        assertTrue(impl.getSsStatus().getRBit());

        assertEquals(impl.getCliRestrictionOption(), CliRestrictionOption.temporaryDefaultAllowed);
        assertEquals(impl.getMaximumEntitledPriority(), EMLPPPriority.priorityLevel3);
        assertEquals(impl.getDefaultPriority(), EMLPPPriority.priorityLevel4);
        assertEquals(impl.getCcbsFeatureList().size(), 1);
        assertEquals((int) impl.getCcbsFeatureList().get(0).getCcbsIndex(), 2);
        assertEquals((int) impl.getNbrSB(), 11);
        assertEquals((int) impl.getNbrUser(), 12);
        assertEquals((int) impl.getNbrSN(), 13);
    }

    @Test(groups = { "functional.encode", "service.supplementary" })
    public void testEncode() throws Exception {

        SSStatus ssStatus = new SSStatusImpl(false, false, true, false);
        GenericServiceInfoImpl impl = new GenericServiceInfoImpl(ssStatus, null, null, null, null, null, null, null);
//        SSStatus ssStatus, CliRestrictionOption cliRestrictionOption, EMLPPPriority maximumEntitledPriority,
//        EMLPPPriority defaultPriority, ArrayList<CCBSFeature> ccbsFeatureList, Integer nbrSB, Integer nbrUser, Integer nbrSN

        AsnOutputStream asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        ArrayList<CCBSFeature> ccbsFeatureList = new ArrayList<CCBSFeature>();
        CCBSFeature ccbs = new CCBSFeatureImpl(2, null, null, null);
        ccbsFeatureList.add(ccbs);
        impl = new GenericServiceInfoImpl(ssStatus, CliRestrictionOption.temporaryDefaultAllowed, EMLPPPriority.priorityLevel3, EMLPPPriority.priorityLevel4,
                ccbsFeatureList, 11, 12, 13);

        asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertEquals(rawData, encodedData);
    }

}
