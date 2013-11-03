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
 * Start time:15:16:34 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.TerminatingNetworkRoutingNumber;

/**
 * Start time:15:16:34 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class TerminatingNetworkRoutingNumberImpl extends AbstractNumber implements TerminatingNetworkRoutingNumber {

    // FIXME: shoudl we add max octets ?
    private int tnrnLengthIndicator;
    private int numberingPlanIndicator;
    private int natureOfAddressIndicator;

    public TerminatingNetworkRoutingNumberImpl() {
        super();

    }

    public TerminatingNetworkRoutingNumberImpl(byte[] representation) throws ParameterException {
        super(representation);

    }

    public TerminatingNetworkRoutingNumberImpl(ByteArrayInputStream bis) throws ParameterException {
        super(bis);

    }

    public TerminatingNetworkRoutingNumberImpl(int numberingPlanIndicator) {
        super();
        this.setNumberingPlanIndicator(numberingPlanIndicator);
        this.tnrnLengthIndicator = 0;
    }

    public TerminatingNetworkRoutingNumberImpl(int numberingPlanIndicator, int natureOfAddressIndicator) {
        super();
        this.setNumberingPlanIndicator(numberingPlanIndicator);
        this.setNatureOfAddressIndicator(natureOfAddressIndicator);
        this.tnrnLengthIndicator = 1;
    }

    public TerminatingNetworkRoutingNumberImpl(String address, int numberingPlanIndicator, int natureOfAddressIndicator) {
        super();
        this.setNumberingPlanIndicator(numberingPlanIndicator);
        this.setNatureOfAddressIndicator(natureOfAddressIndicator);
        this.setAddress(address);
    }

    public int decode(byte[] b) throws ParameterException {
        return super.decode(b);
    }

    public byte[] encode() throws ParameterException {
        return super.encode();
    }

    public int decodeHeader(ByteArrayInputStream bis) throws ParameterException {
        if (bis.available() == 0) {
            throw new ParameterException("No more data to read.");
        }
        int b = bis.read() & 0xff;

        this.oddFlag = (b & 0x80) >> 7;
        this.tnrnLengthIndicator = b & 0x0F;
        this.numberingPlanIndicator = (b >> 4) & 0x07;
        return 1;
    }

    public int encodeHeader(ByteArrayOutputStream bos) {

        int b = 0;
        // Even is 000000000 == 0
        boolean isOdd = this.oddFlag == _FLAG_ODD;
        if (isOdd)
            b |= 0x80;
        b |= this.tnrnLengthIndicator & 0x0F;
        b |= (this.numberingPlanIndicator & 0x07) << 4;
        bos.write(b);
        return 1;
    }

    public int decodeBody(ByteArrayInputStream bis) throws ParameterException {
        if (this.tnrnLengthIndicator > 0) {
            if (bis.available() == 0) {
                throw new ParameterException("No more data to read.");
            }
            this.setNatureOfAddressIndicator(bis.read());
            return 1;
        } else {
            return 0;
        }
    }

    public int encodeBody(ByteArrayOutputStream bos) {

        if (this.tnrnLengthIndicator > 0) {
            bos.write(this.natureOfAddressIndicator);
            return 1;
        } else {
            return 0;
        }
    }

    public int decodeDigits(ByteArrayInputStream bis) throws ParameterException {
        if (this.tnrnLengthIndicator - 1 > 0) {
            if (bis.available() == 0) {
                throw new ParameterException("No more data to read.");
            }
            return super.decodeDigits(bis, this.tnrnLengthIndicator - 1);
        } else {
            return 0;
        }

    }

    public int encodeDigits(ByteArrayOutputStream bos) {
        if (this.tnrnLengthIndicator - 1 > 0)
            return super.encodeDigits(bos);
        else
            return 0;
    }

    public void setAddress(String address) {
        // TODO Auto-generated method stub
        super.setAddress(address);
        int l = super.address.length();
        // +1 for NAI
        this.tnrnLengthIndicator = l / 2 + l % 2 + 1;
        if (tnrnLengthIndicator > 9) {
            throw new IllegalArgumentException("Maximum octets for this parameter in digits part is 8.");
            // FIXME: add check for digit (max 7 ?)
        }

        if (this.tnrnLengthIndicator == 9 && !isOddFlag()) {
            // we allow only odd! digits count in this case
            throw new IllegalArgumentException("To many digits. Maximum number of digits is 15 for tnr length of 9.");
        }
    }

    public int getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(int numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator & 0x07;
    }

    public int getNatureOfAddressIndicator() {
        return natureOfAddressIndicator;
    }

    public void setNatureOfAddressIndicator(int natureOfAddressIndicator) {
        this.natureOfAddressIndicator = natureOfAddressIndicator & 0x7F;
    }

    public int getTnrnLengthIndicator() {
        return tnrnLengthIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
