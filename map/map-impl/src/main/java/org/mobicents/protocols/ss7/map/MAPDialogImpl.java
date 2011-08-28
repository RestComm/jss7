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

package org.mobicents.protocols.ss7.map;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPServiceBase;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.errors.MAPErrorMessageImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * 
 * MAP-DialoguePDU ::= CHOICE { map-open [0] MAP-OpenInfo, map-accept [1]
 * MAP-AcceptInfo, map-close [2] MAP-CloseInfo, map-refuse [3] MAP-RefuseInfo,
 * map-userAbort [4] MAP-UserAbortInfo, map-providerAbort [5]
 * MAP-ProviderAbortInfo}
 * 
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 */
public abstract class MAPDialogImpl implements MAPDialog {
	private static final Logger logger = Logger.getLogger(MAPDialogImpl.class);

	private Object userObject;

	protected Dialog tcapDialog = null;
	protected MAPProviderImpl mapProviderImpl = null;
	protected MAPServiceBase mapService = null;

	// Application Context of this Dialog
	protected MAPApplicationContext appCntx;

	protected AddressString destReference;
	protected AddressString origReference;
	protected MAPExtensionContainer extContainer = null;

	protected MAPDialogState state = MAPDialogState.Idle;
	
	protected Boolean normalDialogShutDown = false;
	
	private Set<Long> incomingInvokeList = new HashSet<Long>();

	protected MAPDialogImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl,
			MAPServiceBase mapService, AddressString origReference, AddressString destReference) {
		this.appCntx = appCntx;
		this.tcapDialog = tcapDialog;
		this.mapProviderImpl = mapProviderImpl;
		this.mapService = mapService;
		this.destReference = destReference;
		this.origReference = origReference;
	}

	@Override
	public void keepAlive() {
		this.tcapDialog.keepAlive();
	}

	public Long getDialogId() {
		return tcapDialog.getDialogId();
	}

	public MAPServiceBase getService() {
		return this.mapService;
	}

	public Dialog getTcapDialog() {
		return tcapDialog;
	}

	public void setExtentionContainer(MAPExtensionContainer extContainer) {
		this.extContainer = extContainer;
	}

	/**
	 * Setting that the MAP Dialog is normally shutting down - 
	 * to prevent performing onDialogReleased()  
	 */
	protected void setNormalDialogShutDown() {
		this.normalDialogShutDown = true;
	}
	
	protected Boolean getNormalDialogShutDown() {
		return this.normalDialogShutDown;
	}
	
	/**
	 * Adding the new incoming invokeId into incomingInvokeList list
	 * 
	 * @param invokeId
	 * @return false: failure - this invokeId already present in the list
	 */
	public boolean addIncomingInvokeId(Long invokeId) {
		synchronized (this.incomingInvokeList) {
			if (this.incomingInvokeList.contains(invokeId))
				return false;
			else {
				this.incomingInvokeList.add(invokeId);
				return true;
			}
		}
	}

	public void removeIncomingInvokeId(Long invokeId) {
		synchronized (this.incomingInvokeList) {
			this.incomingInvokeList.remove(invokeId);
		}
	}

	public Boolean checkIncomingInvokeIdExists(Long invokeId) {
		synchronized (this.incomingInvokeList) {
			return this.incomingInvokeList.contains(invokeId);
		}
	}
	
	public void abort(MAPUserAbortChoice mapUserAbortChoice) throws MAPException {

		synchronized (this) {
			// Dialog is not started or has expunged - we need not send TC-U-ABORT,
			// only Dialog removing
			if (this.getState() == MAPDialogState.Expunged || this.getState() == MAPDialogState.Idle) {
				this.setState(MAPDialogState.Expunged);
				return;
			}

			this.setNormalDialogShutDown();
			this.mapProviderImpl.fireTCAbortUser(this.getTcapDialog(), mapUserAbortChoice, this.extContainer);
			this.extContainer = null;

			this.setState(MAPDialogState.Expunged);
		}
	}

	public void refuse(Reason reason) throws MAPException {

		synchronized (this) {
			// Dialog must be in the InitialReceived state
			if (this.getState() != MAPDialogState.InitialReceived) {
				throw new MAPException("Refuse can be called in the Dialog InitialReceived state");
			}

			this.setNormalDialogShutDown();
			this.mapProviderImpl.fireTCAbortRefused(this.getTcapDialog(), reason, this.extContainer);
			this.extContainer = null;

			this.setState(MAPDialogState.Expunged);
		}
	}

	public void close(boolean prearrangedEnd) throws MAPException {

		synchronized (this) {
			switch (this.tcapDialog.getState()) {
			case InitialReceived:
				ApplicationContextName acn = this.mapProviderImpl
						.getTCAPProvider().getDialogPrimitiveFactory()
						.createApplicationContextName(this.appCntx.getOID());

				this.setNormalDialogShutDown();
				this.mapProviderImpl.fireTCEnd(this.getTcapDialog(), true,
						prearrangedEnd, acn, this.extContainer);
				this.extContainer = null;

				this.setState(MAPDialogState.Expunged);
				break;

			case Active:
				this.setNormalDialogShutDown();
				this.mapProviderImpl.fireTCEnd(this.getTcapDialog(), false,
						prearrangedEnd, null, null);

				this.setState(MAPDialogState.Expunged);
				break;
				
			case Idle:
				throw new MAPException(
						"Awaiting TC-BEGIN to be sent, can not send another dialog initiating primitive!");
			case InitialSent: // we have sent TC-BEGIN already, need to wait
				throw new MAPException(
						"Awaiting TC-BEGIN response, can not send another dialog initiating primitive!");
			case Expunged: // dialog has been terminated on TC level, cant send
				throw new MAPException(
						"Dialog has been terminated, can not send primitives!");
			}
		}
	}

	public void send() throws MAPException {

		synchronized (this) {
			switch (this.tcapDialog.getState()) {

			case Idle:
				ApplicationContextName acn = this.mapProviderImpl
						.getTCAPProvider().getDialogPrimitiveFactory()
						.createApplicationContextName(this.appCntx.getOID());

				this.mapProviderImpl.fireTCBegin(this.getTcapDialog(), acn,
						destReference, origReference, this.extContainer);
				this.extContainer = null;

				this.setState(MAPDialogState.InitialSent);
				break;

			case Active:
				// Its Active send TC-CONTINUE

				this.mapProviderImpl.fireTCContinue(this.getTcapDialog(),
						false, null, null);
				break;

			case InitialReceived:
				// Its first Reply to TC-Begin

				ApplicationContextName acn1 = this.mapProviderImpl
						.getTCAPProvider().getDialogPrimitiveFactory()
						.createApplicationContextName(this.appCntx.getOID());

				this.mapProviderImpl.fireTCContinue(this.getTcapDialog(), true,
						acn1, this.extContainer);
				this.extContainer = null;

				this.setState(MAPDialogState.Active);
				break;

			case InitialSent: // we have sent TC-BEGIN already, need to wait
				throw new MAPException(
						"Awaiting TC-BEGIN response, can not send another dialog initiating primitive!");
			case Expunged: // dialog has been terminated on TC level, cant send
				throw new MAPException(
						"Dialog has been terminated, can not send primitives!");
			}
		}
	}
	
	public MAPApplicationContext getApplicationContext() {
		return appCntx;
	}

	public MAPDialogState getState() {
		return state;
	}

	protected synchronized void setState(MAPDialogState newState) {
		// add checks?
		if (this.state == MAPDialogState.Expunged) {
			return;
		}
		
		this.state = newState;
		if (newState == MAPDialogState.Expunged) {
			this.mapProviderImpl.removeDialog(tcapDialog.getDialogId());
			this.mapProviderImpl.deliverDialogResease(this);
		}
	}

	public void sendInvokeComponent(Invoke invoke) throws MAPException {

		try {
			this.tcapDialog.sendComponent(invoke);
		} catch (TCAPSendException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	public void sendReturnResultComponent(ReturnResult returnResult) throws MAPException {

		try {
			this.tcapDialog.sendComponent(returnResult);
		} catch (TCAPSendException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	public void sendReturnResultLastComponent(ReturnResultLast returnResultLast) throws MAPException {

		this.removeIncomingInvokeId(returnResultLast.getInvokeId());

		try {
			this.tcapDialog.sendComponent(returnResultLast);
		} catch (TCAPSendException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	public void sendErrorComponent(Long invokeId, MAPErrorMessage mem) throws MAPException {

		MAPErrorMessageImpl mapErrorMessage = (MAPErrorMessageImpl)mem;
		
		this.removeIncomingInvokeId(invokeId);
		
		ReturnError returnError = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCReturnErrorRequest();

		try {
			returnError.setInvokeId(invokeId);

			// Error Code
			ErrorCode ec = TcapFactory.createErrorCode();
			ec.setLocalErrorCode(mapErrorMessage.getErrorCode());
			returnError.setErrorCode(ec);
			
			AsnOutputStream aos = new AsnOutputStream();
			mapErrorMessage.encodeData(aos);
			byte[] buf = aos.toByteArray();
			if (buf.length != 0) {
				Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
				p.setTagClass(mapErrorMessage.getTagClass());
				p.setPrimitive(mapErrorMessage.getIsPrimitive());
				p.setTag(mapErrorMessage.getTag());
				p.setData(buf);
				returnError.setParameter(p);
			}

			this.tcapDialog.sendComponent(returnError);

		} catch (TCAPSendException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	public void sendRejectComponent(Long invokeId, Problem problem) throws MAPException {

		if (invokeId != null && problem != null && problem.getInvokeProblemType() != null)
			this.removeIncomingInvokeId(invokeId);

		Reject reject = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCRejectRequest();

		try {
			reject.setInvokeId(invokeId);

			// Error Code
			reject.setProblem(problem);

			this.tcapDialog.sendComponent(reject);

		} catch (TCAPSendException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	public void resetInvokeTimer(Long invokeId) throws MAPException {
		try {
			this.getTcapDialog().resetTimer(invokeId);
		} catch( TCAPException e ) {
			throw new MAPException( "TCAPException occure: " + e.getMessage(), e );
		}
	}

	public boolean cancelInvocation(Long invokeId) throws MAPException {
		try {
			return this.getTcapDialog().cancelInvocation(invokeId);
		} catch( TCAPException e ) {
			throw new MAPException( "TCAPException occure: " + e.getMessage(), e );
		}
	}

	public Object getUserObject() {
		return this.userObject;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("DialogId=").append(this.getDialogId()).append("MAPDialogState=").append(this.getState())
				.append("MAPApplicationContext=").append(this.appCntx).append("TCAPDialogState=")
				.append(this.tcapDialog.getState());
		return sb.toString();
	}

}
