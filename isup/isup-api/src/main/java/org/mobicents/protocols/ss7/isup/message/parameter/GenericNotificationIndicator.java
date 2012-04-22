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

/**
 * Start time:13:04:54 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:04:54 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface GenericNotificationIndicator extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x2C;
	
	/**
	 * See Q.763 3.25 Notification indicator : user suspended
	 */
	public static final int _NI_USER_SUSPENDED = 0;

	/**
	 * See Q.763 3.25 Notification indicator : user resumed
	 */
	public static final int _NI_USER_RESUMED = 1;

	/**
	 * See Q.763 3.25 Notification indicator : bearer service change
	 */
	public static final int _NI_BEARER_SERVICE_CHANGE = 2;
	/**
	 * See Q.763 3.25 Notification indicator : discriminator for extension to
	 * ASN.1
	 */
	public static final int _NI_DISCRIMINATOR_FOR_EXTENSION_TO_ASN1 = 3;

	/**
	 * See Q.763 3.25 Notification indicator : conference established
	 */
	public static final int _NI_CONFERENCE_ESTABILISHED = 0x42;

	/**
	 * See Q.763 3.25 Notification indicator : conference disconnected
	 */
	public static final int _NI_CONFERENCE_DISCONNECTED = 0x43;

	/**
	 * See Q.763 3.25 Notification indicator : other party added
	 */
	public static final int _NI_OTHER_PARTY_ADDED = 0x44;
	/**
	 * See Q.763 3.25 Notification indicator : isolated
	 */
	public static final int _NI_ISOLATED = 0x45;
	/**
	 * See Q.763 3.25 Notification indicator : reattached
	 */
	public static final int _NI_REATTACHED = 0x46;

	/**
	 * See Q.763 3.25 Notification indicator : other party isolated
	 */
	public static final int _NI_OTHER_PARTY_ISOLATED = 0x47;

	/**
	 * See Q.763 3.25 Notification indicator : other party reattached
	 */
	public static final int _NI_OTHER_PARTY_REATTACHED = 0x48;
	/**
	 * See Q.763 3.25 Notification indicator : other party split
	 */
	public static final int _NI_OTHER_PARTY_SPLIT = 0x49;
	/**
	 * See Q.763 3.25 Notification indicator : other party disconnected
	 */
	public static final int _NI_OTHER_PARTY_DISCONNECTED = 0x4A;

	/**
	 * See Q.763 3.25 Notification indicator : conference floating
	 */
	public static final int _NI_CONFERENCE_FLOATING = 0x4B;

	/**
	 * See Q.763 3.25 Notification indicator : call is a waiting call
	 */
	public static final int _NI_CALL_IS_AWAITING = 0xC0;

	/**
	 * See Q.763 3.25 Notification indicator : diversion activated (used in
	 * DSS1)
	 */
	public static final int _NI_DIVERSION_ACTIVATED = 0x68;

	/**
	 * See Q.763 3.25 Notification indicator : call transfer, alerting
	 */
	public static final int _NI_CALL_TRANSFER_ALERTING = 0x69;

	/**
	 * See Q.763 3.25 Notification indicator : call transfer, active
	 */
	public static final int _NI_CALL_TRANSFER_ACTIVE = 0x6A;

	/**
	 * See Q.763 3.25 Notification indicator : remote hold
	 */
	public static final int _NI_REMOTE_HOLD = 0x79;

	/**
	 * See Q.763 3.25 Notification indicator : remote retrieval
	 */
	public static final int _NI_REMOTE_RETRIEVAL = 0x8A;

	/**
	 * See Q.763 3.25 Notification indicator : call is diverting
	 */
	public static final int _NI_RCID = 0x8B;
	
	public int[] getNotificationIndicator();

	public void setNotificationIndicator(int[] notificationIndicator) throws IllegalArgumentException;
}
