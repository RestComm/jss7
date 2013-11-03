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
package org.mobicents.protocols.ss7.cap.EsiGprs;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.DetachSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.InitiatingEntity;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class DetachSpecificInformationImpl extends SequenceBase implements DetachSpecificInformation {

    public static final int _ID_initiatingEntity = 0;
    public static final int _ID_routeingAreaUpdate = 1;

    public static final int _ID_DetachSpecificInformation = 2;

    private InitiatingEntity initiatingEntity;
    private boolean routeingAreaUpdate;

    public DetachSpecificInformationImpl() {
        super("DetachSpecificInformation");
    }

    public DetachSpecificInformationImpl(InitiatingEntity initiatingEntity, boolean routeingAreaUpdate) {
        super("DetachSpecificInformation");
        this.initiatingEntity = initiatingEntity;
        this.routeingAreaUpdate = routeingAreaUpdate;
    }

    @Override
    public InitiatingEntity getInitiatingEntity() {
        return this.initiatingEntity;
    }

    @Override
    public boolean getRouteingAreaUpdate() {
        return this.routeingAreaUpdate;
    }

    public int getTag() throws CAPException {
        return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException,
            MAPParsingComponentException {
        this.initiatingEntity = null;
        this.routeingAreaUpdate = false;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {

                    case _ID_initiatingEntity:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".initiatingEntity: Parameter is not  primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        int i1 = (int) ais.readInteger();

                        this.initiatingEntity = InitiatingEntity.getInstance(i1);
                        break;
                    case _ID_routeingAreaUpdate:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".routeingAreaUpdate: Parameter is not  primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.routeingAreaUpdate = true;
                        ais.readNull();
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        try {
            if (this.initiatingEntity != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_initiatingEntity, this.initiatingEntity.getCode());

            if (routeingAreaUpdate)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_routeingAreaUpdate);

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.initiatingEntity != null) {
            sb.append("initiatingEntity=");
            sb.append(this.initiatingEntity.toString());
            sb.append(", ");
        }

        if (this.routeingAreaUpdate) {
            sb.append("routeingAreaUpdate ");
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }

}
