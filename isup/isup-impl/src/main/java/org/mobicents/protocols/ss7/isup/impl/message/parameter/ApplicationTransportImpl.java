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
 * Start time:15:10:58 2009-04-05<br>
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
import org.mobicents.protocols.ss7.isup.message.parameter.ApplicationTransport;

/**
 * Start time:15:10:58 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ApplicationTransportImpl extends AbstractISUPParameter implements ApplicationTransport {
    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private Byte applicationContextIdentifier, apmSegmentationIndicator, segmentationLocalReference;
    private Boolean sendNotificationIndicator, releaseCallIndicator, segmentationIndicator;
    private byte[] encapsulatedApplicationData;

    public ApplicationTransportImpl() {
        super();
    }

    public ApplicationTransportImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public int decode(byte[] b) throws ParameterException {
        // 4+ lines, depending on "ext" bits...
        if (b == null || b.length < 1) {
            throw new ParameterException("byte[] must not be null or have bigger size.");
        }

        // integrity check
        for (int index = 0; index < 4 && index< b.length; index++) {
            if( (b[index] & 0x80) == 0){
                //expect more
                if(b.length -1 == index){
                    //but there is nothing more
                    throw new ParameterException();
                }
            } else {
                //this should be last
                if(b.length-1-index>0){
                    throw new ParameterException();
                }
            }
        }
        this.applicationContextIdentifier = (byte) (b[0] & 0x7F);
        if (b.length == 1)
            return b.length;
        this.releaseCallIndicator = (b[1] & 0x01) == _TURN_ON;
        this.sendNotificationIndicator = ((b[1] >> 1) & 0x01) == _TURN_ON;
        if (b.length == 2)
            return b.length;
        this.apmSegmentationIndicator = (byte) (b[2] & 0x3F);
        this.segmentationIndicator = ((b[2] >> 6) & 0x01) == _TURN_ON;
        if (b.length == 3)
            return b.length;
        this.segmentationLocalReference = (byte) (b[3] & 0x7F);
        if (b.length == 4)
            return b.length;
        this.encapsulatedApplicationData = new byte[b.length - 4];
        System.arraycopy(b, 4, this.encapsulatedApplicationData, 0, this.encapsulatedApplicationData.length);
        return b.length;
    }

    public byte[] encode() throws ParameterException {
        if(this.applicationContextIdentifier == null){
            throw new ParameterException();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean end = false;
        int value = 0x80;
        if (this.releaseCallIndicator != null) {
            value = 0;
        } else {
            end = true;
        }
        value |= (this.applicationContextIdentifier & 0x7F);
        baos.write(value);
        if (end)
            return baos.toByteArray();
        if (this.apmSegmentationIndicator != null) {
            value = 0;
        } else {
            value = 0x80;
            end = true;
        }
        value |= ((this.sendNotificationIndicator ? _TURN_ON : _TURN_OFF) << 1);
        value |= (this.releaseCallIndicator ? _TURN_ON : _TURN_OFF);
        baos.write(value);
        if (end)
            return baos.toByteArray();

        if (this.segmentationLocalReference != null) {
            value = 0;
        } else {
            value = 0x80;
            end = true;
        }
        value |= ((this.segmentationIndicator ? _TURN_ON : _TURN_OFF) << 6);
        value |= ((this.apmSegmentationIndicator) & 0x3F);
        baos.write(value);
        if (end)
            return baos.toByteArray();

        if (this.encapsulatedApplicationData != null) {
            value = 0;
        } else {
            value = 0x80;
            end = true;
        }
        value |= ((this.segmentationLocalReference) & 0x7F);
        baos.write(value);
        if (end)
            return baos.toByteArray();

        try {
            baos.write(this.encapsulatedApplicationData);
        } catch (IOException ioe) {
            throw new ParameterException(ioe);
        }
        return baos.toByteArray();
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    public Byte getApplicationContextIdentifier() {
        return this.applicationContextIdentifier;
    }

    @Override
    public void setApplicationContextIdentifier(Byte v) {
        this.applicationContextIdentifier = v;
    }

    @Override
    public Boolean isSendNotificationIndicator() {
        return this.sendNotificationIndicator;
    }

    @Override
    public void setSendNotificationIndicator(Boolean v) {
        this.sendNotificationIndicator = v;
    }

    @Override
    public Boolean isReleaseCallIndicator() {
        return this.releaseCallIndicator;
    }

    @Override
    public void setReleaseCallIndicator(Boolean v) {
        this.releaseCallIndicator = v;
    }

    @Override
    public Boolean isSegmentationIndicator() {
        return this.segmentationIndicator;
    }

    @Override
    public void setSegmentationIndicator(Boolean v) {
        this.segmentationIndicator = v;
    }

    @Override
    public Byte getAPMSegmentationIndicator() {
        return this.apmSegmentationIndicator;
    }

    @Override
    public void setAPMSegmentationIndicator(Byte v) {
        this.apmSegmentationIndicator = v;
    }

    @Override
    public Byte getSegmentationLocalReference() {
        return this.segmentationLocalReference;
    }

    @Override
    public void setSegmentationLocalReference(Byte v) {
        this.segmentationLocalReference = v;
    }

    @Override
    public byte[] getEncapsulatedApplicationInformation() {
        return this.encapsulatedApplicationData;
    }

    @Override
    public void setEncapsulatedApplicationInformation(byte[] v) {
        this.encapsulatedApplicationData = v;
    }
}
