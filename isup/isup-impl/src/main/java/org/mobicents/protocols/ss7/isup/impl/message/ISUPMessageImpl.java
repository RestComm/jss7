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

package org.mobicents.protocols.ss7.isup.impl.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CircuitIdentificationCodeImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.EndOfOptionalParametersImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.ISUPParameter;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageType;

/**
 * Start time:14:09:04 2009-04-20<br>
 * Project: mobicents-isup-stack<br>
 * This is super message class for all messages that we have. It defines some methods that need to be implemented
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public abstract class ISUPMessageImpl extends AbstractISUPMessage {

    /**
     * To use one when encoding, created, possibly when decoding
     */
    protected static final EndOfOptionalParametersImpl _END_OF_OPTIONAL_PARAMETERS = new EndOfOptionalParametersImpl();

    // protected static final Logger logger = Logger.getLogger(ISUPMessageImpl.class);

    // TODO: change everything below into [], for such small size of arrays, its faster to even search through them.
    /**
     * F = mandatory fixed length parameter;<br>
     * for type F parameters: the length, in octets, of the parameter content;
     */
    protected Map<Integer, ISUPParameter> f_Parameters;
    /**
     * V = mandatory variable length parameter;<br>
     * for type V parameters: the length, in octets, of the length indicator and of the parameter content. The minimum and the
     * maximum length are indicated;
     */
    protected Map<Integer, ISUPParameter> v_Parameters;
    /**
     * O = optional parameter of fixed or variable length; for type O parameters: the length, in octets, of the parameter name,
     * length indicator and parameter content. For variable length parameters the minimum and maximum length is indicated.
     */
    protected Map<Integer, ISUPParameter> o_Parameters;

    // magic
    protected Set<Integer> mandatoryCodes;
    protected Set<Integer> mandatoryVariableCodes;
    protected Set<Integer> optionalCodes;

    protected Map<Integer, Integer> mandatoryCodeToIndex;
    protected Map<Integer, Integer> mandatoryVariableCodeToIndex;
    protected Map<Integer, Integer> optionalCodeToIndex;

    protected CircuitIdentificationCode cic;
    protected int sls;

    public ISUPMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes,
            Map<Integer, Integer> mandatoryCode2Index, Map<Integer, Integer> mandatoryVariableCode2Index,
            Map<Integer, Integer> optionalCode2Index) {
        super();

        this.f_Parameters = new TreeMap<Integer, ISUPParameter>();
        this.v_Parameters = new TreeMap<Integer, ISUPParameter>();
        this.o_Parameters = new TreeMap<Integer, ISUPParameter>();

        this.mandatoryCodes = mandatoryCodes;
        this.mandatoryVariableCodes = mandatoryVariableCodes;
        this.optionalCodes = optionalCodes;

        this.mandatoryCodeToIndex = mandatoryCode2Index;
        this.mandatoryVariableCodeToIndex = mandatoryVariableCode2Index;
        this.optionalCodeToIndex = optionalCode2Index;

    }

    /**
     *
     */
    public ISUPMessageImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setSls(int sls) {
        // if(sls>=16 || sls<0)
        // {
        // throw new IllegalArgumentException("SLS must be in range of one byte, it is: "+sls+"!");
        // }
        this.sls = (sls & 0x0F);
    }

    @Override
    public int getSls() {
        return this.sls;
    }

    /**
     * @return <ul>
     *         <li><b>true</b> - if all requried parameters are set</li>
     *         <li><b>false</b> - otherwise</li>
     *         </ul>
     */
    public abstract boolean hasAllMandatoryParameters();

    /**
     * Returns message code. See Q.763 Table 4. It simply return value of static constant - _MESSAGE_TYPE, where value of
     * parameter is value _MESSAGE_CODE
     *
     * @return
     */
    public abstract MessageType getMessageType();

    // ////////////////
    // CODE SECTION //
    // ////////////////
    public byte[] encode() throws ParameterException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // akward :)
        this.encode(bos);
        return bos.toByteArray();
    }

    public int encode(ByteArrayOutputStream bos) throws ParameterException {

        // bos.write(this.circuitIdentificationCode);

        final boolean optionalPresent = this.o_Parameters.size() > 1;
        this.encodeMandatoryParameters(f_Parameters, bos);
        this.encodeMandatoryVariableParameters(v_Parameters, bos, optionalPresent);
        if (optionalPresent) {
            this.encodeOptionalParameters(o_Parameters, bos);
        }

        return bos.size();
    }

    // NOTE: those methods are more or less generic.
    protected void encodeMandatoryParameters(Map<Integer, ISUPParameter> parameters, ByteArrayOutputStream bos)
            throws ParameterException {
        // 1.5 Mandatory fixed part
        // Those parameters that are mandatory and of fixed length for a
        // particular message type will be
        // contained in the mandatory fixed part. The position, length and order
        // of the parameters is uniquely
        // defined by the message type; thus, the names of the parameters and
        // the length indicators are not
        // included in the message.
        if (this.cic == null) {
            // this will be changed to different exception
            throw new ParameterException("CIC is not set!");
        }
        ((AbstractISUPParameter) this.cic).encode(bos);
        for (ISUPParameter p : parameters.values()) {
            ((AbstractISUPParameter) p).encode(bos);
        }
    }

    /**
     * takes care of endoding parameters - poniters and actual parameters.
     *
     * @param parameters - list of parameters
     * @param bos - output
     * @param isOptionalPartPresent - if <b>true</b> this will encode pointer to point for start of optional part, otherwise it
     *        will encode this octet as zeros
     * @throws ParameterException
     */
    protected void encodeMandatoryVariableParameters(Map<Integer, ISUPParameter> parameters, ByteArrayOutputStream bos,
            boolean isOptionalPartPresent) throws ParameterException {
        try {
            byte[] pointers = null;
            // complicated
            if (!mandatoryVariablePartPossible()) {
                // we ommit pointer to this part, go straight for optional pointer.
                if (optionalPartIsPossible()) {
                    if (isOptionalPartPresent) {
                        pointers = new byte[] { 0x01 };
                    } else {
                        // zeros
                        pointers = new byte[] { 0x00 };
                    }
                    bos.write(pointers);
                } else {
                    // do nothing?
                }

            } else {
                if (optionalPartIsPossible()) {
                    pointers = new byte[parameters.size() + 1];
                } else {
                    pointers = new byte[parameters.size()];
                }
                ByteArrayOutputStream parametersBodyBOS = new ByteArrayOutputStream();
                byte lastParameterLength = 0;
                byte currentParameterLength = 0;
                for (int index = 0; index < parameters.size(); index++) {
                    AbstractISUPParameter p = (AbstractISUPParameter) parameters.get(index);

                    byte[] body = p.encode();
                    currentParameterLength = (byte) body.length;
                    if (body.length > 255) {
                        // FIXME: is this check valid?
                        throw new ParameterException("Length of body must not be greater than one octet - 255 ");
                    }
                    if (index == 0) {
                        lastParameterLength = currentParameterLength;

                        // This creates pointer to first mandatory variable param,
                        // check on optional is required, since if its not defined
                        // by message, pointer is omited.
                        pointers[index] = (byte) (parameters.size() + (optionalPartIsPossible() ? 1 : 0));
                    } else {

                        pointers[index] = (byte) (pointers[index - 1] + lastParameterLength);
                        lastParameterLength = currentParameterLength;
                    }

                    parametersBodyBOS.write(currentParameterLength);
                    parametersBodyBOS.write(body);
                }

                // we ommit pointer to this part, go straight for optional pointer.
                if (optionalPartIsPossible()) {
                    if (isOptionalPartPresent) {
                        pointers[pointers.length - 1] = (byte) (pointers[pointers.length - 2] + lastParameterLength);
                    } else {
                        // zeros
                        // pointers=new byte[]{0x00};
                    }
                } else {
                    // do nothing?
                }

                bos.write(pointers);
                bos.write(parametersBodyBOS.toByteArray());
            }
        } catch (ParameterException pe) {
            throw pe;
        } catch (Exception e) {
            throw new ParameterException(e);
        }
    }

    /**
     * This method must be called ONLY in case there are optional params. This implies ISUPMessage.o_Parameters.size()>1 !!!
     *
     * @param parameters
     * @param bos
     * @throws ParameterException
     */
    protected void encodeOptionalParameters(Map<Integer, ISUPParameter> parameters, ByteArrayOutputStream bos)
            throws ParameterException {

        // NOTE: parameters MUST have as last endOfOptionalParametersParameter+1
        // param
        for (ISUPParameter p : parameters.values()) {

            if (p == null)
                continue;

            byte[] b = ((AbstractISUPParameter) p).encode();
            // System.err.println("ENCODE O: "+p.getCode()+"---> "+Utils.toHex(b));
            // FIXME: this can be slow, maybe we shoudl remove that, and code
            // this explicitly?
            if (b.length > 255) {
                throw new ParameterException("Parameter length is over 255: " + p);
            }
            if (!(p instanceof EndOfOptionalParametersImpl)) {
                bos.write(p.getCode());

                bos.write(b.length);
            }
            try {
                bos.write(b);
            } catch (IOException e) {
                throw new ParameterException("Failed to encode optional parameters.", e);
            }
        }

    }

    public int decode(byte[] b, ISUPMessageFactory messageFactory,ISUPParameterFactory parameterFactory) throws ParameterException {
        int index = 0;
        index += this.decodeMandatoryParameters(parameterFactory, b, index);

        if (mandatoryVariablePartPossible())
            index += this.decodeMandatoryVariableParameters(parameterFactory, b, index);

        if (!this.optionalPartIsPossible() || b.length == index || b[index] == 0x0) {
            return index;
        }

        // moving pointer to possible location
        // index++;

        // +1 for pointer location :)
        index += b[index];

        index += this.decodeOptionalParameters(parameterFactory, b, index);
        return index;
    }

    // Unfortunelty this cant be generic, can it?
    protected int decodeMandatoryParameters(ISUPParameterFactory parameterFactory, byte[] b, int index)
            throws ParameterException {
        int localIndex = index;
        if (b.length - index >= 3) {
            try {
                byte[] cic = new byte[2];
                cic[0] = b[index++];
                cic[1] = b[index++];
                this.cic = new CircuitIdentificationCodeImpl();
                ((AbstractISUPParameter) this.cic).decode(cic);

            } catch (Exception e) {
                // AIOOBE or IllegalArg
                throw new ParameterException("Failed to parse CircuitIdentificationCode due to: ", e);
            }
            try {
                // Message Type
                if (b[index] != this.getMessageType().getCode()) {
                    throw new ParameterException("Message code is not: " + this.getMessageType().getCode());
                }
            } catch (Exception e) {
                // AIOOBE or IllegalArg
                throw new ParameterException("Failed to parse MessageCode due to: ", e);
            }
            index++;

            // return 3;
            return index - localIndex;
        } else {
            throw new IllegalArgumentException("byte[] must have atleast three octets");
        }
    }

    /**
     * decodes ptrs and returns offset from passed index value to first optional parameter parameter
     *
     * @param b
     * @param index
     * @return
     * @throws ParameterException
     */
    protected int decodeMandatoryVariableParameters(ISUPParameterFactory parameterFactory, byte[] b, int index)
            throws ParameterException {
        // FIXME: possibly this should also be per msg, since if msg lacks
        // proper parameter, decoding wotn pick this up and will throw
        // some bad output, which wont give a clue about reason...
        int readCount = 0;
        // int optionalOffset = 0;

        if (b.length - index > 0) {

            byte extPIndex = -1;
            try {
                int count = getNumberOfMandatoryVariableLengthParameters();
                readCount = count;
                for (int parameterIndex = 0; parameterIndex < count; parameterIndex++) {
                    int lengthPointerIndex = index + parameterIndex;
                    int parameterLengthIndex = b[lengthPointerIndex] + lengthPointerIndex;

                    int parameterLength = b[parameterLengthIndex];
                    byte[] parameterBody = new byte[parameterLength];
                    System.arraycopy(b, parameterLengthIndex + 1, parameterBody, 0, parameterLength);
                    decodeMandatoryVariableBody(parameterFactory, parameterBody, parameterIndex);

                }

                // optionalOffset = b[index + readCount];
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                throw new ParameterException(
                        "Failed to read parameter, to few octets in buffer, parameter index: " + extPIndex, aioobe);
            } catch (IllegalArgumentException e) {
                throw new ParameterException("Failed to parse, paramet index: " + extPIndex, e);
            }
        } else {
            throw new ParameterException(
                    "To few bytes to decode mandatory variable part. There should be atleast on byte to indicate optional part.");
        }

        // return readCount + optionalOffset;
        return readCount;
    }

    protected int decodeOptionalParameters(ISUPParameterFactory parameterFactory, byte[] b, int index)
            throws ParameterException {

        int localIndex = index;

        int readCount = 0;
        // if not, there are no params.
        if (b.length - index > 0) {
            // let it rip :)
            boolean readParameter = true;
            while (readParameter) {
                if (b.length - localIndex > 0 && b[localIndex] != 0) {
                    readParameter = true;
                } else {
                    readParameter = false;
                    continue;
                }
                byte extPCode = -1;
                byte assumedParameterLength = -1;
                try {

                    byte parameterCode = b[localIndex++];
                    extPCode = parameterCode;
                    byte parameterLength = b[localIndex++];
                    assumedParameterLength = parameterLength;
                    byte[] parameterBody = new byte[parameterLength];
                    // This is bad, we will change this

                    System.arraycopy(b, localIndex, parameterBody, 0, parameterLength);
                    localIndex += parameterLength;
                    readCount += 2 + parameterLength;

                    decodeOptionalBody(parameterFactory, parameterBody, parameterCode);

                    if (b.length - localIndex > 0 && b[localIndex] != 0) {
                        readParameter = true;
                    } else {
                        readParameter = false;
                    }

                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    throw new ParameterException("Failed to read parameter, to few octets in buffer, parameter code: "
                            + extPCode + ", assumed length: " + assumedParameterLength, aioobe);
                } catch (IllegalArgumentException e) {
                    throw new ParameterException("Failed to parse parameter: " + extPCode, e);
                }
            }
        }

        return readCount;
    }

    // TODO: add general method to handle decode and "addParam" so we can remove "copy/paste" code to create param and set it in
    // msg.
    /**
     * @param parameterBody
     * @param parameterIndex
     */
    protected abstract void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, byte[] parameterBody,
            int parameterIndex) throws ParameterException;

    protected abstract void decodeOptionalBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, byte parameterCode)
            throws ParameterException;

    protected abstract int getNumberOfMandatoryVariableLengthParameters();

    protected abstract boolean optionalPartIsPossible();

    protected boolean mandatoryVariablePartPossible() {

        return getNumberOfMandatoryVariableLengthParameters() != 0;
    }

    // ////////////////////////
    // PARAM HANDLE SECTION //
    // ////////////////////////
    public void addParameter(ISUPParameter param) throws ParameterException {
        if (param == null) {
            throw new IllegalArgumentException("Argument must not be null");
        }
        int paramCode = param.getCode();
        if (this.mandatoryCodes.contains(paramCode)) {
            int index = this.mandatoryCodeToIndex.get(paramCode);
            this.f_Parameters.put(index, (AbstractISUPParameter) param);
            return;
        }

        if (this.mandatoryVariableCodes.contains(paramCode)) {
            int index = this.mandatoryVariableCodeToIndex.get(paramCode);
            this.v_Parameters.put(index, (AbstractISUPParameter) param);
            return;
        }
        if (this.optionalCodes.contains(paramCode)) {
            int index = this.optionalCodeToIndex.get(paramCode);
            this.o_Parameters.put(index, (AbstractISUPParameter) param);
            return;
        }

        throw new ParameterException("Parameter with code: " + paramCode
                + " is not defined in any type: mandatory, mandatory variable or optional");
    }

    public ISUPParameter getParameter(int parameterCode) throws ParameterException {

        if (this.mandatoryCodes.contains(parameterCode)) {
            int index = this.mandatoryCodeToIndex.get(parameterCode);
            return this.f_Parameters.get(index);
        }

        if (this.mandatoryVariableCodes.contains(parameterCode)) {
            int index = this.mandatoryVariableCodeToIndex.get(parameterCode);
            return this.v_Parameters.get(index);
        }
        if (this.optionalCodes.contains(parameterCode)) {
            int index = this.optionalCodeToIndex.get(parameterCode);
            return this.o_Parameters.get(index);
        }

        throw new ParameterException("Parameter with code: " + parameterCode
                + " is not defined in any type: mandatory, mandatory variable or optional");
    }

    public void removeParameter(int parameterCode) throws ParameterException {
        if (this.mandatoryCodes.contains(parameterCode)) {
            int index = this.mandatoryCodeToIndex.get(parameterCode);
            this.f_Parameters.remove(index);
        }

        if (this.mandatoryVariableCodes.contains(parameterCode)) {
            int index = this.mandatoryVariableCodeToIndex.get(parameterCode);
            this.v_Parameters.remove(index);
        }
        if (this.optionalCodes.contains(parameterCode)) {
            int index = this.optionalCodeToIndex.get(parameterCode);
            this.o_Parameters.remove(index);
        }
        throw new ParameterException("Parameter with code: " + parameterCode
                + " is not defined in any type: mandatory, mandatory variable or optional");
    }

    public String toString() {
        return super.toString() + "[" + getMessageType().getCode() + "]: F" + this.f_Parameters + ", V" + this.v_Parameters
                + ", O" + this.o_Parameters;
    }

    public CircuitIdentificationCode getCircuitIdentificationCode() {
        return this.cic;
    }

    public void setCircuitIdentificationCode(CircuitIdentificationCode cic) {
        this.cic = cic;

    }

}
