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

package org.mobicents.protocols.ss7.sccp;


/**
 * @author amit bhayani
 * @author baranowb
 * @author kulikov
 */
public interface SccpStack {
    public final static int UDT_ONLY = 1;
    public final static int XUDT_ONLY = 2;
    
    /**
     * Starts SCCP stack.
     * 
     * @throws java.lang.IllegalStateException
     */
    public void start() throws IllegalStateException;
    
    /**
     * Terminates SCCP stack.
     * 
     * @throws java.lang.IllegalStateException
     * @throws org.mobicents.protocols.StartFailedException
     */
    public void stop();
    
    /**
     * Exposes SCCP provider object to SCCP user.
     * 
     * @return SCCP provider object.
     */
    public SccpProvider getSccpProvider();
    
    /**
     * Allows to plug custom implementation of SCCP router 
     * 
     * @param router the router implementation.
     */
    //public void setRouter(Router router);
    
    /**
     * Defines transfer type for outgoing messages.
     * 
     * @param type the type of transfer. valid values are UDT_ONLY and XUDT_ONLY.
     */
    public void setTransferType(int type);

}
