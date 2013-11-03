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

package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 *
 *
 * @author amit bhayani
 *
 */
public class SubscriberLocationReportResponseImpl extends LsmMessageImpl implements SubscriberLocationReportResponse {

    private static final int _TAG_NA_ESRK = 0;
    private static final int _TAG_NA_ESRD = 1;

    public static final String _PrimitiveName = "SubscriberLocationReportResponse";

    private ISDNAddressString naEsrd;
    private ISDNAddressString naEsrk;
    private MAPExtensionContainer extensionContainer;

    /**
     *
     */
    public SubscriberLocationReportResponseImpl() {
        super();
    }

    /**
     * @param naEsrd
     * @param naEsrk
     * @param extensionContainer
     */
    public SubscriberLocationReportResponseImpl(ISDNAddressString naEsrd, ISDNAddressString naEsrk,
            MAPExtensionContainer extensionContainer) {
        super();
        this.naEsrd = naEsrd;
        this.naEsrk = naEsrk;
        this.extensionContainer = extensionContainer;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.subscriberLocationReport_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.subscriberLocationReport;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. SubscriberLocationReportResponseIndication#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. SubscriberLocationReportResponseIndication#getNaESRK()
     */
    public ISDNAddressString getNaESRK() {
        return this.naEsrk;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. SubscriberLocationReportResponseIndication#getNaESRD()
     */
    public ISDNAddressString getNaESRD() {
        return this.naEsrd;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
     */
    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass ()
     */
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive ()
     */
    public boolean getIsPrimitive() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeAll
     * (org.mobicents.protocols.asn.AsnInputStream)
     */
    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeData
     * (org.mobicents.protocols.asn.AsnInputStream, int)
     */
    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.naEsrd = null;
        this.naEsrk = null;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                switch (tag) {
                    case Tag.SEQUENCE:
                        // ExtensionContainer
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter extensionContainer is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }// switch
            } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_NA_ESRK:
                        // na-ESRK [0] ISDN-AddressString OPTIONAL
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [na-ESRK [0] ISDN-AddressString] is not Sequence",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.naEsrk = new ISDNAddressStringImpl();
                        ((ISDNAddressStringImpl) this.naEsrk).decodeAll(ais);
                        break;
                    case _TAG_NA_ESRD:
                        // na-ESRD [1] ISDN-AddressString OPTIONAL,
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [na-ESRD [1] ISDN-AddressString] is not Sequence",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.naEsrd = new ISDNAddressStringImpl();
                        ((ISDNAddressStringImpl) this.naEsrd).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }// while
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
     * (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
     * (org.mobicents.protocols.asn.AsnOutputStream, int, int)
     */
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
     * (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.extensionContainer != null) {
            // extensionContainer ExtensionContainer OPTIONAL
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
        }

        if (this.naEsrk != null) {
            // na-ESRK [0] ISDN-AddressString OPTIONAL
            ((ISDNAddressStringImpl) this.naEsrk).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_NA_ESRK);
        }

        if (this.naEsrd != null) {
            // na-ESRD [1] ISDN-AddressString OPTIONAL ,
            ((ISDNAddressStringImpl) this.naEsrd).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_NA_ESRD);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.extensionContainer != null) {
            sb.append("extensionContainer");
            sb.append(this.extensionContainer);
        }
        if (this.naEsrd != null) {
            sb.append(", naEsrd=");
            sb.append(this.naEsrd);
        }
        if (this.naEsrk != null) {
            sb.append(", naEsrk=");
            sb.append(this.naEsrk);
        }

        sb.append("]");

        return sb.toString();
    }
}
