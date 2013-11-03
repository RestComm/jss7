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

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ExtBasicServiceCodeImpl implements ExtBasicServiceCode, MAPAsnPrimitive {

    private static final String EXT_BEARER_SERVICE = "extBearerService";
    private static final String EXT_TELE_SERVICE = "extTeleservice";

    public static final int _ID_ext_BearerService = 2;
    public static final int _ID_ext_Teleservice = 3;

    public static final String _PrimitiveName = "ExtBasicServiceCode";

    private ExtBearerServiceCode extBearerService;
    private ExtTeleserviceCode extTeleservice;

    public ExtBasicServiceCodeImpl() {
    }

    public ExtBasicServiceCodeImpl(ExtBearerServiceCode extBearerService) {
        this.extBearerService = extBearerService;
    }

    public ExtBasicServiceCodeImpl(ExtTeleserviceCode extTeleservice) {
        this.extTeleservice = extTeleservice;
    }

    public ExtBearerServiceCode getExtBearerService() {
        return extBearerService;
    }

    public ExtTeleserviceCode getExtTeleservice() {
        return extTeleservice;
    }

    public int getTag() throws MAPException {
        if (extBearerService != null)
            return _ID_ext_BearerService;
        else
            return _ID_ext_Teleservice;
    }

    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

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

        this.extBearerService = null;
        this.extTeleservice = null;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
                case _ID_ext_BearerService:
                    this.extBearerService = new ExtBearerServiceCodeImpl();
                    ((ExtBearerServiceCodeImpl) this.extBearerService).decodeData(ais, length);
                    break;
                case _ID_ext_Teleservice:
                    this.extTeleservice = new ExtTeleserviceCodeImpl();
                    ((ExtTeleserviceCodeImpl) this.extTeleservice).decodeData(ais, length);
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
            asnOs.writeTag(tagClass, true, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.extBearerService == null && this.extTeleservice == null)
            throw new MAPException("Both extBearerService and extTeleservice must not be null");
        if (this.extBearerService != null && this.extTeleservice != null)
            throw new MAPException("Both extBearerService and extTeleservice must not be not null");

        if (this.extBearerService != null) {
            ((ExtBearerServiceCodeImpl) this.extBearerService).encodeData(asnOs);
        } else {
            ((ExtTeleserviceCodeImpl) this.extTeleservice).encodeData(asnOs);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtBasicServiceCode [");

        if (this.extBearerService != null) {
            sb.append(this.extBearerService.toString());
        }
        if (this.extTeleservice != null) {
            sb.append(this.extTeleservice.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<ExtBasicServiceCodeImpl> EXT_BASIC_SERVICE_CODE_XML = new XMLFormat<ExtBasicServiceCodeImpl>(
            ExtBasicServiceCodeImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ExtBasicServiceCodeImpl extBasicServiceCode)
                throws XMLStreamException {
            extBasicServiceCode.extBearerService = xml.get(EXT_BEARER_SERVICE, ExtBearerServiceCodeImpl.class);
            extBasicServiceCode.extTeleservice = xml.get(EXT_TELE_SERVICE, ExtTeleserviceCodeImpl.class);

        }

        @Override
        public void write(ExtBasicServiceCodeImpl extBasicServiceCode, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            if (extBasicServiceCode.extBearerService != null) {
                xml.add(((ExtBearerServiceCodeImpl) extBasicServiceCode.extBearerService), EXT_BEARER_SERVICE,
                        ExtBearerServiceCodeImpl.class);
            }

            if (extBasicServiceCode.extTeleservice != null) {
                xml.add(((ExtTeleserviceCodeImpl) extBasicServiceCode.extTeleservice), EXT_TELE_SERVICE,
                        ExtTeleserviceCodeImpl.class);
            }
        }
    };
}
