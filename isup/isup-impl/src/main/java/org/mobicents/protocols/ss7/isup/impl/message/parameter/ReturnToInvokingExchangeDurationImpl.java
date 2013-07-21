/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.InformationType;
import org.mobicents.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeDuration;

/**
 * @author baranowb
 *
 */
public class ReturnToInvokingExchangeDurationImpl extends AbstractInformationImpl implements ReturnToInvokingExchangeDuration {

    private int duration;

    public ReturnToInvokingExchangeDurationImpl() {
        super(InformationType.ReturnToInvokingExchangeDuration);
        super.tag = 0x01;
    }

    @Override
    public void setDuration(int seconds) {
        this.duration = seconds & 0XFFFF;
    }

    @Override
    public int getDuration() {
        return this.duration;
    }

    @Override
    byte[] encode() throws ParameterException {
        byte[] data;
        if(this.duration > 0xFF){
            data = new byte[2];
            data[1] = (byte) ((this.duration >> 8) & 0xFF);
        } else {
            data = new byte[1];
        }

        data[0] = (byte) this.duration;
        return data;
    }

    @Override
    void decode(byte[] b) throws ParameterException {
        if(b.length != 1 && b.length!=2){
            throw new ParameterException("Wrong numbder of bytes: "+b.length);
        }
        this.duration = (b[0] & 0xFF);
        if(b.length == 2){
             this.duration |= ((b[1] & 0xFF) << 8);
        }
    }
}
