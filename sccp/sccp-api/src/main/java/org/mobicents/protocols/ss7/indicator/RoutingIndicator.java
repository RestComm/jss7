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
package org.mobicents.protocols.ss7.indicator;

/**
 * The Routing Indicator (RI) specifies whether GTT is required; it determines
 * whether routing based on PC+SSN or GT. If routing is based on the GT, the GT
 * in the address is used for routing. If routing is based on PC+SSN, the PC and
 * SSN in the Called/Calling  Party Address are used.
 * 
 * @author kulikov
 */
public enum RoutingIndicator {
    
    //Route on GT
    ROUTING_BASED_ON_GLOBAL_TITLE, 
    
    //Route on PC + SSN
    ROUTING_BASED_ON_DPC_AND_SSN;
}
