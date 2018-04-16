package org.restcomm.protocols.ss7.map.api.service.sms;

import java.io.Serializable;

/**
<code>
SIP-URI ::= OCTET STRING
-- octets are coded as defined in IETF RFC 3261
</code>
 *
 * @author kostiantyn nosach
 */

public interface SipUri extends Serializable {

    byte[] getData();

}
