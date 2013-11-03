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

package org.mobicents.protocols.ss7.map.dialog;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.Utils;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.mobicents.protocols.ss7.map.api.dialog.ResourceUnavailableReason;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPUserAbortInfoTest {

    // TODO: to Amit: please check this commented tests. They contradict the specification and my live traces
    //
    // @Test(groups = { "functional.decode","dialog"})
    // public void testProcedureCancellationReasonDecode() throws Exception {
    //
    // // The raw data is hand made
    // byte[] data = new byte[] { (byte) 0xa4, 0x05, 0x03, 0x03, 0x0a, 0x01,
    // 0x04 };
    //
    // ByteArrayInputStream baIs = new ByteArrayInputStream(data);
    // AsnInputStream asnIs = new AsnInputStream(baIs);
    //
    // int tag = asnIs.readTag();
    //
    // MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
    // mapUserAbortInfo.decode(asnIs);
    //
    // MAPUserAbortChoice mapUserAbortChoice = mapUserAbortInfo
    // .getMAPUserAbortChoice();
    //
    // assertNotNull(mapUserAbortChoice);
    //
    // assertFalse(mapUserAbortChoice.isUserSpecificReason());
    // assertTrue(mapUserAbortChoice.isProcedureCancellationReason());
    // assertFalse(mapUserAbortChoice.isResourceUnavailableReason());
    // assertFalse(mapUserAbortChoice.isUserResourceLimitation());
    //
    // ProcedureCancellationReason procdCancellReasn = mapUserAbortChoice
    // .getProcedureCancellationReason();
    //
    // assertNotNull(procdCancellReasn);
    //
    // assertEquals( ProcedureCancellationReason.associatedProcedureFailure,procdCancellReasn);
    //
    // }
    //
    // @Test(groups = { "functional.encode","dialog"})
    // public void testResourceUnavailableEncode() throws Exception {
    // MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
    //
    // MAPUserAbortChoiceImpl mapUserAbortChoice = new MAPUserAbortChoiceImpl();
    // mapUserAbortChoice
    // .setResourceUnavailableReason(ResourceUnavailableReason.longTermResourceLimitation);
    //
    // mapUserAbortInfo.setMAPUserAbortChoice(mapUserAbortChoice);
    //
    // AsnOutputStream asnOS = new AsnOutputStream();
    //
    // mapUserAbortInfo.encode(asnOS);
    //
    // byte[] data = asnOS.toByteArray();
    //
    // System.out.println(Utils.dump(data, data.length, false));
    //
    // assertTrue( (byte, 0x05,Arrays.equals(new byte[] { (byte) 0xA4) 0x02,
    // 0x03, 0x0A, 0x01, 0x01 }, data));
    // }
    //
    // @Test(groups = { "functional.encode","dialog"})
    // public void testProcedureCancellationReasonEncode() throws Exception {
    // MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
    //
    // MAPUserAbortChoiceImpl mapUserAbortChoice = new MAPUserAbortChoiceImpl();
    // mapUserAbortChoice
    // .setProcedureCancellationReason(ProcedureCancellationReason.associatedProcedureFailure);
    //
    // mapUserAbortInfo.setMAPUserAbortChoice(mapUserAbortChoice);
    //
    // AsnOutputStream asnOS = new AsnOutputStream();
    //
    // mapUserAbortInfo.encode(asnOS);
    //
    // byte[] data = asnOS.toByteArray();
    //
    // System.out.println(Utils.dump(data, data.length, false));
    //
    // assertTrue( (byte, 0x05,Arrays.equals(new byte[] { (byte) 0xA4) 0x03,
    // 0x03, 0x0A, 0x01, 0x04 }, data));
    // }

    private byte[] getDataUserSpecificReason() {
        return new byte[] { (byte) 164, 2, (byte) 128, 0 };
    }

    private byte[] getUserResourceLimitationReason() {
        return new byte[] { (byte) 164, 2, (byte) 129, 0 };
    }

    private byte[] getResourceUnavailableReason() {
        return new byte[] { (byte) 164, 3, (byte) 130, 1, 0 };
    }

    private byte[] getProcedureCancellationReason() {
        return new byte[] { (byte) 164, 3, (byte) 131, 1, 4 };
        // return new byte[] { (byte) 0xA4, 0x05, (byte) 0x03, 0x03, 0x0A, 0x01, 0x04 };
    }

    private byte[] getDataFull() {
        return new byte[] { -92, 44, -125, 1, 3, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
                3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    private byte[] getBadlyEncodedFromLiveTrace() {
        return new byte[] { -92, 1, -128 };
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testUserSpecificReasonDecode() throws Exception {

        // The raw data is hand made
        byte[] data = getDataUserSpecificReason();
        AsnInputStream asnIs = new AsnInputStream(data);

        int tag = asnIs.readTag();
        assertEquals(tag, 4);

        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
        mapUserAbortInfo.decodeAll(asnIs);

        MAPUserAbortChoice mapUserAbortChoice = mapUserAbortInfo.getMAPUserAbortChoice();

        assertNotNull(mapUserAbortChoice);

        assertTrue(mapUserAbortChoice.isUserSpecificReason());
        assertFalse(mapUserAbortChoice.isProcedureCancellationReason());
        assertFalse(mapUserAbortChoice.isResourceUnavailableReason());
        assertFalse(mapUserAbortChoice.isUserResourceLimitation());

    }

    @Test(groups = { "functional.encode", "dialog" })
    public void testUserSpecificReasonEncode() throws Exception {
        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
        MAPUserAbortChoiceImpl mapUserAbortChoice = new MAPUserAbortChoiceImpl();
        mapUserAbortChoice.setUserSpecificReason();

        mapUserAbortInfo.setMAPUserAbortChoice(mapUserAbortChoice);

        AsnOutputStream asnOS = new AsnOutputStream();

        mapUserAbortInfo.encodeAll(asnOS);

        byte[] data = asnOS.toByteArray();

        System.out.println(Utils.dump(data, data.length, false));

        assertTrue(Arrays.equals(getDataUserSpecificReason(), data));
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testUserResourceLimitationDecode() throws Exception {

        // The raw data is hand made
        byte[] data = getUserResourceLimitationReason();
        AsnInputStream asnIs = new AsnInputStream(data);

        int tag = asnIs.readTag();
        assertEquals(tag, 4);

        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
        mapUserAbortInfo.decodeAll(asnIs);

        MAPUserAbortChoice mapUserAbortChoice = mapUserAbortInfo.getMAPUserAbortChoice();

        assertNotNull(mapUserAbortChoice);

        assertFalse(mapUserAbortChoice.isUserSpecificReason());
        assertFalse(mapUserAbortChoice.isProcedureCancellationReason());
        assertFalse(mapUserAbortChoice.isResourceUnavailableReason());
        assertTrue(mapUserAbortChoice.isUserResourceLimitation());

    }

    @Test(groups = { "functional.encode", "dialog" })
    public void testUserResourceLimitationEncode() throws Exception {
        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
        MAPUserAbortChoiceImpl mapUserAbortChoice = new MAPUserAbortChoiceImpl();
        mapUserAbortChoice.setUserResourceLimitation();

        mapUserAbortInfo.setMAPUserAbortChoice(mapUserAbortChoice);

        AsnOutputStream asnOS = new AsnOutputStream();

        mapUserAbortInfo.encodeAll(asnOS);

        byte[] data = asnOS.toByteArray();

        System.out.println(Utils.dump(data, data.length, false));

        assertTrue(Arrays.equals(getUserResourceLimitationReason(), data));
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testResourceUnavailableDecode() throws Exception {

        // The raw data is hand made
        byte[] data = getResourceUnavailableReason();
        AsnInputStream asnIs = new AsnInputStream(data);

        int tag = asnIs.readTag();
        assertEquals(tag, 4);

        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
        mapUserAbortInfo.decodeAll(asnIs);

        MAPUserAbortChoice mapUserAbortChoice = mapUserAbortInfo.getMAPUserAbortChoice();

        assertNotNull(mapUserAbortChoice);

        assertFalse(mapUserAbortChoice.isUserSpecificReason());
        assertFalse(mapUserAbortChoice.isProcedureCancellationReason());
        assertTrue(mapUserAbortChoice.isResourceUnavailableReason());
        assertFalse(mapUserAbortChoice.isUserResourceLimitation());
        assertEquals(mapUserAbortChoice.getResourceUnavailableReason(), ResourceUnavailableReason.shortTermResourceLimitation);

    }

    @Test(groups = { "functional.encode", "dialog" })
    public void testResourceUnavailableEncode() throws Exception {
        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();

        MAPUserAbortChoiceImpl mapUserAbortChoice = new MAPUserAbortChoiceImpl();
        mapUserAbortChoice.setResourceUnavailableReason(ResourceUnavailableReason.shortTermResourceLimitation);

        mapUserAbortInfo.setMAPUserAbortChoice(mapUserAbortChoice);

        AsnOutputStream asnOS = new AsnOutputStream();

        mapUserAbortInfo.encodeAll(asnOS);

        byte[] data = asnOS.toByteArray();

        System.out.println(Utils.dump(data, data.length, false));

        assertTrue(Arrays.equals(getResourceUnavailableReason(), data));
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testProcedureCancellationReasonDecode() throws Exception {

        // The raw data is hand made
        byte[] data = getProcedureCancellationReason();

        AsnInputStream asnIs = new AsnInputStream(data);

        int tag = asnIs.readTag();
        assertEquals(tag, 4);

        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
        mapUserAbortInfo.decodeAll(asnIs);

        MAPUserAbortChoice mapUserAbortChoice = mapUserAbortInfo.getMAPUserAbortChoice();

        assertNotNull(mapUserAbortChoice);

        assertFalse(mapUserAbortChoice.isUserSpecificReason());
        assertTrue(mapUserAbortChoice.isProcedureCancellationReason());
        assertFalse(mapUserAbortChoice.isResourceUnavailableReason());
        assertFalse(mapUserAbortChoice.isUserResourceLimitation());

        ProcedureCancellationReason procdCancellReasn = mapUserAbortChoice.getProcedureCancellationReason();

        assertNotNull(procdCancellReasn);

        assertEquals(ProcedureCancellationReason.associatedProcedureFailure, procdCancellReasn);

    }

    @Test(groups = { "functional.encode", "dialog" })
    public void testProcedureCancellationReasonEncode() throws Exception {
        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();

        MAPUserAbortChoiceImpl mapUserAbortChoice = new MAPUserAbortChoiceImpl();
        mapUserAbortChoice.setProcedureCancellationReason(ProcedureCancellationReason.associatedProcedureFailure);

        mapUserAbortInfo.setMAPUserAbortChoice(mapUserAbortChoice);

        AsnOutputStream asnOS = new AsnOutputStream();

        mapUserAbortInfo.encodeAll(asnOS);

        byte[] data = asnOS.toByteArray();

        System.out.println(Utils.dump(data, data.length, false));

        assertTrue(Arrays.equals(getProcedureCancellationReason(), data));
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testFullDecode() throws Exception {

        // The raw data is hand made
        byte[] data = getDataFull();

        AsnInputStream asnIs = new AsnInputStream(data);

        int tag = asnIs.readTag();
        assertEquals(tag, 4);

        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
        mapUserAbortInfo.decodeAll(asnIs);

        MAPUserAbortChoice mapUserAbortChoice = mapUserAbortInfo.getMAPUserAbortChoice();

        assertNotNull(mapUserAbortChoice);

        assertFalse(mapUserAbortChoice.isUserSpecificReason());
        assertTrue(mapUserAbortChoice.isProcedureCancellationReason());
        assertFalse(mapUserAbortChoice.isResourceUnavailableReason());
        assertFalse(mapUserAbortChoice.isUserResourceLimitation());

        ProcedureCancellationReason procdCancellReasn = mapUserAbortChoice.getProcedureCancellationReason();

        assertNotNull(procdCancellReasn);

        assertEquals(ProcedureCancellationReason.callRelease, procdCancellReasn);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mapUserAbortInfo.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "dialog" })
    public void testFullEncode() throws Exception {
        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();

        MAPUserAbortChoiceImpl mapUserAbortChoice = new MAPUserAbortChoiceImpl();
        mapUserAbortChoice.setProcedureCancellationReason(ProcedureCancellationReason.callRelease);

        mapUserAbortInfo.setMAPUserAbortChoice(mapUserAbortChoice);
        mapUserAbortInfo.setExtensionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        AsnOutputStream asnOS = new AsnOutputStream();

        mapUserAbortInfo.encodeAll(asnOS);

        byte[] data = asnOS.toByteArray();

        System.out.println(Utils.dump(data, data.length, false));

        assertTrue(Arrays.equals(getDataFull(), data));
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testBadlyEncodedFromLiveTraceDecode() throws Exception {

        // The raw data is hand made
        byte[] data = getBadlyEncodedFromLiveTrace();

        AsnInputStream asnIs = new AsnInputStream(data);

        int tag = asnIs.readTag();
        assertEquals(tag, 4);

        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
        mapUserAbortInfo.decodeAll(asnIs);

        MAPUserAbortChoice mapUserAbortChoice = mapUserAbortInfo.getMAPUserAbortChoice();

        assertNotNull(mapUserAbortChoice);

        assertTrue(mapUserAbortChoice.isUserSpecificReason());
        assertFalse(mapUserAbortChoice.isProcedureCancellationReason());
        assertFalse(mapUserAbortChoice.isResourceUnavailableReason());
        assertFalse(mapUserAbortChoice.isUserResourceLimitation());
    }
}
