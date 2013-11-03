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
 * Start time:12:21:06 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.lang.reflect.InvocationTargetException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:12:21:06 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * Class to test BCI
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class AccessDeliveryInformationTest extends ParameterHarness {

    public AccessDeliveryInformationTest() {
        super();

        super.badBodies.add(new byte[2]);


        super.goodBodies.add(getBody1());
    }

    private byte[] getBody1() {

       byte[] body = new byte[]{(byte)0x01};
       return body;
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        AccessDeliveryInformationImpl at = new AccessDeliveryInformationImpl(getBody1());

        String[] methodNames = { "getAccessDeliveryIndicator" };
        Object[] expectedValues = { AccessDeliveryInformationImpl._NO_SETUP_MESSAGE };

        super.testValues(at, methodNames, expectedValues);
    }

    
    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new org.mobicents.protocols.ss7.isup.impl.message.parameter.AccessDeliveryInformationImpl();
    }

}
