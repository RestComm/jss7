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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * 

M-CSI ::= SEQUENCE {
	mobilityTriggers	MobilityTriggers,
	serviceKey	ServiceKey,
	gsmSCF-Address	[0]	ISDN-AddressString,
	extensionContainer	[1]	ExtensionContainer	OPTIONAL,
	notificationToCSE	[2] NULL		OPTIONAL,
	csi-Active	[3] NULL		OPTIONAL,
	...}
--	notificationToCSE and csi-Active shall not be present when M-CSI is sent to VLR.
--	They may only be included in ATSI/ATM ack/NSDC message.

MobilityTriggers  ::= SEQUENCE SIZE (1..10) OF MM-Code

ServiceKey ::= INTEGER (0..2147483647)

 * 
 * 
 * @author sergey vetyutnev
 * 
 */
public interface MCSI {

	public ArrayList<MMCode> getMobilityTriggers();

	public long getServiceKey();

	public ISDNAddressString getGsmSCFAddress();

	public MAPExtensionContainer getExtensionContainer();

	public boolean getNotificationToCSE();

	public boolean getCsiActive();

}
