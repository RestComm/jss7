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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageCompatibilityInstructionIndicator;

/**
 * @author baranowb
 *
 */
public class MessageCompatibilityInformationImpl extends AbstractISUPParameter implements MessageCompatibilityInformation {

    private List<MessageCompatibilityInstructionIndicator> indicators = new LinkedList<MessageCompatibilityInstructionIndicator>();

    public MessageCompatibilityInformationImpl() {
        // TODO Auto-generated constructor stub
    }

    public MessageCompatibilityInformationImpl(byte[] body) throws ParameterException {
        decode(body);
    }

    @Override
    public int getCode() {
        return _PARAMETER_CODE;
    }

    @Override
    public void setMessageCompatibilityInstructionIndicators(MessageCompatibilityInstructionIndicator... indicators) {
        this.indicators.clear();
        for(MessageCompatibilityInstructionIndicator i:indicators){
            if(i == null)
                continue;
            this.indicators.add(i);
        }
    }

    @Override
    public MessageCompatibilityInstructionIndicator[] getMessageCompatibilityInstructionIndicators() {
        return this.indicators.toArray(new MessageCompatibilityInstructionIndicator[this.indicators.size()]);
    }

    @Override
    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length == 0) {
            throw new ParameterException("byte[] must  not be null and length must  be greater than 0");
        }
        final int limit = b.length -1;
        for(int index = 0;index<b.length;index++){
            int v = b[index];
            if(index==limit){
                if( (v & 0x7F) == 0){
                    throw new ParameterException("Extension bit indicates more content, but byte[] is done... "+Arrays.toString(b));
                }
            } else {
                if( (v & 0x7F) == 1){
                    throw new ParameterException("Extension bit indicates end of content, but byte[] is not done... "+Arrays.toString(b));
                }
            }
            MessageCompatibilityInstructionIndicatorImpl instructions = new MessageCompatibilityInstructionIndicatorImpl();
            instructions.decode(new byte[]{(byte)v});
            this.indicators.add(instructions);
        }
        return b.length;
    }

    @Override
    public byte[] encode() throws ParameterException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(this.indicators.size());
        final int limit = this.indicators.size() -1;
        for(int index = 0;index<this.indicators.size();index++){
            byte[] val = ((MessageCompatibilityInstructionIndicatorImpl)this.indicators.get(index)).encode();
            if(index==limit){
                val[0] = (byte)(val[0] & 0x7F | 0x80);
            } else {
                val[0] = (byte)(val[0] & 0x7F);
            }
            try {
                baos.write(val);
            } catch (IOException e) {
                throw new ParameterException(e);
            }
        }
        return baos.toByteArray();
    }

}
