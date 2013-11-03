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

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBHPLMNData;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ODBDataTest {

    public byte[] getData() {
        return new byte[] { 48, 52, 3, 5, 3, 74, -43, 85, 80, 3, 2, 4, 80, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13,
                14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        ODBDataImpl prim = new ODBDataImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ODBGeneralData oDBGeneralData = prim.getODBGeneralData();
        assertTrue(!oDBGeneralData.getAllOGCallsBarred());
        assertTrue(oDBGeneralData.getInternationalOGCallsBarred());
        assertTrue(!oDBGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsBarred());
        assertTrue(!oDBGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(!oDBGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(oDBGeneralData.getPremiumRateEntertainementOGCallsBarred());
        assertTrue(!oDBGeneralData.getSsAccessBarred());
        assertTrue(oDBGeneralData.getAllECTBarred());
        assertTrue(!oDBGeneralData.getChargeableECTBarred());
        assertTrue(oDBGeneralData.getInternationalECTBarred());
        assertTrue(!oDBGeneralData.getInterzonalECTBarred());
        assertTrue(oDBGeneralData.getDoublyChargeableECTBarred());
        assertTrue(!oDBGeneralData.getMultipleECTBarred());
        assertTrue(oDBGeneralData.getAllPacketOrientedServicesBarred());
        assertTrue(!oDBGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertTrue(oDBGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertTrue(!oDBGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(oDBGeneralData.getAllICCallsBarred());
        assertTrue(!oDBGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertTrue(!oDBGeneralData.getRoamingOutsidePLMNBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertTrue(!oDBGeneralData.getRegistrationAllCFBarred());
        assertTrue(oDBGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertTrue(!oDBGeneralData.getRegistrationInterzonalCFBarred());
        assertTrue(oDBGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(!oDBGeneralData.getRegistrationInternationalCFBarred());

        ODBHPLMNData odbHplmnData = prim.getOdbHplmnData();

        assertTrue(!odbHplmnData.getPlmnSpecificBarringType1());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType2());
        assertTrue(!odbHplmnData.getPlmnSpecificBarringType3());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType4());
        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        ODBGeneralData oDBGeneralData = new ODBGeneralDataImpl(false, true, false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false,
                true, false);
        ODBHPLMNData odbHplmnData = new ODBHPLMNDataImpl(false, true, false, true);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        ODBDataImpl prim = new ODBDataImpl(oDBGeneralData, odbHplmnData, extensionContainer);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }

}
