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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
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
	
	public BitSet getEncodeBitStringType() {
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

		//this will have EXTERNAL
		AsnOutputStream localAsn = new AsnOutputStream();
		try {
			super.encode(localAsn);
		} catch (AsnException e) {
			throw new ParseException(e);
		}
		
		try {
			//now lets write ourselves
			aos.writeTag(_TAG_CLASS, _TAG_PC_PRIMITIVE, _TAG);
			byte[] externalData = localAsn.toByteArray();

			aos.writeLength(externalData.length);
			aos.write(externalData);
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
	 * org.mobicents.protocols.asn.External#decode(org.mobicents.protocols.asn
	 * .AsnInputStream)
	 */
	
	public void decode(AsnInputStream ais) throws ParseException {

		// TAG has been decoded already, now, lets get LEN
		try {
			int len = ais.readLength();
			// add checks?
			
			int tag = ais.readTag();
			if(tag != Tag.EXTERNAL)
			{
				throw new AsnException(
						"Wrong value of tag, expected EXTERNAL, found: " + tag);
			}
			super.decode(ais);

			if (!isAsn() || !isOid()) {
				throw new ParseException("Wrong encode(" + isOid() + ")/encoding(" + isAsn() + ") types");
			}
			
			//Set whether Unidirectional
			this.uniDirectional = Arrays.equals(_DIALG_UNI, this.getOidValue());
			
			byte[] dialogPdu = super.getEncodeType();
			AsnInputStream loaclAsnIS = new AsnInputStream(new ByteArrayInputStream(dialogPdu));

			// now lets get APDU
			tag = loaclAsnIS.readTag();// this sucks, depending on OID, it can have
									// diff values.....
		
			this.dialogAPDU = TcapFactory.createDialogAPDU(loaclAsnIS, tag, isUnidirectional());
			
		} catch (ParseException e) {

			throw e;
		} catch (Exception e) {
			throw new ParseException(e);
		}
	}

//	public void decode(AsnInputStream ais) throws ParseException {
//		// FIXME: LONG form of LEN ??
//		try {
//			int length = ais.readLength();
//			if (length != 0x80 && ais.available() < length) {
//				throw new ParseException("Not enough eoctets: " + ais.available() + ", required: " + length);
//			}
//			// we are SEQUENCE as external, however TCAP uses hardcoded part :)!
//			//External.TAG
//			//External.LEN
//			//OID_TAG
//			//OID_LEN
//			//OID_VAL
//			//Single-ASN.1.TAG
//			//Single-ASN.1.LEN
//			//DialogAPDU
//			int tag = ais.readTag();
//			if(tag!=(_TAG_EXTERNAL))
//			{
//				throw new ParseException("Expected external TAG, found: "+tag);
//			}
//			
//			int childLen = ais.readLength();
//			if(childLen +2 !=length)
//			{
//				throw new ParseException("Wrong length of EXTERNAL content, found: "+childLen);
//			}
//			oid=ais.readObjectIdentifier();
//			
//			tag = ais.readTag();
//
//			if(tag!=_TAG_EXTERNAL_SINGLE)
//			{
//				throw new ParseException("Expected Single-ASN.1 TAG, found: "+tag);
//			}
//			childLen = ais.readLength();
//			if(childLen > ais.available())
//			{
//				throw new ParseException("Wrong length of EXTERNAL.Singla-ASN.1-Type content, found: "+childLen);
//			}
//			//in single ASN.1-Type we have dialogPDU
//			this.dialogAPDU = TcapFactory.createDialogAPDU(ais);
//		} catch (ParseException e) {
//			throw e;
//		} catch (Exception e) {
//			throw new ParseException(e);
//		}
//
//	}
//
//	public void encode(AsnOutputStream aos) throws ParseException {
//		
//		if(this.dialogAPDU==null)
//		{
//			throw new ParseException("No PADU!!!");
//		}
//
//		AsnOutputStream childAsn = new AsnOutputStream();
//		this.dialogAPDU.encode(childAsn);
//		byte[] singleAsnData = childAsn.toByteArray();
//		aos.write(DialogPortion._TAG_E);
//		aos.writeLength(singleAsnData.length+2+_OID_LEN+2);
//		//+2 for single asn tag+len, +_OID_LEN(TLV), +2 EXTERNAL TL
//		
//		//EXTERNAL
//		aos.write(DialogPortion._TAG_EXTERNAL_E);
//		aos.writeLength(singleAsnData.length+2+_OID_LEN);
//		
//		//OID
//		//aos.write(DialogPortion._TAG_EXTERNAL_OBJ_E);
//		//aos.writeLength(_OID_LEN-2);
//		if(isUnidirectional())
//		{
//			try {
//				aos.write(_DIALG_UNI_B);
//			} catch (IOException e) {
//				throw new ParseException(e);
//			}
//		}else
//		{
//			try{
//				aos.write(_DIALG_STRUCTURED_B);
//			} catch (IOException e) {
//				throw new ParseException(e);
//			}
//		}
//		aos.write(_TAG_EXTERNAL_SINGLE_E);
//		aos.writeLength(singleAsnData.length);
//		try {
//			aos.write(singleAsnData);
//		} catch (IOException e) {
//			throw new ParseException(e);
//		}
//		
//	}

}
