package org.mobicents.protocols.ss7.tools.traceparser;

import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.DialogImpl;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;
import org.mobicents.protocols.ss7.tcap.asn.RejectImpl;
import org.mobicents.protocols.ss7.tcap.asn.ReturnErrorImpl;
import org.mobicents.protocols.ss7.tcap.asn.ReturnResultImpl;
import org.mobicents.protocols.ss7.tcap.asn.ReturnResultLastImpl;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCEndMessage;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class DialogImplWrapper extends DialogImpl {
	
	private int acnValue;
	private int acnVersion;
	
	private HashMap<Long,InvokeImpl> invokeList = new HashMap<Long,InvokeImpl>();
	
	public DialogImplWrapper(SccpAddress localAddress, SccpAddress remoteAddress, Long origTransactionId, boolean structured,
			ScheduledExecutorService executor, TCAPProviderImpl provider, int seqControl) {
		super(localAddress, remoteAddress, origTransactionId, structured, executor, provider, seqControl);
	}

	public int getAcnValue() {
		return acnValue;
	}

	public int getAcnVersion() {
		return acnVersion;
	}

	public void setAcnValue(int v) {
		acnValue = v;
	}

	public void setAcnVersion(int v) {
		acnVersion = v;
	}
	
	public void SetStateActive() {
		this.setState(TRPseudoState.Active);
	}
	
	
	@Override
	protected Component[] processOperationsState(Component[] components) {
		if (components == null) {
			return null;
		}
		
		for(Component c : components) {
			switch( c.getType() ) {
			case Invoke:
				InvokeImpl inv = (InvokeImpl)c;
				this.invokeList.put(c.getInvokeId(), inv);
				break;
			case ReturnResult:
				ReturnResultImpl rr = (ReturnResultImpl)c;
				Long invId = rr.getInvokeId();
				inv = this.invokeList.get(invId);
				if (inv != null && rr.getOperationCode() == null)
					rr.setOperationCode(inv.getOperationCode());
				break;
			case ReturnResultLast:
				ReturnResultLastImpl rrl = (ReturnResultLastImpl)c;
				invId = rrl.getInvokeId();
				inv = this.invokeList.get(invId);
				if (inv != null && rrl.getOperationCode() == null)
					rrl.setOperationCode(inv.getOperationCode());
				this.invokeList.remove(invId);
				break;
			case ReturnError:
				ReturnErrorImpl re = (ReturnErrorImpl)c;
				invId = re.getInvokeId();
				this.invokeList.remove(invId);
				break;
			case Reject:
				RejectImpl rej = (RejectImpl)c;
				invId = rej.getInvokeId();
				this.invokeList.remove(invId);
				break;
			}
		}
		
		return components;
	}
	
	@Override
	public void processAbort(TCAbortMessage msg, SccpAddress localAddress2, SccpAddress remoteAddress2) {
		super.processAbort(msg, localAddress2, remoteAddress2);
	}
	
	@Override
	public void processEnd(TCEndMessage msg, SccpAddress localAddress2, SccpAddress remoteAddress2) throws TCAPException {
		super.processEnd(msg, localAddress2, remoteAddress2);
	}
	
	@Override
	public void processContinue(TCContinueMessage msg, SccpAddress localAddress2, SccpAddress remoteAddress2) throws TCAPException {
		super.processContinue(msg, localAddress2, remoteAddress2);
	}
	
	@Override
	public void processBegin(TCBeginMessage msg, SccpAddress localAddress2, SccpAddress remoteAddress2) throws TCAPException {
		super.processBegin(msg, localAddress2, remoteAddress2);
	}
	
	public void send(TCUserAbortRequest event) throws TCAPSendException {
	}
}

