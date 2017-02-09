/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.cap.gap;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.gap.*;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.isup.DigitsImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.CallGapRequestImpl;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 *
 */
public class BasicGapCriteriaTest {

    public static final int SERVICE_KEY = 821;
    public static final int DURATION = 60;
    public static final int GAP_INTERVAL = 1;

    // GapOnService & Gap Indicators
    public byte[] getData() {
        return new byte[] { 48, 16, (byte) 160, 6, (byte) 162, 4, (byte) 128, 2, 3, 53, (byte) 161, 6, (byte) 128, 1, DURATION, (byte) 129, GAP_INTERVAL, (byte) 255};
    }

    // CalledAddressAndService & Gap Indicators
    public byte[] getData1() {
        return new byte[] { 48, 22, (byte) 160, 12, (byte) 189, 10, (byte) 128, 4, 48, 69, 91, 84, (byte) 129, 2, 3, 53, (byte) 161, 6, (byte) 128, 1, 60, (byte) 129, 1, (byte) 255};
    }

    // CallingAddressAndService & Gap Indicators
    public byte[] getData2() {
        return new byte[] { 48, 22, (byte) 160, 12, (byte) 190, 10, (byte) 128, 4, 48, 69, 91, 84, (byte) 129, 2, 3, 53, (byte) 161, 6, (byte) 128, 1, 60, (byte) 129, 1, (byte) 255};
    }

    public byte[] getDigitsData() {
        return new byte[] {48, 69, 91, 84};
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode_GapOnService() throws Exception {

        byte[] data = this.getData();
        AsnInputStream ais = new AsnInputStream(data);
        CallGapRequestImpl elem = new CallGapRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getGapCriteria().getBasicGapCriteria().getGapOnService().getServiceKey(), SERVICE_KEY);
        assertEquals(elem.getGapIndicators().getDuration(), DURATION);
        assertEquals(elem.getGapIndicators().getGapInterval(), -GAP_INTERVAL);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode_GapOnService() throws Exception {

        GapOnService gapOnService = new GapOnServiceImpl(SERVICE_KEY);
        BasicGapCriteria basicGapCriteria = new BasicGapCriteriaImpl(gapOnService);
        GapCriteria gapCriteria = new GapCriteriaImpl(basicGapCriteria);

        GapIndicators gapIndicators = new GapIndicatorsImpl(DURATION, -GAP_INTERVAL);

        CallGapRequestImpl elem = new CallGapRequestImpl(gapCriteria, gapIndicators, null, null, null);

        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode_CalledAddressAndService() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        CallGapRequestImpl elem = new CallGapRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getServiceKey(), SERVICE_KEY);
        assertEquals(elem.getGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getCalledAddressValue().getData(), getDigitsData());
        assertEquals(elem.getGapIndicators().getDuration(), DURATION);
        assertEquals(elem.getGapIndicators().getGapInterval(), -GAP_INTERVAL);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode_CalledAddressAndService() throws Exception {

        Digits digits = new DigitsImpl(getDigitsData());
        CalledAddressAndService calledAddressAndService = new CalledAddressAndServiceImpl(digits, SERVICE_KEY);
        BasicGapCriteria basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);
        GapCriteria gapCriteria = new GapCriteriaImpl(basicGapCriteria);

        GapIndicators gapIndicators = new GapIndicatorsImpl(DURATION, -GAP_INTERVAL);

        CallGapRequestImpl elem = new CallGapRequestImpl(gapCriteria, gapIndicators, null, null, null);

        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode_CallingAddressAndService() throws Exception {

        byte[] data = this.getData2();
        AsnInputStream ais = new AsnInputStream(data);
        CallGapRequestImpl elem = new CallGapRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getGapCriteria().getBasicGapCriteria().getCallingAddressAndService().getServiceKey(), SERVICE_KEY);
        assertEquals(elem.getGapCriteria().getBasicGapCriteria().getCallingAddressAndService().getCallingAddressValue().getData(), getDigitsData());
        assertEquals(elem.getGapIndicators().getDuration(), DURATION);
        assertEquals(elem.getGapIndicators().getGapInterval(), -GAP_INTERVAL);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode_CallingAddressAndService() throws Exception {

        Digits digits = new DigitsImpl(getDigitsData());
        CallingAddressAndService callingAddressAndService = new CallingAddressAndServiceImpl(digits, SERVICE_KEY);
        BasicGapCriteria basicGapCriteria = new BasicGapCriteriaImpl(callingAddressAndService);
        GapCriteria gapCriteria = new GapCriteriaImpl(basicGapCriteria);

        GapIndicators gapIndicators = new GapIndicatorsImpl(DURATION, -GAP_INTERVAL);

        CallGapRequestImpl elem = new CallGapRequestImpl(gapCriteria, gapIndicators, null, null, null);

        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }
}
