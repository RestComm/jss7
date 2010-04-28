/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.DialogImpl;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.api.tc.component.OperationState;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author baranowb
 * @author amit bhayani
 * 
 */
public class InvokeImpl implements Invoke {

	// mandatory
	private Long invokeId;

	// optional
	private Long linkedId;

	// mandatory
	private OperationCode operationCode;

	// optional
	private Parameter[] parameters;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#getInvokeId()
	 */
	public Long getInvokeId() {

		return this.invokeId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#getLinkedId()
	 */
	public Long getLinkedId() {

		return this.linkedId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#getOperationCode()
	 */
	public OperationCode getOperationCode() {

		return this.operationCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#getParameteR()
	 */
	public Parameter[] getParameters() {

		return this.parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#setInvokeId(java.lang
	 * .Integer)
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
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#setLinkedId(java.lang
	 * .Integer)
	 */
	public void setLinkedId(Long i) {
		if ((i == null) || (i < -128 || i > 127)) {
			throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
		}
		this.linkedId = i;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#setOperationCode(org
	 * .mobicents.protocols.ss7.tcap.asn.comp.OperationCode)
	 */
	public void setOperationCode(OperationCode i) {
		this.operationCode = i;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#setParameter(org.mobicents
	 * .protocols.ss7.tcap.asn.comp.Parameter)
	 */
	public void setParameters(Parameter[] p) {
		this.parameters = p;

	}

	public ComponentType getType() {

		return ComponentType.Invoke;
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
			if (ais.available() < len) {
				throw new ParseException("Not enough data!");
			}
			byte[] data = new byte[len];
			if (data.length != ais.read(data)) {
				throw new ParseException("Not enought data read.");
			}
			AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(data));
			int tag = localAis.readTag();
			if (tag != _TAG_IID) {
				throw new ParseException("Expected InvokeID tag, found: " + tag);
			}

			this.invokeId = localAis.readInteger();

			tag = localAis.readTag();
			if (tag == _TAG_LID) {
				// optional
				this.linkedId = localAis.readInteger();
				tag = localAis.readTag();
			}

			if (tag == OperationCode._TAG_GLOBAL || tag == OperationCode._TAG_LOCAL) {
				this.operationCode = TcapFactory.createOperationCode(tag, localAis);
				if (localAis.available() <= 0) {
					return;
				}

			} else {
				throw new ParseException("Expected Local|Global Operation Code tag, found: " + tag);
			}

			// It could be SEQUENCE of PARAMETER
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
				this.parameters = new Parameter[] { TcapFactory.createParameter(tag, localAis) };
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
		if (this.invokeId == null) {
			throw new ParseException("Invoke ID not set!");
		}
		if (this.operationCode == null) {
			throw new ParseException("Operation Code not set!");
		}
		try {
			AsnOutputStream localAos = new AsnOutputStream();

			localAos.writeInteger(_TAG_IID_CLASS, _TAG_IID, this.invokeId);
			if (this.linkedId != null) {
				localAos.writeInteger(_TAG_LID_CLASS, _TAG_LID, this.linkedId);
			}

			this.operationCode.encode(localAos);

			if (this.parameters != null) {
				if (this.parameters.length > 1) {

					AsnOutputStream aosTemp = new AsnOutputStream();
					for (Parameter p : this.parameters) {
						p.encode(aosTemp);
					}

					byte[] paramData = aosTemp.toByteArray();

					// Sequence TAG
					localAos.write(0x30);

					// Sequence Length
					localAos.write(paramData.length);

					// Now write the Parameter's
					localAos.write(paramData);

				} else {
					this.parameters[0].encode(localAos);
				}
			}
			byte[] data = localAos.toByteArray();
			aos.writeTag(_TAG_CLASS, _TAG_PC_PRIMITIVE, _TAG);
			aos.writeLength(data.length);
			aos.write(data);
		} catch (IOException e) {
			throw new ParseException(e);
		}

	}

	// TCAP state data, it is used ONLY on client side
	private final static long _DEFAULT_TIMEOUT = 10000;

	// local to stack
	private InvokeClass invokeClass = InvokeClass.Class1;
	private long invokeTimeout = _DEFAULT_TIMEOUT;
	private OperationState state = OperationState.Idle;
	private Future timerFuture;
	private OperationTimerTask operationTimerTask = new OperationTimerTask();
	private TCAPProviderImpl provider;
	private DialogImpl dialog;

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

	//////////////////////
	// set methods for //
	// relevant data   //
	/////////////////////
	/**
	 * @return the provider
	 */
	public TCAPProviderImpl getProvider() {
		return provider;
	}

	/**
	 * @param provider the provider to set
	 */
	public void setProvider(TCAPProviderImpl provider) {
		this.provider = provider;
	}

	/**
	 * @return the dialog
	 */
	public DialogImpl getDialog() {
		return dialog;
	}

	/**
	 * @param dialog the dialog to set
	 */
	public void setDialog(DialogImpl dialog) {
		this.dialog = dialog;
	}

	/**
	 * @return the state
	 */
	public OperationState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public synchronized void setState(OperationState state) {
		if (this.dialog == null) {
			// bad call on server side.
			return;
		}
		OperationState old = this.state;
		this.state = state;
		if (old != state) {

			switch (state) {
			case Sent:
				// start timer
				this.startTimer();
				break;
			case Idle:
			case Reject_W:
				this.stopTimer();
				dialog.operationEnded(this);
			}
			if (state == OperationState.Sent) {

			} else if (state == OperationState.Idle || state == OperationState.Reject_W) {

			}

		}
	}

	public void onReturnResultLast() {
		this.setState(OperationState.Idle);

	}

	public void onError() {
		this.setState(OperationState.Idle);

	}

	public synchronized void startTimer() {
		this.stopTimer();
		this.timerFuture = this.provider.createOperationTimer(this.operationTimerTask, this.invokeTimeout);

	}

	public synchronized void stopTimer() {

		if (this.timerFuture != null) {
			this.timerFuture.cancel(false);
			this.timerFuture = null;
		}

	}

	public boolean isErrorReported() {
		if (this.invokeClass == InvokeClass.Class1 || this.invokeClass == InvokeClass.Class2) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isSuccessReported() {
		if (this.invokeClass == InvokeClass.Class1 || this.invokeClass == InvokeClass.Class3) {
			return true;
		} else {
			return false;
		}
	}

	private class OperationTimerTask implements Runnable {
		InvokeImpl invoke;

		public void run() {
			synchronized (invoke) {
				// op failed, we must delete it from dialog and notify!
				timerFuture = null;
				setState(OperationState.Idle);
				// TC-L-CANCEL
				((DialogImpl) invoke.dialog).operationTimedOut(invoke);
			}

		}

	}

}
