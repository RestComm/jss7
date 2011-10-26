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

package org.mobicents.protocols.ss7.m3ua;

import java.io.IOException;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.parameter.ParameterFactory;

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
