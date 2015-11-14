/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.mobicents.protocols.ss7.isup.message;

import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectionRequest;
import org.mobicents.protocols.ss7.isup.message.parameter.FacilityIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserIndicators;

/**
 * Super interface for FAA/FAR
 *
 * @author baranowb
 *
 */
public interface AbstractFacilityMessage extends ISUPMessage {
    //bad name for interface, but...
    void setFacilityIndicator(FacilityIndicator fi);

    FacilityIndicator getFacilityIndicator();

    void setUserToUserIndicators(UserToUserIndicators u2ui);

    UserToUserIndicators getUserToUserIndicators();

    void setCallReference(CallReference cf);

    CallReference getCallReference();

    void setConnectionRequest(ConnectionRequest cr);

    ConnectionRequest getConnectionRequest();

    void setParameterCompatibilityInformation(ParameterCompatibilityInformation pci);

    ParameterCompatibilityInformation getCompatibilityInformation();
}
