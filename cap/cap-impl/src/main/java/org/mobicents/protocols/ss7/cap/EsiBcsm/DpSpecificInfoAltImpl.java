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

package org.mobicents.protocols.ss7.cap.EsiBcsm;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.CollectedInfoSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.DpSpecificInfoAlt;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OServiceChangeSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TServiceChangeSpecificInfo;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
*
* @author sergey vetyutnev
*
*/
public class DpSpecificInfoAltImpl extends SequenceBase implements DpSpecificInfoAlt {

    private static final String O_SERVICE_CHANGE_SPECIFIC_INFO = "oServiceChangeSpecificInfo";
    private static final String COLLECTED_INFO_SPECIFIC_INFO = "collectedInfoSpecificInfo";
    private static final String T_SERVICE_CHANGE_SPECIFIC_INFO = "tServiceChangeSpecificInfo";

    public static final int _ID_oServiceChangeSpecificInfo = 0;
    public static final int _ID_tServiceChangeSpecificInfo = 1;
    public static final int _ID_collectedInfoSpecificInfo = 2;

    private OServiceChangeSpecificInfo oServiceChangeSpecificInfo;
    private CollectedInfoSpecificInfo collectedInfoSpecificInfo;
    private TServiceChangeSpecificInfo tServiceChangeSpecificInfo;

    public DpSpecificInfoAltImpl() {
        super("DpSpecificInfoAlt");
    }

    public DpSpecificInfoAltImpl(OServiceChangeSpecificInfo oServiceChangeSpecificInfo, CollectedInfoSpecificInfo collectedInfoSpecificInfo,
            TServiceChangeSpecificInfo tServiceChangeSpecificInfo) {
        super("DpSpecificInfoAlt");

        this.oServiceChangeSpecificInfo = oServiceChangeSpecificInfo;
        this.collectedInfoSpecificInfo = collectedInfoSpecificInfo;
        this.tServiceChangeSpecificInfo = tServiceChangeSpecificInfo;
    }

    @Override
    public OServiceChangeSpecificInfo getOServiceChangeSpecificInfo() {
        return oServiceChangeSpecificInfo;
    }

    @Override
    public CollectedInfoSpecificInfo getCollectedInfoSpecificInfo() {
        return collectedInfoSpecificInfo;
    }

    @Override
    public TServiceChangeSpecificInfo getTServiceChangeSpecificInfo() {
        return tServiceChangeSpecificInfo;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException, MAPParsingComponentException,
            INAPParsingComponentException {

        this.oServiceChangeSpecificInfo = null;
        this.collectedInfoSpecificInfo = null;
        this.tServiceChangeSpecificInfo = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                case _ID_oServiceChangeSpecificInfo:
                    this.oServiceChangeSpecificInfo = new OServiceChangeSpecificInfoImpl();
                    ((OServiceChangeSpecificInfoImpl) this.oServiceChangeSpecificInfo).decodeAll(ais);
                    break;
                case _ID_tServiceChangeSpecificInfo:
                    this.tServiceChangeSpecificInfo = new TServiceChangeSpecificInfoImpl();
                    ((TServiceChangeSpecificInfoImpl) this.tServiceChangeSpecificInfo).decodeAll(ais);
                    break;
                case _ID_collectedInfoSpecificInfo:
                    this.collectedInfoSpecificInfo = new CollectedInfoSpecificInfoImpl();
                    ((CollectedInfoSpecificInfoImpl) this.collectedInfoSpecificInfo).decodeAll(ais);
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
    public void encodeData(AsnOutputStream aos) throws CAPException {
        if (this.oServiceChangeSpecificInfo != null) {
            ((OServiceChangeSpecificInfoImpl) this.oServiceChangeSpecificInfo).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_oServiceChangeSpecificInfo);
        }
        if (this.tServiceChangeSpecificInfo != null) {
            ((TServiceChangeSpecificInfoImpl) this.tServiceChangeSpecificInfo).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_tServiceChangeSpecificInfo);
        }
        if (this.collectedInfoSpecificInfo != null) {
            ((CollectedInfoSpecificInfoImpl) this.collectedInfoSpecificInfo).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_collectedInfoSpecificInfo);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.oServiceChangeSpecificInfo != null) {
            sb.append("oServiceChangeSpecificInfo=");
            sb.append(oServiceChangeSpecificInfo);
            sb.append(", ");
        }
        if (this.tServiceChangeSpecificInfo != null) {
            sb.append("tServiceChangeSpecificInfo=");
            sb.append(tServiceChangeSpecificInfo);
            sb.append(", ");
        }
        if (this.collectedInfoSpecificInfo != null) {
            sb.append("collectedInfoSpecificInfo=");
            sb.append(collectedInfoSpecificInfo);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<DpSpecificInfoAltImpl> DP_SPECIFIC_INFO_ALT_XML = new XMLFormat<DpSpecificInfoAltImpl>(DpSpecificInfoAltImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, DpSpecificInfoAltImpl dpSpecificInfoAlt) throws XMLStreamException {
            dpSpecificInfoAlt.oServiceChangeSpecificInfo = xml.get(O_SERVICE_CHANGE_SPECIFIC_INFO, OServiceChangeSpecificInfoImpl.class);
            dpSpecificInfoAlt.collectedInfoSpecificInfo = xml.get(COLLECTED_INFO_SPECIFIC_INFO, CollectedInfoSpecificInfoImpl.class);
            dpSpecificInfoAlt.tServiceChangeSpecificInfo = xml.get(T_SERVICE_CHANGE_SPECIFIC_INFO, TServiceChangeSpecificInfoImpl.class);
        }

        @Override
        public void write(DpSpecificInfoAltImpl dpSpecificInfoAlt, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (dpSpecificInfoAlt.oServiceChangeSpecificInfo != null)
                xml.add((OServiceChangeSpecificInfoImpl) dpSpecificInfoAlt.oServiceChangeSpecificInfo, O_SERVICE_CHANGE_SPECIFIC_INFO,
                        OServiceChangeSpecificInfoImpl.class);
            if (dpSpecificInfoAlt.collectedInfoSpecificInfo != null)
                xml.add((CollectedInfoSpecificInfoImpl) dpSpecificInfoAlt.collectedInfoSpecificInfo, COLLECTED_INFO_SPECIFIC_INFO,
                        CollectedInfoSpecificInfoImpl.class);
            if (dpSpecificInfoAlt.tServiceChangeSpecificInfo != null)
                xml.add((TServiceChangeSpecificInfoImpl) dpSpecificInfoAlt.tServiceChangeSpecificInfo, T_SERVICE_CHANGE_SPECIFIC_INFO,
                        TServiceChangeSpecificInfoImpl.class);
        }
    };

}
