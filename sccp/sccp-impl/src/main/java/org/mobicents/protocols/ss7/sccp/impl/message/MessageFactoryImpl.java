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
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.MessageType;
import org.mobicents.protocols.ss7.sccp.message.UnitData;
import org.mobicents.protocols.ss7.sccp.message.XUnitData;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author kulikov
 */
public class MessageFactoryImpl implements MessageFactory {

    public UnitData createUnitData(ProtocolClass pClass, SccpAddress calledParty,
            SccpAddress callingParty) {
        return new UnitDataImpl(pClass, calledParty, callingParty);
    }

    public XUnitData createXUnitData(HopCounter hopCounter, ProtocolClass pClass, SccpAddress calledParty, SccpAddress callingParty) {
        return new XUnitDataImpl(hopCounter, pClass, calledParty, callingParty);
    }

    public SccpMessageImpl createMessage(int type, InputStream in) throws IOException {
        SccpMessageImpl msg = null;
        switch (type) {
            case MessageType.UDT :
                msg = new UnitDataImpl();
                break;
            case MessageType.XUDT :
                msg = new XUnitDataImpl();
        }
        if (msg != null) {
            msg.decode(in);
        }
        return msg;
    }
    
}
