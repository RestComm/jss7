/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012. 
 * and individual contributors
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

import java.util.List;

import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface M3UAManagement {

	/**
	 * Unique name of this M3UA stack. The configuration of stack is persisted
	 * in name_m3ua.xml file
	 * 
	 * @return name of this stack
	 */
	public String getName();

	/**
	 * The directory where configuration file is persisted.
	 * 
	 * @return directory where configuration file is persisted
	 */
	public String getPersistDir();

	/**
	 * Set the directory where the configuration file is persisted
	 * 
	 * @param persistDir
	 */
	public void setPersistDir(String persistDir);

	/**
	 * The maximum SLS value. By Default this is set to 256
	 * 
	 * @return maximum SLS
	 */
	public int getMaxSequenceNumber();

	/**
	 * Set the maximum SLS
	 * 
	 * @param maxSequenceNumber
	 *            a integer value
	 */
	public void setMaxSequenceNumber(int maxSequenceNumber);

	/**
	 * <p>
	 * Returns maximum AS that can load-balance payload for given route.
	 * </p>
	 * 
	 * @return maximum AS configured for given route
	 */
	public int getMaxAsForRoute();

	/**
	 * Set maximum AS that can be configured for given route
	 * 
	 * @param maxAsForRoute
	 */
	public void setMaxAsForRoute(int maxAsForRoute);

	/**
	 * Start M3UA stack
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception;

	/**
	 * Stop M3UA stack
	 * 
	 * @throws Exception
	 */
	public void stop() throws Exception;

	/**
	 * Returns true if this M3UA stack is started
	 * 
	 * @return
	 */
	public boolean isStarted();

	/**
	 * Returns list of {@link As} configured for this stack
	 * 
	 * @return list of {@link As} configured for this stack
	 */
	public List<As> getAppServers();

	/**
	 * Returns list of {@link AspFactory} configured for this stack
	 * 
	 * @return
	 */
	public List<AspFactory> getAspfactories();

	/**
	 * Create new {@link As}.
	 * 
	 * @param asName
	 *            Unque name of As
	 * @param functionality
	 *            {@link Functionality} of this As
	 * @param exchangeType
	 *            {@link ExchangeType} of this As. The handshake mechanism
	 *            before As is ACTIVE
	 * @param ipspType
	 *            {@link IPSPType} of this As. This is used only if
	 *            {@link Functionality} is IPSP
	 * @param rc
	 *            Optional {@link RoutingContext}
	 * @param trafficMode
	 *            Optional {@link TrafficModeType}.
	 * @param minAspActiveForLoadbalance
	 *            Minimum {@link Asp} that should be ACTIVE before paylaod can
	 *            be transfered. This is used only if {@link TrafficModeType} is
	 *            Loadshare.
	 * @param na
	 *            Optional {@link NetworkAppearance}.
	 * @return newly created {@link As}
	 * @throws Exception
	 */
	public As createAs(String asName, Functionality functionality, ExchangeType exchangeType, IPSPType ipspType,
			RoutingContext rc, TrafficModeType trafficMode, int minAspActiveForLoadbalance, NetworkAppearance na)
			throws Exception;

	/**
	 * Destroys the {@link As} that matches the passed asName. {@link As} should
	 * be DOWN before it can be destroyed
	 * 
	 * @param asName
	 *            name of As to be destroyed.
	 * @return destroyed {@link As}
	 * @throws Exception
	 */
	public As destroyAs(String asName) throws Exception;

	/**
	 * Create a new {@link AspFactory}. Unique ASP id is assigned
	 * 
	 * @param aspName
	 *            unique name of this AspFactory
	 * @param associationName
	 *            the underlying SCTP Association to be used
	 * @return newly created AspFactory
	 * @throws Exception
	 */
	public AspFactory createAspFactory(String aspName, String associationName) throws Exception;

	/**
	 * Create a new {@link AspFactory}.
	 * 
	 * @param aspName
	 *            unique name of this AspFactory
	 * @param associationName
	 * @param aspid
	 *            unique asp id
	 * @return newly created AspFactory
	 * @throws Exception
	 */
	public AspFactory createAspFactory(String aspName, String associationName, long aspid) throws Exception;

	/**
	 * Destroys the {@link AspFactory} that matches the passed aspName. All the
	 * ASP within this AspFactory should be DOWN and un-assigned from As
	 * 
	 * @param aspName
	 *            name of AspFactory to be destroyed.
	 * @return destroyed AspFactory
	 * @throws Exception
	 */
	public AspFactory destroyAspFactory(String aspName) throws Exception;

	/**
	 * Creates a new {@link Asp} and assigns to {@link As}.
	 * 
	 * @param asName
	 *            name of As
	 * @param aspName
	 *            name of {@link AspFactory} from which Asp is to be created
	 * @return newly created Asp
	 * @throws Exception
	 */
	public Asp assignAspToAs(String asName, String aspName) throws Exception;

	/**
	 * Unassigns {@link Asp} from {@link As}
	 * 
	 * @param asName
	 *            name of As
	 * @param aspName
	 *            name of Asp
	 * @return unassigned Asp
	 * @throws Exception
	 */
	public Asp unassignAspFromAs(String asName, String aspName) throws Exception;

	/**
	 * Starts the {@link AspFactory} and all the {@link Asp} within this
	 * AspFactory will start the hand-shake mechanism
	 * 
	 * @param aspName
	 *            name of AspFactory
	 * @throws Exception
	 */
	public void startAsp(String aspName) throws Exception;

	/**
	 * Stops the {@link AspFactory} and all the {@link Asp} within this
	 * AspFactory will exchange messages to bring down ASP
	 * 
	 * @param aspName
	 *            name of AspFactory
	 * @throws Exception
	 */
	public void stopAsp(String aspName) throws Exception;

	/**
	 * Add new route based on Destination Point Code (DPC), Originating Point
	 * Code (OPC) and Service Indicator (SI). While DPC is mandatory and should
	 * be actual value, OPC and SI can be -1 indicating wild card
	 * 
	 * @param dpc
	 *            destination point code
	 * @param opc
	 *            originating point code
	 * @param si
	 *            service indicator
	 * @param asName
	 *            name of {@link As}
	 * @throws Exception
	 */
	public void addRoute(int dpc, int opc, int si, String asName) throws Exception;

	/**
	 * Remove the As for given route
	 * 
	 * @param dpc
	 *            destination point code
	 * @param opc
	 *            originating point code
	 * @param si
	 *            service indicator
	 * @param asName
	 *            name of {@link As}
	 * @throws Exception
	 */
	public void removeRoute(int dpc, int opc, int si, String asName) throws Exception;

	/**
	 * Convenient method to remove all the resources of this M3UA stack
	 * 
	 * @throws Exception
	 */
	public void removeAllResourses() throws Exception;

	/**
	 * Add new {@link M3UAManagementEventListener}
	 * 
	 * @param m3uaManagementEventListener
	 */
	public void addM3UAManagementEventListener(M3UAManagementEventListener m3uaManagementEventListener);

	/**
	 * Remove existing {@link M3UAManagementEventListener}
	 * 
	 * @param m3uaManagementEventListener
	 */
	public void removeM3UAManagementEventListener(M3UAManagementEventListener m3uaManagementEventListener);
}
