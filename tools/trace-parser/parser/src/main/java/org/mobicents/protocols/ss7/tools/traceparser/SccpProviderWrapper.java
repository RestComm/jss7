package org.mobicents.protocols.ss7.tools.traceparser;

import java.io.IOException;

import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;


/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SccpProviderWrapper implements SccpProvider {

	@Override
	public MessageFactory getMessageFactory() {
		return new MessageFactoryImpl();
	}

	@Override
	public ParameterFactory getParameterFactory() {
		return new ParameterFactoryImpl();
	}

	@Override
	public void registerSccpListener(int ssn, SccpListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deregisterSccpListener(int ssn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(SccpMessage message, int seqControl) throws IOException {
		// TODO Auto-generated method stub

	}

}

