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
package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class UpdateGprsLocationRequestTest {

    public byte[] getData() {
        return new byte[] { 48, -127, -105, 4, 3, 17, 33, 34, 4, 4, -111, 34, 34, -8, 4, 6, 23, 5, 38, 48, 81, 5, 48, 39, -96,
                32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24,
                25, 26, -95, 3, 31, 32, 33, -96, 43, 5, 0, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5,
                6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 0, -126, 0, -125, 6,
                23, 5, 38, 48, 81, 5, -92, 6, -128, 4, 33, 67, 33, 67, -91, 4, -127, 2, 5, -32, -122, 0, -121, 0, -120, 1, 2,
                -119, 0, -118, 0, -117, 0, -116, 0, -115, 0, -114, 1, 1 };
    };

    public byte[] getGSNAddressData() {
        return new byte[] { 23, 5, 38, 48, 81, 5 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        UpdateGprsLocationRequestImpl prim = new UpdateGprsLocationRequestImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(prim.getImsi().getData().equals("111222"));
        assertTrue(prim.getSgsnNumber().getAddress().equals("22228"));
        assertEquals(prim.getSgsnNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(prim.getSgsnNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(Arrays.equals(prim.getSgsnAddress().getData(), getGSNAddressData()));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        assertTrue(prim.getSGSNCapability().getSolsaSupportIndicator());
        assertTrue(prim.getInformPreviousNetworkEntity());
        assertTrue(prim.getPsLCSNotSupportedByUE());
        assertTrue(Arrays.equals(prim.getVGmlcAddress().getData(), getGSNAddressData()));
        assertTrue(prim.getADDInfo().getImeisv().getIMEI().equals("12341234"));
        assertTrue(prim.getEPSInfo().getIsrInformation().getCancelSGSN());
        assertTrue(prim.getServingNodeTypeIndicator());
        assertTrue(prim.getSkipSubscriberDataUpdate());
        assertEquals(prim.getUsedRATType(), UsedRATType.gan);
        assertTrue(prim.getGprsSubscriptionDataNotNeeded());
        assertTrue(prim.getNodeTypeIndicator());
        assertTrue(prim.getAreaRestricted());
        assertTrue(prim.getUeReachableIndicator());
        assertTrue(prim.getEpsSubscriptionDataNotNeeded());
        assertEquals(prim.getUESRVCCCapability(), UESRVCCCapability.ueSrvccSupported);

    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testEncode() throws Exception {
        IMSI imsi = new IMSIImpl("111222");
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        GSNAddress sgsnAddress = new GSNAddressImpl(getGSNAddressData());
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        SGSNCapability sgsnCapability = new SGSNCapabilityImpl(true, extensionContainer, null, false, null, null, null, false,
                null, null, false, null);
        boolean informPreviousNetworkEntity = true;
        boolean psLCSNotSupportedByUE = true;
        GSNAddress vGmlcAddress = new GSNAddressImpl(getGSNAddressData());
        ADDInfo addInfo = new ADDInfoImpl(new IMEIImpl("12341234"), false);
        EPSInfo epsInfo = new EPSInfoImpl(new ISRInformationImpl(true, true, true));
        boolean servingNodeTypeIndicator = true;
        boolean skipSubscriberDataUpdate = true;
        UsedRATType usedRATType = UsedRATType.gan;
        boolean gprsSubscriptionDataNotNeeded = true;
        boolean nodeTypeIndicator = true;
        boolean areaRestricted = true;
        boolean ueReachableIndicator = true;
        boolean epsSubscriptionDataNotNeeded = true;
        UESRVCCCapability uesrvccCapability = UESRVCCCapability.ueSrvccSupported;

        UpdateGprsLocationRequestImpl prim = new UpdateGprsLocationRequestImpl(imsi, sgsnNumber, sgsnAddress,
                extensionContainer, sgsnCapability, informPreviousNetworkEntity, psLCSNotSupportedByUE, vGmlcAddress, addInfo,
                epsInfo, servingNodeTypeIndicator, skipSubscriberDataUpdate, usedRATType, gprsSubscriptionDataNotNeeded,
                nodeTypeIndicator, areaRestricted, ueReachableIndicator, epsSubscriptionDataNotNeeded, uesrvccCapability, 3);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

    }

}
