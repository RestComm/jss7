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

package org.mobicents.protocols.ss7.tools.simulator.tests.sms;

import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.MapProtocolVersion;
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapMan;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TestSmsServerMan extends TesterBase implements TestSmsServerManMBean, Stoppable, MAPDialogListener, MAPServiceSmsListener {

	public static String SOURCE_NAME = "TestSmsServer";

	private static final String MSISDN_ADDRESS = "msisdnAddress";

	private final String name;
	private MapMan mapMan;


	public TestSmsServerMan() {
		super(SOURCE_NAME);
		this.name = "???";
	}

	public TestSmsServerMan(String name) {
		super(SOURCE_NAME);
		this.name = name;
	}

	@Override
	public void onAlertServiceCentreRequest(AlertServiceCentreRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAlertServiceCentreResponse(AlertServiceCentreResponse arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onForwardShortMessageRequest(ForwardShortMessageRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onForwardShortMessageResponse(ForwardShortMessageResponse arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInformServiceCentreRequest(InformServiceCentreRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AddressNature getServiceCenterAddressAddressNature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setServiceCenterAddressAddressNature(AddressNature val) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NumberingPlan getServiceCenterAddressNumberingPlan() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setServiceCenterAddressNumberingPlan(NumberingPlan val) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MapProtocolVersion getMapProtocolVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMapProtocolVersion_Value() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMapProtocolVersion(MapProtocolVersion val) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putMapProtocolVersion(String val) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String performSRIForSM_MtForwardSM(String msg, String origIsdnNumber, String targetIsdnNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String performMtForwardSM(String msg, String targetImsi, String vlrNumber, String origIsdnNumber, String targetIsdnNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String closeCurrentDialog() {
		// TODO Auto-generated method stub
		return null;
	}

}
