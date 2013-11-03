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

package org.mobicents.protocols.ss7.tcapAnsi.asn;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.Confidentiality;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ProtocolVersion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.SecurityContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
@Test(groups = { "asn" })
public class DialogPortionTest {

    private byte[] data1 = new byte[] { -7, 33, -38, 1, 3, -3, 20, 40, 18, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96, 7, 1, 2, 3, 4, 5, 6, 7, -128, 1, 10, -94, 3, -128,
            1, 20 };

    private byte[] data2 = new byte[] { -7, 11, -37, 1, 30, -127, 1, 42, -94, 3, -127, 1, 44 };    

    private byte[] dataValue = new byte[] { 1, 2, 3, 4, 5, 6, 7 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {

        // 1
        AsnInputStream ais = new AsnInputStream(this.data1);
        int tag = ais.readTag();
        assertEquals(tag, DialogPortion._TAG_DIALOG_PORTION);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        DialogPortion dp = TcapFactory.createDialogPortion();
        dp.decode(ais);

        assertNull(dp.getApplicationContext());
        assertTrue(dp.getProtocolVersion().isT1_114_2000Supported());
        UserInformation ui = dp.getUserInformation();
        assertTrue(Arrays.equals(new long[] { 0, 4, 0, 0, 1, 1, 1, 1 }, ui.getUserInformationElements()[0].getOidValue()));
        assertEquals(dataValue, ui.getUserInformationElements()[0].getEncodeType());

        SecurityContext sc = dp.getSecurityContext();
        assertEquals((long) sc.getIntegerSecurityId(), 10);
        Confidentiality con = dp.getConfidentiality();
        assertEquals((long) con.getIntegerConfidentialityId(), 20);

        // 2
        ais = new AsnInputStream(this.data2);
        tag = ais.readTag();
        assertEquals(tag, DialogPortion._TAG_DIALOG_PORTION);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        dp = TcapFactory.createDialogPortion();
        dp.decode(ais);

        assertNull(dp.getProtocolVersion());
        assertEquals(dp.getApplicationContext().getInteger(), 30);
        assertNull(dp.getUserInformation());

        sc = dp.getSecurityContext();
        assertTrue(Arrays.equals(sc.getObjectSecurityId(), new long[] { 1, 2 }));
        con = dp.getConfidentiality();
        assertTrue(Arrays.equals(con.getObjectConfidentialityId(), new long[] { 1, 4 }));
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        // 1
        DialogPortion dp = TcapFactory.createDialogPortion();
        ProtocolVersion pv = TcapFactory.createProtocolVersionFull();
        dp.setProtocolVersion(pv);
        UserInformation ui = TcapFactory.createUserInformation();
        UserInformationElement[] uie = new UserInformationElement[1];
        uie[0] = new UserInformationElementImpl();
        uie[0].setOid(true);
        uie[0].setOidValue(new long[] { 0, 4, 0, 0, 1, 1, 1, 1 });
        uie[0].setAsn(true);
        uie[0].setEncodeType(dataValue);
        ui.setUserInformationElements(uie);
        dp.setUserInformation(ui);
        SecurityContext sc = new SecurityContextImpl();
        sc.setIntegerSecurityId(10L);
        dp.setSecurityContext(sc);
        Confidentiality con = new ConfidentialityImpl();
        con.setIntegerConfidentialityId(20L);
        dp.setConfidentiality(con);

        AsnOutputStream aos = new AsnOutputStream();
        dp.encode(aos);
        byte[] encodedData = aos.toByteArray();
        byte[] expectedData = data1;
        assertEquals(encodedData, expectedData);

        // 2
        dp = TcapFactory.createDialogPortion();
        ApplicationContext ac = TcapFactory.createApplicationContext(30);
        dp.setApplicationContext(ac);
        sc = new SecurityContextImpl();
        sc.setObjectSecurityId(new long[] { 1, 2 });
        dp.setSecurityContext(sc);
        con = new ConfidentialityImpl();
        con.setObjectConfidentialityId(new long[] { 1, 4 });
        dp.setConfidentiality(con);

        aos = new AsnOutputStream();
        dp.encode(aos);
        encodedData = aos.toByteArray();
        expectedData = data2;
        assertEquals(encodedData, expectedData);

    }

}
