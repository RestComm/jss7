package org.mobicents.protocols.ss7.tools.traceparser;

import java.util.concurrent.ScheduledExecutorService;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.DialogImpl;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
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

