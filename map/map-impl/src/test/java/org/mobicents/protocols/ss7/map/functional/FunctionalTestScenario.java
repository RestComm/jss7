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

package org.mobicents.protocols.ss7.map.functional;

/**
 * Steps of MAP functional test
 * 
 * @author sergey vetyutnev
 * 
 */
public enum FunctionalTestScenario {
	/**
	 * TC-BEGIN + ExtensionContainer + addProcessUnstructuredSSRequest
	 * TC-CONTINUE + ExtensionContainer + addUnstructuredSSRequest 
	 * TC-END
	 */
	Action_Dialog_A(0),
	/**
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * refuse() -> TC-ABORT + MapRefuseInfo + ExtensionContainer
	 */
	Action_Dialog_B(1),
	/**
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-ABORT(Reason=ACN_Not_Supprted) + alternativeApplicationContextName 
	 */
	Action_Dialog_C(2),
	/**
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-END + ExtensionContainer + addUnstructuredSSRequest 
	 */
	Action_Dialog_D(3),
	/**
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-CONTINUE + addUnstructuredSSRequest 
	 * TC-ABORT(MAP-UserAbortInfo) + ExtensionContainer 
	 */
	Action_Dialog_E(4),
	/**
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-ABORT(MAP-ProviderAbortInfo) 
	 */
	Action_Dialog_F(5),
	/**
	 * TC-BEGIN + Ericsson-style MAP-OpenInfo + addProcessUnstructuredSSRequest
	 * TC-END
	 */
	Action_Dialog_Eri(6),

	/**
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-END + ReturnError(systemFailure)  
	 */
	Action_Component_A(11),
	/**
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-END + ReturnError(SM-DeliveryFailure + SM-DeliveryFailureCause)  
	 */
	Action_Component_B(12),
	/**
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-CONTINUE + ReturnResult (addProcessUnstructuredSSResponse)
	 * TC-CONTINUE  
	 * TC-END + ReturnResultLast (addProcessUnstructuredSSResponse) 
	 */
	Action_Component_D(13),
	/**
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-END + Reject (invokeProblem)  
	 */
	Action_Component_E(14),
	/**
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-END + ReturnError(systemFailure) - using "close(true)" - so no ReturnError must be sent !   
	 */
	Action_Component_F(15),
	/**
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-END + Reject (generalProblem) without invokeId!  
	 */
	Action_Component_G(16),

	/**
	 * TC-BEGIN + AlertServiceCentreRequest
	 * TC-END  
	 */
	Action_Sms_AlertServiceCentre(31),
	/**
	 * TC-BEGIN + MoForwardSMRequest
	 * TC-END + MoForwardSMResponse  
	 */
	Action_Sms_MoForwardSM(32),
	/**
	 * TC-BEGIN + MtForwardSMRequest
	 * TC-END + MtForwardSMResponse  
	 */
	Action_Sms_MtForwardSM(33),
	/**
	 * TC-BEGIN + SendRoutingInfoForSMRequest
	 * TC-END + SendRoutingInfoForSMResponse + InformServiceCentreRequest
	 */
	Action_Sms_SendRoutingInfoForSM(34),
	/**
	 * TC-BEGIN + ReportSMDeliveryStatusRequest
	 * TC-END + ReportSMDeliveryStatusResponse  
	 */
	Action_Sms_ReportSMDeliveryStatus(35),
	/**
	 * TC-BEGIN + ForwardSMRequest_V2
	 * TC-END + ForwardSMResponse_V2  
	 */
	Action_Sms_ForwardSM(37),

	/**
	 * TC-BEGIN+INVOKE(opCode=47) -> TC-END+RRL(opCode=47) (47=reportSM-DeliveryStatus)
	 */
	Action_V1_A(41),
	/**
	 * TC-BEGIN+INVOKE(opCode=49) -> release()
	 */
	Action_V1_B(42),
	/**
	 * TC-BEGIN(empty - no components) -> TC-ABORT V1
	 */
	Action_V1_C(43),
	/**
	 * TC-BEGIN(unsupported opCode) -> TC-ABORT V1
	 */
	Action_V1_D(44),
	/**
	 * TC-BEGIN+INVOKE(opCode=46) -> TC-CONTINUE(empty) -> TC-ABORT(UserReason) (->Abort V1)
	 */
	Action_V1_E(45),

	/**
	 * TC-BEGIN+MtForward(Short SMS) -> TC-END+MtForward(Response)
	 */
	Action_TestMsgLength_A(51),
	/**
	 * TC-BEGIN -> TC-CONTINUE -> TC-CONTINUE+MtForward(Long SMS) -> TC-END+MtForward(Response)
	 */
	Action_TestMsgLength_B(52);
	
	
	private int code;

	private FunctionalTestScenario(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}

}


