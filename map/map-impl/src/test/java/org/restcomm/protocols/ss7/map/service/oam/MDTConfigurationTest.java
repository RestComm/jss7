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

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.GlobalCellId;
import org.restcomm.protocols.ss7.map.api.service.oam.AreaScope;
import org.restcomm.protocols.ss7.map.api.service.oam.JobType;
import org.restcomm.protocols.ss7.map.api.service.oam.ListOfMeasurements;
import org.restcomm.protocols.ss7.map.api.service.oam.LoggingDuration;
import org.restcomm.protocols.ss7.map.api.service.oam.LoggingInterval;
import org.restcomm.protocols.ss7.map.api.service.oam.ReportAmount;
import org.restcomm.protocols.ss7.map.api.service.oam.ReportInterval;
import org.restcomm.protocols.ss7.map.api.service.oam.ReportingTrigger;
import org.restcomm.protocols.ss7.map.primitives.GlobalCellIdImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.oam.AreaScopeImpl;
import org.restcomm.protocols.ss7.map.service.oam.ListOfMeasurementsImpl;
import org.restcomm.protocols.ss7.map.service.oam.MDTConfigurationImpl;
import org.restcomm.protocols.ss7.map.service.oam.ReportingTriggerImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class MDTConfigurationTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 3, 10, 1, 2 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 84, 10, 1, 2, 48, 11, (byte) 160, 9, 4, 7, 33, (byte) 240, 16, 7, (byte) 208, 4, 87, 4, 4, 11, 12, 13, 14, (byte) 128, 1, 121,
                10, 1, 20, (byte) 129, 1, 6, 2, 1, 10, (byte) 130, 1, 11, (byte) 131, 1, 4, (byte) 132, 1, 2, (byte) 165, 39, (byte) 160, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 };
    }

    private byte[] getListOfMeasurements() {
        return new byte[] { 11, 12, 13, 14 };
    }

    @Test(groups = { "functional.decode", "service.oam" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        MDTConfigurationImpl asc = new MDTConfigurationImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(asc.getJobType(), JobType.traceOnly);

        assertNull(asc.getAreaScope());
        assertNull(asc.getListOfMeasurements());
        assertNull(asc.getReportingTrigger());
        assertNull(asc.getReportInterval());
        assertNull(asc.getReportAmount());
        assertNull(asc.getEventThresholdRSRP());
        assertNull(asc.getEventThresholdRSRQ());
        assertNull(asc.getLoggingInterval());
        assertNull(asc.getLoggingDuration());
        assertNull(asc.getExtensionContainer());


        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new MDTConfigurationImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(asc.getJobType(), JobType.traceOnly);

        assertEquals(asc.getAreaScope().getCgiList().size(), 1);
        assertEquals(asc.getAreaScope().getCgiList().get(0).getMcc(), 120);
        assertEquals(asc.getAreaScope().getCgiList().get(0).getMnc(), 1);
        assertEquals(asc.getAreaScope().getCgiList().get(0).getLac(), 2000);
        assertEquals(asc.getAreaScope().getCgiList().get(0).getCellId(), 1111);

        assertEquals(asc.getListOfMeasurements().getData(), getListOfMeasurements());
        assertEquals(asc.getReportingTrigger().getData(), 121);
        assertEquals(asc.getReportInterval(), ReportInterval.lte2048ms);
        assertEquals(asc.getReportAmount(), ReportAmount.d64);
        assertEquals((int)asc.getEventThresholdRSRP(), 10);
        assertEquals((int)asc.getEventThresholdRSRQ(), 11);
        assertEquals(asc.getLoggingInterval(), LoggingInterval.d20dot48);
        assertEquals(asc.getLoggingDuration(), LoggingDuration.d2400sec);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.oam" })
    public void testEncode() throws Exception {

        MDTConfigurationImpl asc = new MDTConfigurationImpl(JobType.traceOnly, null, null, null, null, null, null, null, null, null, null);
//        JobType jobType, AreaScope areaScope, ListOfMeasurements listOfMeasurements, ReportingTrigger reportingTrigger,
//        ReportInterval reportInterval, ReportAmount reportAmount, Integer eventThresholdRSRP, Integer eventThresholdRSRQ, LoggingInterval loggingInterval,
//        LoggingDuration loggingDuration, MAPExtensionContainer extensionContainer

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        ArrayList<GlobalCellId> cgiList = new ArrayList<GlobalCellId>();
        GlobalCellId globalCellId = new GlobalCellIdImpl(120, 1, 2000, 1111); // int mcc, int mnc, int lac, int cellId
        cgiList.add(globalCellId);
        AreaScope areaScope = new AreaScopeImpl(cgiList, null, null, null, null, null);
        ListOfMeasurements listOfMeasurements = new ListOfMeasurementsImpl(getListOfMeasurements());
        ReportingTrigger reportingTrigger = new ReportingTriggerImpl(121);
        asc = new MDTConfigurationImpl(JobType.traceOnly, areaScope, listOfMeasurements, reportingTrigger, ReportInterval.lte2048ms, ReportAmount.d64, 10, 11,
                LoggingInterval.d20dot48, LoggingDuration.d2400sec, MAPExtensionContainerTest.GetTestExtensionContainer());

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

    }

}
