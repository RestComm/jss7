package org.mobicents.protocols.ss7.cap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.NAINumber;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public class Example {

	private static SccpProvider getSccpProvider() throws NamingException {
		// no arg is ok, if we run in JBoss

		InitialContext ctx = new InitialContext();
		try {
			String providerJndiName = "/mobicents/ss7/sccp";
			return ((SccpStack) ctx.lookup(providerJndiName)).getSccpProvider();
		} finally {
			ctx.close();
		}
	}

	private static SccpAddress createLocalAddress() {
		return new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 1, null, 146);
	}

	private static SccpAddress createRemoteAddress() {
		return new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 2, null, 146);
	}


	public static void startCallSsf() throws Exception {
		SccpProvider sccpProvider = getSccpProvider(); // JNDI lookup of SCCP
		CallSsfExample client = new CallSsfExample(sccpProvider, 146);

		client.start();

		SccpAddress origAddress = createLocalAddress();
		SccpAddress destAddress = createRemoteAddress();

		int serviceKey = 1;

		CalledPartyNumber calledPartyNumber = client.getCAPProvider().getISUPParameterFactory().createCalledPartyNumber();
		calledPartyNumber.setAddress("552348762");
		calledPartyNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
		calledPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
		calledPartyNumber.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
		CalledPartyNumberCap calledPartyNumberCap = client.getCAPProvider().getCAPParameterFactory().createCalledPartyNumberCap(calledPartyNumber);

		CallingPartyNumber callingPartyNumber = client.getCAPProvider().getISUPParameterFactory().createCallingPartyNumber();
		callingPartyNumber.setAddress("55998223");
		callingPartyNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
		callingPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
		callingPartyNumber.setAddressRepresentationREstrictedIndicator(CallingPartyNumber._APRI_ALLOWED);
		callingPartyNumber.setScreeningIndicator(CallingPartyNumber._SI_NETWORK_PROVIDED);
		CallingPartyNumberCap callingPartyNumberCap = client.getCAPProvider().getCAPParameterFactory().createCallingPartyNumberCap(callingPartyNumber);

		LocationNumber locationNumber = client.getCAPProvider().getISUPParameterFactory().createLocationNumber();
		locationNumber.setAddress("55200001");
		locationNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
		locationNumber.setNumberingPlanIndicator(LocationNumber._NPI_ISDN);
		locationNumber.setAddressRepresentationRestrictedIndicator(LocationNumber._APRI_ALLOWED);
		locationNumber.setScreeningIndicator(LocationNumber._SI_NETWORK_PROVIDED);
		locationNumber.setInternalNetworkNumberIndicator(LocationNumber._INN_ROUTING_ALLOWED);
		LocationNumberCap locationNumberCap = client.getCAPProvider().getCAPParameterFactory().createLocationNumberCap(locationNumber);

		ISDNAddressString vlrNumber = client.getCAPProvider().getMAPParameterFactory()
				.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "552000002");
		LocationInformation locationInformation = client.getCAPProvider().getMAPParameterFactory()
				.createLocationInformation(10, null, vlrNumber, null, null, null, null, vlrNumber, null, false, false, null, null);

		client.sendInitialDP(origAddress, destAddress, serviceKey, calledPartyNumberCap, callingPartyNumberCap, locationNumberCap, EventTypeBCSM.collectedInfo,
				locationInformation);
		
		// wait for answer
		Thread.sleep(600000);

		client.stop();
	}

	public static void startCallScf() throws Exception {
		SccpProvider sccpProvider = getSccpProvider(); // JNDI lookup of SCCP
		CallScfExample server = new CallScfExample(sccpProvider, 146);

		server.start();
		
		// wait for a request
		Thread.sleep(600000);

		server.stop();
	}
}
