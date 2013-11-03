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

package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:11:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author sergey vetyutnev
 */
public interface CauseIndicators extends ISUPParameter {
    int _PARAMETER_CODE = 0x12;

    /**
     * See Q.850
     */
    int _CODING_STANDARD_ITUT = 0;

    /**
     * See Q.850
     */
    int _CODING_STANDARD_IOS_IEC = 1;

    /**
     * See Q.850
     */
    int _CODING_STANDARD_NATIONAL = 2;
    /**
     * See Q.850
     */
    int _CODING_STANDARD_SPECIFIC = 3;

    /**
     * See Q.850
     */
    int _LOCATION_USER = 0;

    /**
     * See Q.850 private network serving the local user (LPN)
     */
    int _LOCATION_PRIVATE_NSLU = 1;

    /**
     * See Q.850 public network serving the local user (LN)
     */
    int _LOCATION_PUBLIC_NSLU = 2;

    /**
     * See Q.850 transit network (TN)
     */
    int _LOCATION_TRANSIT_NETWORK = 3;

    /**
     * See Q.850 private network serving the remote user (RPN)
     */
    int _LOCATION_PRIVATE_NSRU = 5;

    /**
     * See Q.850 public network serving the remote user (RLN)
     */
    int _LOCATION_PUBLIC_NSRU = 4;
    /**
     * See Q.850
     */
    int _LOCATION_INTERNATIONAL_NETWORK = 7;

    /**
     * See Q.850 network beyond interworking point (BI)
     */
    int _LOCATION_NETWORK_BEYOND_IP = 10;

    // cause values
    int _CV_UNALLOCATED = 1;

    int _CV_NO_ROUTE_TO_TRANSIT_NET = 2;

    int _CV_NO_ROUTE_TO_DEST = 3;

    int _CV_SEND_SPECIAL_TONE = 4;

    int _CV_MISDIALED_TRUNK_PREFIX = 5;

    int _CV_ALL_CLEAR = 16;

    int _CV_USER_BUSY = 17;

    int _CV_NO_USER_RESPONSE = 18;

    int _CV_NO_ANSWER = 19;

    int _CV_SUBSCRIBER_ABSENT = 20;

    int _CV_CALL_REJECTED = 21;

    int _CV_NUMBER_CHANGED = 22;

    int _CV_REJECTED_DUE_TO_ACR_SUPP_SERVICES = 24;

    int _CV_EXCHANGE_ROUTING_ERROR = 25;

    int _CV_DESTINATION_OUT_OF_ORDER = 27;

    int _CV_ADDRESS_INCOMPLETE = 28;

    int _CV_FACILITY_REJECTED = 29;

    int _CV_NORMAL_UNSPECIFIED = 31;

    int _CV_NO_CIRCUIT_AVAILABLE = 34;

    int _CV_NETWORK_OUT_OF_ORDER = 38;

    int _CV_TEMPORARY_FAILURE = 41;

    int _CV_SWITCH_EQUIPMENT_CONGESTION = 42;

    int _CV_USER_INFORMATION_DISCARDED = 43;

    int _CV_REQUESTED_CIRCUIT_UNAVAILABLE = 44;

    int _CV_PREEMPTION = 47;

    int _CV_RESOURCE_UNAVAILABLE = 47;

    int _CV_FACILITY_NOT_SUBSCRIBED = 50;

    int _CV_INCOMING_CALL_BARRED_WITHIN_CUG = 55;

    int _CV_BEARER_CAPABILITY_NOT_AUTHORIZED = 57;

    int _CV_BEARER_CAPABILITY_NOT_AVAILABLE = 58;

    int _CV_SERVICE_OR_OPTION_NOT_AVAILABLE = 63;

    int _CV_BEARER_CAPABILITY_NOT_IMPLEMENTED = 65;

    int _CV_FACILITY_NOT_IMPLEMENTED = 69;

    int _CV_RESTRICTED_DIGITAL_BEARED_AVAILABLE = 70;

    int _CV_SERVICE_OR_OPTION_NOT_IMPLEMENTED = 79;

    int _CV_INVALID_CALL_REFERENCE = 81;

    int _CV_CALLED_USER_NOT_MEMBER_OF_CUG = 87;

    int _CV_INCOMPATIBLE_DESTINATION = 88;

    int _CV_INVALID_TRANSIT_NETWORK_SELECTION = 91;

    int _CV_INVALID_MESSAGE_UNSPECIFIED = 95;

    int _CV_MANDATORY_ELEMENT_MISSING = 96;

    int _CV_MESSAGE_TYPE_NON_EXISTENT = 97;

    int _CV_PARAMETER_NON_EXISTENT_DISCARD = 99;

    int _CV_INVALID_PARAMETER_CONTENT = 100;

    int _CV_TIMEOUT_RECOVERY = 102;

    int _CV_PARAMETER_NON_EXISTENT_PASS_ALONG = 103;

    int _CV_MESSAGE_WITH_UNRECOGNIZED_PARAMETER_DISCARDED = 110;

    int _CV_PROTOCOL_ERROR = 111;

    int _CV_INTERNETWORKING_UNSPECIFIED = 127;

    int getCodingStandard();

    void setCodingStandard(int codingStandard);

    int getLocation();

    void setLocation(int location);

    int getRecommendation();

    void setRecommendation(int recommendation);

    int getCauseValue();

    void setCauseValue(int causeValue);

    byte[] getDiagnostics();

    void setDiagnostics(byte[] diagnostics);

}
