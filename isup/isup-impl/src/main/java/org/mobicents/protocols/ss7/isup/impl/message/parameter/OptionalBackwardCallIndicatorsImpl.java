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
 * Start time:11:38:33 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.OptionalBackwardCallIndicators;

/**
 * Start time:11:38:33 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class OptionalBackwardCallIndicatorsImpl extends AbstractISUPParameter implements OptionalBackwardCallIndicators {

    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private boolean inbandInformationIndicator;
    private boolean callDiversionMayOccurIndicator;
    private boolean simpleSegmentationIndicator;
    private boolean mllpUserIndicator;

    public OptionalBackwardCallIndicatorsImpl() {
        super();

    }

    public OptionalBackwardCallIndicatorsImpl(boolean inbandInformationIndicator, boolean callDiversionMayOccurIndicator,
            boolean simpleSegmentationIndicator, boolean mllpUserIndicator) {
        super();
        this.inbandInformationIndicator = inbandInformationIndicator;
        this.callDiversionMayOccurIndicator = callDiversionMayOccurIndicator;
        this.simpleSegmentationIndicator = simpleSegmentationIndicator;
        this.mllpUserIndicator = mllpUserIndicator;
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 1) {
            throw new ParameterException("byte[] must  not be null and length must  be 1");
        }
        this.inbandInformationIndicator = (b[0] & 0x01) == _TURN_ON;
        this.callDiversionMayOccurIndicator = ((b[0] >> 1) & 0x01) == _TURN_ON;
        this.simpleSegmentationIndicator = ((b[0] >> 2) & 0x01) == _TURN_ON;
        this.mllpUserIndicator = ((b[0] >> 3) & 0x01) == _TURN_ON;
        return 1;
    }

    public byte[] encode() throws ParameterException {

        int b0 = 0;

        b0 = this.inbandInformationIndicator ? _TURN_ON : _TURN_OFF;
        b0 |= (this.callDiversionMayOccurIndicator ? _TURN_ON : _TURN_OFF) << 1;
        b0 |= (this.simpleSegmentationIndicator ? _TURN_ON : _TURN_OFF) << 2;
        b0 |= (this.mllpUserIndicator ? _TURN_ON : _TURN_OFF) << 3;

        return new byte[] { (byte) b0 };
    }

    public boolean isInbandInformationIndicator() {
        return this.inbandInformationIndicator;
    }

    public void setInbandInformationIndicator(boolean inbandInformationIndicator) {
        this.inbandInformationIndicator = inbandInformationIndicator;
    }

    public boolean isCallDiversionMayOccurIndicator() {
        return callDiversionMayOccurIndicator;
    }

    public void setCallDiversionMayOccurIndicator(boolean callDiversionMayOccurIndicator) {
        this.callDiversionMayOccurIndicator = callDiversionMayOccurIndicator;
    }

    public boolean isSimpleSegmentationIndicator() {
        return simpleSegmentationIndicator;
    }

    public void setSimpleSegmentationIndicator(boolean simpleSegmentationIndicator) {
        this.simpleSegmentationIndicator = simpleSegmentationIndicator;
    }

    public boolean isMllpUserIndicator() {
        return mllpUserIndicator;
    }

    public void setMllpUserIndicator(boolean mllpUserIndicator) {
        this.mllpUserIndicator = mllpUserIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
