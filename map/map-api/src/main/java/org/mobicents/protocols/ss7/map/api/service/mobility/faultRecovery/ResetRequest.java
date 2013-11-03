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

package org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NetworkResource;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 MAP V1-2:
 *
 * reset OPERATION ::= { --Timer m ARGUMENT ResetArg CODE local:37 }
 *
 * ResetArg ::= SEQUENCE { networkResource NetworkResource OPTIONAL, -- networkResource must be present in version 1 --
 * networkResource must be absent in version greater 1
 *
 * hlr-Number ISDN-AddressString, hlr-List HLR-List OPTIONAL, ...}
 *
 * HLR-List ::= SEQUENCE SIZE (1..50) OF HLR-Id
 *
 * HLR-Id ::= IMSI -- leading digits of IMSI, i.e. (MCC, MNC, leading digits of -- MSIN) forming HLR Id defined in TS 3GPP TS
 * 23.003 [17].
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ResetRequest extends MobilityMessage {

    NetworkResource getNetworkResource();

    ISDNAddressString getHlrNumber();

    ArrayList<IMSI> getHlrList();

}
