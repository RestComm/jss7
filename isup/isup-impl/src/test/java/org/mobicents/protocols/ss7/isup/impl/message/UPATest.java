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
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.UserPartAvailableMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInstructionIndicators;
import org.testng.annotations.Test;

/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * Test for ACM
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class UPATest extends MessageHarness {

    @Test(groups = { "functional.encode", "functional.decode", "message" })
    public void testTwo_Params() throws Exception {

        byte[] message = getDefaultBody();


        UserPartAvailableMessage msg =  super.messageFactory.createUPA();
        ((AbstractISUPMessage) msg).decode(message, messageFactory,parameterFactory);

        assertNotNull(msg.getParameterCompatibilityInformation());
        assertNotNull(msg.getParameterCompatibilityInformation().getParameterCompatibilityInstructionIndicators());
        assertEquals(msg.getParameterCompatibilityInformation().getParameterCompatibilityInstructionIndicators().length,4);

        final ParameterCompatibilityInstructionIndicators[] indicators = msg.getParameterCompatibilityInformation().getParameterCompatibilityInstructionIndicators();
        int index = 0;
        assertNotNull(indicators[index]);
        assertEquals(indicators[index].getParameterCode(),(byte) 1);
        assertEquals(indicators[index].isTransitAtIntermediateExchangeIndicator(),true);
        assertEquals(indicators[index].isReleaseCallIndicator(),true);
        assertEquals(indicators[index].isSendNotificationIndicator(),false);
        assertEquals(indicators[index].isDiscardMessageIndicator(),true);
        assertEquals(indicators[index].isDiscardParameterIndicator(),false);
        assertEquals(indicators[index].getPassOnNotPossibleIndicator(),1);
        assertEquals(indicators[index].isSecondOctetPresent(),true);
        assertEquals(indicators[index].getBandInterworkingIndicator(),3);
        
        
        index = 1;
        assertNotNull(indicators[index]);
        assertEquals(indicators[index].getParameterCode(),(byte) 2);
        assertEquals(indicators[index].isTransitAtIntermediateExchangeIndicator(),false);
        assertEquals(indicators[index].isReleaseCallIndicator(),false);
        assertEquals(indicators[index].isSendNotificationIndicator(),false);
        assertEquals(indicators[index].isDiscardMessageIndicator(),false);
        assertEquals(indicators[index].isDiscardParameterIndicator(),false);
        assertEquals(indicators[index].getPassOnNotPossibleIndicator(),0);
        assertEquals(indicators[index].isSecondOctetPresent(),false);
        
        index = 2;
        assertNotNull(indicators[index]);
        assertEquals(indicators[index].getParameterCode(),(byte) 3);
        assertEquals(indicators[index].isTransitAtIntermediateExchangeIndicator(),true);
        assertEquals(indicators[index].isReleaseCallIndicator(),true);
        assertEquals(indicators[index].isSendNotificationIndicator(),true);
        assertEquals(indicators[index].isDiscardMessageIndicator(),true);
        assertEquals(indicators[index].isDiscardParameterIndicator(),false);
        assertEquals(indicators[index].getPassOnNotPossibleIndicator(),1);
        assertEquals(indicators[index].isSecondOctetPresent(),true);
        assertEquals(indicators[index].getBandInterworkingIndicator(),1);

        index = 3;
        assertNotNull(indicators[index]);
        assertEquals(indicators[index].getParameterCode(),(byte) 4);
        assertEquals(indicators[index].isTransitAtIntermediateExchangeIndicator(),true);
        assertEquals(indicators[index].isReleaseCallIndicator(),true);
        assertEquals(indicators[index].isSendNotificationIndicator(),false);
        assertEquals(indicators[index].isDiscardMessageIndicator(),true);
        assertEquals(indicators[index].isDiscardParameterIndicator(),true);
        assertEquals(indicators[index].getPassOnNotPossibleIndicator(),1);
        assertEquals(indicators[index].isSecondOctetPresent(),true);
        assertEquals(indicators[index].getBandInterworkingIndicator(),2);
    }

    protected byte[] getDefaultBody() {
        byte[] message = {
                // CIC
                0x0C, (byte) 0x0B,
                UserPartAvailableMessage.MESSAGE_CODE, 
                    //pointer
                    0x01,
                    ParameterCompatibilityInformation._PARAMETER_CODE,
                        //len
                        0x0D,
                            //1
                            0x01,
                            (byte) 0xab,
                            0x03,
                            //2
                            0x02,
                            (byte) 0xab,
                            (byte) 0x83,
                            0x0c,
                            //3
                            0x03,
                            (byte) 0xaf,
                            0x01,
                            //4
                            0x04,
                            (byte) 0xbb,
                            0x02,
                0x00
                
        };
        return message;
    }

    protected ISUPMessage getDefaultMessage() {
        return super.messageFactory.createUPA();
    }
}
