/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.map.service.callhandling;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UUData;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UUI;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UUIndicator;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class UUDataImpl extends SequenceBase implements UUData {
    public static final int _ID_uuIndicator = 0;
    public static final int _ID_uuI = 1;
    public static final int _ID_uusCFInteraction = 2;
    public static final int _ID_extensionContainer = 3;

    private static final String UU_INDICATOR = "uuIndicator";
    private static final String UU_I = "uuI";
    private static final String UUS_CF_INTERACTION = "uusCFInteraction";
    private static final String EXTENSION_CONTAINER = "extensionContainer";

    private UUIndicator uuIndicator;
    private UUI uuI;
    private boolean uusCFInteraction;
    private MAPExtensionContainer extensionContainer;

    public UUDataImpl() {
        super("UUData");
    }

    public UUDataImpl(UUIndicator uuIndicator, UUI uuI, boolean uusCFInteraction, MAPExtensionContainer extensionContainer) {
        super("UUData");

        this.uuIndicator = uuIndicator;
        this.uuI = uuI;
        this.uusCFInteraction = uusCFInteraction;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public UUIndicator getUUIndicator() {
        return uuIndicator;
    }

    @Override
    public UUI getUUI() {
        return uuI;
    }

    @Override
    public boolean getUusCFInteraction() {
        return uusCFInteraction;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.uuIndicator = null;
        this.uuI = null;
        this.uusCFInteraction = false;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                case _ID_uuIndicator:
                    this.uuIndicator = new UUIndicatorImpl();
                    ((UUIndicatorImpl) this.uuIndicator).decodeAll(ais);
                    break;
                case _ID_uuI:
                    this.uuI = new UUIImpl();
                    ((UUIImpl) this.uuI).decodeAll(ais);
                    break;
                case _ID_uusCFInteraction:
                    ais.readNull();
                    this.uusCFInteraction = true;
                    break;
                case _ID_extensionContainer:
                    this.extensionContainer = new MAPExtensionContainerImpl();
                    ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
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
    public void encodeData(AsnOutputStream aos) throws MAPException {

        try {
            if (uuIndicator != null) {
                ((UUIndicatorImpl) this.uuIndicator).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_uuIndicator);
            }
            if (uuI != null) {
                ((UUIImpl) this.uuI).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_uuI);
            }
            if (uusCFInteraction) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_uusCFInteraction);
            }
            if (extensionContainer != null) {
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensionContainer);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<UUDataImpl> UU_DATA_XML = new XMLFormat<UUDataImpl>(UUDataImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, UUDataImpl uuData) throws XMLStreamException {
            uuData.uuIndicator = xml.get(UU_INDICATOR, UUIndicatorImpl.class);
            uuData.uuI = xml.get(UU_I, UUIImpl.class);
            Boolean bval = xml.get(UUS_CF_INTERACTION, Boolean.class);
            if (bval != null)
                uuData.uusCFInteraction = bval;
            uuData.extensionContainer = xml.get(EXTENSION_CONTAINER, MAPExtensionContainerImpl.class);
        }

        @Override
        public void write(UUDataImpl uuData, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (uuData.uuIndicator != null)
                xml.add((UUIndicatorImpl) uuData.uuIndicator, UU_INDICATOR, UUIndicatorImpl.class);
            if (uuData.uuI != null)
                xml.add((UUIImpl) uuData.uuI, UU_I, UUIImpl.class);
            if (uuData.uusCFInteraction)
                xml.add(uuData.uusCFInteraction, UUS_CF_INTERACTION, Boolean.class);
            if (uuData.extensionContainer != null)
                xml.add((MAPExtensionContainerImpl) uuData.extensionContainer, EXTENSION_CONTAINER, MAPExtensionContainerImpl.class);
        }
    };

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.uuIndicator != null) {
            sb.append("uuIndicator=");
            sb.append(uuIndicator);
            sb.append(", ");
        }
        if (this.uuI != null) {
            sb.append("uuI=");
            sb.append(uuI);
            sb.append(", ");
        }
        if (this.uusCFInteraction) {
            sb.append("uusCFInteraction");
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
