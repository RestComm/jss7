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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumber;
import org.testng.annotations.Test;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class RedirectionNumberTest extends ParameterHarness {

    /**
     * @throws IOException
     */
    public RedirectionNumberTest() throws IOException {
        super.badBodies.add(new byte[1]);

    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        RedirectionNumberImpl bci = new RedirectionNumberImpl(getBody(false, RedirectionNumber._NAI_INTERNATIONAL_NUMBER,
                RedirectionNumberImpl._INN_ROUTING_ALLOWED, RedirectionNumberImpl._NPI_TELEX, getSixDigits()));

        String[] methodNames = { "isOddFlag", "getNatureOfAddressIndicator", "getInternalNetworkNumberIndicator",
                "getNumberingPlanIndicator", "getAddress" };
        Object[] expectedValues = { false, RedirectionNumber._NAI_INTERNATIONAL_NUMBER,
                RedirectionNumberImpl._INN_ROUTING_ALLOWED, RedirectionNumberImpl._NPI_TELEX, getSixDigitsString() };
        super.testValues(bci, methodNames, expectedValues);
    }

    private byte[] getBody(boolean isODD, int naiNetworkSpecific, int innRoutingAllowed, int npiTelex, byte[] dgits)
            throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // we will use odd number of digits, so we leave zero as MSB

        if (isODD) {
            bos.write(0x80 | naiNetworkSpecific);
        } else {
            bos.write(naiNetworkSpecific);
        }

        bos.write((innRoutingAllowed << 7) | (npiTelex << 4));

        bos.write(dgits);
        return bos.toByteArray();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() {
        return new RedirectionNumberImpl(0, "1", 1, 1);
    }

}
