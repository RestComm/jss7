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

package org.mobicents.protocols.ss7.sccp.impl.router;

import java.io.Serializable;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * Presents the routing information.
 * 
 * @author kulikov
 */
public class Route  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1305958638683555285L;
	/** translated address */
    protected SccpAddress address;
    /** mtp routing info */
    protected MTPInfo mtpInfo;

    /**
     * Gets the translated address.
     * 
     * @return SCCP address.
     */
    public SccpAddress getAddress() {
        return address;
    }
    
    /**
     * MTP information.
     * 
     * @return MTP info object.
     */
    public MTPInfo getMTPInfo() {
        return mtpInfo;
    }
}
