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
 * Start time:08:29:07 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.MCIDResponseIndicators;

/**
 * Start time:08:29:07 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MCIDResponseIndicatorsImpl extends AbstractISUPParameter implements MCIDResponseIndicators {

    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;
    public static boolean HOLDING_NOT_RROVIDED = false;

    public static boolean HOLDING_PROVIDED = true;

    public static boolean MCID_INCLUDED = true;

    public static boolean MCID_NOT_INCLUDED = false;

    private boolean mcidIncludedIndicator = false;
    private boolean holdingProvidedIndicator = false;

    public MCIDResponseIndicatorsImpl() {
        super();

    }

    public MCIDResponseIndicatorsImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public MCIDResponseIndicatorsImpl(boolean mcidIncludedIndicator, boolean holdingProvidedIndicator) {
        super();
        this.mcidIncludedIndicator = mcidIncludedIndicator;
        this.holdingProvidedIndicator = holdingProvidedIndicator;
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 1) {
            throw new ParameterException("byte[] must  not be null and length must  be 1");
        }

        this.mcidIncludedIndicator = (b[0] & 0x01) == _TURN_ON;
        this.holdingProvidedIndicator = ((b[0] >> 1) & 0x01) == _TURN_ON;
        return 1;
    }

    public byte[] encode() throws ParameterException {
        int b0 = 0;

        b0 |= (this.mcidIncludedIndicator ? _TURN_ON : _TURN_OFF);
        b0 |= ((this.holdingProvidedIndicator ? _TURN_ON : _TURN_OFF)) << 1;

        return new byte[] { (byte) b0 };
    }

    public boolean isMcidIncludedIndicator() {
        return mcidIncludedIndicator;
    }

    public void setMcidIncludedIndicator(boolean mcidIncludedIndicator) {
        this.mcidIncludedIndicator = mcidIncludedIndicator;
    }

    public boolean isHoldingProvidedIndicator() {
        return holdingProvidedIndicator;
    }

    public void setHoldingProvidedIndicator(boolean holdingProvidedIndicator) {
        this.holdingProvidedIndicator = holdingProvidedIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
