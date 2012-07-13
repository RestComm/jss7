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

package org.mobicents.protocols.ss7.map.api.service.mobility;

import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface MAPServiceMobilityListener extends MAPServiceListener {

	// -- Location Management Service
	public void onUpdateLocationRequest(UpdateLocationRequest ind);
	public void onUpdateLocationResponse(UpdateLocationResponse ind);

	// -- Authentication management services
	public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest ind);
	public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse ind);
	
	// -- Subscriber Information services
	public void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest request);
	public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse response);
	
	// -- International mobile equipment identities management services
	public void onCheckImeiRequest(CheckImeiRequest request);
	public void onCheckImeiResponse(CheckImeiResponse response);

}
