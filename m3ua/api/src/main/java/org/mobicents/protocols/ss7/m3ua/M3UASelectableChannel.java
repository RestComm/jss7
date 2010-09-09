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

import java.nio.channels.ClosedChannelException;

/**
 * A channel that can be multiplexed via a M3UASelector.
 * 
 * In order to be used with a selector, an instance of this class must first be registered via 
 * the register method. This method returns a new M3UASelectionKey object that represents 
 * the channel's registration with the selector.
 * 
 * @author kulikov
 */
public interface M3UASelectableChannel {
    /**
     * Registers this channel with the given selector, returning a selection key.
     * 
     * @param selector The selector with which this channel is to be registered
     * @param ops The interest set for the resulting key
     * @return A key representing the registration of this channel with the given selector.
     * @throws java.nio.channels.ClosedChannelException
     */
    public M3UASelectionKey register(M3UASelector selector, int ops) throws ClosedChannelException;
}
