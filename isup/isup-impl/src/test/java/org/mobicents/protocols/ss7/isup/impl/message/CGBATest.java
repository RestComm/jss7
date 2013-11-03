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
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;
import org.testng.annotations.Test;

/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * Test for CGBA
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CGBATest extends MessageHarness {

    @Test(groups = { "functional.encode", "functional.decode", "message" })
    public void testTwo_Params() throws Exception {
        byte[] message = getDefaultBody();

        CircuitGroupBlockingAckMessage cgb = super.messageFactory.createCGBA();
        ((AbstractISUPMessage) cgb).decode(message, messageFactory,parameterFactory);

        try {
            RangeAndStatus RS = (RangeAndStatus) cgb.getParameter(RangeAndStatus._PARAMETER_CODE);
            assertNotNull(RS, "Range And Status return is null, it should not be");
            assertNotNull(RS, "Range And Status return is null, it should not be");
            if (RS == null)
                return;
            byte range = RS.getRange();
            assertEquals(range, 0x11, "Range is wrong");
            byte[] b = RS.getStatus();
            assertNotNull(b, "RangeAndStatus.getRange() is null");
            if (b == null) {
                return;
            }
            assertEquals(b.length, 3, "Length of param is wrong");
            if (b.length != 3)
                return;
            assertTrue(super.makeCompare(b, new byte[] { 0x02, 0x03, 0x04 }), "RangeAndStatus.getRange() is wrong");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed on get parameter[" + CallReference._PARAMETER_CODE + "]:" + e);
        }

    }

    protected byte[] getDefaultBody() {
        byte[] message = {

        0x0C, (byte) 0x0B, CircuitGroupBlockingAckMessage.MESSAGE_CODE
                // Circuit group supervision message type
                , 0x01 // hardware failure oriented
                , 0x01 // ptr to variable part
                // no optional, so no pointer
                // RangeAndStatus._PARAMETER_CODE
                , 0x04, 0x11, 0x02, 0x03, 0x04 };
        return message;
    }

    protected ISUPMessage getDefaultMessage() {
        return super.messageFactory.createCGBA();
    }
}
