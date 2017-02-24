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
import org.mobicents.protocols.ss7.cap.api.gap.CalledAddressAndService;
import org.mobicents.protocols.ss7.cap.api.gap.CallingAddressAndService;
import org.mobicents.protocols.ss7.cap.api.gap.GapOnService;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.isup.DigitsImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 */
public class BasicGapCriteriaImpl implements BasicGapCriteria, CAPAsnPrimitive {

    public static final int _ID_calledAddressValue = 0;
    public static final int _ID_gapOnService = 2;
    public static final int _ID_calledAddressAndService = 29;
    public static final int _ID_callingAddressAndService = 30;

    private static final String CALLED_ADDRESS_VALUE = "calledAddressValue";
    private static final String GAP_ON_SERVICE = "gapOnService";
    private static final String CALLED_ADDRESS_AND_SERVICE = "calledAddressAndService";
    private static final String CALLIING_ADDRESS_AND_SERVICE = "callingAddressAndService";

    public static final String _PrimitiveName = "BasicGapCriteria";

    private Digits calledAddressValue;
    private GapOnService gapOnService;
    private CalledAddressAndService calledAddressAndService;
    private CallingAddressAndService callingAddressAndService;

    public BasicGapCriteriaImpl() {
    }

    public BasicGapCriteriaImpl(Digits calledAddressValue) {
        this.calledAddressValue = calledAddressValue;
    }

    public BasicGapCriteriaImpl(GapOnService gapOnService) {
        this.gapOnService = gapOnService;
    }

    public BasicGapCriteriaImpl(CalledAddressAndService calledAddressAndService) {
        this.calledAddressAndService = calledAddressAndService;
    }

    public BasicGapCriteriaImpl(CallingAddressAndService callingAddressAndService) {
        this.callingAddressAndService = callingAddressAndService;
    }

    public Digits getCalledAddressValue() {
        return calledAddressValue;
    }

    public GapOnService getGapOnService() {
        return gapOnService;
    }

    public CalledAddressAndService getCalledAddressAndService() {
        return calledAddressAndService;
    }

    public CallingAddressAndService getCallingAddressAndService() {
        return callingAddressAndService;
    }

    public int getTag() throws CAPException {
        if (calledAddressValue != null) {
            return _ID_calledAddressValue;
        } else if (gapOnService != null) {
            return _ID_gapOnService;
        } else if (calledAddressAndService != null) {
            return _ID_calledAddressAndService;
        } else if (callingAddressAndService != null) {
            return _ID_callingAddressAndService;
        }

        throw new CAPException("Error while encoding " + _PrimitiveName + ": no choice is specified");
    }

    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    public boolean getIsPrimitive() {
        if (calledAddressValue != null)
            return true;
        else
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

    private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.calledAddressValue = null;
        this.gapOnService = null;
        this.calledAddressAndService = null;
        this.callingAddressAndService = null;

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

            switch (ais.getTag()) {
                case _ID_calledAddressValue: {
                    this.calledAddressValue = new DigitsImpl();
                    ((DigitsImpl) this.calledAddressValue).decodeData(ais, length);
                    this.calledAddressValue.setIsGenericNumber();
                    break;
                }
                case _ID_gapOnService: {
                    this.gapOnService = new GapOnServiceImpl();
                    ((GapOnServiceImpl) this.gapOnService).decodeData(ais, length);
                    break;
                }
                case _ID_calledAddressAndService: {
                    this.calledAddressAndService = new CalledAddressAndServiceImpl();
                    ((CalledAddressAndServiceImpl) this.calledAddressAndService).decodeData(ais, length);
                    break;
                }
                case _ID_callingAddressAndService: {
                    this.callingAddressAndService = new CallingAddressAndServiceImpl();
                    ((CallingAddressAndServiceImpl) this.callingAddressAndService).decodeData(ais, length);
                    break;
                }
                default: {
                    throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tag",
                            CAPParsingComponentExceptionReason.MistypedParameter);
                }
            }
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

        int cnt = 0;
        if (this.calledAddressValue != null)
            cnt++;
        if (this.gapOnService != null)
            cnt++;
        if (this.calledAddressAndService != null)
            cnt++;
        if (this.callingAddressAndService != null)
            cnt++;
        if (cnt != 1) {
            throw new CAPException("Error while encoding " + _PrimitiveName + ": One and only one choice must be selected");
        }

        try {
            if (calledAddressValue != null) {
                ((DigitsImpl) calledAddressValue).encodeData(asnOs);

            } else if (gapOnService != null) {
                ((GapOnServiceImpl) gapOnService).encodeData(asnOs);

            } else if (calledAddressAndService != null) {
                ((CalledAddressAndServiceImpl) calledAddressAndService).encodeData(asnOs);

            } else if (callingAddressAndService != null) {
                ((CallingAddressAndServiceImpl) callingAddressAndService).encodeData(asnOs);
            }
        } catch (CAPException e) {
            throw new CAPException("CAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    protected static final XMLFormat<BasicGapCriteriaImpl> BASIC_GAP_CRITERIA_XML = new XMLFormat<BasicGapCriteriaImpl>(BasicGapCriteriaImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, BasicGapCriteriaImpl basicGapCriteria) throws XMLStreamException {
            basicGapCriteria.calledAddressValue = xml.get(CALLED_ADDRESS_VALUE, DigitsImpl.class);
            basicGapCriteria.gapOnService = xml.get(GAP_ON_SERVICE, GapOnServiceImpl.class);
            basicGapCriteria.calledAddressAndService = xml.get(CALLED_ADDRESS_AND_SERVICE, CalledAddressAndServiceImpl.class);
            basicGapCriteria.callingAddressAndService = xml.get(CALLIING_ADDRESS_AND_SERVICE, CallingAddressAndServiceImpl.class);
        }

        @Override
        public void write(BasicGapCriteriaImpl basicGapCriteria, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (basicGapCriteria.calledAddressValue != null) {
                xml.add((DigitsImpl) basicGapCriteria.calledAddressValue, CALLED_ADDRESS_VALUE, DigitsImpl.class);
            }
            if (basicGapCriteria.gapOnService != null) {
                xml.add((GapOnServiceImpl) basicGapCriteria.gapOnService, GAP_ON_SERVICE, GapOnServiceImpl.class);
            }
            if (basicGapCriteria.calledAddressAndService != null) {
                xml.add((CalledAddressAndServiceImpl) basicGapCriteria.calledAddressAndService, CALLED_ADDRESS_AND_SERVICE, CalledAddressAndServiceImpl.class);
            }
            if (basicGapCriteria.callingAddressAndService != null) {
                xml.add((CallingAddressAndServiceImpl) basicGapCriteria.callingAddressAndService, CALLIING_ADDRESS_AND_SERVICE, CallingAddressAndServiceImpl.class);
            }
        }
    };

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (calledAddressValue != null) {
            sb.append("calledAddressValue=");
            sb.append(calledAddressValue);
        } else if (gapOnService != null) {
            sb.append("gapOnService=");
            sb.append(gapOnService);
        } else if (calledAddressAndService != null) {
            sb.append("calledAddressAndService=");
            sb.append(calledAddressAndService);
        } else if (callingAddressAndService != null) {
            sb.append("callingAddressAndService=");
            sb.append(callingAddressAndService);
        }

        sb.append("]");

        return sb.toString();
    }

}
