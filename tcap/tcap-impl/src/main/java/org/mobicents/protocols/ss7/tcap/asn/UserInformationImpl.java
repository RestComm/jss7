/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.External;

/**
 * @author baranowb
 *
 */
public class UserInformationImpl extends External implements UserInformation {
	//I have no idea how to hack this... 
	

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.asn.External#decode(org.mobicents.protocols.asn.AsnInputStream)
	 */
	@Override
	public void decode(AsnInputStream ais) throws ParseException {
		// TODO Auto-generated method stub
		//super.decode(ais);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.asn.External#encode(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encode(AsnOutputStream aos) throws ParseException {
		//this will have EXTERNAL
		AsnOutputStream localAsn = new AsnOutputStream();
		try {
			super.encode(localAsn);
		} catch (AsnException e) {
			throw new ParseException(e);
		}
		
		//now lets write ourselves
		aos.writeTag(_TAG_CLASS, _TAG_PC_PRIMITIVE, _TAG);
		byte[] externalData = localAsn.toByteArray();
		aos.writeLength(externalData.length);
		try {
			aos.write(externalData);
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

	public byte[] encodeType() throws AsnException {
		// TODO Auto-generated method stub
		return null;
	}


	
	
}
