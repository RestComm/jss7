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
import java.util.Properties;

import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.ss7.mtp.RoutingLabel;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public interface SccpProvider {
	
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
	 * @param ar - reference with mtp3 routing label
	 * @throws IOException
	 */
	public void send(SccpAddress calledParty, SccpAddress callingParty,
			byte[] data,RoutingLabel ar) throws IOException;//FIXME: add support for UDTs?

	/**
	 * UnitData factory instance.
	 * @return
	 */
	public SccpUnitDataFactory getUnitDataFactory();
	/**
	 * Parameter factory instance.
	 * @return
	 */
	public SccpParameterFactory getSccpParameterFactory();
	
	public void configure(Properties p) throws ConfigurationException;
}
