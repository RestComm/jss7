/**
 * 
 */
package org.mobicents.protocols.ss7.tcap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.tc.component.ComponentRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCInvokeRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.DialogRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage;
import org.mobicents.protocols.ss7.tcap.tc.component.TCInvokeRequestImpl;

/**
 * @author baranowb
 * 
 */
public class DialogImpl implements Dialog {

	//sent/received acn, holds last acn.
	private ApplicationContextName acn;
	
	private Long localTransactionId;
	private Long remoteTransactionId;

	private SccpAddress localAddress;
	private SccpAddress remoteAddress;

	private TRPseudoState state = TRPseudoState.Idle;
	private boolean strutured = true;
	// invokde ID space :)
	private static final boolean _INVOKEID_TAKEN = true;
	private static final boolean _INVOKEID_FREE = false;
	private static final int _INVOKE_TABLE_SHIFT = 128;

	private boolean[] invokeIDTable = new boolean[255];
	private int freeCount = invokeIDTable.length;

	// damn.., this is for sent invocations, apparently TCAP is per client fsm.
	private TCInvokeRequestImpl[] operationsSent = new TCInvokeRequestImpl[invokeIDTable.length];
	private ScheduledExecutorService executor;

	// scheduled components list
	private List<ComponentRequest> scheduledComponentList = new ArrayList<ComponentRequest>();
	private TCAProviderImpl provider;

	
	private static final int getIndexFromInvokeId(Long l) {
		int tmp = l.intValue();
		return tmp + _INVOKE_TABLE_SHIFT;
	}

	private static final Long getInvokeIdFromIndex(int index) {
		int tmp = index - _INVOKE_TABLE_SHIFT;
		return new Long(tmp);
	}

	DialogImpl(SccpAddress localAddress, SccpAddress remoteAddress, Long origTransactionId, ScheduledExecutorService executor, TCAProviderImpl provider) {
		super();
		this.localAddress = localAddress;
		this.remoteAddress = remoteAddress;
		this.localTransactionId = origTransactionId;
		this.executor = executor;
		this.provider = provider;
		if (origTransactionId > 0) {
			this.strutured = true;
		} else {
			this.strutured = false;
		}
		
	}

	public void release() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#getDialogId()
	 */
	public Long getDialogId() {

		return localTransactionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#getNewInvokeId()
	 */
	public synchronized Long getNewInvokeId() throws TCAPException {
		if (freeCount == 0) {
			throw new TCAPException("No free invoke ID!");
		}
		// find new...
		Long r = null;
		for (int index = 0; index < this.invokeIDTable.length; index++) {
			if (this.invokeIDTable[index] == _INVOKEID_FREE) {
				freeCount--;
				this.invokeIDTable[index] = _INVOKEID_TAKEN;
				r = this.getInvokeIdFromIndex(index);
				break;
			}
		}
		if(r == null)
		{
			throw new TCAPException("No free invoke ID");
		}
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#getRemoteAddress()
	 */
	public SccpAddress getRemoteAddress() {

		return this.remoteAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#getLocalAddress()
	 */
	public SccpAddress getLocalAddress() {

		return this.localAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#isEstabilished()
	 */
	public boolean isEstabilished() {

		return this.state == TRPseudoState.Active;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#isStructured()
	 */
	public boolean isStructured() {

		return this.strutured;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest)
	 */
	public void send(TCBeginRequest event) throws TCAPSendException {
		
		if(this.isStructured())
		{
			
		}
		//build proper msg
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest)
	 */
	public void send(TCContinueRequest event) throws TCAPSendException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest)
	 */
	public void send(TCEndRequest event) throws TCAPSendException {
		// TODO Auto-generated method stub
		
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#sendComponent(org
	 * .mobicents.protocols.ss7.tcap.api.tc.component.ComponentRequest)
	 */
	public void sendComponent(ComponentRequest componentRequest) {

		// if(TC-UCANCEL-REQ)
		// {
		// Long cancelIID = ....
		// if(this.operationsSent.contains(cancelIID))
		// {
		//
		// }
		// }else if(TC-U-REJECT)
		// {
		// Long cancelIID = ....
		// if(this.operationsSent.contains(cancelIID))
		// {
		//
		// }
		// }else
		// {

		this.scheduledComponentList.add(componentRequest);

		// }

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#sendBegin()
	 */
	public void sendBegin() throws TCAPSendException {
		// TODO Auto-generated method stub
		
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#sendContinue()
	 */
	public void sendContinue() throws TCAPSendException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#sendEnd()
	 */
	public void sendEnd() throws TCAPSendException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#sendUni()
	 */
	public void sendUni() throws TCAPSendException {
		// TODO Auto-generated method stub

	}

	// /////////////////
	// LOCAL METHODS //
	// /////////////////

	/**
	 * @return the localTransactionId
	 */
	Long getLocalTransactionId() {
		return localTransactionId;
	}

	/**
	 * @param localTransactionId
	 *            the localTransactionId to set
	 */
	void setLocalTransactionId(Long localTransactionId) {
		this.localTransactionId = localTransactionId;
	}

	/**
	 * @return the remoteTransactionId
	 */
	Long getRemoteTransactionId() {
		return remoteTransactionId;
	}

	/**
	 * @param remoteTransactionId
	 *            the remoteTransactionId to set
	 */
	void setRemoteTransactionId(Long remoteTransactionId) {
		this.remoteTransactionId = remoteTransactionId;
	}

	/**
	 * @param localAddress
	 *            the localAddress to set
	 */
	void setLocalAddress(SccpAddress localAddress) {
		this.localAddress = localAddress;
	}

	/**
	 * @param remoteAddress
	 *            the remoteAddress to set
	 */
	void setRemoteAddress(SccpAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	void processUni(TCUniMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {

	}

	void processBegin(TCBeginMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {

	}

	void processContinue(TCContinueMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {

	}

	void processEnd(TCEndMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {

	}

}
