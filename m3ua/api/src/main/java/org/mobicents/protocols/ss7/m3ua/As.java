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
 * <p>
 * Application Server (AS) is a logical entity serving a specific Routing Key. An example of an Application Server is a virtual
 * switch element handling all call processing for a signalling relation, identified by an SS7 DPC/OPC. Another example is a
 * virtual database element, handling all HLR transactions for a particular SS7 SIO/DPC/OPC combination. The AS contains a set
 * of one or more unique Application Server Processes, of which one or more is normally actively processing traffic
 * </p>
 *
 * @author amit bhayani
 *
 */

public interface As {

    /**
     * Each As in M3UA stack is uniquely identified by its name
     *
     * @return name of this As
     */
    String getName();

    /**
     * Returns true if atleast one {@link Asp} in this As is ACTIVE and exchanging payload
     *
     * @return
     */
    boolean isConnected();

    /**
     * Returns true if state of this As is ACTIVE
     *
     * @return
     */
    boolean isUp();

    /**
     * {@link RoutingContext} associated with this As. Configuring routing context is optional and can return null if not
     * configured. Note that there is a 1:1 relationship between an AS and a Routing Key
     *
     * @return Routing Key
     */
    RoutingContext getRoutingContext();

    /**
     * The {@link Functionality} of this As
     *
     * @return
     */
    Functionality getFunctionality();

    /**
     * {@link ExchangeType} for this As
     *
     * @return
     */
    ExchangeType getExchangeType();

    /**
     * {@link IPSPType} for this As. This is useful only if {@link Functionality} for this As is IPSP
     *
     * @return
     */
    IPSPType getIpspType();

    /**
     * {@link NetworkAppearance} for this As
     *
     * @return
     */
    NetworkAppearance getNetworkAppearance();

    /**
     * {@link TrafficModeType} for this As
     *
     * @return
     */
    TrafficModeType getTrafficModeType();

    /**
     * Default {@link TrafficModeType} that this As will assume if TrafficModeType is not negotiated or not set when defining
     * this As
     *
     * @return
     */
    TrafficModeType getDefaultTrafficModeType();

    /**
     * The minimu number of {@link Asp} that should be ACTIVE before this As becomes ACTIVE. This value is useful only if
     * {@link TrafficModeType} is loadshare
     *
     * @return
     */
    int getMinAspActiveForLb();

    /**
     * Number of {@link Asp} assigned to this As
     *
     * @return
     */
    List<Asp> getAspList();

    /**
     * Current state of this As
     *
     * @return
     */
    State getState();

}
