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

package org.mobicents.protocols.ss7.sccp.impl.oam;

/**
 * Declares static messages used by RouterImpl
 * 
 * @author amit bhayani
 * 
 */
public interface SccpOAMMessage {

	public static final String INVALID_COMMAND = "Invalid Command";

	public static final String RULE_ALREADY_EXIST = "Rule already exist";

	public static final String INVALID_MASK = "Invalid Mask";

	public static final String RULE_DOESNT_EXIST = "Rule doesn't exist";

	public static final String RULE_SUCCESSFULLY_ADDED = "Rule successfully added";

	public static final String RULE_SUCCESSFULLY_MODIFIED = "Rule successfully modified";

	public static final String RULE_SUCCESSFULLY_REMOVED = "Rule successfully removed";

	public static final String ADDRESS_ALREADY_EXIST = "Address already exist";

	public static final String ADDRESS_DOESNT_EXIST = "Address doesn't exist";

	public static final String ADDRESS_SUCCESSFULLY_ADDED = "Address successfully added";

	public static final String ADDRESS_SUCCESSFULLY_MODIFIED = "Address successfully modified";

	public static final String ADDRESS_SUCCESSFULLY_DELETED = "Address successfully deleted";

	public static final String SERVER_ERROR = "Server Error";

	public static final String NO_PRIMARY_ADDRESS = "No primary address defined for id=%d";

	public static final String NO_BACKUP_ADDRESS = "No backup address defined for id=%d";

	public static final String RSPC_ALREADY_EXIST = "Remote Signaling Pointcode already exists";

	public static final String RSPC_DOESNT_EXIST = "Remote Signaling Pointcode doesn't exist";

	public static final String RSPC_SUCCESSFULLY_ADDED = "Remote Signaling Pointcode successfully added";

	public static final String RSPC_SUCCESSFULLY_MODIFIED = "Remote Signaling Pointcode successfully modified";

	public static final String RSPC_SUCCESSFULLY_DELETED = "Remote Signaling Pointcode successfully deleted";

	public static final String RSS_ALREADY_EXIST = "Remote Subsystem already exists";

	public static final String RSS_DOESNT_EXIST = "Remote Subsystem doesn't exist";

	public static final String RSS_SUCCESSFULLY_ADDED = "Remote Subsystem successfully added";

	public static final String RSS_SUCCESSFULLY_MODIFIED = "Remote Subsystem successfully modified";

	public static final String RSS_SUCCESSFULLY_DELETED = "Remote Subsystem successfully deleted";

	public static final String LMR_ALREADY_EXIST = "Long message rule already exists";

	public static final String LMR_DOESNT_EXIST = "Long message rule doesn't exist";

	public static final String LMR_SUCCESSFULLY_ADDED = "Long message rule successfully added";

	public static final String LMR_SUCCESSFULLY_MODIFIED = "Long message rule successfully modified";

	public static final String LMR_SUCCESSFULLY_DELETED = "Long message rule successfully deleted";

	public static final String SAP_ALREADY_EXIST = "Service access point already exists";

	public static final String SAP_DOESNT_EXIST = "Service access point doesn't exist";

	public static final String SAP_SUCCESSFULLY_ADDED = "Service access point successfully added";

	public static final String SAP_SUCCESSFULLY_MODIFIED = "Service access point successfully modified";

	public static final String SAP_SUCCESSFULLY_DELETED = "Service access point successfully deleted";

	public static final String MUP_DOESNT_EXIST = "Mtp3UserPart doesn't exist";

	public static final String DEST_ALREADY_EXIST = "Destination definition already exists";

	public static final String DEST_DOESNT_EXIST = "Destination definition doesn't exist";

	public static final String DEST_SUCCESSFULLY_ADDED = "Destination definition successfully added";

	public static final String DEST_SUCCESSFULLY_MODIFIED = "Destination definition successfully modified";

	public static final String DEST_SUCCESSFULLY_DELETED = "Destination definition successfully deleted";

	public static final String CS_ALREADY_EXIST = "Concerned spc already exists";

	public static final String CS_DOESNT_EXIST = "Concerned spc doesn't exist";

	public static final String CS_SUCCESSFULLY_ADDED = "Concerned spc successfully added";

	public static final String CS_SUCCESSFULLY_MODIFIED = "Concerned spc successfully modified";

	public static final String CS_SUCCESSFULLY_DELETED = "Concerned spc successfully deleted";

	public static final String PARAMETER_SUCCESSFULLY_SET = "Parameter has been successfully set";
	
	public static final String RULETYPE_NOT_SOLI_SEC_ADD_MANDATORY= "If RuleType is not Solitary, specifying Secondar Address is mandatory";

}
