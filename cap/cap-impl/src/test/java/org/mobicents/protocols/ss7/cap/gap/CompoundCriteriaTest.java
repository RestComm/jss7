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
import org.mobicents.protocols.ss7.cap.api.primitives.ScfID;
import org.mobicents.protocols.ss7.cap.isup.DigitsImpl;
import org.mobicents.protocols.ss7.cap.primitives.ScfIDImpl;
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
public class CompoundCriteriaTest {

    public static final int SERVICE_KEY = 821;
    public static final int DURATION = 60;
    public static final int GAP_INTERVAL = 1;

    // CalledAddressAndService & Gap Indicators
    public byte[] getData() {
        return new byte[] { 48, 32, (byte) 160, 22, 48, 20, (byte) 160, 12, (byte) 189, 10, (byte) 128,
                4, 48, 69, 91, 11, (byte) 129, 2, 3, 53, (byte) 129, 4, 12, 32, 23, 56, (byte) 161, 6,
                (byte) 128, 1, DURATION, (byte) 129, GAP_INTERVAL, (byte) 255};
    }

    public byte[] getDigitsData() {
        return new byte[] {48, 69, 91, 11};
    }

    public byte[] getDigitsData1() {
        return new byte[] {12, 32, 23, 56};
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode_CalledAddressAndService() throws Exception {

        byte[] data = this.getData();
        AsnInputStream ais = new AsnInputStream(data);
        CallGapRequestImpl elem = new CallGapRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getGapCriteria().getCompoundGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getServiceKey(), SERVICE_KEY);
        assertEquals(elem.getGapCriteria().getCompoundGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getCalledAddressValue().getData(), getDigitsData());
        assertEquals(elem.getGapIndicators().getDuration(), DURATION);
        assertEquals(elem.getGapIndicators().getGapInterval(), -GAP_INTERVAL);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode_CalledAddressAndService() throws Exception {

        Digits digits = new DigitsImpl(getDigitsData());
        CalledAddressAndService calledAddressAndService = new CalledAddressAndServiceImpl(digits, SERVICE_KEY);
        BasicGapCriteria basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);

        ScfID scfId = new ScfIDImpl(getDigitsData1());

        CompoundCriteria compoundCriteria = new CompoundCriteriaImpl(basicGapCriteria, scfId);
        GapCriteria gapCriteria = new GapCriteriaImpl(compoundCriteria);

        GapIndicators gapIndicators = new GapIndicatorsImpl(DURATION, -GAP_INTERVAL);

        CallGapRequestImpl elem = new CallGapRequestImpl(gapCriteria, gapIndicators, null, null, null);

        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));
    }


}
