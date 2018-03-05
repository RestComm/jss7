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

/**
 * Start time:13:31:04 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.AccessDeliveryInformation;

/**
 * Start time:13:31:04 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 *
 */
public class AccessDeliveryInformationImpl extends AbstractISUPParameter implements AccessDeliveryInformation {

    private int accessDeliveryIndicator;

    public AccessDeliveryInformationImpl(int accessDeliveryIndicator) {
        super();
        this.accessDeliveryIndicator = accessDeliveryIndicator;
    }

    public AccessDeliveryInformationImpl() {
        super();

    }

    public AccessDeliveryInformationImpl(byte[] representation) throws ParameterException {
        super();
        this.decode(representation);
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 1) {
            throw new IllegalArgumentException("byte[] must not be null or have different size than 1");
        }
        this.accessDeliveryIndicator = (byte) (b[0] & 0x01);

        return 1;
    }

    public byte[] encode() throws ParameterException {

        return new byte[] { (byte) this.accessDeliveryIndicator };
    }

    public int encode(ByteArrayOutputStream bos) throws ParameterException {
        bos.write(this.accessDeliveryIndicator);
        return 1;
    }

    public int getAccessDeliveryIndicator() {
        return accessDeliveryIndicator;
    }

    public void setAccessDeliveryIndicator(int accessDeliveryIndicator) {
        this.accessDeliveryIndicator = accessDeliveryIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("AccessDeliveryInformation [");

        sb.append("accessDeliveryIndicator=");
        sb.append(accessDeliveryIndicator);

        sb.append("]");
        return sb.toString();
    }

}
