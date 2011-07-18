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

/**
 * Standard Operation Code included in Invoke. ETS 300 974: December 2000 (GSM
 * 09.02 version 5.15.1)
 * 
 * @author amit bhayani
 * 
 */
public interface MAPOperationCode {

	// -- supplementary service handling operation codes
	public static final int registerSS = 10;
	public static final int eraseSS = 11;
	public static final int activateSS = 12;
	public static final int deactivateSS = 13;
	public static final int interrogateSS = 14;

	public static final int processUnstructuredSS_Request = 59;
	public static final int unstructuredSS_Request = 60;
	public static final int unstructuredSS_Notify = 61;
	public static final int registerPassword = 17;
	public static final int getPassword = 18;
	
	
	//-- short message service operation codes
	public static final int  sendRoutingInfoForSM = 45;
	public static final int  mo_forwardSM = 46;
	public static final int  mt_forwardSM = 44;
	public static final int  reportSM_DeliveryStatus = 47;
	public static final int  informServiceCentre = 63;
	public static final int  alertServiceCentre = 64;
	public static final int  readyForSM = 66;
	
	//Location Service Management
	public static final int provideSubscriberLocation = 83;
	public static final int subscriberLocationReport = 86;
	
	public static final int sendRoutingInfoForLCS = 85;

}
