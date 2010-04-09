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
public class AbortSourceImpl implements AbortSource {

	private AbortSourceType type;
	
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.asn.AbortSource#getAbortSourceType()
	 */
	public AbortSourceType getAbortSourceType() {
		return this.type;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.asn.AbortSource#setAbortSourceType(org.mobicents.protocols.ss7.tcap.asn.AbortSourceType)
	 */
	public void setAbortSourceType(AbortSourceType t) {
		this.type = t;

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols.asn.AsnInputStream)
	 */
	public void decode(AsnInputStream ais) throws ParseException {
//		int len;
//		try {
//			len = ais.readLength();
//
//			if (len > ais.available()) {
//				throw new ParseException("Not enough data: " + ais.available());
//			}
//
//			int tag = ais.readTag();
//			if (tag != Tag.INTEGER) {
//				throw new ParseException("Expected INTEGER tag, found: " + tag);
//
//			}
//			// y, its a bit of enum, should be ok to cast :)
//			long t = ais.readInteger();
//			this.type = AbortSourceType.Provider.getFromInt(t);
//		} catch (IOException e) {
//			throw new ParseException(e);
//		} catch (AsnException e) {
//			throw new ParseException(e);
//		}

		
		try {
			
			// y, its a bit of enum, should be ok to cast :)
			long t = ais.readInteger();
			this.type = AbortSourceType.Provider.getFromInt(t);
		} catch (IOException e) {
			throw new ParseException(e);
		} catch (AsnException e) {
			throw new ParseException(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encode(AsnOutputStream aos) throws ParseException {
		if (type == null) {
			throw new ParseException("No type set!");
		}
//		try {
//			AsnOutputStream localAos = new AsnOutputStream();
//			localAos.writeInteger(this.type.getType());
//			byte[] b = localAos.toByteArray();
//			aos.writeTag(_TAG_CLASS, _TAG_PC_PRIMITIVE, _TAG);
//			aos.writeLength(b.length);
//			aos.write(b);
//		} catch (IOException e) {
//			throw new ParseException(e);
//		}
		try {
		
		aos.writeInteger(_TAG_CLASS,_TAG,type.getType());
	} catch (IOException e) {
		throw new ParseException(e);
	}

	}

}
