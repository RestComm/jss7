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

import java.io.Serializable;

import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageFactory;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.gprs.CAPServiceGprs;
import org.mobicents.protocols.ss7.cap.api.service.sms.CAPServiceSms;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface CAPProvider extends Serializable {

	/**
	 * Add CAP Dialog listener to the Stack
	 * 
	 * @param capDialogListener
	 */
	public void addCAPDialogListener(CAPDialogListener capDialogListener);

	/**
	 * Remove CAP DIalog Listener from the stack
	 * 
	 * @param capDialogListener
	 */
	public void removeCAPDialogListener(CAPDialogListener capDialogListener);

	/**
	 * Get the {@link CAPParameterFactory}
	 * 
	 * @return
	 */
	public CAPParameterFactory getCAPParameterFactory();

	/**
	 * Get the {@link MAPParameterFactory}
	 * 
	 * @return
	 */
	public MAPParameterFactory getMAPParameterFactory();

	/**
	 * Get the {@link ISUPParameterFactory}
	 * 
	 * @return
	 */
	public ISUPParameterFactory getISUPParameterFactory();

	/**
	 * Get the {@link CAPErrorMessageFactory}
	 * 
	 * @return
	 */
	public CAPErrorMessageFactory getCAPErrorMessageFactory();
	
	/**
	 * Get {@link CAPDialog} corresponding to passed dialogId
	 * 
	 * @param dialogId
	 * @return
	 */
	public CAPDialog getCAPDialog(Long dialogId);

	public CAPServiceCircuitSwitchedCall getCAPServiceCircuitSwitchedCall();
	public CAPServiceGprs getCAPServiceGprs();
	public CAPServiceSms getCAPServiceSms();
		
}
