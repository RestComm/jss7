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
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class TransitNetworkSelectionTest extends ParameterHarness {

    /**
     * @throws IOException
     */
    public TransitNetworkSelectionTest() throws IOException {
        super.badBodies.add(new byte[1]);

    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        TransitNetworkSelectionImpl bci = new TransitNetworkSelectionImpl(getBody(false,
                TransitNetworkSelectionImpl._NIP_PDNIC, TransitNetworkSelectionImpl._TONI_ITU_T, getSixDigits()));

        String[] methodNames = { "isOddFlag", "getNetworkIdentificationPlan", "getTypeOfNetworkIdentification", "getAddress" };
        Object[] expectedValues = { false, TransitNetworkSelectionImpl._NIP_PDNIC, TransitNetworkSelectionImpl._TONI_ITU_T,
                getSixDigitsString() };
        super.testValues(bci, methodNames, expectedValues);
    }

    private byte[] getBody(boolean isODD, int _NIP, int _TONI, byte[] digits) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // we will use odd number of digits, so we leave zero as MSB

        if (isODD) {
            bos.write(0x80 | (_TONI << 4) | _NIP);
        } else {
            bos.write((_TONI << 4) | _NIP);
        }

        bos.write(digits);
        return bos.toByteArray();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() {
        return new TransitNetworkSelectionImpl("11", 1, 1);
    }

}
