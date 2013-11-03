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

package org.mobicents.protocols.ss7.isup.impl.message;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.mobicents.protocols.ss7.isup.message.AnswerMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.ServiceActivation;
import org.testng.annotations.Test;

/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * Test for ANM
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ANMTest extends MessageHarness {

    @Test(groups = { "functional.encode", "functional.decode", "message" })
    public void testTwo_Params() throws Exception {
        byte[] message = getDefaultBody();

        // AnswerMessageImpl ANM=new AnswerMessageImpl(this,message);
        AnswerMessage ANM = super.messageFactory.createANM();
        ((AbstractISUPMessage) ANM).decode(message, messageFactory,parameterFactory);
        try {
            CallReference cr = (CallReference) ANM.getParameter(CallReference._PARAMETER_CODE);
            assertNotNull(cr, "Call Reference return is null, it should not be");
            if (cr == null)
                return;
            assertEquals(cr.getCallIdentity(), 65793, "CallIdentity missmatch");
            assertEquals(cr.getSignalingPointCode(), 478, "SignalingPointCode missmatch");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed on get parameter[" + CallReference._PARAMETER_CODE + "]:" + e);
        }
        try {
            ServiceActivation sa = (ServiceActivation) ANM.getParameter(ServiceActivation._PARAMETER_CODE);
            assertNotNull(sa, "Service Activation return is null, it should not be");
            if (sa == null)
                return;

            byte[] b = sa.getFeatureCodes();
            assertNotNull(b, "ServerActivation.getFeatureCodes() is null");
            if (b == null) {
                return;
            }
            assertEquals(b.length, 7, "Length of param is wrong");
            if (b.length != 7)
                return;
            assertTrue(super.makeCompare(b, new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 }),
                    "Content of ServiceActivation.getFeatureCodes is wrong");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed on get parameter[" + CallReference._PARAMETER_CODE + "]:" + e);
        }

    }

    protected byte[] getDefaultBody() {
        byte[] message = {

        0x0C, (byte) 0x0B, AnswerMessage.MESSAGE_CODE
                // No mandatory varaible part, no ptr
                , 0x01 // ptr to optional part

                // Call reference
                , 0x01, 0x05, 0x01, 0x01, 0x01, (byte) 0xDE, 0x01
                // ServiceActivation
                , 0x33, 0x07, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07

                // End of optional part
                , 0x0

        };
        return message;
    }

    protected ISUPMessage getDefaultMessage() {
        return super.messageFactory.createANM();
    }
}
