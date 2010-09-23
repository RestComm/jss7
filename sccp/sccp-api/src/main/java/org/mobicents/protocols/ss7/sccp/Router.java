/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.protocols.ss7.sccp;

import java.io.Serializable;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * Represents the SCCP router.
 * 
 * @author kulikov
 */
public interface Router extends Serializable {
    /**
     * Determines the route for the message using called and calling address.
     * 
     * @param calledPartyAddress
     * @param callingPartyAddress
     * @return the point code of linkset which will be used for routing
     */
    public int getRoute(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress);
}
