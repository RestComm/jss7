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

package org.mobicents.protocols.ss7.map;

import java.util.HashSet;
import java.util.Set;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPServiceBase;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

/**
 * This class must be the super class of all MAP services
 * 
 * @author sergey vetyutnev
 * 
 */
public abstract class MAPServiceBaseImpl implements MAPServiceBase {
	protected Boolean _isActivated = false;
	protected Set<MAPServiceListener> serviceListeners = new HashSet<MAPServiceListener>();
	protected MAPProviderImpl mapProviderImpl = null;

	protected MAPServiceBaseImpl(MAPProviderImpl mapProviderImpl) {
		this.mapProviderImpl = mapProviderImpl;
	}

	public MAPProvider getMAPProvider() {
		return this.getMAPProvider();
	}

	/**
	 * Creation a MAP Dialog implementation for the specific service
	 * 
	 * @param appCntx
	 * @param tcapDialog
	 * @return
	 */
	protected abstract MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog);

	/**
	 * Creating new outgoing TCAP Dialog. Used when creating a new outgoing MAP
	 * Dialog
	 * 
	 * @param origAddress
	 * @param destAddress
	 * @return
	 * @throws MAPException
	 */
	protected Dialog createNewTCAPDialog(SccpAddress origAddress, SccpAddress destAddress) throws MAPException {
		try {
			return this.mapProviderImpl.getTCAPProvider().getNewDialog(origAddress, destAddress);
		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	/**
	 * Adding MAP Dialog into MAPProviderImpl.dialogs Used when creating a new
	 * outgoing MAP Dialog
	 * 
	 * @param dialog
	 */
	protected void PutMADDialogIntoCollection(MAPDialogImpl dialog) {
		this.mapProviderImpl.addDialog((MAPDialogImpl) dialog);
	}

	protected void addMAPServiceListener(MAPServiceListener mapServiceListener) {
		this.serviceListeners.add(mapServiceListener);
	}

	protected void removeMAPServiceListener(MAPServiceListener mapServiceListener) {
		this.serviceListeners.remove(mapServiceListener);
	}

	public Boolean isActivated() {
		return this._isActivated;
	}

	public void acivate() {
		this._isActivated = true;
	}

	public void deactivate() {
		this._isActivated = false;

		// TODO: abort all active dialog ?
	}

	protected void deliverErrorComponent(Long invokeId, MAPErrorMessage mapErrorMessage) {
		for (MAPServiceListener serLis : this.serviceListeners) {
			((MAPServiceSupplementaryListener) serLis).onErrorComponent(invokeId, mapErrorMessage);
		}
	}

	protected void deliverRejectComponent(Long invokeId, Problem problem) {
		for (MAPServiceListener serLis : this.serviceListeners) {
			((MAPServiceSupplementaryListener) serLis).onRejectComponent(invokeId, problem);
		}
	}

	protected void deliverProviderErrorComponent(Long invokeId, MAPProviderError providerError) {
		for (MAPServiceListener serLis : this.serviceListeners) {
			((MAPServiceSupplementaryListener) serLis).onProviderErrorComponent(invokeId, providerError);
		}
	}

	protected void deliverInvokeTimeout(MAPDialog mapDialog, Invoke invoke) {
		for (MAPServiceListener serLis : this.serviceListeners) {
			((MAPServiceSupplementaryListener) serLis).onInvokeTimeout(mapDialog, invoke.getInvokeId());
		}
	}

}
