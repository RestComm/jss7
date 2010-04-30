package org.mobicents.protocols.ss7.tcap.functional;

import junit.framework.TestCase;

import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

public class Server implements TCListener{

	private TCAPStack stack;
	private TestCase runningTestCase;
	private SccpAddress thisAddress;
	private SccpAddress remoteAddress;
	private TCAPProvider tcapProvider;
	private Dialog serverDialog;
	
	private boolean finished = true;
	private boolean _S_recievedBegin,_S_sentContinue,_S_receivedEnd,_S_dialogReleased;
	private String unexpected = "";
	
	Server(SccpProvider sccpPprovider, TestCase runningTestCase, SccpAddress thisAddress,SccpAddress remoteAddress) {
		super();
		this.stack = new TCAPStackImpl(sccpPprovider);
		this.runningTestCase = runningTestCase;
		this.thisAddress = thisAddress;
		this.remoteAddress = remoteAddress;
		this.tcapProvider = this.stack.getProvider();
		this.tcapProvider.addTCListener(this);
	}
	
	public void dialogReleased(Dialog d) {
		
		
		_S_dialogReleased = true;
	}

	public void onInvokeTimeout(Invoke tcInvokeRequest) {
		
		//to indiacte failures.. since it does not fail
		//runningTestCase.fail("Invocation timed out: "+tcInvokeRequest.getInvokeId());
		unexpected+="Receveid Invoke timeout, this should not happen\n";
		finished = false;
		throw new RuntimeException();
		
	}

	public void onTCBegin(TCBeginIndication ind) {
		_S_recievedBegin = true;
		if(ind.getDialog() == null)
		{
			throw new RuntimeException("NO DIALOG");
		}
		if(ind.getApplicationContextName()== null)
		{
			throw new RuntimeException("NO ACN");
		}
		this.serverDialog = ind.getDialog();
		Component[] comps = ind.getComponents();
		if(comps == null || comps.length!=1)
		{
			throw new RuntimeException("Bad comps!"); 
		}
		Component c = comps[0];
		if(c.getType()!= ComponentType.Invoke)
		{
			throw new RuntimeException("Bad type: "+c.getType());
		}
		//lets kill this Invoke
		Invoke invoke = (Invoke) c;
		ReturnResultLast rrlast = this.tcapProvider.getComponentPrimitiveFactory().createTCResultLastRequest();
		rrlast.setInvokeId(invoke.getInvokeId());
		rrlast.setOperationCode(invoke.getOperationCode());
		
		TCContinueRequest continueRequest = this.tcapProvider.getDialogPrimitiveFactory().createContinue(this.serverDialog);
		continueRequest.setApplicationContextName(ind.getApplicationContextName());
		try {
			this.serverDialog.sendComponent(rrlast);
			this.serverDialog.send(continueRequest);
			_S_sentContinue = true;
		} catch (TCAPSendException e) {
			throw new RuntimeException(e);
		}
	}

	public void onTCContinue(TCContinueIndication ind) {
		unexpected+="Receveid TC Continue, this should not happen\n";
		finished = false;
		
	}

	public void onTCEnd(TCEndIndication ind) {
		_S_receivedEnd = true;
		
	}

	public void onTCUni(TCUniIndication ind) {
		unexpected+="Receveid TC Uni, this should not happen\n";
		finished = false;
	}

	public boolean isFinished() {
		
		return this.finished && _S_dialogReleased && _S_receivedEnd && _S_recievedBegin && _S_sentContinue;
	}
	
	public String getStatus()
	{
		String status = "";

		status +="_S_dialogReleased["+_S_dialogReleased+"]" +"\n";
		status +="_S_sentContinue["+_S_sentContinue+"]" +"\n";
		status +="_S_recievedBegin["+_S_recievedBegin+"]" +"\n";
		status +="_S_receivedEnd["+_S_receivedEnd+"]" +"\n";
		
		
		return status+unexpected;
	}

	public void onTCPAbort(TCPAbortIndication ind) {
		// TODO Auto-generated method stub
		
	}

	public void onTCUserAbort(TCUserAbortIndication ind) {
		// TODO Auto-generated method stub
		
	}
	
	
}
