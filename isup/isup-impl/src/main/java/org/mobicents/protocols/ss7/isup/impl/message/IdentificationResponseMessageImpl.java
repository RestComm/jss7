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
 * Start time:00:11:43 2009-09-07<br>
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
import org.mobicents.protocols.ss7.isup.message.IdentificationResponseMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.ChargedPartyIdentification;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.MCIDResponseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageType;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Start time:00:11:43 2009-09-07<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class IdentificationResponseMessageImpl extends ISUPMessageImpl implements IdentificationResponseMessage {
    public static final MessageTypeImpl _MESSAGE_TYPE = new MessageTypeImpl(MESSAGE_CODE);
    private static final int _MANDATORY_VAR_COUNT = 0;
    private static final boolean _HAS_MANDATORY = true;
    private static final boolean _OPTIONAL_POSSIBLE = true;

    static final int _INDEX_F_MessageType = 0;

    static final int _INDEX_O_MCIDResponseIndicators = 0;
    static final int _INDEX_O_MessageCompatibilityInformation = 1;
    static final int _INDEX_O_ParameterCompatibilityInformation = 2;
    static final int _INDEX_O_CallingPartyNumber = 3;
    static final int _INDEX_O_AccessTransport = 4;
    static final int _INDEX_O_GenericNumber = 5;
    static final int _INDEX_O_ChargedPartyIdentification = 6;
    static final int _INDEX_O_EndOfOptionalParameters = 7;
    /**
     *
     * @param source
     * @throws ParameterException
     */
    public IdentificationResponseMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes,
            Map<Integer, Integer> mandatoryCode2Index, Map<Integer, Integer> mandatoryVariableCode2Index,
            Map<Integer, Integer> optionalCode2Index) {
        super(mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index,
                optionalCode2Index);
        super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
        super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, _END_OF_OPTIONAL_PARAMETERS);
    }

    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, int parameterIndex)
            throws ParameterException {
    }

    protected void decodeOptionalBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, byte parameterCode)
            throws ParameterException {
        switch (parameterCode & 0xFF) {
            case MessageCompatibilityInformation._PARAMETER_CODE:
                MessageCompatibilityInformation mci = parameterFactory.createMessageCompatibilityInformation();
                ((AbstractISUPParameter) mci).decode(parameterBody);
                this.setMessageCompatibilityInformation(mci);
                break;
            case ParameterCompatibilityInformation._PARAMETER_CODE:
                ParameterCompatibilityInformation pci = parameterFactory.createParameterCompatibilityInformation();
                ((AbstractISUPParameter) pci).decode(parameterBody);
                this.setParameterCompatibilityInformation(pci);
                break;
            case MCIDResponseIndicators._PARAMETER_CODE:
                MCIDResponseIndicators ro = parameterFactory.createMCIDResponseIndicators();
                ((AbstractISUPParameter) ro).decode(parameterBody);
                this.setMCIDResponseIndicators(ro);
                break;
            case CallingPartyNumber._PARAMETER_CODE:
                CallingPartyNumber cpn = parameterFactory.createCallingPartyNumber();
               ((AbstractISUPParameter) cpn).decode(parameterBody);
               this.setCallingPartyNumber(cpn);
               break;
            case AccessTransport._PARAMETER_CODE:
                AccessTransport at = parameterFactory.createAccessTransport();
               ((AbstractISUPParameter) at).decode(parameterBody);
               this.setAccessTransport(at);
               break;
            case GenericNumber._PARAMETER_CODE:
                GenericNumber gn = parameterFactory.createGenericNumber();
               ((AbstractISUPParameter) gn).decode(parameterBody);
               this.setGenericNumber(gn);
               break;
            case ChargedPartyIdentification._PARAMETER_CODE:
                ChargedPartyIdentification cpi = parameterFactory.createChargedPartyIdentification();
               ((AbstractISUPParameter) cpi).decode(parameterBody);
               this.setChargedPartyIdentification(cpi);
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
        return _HAS_MANDATORY;
    }

    protected boolean optionalPartIsPossible() {
        return _OPTIONAL_POSSIBLE;
    }

    @Override
    public void setMCIDResponseIndicators(MCIDResponseIndicators mcid) {
        super.o_Parameters.put(_INDEX_O_MCIDResponseIndicators, mcid);
    }

    @Override
    public MCIDResponseIndicators getMCIDResponseIndicators() {
        return (MCIDResponseIndicators) super.o_Parameters.get(_INDEX_O_MCIDResponseIndicators);
    }

    @Override
    public void setMessageCompatibilityInformation(MessageCompatibilityInformation mci) {
        super.o_Parameters.put(_INDEX_O_MessageCompatibilityInformation, mci);
    }

    @Override
    public MessageCompatibilityInformation getMessageCompatibilityInformation() {
        return (MessageCompatibilityInformation) super.o_Parameters.get(_INDEX_O_MessageCompatibilityInformation);
    }

    @Override
    public void setParameterCompatibilityInformation(ParameterCompatibilityInformation pci) {
        super.o_Parameters.put(_INDEX_O_ParameterCompatibilityInformation, pci);
    }

    @Override
    public ParameterCompatibilityInformation getParameterCompatibilityInformation() {
        return (ParameterCompatibilityInformation) super.o_Parameters.get(_INDEX_O_ParameterCompatibilityInformation);
    }

    @Override
    public void setCallingPartyNumber(CallingPartyNumber cpn) {
        super.o_Parameters.put(_INDEX_O_CallingPartyNumber, cpn);
    }

    @Override
    public CallingPartyNumber getCallingPartyNumber() {
        return (CallingPartyNumber) super.o_Parameters.get(_INDEX_O_CallingPartyNumber);
    }

    @Override
    public void setAccessTransport(AccessTransport at) {
        super.o_Parameters.put(_INDEX_O_AccessTransport, at);
    }

    @Override
    public AccessTransport getAccessTransport() {
        return (AccessTransport) super.o_Parameters.get(_INDEX_O_AccessTransport);
    }

    @Override
    public void setGenericNumber(GenericNumber gn) {
        super.o_Parameters.put(_INDEX_O_GenericNumber, gn);
    }

    @Override
    public GenericNumber getGenericNumber() {
        return (GenericNumber) super.o_Parameters.get(_INDEX_O_GenericNumber);
    }

    @Override
    public void setChargedPartyIdentification(ChargedPartyIdentification cpi) {
        super.o_Parameters.put(_INDEX_O_ChargedPartyIdentification, cpi);
    }

    @Override
    public ChargedPartyIdentification getChargedPartyIdentification() {
        return (ChargedPartyIdentification) super.o_Parameters.get(_INDEX_O_ChargedPartyIdentification);
    }

}
