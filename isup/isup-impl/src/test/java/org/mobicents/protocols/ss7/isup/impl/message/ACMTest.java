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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.mobicents.protocols.ss7.isup.message.AddressCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.testng.annotations.Test;

/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * Test for ACM
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ACMTest extends MessageHarness {

    @Test(groups = { "functional.encode", "functional.decode", "message" })
    public void testTwo_Params() throws Exception {

        byte[] message = getDefaultBody();

        // AddressCompleteMessageImpl acm=new
        // AddressCompleteMessageImpl(this,message);
        AddressCompleteMessage acm = super.messageFactory.createACM();
        ((AbstractISUPMessage) acm).decode(message, messageFactory,parameterFactory);

        assertNotNull(acm.getBackwardCallIndicators(), "BackwardCallIndicator is null");
        assertNotNull(acm.getOptionalBackwardCallIndicators(), "OptionalBackwardCallIndicator is null");
        assertNotNull(acm.getCauseIndicators(), "Cause Indicator is null");

        BackwardCallIndicators bci = acm.getBackwardCallIndicators();
        assertEquals(1, bci.getChargeIndicator(), "BackwardCallIndicator charge indicator does not match");
        assertEquals(0, bci.getCalledPartysStatusIndicator(), "BackwardCallIndicator called party status does not match");
        assertEquals(0, bci.getCalledPartysCategoryIndicator(), "BackwardCallIndicator called party category does not match");
        assertFalse(bci.isInterworkingIndicator());
        assertFalse(bci.isEndToEndInformationIndicator());
        assertFalse(bci.isIsdnAccessIndicator());
        assertFalse(bci.isHoldingIndicator());
        assertTrue(bci.isEchoControlDeviceIndicator());
        assertEquals(0, bci.getSccpMethodIndicator(), "BackwardCallIndicator sccp method does not match");

        CircuitIdentificationCode cic = acm.getCircuitIdentificationCode();
        assertNotNull(cic, "CircuitIdentificationCode must not be null");
        assertEquals(getDefaultCIC(), cic.getCIC(), "CircuitIdentificationCode value does not match");

    }

    protected byte[] getDefaultBody() {
        byte[] message = {

        0x0C, (byte) 0x0B, 0x06, 0x01, 0x20, 0x01, 0x29, 0x01, 0x01, 0x12, 0x02, (byte) 0x82, (byte) 0x9C, 0x00

        };
        return message;
    }

    protected ISUPMessage getDefaultMessage() {
        return super.messageFactory.createACM();
    }
}
