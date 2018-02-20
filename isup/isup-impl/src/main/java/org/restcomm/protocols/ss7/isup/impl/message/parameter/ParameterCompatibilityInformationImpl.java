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
 * Start time:12:39:34 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.ParameterCompatibilityInstructionIndicators;

/**
 * Start time:12:39:34 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ParameterCompatibilityInformationImpl extends AbstractISUPParameter implements ParameterCompatibilityInformation {

    private List<ParameterCompatibilityInstructionIndicators> instructionIndicators = new ArrayList<ParameterCompatibilityInstructionIndicators>();

    public ParameterCompatibilityInformationImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public ParameterCompatibilityInformationImpl() {
        super();

    }

    public int decode(byte[] b) throws ParameterException {

        if (b == null || b.length < 2) {
            throw new ParameterException("byte[] must  not be null and length must  greater than 1");
        }

        ByteArrayOutputStream bos = null;
        boolean newParameter = true;
        byte parameterCode = 0;

        for (int index = 0; index < b.length; index++) {
            if (newParameter) {
                parameterCode = b[index];
                bos = new ByteArrayOutputStream();
                newParameter = false;
                continue;
            } else {
                bos.write(b[index]);

                if (((b[index] >> 7) & 0x01) == 0) {
                    // ext bit is zero, this is last octet

                    if (bos.size() < 3) {
                        this.instructionIndicators.add(new ParameterCompatibilityInstructionIndicatorsImpl(parameterCode, bos
                                .toByteArray()));
                    } else {
                        this.instructionIndicators.add(new ParameterCompatibilityInstructionIndicatorsImpl(
                                parameterCode, bos.toByteArray(), true));
                    }
                    newParameter = true;
                } else {

                    continue;
                }
            }

        }

        return b.length;
    }

    public byte[] encode() throws ParameterException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int index = 0; index < this.instructionIndicators.size(); index++) {

            try {
                final ParameterCompatibilityInstructionIndicators ii = this.instructionIndicators.get(index);
                bos.write(ii.getParameterCode());
                bos.write(((Encodable) ii).encode());
            } catch (IOException e) {
                throw new ParameterException(e);
            }
        }
        return bos.toByteArray();
    }

    @Override
    public void setParameterCompatibilityInstructionIndicators(
            ParameterCompatibilityInstructionIndicators... compatibilityInstructionIndicators) {
        this.instructionIndicators.clear();
        if(compatibilityInstructionIndicators == null || compatibilityInstructionIndicators.length == 0)
            return;
        for(ParameterCompatibilityInstructionIndicators ii: compatibilityInstructionIndicators)
            if(ii!=null)
                this.instructionIndicators.add(ii);
    }

    @Override
    public ParameterCompatibilityInstructionIndicators[] getParameterCompatibilityInstructionIndicators() {
        return this.instructionIndicators.toArray(new ParameterCompatibilityInstructionIndicators[this.instructionIndicators.size()]);
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("ParameterCompatibilityInformation [");

        for (ParameterCompatibilityInstructionIndicators pci : instructionIndicators) {
            sb.append("ParameterCompatibilityInstructionIndicators=[");
            sb.append(pci);
            sb.append("], ");
        }

        sb.append("]");
        return sb.toString();
    }
}
