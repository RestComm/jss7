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
 * Start time:23:59:42 2009-09-06<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import java.util.Map;
import java.util.Set;

import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.mobicents.protocols.ss7.isup.message.FacilityRejectedMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.FacilityIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageType;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserIndicators;

/**
 * Start time:23:59:42 2009-09-06<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class FacilityRejectedMessageImpl extends ISUPMessageImpl implements FacilityRejectedMessage {
    public static final MessageTypeImpl _MESSAGE_TYPE = new MessageTypeImpl(MESSAGE_CODE);
    private static final int _MANDATORY_VAR_COUNT = 1;
    private static final boolean _OPTIONAL_POSSIBLE = true;

    static final int _INDEX_F_MessageType = 0;
    static final int _INDEX_F_FacilityIndicator = 1;

    static final int _INDEX_V_CauseIndicators = 0;

    static final int _INDEX_O_UserToUserIndicators = 0;
    static final int _INDEX_O_EndOfOptionalParameters = 4;

    /**
     *
     * @param source
     * @throws ParameterException
     */
    public FacilityRejectedMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes,
            Map<Integer, Integer> mandatoryCode2Index, Map<Integer, Integer> mandatoryVariableCode2Index,
            Map<Integer, Integer> optionalCode2Index) {
        super(mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index,
                optionalCode2Index);

        super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
        super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, _END_OF_OPTIONAL_PARAMETERS);
    }

    @Override
    protected int decodeMandatoryParameters(ISUPParameterFactory parameterFactory, byte[] b, int index)
            throws ParameterException {
        int localIndex = index;
        index += super.decodeMandatoryParameters(parameterFactory, b, index);
        if (b.length - index > 1) {

            // this.circuitIdentificationCode = b[index++];
            try {
                byte[] facilityIndicator = new byte[1];
                facilityIndicator[0] = b[index++];

                FacilityIndicator bci = parameterFactory.createFacilityIndicator();
                ((AbstractISUPParameter) bci).decode(facilityIndicator);
                this.setFacilityIndicator(bci);
            } catch (Exception e) {
                // AIOOBE or IllegalArg
                throw new ParameterException("Failed to parse BackwardCallIndicators due to: ", e);
            }

            // return 3;
            return index - localIndex;
        } else {
            throw new IllegalArgumentException("byte[] must have atleast four octets");
        }
    }

    @Override
    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, int parameterIndex)
            throws ParameterException {
        switch (parameterIndex) {
            case _INDEX_V_CauseIndicators:
                CauseIndicators ras = parameterFactory.createCauseIndicators();
                ((AbstractISUPParameter) ras).decode(parameterBody);
                this.setCauseIndicators(ras);
                break;
            default:
                throw new ParameterException("Unrecognized parameter index for mandatory variable part, index: "
                        + parameterIndex);

        }

    }

    @Override
    protected void decodeOptionalBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, byte parameterCode)
            throws ParameterException {
        switch (parameterCode & 0xFF) {
            case UserToUserIndicators._PARAMETER_CODE:
                UserToUserIndicators u2ui = parameterFactory.createUserToUserIndicators();
                ((AbstractISUPParameter) u2ui).decode(parameterBody);
                this.setUserToUserIndicators(u2ui);
                break;
            default:
                throw new ParameterException("Unrecognized parameter code for optional part: " + parameterCode);
        }

    }

    @Override
    public MessageType getMessageType() {
        return _MESSAGE_TYPE;
    }

    @Override
    protected int getNumberOfMandatoryVariableLengthParameters() {
        return _MANDATORY_VAR_COUNT;
    }

    @Override
    public boolean hasAllMandatoryParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected boolean optionalPartIsPossible() {
        return _OPTIONAL_POSSIBLE;
    }

    @Override
    public void setFacilityIndicator(FacilityIndicator fi) {
        super.f_Parameters.put(_INDEX_F_FacilityIndicator, fi);
    }

    @Override
    public FacilityIndicator getFacilityIndicator() {
        return (FacilityIndicator) super.f_Parameters.get(_INDEX_F_FacilityIndicator);
    }

    @Override
    public void setCauseIndicators(CauseIndicators ci) {
        super.v_Parameters.put(_INDEX_V_CauseIndicators, ci);
    }

    @Override
    public CauseIndicators getCauseIndicators() {
        return (CauseIndicators) super.v_Parameters.get(_INDEX_V_CauseIndicators);
    }

    @Override
    public void setUserToUserIndicators(UserToUserIndicators u2ui) {
        super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, u2ui);
    }

    @Override
    public UserToUserIndicators getUserToUserIndicators() {
        return (UserToUserIndicators) super.o_Parameters.get(_INDEX_O_EndOfOptionalParameters);
    }

}
