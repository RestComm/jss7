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

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.mobicents.protocols.ss7.mtp.provider.MtpProvider;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;

/**
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public interface SccpProvider extends Serializable {

    /**
     * Gets the access to message factory.
     * 
     * @return message factory.
     */
    public MessageFactory getMessageFactory();

    /**
     * Gets the access to parameter factory.
     * 
     * @return parameter factory
     */
    public ParameterFactory getParameterFactory();
    
    /**
     * Assigns router for SCCP service.
     * 
     * @param router
     */
    public void setRouter(Router router);
    /**
     * Sets sccp listener
     * @param listener
     */
    public void addSccpListener(SccpListener listener);

    /**
     * Removes listener
     */
    public void removeSccpListener(SccpListener listener);

    /**
     * Send sccp byte[] to desired addres.
     * @param calledParty - destination address of this message
     * @param callingParty - local address
     * @param data - byte[] encoded of sccp parameters
     * @throws IOException
     */
    public void send(SccpMessage message) throws IOException;//FIXME: add support for UDTs?

    /**
     * Assigns linksets to the SCCP layer.
     * 
     * @param linksets the MTP linkset list.
     */
    public void setLinksets(List<MtpProvider> linksets);

    /**
     * Starts SCCP layer.
     * 
     * @throws IllegalStateException if the SCCP has no assigned linksets. 
     */
    public void start() throws Exception;

    /**
     * Stops SCCP layer.
     * 
     */
    public void stop();
}
