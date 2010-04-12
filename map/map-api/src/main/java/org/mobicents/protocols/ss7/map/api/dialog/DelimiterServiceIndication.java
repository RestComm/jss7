package org.mobicents.protocols.ss7.map.api.dialog;

import org.mobicents.protocols.ss7.map.api.DialogIndication;

/**
 * This sequence may not be present in some MAP dialogues. If it is present, it
 * ends with a MAP-DELIMITER primitive. If more than one user specific
 * service-primitive is included, all are to be included in the same Continue
 * message.
 * 
 * @author amit bhayani
 * 
 */
public interface DelimiterServiceIndication extends DialogIndication {

}
