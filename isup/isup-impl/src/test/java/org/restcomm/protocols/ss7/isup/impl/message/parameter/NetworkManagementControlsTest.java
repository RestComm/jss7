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
 * Start time:13:20:04 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import static org.testng.Assert.fail;

import java.io.IOException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.NetworkManagementControlsImpl;
import org.testng.annotations.Test;

/**
 * Start time:13:20:04 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class NetworkManagementControlsTest extends ParameterHarness {

    public NetworkManagementControlsTest() {
        super();

        super.goodBodies.add(new byte[1]);
        super.goodBodies.add(new byte[] { 0x0E });
        super.goodBodies.add(new byte[] { 0x0E, 32, 45, 0x0A });
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws IOException, ParameterException {

        boolean[] bools = new boolean[] { true, true, false, true, false, true, true };
        NetworkManagementControlsImpl eci = new NetworkManagementControlsImpl(getBody1(bools));
        byte[] encoded = eci.encode();
        for (int index = 0; index < encoded.length; index++) {
            if (bools[index] != eci.isTARControlEnabled(encoded[index])) {
                fail("Failed to get TAR bits, at index: " + index);
            }

            if (index == encoded.length - 1) {
                if (((encoded[index] >> 7) & 0x01) != 1) {
                    fail("Last byte must have MSB turned on to indicate last byte, this one does not.");
                }
            }
        }

    }

    private byte[] getBody1(boolean[] tarEnabled) {
        boolean[] bools = new boolean[] { true, true, false, true, false, true, true };
        NetworkManagementControlsImpl eci = new NetworkManagementControlsImpl();
        byte[] b = new byte[tarEnabled.length];
        for (int index = 0; index < tarEnabled.length; index++) {
            b[index] = eci.createTAREnabledByte(tarEnabled[index]);
        }
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new NetworkManagementControlsImpl(new byte[1]);
    }

}
