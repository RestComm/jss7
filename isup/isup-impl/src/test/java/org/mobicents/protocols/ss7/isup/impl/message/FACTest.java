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

import org.mobicents.protocols.ss7.isup.message.FacilityMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CallTransferNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageCompatibilityInformation;
import org.testng.annotations.Test;

/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * Test for ACM
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class FACTest extends MessageHarness {

    @Test(groups = { "functional.encode", "functional.decode", "message" })
    public void testTwo_Params() throws Exception {

        byte[] message = getDefaultBody();


        FacilityMessage msg = super.messageFactory.createFAC();
        ((AbstractISUPMessage) msg).decode(message, messageFactory,parameterFactory);

        
    }

    protected byte[] getDefaultBody() {
        byte[] message = {
                // CIC
                0x0C, (byte) 0x0B,
                FacilityMessage.MESSAGE_CODE, 
                //pointer
                0x01,
                //MCI
                MessageCompatibilityInformation._PARAMETER_CODE,
                    //len
                    0x02,
                    0x42, 
                    (byte) 0x81,
                //CT number
                CallTransferNumber._PARAMETER_CODE,
                    //len
                    0x06,
                    (byte) 0x88,
                    0x43,
                    (byte) 0x83,
                    0x60,
                    0x33,
                    0x8,
               0x00
                
        };
        return message;
    }

    protected ISUPMessage getDefaultMessage() {
        return super.messageFactory.createFAC();
    }
}
