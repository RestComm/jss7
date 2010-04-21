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
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * @author baranowb
 * @author amit bhayani
 * 
 */
public class ReturnResultLastImpl implements ReturnResultLast {

	// mandatory
	private Long invokeId;

	// optional: this is sequence
	private OperationCode[] operationCode;

	// optional
	private Parameter[] parameters;

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
	public Parameter[] getParameters() {
		return this.parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Return#setInvokeId(java.lang
	 * .Long)
	 */
	public void setInvokeId(Long i) {
		if ((i == null) || (i < -128 || i > 127)) {
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
	public void setParameters(Parameter[] p) {
		this.parameters = p;
	}

	public ComponentType getType() {

		return ComponentType.ReturnResultLast;
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
			
			
			/*
			 * TODO
			 * 
			 * As per the definition of ASN in Q.773 the Return Result is defined as 
			 * 
			 * 
			 * ReturnResult ::= SEQUENCE {
                     invokeID             InvokeIdType,
                     result               SEQUENCE {
                            operationCode OPERATION,
                            parameter     ANY DEFINED BY operationCode
                     } OPTIONAL
                 }


			 * Hence the result is sequence of Operation Code and Parameter. The bellow decoding takes only sequence of Operation Code
			 * and the Parameter which is wrong.
			 * 
			 * 
			 * Look at trace from nad1053.pcap
			 */			
			
			

			if (tag == Tag.SEQUENCE) {
				// sequence of OperationCode
				
				len = localAis.readLength();
				if (len == 0x80) {
					throw new ParseException("Unspiecified length is not supported.");
				}

				data = new byte[len];
				if (len != localAis.read(data)) {
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

				} else {
					return;
				}
			}
			tag = localAis.readTag();
			
			if (tag == Tag.SEQUENCE) {
				
				int length = localAis.readLength();
				
				List<Parameter> paramsList = new ArrayList<Parameter>();

				while (localAis.available() > 0) {
					// This is Parameter Tag
					tag = localAis.readTag();
					Parameter p = TcapFactory.createParameter(tag, localAis);
					paramsList.add(p);
				}
				
				this.parameters = new Parameter[paramsList.size()];
				this.parameters = paramsList.toArray(this.parameters);
				
				paramsList.clear();				
				
			} else {
				this.parameters = new Parameter[] { TcapFactory
						.createParameter(tag, localAis) };
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

			if (this.parameters != null) {
				if(this.parameters.length > 1 ){
					
					AsnOutputStream aosTemp = new AsnOutputStream();
					for(Parameter p : this.parameters){
						p.encode(aosTemp);
					}
					
					byte[] paramData = aosTemp.toByteArray();
					
					//Sequence TAG
					localAos.write(0x30);
					
					//Sequence Length
					localAos.write(paramData.length);
					
					//Now write the Parameter's 
					localAos.write(paramData);
					
				} else{
					this.parameters[0].encode(localAos);
				}
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
