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
 * Start time:13:49:00 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardCallIndicators;

/**
 * Start time:13:49:00 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class BackwardCallIndicatorsImpl extends AbstractISUPParameter implements BackwardCallIndicators {

    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private int chargeIndicator = 0;

    private int calledPartysStatusIndicator = 0;

    private int calledPartysCategoryIndicator = 0;

    private int endToEndMethodIndicator = 0;

    private boolean interworkingIndicator = false;

    private boolean endToEndInformationIndicator = false;

    private boolean isdnUserPartIndicator = false;

    private boolean isdnAccessIndicator = false;

    private boolean echoControlDeviceIndicator = false;

    private boolean holdingIndicator = false;

    private int sccpMethodIndicator = 0;

    public BackwardCallIndicatorsImpl() {

    }

    public BackwardCallIndicatorsImpl(int chargeIndicator, int calledPartysStatusIndicator, int calledPartysCategoryIndicator,
            int endToEndMethodIndicator, boolean interworkingIndicator, boolean endToEndInformationIndicator,
            boolean isdnUserPartIndicator, boolean isdnAccessIndicator, boolean echoControlDeviceIndicator,
            boolean holdingIndicator, int sccpMethodIndicator) {
        super();
        this.chargeIndicator = chargeIndicator;
        this.calledPartysStatusIndicator = calledPartysStatusIndicator;
        this.calledPartysCategoryIndicator = calledPartysCategoryIndicator;
        this.endToEndMethodIndicator = endToEndMethodIndicator;
        this.interworkingIndicator = interworkingIndicator;
        this.endToEndInformationIndicator = endToEndInformationIndicator;
        this.isdnUserPartIndicator = isdnUserPartIndicator;
        this.isdnAccessIndicator = isdnAccessIndicator;
        this.echoControlDeviceIndicator = echoControlDeviceIndicator;
        this.holdingIndicator = holdingIndicator;
        this.sccpMethodIndicator = sccpMethodIndicator;
    }

    /**
     * requres length 2 array.
     *
     * @param b
     */
    public BackwardCallIndicatorsImpl(byte[] b) throws ParameterException {
        this.decode(b);
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 2) {
            throw new ParameterException("byte[] must not be null or have different size than 2");
        }

        int v = b[0];
        this.chargeIndicator = v & 0x03;
        this.calledPartysStatusIndicator = (v >> 2) & 0x03;
        this.calledPartysCategoryIndicator = (v >> 4) & 0x03;
        this.endToEndMethodIndicator = (v >> 6) & 0x03;

        v = b[1];
        this.interworkingIndicator = (v & 0x01) == _TURN_ON;
        this.endToEndInformationIndicator = ((v >> 1) & 0x01) == _TURN_ON;
        this.isdnUserPartIndicator = ((v >> 2) & 0x01) == _TURN_ON;
        this.holdingIndicator = ((v >> 3) & 0x01) == _TURN_ON;
        this.isdnAccessIndicator = ((v >> 4) & 0x01) == _TURN_ON;
        this.echoControlDeviceIndicator = ((v >> 5) & 0x01) == _TURN_ON;
        this.sccpMethodIndicator = (v >> 6) & 0x03;
        return 2;
    }

    public byte[] encode() throws ParameterException {

        byte[] b = new byte[2];
        int v = 0;
        v |= this.chargeIndicator & 0x03;
        v |= (this.calledPartysStatusIndicator & 0x03) << 2;
        v |= (this.calledPartysCategoryIndicator & 0x03) << 4;
        v |= (this.endToEndMethodIndicator & 0x03) << 6;
        b[0] = (byte) v;
        v = 0;

        v |= (this.interworkingIndicator ? _TURN_ON : _TURN_OFF);
        v |= (this.endToEndInformationIndicator ? _TURN_ON : _TURN_OFF) << 1;
        v |= (this.isdnUserPartIndicator ? _TURN_ON : _TURN_OFF) << 2;
        v |= (this.holdingIndicator ? _TURN_ON : _TURN_OFF) << 3;
        v |= (this.isdnAccessIndicator ? _TURN_ON : _TURN_OFF) << 4;
        v |= (this.echoControlDeviceIndicator ? _TURN_ON : _TURN_OFF) << 5;
        v |= (this.sccpMethodIndicator & 0x03) << 6;

        b[1] = (byte) v;
        return b;
    }

    public int encode(ByteArrayOutputStream bos) throws ParameterException {
        byte[] b = encode();
        try {
            bos.write(b);
        } catch (IOException e) {
            throw new ParameterException(e);
        }
        return b.length;
    }

    public int getChargeIndicator() {
        return chargeIndicator;
    }

    public void setChargeIndicator(int chargeIndicator) {
        this.chargeIndicator = chargeIndicator;
    }

    public int getCalledPartysStatusIndicator() {
        return calledPartysStatusIndicator;
    }

    public void setCalledPartysStatusIndicator(int calledPartysStatusIndicator) {
        this.calledPartysStatusIndicator = calledPartysStatusIndicator;
    }

    public int getCalledPartysCategoryIndicator() {
        return calledPartysCategoryIndicator;
    }

    public void setCalledPartysCategoryIndicator(int calledPartysCategoryIndicator) {
        this.calledPartysCategoryIndicator = calledPartysCategoryIndicator;
    }

    public int getEndToEndMethodIndicator() {
        return endToEndMethodIndicator;
    }

    public void setEndToEndMethodIndicator(int endToEndMethodIndicator) {
        this.endToEndMethodIndicator = endToEndMethodIndicator;
    }

    public boolean isInterworkingIndicator() {
        return interworkingIndicator;
    }

    public void setInterworkingIndicator(boolean interworkingIndicator) {
        this.interworkingIndicator = interworkingIndicator;
    }

    public boolean isEndToEndInformationIndicator() {
        return endToEndInformationIndicator;
    }

    public void setEndToEndInformationIndicator(boolean endToEndInformationIndicator) {
        this.endToEndInformationIndicator = endToEndInformationIndicator;
    }

    public boolean isIsdnUserPartIndicator() {
        return isdnUserPartIndicator;
    }

    public void setIsdnUserPartIndicator(boolean isdnUserPartIndicator) {
        this.isdnUserPartIndicator = isdnUserPartIndicator;
    }

    public boolean isIsdnAccessIndicator() {
        return isdnAccessIndicator;
    }

    public void setIsdnAccessIndicator(boolean isdnAccessIndicator) {
        this.isdnAccessIndicator = isdnAccessIndicator;
    }

    public boolean isEchoControlDeviceIndicator() {
        return echoControlDeviceIndicator;
    }

    public void setEchoControlDeviceIndicator(boolean echoControlDeviceIndicator) {
        this.echoControlDeviceIndicator = echoControlDeviceIndicator;
    }

    public boolean isHoldingIndicator() {
        return holdingIndicator;
    }

    public void setHoldingIndicator(boolean holdingIndicator) {
        this.holdingIndicator = holdingIndicator;
    }

    public int getSccpMethodIndicator() {
        return sccpMethodIndicator;
    }

    public void setSccpMethodIndicator(int sccpMethodIndicator) {
        this.sccpMethodIndicator = sccpMethodIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
