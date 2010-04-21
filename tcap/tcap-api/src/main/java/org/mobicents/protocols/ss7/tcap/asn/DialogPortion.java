package org.mobicents.protocols.ss7.tcap.asn;

import java.util.BitSet;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.Tag;

/**
 * 
 * @author baranowb
 * @author amit bhayani
 * 
 */
public interface DialogPortion extends Encodable {

	// Dialog portion is actually type of EXTERNAL, this sucks....
	public static final int _TAG_CLASS = Tag.CLASS_APPLICATION;
	public static final boolean _TAG_PC_PRIMITIVE = false;
	public static final int _TAG = 0x0B;

	/**
	 * @return the dialogAPDU
	 */
	public DialogAPDU getDialogAPDU();

	/**
	 * @param dialogAPDU
	 *            the dialogAPDU to set
	 */
	public void setDialogAPDU(DialogAPDU dialogAPDU);

	public void setUnidirectional(boolean flag);

	public boolean isUnidirectional();

	// From External

	public boolean isOid();

	public void setOid(boolean oid);

	public long[] getOidValue();

	public void setOidValue(long[] oidValue);

	public boolean isInteger();

	public void setInteger(boolean integer);

	public long getIndirectReference();

	public void setIndirectReference(long indirectReference);

	public boolean isObjDescriptor();

	public void setObjDescriptor(boolean objDescriptor);

	public boolean isAsn();

	public void setAsn(boolean asn);

	public byte[] getEncodeType() throws AsnException;

	public void setEncodeType(byte[] data);

	public boolean isOctet();

	public void setOctet(boolean octet);

	public boolean isArbitrary();

	public void setArbitrary(boolean arbitrary);

	public BitSet getEncodeBitStringType();

	public void setEncodeBitStringType(BitSet data);

}
