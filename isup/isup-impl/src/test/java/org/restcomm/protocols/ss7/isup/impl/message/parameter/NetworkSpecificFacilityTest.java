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

/**
 * Start time:14:40:20 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import static org.testng.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.NetworkSpecificFacilityImpl;
import org.testng.annotations.Test;

/**
 * Start time:14:40:20 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class NetworkSpecificFacilityTest extends ParameterHarness {

    public NetworkSpecificFacilityTest() {
        super(); // L1, ext bit,|| this byte
        // super.goodBodies.add(new byte[] { 1, (byte) 0x80, 11, 1, 2, 3, 4 });
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        NetworkSpecificFacilityImpl bci = new NetworkSpecificFacilityImpl(getBody(NetworkSpecificFacilityImpl._TNI_NNI, 1,
                new byte[] { 1, 2, 3, 4, 5, (byte) 0xAA }, new byte[10]));

        String[] methodNames = { "isIncludeNetworkIdentification", "getLengthOfNetworkIdentification",
                "getTypeOfNetworkIdentification", "getNetworkIdentificationPlan" };
        Object[] expectedValues = { true, 6, NetworkSpecificFacilityImpl._TNI_NNI, 1 };
        super.testValues(bci, methodNames, expectedValues);

        // now some custom part?
        byte[] body = bci.encode();

        for (int index = 2; index < 8; index++) {
            if (index == 7) {
                if (((body[index] >> 7) & 0x01) != 0) {
                    fail("Last octet must have MSB set to zero to indicate last byte.");
                }
            } else {
                if (((body[index] >> 7) & 0x01) != 1) {
                    fail("Last octet must have MSB set to one to indicate that there are more bytes.");
                }
            }
        }
    }

    private byte[] getBody(int _TNI, int _NIP, byte[] ntwrkID, byte[] ntwrSpecificFacilityIndicator) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int v = _NIP;
        v |= _TNI << 4;
        if (ntwrkID != null) {
            bos.write(ntwrkID.length);
            bos.write(v);
            for (int index = 0; index < ntwrkID.length; index++) {
                if (index == ntwrkID.length - 1) {
                    ntwrkID[index] |= 0x01 << 7;
                } else {
                    ntwrkID[index] &= 0x7F;
                }
            }
            bos.write(ntwrkID);
        } else {
            v |= 0x01 << 7;
            bos.write(v);
        }

        bos.write(ntwrSpecificFacilityIndicator);
        return bos.toByteArray();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new NetworkSpecificFacilityImpl(new byte[] { 1, (byte) 0xFF, 1, 2, 2 });
    }

}
