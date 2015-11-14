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

package org.mobicents.protocols.ss7.map.service.oam;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.GlobalCellId;
import org.mobicents.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.EUtranCgi;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TAId;
import org.mobicents.protocols.ss7.map.primitives.GlobalCellIdImpl;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.EUtranCgiImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.RAIdentityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.TAIdImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class AreaScopeTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 11, (byte) 160, 9, 4, 7, 82, (byte) 240, 112, 69, (byte) 224, 87, (byte) 172 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 91, (byte) 160, 9, 4, 7, 82, (byte) 240, 112, 69, (byte) 224, 87, (byte) 172, (byte) 161, 9, 4, 7, 1, 2, 3, 4, 5, 6, 7,
                (byte) 162, 8, 4, 6, 11, 12, 13, 14, 15, 16, (byte) 163, 7, 4, 5, 81, (byte) 240, 17, 13, 5, (byte) 164, 7, 4, 5, 21, 22, 23, 24, 25,
                (byte) 165, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, (byte) 161, 3, 31, 32, 33 };
    }

    private byte[] getEUtranCgiData() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7 };
    }

    private byte[] getRAIdentity() {
        return new byte[] { 11, 12, 13, 14, 15, 16 };
    }

    private byte[] getTAId() {
        return new byte[] { 21, 22, 23, 24, 25 };
    }

    @Test(groups = { "functional.decode", "service.oam" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        AreaScopeImpl asc = new AreaScopeImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(asc.getCgiList().size(), 1);
        assertEquals(asc.getCgiList().get(0).getMcc(), 250);
        assertEquals(asc.getCgiList().get(0).getMnc(), 7);
        assertEquals(asc.getCgiList().get(0).getCellId(), 22444);

        assertNull(asc.getEUutranCgiList());
        assertNull(asc.getRoutingAreaIdList());
        assertNull(asc.getLocationAreaIdList());
        assertNull(asc.getTrackingAreaIdList());
        assertNull(asc.getExtensionContainer());


        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new AreaScopeImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(asc.getCgiList().size(), 1);
        assertEquals(asc.getCgiList().get(0).getMcc(), 250);
        assertEquals(asc.getCgiList().get(0).getMnc(), 7);
        assertEquals(asc.getCgiList().get(0).getCellId(), 22444);

        assertEquals(asc.getEUutranCgiList().size(), 1);
        assertEquals(asc.getEUutranCgiList().get(0).getData(), getEUtranCgiData());

        assertEquals(asc.getRoutingAreaIdList().size(), 1);
        assertEquals(asc.getRoutingAreaIdList().get(0).getData(), getRAIdentity());

        assertEquals(asc.getLocationAreaIdList().size(), 1);
        assertEquals(asc.getLocationAreaIdList().get(0).getMCC(), 150);
        assertEquals(asc.getLocationAreaIdList().get(0).getMNC(), 11);
        assertEquals(asc.getLocationAreaIdList().get(0).getLac(), 3333);

        assertEquals(asc.getTrackingAreaIdList().size(), 1);
        assertEquals(asc.getTrackingAreaIdList().get(0).getData(), getTAId());

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode", "service.oam" })
    public void testEncode() throws Exception {

        ArrayList<GlobalCellId> cgiList = new ArrayList<GlobalCellId>();
        GlobalCellId gci = new GlobalCellIdImpl(250, 7, 17888, 22444); // int mcc, int mnc, int lac, int cellId
        cgiList.add(gci);
        AreaScopeImpl asc = new AreaScopeImpl(cgiList, null, null, null, null, null);
//        ArrayList<GlobalCellId> cgiList, ArrayList<EUtranCgi> eUtranCgiList, ArrayList<RAIdentity> routingAreaIdList,
//        ArrayList<LAIFixedLength> locationAreaIdList, ArrayList<TAId> trackingAreaIdList, MAPExtensionContainer extensionContainer

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        ArrayList<EUtranCgi> eUtranCgiList = new ArrayList<EUtranCgi>();
        EUtranCgi eUtranCgi = new EUtranCgiImpl(getEUtranCgiData());
        eUtranCgiList.add(eUtranCgi);
        ArrayList<RAIdentity> routingAreaIdList = new ArrayList<RAIdentity>();
        RAIdentity raIdentity = new RAIdentityImpl(getRAIdentity());
        routingAreaIdList.add(raIdentity);
        ArrayList<LAIFixedLength> locationAreaIdList = new ArrayList<LAIFixedLength>();
        LAIFixedLength laiFixedLength = new LAIFixedLengthImpl(150, 11, 3333); // int mcc, int mnc, int lac
        locationAreaIdList.add(laiFixedLength);
        ArrayList<TAId> trackingAreaIdList = new ArrayList<TAId>();
        TAId taId = new TAIdImpl(getTAId());
        trackingAreaIdList.add(taId);
        asc = new AreaScopeImpl(cgiList, eUtranCgiList, routingAreaIdList, locationAreaIdList, trackingAreaIdList,
                MAPExtensionContainerTest.GetTestExtensionContainer());

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
