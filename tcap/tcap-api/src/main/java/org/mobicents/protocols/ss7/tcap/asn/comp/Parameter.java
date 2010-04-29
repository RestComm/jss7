/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.Encodable;

/**
 * This class embeds Parameter/Parameters. It can be either primtiive or
 * constructed. It supports two types of encoding - content as byte[] - either
 * simple or complex - depends on how TC-User sets parameters, or content as
 * Parameter[] - in this case encoding is done as constructed element. It
 * supports following cases:
 * <ul>
 * <li><br>
 * byte[] as primitive -
 * 
 * <pre>
 * Parameter p = ....
 *  p.setTag(0x01);
 *  p.setTagClass({@link Tag.CLASS_APPLICATION});
 *  p.setData(new byte[]{ENCODED});
 *  p.setPrimitive(true);
 * </pre>
 * 
 * </li>
 * <li><br>
 * byte[] as constructed
 * 
 * <pre>
 * Parameter p = ....
 *  p.setTag(0x01);
 *  p.setTagClass({@link Tag.CLASS_APPLICATION});
 *  p.setData(new byte[]{ENCODED});
 *  p.setPrimitive(false);
 * </pre>
 * 
 * </li>
 * <li><br>
 * As constructed, with Parameter[] -
 * 
 * <pre>
 * Parameter[] params = ....;
 *  Parameter p = ....
 *  p.setTag(0x01);
 *  p.setTagClass({@link Tag.CLASS_APPLICATION});
 *  //This sets primitive explicitly to false, it must be constructed type.
 *  p.setParameters(params);
 * </pre>
 * 
 * </li>
 * </ul>
 * Note that on read only byte[] is filled! In case TC-USER makes call to
 * {@link #getParameters()} method - it triggers parsing to array, so it is
 * perfectly legal to obtain byte[], rather than Parameter[].
 * 
 * @author baranowb
 * 
 */
public interface Parameter extends Encodable {
	// public static final int _TAG = 0x02;
	// public static final boolean _TAG_PRIMITIVE = true;

	public byte[] getData();

	public void setData(byte[] b);

	public boolean isPrimitive();

	public void setPrimitive(boolean b);

	/**
	 * @return the tag
	 */
	public int getTag();

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(int tag);

	/**
	 * @return the tagClass
	 */
	public int getTagClass();

	/**
	 * @param tagClass
	 *            the tagClass to set
	 */
	public void setTagClass(int tagClass);

	public Parameter[] getParameters();

	public void setParameters(Parameter[] paramss);

}
