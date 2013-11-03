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
package org.mobicents.protocols.ss7.cap.service.gprs.primitive;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.EsiGprs.DetachSpecificInformationImpl;
import org.mobicents.protocols.ss7.cap.EsiGprs.DisconnectSpecificInformationImpl;
import org.mobicents.protocols.ss7.cap.EsiGprs.PDPContextEstablishmentAcknowledgementSpecificInformationImpl;
import org.mobicents.protocols.ss7.cap.EsiGprs.PDPContextEstablishmentSpecificInformationImpl;
import org.mobicents.protocols.ss7.cap.EsiGprs.PdpContextchangeOfPositionSpecificInformationImpl;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.DetachSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.DisconnectSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentAcknowledgementSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.PdpContextchangeOfPositionSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformation;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationGPRSImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class GPRSEventSpecificInformationImpl implements GPRSEventSpecificInformation, CAPAsnPrimitive {

    public static final String _PrimitiveName = "GPRSEventSpecificInformation";

    public static final int _ID_locationInformationGPRS = 0;
    public static final int _ID_pdpContextchangeOfPositionSpecificInformation = 1;
    public static final int _ID_detachSpecificInformation = 2;
    public static final int _ID_disconnectSpecificInformation = 3;
    public static final int _ID_pdpContextEstablishmentSpecificInformation = 4;
    public static final int _ID_pdpContextEstablishmentAcknowledgementSpecificInformation = 5;

    private LocationInformationGPRS locationInformationGPRS;
    private PdpContextchangeOfPositionSpecificInformation pdpContextchangeOfPositionSpecificInformation;
    private DetachSpecificInformation detachSpecificInformation;
    private DisconnectSpecificInformation disconnectSpecificInformation;
    private PDPContextEstablishmentSpecificInformation pdpContextEstablishmentSpecificInformation;
    private PDPContextEstablishmentAcknowledgementSpecificInformation pdpContextEstablishmentAcknowledgementSpecificInformation;

    public GPRSEventSpecificInformationImpl() {
        super();
    }

    public GPRSEventSpecificInformationImpl(LocationInformationGPRS locationInformationGPRS) {
        super();
        this.locationInformationGPRS = locationInformationGPRS;
    }

    public GPRSEventSpecificInformationImpl(
            PdpContextchangeOfPositionSpecificInformation pdpContextchangeOfPositionSpecificInformation) {
        super();
        this.pdpContextchangeOfPositionSpecificInformation = pdpContextchangeOfPositionSpecificInformation;
    }

    public GPRSEventSpecificInformationImpl(DetachSpecificInformation detachSpecificInformation) {
        super();
        this.detachSpecificInformation = detachSpecificInformation;
    }

    public GPRSEventSpecificInformationImpl(DisconnectSpecificInformation disconnectSpecificInformation) {
        super();
        this.disconnectSpecificInformation = disconnectSpecificInformation;
    }

    public GPRSEventSpecificInformationImpl(
            PDPContextEstablishmentSpecificInformation pdpContextEstablishmentSpecificInformation) {
        super();
        this.pdpContextEstablishmentSpecificInformation = pdpContextEstablishmentSpecificInformation;
    }

    public GPRSEventSpecificInformationImpl(
            PDPContextEstablishmentAcknowledgementSpecificInformation pdpContextEstablishmentAcknowledgementSpecificInformation) {
        super();
        this.pdpContextEstablishmentAcknowledgementSpecificInformation = pdpContextEstablishmentAcknowledgementSpecificInformation;
    }

    @Override
    public LocationInformationGPRS getLocationInformationGPRS() {
        return this.locationInformationGPRS;
    }

    @Override
    public PdpContextchangeOfPositionSpecificInformation getPdpContextchangeOfPositionSpecificInformation() {
        return this.pdpContextchangeOfPositionSpecificInformation;
    }

    @Override
    public DetachSpecificInformation getDetachSpecificInformation() {
        return this.detachSpecificInformation;
    }

    @Override
    public DisconnectSpecificInformation getDisconnectSpecificInformation() {
        return this.disconnectSpecificInformation;
    }

    @Override
    public PDPContextEstablishmentSpecificInformation getPDPContextEstablishmentSpecificInformation() {
        return this.pdpContextEstablishmentSpecificInformation;
    }

    @Override
    public PDPContextEstablishmentAcknowledgementSpecificInformation getPDPContextEstablishmentAcknowledgementSpecificInformation() {
        return this.pdpContextEstablishmentAcknowledgementSpecificInformation;
    }

    @Override
    public int getTag() throws CAPException {

        if (locationInformationGPRS != null) {
            return _ID_locationInformationGPRS;
        } else if (pdpContextchangeOfPositionSpecificInformation != null) {
            return _ID_pdpContextchangeOfPositionSpecificInformation;
        } else if (detachSpecificInformation != null) {
            return _ID_detachSpecificInformation;
        } else if (disconnectSpecificInformation != null) {
            return _ID_disconnectSpecificInformation;
        } else if (pdpContextEstablishmentSpecificInformation != null) {
            return _ID_pdpContextEstablishmentSpecificInformation;
        } else {
            return _ID_pdpContextEstablishmentAcknowledgementSpecificInformation;
        }
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
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

    private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, IOException, AsnException,
            MAPParsingComponentException {

        this.locationInformationGPRS = null;
        this.pdpContextchangeOfPositionSpecificInformation = null;
        this.detachSpecificInformation = null;
        this.disconnectSpecificInformation = null;
        this.pdpContextEstablishmentSpecificInformation = null;
        this.pdpContextEstablishmentAcknowledgementSpecificInformation = null;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
                case _ID_locationInformationGPRS:
                    if (ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".locationInformationGPRS: Parameter is primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.locationInformationGPRS = new LocationInformationGPRSImpl();
                    ((LocationInformationGPRSImpl) this.locationInformationGPRS).decodeData(ais, length);
                    break;
                case _ID_pdpContextchangeOfPositionSpecificInformation:
                    if (ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".pdpContextchangeOfPositionSpecificInformation: Parameter is primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.pdpContextchangeOfPositionSpecificInformation = new PdpContextchangeOfPositionSpecificInformationImpl();
                    ((PdpContextchangeOfPositionSpecificInformationImpl) this.pdpContextchangeOfPositionSpecificInformation)
                            .decodeData(ais, length);
                    break;
                case _ID_detachSpecificInformation:
                    if (ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".detachSpecificInformation: Parameter is primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.detachSpecificInformation = new DetachSpecificInformationImpl();
                    ((DetachSpecificInformationImpl) this.detachSpecificInformation).decodeData(ais, length);
                    break;
                case _ID_disconnectSpecificInformation:
                    if (ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".disconnectSpecificInformation: Parameter is primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.disconnectSpecificInformation = new DisconnectSpecificInformationImpl();
                    ((DisconnectSpecificInformationImpl) this.disconnectSpecificInformation).decodeData(ais, length);
                    break;
                case _ID_pdpContextEstablishmentSpecificInformation:
                    if (ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".pdpContextEstablishmentSpecificInformation: Parameter is primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.pdpContextEstablishmentSpecificInformation = new PDPContextEstablishmentSpecificInformationImpl();
                    ((PDPContextEstablishmentSpecificInformationImpl) this.pdpContextEstablishmentSpecificInformation)
                            .decodeData(ais, length);
                    break;
                case _ID_pdpContextEstablishmentAcknowledgementSpecificInformation:
                    if (ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".pdpContextEstablishmentAcknowledgementSpecificInformation: Parameter is primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.pdpContextEstablishmentAcknowledgementSpecificInformation = new PDPContextEstablishmentAcknowledgementSpecificInformationImpl();
                    ((PDPContextEstablishmentAcknowledgementSpecificInformationImpl) this.pdpContextEstablishmentAcknowledgementSpecificInformation)
                            .decodeData(ais, length);
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

        int cnt = 0;
        if (this.locationInformationGPRS != null)
            cnt++;
        if (this.pdpContextchangeOfPositionSpecificInformation != null)
            cnt++;
        if (this.detachSpecificInformation != null)
            cnt++;
        if (this.disconnectSpecificInformation != null)
            cnt++;
        if (this.pdpContextEstablishmentSpecificInformation != null)
            cnt++;
        if (this.pdpContextEstablishmentAcknowledgementSpecificInformation != null)
            cnt++;

        if (cnt != 1)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": one and only one choice is required.");

        try {
            if (this.locationInformationGPRS != null) {
                ((LocationInformationGPRSImpl) this.locationInformationGPRS).encodeData(asnOs);
                return;
            }

            if (this.pdpContextchangeOfPositionSpecificInformation != null) {
                ((PdpContextchangeOfPositionSpecificInformationImpl) this.pdpContextchangeOfPositionSpecificInformation)
                        .encodeData(asnOs);
                return;
            }

            if (this.detachSpecificInformation != null) {
                ((DetachSpecificInformationImpl) this.detachSpecificInformation).encodeData(asnOs);
                return;
            }

            if (this.disconnectSpecificInformation != null) {
                ((DisconnectSpecificInformationImpl) this.disconnectSpecificInformation).encodeData(asnOs);
                return;
            }

            if (this.pdpContextEstablishmentSpecificInformation != null) {
                ((PDPContextEstablishmentSpecificInformationImpl) this.pdpContextEstablishmentSpecificInformation)
                        .encodeData(asnOs);
                return;
            }

            if (this.pdpContextEstablishmentAcknowledgementSpecificInformation != null) {
                ((PDPContextEstablishmentAcknowledgementSpecificInformationImpl) this.pdpContextEstablishmentAcknowledgementSpecificInformation)
                        .encodeData(asnOs);
                return;
            }

        } catch (MAPException e) {
            throw new CAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.locationInformationGPRS != null) {
            sb.append("locationInformationGPRS=");
            sb.append(this.locationInformationGPRS.toString());
            sb.append(" ");
        } else if (this.pdpContextchangeOfPositionSpecificInformation != null) {
            sb.append("pdpContextchangeOfPositionSpecificInformation=");
            sb.append(this.pdpContextchangeOfPositionSpecificInformation.toString());
            sb.append(" ");
        } else if (this.detachSpecificInformation != null) {
            sb.append("detachSpecificInformation=");
            sb.append(this.detachSpecificInformation.toString());
            sb.append(" ");
        } else if (this.disconnectSpecificInformation != null) {
            sb.append("disconnectSpecificInformation=");
            sb.append(this.disconnectSpecificInformation.toString());
            sb.append(" ");
        } else if (this.pdpContextEstablishmentSpecificInformation != null) {
            sb.append("pdpContextEstablishmentSpecificInformation=");
            sb.append(this.pdpContextEstablishmentSpecificInformation.toString());
            sb.append(" ");
        } else if (this.pdpContextEstablishmentAcknowledgementSpecificInformation != null) {
            sb.append("pdpContextEstablishmentAcknowledgementSpecificInformation=");
            sb.append(this.pdpContextEstablishmentAcknowledgementSpecificInformation.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }

}
