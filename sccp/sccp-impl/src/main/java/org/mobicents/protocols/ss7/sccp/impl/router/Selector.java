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
