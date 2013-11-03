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
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoS;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.QoSSubscribedImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class GPRSQoSImpl implements GPRSQoS, CAPAsnPrimitive {

    public static final String _PrimitiveName = "GPRSQoS";

    public static final int _ID_shortQoSFormat = 0;
    public static final int _ID_longQoSFormat = 1;

    private QoSSubscribed shortQoSFormat;
    private ExtQoSSubscribed longQoSFormat;

    public GPRSQoSImpl() {

    }

    public GPRSQoSImpl(QoSSubscribed shortQoSFormat) {
        this.shortQoSFormat = shortQoSFormat;
    }

    public GPRSQoSImpl(ExtQoSSubscribed longQoSFormat) {
        this.longQoSFormat = longQoSFormat;
    }

    @Override
    public QoSSubscribed getShortQoSFormat() {
        return this.shortQoSFormat;
    }

    @Override
    public ExtQoSSubscribed getLongQoSFormat() {
        return this.longQoSFormat;
    }

    @Override
    public int getTag() throws CAPException {
        if (shortQoSFormat != null) {
            return _ID_shortQoSFormat;
        } else {
            return _ID_longQoSFormat;
        }

    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        return true;
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

        this.shortQoSFormat = null;
        this.longQoSFormat = null;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
                case _ID_shortQoSFormat:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".shortQoSFormat: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.shortQoSFormat = new QoSSubscribedImpl();
                    ((QoSSubscribedImpl) this.shortQoSFormat).decodeData(ais, length);
                    break;
                case _ID_longQoSFormat:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".longQoSFormat: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.longQoSFormat = new ExtQoSSubscribedImpl();
                    ((ExtQoSSubscribedImpl) this.longQoSFormat).decodeData(ais, length);
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
        if (this.shortQoSFormat == null && this.longQoSFormat == null || this.shortQoSFormat != null
                && this.longQoSFormat != null) {
            throw new CAPException("Error while decoding " + _PrimitiveName + ": One and only one choice must be selected");
        }

        try {
            if (this.shortQoSFormat != null) {
                ((QoSSubscribedImpl) this.shortQoSFormat).encodeData(asnOs);
            } else {
                ((ExtQoSSubscribedImpl) this.longQoSFormat).encodeData(asnOs);
            }
        } catch (MAPException e) {
            throw new CAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.shortQoSFormat != null) {
            sb.append("shortQoSFormat=");
            sb.append(this.shortQoSFormat.toString());
        }

        if (this.longQoSFormat != null) {
            sb.append("longQoSFormat=");
            sb.append(this.longQoSFormat.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
