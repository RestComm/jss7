/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.map.service.pdpContextActivation;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.MAPServiceBaseImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckResult;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.MAPDialogPdpContextActivation;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.MAPServicePdpContextActivation;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.MAPServicePdpContextActivationListener;
import org.mobicents.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPServicePdpContextActivationImpl extends MAPServiceBaseImpl implements MAPServicePdpContextActivation {

    protected Logger loger = Logger.getLogger(MAPServicePdpContextActivationImpl.class);

    public MAPServicePdpContextActivationImpl(MAPProviderImpl mapProviderImpl) {
        super(mapProviderImpl);
    }

    /*
     * Creating a new outgoing MAP PdpContextActivation dialog and adding it to the MAPProvider.dialog collection
     */
    public MAPDialogPdpContextActivation createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference,
            SccpAddress destAddress, AddressString destReference) throws MAPException {
        return this.createNewDialog(appCntx, origAddress, origReference, destAddress, destReference, null);
    }

    public MAPDialogPdpContextActivation createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference,
            SccpAddress destAddress, AddressString destReference, Long localTrId) throws MAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new MAPException(
                    "Cannot create MAPDialogPdpContextActivation because MAPServicePdpContextActivation is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        MAPDialogPdpContextActivationImpl dialog = new MAPDialogPdpContextActivationImpl(appCntx, tcapDialog,
                this.mapProviderImpl, this, origReference, destReference);

        this.putMAPDialogIntoCollection(dialog);

        return dialog;
    }

    protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog) {
        return new MAPDialogPdpContextActivationImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
    }

    public void addMAPServiceListener(MAPServicePdpContextActivationListener mapServiceListener) {
        super.addMAPServiceListener(mapServiceListener);
    }

    public void removeMAPServiceListener(MAPServicePdpContextActivationListener mapServiceListener) {
        super.removeMAPServiceListener(mapServiceListener);
    }

    public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
        // TODO Implement it

        return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
    }

    public void processComponent(ComponentType compType, OperationCode oc, Parameter parameter, MAPDialog mapDialog,
            Long invokeId, Long linkedId, Invoke linkedInvoke) throws MAPParsingComponentException {
        // TODO Implement it
    }
}
