/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 * 

MAP V3:
CancelLocationArg ::= [3] SEQUENCE {
	identity		Identity,
	cancellationType	CancellationType	OPTIONAL,
	extensionContainer	ExtensionContainer	OPTIONAL,
	...,
	typeOfUpdate	[0] TypeOfUpdate	OPTIONAL,
	mtrf-SupportedAndAuthorized	[1] NULL		OPTIONAL,
	mtrf-SupportedAndNotAuthorized	[2] NULL		OPTIONAL,
	newMSC-Number	[3] ISDN-AddressString	OPTIONAL,
	newVLR-Number	[4] ISDN-AddressString	OPTIONAL,
	new-lmsi		[5] LMSI		OPTIONAL
}
--mtrf-SupportedAndAuthorized and mtrf-SupportedAndNotAuthorized shall not
-- both be present

MAP V2:
CancelLocationArg ::= CHOICE {
	imsi			IMSI,
	imsi-WithLMSI	IMSI-WithLMSI
}

Identity ::= CHOICE {
	imsi			IMSI,
	imsi-WithLMSI	IMSI-WithLMSI}

 * 
 * @author sergey vetyutnev
 * 
 */
public interface CancelLocationRequest extends MobilityMessage {

	public IMSI getImsi();

	public IMSIWithLMSI getImsiWithLmsi();

	public CancellationType getCancellationType();

	public MAPExtensionContainer getExtensionContainer();

	public TypeOfUpdate getTypeOfUpdate();

	public boolean getMtrfSupportedAndAuthorized();

	public boolean getMtrfSupportedAndNotAuthorized();

	public ISDNAddressString getNewMSCNumber();

	public ISDNAddressString getNewVLRNumber();

	public LMSI getNewLmsi();

}
