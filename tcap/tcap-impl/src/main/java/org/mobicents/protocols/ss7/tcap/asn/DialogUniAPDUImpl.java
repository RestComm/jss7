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
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.DialogAPDUType;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.ProtocolVersion;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

/**
 * @author baranowb
 * 
 */
public class DialogUniAPDUImpl implements DialogUniAPDU {

	private ApplicationContextName acn;
	private UserInformation ui;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDU#
	 * getApplicationContextName()
	 */
	public ApplicationContextName getApplicationContextName() {
		return acn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDU#getProtocolVersion
	 * ()
	 */
	public int getProtocolVersion() {

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDU#getUserInformation
	 * ()
	 */
	public UserInformation getUserInformation() {
		return this.ui;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDU#
	 * setApplicationContextName
	 * (org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName)
	 */
	public void setApplicationContextName(ApplicationContextName acn) {
		this.acn = acn;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDU#setUserInformation
	 * (org.mobicents.protocols.ss7.tcap.asn.UserInformation[])
	 */
	public void setUserInformation(UserInformation ui) {
		this.ui = ui;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.DialogAPDU#getType()
	 */
	public DialogAPDUType getType() {
		return DialogAPDUType.Request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.DialogAPDU#isUniDirectional()
	 */
	public boolean isUniDirectional() {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols
	 * .asn.AsnInputStream)
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
			if(len!=ais.read(dataChunk))
			{
				throw new ParseException("Not enough data read.");
			}
			AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(dataChunk));
			int tag = localAis.readTag();
			// optional protocol version
			if (tag == ProtocolVersion._TAG_PROTOCOL_VERSION) {
				// we have protocol version on a
				// decode it
				TcapFactory.createProtocolVersion(localAis);
				tag = localAis.readTag();
			}

			// now there is mandatory part
			if (tag != ApplicationContextName._TAG) {
				throw new ParseException("Expected Application Context Name tag, found: " + tag);
			}
			this.acn = TcapFactory.createApplicationContextName(localAis);

			// optional sequence.
			if (localAis.available() > 0) {
				// we have optional seq;

				// TODO: The Q.773 defines SEQUENCE of USER INFORMATION, however all
				// the traces shows no SEQUNECE and just one USER INFORMATION

				// tag = localAis.readTag();
				// if (tag != Tag.SEQUENCE) {
				// throw new ParseException("Expected SEQUENCE tag, found: " +
				// tag);
				// }
				// byte[] data = localAis.readSequence();
				// localAis = new AsnInputStream(new
				// ByteArrayInputStream(data));

				tag = localAis.readTag();
				if(tag != UserInformation._TAG)
				{
					throw new ParseException("Expected UserInformation tag, found: "+tag);
				}
				this.ui = TcapFactory.createUserInformation(localAis);
			}
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

		if (acn == null) {
			throw new ParseException("No Application Context Name!");
		}
		try {
			// lets not ommit protocol version, we check byte[] in tests, it screws them :)

			AsnOutputStream localAos = new AsnOutputStream();
	
			localAos.reset();
			byte[] byteData = null;
			if (ui != null) {
				ui.encode(localAos);
				byteData = localAos.toByteArray();
				localAos.reset();

				localAos.writeSequence(byteData);

				byteData = localAos.toByteArray();

			}
			ProtocolVersion pv = TcapFactory.createProtocolVersion();
			pv.encode(localAos);
			this.acn.encode(localAos);
			
			if (byteData != null) {
				localAos.write(byteData);
			}
			byteData = localAos.toByteArray();
			aos.writeTag(_TAG_CLASS, _TAG_PRIMITIVE, _TAG_UNIDIRECTIONAL);
			aos.writeLength(byteData.length);
			aos.write(byteData);

		} catch (IOException e) {
			throw new ParseException(e);
		}

	}

}
