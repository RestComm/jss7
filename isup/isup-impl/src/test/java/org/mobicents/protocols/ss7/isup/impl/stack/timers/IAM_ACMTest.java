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

package org.mobicents.protocols.ss7.isup.impl.stack.timers;

import org.mobicents.protocols.ss7.isup.ISUPTimeoutEvent;
import org.mobicents.protocols.ss7.isup.message.AddressCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.InitialAddressMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.ForwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.NatureOfConnectionIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumRequirement;

/**
 * @author baranowb
 *
 */
public class IAM_ACMTest extends SingleTimers {

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.stack.timers.SingleTimers#getT()
     */

    protected long getT() {
        return ISUPTimeoutEvent.T7_DEFAULT;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.stack.timers.SingleTimers#getT_ID()
     */

    protected int getT_ID() {
        return ISUPTimeoutEvent.T7;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.stack.timers.EventTestHarness#getRequest()
     */

    protected ISUPMessage getRequest() {
        InitialAddressMessage msg = super.provider.getMessageFactory().createIAM(1);
        NatureOfConnectionIndicators nai = super.provider.getParameterFactory().createNatureOfConnectionIndicators();
        ForwardCallIndicators fci = super.provider.getParameterFactory().createForwardCallIndicators();
        CallingPartyCategory cpg = super.provider.getParameterFactory().createCallingPartyCategory();
        TransmissionMediumRequirement tmr = super.provider.getParameterFactory().createTransmissionMediumRequirement();
        CalledPartyNumber cpn = super.provider.getParameterFactory().createCalledPartyNumber();
        cpn.setAddress("14614577");

        msg.setNatureOfConnectionIndicators(nai);
        msg.setForwardCallIndicators(fci);
        msg.setCallingPartCategory(cpg);
        msg.setCalledPartyNumber(cpn);
        msg.setTransmissionMediumRequirement(tmr);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.stack.timers.EventTestHarness#getAnswer()
     */

    protected ISUPMessage getAnswer() {
        AddressCompleteMessage ans = super.provider.getMessageFactory().createACM();
        BackwardCallIndicators bci = super.provider.getParameterFactory().createBackwardCallIndicators();
        ans.setBackwardCallIndicators(bci);
        CircuitIdentificationCode cic = super.provider.getParameterFactory().createCircuitIdentificationCode();
        cic.setCIC(1);
        ans.setCircuitIdentificationCode(cic);
        return ans;
    }
}
