/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn.comp;


import org.mobicents.protocols.ss7.tcap.asn.Encodable;

/**
 * @author baranowb
 *
 */
public interface Parameter extends Encodable{
	//public static final int _TAG = 0x02;
	//public static final boolean _TAG_PRIMITIVE = true;
	
	
	public byte[] getData();
	public void setData(byte[] b);
	
	
	public boolean isPrimitive();
	public void setPrimitive(boolean b);
	/**
	 * @return the tag
	 */
	public int getTag();

	/**
	 * @param tag the tag to set
	 */
	public void setTag(int tag);

	/**
	 * @return the tagClass
	 */
	public int getTagClass() ;
	/**
	 * @param tagClass the tagClass to set
	 */
	public void setTagClass(int tagClass);

}
