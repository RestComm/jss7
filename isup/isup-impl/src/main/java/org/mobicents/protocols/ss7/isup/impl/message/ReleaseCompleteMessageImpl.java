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
 * Start time:08:20:34 2009-07-18<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import java.util.Map;
import java.util.Set;

import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.mobicents.protocols.ss7.isup.message.ReleaseCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.util.ISUPUtility;

/**
 * Start time:08:20:34 2009-07-18<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 *
 */
class ReleaseCompleteMessageImpl extends ISUPMessageImpl implements ReleaseCompleteMessage {

    public static final MessageTypeImpl _MESSAGE_TYPE = new MessageTypeImpl(MESSAGE_CODE);
    private static final int _MANDATORY_VAR_COUNT = 0;

    // mandatory fixed L
    static final int _INDEX_F_MessageType = 0;
    // mandatory variable L
    // optional O
    static final int _INDEX_O_CauseIndicators = 0;
    static final int _INDEX_O_EndOfOptionalParameters = 1;

    ReleaseCompleteMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes,
            Map<Integer, Integer> mandatoryCode2Index, Map<Integer, Integer> mandatoryVariableCode2Index,
            Map<Integer, Integer> optionalCode2Index) {
        super(mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index,
                optionalCode2Index);

        super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
        super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, _END_OF_OPTIONAL_PARAMETERS);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.ISUPMessage#decodeMandatoryVariableBody(byte [], int)
     */

    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, int parameterIndex)
            throws ParameterException {
        throw new ParameterException("This message has no mandatory parameters, unknown parameter index: " + parameterIndex
                + ", body: " + ISUPUtility.toHex(parameterBody));

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.ISUPMessage#decodeOptionalBody(byte[], byte)
     */

    protected void decodeOptionalBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, byte parameterCode)
            throws ParameterException {

        switch (parameterCode & 0xFF) {
            case CauseIndicators._PARAMETER_CODE:
                CauseIndicators cpn = parameterFactory.createCauseIndicators();
                ((AbstractISUPParameter) cpn).decode(parameterBody);
                this.setCauseIndicators(cpn);
                break;
            default:
                throw new ParameterException("Unrecognized parameter index for optional part: " + parameterCode);
        }

    }

    public CauseIndicators getCauseIndicators() {
        return (CauseIndicators) super.o_Parameters.get(_INDEX_O_CauseIndicators);
    }

    public void setCauseIndicators(CauseIndicators v) {
        super.o_Parameters.put(_INDEX_O_CauseIndicators, v);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.ISUPMessage#getMessageType()
     */

    public MessageTypeImpl getMessageType() {
        return this._MESSAGE_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.isup.messages.ISUPMessage# getNumberOfMandatoryVariableLengthParameters()
     */

    protected int getNumberOfMandatoryVariableLengthParameters() {

        return _MANDATORY_VAR_COUNT;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.ISUPMessage#hasAllMandatoryParameters()
     */

    public boolean hasAllMandatoryParameters() {
        if (this.f_Parameters.get(_INDEX_F_MessageType) == null
                || this.f_Parameters.get(_INDEX_F_MessageType).getCode() != this.getMessageType().getCode()) {
            return false;
        }

        return true;
    }

    protected boolean optionalPartIsPossible() {

        return true;
    }
}
