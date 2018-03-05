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
package org.restcomm.protocols.ss7.cap.service.sms.primitive;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.cap.EsiSms.OSmsFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiSms.OSmsSubmissionSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiSms.TSmsDeliverySpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiSms.TSmsFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.cap.api.EsiSms.OSmsFailureSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiSms.OSmsSubmissionSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiSms.TSmsDeliverySpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiSms.TSmsFailureSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMS;
import org.restcomm.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class EventSpecificInformationSMSImpl implements EventSpecificInformationSMS, CAPAsnPrimitive {

    public static final String _PrimitiveName = "EventSpecificInformationSMS";

    public static final int _ID_oSmsFailureSpecificInfo = 0;
    public static final int _ID_oSmsSubmissionSpecificInfo = 1;
    public static final int _ID_tSmsFailureSpecificInfo = 2;
    public static final int _ID_tSmsDeliverySpecificInfo = 3;

    private OSmsFailureSpecificInfo oSmsFailureSpecificInfo;
    private OSmsSubmissionSpecificInfo oSmsSubmissionSpecificInfo;
    private TSmsFailureSpecificInfo tSmsFailureSpecificInfo;
    private TSmsDeliverySpecificInfo tSmsDeliverySpecificInfo;

    public EventSpecificInformationSMSImpl() {
        super();
    }

    public EventSpecificInformationSMSImpl(OSmsFailureSpecificInfo oSmsFailureSpecificInfo) {
        super();
        this.oSmsFailureSpecificInfo = oSmsFailureSpecificInfo;
    }

    public EventSpecificInformationSMSImpl(OSmsSubmissionSpecificInfo oSmsSubmissionSpecificInfo) {
        super();
        this.oSmsSubmissionSpecificInfo = oSmsSubmissionSpecificInfo;
    }

    public EventSpecificInformationSMSImpl(TSmsFailureSpecificInfo tSmsFailureSpecificInfo) {
        super();
        this.tSmsFailureSpecificInfo = tSmsFailureSpecificInfo;
    }

    public EventSpecificInformationSMSImpl(TSmsDeliverySpecificInfo tSmsDeliverySpecificInfo) {
        super();
        this.tSmsDeliverySpecificInfo = tSmsDeliverySpecificInfo;
    }

    @Override
    public OSmsFailureSpecificInfo getOSmsFailureSpecificInfo() {
        return this.oSmsFailureSpecificInfo;
    }

    @Override
    public OSmsSubmissionSpecificInfo getOSmsSubmissionSpecificInfo() {
        return this.oSmsSubmissionSpecificInfo;
    }

    @Override
    public TSmsFailureSpecificInfo getTSmsFailureSpecificInfo() {
        return this.tSmsFailureSpecificInfo;
    }

    @Override
    public TSmsDeliverySpecificInfo getTSmsDeliverySpecificInfo() {
        return this.tSmsDeliverySpecificInfo;
    }

    @Override
    public int getTag() throws CAPException {
        if (oSmsFailureSpecificInfo != null) {
            return _ID_oSmsFailureSpecificInfo;
        } else if (oSmsSubmissionSpecificInfo != null) {
            return _ID_oSmsSubmissionSpecificInfo;
        } else if (tSmsFailureSpecificInfo != null) {
            return _ID_tSmsFailureSpecificInfo;
        } else {
            return _ID_tSmsDeliverySpecificInfo;
        }
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
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
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName
                    + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName
                    + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, IOException,
            AsnException, MAPParsingComponentException {

        this.oSmsFailureSpecificInfo = null;
        this.oSmsSubmissionSpecificInfo = null;
        this.tSmsFailureSpecificInfo = null;
        this.tSmsDeliverySpecificInfo = null;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
            case _ID_oSmsFailureSpecificInfo:
                if (ais.isTagPrimitive())
                    throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ".oSmsFailureSpecificInfo: Parameter is primitive",
                            CAPParsingComponentExceptionReason.MistypedParameter);
                this.oSmsFailureSpecificInfo = new OSmsFailureSpecificInfoImpl();
                ((OSmsFailureSpecificInfoImpl) this.oSmsFailureSpecificInfo).decodeData(ais, length);
                break;
            case _ID_oSmsSubmissionSpecificInfo:
                if (ais.isTagPrimitive())
                    throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ".oSmsSubmissionSpecificInfo: Parameter is primitive",
                            CAPParsingComponentExceptionReason.MistypedParameter);
                this.oSmsSubmissionSpecificInfo = new OSmsSubmissionSpecificInfoImpl();
                ((OSmsSubmissionSpecificInfoImpl) this.oSmsSubmissionSpecificInfo).decodeData(ais, length);
                break;
            case _ID_tSmsFailureSpecificInfo:
                if (ais.isTagPrimitive())
                    throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ".tSmsFailureSpecificInfo: Parameter is primitive",
                            CAPParsingComponentExceptionReason.MistypedParameter);
                this.tSmsFailureSpecificInfo = new TSmsFailureSpecificInfoImpl();
                ((TSmsFailureSpecificInfoImpl) this.tSmsFailureSpecificInfo).decodeData(ais, length);
                break;
            case _ID_tSmsDeliverySpecificInfo:
                if (ais.isTagPrimitive())
                    throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ".tSmsDeliverySpecificInfo: Parameter is primitive",
                            CAPParsingComponentExceptionReason.MistypedParameter);
                this.tSmsDeliverySpecificInfo = new TSmsDeliverySpecificInfoImpl();
                ((TSmsDeliverySpecificInfoImpl) this.tSmsDeliverySpecificInfo).decodeData(ais, length);
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

        if ((this.oSmsFailureSpecificInfo == null && this.oSmsSubmissionSpecificInfo == null
                && this.tSmsFailureSpecificInfo == null && this.tSmsDeliverySpecificInfo == null)
                || (this.oSmsFailureSpecificInfo != null && this.oSmsSubmissionSpecificInfo != null || this.tSmsFailureSpecificInfo != null
                        && this.tSmsDeliverySpecificInfo != null)) {
            throw new CAPException("Error while decoding " + _PrimitiveName
                    + ": One and only one choice must be selected");
        }

        try {
            if (this.oSmsFailureSpecificInfo != null) {
                ((OSmsFailureSpecificInfoImpl) this.oSmsFailureSpecificInfo).encodeData(asnOs);
            } else if (this.oSmsSubmissionSpecificInfo != null) {
                ((OSmsSubmissionSpecificInfoImpl) this.oSmsSubmissionSpecificInfo).encodeData(asnOs);
            } else if (this.tSmsFailureSpecificInfo != null) {
                ((TSmsFailureSpecificInfoImpl) this.tSmsFailureSpecificInfo).encodeData(asnOs);
            } else {
                ((TSmsDeliverySpecificInfoImpl) this.tSmsDeliverySpecificInfo).encodeData(asnOs);
            }
        } catch (CAPException e) {
            throw new CAPException("CAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.oSmsFailureSpecificInfo != null) {
            sb.append("oSmsFailureSpecificInfo=");
            sb.append(oSmsFailureSpecificInfo.toString());
        }
        if (this.oSmsSubmissionSpecificInfo != null) {
            sb.append(" oSmsSubmissionSpecificInfo=");
            sb.append(oSmsSubmissionSpecificInfo.toString());
        }
        if (this.tSmsFailureSpecificInfo != null) {
            sb.append(" tSmsFailureSpecificInfo=");
            sb.append(tSmsFailureSpecificInfo.toString());
        }
        if (this.tSmsDeliverySpecificInfo != null) {
            sb.append(" tSmsDeliverySpecificInfo=");
            sb.append(tSmsDeliverySpecificInfo.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
