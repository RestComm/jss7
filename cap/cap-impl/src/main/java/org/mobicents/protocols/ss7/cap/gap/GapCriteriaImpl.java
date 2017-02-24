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
import org.mobicents.protocols.ss7.cap.api.gap.GapCriteria;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 */
public class GapCriteriaImpl implements GapCriteria, CAPAsnPrimitive {

    private static final String BASIC_GAP_CRITERIA = "basicGapCriteria";
    private static final String COMPOUND_CRITERIA = "compoundCriteria";

    public static final String _PrimitiveName = "GapCriteria";

    private BasicGapCriteria basicGapCriteria;
    private CompoundCriteria compoundCriteria;

    public GapCriteriaImpl() {
    }

    public GapCriteriaImpl(BasicGapCriteria basicGapCriteria) {
        this.basicGapCriteria = basicGapCriteria;
    }

    public GapCriteriaImpl(CompoundCriteria compoundCriteria) {
        this.compoundCriteria = compoundCriteria;
    }

    public BasicGapCriteria getBasicGapCriteria() {
        return basicGapCriteria;
    }

    public CompoundCriteria getCompoundGapCriteria() {
        return compoundCriteria;
    }

    public int getTag() throws CAPException {
        if (basicGapCriteria != null) {
            return ((BasicGapCriteriaImpl) basicGapCriteria).getTag();
        } else if (compoundCriteria != null) {
            return ((CompoundCriteriaImpl) compoundCriteria).getTag();
        }

        throw new CAPException("Error while encoding " + _PrimitiveName + ": no choice is specified");
    }

    public int getTagClass() {
        if (basicGapCriteria != null) {
            return ((BasicGapCriteriaImpl) basicGapCriteria).getTagClass();
        } else if (compoundCriteria != null) {
            return ((CompoundCriteriaImpl) compoundCriteria).getTagClass();
        }
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    public boolean getIsPrimitive() {
        if (basicGapCriteria != null) {
            return ((BasicGapCriteriaImpl) basicGapCriteria).getIsPrimitive();
        } else if (compoundCriteria != null) {
            return ((CompoundCriteriaImpl) compoundCriteria).getIsPrimitive();
        }
        return false;
    }

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
        this.basicGapCriteria = null;
        this.compoundCriteria = null;

        if (ansIS.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            basicGapCriteria = new BasicGapCriteriaImpl();
            ((BasicGapCriteriaImpl) basicGapCriteria).decodeData(ansIS, length);
        } else if (ansIS.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.compoundCriteria = new CompoundCriteriaImpl();
            ((CompoundCriteriaImpl) compoundCriteria).decodeData(ansIS, length);
        } else {
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagClass",
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }

    }

    public void encodeAll(AsnOutputStream asnOs) throws CAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

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

    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if ((this.basicGapCriteria == null && this.compoundCriteria == null) || (this.basicGapCriteria != null && this.compoundCriteria != null)) {
            throw new CAPException("Error while decoding " + _PrimitiveName + ": One and only one choice must be selected");
        }

        try {
            if (basicGapCriteria != null) {
                ((BasicGapCriteriaImpl) basicGapCriteria).encodeData(asnOs);

            } else if (compoundCriteria != null) {
                ((CompoundCriteriaImpl) compoundCriteria).encodeData(asnOs);
            }
        } catch (CAPException e) {
            throw new CAPException("CAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    protected static final XMLFormat<GapCriteriaImpl> GAP_CRITERIA_XML = new XMLFormat<GapCriteriaImpl>(GapCriteriaImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, GapCriteriaImpl gapCriteria) throws XMLStreamException {
            gapCriteria.basicGapCriteria = xml.get(BASIC_GAP_CRITERIA, BasicGapCriteriaImpl.class);
            gapCriteria.compoundCriteria = xml.get(COMPOUND_CRITERIA, CompoundCriteriaImpl.class);
        }

        @Override
        public void write(GapCriteriaImpl gapCriteria, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (gapCriteria.basicGapCriteria != null) {
                xml.add((BasicGapCriteriaImpl) gapCriteria.basicGapCriteria, BASIC_GAP_CRITERIA, BasicGapCriteriaImpl.class);
            }
            if (gapCriteria.compoundCriteria != null) {
                xml.add((CompoundCriteriaImpl) gapCriteria.compoundCriteria, COMPOUND_CRITERIA, CompoundCriteriaImpl.class);
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
        } else if (compoundCriteria != null) {
            sb.append("compoundCriteria=");
            sb.append(compoundCriteria);
        }

        sb.append("]");

        return sb.toString();
    }

}
