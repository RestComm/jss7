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

package org.mobicents.protocols.ss7.cap.api;

import org.mobicents.protocols.ss7.cap.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface CAPServiceBase {
	public CAPProvider getCAPProvider();

	/**
	 * Creates a new Dialog.
	 * 
	 * @param applicationCntx
	 *            This parameter identifies the type of application context
	 *            being established. If the dialogue is accepted the received
	 *            application context name shall be echoed. In case of refusal
	 *            of dialogue this parameter shall indicate the highest version
	 *            supported.
	 * 
	 * @param destAddress
	 *            A valid SCCP address identifying the destination peer entity.
	 *            As an implementation option, this parameter may also, in the
	 *            indication, be implicitly associated with the service access
	 *            point at which the primitive is issued.
	 * 
	 * @param origAddress
	 *            A valid SCCP address identifying the requestor of a CAP
	 *            dialogue. As an implementation option, this parameter may
	 *            also, in the request, be implicitly associated with the
	 *            service access point at which the primitive is issued.
	 * 
	 * @return
	 */
	public CAPDialog createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress) throws CAPException;

	/**
	 * Returns true if the service can perform dialogs with given
	 * ApplicationContext
	 */
	public ServingCheckData isServingService(CAPApplicationContext dialogApplicationContext);


	public boolean isActivated();

	public void acivate();

	public void deactivate();
}

