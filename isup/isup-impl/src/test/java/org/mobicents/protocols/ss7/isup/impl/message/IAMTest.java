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
 * Start time:15:07:07 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import static org.testng.Assert.assertNotNull;

import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.InitialAddressMessage;
import org.testng.annotations.Test;

/**
 * Start time:15:07:07 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class IAMTest extends MessageHarness {

    @Test(groups = { "functional.encode", "functional.decode", "message" })
    public void testTwo_Parameters() throws Exception {
        byte[] message = getDefaultBody();

        // InitialAddressMessageImpl iam=new InitialAddressMessageImpl(this,message);
        InitialAddressMessage iam = super.messageFactory.createIAM(0);
        ((AbstractISUPMessage) iam).decode(message, messageFactory,parameterFactory);
        assertNotNull(iam.getNatureOfConnectionIndicators());
        assertNotNull(iam.getForwardCallIndicators());
        assertNotNull(iam.getCallingPartCategory());
        assertNotNull(iam.getTransmissionMediumRequirement());
        assertNotNull(iam.getCalledPartyNumber());
        assertNotNull(iam.getCallingPartyNumber());

    }

    public void testThree_Trace() throws Exception {

        byte[] message = new byte[] { 0x73,
                0x00, // CIC
                0x01, // IAM
                0x11, // M: natur of connection indicators
                0x60, // M: forward call ind
                0x00, // M: forward call ind
                0x0a, // M: calling party's category
                0x03, // M: transmission medium req
                0x02, // pointer to mandatory var:
                0x09, // pointer to optional
                0x07, // MV: len of called party number
                (byte) 0x83, (byte) 0x90, 0x79, 0x70, 0x45, 0x26, 0x09,
                0x0a, // opt, CallingParty Num
                0x07, (byte) 0x83, 0x13, 0x59, 0x63, (byte) 0x85, (byte) 0x96, 0x00,
                0x3f, // opt Location Num
                (byte) 0x07, (byte) 0x83, 0x17, 0x17, 0x50, 0x20, 0x03, 0x00,
                0x03, // opt Forward GVNS
                0x13, 0x28, 0x11, 0x42, 0x45, 0x52, 0x54, 0x48, 0x41, 0x4c, 0x49, 0x4e, 0x20, 0x4a, 0x61, 0x63, 0x71, 0x75,
                0x65, 0x73, 0x00
        /*
         * ,0x1b, 0x1d
         */};
        InitialAddressMessage iam = super.messageFactory.createIAM(0);
        ((AbstractISUPMessage) iam).decode(message, messageFactory,parameterFactory);
    }

    protected byte[] getDefaultBody() {
        // FIXME: for now we strip MTP part
        byte[] message = {

        0x0C, (byte) 0x0B, 0x01, 0x10, 0x00, 0x01, 0x0A, 0x03, 0x02, 0x0A, 0x08, 0x03, 0x10, (byte) 0x83, 0x60, 0x38, 0x04,
                0x10, 0x65, 0x0A, 0x07, 0x03, 0x13, 0x09, 0x32, 0x36, 0x11, 0x37, 0x00

        };

        return message;
    }

    protected ISUPMessage getDefaultMessage() {
        return super.messageFactory.createIAM(0);
    }
}
