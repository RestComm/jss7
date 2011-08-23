/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.External;
import org.mobicents.protocols.asn.Tag;


/**
 * <p>
 * As per the ITU-T Rec Q.773 Table 30/Q.773 – Dialogue Portion
 * </p>
 * <br/>
 * <p>
 * The DialogPortion consist of Dialogue Portion Tag (0x6B), Dialogue Portion
 * Length; External Tag (0x28), External Length; Structured or unstructured
 * dialogue
 * </p>
 * <br/>
 * <p>
 * As per the Table 33/Q.773 –Structured Dialogue is represented as
 * </p>
 * <br/>
 * <p>
 * Object Identifier Tag (0x06), Object Identifier Length; Dialogue-as-ID value;
 * Single-ASN.1-type Tag (0xa0), Single-ASN.1-type Length; Dialogue PDU
 * </p>
 * <br/>
 * <p>
 * As per the Table 37/Q.773 – Dialogue-As-ID Value is represented OID. Please
 * look {@link DialogPortionImpl._DIALG_STRUCTURED}
 * </p>
 * <br/>
 * <p>
 * As per the Table 34/Q.773 – Unstructured Dialogue is represented as
 * </p>
 * <br/>
 * <p>
 * Object Identifier Tag (0x06), Object Identifier Length; Unidialogue-as-ID
 * value; Single-ASN.1-type Tag (0xa0), Single-ASN.1-type Length; Unidirectional
 * Dialogue PDU
 * </p>
 * <br/>
 * <p>
 * As per the Table 36/Q.773 – Unidialogue-As-ID Value is represented as OID.
 * Please look {@link DialogPortionImpl._DIALG_UNI}
 * </p>
 * 
 * 
 * 
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class DialogPortionImpl extends External implements DialogPortion {

	//Encoded OID, dont like this....
	private static final long[] _DIALG_UNI = new long[]{0, 0, 17, 773, 1, 2, 1};
	private static final long[] _DIALG_STRUCTURED = new long[]{0, 0, 17, 773, 1, 1, 1};
	//private static final byte[] _DIALG_UNI_B = new byte[]{ 0x6, 0x7, 0x00, 0x11, (byte) 0x86, 0x05,0x01,0x02,0x01};
	//private static final byte[] _DIALG_STRUCTURED_B = new byte[]{ 0x6, 0x7, 0x00, 0x11, (byte) 0x86, 0x05,0x01,0x01,0x01};
	
	// MANDATORY - in sequence, our payload
	private DialogAPDU dialogAPDU;
	
	private boolean uniDirectional;

	public DialogPortionImpl() {
		super();
		// our defs, will be those are overiden on read in super class, but for
		// DialogPortion, this is what we want.
		setOid(true);
		setAsn(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.DialogPortion#isUnidirectional()
	 */
	public boolean isUnidirectional() {
		return this.uniDirectional;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.DialogPortion#setUnidirectional(
	 * boolean)
	 */
	public void setUnidirectional(boolean flag) {
		if (flag) {
			super.oidValue = _DIALG_UNI;
		} else {
			super.oidValue = _DIALG_STRUCTURED;
		}
		this.uniDirectional = flag;

	}

	/**
	 * @return the dialogAPDU
	 */
	public DialogAPDU getDialogAPDU() {
		return dialogAPDU;
	}

	/**
	 * @param dialogAPDU
	 *            the dialogAPDU to set
	 */
	public void setDialogAPDU(DialogAPDU dialogAPDU) {
		// FIXME: check content VS apdu TYPE ?
		this.dialogAPDU = dialogAPDU;
	}

	

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.asn.External#getEncodeBitStringType()
	 */
	
	public BitSetStrictLength getEncodeBitStringType() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.asn.External#getEncodeType()
	 */
	
	public byte[] getEncodeType() throws AsnException {
		if (this.dialogAPDU == null) {
			throw new AsnException("No APDU!");
		}
		AsnOutputStream aos = new AsnOutputStream();
		this.dialogAPDU.encode(aos);
		return aos.toByteArray();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.asn.External#setEncodeBitStringType(java.util.BitSet)
	 */
	
	public void setEncodeBitStringType(BitSet data) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.asn.External#setEncodeType(byte[])
	 */
	
	public void setEncodeType(byte[] data) {
		throw new UnsupportedOperationException();
	}

	
	public String toString() {
		return "DialogPortion[dialogAPDU=" + dialogAPDU + ", uniDirectional=" + uniDirectional + "]";
	}

	public void encode(AsnOutputStream aos) throws ParseException {
		
		try {
			aos.writeTag(Tag.CLASS_APPLICATION, false, _TAG);
			int pos = aos.StartContentDefiniteLength();

			super.encode(aos);
			
			aos.FinalizeContent(pos);
			
		} catch (AsnException e) {
			throw new ParseException("AsnException when encoding DialogPortion: " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.asn.External#decode(org.mobicents.protocols.asn
	 * .AsnInputStream)
	 */
	
	public void decode(AsnInputStream aisA) throws ParseException {

		// TAG has been decoded already, now, lets get LEN
		try {
			AsnInputStream ais = aisA.readSequenceStream();

			int tag = ais.readTag();
			if (tag != Tag.EXTERNAL)
				throw new ParseException("Error decoding DialogPortion: wrong value of tag, expected EXTERNAL, found: " + tag);
			
			super.decode(ais);

			if (!isAsn() || !isOid()) {
				throw new ParseException("Error decoding DialogPortion: Oid and Asd parts not found");
			}

			// Check Oid
			if (Arrays.equals(_DIALG_UNI, this.getOidValue()))
				this.uniDirectional = true;
			else if (Arrays.equals(_DIALG_STRUCTURED, this.getOidValue()))
				this.uniDirectional = false;
			else
				throw new ParseException("Error decoding DialogPortion: bad Oid value");				
			
			AsnInputStream loaclAsnIS = new AsnInputStream(super.getEncodeType());

			// now lets get APDU
			tag = loaclAsnIS.readTag();
			this.dialogAPDU = TcapFactory.createDialogAPDU(loaclAsnIS, tag, isUnidirectional());
			
		} catch (IOException e) {
			throw new ParseException("IOException when decoding DialogPortion: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new ParseException("AsnException when decoding DialogPortion: " + e.getMessage(), e);
		}
	}
}
