package org.mobicents.protocols.ss7.m3ua.message.aspsm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;

/**
 * The ASP Up message is used to indicate to a remote M3UA peer that the
 * adaptation layer is ready to receive any ASPSM/ASPTM messages for all Routing
 * Keys that the ASP is configured to serve. Both ASP Identifier and INFO String
 * are optional
 * 
 * @author amit bhayani
 * 
 */
public interface ASPUp extends M3UAMessage {

    public ASPIdentifier getASPIdentifier();

    public void setASPIdentifier(ASPIdentifier aspId);

    public InfoString getInfoString();

    public void setInfoString(InfoString str);
}
