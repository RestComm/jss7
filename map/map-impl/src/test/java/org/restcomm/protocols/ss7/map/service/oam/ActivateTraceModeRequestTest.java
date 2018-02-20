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

package org.restcomm.protocols.ss7.map.service.oam;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.oam.JobType;
import org.restcomm.protocols.ss7.map.api.service.oam.MDTConfiguration;
import org.restcomm.protocols.ss7.map.api.service.oam.MSCSEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.MSCSInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepth;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepthList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceNETypeList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference2;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceType;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.oam.ActivateTraceModeRequestImpl_Base;
import org.restcomm.protocols.ss7.map.service.oam.MDTConfigurationImpl;
import org.restcomm.protocols.ss7.map.service.oam.MSCSEventListImpl;
import org.restcomm.protocols.ss7.map.service.oam.MSCSInterfaceListImpl;
import org.restcomm.protocols.ss7.map.service.oam.TraceDepthListImpl;
import org.restcomm.protocols.ss7.map.service.oam.TraceEventListImpl;
import org.restcomm.protocols.ss7.map.service.oam.TraceInterfaceListImpl;
import org.restcomm.protocols.ss7.map.service.oam.TraceNETypeListImpl;
import org.restcomm.protocols.ss7.map.service.oam.TraceReference2Impl;
import org.restcomm.protocols.ss7.map.service.oam.TraceReferenceImpl;
import org.restcomm.protocols.ss7.map.service.oam.TraceTypeImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class ActivateTraceModeRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 15, (byte) 128, 7, 51, 51, 35, 34, 34, 0, 17, (byte) 129, 1, 11, (byte) 130, 1, 55 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 107, (byte) 128, 7, 51, 51, 35, 34, 34, 0, 17, (byte) 129, 1, 11, (byte) 130, 1, 55, (byte) 131, 6, (byte) 145, 17, 17, 49, 51,
                51, (byte) 164, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24,
                25, 26, (byte) 161, 3, 31, 32, 33, (byte) 133, 3, 12, 13, 14, (byte) 166, 6, (byte) 128, 1, 2, (byte) 129, 1, 0, (byte) 135, 3, 6, 16, 0,
                (byte) 168, 5, (byte) 128, 3, 6, 64, 0, (byte) 169, 4, (byte) 128, 2, 3, (byte) 128, (byte) 138, 5, 4, (byte) 192, (byte) 168, 4, 1,
                (byte) 171, 3, 10, 1, 3 };
    }

    private byte[] getTraceReferenceData() {
        return new byte[] { 11 };
    }

    private byte[] getTraceReference2Data() {
        return new byte[] { 12, 13, 14 };
    }

    private byte[] getTraceCollectionEntityData() {
        return new byte[] { (byte) 192, (byte) 168, 4, 1 };
    }

    @Test(groups = { "functional.decode", "service.oam" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        ActivateTraceModeRequestImpl_Base asc = new ActivateTraceModeRequestImpl_Base();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(asc.getImsi().getData(), "33333222220011");
        assertEquals(asc.getTraceReference().getData(), getTraceReferenceData());
        assertEquals(asc.getTraceType().getData(), 55);

        assertNull(asc.getOmcId());
        assertNull(asc.getExtensionContainer());
        assertNull(asc.getTraceReference2());
        assertNull(asc.getTraceDepthList());
        assertNull(asc.getTraceNeTypeList());
        assertNull(asc.getTraceInterfaceList());
        assertNull(asc.getTraceEventList());
        assertNull(asc.getTraceCollectionEntity());
        assertNull(asc.getMdtConfiguration());


        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new ActivateTraceModeRequestImpl_Base();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(asc.getImsi().getData(), "33333222220011");
        assertEquals(asc.getTraceReference().getData(), getTraceReferenceData());
        assertEquals(asc.getTraceType().getData(), 55);

        assertEquals(asc.getOmcId().getAddress(), "1111133333");
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));
        assertEquals(asc.getTraceReference2().getData(), getTraceReference2Data());
        assertEquals(asc.getTraceDepthList().getMscSTraceDepth(), TraceDepth.maximum);
        assertEquals(asc.getTraceDepthList().getMgwTraceDepth(), TraceDepth.minimum);
        assertTrue(asc.getTraceNeTypeList().getGgsn());
        assertFalse(asc.getTraceNeTypeList().getSgsn());
        assertTrue(asc.getTraceInterfaceList().getMscSList().getIu());
        assertFalse(asc.getTraceInterfaceList().getMscSList().getA());
        assertTrue(asc.getTraceEventList().getMscSList().getMoMtCall());
        assertFalse(asc.getTraceEventList().getMscSList().getMoMtSms());
        assertEquals(asc.getTraceCollectionEntity().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertEquals(asc.getTraceCollectionEntity().getGSNAddressData(), getTraceCollectionEntityData());
        assertEquals(asc.getMdtConfiguration().getJobType(), JobType.immediateMdtAndTrace);
    }

    @Test(groups = { "functional.encode", "service.oam" })
    public void testEncode() throws Exception {

        IMSI imsi = new IMSIImpl("33333222220011");
        TraceReference traceReference = new TraceReferenceImpl(getTraceReferenceData());
        TraceType traceType = new TraceTypeImpl(55);
        ActivateTraceModeRequestImpl_Base asc = new ActivateTraceModeRequestImpl_Base(imsi, traceReference, traceType, null, null, null, null, null, null, null, null,
                null);
//        IMSI imsi, TraceReference traceReference, TraceType traceType, AddressString omcId,
//        MAPExtensionContainer extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
//        TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        AddressString omcId = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1111133333");
        TraceReference2 traceReference2 = new TraceReference2Impl(getTraceReference2Data());
        TraceDepthList traceDepthList = new TraceDepthListImpl(TraceDepth.maximum, TraceDepth.minimum, null, null, null, null, null, null, null, null);
        TraceNETypeList traceNeTypeList = new TraceNETypeListImpl(false, false, false, true, false, false, false, false, false, false);
        // boolean mscS, boolean mgw, boolean sgsn, boolean ggsn, boolean rnc, boolean bmSc, boolean mme, boolean sgw, boolean pgw, boolean enb
        MSCSInterfaceList mscSList = new MSCSInterfaceListImpl(false, true, false, false, false, false, false, false, false, false);
        // boolean a, boolean iu, boolean mc, boolean mapG, boolean mapB, boolean mapE, boolean mapF, boolean cap, boolean mapD, boolean mapC
        TraceInterfaceList traceInterfaceList = new TraceInterfaceListImpl(mscSList, null, null, null, null, null, null, null, null, null);
        MSCSEventList mscSList2 = new MSCSEventListImpl(true, false, false, false, false);
        // boolean moMtCall, boolean moMtSms, boolean luImsiAttachImsiDetach, boolean handovers, boolean ss
        TraceEventList traceEventList = new TraceEventListImpl(mscSList2, null, null, null, null, null, null, null);
        GSNAddress traceCollectionEntity = new GSNAddressImpl(GSNAddressAddressType.IPv4, getTraceCollectionEntityData());
        MDTConfiguration mdtConfiguration = new MDTConfigurationImpl(JobType.immediateMdtAndTrace, null, null, null, null, null, null, null, null, null, null);
        asc = new ActivateTraceModeRequestImpl_Base(imsi, traceReference, traceType, omcId, MAPExtensionContainerTest.GetTestExtensionContainer(), traceReference2,
                traceDepthList, traceNeTypeList, traceInterfaceList, traceEventList, traceCollectionEntity, mdtConfiguration);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
