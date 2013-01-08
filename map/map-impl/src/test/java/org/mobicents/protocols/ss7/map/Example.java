package org.mobicents.protocols.ss7.map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
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
		return new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 1, null, 8);
	}

	private static SccpAddress createRemoteAddress() {
		return new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 2, null, 8);
	}
	
	
	public static void startUssdClient() throws Exception {
		SccpProvider sccpProvider = getSccpProvider(); // JNDI lookup of SCCP
		UssdClientExample client = new UssdClientExample(sccpProvider, 8);

		client.start();

		SccpAddress origAddress = createLocalAddress();
		ISDNAddressString origReference = client.getMAPProvider().getMAPParameterFactory()
				.createISDNAddressString(AddressNature.international_number, 
						NumberingPlan.land_mobile, "31628968300");
		SccpAddress destAddress = createRemoteAddress();
		ISDNAddressString destReference = client.getMAPProvider().getMAPParameterFactory()
				.createISDNAddressString(AddressNature.international_number, 
						NumberingPlan.land_mobile, "204208300008002");

		ISDNAddressString msisdn = client.getMAPProvider().getMAPParameterFactory()
				.createISDNAddressString(AddressNature.international_number, 
						NumberingPlan.ISDN, "31628838002");
		client.sendProcessUssdRequest(origAddress, origReference, destAddress, destReference, 
				"*123#", null, msisdn);
		
		// wait for answer
		Thread.sleep(600000);

		client.stop();
	}
	
	public static void startUssdServer() throws Exception {
		SccpProvider sccpProvider = getSccpProvider(); // JNDI lookup of SCCP
		UssdServerExample server = new UssdServerExample(sccpProvider, 8);

		server.start();
		
		// wait for a request
		Thread.sleep(600000);

		server.stop();
	}
}

