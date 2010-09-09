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
package org.mobicents.protocols.ss7.m3ua;

import java.io.IOException;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.message.parm.ParameterFactory;

/**
 * Service-provider class for selectors and M3UA channels.
 * @author kulikov
 */
public interface M3UAProvider {
    /**
     * Opens new M3UA channel.
     * 
     * @return the new channel.
     * @throws java.io.IOException
     */
    public M3UAChannel openChannel() throws IOException;
    
    /**
     * Opens new server channel.
     * 
     * @return new server channel.
     * @throws java.io.IOException
     */
    public M3UAServerChannel openServerChannel() throws IOException;
    
    /**
     * Opens new M3UA multiplexer.
     * 
     * @return new selector.
     * @throws java.io.IOException
     */
    public M3UASelector openSelector() throws IOException; 
    
    /**
     * Provides access to the message factory implementation.
     * 
     * @return message factory instance
     */
    public MessageFactory getMessageFactory();
    
    /**
     * Provides access to the parameter factory implementation.
     * 
     * @return parameter factory instance
     */
    public ParameterFactory getParameterFactory();
}
