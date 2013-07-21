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

package org.mobicents.protocols.ss7.isup.impl.message;

import java.util.Map;
import java.util.Set;

import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.mobicents.protocols.ss7.isup.message.AbstractFacilityMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectionRequest;
import org.mobicents.protocols.ss7.isup.message.parameter.FacilityIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserIndicators;

/**
 * Base class for FAA and FAR - both have the same content.
 *
 * @author baranowb
 *
 */
public abstract class AbstractFacilityMessageImpl extends ISUPMessageImpl implements AbstractFacilityMessage {

    private static final int _MANDATORY_VAR_COUNT = 0;
    private static final boolean _OPTIONAL_POSSIBLE = true;

    static final int _INDEX_F_MessageType = 0;
    static final int _INDEX_F_FacilityIndicator = 1;

    static final int _INDEX_O_UserToUserIndicators = 0;
    static final int _INDEX_O_CallReference = 1;
    static final int _INDEX_O_ConnectionRequest = 2;
    static final int _INDEX_O_ParameterCompatibilityInformation = 3;
    static final int _INDEX_O_EndOfOptionalParameters = 4;

    AbstractFacilityMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes,
            Map<Integer, Integer> mandatoryCode2Index, Map<Integer, Integer> mandatoryVariableCode2Index,
            Map<Integer, Integer> optionalCode2Index) {
        super(mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index,
                optionalCode2Index);

        super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
        super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, _END_OF_OPTIONAL_PARAMETERS);
    }

    @Override
    public boolean hasAllMandatoryParameters() {
        if (super.f_Parameters.get(_INDEX_F_MessageType) == null) {
            return false;
        }

        if (super.f_Parameters.get(_INDEX_F_FacilityIndicator) == null) {
            return false;
        }

        return true;
    }

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

            case CallReference._PARAMETER_CODE:
                CallReference cr = parameterFactory.createCallReference();
                ((AbstractISUPParameter) cr).decode(parameterBody);
                this.setCallReference(cr);
                break;

            case ConnectionRequest._PARAMETER_CODE:
                ConnectionRequest conr = parameterFactory.createConnectionRequest();
                ((AbstractISUPParameter) conr).decode(parameterBody);
                this.setConnectionRequest(conr);
                break;

            case ParameterCompatibilityInformation._PARAMETER_CODE:
                ParameterCompatibilityInformation pci = parameterFactory.createParameterCompatibilityInformation();
                ((AbstractISUPParameter) pci).decode(parameterBody);
                this.setParameterCompatibilityInformation(pci);
                break;

            default:
                throw new ParameterException("Unrecognized parameter code for optional part: " + parameterCode);
        }

    }

    @Override
    protected int getNumberOfMandatoryVariableLengthParameters() {
        return _MANDATORY_VAR_COUNT;
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
    public void setUserToUserIndicators(UserToUserIndicators u2ui) {
        super.o_Parameters.put(_INDEX_O_UserToUserIndicators, u2ui);
    }

    @Override
    public UserToUserIndicators getUserToUserIndicators() {
        return (UserToUserIndicators) super.o_Parameters.get(_INDEX_O_UserToUserIndicators);
    }

    @Override
    public void setCallReference(CallReference cf) {
        super.o_Parameters.put(_INDEX_O_CallReference, cf);
    }

    @Override
    public CallReference getCallReference() {
        return (CallReference) super.o_Parameters.get(_INDEX_O_CallReference);
    }

    @Override
    public void setConnectionRequest(ConnectionRequest cr) {
        super.o_Parameters.put(_INDEX_O_ConnectionRequest, cr);
    }

    @Override
    public ConnectionRequest getConnectionRequest() {
        return (ConnectionRequest) super.o_Parameters.get(_INDEX_O_ConnectionRequest);
    }

    @Override
    public void setParameterCompatibilityInformation(ParameterCompatibilityInformation pci) {
        super.o_Parameters.put(_INDEX_O_ParameterCompatibilityInformation, pci);
    }

    @Override
    public ParameterCompatibilityInformation getCompatibilityInformation() {
        return (ParameterCompatibilityInformation) super.o_Parameters.get(_INDEX_O_ParameterCompatibilityInformation);
    }

}
