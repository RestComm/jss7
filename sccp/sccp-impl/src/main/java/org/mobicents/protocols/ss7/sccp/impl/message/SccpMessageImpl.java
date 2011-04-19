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
import java.io.OutputStream;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author kulikov
 * @author baranowb
 */
public abstract class SccpMessageImpl implements SccpMessage {
	
	
    
    private int type;  //private :)
    protected SccpAddress calledParty;
    protected SccpAddress callingParty;
    protected ProtocolClass protocolClass;
    
    protected SccpMessageImpl(int type) {
        this.type = type;
    }
    
    public int getType() {
        return type;
    }

    public SccpAddress getCalledPartyAddress() {
		return calledParty;
	}

	public void setCalledPartyAddress(SccpAddress calledParty) {
		this.calledParty = calledParty;
	}

	public SccpAddress getCallingPartyAddress() {
		return callingParty;
	}

	public void setCallingPartyAddress(SccpAddress callingParty) {
		this.callingParty = callingParty;
	}

	public ProtocolClass getProtocolClass() {
		return protocolClass;
	}

	public void setProtocolClass(ProtocolClass protocolClass) {
		this.protocolClass = protocolClass;
	}

	public abstract void decode(InputStream in) throws IOException;
    public abstract void encode(OutputStream out) throws IOException;
}
