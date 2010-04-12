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
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * @author baranowb
 * 
 */
public class ReturnResultImpl implements ReturnResult {

	// mandatory
	private Long invokeId;

	// optional: this is sequence
	private OperationCode[] operationCode;

	// optional
	private Parameter parameter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.Return#getInvokeId()
	 */
	public Long getInvokeId() {

		return this.invokeId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.Return#getOperationCode()
	 */
	public OperationCode[] getOperationCode() {

		return this.operationCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.Return#getParameter()
	 */
	public Parameter getParameter() {

		return this.parameter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Return#setInvokeId(java.lang
	 * .Long)
	 */
	public void setInvokeId(Long i) {
		if (i < -128 || i > 127) {
			throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
		}
		this.invokeId = i;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Return#setOperationCode(org
	 * .mobicents.protocols.ss7.tcap.asn.comp.OperationCode[])
	 */
	public void setOperationCode(OperationCode[] oc) {
		this.operationCode = oc;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Return#setParameter(org.mobicents
	 * .protocols.ss7.tcap.asn.comp.Parameter)
	 */
	public void setParameter(Parameter p) {

		this.parameter = p;
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
			if (len == 0x80) {
				throw new ParseException("Unspiecified length is not supported.");
			}

			byte[] data = new byte[len];
			if (len != ais.read(data)) {
				throw new ParseException("Not enough data read.");
			}

			AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(data));
			int tag = localAis.readTag();
			if (tag != _TAG_IID) {
				throw new ParseException("Expected InvokeID tag, found: " + tag);
			}
			this.invokeId = localAis.readInteger();
			if (localAis.available() <= 0) {
				return;
			}

			tag = localAis.readTag();
			if (tag == Tag.SEQUENCE) {
				// sequence of OperationCode
				len = ais.readLength();
				if (len == 0x80) {
					throw new ParseException("Unspiecified length is not supported.");
				}

				data = new byte[len];
				if (len != ais.read(data)) {
					throw new ParseException("Not enough data read.");
				}
				AsnInputStream sequenceStream = new AsnInputStream(new ByteArrayInputStream(data));
				List<OperationCode> opCodes = new ArrayList<OperationCode>();
				while (sequenceStream.available() > 0) {
					tag = sequenceStream.readTag();
					if (tag == OperationCode._TAG_GLOBAL || tag == OperationCode._TAG_LOCAL) {
						opCodes.add(TcapFactory.createOperationCode(tag, sequenceStream));
					} else {
						throw new ParseException("Expected Global|Local operation code.");
					}
				}
				this.operationCode = new OperationCode[opCodes.size()];
				this.operationCode = opCodes.toArray(this.operationCode);

				if (localAis.available() > 0) {
					tag = localAis.readTag();
				} else {
					return;
				}
			}

			this.parameter = TcapFactory.createParameter(tag, localAis);
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

		if (invokeId == null) {
			throw new ParseException("No Invoke ID set.");
		}
		try {
			AsnOutputStream localAos = new AsnOutputStream();
			byte[] data = null;
			if (this.operationCode != null) {
				for (OperationCode oc : this.operationCode) {
					oc.encode(localAos);
				}
				data = localAos.toByteArray();
				localAos.reset();
			}

			// form msg from top.

			if (data != null) {

				localAos.writeInteger(_TAG_IID_CLASS, _TAG_IID, this.invokeId);

				localAos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
				localAos.writeLength(data.length);
				localAos.write(data);
			}

			if (parameter != null) {
				parameter.encode(localAos);
			}

			data = localAos.toByteArray();

			aos.writeTag(_TAG_CLASS, _TAG_PC_PRIMITIVE, _TAG);
			aos.writeLength(data.length);
			aos.write(data);
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

}
