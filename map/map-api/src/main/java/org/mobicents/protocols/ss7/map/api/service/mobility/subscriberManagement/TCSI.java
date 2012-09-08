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
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * 

T-CSI ::= SEQUENCE {
	t-BcsmCamelTDPDataList	T-BcsmCamelTDPDataList,
	extensionContainer	ExtensionContainer	OPTIONAL,
	...,
	camelCapabilityHandling	[0] CamelCapabilityHandling	OPTIONAL,
	notificationToCSE	[1] NULL		OPTIONAL,
	csi-Active	[2] NULL		OPTIONAL}
--	notificationToCSE and csi-Active shall not be present when VT-CSI/T-CSI is sent
--	to VLR/GMSC.
--	They may only be included in ATSI/ATM ack/NSDC message.
--	T-CSI shall not be segmented.

T-BcsmCamelTDPDataList ::= SEQUENCE SIZE (1..10) OF T-BcsmCamelTDPData
	--- T-BcsmCamelTDPDataList shall not contain more than one instance of
	--- T-BcsmCamelTDPData containing the same value for t-BcsmTriggerDetectionPoint.
	--- For CAMEL Phase 2, this means that only one instance of T-BcsmCamelTDPData is allowed
	--- with t-BcsmTriggerDetectionPoint being equal to DP12. 
	--- For CAMEL Phase 3, more TDPs are allowed.

CamelCapabilityHandling ::= INTEGER(1..16) 
	-- value 1 = CAMEL phase 1,
	-- value 2 = CAMEL phase 2,
	-- value 3 = CAMEL Phase 3,
	-- value 4 = CAMEL phase 4:
	-- reception of values greater than 4 shall be treated as CAMEL phase 4.

 * 
 * 
 * @author sergey vetyutnev
 * 
 */
public interface TCSI {

	public ArrayList<TBcsmCamelTDPData> getTBcsmCamelTDPDataList();

	public MAPExtensionContainer getExtensionContainer();

	public Integer getCamelCapabilityHandling();

	public MAPExtensionContainer getNotificationToCSE();

	public MAPExtensionContainer getCsiActive();

}
