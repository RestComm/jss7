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
 * Start time:00:05:36 2009-09-07<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.mobicents.protocols.ss7.isup.message.SuspendMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageType;
import org.mobicents.protocols.ss7.isup.message.parameter.SuspendResumeIndicators;

/**
 * Start time:00:05:36 2009-09-07<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class SuspendMessageImpl extends ISUPMessageImpl implements SuspendMessage {
    private static final int _MANDATORY_VAR_COUNT = 0;

    static final int _INDEX_F_MessageType = 0;
    static final int _INDEX_F_SuspendResumeIndicators = 1;
    static final int _INDEX_O_CallReference = 0;
    static final int _INDEX_O_EndOfOptionalParameters = 1;

    protected static final List<Integer> mandatoryParam;
    static {
        List<Integer> tmp = new ArrayList<Integer>();
        tmp.add(_INDEX_F_MessageType);
        tmp.add(_INDEX_F_SuspendResumeIndicators);
        mandatoryParam = Collections.unmodifiableList(tmp);

    }

    public static final MessageTypeImpl _MESSAGE_TYPE = new MessageTypeImpl(MESSAGE_CODE);
    /**
     *
     * @param source
     * @throws ParameterException
     */
    public SuspendMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes,
            Set<Integer> optionalCodes, Map<Integer, Integer> mandatoryCode2Index,
            Map<Integer, Integer> mandatoryVariableCode2Index, Map<Integer, Integer> optionalCode2Index) {
        super(mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index,
                optionalCode2Index);

        super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
        super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, _END_OF_OPTIONAL_PARAMETERS);
    }

    protected int decodeMandatoryParameters(ISUPParameterFactory parameterFactory, byte[] b, int index)
            throws ParameterException {
        int localIndex = index;
        index += super.decodeMandatoryParameters(parameterFactory, b, index);

        if (b.length - index > 0) {
            byte[] si = new byte[1];
            si[0]=b[index++];
            SuspendResumeIndicators sri = parameterFactory.createSuspendResumeIndicators();
            ((AbstractISUPParameter)sri).decode(si);
            this.setSuspendResumeIndicators(sri);
            return index - localIndex;
        } else {
            throw new ParameterException("byte[] must have atleast eight octets");
        }
    }

    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, int parameterIndex)
            throws ParameterException {
        // TODO Auto-generated method stub

    }

    protected void decodeOptionalBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, byte parameterCode)
            throws ParameterException {
        switch (parameterCode & 0xFF) {
            case CallReference._PARAMETER_CODE:
                CallReference v = parameterFactory.createCallReference();
                ((AbstractISUPParameter) v).decode(parameterBody);
                setCallReference(v);
                break;

            default:
                throw new ParameterException("Unrecognized parameter code for optional part: " + parameterCode);
        }

    }

    public MessageType getMessageType() {
        return _MESSAGE_TYPE;
    }

    protected int getNumberOfMandatoryVariableLengthParameters() {
        return _MANDATORY_VAR_COUNT;
    }

    public boolean hasAllMandatoryParameters() {
        if (!super.f_Parameters.keySet().containsAll(mandatoryParam) || super.f_Parameters.values().contains(null)) {
            return false;
        }

        return true;
    }

    protected boolean optionalPartIsPossible() {

        return true;
    }

    @Override
    public void setSuspendResumeIndicators(SuspendResumeIndicators ri) {
        super.f_Parameters.put(_INDEX_F_SuspendResumeIndicators, ri);
    }

    @Override
    public SuspendResumeIndicators getSuspendResumeIndicators() {
        return (SuspendResumeIndicators) super.f_Parameters.get(_INDEX_F_SuspendResumeIndicators);
    }

    @Override
    public void setCallReference(CallReference cr) {
        super.o_Parameters.put(_INDEX_O_CallReference, cr);
    }

    @Override
    public CallReference getCallReference() {
        return (CallReference) super.o_Parameters.get(_INDEX_O_CallReference);
    }
}
