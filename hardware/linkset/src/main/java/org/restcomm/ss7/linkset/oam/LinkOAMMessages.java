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

package org.restcomm.ss7.linkset.oam;

/**
 * Declares static messages used by links and linksets
 *
 * @author amit bhayani
 *
 */
public interface LinkOAMMessages {

    /**
     * Pre defined messages
     */
    String INVALID_COMMAND = "Invalid Command";

    String LINKSET_ALREADY_EXIST = "LinkSet already exist";

    String LINKSET_DOESNT_EXIST = "LinkSet doesn't exist";

    String ACTIVATE_LINKSET_SUCCESSFULLY = "LinkSet activated  successfully";

    String ACTIVATE_LINKSET_FAILED = "LinkSet activation failed";

    String ACTIVATE_LINK_SUCCESSFULLY = "Link activated successfully";

    String ACTIVATE_LINK_FAILED = "Link activation failed";

    String LINK_DOESNT_EXIST = "Link doesn't exist";

    String LINKSET_SUCCESSFULLY_ADDED = "LinkSet successfully added";

    String LINKSET_SUCCESSFULLY_REMOVED = "LinkSet successfully removed";

    String INCORRECT_LINKSET_TYPE = "LinkSet type is incorrect";

    String LINKSET_ALREADY_ACTIVE = "Linkset already active";

    String LINKSET_NO_LINKS_CONFIGURED = "No Links for this LinkSet are configured";

    String LINK_SUCCESSFULLY_ADDED = "Link successfully added";

    String LINK_SUCCESSFULLY_REMOVED = "Link successfully removed";

    String CANT_DELETE_LINKSET = "Linkset is Available. Can't delete. Please deactivate all links within Linkset and remove each of them before removing this linkset";

    String LINK_ADD_FAILED = "Addition of Link failed";

    String LINK_ALREADY_EXIST = "Link already exist";

    String LINKSET_NOT_DAHDI = "Linkset is of not Dahdi type";

    String LINKSET_NOT_DIALOGI = "Linkset is of not Dialogic type";

    String LINKSET_NOT_M3UA = "Linkset is of not M3UA type";

    String NOT_IMPLEMENTED = "Not implemented yet";

    String CANT_DELETE_LINK = "Link is Active. Can't delete. Please Shutdown first";

    String LINK_ALREADY_ACTIVE = "Link already active";

    String LINK_ALREADY_DEACTIVE = "Link already deactive";

    String LINK_NOT_CONFIGURED = "Not all mandatory parameters are set";

    String OPERATION_NOT_SUPPORTED = "Operation not supported";

    String SERVER_ERROR = "Server Error";

}
