/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.component;

import org.mobicents.protocols.ss7.tcap.TRPseudoState;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCInvokeRequest;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author baranowb
 * 
 */
public class TCInvokeRequestImpl extends ComponentRequestImpl implements TCInvokeRequest {

	private final static long _DEFAULT_TIMEOUT = 10000;

	// local to stack
	private InvokeClass invokeClass = InvokeClass.Class1;
	private long invokeTimeout = _DEFAULT_TIMEOUT;
	private TRPseudoState state = TRPseudoState.Idle;
	private Invoke invoke;

	public TCInvokeRequestImpl() {
		super();
		this.invoke = (Invoke) super.component;
	}

	/**
	 * @return the invokeClass
	 */
	public InvokeClass getInvokeClass() {
		return this.invokeClass;
	}

	/**
	 * @param invokeClass
	 *            the invokeClass to set
	 */
	public void setInvokeClass(InvokeClass invokeClass) {
		this.invokeClass = invokeClass;
	}

	/**
	 * @return the invokeTimeout
	 */
	public long getInvokeTimeout() {
		return invokeTimeout;
	}

	/**
	 * @param invokeTimeout
	 *            the invokeTimeout to set
	 */
	public void setInvokeTimeout(long invokeTimeout) {
		this.invokeTimeout = invokeTimeout;
	}

	/**
	 * @return the linedId
	 */
	public Long getLinkedId() {
		return this.invoke.getLinkedId();
	}

	/**
	 * @param linedId
	 *            the linedId to set
	 */
	public void setLinkedId(Long linkedId) {
		this.invoke.setLinkedId(linkedId);
	}

	/**
	 * @return the operation
	 */
	public OperationCode getOperation() {
		return this.invoke.getOperationCode();
	}

	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(OperationCode operation) {
		this.invoke.setOperationCode(operation);
	}

	/**
	 * @return the parameter
	 */
	public Parameter getParameter() {
		//TODO this class will not be used clean it == Amit.
		//return this.invoke.getParameter();
		return null;
	}

	/**
	 * @param parameter
	 *            the parameter to set
	 */
	public void setParameter(Parameter parameter) {
		//this.invoke.setParameter(parameter);
	}

	/**
	 * @return the state
	 */
	public TRPseudoState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(TRPseudoState state) {
		this.state = state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.component.ComponentRequest#getType
	 * ()
	 */
	public ComponentType getType() {
		// we dont have to deffer.
		return ComponentType.Invoke;
	}

	@Override
	protected Component getEncodableComponent() {

		return TcapFactory.createComponentInvoke();
	}

}
