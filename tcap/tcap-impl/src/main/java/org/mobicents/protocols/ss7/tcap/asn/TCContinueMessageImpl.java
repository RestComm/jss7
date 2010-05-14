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
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;

/**
 * @author baranowb
 * 
 */
public class TCContinueMessageImpl implements TCContinueMessage {

	private static final String _OCTET_STRING_ENCODE = "US-ASCII";

	// mandatory
	private Long originatingTransactionId;
	private Long destinationTransactionId;
	// opt
	private DialogPortion dp;
	// opt
	private Component[] component;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage#getComponent
	 * ()
	 */
	public Component[] getComponent() {

		return this.component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage#
	 * getDestinationTransactionId()
	 */
	public Long getDestinationTransactionId() {

		return this.destinationTransactionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage#getDialogPortion
	 * ()
	 */
	public DialogPortion getDialogPortion() {

		return this.dp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage#
	 * getOriginatingTransactionId()
	 */
	public Long getOriginatingTransactionId() {

		return this.originatingTransactionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage#setComponent
	 * (org.mobicents.protocols.ss7.tcap.asn.comp.Component[])
	 */
	public void setComponent(Component[] c) {

		this.component = c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage#
	 * setDestinationTransactionId(java.lang.String)
	 */
	public void setDestinationTransactionId(Long t) {
		this.destinationTransactionId = t;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage#setDialogPortion
	 * (org.mobicents.protocols.ss7.tcap.asn.DialogPortion)
	 */
	public void setDialogPortion(DialogPortion dp) {
		this.dp = dp;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage#
	 * setOriginatingTransactionId(java.lang.String)
	 */
	public void setOriginatingTransactionId(Long t) {

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
			if (len > ais.available()) {
				throw new ParseException("Not enough data: " + ais.available());
			}
			if (len == 0x80) {
				//
				throw new ParseException("Undefined len not supported");
			}
			byte[] data = new byte[len];
			if(len!=ais.read(data))
			{
				throw new ParseException("Not enough data read.");
			}
			AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(data));

			int tag = localAis.readTag();
			if (tag != _TAG_OTX) {
				throw new ParseException("Expected OriginatingTransactionId, found: " + tag);
			}

			this.originatingTransactionId = Utils.readTransactionId(localAis);

			tag = localAis.readTag();

			if (tag != _TAG_DTX) {
				throw new ParseException("Expected OriginatingTransactionId, found: " + tag);
			}

			this.destinationTransactionId = Utils.readTransactionId(localAis);

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
				Component c = TcapFactory.createComponent(localAis);
				if(c == null)
				{
					break;
				}
				cps.add(c);
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
			throw new ParseException("Expected Originating transaction ID.");
		}
		if (this.destinationTransactionId == null) {
			throw new ParseException("Expected Destination transaction ID.");
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
			//localAos.writeInteger( _TAG_CLASS_OTX,_TAG_OTX, (this.originatingTransactionId));
			Utils.writeTransactionId(localAos, this.originatingTransactionId, _TAG_CLASS_OTX, _TAG_OTX);
			//localAos.writeInteger(_TAG_CLASS_DTX, _TAG_DTX, (this.destinationTransactionId));
			Utils.writeTransactionId(localAos,this.destinationTransactionId, _TAG_CLASS_DTX, _TAG_DTX);
			

			if (this.dp != null) {
				this.dp.encode(localAos);
				if (data != null) {
					localAos.write(data);
				}
				data = localAos.toByteArray();
			} else {
				if (data != null) {
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
