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

package org.mobicents.protocols.ss7.map.service.mobility;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.PlmnId;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoResponseImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MAPDialogMobilityImpl extends MAPDialogImpl implements MAPDialogMobility {

	protected MAPDialogMobilityImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl, MAPServiceMobility mapService,
			AddressString origReference, AddressString destReference) {
		super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
	}

	public Long addSendAuthenticationInfoRequest(long mapProtocolVersion, IMSI imsi, int numberOfRequestedVectors, boolean segmentationProhibited,
			boolean immediateResponsePreferred, ReSynchronisationInfo reSynchronisationInfo, MAPExtensionContainer extensionContainer,
			RequestingNodeType requestingNodeType, PlmnId requestingPlmnId, Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS)
			throws MAPException {
		return this.addSendAuthenticationInfoRequest(_Timer_Default, mapProtocolVersion, imsi, numberOfRequestedVectors, segmentationProhibited,
				immediateResponsePreferred, reSynchronisationInfo, extensionContainer, requestingNodeType, requestingPlmnId,
				numberOfRequestedAdditionalVectors, additionalVectorsAreForEPS);
	}

	public Long addSendAuthenticationInfoRequest(int customInvokeTimeout, long mapProtocolVersion, IMSI imsi, int numberOfRequestedVectors,
			boolean segmentationProhibited, boolean immediateResponsePreferred, ReSynchronisationInfo reSynchronisationInfo,
			MAPExtensionContainer extensionContainer, RequestingNodeType requestingNodeType, PlmnId requestingPlmnId,
			Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) throws MAPException {

		if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.infoRetrievalContext)
				|| (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
			throw new MAPException("Bad application context name for sendAuthenticationInfoRequest: must be infoRetrievalContext_V2 or V3");

		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
		if (customInvokeTimeout == _Timer_Default)
			invoke.setTimeout(_Timer_ml);
		else
			invoke.setTimeout(customInvokeTimeout);

		OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.sendAuthenticationInfo);
		invoke.setOperationCode(oc);

		SendAuthenticationInfoRequestImpl req = new SendAuthenticationInfoRequestImpl(mapProtocolVersion, imsi, numberOfRequestedVectors,
				segmentationProhibited, immediateResponsePreferred, reSynchronisationInfo, extensionContainer, requestingNodeType, requestingPlmnId,
				numberOfRequestedAdditionalVectors, additionalVectorsAreForEPS);
		AsnOutputStream aos = new AsnOutputStream();
		req.encodeData(aos);

		Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
		p.setTagClass(req.getTagClass());
		p.setPrimitive(req.getIsPrimitive());
		p.setTag(req.getTag());
		p.setData(aos.toByteArray());
		invoke.setParameter(p);

		Long invokeId;
		try {
			invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);
		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}

		this.sendInvokeComponent(invoke);

		return invokeId;
	}

	public void addSendAuthenticationInfoResponse(long invokeId, long mapProtocolVersion, AuthenticationSetList authenticationSetList,
			MAPExtensionContainer extensionContainer, EpsAuthenticationSetList epsAuthenticationSetList) throws MAPException {

		if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.infoRetrievalContext)
				|| (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
			throw new MAPException("Bad application context name for addSendAuthenticationInfoResponse: must be infoRetrievalContext_V2 or V3");

		ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

		resultLast.setInvokeId(invokeId);

		// Operation Code
		OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.sendAuthenticationInfo);
		resultLast.setOperationCode(oc);

		if (authenticationSetList != null || extensionContainer != null || epsAuthenticationSetList != null) {

			SendAuthenticationInfoResponseImpl req = new SendAuthenticationInfoResponseImpl(mapProtocolVersion, authenticationSetList, extensionContainer,
					epsAuthenticationSetList);
			AsnOutputStream aos = new AsnOutputStream();
			req.encodeData(aos);

			Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(req.getTagClass());
			p.setPrimitive(req.getIsPrimitive());
			p.setTag(req.getTag());
			p.setData(aos.toByteArray());
			resultLast.setParameter(p);
		}

		this.sendReturnResultLastComponent(resultLast);
	}
}
