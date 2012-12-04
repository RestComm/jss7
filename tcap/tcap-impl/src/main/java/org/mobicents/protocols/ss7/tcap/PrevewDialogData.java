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

package org.mobicents.protocols.ss7.tcap;

import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;

/**
 *
 * @author sergey vetyutnev
 * 
 */
public class PrevewDialogData {
	private ApplicationContextName lastACN;
	private InvokeImpl[] operationsSent;

	private Object upperDialog;

	protected TCAPProviderImpl.PrevewDialogDataKey prevewDialogDataKey1;
	protected TCAPProviderImpl.PrevewDialogDataKey prevewDialogDataKey2;

	public ApplicationContextName getLastACN() {
		return lastACN;
	}

	public InvokeImpl[] getOperationsSent() {
		return operationsSent;
	}

	public Object getUpperDialog() {
		return upperDialog;
	}

	public void setLastACN(ApplicationContextName val) {
		lastACN = val;
	}

	public void setOperationsSent(InvokeImpl[] val) {
		operationsSent = val;
	}

	public void setUpperDialog(Object val) {
		upperDialog = val;
	}
}
