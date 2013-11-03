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

package org.mobicents.protocols.ss7.sccp.impl.oam;

/**
 * Declares static messages used by RouterImpl
 *
 * @author amit bhayani
 *
 */
public interface SccpOAMMessage {

    String INVALID_COMMAND = "Invalid Command";

    String RULE_ALREADY_EXIST = "Rule already exist";

    String INVALID_MASK = "Invalid Mask";

    String RULE_DOESNT_EXIST = "Rule doesn't exist";

    String RULE_SUCCESSFULLY_ADDED = "Rule successfully added";

    String RULE_SUCCESSFULLY_MODIFIED = "Rule successfully modified";

    String RULE_SUCCESSFULLY_REMOVED = "Rule successfully removed";

    String ADDRESS_ALREADY_EXIST = "Address already exist";

    String ADDRESS_DOESNT_EXIST = "Address doesn't exist";

    String ADDRESS_SUCCESSFULLY_ADDED = "Address successfully added";

    String ADDRESS_SUCCESSFULLY_MODIFIED = "Address successfully modified";

    String ADDRESS_SUCCESSFULLY_DELETED = "Address successfully deleted";

    String SERVER_ERROR = "Server Error";

    String NO_PRIMARY_ADDRESS = "No primary address defined for id=%d";

    String NO_BACKUP_ADDRESS = "No backup address defined for id=%d";

    String RSPC_ALREADY_EXIST = "Remote Signaling Pointcode already exists";

    String RSPC_DOESNT_EXIST = "Remote Signaling Pointcode doesn't exist";

    String RSPC_SUCCESSFULLY_ADDED = "Remote Signaling Pointcode successfully added";

    String RSPC_SUCCESSFULLY_MODIFIED = "Remote Signaling Pointcode successfully modified";

    String RSPC_SUCCESSFULLY_DELETED = "Remote Signaling Pointcode successfully deleted";

    String RSS_ALREADY_EXIST = "Remote Subsystem already exists";

    String RSS_DOESNT_EXIST = "Remote Subsystem doesn't exist";

    String RSS_SUCCESSFULLY_ADDED = "Remote Subsystem successfully added";

    String RSS_SUCCESSFULLY_MODIFIED = "Remote Subsystem successfully modified";

    String RSS_SUCCESSFULLY_DELETED = "Remote Subsystem successfully deleted";

    String LMR_ALREADY_EXIST = "Long message rule already exists";

    String LMR_DOESNT_EXIST = "Long message rule doesn't exist";

    String LMR_SUCCESSFULLY_ADDED = "Long message rule successfully added";

    String LMR_SUCCESSFULLY_MODIFIED = "Long message rule successfully modified";

    String LMR_SUCCESSFULLY_DELETED = "Long message rule successfully deleted";

    String SAP_ALREADY_EXIST = "Service access point already exists";

    String SAP_DOESNT_EXIST = "Service access point doesn't exist";

    String SAP_SUCCESSFULLY_ADDED = "Service access point successfully added";

    String SAP_SUCCESSFULLY_MODIFIED = "Service access point successfully modified";

    String SAP_SUCCESSFULLY_DELETED = "Service access point successfully deleted";

    String MUP_DOESNT_EXIST = "Mtp3UserPart doesn't exist";

    String DEST_ALREADY_EXIST = "Destination definition already exists";

    String DEST_DOESNT_EXIST = "Destination definition doesn't exist";

    String DEST_SUCCESSFULLY_ADDED = "Destination definition successfully added";

    String DEST_SUCCESSFULLY_MODIFIED = "Destination definition successfully modified";

    String DEST_SUCCESSFULLY_DELETED = "Destination definition successfully deleted";

    String CS_ALREADY_EXIST = "Concerned spc already exists";

    String CS_DOESNT_EXIST = "Concerned spc doesn't exist";

    String CS_SUCCESSFULLY_ADDED = "Concerned spc successfully added";

    String CS_SUCCESSFULLY_MODIFIED = "Concerned spc successfully modified";

    String CS_SUCCESSFULLY_DELETED = "Concerned spc successfully deleted";

    String PARAMETER_SUCCESSFULLY_SET = "Parameter has been successfully set";

    String RULETYPE_NOT_SOLI_SEC_ADD_MANDATORY = "If RuleType is not Solitary, specifying Secondar Address is mandatory";

    String SEC_MISMATCH_PATTERN = "Number of sections in mask doesn't match with number of sections in pattern GlobalTitle digits";

    String SEC_MISMATCH_PRIMADDRESS = "Number of sections in mask doesn't match with number of sections in primary address GlobalTitle digits";

    String SEC_MISMATCH_SECADDRESS = "Number of sections in mask doesn't match with number of sections in secondary address GlobalTitle digits";

}
