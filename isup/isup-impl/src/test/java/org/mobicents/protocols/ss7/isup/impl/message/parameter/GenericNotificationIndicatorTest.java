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

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:11:34:01 2009-04-24<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class GenericNotificationIndicatorTest extends ParameterHarness {

    public GenericNotificationIndicatorTest() {
        super();
        super.goodBodies.add(new byte[] { 67, 12, 13, 14, 15, 16, 17, (byte) (18 | (0x01 << 7)) });
        super.badBodies.add(new byte[1]);
    }

    private byte[] getBody() {
        return super.goodBodies.get(0);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws IOException, ParameterException {
        GenericNotificationIndicatorImpl eci = new GenericNotificationIndicatorImpl(getBody());
        byte[] body = getBody();
        byte[] encoded = eci.encode();
        boolean equal = Arrays.equals(body, encoded);
        assertTrue(equal, "Body index: \n" + makeCompare(body, encoded));

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new GenericNotificationIndicatorImpl(new byte[2]);
    }

}
