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
public class InformationIndicatorsTest extends ParameterHarness {
    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    public InformationIndicatorsTest() {
        super();
        // super.goodBodies.add(new byte[] { 67, 12 });
        // super.badBodies.add(new byte[3]);
        // super.badBodies.add(new byte[1]);
    }

    private byte[] getBody(boolean _CIRI_INCLUDED, int _CPARI_ADDRESS_INCLUDED, boolean _CPCRI_CATEOGRY_INCLUDED,
            boolean _HPI_NOT_PROVIDED, boolean _SII_UNSOLICITED, int reserved) {

        int b0 = 0;
        int b1 = 0;
        b0 |= _CPARI_ADDRESS_INCLUDED;
        b0 |= (_HPI_NOT_PROVIDED ? _TURN_ON : _TURN_OFF) << 2;
        b0 |= (_CPCRI_CATEOGRY_INCLUDED ? _TURN_ON : _TURN_OFF) << 5;
        b0 |= (_CIRI_INCLUDED ? _TURN_ON : _TURN_OFF) << 6;
        b0 |= (_SII_UNSOLICITED ? _TURN_ON : _TURN_OFF) << 7;

        b1 |= (reserved & 0x0F) << 4;

        return new byte[] { (byte) b0, (byte) b1 };
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws ParameterException {
        InformationIndicatorsImpl eci = new InformationIndicatorsImpl(getBody(InformationIndicatorsImpl._CIRI_INCLUDED,
                InformationIndicatorsImpl._CPARI_ADDRESS_INCLUDED, InformationIndicatorsImpl._CPCRI_CATEOGRY_INCLUDED,
                InformationIndicatorsImpl._HPI_NOT_PROVIDED, InformationIndicatorsImpl._SII_UNSOLICITED, 10));

        String[] methodNames = { "isChargeInformationResponseIndicator", "getCallingPartyAddressResponseIndicator",
                "isCallingPartysCategoryResponseIndicator", "isHoldProvidedIndicator", "isSolicitedInformationIndicator",
                "getReserved" };
        Object[] expectedValues = { InformationIndicatorsImpl._CIRI_INCLUDED,
                InformationIndicatorsImpl._CPARI_ADDRESS_INCLUDED, InformationIndicatorsImpl._CPCRI_CATEOGRY_INCLUDED,
                InformationIndicatorsImpl._HPI_NOT_PROVIDED, InformationIndicatorsImpl._SII_UNSOLICITED, 10 };
        super.testValues(eci, methodNames, expectedValues);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new InformationIndicatorsImpl(new byte[2]);
    }

}
