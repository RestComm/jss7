/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.tcap;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
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
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;

public class Client implements TCListener{

	private TCAPStack stack;
	private SccpAddress thisAddress;
	private SccpAddress remoteAddress;
	
	private TCAPProvider tcapProvider;

	private boolean finished = true;
	private String unexpected = "";
	private boolean _S_receivedContinue,_S_sentEnd,_S_dialogReleased;
	
	private Dialog clientDialog;
	
	Client(TCAPStack stack, SccpAddress thisAddress,SccpAddress remoteAddress) {
		super();
		this.stack = stack;
		
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
		Invoke invoke = cpFactory.createTCInvokeRequest(InvokeClass.Class1);
		invoke.setInvokeId(this.clientDialog.getNewInvokeId());
		
		invoke.setOperationCode(cpFactory.createOperationCode(true,new Long(12)));
		//no parameter
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
		unexpected+="Received TC Begin, this should not happen\n";
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
		
		unexpected+="Received TC End, this should not happen\n";
		finished = false;
	}

	public void onTCUni(TCUniIndication ind) {
	
		unexpected+="Received TC Uni, this should not happen\n";
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
		unexpected+="Received TC PAbort, this should not happen\n";
		finished = false;
		
	}

	public void onTCUserAbort(TCUserAbortIndication ind) {
		unexpected+="Received TC UAbort, this should not happen\n";
		finished = false;
	}

	public void dialogTimedout(Dialog d) {
		unexpected+="Received Dilaog timeout, this should not happen\n";
		finished = false;
		
	}
	
	
}
