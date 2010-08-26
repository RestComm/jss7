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

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * This class responsible for selecting adjasent point code.
 * 
 * The link with selected point code will be used for trasmission.
 * 
 * @author kulikov
 */
public class Selector {
    /**
     * Selects the point code of the link which will be used for routing
     * 
     * @param opc the original point code
     * @param cgn calling party number
     * @param cdn called party number
     * @return the selected point code or -1 if message must be processed localy
     */
    public int getAdjasentPointCode(int opc, SccpAddress cgn, SccpAddress cdn) {
        return -1;
    }
}
