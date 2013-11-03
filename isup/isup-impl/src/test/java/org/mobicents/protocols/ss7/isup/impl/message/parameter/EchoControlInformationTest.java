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
 * Start time:11:34:01 2009-04-24<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:11:34:01 2009-04-24<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class EchoControlInformationTest extends ParameterHarness {

    public EchoControlInformationTest() {
        super();
        super.goodBodies.add(new byte[] { 67 });
        super.badBodies.add(new byte[2]);
    }

    private byte[] getBody(int _OUT_E_CDII, int _IN_E_CDII, int _IN_E_CDRI, int _OUT_E_CDRI) {
        byte[] b = new byte[1];
        int v = _OUT_E_CDII;
        v |= _IN_E_CDII << 2;
        v |= _OUT_E_CDRI << 4;
        v |= _IN_E_CDRI << 6;
        b[0] = (byte) v;

        return b;
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws ParameterException {
        EchoControlInformationImpl eci = new EchoControlInformationImpl(getBody(
                EchoControlInformationImpl._OUTGOING_ECHO_CDII_NINA, EchoControlInformationImpl._INCOMING_ECHO_CDII_INCLUDED,
                EchoControlInformationImpl._INCOMING_ECHO_CDRI_AR, EchoControlInformationImpl._OUTGOING_ECHO_CDRI_NOINFO));

        String[] methodNames = { "getOutgoingEchoControlDeviceInformationIndicator",
                "getIncomingEchoControlDeviceInformationIndicator", "getIncomingEchoControlDeviceInformationRequestIndicator",
                "getOutgoingEchoControlDeviceInformationRequestIndicator" };
        Object[] expectedValues = { EchoControlInformationImpl._OUTGOING_ECHO_CDII_NINA,
                EchoControlInformationImpl._INCOMING_ECHO_CDII_INCLUDED, EchoControlInformationImpl._INCOMING_ECHO_CDRI_AR,
                EchoControlInformationImpl._OUTGOING_ECHO_CDRI_NOINFO };
        super.testValues(eci, methodNames, expectedValues);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new EchoControlInformationImpl(new byte[1]);
    }

}
