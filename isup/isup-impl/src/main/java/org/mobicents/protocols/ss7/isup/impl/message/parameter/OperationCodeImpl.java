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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.OperationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.OperationCodeType;

/**
 * @author baranowb
 *
 */
public class OperationCodeImpl extends AbstractAsnEncodable implements OperationCode {

    private Long localOperationCode;
    private long[] globalOperationCode;
    private OperationCodeType type;

    public OperationCodeType getOperationType() {

        return type;
    }

    public void setOperationType(OperationCodeType t) {
        this.type = t;

    }

    public void setLocalOperationCode(Long localOperationCode) {
        this.localOperationCode = localOperationCode;
        this.globalOperationCode = null;
        this.type = OperationCodeType.Local;
    }

    public void setGlobalOperationCode(long[] globalOperationCode) {
        this.localOperationCode = null;
        this.globalOperationCode = globalOperationCode;
        this.type = OperationCodeType.Global;
    }

    public Long getLocalOperationCode() {
        return this.localOperationCode;
    }

    public long[] getGlobalOperationCode() {
        return this.globalOperationCode;
    }

    public String toString() {
        if (this.localOperationCode != null)
            return "OperationCode[OperationType=Local, data=" + this.localOperationCode.toString() + "]";
        else if (this.globalOperationCode != null)
            return "OperationCode[OperationType=Global, data=" + Arrays.toString(this.globalOperationCode) + "]";
        else
            return "OperationCode[empty]";
    }

    public void decode(AsnInputStream ais) throws ParameterException {

        try {
            if (this.type == OperationCodeType.Global) {
                this.globalOperationCode = ais.readObjectIdentifier();
            } else if (this.type == OperationCodeType.Local) {
                this.localOperationCode = ais.readInteger();
            } else {
                throw new ParameterException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ParameterException("IOException while parsing OperationCode: " + e.getMessage(), e);
        } catch (AsnException e) {
            e.printStackTrace();
            throw new ParameterException("AsnException while parsing OperationCode: " + e.getMessage(), e);
        }
    }

    public void encode(AsnOutputStream aos) throws ParameterException {

        if (this.localOperationCode == null && this.globalOperationCode == null)
            throw new ParameterException("Operation code: No Operation code set!");

        try {
            if (this.type == OperationCodeType.Local) {
                aos.writeInteger(this.localOperationCode);
            } else if (this.type == OperationCodeType.Global) {
                aos.writeObjectIdentifier(this.globalOperationCode);
            } else {
                throw new ParameterException();
            }

        } catch (IOException e) {
            throw new ParameterException(e);
        } catch (AsnException e) {
            throw new ParameterException(e);
        }
    }

}
