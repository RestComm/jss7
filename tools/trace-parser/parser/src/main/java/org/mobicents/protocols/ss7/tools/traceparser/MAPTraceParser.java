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

package org.mobicents.protocols.ss7.tools.traceparser;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.CAPDialogImpl;
import org.mobicents.protocols.ss7.cap.CAPProviderImpl;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPDialogListener;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsCommandTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsDeliverReportTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsDeliverTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsStatusReportTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsSubmitTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserData;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SegmentationImpl;
import org.mobicents.protocols.ss7.sccp.message.UnitData;
import org.mobicents.protocols.ss7.sccp.message.XUnitData;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.DialogAPDU;
import org.mobicents.protocols.ss7.tcap.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDU;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCEndMessage;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MAPTraceParser implements TraceReaderListener, MAPDialogListener, CAPDialogListener, Runnable, ProcessControl, MAPServiceSmsListener {

	private Ss7ParseParameters par;
	private Thread t;
	private boolean taskIsFinished = false;
	private boolean needInterrupt = false;
	private String errorMessage = null;
	private PrintWriter pw;
	private int msgCount;

	private TraceReaderDriver driver;
	private TCAPProviderImplWrapper tcapProvider;
	private TCAPStackImplWrapper tcapStack;
	private SccpProviderWrapper sccpProvider;
	private MAPProviderImpl mapProvider;
	private CAPProviderImpl capProvider;
	private MessageFactoryImpl msgFact = new MessageFactoryImpl(false); 

	private Map<Integer, Map<Long, DialogImplWrapper>> dialogs = new HashMap<Integer, Map<Long, DialogImplWrapper>>();
	private long dialogEnumerator = 0;
	private long tcapLogMsg = 0;
	

	public MAPTraceParser(Ss7ParseParameters par) {
		this.par = par;
	}
	
	public void parse() {
		this.t = new Thread(this);
		this.t.start();
	}

	@Override
	public void run() {
		
		String filePath = this.par.getSourceFilePath();
		
		switch (this.par.getFileTypeN()) {
		case Acterna:
			this.driver = new TraceReaderDriverActerna(this, filePath);
			break;
		case SimpleSeq:
			this.driver = new TraceReaderDriverSimpleSeq(this, filePath);
			break;
		case Pcap:
			this.driver = new TraceReaderDriverPcap(this, filePath);
			break;
		default:
			this.setFinishedState("Unknown TraceReaderDriver: " + this.par.getFileTypeN());
			return;
		}
		
		try {
			if( this.checkNeedInterrupt() )
				return;			
			
			// opening message log file
			String logFileName = par.getMsgLogFilePath();
			if (logFileName != null && !logFileName.equals("")) {
				try {
					FileOutputStream fos = new FileOutputStream(logFileName);
					pw = new PrintWriter(fos);
				} catch (Exception e) {
					e.printStackTrace();
					this.setFinishedState("Exception while opening th message log file:\nFileName=" + logFileName + "\nMessage=" + e.getMessage());
					return;
				}
			}
			
			this.xLst.clear();

			this.sccpProvider = new SccpProviderWrapper();
			this.tcapStack = new TCAPStackImplWrapper(this.sccpProvider, 1);
			this.tcapProvider = (TCAPProviderImplWrapper) this.tcapStack.getProvider();

			this.mapProvider = new MAPProviderImpl(this.tcapProvider);

			this.mapProvider.getMAPServiceSupplementary().acivate();
			this.mapProvider.getMAPServiceSms().acivate();
			this.mapProvider.getMAPServiceLsm().acivate();

			
			this.mapProvider.addMAPDialogListener(this);
			this.mapProvider.getMAPServiceSms().addMAPServiceListener((MAPServiceSmsListener) this);

			this.capProvider = new CAPProviderImpl(this.tcapProvider);

			this.capProvider.getCAPServiceCircuitSwitchedCall().acivate();
			this.capProvider.getCAPServiceGprs().acivate();
			this.capProvider.getCAPServiceSms().acivate();

			this.tcapStack.start();
			if (this.par.getParseProtocol() == ParseProtocol.Map)
				this.mapProvider.start();
			else
				this.capProvider.start();
			
			this.capProvider.addCAPDialogListener(this);
			
			this.driver.addTraceListener(this);
			
			if( this.checkNeedInterrupt() )
				return;			
			
			this.driver.startTraceFile();			
			
			this.setFinishedState(null);

		} catch (Exception e) {
			e.printStackTrace();
			this.setFinishedState("Exception while parsing: " + e.getMessage());
		}
		finally {
			if (this.pw != null)
				this.pw.close();
			if (this.mapProvider != null)
				this.mapProvider.stop();
			if (this.capProvider != null)
				this.capProvider.stop();
			if (this.tcapStack != null)
				this.tcapStack.stop();
			if (this.driver != null)
				this.driver.removeTraceListener(this);
		}
	}
	
	private void setFinishedState(String errorMessage) {
		if (errorMessage != null)
			this.errorMessage = errorMessage;
		this.taskIsFinished = true;
	}
	

	@Override
	public void ss7Message(byte[] data) throws TraceReaderException {
		this.msgCount++;
		
		if (data == null || data.length < 5) {
			throw new TraceReaderException("Too little data in the raw data");
		}
		
		try {
			ByteArrayInputStream in0 = new ByteArrayInputStream(data);
			DataInputStream in = new DataInputStream(in0);

			int b = in.read();
			int bsn = b & 0x7F;
			int bib = (b & 0x80) >>> 7;
			b = in.read();
			int fsn = b & 0x7F;
			int fib = (b & 0x80) >>> 7;
			int li = in.read() & 0x3F;
			if (li < 2) {
				// not MSU - LSSU or FISU - skip
				return;
			}

			int sio = in.read();
			int si = sio & 0x0F;
			int ni = sio >>> 6;
			int prioriy = (sio & 0x30) >>> 4;
			if (si == 3) {
				// sccp message
				byte b1 = in.readByte();
				byte b2 = in.readByte();
				byte b3 = in.readByte();
				byte b4 = in.readByte();

				int dpc = ((b2 & 0x3f) << 8) | (b1 & 0xff);
				int opc = ((b4 & 0x0f) << 10) | ((b3 & 0xff) << 2) | ((b2 & 0xc0) >> 6);
				int sls = ((b4 & 0xf0) >> 4);

				int type = in.readUnsignedByte();
				SccpMessageImpl msg = msgFact.createMessage(type, in);
				if (msg != null) {
					msg.setDpc(dpc);
					msg.setOpc(opc);
					msg.setSls(sls);

//					this.tcapProvider.onMessage(msg, 0);
					this.onMessage(msg, 0);
				} else {
					// unknown sccp message type
					return;
				}

			} else {
				// other Service Indicator
				return;
			}
		} catch (IOException e) {
			throw new TraceReaderException("IOException: " + e.getMessage(), e);
		}
	}

	HashMap<Long, XUnitDataDef> xLst = new HashMap<Long, XUnitDataDef>();

	private class XUnitDataDef {
		public int remainingSegm;
		public ArrayList<byte[]> dLst = new ArrayList<byte[]>();
	}

	public void onMessage(SccpMessageImpl message, int seqControl) {
		try {
			byte[] data = null;
			SccpAddress localAddress = null;
			SccpAddress remoteAddress = null;

			if (message.getType() == UnitData.MESSAGE_TYPE) {
				data = ((UnitData) message).getData();
				localAddress = ((UnitData) message).getCalledPartyAddress();
				remoteAddress = ((UnitData) message).getCallingPartyAddress();
			} else if (message.getType() == XUnitData.MESSAGE_TYPE) {
				XUnitData xMsg = (XUnitData) message;
				data = xMsg.getData();
				localAddress = xMsg.getCalledPartyAddress();
				remoteAddress = xMsg.getCallingPartyAddress();

				SegmentationImpl sgm = (SegmentationImpl)xMsg.getSegmentation();
				if (sgm != null && (!sgm.isFirstSegIndication() || sgm.getRemainingSegments() > 0)) {
					XUnitDataDef msgDef;
					long ind = 0;
					for (byte bt : sgm.getSegmentationLocalRef()) {
						ind = (ind << 8) + (bt & 0xFF);
					}
					if (!sgm.isFirstSegIndication()) {
						msgDef = xLst.get(ind);
						if (msgDef == null)
							return;
						if (msgDef.remainingSegm - 1 != sgm.getRemainingSegments()) {
							xLst.remove(ind);
							return;
						}
					} else {
						msgDef = new XUnitDataDef();
						xLst.put(ind, msgDef);
					}
					msgDef.remainingSegm = sgm.getRemainingSegments();
					msgDef.dLst.add(data);
					
					if (sgm.getRemainingSegments() == 0) {
						int ln = 0;
						for (byte[] buf : msgDef.dLst) {
							ln += buf.length;
						}
						data = new byte[ln];
						int offs = 0;
						for (byte[] buf : msgDef.dLst) {
							System.arraycopy(buf, 0, data, offs, buf.length);
							offs += buf.length;
						}
						xLst.remove(ind);
					} else {
						return;
					}
				}

			} else {
				return;
			}

			// asnData - it should pass
			AsnInputStream ais = new AsnInputStream(data);
			AsnInputStream aisMsg = new AsnInputStream(data);

			// this should have TC message tag :)
			int tag = ais.readTag();

			switch (tag) {
			// continue first, usually we will get more of those. small perf
			// boost
			case TCContinueMessage._TAG: {
				TCContinueMessage tcm = TcapFactory.createTCContinueMessage(ais);
				// received continue, destID == localDialogId(originatingTxId of
				// begin);
				long originatingTransactionId = tcm.getOriginatingTransactionId();
				long destinationTransactionId = tcm.getDestinationTransactionId();
				int dpc = message.getDpc();
				Map<Long, DialogImplWrapper> dpcData = this.dialogs.get(dpc);
				int acnValue = 0;
				int acnVersion = 0;
				if (dpcData != null) {
					
					
					// TODO: remove it
					if (destinationTransactionId == 3452) {
						destinationTransactionId = 5048;
					}
					// TODO: remove it
					
					
					DialogImplWrapper di = dpcData.get(destinationTransactionId);
					if (di != null) {
						int opc = message.getOpc();
						Map<Long, DialogImplWrapper> opcData = this.dialogs.get(opc);
						if (opcData == null) {
							opcData = new HashMap<Long, DialogImplWrapper>();
							this.dialogs.put(opc, opcData);
						}
						opcData.put(originatingTransactionId, di);

						acnValue = di.getAcnValue();
						acnVersion = di.getAcnVersion();

						di.SetStateActive();

						Integer applicationContextFilter = par.getApplicationContextFilter();
						if (applicationContextFilter != null && applicationContextFilter != acnValue)
							return;

						if (!CheckDialogIdFilter(originatingTransactionId, destinationTransactionId))
							return;

						di.curOpc = message.getOpc();
						di.processContinue(tcm, localAddress, remoteAddress);

						if (this.pw != null) {
//							this.pw.print("TC-CONTINUE: OPC=" + message.getOpc() + ", DPC=" + message.getDpc() + ", originatingTransactionId="
//									+ originatingTransactionId + ", destinationTransactionId=" + destinationTransactionId);
							this.pw.print("TC-CONTINUE: ");
							this.printAddresses(message, originatingTransactionId, destinationTransactionId);
							if (this.par.getTcapMsgData()) {
								LogDataTag(aisMsg, "Continue", tcm.getComponent(), acnValue, acnVersion, tcm.getDialogPortion());
							}
							
//							this.LogComponents(tcm.getComponent(), acnValue, acnVersion, comp);
						}
					}
				}

				break;
			}

			case TCBeginMessage._TAG: {
				TCBeginMessage tcb = TcapFactory.createTCBeginMessage(ais);
				DialogImplWrapper di = new DialogImplWrapper(localAddress, remoteAddress, ++this.dialogEnumerator, true, this.tcapProvider.getExecuter(),
						this.tcapProvider, 0);
				long originatingTransactionId = tcb.getOriginatingTransactionId();
				int opc = message.getOpc();
				Map<Long, DialogImplWrapper> opcData = this.dialogs.get(opc);
				if (opcData == null) {
					opcData = new HashMap<Long, DialogImplWrapper>();
					this.dialogs.put(opc, opcData);
				}
				opcData.put(originatingTransactionId, di);

				DialogPortion dp = tcb.getDialogPortion();
				if (dp != null) {
					DialogAPDU apduN = dp.getDialogAPDU();
					if (apduN instanceof DialogRequestAPDU) {
						DialogRequestAPDU apdu = (DialogRequestAPDU) apduN;
						ApplicationContextName acnV = apdu.getApplicationContextName();
						if (acnV != null) {
							if (acnV.getOid()[5] == 0) {
								di.setAcnValue(((int) acnV.getOid()[6]));
								di.setAcnVersion(((int) acnV.getOid()[7]));
							} else {
								di.setAcnValue(((int) acnV.getOid()[7]));
								di.setAcnVersion(((int) acnV.getOid()[5]));
							}
						}
					}
				}

				Integer applicationContextFilter = par.getApplicationContextFilter();
				if (applicationContextFilter != null && applicationContextFilter != di.getAcnValue())
					return;

				if (!CheckDialogIdFilter(originatingTransactionId, Integer.MIN_VALUE))
					return;

				di.curOpcOrig = message.getOpc();
				di.curOpc = message.getOpc();
				di.processBegin(tcb, localAddress, remoteAddress);

				if (this.pw != null) {
//					this.pw.print("TC-BEGIN: OPC=" + message.getOpc() + ", DPC=" + message.getDpc() + ", originatingTransactionId=" + originatingTransactionId);
					this.pw.print("TC-BEGIN: ");
					this.printAddresses(message, originatingTransactionId, -1);
					if (this.par.getTcapMsgData()) {
						LogDataTag(aisMsg, "Begin", tcb.getComponent(), di.getAcnValue(), di.getAcnVersion(), tcb.getDialogPortion());
					}
					// this.LogComponents(tcb.getComponent(),
					// di.getAcnValue(), di.getAcnVersion(), comp);
				}
				break;
			}

			case TCEndMessage._TAG: {
				TCEndMessage teb = TcapFactory.createTCEndMessage(ais);
				long destinationTransactionId = teb.getDestinationTransactionId();

				int dpc = message.getDpc();
				Map<Long, DialogImplWrapper> dpcData = this.dialogs.get(dpc);
				int acnValue = 0;
				int acnVersion = 0;
				if (dpcData != null) {
					DialogImplWrapper di = dpcData.get(destinationTransactionId);
					if (di != null) {
						dpcData.remove(destinationTransactionId);

						acnValue = di.getAcnValue();
						acnVersion = di.getAcnVersion();

						Integer applicationContextFilter = par.getApplicationContextFilter();
						if (applicationContextFilter != null && applicationContextFilter != acnValue)
							return;

						if (!CheckDialogIdFilter(destinationTransactionId, Integer.MIN_VALUE))
							return;

						di.curOpc = message.getOpc();
						di.processEnd(teb, localAddress, remoteAddress);

						if (this.pw != null) {
//							this.pw.print("TC-END: OPC=" + message.getOpc() + ", DPC=" + message.getDpc() + ", destinationTransactionId="
//									+ destinationTransactionId);
							this.pw.print("TC-END: ");
							this.printAddresses(message, -1, destinationTransactionId);
							if (this.par.getTcapMsgData()) {
								LogDataTag(aisMsg, "End", teb.getComponent(), acnValue, acnVersion, teb.getDialogPortion());
							}
							
//							this.LogComponents(teb.getComponent(), acnValue, acnVersion, comp);
						}
					}
				}

				break;
			}

			case TCAbortMessage._TAG: {
				TCAbortMessage tub = TcapFactory.createTCAbortMessage(ais);
				long destinationTransactionId = tub.getDestinationTransactionId();

				int dpc = message.getDpc();
				Map<Long, DialogImplWrapper> dpcData = this.dialogs.get(dpc);
				int acnValue = 0;
				int acnVersion = 0;
				if (dpcData != null) {
					DialogImplWrapper di = dpcData.get(destinationTransactionId);
					if (di != null) {
						acnValue = di.getAcnValue();
						acnVersion = di.getAcnVersion();

						dpcData.remove(destinationTransactionId);

						Integer applicationContextFilter = par.getApplicationContextFilter();
						if (applicationContextFilter != null && applicationContextFilter != acnValue)
							return;

						if (!CheckDialogIdFilter(destinationTransactionId, Integer.MIN_VALUE))
							return;

						di.curOpc = message.getOpc();
						di.processAbort(tub, localAddress, remoteAddress);

						if (this.pw != null) {
//							this.pw.print("TC-ABORT: OPC=" + message.getOpc() + ", DPC=" + message.getDpc() + ", destinationTransactionId="
//									+ destinationTransactionId);
							this.pw.print("TC-ABORT: ");
							this.printAddresses(message, -1, destinationTransactionId);
							if (this.par.getTcapMsgData()) {
								LogDataTag(aisMsg, "Abort", null, acnValue, acnVersion, tub.getDialogPortion());
							}
							
//							this.LogComponents(null, acnValue, acnVersion, comp);
							this.pw.println();
							this.pw.flush();
						}
					}
				}

				break;
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void printAddresses(SccpMessageImpl message, long originationTransactionId, long destinationTransactionId) {
		this.pw.print("OPC=" + message.getOpc() + ", DPC=" + message.getDpc());
		if (originationTransactionId != -1)
			this.pw.print(", originationTransactionId=" + originationTransactionId);
		if (destinationTransactionId != -1)
			this.pw.print(", destinationTransactionId=" + destinationTransactionId);
		this.pw.println();
		if (message.getCalledPartyAddress() != null)
			this.pw.print("CalledPartyAddress=" + message.getCalledPartyAddress().toString());
		if (message.getCallingPartyAddress() != null)
			this.pw.print(", CallingPartyAddress=" + message.getCallingPartyAddress().toString());

//		this.pw.print("OPC=" + message.getOpc() + ", DPC=" + message.getDpc() + ", destinationTransactionId=" + destinationTransactionId);
	}

	private boolean CheckDialogIdFilter(long originatingTransactionId, long destinationTransactionId) {
		Long dialogIdFilter = par.getDialogIdFilter();
		Long dialogIdFilter2 = par.getDialogIdFilter2();
		if (dialogIdFilter == null && dialogIdFilter2 == null)
			return true;
		
		if (dialogIdFilter != null && (dialogIdFilter == originatingTransactionId || dialogIdFilter == destinationTransactionId))
			return true;
		
		if (dialogIdFilter2 != null && (dialogIdFilter2 == originatingTransactionId || dialogIdFilter2 == destinationTransactionId))
			return true;

		return false;
	}

	private void LogDataTag(AsnInputStream aisMsg, String name, Component[] comp, int acnValue, int acnVersion, DialogPortion dp) {
		
		try {
			aisMsg.readTag();
			aisMsg.readLength();

			int pos = aisMsg.position();
			aisMsg.position(0);
			byte[] buf = new byte[pos];
			aisMsg.read(buf);

			this.writeDataArray(name + " tag+length: ", buf);

			while( aisMsg.available() > 0 ) {
				pos = aisMsg.position();
				int tag = aisMsg.readTag();
				String ttl = "???";
				switch( tag ) {
				case 8:
					ttl = "OrigTransactionId";
					break;
				case 9:
					ttl = "DestTransactionId";
					break;
				case 10:
					ttl = "P-AbortCause";
					break;
				case 11:
					ttl = "DialogPortion";
					break;
				case 12:
					ttl = "ComponentPortion";
					break;
				}
				int length = aisMsg.readLength();
				int newPos;
				if (length == Tag.Indefinite_Length) {
					aisMsg.readIndefinite();
					newPos = aisMsg.position();
				} else {
					newPos = aisMsg.position() + length; 
				}				
				aisMsg.position(pos);
				buf = new byte[newPos - pos];
				aisMsg.read(buf);
				
				this.writeDataArray(ttl + ": ", buf);
				
				if (tag == 11 && this.par.getDetailedDialog())
					this.LogDialog(dp, buf);
				if (tag == 12 && this.par.getDetailedComponents())
					this.LogComponents(comp, acnValue, acnVersion, buf);
			}
			
			this.pw.println();
			this.pw.println();
			this.pw.flush();

		} catch (Exception e) {
			e.printStackTrace();
			this.pw.println();
			this.pw.print("Exception parsing TCAP msg");
			this.pw.print(e.getMessage());
		}
	}
	
	private void writeDataArray( String title, byte[] data ) {
		this.pw.println();
		this.pw.print(title);
		int i1 = 0;
		for( byte b : data ) {
			if (i1 == 0)
				i1 = 1;
			else
				this.pw.print(",");
			this.pw.print(" ");
			
			if (b < 0)
				this.pw.print("(byte)");
			this.pw.print((int) (b & 0xFF));
		}
	}
	
	private void LogDialog(DialogPortion dp, byte[] logData) {
		
		AsnInputStream ais = new AsnInputStream(logData);
		try {
			int tag = ais.readTag();
			int length = ais.readLength();
			tag = ais.readTag();
			length = ais.readLength();
			int pos = ais.position();
			ais.position(0);
			byte[] buf = new byte[pos];
			ais.read(buf);
			this.writeDataArray("\tDialogPort+External tags: ", buf);
			
			tag = ais.readTag();
			long[] oid = ais.readObjectIdentifier();
			int pos2 = ais.position();
			ais.position(pos);
			buf = new byte[pos2 - pos];
			ais.read(buf);
			this.writeDataArray("\tExternal OId: ", buf);
			
			tag = ais.readTag();
			length = ais.readLength();
			int pos3 = ais.position();
			ais.position(pos2);
			buf = new byte[pos3 - pos2];
			ais.read(buf);
			this.writeDataArray("\tExternal Asn tag: ", buf);
			
			buf = new byte[ais.available()];
			ais.read(buf);
			this.writeDataArray("\t\tDialogAPDU_" + dp.getDialogAPDU().getType().toString(), buf);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void LogComponents(Component[] comp, int acnValue, int acnVersion, byte[] logData) {
		this.pw.println();
		this.pw.print("\tAcn: ");
		if (acnValue > 0)
			this.pw.print(acnValue + "-" + acnVersion);
		else
			this.pw.print("???");
		this.pw.print("\tComponents: ");
		if (comp != null) {
			int i1 = 0;
			for (Component c : comp) {
				if (i1 == 0)
					i1 = 1;
				else
					this.pw.print(", ");
				this.pw.print(c.getType());
				this.pw.print(":");
				switch (c.getType()) {
				case Invoke:
					Invoke inv = (Invoke)c;
					this.LogOperationCode(inv.getOperationCode());
					break;
				case ReturnResult:
					ReturnResult rr = (ReturnResult)c;
					this.LogOperationCode(rr.getOperationCode());
					break;
				case ReturnResultLast:
					ReturnResultLast rrl = (ReturnResultLast)c;
					this.LogOperationCode(rrl.getOperationCode());
					break;
				case ReturnError:
					ReturnError re = (ReturnError)c;
					this.pw.print(re.getErrorCode());
					break;
				case Reject:
					Reject rej = (Reject)c;
					this.pw.print(rej.getClass());
					break;
				}
			}
		}
		
		if (logData != null) {
			try {
				AsnInputStream ais = new AsnInputStream(logData);
				this.LogSequence(ais, 1, "Components");
			} catch (Exception e) {
				e.printStackTrace();
				this.pw.println();
				this.pw.print("Exception parsing TCAP components");
				this.pw.print(e.getMessage());
			}
		}
	}
	
	private String LodSequenceName(int dep, int tag, int tagClass, int ind, String parent) {

		if (dep == 1) {
			switch (tag) {
			case 1:
				return "Invoke";
			case 2:
				return "ReturnResultLast";
			case 3:
				return "ReturnError";
			case 4:
				return "Reject";
			case 7:
				return "ReturnResult";
			}
		}

		if (dep == 2) {
			if (ind == 0)
				return "invokeId";
			
			if (tag == Tag.INTEGER && tagClass == Tag.CLASS_UNIVERSAL)
				return "operationCode";
			
			if (tag == 0 && tagClass == Tag.CLASS_CONTEXT_SPECIFIC) {
				if (parent.equals("Invoke"))
					return "linkedId";
				else
					return "GeneralProblem";
			}
			if (tag == 1 && tagClass == Tag.CLASS_CONTEXT_SPECIFIC)
				return "InvokeProblem";
			if (tag == 2 && tagClass == Tag.CLASS_CONTEXT_SPECIFIC)
				return "ReturnResultProblem";
			if (tag == 3 && tagClass == Tag.CLASS_CONTEXT_SPECIFIC)
				return "ReturnErrorProblem";
			
			if (parent.equals("ReturnResultLast") || parent.equals("ReturnResult")) {
				if (tag == Tag.SEQUENCE)
					return "ReturnResultData";
			}
			
			if (parent.equals("ReturnError")) {
				if (ind == 0)
					return "ErrorCode";
			}
			
			return "Parameter";
		}

		if (dep == 3) {
			if (parent.equals("ReturnResultData")) {
				if (tag == Tag.INTEGER && tagClass == Tag.CLASS_UNIVERSAL)
					return "operationCode";
				else
					return "Parameter";
			}
		}
		
		return "";
	}

	private void LogSequence(AsnInputStream ais, int dep, String name) throws IOException, AsnException {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < dep; i++) {
			sb.append("\t");
		}
		String pref = sb.toString();

		int pos = ais.position();
		int tag = ais.readTag();
		int length = ais.readLength();
		int newPos = ais.position();
		byte[] buf = new byte[newPos - pos];
		ais.position(pos);
		ais.read(buf);
		this.writeDataArray(pref + name + ": tag+length: ", buf);

		int ind = 0;
		while (ais.available() > 0) {
			pos = ais.position();
			tag = ais.readTag();

			length = ais.readLength();

			boolean isConstr = !ais.isTagPrimitive();
			if (isConstr && length == Tag.Indefinite_Length) {
				ais.readSequenceData(length);
				newPos = ais.position();
			} else
				newPos = ais.position() + length;
			
			buf = new byte[newPos - pos];
			ais.position(pos);
			ais.read(buf);
			String name2 = this.LodSequenceName(dep, tag, ais.getTagClass(), ind, name);
			this.writeDataArray(pref + name2 + ": ", buf);

			if (isConstr) {

				AsnInputStream ais2 = new AsnInputStream(buf);
				this.LogSequence(ais2, dep + 1, name2);
			}
			
			ind++;
		}
	}
	
	private void LogOperationCode(OperationCode op) {
		this.pw.print("OpCode=");
		if (op != null)
			this.pw.print(op.getLocalOperationCode());
		else
			this.pw.print("???");
	}

	@Override
	public void onDialogDelimiter(MAPDialog mapDialog) {
		// TODO Auto-generated method stub
		
		try {
			mapDialog.send();
		} catch (MAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference, MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
		DialogImplWrapper di = (DialogImplWrapper)((MAPDialogImpl)mapDialog).getTcapDialog();
		if (mapDialog.getApplicationContext() != null) {
			di.setAcnValue(mapDialog.getApplicationContext().getApplicationContextName().getApplicationContextCode());
			di.setAcnVersion(mapDialog.getApplicationContext().getApplicationContextVersion().getVersion());
		}
	}

	@Override
	public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, MAPProviderError providerError,
			ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason, MAPAbortSource abortSource,
			MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogClose(MAPDialog mapDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogResease(MAPDialog mapDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogTimeout(MAPDialog mapDialog) {
		// TODO Auto-generated method stub
		
		mapDialog.keepAlive();
	}

	@Override
	public boolean isFinished() {
		return this.taskIsFinished;
	}

	@Override
	public String getErrorMessage() {
		return this.errorMessage;
	}

	@Override
	public void interrupt() {
		this.needInterrupt = true;
	}
	
	@Override
	public boolean checkNeedInterrupt() {
		if (this.needInterrupt) {
			this.errorMessage = "User break";
			this.taskIsFinished = true;
			return true;
		} else
			return false;
	}

	@Override
	public int getMsgCount() {
		return this.msgCount;
	}
	
	private class LogData {
		public byte[] dialogPortion;
		public byte[] componentPortion;
	}

	@Override
	public void onDialogAccept(CAPDialog arg0, CAPGprsReferenceNumber arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogClose(CAPDialog arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogDelimiter(CAPDialog capDialog) {
		// TODO Auto-generated method stub
		
		try {
			capDialog.send();
		} catch (CAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onDialogNotice(CAPDialog arg0, CAPNoticeProblemDiagnostic arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogProviderAbort(CAPDialog arg0, PAbortCauseType arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber arg1) {
		// TODO Auto-generated method stub
		
		DialogImplWrapper di = (DialogImplWrapper)((CAPDialogImpl)capDialog).getTcapDialog();
		if (capDialog.getApplicationContext() != null) {
//			di.setAcnValue(capDialog.getApplicationContext().getApplicationContextName().getApplicationContextCode());
			di.setAcnVersion(capDialog.getApplicationContext().getVersion().getVersion());
		}
	}

	@Override
	public void onDialogResease(CAPDialog arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogTimeout(CAPDialog capDialog) {
		// TODO Auto-generated method stub
		
		capDialog.keepAlive();
	}

	@Override
	public void onDialogUserAbort(CAPDialog arg0, CAPGeneralAbortReason arg1, CAPUserAbortReason arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderErrorComponent(MAPDialog mapDialog, Long invokeId, MAPProviderError providerError) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
		// TODO Auto-generated method stub
		
	}

	// SMS service listener
	private void parseSmsSignalInfo(SmsSignalInfo si, boolean isMo, boolean isMt) {

		if (si == null)
			return;
		
		if (isMo) {
			try {
				SmsTpdu tpdu = si.decodeTpdu(true);
				parseSmsTpdu(tpdu);
			} catch (MAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (isMt) {
			try {
				SmsTpdu tpdu = si.decodeTpdu(false);
				parseSmsTpdu(tpdu);
			} catch (MAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void parseSmsTpdu(SmsTpdu tpdu) throws MAPException {
		
		UserData ud = null;
		switch (tpdu.getSmsTpduType()) {
		case SMS_DELIVER_REPORT: {
			SmsDeliverReportTpdu t = (SmsDeliverReportTpdu) tpdu;
//			ud = t.getUserData();
		}
			break;
		case SMS_SUBMIT: {
			SmsSubmitTpdu t = (SmsSubmitTpdu) tpdu;
			ud = t.getUserData();
		}
			break;
		case SMS_COMMAND: {
			SmsCommandTpdu t = (SmsCommandTpdu) tpdu;
		}
			break;
		case SMS_DELIVER: {
			SmsDeliverTpdu t = (SmsDeliverTpdu) tpdu;
			ud = t.getUserData();
		}
			break;
		case SMS_SUBMIT_REPORT: {
			SmsSubmitTpdu t = (SmsSubmitTpdu) tpdu;
//			ud = t.getUserData();
		}
			break;
		case SMS_STATUS_REPORT: {
			SmsStatusReportTpdu t = (SmsStatusReportTpdu) tpdu;
//			ud = t.getUserData();
		}
			break;
		}

		if (ud != null) {
			ud.decode();
			int i1 = 0;
			i1++;
		}
	}
	
	@Override
	public void onForwardShortMessageIndication(ForwardShortMessageRequestIndication forwSmInd) {
		this.parseSmsSignalInfo(forwSmInd.getSM_RP_UI(), true, true);
	}

	@Override
	public void onForwardShortMessageRespIndication(ForwardShortMessageResponseIndication forwSmRespInd) {
	}

	@Override
	public void onMoForwardShortMessageIndication(MoForwardShortMessageRequestIndication moForwSmInd) {
		this.parseSmsSignalInfo(moForwSmInd.getSM_RP_UI(), true, false);
	}

	@Override
	public void onMoForwardShortMessageRespIndication(MoForwardShortMessageResponseIndication moForwSmRespInd) {
		this.parseSmsSignalInfo(moForwSmRespInd.getSM_RP_UI(), false, true);
	}

	@Override
	public void onMtForwardShortMessageIndication(MtForwardShortMessageRequestIndication mtForwSmInd) {
		this.parseSmsSignalInfo(mtForwSmInd.getSM_RP_UI(), false, true);
	}

	@Override
	public void onMtForwardShortMessageRespIndication(MtForwardShortMessageResponseIndication mtForwSmRespInd) {
		this.parseSmsSignalInfo(mtForwSmRespInd.getSM_RP_UI(), true, false);
	}

	@Override
	public void onSendRoutingInfoForSMIndication(SendRoutingInfoForSMRequestIndication sendRoutingInfoForSMInd) {
	}

	@Override
	public void onSendRoutingInfoForSMRespIndication(SendRoutingInfoForSMResponseIndication sendRoutingInfoForSMRespInd) {
	}

	@Override
	public void onReportSMDeliveryStatusIndication(ReportSMDeliveryStatusRequestIndication reportSMDeliveryStatusInd) {
	}

	@Override
	public void onReportSMDeliveryStatusRespIndication(ReportSMDeliveryStatusResponseIndication reportSMDeliveryStatusRespInd) {
	}

	@Override
	public void onInformServiceCentreIndication(InformServiceCentreRequestIndication informServiceCentreInd) {
	}

	@Override
	public void onAlertServiceCentreIndication(AlertServiceCentreRequestIndication alertServiceCentreInd) {
	}

	@Override
	public void onAlertServiceCentreRespIndication(AlertServiceCentreResponseIndication alertServiceCentreInd) {
	}
}
