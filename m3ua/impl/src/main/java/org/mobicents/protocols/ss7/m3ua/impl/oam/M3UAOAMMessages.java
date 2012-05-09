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
	public static final String INVALID_COMMAND = "Invalid Command";

	public static final String ADD_ASP_TO_AS_SUCESSFULL = "Successfully added ASP name=%s to AS name=%s";
	
	public static final String REMOVE_ASP_FROM_AS_SUCESSFULL = "Successfully removed ASP name=%s from AS name=%s";

	public static final String NO_AS_FOUND = "No AS found for given name %s";
	
	public static final String ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_THIS_AS = "Cannot assign ASP=%s to AS=%s. This ASP is already assigned to this AS";
	
	public static final String ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS = "Cannot assign ASP=% to AS=%. This ASP is already assigned to other AS.";
	
	public static final String ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS_WITH_NULL_RC = "Cannot assign ASP=% to AS=%. This ASP is already assigned to other AS which has null RoutingContext.";
	
	public static final String ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS_TYPE = "Cannot assign ASP=% to AS=%. This ASP is already assigned to other AS of type=%s";
	
	public static final String ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_IPSP_TYPE = "Cannot assign ASP=% to AS=%. This ASP is already assigned to other AS of which has IPSP type=%s";
	
	public static final String ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_OTHER_AS_EXCHANGETYPE = "Cannot assign ASP=% to AS=%. This ASP is already assigned to other AS of ExchangeType=%s";
	
	public static final String ASP_NOT_ASSIGNED_TO_AS = "ASP name=%s not assigned to any AS yet";

	public static final String NO_ASP_FOUND = "No ASP found for given name %s";
	
	public static final String ASP_ALREADY_STOPPED = "ASP name=%s already stopped";
	
	public static final String ASP_ALREADY_STARTED = "ASP name=%s already started";

	public static final String ASP_START_SUCESSFULL = "Successfully started ASP name=%s";

	public static final String ASP_STOP_SUCESSFULL = "Successfully stopped ASP name=%s";

	public static final String CREATE_AS_SUCESSFULL = "Successfully created AS name=%s";
	
	public static final String DESTROY_AS_SUCESSFULL = "Successfully destroyed AS name=%s";
	
	public static final String DESTROY_AS_FAILED_ASP_ASSIGNED = "As=%s still has ASP's assigned. Unassign Asp's before destroying this As";

	public static final String CREATE_AS_FAIL_NAME_EXIST = "Creation of AS failed. Other AS with name=%s already exist";

	public static final String CREATE_ASP_SUCESSFULL = "Successfully created ASP name=%s";
	
	public static final String DESTROY_ASP_SUCESSFULL = "Successfully destroyed ASP name=%s";

	public static final String CREATE_ASP_FAIL_NAME_EXIST = "Creation of ASP failed. Other ASP with name=%s already exist";

	public static final String CREATE_ASP_FAIL_IPPORT_EXIST = "Creation of ASP failed. Other ASP with ip=%s port=%d already exist";

	public static final String ROUTE_AS_FOR_DPC_EXIST = "AS=%s already routes for DPC=%d";

	public static final String ADD_ROUTE_AS_FOR_DPC_SUCCESSFULL = "AS=%s successfully added as route for DPC=%d";

	public static final String NO_ROUTE_DEFINED_FOR_DPC = "No route defined for DPC=%d";

	public static final String NO_AS_ROUTE_FOR_DPC = "AS=%s doesn't routes for DPC=%d";

	public static final String REMOVE_AS_ROUTE_FOR_DPC_SUCCESSFULL = "Successfully removed AS=%s as route for DPC=%d";

	public static final String CMD_NOTSUPPORTED_M3UAMANAGEMENT_IS_SERVER = "The M3UAManagement is Server side and doesnt support command %s";
	
	public static final String NOT_SUPPORTED_YET = "Not supported yet";
	
	public static final String NO_ASP_DEFINED_YET = "No ASP defined yet";
	
	public static final String NO_AS_DEFINED_YET = "No AS defined yet";
	
	public static final String NO_ROUTE_DEFINED_YET = "No Route defined yet";
	
	public static final String AS_USED_IN_ROUTE_ERROR = "As=%s used in route=%s. Remove from route";
	
	/**
	 * Generic constants
	 */
	public static final String TAB = "        ";
	
	public static final String NEW_LINE = "\n";
	
	public static final String COMMA = ",";
	
	/**
	 * Show command specific contsnats
	 */
	public static final String SHOW_ASSIGNED_TO = "Assigned to :\n";
	
	public static final String SHOW_ASP_NAME = "ASP name=";
	
	public static final String SHOW_AS_NAME = "AS name=";
	
	public static final String SHOW_SCTP_ASSOC = " sctpAssoc=";
	
	public static final String SHOW_STARTED = " started=";
	
	public static final String SHOW_FUNCTIONALITY = " functionality=";
	
	public static final String SHOW_MODE = " mode=";
	
	public static final String SHOW_IPSP_TYPE = " ipspType=";
	
	public static final String SHOW_LOCAL_FSM_STATE = " localFSMState=";
	
	public static final String SHOW_PEER_FSM_STATE = " peerFSMState=";

}
