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

import org.mobicents.protocols.ss7.isup.message.ApplicationTransportMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.ApplicationTransport;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageCompatibilityInstructionIndicator;
import org.testng.annotations.Test;

/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * Test for ACM
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class APTTest extends MessageHarness {

    @Test(groups = { "functional.encode", "functional.decode", "message" })
    public void testTwo_Params() throws Exception {

        byte[] message = getDefaultBody();


        ApplicationTransportMessage msg = super.messageFactory.createAPT();
        ((AbstractISUPMessage) msg).decode(message, messageFactory,parameterFactory);

        assertNotNull(msg.getApplicationTransport());
        ApplicationTransport at = msg.getApplicationTransport();
        assertEquals(at.getApplicationContextIdentifier(),new Byte((byte)5));
        assertEquals(at.isSendNotificationIndicator(),Boolean.TRUE);
        assertEquals(at.isReleaseCallIndicator(),Boolean.FALSE);
        assertEquals(at.getAPMSegmentationIndicator(),new Byte((byte)5));

        assertNotNull(msg.getMessageCompatibilityInformation());
        MessageCompatibilityInformation mcis = msg.getMessageCompatibilityInformation();
        assertNotNull(mcis.getMessageCompatibilityInstructionIndicators());
        assertEquals(mcis.getMessageCompatibilityInstructionIndicators().length,2);
        assertNotNull(mcis.getMessageCompatibilityInstructionIndicators()[0]);
        assertNotNull(mcis.getMessageCompatibilityInstructionIndicators()[1]);
        assertEquals(mcis.getMessageCompatibilityInstructionIndicators()[0].getBandInterworkingIndicator(),2);
        assertEquals(mcis.getMessageCompatibilityInstructionIndicators()[1].getBandInterworkingIndicator(),0);
        
        MessageCompatibilityInstructionIndicator mic = mcis.getMessageCompatibilityInstructionIndicators()[0];
        assertEquals(mic.getBandInterworkingIndicator(), 2);
        mic = mcis.getMessageCompatibilityInstructionIndicators()[1];
        assertEquals(mic.getBandInterworkingIndicator(), 0);

        CircuitIdentificationCode cic = msg.getCircuitIdentificationCode();
        assertNotNull(cic, "CircuitIdentificationCode must not be null");
        assertEquals(getDefaultCIC(), cic.getCIC(), "CircuitIdentificationCode value does not match");

    }

    protected byte[] getDefaultBody() {
        byte[] message = {
                // CIC
                0x0C, (byte) 0x0B,
                ApplicationTransportMessage.MESSAGE_CODE, 
                //pointer
                0x01,
                //MCI
                MessageCompatibilityInformation._PARAMETER_CODE,
                    //len
                    0x02,
                    0x42, 
                    (byte) 0x81,
                //AT
                ApplicationTransport._PARAMETER_CODE,
                    //len
                    0x04,
                    //ACI
                    5,
                    //SNI,RCI (1 0)
                    0x02,
                    //SI,APM indicator (1 00 01 01)
                    0x45,
                    //Segmentation reference
                    (byte)(0x80 | 6),
               0x00
                
        };
        return message;
    }

    protected ISUPMessage getDefaultMessage() {
        return super.messageFactory.createAPT();
    }
}
