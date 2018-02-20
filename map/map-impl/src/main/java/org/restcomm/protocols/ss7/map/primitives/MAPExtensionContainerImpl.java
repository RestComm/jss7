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

package org.restcomm.protocols.ss7.map.primitives;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.ByteArrayContainer;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.MAPPrivateExtension;

/**
 * @author sergey vetyutnev
 *
 */
public class MAPExtensionContainerImpl extends SequenceBase implements MAPExtensionContainer {

    protected static final int PRIVATEEXTENSIONLIST_REF_TAG = 0x00;
    protected static final int PSCEXTENSIONS_REF_TAG = 0x01;

    private static final String PRIVATE_EXTENSION = "privateExtension";
    private static final String PRIVATE_EXTENSION_LIST = "privateExtensionList";
    private static final String PCS_EXTENSIONS = "pcsExtensions";

    private ArrayList<MAPPrivateExtension> privateExtensionList;
    private byte[] pcsExtensions;

    public MAPExtensionContainerImpl() {
        super("MAPExtensionContainer");
    }

    public MAPExtensionContainerImpl(ArrayList<MAPPrivateExtension> privateExtensionList, byte[] pcsExtensions) {
        super("MAPExtensionContainer");

        this.privateExtensionList = privateExtensionList;
        this.pcsExtensions = pcsExtensions;
    }

    public ArrayList<MAPPrivateExtension> getPrivateExtensionList() {
        return this.privateExtensionList;
    }

    public void setPrivateExtensionList(ArrayList<MAPPrivateExtension> privateExtensionList) {
        this.privateExtensionList = privateExtensionList;
    }

    public byte[] getPcsExtensions() {
        return this.pcsExtensions;
    }

    public void setPcsExtensions(byte[] pcsExtensions) {
        this.pcsExtensions = pcsExtensions;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.privateExtensionList = null;
        this.pcsExtensions = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();
            if (tag == MAPExtensionContainerImpl.PRIVATEEXTENSIONLIST_REF_TAG
                    && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                if (ais.isTagPrimitive())
                    throw new MAPParsingComponentException(
                            "Error while ExtensionContainer decoding: privateExtensionList is primitive",
                            MAPParsingComponentExceptionReason.MistypedParameter);
                if (this.privateExtensionList != null)
                    throw new MAPParsingComponentException(
                            "Error while ExtensionContainer decoding: More than one PrivateExtensionList has found",
                            MAPParsingComponentExceptionReason.MistypedParameter);

                AsnInputStream localAis2 = ais.readSequenceStream();
                this.privateExtensionList = new ArrayList<MAPPrivateExtension>();
                while (localAis2.available() > 0) {
                    tag = localAis2.readTag();
                    if (tag != Tag.SEQUENCE || localAis2.getTagClass() != Tag.CLASS_UNIVERSAL || localAis2.isTagPrimitive())
                        throw new MAPParsingComponentException(
                                "Error while ExtensionContainer decoding: Bad tag, tagClass or primitiveFactor of PrivateExtension",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    if (this.privateExtensionList.size() >= 10)
                        throw new MAPParsingComponentException(
                                "More then 10 PrivateExtension found when PrivateExtensionList decoding",
                                MAPParsingComponentExceptionReason.MistypedParameter);

                    MAPPrivateExtensionImpl privateExtension = new MAPPrivateExtensionImpl();
                    privateExtension.decodeAll(localAis2);
                    this.privateExtensionList.add(privateExtension);
                }
            } else if (tag == MAPExtensionContainerImpl.PSCEXTENSIONS_REF_TAG
                    && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                if (ais.isTagPrimitive())
                    throw new MAPParsingComponentException("Error while PCS-Extensions decoding: PCS-Extensions is primitive",
                            MAPParsingComponentExceptionReason.MistypedParameter);
                if (this.pcsExtensions != null)
                    throw new MAPParsingComponentException(
                            "Error while PCS-Extensions decoding: More than one PCS-Extensions has found",
                            MAPParsingComponentExceptionReason.MistypedParameter);

                this.pcsExtensions = ais.readSequence();
            }
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.privateExtensionList == null && this.pcsExtensions == null)
            throw new MAPException(
                    "Error when encoding ExtensionContainer: Both PrivateExtensionList and PcsExtensions are empty when ExtensionContainer encoding");
        if (this.privateExtensionList != null
                && (this.privateExtensionList.size() == 0 || this.privateExtensionList.size() > 10))
            throw new MAPException(
                    "Error when encoding ExtensionContainer: PrivateExtensionList must contains from 1 to 10 elements when ExtensionContainer encoding");

        try {

            if (this.privateExtensionList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, PRIVATEEXTENSIONLIST_REF_TAG);
                int pos2 = asnOs.StartContentDefiniteLength();

                for (MAPPrivateExtension pe : this.privateExtensionList) {
                    ((MAPPrivateExtensionImpl) pe).encodeAll(asnOs);
                }

                asnOs.FinalizeContent(pos2);
            }

            if (this.pcsExtensions != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, PSCEXTENSIONS_REF_TAG);
                int pos2 = asnOs.StartContentDefiniteLength();

                asnOs.write(this.pcsExtensions);

                asnOs.FinalizeContent(pos2);
            }
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.privateExtensionList != null && this.privateExtensionList.size() > 0) {
            for (MAPPrivateExtension pe : this.privateExtensionList) {
                sb.append("\n");
                sb.append(pe.toString());
            }
        }

        if (this.pcsExtensions != null) {
            sb.append("\nPcsExtensions=");
            sb.append(this.ArrayToString(this.pcsExtensions));
        }

        sb.append("]");

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(pcsExtensions);
        result = prime * result + ((privateExtensionList == null) ? 0 : privateExtensionList.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MAPExtensionContainerImpl other = (MAPExtensionContainerImpl) obj;
        if (!Arrays.equals(pcsExtensions, other.pcsExtensions))
            return false;
        if (privateExtensionList == null) {
            if (other.privateExtensionList != null)
                return false;
        } else if (!privateExtensionList.equals(other.privateExtensionList))
            return false;
        return true;
    }

    private String ArrayToString(byte[] array) {
        StringBuilder sb = new StringBuilder();
        int i1 = 0;
        for (byte b : array) {
            if (i1 == 0)
                i1 = 1;
            else
                sb.append(", ");
            sb.append(b);
        }
        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<MAPExtensionContainerImpl> MAP_EXTENSION_CONTAINER_XML = new XMLFormat<MAPExtensionContainerImpl>(
            MAPExtensionContainerImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MAPExtensionContainerImpl mapExtensionContainer)
                throws XMLStreamException {
            MAPExtensionContainer_privateExtensionList al = xml.get(PRIVATE_EXTENSION_LIST,
                    MAPExtensionContainer_privateExtensionList.class);
            if (al != null) {
                mapExtensionContainer.privateExtensionList = al.getData();
            }

            ByteArrayContainer bc = xml.get(PCS_EXTENSIONS, ByteArrayContainer.class);
            if (bc != null) {
                mapExtensionContainer.pcsExtensions = bc.getData();
            }
        }

        @Override
        public void write(MAPExtensionContainerImpl mapExtensionContainer, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            if (mapExtensionContainer.privateExtensionList != null) {
                MAPExtensionContainer_privateExtensionList al = new MAPExtensionContainer_privateExtensionList(
                        mapExtensionContainer.privateExtensionList);
                xml.add(al, PRIVATE_EXTENSION_LIST, MAPExtensionContainer_privateExtensionList.class);
            }

            if (mapExtensionContainer.pcsExtensions != null) {
                ByteArrayContainer bac = new ByteArrayContainer(mapExtensionContainer.pcsExtensions);
                xml.add(bac, PCS_EXTENSIONS, ByteArrayContainer.class);
            }
        }
    };

    public static class MAPExtensionContainer_privateExtensionList extends ArrayListSerializingBase<MAPPrivateExtension> {

        public MAPExtensionContainer_privateExtensionList() {
            super(PRIVATE_EXTENSION, MAPPrivateExtensionImpl.class);
        }

        public MAPExtensionContainer_privateExtensionList(ArrayList<MAPPrivateExtension> data) {
            super(PRIVATE_EXTENSION, MAPPrivateExtensionImpl.class, data);
        }

    }
}
