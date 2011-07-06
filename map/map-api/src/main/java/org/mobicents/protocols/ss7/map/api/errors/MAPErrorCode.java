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

package org.mobicents.protocols.ss7.map.api.errors;

/**
 * MAP Error codes 
 * Carried by ReturnError primitive 
 * 
 * @author sergey vetyutnev
 * 
 */
public interface MAPErrorCode {

	// -- values band 
	public static final int minimalCodeValue = 1;
	public static final int maximumCodeValue = 72;

	// -- generic error codes 
	public static final int systemFailure = 34;
	public static final int dataMissing = 35;
	public static final int unexpectedDataValue = 36;
	public static final int facilityNotSupported = 21;
	
	// -- call handling error codes
	public static final int noRoamingNumberAvailable = 39;
	public static final int absentSubscriber = 27;
	public static final int busySubscriber = 45;
	public static final int noSubscriberReply = 46;
	public static final int callBarred = 13;
	public static final int forwardingFailed = 47;
	public static final int orNotAllowed = 48;
	public static final int forwardingViolation = 14;
	public static final int cugReject = 15;
	
	// -- subscription error codes
	public static final int roamingNotAllowed = 8;
	public static final int illegalSubscriber = 9;
	public static final int illegalEquipment = 12;
	public static final int bearerServiceNotProvisioned = 10;
	public static final int teleserviceNotProvisioned = 11;
	
	// -- short message service errors
	public static final int subscriberBusyForMTSMS = 31;
	public static final int smDeliveryFailure = 32;
	public static final int messageWaitingListFull = 33;
	public static final int absentSubscriberSM = 6;
}

