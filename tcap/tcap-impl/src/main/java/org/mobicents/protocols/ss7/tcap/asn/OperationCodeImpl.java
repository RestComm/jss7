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

package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCodeType;

/**
 * @author baranowb
 *
 */
public class OperationCodeImpl implements OperationCode {

    private Long localOperationCode;
    private long[] globalOperationCode;
    private OperationCodeType type;

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.OperationCode#getOperationType()
     */
    public OperationCodeType getOperationType() {

        return type;
    }

    public void setOperationType(OperationCodeType t) {
        this.type = t;

    }

    public void setOperationCodeType(OperationCodeType type) {
        this.type = type;
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

    public String getStringValue() {
        if (this.localOperationCode != null)
            return this.localOperationCode.toString();
        else if (this.globalOperationCode != null)
            return Arrays.toString(this.globalOperationCode);
        else
            return "empty";
    }

    public String toString() {
        if (this.localOperationCode != null)
            return "OperationCode[OperationType=Local, data=" + this.localOperationCode.toString() + "]";
        else if (this.globalOperationCode != null)
            return "OperationCode[OperationType=Global, data=" + Arrays.toString(this.globalOperationCode) + "]";
        else
            return "OperationCode[empty]";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {

        try {
            if (this.type == OperationCodeType.Global) {
                this.globalOperationCode = ais.readObjectIdentifier();
            } else if (this.type == OperationCodeType.Local) {
                this.localOperationCode = ais.readInteger();
            } else {
                throw new ParseException(null, GeneralProblemType.MistypedComponent);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ParseException(null, GeneralProblemType.BadlyStructuredComponent,
                    "IOException while parsing OperationCode: " + e.getMessage(), e);
        } catch (AsnException e) {
            e.printStackTrace();
            throw new ParseException(null, GeneralProblemType.BadlyStructuredComponent,
                    "AsnException while parsing OperationCode: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        if (this.localOperationCode == null && this.globalOperationCode == null)
            throw new EncodeException("Operation code: No Operation code set!");

        try {
            if (this.type == OperationCodeType.Local) {
                aos.writeInteger(this.localOperationCode);
            } else if (this.type == OperationCodeType.Global) {
                aos.writeObjectIdentifier(this.globalOperationCode);
            } else {
                throw new EncodeException();
            }

        } catch (IOException e) {
            throw new EncodeException(e);
        } catch (AsnException e) {
            throw new EncodeException(e);
        }
    }

}
