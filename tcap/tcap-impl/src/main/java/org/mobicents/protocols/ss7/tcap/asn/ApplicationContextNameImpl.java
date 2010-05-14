/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * @author baranowb
 * 
 */
public class ApplicationContextNameImpl implements ApplicationContextName {

	// object identifier value
	private long[] oid;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols
	 * .asn.AsnInputStream)
	 */
	public void decode(AsnInputStream ais) throws ParseException {
		try {
			int len = ais.readLength();
			// check len?

			int tag = ais.readTag();
			if (tag != Tag.OBJECT_IDENTIFIER) {
				throw new ParseException("Expected OID tag, found: " + tag);
			}
			this.oid = ais.readObjectIdentifier();
		} catch (IOException e) {
			throw new ParseException(e);
		} catch (AsnException e) {
			throw new ParseException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols
	 * .asn.AsnOutputStream)
	 */
	public void encode(AsnOutputStream aos) throws ParseException {
		// TODO Auto-generated method stub
		if (this.oid == null) {
			throw new ParseException("No OID value set!");
		}
		try {
			AsnOutputStream localAOS = new AsnOutputStream();
			localAOS.writeObjectIdentifier(this.oid);
			byte[] oidEncoded = localAOS.toByteArray();
			aos.writeTag(_TAG_CLASS, _TAG_PC_PRIMITIVE, _TAG);
			aos.writeLength(oidEncoded.length);

			aos.write(oidEncoded);
		} catch (IOException e) {
			throw new ParseException(e);
		}

	}

	/**
	 * @return the oid
	 */
	public long[] getOid() {
		return oid;
	}

	/**
	 * @param oid
	 *            the oid to set
	 */
	public void setOid(long[] oid) {
		this.oid = oid;
	}

}
