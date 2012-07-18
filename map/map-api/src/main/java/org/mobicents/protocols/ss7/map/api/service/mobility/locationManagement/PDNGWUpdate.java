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

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWIdentity;

/**
 * 

PDN-GW-Update ::= SEQUENCE{
	apn			[0] APN		OPTIONAL,
	pdn-gw-Identity	[1] PDN-GW-Identity	OPTIONAL,
	contextId		[2] ContextId                     OPTIONAL,
	extensionContainer	[3] ExtensionContainer	OPTIONAL,
	... }
--	The pdn-gw-update IE shall include the pdn-gw-Identity, and the apn or/and the contextID. 
--	The HSS shall ignore the eps-info IE if it includes a pdn-gw-update IE which does not 
--	include pdn-gw-Identity.
--	The pdn-gw-Identity is defined as OPTIONAL for backward compatility reason with  
--	outdated earlier versions of this specification.

ContextId ::= INTEGER (1..50)

 * 
 * @author sergey vetyutnev
 * 
 */
public interface PDNGWUpdate {

	public APN getAPN();

	public PDNGWIdentity getPdnGwIdentity();

	public Integer getContextId();

	public MAPExtensionContainer getExtensionContainer();

}
