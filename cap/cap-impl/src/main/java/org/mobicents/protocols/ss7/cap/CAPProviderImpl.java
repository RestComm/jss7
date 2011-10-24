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

package org.mobicents.protocols.ss7.cap;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPDialogListener;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParameterFactory;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPProvider;
import org.mobicents.protocols.ss7.cap.api.CAPServiceBase;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPComponentErrorReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorCode;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageFactory;
import org.mobicents.protocols.ss7.cap.dialog.CAPGprsReferenceNumberImpl;
import org.mobicents.protocols.ss7.cap.dialog.CAPUserAbortPrimitiveImpl;
import org.mobicents.protocols.ss7.cap.errors.CAPErrorMessageFactoryImpl;
import org.mobicents.protocols.ss7.cap.errors.CAPErrorMessageImpl;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.DialogServiceProviderType;
import org.mobicents.protocols.ss7.tcap.asn.DialogServiceUserType;
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;
import org.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCodeType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class CAPProviderImpl implements CAPProvider, TCListener {

	protected Logger loger = Logger.getLogger(CAPProviderImpl.class);

	private List<CAPDialogListener> dialogListeners = new CopyOnWriteArrayList<CAPDialogListener>();

	protected Map<Long, CAPDialogImpl> dialogs = new HashMap<Long, CAPDialogImpl>();

	private TCAPProvider tcapProvider = null;

	private final CAPParameterFactory capParameterFactory = new CAPParameterFactoryImpl();
	private final CAPErrorMessageFactory capErrorMessageFactory = new CAPErrorMessageFactoryImpl();

	protected Set<CAPServiceBase> capServices = new HashSet<CAPServiceBase>();
//	private final CAPServiceSms capServiceSms = new CAPServiceSmsImpl(this);
	

	public CAPProviderImpl(TCAPProvider tcapProvider) {
		this.tcapProvider = tcapProvider;

//		this.capServices.add(this.capServiceSms);
	}

	public TCAPProvider getTCAPProvider() {
		return this.tcapProvider;
	}

//	public CAPServiceSms getCAPServiceSms() {
//		return this.capServiceSms;
//	}


	@Override
	public void addCAPDialogListener(CAPDialogListener capDialogListener) {
		this.dialogListeners.add(capDialogListener);
	}

	@Override
	public CAPParameterFactory getCAPParameterFactory() {
		return capParameterFactory;
	}
	
	@Override
	public CAPErrorMessageFactory getCAPErrorMessageFactory() {
		return this.capErrorMessageFactory;
	}


	@Override
	public void removeCAPDialogListener(CAPDialogListener capDialogListener) {
		this.dialogListeners.remove(capDialogListener);
	}

	@Override
	public CAPDialog getCAPDialog(Long dialogId) {
		synchronized (this.dialogs) {
			return this.dialogs.get(dialogId);
		}
	}

	public void start() {
		this.tcapProvider.addTCListener(this);
	}

	public void stop() {
		this.tcapProvider.removeTCListener(this);

	}

	protected void addDialog(CAPDialogImpl dialog) {
		synchronized (this.dialogs) {
			this.dialogs.put(dialog.getDialogId(), dialog);
		}
	}

	protected void removeDialog(Long dialogId) {
		synchronized (this.dialogs) {
			this.dialogs.remove(dialogId);
		}
	}

	private void SendUnsupportedAcn(ApplicationContextName acn, Dialog dialog, String cs) {
		StringBuffer s = new StringBuffer();
		s.append(cs + " ApplicationContextName is received: ");
		for (long l : acn.getOid()) {
			s.append(l).append(", ");
		}
		loger.warn(s.toString());
		
		try {
			this.fireTCAbort(dialog, CAPGeneralAbortReason.ACNNotSupported, null);
		} catch (CAPException e1) {
			loger.error("Error while firing TC-U-ABORT. ", e1);
		}
	}

	private CAPGprsReferenceNumberImpl ParseUserInfo(UserInformation userInfo, Dialog dialog) {
		
		// Parsing userInfo
		CAPGprsReferenceNumberImpl referenceNumber = null;
			
		// Checking UserData ObjectIdentifier
		if (!userInfo.isOid()) {
			loger.warn("onTCBegin: userInfo.isOid() is null");
			return null;
		}

		if (!Arrays.equals(CAPGprsReferenceNumberImpl.CAP_Dialogue_OId, userInfo.getOidValue())) {
			loger.warn("onTCBegin: userInfo.isOid() has bad value");
			return null;
		}

		if (!userInfo.isAsn()) {
			loger.warn("onTCBegin: userInfo.isAsn() is null");
			return null;
		}

		try {
			referenceNumber = new CAPGprsReferenceNumberImpl();
			byte[] asnData = userInfo.getEncodeType();

			AsnInputStream ais = new AsnInputStream(asnData);

			int tag = ais.readTag();
			// It should be SEQUENCE Tag
			if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL || ais.isTagPrimitive()) {
				loger.warn("onTCBegin: Error parsing CAPGprsReferenceNumber: bad tag or tag class or is primitive");
				return null;
			}

			referenceNumber.decodeAll(ais);
		} catch (AsnException e) {
			loger.error("AsnException when parsing CAP-OPEN Pdu: " + e.getMessage());
			return null;
		} catch (IOException e) {
			loger.error("IOException when parsing CAP-OPEN Pdu: " + e.getMessage());
			return null;
		} catch (CAPParsingComponentException e) {
			loger.error("CAPParsingComponentException when parsing CAP-OPEN Pdu: " + e.getMessage());
			return null;
		}
			
		return referenceNumber;
	}	
	
	public void onTCBegin(TCBeginIndication tcBeginIndication) {
		
		ApplicationContextName acn = tcBeginIndication.getApplicationContextName();
		Component[] comps = tcBeginIndication.getComponents();

		// ACN must be present in CAMEL
		if (acn == null) {
			loger.warn("onTCBegin: Received TCBeginIndication without application context name");

			try {
				this.fireTCAbort(tcBeginIndication.getDialog(), CAPGeneralAbortReason.BadReceivedData, null);
			} catch (CAPException e) {
				loger.error("Error while firing TC-U-ABORT. ", e);
			}
			return;
		}

		CAPApplicationContext capAppCtx = CAPApplicationContext.getInstance(acn.getOid());
		// Check if ApplicationContext is recognizable for CAP
		// If no - TC-U-ABORT - ACN-Not-Supported
		if (capAppCtx == null) {
			SendUnsupportedAcn(acn, tcBeginIndication.getDialog(), "onTCBegin: Unrecognizable");
			return;
		}

		// Parsing CAPGprsReferenceNumber if exists
		CAPGprsReferenceNumberImpl referenceNumber = null;
		UserInformation userInfo = tcBeginIndication.getUserInformation();
		if (userInfo != null) {
			referenceNumber = ParseUserInfo(userInfo, tcBeginIndication.getDialog());
			if (referenceNumber == null) {
				try {
					this.fireTCAbort(tcBeginIndication.getDialog(), CAPGeneralAbortReason.BadReceivedData, null);
				} catch (CAPException e) {
					loger.error("Error while firing TC-U-ABORT. ", e);
				}
				
				return;
			}
		}

		// Selecting the CAP service that can perform the ApplicationContext
		CAPServiceBase perfSer = null;
		for (CAPServiceBase ser : this.capServices) {

			ServingCheckData chkRes = ser.isServingService(capAppCtx);
			switch (chkRes.getResult()) {
			case AC_Serving:
				perfSer = ser;
				break;

			case AC_VersionIncorrect:
				SendUnsupportedAcn(acn, tcBeginIndication.getDialog(), "onTCBegin: Unsupported");
				return;
			}

			if (perfSer != null)
				break;
		}

		// No CAPService can accept the received ApplicationContextName
		if (perfSer == null) {
			SendUnsupportedAcn(acn, tcBeginIndication.getDialog(), "onTCBegin: Unsupported");
			return;
		}

		// CAPService is not activated
		if (!perfSer.isActivated()) {
			SendUnsupportedAcn(acn, tcBeginIndication.getDialog(), "onTCBegin: Inactive CAPService");
			return;
		}

		CAPDialogImpl capDialogImpl = ((CAPServiceBaseImpl) perfSer).createNewDialogIncoming(capAppCtx, tcBeginIndication.getDialog());
		synchronized (capDialogImpl) {
			this.addDialog(capDialogImpl);

			capDialogImpl.setState(CAPDialogState.InitialReceived);

			this.deliverDialogRequest(capDialogImpl, referenceNumber);
			if (capDialogImpl.getState() == CAPDialogState.Expunged)
				// The Dialog was aborter or refused
				return;

			// Now let us decode the Components
			if (comps != null) {
				processComponents(capDialogImpl, comps);
			}

			this.deliverDialogDelimiter(capDialogImpl);
		}
	}
	
	public void onTCContinue(TCContinueIndication tcContinueIndication) {

		Dialog tcapDialog = tcContinueIndication.getDialog();

		CAPDialogImpl capDialogImpl = (CAPDialogImpl) this.getCAPDialog(tcapDialog.getDialogId());

		if (capDialogImpl == null) {
			loger.warn("CAP Dialog not found for Dialog Id " + tcapDialog.getDialogId());
			try {
				this.fireTCAbort(tcContinueIndication.getDialog(), CAPGeneralAbortReason.BadReceivedData, null);
			} catch (CAPException e) {
				loger.error("Error while firing TC-U-ABORT. ", e);
			}
			return;
		}

		synchronized (capDialogImpl) {
			if (capDialogImpl.getState() == CAPDialogState.InitialSent) {
				ApplicationContextName acn = tcContinueIndication.getApplicationContextName();
				
				if (acn == null) {
					loger.warn("CAP Dialog is in InitialSent state but no application context name is received");
					try {
						this.fireTCAbort(tcContinueIndication.getDialog(), CAPGeneralAbortReason.BadReceivedData, null);
					} catch (CAPException e) {
						loger.error("Error while firing TC-U-ABORT. ", e);
					}

					capDialogImpl.setNormalDialogShutDown();
					this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.AbnormalDialogAction);
					capDialogImpl.setState(CAPDialogState.Expunged);

					return;
				}

				CAPApplicationContext capAcn = CAPApplicationContext.getInstance(acn.getOid());
				if (capAcn == null || !capAcn.equals(capDialogImpl.getApplicationContext())) {
					loger.warn(String.format("Received first TC-CONTINUE. But the received ACN is not the equal to the original ACN"));

					try {
						this.fireTCAbort(tcContinueIndication.getDialog(), CAPGeneralAbortReason.BadReceivedData, null);
					} catch (CAPException e) {
						loger.error("Error while firing TC-U-ABORT. ", e);
					}

					capDialogImpl.setNormalDialogShutDown();
					this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.AbnormalDialogAction);
					capDialogImpl.setState(CAPDialogState.Expunged);

					return;
				}

				// Parsing CAPGprsReferenceNumber if exists
				// we ignore all errors this
				CAPGprsReferenceNumberImpl referenceNumber = null;
				UserInformation userInfo = tcContinueIndication.getUserInformation();
				if (userInfo != null) {
					referenceNumber = ParseUserInfo(userInfo, tcContinueIndication.getDialog());
				}

				capDialogImpl.setState(CAPDialogState.Active);
				this.deliverDialogAccept(capDialogImpl, referenceNumber);

				if (capDialogImpl.getState() == CAPDialogState.Expunged)
					// The Dialog was aborter
					return;
			}

			// Now let us decode the Components
			if (capDialogImpl.getState() == CAPDialogState.Active) {
				Component[] comps = tcContinueIndication.getComponents();
				if (comps != null) {
					processComponents(capDialogImpl, comps);
				}
			} else {
				// This should never happen
				loger.error(String.format("Received TC-CONTINUE. CAPDialog=%s. But state is not Active", capDialogImpl));
			}

			this.deliverDialogDelimiter(capDialogImpl);
		}
	}	
	
	public void onTCEnd(TCEndIndication tcEndIndication) {

		Dialog tcapDialog = tcEndIndication.getDialog();

		CAPDialogImpl capDialogImpl = (CAPDialogImpl) this.getCAPDialog(tcapDialog.getDialogId());

		if (capDialogImpl == null) {
			loger.warn("CAP Dialog not found for Dialog Id " + tcapDialog.getDialogId());
			try {
				this.fireTCAbort(tcEndIndication.getDialog(), CAPGeneralAbortReason.BadReceivedData, null);
			} catch (CAPException e) {
				loger.error("Error while firing TC-U-ABORT. ", e);
			}
			return;
		}

		synchronized (capDialogImpl) {
			if (capDialogImpl.getState() == CAPDialogState.InitialSent) {
				ApplicationContextName acn = tcEndIndication.getApplicationContextName();

				if (acn == null) {
					loger.warn("CAP Dialog is in InitialSent state but no application context name is received");
					try {
						this.fireTCAbort(tcEndIndication.getDialog(), CAPGeneralAbortReason.BadReceivedData, null);
					} catch (CAPException e) {
						loger.error("Error while firing TC-U-ABORT. ", e);
					}

					capDialogImpl.setNormalDialogShutDown();
					this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.AbnormalDialogAction);
					capDialogImpl.setState(CAPDialogState.Expunged);

					return;
				}
				
				CAPApplicationContext capAcn = CAPApplicationContext.getInstance(acn.getOid());

				if (capAcn == null || !capAcn.equals(capDialogImpl.getApplicationContext())) {
					loger.error(String.format("Received first TC-END. CAPDialog=%s. But CAPApplicationContext=%s", capDialogImpl, capAcn));

					capDialogImpl.setNormalDialogShutDown();
					this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.AbnormalDialogAction);
					capDialogImpl.setState(CAPDialogState.Expunged);

					return;
				}

				capDialogImpl.setState(CAPDialogState.Active);

				// Parsing CAPGprsReferenceNumber if exists
				// we ignore all errors this
				CAPGprsReferenceNumberImpl referenceNumber = null;
				UserInformation userInfo = tcEndIndication.getUserInformation();
				if (userInfo != null) {
					referenceNumber = ParseUserInfo(userInfo, tcEndIndication.getDialog());
				}

				this.deliverDialogAccept(capDialogImpl, referenceNumber);
				if (capDialogImpl.getState() == CAPDialogState.Expunged)
					// The Dialog was aborter
					return;
			}

			// Now let us decode the Components
			Component[] comps = tcEndIndication.getComponents();
			if (comps != null) {
				processComponents(capDialogImpl, comps);
			}

			capDialogImpl.setNormalDialogShutDown();
			this.deliverDialogClose(capDialogImpl);
			capDialogImpl.setState(CAPDialogState.Expunged);
		}
	}	

	public void onTCUni(TCUniIndication arg0) {
	}
	
	@Override
	public void onInvokeTimeout(Invoke invoke) {

		CAPDialogImpl capDialogImpl = (CAPDialogImpl) this.getCAPDialog(((InvokeImpl) invoke).getDialog().getDialogId());

		if (capDialogImpl != null) {
			synchronized (capDialogImpl) {
				if (capDialogImpl.getState() != CAPDialogState.Expunged && !capDialogImpl.getNormalDialogShutDown()) {

					// Getting the CAP Service that serves the CAP Dialog
					CAPServiceBaseImpl perfSer = (CAPServiceBaseImpl)capDialogImpl.getService();
					
					// Check if the InvokeTimeout in this situation is normal (may be for a class 2,3,4 components)
					// TODO: ................................
					
					perfSer.deliverInvokeTimeout(capDialogImpl, invoke);
				}
			}
		}
	}

	@Override
	public void onDialogTimeout(Dialog tcapDialog) {
		
		CAPDialogImpl capDialogImpl = (CAPDialogImpl) this.getCAPDialog(tcapDialog.getDialogId());

		if (capDialogImpl != null) {
			synchronized (capDialogImpl) {
				if (capDialogImpl.getState() != CAPDialogState.Expunged && !capDialogImpl.getNormalDialogShutDown()) {

					this.deliverDialogTimeout(capDialogImpl);
				}
			}
		}
	}

	@Override
	public void onDialogReleased(Dialog tcapDialog) {

		CAPDialogImpl capDialogImpl = (CAPDialogImpl) this.getCAPDialog(tcapDialog.getDialogId());

		if (capDialogImpl != null) {
			synchronized (capDialogImpl) {
				if (capDialogImpl.getState() != CAPDialogState.Expunged && !capDialogImpl.getNormalDialogShutDown()) {

					// TCAP Dialog is destroyed when CapDialog is alive and not shutting down
					capDialogImpl.setNormalDialogShutDown();
					this.deliverDialogUserAbort(capDialogImpl, CAPGeneralAbortReason.BadReceivedData, null);
					
					capDialogImpl.setState(CAPDialogState.Expunged);
				}
			}
		}
	}

	public void onTCPAbort(TCPAbortIndication tcPAbortIndication) {
		Dialog tcapDialog = tcPAbortIndication.getDialog();

		CAPDialogImpl capDialogImpl = (CAPDialogImpl) this.getCAPDialog(tcapDialog.getDialogId());

		if (capDialogImpl == null) {
			loger.warn("CAP Dialog not found for Dialog Id " + tcapDialog.getDialogId());
			return;
		}

		synchronized (capDialogImpl) {
			PAbortCauseType pAbortCause = tcPAbortIndication.getPAbortCause();

			capDialogImpl.setNormalDialogShutDown();
			this.deliverDialogProviderAbort(capDialogImpl, pAbortCause);

			capDialogImpl.setState(CAPDialogState.Expunged);
		}
	}

	public void onTCUserAbort(TCUserAbortIndication tcUserAbortIndication) {
		
		Dialog tcapDialog = tcUserAbortIndication.getDialog();

		CAPDialogImpl capDialogImpl = (CAPDialogImpl) this.getCAPDialog(tcapDialog.getDialogId());

		if (capDialogImpl == null) {
			loger.error("CAP Dialog not found for Dialog Id " + tcapDialog.getDialogId());
			return;
		}

		synchronized (capDialogImpl) {
			CAPGeneralAbortReason generalReason = CAPGeneralAbortReason.BadReceivedData;
			CAPUserAbortReason userReason = null;

			if (tcUserAbortIndication.IsAareApdu()) {
				if (capDialogImpl.getState() == CAPDialogState.InitialSent) {
					generalReason = CAPGeneralAbortReason.DialogRefused;
					ResultSourceDiagnostic resultSourceDiagnostic = tcUserAbortIndication.getResultSourceDiagnostic();
					if (resultSourceDiagnostic != null) {
						if (resultSourceDiagnostic.getDialogServiceUserType() == DialogServiceUserType.AcnNotSupported) {
							generalReason = CAPGeneralAbortReason.ACNNotSupported;
						} else if (resultSourceDiagnostic.getDialogServiceProviderType() == DialogServiceProviderType.NoCommonDialogPortion) {
							generalReason = CAPGeneralAbortReason.NoCommonDialogPortionReceived;
						}
					}
				}
			} else {
				UserInformation userInfo = tcUserAbortIndication.getUserInformation();

				if (userInfo != null) {
					// Checking userInfo.Oid==CAPUserAbortPrimitiveImpl.CAP_AbortReason_OId
					if (!userInfo.isOid()) {
						loger.warn("When parsing TCUserAbortIndication indication: userInfo.isOid() is null");
					} else {
						if (Arrays.equals(userInfo.getOidValue(), CAPUserAbortPrimitiveImpl.CAP_AbortReason_OId)) {
							loger.warn("When parsing TCUserAbortIndication indication: userInfo.getOidValue() must be CAPUserAbortPrimitiveImpl.CAP_AbortReason_OId");
						} else if (!userInfo.isAsn()) {
							loger.warn("When parsing TCUserAbortIndication indication: userInfo.isAsn() check failed");
						} else {
							try {
								byte[] asnData = userInfo.getEncodeType();

								AsnInputStream ais = new AsnInputStream(asnData);

								int tag = ais.readTag();
								if (tag != Tag.ENUMERATED || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive()) {
									loger.warn("When parsing TCUserAbortIndication indication: userInfo has bad tag or tagClass or is not primitive");
								} else {
									CAPUserAbortPrimitiveImpl capUserAbortPrimitive = new CAPUserAbortPrimitiveImpl(); 
									capUserAbortPrimitive.decodeAll(ais);
									generalReason = CAPGeneralAbortReason.UserSpecific;
									userReason = capUserAbortPrimitive.getCAPUserAbortReason();
								}
							} catch (AsnException e) {
								loger.warn("When parsing TCUserAbortIndication indication: AsnException" + e.getMessage(), e);
							} catch (IOException e) {
								loger.warn("When parsing TCUserAbortIndication indication: IOException" + e.getMessage(), e);
							} catch (CAPParsingComponentException e) {
								loger.warn("When parsing TCUserAbortIndication indication: CAPParsingComponentException" + e.getMessage(), e);
							}
						}
					}
				}
			}

			capDialogImpl.setNormalDialogShutDown();
			this.deliverDialogUserAbort(capDialogImpl, generalReason, userReason);

			capDialogImpl.setState(CAPDialogState.Expunged);
		}
	}

	private void processComponents(CAPDialogImpl capDialogImpl, Component[] components) {

		// Getting the CAP Service that serves the CAP Dialog
		CAPServiceBaseImpl perfSer = (CAPServiceBaseImpl)capDialogImpl.getService();

		// Now let us decode the Components
		for (Component c : components) {

			try {
				ComponentType compType = c.getType();

				Long invokeId = c.getInvokeId();

				Parameter parameter;
				OperationCode oc;
				Long linkedId = 0L;
				
				switch (compType) {
				case Invoke: {
					Invoke comp = (Invoke) c;
					oc = comp.getOperationCode();
					parameter = comp.getParameter();
					linkedId = comp.getLinkedId();
					
					// Checking if the invokeId is not duplicated
					if (!capDialogImpl.addIncomingInvokeId(invokeId)) {
						this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.DuplicatedInvokeIdReceived);
						
						Problem problem = this.getTCAPProvider().getComponentPrimitiveFactory().createProblem(ProblemType.Invoke);
						problem.setInvokeProblemType(InvokeProblemType.DuplicateInvokeID);
						capDialogImpl.sendRejectComponent(null, problem);

						return;
					}
					
					if (linkedId != null) {
						// linkedId exists Checking if the linkedId exists
						if (!capDialogImpl.checkIncomingInvokeIdExists(linkedId)) {
							this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.UnknownLinkedIdReceived);

							Problem problem = this.getTCAPProvider().getComponentPrimitiveFactory().createProblem(ProblemType.Invoke);
							problem.setInvokeProblemType(InvokeProblemType.UnrechognizedLinkedID);
							capDialogImpl.sendRejectComponent(invokeId, problem);

							return;
						}
					}
				}
					break;

				case ReturnResult: {
					// ReturnResult is not supported by CAMEL
					this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.AbnormalComponentReceivedFromThePeer);
					
					Problem problem = this.getTCAPProvider().getComponentPrimitiveFactory().createProblem(ProblemType.Invoke);
					problem.setInvokeProblemType(InvokeProblemType.UnrecognizedOperation);
					capDialogImpl.sendRejectComponent(null, problem);

					return;
				}

				case ReturnResultLast: {
					ReturnResultLast comp = (ReturnResultLast) c;
					oc = comp.getOperationCode();
					parameter = comp.getParameter();
				}
					break;

				case ReturnError: {
					ReturnError comp = (ReturnError) c;
					
					long errorCode = 0;
					if (comp.getErrorCode() != null && comp.getErrorCode().getErrorType() == ErrorCodeType.Local)
						errorCode = comp.getErrorCode().getLocalErrorCode();
					if (errorCode < CAPErrorCode.minimalCodeValue || errorCode > CAPErrorCode.maximumCodeValue) {
						// Not Local error code and not CAP error code received
						perfSer.deliverProviderErrorComponent(capDialogImpl, invokeId, CAPComponentErrorReason.InvalidErrorComponentReceived);

						Problem problem = this.getTCAPProvider().getComponentPrimitiveFactory().createProblem(ProblemType.ReturnError);
						problem.setReturnErrorProblemType(ReturnErrorProblemType.UnrecognizedError);
						capDialogImpl.sendRejectComponent(invokeId, problem);
						
						return;
					}
					
					CAPErrorMessage msgErr = this.capErrorMessageFactory.createMessageFromErrorCode(errorCode);
					try {
						Parameter p = comp.getParameter();
						if (p != null && p.getData() != null) {
							byte[] data = p.getData();
							AsnInputStream ais = new AsnInputStream(data, p.getTagClass(), p.isPrimitive(), p.getTag());
							((CAPErrorMessageImpl)msgErr).decodeData(ais, data.length);
						}
					} catch ( CAPParsingComponentException e) {
						// Failed when parsing the component - send TC-U-REJECT
						perfSer.deliverProviderErrorComponent(capDialogImpl, invokeId, CAPComponentErrorReason.InvalidErrorComponentReceived);

						Problem problem = this.getTCAPProvider().getComponentPrimitiveFactory().createProblem(ProblemType.ReturnError);
						problem.setReturnErrorProblemType(ReturnErrorProblemType.MistypedParameter);
						capDialogImpl.sendRejectComponent(invokeId, problem);

						return;
					}
					perfSer.deliverErrorComponent(capDialogImpl, comp.getInvokeId(), msgErr);
					
					return;
				}

				case Reject: {
					Reject comp = (Reject) c;
					perfSer.deliverRejectComponent(capDialogImpl, comp.getInvokeId(), comp.getProblem());
					
					return;
				}
				
				default:
					return;
				}
				
				try {
					
					perfSer.processComponent(compType, oc, parameter, capDialogImpl, invokeId, linkedId);
					
				} catch (CAPParsingComponentException e) {
					
					loger.error("CAPParsingComponentException when parsing components: " + e.getReason().toString() + " - " + e.getMessage(), e);
					
					switch (e.getReason()) {
					case UnrecognizedOperation:
						// Component does not supported - send TC-U-REJECT
						if (compType == ComponentType.Invoke) {
							this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.UnrecognizedOperation);

							Problem problem = this.getTCAPProvider().getComponentPrimitiveFactory().createProblem(ProblemType.Invoke);
							problem.setInvokeProblemType(InvokeProblemType.UnrecognizedOperation);
							capDialogImpl.sendRejectComponent(invokeId, problem);
						} else {
							perfSer.deliverProviderErrorComponent(capDialogImpl, invokeId, CAPComponentErrorReason.UnrecognizedOperation);
							
							Problem problem = this.getTCAPProvider().getComponentPrimitiveFactory().createProblem(ProblemType.ReturnResult);
							problem.setReturnResultProblemType(ReturnResultProblemType.MistypedParameter);
							capDialogImpl.sendRejectComponent(invokeId, problem);
						}
						break;

					case MistypedParameter:
						// Failed when parsing the component - send TC-U-REJECT
						if (compType == ComponentType.Invoke) {
							this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.AbnormalComponentReceivedFromThePeer);
							
							Problem problem = this.getTCAPProvider().getComponentPrimitiveFactory().createProblem(ProblemType.Invoke);
							problem.setInvokeProblemType(InvokeProblemType.MistypedParameter);
							capDialogImpl.sendRejectComponent(invokeId, problem);
						} else {
							if (compType == ComponentType.Reject)
								perfSer.deliverProviderErrorComponent(capDialogImpl, invokeId, CAPComponentErrorReason.InvalidRejectReceived);
							else
								perfSer.deliverProviderErrorComponent(capDialogImpl, invokeId, CAPComponentErrorReason.InvalidReturnResultComponentReceived);
							
							Problem problem = this.getTCAPProvider().getComponentPrimitiveFactory().createProblem(ProblemType.ReturnResult);
							problem.setReturnResultProblemType(ReturnResultProblemType.MistypedParameter);
							capDialogImpl.sendRejectComponent(invokeId, problem);
						}
						break;

					case LinkedResponseUnexpected:
						// Failed when parsing the component - send TC-U-REJECT
						if (compType == ComponentType.Invoke) {
							this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.LinkedResponseUnexpected);
							
							Problem problem = this.getTCAPProvider().getComponentPrimitiveFactory().createProblem(ProblemType.Invoke);
							problem.setInvokeProblemType(InvokeProblemType.LinkedResponseUnexpected);
							capDialogImpl.sendRejectComponent(invokeId, problem);
						}
						break;

					case UnexpectedLinkedOperation:
						// Failed when parsing the component - send TC-U-REJECT
						if (compType == ComponentType.Invoke) {
							this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.UnexpectedLinkedOperation);
							
							Problem problem = this.getTCAPProvider().getComponentPrimitiveFactory().createProblem(ProblemType.Invoke);
							problem.setInvokeProblemType(InvokeProblemType.UnexpectedLinkedOperation);
							capDialogImpl.sendRejectComponent(invokeId, problem);
						}
						break;
					}

				}
			} catch (CAPException e) {
				loger.error("Error sending the RejectComponent: " + e.getMessage(), e);
			}
		} 
	}

	private void deliverDialogDelimiter(CAPDialog capDialog) {
		for (CAPDialogListener listener : this.dialogListeners) {
			listener.onDialogDelimiter(capDialog);
		}
	}

	private void deliverDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
		for (CAPDialogListener listener : this.dialogListeners) {
			listener.onDialogRequest(capDialog, capGprsReferenceNumber);
		}
	}

	private void deliverDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
		for (CAPDialogListener listener : this.dialogListeners) {
			listener.onDialogAccept(capDialog, capGprsReferenceNumber);
		}
	}

	private void deliverDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason, CAPUserAbortReason userReason) {
		for (CAPDialogListener listener : this.dialogListeners) {
			listener.onDialogUserAbort(capDialog, generalReason, userReason);
		}
	}

	private void deliverDialogProviderAbort(CAPDialog capDialog, PAbortCauseType abortCause) {
		for (CAPDialogListener listener : this.dialogListeners) {
			listener.onDialogProviderAbort(capDialog, abortCause);
		}
	}

	private void deliverDialogClose(CAPDialog capDialog) {
		for (CAPDialogListener listener : this.dialogListeners) {
			listener.onDialogClose(capDialog);
		}
	}

	protected void deliverDialogResease(CAPDialog capDialog) {
		for (CAPDialogListener listener : this.dialogListeners) {
			listener.onDialogResease(capDialog);
		}
	}

	protected void deliverDialogTimeout(CAPDialog capDialog) {
		for (CAPDialogListener listener : this.dialogListeners) {
			listener.onDialogTimeout(capDialog);
		}
	}
	
	protected void deliverDialogNotice(CAPDialog capDialog, CAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		for (CAPDialogListener listener : this.dialogListeners) {
			listener.onDialogNotice(capDialog, noticeProblemDiagnostic);
		}
	}

	protected void fireTCBegin(Dialog tcapDialog, ApplicationContextName acn, CAPGprsReferenceNumber gprsReferenceNumber) throws CAPException {

		TCBeginRequest tcBeginReq = encodeTCBegin(tcapDialog, acn, gprsReferenceNumber);

		try {
			tcapDialog.send(tcBeginReq);
		} catch (TCAPSendException e) {
			throw new CAPException(e.getMessage(), e);
		}

	}

	protected TCBeginRequest encodeTCBegin(Dialog tcapDialog, ApplicationContextName acn, CAPGprsReferenceNumber gprsReferenceNumber) throws CAPException {
		
		TCBeginRequest tcBeginReq = this.getTCAPProvider().getDialogPrimitiveFactory().createBegin(tcapDialog);

		tcBeginReq.setApplicationContextName(acn);

		if (gprsReferenceNumber != null) {
			AsnOutputStream localasnOs = new AsnOutputStream();
			((CAPGprsReferenceNumberImpl)gprsReferenceNumber).encodeAll(localasnOs);

			UserInformation userInformation = TcapFactory.createUserInformation();

			userInformation.setOid(true);
			userInformation.setOidValue(CAPGprsReferenceNumberImpl.CAP_Dialogue_OId);

			userInformation.setAsn(true);
			userInformation.setEncodeType(localasnOs.toByteArray());

			tcBeginReq.setUserInformation(userInformation);
		}
		return tcBeginReq;
	}

	protected void fireTCContinue(Dialog tcapDialog, ApplicationContextName acn, CAPGprsReferenceNumber gprsReferenceNumber)
			throws CAPException {

		TCContinueRequest tcContinueReq = encodeTCContinue(tcapDialog, acn, gprsReferenceNumber);

		try {
			tcapDialog.send(tcContinueReq);
		} catch (TCAPSendException e) {
			throw new CAPException(e.getMessage(), e);
		}
	}

	protected TCContinueRequest encodeTCContinue(Dialog tcapDialog, ApplicationContextName acn, CAPGprsReferenceNumber gprsReferenceNumber) throws CAPException {
		TCContinueRequest tcContinueReq = this.getTCAPProvider().getDialogPrimitiveFactory().createContinue(tcapDialog);

		if (acn != null)
			tcContinueReq.setApplicationContextName(acn);

		if (gprsReferenceNumber != null) {

			AsnOutputStream localasnOs = new AsnOutputStream();
			((CAPGprsReferenceNumberImpl)gprsReferenceNumber).encodeAll(localasnOs);

			UserInformation userInformation = TcapFactory.createUserInformation();

			userInformation.setOid(true);
			userInformation.setOidValue(CAPGprsReferenceNumberImpl.CAP_Dialogue_OId);

			userInformation.setAsn(true);
			userInformation.setEncodeType(localasnOs.toByteArray());

			tcContinueReq.setUserInformation(userInformation);
		}
		return tcContinueReq;
	}

	protected void fireTCEnd(Dialog tcapDialog, boolean prearrangedEnd, ApplicationContextName acn, CAPGprsReferenceNumber gprsReferenceNumber)
			throws CAPException {

		TCEndRequest endRequest = encodeTCEnd(tcapDialog, prearrangedEnd, acn, gprsReferenceNumber);

		try {
			tcapDialog.send(endRequest);
		} catch (TCAPSendException e) {
			throw new CAPException(e.getMessage(), e);
		}
	}

	protected TCEndRequest encodeTCEnd(Dialog tcapDialog, boolean prearrangedEnd, ApplicationContextName acn, CAPGprsReferenceNumber gprsReferenceNumber) throws CAPException {
		TCEndRequest endRequest = this.getTCAPProvider().getDialogPrimitiveFactory().createEnd(tcapDialog);

		if (!prearrangedEnd) {
			endRequest.setTermination(TerminationType.Basic);
		} else {
			endRequest.setTermination(TerminationType.PreArranged);
		}

		if (acn != null)
			endRequest.setApplicationContextName(acn);

		if (gprsReferenceNumber != null) {

			AsnOutputStream localasnOs = new AsnOutputStream();
			((CAPGprsReferenceNumberImpl)gprsReferenceNumber).encodeAll(localasnOs);

			UserInformation userInformation = TcapFactory.createUserInformation();

			userInformation.setOid(true);
			userInformation.setOidValue(CAPGprsReferenceNumberImpl.CAP_Dialogue_OId);

			userInformation.setAsn(true);
			userInformation.setEncodeType(localasnOs.toByteArray());

			endRequest.setUserInformation(userInformation);
		}
		return endRequest;
	}

	protected void fireTCAbort(Dialog tcapDialog, CAPGeneralAbortReason generalAbortReason, CAPUserAbortReason userAbortReason) throws CAPException {

		TCUserAbortRequest tcUserAbort = this.getTCAPProvider().getDialogPrimitiveFactory().createUAbort(tcapDialog);

		switch(generalAbortReason){
		case ACNNotSupported:
			tcUserAbort.setDialogServiceUserType(DialogServiceUserType.AcnNotSupported);
			tcUserAbort.setApplicationContextName(tcapDialog.getApplicationContextName());
			break;
			
		case UserSpecific:
			if (userAbortReason == null)
				userAbortReason = CAPUserAbortReason.no_reason_given;
			CAPUserAbortPrimitiveImpl abortReasonPrimitive = new CAPUserAbortPrimitiveImpl(userAbortReason);
			AsnOutputStream localasnOs = new AsnOutputStream();
			abortReasonPrimitive.encodeAll(localasnOs);

			UserInformation userInformation = TcapFactory.createUserInformation();
			userInformation.setOid(true);
			userInformation.setOidValue(CAPUserAbortPrimitiveImpl.CAP_AbortReason_OId);
			userInformation.setAsn(true);
			userInformation.setEncodeType(localasnOs.toByteArray());

			tcUserAbort.setUserInformation(userInformation);
			break;
			
		case DialogRefused:
		default:
			tcUserAbort.setDialogServiceUserType(DialogServiceUserType.NoReasonGive);
			tcUserAbort.setApplicationContextName(tcapDialog.getApplicationContextName());
			break;
		}

		try {
			tcapDialog.send(tcUserAbort);
		} catch (TCAPSendException e) {
			throw new CAPException(e.getMessage(), e);
		}
	}
}

