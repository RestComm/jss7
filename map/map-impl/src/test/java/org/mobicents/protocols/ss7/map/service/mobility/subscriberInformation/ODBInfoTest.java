package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBHPLMNData;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ODBDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ODBGeneralDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ODBHPLMNDataImpl;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author vadim subbotin
 */
public class ODBInfoTest {
    private byte[] data = {48, 97, 48, 52, 3, 5, 3, -1, -4, 0, 0, 3, 2, 4, -96, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4,
            11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32,
            33, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3,
            42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
        AsnInputStream asn = new AsnInputStream(data);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ODBInfoImpl odbInfo = new ODBInfoImpl();
        odbInfo.decodeAll(asn);

        assertNotNull(odbInfo.getOdbData());
        assertTrue(odbInfo.getNotificationToCSE());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(odbInfo.getExtensionContainer()));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(odbInfo.getOdbData().getExtensionContainer()));

        ODBGeneralData odbGeneralData = odbInfo.getOdbData().getODBGeneralData();
        assertTrue(odbGeneralData.getAllOGCallsBarred());
        assertTrue(odbGeneralData.getInternationalOGCallsBarred());
        assertTrue(odbGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(odbGeneralData.getPremiumRateEntertainementOGCallsBarred());
        assertTrue(odbGeneralData.getSsAccessBarred());
        assertTrue(odbGeneralData.getAllECTBarred());
        assertTrue(odbGeneralData.getChargeableECTBarred());
        assertTrue(odbGeneralData.getInternationalECTBarred());
        assertTrue(odbGeneralData.getInterzonalECTBarred());
        assertTrue(odbGeneralData.getDoublyChargeableECTBarred());
        assertFalse(odbGeneralData.getMultipleECTBarred());
        assertFalse(odbGeneralData.getAllPacketOrientedServicesBarred());
        assertFalse(odbGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertFalse(odbGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertFalse(odbGeneralData.getAllICCallsBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertFalse(odbGeneralData.getRegistrationAllCFBarred());
        assertFalse(odbGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertFalse(odbGeneralData.getRegistrationInterzonalCFBarred());
        assertFalse(odbGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertFalse(odbGeneralData.getRegistrationInternationalCFBarred());

        ODBHPLMNData odbhplmnData = odbInfo.getOdbData().getOdbHplmnData();
        assertTrue(odbhplmnData.getPlmnSpecificBarringType1());
        assertFalse(odbhplmnData.getPlmnSpecificBarringType2());
        assertTrue(odbhplmnData.getPlmnSpecificBarringType3());
        assertFalse(odbhplmnData.getPlmnSpecificBarringType4());
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
        ODBGeneralDataImpl odbGeneralData = new ODBGeneralDataImpl(true, true, true, true, true, true, true, true, true,
                true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false,
                false, false, false, false);
        ODBHPLMNDataImpl odbhplmnData = new ODBHPLMNDataImpl(true, false, true, false);
        ODBDataImpl odbData = new ODBDataImpl(odbGeneralData, odbhplmnData, MAPExtensionContainerTest.GetTestExtensionContainer());

        ODBInfoImpl odbInfo = new ODBInfoImpl(odbData, true, MAPExtensionContainerTest.GetTestExtensionContainer());

        AsnOutputStream asnOS = new AsnOutputStream();
        odbInfo.encodeAll(asnOS);
        byte[] raw = asnOS.toByteArray();
        assertTrue(Arrays.equals(raw, data));
    }
}
