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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ODBGeneralDataTest {

    private byte[] getEncodedData() {
        return new byte[] { 3, 5, 3, 74, -43, 85, 80 };
    }

    private byte[] getEncodedData1() {
        return new byte[] { 3, 5, 3, -75, 42, -86, -88 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        ODBGeneralDataImpl imp = new ODBGeneralDataImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(!imp.getAllOGCallsBarred());
        assertTrue(imp.getInternationalOGCallsBarred());
        assertTrue(!imp.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(imp.getInterzonalOGCallsBarred());
        assertTrue(!imp.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(imp.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(!imp.getPremiumRateInformationOGCallsBarred());
        assertTrue(imp.getPremiumRateEntertainementOGCallsBarred());
        assertTrue(!imp.getSsAccessBarred());
        assertTrue(imp.getAllECTBarred());
        assertTrue(!imp.getChargeableECTBarred());
        assertTrue(imp.getInternationalECTBarred());
        assertTrue(!imp.getInterzonalECTBarred());
        assertTrue(imp.getDoublyChargeableECTBarred());
        assertTrue(!imp.getMultipleECTBarred());
        assertTrue(imp.getAllPacketOrientedServicesBarred());
        assertTrue(!imp.getRoamerAccessToHPLMNAPBarred());
        assertTrue(imp.getRoamerAccessToVPLMNAPBarred());
        assertTrue(!imp.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(imp.getAllICCallsBarred());
        assertTrue(!imp.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(imp.getRoamingOutsidePLMNICountryICCallsBarred());
        assertTrue(!imp.getRoamingOutsidePLMNBarred());
        assertTrue(imp.getRoamingOutsidePLMNCountryBarred());
        assertTrue(!imp.getRegistrationAllCFBarred());
        assertTrue(imp.getRegistrationCFNotToHPLMNBarred());
        assertTrue(!imp.getRegistrationInterzonalCFBarred());
        assertTrue(imp.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(!imp.getRegistrationInternationalCFBarred());

        rawData = getEncodedData1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        imp = new ODBGeneralDataImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(imp.getAllOGCallsBarred());
        assertTrue(!imp.getInternationalOGCallsBarred());
        assertTrue(imp.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(!imp.getInterzonalOGCallsBarred());
        assertTrue(imp.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(!imp.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(imp.getPremiumRateInformationOGCallsBarred());
        assertTrue(!imp.getPremiumRateEntertainementOGCallsBarred());
        assertTrue(imp.getSsAccessBarred());
        assertTrue(!imp.getAllECTBarred());
        assertTrue(imp.getChargeableECTBarred());
        assertTrue(!imp.getInternationalECTBarred());
        assertTrue(imp.getInterzonalECTBarred());
        assertTrue(!imp.getDoublyChargeableECTBarred());
        assertTrue(imp.getMultipleECTBarred());
        assertTrue(!imp.getAllPacketOrientedServicesBarred());
        assertTrue(imp.getRoamerAccessToHPLMNAPBarred());
        assertTrue(!imp.getRoamerAccessToVPLMNAPBarred());
        assertTrue(imp.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(!imp.getAllICCallsBarred());
        assertTrue(imp.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(!imp.getRoamingOutsidePLMNICountryICCallsBarred());
        assertTrue(imp.getRoamingOutsidePLMNBarred());
        assertTrue(!imp.getRoamingOutsidePLMNCountryBarred());
        assertTrue(imp.getRegistrationAllCFBarred());
        assertTrue(!imp.getRegistrationCFNotToHPLMNBarred());
        assertTrue(imp.getRegistrationInterzonalCFBarred());
        assertTrue(!imp.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(imp.getRegistrationInternationalCFBarred());
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        ODBGeneralDataImpl imp = new ODBGeneralDataImpl(false, true, false, true, false, true, false, true, false, true, false,
                true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true,
                false);

        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        assertTrue(Arrays.equals(getEncodedData(), asnOS.toByteArray()));

        imp = new ODBGeneralDataImpl(true, false, true, false, true, false, true, false, true, false, true, false, true, false,
                true, false, true, false, true, false, true, false, true, false, true, false, true, false, true);

        asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        assertTrue(Arrays.equals(getEncodedData1(), asnOS.toByteArray()));
    }
}
