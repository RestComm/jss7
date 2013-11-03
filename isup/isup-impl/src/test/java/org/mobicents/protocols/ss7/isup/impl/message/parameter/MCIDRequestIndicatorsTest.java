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
public class MCIDRequestIndicatorsTest extends ParameterHarness {
    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    public MCIDRequestIndicatorsTest() {
        super();
        super.goodBodies.add(new byte[] { 3 });
        super.badBodies.add(new byte[2]);
    }

    private byte[] getBody(boolean mcidRequest, boolean holdingRequested) {
        int b0 = 0;

        b0 |= (mcidRequest ? _TURN_ON : _TURN_OFF);
        b0 |= ((holdingRequested ? _TURN_ON : _TURN_OFF)) << 1;

        return new byte[] { (byte) b0 };
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws ParameterException {
        MCIDRequestIndicatorsImpl eci = new MCIDRequestIndicatorsImpl(getBody(MCIDRequestIndicatorsImpl._INDICATOR_REQUESTED,
                MCIDRequestIndicatorsImpl._INDICATOR_REQUESTED));

        String[] methodNames = { "isMcidRequestIndicator", "isHoldingIndicator" };
        Object[] expectedValues = { MCIDRequestIndicatorsImpl._INDICATOR_REQUESTED,
                MCIDRequestIndicatorsImpl._INDICATOR_REQUESTED };
        super.testValues(eci, methodNames, expectedValues);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new MCIDRequestIndicatorsImpl(new byte[1]);
    }

}
