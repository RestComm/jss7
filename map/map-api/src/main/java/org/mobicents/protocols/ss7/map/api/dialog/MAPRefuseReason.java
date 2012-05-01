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
 * This parameter is present only if the Result parameter indicates that the dialogue is refused. 
 * It takes one of the following values:
 * -	Application-context-not-supported;
 * -	Invalid-destination-reference;
 * -	Invalid-originating-reference;
 * -	No-reason-given;
 * -	Remote node not reachable;
 * -	Potential version incompatibility.
 *
 * @author sergey vetyutnev
 *
 */
public enum MAPRefuseReason {
	ApplicationContextNotSupported(0), 
	InvalidDestinationReference(1), 
	InvalidOriginatingReference(2),
	NoReasonGiven(3),
	RemoteNodeNotReachable(4),
	PotentialVersionIncompatibility(5);

	private int code;

	private MAPRefuseReason(int code) {
		this.code = code;
	}
}
