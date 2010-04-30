package org.mobicents.protocols.ss7.tcap.functional;

import junit.framework.TestCase;

import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;

public class Client implements TCListener{

	private TCAPStack stack;
	private TCAPFunctionalTest runningTestCase;
	private SccpAddress thisAddress;
	private SccpAddress remoteAddress;
	
	private TCAPProvider tcapProvider;

	private boolean finished = true;
	private String unexpected = "";
	private boolean _S_receivedContinue,_S_sentEnd,_S_dialogReleased;
	
	private Dialog clientDialog;
	
	Client(SccpProvider sccpPprovider, TCAPFunctionalTest runningTestCase, SccpAddress thisAddress,SccpAddress remoteAddress) {
		super();
		this.stack = new TCAPStackImpl(sccpPprovider);
		this.runningTestCase = runningTestCase;
		this.thisAddress = thisAddress;
		this.remoteAddress = remoteAddress;
		this.tcapProvider = this.stack.getProvider();
		this.tcapProvider.addTCListener(this);
	}

	public void start() throws TCAPException, TCAPSendException
	{
		clientDialog = this.tcapProvider.getNewDialog(thisAddress, remoteAddress);
		ComponentPrimitiveFactory cpFactory = this.tcapProvider.getComponentPrimitiveFactory();
		
		//create some INVOKE
		Invoke invoke = cpFactory.createTCInvokeRequest();
		invoke.setInvokeId(this.clientDialog.getNewInvokeId());
		
		invoke.setOperationCode(cpFactory.createOperationCode(true,new Long(12)));
		
		this.clientDialog.sendComponent(invoke);
		
		ApplicationContextName acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContextName(TCAPFunctionalTest._ACN_);
		//UI is optional!
		TCBeginRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createBegin(this.clientDialog);
		tcbr.setApplicationContextName(acn);
		this.clientDialog.send(tcbr);
	}
	
	public void dialogReleased(Dialog d) {
		
		_S_dialogReleased = true;
		
	}

	public void onInvokeTimeout(Invoke tcInvokeRequest) {
		unexpected+="Invocation timed out: "+tcInvokeRequest.getInvokeId()+"\n";
		finished = false;
	}

	public void onTCBegin(TCBeginIndication ind) {
		unexpected+="Receveid TC Begin, this should not happen\n";
		finished = false;
		
	}

	public void onTCContinue(TCContinueIndication ind) {
		_S_receivedContinue = true;
		//send end
		TCEndRequest end = this.tcapProvider.getDialogPrimitiveFactory().createEnd(ind.getDialog());
		end.setTermination(TerminationType.Basic);
		try {
			ind.getDialog().send(end);
			_S_sentEnd = true;
		} catch (TCAPSendException e) {
			throw new RuntimeException(e);
		}
	}

	public void onTCEnd(TCEndIndication ind) {
		
		unexpected+="Receveid TC End, this should not happen\n";
		finished = false;
	}

	public void onTCUni(TCUniIndication ind) {
	
		unexpected+="Receveid TC Uni, this should not happen\n";
		finished = false;
	}

	public boolean isFinished() {
		
		return this.finished && _S_dialogReleased && _S_receivedContinue && _S_sentEnd;
	}
	
	public String getStatus()
	{
		String status = "";

		status +="_S_receivedContinue["+_S_receivedContinue+"]" +"\n";
		status +="_S_sentEnd["+_S_sentEnd+"]" +"\n";
		status +="_S_dialogReleased["+_S_dialogReleased+"]" +"\n";
		
		
		return status+unexpected;
	}

	public void onTCPAbort(TCPAbortIndication ind) {
		// TODO Auto-generated method stub
		
	}

	public void onTCUserAbort(TCUserAbortIndication ind) {
		// TODO Auto-generated method stub
		
	}
	
	
}
