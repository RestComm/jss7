/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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

package org.mobicents.protocols.ss7.cap.EsiBcsm;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.MetDPCriterion;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.MetDPCriterionAlt;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;

/**
*
* @author sergey vetyutnev
*
*/
public class MetDPCriterionImpl implements MetDPCriterion, CAPAsnPrimitive {

    private static final String ENTERING_CELL_GLOBAL_ID = "enteringCellGlobalId";
    private static final String LEAVING_CELL_GLOBAL_ID = "leavingCellGlobalId";
    private static final String ENTERING_SERVICE_AREA_ID = "enteringServiceAreaId";
    private static final String LEAVING_SERVICE_AREA_ID = "leavingServiceAreaId";
    private static final String ENTERING_LOCATION_AREA_ID = "enteringLocationAreaId";
    private static final String LEAVING_LOCATION_AREA_ID = "leavingLocationAreaId";
    private static final String INTER_SYSTEM_HAND_OVER_TO_UMTS = "interSystemHandOverToUMTS";
    private static final String INTER_SYSTEM_HAND_OVER_TO_GSM = "interSystemHandOverToGSM";
    private static final String INTER_PLMN_HAND_OVER = "interPLMNHandOver";
    private static final String INTER_MSC_HAND_OVER = "interMSCHandOver";
    private static final String MET_DP_CRITERION_ALT = "metDPCriterionAlt";

    public static final int _ID_enteringCellGlobalId = 0;
    public static final int _ID_leavingCellGlobalId = 1;
    public static final int _ID_enteringServiceAreaId = 2;
    public static final int _ID_leavingServiceAreaId = 3;
    public static final int _ID_enteringLocationAreaId = 4;
    public static final int _ID_leavingLocationAreaId = 5;
    public static final int _ID_interSystemHandOverToUMTS = 6;
    public static final int _ID_interSystemHandOverToGSM = 7;
    public static final int _ID_interPLMNHandOver = 8;
    public static final int _ID_interMSCHandOver = 9;
    public static final int _ID_metDPCriterionAlt = 10;

    public static final String _PrimitiveName = "MetDPCriterion";

    private CellGlobalIdOrServiceAreaIdFixedLength enteringCellGlobalId;
    private CellGlobalIdOrServiceAreaIdFixedLength leavingCellGlobalId;
    private CellGlobalIdOrServiceAreaIdFixedLength enteringServiceAreaId;
    private CellGlobalIdOrServiceAreaIdFixedLength leavingServiceAreaId;
    private LAIFixedLength enteringLocationAreaId;
    private LAIFixedLength leavingLocationAreaId;
    private boolean interSystemHandOverToUMTS;
    private boolean interSystemHandOverToGSM;
    private boolean interPLMNHandOver;
    private boolean interMSCHandOver;
    private MetDPCriterionAlt metDPCriterionAlt;

    public MetDPCriterionImpl() {
    }

    public MetDPCriterionImpl(CellGlobalIdOrServiceAreaIdFixedLength value, CellGlobalIdOrServiceAreaIdFixedLength_Option option) {
        switch (option) {
        case enteringCellGlobalId:
            this.enteringCellGlobalId = value;
            break;
        case leavingCellGlobalId:
            this.leavingCellGlobalId = value;
            break;
        case enteringServiceAreaId:
            this.enteringServiceAreaId = value;
            break;
        case leavingServiceAreaId:
            this.leavingServiceAreaId = value;
            break;
        }
    }

    public MetDPCriterionImpl(LAIFixedLength value, LAIFixedLength_Option option) {
        switch (option) {
        case enteringLocationAreaId:
            this.enteringLocationAreaId = value;
            break;
        case leavingLocationAreaId:
            this.leavingLocationAreaId = value;
            break;
        }
    }

    public MetDPCriterionImpl(Boolean_Option option) {
        switch (option) {
        case interSystemHandOverToUMTS:
            this.interSystemHandOverToUMTS = true;
            break;
        case interSystemHandOverToGSM:
            this.interSystemHandOverToGSM = true;
            break;
        case interPLMNHandOver:
            this.interPLMNHandOver = true;
            break;
        case interMSCHandOver:
            this.interMSCHandOver = true;
            break;
        }
    }

    public MetDPCriterionImpl(MetDPCriterionAlt metDPCriterionAlt) {
        this.metDPCriterionAlt = metDPCriterionAlt;
    }


    @Override
    public int getTag() throws CAPException {
        if (enteringCellGlobalId != null) {
            return _ID_enteringCellGlobalId;
        } else if (leavingCellGlobalId != null) {
            return _ID_leavingCellGlobalId;
        } else if (enteringServiceAreaId != null) {
            return _ID_enteringServiceAreaId;
        } else if (leavingServiceAreaId != null) {
            return _ID_leavingServiceAreaId;
        } else if (enteringLocationAreaId != null) {
            return _ID_enteringLocationAreaId;
        } else if (leavingLocationAreaId != null) {
            return _ID_leavingLocationAreaId;
        } else if (interSystemHandOverToUMTS) {
            return _ID_interSystemHandOverToUMTS;
        } else if (interSystemHandOverToGSM) {
            return _ID_interSystemHandOverToGSM;
        } else if (interPLMNHandOver) {
            return _ID_interPLMNHandOver;
        } else if (interMSCHandOver) {
            return _ID_interMSCHandOver;
        } else if (metDPCriterionAlt != null) {
            return _ID_metDPCriterionAlt;
        }

        throw new CAPException("Error while encoding " + _PrimitiveName + ": no choice is specified");
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        if (metDPCriterionAlt != null)
            return false;
        else
            return true;
    }

    @Override
    public CellGlobalIdOrServiceAreaIdFixedLength getEnteringCellGlobalId() {
        return enteringCellGlobalId;
    }

    @Override
    public CellGlobalIdOrServiceAreaIdFixedLength getLeavingCellGlobalId() {
        return leavingCellGlobalId;
    }

    @Override
    public CellGlobalIdOrServiceAreaIdFixedLength getEnteringServiceAreaId() {
        return enteringServiceAreaId;
    }

    @Override
    public CellGlobalIdOrServiceAreaIdFixedLength getLeavingServiceAreaId() {
        return leavingServiceAreaId;
    }

    @Override
    public LAIFixedLength getEnteringLocationAreaId() {
        return enteringLocationAreaId;
    }

    @Override
    public LAIFixedLength getLeavingLocationAreaId() {
        return leavingLocationAreaId;
    }

    @Override
    public boolean getInterSystemHandOverToUMTS() {
        return interSystemHandOverToUMTS;
    }

    @Override
    public boolean getInterSystemHandOverToGSM() {
        return interSystemHandOverToGSM;
    }

    @Override
    public boolean getInterPLMNHandOver() {
        return interPLMNHandOver;
    }

    @Override
    public boolean getInterMSCHandOver() {
        return interMSCHandOver;
    }

    @Override
    public MetDPCriterionAlt getMetDPCriterionAlt() {
        return metDPCriterionAlt;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, MAPParsingComponentException,
            IOException, AsnException {

        this.enteringCellGlobalId = null;
        this.leavingCellGlobalId = null;
        this.enteringServiceAreaId = null;
        this.leavingServiceAreaId = null;
        this.enteringLocationAreaId = null;
        this.leavingLocationAreaId = null;
        this.interSystemHandOverToUMTS = false;
        this.interSystemHandOverToGSM = false;
        this.interPLMNHandOver = false;
        this.interMSCHandOver = false;
        this.metDPCriterionAlt = null;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
            case _ID_enteringCellGlobalId:
                this.enteringCellGlobalId = new CellGlobalIdOrServiceAreaIdFixedLengthImpl();
                ((CellGlobalIdOrServiceAreaIdFixedLengthImpl) this.enteringCellGlobalId).decodeData(ais, length);
                break;
            case _ID_leavingCellGlobalId:
                this.leavingCellGlobalId = new CellGlobalIdOrServiceAreaIdFixedLengthImpl();
                ((CellGlobalIdOrServiceAreaIdFixedLengthImpl) this.leavingCellGlobalId).decodeData(ais, length);
                break;
            case _ID_enteringServiceAreaId:
                this.enteringServiceAreaId = new CellGlobalIdOrServiceAreaIdFixedLengthImpl();
                ((CellGlobalIdOrServiceAreaIdFixedLengthImpl) this.enteringServiceAreaId).decodeData(ais, length);
                break;
            case _ID_leavingServiceAreaId:
                this.leavingServiceAreaId = new CellGlobalIdOrServiceAreaIdFixedLengthImpl();
                ((CellGlobalIdOrServiceAreaIdFixedLengthImpl) this.leavingServiceAreaId).decodeData(ais, length);
                break;

            case _ID_enteringLocationAreaId:
                this.enteringLocationAreaId = new LAIFixedLengthImpl();
                ((LAIFixedLengthImpl) this.enteringLocationAreaId).decodeData(ais, length);
                break;
            case _ID_leavingLocationAreaId:
                this.leavingLocationAreaId = new LAIFixedLengthImpl();
                ((LAIFixedLengthImpl) this.leavingLocationAreaId).decodeData(ais, length);
                break;

            case _ID_interSystemHandOverToUMTS:
                ais.readNullData(length);
                this.interSystemHandOverToUMTS = true;
                break;
            case _ID_interSystemHandOverToGSM:
                ais.readNullData(length);
                this.interSystemHandOverToGSM = true;
                break;
            case _ID_interPLMNHandOver:
                ais.readNullData(length);
                this.interPLMNHandOver = true;
                break;
            case _ID_interMSCHandOver:
                ais.readNullData(length);
                this.interMSCHandOver = true;
                break;

            case _ID_metDPCriterionAlt:
                this.metDPCriterionAlt = new MetDPCriterionAltImpl();
                ((MetDPCriterionAltImpl) this.metDPCriterionAlt).decodeData(ais, length);
                break;

                default:
                    throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tag",
                            CAPParsingComponentExceptionReason.MistypedParameter);
            }
        } else {
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagClass",
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {
        try {
            if (enteringCellGlobalId != null) {
                ((CellGlobalIdOrServiceAreaIdFixedLengthImpl) enteringCellGlobalId).encodeData(asnOs);
                return;
            } else if (leavingCellGlobalId != null) {
                ((CellGlobalIdOrServiceAreaIdFixedLengthImpl) leavingCellGlobalId).encodeData(asnOs);
                return;
            } else if (enteringServiceAreaId != null) {
                ((CellGlobalIdOrServiceAreaIdFixedLengthImpl) enteringServiceAreaId).encodeData(asnOs);
                return;
            } else if (leavingServiceAreaId != null) {
                ((CellGlobalIdOrServiceAreaIdFixedLengthImpl) leavingServiceAreaId).encodeData(asnOs);
                return;

            } else if (enteringLocationAreaId != null) {
                ((LAIFixedLengthImpl) enteringLocationAreaId).encodeData(asnOs);
                return;
            } else if (leavingLocationAreaId != null) {
                ((LAIFixedLengthImpl) leavingLocationAreaId).encodeData(asnOs);
                return;

            } else if (interSystemHandOverToUMTS) {
                asnOs.writeNullData();
                return;
            } else if (interSystemHandOverToGSM) {
                asnOs.writeNullData();
                return;
            } else if (interPLMNHandOver) {
                asnOs.writeNullData();
                return;
            } else if (interMSCHandOver) {
                asnOs.writeNullData();
                return;

            } else if (metDPCriterionAlt != null) {
                ((MetDPCriterionAltImpl) metDPCriterionAlt).encodeData(asnOs);
                return;
            }
        } catch (MAPException e) {
            throw new CAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

        throw new CAPException("Error while encoding " + _PrimitiveName + ": no choice is specified");
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (enteringCellGlobalId != null) {
            sb.append("enteringCellGlobalId=");
            sb.append(enteringCellGlobalId.toString());
        } else if (leavingCellGlobalId != null) {
            sb.append("leavingCellGlobalId=");
            sb.append(leavingCellGlobalId.toString());
        } else if (enteringServiceAreaId != null) {
            sb.append("enteringServiceAreaId=");
            sb.append(enteringServiceAreaId.toString());
        } else if (leavingServiceAreaId != null) {
            sb.append("leavingServiceAreaId=");
            sb.append(leavingServiceAreaId.toString());
        } else if (enteringLocationAreaId != null) {
            sb.append("enteringLocationAreaId=");
            sb.append(enteringLocationAreaId.toString());
        } else if (leavingLocationAreaId != null) {
            sb.append("leavingLocationAreaId=");
            sb.append(leavingLocationAreaId.toString());
        } else if (interSystemHandOverToUMTS) {
            sb.append("interSystemHandOverToUMTS");
        } else if (interSystemHandOverToGSM) {
            sb.append("interSystemHandOverToGSM");
        } else if (interPLMNHandOver) {
            sb.append("interPLMNHandOver");
        } else if (interMSCHandOver) {
            sb.append("interMSCHandOver");
        } else if (metDPCriterionAlt != null) {
            sb.append("metDPCriterionAlt=");
            sb.append(metDPCriterionAlt.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    protected static final XMLFormat<MetDPCriterionImpl> MET_DP_CRITERION_XML = new XMLFormat<MetDPCriterionImpl>(MetDPCriterionImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MetDPCriterionImpl metDPCriterion) throws XMLStreamException {
            metDPCriterion.enteringCellGlobalId = xml.get(ENTERING_CELL_GLOBAL_ID, CellGlobalIdOrServiceAreaIdFixedLengthImpl.class);
            metDPCriterion.leavingCellGlobalId = xml.get(LEAVING_CELL_GLOBAL_ID, CellGlobalIdOrServiceAreaIdFixedLengthImpl.class);
            metDPCriterion.enteringServiceAreaId = xml.get(ENTERING_SERVICE_AREA_ID, CellGlobalIdOrServiceAreaIdFixedLengthImpl.class);
            metDPCriterion.leavingServiceAreaId = xml.get(LEAVING_SERVICE_AREA_ID, CellGlobalIdOrServiceAreaIdFixedLengthImpl.class);

            metDPCriterion.enteringLocationAreaId = xml.get(ENTERING_LOCATION_AREA_ID, LAIFixedLengthImpl.class);
            metDPCriterion.leavingLocationAreaId = xml.get(LEAVING_LOCATION_AREA_ID, LAIFixedLengthImpl.class);

            Boolean bval = xml.get(INTER_SYSTEM_HAND_OVER_TO_UMTS, Boolean.class);
            if (bval != null)
                metDPCriterion.interSystemHandOverToUMTS = bval;
            bval = xml.get(INTER_SYSTEM_HAND_OVER_TO_GSM, Boolean.class);
            if (bval != null)
                metDPCriterion.interSystemHandOverToGSM = bval;
            bval = xml.get(INTER_PLMN_HAND_OVER, Boolean.class);
            if (bval != null)
                metDPCriterion.interPLMNHandOver = bval;
            bval = xml.get(INTER_MSC_HAND_OVER, Boolean.class);
            if (bval != null)
                metDPCriterion.interMSCHandOver = bval;

            metDPCriterion.metDPCriterionAlt = xml.get(MET_DP_CRITERION_ALT, MetDPCriterionAltImpl.class);
        }

        @Override
        public void write(MetDPCriterionImpl metDPCriterion, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (metDPCriterion.enteringCellGlobalId != null) {
                xml.add((CellGlobalIdOrServiceAreaIdFixedLengthImpl) metDPCriterion.enteringCellGlobalId, ENTERING_CELL_GLOBAL_ID, CellGlobalIdOrServiceAreaIdFixedLengthImpl.class);
            }
            if (metDPCriterion.leavingCellGlobalId != null) {
                xml.add((CellGlobalIdOrServiceAreaIdFixedLengthImpl) metDPCriterion.leavingCellGlobalId, LEAVING_CELL_GLOBAL_ID, CellGlobalIdOrServiceAreaIdFixedLengthImpl.class);
            }
            if (metDPCriterion.enteringServiceAreaId != null) {
                xml.add((CellGlobalIdOrServiceAreaIdFixedLengthImpl) metDPCriterion.enteringServiceAreaId, ENTERING_SERVICE_AREA_ID, CellGlobalIdOrServiceAreaIdFixedLengthImpl.class);
            }
            if (metDPCriterion.leavingServiceAreaId != null) {
                xml.add((CellGlobalIdOrServiceAreaIdFixedLengthImpl) metDPCriterion.leavingServiceAreaId, LEAVING_SERVICE_AREA_ID, CellGlobalIdOrServiceAreaIdFixedLengthImpl.class);
            }

            if (metDPCriterion.enteringLocationAreaId != null) {
                xml.add((LAIFixedLengthImpl) metDPCriterion.enteringLocationAreaId, ENTERING_LOCATION_AREA_ID, LAIFixedLengthImpl.class);
            }
            if (metDPCriterion.leavingLocationAreaId != null) {
                xml.add((LAIFixedLengthImpl) metDPCriterion.leavingLocationAreaId, LEAVING_LOCATION_AREA_ID, LAIFixedLengthImpl.class);
            }

            if (metDPCriterion.interSystemHandOverToUMTS)
                xml.add(metDPCriterion.interSystemHandOverToUMTS, INTER_SYSTEM_HAND_OVER_TO_UMTS, Boolean.class);
            if (metDPCriterion.interSystemHandOverToGSM)
                xml.add(metDPCriterion.interSystemHandOverToGSM, INTER_SYSTEM_HAND_OVER_TO_GSM, Boolean.class);
            if (metDPCriterion.interPLMNHandOver)
                xml.add(metDPCriterion.interPLMNHandOver, INTER_PLMN_HAND_OVER, Boolean.class);
            if (metDPCriterion.interMSCHandOver)
                xml.add(metDPCriterion.interMSCHandOver, INTER_MSC_HAND_OVER, Boolean.class);

            if (metDPCriterion.metDPCriterionAlt != null) {
                xml.add((MetDPCriterionAltImpl) metDPCriterion.metDPCriterionAlt, MET_DP_CRITERION_ALT, MetDPCriterionAltImpl.class);
            }
        }
    };

    public enum CellGlobalIdOrServiceAreaIdFixedLength_Option {
        enteringCellGlobalId, leavingCellGlobalId, enteringServiceAreaId, leavingServiceAreaId;
    }

    public enum LAIFixedLength_Option {
        enteringLocationAreaId, leavingLocationAreaId;
    }

    public enum Boolean_Option {
        interSystemHandOverToUMTS, interSystemHandOverToGSM, interPLMNHandOver, interMSCHandOver;
    }

}
