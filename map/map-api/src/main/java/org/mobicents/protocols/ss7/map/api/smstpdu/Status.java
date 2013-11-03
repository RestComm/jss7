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
package org.mobicents.protocols.ss7.map.api.smstpdu;

import java.io.Serializable;

/**
 * <p>
 * The TP-Status field indicates the status of a previously submitted SMS-SUBMIT and certain SMS COMMANDS for which a Status
 * -Report has been requested.
 * </p>
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 *
 */
public interface Status extends Serializable {

    /**
     * Short message transaction completed
     */

    // Short message received by the SME
    int SMS_RECEIVED = 0;

    // Short message forwarded by the SC to the SME but the SC is unable to
    // confirm delivery
    int SMS_UNABLE_TO_CONFIRM_DELIVERY = 1;

    // Short message replaced by the SC
    int SMS_REPLACED_BY_SC = 2;

    /**
     * Temporary error, SC still trying to transfer SM
     */

    int CONGESTION_STILL_TRYING = 32;

    int SME_BUSY_STILL_TRYING = 33;

    int NO_REPOSNE_FROM_SME_STILL_TRYING = 34;

    int SERVICE_REJECTED_STILL_TRYING = 35;

    int QUALITY_OF_SERVICE_NOT_AVAILABLE_STILL_TRYING = 36;

    int ERROR_IN_SME_STILL_TRYING = 37;

    /**
     * Permanent error, SC is not making any more transfer attempts
     */
    int REMOTE_PROCEDURE_ERROR = 64;

    int INVOMPATIBLE_DESTINATION = 65;

    int CONNECTION_REJECTED_BY_SME = 66;

    int NOT_OBTAINABLE = 67;

    int QOS_NOT_AVAILABLE = 68;

    int NO_INTERWORKING_AVAILABLE = 69;

    int SMS_VALIDITY_PERIOD_EXPIRED = 70;

    int SMS_DELETED_BY_ORIGINATING_SME = 71;

    int SMS_DELETED_BY_ADMINISTRATOR = 72;

    // The SM may have previously existed in the SC but the SC no longer has
    // knowledge of it or the SM may never have previously existed in the SC
    int SMS_DOES_NOT_EXIST = 73;

    /**
     * Temporary error, SC is not making any more transfer attempts
     */

    int CONGESTION = 96;

    int SME_BUSY = 97;

    int NO_REPOSNE_FROM_SME = 98;

    int SERVICE_REJECTED = 99;

    int QUALITY_OF_SERVICE_NOT_AVAILABLE = 100;

    int ERROR_IN_SME = 101;

    /**
     *
     * @return one of the status codes as declared above
     */
    int getCode();

}
