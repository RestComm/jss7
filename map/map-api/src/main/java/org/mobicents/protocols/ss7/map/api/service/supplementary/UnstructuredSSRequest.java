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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;


/**
 * unstructuredSS-Request OPERATION ::= { --Timer ml
 *	ARGUMENT
 *		USSD-Arg
 *	RESULT
 *		USSD-Res
 *		-- optional
 *	ERRORS {
 *		systemFailure |
 *		dataMissing |
 *		unexpectedDataValue |
 *		absentSubscriber |
 *		illegalSubscriber |
 *		illegalEquipment |
 *		unknownAlphabet |
 *		ussd-Busy}
 *		CODE local:60 }
 * 
 * This service is used between the HLR and the VLR and between the VLR and the
 * MSC when the invoking entity requires information from the mobile user, in
 * connection with unstructured supplementary service handling.
 * 
 * @author amit bhayani
 * 
 */
public interface UnstructuredSSRequest extends SupplementaryMessage {
	public ISDNAddressString getMSISDNAddressString();
	
	public AlertingPattern getAlertingPattern();
}
