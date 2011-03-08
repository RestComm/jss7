/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.mobicents.protocols.ss7.m3ua.impl;

import java.io.IOException;

import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;

/**
 * A process instance of a Signalling Gateway (SGP). It serves as an active,
 * backup, load-sharing, or broadcast process of a Signalling Gateway.
 * 
 * @author amit bhayani
 * 
 */
public interface Sgp {

    /**
     * Start this Signalling Gateway Process
     * 
     * @throws IOException
     */
    public void start() throws IOException;

    /**
     * Stop this Signalling Gateway Process
     */
    public void stop();

    /**
     * Send the payload data to peer. The SGP process should find appropriate
     * {@link As} depending on the DPC / OPC / SI included in this payload
     * 
     * @param msu
     * @throws Exception
     */
    public void send(byte[] msu) throws Exception;

    /**
     * Poll this SGP and retrieve the {@link PayloadData} from all the
     * {@link As} that this SGP represents
     * 
     * @return
     */
    public PayloadData poll();

    /**
     * Create new {@link As}
     * 
     * @param args
     * @return
     * @throws Exception
     */
    public As createAppServer(String args[]) throws Exception;

    /**
     * Create new {@link AspFactory}
     * 
     * @param args
     * @return
     * @throws Exception
     */
    public AspFactory createAspFactory(String[] args) throws Exception;

    /**
     * Associate {@link Asp} to {@link As}
     * 
     * @param asName
     * @param aspName
     * @return
     * @throws Exception
     */
    public Asp assignAspToAs(String asName, String aspName) throws Exception;
    
    public void startAsp(String aspName) throws Exception;
    
    public void stopAsp(String aspName) throws Exception;

}
