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
package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GroupId;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LongGroupId;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class VoiceBroadcastDataTest {

    public byte[] getData() {
        return new byte[] { 48, 54, 4, 3, -1, -1, -1, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5,
                6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 4, 33, 67, 101, -121 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 48, 4, 3, 33, 67, 101, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48,
                5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getData3() {
        return new byte[] { 48, 54, 4, 3, -1, -1, -1, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5,
                6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 4, 33, 67, 101, -121 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        // Option 1
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        VoiceBroadcastDataImpl prim = new VoiceBroadcastDataImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(prim.getGroupId().getGroupId().equals(""));
        assertTrue(prim.getLongGroupId().getLongGroupId().equals("12345678"));
        assertTrue(prim.getBroadcastInitEntitlement());
        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

        // Option 2
        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new VoiceBroadcastDataImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(prim.getGroupId().getGroupId().equals("123456"));
        assertNull(prim.getLongGroupId());
        assertTrue(prim.getBroadcastInitEntitlement());
        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

        // Option 3
        data = this.getData3();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new VoiceBroadcastDataImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(prim.getGroupId().getGroupId().equals(""));
        assertTrue(prim.getLongGroupId().getLongGroupId().equals("12345678"));
        assertTrue(prim.getBroadcastInitEntitlement());
        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        // option 1
        GroupId groupId = new GroupIdImpl("123456");
        boolean broadcastInitEntitlement = true;
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        LongGroupId longGroupId = new LongGroupIdImpl("12345678");

        VoiceBroadcastDataImpl prim = new VoiceBroadcastDataImpl(groupId, broadcastInitEntitlement, extensionContainer,
                longGroupId);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

        // option 2
        prim = new VoiceBroadcastDataImpl(groupId, broadcastInitEntitlement, extensionContainer, null);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));

        // option 3
        prim = new VoiceBroadcastDataImpl(null, broadcastInitEntitlement, extensionContainer, longGroupId);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData3()));
    }

}
