/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;

/**
 * @author baranowb
 * @author amit bhayani
 * 
 */
public interface Invoke extends Component {
	// FIXME: add dialog field!
	// this is sequence
	public static final int _TAG = 0x01;
	public static final boolean _TAG_PC_PRIMITIVE = false;
	public static final int _TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;

	public static final int _TAG_IID = 0x02;
	public static final boolean _TAG_IID_PC_PRIMITIVE = true;
	public static final int _TAG_IID_CLASS = Tag.CLASS_UNIVERSAL;

	public static final int _TAG_LID = 0x00;
	public static final boolean _TAG_LID_PC_PRIMITIVE = true;
	public static final int _TAG_LID_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;

	// local, relevant only for send
	public InvokeClass getInvokeClass();

	/**
	 * 
	 * @param invokeClass
	 */
	public void setInvokeClass(InvokeClass invokeClass);

	/**
	 * @return the invokeTimeout
	 */
	public long getInvokeTimeout();

	/**
	 * Sets timeout for this invoke operation in miliseconds. If no indication
	 * on operation status is received, before this value passes, operation
	 * timesout.
	 * 
	 * @param invokeTimeout
	 *            the invokeTimeout to set
	 */
	public void setInvokeTimeout(long invokeTimeout);

	// optional
	public void setLinkedId(Long i);

	public Long getLinkedId();

	// mandatory
	public void setOperationCode(OperationCode i);

	public OperationCode getOperationCode();

	// optional
	public void setParameter(Parameter p);

	public Parameter getParameter();
}
