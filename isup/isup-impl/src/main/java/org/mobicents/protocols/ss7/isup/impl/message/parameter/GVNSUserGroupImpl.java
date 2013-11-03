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
 * Start time:13:58:48 2009-04-04<br>
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
import org.mobicents.protocols.ss7.isup.message.parameter.GVNSUserGroup;

/**
 * Start time:13:58:48 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class GVNSUserGroupImpl extends AbstractNumber implements GVNSUserGroup {

    // FIXME: shoudl we add max octets ?
    private int gugLengthIndicator;

    public GVNSUserGroupImpl() {

    }

    public GVNSUserGroupImpl(byte[] representation) throws ParameterException {
        super(representation);

    }

    public GVNSUserGroupImpl(ByteArrayInputStream bis) throws ParameterException {
        super(bis);

    }

    public GVNSUserGroupImpl(String address) {
        super(address);

    }

    public int decode(byte[] b) throws ParameterException {
        return super.decode(b);
    }

    public byte[] encode() throws ParameterException {
        return super.encode();
    }

    public int decodeHeader(ByteArrayInputStream bis) throws IllegalArgumentException {
        int b = bis.read() & 0xff;

        this.oddFlag = (b & 0x80) >> 7;
        this.gugLengthIndicator = b & 0x0F;
        return 1;
    }

    public int encodeHeader(ByteArrayOutputStream bos) {
        int b = 0;
        // Even is 000000000 == 0
        boolean isOdd = this.oddFlag == _FLAG_ODD;
        if (isOdd)
            b |= 0x80;
        b |= this.gugLengthIndicator & 0x0F;
        bos.write(b);
        return 1;
    }

    public int decodeBody(ByteArrayInputStream bis) throws IllegalArgumentException {

        return 0;
    }

    public int encodeBody(ByteArrayOutputStream bos) {

        return 0;
    }

    public int getGugLengthIndicator() {
        return gugLengthIndicator;
    }

    public int decodeDigits(ByteArrayInputStream bis) throws IllegalArgumentException, ParameterException {
        return super.decodeDigits(bis, this.gugLengthIndicator);
    }

    public void setAddress(String address) {
        // TODO Auto-generated method stub
        super.setAddress(address);
        int l = super.address.length();
        this.gugLengthIndicator = l / 2 + l % 2;
        if (gugLengthIndicator > 8) {
            throw new IllegalArgumentException("Maximum octets for this parameter in digits part is 8.");
            // FIXME: add check for digit (max 7 ?)
        }
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
