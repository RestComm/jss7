package org.mobicents.protocols.ss7.map.service.sms;

import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponseIndication;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

public class SendRoutingInfoForSMResponseIndicationImpl extends SmsServiceImpl implements SendRoutingInfoForSMResponseIndication {
	
	protected static final int _TAG_LocationInfoWithLMSI = 0; 
	protected static final int _TAG_ExtensionContainer = 4; 

	private IMSI imsi;
	private LocationInfoWithLMSI locationInfoWithLMSI;
	private MAPExtensionContainer extensionContainer;
	
	
	public SendRoutingInfoForSMResponseIndicationImpl() {
	}	
	
	public SendRoutingInfoForSMResponseIndicationImpl(IMSI imsi, LocationInfoWithLMSI locationInfoWithLMSI, MAPExtensionContainer extensionContainer) {
		this.imsi = imsi;
		this.locationInfoWithLMSI = locationInfoWithLMSI;
		this.extensionContainer = extensionContainer;
	}	

	
	@Override
	public IMSI getIMSI() {
		return this.imsi;
	}

	@Override
	public LocationInfoWithLMSI getLocationInfoWithLMSI() {
		return this.locationInfoWithLMSI;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}
	
	
	public void decode(Parameter parameter) throws MAPParsingComponentException {

		Parameter[] parameters = parameter.getParameters();

		if (parameters.length < 2)
			throw new MAPParsingComponentException("Error while decoding sendRoutingInfoForSMResponse: Needs at least 2 mandatory parameters, found"
					+ parameters.length, MAPParsingComponentExceptionReason.MistypedParameter);

		// imsi
		Parameter p = parameters[0];
		if (p.getTagClass() != Tag.CLASS_UNIVERSAL || !p.isPrimitive() || p.getTag() != Tag.STRING_OCTET)
			throw new MAPParsingComponentException("Error while decoding sendRoutingInfoForSMResponse: Parameter 0 bad tag class or tag or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		this.imsi = new IMSIImpl();
		((IMSIImpl)this.imsi).decode(p);

		// locationInfoWithLMSI
		p = parameters[1];
		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()
				|| p.getTag() != SendRoutingInfoForSMResponseIndicationImpl._TAG_LocationInfoWithLMSI)
			throw new MAPParsingComponentException("Error while decoding sendRoutingInfoForSMResponse: Parameter 1 bad tag class or tag or primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		this.locationInfoWithLMSI = new LocationInfoWithLMSIImpl();
		((LocationInfoWithLMSIImpl)this.locationInfoWithLMSI).decode(p);

		for (int i1 = 2; i1 < parameters.length; i1++) {
			p = parameters[i1];

			if (p.getTag() == SendRoutingInfoForSMResponseIndicationImpl._TAG_ExtensionContainer && p.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				if (p.isPrimitive())
					throw new MAPParsingComponentException("Error while decoding sendRoutingInfoForSMResponse: Parameter extensionContainer is primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				this.extensionContainer = new MAPExtensionContainerImpl();
				((MAPExtensionContainerImpl)this.extensionContainer).decode(p);
			}
		}
	}

	public Parameter encode(ComponentPrimitiveFactory factory) throws MAPException {

		// Sequence of Parameter
		ArrayList<Parameter> lstPar = new ArrayList<Parameter>();

		// imsi
		Parameter p = ((IMSIImpl) this.imsi).encode();
		lstPar.add(p);

		// locationInfoWithLMSI
		p = ((LocationInfoWithLMSIImpl) this.locationInfoWithLMSI).encode();
		p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
		p.setTag(SendRoutingInfoForSMResponseIndicationImpl._TAG_LocationInfoWithLMSI);
		lstPar.add(p);

		// extensionContainer
		if (this.extensionContainer != null) {
			p = ((MAPExtensionContainerImpl) this.extensionContainer).encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(SendRoutingInfoForSMResponseIndicationImpl._TAG_ExtensionContainer);
			lstPar.add(p);
		}

		p = factory.createParameter();

		Parameter[] pp = new Parameter[lstPar.size()];
		lstPar.toArray(pp);
		p.setParameters(pp);

		return p;
	}
}
