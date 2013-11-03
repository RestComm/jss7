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

/**
 *
 00000000 Enquiry relating to previously submitted short message 1 00000001 Cancel Status Report Request relating to
 * previously submitted short message 0 00000010 Delete previously submitted Short Message 0 00000011 Enable Status Report
 * Request relating to previously submitted short message 0 00000100..00011111 Reserved unspecified 11100000..11111111 Values
 * specific for each SC 1 or 0
 *
 * @author sergey vetyutnev
 *
 */
public enum CommandTypeValue {

    EnquiryPreviouslySubmittedShortMessage(0), CancelStatusReportRequest(1), DeletePreviouslySubmittedShortMessage(2), EnableStatusReportRequestToPreviouslySubmittedShortMessage(
            3), Reserved(256);

    private int code;

    private CommandTypeValue(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static CommandTypeValue getInstance(int code) {
        switch (code) {
            case 0:
                return EnquiryPreviouslySubmittedShortMessage;
            case 1:
                return CancelStatusReportRequest;
            case 2:
                return DeletePreviouslySubmittedShortMessage;
            case 3:
                return EnableStatusReportRequestToPreviouslySubmittedShortMessage;
            default:
                return Reserved;
        }
    }
}