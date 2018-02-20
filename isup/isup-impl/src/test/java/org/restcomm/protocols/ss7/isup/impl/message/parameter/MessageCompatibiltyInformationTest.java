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
 * Start time:12:21:06 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import java.lang.reflect.InvocationTargetException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.MessageCompatibilityInformationImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageCompatibilityInstructionIndicator;
import org.testng.annotations.Test;

/**
 * Start time:12:21:06 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * Class to test BCI
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class MessageCompatibiltyInformationTest extends ParameterHarness {

    public MessageCompatibiltyInformationTest() {
        super();

        super.badBodies.add(new byte[1]);
        super.badBodies.add(new byte[3]);

        super.goodBodies.add(getBody1());
        super.goodBodies.add(getBody2());
    }

    private byte[] getBody1() {

        byte[] body = new byte[] { (byte) 0x81 };
        return body;
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        MessageCompatibilityInformationImpl at = new MessageCompatibilityInformationImpl(getBody1());
        final String[] getterMethodNames = new String[]{"isTransitAtIntermediateExchangeIndicator",
                "isReleaseCallIndicator",
                "isSendNotificationIndicator",
                "isDiscardMessageIndicator",
                "isPassOnNotPossibleIndicator",
                "getBandInterworkingIndicator"};
        final Object[][] expectedValues = new Object[][]{
                new Object[]{true,false,false,false,false,0}
        };
        testValues(at, "getMessageCompatibilityInstructionIndicators", getterMethodNames, expectedValues);
    }

    private byte[] getBody2() {

        byte[] body = new byte[] { 0x42, (byte) 0x81 };
        return body;
    }
    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        MessageCompatibilityInformationImpl at = new MessageCompatibilityInformationImpl(getBody2());
        final String[] getterMethodNames = new String[]{"isTransitAtIntermediateExchangeIndicator",
                "isReleaseCallIndicator",
                "isSendNotificationIndicator",
                "isDiscardMessageIndicator",
                "isPassOnNotPossibleIndicator",
                "getBandInterworkingIndicator"};
        final Object[][] expectedValues = new Object[][]{
                new Object[]{false,true,false,false,false,2},
                new Object[]{true,false,false,false,false,0}
        };
        testValues(at, "getMessageCompatibilityInstructionIndicators", getterMethodNames, expectedValues);
    }
    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new org.restcomm.protocols.ss7.isup.impl.message.parameter.MessageCompatibilityInformationImpl();
    }
    
}
