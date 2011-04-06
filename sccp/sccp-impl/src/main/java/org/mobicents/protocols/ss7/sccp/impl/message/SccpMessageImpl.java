/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
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
