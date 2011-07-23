package org.mobicents.protocols.ss7.tools.traceparser;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.mobicents.protocols.asn.AsnInputStream;
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
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.message.UnitData;
import org.mobicents.protocols.ss7.sccp.message.UnitDataService;
import org.mobicents.protocols.ss7.sccp.message.XUnitData;
import org.mobicents.protocols.ss7.sccp.message.XUnitDataService;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.DialogAPDU;
import org.mobicents.protocols.ss7.tcap.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDU;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
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
public class MAPTraceParser implements TraceReaderListener, MAPDialogListener, Runnable, ProcessControl {
	
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
	private MessageFactoryImpl msgFact = new MessageFactoryImpl(); 

	private Map<Integer, Map<Long, DialogImplWrapper>> dialogs = new HashMap<Integer, Map<Long, DialogImplWrapper>>();
	private long dialogEnumerator = 0;
	

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
		
		switch (this.par.getFileType()) {
		case 1:
			this.driver = new TraceReaderDriverActerna(this, filePath);
			break;
		default:
			this.setFinishedState("Unknown TraceReaderDriver: " + this.par.getFileType());
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

			this.sccpProvider = new SccpProviderWrapper();
			this.tcapStack = new TCAPStackImplWrapper(this.sccpProvider, 1);
			this.tcapProvider = (TCAPProviderImplWrapper) this.tcapStack.getProvider();
			this.mapProvider = new MAPProviderImpl(this.tcapProvider);

			this.mapProvider.getMAPServiceSupplementary().acivate();
			this.mapProvider.getMAPServiceSms().acivate();

			this.tcapStack.start();
			this.mapProvider.start();
			
			this.mapProvider.addMAPDialogListener(this);
			
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

	
	public void onMessage(SccpMessageImpl message, int seqControl) {
		try {
			byte[] data = null;
			SccpAddress localAddress = null;
			SccpAddress remoteAddress = null;
			
			if(this.msgCount==8) {
				int dsa=0;
				dsa++;
			}

			switch (message.getType()) {

			case UnitData.MESSAGE_TYPE:
				data = ((UnitData) message).getData();
				localAddress = ((UnitData) message).getCalledPartyAddress();
				remoteAddress = ((UnitData) message).getCallingPartyAddress();
				break;
			case XUnitData.MESSAGE_TYPE:
				data = ((XUnitData) message).getData();
				localAddress = ((XUnitData) message).getCalledPartyAddress();
				remoteAddress = ((XUnitData) message).getCallingPartyAddress();
				break;
			// TODO: determine action based on cause?
			case UnitDataService.MESSAGE_TYPE:
				data = ((XUnitData) message).getData();
				localAddress = ((XUnitData) message).getCalledPartyAddress();
				remoteAddress = ((XUnitData) message).getCallingPartyAddress();
				break;
			case XUnitDataService.MESSAGE_TYPE:
				data = ((XUnitData) message).getData();
				localAddress = ((XUnitData) message).getCalledPartyAddress();
				remoteAddress = ((XUnitData) message).getCallingPartyAddress();
				break;
			}
			// FIXME: Qs state that OtxID and DtxID consittute to dialog id.....

			// asnData - it should pass
			AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(data));

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
						
						Long dialogIdFilter = par.getDialogIdFilter(); 
						if (dialogIdFilter != null && dialogIdFilter != originatingTransactionId && dialogIdFilter != destinationTransactionId)
							return;
						
						di.processContinue(tcm, localAddress, remoteAddress);
						
						this.LogMsg("TC-CONTINUE", message.getOpc(), message.getDpc(), acnValue, acnVersion, originatingTransactionId, destinationTransactionId);
						this.LogComponents(tcm.getComponent());
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
					if( apduN instanceof DialogRequestAPDU ) {
						DialogRequestAPDU apdu = (DialogRequestAPDU)apduN;
						ApplicationContextName acnV = apdu.getApplicationContextName();
						if (acnV != null) {
							di.setAcnValue(((int) acnV.getOid()[6]));
							di.setAcnVersion(((int) acnV.getOid()[7]));
						}
					}
				}
				
				Integer applicationContextFilter = par.getApplicationContextFilter(); 
				if (applicationContextFilter != null && applicationContextFilter != di.getAcnValue())
					return;

				Long dialogIdFilter = par.getDialogIdFilter(); 
				if (dialogIdFilter != null && dialogIdFilter != originatingTransactionId)
					return;

				di.processBegin(tcb, localAddress, remoteAddress);
				
				this.LogMsg("TC-BEGIN", message.getOpc(), message.getDpc(), di.getAcnValue(), di.getAcnVersion(), originatingTransactionId, null);
				this.LogComponents(tcb.getComponent());
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

						Long dialogIdFilter = par.getDialogIdFilter(); 
						if (dialogIdFilter != null && dialogIdFilter != destinationTransactionId)
							return;

						di.processEnd(teb, localAddress, remoteAddress);
						
						this.LogMsg("TC-END", message.getOpc(), message.getDpc(), acnValue, acnVersion, null, destinationTransactionId);
						this.LogComponents(teb.getComponent());
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

						Long dialogIdFilter = par.getDialogIdFilter(); 
						if (dialogIdFilter != null && dialogIdFilter != destinationTransactionId)
							return;

						di.processAbort(tub, localAddress, remoteAddress);
						
						this.LogMsg("TC-ABORT", message.getOpc(), message.getDpc(), acnValue, acnVersion, null, destinationTransactionId);
						this.LogComponents(null);
//						this.pw.println();
//						this.pw.flush();
					}
				}
				
				break;
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private void LogMsg(String msgType, int opc, int dpc, int acnValue, int acnVersion, Long origTransId, Long destTransId) {
		if (this.pw == null)
			return;
		
		this.pw.print(msgType);
		this.pw.print(":\tMsgNum=");
		this.pw.print(this.msgCount);
		this.pw.print(",\tAcn=");
		if (acnValue > 0)
			this.pw.print(acnValue + "-" + acnVersion);
		else
			this.pw.print("???");

		this.pw.print(",\tOPC=");
		this.pw.print(opc);
		this.pw.print(", DPC=");
		this.pw.print(dpc);

		if (origTransId != null) {
			this.pw.print(", origTransId=");
			this.pw.print(origTransId);
		}
		if (destTransId != null) {
			this.pw.print(", destTransId=");
			this.pw.print(destTransId);
		}
	}
	
	private void LogComponents(Component[] comp) {
		if (this.pw == null)
			return;
		
		if (comp != null) {
			for (Component c : comp) {
				this.pw.println();
				this.pw.print("\t");
				this.pw.print(c.getType());
				this.pw.print(":");
				switch (c.getType()) {
				case Invoke:
					Invoke inv = (Invoke)c;
					this.LogInvokeId(inv.getInvokeId());
					this.LogOperationCode(inv.getOperationCode());
					break;
				case ReturnResult:
					ReturnResult rr = (ReturnResult)c;
					this.LogInvokeId(rr.getInvokeId());
					this.LogOperationCode(rr.getOperationCode());
					break;
				case ReturnResultLast:
					ReturnResultLast rrl = (ReturnResultLast)c;
					this.LogInvokeId(rrl.getInvokeId());
					this.LogOperationCode(rrl.getOperationCode());
					break;
				case ReturnError:
					ReturnError re = (ReturnError)c;
					this.LogInvokeId(re.getInvokeId());
					this.pw.print(", ErrCode=");
					this.pw.print(re.getErrorCode());
					break;
				case Reject:
					Reject rej = (Reject)c;
					this.LogInvokeId(rej.getInvokeId());
					this.pw.print(", Problem=");
					this.pw.print(rej.getProblem());
					break;
				}
			}
		}
		this.pw.println();
		this.pw.flush();
		
	}
	
	private void LogInvokeId(Long invId) {
		this.pw.print("InvId=");
		if (invId != null)
			this.pw.print(invId);
		else
			this.pw.print("???");
	}
	
	private void LogOperationCode(OperationCode op) {
		this.pw.print(", OpCode=");
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
}
