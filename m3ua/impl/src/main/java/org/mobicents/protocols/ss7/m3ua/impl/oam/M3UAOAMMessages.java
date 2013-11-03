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

package org.mobicents.protocols.ss7.m3ua.impl.oam;

/**
 *
 * @author amit bhayani
 *
 */
public interface M3UAOAMMessages {

    /**
     * Pre defined messages
     */
    String INVALID_COMMAND = "Invalid Command";

    String ADD_ASP_TO_AS_SUCESSFULL = "Successfully added ASP name=%s to AS name=%s";

    String REMOVE_ASP_FROM_AS_SUCESSFULL = "Successfully removed ASP name=%s from AS name=%s";

    String NO_AS_FOUND = "No AS found for given name %s";

    String ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_THIS_AS = "Cannot assign ASP=%s to AS=%s. This ASP is already assigned to this AS";

    String ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS = "Cannot assign ASP=%s to AS=%s. This ASP is already assigned to other AS.";

    String ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS_WITH_NULL_RC = "Cannot assign ASP=%s to AS=%s. This ASP is already assigned to other AS which has null RoutingContext.";

    String ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS_TYPE = "Cannot assign ASP=%s to AS=%s. This ASP is already assigned to other AS of type=%s";

    String ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_IPSP_TYPE = "Cannot assign ASP=%s to AS=%s. This ASP is already assigned to other AS of which has IPSP type=%s";

    String ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS_EXCHANGETYPE = "Cannot assign ASP=%s to AS=%s. This ASP is already assigned to other AS of ExchangeType=%s";

    String ASP_NOT_ASSIGNED_TO_AS = "ASP name=%s not assigned to any AS yet";

    String NO_ASP_FOUND = "No ASP found for given name %s";

    String ASP_ALREADY_STOPPED = "ASP name=%s already stopped";

    String ASP_ALREADY_STARTED = "ASP name=%s already started";

    String ASP_START_SUCESSFULL = "Successfully started ASP name=%s";

    String ASP_STOP_SUCESSFULL = "Successfully stopped ASP name=%s";

    String CREATE_AS_SUCESSFULL = "Successfully created AS name=%s";

    String DESTROY_AS_SUCESSFULL = "Successfully destroyed AS name=%s";

    String DESTROY_AS_FAILED_ASP_ASSIGNED = "As=%s still has ASP's assigned. Unassign Asp's before destroying this As";

    String CREATE_AS_FAIL_NAME_EXIST = "Creation of AS failed. Other AS with name=%s already exist";

    String CREATE_ASP_SUCESSFULL = "Successfully created ASP name=%s";

    String DESTROY_ASP_SUCESSFULL = "Successfully destroyed ASP name=%s";

    String CREATE_ASP_FAIL_NAME_EXIST = "Creation of ASP failed. Other ASP with name=%s already exist";

    String CREATE_ASP_FAIL_IPPORT_EXIST = "Creation of ASP failed. Other ASP with ip=%s port=%d already exist";

    String ROUTE_AS_FOR_DPC_EXIST = "AS=%s already routes for DPC=%d";

    String ADD_ROUTE_AS_FOR_DPC_SUCCESSFULL = "AS=%s successfully added as route for DPC=%d";

    String NO_ROUTE_DEFINED_FOR_DPC = "No route defined for DPC=%d";

    String NO_AS_ROUTE_FOR_DPC = "AS=%s doesn't routes for DPC=%d";

    String REMOVE_AS_ROUTE_FOR_DPC_SUCCESSFULL = "Successfully removed AS=%s as route for DPC=%d";

    String CMD_NOTSUPPORTED_M3UAMANAGEMENT_IS_SERVER = "The M3UAManagement is Server side and doesnt support command %s";

    String NOT_SUPPORTED_YET = "Not supported yet";

    String NO_ASP_DEFINED_YET = "No ASP defined yet";

    String NO_AS_DEFINED_YET = "No AS defined yet";

    String NO_ROUTE_DEFINED_YET = "No Route defined yet";

    String AS_USED_IN_ROUTE_ERROR = "As=%s used in route=%s. Remove from route";

    String NO_ASSOCIATION_FOUND = "No Association found for name=%s";

    String ASSOCIATION_IS_STARTED = "Association=%s is started";

    String ASSOCIATION_IS_ASSOCIATED = "Association=%s is already associated";

    String ASP_ID_TAKEN = "ASP Identifier=%d is already taken";

    /**
     * Generic constants
     */
    String TAB = "        ";

    String NEW_LINE = "\n";

    String COMMA = ",";

    /**
     * Show command specific contsnats
     */
    String SHOW_ASSIGNED_TO = "Assigned to :\n";

    String SHOW_ASP_NAME = "ASP name=";

    String SHOW_AS_NAME = "AS name=";

    String SHOW_SCTP_ASSOC = " sctpAssoc=";

    String SHOW_ASPID = " aspid=";

    String SHOW_HEARTBEAT_ENABLED = " heartbeat=";

    String SHOW_STARTED = " started=";

    String SHOW_FUNCTIONALITY = " functionality=";

    String SHOW_MODE = " mode=";

    String SHOW_IPSP_TYPE = " ipspType=";

    String SHOW_LOCAL_FSM_STATE = " localFSMState=";

    String SHOW_PEER_FSM_STATE = " peerFSMState=";

}
