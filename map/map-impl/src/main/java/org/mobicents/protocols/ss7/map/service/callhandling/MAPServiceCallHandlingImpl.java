package org.mobicents.protocols.ss7.map.service.callhandling;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.MAPServiceBaseImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckResult;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.MAPDialogCallHandling;
import org.mobicents.protocols.ss7.map.api.service.callhandling.MAPServiceCallHandling;
import org.mobicents.protocols.ss7.map.api.service.callhandling.MAPServiceCallHandlingListener;
import org.mobicents.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;


/*
 * 
 * @author cristian veliscu
 * 
 */
public class MAPServiceCallHandlingImpl extends MAPServiceBaseImpl implements MAPServiceCallHandling {
	private static final Logger loger = Logger.getLogger(MAPServiceCallHandlingImpl.class);

	// Include these constants in MAPApplicationContextName and MAPOperationCode
	// sendRoutingInfo_Request: add constant to MAPMessageType
	// sendRoutingInfo_Response: add constant to MAPMessageType
	protected static final int locationInfoRetrievalContext = 5;
	protected static final int sendRoutingInfo = 22;
	protected static final int version = 3;
	
	
	public MAPServiceCallHandlingImpl(MAPProviderImpl mapProviderImpl) {
		super(mapProviderImpl);
	}
	
	@Override
	public MAPDialogCallHandling createNewDialog(MAPApplicationContext appCntx, 
			    SccpAddress origAddress, AddressString origReference, 
				SccpAddress destAddress, AddressString destReference) throws MAPException {

		// We cannot create a dialog if the service is not activated
		if (!this.isActivated())
			throw new MAPException("Cannot create MAPDialogRoutingInformation because MAPServiceRoutingInformation is not activated");

		Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress);
		MAPDialogCallHandlingImpl dialog = new MAPDialogCallHandlingImpl(appCntx, tcapDialog, this.mapProviderImpl, this, origReference, destReference);

		this.putMAPDialogIntoCollection(dialog);
		return dialog;
	}
	
	@Override
	protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog) {
		return new MAPDialogCallHandlingImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
	}
	
	@Override
	public void addMAPServiceListener(MAPServiceCallHandlingListener mapServiceListener) {
		super.addMAPServiceListener(mapServiceListener);
	}

	@Override
	public void removeMAPServiceListener(MAPServiceCallHandlingListener mapServiceListener) {
		super.removeMAPServiceListener(mapServiceListener);
	}

	@Override
	public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
		MAPApplicationContextName ctx = dialogApplicationContext.getApplicationContextName();
		int vers = dialogApplicationContext.getApplicationContextVersion().getVersion();

		switch (ctx) {
		case locationInfoRetrievalContext:
			if (vers == 3) {
				return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
			} else {
				return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
			}
		}

		return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
	}
	
	@Override
	public MAPApplicationContext getMAPv1ApplicationContext(int operationCode, Invoke invoke) {
		return super.getMAPv1ApplicationContext(operationCode, invoke);
	}

	@Override
	public void processComponent(ComponentType compType, OperationCode oc,
			Parameter parameter, MAPDialog mapDialog, Long invokeId,
			Long linkedId) throws MAPParsingComponentException {
		
		if (compType == ComponentType.Invoke && this.mapProviderImpl.isCongested()) {
			// we reject all supplementary services when congestion
			return;
		}
		
		MAPDialogCallHandlingImpl mapDialogImpl = (MAPDialogCallHandlingImpl) mapDialog;
	
		Long ocValue = oc.getLocalOperationCode();
		if (ocValue == null)
			new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);

		if(ocValue.intValue() ==  MAPOperationCode.sendRoutingInfo) {
		  if (compType == ComponentType.Invoke) 
			 this.sendRoutingInformationRequest(parameter, mapDialogImpl, invokeId);
		  else if(compType == ComponentType.ReturnResult || 
				  compType == ComponentType.ReturnResultLast)
			      this.sendRoutingInformationResponse(parameter, mapDialogImpl, invokeId);
		}
	}
	
	private void sendRoutingInformationRequest(Parameter parameter, MAPDialogCallHandlingImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		if (parameter == null)
			throw new MAPParsingComponentException("Error while decoding SendRoutingInformationRequestIndication: Parameter is mandatory but not found",
					MAPParsingComponentExceptionReason.MistypedParameter);

		if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
			throw new MAPParsingComponentException(
					"Error while decoding SendRoutingInformationRequestIndication: Bad tag or tagClass or parameter is primitive, received tag=" + parameter.getTag(),
					MAPParsingComponentExceptionReason.MistypedParameter);

		byte[] buf = parameter.getData();
		AsnInputStream ais = new AsnInputStream(buf);

		SendRoutingInformationRequestImpl ind = new SendRoutingInformationRequestImpl();
		ind.decodeData(ais, buf.length);
		ind.setInvokeId(invokeId);
		ind.setMAPDialog(mapDialogImpl);

		for (MAPServiceListener serLis : this.serviceListeners) {
			try {
				serLis.onMAPMessage(ind);
				((MAPServiceCallHandlingListener) serLis).onSendRoutingInformationRequest(ind);
			} catch (Exception e) {
				loger.error("Error processing SendRoutingInformationRequestIndication: " + e.getMessage(), e);
			}
		}
	}
	
	private void sendRoutingInformationResponse(Parameter parameter, MAPDialogCallHandlingImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		if (parameter == null)
			throw new MAPParsingComponentException("Error while decoding SendRoutingInformationResponseIndication: Parameter is mandatory but not found",
					MAPParsingComponentExceptionReason.MistypedParameter);

		if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
			throw new MAPParsingComponentException(
					"Error while decoding SendRoutingInformationResponseIndication: Bad tag or tagClass or parameter is primitive, received tag=" + parameter.getTag(),
					MAPParsingComponentExceptionReason.MistypedParameter);

		byte[] buf = parameter.getData();
		AsnInputStream ais = new AsnInputStream(buf);

		SendRoutingInformationResponseImpl ind = new SendRoutingInformationResponseImpl();
		ind.decodeData(ais, buf.length);
		ind.setInvokeId(invokeId);
		ind.setMAPDialog(mapDialogImpl);

		for (MAPServiceListener serLis : this.serviceListeners) {
			try {
				serLis.onMAPMessage(ind);
				((MAPServiceCallHandlingListener) serLis).onSendRoutingInformationResponse(ind);
			} catch (Exception e) {
				loger.error("Error processing SendRoutingInformationResponseIndication: " + e.getMessage(), e);
			}
		}
	}
}