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
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CallTypeCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DestinationNumberCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MatchType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class OBcsmCamelTdpCriteriaTest {

    public byte[] getData() {
        return new byte[] { 48, 87, 10, 1, 2, -96, 28, -128, 1, 1, -95, 12, 4, 4, -111, 34, 50, -12, 4, 4, -111, 34, 50, -11,
                -94, 9, 2, 1, 2, 2, 1, 4, 2, 1, 1, -95, 3, -126, 1, 22, -126, 1, 0, -93, 3, 4, 1, 7, -92, 39, -96, 32, 48, 10,
                6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95,
                3, 31, 32, 33 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        OBcsmCamelTdpCriteriaImpl prim = new OBcsmCamelTdpCriteriaImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        DestinationNumberCriteria destinationNumberCriteria = prim.getDestinationNumberCriteria();

        ArrayList<ISDNAddressString> destinationNumberList = destinationNumberCriteria.getDestinationNumberList();
        assertNotNull(destinationNumberList);
        assertEquals(destinationNumberList.size(), 2);
        ISDNAddressString destinationNumberOne = destinationNumberList.get(0);
        assertNotNull(destinationNumberOne);
        assertTrue(destinationNumberOne.getAddress().equals("22234"));
        assertEquals(destinationNumberOne.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberOne.getNumberingPlan(), NumberingPlan.ISDN);
        ISDNAddressString destinationNumberTwo = destinationNumberList.get(1);
        assertNotNull(destinationNumberTwo);
        assertTrue(destinationNumberTwo.getAddress().equals("22235"));
        assertEquals(destinationNumberTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(destinationNumberCriteria.getMatchType(), MatchType.enabling);
        ArrayList<Integer> destinationNumberLengthList = destinationNumberCriteria.getDestinationNumberLengthList();
        assertNotNull(destinationNumberLengthList);
        assertEquals(destinationNumberLengthList.size(), 3);
        assertEquals(destinationNumberLengthList.get(0).intValue(), 2);
        assertEquals(destinationNumberLengthList.get(1).intValue(), 4);
        assertEquals(destinationNumberLengthList.get(2).intValue(), 1);

        assertEquals(prim.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);

        assertNotNull(prim.getBasicServiceCriteria());
        assertEquals(prim.getBasicServiceCriteria().size(), 1);
        ExtBasicServiceCode basicService = prim.getBasicServiceCriteria().get(0);
        assertNotNull(basicService);
        assertEquals(basicService.getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.Asynchronous9_6kbps);
        assertEquals(prim.getCallTypeCriteria(), CallTypeCriteria.forwarded);
        ArrayList<CauseValue> oCauseValueCriteria = prim.getOCauseValueCriteria();
        assertNotNull(oCauseValueCriteria);
        assertEquals(oCauseValueCriteria.size(), 1);
        assertNotNull(oCauseValueCriteria.get(0));
        assertEquals(oCauseValueCriteria.get(0).getData(), 7);
        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint = OBcsmTriggerDetectionPoint.collectedInfo;
        ISDNAddressStringImpl destinationNumberOne = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "22234");
        ISDNAddressStringImpl destinationNumberTwo = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "22235");
        ArrayList<ISDNAddressString> destinationNumberList = new ArrayList<ISDNAddressString>();
        destinationNumberList.add(destinationNumberOne);
        destinationNumberList.add(destinationNumberTwo);
        ArrayList<Integer> destinationNumberLengthList = new ArrayList<Integer>();
        destinationNumberLengthList.add(new Integer(2));
        destinationNumberLengthList.add(new Integer(4));
        destinationNumberLengthList.add(new Integer(1));
        DestinationNumberCriteria destinationNumberCriteria = new DestinationNumberCriteriaImpl(MatchType.enabling,
                destinationNumberList, destinationNumberLengthList);

        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.Asynchronous9_6kbps);
        ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(b);
        ArrayList<ExtBasicServiceCode> basicServiceCriteria = new ArrayList<ExtBasicServiceCode>();
        basicServiceCriteria.add(basicService);

        CallTypeCriteria callTypeCriteria = CallTypeCriteria.forwarded;
        ArrayList<CauseValue> oCauseValueCriteria = new ArrayList<CauseValue>();
        oCauseValueCriteria.add(new CauseValueImpl(7));
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        OBcsmCamelTdpCriteriaImpl prim = new OBcsmCamelTdpCriteriaImpl(oBcsmTriggerDetectionPoint, destinationNumberCriteria,
                basicServiceCriteria, callTypeCriteria, oCauseValueCriteria, extensionContainer);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }
}
