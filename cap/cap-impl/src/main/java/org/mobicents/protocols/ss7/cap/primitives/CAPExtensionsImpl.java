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

package org.mobicents.protocols.ss7.cap.primitives;

import java.io.IOException;
import java.util.ArrayList;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.ExtensionField;
import org.mobicents.protocols.ss7.map.primitives.ArrayListSerializingBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAPExtensionsImpl implements CAPExtensions, CAPAsnPrimitive {

    public static final String _PrimitiveName = "CAPExtensions";

    private static final String EXTENSION_FIELD = "extensionField";
    private static final String EXTENSION_FIELD_LIST = "extensionFieldList";

    private ArrayList<ExtensionField> extensionFields;

    public CAPExtensionsImpl() {
    }

    public CAPExtensionsImpl(ArrayList<ExtensionField> fieldsList) {
        this.extensionFields = fieldsList;
    }

    @Override
    public ArrayList<ExtensionField> getExtensionFields() {
        return extensionFields;
    }

    @Override
    public void setExtensionFields(ArrayList<ExtensionField> fieldsList) {
        this.extensionFields = fieldsList;
    }

    @Override
    public int getTag() throws CAPException {
        return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
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
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.extensionFields = null;

        ArrayList<ExtensionField> res = new ArrayList<ExtensionField>();
        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || tag != Tag.SEQUENCE || ais.isTagPrimitive())
                throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                        + ": Bad ExtensionField tag or tag class or is primitive",
                        CAPParsingComponentExceptionReason.MistypedParameter);

            ExtensionFieldImpl elem = new ExtensionFieldImpl();
            elem.decodeAll(ais);
            res.add(elem);
        }

        this.extensionFields = res;
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws CAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {

        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (this.extensionFields == null)
            throw new CAPException("Error while decoding " + _PrimitiveName + ": extensionFields field must not be null");
        if (this.extensionFields.size() < 1 || this.extensionFields.size() > 10)
            throw new CAPException("Error while decoding " + _PrimitiveName
                    + ": extensionFields field length must be from 1 to 10");

        for (ExtensionField fld : this.extensionFields) {
            ((ExtensionFieldImpl) fld).encodeAll(asnOs);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.extensionFields != null) {
            boolean isFirst = true;
            for (ExtensionField fld : this.extensionFields) {
                if (isFirst)
                    isFirst = false;
                else
                    sb.append("\n");
                sb.append(fld.toString());
            }
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CAPExtensionsImpl> CAP_Extensions_XML = new XMLFormat<CAPExtensionsImpl>(
            CAPExtensionsImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CAPExtensionsImpl capExtensions) throws XMLStreamException {
            CAPExtensions_ExtensionFields al = xml.get(EXTENSION_FIELD_LIST, CAPExtensions_ExtensionFields.class);
            if (al != null) {
                capExtensions.extensionFields = al.getData();
            }
        }

        @Override
        public void write(CAPExtensionsImpl capExtensions, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            if (capExtensions.extensionFields == null || capExtensions.extensionFields.size() == 0)
                return;

            if (capExtensions.extensionFields != null) {
                CAPExtensions_ExtensionFields al = new CAPExtensions_ExtensionFields(capExtensions.extensionFields);
                xml.add(al, EXTENSION_FIELD_LIST, CAPExtensions_ExtensionFields.class);
            }
        }
    };

    public static class CAPExtensions_ExtensionFields extends ArrayListSerializingBase<ExtensionField> {

        public CAPExtensions_ExtensionFields() {
            super(EXTENSION_FIELD, ExtensionFieldImpl.class);
        }

        public CAPExtensions_ExtensionFields(ArrayList<ExtensionField> data) {
            super(EXTENSION_FIELD, ExtensionFieldImpl.class, data);
        }

    }
}
