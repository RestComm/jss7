/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * @author baranowb
 *
 */
public class DialogAbortAPDUImpl implements DialogAbortAPDU {

	private AbortSource abortSource;
	private UserInformation[] userInformation;
	
	

	/**
	 * @return the abortSource
	 */
	public AbortSource getAbortSource() {
		return abortSource;
	}

	/**
	 * @param abortSource the abortSource to set
	 */
	public void setAbortSource(AbortSource abortSource) {
		this.abortSource = abortSource;
	}

	/**
	 * @return the userInformation
	 */
	public UserInformation[] getUserInformation() {
		return userInformation;
	}

	/**
	 * @param userInformation the userInformation to set
	 */
	public void setUserInformation(UserInformation[] userInformation) {
		this.userInformation = userInformation;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.asn.DialogAPDU#getType()
	 */
	public DialogAPDUType getType() {
		return DialogAPDUType.Abort;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.asn.DialogAPDU#isUniDirectional()
	 */
	public boolean isUniDirectional() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols.asn.AsnInputStream)
	 */
	public void decode(AsnInputStream ais) throws ParseException {
		try {
			// len here is quite important!
			int len;

			len = ais.readLength();

			if (len == 0x80) {
				throw new ParseException("Undefined len not supported!");
			}
			// going the easy way; not going to work with undefined!
			// this way we dont have to go through remaining len countdown
			byte[] dataChunk = new byte[len];
			ais.read(dataChunk);
			AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(dataChunk));
			int tag = localAis.readTag();
			// optional protocol version
			if (tag != AbortSource._TAG) {
				throw new ParseException("Expected Abort Source tag, found: " + tag);
			}
			this.abortSource = TcapFactory.createAbortSource(localAis);
			// now there is mandatory part
			

			// optional sequence.
			if (localAis.available() > 0) {
				// we have optional seq;
				tag = localAis.readTag();
				if (tag != Tag.SEQUENCE) {
					throw new ParseException("Expected SEQUENCE tag, found: " + tag);
				}
				byte[] data = localAis.readSequence();
				localAis = new AsnInputStream(new ByteArrayInputStream(data));
				List<UserInformation> list = new ArrayList<UserInformation>();
				while (localAis.available() > 0) {
					tag = localAis.readTag();
					if (tag != UserInformation._TAG) {
						throw new ParseException("Expected User Information tag, found: " + tag);

					}
					list.add(TcapFactory.createUserInformation(localAis));
				}
				this.userInformation = new UserInformation[list.size()];
				this.userInformation = list.toArray(this.userInformation);
			}
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
		if (abortSource == null) {
			throw new ParseException("No Abort Source Name!");
		}
		try {
			// lets not ommit protocol version, we check byte[] in tests, it screws them :)

			AsnOutputStream localAos = new AsnOutputStream();
	
			localAos.reset();
			byte[] byteData = null;
			if (userInformation != null) {
				for (UserInformation _ui : userInformation) {
					_ui.encode(localAos);
				}
				byteData = localAos.toByteArray();
				localAos.reset();

				localAos.writeSequence(byteData);

				byteData = localAos.toByteArray();

			}
			
			this.abortSource.encode(localAos);
			
			if (byteData != null) {
				localAos.write(byteData);
			}
			byteData = localAos.toByteArray();
			aos.writeTag(_TAG_CLASS, _TAG_PRIMITIVE, _TAG_ABORT);
			aos.writeLength(byteData.length);
			aos.write(byteData);

		} catch (IOException e) {
			throw new ParseException(e);
		}


	}

}
