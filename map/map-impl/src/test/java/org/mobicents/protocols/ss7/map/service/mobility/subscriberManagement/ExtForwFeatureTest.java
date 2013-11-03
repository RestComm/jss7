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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.FTNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNSubaddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptions;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsForwardingReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ExtForwFeatureTest {
    public byte[] getData() {
        return new byte[] { 48, 69, -126, 1, 22, -124, 1, 3, -123, 4, -111, 34, 34, -8, -120, 2, 2, 5, -122, 1, -92, -121, 1,
                2, -87, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
                21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -118, 4, -111, 34, 34, -9 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 17, (byte) 131, 1, 16, (byte) 132, 1, 15, (byte) 133, 6, (byte) 145, (byte) 153, (byte) 137,
                (byte) 136, 119, (byte) 247, (byte) 134, 1, 0 };
    };

    private byte[] getISDNSubaddressStringData() {
        return new byte[] { 2, 5 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        ExtForwFeatureImpl prim = new ExtForwFeatureImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        assertEquals(prim.getBasicService().getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.Asynchronous9_6kbps);
        assertNull(prim.getBasicService().getExtTeleservice());
        assertNotNull(prim.getSsStatus());
        assertTrue(prim.getSsStatus().getBitA());
        assertTrue(!prim.getSsStatus().getBitP());
        assertTrue(!prim.getSsStatus().getBitQ());
        assertTrue(prim.getSsStatus().getBitR());

        ISDNAddressString forwardedToNumber = prim.getForwardedToNumber();
        assertNotNull(forwardedToNumber);
        assertTrue(forwardedToNumber.getAddress().equals("22228"));
        assertEquals(forwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(forwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertTrue(Arrays.equals(prim.getForwardedToSubaddress().getData(), this.getISDNSubaddressStringData()));
        assertTrue(prim.getForwardingOptions().getNotificationToCallingParty());
        assertTrue(prim.getForwardingOptions().getNotificationToForwardingParty());
        assertTrue(!prim.getForwardingOptions().getRedirectingPresentation());
        assertEquals(prim.getForwardingOptions().getExtForwOptionsForwardingReason(), ExtForwOptionsForwardingReason.msBusy);
        assertNotNull(prim.getNoReplyConditionTime());
        assertEquals(prim.getNoReplyConditionTime().intValue(), 2);
        FTNAddressString longForwardedToNumber = prim.getLongForwardedToNumber();
        assertNotNull(longForwardedToNumber);
        assertTrue(longForwardedToNumber.getAddress().equals("22227"));
        assertEquals(longForwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(longForwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new ExtForwFeatureImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        extensionContainer = prim.getExtensionContainer();
        assertEquals(prim.getBasicService().getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.allSpeechTransmissionServices);
        assertNull(prim.getBasicService().getExtBearerService());
        assertNotNull(prim.getSsStatus());
        assertTrue(prim.getSsStatus().getBitA());
        assertTrue(prim.getSsStatus().getBitP());
        assertTrue(prim.getSsStatus().getBitQ());
        assertTrue(prim.getSsStatus().getBitR());

        forwardedToNumber = prim.getForwardedToNumber();
        assertNotNull(forwardedToNumber);
        assertTrue(forwardedToNumber.getAddress().equals("999888777"));
        assertEquals(forwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(forwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertNull(prim.getForwardedToSubaddress());
        assertFalse(prim.getForwardingOptions().getNotificationToCallingParty());
        assertFalse(prim.getForwardingOptions().getNotificationToForwardingParty());
        assertFalse(prim.getForwardingOptions().getRedirectingPresentation());
        assertEquals(prim.getForwardingOptions().getExtForwOptionsForwardingReason(),
                ExtForwOptionsForwardingReason.msNotReachable);
        assertNull(prim.getNoReplyConditionTime());
        longForwardedToNumber = prim.getLongForwardedToNumber();
        assertNull(longForwardedToNumber);
        assertNull(extensionContainer);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.Asynchronous9_6kbps);
        ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(b);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        ExtSSStatusImpl ssStatus = new ExtSSStatusImpl(false, false, true, true);
        ISDNAddressString forwardedToNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        ISDNSubaddressString forwardedToSubaddress = new ISDNSubaddressStringImpl(this.getISDNSubaddressStringData());
        ExtForwOptions forwardingOptions = new ExtForwOptionsImpl(true, false, true, ExtForwOptionsForwardingReason.msBusy);
        Integer noReplyConditionTime = new Integer(2);
        FTNAddressString longForwardedToNumber = new FTNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "22227");

        ExtForwFeatureImpl prim = new ExtForwFeatureImpl(basicService, ssStatus, forwardedToNumber, forwardedToSubaddress,
                forwardingOptions, noReplyConditionTime, extensionContainer, longForwardedToNumber);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

        basicService = new ExtBasicServiceCodeImpl(new ExtTeleserviceCodeImpl(
                TeleserviceCodeValue.allSpeechTransmissionServices));
        ssStatus = new ExtSSStatusImpl(true, true, true, true);
        forwardedToNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "999888777");
        forwardingOptions = new ExtForwOptionsImpl(false, false, false, ExtForwOptionsForwardingReason.msNotReachable);

        prim = new ExtForwFeatureImpl(basicService, ssStatus, forwardedToNumber, null, forwardingOptions, null, null, null);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));
    }
}
