package org.mobicents.protocols.ss7.tcap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.tc.component.OperationState;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.DialogAPDU;
import org.mobicents.protocols.ss7.tcap.asn.DialogAPDUType;
import org.mobicents.protocols.ss7.tcap.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDU;
import org.mobicents.protocols.ss7.tcap.asn.DialogResponseAPDU;
import org.mobicents.protocols.ss7.tcap.asn.DialogServiceUserType;
import org.mobicents.protocols.ss7.tcap.asn.DialogUniAPDU;
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;
import org.mobicents.protocols.ss7.tcap.asn.Result;
import org.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic;
import org.mobicents.protocols.ss7.tcap.asn.ResultType;
import org.mobicents.protocols.ss7.tcap.asn.TCBeginMessageImpl;
import org.mobicents.protocols.ss7.tcap.asn.TCContinueMessageImpl;
import org.mobicents.protocols.ss7.tcap.asn.TCEndMessageImpl;
import org.mobicents.protocols.ss7.tcap.asn.TCUniMessageImpl;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.DialogPrimitiveFactoryImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCBeginIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCContinueIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCEndIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCUniIndicationImpl;

/**
 * @author baranowb
 * 
 */
public class DialogImpl implements Dialog {

	// timeout of remove task after TC_END
    private static final int _REMOVE_TIMEOUT = 60000;

	private static final Logger logger = Logger.getLogger(DialogImpl.class);

	// sent/received acn, holds last acn/ui.
	private ApplicationContextName lastACN;
	private UserInformation lastUI; // optional

	private Long localTransactionId;
	private Long remoteTransactionId;

	private SccpAddress localAddress;
	private SccpAddress remoteAddress;

	private TRPseudoState state = TRPseudoState.Idle;
	private boolean structured = true;
	// invokde ID space :)
	private static final boolean _INVOKEID_TAKEN = true;
	private static final boolean _INVOKEID_FREE = false;
	private static final int _INVOKE_TABLE_SHIFT = 128;

	private boolean[] invokeIDTable = new boolean[255];
	private int freeCount = invokeIDTable.length;

	// only originating side keeps FSM, see: Q.771 - 3.1.5
	private InvokeImpl[] operationsSent = new InvokeImpl[invokeIDTable.length];
	private ScheduledExecutorService executor;

	// scheduled components list
	private List<Component> scheduledComponentList = new ArrayList<Component>();
	private TCAPProviderImpl provider;

	private static final int getIndexFromInvokeId(Long l) {
		int tmp = l.intValue();
		return tmp + _INVOKE_TABLE_SHIFT;
	}

	private static final Long getInvokeIdFromIndex(int index) {
		int tmp = index - _INVOKE_TABLE_SHIFT;
		return new Long(tmp);
	}

	//DialogImpl(SccpAddress localAddress, SccpAddress remoteAddress, Long origTransactionId, ScheduledExecutorService executor,
	//		TCAProviderImpl provider, ApplicationContextName acn, UserInformation[] ui) {
	 DialogImpl(SccpAddress localAddress, SccpAddress remoteAddress, Long
		 origTransactionId, boolean structured, ScheduledExecutorService executor, TCAPProviderImpl
		 provider) {
		super();
		this.localAddress = localAddress;
		this.remoteAddress = remoteAddress;
		this.localTransactionId = origTransactionId;
		this.executor = executor;
		this.provider = provider;
		//this.initialACN = acn;
		//this.initialUI = ui;
		if (origTransactionId > 0) {
			this.structured = true;
		} else {
			this.structured = false;
		}
		this.structured = structured;
	}

	public void release() {
		this.setState(TRPseudoState.Expunged);

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
	public synchronized Long getNewInvokeId() {
		if (freeCount == 0) {
			return null;
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
	
		return r;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#cancelInvocation(java.lang.Long)
	 */
	public boolean cancelInvocation(Long invokeId) throws TCAPException {
		int index = getIndexFromInvokeId(invokeId);
		if(index<0 || index>=operationsSent.length)
		{
			throw new TCAPException("Wrong invoke id passed.");
		}
		
		//lookup through send buffer.
		for( index =0 ;index<scheduledComponentList.size();index++)
		{
			Component cr = scheduledComponentList.get(index);
			if(cr.getType() ==ComponentType.Invoke && cr.getInvokeId().equals(invokeId))
			{
				//lucky
				//TCInvokeRequestImpl invoke = (TCInvokeRequestImpl) cr;
				//there is no notification on cancel?
				scheduledComponentList.remove(index);
				return true;
			}
		}
		
		return false;
	}

	private synchronized void freeInvokeId(Long l)
	{
		int index = getIndexFromInvokeId(l);
		this.freeCount--;
		this.invokeIDTable[index] = _INVOKEID_FREE;
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

		return this.structured;
	}

	/**
	 * @return the acn
	 */
	public ApplicationContextName getApplicationContextName() {
		return lastACN;
	}

	/**
	 * @return the ui
	 */
	public UserInformation getUserInformation() {
		return lastUI;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.mobicents
	 * .protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest)
	 */
	public void send(TCBeginRequest event) throws TCAPSendException {
		if (this.state != TRPseudoState.Idle) {
			throw new TCAPSendException("Can not send Begin in this state: " + this.state);
		}

		if (!this.isStructured()) {
			throw new TCAPSendException("Unstructured dialogs do not use Begin");
		}
		
		TCBeginMessageImpl tcbm = (TCBeginMessageImpl) TcapFactory.createTCBeginMessage();
		
		//build DP
		
		if(event.getApplicationContextName()!=null)
		{
			DialogPortion dp = TcapFactory.createDialogPortion();
			dp.setUnidirectional(false);
			DialogRequestAPDU apdu = TcapFactory.createDialogAPDURequest();
			dp.setDialogAPDU(apdu);
			apdu.setApplicationContextName(event.getApplicationContextName());
			if (event.getUserInformation() != null) {
				apdu.setUserInformation(event.getUserInformation());
			}
			tcbm.setDialogPortion(dp);
		}
		
		//now comps
		tcbm.setOriginatingTransactionId(this.localTransactionId);
		if(this.scheduledComponentList.size()>0)
		{
			Component[] componentsToSend = new Component[this.scheduledComponentList.size()];
			this.prepareComponents(componentsToSend);
			tcbm.setComponent(componentsToSend);
			
		}
		

		AsnOutputStream aos = new AsnOutputStream();
		try {
			tcbm.encode(aos);
			this.provider.send(aos.toByteArray(), event.getQOS() == null ? 0 : event.getQOS().byteValue(), this.remoteAddress, this.localAddress);
			this.setState(TRPseudoState.InitialSent);
		} catch (Exception e) {
			// FIXME: add proper handling here. TC-NOTICE ?
			// FIXME: remove freshly added invokes to free invoke ID?? 
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Failed to send message: ", e);
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.mobicents
	 * .protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest)
	 */
	public void send(TCContinueRequest event) throws TCAPSendException {
		if (!this.isStructured()) {
			throw new TCAPSendException("Unstructured dialogs do not use Continue");
		}
		if (this.state == TRPseudoState.InitialReceived) {
			
			TCContinueMessageImpl tcbm = (TCContinueMessageImpl) TcapFactory.createTCContinueMessage();
			
			if (event.getApplicationContextName() != null) {
				
				// set dialog portion
				DialogPortion dp = TcapFactory.createDialogPortion();
				dp.setUnidirectional(false);
				DialogResponseAPDU apdu = TcapFactory.createDialogAPDUResponse();
				dp.setDialogAPDU(apdu);
				apdu.setApplicationContextName(event.getApplicationContextName());
				if(event.getUserInformation()!=null)
				{
					apdu.setUserInformation(event.getUserInformation());
				}
				//WHERE THE HELL THIS COMES FROM!!!!
				//WHEN REJECTED IS USED !!!!!
				Result res = TcapFactory.createResult();
				res.setResultType(ResultType.Accepted);
				ResultSourceDiagnostic rsd = TcapFactory.createResultSourceDiagnostic();
				rsd.setDialogServiceUserType(DialogServiceUserType.Null);
				apdu.setResultSourceDiagnostic(rsd);
				apdu.setResult(res);
				tcbm.setDialogPortion(dp);

			}

			tcbm.setOriginatingTransactionId(this.localTransactionId);
			tcbm.setDestinationTransactionId(this.remoteTransactionId);
			if(this.scheduledComponentList.size()>0)
			{
				Component[] componentsToSend = new Component[this.scheduledComponentList.size()];
				this.prepareComponents(componentsToSend);
				tcbm.setComponent(componentsToSend);
				
			}
			//local address may change, lets check it;
			if(event.getOriginatingAddress()!=null && !event.getOriginatingAddress().equals(this.localAddress))
			{
				this.localAddress = event.getOriginatingAddress();
			}
			AsnOutputStream aos = new AsnOutputStream();
			try {
				tcbm.encode(aos);
				this.provider.send(aos.toByteArray(), event.getQOS() == null ? 0 : event.getQOS().byteValue(), this.remoteAddress, this.localAddress);
				this.setState(TRPseudoState.Active);
				this.scheduledComponentList.clear();
			} catch (Exception e) {
				// FIXME: add proper handling here. TC-NOTICE ?
				// FIXME: remove freshly added invokes to free invoke ID?? 
				if (logger.isEnabledFor(Level.ERROR)) {
					logger.error("Failed to send message: ", e);
				}

			}

		} else if (state == TRPseudoState.Active) {
			// in this we ignore acn and passed args(except qos)
			TCContinueMessageImpl tcbm = (TCContinueMessageImpl) TcapFactory.createTCContinueMessage();

			tcbm.setOriginatingTransactionId(this.localTransactionId);
			tcbm.setDestinationTransactionId(this.remoteTransactionId);
			if(this.scheduledComponentList.size()>0)
			{
				Component[] componentsToSend = new Component[this.scheduledComponentList.size()];
				this.prepareComponents(componentsToSend);
				tcbm.setComponent(componentsToSend);
				
			}
			
			//FIXME: SPECS SAY HERE UI/ACN CAN BE SENT, HOOOOOOOWWW!?
			
			AsnOutputStream aos = new AsnOutputStream();
			try {
				tcbm.encode(aos);
				this.provider.send(aos.toByteArray(), event.getQOS() == null ? 0 : event.getQOS().byteValue(), this.remoteAddress, this.localAddress);
				this.scheduledComponentList.clear();
			} catch (Exception e) {
				// FIXME: add proper handling here. TC-NOTICE ?
				if (logger.isEnabledFor(Level.ERROR)) {
					logger.error("Failed to send message: ", e);
				}

			}
		} else {
			throw new TCAPSendException("Wrong state: "+this.state);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.mobicents
	 * .protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest)
	 */
	public void send(TCEndRequest event) throws TCAPSendException {
		if (!this.isStructured()) {
			throw new TCAPSendException("Unstructured dialogs do not use End");
		}
		
		if (state != TRPseudoState.Active)
		{
			throw new TCAPSendException("State is not: "+TRPseudoState.Active+", it is: "+this.state);
		}
		
		TCEndMessageImpl tcbm = (TCEndMessageImpl) TcapFactory.createTCEndMessage();

		tcbm.setDestinationTransactionId(this.remoteTransactionId);
		if(event.getTerminationType() == TerminationType.Basic)
		{
			if(this.scheduledComponentList.size()>0)
			{
				Component[] componentsToSend = new Component[this.scheduledComponentList.size()];
				this.prepareComponents(componentsToSend);
				tcbm.setComponent(componentsToSend);
				
			}
		}else if(event.getTerminationType() == TerminationType.PreArranged)
		{
			this.scheduledComponentList.clear();
		}else
		{
			throw new TCAPSendException("Termination TYPE must be present");
		}
		
		
		//FIXME: SPECS SAY HERE UI/ACN CAN BE SENT, HOOOOOOOWWW!?
		AsnOutputStream aos = new AsnOutputStream();
		try {
			tcbm.encode(aos);
			this.provider.send(aos.toByteArray(), event.getQOS() == null ? 0 : event.getQOS().byteValue(), this.remoteAddress, this.localAddress);
			this.setState(TRPseudoState.Expunged);
			this.scheduledComponentList.clear();
		} catch (Exception e) {
			// FIXME: add proper handling here. TC-NOTICE ?
			// FIXME: remove freshly added invokes to free invoke ID?? 
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Failed to send message: ", e);
			}

		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#sendUni()
	 */
	public void sendUni(TCUniRequest event) throws TCAPSendException {
		if (this.isStructured()) {
			throw new TCAPSendException("Structured dialogs do not use Uni");
		}
		
		TCUniMessageImpl msg = (TCUniMessageImpl) TcapFactory.createTCUniMessage();

		if(event.getApplicationContextName()!=null)
		{
			DialogPortion dp = TcapFactory.createDialogPortion();
			DialogUniAPDU apdu  = TcapFactory.createDialogAPDUUni();
			apdu.setApplicationContextName(event.getApplicationContextName());
			if(event.getUserInformation()!=null)
			{
				apdu.setUserInformation(event.getUserInformation());
			}
			dp.setUnidirectional(true);
			dp.setDialogAPDU(apdu);
			msg.setDialogPortion(dp);
			
			
		}
		
		if(this.scheduledComponentList.size()>0)
		{
			Component[] componentsToSend = new Component[this.scheduledComponentList.size()];
			this.prepareComponents(componentsToSend);
			msg.setComponent(componentsToSend);
			
		}
		
		AsnOutputStream aos = new AsnOutputStream();
		try {
			msg.encode(aos);
			this.provider.send(aos.toByteArray(), event.getQOS() == null ? 0 : event.getQOS().byteValue(), this.remoteAddress, this.localAddress);
			this.setState(TRPseudoState.Expunged);
			this.scheduledComponentList.clear();
		} catch (Exception e) {
			// FIXME: add proper handling here. TC-NOTICE ?
			// FIXME: remove freshly added invokes to free invoke ID?? 
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Failed to send message: ", e);
			}

		}
		
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#sendComponent(org
	 * .mobicents.protocols.ss7.tcap.api.tc.component.ComponentRequest)
	 */
	public void sendComponent(Component componentRequest) throws TCAPSendException {

		
		if (componentRequest.getType() == ComponentType.Invoke) {
			InvokeImpl invoke = (InvokeImpl) componentRequest;
			
			//check if its taken!
			int invokeIndex = this.getIndexFromInvokeId(invoke.getInvokeId());
			if(this.operationsSent[invokeIndex]!=null)
			{
				//This is TC-L-REJECT?
				//TC-L-REJECT (local reject): Informs the local TC-user that a Component sublayer detected
				//invalid component was received.  <-- who wrote this?
				throw new TCAPSendException("There is already operation with such invoke id!");
			}
			
			invoke.setState(OperationState.Pending);
			invoke.setDialog(this);
		}
		this.scheduledComponentList.add(componentRequest);


	}


	private void prepareComponents(Component[] res) {

		int index = 0;
		while (this.scheduledComponentList.size() > index) {
			Component cr = this.scheduledComponentList.get(index);
			// FIXME: add more ?
			if (cr.getType() == ComponentType.Invoke) {
				InvokeImpl in = (InvokeImpl) cr;
				// check not null?
				this.operationsSent[this.getIndexFromInvokeId(in.getInvokeId())] = in;
				// FIXME: deffer this ?
				in.setState(OperationState.Sent);
			}

			res[index++] = cr;

		}

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

	void processUni(TCUniMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {
		// this is invoked ONLY for server.
		if (state != TRPseudoState.Idle) {
			// should we terminate dialog here?
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Received Uni primitive, but state is not: " + TRPseudoState.Idle + ". Dialog: " + this);

			}
			throw new TCAPException("Received Uni primitive, but state is not: " + TRPseudoState.Idle + ". Dialog: " + this);
		}
		// lets setup
		this.setRemoteAddress(remoteAddress);
		this.setLocalAddress(localAddress);

		// no dialog portion!
		// convert to indications
		TCUniIndicationImpl tcUniIndication = (TCUniIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider.getDialogPrimitiveFactory())
				.createUniIndication(this);

		tcUniIndication.setDestinationAddress(localAddress);
		tcUniIndication.setOriginatingAddress(remoteAddress);
		// now comps
		Component[] comps = msg.getComponent();
		tcUniIndication.setComponents(comps);
		
		if(msg.getDialogPortion()!=null)
		{
			//it should be dialog req?
			DialogPortion dp = msg.getDialogPortion();
			DialogUniAPDU apdu = (DialogUniAPDU) dp.getDialogAPDU();
			this.lastACN = apdu.getApplicationContextName();
			this.lastUI = apdu.getUserInformation();
			tcUniIndication.setApplicationContextName(this.lastACN);
			tcUniIndication.setUserInformation(this.lastUI);
		}

		// lets deliver to provider, this MUST not throw anything
		this.provider.deliver(this, tcUniIndication);
		// schedule removal
		this.release();
	}

	void processBegin(TCBeginMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {
		// this is invoked ONLY for server.
		if (state != TRPseudoState.Idle) {
			// should we terminate dialog here?
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Received Begin primitive, but state is not: " + TRPseudoState.Idle + ". Dialog: " + this);

			}
			throw new TCAPException("Received Begin primitive, but state is not: " + TRPseudoState.Idle + ". Dialog: " + this);
		}
		// lets setup
		this.setRemoteAddress(remoteAddress);
		this.setLocalAddress(localAddress);
		this.setRemoteTransactionId(msg.getOriginatingTransactionId());
		// convert to indications
		TCBeginIndicationImpl tcBeginIndication = (TCBeginIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
				.getDialogPrimitiveFactory()).createBeginIndication(this);

		tcBeginIndication.setDestinationAddress(localAddress);
		tcBeginIndication.setOriginatingAddress(remoteAddress);

		// if APDU and context data present, lets store it
		DialogPortion dialogPortion = msg.getDialogPortion();

		if (dialogPortion != null) {
			// this should not be null....
			DialogAPDU apdu = dialogPortion.getDialogAPDU();
			if (apdu.getType() != DialogAPDUType.Request) {
				throw new TCAPException("Received non-Request APDU: " + apdu.getType() + ". Dialog: " + this);
			}
			DialogRequestAPDU requestAPDU = (DialogRequestAPDU) apdu;
			this.lastACN = requestAPDU.getApplicationContextName();
			this.lastUI = requestAPDU.getUserInformation();
			tcBeginIndication.setApplicationContextName(this.lastACN);
			tcBeginIndication.setUserInformation(this.lastUI);
		}

		
		// lets deliver to provider
		this.provider.deliver(this, tcBeginIndication);
		// change state?
		this.setState(TRPseudoState.InitialReceived);

	}

	void processContinue(TCContinueMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {

		if (state == TRPseudoState.InitialSent) {
			//

			
			TCContinueIndicationImpl tcContinueIndication = (TCContinueIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
					.getDialogPrimitiveFactory()).createContinueIndication(this);
			// in continue remote address MAY change, so lets update!
			this.setRemoteAddress(remoteAddress);
			this.setRemoteTransactionId(msg.getOriginatingTransactionId());
			tcContinueIndication.setOriginatingAddress(remoteAddress);
			
			// here we will receive DialogResponse APDU - if request was
			// present!
			DialogPortion dialogPortion = msg.getDialogPortion();
			if (dialogPortion != null) {
				// this should not be null....
				DialogAPDU apdu = dialogPortion.getDialogAPDU();
				if (apdu.getType() != DialogAPDUType.Response) {
					throw new TCAPException("Received non-Response APDU: " + apdu.getType() + ". Dialog: " + this);
				}
				DialogResponseAPDU responseAPDU = (DialogResponseAPDU) apdu;
				// this will be present if APDU is present.
				if(!responseAPDU.getApplicationContextName().equals(this.lastACN))
				{
					this.lastACN = responseAPDU.getApplicationContextName();
				}	
				if(responseAPDU.getUserInformation()!=null)
				{
					this.lastUI = responseAPDU.getUserInformation();
				}
				tcContinueIndication.setApplicationContextName(responseAPDU.getApplicationContextName());
				tcContinueIndication.setUserInformation(responseAPDU.getUserInformation());
			}
			tcContinueIndication.setOriginatingAddress(remoteAddress);
			// now comps
			tcContinueIndication.setComponents(processOperationsState(tcContinueIndication.getComponents()));
			// lets deliver to provider
			this.provider.deliver(this, tcContinueIndication);
			
			
			
			// change state?
			this.setState(TRPseudoState.Active);
		} else if (state == TRPseudoState.Active) {

			// XXX: here NO APDU will be present, hence, no ACN/UI change
			TCContinueIndicationImpl tcContinueIndication = (TCContinueIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
					.getDialogPrimitiveFactory()).createContinueIndication(this);

			tcContinueIndication.setOriginatingAddress(remoteAddress);

			// now comps
			tcContinueIndication.setComponents(processOperationsState(tcContinueIndication.getComponents()));
			// lets deliver to provider
			this.provider.deliver(this, tcContinueIndication);
			
		} else {
			throw new TCAPException("Received Continue primitive, but state is not proper: " + this.state + ", Dialog: " + this);
		}

	}

	private Component[] processOperationsState(Component[] components) {
		if(components == null)
		{
			return null;
		}
		List<Component> resultingIndications = new ArrayList<Component>();
		for(Component ci : components)
		{
			int index = getIndexFromInvokeId(ci.getInvokeId());
			InvokeImpl invoke = this.operationsSent[index];
			switch(ci.getType())
			{
			
				case ReturnResultLast:
					
					if(invoke == null)
					{
						//FIXME: send something back?
					}else
					{
						invoke.onReturnResultLast();
						if(invoke.isSuccessReported())
						{
							resultingIndications.add(ci);
						}
						
					}
					break;
					
				//case Reject_U:
				//	break;
				//case Reject_R:
				//	break;
					
				case Error:
					if(invoke == null)
					{
						//FIXME: send something back?
					}else
					{
						invoke.onError();
						if(invoke.isErrorReported())
						{
							resultingIndications.add(ci);
						}
					}
					break;
				default:
					resultingIndications.add(ci);
					break;
			}
		}
		
		components = new Component[resultingIndications.size()];
		components = resultingIndications.toArray(components);
		return components;
		
	}

	void processEnd(TCEndMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {
		
		TCEndIndicationImpl tcEndIndication = (TCEndIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider.getDialogPrimitiveFactory())
				.createEndIndication(this);

		// now comps
		tcEndIndication.setComponents(processOperationsState(tcEndIndication.getComponents()));
		// lets deliver to provider
		this.provider.deliver(this, tcEndIndication);
		
		// this.setState(TRPseudoState.Idle);
		this.setState(TRPseudoState.Expunged);
	}

	private synchronized void setState(TRPseudoState newState) {
		// add checks?
		if (this.state == TRPseudoState.Expunged) {
			return;
		}
		this.state = newState;
		if (newState == TRPseudoState.Expunged) {

			 RemovalTimerTask rtt = new RemovalTimerTask();
			 rtt.d = this;
			 this.executor.schedule(rtt, _REMOVE_TIMEOUT,
			 TimeUnit.MILLISECONDS);
			//provider.release(this);
		}

	}


	private class RemovalTimerTask implements Runnable {
		DialogImpl d;

		public void run() {
			provider.release(d);

		}

	}
	
	
	
	//////////////////////
	// IND like methods //
	/////////////////////
	public void operationEnded(InvokeImpl tcInvokeRequestImpl) {
		//this op died cause of timeout, TC-L-CANCEL!
		int index = getIndexFromInvokeId(tcInvokeRequestImpl.getInvokeId());
		freeInvokeId(tcInvokeRequestImpl.getInvokeId());
		this.operationsSent[index] = null;
		//lets call listener
		//This is done actually with COmponentIndication ....
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#operationEnded(org.mobicents.protocols.ss7.tcap.tc.component.TCInvokeRequestImpl)
	 */
	public void operationTimedOut(InvokeImpl invoke) {
		//this op died cause of timeout, TC-L-CANCEL!
		int index = getIndexFromInvokeId(invoke.getInvokeId());
		freeInvokeId(invoke.getInvokeId());
		this.operationsSent[index] = null;
		//lets call listener
		this.provider.operationTimedOut(invoke);
		
	}
	
	//TC-TIMER-RESET
	public void  resetTimer(Long invokeId) throws TCAPException
	{
		int index = getIndexFromInvokeId(invokeId);
		InvokeImpl invoke = operationsSent[index];
		if(invoke == null)
		{
			throw new TCAPException("No operation with this ID");
		}
		invoke.startTimer();
	}
	
	public TRPseudoState getState(){
		return this.state;
	}
	
	
}
