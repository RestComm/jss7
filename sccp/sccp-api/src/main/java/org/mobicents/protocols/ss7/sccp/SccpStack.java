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

import java.util.List;

import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.mtp.provider.MtpProvider;

/**
 * 
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
     * @throws org.mobicents.protocols.StartFailedException
     */
    public void start() throws IllegalStateException, StartFailedException;
    
    /**
     * Terminates SCCP stack.
     * 
     * @throws java.lang.IllegalStateException
     * @throws org.mobicents.protocols.StartFailedException
     */
    public void stop();
    
    /**
     * Assigns linksets. 
     * 
     * Current versions suppose static assignment. 
     * For next versions implement link management functions.
     * 
     * @param linksets the list of available linksets.
     */
    public void setLinksets(List<MtpProvider> linksets);

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
    public void setRouter(Router router);
    
    /**
     * Defines transfer type for outgoing messages.
     * 
     * @param type the type of transfer. valid values are UDT_ONLY and XUDT_ONLY.
     */
    public void setTransferType(int type);

}
