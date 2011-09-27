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

package org.mobicents.protocols.ss7.map.api.dialog;

/**
 * 7.6.1.3 Provider error This parameter is used to indicate a protocol related
 * type of error: - duplicated invoke Id; - not supported service; - mistyped
 * parameter; - resource limitation; - initiating release, i.e. the peer has
 * already initiated release of the dialogue and the service has to be released;
 * - unexpected response from the peer; - service completion failure; - no
 * response from the peer; - invalid response received.
 * 
 * @author sergey vetyutnev
 * 
 */
public enum MAPProviderError {
	DuplicatedInvokeId(0), NotSupportedService(1), MistypedParameter(2), ResourceLimitation(3), InitiatingRelease(4), UnexpectedResponseFromThePeer(
			5), ServiceCompletionFailure(6), NoResponseFromThePeer(7), InvalidResponseReceived(8);

	private int code;

	private MAPProviderError(int code) {
		this.code = code;
	}
}
