/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;

/**
 * @author baranowb
 * 
 */
public class TCBeginMessageImpl implements TCBeginMessage {

	private static final String _OCTET_STRING_ENCODE = "US-ASCII";
	// mandatory
	private String originatingTransactionId;
	// opt
	private DialogPortion dp;
	// opt
	private Component[] component;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage#getComponent()
	 */
	public Component[] getComponent() {

		return this.component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage#getDialogPortion
	 * ()
	 */
	public DialogPortion getDialogPortion() {

		return this.dp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage#
	 * getOriginatingTransactionId()
	 */
	public String getOriginatingTransactionId() {

		return this.originatingTransactionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage#setComponent
	 * (org.mobicents.protocols.ss7.tcap.asn.comp.Component[])
	 */
	public void setComponent(Component[] c) {
		this.component = c;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage#setDialogPortion
	 * (org.mobicents.protocols.ss7.tcap.asn.DialogPortion)
	 */
	public void setDialogPortion(DialogPortion dp) {
		this.dp = dp;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage#
	 * setOriginatingTransactionId(java.lang.String)
	 */
	public void setOriginatingTransactionId(String t) {
		this.originatingTransactionId = t;

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
			int len = ais.readLength();
			if (len < ais.available()) {
				throw new ParseException("Not enough data: " + ais.available());
			}
			if (len == 0x80) {
				//
				throw new ParseException("Undefined len not supported");
			}
			byte[] data = new byte[len];
			ais.read(data);
			AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(data));

			int tag = localAis.readTag();
			if (tag != _TAG_OTX) {
				throw new ParseException("Expected OriginatingTransactionId, found: " + tag);
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			localAis.readOctetString(bos);
			this.originatingTransactionId = new String(bos.toByteArray(), _OCTET_STRING_ENCODE);

			if (localAis.available() <= 0) {
				return;
			}

			// we hav optional;
			tag = localAis.readTag();
			if (tag == DialogPortion._TAG) {
				this.dp = TcapFactory.createDialogPortion(localAis);
				if (localAis.available() <= 0) {
					return;
				}
				tag = localAis.readTag();
			}

			len = localAis.readLength();
			if (len < localAis.available() || len == 0) {
				throw new ParseException("Not enough data");
			}
			List<Component> cps = new ArrayList<Component>();
			// its iterator :)
			while (localAis.available() > 0) {
				cps.add(TcapFactory.createComponent(localAis));
			}

			this.component = new Component[cps.size()];
			this.component = cps.toArray(this.component);
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
		if (this.originatingTransactionId == null) {
			throw new ParseException("Transaction ID.");
		}
		try {
			AsnOutputStream localAos = new AsnOutputStream();
			byte[] data = null;

			if (component != null) {
				for (Component c : this.component) {
					c.encode(localAos);
				}
				data = localAos.toByteArray();
				localAos.reset();
				localAos.writeTag(Component._COMPONENT_TAG_CLASS, Component._COMPONENT_TAG_PC_PRIMITIVE, Component._COMPONENT_TAG);
				localAos.writeLength(data.length);
				localAos.write(data);
				data = localAos.toByteArray();
				localAos.reset();
			}

			

			

			// write TX
			localAos.writeStringOctet(_TAG_OTX, _TAG_CLASS_OTX, new ByteArrayInputStream(this.originatingTransactionId
					.getBytes(_OCTET_STRING_ENCODE)));

			if (this.dp != null) {
				this.dp.encode(localAos);
				if(data!=null)
				{
					localAos.write(data);
				}
				data = localAos.toByteArray();
			}else
			{
				if(data!=null)
				{
					localAos.write(data);
				}
				data = localAos.toByteArray();
			}

			aos.writeTag(_TAG_CLASS, _TAG_PC_PRIMITIVE, _TAG);
			aos.writeLength(data.length);
			aos.write(data);
		} catch (IOException e) {
			throw new ParseException(e);
		} catch (AsnException e) {
			throw new ParseException(e);
		}

	}

}
