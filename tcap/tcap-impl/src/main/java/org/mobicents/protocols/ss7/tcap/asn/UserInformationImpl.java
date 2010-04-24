/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.External;
import org.mobicents.protocols.asn.Tag;

/**
 * <p>
 * According to ITU-T Rec Q.773 the UserInformation is defined as
 * </p>
 * <br/>
 * <p>
 * user-information [30] IMPLICIT SEQUENCE OF EXTERNAL
 * </p>
 * <br/>
 * <p>
 * For definition of EXTERNAL look {@link org.mobicents.protocols.asn.External}
 * from Mobicents ASN module
 * </p>
 * 
 * @author baranowb
 * @author amit bhayani
 * 
 */
public class UserInformationImpl extends External implements UserInformation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.asn.External#decode(org.mobicents.protocols.asn.AsnInputStream)
	 */
	@Override
	public void decode(AsnInputStream ais) throws ParseException {

		try {
			int length = ais.readLength();

			int tag = ais.readTag();

			if (tag != Tag.EXTERNAL) {
				throw new AsnException(
						"Wrong value of tag, expected EXTERNAL, found: " + tag);
			}

			super.decode(ais);
		} catch (AsnException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.asn.External#encode(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encode(AsnOutputStream aos) throws ParseException {
		// this will have EXTERNAL
		AsnOutputStream localAsn = new AsnOutputStream();
		try {
			super.encode(localAsn);
		} catch (AsnException e) {
			throw new ParseException(e);
		}

		// now lets write ourselves
		aos.writeTag(_TAG_CLASS, _TAG_PC_PRIMITIVE, _TAG);
		byte[] externalData = localAsn.toByteArray();
		aos.writeLength(externalData.length);
		try {
			aos.write(externalData);
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}
}
