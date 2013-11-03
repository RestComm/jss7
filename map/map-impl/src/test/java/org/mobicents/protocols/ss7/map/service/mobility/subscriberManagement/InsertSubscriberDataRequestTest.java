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

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.FTNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNSubaddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NAEACIC;
import org.mobicents.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.Time;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.AgeIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContext;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.*;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.mobicents.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.primitives.NAEACICImpl;
import org.mobicents.protocols.ss7.map.primitives.NAEAPreferredCIImpl;
import org.mobicents.protocols.ss7.map.primitives.TimeImpl;
import org.mobicents.protocols.ss7.map.service.lsm.LCSClientExternalIDImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.PDPContextImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class InsertSubscriberDataRequestTest {

    public byte[] getData() {
        return new byte[] { 48, -126, 16, -63, -128, 5, 17, 17, 33, 34, 34, -127, 4, -111, 34, 50, -12, -126, 1, 5, -125, 1, 1, -92, 3, 4, 1, 22, -90, 3, 4, 1,
                16, -89, 119, -96, 117, 4, 1, 0, 48, 71, 48, 69, -126, 1, 22, -124, 1, 15, -123, 4, -111, 34, 34, -8, -120, 2, 2, 5, -122, 1, -92, -121, 1, 2,
                -87, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33, -118, 4, -111, 34, 34, -9, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42,
                3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -88, 52, 3, 5, 3, 74, -43, 85, 80, 3, 2, 4, 80, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11,
                12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -119, 0, -86, 4, 4, 2, 0, 2, -85,
                56, 48, 54, 4, 3, -1, -1, -1, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
                21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 4, -11, -1, -1, -1, -84, 62, 48, 60, 4, 3, -1, -1, -1, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3,
                4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 3, 2, 5, -96, -128, 2, 7,
                -128, -127, 4, -11, -1, -1, -1, -83, -126, 2, -48, -96, 23, 48, 18, 48, 16, 10, 1, 4, 2, 1, 3, -128, 5, -111, 17, 34, 51, -13, -127, 1, 1,
                -128, 1, 2, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33, -94, 99, 48, 52, 48, 3, 4, 1, 96, 4, 4, -111, 34, 50, -11, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14,
                15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12,
                13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 0, -127, 0, -92, 92, 48, 90, 10,
                1, 2, -96, 28, -128, 1, 1, -95, 12, 4, 4, -111, 34, 50, -12, 4, 4, -111, 34, 50, -11, -94, 9, 2, 1, 2, 2, 1, 4, 2, 1, 1, -95, 6, -126, 1, 22,
                -125, 1, 16, -126, 1, 0, -93, 3, 4, 1, 7, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3,
                42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -91, 62, 48, 6, 4, 1, -125, 4, 1, 2, 2, 1, 3, -128, 4, -111, 34, 50, -11, -95, 39, -96,
                32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33,
                -126, 0, -125, 0, -90, 108, -96, 58, 48, 56, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 39, -96, 32, 48, 10, 6, 3,
                42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 8, -94, 39,
                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33,
                -125, 0, -124, 0, -89, 22, 48, 17, 48, 15, 10, 1, 12, 2, 1, 3, -128, 4, -111, 34, 50, -11, -127, 1, 1, -128, 1, 2, -88, 21, 48, 19, 10, 1, 13,
                -96, 6, -126, 1, 22, -125, 1, 16, -95, 6, 4, 1, 7, 4, 1, 6, -87, 111, -96, 61, 48, 59, 4, 4, -111, 34, 50, -12, 2, 1, 7, 4, 4, -111, 34, 50,
                -11, 2, 1, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33, -127, 1, 2, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3,
                5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0, -86, 108, -96, 58, 48, 56, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11,
                -125, 1, 0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33, -127, 1, 8, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3,
                5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0, -85, 13, 48, 11, 10, 1, 1, -96, 6, 10, 1, 0, 10, 1, 2, -82, 39, -96, 32, 48,
                10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -81, 46,
                -128, 3, 15, 48, 5, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
                24, 25, 26, -95, 3, 31, 32, 33, -80, -127, -93, -95, 109, 48, 107, 2, 1, 1, -112, 2, 5, 3, -111, 3, 5, 6, 7, -110, 3, 4, 7, 7, -108, 2, 6, 7,
                -75, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33, -128, 2, 1, 7, -127, 2, 6, 5, -126, 2, 1, 8, -125, 2, 2, 6, -124, 1, 2, -123, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12, -122, 2, 6,
                5, -121, 3, 4, 6, 5, -120, 1, 0, -119, 1, 2, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3,
                42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12, -105, 0, -104, 1, 0, -71, 101, 5, 0, -127,
                1, 1, -94, 53, 48, 51, -128, 3, 12, 34, 26, -127, 1, 5, -126, 0, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
                3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5,
                6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -107, 0, -74, -126, 6, 111, -96, 6, 4, 4, -111, 34, 50,
                -11, -95, -126, 4, -16, 48, -126, 1, 56, 4, 1, 0, 4, 1, 15, -128, 1, 3, -95, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32,
                48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1,
                0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33, -94, 6, 2, 1, 0, 2, 1, 1, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11,
                6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -92, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3,
                42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1,
                3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33, -91, 52, 48, 50, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3,
                6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 48, -126, 1, 56, 4, 1, 96, 4, 1, 15, -128, 1, 3, -95, 98, 48, 96, 48,
                47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21,
                22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
                3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 6, 2, 1, 0, 2, 1, 1, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4,
                11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -92, 98, 48, 96, 48, 47, -128,
                4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
                24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6,
                48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -91, 52, 48, 50, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10,
                6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 48, -126, 1, 56,
                4, 1, 32, 4, 1, 15, -128, 1, 3, -95, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14,
                15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48,
                10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 6, 2,
                1, 0, 2, 1, 1, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33, -92, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48,
                5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3,
                42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -91, 52, 48, 50, 2, 1,
                1, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
                23, 24, 25, 26, -95, 3, 31, 32, 33, 48, -126, 1, 56, 4, 1, 16, 4, 1, 15, -128, 1, 3, -95, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95,
                39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32,
                33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21,
                22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 6, 2, 1, 0, 2, 1, 1, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3,
                42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -92, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96,
                32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33,
                -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
                23, 24, 25, 26, -95, 3, 31, 32, 33, -91, 52, 48, 50, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14,
                15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 49, 48, 47, 4, 1, 0, 4, 1, 15, -96, 39,
                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33,
                -93, -126, 1, 60, 48, -126, 1, 56, 4, 1, 0, 4, 1, 15, -128, 1, 3, -95, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10,
                6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0,
                -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33, -94, 6, 2, 1, 0, 2, 1, 1, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11,
                6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -92, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3,
                42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1,
                3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33, -91, 52, 48, 50, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3,
                6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -102, 1, 21, -101, 1, 48, -68, 53, -128, 1, 0, -127, 1, 15, -126, 1, 2,
                -125, 1, 4, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33, -99, 1, 4, -79, -126, 1, -66, -96, 108, -96, 58, 48, 56, -128, 1, 2, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1,
                1, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33, -127, 1, 8, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
                23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0, -95, 108, -96, 58, 48, 56, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1,
                0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33, -127, 1, 8, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
                23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48,
                11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -93, 104, -96, 58, 48, 56, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11,
                -125, 1, 0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33, -127, 1, 8, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3,
                5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -92, 13, 48, 11, 10, 1, 1, -96, 6, 10, 1, 0, 10, 1, 2, -91, 62, 48, 6, 4, 1, -125, 4, 1, 2, 2,
                1, 3, -128, 4, -111, 34, 50, -11, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
                21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -126, 0, -125, 0, -110, 2, 6, 5, -109, 2, 2, 84, -108, 1, -1, -65, 31, -126, 2, 71, -128, 9, 48,
                12, 17, 17, 119, 22, 62, 34, 12, -126, 1, 4, -93, 47, -128, 1, 2, -127, 1, 4, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48,
                5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -92, -126, 1, -47, 2, 1, 2, 5, 0, -95, -126, 1, -97, 48,
                -126, 1, -101, -128, 1, 1, -127, 1, 1, -126, 3, 5, 6, 7, -125, 2, 6, 7, -92, 96, -128, 1, 1, -95, 50, -128, 1, 1, -127, 1, -1, -126, 1, -1,
                -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33, -91, 63, -128, 3, 5, 6, 7, -127, 3, 5, 6, 7, -126, 10, 4, 1, 6, 8, 3, 2, 5, 6, 1, 7, -93, 39, -96, 32, 48, 10, 6, 3,
                42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -122, 1, 1, -120, 2,
                6, 5, -87, 47, -128, 1, 2, -127, 1, 4, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42,
                3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -86, 112, 48, 110, -128, 2, 6, 7, -95, 63, -128, 3, 5, 6, 7, -127, 3, 5, 6, 7, -126, 10, 4,
                1, 6, 8, 3, 2, 5, 6, 1, 7, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
                23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3,
                5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -85, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6,
                3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -116, 3, 5, 6, 7, -115, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12, -114, 1, 0, -113, 1,
                2, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33, -122, 4, -111, 34, 34, -8, -91, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42,
                3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -121, 0, -120, 0, -65, 32, 62, 48, 60, 3, 5, 5, -128, 0, 0, 32, 4, 4, 10, 22, 41, 34, 48, 39,
                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33,
                -96, 4, 4, 2, 6, 7, -97, 33, 0, -97, 34, 4, -111, 34, 34, -8, -97, 35, 9, 41, 42, 43, 44, 45, 46, 47, 48, 49, -97, 36, 1, 2, -97, 37, 0, -97,
                38, 1, -1, -97, 39, 1, 2 };
    }

    private byte[] getData1() {
        return new byte[] { 48, -126, 4, 34, -128, 5, 17, 17, 33, 34, 34, -127, 4, -111, 34, 50, -12, -126, 1, 5, -125, 1, 1,
                -92, 3, 4, 1, 22, -90, 3, 4, 1, 16, -89, 119, -96, 117, 4, 1, 0, 48, 71, 48, 69, -126, 1, 22, -124, 1, 15,
                -123, 4, -111, 34, 34, -8, -120, 2, 2, 5, -122, 1, -92, -121, 1, 2, -87, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4,
                11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33,
                -118, 4, -111, 34, 34, -9, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6,
                48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -88, 52, 3, 5, 3, 74, -43, 85, 80, 3, 2, 4,
                80, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
                21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -119, 0, -86, 4, 4, 2, 0, 2, -85, 56, 48, 54, 4, 3, -1, -1, -1, 5,
                0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
                21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 4, -11, -1, -1, -1, -84, 62, 48, 60, 4, 3, -1, -1, -1, 48,
                39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
                23, 24, 25, 26, -95, 3, 31, 32, 33, 3, 2, 5, -96, -128, 2, 7, -128, -127, 4, -11, -1, -1, -1, -83, -126, 2,
                -48, -96, 23, 48, 18, 48, 16, 10, 1, 4, 2, 1, 3, -128, 5, -111, 17, 34, 51, -13, -127, 1, 1, -128, 1, 2, -95,
                39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
                23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 99, 48, 52, 48, 3, 4, 1, 96, 4, 4, -111, 34, 50, -11, -96, 39, -96,
                32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24,
                25, 26, -95, 3, 31, 32, 33, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6,
                48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 0, -127, 0, -92, 92, 48, 90, 10, 1,
                2, -96, 28, -128, 1, 1, -95, 12, 4, 4, -111, 34, 50, -12, 4, 4, -111, 34, 50, -11, -94, 9, 2, 1, 2, 2, 1, 4, 2,
                1, 1, -95, 6, -126, 1, 22, -125, 1, 16, -126, 1, 0, -93, 3, 4, 1, 7, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4,
                11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33,
                -91, 62, 48, 6, 4, 1, -125, 4, 1, 2, 2, 1, 3, -128, 4, -111, 34, 50, -11, -95, 39, -96, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31,
                32, 33, -126, 0, -125, 0, -90, 108, -96, 58, 48, 56, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125,
                1, 0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3,
                5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 8, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12,
                13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0,
                -124, 0, -89, 22, 48, 17, 48, 15, 10, 1, 12, 2, 1, 3, -128, 4, -111, 34, 50, -11, -127, 1, 1, -128, 1, 2, -88,
                21, 48, 19, 10, 1, 13, -96, 6, -126, 1, 22, -125, 1, 16, -95, 6, 4, 1, 7, 4, 1, 6, -87, 111, -96, 61, 48, 59,
                4, 4, -111, 34, 50, -12, 2, 1, 7, 4, 4, -111, 34, 50, -11, 2, 1, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4,
                11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33,
                -127, 1, 2, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3,
                42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0, -86, 108, -96, 58, 48, 56, -128, 1, 1,
                -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14,
                15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 8, -94,
                39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
                23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0, -85, 13, 48, 11, 10, 1, 1, -96, 6, 10, 1, 0, 10, 1, 2 };
    }

    private byte[] getISDNSubaddressStringData() {
        return new byte[] { 2, 5 };
    }

    public byte[] getNAEACICIData() {
        return new byte[] { 15, 48, 5 };
    };

    public byte[] getAPNOIReplacementData() {
        return new byte[] { 48, 12, 17, 17, 119, 22, 62, 34, 12 };
    };

    public byte[] getPDPTypeData() {
        return new byte[] { 5, 3 };
    };

    public byte[] getPDPAddressData() {
        return new byte[] { 5, 6, 7 };
    };

    public byte[] getPDPAddressData2() {
        return new byte[] { 4, 6, 5 };
    };

    public byte[] getQoSSubscribedData() {
        return new byte[] { 4, 7, 7 };
    };

    public byte[] getAPNData() {
        return new byte[] { 6, 7 };
    };

    public byte[] getExtQoSSubscribedData() {
        return new byte[] { 1, 7 };
    };

    public byte[] getExt2QoSSubscribedData() {
        return new byte[] { 1, 8 };
    };

    public byte[] getExt3QoSSubscribedData() {
        return new byte[] { 2, 6 };
    };

    public byte[] getChargingCharacteristicsData() {
        return new byte[] { 6, 5 };
    };

    public byte[] getExtPDPTypeData() {
        return new byte[] { 6, 5 };
    };

    public byte[] getDataLSAIdentity() {
        return new byte[] { 12, 34, 26 };
    };

    public byte[] getAgeIndicatorData() {
        return new byte[] { 48 };
    };

    public byte[] getFQDNData() {
        return new byte[] { 4, 1, 6, 8, 3, 2, 5, 6, 1, 7 };
    };

    public byte[] getTimeData() {
        return new byte[] { 10, 22, 41, 34 };
    };

    private byte[] getDiameterIdentity() {
        return new byte[] { 41, 42, 43, 44, 45, 46, 47, 48, 49 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        // MAP Protocol Version 3 message Testing
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        InsertSubscriberDataRequestImpl prim = new InsertSubscriberDataRequestImpl(3);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        // imsi
        IMSI imsi = prim.getImsi();
        assertTrue(imsi.getData().equals("1111122222"));

        // msisdn
        ISDNAddressString msisdn = prim.getMsisdn();
        assertTrue(msisdn.getAddress().equals("22234"));
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);

        // category
        Category category = prim.getCategory();
        assertEquals(category.getData(), 5);

        // subscriberStatus
        SubscriberStatus subscriberStatus = prim.getSubscriberStatus();
        assertEquals(subscriberStatus, SubscriberStatus.operatorDeterminedBarring);

        // bearerServiceList
        ArrayList<ExtBearerServiceCode> bearerServiceList = prim.getBearerServiceList();
        assertNotNull(bearerServiceList);
        assertEquals(bearerServiceList.size(), 1);
        ExtBearerServiceCode extBearerServiceCode = bearerServiceList.get(0);
        assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.Asynchronous9_6kbps);

        // teleserviceList
        ArrayList<ExtTeleserviceCode> teleserviceList = prim.getTeleserviceList();
        assertNotNull(teleserviceList);
        assertEquals(teleserviceList.size(), 1);
        ExtTeleserviceCode extTeleserviceCode = teleserviceList.get(0);
        assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        // start provisionedSS
        ArrayList<ExtSSInfo> provisionedSS = prim.getProvisionedSS();
        assertNotNull(provisionedSS);
        assertEquals(provisionedSS.size(), 1);
        ExtSSInfo extSSInfo = provisionedSS.get(0);

        ExtForwInfo forwardingInfo = extSSInfo.getForwardingInfo();
        ExtCallBarInfo callBarringInfo = extSSInfo.getCallBarringInfo();
        CUGInfo cugInfo = extSSInfo.getCugInfo();
        ExtSSData ssData = extSSInfo.getSsData();
        EMLPPInfo emlppInfo = extSSInfo.getEmlppInfo();

        assertNotNull(forwardingInfo);
        assertNull(callBarringInfo);
        assertNull(cugInfo);
        assertNull(ssData);
        assertNull(emlppInfo);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(forwardingInfo.getExtensionContainer()));
        assertEquals(forwardingInfo.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allServices);

        ArrayList<ExtForwFeature> forwardingFeatureList = forwardingInfo.getForwardingFeatureList();
        ;
        assertNotNull(forwardingFeatureList);
        assertEquals(forwardingFeatureList.size(), 1);
        ExtForwFeature extForwFeature = forwardingFeatureList.get(0);
        assertNotNull(extForwFeature);

        assertEquals(extForwFeature.getBasicService().getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.Asynchronous9_6kbps);
        assertNull(extForwFeature.getBasicService().getExtTeleservice());
        assertNotNull(extForwFeature.getSsStatus());
        assertTrue(extForwFeature.getSsStatus().getBitA());
        assertTrue(extForwFeature.getSsStatus().getBitP());
        assertTrue(extForwFeature.getSsStatus().getBitQ());
        assertTrue(extForwFeature.getSsStatus().getBitR());

        ISDNAddressString forwardedToNumber = extForwFeature.getForwardedToNumber();
        assertNotNull(forwardedToNumber);
        assertTrue(forwardedToNumber.getAddress().equals("22228"));
        assertEquals(forwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(forwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertTrue(Arrays.equals(extForwFeature.getForwardedToSubaddress().getData(), this.getISDNSubaddressStringData()));
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToCallingParty());
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToForwardingParty());
        assertTrue(!extForwFeature.getForwardingOptions().getRedirectingPresentation());
        assertEquals(extForwFeature.getForwardingOptions().getExtForwOptionsForwardingReason(),
                ExtForwOptionsForwardingReason.msBusy);
        assertNotNull(extForwFeature.getNoReplyConditionTime());
        assertEquals(extForwFeature.getNoReplyConditionTime().intValue(), 2);
        FTNAddressString longForwardedToNumber = extForwFeature.getLongForwardedToNumber();
        assertNotNull(longForwardedToNumber);
        assertTrue(longForwardedToNumber.getAddress().equals("22227"));
        assertEquals(longForwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(longForwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);
        // end provisionedSS

        // start odbData
        ODBData odbData = prim.getODBData();
        ODBGeneralData oDBGeneralData = odbData.getODBGeneralData();
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
        ODBHPLMNData odbHplmnData = odbData.getOdbHplmnData();
        assertTrue(!odbHplmnData.getPlmnSpecificBarringType1());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType2());
        assertTrue(!odbHplmnData.getPlmnSpecificBarringType3());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType4());
        assertNotNull(odbData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(odbData.getExtensionContainer()));
        // end odbData

        // start roamingRestrictionDueToUnsupportedFeature
        assertTrue(prim.getRoamingRestrictionDueToUnsupportedFeature());

        // start regionalSubscriptionData
        ArrayList<ZoneCode> regionalSubscriptionData = prim.getRegionalSubscriptionData();
        assertNotNull(regionalSubscriptionData);
        assertEquals(regionalSubscriptionData.size(), 1);
        ZoneCode zoneCode = regionalSubscriptionData.get(0);
        assertEquals(zoneCode.getValue(), 2);
        // end regionalSubscriptionData

        // start vbsSubscriptionData
        ArrayList<VoiceBroadcastData> vbsSubscriptionData = prim.getVbsSubscriptionData();
        assertNotNull(vbsSubscriptionData);
        assertEquals(vbsSubscriptionData.size(), 1);
        VoiceBroadcastData voiceBroadcastData = vbsSubscriptionData.get(0);
        assertTrue(voiceBroadcastData.getGroupId().getGroupId().equals(""));
        assertTrue(voiceBroadcastData.getLongGroupId().getLongGroupId().equals("5"));
        assertTrue(voiceBroadcastData.getBroadcastInitEntitlement());
        assertNotNull(voiceBroadcastData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(voiceBroadcastData.getExtensionContainer()));
        // end vbsSubscriptionData

        // start vgcsSubscriptionData
        ArrayList<VoiceGroupCallData> vgcsSubscriptionData = prim.getVgcsSubscriptionData();
        assertNotNull(vgcsSubscriptionData);
        assertEquals(vgcsSubscriptionData.size(), 1);
        VoiceGroupCallData voiceGroupCallData = vgcsSubscriptionData.get(0);
        assertTrue(voiceGroupCallData.getGroupId().getGroupId().equals(""));
        assertTrue(voiceGroupCallData.getLongGroupId().getLongGroupId().equals("5"));
        assertNotNull(voiceGroupCallData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(voiceGroupCallData.getExtensionContainer()));
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyReset());
        assertFalse(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyUplinkRequest());
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getPrivilegedUplinkRequest());
        assertNotNull(voiceGroupCallData.getAdditionalInfo());
        assertTrue(voiceGroupCallData.getAdditionalInfo().getData().get(0));
        // end vgcsSubscriptionData

        // start vlrCamelSubscriptionInfo
        VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo = prim.getVlrCamelSubscriptionInfo();

        OCSI oCsi = vlrCamelSubscriptionInfo.getOCsi();
        ArrayList<OBcsmCamelTDPData> lst = oCsi.getOBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        OBcsmCamelTDPData cd = lst.get(0);
        assertEquals(cd.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(cd.getServiceKey(), 3);
        assertEquals(cd.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(cd.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(cd.getGsmSCFAddress().getAddress().equals("1122333"));
        assertEquals(cd.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(cd.getExtensionContainer());

        assertNull(oCsi.getExtensionContainer());
        assertEquals((int) oCsi.getCamelCapabilityHandling(), 2);
        assertFalse(oCsi.getNotificationToCSE());
        assertFalse(oCsi.getCsiActive());

        assertNotNull(vlrCamelSubscriptionInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(vlrCamelSubscriptionInfo.getExtensionContainer()));

        SSCSI ssCsi = vlrCamelSubscriptionInfo.getSsCsi();
        SSCamelData ssCamelData = ssCsi.getSsCamelData();

        ArrayList<SSCode> ssEventList = ssCamelData.getSsEventList();
        assertNotNull(ssEventList);
        assertEquals(ssEventList.size(), 1);
        SSCode one = ssEventList.get(0);
        assertNotNull(one);
        assertEquals(one.getSupplementaryCodeValue(), SupplementaryCodeValue.allFacsimileTransmissionServices);
        ISDNAddressString gsmSCFAddress = ssCamelData.getGsmSCFAddress();
        assertTrue(gsmSCFAddress.getAddress().equals("22235"));
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(ssCamelData.getExtensionContainer());
        assertNotNull(ssCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ssCsi.getExtensionContainer()));
        assertTrue(ssCsi.getCsiActive());
        assertTrue(ssCsi.getNotificationToCSE());

        ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList = vlrCamelSubscriptionInfo.getOBcsmCamelTDPCriteriaList();
        assertNotNull(oBcsmCamelTDPCriteriaList);
        assertEquals(oBcsmCamelTDPCriteriaList.size(), 1);
        OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria = oBcsmCamelTDPCriteriaList.get(0);
        assertNotNull(oBcsmCamelTdpCriteria);

        DestinationNumberCriteria destinationNumberCriteria = oBcsmCamelTdpCriteria.getDestinationNumberCriteria();
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
        assertEquals(destinationNumberCriteria.getMatchType().getCode(), MatchType.enabling.getCode());
        ArrayList<Integer> destinationNumberLengthList = destinationNumberCriteria.getDestinationNumberLengthList();
        assertNotNull(destinationNumberLengthList);
        assertEquals(destinationNumberLengthList.size(), 3);
        assertEquals(destinationNumberLengthList.get(0).intValue(), 2);
        assertEquals(destinationNumberLengthList.get(1).intValue(), 4);
        assertEquals(destinationNumberLengthList.get(2).intValue(), 1);
        assertEquals(oBcsmCamelTdpCriteria.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
        assertNotNull(oBcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        ExtBasicServiceCode basicServiceOne = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(0);
        assertNotNull(basicServiceOne);
        assertEquals(basicServiceOne.getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.Asynchronous9_6kbps);

        ExtBasicServiceCode basicServiceTwo = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(1);
        assertNotNull(basicServiceTwo);
        assertEquals(basicServiceTwo.getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.allSpeechTransmissionServices);

        assertEquals(oBcsmCamelTdpCriteria.getCallTypeCriteria(), CallTypeCriteria.forwarded);
        ArrayList<CauseValue> oCauseValueCriteria = oBcsmCamelTdpCriteria.getOCauseValueCriteria();
        assertNotNull(oCauseValueCriteria);
        assertEquals(oCauseValueCriteria.size(), 1);
        assertNotNull(oCauseValueCriteria.get(0));
        assertEquals(oCauseValueCriteria.get(0).getData(), 7);

        assertFalse(vlrCamelSubscriptionInfo.getTifCsi());

        MCSI mCsi = vlrCamelSubscriptionInfo.getMCsi();
        ArrayList<MMCode> mobilityTriggers = mCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggers);
        assertEquals(mobilityTriggers.size(), 2);
        MMCode mmCode = mobilityTriggers.get(0);
        assertNotNull(mmCode);
        assertEquals(MMCodeValue.GPRSAttach, mmCode.getMMCodeValue());
        MMCode mmCode2 = mobilityTriggers.get(1);
        assertNotNull(mmCode2);
        assertEquals(MMCodeValue.IMSIAttach, mmCode2.getMMCodeValue());
        assertNotNull(mCsi.getServiceKey());
        assertEquals(mCsi.getServiceKey(), 3);
        ISDNAddressString gsmSCFAddressTwo = mCsi.getGsmSCFAddress();
        assertTrue(gsmSCFAddressTwo.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(mCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mCsi.getExtensionContainer()));
        assertTrue(mCsi.getCsiActive());
        assertTrue(mCsi.getNotificationToCSE());

        SMSCSI smsCsi = vlrCamelSubscriptionInfo.getSmsCsi();
        ArrayList<SMSCAMELTDPData> smsCamelTdpDataList = smsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataList);
        assertEquals(smsCamelTdpDataList.size(), 1);
        SMSCAMELTDPData smsCAMELTDPData = smsCamelTdpDataList.get(0);
        assertNotNull(smsCAMELTDPData);
        assertEquals(smsCAMELTDPData.getServiceKey(), 3);
        assertEquals(smsCAMELTDPData.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressString gsmSCFAddressSmsCAMELTDPData = smsCAMELTDPData.getGsmSCFAddress();
        assertTrue(gsmSCFAddressSmsCAMELTDPData.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPData.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPData.getExtensionContainer()));
        assertTrue(smsCsi.getCsiActive());
        assertTrue(smsCsi.getNotificationToCSE());
        assertEquals(smsCsi.getCamelCapabilityHandling().intValue(), 8);

        TCSI vtCsi = vlrCamelSubscriptionInfo.getVtCsi();
        ArrayList<TBcsmCamelTDPData> tbcsmCamelTDPDatalst = vtCsi.getTBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        TBcsmCamelTDPData tbcsmCamelTDPData = tbcsmCamelTDPDatalst.get(0);
        assertEquals(tbcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.termAttemptAuthorized);
        assertEquals(tbcsmCamelTDPData.getServiceKey(), 3);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(tbcsmCamelTDPData.getGsmSCFAddress().getAddress().equals("22235"));
        assertEquals(tbcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(tbcsmCamelTDPData.getExtensionContainer());
        assertNull(vtCsi.getExtensionContainer());
        assertEquals((int) vtCsi.getCamelCapabilityHandling(), 2);
        assertFalse(vtCsi.getNotificationToCSE());
        assertFalse(vtCsi.getCsiActive());

        ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getTBcsmCamelTdpCriteriaList();
        assertNotNull(tBcsmCamelTdpCriteriaList);
        assertEquals(tBcsmCamelTdpCriteriaList.size(), 1);
        assertNotNull(tBcsmCamelTdpCriteriaList.get(0));
        TBcsmCamelTdpCriteria tbcsmCamelTdpCriteria = tBcsmCamelTdpCriteriaList.get(0);
        assertEquals(tbcsmCamelTdpCriteria.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(tbcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(0));
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(1));
        ArrayList<CauseValue> oCauseValueCriteriaLst = tbcsmCamelTdpCriteria.getTCauseValueCriteria();
        assertNotNull(oCauseValueCriteriaLst);
        assertEquals(oCauseValueCriteriaLst.size(), 2);
        assertNotNull(oCauseValueCriteriaLst.get(0));
        assertEquals(oCauseValueCriteriaLst.get(0).getData(), 7);
        assertNotNull(oCauseValueCriteriaLst.get(1));
        assertEquals(oCauseValueCriteriaLst.get(1).getData(), 6);

        DCSI dCsi = vlrCamelSubscriptionInfo.getDCsi();
        ArrayList<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = dCsi.getDPAnalysedInfoCriteriaList();
        assertNotNull(dpAnalysedInfoCriteriaList);
        assertEquals(dpAnalysedInfoCriteriaList.size(), 1);
        DPAnalysedInfoCriterium dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
        assertNotNull(dpAnalysedInfoCriterium);
        ISDNAddressString dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
        assertTrue(dialledNumber.getAddress().equals("22234"));
        assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
        ISDNAddressString gsmSCFAddressDp = dpAnalysedInfoCriterium.getGsmSCFAddress();
        assertTrue(gsmSCFAddressDp.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressDp.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressDp.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNotNull(dCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(dCsi.getExtensionContainer()));
        assertEquals(dCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(dCsi.getCsiActive());
        assertTrue(dCsi.getNotificationToCSE());

        SMSCSI mtSmsCSI = vlrCamelSubscriptionInfo.getMtSmsCSI();
        ArrayList<SMSCAMELTDPData> smsCamelTdpDataListOfmtSmsCSI = mtSmsCSI.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListOfmtSmsCSI);
        assertEquals(smsCamelTdpDataListOfmtSmsCSI.size(), 1);
        SMSCAMELTDPData smsCAMELTDPDataOfMtSmsCSI = smsCamelTdpDataListOfmtSmsCSI.get(0);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getServiceKey(), 3);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressString gsmSCFAddressOfMtSmsCSI = smsCAMELTDPDataOfMtSmsCSI.getGsmSCFAddress();
        assertTrue(gsmSCFAddressOfMtSmsCSI.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressOfMtSmsCSI.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressOfMtSmsCSI.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer()));
        assertNotNull(mtSmsCSI.getExtensionContainer());
        assertTrue(mtSmsCSI.getCsiActive());
        assertTrue(mtSmsCSI.getNotificationToCSE());
        assertEquals(mtSmsCSI.getCamelCapabilityHandling().intValue(), 8);

        ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getMtSmsCamelTdpCriteriaList();
        assertNotNull(mtSmsCamelTdpCriteriaList);
        assertEquals(mtSmsCamelTdpCriteriaList.size(), 1);
        MTsmsCAMELTDPCriteria mtsmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaList.get(0);

        ArrayList<MTSMSTPDUType> tPDUTypeCriterion = mtsmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterion);
        assertEquals(tPDUTypeCriterion.size(), 2);
        MTSMSTPDUType mtSMSTPDUTypeOne = tPDUTypeCriterion.get(0);
        assertNotNull(mtSMSTPDUTypeOne);
        assertEquals(mtSMSTPDUTypeOne, MTSMSTPDUType.smsDELIVER);

        MTSMSTPDUType mtSMSTPDUTypeTwo = tPDUTypeCriterion.get(1);
        assertNotNull(mtSMSTPDUTypeTwo);
        assertTrue(mtSMSTPDUTypeTwo == MTSMSTPDUType.smsSTATUSREPORT);
        assertEquals(mtsmsCAMELTDPCriteria.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        // end vlrCamelSubscriptionInfo

        // start naeaPreferredCI
        NAEAPreferredCI naeaPreferredCI = prim.getNAEAPreferredCI();
        assertEquals(naeaPreferredCI.getNaeaPreferredCIC().getData(), this.getNAEACICIData());
        assertNotNull(naeaPreferredCI.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(naeaPreferredCI.getExtensionContainer()));
        // end naeaPreferredCI

        // start gprsSubscriptionData
        GPRSSubscriptionData gprsSubscriptionData = prim.getGPRSSubscriptionData();

        assertTrue(!gprsSubscriptionData.getCompleteDataListIncluded());
        ArrayList<PDPContext> gprsDataList = gprsSubscriptionData.getGPRSDataList();
        assertNotNull(gprsDataList);
        assertEquals(gprsDataList.size(), 1);
        PDPContext pdpContext = gprsDataList.get(0);
        assertNotNull(pdpContext);
        APN apn = pdpContext.getAPN();
        assertTrue(Arrays.equals(apn.getData(), this.getAPNData()));
        APNOIReplacement apnoiReplacement = pdpContext.getAPNOIReplacement();
        assertTrue(Arrays.equals(apnoiReplacement.getData(), this.getAPNOIReplacementData()));
        ChargingCharacteristics chargingCharacteristics = pdpContext.getChargingCharacteristics();
        assertTrue(Arrays.equals(chargingCharacteristics.getData(), this.getChargingCharacteristicsData()));

        Ext2QoSSubscribed ext2QoSSubscribed = pdpContext.getExt2QoSSubscribed();
        assertTrue(Arrays.equals(ext2QoSSubscribed.getData(), this.getExt2QoSSubscribedData()));
        Ext3QoSSubscribed ext3QoSSubscribed = pdpContext.getExt3QoSSubscribed();
        assertTrue(Arrays.equals(ext3QoSSubscribed.getData(), this.getExt3QoSSubscribedData()));
        Ext4QoSSubscribed ext4QoSSubscribed = pdpContext.getExt4QoSSubscribed();
        assertEquals(ext4QoSSubscribed.getData(), 2);
        MAPExtensionContainer pdpContextExtensionContainer = pdpContext.getExtensionContainer();
        assertNotNull(pdpContextExtensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdpContextExtensionContainer));
        PDPAddress extpdpAddress = pdpContext.getExtPDPAddress();
        assertTrue(Arrays.equals(extpdpAddress.getData(), this.getPDPAddressData2()));
        ExtPDPType extpdpType = pdpContext.getExtPDPType();
        assertTrue(Arrays.equals(extpdpType.getData(), this.getExtPDPTypeData()));
        ExtQoSSubscribed extQoSSubscribed = pdpContext.getExtQoSSubscribed();
        assertTrue(Arrays.equals(extQoSSubscribed.getData(), this.getExtQoSSubscribedData()));
        assertEquals(pdpContext.getLIPAPermission(), LIPAPermission.lipaConditional);
        PDPAddress pdpAddress = pdpContext.getPDPAddress();
        assertTrue(Arrays.equals(pdpAddress.getData(), this.getPDPAddressData()));
        assertEquals(pdpContext.getPDPContextId(), 1);
        PDPType pdpType = pdpContext.getPDPType();
        assertTrue(Arrays.equals(pdpType.getData(), this.getPDPTypeData()));
        QoSSubscribed qosSubscribed = pdpContext.getQoSSubscribed();
        assertTrue(Arrays.equals(qosSubscribed.getData(), this.getQoSSubscribedData()));
        assertEquals(pdpContext.getSIPTOPermission(), SIPTOPermission.siptoAllowed);

        APNOIReplacement apnOiReplacement = gprsSubscriptionData.getApnOiReplacement();
        assertNotNull(apnOiReplacement);
        assertTrue(Arrays.equals(apnOiReplacement.getData(), this.getAPNOIReplacementData()));
        assertNotNull(gprsSubscriptionData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(gprsSubscriptionData.getExtensionContainer()));
        // end gprsSubscriptionData

        // RoamingRestrictedInSgsnDueToUnsupportedFeature
        assertTrue(prim.getRoamingRestrictedInSgsnDueToUnsupportedFeature());

        // NetworkAccessMode
        assertEquals(prim.getNetworkAccessMode(), NetworkAccessMode.packetAndCircuit);

        // start lsaInformation
        LSAInformation lsaInformation = prim.getLSAInformation();
        assertTrue(lsaInformation.getCompleteDataListIncluded());
        assertEquals(lsaInformation.getLSAOnlyAccessIndicator(), LSAOnlyAccessIndicator.accessOutsideLSAsRestricted);
        ArrayList<LSAData> lsaDataList = lsaInformation.getLSADataList();
        assertNotNull(lsaDataList);
        assertEquals(lsaDataList.size(), 1);
        LSAData lsaData = lsaDataList.get(0);
        assertTrue(Arrays.equals(lsaData.getLSAIdentity().getData(), this.getDataLSAIdentity()));
        assertEquals(lsaData.getLSAAttributes().getData(), 5);
        assertTrue(lsaData.getLsaActiveModeIndicator());
        assertNotNull(lsaData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lsaData.getExtensionContainer()));
        assertNotNull(lsaInformation.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lsaInformation.getExtensionContainer()));
        // end lsaInformation

        // LmuIndicator
        assertTrue(prim.getLmuIndicator());

        // lcsPrivacyClass
        LCSInformation lcsInformation = prim.getLCSInformation();
        ArrayList<ISDNAddressString> gmlcList = lcsInformation.getGmlcList();
        assertNotNull(gmlcList);
        assertEquals(gmlcList.size(), 1);
        ISDNAddressString isdnAddressString = gmlcList.get(0);
        assertTrue(isdnAddressString.getAddress().equals("22235"));
        assertEquals(isdnAddressString.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAddressString.getNumberingPlan(), NumberingPlan.ISDN);
        ArrayList<LCSPrivacyClass> lcsPrivacyExceptionList = lcsInformation.getLcsPrivacyExceptionList();
        assertNotNull(lcsPrivacyExceptionList);
        assertEquals(lcsPrivacyExceptionList.size(), 4);
        LCSPrivacyClass lcsPrivacyClass = lcsPrivacyExceptionList.get(0);
        assertEquals(lcsPrivacyClass.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allServices);
        ExtSSStatus ssStatus = lcsPrivacyClass.getSsStatus();
        assertTrue(ssStatus.getBitA());
        assertTrue(ssStatus.getBitP());
        assertTrue(ssStatus.getBitQ());
        assertTrue(ssStatus.getBitR());

        assertEquals(lcsPrivacyClass.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);

        ArrayList<ExternalClient> externalClientList = lcsPrivacyClass.getExternalClientList();
        assertNotNull(externalClientList);
        assertEquals(externalClientList.size(), 1);
        ExternalClient externalClient = externalClientList.get(0);
        MAPExtensionContainer extensionContainerExternalClient = externalClient.getExtensionContainer();
        LCSClientExternalID clientIdentity = externalClient.getClientIdentity();
        ISDNAddressString externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(extensionContainerExternalClient);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerExternalClient));

        ArrayList<LCSClientInternalID> plmnClientList = lcsPrivacyClass.getPLMNClientList();
        assertNotNull(plmnClientList);
        assertEquals(plmnClientList.size(), 2);
        assertEquals(plmnClientList.get(0), LCSClientInternalID.broadcastService);
        assertEquals(plmnClientList.get(1), LCSClientInternalID.oandMHPLMN);

        ArrayList<ExternalClient> extExternalClientList = lcsPrivacyClass.getExtExternalClientList();
        assertNotNull(extExternalClientList);
        assertEquals(extExternalClientList.size(), 1);
        externalClient = extExternalClientList.get(0);
        clientIdentity = externalClient.getClientIdentity();
        externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(externalClient.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(externalClient.getExtensionContainer()));

        ArrayList<ServiceType> serviceTypeList = lcsPrivacyClass.getServiceTypeList();
        assertNotNull(serviceTypeList);
        assertEquals(serviceTypeList.size(), 1);
        ServiceType serviceType = serviceTypeList.get(0);
        assertEquals(serviceType.getServiceTypeIdentity(), 1);
        assertEquals(serviceType.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(serviceType.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(serviceType.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(serviceType.getExtensionContainer()));

        assertNotNull(lcsPrivacyClass.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lcsPrivacyClass.getExtensionContainer()));
        ArrayList<MOLRClass> molrList = lcsInformation.getMOLRList();
        assertNotNull(molrList);
        assertEquals(molrList.size(), 1);
        MOLRClass molrClass = molrList.get(0);
        assertEquals(molrClass.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allServices);
        assertTrue(molrClass.getSsStatus().getBitA());
        assertTrue(molrClass.getSsStatus().getBitP());
        assertTrue(molrClass.getSsStatus().getBitQ());
        assertTrue(molrClass.getSsStatus().getBitR());
        assertNotNull(molrClass.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(molrClass.getExtensionContainer()));

        ArrayList<LCSPrivacyClass> addLcsPrivacyExceptionList = lcsInformation.getAddLcsPrivacyExceptionList();
        assertNotNull(addLcsPrivacyExceptionList);
        assertEquals(addLcsPrivacyExceptionList.size(), 1);
        lcsPrivacyClass = addLcsPrivacyExceptionList.get(0);
        assertEquals(lcsPrivacyClass.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allServices);
        ssStatus = lcsPrivacyClass.getSsStatus();
        assertTrue(ssStatus.getBitA());
        assertTrue(ssStatus.getBitP());
        assertTrue(ssStatus.getBitQ());
        assertTrue(ssStatus.getBitR());

        assertEquals(lcsPrivacyClass.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);

        externalClientList = lcsPrivacyClass.getExternalClientList();
        assertNotNull(externalClientList);
        assertEquals(externalClientList.size(), 1);
        externalClient = externalClientList.get(0);
        extensionContainerExternalClient = externalClient.getExtensionContainer();
        clientIdentity = externalClient.getClientIdentity();
        externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(extensionContainerExternalClient);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerExternalClient));

        plmnClientList = lcsPrivacyClass.getPLMNClientList();
        assertNotNull(plmnClientList);
        assertEquals(plmnClientList.size(), 2);
        assertEquals(plmnClientList.get(0), LCSClientInternalID.broadcastService);
        assertEquals(plmnClientList.get(1), LCSClientInternalID.oandMHPLMN);

        extExternalClientList = lcsPrivacyClass.getExtExternalClientList();
        assertNotNull(extExternalClientList);
        assertEquals(extExternalClientList.size(), 1);
        externalClient = extExternalClientList.get(0);
        clientIdentity = externalClient.getClientIdentity();
        externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(externalClient.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(externalClient.getExtensionContainer()));

        serviceTypeList = lcsPrivacyClass.getServiceTypeList();
        assertNotNull(serviceTypeList);
        assertEquals(serviceTypeList.size(), 1);
        serviceType = serviceTypeList.get(0);
        assertEquals(serviceType.getServiceTypeIdentity(), 1);
        assertEquals(serviceType.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(serviceType.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(serviceType.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(serviceType.getExtensionContainer()));

        assertNotNull(lcsPrivacyClass.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lcsPrivacyClass.getExtensionContainer()));
        // end lcsPrivacyClass

        // istAlertTimer
        assertTrue(prim.getIstAlertTimer().equals(new Integer(21)));

        // superChargerSupportedInHLR
        AgeIndicator superChargerSupportedInHLR = prim.getSuperChargerSupportedInHLR();
        assertEquals(superChargerSupportedInHLR.getData(), this.getAgeIndicatorData());

        // start mcSsInfo
        MCSSInfo mcSsInfo = prim.getMcSsInfo();
        assertEquals(mcSsInfo.getSSCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allServices);
        ExtSSStatus ssStatusMcSsInfo = mcSsInfo.getSSStatus();
        assertTrue(ssStatusMcSsInfo.getBitA());
        assertTrue(ssStatusMcSsInfo.getBitP());
        assertTrue(ssStatusMcSsInfo.getBitQ());
        assertTrue(ssStatusMcSsInfo.getBitR());

        assertEquals(mcSsInfo.getNbrSB(), 2);
        assertEquals(mcSsInfo.getNbrUser(), 4);
        assertNotNull(mcSsInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mcSsInfo.getExtensionContainer()));
        // end mcSsInfo

        // csAllocationRetentionPriority
        CSAllocationRetentionPriority csAllocationRetentionPriority = prim.getCSAllocationRetentionPriority();
        assertEquals(csAllocationRetentionPriority.getData(), 4);

        // end sgsnCamelSubscriptionInfo
        SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo = prim.getSgsnCamelSubscriptionInfo();

        // sgsnCamelSubscriptionInfo
        GPRSCSI gprsCsi = sgsnCamelSubscriptionInfo.getGprsCsi();
        assertNotNull(gprsCsi.getGPRSCamelTDPDataList());
        assertEquals(gprsCsi.getGPRSCamelTDPDataList().size(), 1);
        GPRSCamelTDPData gprsCamelTDPData = gprsCsi.getGPRSCamelTDPDataList().get(0);

        MAPExtensionContainer extensionContainergprsCamelTDPData = gprsCamelTDPData.getExtensionContainer();
        ISDNAddressString gsmSCFAddressSgsnCamelSubscriptionInfo = gprsCamelTDPData.getGsmSCFAddress();
        assertTrue(gsmSCFAddressSgsnCamelSubscriptionInfo.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressSgsnCamelSubscriptionInfo.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressSgsnCamelSubscriptionInfo.getNumberingPlan(), NumberingPlan.ISDN);

        assertEquals(gprsCamelTDPData.getDefaultSessionHandling(), DefaultGPRSHandling.releaseTransaction);
        assertEquals(gprsCamelTDPData.getGPRSTriggerDetectionPoint(), GPRSTriggerDetectionPoint.attachChangeOfPosition);
        assertEquals(gprsCamelTDPData.getServiceKey(), 3);

        assertNotNull(extensionContainergprsCamelTDPData);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainergprsCamelTDPData));

        assertEquals(gprsCsi.getCamelCapabilityHandling().intValue(), 8);
        assertNotNull(gprsCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(gprsCsi.getExtensionContainer()));
        assertTrue(gprsCsi.getCsiActive());
        assertTrue(gprsCsi.getNotificationToCSE());

        SMSCSI moSmsCsi = sgsnCamelSubscriptionInfo.getMoSmsCsi();
        ArrayList<SMSCAMELTDPData> smsCamelTdpDataListSgsnCamelSubscriptionInfo = moSmsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListSgsnCamelSubscriptionInfo);
        assertEquals(smsCamelTdpDataListSgsnCamelSubscriptionInfo.size(), 1);
        SMSCAMELTDPData oneSgsnCamelSubscriptionInfo = smsCamelTdpDataListSgsnCamelSubscriptionInfo.get(0);
        assertNotNull(oneSgsnCamelSubscriptionInfo);
        assertEquals(oneSgsnCamelSubscriptionInfo.getServiceKey(), 3);
        assertEquals(oneSgsnCamelSubscriptionInfo.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressString gsmSCFAddressMoSmsCsi = oneSgsnCamelSubscriptionInfo.getGsmSCFAddress();
        assertTrue(gsmSCFAddressMoSmsCsi.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressMoSmsCsi.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressMoSmsCsi.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oneSgsnCamelSubscriptionInfo.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(oneSgsnCamelSubscriptionInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(oneSgsnCamelSubscriptionInfo.getExtensionContainer()));

        assertNotNull(moSmsCsi.getExtensionContainer());
        assertTrue(moSmsCsi.getCsiActive());
        assertTrue(moSmsCsi.getNotificationToCSE());
        assertEquals(moSmsCsi.getCamelCapabilityHandling().intValue(), 8);

        SMSCSI mtSmsCsi = sgsnCamelSubscriptionInfo.getMtSmsCsi();
        ArrayList<SMSCAMELTDPData> smsCamelTdpDataListMtSmsCsi = mtSmsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListMtSmsCsi);
        assertEquals(smsCamelTdpDataListMtSmsCsi.size(), 1);
        SMSCAMELTDPData oneSmsCamelTdpDataListMtSmsCsi = smsCamelTdpDataListMtSmsCsi.get(0);
        assertNotNull(oneSmsCamelTdpDataListMtSmsCsi);
        assertEquals(oneSmsCamelTdpDataListMtSmsCsi.getServiceKey(), 3);
        assertEquals(oneSmsCamelTdpDataListMtSmsCsi.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressString gsmSCFAddressmtSmsCsi = oneSgsnCamelSubscriptionInfo.getGsmSCFAddress();
        assertTrue(gsmSCFAddressmtSmsCsi.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressmtSmsCsi.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressmtSmsCsi.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oneSgsnCamelSubscriptionInfo.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(oneSgsnCamelSubscriptionInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(oneSgsnCamelSubscriptionInfo.getExtensionContainer()));

        assertNotNull(mtSmsCsi.getExtensionContainer());
        assertFalse(mtSmsCsi.getCsiActive());
        assertFalse(mtSmsCsi.getNotificationToCSE());
        assertEquals(mtSmsCsi.getCamelCapabilityHandling().intValue(), 8);

        ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaListSgsnCamelSubscriptionInfo = sgsnCamelSubscriptionInfo
                .getMtSmsCamelTdpCriteriaList();
        assertNotNull(mtSmsCamelTdpCriteriaListSgsnCamelSubscriptionInfo);
        assertEquals(mtSmsCamelTdpCriteriaListSgsnCamelSubscriptionInfo.size(), 1);
        MTsmsCAMELTDPCriteria mtSmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaListSgsnCamelSubscriptionInfo.get(0);

        ArrayList<MTSMSTPDUType> tPDUTypeCriterionSgsnCamelSubscriptionInfo = mtSmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterionSgsnCamelSubscriptionInfo);
        assertEquals(tPDUTypeCriterionSgsnCamelSubscriptionInfo.size(), 2);
        MTSMSTPDUType oneMTSMSTPDUType = tPDUTypeCriterion.get(0);
        assertNotNull(oneMTSMSTPDUType);
        assertEquals(oneMTSMSTPDUType, MTSMSTPDUType.smsDELIVER);

        MTSMSTPDUType twoMTSMSTPDUType = tPDUTypeCriterion.get(1);
        assertNotNull(twoMTSMSTPDUType);
        assertEquals(twoMTSMSTPDUType, MTSMSTPDUType.smsSTATUSREPORT);
        assertEquals(mtSmsCAMELTDPCriteria.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);

        MGCSI mgCsi = sgsnCamelSubscriptionInfo.getMgCsi();

        ArrayList<MMCode> mobilityTriggersSgsnCamelSubscriptionInfo = mgCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggersSgsnCamelSubscriptionInfo);
        assertEquals(mobilityTriggersSgsnCamelSubscriptionInfo.size(), 2);
        MMCode oneMMCode = mobilityTriggersSgsnCamelSubscriptionInfo.get(0);
        assertNotNull(oneMMCode);
        assertEquals(oneMMCode.getMMCodeValue(), MMCodeValue.GPRSAttach);
        MMCode twoMMCode = mobilityTriggers.get(1);
        assertNotNull(twoMMCode);
        assertEquals(twoMMCode.getMMCodeValue(), MMCodeValue.IMSIAttach);

        assertEquals(mgCsi.getServiceKey(), 3);

        ISDNAddressString gsmSCFAddressMgCsi = mgCsi.getGsmSCFAddress();
        assertTrue(gsmSCFAddressMgCsi.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressMgCsi.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressMgCsi.getNumberingPlan(), NumberingPlan.ISDN);

        assertNotNull(mgCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mgCsi.getExtensionContainer()));
        assertTrue(mgCsi.getCsiActive());
        assertTrue(mgCsi.getNotificationToCSE());

        assertNotNull(sgsnCamelSubscriptionInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(sgsnCamelSubscriptionInfo.getExtensionContainer()));
        // end sgsnCamelSubscriptionInfo

        // chargingCharacteristics
        ChargingCharacteristics chargingCharacteristicsISD = prim.getChargingCharacteristics();
        assertTrue(Arrays.equals(chargingCharacteristicsISD.getData(), this.getChargingCharacteristicsData()));

        // start accessRestrictionData
        AccessRestrictionData accessRestrictionData = prim.getAccessRestrictionData();
        assertTrue(!accessRestrictionData.getUtranNotAllowed());
        assertTrue(accessRestrictionData.getGeranNotAllowed());
        assertTrue(!accessRestrictionData.getGanNotAllowed());
        assertTrue(accessRestrictionData.getIHspaEvolutionNotAllowed());
        assertTrue(!accessRestrictionData.getEUtranNotAllowed());
        assertTrue(accessRestrictionData.getHoToNon3GPPAccessNotAllowed());
        // end accessRestrictionData

        // icsIndicator
        Boolean icsIndicator = prim.getIcsIndicator();
        assertNotNull(icsIndicator);
        assertTrue(icsIndicator);

        // start epsSubscriptionData
        EPSSubscriptionData epsSubscriptionData = prim.getEpsSubscriptionData();
        AMBR ambrepsSubscriptionData = epsSubscriptionData.getAmbr();
        MAPExtensionContainer extensionContainerambrambrepsSubscriptionData = ambrepsSubscriptionData.getExtensionContainer();
        assertEquals(ambrepsSubscriptionData.getMaxRequestedBandwidthDL(), 4);
        assertEquals(ambrepsSubscriptionData.getMaxRequestedBandwidthUL(), 2);
        assertNotNull(extensionContainerambrambrepsSubscriptionData);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerambrambrepsSubscriptionData));
        assertTrue(Arrays.equals(epsSubscriptionData.getApnOiReplacement().getData(), this.getAPNOIReplacementData()));
        MAPExtensionContainer epsSubscriptionDataMAPExtensionContainer = epsSubscriptionData.getExtensionContainer();
        assertNotNull(epsSubscriptionDataMAPExtensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(epsSubscriptionDataMAPExtensionContainer));

        assertTrue(epsSubscriptionData.getMpsCSPriority());
        assertTrue(epsSubscriptionData.getMpsEPSPriority());

        assertEquals(epsSubscriptionData.getRfspId().intValue(), 4);

        ISDNAddressString stnSr = epsSubscriptionData.getStnSr();
        assertTrue(stnSr.getAddress().equals("22228"));
        assertEquals(stnSr.getAddressNature(), AddressNature.international_number);
        assertEquals(stnSr.getNumberingPlan(), NumberingPlan.ISDN);

        APNConfigurationProfile apnConfigurationProfile = epsSubscriptionData.getAPNConfigurationProfile();

        assertEquals(apnConfigurationProfile.getDefaultContext(), 2);
        assertTrue(apnConfigurationProfile.getCompleteDataListIncluded());
        MAPExtensionContainer apnConfigurationProfileExtensionContainer = apnConfigurationProfile.getExtensionContainer();
        assertNotNull(apnConfigurationProfileExtensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(apnConfigurationProfileExtensionContainer));

        ArrayList<APNConfiguration> ePSDataList = apnConfigurationProfile.getEPSDataList();
        assertNotNull(ePSDataList);
        assertEquals(ePSDataList.size(), 1);

        APNConfiguration apnConfiguration = ePSDataList.get(0);
        assertEquals(apnConfiguration.getContextId(), 1);
        assertEquals(apnConfiguration.getPDNType().getPDNTypeValue(), PDNTypeValue.IPv4);
        PDPAddress servedPartyIPIPv4Address = apnConfiguration.getServedPartyIPIPv4Address();
        assertNotNull(servedPartyIPIPv4Address);
        assertTrue(Arrays.equals(this.getPDPAddressData(), servedPartyIPIPv4Address.getData()));
        assertTrue(Arrays.equals(apnConfiguration.getApn().getData(), this.getAPNData()));

        EPSQoSSubscribed ePSQoSSubscribed = apnConfiguration.getEPSQoSSubscribed();
        AllocationRetentionPriority allocationRetentionPriority = ePSQoSSubscribed.getAllocationRetentionPriority();
        MAPExtensionContainer extensionContainerePSQoSSubscribed = ePSQoSSubscribed.getExtensionContainer();
        assertEquals(allocationRetentionPriority.getPriorityLevel(), 1);
        assertTrue(allocationRetentionPriority.getPreEmptionCapability());
        assertTrue(allocationRetentionPriority.getPreEmptionVulnerability());
        assertNotNull(allocationRetentionPriority.getExtensionContainer());
        ;
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(allocationRetentionPriority.getExtensionContainer()));
        assertNotNull(extensionContainerePSQoSSubscribed);
        assertEquals(ePSQoSSubscribed.getQoSClassIdentifier(), QoSClassIdentifier.QCI_1);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerePSQoSSubscribed));

        PDNGWIdentity pdnGWIdentity = apnConfiguration.getPdnGwIdentity();
        PDPAddress pdnGwIpv4Address = pdnGWIdentity.getPdnGwIpv4Address();
        assertNotNull(pdnGwIpv4Address);
        assertTrue(Arrays.equals(this.getPDPAddressData(), pdnGwIpv4Address.getData()));
        PDPAddress pdnGwIpv6Address = pdnGWIdentity.getPdnGwIpv6Address();
        assertNotNull(pdnGwIpv6Address);
        assertTrue(Arrays.equals(this.getPDPAddressData(), pdnGwIpv6Address.getData()));
        FQDN pdnGwName = pdnGWIdentity.getPdnGwName();
        assertNotNull(pdnGwName);
        assertTrue(Arrays.equals(this.getFQDNData(), pdnGwName.getData()));
        assertNotNull(pdnGWIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdnGWIdentity.getExtensionContainer()));

        assertEquals(apnConfiguration.getPdnGwAllocationType(), PDNGWAllocationType._dynamic);
        assertFalse(apnConfiguration.getVplmnAddressAllowed());
        assertTrue(Arrays
                .equals(this.getChargingCharacteristicsData(), apnConfiguration.getChargingCharacteristics().getData()));

        AMBR ambr = apnConfiguration.getAmbr();
        MAPExtensionContainer extensionContainerambr = ambr.getExtensionContainer();
        assertEquals(ambr.getMaxRequestedBandwidthDL(), 4);
        assertEquals(ambr.getMaxRequestedBandwidthUL(), 2);
        assertNotNull(extensionContainerambr);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerambr));

        ArrayList<SpecificAPNInfo> specificAPNInfoList = apnConfiguration.getSpecificAPNInfoList();
        assertNotNull(specificAPNInfoList);
        assertEquals(specificAPNInfoList.size(), 1);
        SpecificAPNInfo specificAPNInfo = specificAPNInfoList.get(0);

        PDNGWIdentity pdnGWIdentitySpecificAPNInfo = specificAPNInfo.getPdnGwIdentity();
        PDPAddress pdnGwIpv4AddressSpecificAPNInfo = pdnGWIdentitySpecificAPNInfo.getPdnGwIpv4Address();
        assertNotNull(pdnGwIpv4AddressSpecificAPNInfo);
        assertTrue(Arrays.equals(this.getPDPAddressData(), pdnGwIpv4AddressSpecificAPNInfo.getData()));
        PDPAddress pdnGwIpv6AddressSpecificAPNInfo = pdnGWIdentitySpecificAPNInfo.getPdnGwIpv6Address();
        assertNotNull(pdnGwIpv6AddressSpecificAPNInfo);
        assertTrue(Arrays.equals(this.getPDPAddressData(), pdnGwIpv6AddressSpecificAPNInfo.getData()));
        FQDN pdnGwNameSpecificAPNInfo = pdnGWIdentitySpecificAPNInfo.getPdnGwName();
        assertNotNull(pdnGwNameSpecificAPNInfo);
        assertTrue(Arrays.equals(this.getFQDNData(), pdnGwNameSpecificAPNInfo.getData()));
        assertNotNull(pdnGWIdentitySpecificAPNInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdnGWIdentitySpecificAPNInfo.getExtensionContainer()));
        MAPExtensionContainer extensionContainerspecificAPNInfo = specificAPNInfo.getExtensionContainer();
        assertNotNull(extensionContainerspecificAPNInfo);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerspecificAPNInfo));

        assertTrue(Arrays.equals(specificAPNInfo.getAPN().getData(), this.getAPNData()));

        PDPAddress servedPartyIPIPv6Address = apnConfiguration.getServedPartyIPIPv6Address();
        assertNotNull(servedPartyIPIPv6Address);
        assertTrue(Arrays.equals(this.getPDPAddressData(), servedPartyIPIPv6Address.getData()));
        assertTrue(Arrays.equals(this.getAPNOIReplacementData(), apnConfiguration.getApnOiReplacement().getData()));
        assertEquals(apnConfiguration.getSiptoPermission(), SIPTOPermission.siptoAllowed);
        assertEquals(apnConfiguration.getLipaPermission(), LIPAPermission.lipaConditional);
        assertNotNull(apnConfiguration.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(apnConfiguration.getExtensionContainer()));
        // end epsSubscriptionData

        // start csgSubscriptionDataList
        ArrayList<CSGSubscriptionData> csgSubscriptionDataList = prim.getCsgSubscriptionDataList();
        assertNotNull(csgSubscriptionDataList);
        assertEquals(csgSubscriptionDataList.size(), 1);
        CSGSubscriptionData csgSubscriptionData = csgSubscriptionDataList.get(0);

        BitSetStrictLength bs = csgSubscriptionData.getCsgId().getData();
        assertTrue(bs.get(0));
        assertFalse(bs.get(1));
        assertFalse(bs.get(25));
        assertTrue(bs.get(26));

        assertTrue(Arrays.equals(csgSubscriptionData.getExpirationDate().getData(), this.getTimeData()));

        ArrayList<APN> lipaAllowedAPNList = csgSubscriptionData.getLipaAllowedAPNList();
        assertNotNull(lipaAllowedAPNList);
        assertEquals(lipaAllowedAPNList.size(), 1);
        assertTrue(Arrays.equals(lipaAllowedAPNList.get(0).getData(), this.getAPNData()));
        assertNotNull(csgSubscriptionData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(csgSubscriptionData.getExtensionContainer()));
        // end csgSubscriptionDataList

        // ueReachabilityRequestIndicator
        assertTrue(prim.getUeReachabilityRequestIndicator());

        // start sgsnNumber
        ISDNAddressString sgsnNumber = prim.getSgsnNumber();
        assertNotNull(sgsnNumber);
        assertTrue(sgsnNumber.getAddress().equals("22228"));
        assertEquals(sgsnNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(sgsnNumber.getNumberingPlan(), NumberingPlan.ISDN);
        // end sgsnNumber

        // mmeName
        DiameterIdentity mmeName = prim.getMmeName();
        assertTrue(Arrays.equals(mmeName.getData(), this.getDiameterIdentity()));

        // SubscribedPeriodicRAUTAUtimer
        assertTrue(prim.getSubscribedPeriodicRAUTAUtimer().equals(new Long(2)));

        // vplmnLIPAAllowed
        assertTrue(prim.getVplmnLIPAAllowed());

        // mdtUserConsent
        Boolean mdtUserConsent = prim.getMdtUserConsent();
        assertNotNull(mdtUserConsent);
        assertTrue(mdtUserConsent);

        // SubscribedPeriodicLAUtimer
        assertTrue(prim.getSubscribedPeriodicLAUtimer().equals(new Long(2)));

        // extensionContainer
        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        // End ISD MAP Protocol Version 3 message Testing

        // Start MAP Protocol Version 2 message Testing
        data = this.getData1();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new InsertSubscriberDataRequestImpl(2);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        // imsi
        imsi = prim.getImsi();
        assertTrue(imsi.getData().equals("1111122222"));

        // msisdn
        msisdn = prim.getMsisdn();
        assertTrue(msisdn.getAddress().equals("22234"));
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);

        // category
        category = prim.getCategory();
        assertEquals(category.getData(), 5);

        // subscriberStatus
        subscriberStatus = prim.getSubscriberStatus();
        assertEquals(subscriberStatus, SubscriberStatus.operatorDeterminedBarring);

        // bearerServiceList
        bearerServiceList = prim.getBearerServiceList();
        assertNotNull(bearerServiceList);
        assertEquals(bearerServiceList.size(), 1);
        extBearerServiceCode = bearerServiceList.get(0);
        assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.Asynchronous9_6kbps);

        // teleserviceList
        teleserviceList = prim.getTeleserviceList();
        assertNotNull(teleserviceList);
        assertEquals(teleserviceList.size(), 1);
        extTeleserviceCode = teleserviceList.get(0);
        assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        // start provisionedSS
        provisionedSS = prim.getProvisionedSS();
        assertNotNull(provisionedSS);
        assertEquals(provisionedSS.size(), 1);
        extSSInfo = provisionedSS.get(0);

        forwardingInfo = extSSInfo.getForwardingInfo();
        callBarringInfo = extSSInfo.getCallBarringInfo();
        cugInfo = extSSInfo.getCugInfo();
        ssData = extSSInfo.getSsData();
        emlppInfo = extSSInfo.getEmlppInfo();

        assertNotNull(forwardingInfo);
        assertNull(callBarringInfo);
        assertNull(cugInfo);
        assertNull(ssData);
        assertNull(emlppInfo);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(forwardingInfo.getExtensionContainer()));
        assertEquals(forwardingInfo.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allServices);

        forwardingFeatureList = forwardingInfo.getForwardingFeatureList();
        ;
        assertNotNull(forwardingFeatureList);
        assertEquals(forwardingFeatureList.size(), 1);
        extForwFeature = forwardingFeatureList.get(0);
        assertNotNull(extForwFeature);

        assertEquals(extForwFeature.getBasicService().getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.Asynchronous9_6kbps);
        assertNull(extForwFeature.getBasicService().getExtTeleservice());
        assertNotNull(extForwFeature.getSsStatus());
        assertTrue(extForwFeature.getSsStatus().getBitA());
        assertTrue(extForwFeature.getSsStatus().getBitP());
        assertTrue(extForwFeature.getSsStatus().getBitQ());
        assertTrue(extForwFeature.getSsStatus().getBitR());

        forwardedToNumber = extForwFeature.getForwardedToNumber();
        assertNotNull(forwardedToNumber);
        assertTrue(forwardedToNumber.getAddress().equals("22228"));
        assertEquals(forwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(forwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertTrue(Arrays.equals(extForwFeature.getForwardedToSubaddress().getData(), this.getISDNSubaddressStringData()));
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToCallingParty());
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToForwardingParty());
        assertTrue(!extForwFeature.getForwardingOptions().getRedirectingPresentation());
        assertEquals(extForwFeature.getForwardingOptions().getExtForwOptionsForwardingReason(),
                ExtForwOptionsForwardingReason.msBusy);
        assertNotNull(extForwFeature.getNoReplyConditionTime());
        assertEquals(extForwFeature.getNoReplyConditionTime().intValue(), 2);
        longForwardedToNumber = extForwFeature.getLongForwardedToNumber();
        assertNotNull(longForwardedToNumber);
        assertTrue(longForwardedToNumber.getAddress().equals("22227"));
        assertEquals(longForwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(longForwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);
        // end provisionedSS

        // start odbData
        odbData = prim.getODBData();
        oDBGeneralData = odbData.getODBGeneralData();
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
        odbHplmnData = odbData.getOdbHplmnData();
        assertTrue(!odbHplmnData.getPlmnSpecificBarringType1());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType2());
        assertTrue(!odbHplmnData.getPlmnSpecificBarringType3());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType4());
        assertNotNull(odbData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(odbData.getExtensionContainer()));
        // end odbData

        // start roamingRestrictionDueToUnsupportedFeature
        assertTrue(prim.getRoamingRestrictionDueToUnsupportedFeature());

        // start regionalSubscriptionData
        regionalSubscriptionData = prim.getRegionalSubscriptionData();
        assertNotNull(regionalSubscriptionData);
        assertEquals(regionalSubscriptionData.size(), 1);
        zoneCode = regionalSubscriptionData.get(0);
        assertEquals(zoneCode.getValue(), 2);
        // end regionalSubscriptionData

        // start vbsSubscriptionData
        vbsSubscriptionData = prim.getVbsSubscriptionData();
        assertNotNull(vbsSubscriptionData);
        assertEquals(vbsSubscriptionData.size(), 1);
        voiceBroadcastData = vbsSubscriptionData.get(0);
        assertTrue(voiceBroadcastData.getGroupId().getGroupId().equals(""));
        assertTrue(voiceBroadcastData.getLongGroupId().getLongGroupId().equals("5"));
        assertTrue(voiceBroadcastData.getBroadcastInitEntitlement());
        assertNotNull(voiceBroadcastData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(voiceBroadcastData.getExtensionContainer()));
        // end vbsSubscriptionData

        // start vgcsSubscriptionData
        vgcsSubscriptionData = prim.getVgcsSubscriptionData();
        assertNotNull(vgcsSubscriptionData);
        assertEquals(vgcsSubscriptionData.size(), 1);
        voiceGroupCallData = vgcsSubscriptionData.get(0);
        assertTrue(voiceGroupCallData.getGroupId().getGroupId().equals(""));
        assertTrue(voiceGroupCallData.getLongGroupId().getLongGroupId().equals("5"));
        assertNotNull(voiceGroupCallData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(voiceGroupCallData.getExtensionContainer()));
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyReset());
        assertFalse(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyUplinkRequest());
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getPrivilegedUplinkRequest());
        assertNotNull(voiceGroupCallData.getAdditionalInfo());
        assertTrue(voiceGroupCallData.getAdditionalInfo().getData().get(0));
        // end vgcsSubscriptionData

        // start vlrCamelSubscriptionInfo
        vlrCamelSubscriptionInfo = prim.getVlrCamelSubscriptionInfo();

        oCsi = vlrCamelSubscriptionInfo.getOCsi();
        lst = oCsi.getOBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        cd = lst.get(0);
        assertEquals(cd.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(cd.getServiceKey(), 3);
        assertEquals(cd.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(cd.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(cd.getGsmSCFAddress().getAddress().equals("1122333"));
        assertEquals(cd.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(cd.getExtensionContainer());

        assertNull(oCsi.getExtensionContainer());
        assertEquals((int) oCsi.getCamelCapabilityHandling(), 2);
        assertFalse(oCsi.getNotificationToCSE());
        assertFalse(oCsi.getCsiActive());

        assertNotNull(vlrCamelSubscriptionInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(vlrCamelSubscriptionInfo.getExtensionContainer()));

        ssCsi = vlrCamelSubscriptionInfo.getSsCsi();
        ssCamelData = ssCsi.getSsCamelData();

        ssEventList = ssCamelData.getSsEventList();
        assertNotNull(ssEventList);
        assertEquals(ssEventList.size(), 1);
        one = ssEventList.get(0);
        assertNotNull(one);
        assertEquals(one.getSupplementaryCodeValue(), SupplementaryCodeValue.allFacsimileTransmissionServices);
        gsmSCFAddress = ssCamelData.getGsmSCFAddress();
        assertTrue(gsmSCFAddress.getAddress().equals("22235"));
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(ssCamelData.getExtensionContainer());
        assertNotNull(ssCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ssCsi.getExtensionContainer()));
        assertTrue(ssCsi.getCsiActive());
        assertTrue(ssCsi.getNotificationToCSE());

        oBcsmCamelTDPCriteriaList = vlrCamelSubscriptionInfo.getOBcsmCamelTDPCriteriaList();
        assertNotNull(oBcsmCamelTDPCriteriaList);
        assertEquals(oBcsmCamelTDPCriteriaList.size(), 1);
        oBcsmCamelTdpCriteria = oBcsmCamelTDPCriteriaList.get(0);
        assertNotNull(oBcsmCamelTdpCriteria);

        destinationNumberCriteria = oBcsmCamelTdpCriteria.getDestinationNumberCriteria();
        destinationNumberList = destinationNumberCriteria.getDestinationNumberList();
        assertNotNull(destinationNumberList);
        assertEquals(destinationNumberList.size(), 2);
        destinationNumberOne = destinationNumberList.get(0);
        assertNotNull(destinationNumberOne);
        assertTrue(destinationNumberOne.getAddress().equals("22234"));
        assertEquals(destinationNumberOne.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberOne.getNumberingPlan(), NumberingPlan.ISDN);
        destinationNumberTwo = destinationNumberList.get(1);
        assertNotNull(destinationNumberTwo);
        assertTrue(destinationNumberTwo.getAddress().equals("22235"));
        assertEquals(destinationNumberTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(destinationNumberCriteria.getMatchType().getCode(), MatchType.enabling.getCode());
        destinationNumberLengthList = destinationNumberCriteria.getDestinationNumberLengthList();
        assertNotNull(destinationNumberLengthList);
        assertEquals(destinationNumberLengthList.size(), 3);
        assertEquals(destinationNumberLengthList.get(0).intValue(), 2);
        assertEquals(destinationNumberLengthList.get(1).intValue(), 4);
        assertEquals(destinationNumberLengthList.get(2).intValue(), 1);
        assertEquals(oBcsmCamelTdpCriteria.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
        assertNotNull(oBcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        basicServiceOne = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(0);
        assertNotNull(basicServiceOne);
        assertEquals(basicServiceOne.getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.Asynchronous9_6kbps);

        basicServiceTwo = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(1);
        assertNotNull(basicServiceTwo);
        assertEquals(basicServiceTwo.getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.allSpeechTransmissionServices);

        assertEquals(oBcsmCamelTdpCriteria.getCallTypeCriteria(), CallTypeCriteria.forwarded);
        oCauseValueCriteria = oBcsmCamelTdpCriteria.getOCauseValueCriteria();
        assertNotNull(oCauseValueCriteria);
        assertEquals(oCauseValueCriteria.size(), 1);
        assertNotNull(oCauseValueCriteria.get(0));
        assertEquals(oCauseValueCriteria.get(0).getData(), 7);

        assertFalse(vlrCamelSubscriptionInfo.getTifCsi());

        mCsi = vlrCamelSubscriptionInfo.getMCsi();
        mobilityTriggers = mCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggers);
        assertEquals(mobilityTriggers.size(), 2);
        mmCode = mobilityTriggers.get(0);
        assertNotNull(mmCode);
        assertEquals(MMCodeValue.GPRSAttach, mmCode.getMMCodeValue());
        mmCode2 = mobilityTriggers.get(1);
        assertNotNull(mmCode2);
        assertEquals(MMCodeValue.IMSIAttach, mmCode2.getMMCodeValue());
        assertNotNull(mCsi.getServiceKey());
        assertEquals(mCsi.getServiceKey(), 3);
        gsmSCFAddressTwo = mCsi.getGsmSCFAddress();
        assertTrue(gsmSCFAddressTwo.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(mCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mCsi.getExtensionContainer()));
        assertTrue(mCsi.getCsiActive());
        assertTrue(mCsi.getNotificationToCSE());

        smsCsi = vlrCamelSubscriptionInfo.getSmsCsi();
        smsCamelTdpDataList = smsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataList);
        assertEquals(smsCamelTdpDataList.size(), 1);
        smsCAMELTDPData = smsCamelTdpDataList.get(0);
        assertNotNull(smsCAMELTDPData);
        assertEquals(smsCAMELTDPData.getServiceKey(), 3);
        assertEquals(smsCAMELTDPData.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        gsmSCFAddressSmsCAMELTDPData = smsCAMELTDPData.getGsmSCFAddress();
        assertTrue(gsmSCFAddressSmsCAMELTDPData.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPData.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPData.getExtensionContainer()));
        assertTrue(smsCsi.getCsiActive());
        assertTrue(smsCsi.getNotificationToCSE());
        assertEquals(smsCsi.getCamelCapabilityHandling().intValue(), 8);

        vtCsi = vlrCamelSubscriptionInfo.getVtCsi();
        tbcsmCamelTDPDatalst = vtCsi.getTBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        tbcsmCamelTDPData = tbcsmCamelTDPDatalst.get(0);
        assertEquals(tbcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.termAttemptAuthorized);
        assertEquals(tbcsmCamelTDPData.getServiceKey(), 3);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(tbcsmCamelTDPData.getGsmSCFAddress().getAddress().equals("22235"));
        assertEquals(tbcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(tbcsmCamelTDPData.getExtensionContainer());
        assertNull(vtCsi.getExtensionContainer());
        assertEquals((int) vtCsi.getCamelCapabilityHandling(), 2);
        assertFalse(vtCsi.getNotificationToCSE());
        assertFalse(vtCsi.getCsiActive());

        tBcsmCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getTBcsmCamelTdpCriteriaList();
        assertNotNull(tBcsmCamelTdpCriteriaList);
        assertEquals(tBcsmCamelTdpCriteriaList.size(), 1);
        assertNotNull(tBcsmCamelTdpCriteriaList.get(0));
        tbcsmCamelTdpCriteria = tBcsmCamelTdpCriteriaList.get(0);
        assertEquals(tbcsmCamelTdpCriteria.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(tbcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(0));
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(1));
        oCauseValueCriteriaLst = tbcsmCamelTdpCriteria.getTCauseValueCriteria();
        assertNotNull(oCauseValueCriteriaLst);
        assertEquals(oCauseValueCriteriaLst.size(), 2);
        assertNotNull(oCauseValueCriteriaLst.get(0));
        assertEquals(oCauseValueCriteriaLst.get(0).getData(), 7);
        assertNotNull(oCauseValueCriteriaLst.get(1));
        assertEquals(oCauseValueCriteriaLst.get(1).getData(), 6);

        dCsi = vlrCamelSubscriptionInfo.getDCsi();
        dpAnalysedInfoCriteriaList = dCsi.getDPAnalysedInfoCriteriaList();
        assertNotNull(dpAnalysedInfoCriteriaList);
        assertEquals(dpAnalysedInfoCriteriaList.size(), 1);
        dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
        assertNotNull(dpAnalysedInfoCriterium);
        dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
        assertTrue(dialledNumber.getAddress().equals("22234"));
        assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
        gsmSCFAddressDp = dpAnalysedInfoCriterium.getGsmSCFAddress();
        assertTrue(gsmSCFAddressDp.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressDp.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressDp.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNotNull(dCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(dCsi.getExtensionContainer()));
        assertEquals(dCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(dCsi.getCsiActive());
        assertTrue(dCsi.getNotificationToCSE());

        mtSmsCSI = vlrCamelSubscriptionInfo.getMtSmsCSI();
        smsCamelTdpDataListOfmtSmsCSI = mtSmsCSI.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListOfmtSmsCSI);
        assertEquals(smsCamelTdpDataListOfmtSmsCSI.size(), 1);
        smsCAMELTDPDataOfMtSmsCSI = smsCamelTdpDataListOfmtSmsCSI.get(0);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getServiceKey(), 3);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        gsmSCFAddressOfMtSmsCSI = smsCAMELTDPDataOfMtSmsCSI.getGsmSCFAddress();
        assertTrue(gsmSCFAddressOfMtSmsCSI.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressOfMtSmsCSI.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressOfMtSmsCSI.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer()));
        assertNotNull(mtSmsCSI.getExtensionContainer());
        assertTrue(mtSmsCSI.getCsiActive());
        assertTrue(mtSmsCSI.getNotificationToCSE());
        assertEquals(mtSmsCSI.getCamelCapabilityHandling().intValue(), 8);

        mtSmsCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getMtSmsCamelTdpCriteriaList();
        assertNotNull(mtSmsCamelTdpCriteriaList);
        assertEquals(mtSmsCamelTdpCriteriaList.size(), 1);
        mtsmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaList.get(0);

        tPDUTypeCriterion = mtsmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterion);
        assertEquals(tPDUTypeCriterion.size(), 2);
        mtSMSTPDUTypeOne = tPDUTypeCriterion.get(0);
        assertNotNull(mtSMSTPDUTypeOne);
        assertEquals(mtSMSTPDUTypeOne, MTSMSTPDUType.smsDELIVER);

        mtSMSTPDUTypeTwo = tPDUTypeCriterion.get(1);
        assertNotNull(mtSMSTPDUTypeTwo);
        assertTrue(mtSMSTPDUTypeTwo == MTSMSTPDUType.smsSTATUSREPORT);
        assertEquals(mtsmsCAMELTDPCriteria.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        // end vlrCamelSubscriptionInfo

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        IMSI imsi = new IMSIImpl("1111122222");
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22234");
        Category category = new CategoryImpl(5);
        SubscriberStatus subscriberStatus = SubscriberStatus.operatorDeterminedBarring;

        // bearerServiceList
        ArrayList<ExtBearerServiceCode> bearerServiceList = new ArrayList<ExtBearerServiceCode>();
        ExtBearerServiceCodeImpl extBearerServiceCode = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.Asynchronous9_6kbps);
        bearerServiceList.add(extBearerServiceCode);

        // teleserviceList
        ArrayList<ExtTeleserviceCode> teleserviceList = new ArrayList<ExtTeleserviceCode>();
        ExtTeleserviceCode extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allSpeechTransmissionServices);
        teleserviceList.add(extTeleservice);

        // provisionedSS
        ArrayList<ExtSSInfo> provisionedSS = new ArrayList<ExtSSInfo>();
        SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.allServices);
        ArrayList<ExtForwFeature> forwardingFeatureList = new ArrayList<ExtForwFeature>();
        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.Asynchronous9_6kbps);
        ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(b);
        ExtSSStatusImpl ssStatus = new ExtSSStatusImpl(true, true, true, true);
        ISDNAddressString forwardedToNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        ISDNSubaddressString forwardedToSubaddress = new ISDNSubaddressStringImpl(this.getISDNSubaddressStringData());
        ExtForwOptions forwardingOptions = new ExtForwOptionsImpl(true, false, true, ExtForwOptionsForwardingReason.msBusy);
        Integer noReplyConditionTime = new Integer(2);
        FTNAddressString longForwardedToNumber = new FTNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "22227");
        ExtForwFeatureImpl extForwFeature = new ExtForwFeatureImpl(basicService, ssStatus, forwardedToNumber,
                forwardedToSubaddress, forwardingOptions, noReplyConditionTime, extensionContainer, longForwardedToNumber);
        forwardingFeatureList.add(extForwFeature);
        ExtForwInfo forwardingInfo = new ExtForwInfoImpl(ssCode, forwardingFeatureList, extensionContainer);
        ExtSSInfoImpl extSSInfo = new ExtSSInfoImpl(forwardingInfo);
        provisionedSS.add(extSSInfo);

        // odbData
        ODBGeneralData oDBGeneralData = new ODBGeneralDataImpl(false, true, false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false,
                true, false);
        ODBHPLMNData odbHplmnData = new ODBHPLMNDataImpl(false, true, false, true);
        ODBData odbData = new ODBDataImpl(oDBGeneralData, odbHplmnData, extensionContainer);

        // roamingRestrictionDueToUnsupportedFeature
        boolean roamingRestrictionDueToUnsupportedFeature = true;

        // regionalSubscriptionData
        ArrayList<ZoneCode> regionalSubscriptionData = new ArrayList<ZoneCode>();
        ZoneCodeImpl zoneCode = new ZoneCodeImpl(2);
        regionalSubscriptionData.add(zoneCode);

        // vbsSubscriptionData
        ArrayList<VoiceBroadcastData> vbsSubscriptionData = new ArrayList<VoiceBroadcastData>();
        GroupId groupId = new GroupIdImpl("4");
        boolean broadcastInitEntitlement = true;
        LongGroupId longGroupId = new LongGroupIdImpl("5");
        VoiceBroadcastDataImpl voiceBroadcastData = new VoiceBroadcastDataImpl(groupId, broadcastInitEntitlement,
                extensionContainer, longGroupId);
        vbsSubscriptionData.add(voiceBroadcastData);

        // vgcsSubscriptionData
        ArrayList<VoiceGroupCallData> vgcsSubscriptionData = new ArrayList<VoiceGroupCallData>();
        AdditionalSubscriptions additionalSubscriptions = new AdditionalSubscriptionsImpl(true, false, true);
        BitSetStrictLength bitSetStrictLength = new BitSetStrictLength(1);
        bitSetStrictLength.set(0);
        AdditionalInfo additionalInfo = new AdditionalInfoImpl(bitSetStrictLength);
        VoiceGroupCallDataImpl voiceGroupCallData = new VoiceGroupCallDataImpl(groupId, extensionContainer,
                additionalSubscriptions, additionalInfo, longGroupId);
        vgcsSubscriptionData.add(voiceGroupCallData);

        // start vlrCamelSubscriptionInfo
        TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint = TBcsmTriggerDetectionPoint.tBusy;
        ArrayList<ExtBasicServiceCode> basicServiceCriteria = new ArrayList<ExtBasicServiceCode>();
        ExtBasicServiceCodeImpl basicServiceOne = new ExtBasicServiceCodeImpl(b);
        ExtBasicServiceCodeImpl basicServiceTwo = new ExtBasicServiceCodeImpl(extTeleservice);
        basicServiceCriteria.add(basicServiceOne);
        basicServiceCriteria.add(basicServiceTwo);

        ArrayList<CauseValue> tCauseValueCriteria = new ArrayList<CauseValue>();
        tCauseValueCriteria.add(new CauseValueImpl(7));
        tCauseValueCriteria.add(new CauseValueImpl(6));

        ISDNAddressStringImpl gsmSCFAddressOne = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "1122333");
        OBcsmCamelTDPDataImpl cind = new OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint.routeSelectFailure, 3,
                gsmSCFAddressOne, DefaultCallHandling.releaseCall, null);
        ArrayList<OBcsmCamelTDPData> lst = new ArrayList<OBcsmCamelTDPData>();
        lst.add(cind);

        OCSI oCsi = new OCSIImpl(lst, null, 2, false, false);
        ArrayList<SSCode> ssEventList = new ArrayList<SSCode>();
        ssEventList.add(new SSCodeImpl(SupplementaryCodeValue.allFacsimileTransmissionServices.getCode()));
        ISDNAddressString gsmSCFAddressTwo = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22235");
        SSCamelData ssCamelData = new SSCamelDataImpl(ssEventList, gsmSCFAddressTwo, extensionContainer);
        boolean notificationToCSE = true;
        boolean csiActive = true;

        SSCSI ssCsi = new SSCSIImpl(ssCamelData, extensionContainer, notificationToCSE, csiActive);

        ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList = new ArrayList<OBcsmCamelTdpCriteria>();
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

        CallTypeCriteria callTypeCriteria = CallTypeCriteria.forwarded;
        ArrayList<CauseValue> oCauseValueCriteria = new ArrayList<CauseValue>();
        oCauseValueCriteria.add(new CauseValueImpl(7));

        OBcsmCamelTdpCriteriaImpl oBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl(oBcsmTriggerDetectionPoint,
                destinationNumberCriteria, basicServiceCriteria, callTypeCriteria, oCauseValueCriteria, extensionContainer);
        oBcsmCamelTDPCriteriaList.add(oBcsmCamelTdpCriteria);

        boolean tifCsi = false;

        ArrayList<MMCode> mobilityTriggers = new ArrayList<MMCode>();
        Long serviceKey = new Long(3);
        ISDNAddressString gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22235");
        ;
        mobilityTriggers.add(new MMCodeImpl(MMCodeValue.GPRSAttach));
        mobilityTriggers.add(new MMCodeImpl(MMCodeValue.IMSIAttach));

        MCSI mCsi = new MCSIImpl(mobilityTriggers, serviceKey, gsmSCFAddress, extensionContainer, notificationToCSE, csiActive);

        SMSTriggerDetectionPoint smsTriggerDetectionPoint = SMSTriggerDetectionPoint.smsCollectedInfo;
        DefaultSMSHandling defaultSMSHandling = DefaultSMSHandling.continueTransaction;

        ArrayList<SMSCAMELTDPData> smsCamelTdpDataList = new ArrayList<SMSCAMELTDPData>();
        SMSCAMELTDPDataImpl smsCAMELTDPData = new SMSCAMELTDPDataImpl(smsTriggerDetectionPoint, serviceKey, gsmSCFAddress,
                defaultSMSHandling, extensionContainer);
        smsCamelTdpDataList.add(smsCAMELTDPData);

        Integer camelCapabilityHandling = new Integer(8);

        SMSCSI smsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE,
                csiActive);

        TBcsmCamelTDPDataImpl tBcsmCamelTDPData = new TBcsmCamelTDPDataImpl(TBcsmTriggerDetectionPoint.termAttemptAuthorized,
                3, gsmSCFAddress, DefaultCallHandling.releaseCall, null);
        ArrayList<TBcsmCamelTDPData> tBcsmCamelTDPDatalst = new ArrayList<TBcsmCamelTDPData>();
        tBcsmCamelTDPDatalst.add(tBcsmCamelTDPData);
        TCSI vtCsi = new TCSIImpl(tBcsmCamelTDPDatalst, null, 2, false, false);

        TBcsmCamelTdpCriteriaImpl tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint,
                basicServiceCriteria, tCauseValueCriteria);
        ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList = new ArrayList<TBcsmCamelTdpCriteria>();
        tBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);

        ISDNAddressStringImpl dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22234");

        DPAnalysedInfoCriteriumImpl dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, 7, gsmSCFAddress,
                DefaultCallHandling.continueCall, extensionContainer);

        ArrayList<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = new ArrayList<DPAnalysedInfoCriterium>();
        dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);

        DCSI dCsi = new DCSIImpl(dpAnalysedInfoCriteriaList, 2, extensionContainer, true, true);

        SMSCSI mtSmsCSI = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE,
                csiActive);
        ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList = new ArrayList<MTsmsCAMELTDPCriteria>();
        ArrayList<MTSMSTPDUType> tPDUTypeCriterion = new ArrayList<MTSMSTPDUType>();
        tPDUTypeCriterion.add(MTSMSTPDUType.smsDELIVER);
        tPDUTypeCriterion.add(MTSMSTPDUType.smsSTATUSREPORT);

        MTsmsCAMELTDPCriteriaImpl mTsmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl(smsTriggerDetectionPoint,
                tPDUTypeCriterion);
        mtSmsCamelTdpCriteriaList.add(mTsmsCAMELTDPCriteria);

        VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo = new VlrCamelSubscriptionInfoImpl(oCsi, extensionContainer, ssCsi,
                oBcsmCamelTDPCriteriaList, tifCsi, mCsi, smsCsi, vtCsi, tBcsmCamelTdpCriteriaList, dCsi, mtSmsCSI,
                mtSmsCamelTdpCriteriaList);
        // end vlrCamelSubscriptionInfo

        // naeaPreferredCI
        NAEACIC naeaPreferredCIC = new NAEACICImpl(this.getNAEACICIData());
        NAEAPreferredCI naeaPreferredCI = new NAEAPreferredCIImpl(naeaPreferredCIC, extensionContainer);

        // start gprsSubscriptionData
        int pdpContextId = 1;
        PDPType pdpType = new PDPTypeImpl(this.getPDPTypeData());
        PDPAddress pdpAddress = new PDPAddressImpl(this.getPDPAddressData());
        QoSSubscribed qosSubscribed = new QoSSubscribedImpl(this.getQoSSubscribedData());
        boolean vplmnAddressAllowed = false;
        APN apn = new APNImpl(this.getAPNData());
        ExtQoSSubscribed extQoSSubscribed = new ExtQoSSubscribedImpl(this.getExtQoSSubscribedData());
        ChargingCharacteristics chargingCharacteristics = new ChargingCharacteristicsImpl(this.getChargingCharacteristicsData());
        Ext2QoSSubscribed ext2QoSSubscribed = new Ext2QoSSubscribedImpl(this.getExt2QoSSubscribedData());
        Ext3QoSSubscribed ext3QoSSubscribed = new Ext3QoSSubscribedImpl(this.getExt3QoSSubscribedData());
        Ext4QoSSubscribed ext4QoSSubscribed = new Ext4QoSSubscribedImpl(2);
        APNOIReplacement apnoiReplacement = new APNOIReplacementImpl(this.getAPNOIReplacementData());
        ExtPDPType extpdpType = new ExtPDPTypeImpl(this.getExtPDPTypeData());
        PDPAddress extpdpAddress = new PDPAddressImpl(this.getPDPAddressData2());
        ;
        SIPTOPermission sipToPermission = SIPTOPermission.siptoAllowed;
        LIPAPermission lipaPermission = LIPAPermission.lipaConditional;

        PDPContext pdpContext = new PDPContextImpl(pdpContextId, pdpType, pdpAddress, qosSubscribed, vplmnAddressAllowed, apn,
                extensionContainer, extQoSSubscribed, chargingCharacteristics, ext2QoSSubscribed, ext3QoSSubscribed,
                ext4QoSSubscribed, apnoiReplacement, extpdpType, extpdpAddress, sipToPermission, lipaPermission);
        ArrayList<PDPContext> gprsDataList = new ArrayList<PDPContext>();
        gprsDataList.add(pdpContext);

        APNOIReplacement apnOiReplacement = new APNOIReplacementImpl(this.getAPNOIReplacementData());
        GPRSSubscriptionData gprsSubscriptionData = new GPRSSubscriptionDataImpl(false, gprsDataList, extensionContainer,
                apnOiReplacement);
        // end gprsSubscriptionData

        // roamingRestrictedInSgsnDueToUnsupportedFeature
        boolean roamingRestrictedInSgsnDueToUnsupportedFeature = true;

        // networkAccessMode
        NetworkAccessMode networkAccessMode = NetworkAccessMode.packetAndCircuit;

        // start lsaInformation
        boolean completeDataListIncluded = true;
        LSAOnlyAccessIndicator lsaOnlyAccessIndicator = LSAOnlyAccessIndicator.accessOutsideLSAsRestricted;
        ArrayList<LSAData> lsaDataList = new ArrayList<LSAData>();
        LSAIdentity lsaIdentity = new LSAIdentityImpl(this.getDataLSAIdentity());
        LSAAttributes lsaAttributes = new LSAAttributesImpl(5);
        boolean lsaActiveModeIndicator = true;
        LSAData lsaData = new LSADataImpl(lsaIdentity, lsaAttributes, lsaActiveModeIndicator, extensionContainer);
        lsaDataList.add(lsaData);
        LSAInformation lsaInformation = new LSAInformationImpl(completeDataListIncluded, lsaOnlyAccessIndicator, lsaDataList,
                extensionContainer);
        // end lsaInformation

        // lmuIndicator
        boolean lmuIndicator = true;

        // start lcsInformation
        ArrayList<ISDNAddressString> gmlcList = new ArrayList<ISDNAddressString>();
        ISDNAddressString isdnAddressString = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22235");
        gmlcList.add(isdnAddressString);

        ArrayList<LCSPrivacyClass> lcsPrivacyExceptionList = new ArrayList<LCSPrivacyClass>();
        NotificationToMSUser notificationToMSUser = NotificationToMSUser.locationNotAllowed;
        ArrayList<ExternalClient> externalClientList = new ArrayList<ExternalClient>();
        ISDNAddressString externalAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        LCSClientExternalID clientIdentity = new LCSClientExternalIDImpl(externalAddress, extensionContainer);
        GMLCRestriction gmlcRestriction = GMLCRestriction.gmlcList;
        ExternalClient externalClient = new ExternalClientImpl(clientIdentity, gmlcRestriction, notificationToMSUser,
                extensionContainer);
        externalClientList.add(externalClient);

        ArrayList<LCSClientInternalID> plmnClientList = new ArrayList<LCSClientInternalID>();
        LCSClientInternalID lcsClientInternalIdOne = LCSClientInternalID.broadcastService;
        LCSClientInternalID lcsClientInternalIdTwo = LCSClientInternalID.oandMHPLMN;
        plmnClientList.add(lcsClientInternalIdOne);
        plmnClientList.add(lcsClientInternalIdTwo);

        ArrayList<ExternalClient> extExternalClientList = new ArrayList<ExternalClient>();
        extExternalClientList.add(externalClient);

        ArrayList<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
        int serviceTypeIdentity = 1;
        ServiceType serviceType = new ServiceTypeImpl(serviceTypeIdentity, gmlcRestriction, notificationToMSUser,
                extensionContainer);
        serviceTypeList.add(serviceType);

        SSCode ssCodeTwo = new SSCodeImpl(SupplementaryCodeValue.allFacsimileTransmissionServices);
        SSCode ssCodeThree = new SSCodeImpl(SupplementaryCodeValue.allShortMessageServices);
        SSCode ssCodeFour = new SSCodeImpl(SupplementaryCodeValue.allSpeechTransmissionServices);
        LCSPrivacyClass lcsPrivacyClassOne = new LCSPrivacyClassImpl(ssCode, ssStatus, notificationToMSUser,
                externalClientList, plmnClientList, extensionContainer, extExternalClientList, serviceTypeList);
        LCSPrivacyClass lcsPrivacyClassTwo = new LCSPrivacyClassImpl(ssCodeTwo, ssStatus, notificationToMSUser,
                externalClientList, plmnClientList, extensionContainer, extExternalClientList, serviceTypeList);
        LCSPrivacyClass lcsPrivacyClassThree = new LCSPrivacyClassImpl(ssCodeThree, ssStatus, notificationToMSUser,
                externalClientList, plmnClientList, extensionContainer, extExternalClientList, serviceTypeList);
        LCSPrivacyClass lcsPrivacyClassFour = new LCSPrivacyClassImpl(ssCodeFour, ssStatus, notificationToMSUser,
                externalClientList, plmnClientList, extensionContainer, extExternalClientList, serviceTypeList);

        lcsPrivacyExceptionList.add(lcsPrivacyClassOne);
        lcsPrivacyExceptionList.add(lcsPrivacyClassTwo);
        lcsPrivacyExceptionList.add(lcsPrivacyClassThree);
        lcsPrivacyExceptionList.add(lcsPrivacyClassFour);

        ArrayList<MOLRClass> molrList = new ArrayList<MOLRClass>();
        MOLRClass molrClass = new MOLRClassImpl(ssCode, ssStatus, extensionContainer);
        molrList.add(molrClass);

        ArrayList<LCSPrivacyClass> addLcsPrivacyExceptionList = new ArrayList<LCSPrivacyClass>();
        addLcsPrivacyExceptionList.add(lcsPrivacyClassOne);

        LCSInformation lcsInformation = new LCSInformationImpl(gmlcList, lcsPrivacyExceptionList, molrList,
                addLcsPrivacyExceptionList);
        // end lcsInformation

        // start istAlertTimer
        Integer istAlertTimer = new Integer(21);

        // superChargerSupportedInHLR
        AgeIndicator superChargerSupportedInHLR = new AgeIndicatorImpl(this.getAgeIndicatorData());

        // start mcSsInfo
        MCSSInfo mcSsInfo = new MCSSInfoImpl(ssCode, ssStatus, 2, 4, extensionContainer);
        // end mcSsInfo

        // start csAllocationRetentionPriority
        CSAllocationRetentionPriority csAllocationRetentionPriority = new CSAllocationRetentionPriorityImpl(4);

        // start sgsnCamelSubscriptionInfo
        GPRSTriggerDetectionPoint gprsTriggerDetectionPoint = GPRSTriggerDetectionPoint.attachChangeOfPosition;
        DefaultGPRSHandling defaultSessionHandling = DefaultGPRSHandling.releaseTransaction;
        GPRSCamelTDPDataImpl gprsCamelTDPData = new GPRSCamelTDPDataImpl(gprsTriggerDetectionPoint, serviceKey, gsmSCFAddress,
                defaultSessionHandling, extensionContainer);
        ArrayList<GPRSCamelTDPData> gprsCamelTDPDataList = new ArrayList<GPRSCamelTDPData>();
        gprsCamelTDPDataList.add(gprsCamelTDPData);
        GPRSCSI gprsCsi = new GPRSCSIImpl(gprsCamelTDPDataList, camelCapabilityHandling, extensionContainer, notificationToCSE,
                csiActive);
        SMSCSI moSmsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE,
                csiActive);
        SMSCSI mtSmsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, false, false);
        MGCSI mgCsi = new MGCSIImpl(mobilityTriggers, 3, gsmSCFAddress, extensionContainer, true, true);
        SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo = new SGSNCAMELSubscriptionInfoImpl(gprsCsi, moSmsCsi,
                extensionContainer, mtSmsCsi, mtSmsCamelTdpCriteriaList, mgCsi);
        // end sgsnCamelSubscriptionInfo

        // accessRestrictionData
        AccessRestrictionData accessRestrictionData = new AccessRestrictionDataImpl(false, true, false, true, false, true);

        // icsIndicator
        Boolean icsIndicator = Boolean.TRUE;

        // start epsSubscriptionData
        Integer rfspId = new Integer(4);
        AMBR ambr = new AMBRImpl(2, 4, extensionContainer);
        int defaultContext = 2;
        ArrayList<APNConfiguration> ePSDataList = new ArrayList<APNConfiguration>();
        int contextId = 1;
        PDNType pDNType = new PDNTypeImpl(PDNTypeValue.IPv4);
        PDPAddress servedPartyIPIPv4Address = new PDPAddressImpl(this.getPDPAddressData());
        QoSClassIdentifier qoSClassIdentifier = QoSClassIdentifier.QCI_1;
        AllocationRetentionPriority allocationRetentionPriority = new AllocationRetentionPriorityImpl(1, Boolean.TRUE,
                Boolean.TRUE, extensionContainer);
        EPSQoSSubscribed ePSQoSSubscribed = new EPSQoSSubscribedImpl(qoSClassIdentifier, allocationRetentionPriority,
                extensionContainer);
        PDPAddress pdnGwIpv4Address = new PDPAddressImpl(this.getPDPAddressData());
        PDPAddress pdnGwIpv6Address = new PDPAddressImpl(this.getPDPAddressData());
        FQDN pdnGwName = new FQDNImpl(this.getFQDNData());
        PDNGWIdentity pdnGwIdentity = new PDNGWIdentityImpl(pdnGwIpv4Address, pdnGwIpv6Address, pdnGwName, extensionContainer);
        PDNGWAllocationType pdnGwAllocationType = PDNGWAllocationType._dynamic;
        SpecificAPNInfo specificAPNInfo = new SpecificAPNInfoImpl(apn, pdnGwIdentity, extensionContainer);
        ArrayList<SpecificAPNInfo> specificAPNInfoList = new ArrayList<SpecificAPNInfo>();
        specificAPNInfoList.add(specificAPNInfo);
        PDPAddress servedPartyIPIPv6Address = new PDPAddressImpl(this.getPDPAddressData());
        SIPTOPermission siptoPermission = SIPTOPermission.siptoAllowed;
        APNConfiguration APNConfiguration = new APNConfigurationImpl(contextId, pDNType, servedPartyIPIPv4Address, apn,
                ePSQoSSubscribed, pdnGwIdentity, pdnGwAllocationType, vplmnAddressAllowed, chargingCharacteristics, ambr,
                specificAPNInfoList, extensionContainer, servedPartyIPIPv6Address, apnOiReplacement, siptoPermission,
                lipaPermission);
        ePSDataList.add(APNConfiguration);
        APNConfigurationProfile apnConfigurationProfile = new APNConfigurationProfileImpl(defaultContext,
                completeDataListIncluded, ePSDataList, extensionContainer);
        ISDNAddressString stnSr = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");
        boolean mpsCSPriority = true;
        boolean mpsEPSPriority = true;
        EPSSubscriptionData epsSubscriptionData = new EPSSubscriptionDataImpl(apnOiReplacement, rfspId, ambr,
                apnConfigurationProfile, stnSr, extensionContainer, mpsCSPriority, mpsEPSPriority);
        // end epsSubscriptionData

        // start csgSubscriptionDataList
        ArrayList<CSGSubscriptionData> csgSubscriptionDataList = new ArrayList<CSGSubscriptionData>();
        BitSetStrictLength bs = new BitSetStrictLength(27);
        bs.set(0);
        bs.set(26);
        CSGId csgId = new CSGIdImpl(bs);
        Time expirationDate = new TimeImpl(this.getTimeData());
        ArrayList<APN> lipaAllowedAPNList = new ArrayList<APN>();
        lipaAllowedAPNList.add(apn);
        CSGSubscriptionDataImpl csgSubscriptionData = new CSGSubscriptionDataImpl(csgId, expirationDate, extensionContainer,
                lipaAllowedAPNList);
        csgSubscriptionDataList.add(csgSubscriptionData);
        // end csgSubscriptionDataList

        // ueReachabilityRequestIndicator
        boolean ueReachabilityRequestIndicator = true;

        // sgsnNumber
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");

        // mmeName
        DiameterIdentity mmeName = new DiameterIdentityImpl(this.getDiameterIdentity());

        // subscribedPeriodicRAUTAUtimer
        Long subscribedPeriodicRAUTAUtimer = new Long(2);

        // vplmnLIPAAllowed
        boolean vplmnLIPAAllowed = true;

        // mdtUserConsent
        Boolean mdtUserConsent = Boolean.TRUE;

        // subscribedPeriodicLAUtimer
        Long subscribedPeriodicLAUtimer = new Long(2);

        InsertSubscriberDataRequestImpl prim = new InsertSubscriberDataRequestImpl(3, imsi, msisdn, category, subscriberStatus,
                bearerServiceList, teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature,
                regionalSubscriptionData, vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo,
                extensionContainer, naeaPreferredCI, gprsSubscriptionData, roamingRestrictedInSgsnDueToUnsupportedFeature,
                networkAccessMode, lsaInformation, lmuIndicator, lcsInformation, istAlertTimer, superChargerSupportedInHLR,
                mcSsInfo, csAllocationRetentionPriority, sgsnCamelSubscriptionInfo, chargingCharacteristics,
                accessRestrictionData, icsIndicator, epsSubscriptionData, csgSubscriptionDataList,
                ueReachabilityRequestIndicator, sgsnNumber, mmeName, subscribedPeriodicRAUTAUtimer, vplmnLIPAAllowed,
                mdtUserConsent, subscribedPeriodicLAUtimer);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);
        byte[] encodedData = asn.toByteArray();
        
        byte[] arr1 = new byte[1000];
        byte[] arr2 = new byte[1000];
        byte[] arr3 = new byte[1000];
        byte[] arr4 = new byte[1000];
        byte[] arr5 = new byte[293];
        System.arraycopy(encodedData, 0, arr1, 0, 1000);
        System.arraycopy(encodedData, 1000, arr2, 0, 1000);
        System.arraycopy(encodedData, 2000, arr3, 0, 1000);
        System.arraycopy(encodedData, 3000, arr4, 0, 1000);
        System.arraycopy(encodedData, 4000, arr5, 0, 293);
        
        
        assertTrue(Arrays.equals(encodedData, this.getData()));

        prim = new InsertSubscriberDataRequestImpl(2, imsi, msisdn, category, subscriberStatus, bearerServiceList,
                teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData,
                vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));
    }
}
