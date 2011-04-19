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

package org.mobicents.protocols.ss7.map.api;

import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface MAPProvider {

	public static final int NETWORK_UNSTRUCTURED_SS_CONTEXT_V2 = 1;

	/**
	 * Creates a new Dialog. This is equivalent to issuing MAP_OPEN Service
	 * Request to MAP Provider.
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
	 * @param destReference
	 *            This parameter is a reference which refines the identification
	 *            of the called process. It may be identical to Destination
	 *            address but its value is to be carried at MAP level.
	 * 
	 * @param origAddress
	 *            A valid SCCP address identifying the requestor of a MAP
	 *            dialogue. As an implementation option, this parameter may
	 *            also, in the request, be implicitly associated with the
	 *            service access point at which the primitive is issued.
	 * 
	 * @param origReference
	 *            This parameter is a reference which refines the identification
	 *            of the calling process. It may be identical to the Originating
	 *            address but its value is to be carried at MAP level.
	 *            Processing of the Originating-reference shall be performed
	 *            according to the supplementary service descriptions and other
	 *            service descriptions, e.g. operator determined barring.
	 * @return
	 */
	public MAPDialog createNewDialog(MAPApplicationContext appCntx,
			SccpAddress origAddress, AddressString origReference,
			SccpAddress destAddress, AddressString destReference)
			throws MAPException;

	/**
	 * Add MAP Dialog listener to the Stack
	 * 
	 * @param mapDialogListener
	 */
	public void addMAPDialogListener(MAPDialogListener mapDialogListener);

	/**
	 * Remove MAP DIalog Listener from the stack
	 * 
	 * @param mapDialogListener
	 */
	public void removeMAPDialogListener(MAPDialogListener mapDialogListener);

	/**
	 * Add MAP Service listener to the stack
	 * 
	 * @param mapServiceListener
	 */
	public void addMAPServiceListener(MAPServiceListener mapServiceListener);

	/**
	 * Remove MAP Service listener from the stack
	 * 
	 * @param mapServiceListener
	 */
	public void removeMAPServiceListener(MAPServiceListener mapServiceListener);

	/**
	 * Get the {@link MapServiceFactory}
	 * 
	 * @return
	 */
	public MapServiceFactory getMapServiceFactory();

	/**
	 * Get {@link MAPDialog} corresponding to passed dialogId
	 * 
	 * @param dialogId
	 * @return
	 */
	public MAPDialog getMAPDialog(Long dialogId);

}
