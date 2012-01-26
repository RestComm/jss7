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

package org.mobicents.protocols.ss7.sccp.impl.message;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.UnitData;
import org.mobicents.protocols.ss7.sccp.message.UnitDataService;
import org.mobicents.protocols.ss7.sccp.message.XUnitData;
import org.mobicents.protocols.ss7.sccp.message.XUnitDataService;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCause;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author kulikov
 */
public class MessageFactoryImpl implements MessageFactory {
	private static final Logger logger = Logger.getLogger(MessageFactoryImpl.class);
	
	private boolean removeSpc = false;
	
	public MessageFactoryImpl(boolean removeSpc){
		this.removeSpc = removeSpc;
	}

	
	//TODO: add checks, PC vs Q.713 Table 1
	public UnitData createUnitData(ProtocolClass pClass, SccpAddress calledParty, SccpAddress callingParty) {
		return new UnitDataImpl(pClass, calledParty, callingParty, this.removeSpc);
	}

	public UnitDataService createUnitDataService(ReturnCause returnCause, SccpAddress calledParty, SccpAddress callingParty) {
		return new UnitDataServiceImpl(returnCause, calledParty, callingParty, this.removeSpc);
	}
	
	public XUnitData createXUnitData(HopCounter hopCounter, ProtocolClass pClass, SccpAddress calledParty, SccpAddress callingParty) {
		return new XUnitDataImpl(hopCounter, pClass, calledParty, callingParty, this.removeSpc);
	}

	public XUnitDataService createXUnitDataService(HopCounter hopCounter, ReturnCause rc, SccpAddress calledParty, SccpAddress callingParty) {
		return new XUnitDataServiceImpl(hopCounter, rc, calledParty, callingParty, this.removeSpc);
	}

	public SccpMessageImpl createMessage(int type, InputStream in) throws IOException {
		SccpMessageImpl msg = null;
		switch (type) {
		case UnitData.MESSAGE_TYPE:
			msg = new UnitDataImpl(this.removeSpc);
			break;
		case UnitDataService.MESSAGE_TYPE:
			msg = new UnitDataServiceImpl(this.removeSpc);
			break;
		case XUnitData.MESSAGE_TYPE:
			msg = new XUnitDataImpl(this.removeSpc);
			break;
		case XUnitDataService.MESSAGE_TYPE:
			msg = new XUnitDataServiceImpl(this.removeSpc);
			break;
		}
		if (msg != null) {
			msg.decode(in);
		} else if (logger.isEnabledFor(Level.WARN)) {
			logger.warn("No message implementation for MT: " + type);
		}
		return msg;
	}

}
