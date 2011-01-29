package org.mobicents.protocols.ss7.m3ua.message.aspsm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;

/**
 * The ASP Down message is used to indicate to a remote M3UA peer that the
 * adaptation layer is NOT ready to receive DATA, SSNM, RKM, or ASPTM messages.
 * 
 * @author amit bhayani
 * 
 */
public interface ASPDown extends M3UAMessage {

    public InfoString getInfoString();

    public void setInfoString(InfoString str);

}
