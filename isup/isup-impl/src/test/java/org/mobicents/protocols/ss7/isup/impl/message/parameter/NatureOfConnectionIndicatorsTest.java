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

/**
 * Start time:13:20:04 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:13:20:04 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class NatureOfConnectionIndicatorsTest extends ParameterHarness {

    public NatureOfConnectionIndicatorsTest() {
        super();
        super.badBodies.add(new byte[2]);
        super.goodBodies.add(new byte[1]);
        super.goodBodies.add(new byte[] { 0x0E });
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws IOException, ParameterException {

        NatureOfConnectionIndicatorsImpl eci = new NatureOfConnectionIndicatorsImpl(
                getBody(NatureOfConnectionIndicatorsImpl._SI_ONE_SATELLITE,
                        NatureOfConnectionIndicatorsImpl._CCI_REQUIRED_ON_THIS_CIRCUIT,
                        NatureOfConnectionIndicatorsImpl._ECDI_INCLUDED));

        String[] methodNames = { "getSatelliteIndicator", "getContinuityCheckIndicator", "isEchoControlDeviceIndicator" };
        Object[] expectedValues = { NatureOfConnectionIndicatorsImpl._SI_ONE_SATELLITE,
                NatureOfConnectionIndicatorsImpl._CCI_REQUIRED_ON_THIS_CIRCUIT, NatureOfConnectionIndicatorsImpl._ECDI_INCLUDED };

        super.testValues(eci, methodNames, expectedValues);
    }

    private byte[] getBody(int siOneSatellite, int cciRequiredOnThisCircuit, boolean ecdiIncluded) {

        byte b = (byte) (siOneSatellite | (cciRequiredOnThisCircuit << 2) | (ecdiIncluded ? (0x01 << 4) : (0x00 << 4)));

        return new byte[] { b };
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new NatureOfConnectionIndicatorsImpl(new byte[1]);
    }

}
