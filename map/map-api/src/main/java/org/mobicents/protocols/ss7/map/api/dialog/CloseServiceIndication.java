package org.mobicents.protocols.ss7.map.api.dialog;

import org.mobicents.protocols.ss7.map.api.DialogIndication;

/**
 * The sequence can only appear after an opening sequence or a continuing
 * sequence. The sequence may contain none, one or several user specific
 * service-primitives if the MAP-CLOSE primitive specifies normal release. If no
 * user specific service-primitive is included, then this will correspond to
 * sending an empty End message in TC. If more than one user specific
 * service-primitive is included, all are to be sent in the same End message. If
 * pre-arranged end is specified, the sequence cannot contain any user specific
 * service-primitive. The MAP-CLOSE primitive must be sent after all user
 * specific service-primitives have been delivered to the MAP service-provider.
 * 
 * @author amit bhayani
 * 
 */
public interface CloseServiceIndication extends DialogIndication {

}
