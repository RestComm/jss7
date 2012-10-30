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

	public String getName();

	public String getPersistDir();

	public void setPersistDir(String persistDir);

	public int getMaxSequenceNumber();

	public void setMaxSequenceNumber(int maxSequenceNumber);

	public int getMaxAsForRoute();

	public void setMaxAsForRoute(int maxAsForRoute);

	public void start() throws Exception;

	public void stop() throws Exception;

	public List<As> getAppServers();

	public List<AspFactory> getAspfactories();

	public As createAs(String asName, Functionality functionality, ExchangeType exchangeType, IPSPType ipspType,
			RoutingContext rc, TrafficModeType trafficMode, NetworkAppearance na) throws Exception;

	public As destroyAs(String asName) throws Exception;

	public AspFactory createAspFactory(String aspName, String associationName) throws Exception;

	public AspFactory createAspFactory(String aspName, String associationName, long aspid) throws Exception;

	public AspFactory destroyAspFactory(String aspName) throws Exception;

	public Asp assignAspToAs(String asName, String aspName) throws Exception;

	public Asp unassignAspFromAs(String asName, String aspName) throws Exception;

	public void startAsp(String aspName) throws Exception;

	public void stopAsp(String aspName) throws Exception;

	public void addRoute(int dpc, int opc, int si, String asName) throws Exception;

	public void removeRoute(int dpc, int opc, int si, String asName) throws Exception;

	public void removeAllResourses() throws Exception;
	
	public void addM3UAManagementEventListener(M3UAManagementEventListener m3uaManagementEventListener);
	
	public void removeM3UAManagementEventListener(M3UAManagementEventListener m3uaManagementEventListener);
}
