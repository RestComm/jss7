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

	public void onForwardShortMessageIndication(ForwardShortMessageRequest forwSmInd);

	public void onForwardShortMessageRespIndication(ForwardShortMessageResponse forwSmRespInd);

	public void onMoForwardShortMessageIndication(MoForwardShortMessageRequest moForwSmInd);

	public void onMoForwardShortMessageRespIndication(MoForwardShortMessageResponse moForwSmRespInd);

	public void onMtForwardShortMessageIndication(MtForwardShortMessageRequest mtForwSmInd);

	public void onMtForwardShortMessageRespIndication(MtForwardShortMessageResponse mtForwSmRespInd);

	public void onSendRoutingInfoForSMIndication(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd);

	public void onSendRoutingInfoForSMRespIndication(SendRoutingInfoForSMResponse sendRoutingInfoForSMRespInd);
	
	public void onReportSMDeliveryStatusIndication(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd);

	public void onReportSMDeliveryStatusRespIndication(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd);

	public void onInformServiceCentreIndication(InformServiceCentreRequest informServiceCentreInd);
	
	public void onAlertServiceCentreIndication(AlertServiceCentreRequest alertServiceCentreInd);

	public void onAlertServiceCentreRespIndication(AlertServiceCentreResponse alertServiceCentreInd);

}
