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

package org.mobicents.protocols.ss7.mtp.provider;

import java.io.IOException;
import java.util.Properties;

import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;

/**
 * Interface for classes able to provide MTP signaling.
 * 
 * @author baranowb
 * @author kulikov
 */
public interface MtpProvider {

    /**
     * Gets the name of this linkset.
     * 
     * @return
     */
    public String getName();
    
    /**
     * Assigns originated point code
     * 
     * @param opc the originated point code
     */
    public void setOriginalPointCode(int opc);

    /**
     * Assigns destination point code.
     * 
     * @param dpc destination point code in decimal format.
     */
    public void setAdjacentPointCode(int dpc);

    /**
     * @return the dpc
     */
    public int getAdjacentPointCode();

    /**
     * @return the opc
     */
    public int getOriginalPointCode();
    /**
     * Sets network indicator to be used as part of SIO( actually SSI). It Accepts 2 bit integer.
     * @param ni
     */
    public void setNetworkIndicator(int ni);
    
    public int getNetworkIndicator();

    /**
     * Sets listener for MTP callbacks. If null is passed internal refence is
     * cleared.
     * 
     * @param lst
     */
    public void setMtpListener(MtpListener lst); 

    /**
     * Passes argument to MTP layers for processing. Passed buffer should not be
     * reused after passing to this method!
     * 
     * @param msu
     * @throws IOException
     *             - when IO can not be performed, ie, link is not up.
     * @return
     */
    public void send(byte[] msu) throws IOException;

    /**
     * Starts this provider implementation. Depending on internal it can start
     * local MTP process, or M3UA layer.
     * 
     * @throws IOException
     * @throws StartFailedException
     */
    public void start() throws StartFailedException;

    /**
     * Stops this provider. This call clears all references, ie. listener is
     * cleared as {@link #setMtpListener(MtpListener)} with null argument.
     */
    public void stop();

    /**
     * Method which configures implementation. Depending on implementation
     * different properties are supported. However each property starts with
     * "mtp." prefix.
     * 
     * @param p
     */
    public void configure(Properties p) throws ConfigurationException;

    /**
     * Checks if link is up;
     * 
     * @return
     */
    public boolean isLinkUp();
}
