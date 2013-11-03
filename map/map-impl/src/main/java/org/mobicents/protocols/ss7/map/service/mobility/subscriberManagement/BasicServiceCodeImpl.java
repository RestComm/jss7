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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class BasicServiceCodeImpl implements BasicServiceCode, MAPAsnPrimitive {

    public static final int _TAG_bearerService = 2;
    public static final int _TAG_teleservice = 3;

    public static final String _PrimitiveName = "BasicServiceCode";

    private BearerServiceCode bearerService;
    private TeleserviceCode teleservice;

    public BasicServiceCodeImpl() {
    }

    public BasicServiceCodeImpl(TeleserviceCode teleservice) {
        this.teleservice = teleservice;
    }

    public BasicServiceCodeImpl(BearerServiceCode bearerService) {
        this.bearerService = bearerService;
    }

    @Override
    public BearerServiceCode getBearerService() {
        return bearerService;
    }

    @Override
    public TeleserviceCode getTeleservice() {
        return teleservice;
    }

    @Override
    public int getTag() throws MAPException {
        if (bearerService != null)
            return _TAG_bearerService;
        else
            return _TAG_teleservice;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        return true;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.bearerService = null;
        this.teleservice = null;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
                case _TAG_bearerService:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".bearerService: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.bearerService = new BearerServiceCodeImpl();
                    ((BearerServiceCodeImpl) this.bearerService).decodeData(ais, length);
                    break;
                case _TAG_teleservice:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".teleservice: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.teleservice = new TeleserviceCodeImpl();
                    ((TeleserviceCodeImpl) this.teleservice).decodeData(ais, length);
                    break;

                default:
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tag",
                            MAPParsingComponentExceptionReason.MistypedParameter);
            }
        } else {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagClass",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.bearerService == null && this.teleservice == null || this.bearerService != null && this.teleservice != null) {
            throw new MAPException("Error while decoding " + _PrimitiveName + ": One and only one choice must be selected");
        }

        if (this.bearerService != null) {
            ((BearerServiceCodeImpl) this.bearerService).encodeData(asnOs);
        } else {
            ((TeleserviceCodeImpl) this.teleservice).encodeData(asnOs);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.bearerService != null) {
            sb.append("bearerService=" + this.bearerService.toString());
            sb.append(", ");
        }
        if (this.teleservice != null)
            sb.append("teleservice=" + this.teleservice.toString());
        sb.append("]");

        return sb.toString();
    }

}
