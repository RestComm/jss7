package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

public class DialogPortionImpl implements DialogPortion {

	//Encoded OID, dont like this....
	private static final long[] _DIALG_UNI = new long[]{0, 0, 17, 773, 1, 2, 1};
	private static final long[] _DIALG_STRUCTURED = new long[]{0, 0, 17, 773, 1, 1, 1};
	private static final byte[] _DIALG_UNI_B = new byte[]{ 0x6, 0x7, 0x00, 0x11, (byte) 0x86, 0x05,0x01,0x02,0x01};
	private static final byte[] _DIALG_STRUCTURED_B = new byte[]{ 0x6, 0x7, 0x00, 0x11, (byte) 0x86, 0x05,0x01,0x01,0x01};
	
	//encoded OID len, to speed up coding
	private static final int _OID_LEN = 9; 
	
	
	// MANDATORY - in sequence
	private long[] oid=_DIALG_UNI;
	private DialogAPDU dialogAPDU; 


	

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.asn.DialogPortion#isUnidirectional()
	 */
	public boolean isUnidirectional() {
		return _DIALG_UNI[5] == this.oid[5];
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.asn.DialogPortion#setUnidirectional(boolean)
	 */
	public void setUnidirectional(boolean flag) {
		if(flag)
		{
			this.oid = _DIALG_UNI;
		}else
		{
			this.oid = _DIALG_STRUCTURED;
		}
		
	}

	/**
	 * @return the dialogAPDU
	 */
	public DialogAPDU getDialogAPDU() {
		return dialogAPDU;
	}

	/**
	 * @param dialogAPDU the dialogAPDU to set
	 */
	public void setDialogAPDU(DialogAPDU dialogAPDU) {
		//FIXME: check content VS apdu TYPE ?
		this.dialogAPDU = dialogAPDU;
	}

	public void decode(AsnInputStream ais) throws ParseException {
		// FIXME: LONG form of LEN ??
		try {
			int length = ais.readLength();
			if (length != 0x80 && ais.available() < length) {
				throw new ParseException("Not enough eoctets: " + ais.available() + ", required: " + length);
			}
			// we are SEQUENCE as external, however TCAP uses hardcoded part :)!
			//External.TAG
			//External.LEN
			//OID_TAG
			//OID_LEN
			//OID_VAL
			//Single-ASN.1.TAG
			//Single-ASN.1.LEN
			//DialogAPDU
			int tag = ais.readTag();
			if(tag!=(_TAG_EXTERNAL))
			{
				throw new ParseException("Expected external TAG, found: "+tag);
			}
			
			int childLen = ais.readLength();
			if(childLen +2 !=length)
			{
				throw new ParseException("Wrong length of EXTERNAL content, found: "+childLen);
			}
			oid=ais.readObjectIdentifier();
			
			tag = ais.readTag();

			if(tag!=_TAG_EXTERNAL_SINGLE)
			{
				throw new ParseException("Expected Single-ASN.1 TAG, found: "+tag);
			}
			childLen = ais.readLength();
			if(childLen > ais.available())
			{
				throw new ParseException("Wrong length of EXTERNAL.Singla-ASN.1-Type content, found: "+childLen);
			}
			//in single ASN.1-Type we have dialogPDU
			this.dialogAPDU = TcapFactory.createDialogAPDU(ais);
		} catch (ParseException e) {
			throw e;
		} catch (Exception e) {
			throw new ParseException(e);
		}

	}

	public void encode(AsnOutputStream aos) throws ParseException {
		
		if(this.dialogAPDU==null)
		{
			throw new ParseException("No PADU!!!");
		}

		AsnOutputStream childAsn = new AsnOutputStream();
		this.dialogAPDU.encode(childAsn);
		byte[] singleAsnData = childAsn.toByteArray();
		aos.write(DialogPortion._TAG_E);
		aos.writeLength(singleAsnData.length+2+_OID_LEN+2);
		//+2 for single asn tag+len, +_OID_LEN(TLV), +2 EXTERNAL TL
		
		//EXTERNAL
		aos.write(DialogPortion._TAG_EXTERNAL_E);
		aos.writeLength(singleAsnData.length+2+_OID_LEN);
		
		//OID
		//aos.write(DialogPortion._TAG_EXTERNAL_OBJ_E);
		//aos.writeLength(_OID_LEN-2);
		if(isUnidirectional())
		{
			try {
				aos.write(_DIALG_UNI_B);
			} catch (IOException e) {
				throw new ParseException(e);
			}
		}else
		{
			try{
				aos.write(_DIALG_STRUCTURED_B);
			} catch (IOException e) {
				throw new ParseException(e);
			}
		}
		aos.write(_TAG_EXTERNAL_SINGLE_E);
		aos.writeLength(singleAsnData.length);
		try {
			aos.write(singleAsnData);
		} catch (IOException e) {
			throw new ParseException(e);
		}
		
	}

}
