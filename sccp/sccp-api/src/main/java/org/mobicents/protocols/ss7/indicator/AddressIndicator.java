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
 *
 * @author kulikov
 */
public class AddressIndicator {
    //Global title indicator
    private GlobalTitleIndicator globalTitleIndicator;
    //point code indicator
    private boolean pcPresent;
    //ssn indicator
    private boolean ssnPresent;
    //routing indicator             
    private RoutingIndicator routingIndicator;
    
    public AddressIndicator(boolean pcPresent, boolean ssnPresent, RoutingIndicator rti, GlobalTitleIndicator gti) {
        this.pcPresent = pcPresent;
        this.ssnPresent = ssnPresent;
        this.routingIndicator = rti;
        this.globalTitleIndicator = gti;
    }
    
    public AddressIndicator(byte v) {
        pcPresent = (v & 0x01) == 0x01;
        ssnPresent = (v & 0x02) == 0x02;
        globalTitleIndicator = GlobalTitleIndicator.valueOf((v >> 2) & 0x0f);
        
        routingIndicator = ((v >> 6) & 0x01) == 0x01 ? 
             RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN :
             RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE;
    }
    
    public GlobalTitleIndicator getGlobalTitleIndicator() {
        return globalTitleIndicator;
    }
    
    public boolean pcPresent() {
        return pcPresent;
    }
    
    public boolean ssnPresent() {
        return ssnPresent;
    }
    
    public RoutingIndicator getRoutingIndicator() {
        return routingIndicator;
    }
    
    public byte getValue() {
        int b = 0;
        
        if (pcPresent) {
            b |= 0x01;
        }
        
        if (ssnPresent) {
            b |= 0x02;
        }
        
        b |= (globalTitleIndicator.getValue() << 2);
        
        if (routingIndicator == RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN) {
            b |= 0x40;
        }
        
        return (byte)b;
    }
}
