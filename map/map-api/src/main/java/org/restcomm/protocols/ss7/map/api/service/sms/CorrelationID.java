package org.restcomm.protocols.ss7.map.api.service.sms;

import java.io.Serializable;

import org.restcomm.protocols.ss7.map.api.primitives.IMSI;

/**
<code>
CorrelationID ::= SEQUENCE
{
    hlr-id [0] HLR-Id OPTIONAL,
    sip-uri-A [1] SIP-URI OPTIONAL,
    sip-uri-B [2] SIP-URI OPTIONAL
}

HLR-Id ::= IMSI
SIP-URI ::= OCTET STRING
-- octets are coded as defined in IETF RFC 3261
</code>
 *
 * @author kostiantyn nosach
 */

public interface CorrelationID extends Serializable {

    IMSI getHlrId();

    SipUri getSipUriA();

    SipUri getSipUriB();
}
