package org.mobicents.protocols.ss7.tcap.asn;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * Marker interface.
 * @author baranowb
 *
 */
public interface Encodable {

	
	public void encode(AsnOutputStream aos) throws ParseException;
	public void decode(AsnInputStream ais) throws ParseException;
	
	
}
