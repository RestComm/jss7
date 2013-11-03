/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;

/**
 *
 Ext2-QoS-Subscribed ::= OCTET STRING (SIZE (1..3)) -- Octets 1-3 are coded according to 3GPP TS 24.008 [35] Quality of
 * Service Octets 14-16. -- If Quality of Service information is structured with 14 octet length, then -- Octet 1 is coded
 * according to 3GPP TS 24.008 [35] Quality of Service Octet 14.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface Ext2QoSSubscribed extends Serializable {

    byte[] getData();

}
