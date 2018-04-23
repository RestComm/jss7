package org.restcomm.protocols.ss7.map.service.sms;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.service.sms.CorrelationID;
import org.restcomm.protocols.ss7.map.api.service.sms.SipUri;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.SequenceBase;

/**
*
* @author kostiantyn nosach
*
*/

public class CorrelationIDImpl extends SequenceBase implements CorrelationID {

    public static final String PRIMITIVE_NAME = "CorrelationID";

    private static final int _TAG_HlrId = 0;
    private static final int _TAG_SipUriA = 1;
    private static final int _TAG_SipUriB = 2;

    private IMSI hlrId;
    private SipUri sipUriA;
    private SipUri sipUriB;

    public CorrelationIDImpl() {
        super(PRIMITIVE_NAME);
    }

    public CorrelationIDImpl(IMSI hlrId, SipUri sipUriA, SipUri sipUriB) {
        this();
        this.hlrId = hlrId;
        this.sipUriA = sipUriA;
        this.sipUriB = sipUriB;
    }

    @Override
    public IMSI getHlrId() {
        return hlrId;
    }

    @Override
    public SipUri getSipUriA() {
        return sipUriA;
    }

    @Override
    public SipUri getSipUriB() {
        return sipUriB;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.hlrId = null;
        this.sipUriA = null;
        this.sipUriB = null;
        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case _TAG_HlrId:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".hlr-id: is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.hlrId = new IMSIImpl();
                            ((IMSIImpl) this.hlrId).decodeAll(ais);
                            break;

                        case _TAG_SipUriA:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".sipUriA: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.sipUriA = new SipUriImpl();
                            ((SipUriImpl) this.sipUriA).decodeAll(ais);
                            break;

                        case _TAG_SipUriB:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".sipUriB: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.sipUriB = new SipUriImpl();
                            ((SipUriImpl) this.sipUriB).decodeAll(ais);
                            break;

                        default:
                            ais.advanceElement();
                            break;
                    }
                    break;

                default:
                    ais.advanceElement();
                    break;
            }
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.hlrId != null)
            ((IMSIImpl) this.hlrId).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_HlrId);

        if (this.sipUriA != null)
            ((SipUriImpl) this.sipUriA).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_SipUriA);

        if (this.sipUriB != null)
            ((SipUriImpl) this.sipUriB).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_SipUriB);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if(this.hlrId!=null) {
            sb.append("hlrId=");
            sb.append(this.hlrId.toString());
            sb.append(", ");
        }
        if(this.sipUriA!=null) {
            sb.append("sipUriA=");
            sb.append(this.sipUriA.toString());
            sb.append(", ");
        }
        if (this.sipUriB != null) {
            sb.append(", sipUriB=");
            sb.append(this.sipUriB.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
