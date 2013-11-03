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
import static org.testng.Assert.assertNull;

import org.mobicents.protocols.ss7.isup.message.FacilityRejectedMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.testng.annotations.Test;

/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * Test for ACM
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class FRJTest extends MessageHarness {

    @Test(groups = { "functional.encode", "functional.decode", "message" })
    public void testTwo_Params() throws Exception {

        byte[] message = getDefaultBody();

        FacilityRejectedMessage msg = super.messageFactory.createFRJ();
        ((AbstractISUPMessage) msg).decode(message, messageFactory, parameterFactory);

        assertNotNull(msg.getFacilityIndicator());
        assertEquals(msg.getFacilityIndicator().getFacilityIndicator(), 89);

        assertNotNull(msg.getCauseIndicators());
        assertEquals(msg.getCauseIndicators().getCauseValue(), 12);
        assertEquals(msg.getCauseIndicators().getCodingStandard(), 3);
        assertEquals(msg.getCauseIndicators().getRecommendation(), 0);
        assertEquals(msg.getCauseIndicators().getLocation(), 4);
        assertNull(msg.getCauseIndicators().getDiagnostics());
    }

    protected byte[] getDefaultBody() {
        byte[] message = {
                // CIC
                0x0C, (byte) 0x0B,
                FacilityRejectedMessage.MESSAGE_CODE, 
                //MF
                0x59,
                //pointer to MV
                0x02,
                //pointer to O
                0x00,
                //Cause indicators
                //len
                0x02,
                    (byte) 0xE4,
                    (byte) 0x8C
                
        };
        return message;
    }

    protected ISUPMessage getDefaultMessage() {
        return super.messageFactory.createFRJ();
    }
}
