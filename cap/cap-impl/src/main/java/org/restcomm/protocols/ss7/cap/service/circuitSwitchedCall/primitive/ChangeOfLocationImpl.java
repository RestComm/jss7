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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ChangeOfLocation;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ChangeOfLocationAlt;
import org.restcomm.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.LAIFixedLengthImpl;

/**
*
* @author sergey vetyutnev
*
*/
public class ChangeOfLocationImpl implements ChangeOfLocation, CAPAsnPrimitive {

    private static final String CELL_GLOBAL_ID = "cellGlobalId";
    private static final String SERVICE_AREA_ID = "serviceAreaId";
    private static final String LOCATION_AREA_ID = "locationAreaId";
    private static final String INTER_SYSTEM_HAND_OVER = "interSystemHandOver";
    private static final String INTER_PLMN_HAND_OVER = "interPLMNHandOver";
    private static final String INTER_MSC_HAND_OVER = "interMSCHandOver";
    private static final String CHANGE_OF_LOCATION_ALT = "changeOfLocationAlt";

    public static final int _ID_cellGlobalId = 0;
    public static final int _ID_serviceAreaId = 1;
    public static final int _ID_locationAreaId = 2;
    public static final int _ID_interSystemHandOver = 3;
    public static final int _ID_interPLMNHandOver = 4;
    public static final int _ID_interMSCHandOver = 5;
    public static final int _ID_changeOfLocationAlt = 6;

    public static final String _PrimitiveName = "ChangeOfLocation";

    private CellGlobalIdOrServiceAreaIdFixedLength cellGlobalId;
    private CellGlobalIdOrServiceAreaIdFixedLength serviceAreaId;
    private LAIFixedLength locationAreaId;
    private boolean interSystemHandOver;
    private boolean interPLMNHandOver;
    private boolean interMSCHandOver;
    private ChangeOfLocationAlt changeOfLocationAlt;

    public ChangeOfLocationImpl() {
    }

    public ChangeOfLocationImpl(CellGlobalIdOrServiceAreaIdFixedLength value, CellGlobalIdOrServiceAreaIdFixedLength_Option option) {
        switch (option) {
        case cellGlobalId:
            this.cellGlobalId = value;
            break;
        case serviceAreaId:
            this.serviceAreaId = value;
            break;
        }
    }

    public ChangeOfLocationImpl(LAIFixedLength locationAreaId) {
        this.locationAreaId = locationAreaId;
    }

    public ChangeOfLocationImpl(Boolean_Option option) {
        switch (option) {
        case interSystemHandOver:
            this.interSystemHandOver = true;
            break;
        case interPLMNHandOver:
            this.interPLMNHandOver = true;
            break;
        case interMSCHandOver:
            this.interMSCHandOver = true;
            break;
        }
    }

    public ChangeOfLocationImpl(ChangeOfLocationAlt changeOfLocationAlt) {
        this.changeOfLocationAlt = changeOfLocationAlt;
    }


    @Override
    public int getTag() throws CAPException {
        if (cellGlobalId != null) {
            return _ID_cellGlobalId;
        } else if (serviceAreaId != null) {
            return _ID_serviceAreaId;
        } else if (locationAreaId != null) {
            return _ID_locationAreaId;
        } else if (interSystemHandOver) {
            return _ID_interSystemHandOver;
        } else if (interPLMNHandOver) {
            return _ID_interPLMNHandOver;
        } else if (interMSCHandOver) {
            return _ID_interMSCHandOver;
        } else if (changeOfLocationAlt != null) {
            return _ID_changeOfLocationAlt;
        }

        throw new CAPException("Error while encoding " + _PrimitiveName + ": no choice is specified");
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        if (changeOfLocationAlt != null)
            return false;
        else
            return true;
    }

    @Override
    public CellGlobalIdOrServiceAreaIdFixedLength getCellGlobalId() {
        return cellGlobalId;
    }

    @Override
    public CellGlobalIdOrServiceAreaIdFixedLength getServiceAreaId() {
        return serviceAreaId;
    }

    @Override
    public LAIFixedLength getLocationAreaId() {
        return locationAreaId;
    }

    @Override
    public boolean isInterSystemHandOver() {
        return interSystemHandOver;
    }

    @Override
    public boolean isInterPLMNHandOver() {
        return interPLMNHandOver;
    }

    @Override
    public boolean isInterMSCHandOver() {
        return interMSCHandOver;
    }

    @Override
    public ChangeOfLocationAlt getChangeOfLocationAlt() {
        return changeOfLocationAlt;
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

        this.cellGlobalId = null;
        this.serviceAreaId = null;
        this.locationAreaId = null;
        this.interSystemHandOver = false;
        this.interPLMNHandOver = false;
        this.interMSCHandOver = false;
        this.changeOfLocationAlt = null;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
//            public static final int _ID_changeOfLocationAlt = 6;
            case _ID_cellGlobalId:
                this.cellGlobalId = new CellGlobalIdOrServiceAreaIdFixedLengthImpl();
                ((CellGlobalIdOrServiceAreaIdFixedLengthImpl) this.cellGlobalId).decodeData(ais, length);
                break;
            case _ID_serviceAreaId:
                this.serviceAreaId = new CellGlobalIdOrServiceAreaIdFixedLengthImpl();
                ((CellGlobalIdOrServiceAreaIdFixedLengthImpl) this.serviceAreaId).decodeData(ais, length);
                break;

            case _ID_locationAreaId:
                this.locationAreaId = new LAIFixedLengthImpl();
                ((LAIFixedLengthImpl) this.locationAreaId).decodeData(ais, length);
                break;

            case _ID_interSystemHandOver:
                ais.readNullData(length);
                this.interSystemHandOver = true;
                break;
            case _ID_interPLMNHandOver:
                ais.readNullData(length);
                this.interPLMNHandOver = true;
                break;
            case _ID_interMSCHandOver:
                ais.readNullData(length);
                this.interMSCHandOver = true;
                break;

            case _ID_changeOfLocationAlt:
                this.changeOfLocationAlt = new ChangeOfLocationAltImpl();
                ((ChangeOfLocationAltImpl) this.changeOfLocationAlt).decodeData(ais, length);
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
            if (cellGlobalId != null) {
                ((CellGlobalIdOrServiceAreaIdFixedLengthImpl) cellGlobalId).encodeData(asnOs);
                return;
            } else if (serviceAreaId != null) {
                ((CellGlobalIdOrServiceAreaIdFixedLengthImpl) serviceAreaId).encodeData(asnOs);
                return;

            } else if (locationAreaId != null) {
                ((LAIFixedLengthImpl) locationAreaId).encodeData(asnOs);
                return;

            } else if (interSystemHandOver) {
                asnOs.writeNullData();
                return;
            } else if (interPLMNHandOver) {
                asnOs.writeNullData();
                return;
            } else if (interMSCHandOver) {
                asnOs.writeNullData();
                return;

            } else if (changeOfLocationAlt != null) {
                ((ChangeOfLocationAltImpl) changeOfLocationAlt).encodeData(asnOs);
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

        if (cellGlobalId != null) {
            sb.append("cellGlobalId=");
            sb.append(cellGlobalId);
        } else if (serviceAreaId != null) {
            sb.append("serviceAreaId=");
            sb.append(serviceAreaId);
        } else if (locationAreaId != null) {
            sb.append("locationAreaId=");
            sb.append(locationAreaId);
        } else if (interSystemHandOver) {
            sb.append("interSystemHandOver");
        } else if (interPLMNHandOver) {
            sb.append("interPLMNHandOver");
        } else if (interMSCHandOver) {
            sb.append("interMSCHandOver");
        } else if (changeOfLocationAlt != null) {
            sb.append("changeOfLocationAlt=");
            sb.append(changeOfLocationAlt);
        }

        sb.append("]");

        return sb.toString();
    }

    protected static final XMLFormat<ChangeOfLocationImpl> CHANGE_OF_LOCATION_XML = new XMLFormat<ChangeOfLocationImpl>(ChangeOfLocationImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ChangeOfLocationImpl changeOfLocation) throws XMLStreamException {
            changeOfLocation.cellGlobalId = xml.get(CELL_GLOBAL_ID, CellGlobalIdOrServiceAreaIdFixedLengthImpl.class);
            changeOfLocation.serviceAreaId = xml.get(SERVICE_AREA_ID, CellGlobalIdOrServiceAreaIdFixedLengthImpl.class);

            changeOfLocation.locationAreaId = xml.get(LOCATION_AREA_ID, LAIFixedLengthImpl.class);

            Boolean bval = xml.get(INTER_SYSTEM_HAND_OVER, Boolean.class);
            if (bval != null)
                changeOfLocation.interSystemHandOver = bval;
            bval = xml.get(INTER_PLMN_HAND_OVER, Boolean.class);
            if (bval != null)
                changeOfLocation.interPLMNHandOver = bval;
            bval = xml.get(INTER_MSC_HAND_OVER, Boolean.class);
            if (bval != null)
                changeOfLocation.interMSCHandOver = bval;

            changeOfLocation.changeOfLocationAlt = xml.get(CHANGE_OF_LOCATION_ALT, ChangeOfLocationAltImpl.class);
        }

        @Override
        public void write(ChangeOfLocationImpl changeOfLocation, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (changeOfLocation.cellGlobalId != null) {
                xml.add((CellGlobalIdOrServiceAreaIdFixedLengthImpl) changeOfLocation.cellGlobalId, CELL_GLOBAL_ID, CellGlobalIdOrServiceAreaIdFixedLengthImpl.class);
            }
            if (changeOfLocation.serviceAreaId != null) {
                xml.add((CellGlobalIdOrServiceAreaIdFixedLengthImpl) changeOfLocation.serviceAreaId, SERVICE_AREA_ID, CellGlobalIdOrServiceAreaIdFixedLengthImpl.class);
            }

            if (changeOfLocation.locationAreaId != null) {
                xml.add((LAIFixedLengthImpl) changeOfLocation.locationAreaId, LOCATION_AREA_ID, LAIFixedLengthImpl.class);
            }

            if (changeOfLocation.interSystemHandOver)
                xml.add(changeOfLocation.interSystemHandOver, INTER_SYSTEM_HAND_OVER, Boolean.class);
            if (changeOfLocation.interPLMNHandOver)
                xml.add(changeOfLocation.interPLMNHandOver, INTER_PLMN_HAND_OVER, Boolean.class);
            if (changeOfLocation.interMSCHandOver)
                xml.add(changeOfLocation.interMSCHandOver, INTER_MSC_HAND_OVER, Boolean.class);

            if (changeOfLocation.changeOfLocationAlt != null) {
                xml.add((ChangeOfLocationAltImpl) changeOfLocation.changeOfLocationAlt, CHANGE_OF_LOCATION_ALT, ChangeOfLocationAltImpl.class);
            }
        }
    };

    public enum CellGlobalIdOrServiceAreaIdFixedLength_Option {
        cellGlobalId, serviceAreaId;
    }

    public enum Boolean_Option {
        interSystemHandOver, interPLMNHandOver, interMSCHandOver;
    }

}
