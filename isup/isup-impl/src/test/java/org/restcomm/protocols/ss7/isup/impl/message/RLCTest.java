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

package org.restcomm.protocols.ss7.isup.impl.message;

import org.restcomm.protocols.ss7.isup.impl.message.AbstractISUPMessage;
import org.restcomm.protocols.ss7.isup.message.ISUPMessage;
import org.restcomm.protocols.ss7.isup.message.ReleaseCompleteMessage;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Start time:15:07:07 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RLCTest extends MessageHarness {

    protected byte[] getDefaultBody() {
        // FIXME: for now we strip MTP part
        byte[] message = {

        0x0C, (byte) 0x0B, 0x10, 0x00

        };

        return message;
    }

    protected ISUPMessage getDefaultMessage() {
        return super.messageFactory.createRLC();
    }

    @Test(groups = { "functional.encode", "functional.decode", "message" })
    public void testCauseIndicators() throws Exception {
        final CauseIndicators causeIndicators = super.parameterFactory.createCauseIndicators();
        causeIndicators.setCauseValue(CauseIndicators._CV_ALL_CLEAR);
        final ReleaseCompleteMessage releaseCompleteMessage = super.messageFactory.createRLC(1);
        releaseCompleteMessage.setCauseIndicators(causeIndicators);
        releaseCompleteMessage.setSls(2);
        final byte[] encoded = ((AbstractISUPMessage) releaseCompleteMessage).encode();
        final AbstractISUPMessage msg = (AbstractISUPMessage) getDefaultMessage();
        msg.decode(encoded, messageFactory,parameterFactory);
        final ReleaseCompleteMessage decodedRLC = (ReleaseCompleteMessage) msg;
        Assert.assertNotNull(decodedRLC.getCauseIndicators(), "No Cause Indicators present!");
        Assert.assertEquals(decodedRLC.getCauseIndicators().getCauseValue(), CauseIndicators._CV_ALL_CLEAR, "Wrong CauseValue");
    }
}
