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
import static org.testng.Assert.fail;

import org.mobicents.protocols.ss7.isup.message.CircuitGroupQueryResponseMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitStateIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;
import org.testng.annotations.Test;

/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * Test for CQR
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CQRTest extends MessageHarness {

    @Test(groups = { "functional.encode", "functional.decode", "message" })
    public void testTwo_Params() throws Exception {
        byte[] message = getDefaultBody();
        // CircuitGroupQueryResponseMessage grs=new CircuitGroupQueryResponseMessageImpl(this,message);
        CircuitGroupQueryResponseMessage grs = super.messageFactory.createCQR();
        ((AbstractISUPMessage) grs).decode(message, messageFactory,parameterFactory);

        try {
            RangeAndStatus RS = (RangeAndStatus) grs.getParameter(RangeAndStatus._PARAMETER_CODE);
            assertNotNull(RS, "Range And Status retrun is null, it shoul not be");
            if (RS == null)
                return;
            byte range = RS.getRange();
            assertEquals(range, 0x01, "Range is wrong,");
            byte[] b = RS.getStatus();
            assertNull(b, "RangeAndStatus.getRange() is not null");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed on get parameter[" + CallReference._PARAMETER_CODE + "]:" + e);
        }
        try {
            CircuitStateIndicator CSI = (CircuitStateIndicator) grs.getParameter(CircuitStateIndicator._PARAMETER_CODE);
            assertNotNull(CSI, "Circuit State Indicator return is null, it should not be");
            if (CSI == null)
                return;
            assertNotNull(CSI.getCircuitState(), "CircuitStateIndicator getCircuitState return is null, it should not be");
            byte[] circuitState = CSI.getCircuitState();
            assertEquals(circuitState.length, 3, "CircuitStateIndicator.getCircuitState() length is nto correct");
            assertEquals(CSI.getMaintenanceBlockingState(circuitState[0]), 1,
                    "CircuitStateIndicator.getCircuitState()[0] value is not correct");
            assertEquals(CSI.getMaintenanceBlockingState(circuitState[1]), 2,
                    "CircuitStateIndicator.getCircuitState()[1] value is not correct");
            assertEquals(CSI.getMaintenanceBlockingState(circuitState[2]), 3,
                    "CircuitStateIndicator.getCircuitState()[2] value is not correct");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed on get parameter[" + CallReference._PARAMETER_CODE + "]:" + e);
        }

    }

    protected byte[] getDefaultBody() {
        // FIXME: for now we strip MTP part
        byte[] message = {

        0x0C, (byte) 0x0B, CircuitGroupQueryResponseMessage.MESSAGE_CODE

        , 0x02 // ptr to variable part
                , 0x03

                // no optional, so no pointer
                // RangeAndStatus._PARAMETER_CODE
                , 0x01, 0x01
                // CircuitStateIndicator
                , 0x03, 0x01, 0x02, 0x03

        };

        return message;
    }

    protected ISUPMessage getDefaultMessage() {
        return super.messageFactory.createCQR();
    }
}
