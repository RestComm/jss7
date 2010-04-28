/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage;

import sun.awt.image.ByteArrayImageSource;

/**
 * @author baranowb
 * 
 */
public class TCAbortMessageImpl implements TCAbortMessage {

	private static final String _OCTET_STRING_ENCODE = "US-ASCII";

	private Long destTxId;
	private PAbortCauseType type;
	private DialogPortion dp;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage#
	 * getDestinationTransactionId()
	 */
	public Long getDestinationTransactionId() {

		return destTxId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage#getDialogPortion
	 * ()
	 */
	public DialogPortion getDialogPortion() {

		return dp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage#getPAbortCause()
	 */
	public PAbortCauseType getPAbortCause() {

		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage#
	 * setDestinationTransactionId()
	 */
	public void setDestinationTransactionId(Long t) {
		this.destTxId = t;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage#setDialogPortion
	 * (org.mobicents.protocols.ss7.tcap.asn.DialogPortion)
	 */
	public void setDialogPortion(DialogPortion dp) {
		this.dp = dp;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage#setPAbortCause
	 * (org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType)
	 */
	public void setPAbortCause(PAbortCauseType t) {
		this.type = t;

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
			if (tag != _TAG_DTX) {
				throw new ParseException("Expected Destination Tx ID tag, found: " + tag);
			}
		
			this.destTxId = Utils.readTransactionId(localAis);

			if (localAis.available() <= 0) {
				// end
				return;
			}

			// else we have optional part
			tag = localAis.readTag();
			if (tag == _TAG_P) {
				// primitive?
				this.type = PAbortCauseType.getFromInt(localAis.readInteger());

				if (localAis.available() <= 0) {
					// no dialog portion
					return;
				}
				tag = localAis.readTag();
			}

			if (tag == DialogPortion._TAG) {
				this.dp = TcapFactory.createDialogPortion(localAis);
			}

			if (localAis.available() > 0) {
				throw new ParseException("To much data in stream.");
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
		if (this.destTxId == null) {
			throw new ParseException("No destination TxID");
		}

		try {
			AsnOutputStream localAos = new AsnOutputStream();
			Utils.writeTransactionId(localAos, this.destTxId, _TAG_CLASS_DTX, _TAG_DTX);
			
			// FIXME: check if both are not null, should not be at the sime time
			// != null
			if (this.type != null) {
				localAos.writeTag(_TAG_CLASS_P, _TAG_P_PC_PRIMITIVE, _TAG_P);
				localAos.writeInteger(_TAG_CLASS_P, _TAG_P, this.type.getType());
			}
			if (this.dp != null) {
				this.dp.encode(localAos);
			}
			byte[] data = localAos.toByteArray();
			aos.writeTag(_TAG_CLASS, _TAG_PC_PRIMITIVE, _TAG);
			aos.writeLength(data.length);
			aos.write(data);
		} catch (UnsupportedEncodingException e) {
			throw new ParseException(e);
		} catch (AsnException e) {
			throw new ParseException(e);
		} catch (IOException e) {
			throw new ParseException(e);
		}

	}

}
