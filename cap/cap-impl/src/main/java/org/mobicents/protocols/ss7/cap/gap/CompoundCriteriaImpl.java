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
package org.mobicents.protocols.ss7.cap.gap;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.gap.BasicGapCriteria;
import org.mobicents.protocols.ss7.cap.api.gap.CompoundCriteria;
import org.mobicents.protocols.ss7.cap.api.primitives.ScfID;
import org.mobicents.protocols.ss7.cap.primitives.ScfIDImpl;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 */
public class CompoundCriteriaImpl extends SequenceBase implements CompoundCriteria {

    private static final int _ID_basicGapCriteria = 0;
    private static final int _ID_scfId = 1;

    private static final String BASIC_GAP_CRITERIA = "basicGapCriteria";
    private static final String SCF_ID = "scfId";

    private BasicGapCriteria basicGapCriteria;
    private ScfID scfId;

    public CompoundCriteriaImpl() {
        super("CompoundCriteria");
    }

    public CompoundCriteriaImpl(BasicGapCriteria basicGapCriteria, ScfID scfId) {
        super("CompoundCriteria");

        this.basicGapCriteria = basicGapCriteria;
        this.scfId = scfId;
    }

    public BasicGapCriteria getBasicGapCriteria() {
        return basicGapCriteria;
    }

    public ScfID getScfID() {
        return scfId;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.basicGapCriteria = null;
        this.scfId = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0) {
                break;
            }

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                switch (tag) {
                    case _ID_basicGapCriteria: {
                        int l = ais.readLength();
                        AsnInputStream ais2 = ais.readSequenceStreamData(l);
                        basicGapCriteria = new BasicGapCriteriaImpl();
                        ais2.readTag();
                        ((BasicGapCriteriaImpl) basicGapCriteria).decodeAll(ais2);
                        break;
                    }
                    case _ID_scfId: {
                        scfId = new ScfIDImpl();
                        ((ScfIDImpl) scfId).decodeAll(ais);
                        break;
                    }

                    default: {
                        ais.advanceElement();
                        break;
                    }
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.basicGapCriteria == null) {
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": parameter basicGapCriteria is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (this.basicGapCriteria == null) {
            throw new CAPException("Error while encoding " + _PrimitiveName + ": basicGapCriteria must not be null");
        }

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_basicGapCriteria);
            int pos = asnOs.StartContentDefiniteLength();
            ((BasicGapCriteriaImpl) this.basicGapCriteria).encodeAll(asnOs);
            asnOs.FinalizeContent(pos);

            if (scfId != null) {
                ((ScfIDImpl) scfId).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_scfId);
            }
        } catch (CAPException e) {
            throw new CAPException("CAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException ex) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + ex.getMessage(), ex);
        }
    }

    protected static final XMLFormat<CompoundCriteriaImpl> COMPOUND_CRITERIA_XML = new XMLFormat<CompoundCriteriaImpl>(CompoundCriteriaImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CompoundCriteriaImpl compoundCriteria) throws XMLStreamException {
            compoundCriteria.basicGapCriteria = xml.get(BASIC_GAP_CRITERIA, BasicGapCriteriaImpl.class);
            compoundCriteria.scfId = xml.get(SCF_ID, ScfIDImpl.class);
        }

        @Override
        public void write(CompoundCriteriaImpl compoundCriteria, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.add((BasicGapCriteriaImpl) compoundCriteria.basicGapCriteria, BASIC_GAP_CRITERIA, BasicGapCriteriaImpl.class);

            if (compoundCriteria.scfId != null) {
                xml.add((ScfIDImpl) compoundCriteria.scfId, SCF_ID, ScfIDImpl.class);
            }
        }
    };

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (basicGapCriteria != null) {
            sb.append("basicGapCriteria=");
            sb.append(basicGapCriteria);
        }

        if (scfId != null) {
            sb.append(", scfId=");
            sb.append(scfId);
        }

        sb.append("]");

        return sb.toString();
    }

}
