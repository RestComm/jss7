package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.service.sms.SipUri;
import org.restcomm.protocols.ss7.map.primitives.OctetStringBase;

/**
*
* @author kostiantyn nosach
*
*/

public class SipUriImpl extends OctetStringBase implements SipUri {

    @Override
    public byte[] getData() {
        return data;
    }

    public SipUriImpl() {
        super(1, 136, "SIP-URI");
    }

    public SipUriImpl(byte[] data) {
        super(1, 136, "SIP-URI", data);
    }
}
