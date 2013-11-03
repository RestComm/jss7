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

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class ProblemImpl implements Problem {

    private ProblemType type;

    private GeneralProblemType generalProblemType;
    private InvokeProblemType invokeProblemType;
    private ReturnErrorProblemType returnErrorProblemType;
    private ReturnResultProblemType returnResultProblemType;

    /**
     * @return the type
     */
    public ProblemType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ProblemType type) {
        this.type = type;
    }

    /**
     * @return the generalProblemType
     */
    public GeneralProblemType getGeneralProblemType() {
        return generalProblemType;
    }

    /**
     * @param generalProblemType the generalProblemType to set
     */
    public void setGeneralProblemType(GeneralProblemType generalProblemType) {
        this.generalProblemType = generalProblemType;
        this.setType(ProblemType.General);
    }

    /**
     * @return the invokeProblemType
     */
    public InvokeProblemType getInvokeProblemType() {
        return invokeProblemType;
    }

    /**
     * @param invokeProblemType the invokeProblemType to set
     */
    public void setInvokeProblemType(InvokeProblemType invokeProblemType) {
        this.setType(ProblemType.Invoke);
        this.invokeProblemType = invokeProblemType;
    }

    /**
     * @return the returnErrorProblemType
     */
    public ReturnErrorProblemType getReturnErrorProblemType() {
        return returnErrorProblemType;
    }

    /**
     * @param returnErrorProblemType the returnErrorProblemType to set
     */
    public void setReturnErrorProblemType(ReturnErrorProblemType returnErrorProblemType) {
        this.returnErrorProblemType = returnErrorProblemType;
        this.setType(ProblemType.ReturnError);
    }

    /**
     * @return the returnResultProblemType
     */
    public ReturnResultProblemType getReturnResultProblemType() {
        return returnResultProblemType;
    }

    /**
     * @param returnResultProblemType the returnResultProblemType to set
     */
    public void setReturnResultProblemType(ReturnResultProblemType returnResultProblemType) {
        this.returnResultProblemType = returnResultProblemType;
        this.setType(ProblemType.ReturnResult);
    }

    public String getStringValue() {
        StringBuilder sb = new StringBuilder();
        switch (type) {
        case General:
            sb.append("generalProblemType=");
            sb.append(this.generalProblemType);
            break;
        case Invoke:
            sb.append("invokeProblemType=");
            sb.append(this.invokeProblemType);
            break;
        case ReturnResult:
            sb.append("returnResultProblemType=");
            sb.append(this.returnResultProblemType);
            break;
        case ReturnError:
            sb.append("returnErrorProblemType=");
            sb.append(this.returnErrorProblemType);
            break;
        }
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Problem[type=");
        sb.append(type);
        sb.append(" ");
        switch (type) {
            case General:
                sb.append("generalProblemType=");
                sb.append(this.generalProblemType);
                break;
            case Invoke:
                sb.append("invokeProblemType=");
                sb.append(this.invokeProblemType);
                break;
            case ReturnResult:
                sb.append("returnResultProblemType=");
                sb.append(this.returnResultProblemType);
                break;
            case ReturnError:
                sb.append("returnErrorProblemType=");
                sb.append(this.returnErrorProblemType);
                break;
        }
        sb.append("]");

        return sb.toString();
    }

    public void decode(AsnInputStream ais) throws ParseException {

        try {
            long t = ais.readInteger();

            switch (type) {
                case General:
                    this.generalProblemType = GeneralProblemType.getFromInt(t);
                    break;
                case Invoke:
                    this.invokeProblemType = InvokeProblemType.getFromInt(t);
                    break;
                case ReturnError:
                    this.returnErrorProblemType = ReturnErrorProblemType.getFromInt(t);
                    break;
                case ReturnResult:
                    this.returnResultProblemType = ReturnResultProblemType.getFromInt(t);
                    break;
                default:
                    // should not happen
                    throw new ParseException(null, GeneralProblemType.MistypedComponent);
            }

        } catch (IOException e) {
            throw new ParseException(null, GeneralProblemType.BadlyStructuredComponent, "IOException while decoding Problem: "
                    + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(null, GeneralProblemType.BadlyStructuredComponent, "AsnException while decoding Problem: "
                    + e.getMessage(), e);
        }

    }

    public void encode(AsnOutputStream aos) throws EncodeException {

        try {

            switch (type) {
                case General:
                    if (this.generalProblemType == null)
                        throw new EncodeException("Problem Type is General, no specific type set");

                    aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, (int) type.getTypeTag(), this.generalProblemType.getType());
                    break;

                case Invoke:
                    if (this.invokeProblemType == null)
                        throw new EncodeException("Problem Type is Invoke, no specific type set");
                    aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, (int) type.getTypeTag(), this.invokeProblemType.getType());
                    break;

                case ReturnError:
                    if (this.returnErrorProblemType == null)
                        throw new EncodeException("Problem Type is ReturnError, no specific type set");
                    aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, (int) type.getTypeTag(), this.returnErrorProblemType.getType());
                    break;

                case ReturnResult:
                    if (this.returnResultProblemType == null)
                        throw new EncodeException("Problem Type is Result, no specific type set");
                    aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, (int) type.getTypeTag(),
                            this.returnResultProblemType.getType());
                    break;

                default:
                    // should not happen
                    throw new EncodeException();
            }

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding Problem: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding Problem: " + e.getMessage(), e);
        }

    }

}
