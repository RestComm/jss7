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
 * Start time:00:00:14 2009-09-07<br>
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
import org.mobicents.protocols.ss7.isup.message.InformationRequestMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.InformationRequestIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageType;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;

/**
 * Start time:00:00:14 2009-09-07<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class InformationRequestMessageImpl extends ISUPMessageImpl implements InformationRequestMessage {
    public static final MessageTypeImpl _MESSAGE_TYPE = new MessageTypeImpl(MESSAGE_CODE);
    static final int _INDEX_F_MessageType = 0;
    static final int _INDEX_F_InformationRequestIndicators = 1;
    static final int _INDEX_O_CallReference = 0;
    static final int _INDEX_O_ParameterCompatibilityInformation = 1;
    static final int _INDEX_O_NetworkSpecificFacility = 2;
    static final int _INDEX_O_EndOfOptionalParameters = 3;

    /**
     *
     * @param source
     * @throws ParameterException
     */
    public InformationRequestMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes,
            Set<Integer> optionalCodes, Map<Integer, Integer> mandatoryCode2Index,
            Map<Integer, Integer> mandatoryVariableCode2Index, Map<Integer, Integer> optionalCode2Index) {
        super(mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index,
                optionalCode2Index);

        super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
        super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, _END_OF_OPTIONAL_PARAMETERS);
    }

    @Override
    public void setInformationRequestIndicators(InformationRequestIndicators inri) {
        super.f_Parameters.put(_INDEX_F_InformationRequestIndicators, inri);

    }

    @Override
    public InformationRequestIndicators getInformationRequestIndicators() {
        return (InformationRequestIndicators) super.f_Parameters.get(_INDEX_F_InformationRequestIndicators);
    }

    @Override
    public void setCallReference(CallReference inri) {
        super.o_Parameters.put(_INDEX_O_CallReference, inri);

    }

    @Override
    public CallReference getCallReference() {
        return (CallReference) super.o_Parameters.get(_INDEX_O_CallReference);
    }

    @Override
    public void setParameterCompatibilityInformation(ParameterCompatibilityInformation inri) {
        super.o_Parameters.put(_INDEX_O_ParameterCompatibilityInformation, inri);

    }

    @Override
    public ParameterCompatibilityInformation getParameterCompatibilityInformation() {
        return (ParameterCompatibilityInformation) super.o_Parameters.get(_INDEX_O_ParameterCompatibilityInformation);
    }

    @Override
    public void setNetworkSpecificFacility(NetworkSpecificFacility inri) {
        super.o_Parameters.put(_INDEX_O_NetworkSpecificFacility, inri);

    }

    @Override
    public NetworkSpecificFacility getNetworkSpecificFacility() {
        return (NetworkSpecificFacility) super.o_Parameters.get(_INDEX_O_NetworkSpecificFacility);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#decodeMandatoryParameters (byte[], int)
     */
    protected int decodeMandatoryParameters(ISUPParameterFactory parameterFactory, byte[] b, int index)
            throws ParameterException {
        int localIndex = index;
        index += super.decodeMandatoryParameters(parameterFactory, b, index);
        if (b.length - index > 1) {

            try {
                byte[] informationInd = new byte[2];
                informationInd[0] = b[index++];
                informationInd[1] = b[index++];
                InformationRequestIndicators bci = parameterFactory.createInformationRequestIndicators();
                ((AbstractISUPParameter) bci).decode(informationInd);
                this.setInformationRequestIndicators(bci);
            } catch (Exception e) {
                // AIOOBE or IllegalArg
                throw new ParameterException("Failed to parse BackwardCallIndicators due to: ", e);
            }

            // return 3;
            return index - localIndex;
        } else {
            throw new IllegalArgumentException("byte[] must have atleast 2 octets");
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#decodeMandatoryVariableBody (byte[], int)
     */

    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, int parameterIndex)
            throws ParameterException {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#decodeOptionalBody(byte [], byte)
     */
    protected void decodeOptionalBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, byte parameterCode)
            throws ParameterException {

        switch (parameterCode & 0xFF) {

            case CallReference._PARAMETER_CODE:
                CallReference RS = parameterFactory.createCallReference();
                ((AbstractISUPParameter) RS).decode(parameterBody);
                this.setCallReference(RS);
                break;
            case ParameterCompatibilityInformation._PARAMETER_CODE:
                ParameterCompatibilityInformation pri = parameterFactory.createParameterCompatibilityInformation();
                ((AbstractISUPParameter) pri).decode(parameterBody);
                this.setParameterCompatibilityInformation(pri);
                break;
            case NetworkSpecificFacility._PARAMETER_CODE:
                NetworkSpecificFacility nsf = parameterFactory.createNetworkSpecificFacility();
                ((AbstractISUPParameter) nsf).decode(parameterBody);
                this.setNetworkSpecificFacility(nsf);
                break;
            default:
                throw new ParameterException("Unrecognized parameter code for optional part: " + parameterCode);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#getMessageType()
     */

    public MessageType getMessageType() {
        return this._MESSAGE_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl# getNumberOfMandatoryVariableLengthParameters()
     */

    protected int getNumberOfMandatoryVariableLengthParameters() {
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#hasAllMandatoryParameters ()
     */

    public boolean hasAllMandatoryParameters() {
        return getInformationRequestIndicators() != null;
    }

    protected boolean optionalPartIsPossible() {

        return true;
    }

}
