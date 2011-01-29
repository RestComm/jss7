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
 * v. 2.0 along with this distribution; if not, encode to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.protocols.ss7.m3ua.parameter;


/**
 * Protocl data parameter.
 * 
 * @author kulikov
 */
public interface ProtocolData extends Parameter {
    /** 
     * Gets origination point code.
     * 
     * @return point code value in decimal format.
     */
    public int getOpc();
    
    /**
     * Gets destination point code
     * 
     * @return point code value in decimal format
     */
    public int getDpc();
    
    /**
     * Gets the service indicator.
     * 
     * @return service indicator value.
     */
    public int getSI();
    
    /**
     * Gets the network indicator.
     * 
     * @return the network indicator value.
     */
    public int getNI();
    
    /**
     * Gets the message priority.
     * 
     * @return message priority value.
     */
    public int getMP();
    
    /**
     * Gets the signaling link selection.
     * 
     * @return the signaling link selection value
     */
    public int getSLS();

    /**
     * Gets the payload of message.
     * 
     * @return binary message.
     */
    public byte[] getData();

    /**
     * Gets the message encoded as SS7 message signaling unit.
     * 
     * @return binary message signaling unit
     */
    public byte[] getMsu();
}
