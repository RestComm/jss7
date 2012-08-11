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
package org.mobicents.protocols.ss7.map.api.smstpdu;

/**
 * <p>
 * The TP-Status field indicates the status of a previously submitted SMS-SUBMIT
 * and certain SMS COMMANDS for which a Status -Report has been requested.
 * </p>
 * 
 * @author sergey vetyutnev
 * @author amit bhayani
 * 
 */
public interface Status {

	/**
	 * Short message transaction completed
	 */

	// Short message received by the SME
	public static final int SMS_RECEIVED = 0;

	// Short message forwarded by the SC to the SME but the SC is unable to
	// confirm delivery
	public static final int SMS_UNABLE_TO_CONFIRM_DELIVERY = 1;

	// Short message replaced by the SC
	public static final int SMS_REPLACED_BY_SC = 2;

	/**
	 * Temporary error, SC still trying to transfer SM
	 */

	public static final int CONGESTION_STILL_TRYING = 32;

	public static final int SME_BUSY_STILL_TRYING = 33;

	public static final int NO_REPOSNE_FROM_SME_STILL_TRYING = 34;

	public static final int SERVICE_REJECTED_STILL_TRYING = 35;

	public static final int QUALITY_OF_SERVICE_NOT_AVAILABLE_STILL_TRYING = 36;

	public static final int ERROR_IN_SME_STILL_TRYING = 37;

	/**
	 * Permanent error, SC is not making any more transfer attempts
	 */
	public static final int REMOTE_PROCEDURE_ERROR = 64;

	public static final int INVOMPATIBLE_DESTINATION = 65;

	public static final int CONNECTION_REJECTED_BY_SME = 66;

	public static final int NOT_OBTAINABLE = 67;

	public static final int QOS_NOT_AVAILABLE = 68;

	public static final int NO_INTERWORKING_AVAILABLE = 69;

	public static final int SMS_VALIDITY_PERIOD_EXPIRED = 70;

	public static final int SMS_DELETED_BY_ORIGINATING_SME = 71;

	public static final int SMS_DELETED_BY_ADMINISTRATOR = 72;

	// The SM may have previously existed in the SC but the SC no longer has
	// knowledge of it or the SM may never have previously existed in the SC
	public static final int SMS_DOES_NOT_EXIST = 73;

	/**
	 * Temporary error, SC is not making any more transfer attempts
	 */

	public static final int CONGESTION = 96;

	public static final int SME_BUSY = 97;

	public static final int NO_REPOSNE_FROM_SME = 98;

	public static final int SERVICE_REJECTED = 99;

	public static final int QUALITY_OF_SERVICE_NOT_AVAILABLE = 100;

	public static final int ERROR_IN_SME = 101;

	/**
	 * 
	 * @return one of the status codes as declared above
	 */
	public int getCode();

}
