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

package org.mobicents.protocols.ss7.map.api.service.sms;

import org.mobicents.protocols.ss7.map.api.MAPServiceListener;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface MAPServiceSmsListener extends MAPServiceListener {

	public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd);

	public void onForwardShortMessageResponse(ForwardShortMessageResponse forwSmRespInd);

	public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd);

	public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd);

	public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwSmInd);

	public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwSmRespInd);

	public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd);

	public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMRespInd);
	
	public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd);

	public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd);

	public void onInformServiceCentreRequest(InformServiceCentreRequest informServiceCentreInd);
	
	public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreInd);

	public void onAlertServiceCentreResponse(AlertServiceCentreResponse alertServiceCentreInd);

}
