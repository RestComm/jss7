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
 * Start time:13:31:04 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.AccessDeliveryInformation;

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

}
