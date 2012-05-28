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

import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPException;
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
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;

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

	@Override
	public Long addSendAuthenticationInfoRequest(long mapProtocolVersion, IMSI imsi, int numberOfRequestedVectors, boolean segmentationProhibited,
			boolean immediateResponsePreferred, ReSynchronisationInfo reSynchronisationInfo, MAPExtensionContainer extensionContainer,
			RequestingNodeType requestingNodeType, PlmnId requestingPlmnId, Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS)
			throws MAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long addSendAuthenticationInfoRequest(int customInvokeTimeout, long mapProtocolVersion, IMSI imsi, int numberOfRequestedVectors,
			boolean segmentationProhibited, boolean immediateResponsePreferred, ReSynchronisationInfo reSynchronisationInfo,
			MAPExtensionContainer extensionContainer, RequestingNodeType requestingNodeType, PlmnId requestingPlmnId,
			Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) throws MAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addSendAuthenticationInfoResponse(long invokeId, long mapProtocolVersion, AuthenticationSetList authenticationSetList,
			MAPExtensionContainer extensionContainer, EpsAuthenticationSetList epsAuthenticationSetList) throws MAPException {
		// TODO Auto-generated method stub
		
	}
}

