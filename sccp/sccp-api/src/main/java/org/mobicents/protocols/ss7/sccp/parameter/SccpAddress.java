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
package org.mobicents.protocols.ss7.sccp.parameter;

import org.mobicents.protocols.ss7.indicator.AddressIndicator;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;

/**
 * 
 * 
 * @author baranowb
 * @author kulikov
 */
public class SccpAddress {

    private GlobalTitle gt;
    private int pc;
    private int ssn;

    private AddressIndicator ai;
    
    public SccpAddress(GlobalTitle gt, int ssn) {
        this.gt = gt;
        this.ssn = ssn;
        this.ai = new AddressIndicator(false, ssn != 0, 
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt.getIndicator());
    }

    public SccpAddress(int pc, int ssn) {
        this.pc = pc;
        this.ssn = ssn;
        this.ai = new AddressIndicator(pc != 0, ssn != 0, 
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, gt.getIndicator());
    }

    public AddressIndicator getAddressIndicator() {
        return this.ai;
    }
    
    public int getSignalingPointCode() {
        return pc;
    }

    public int getSubsystemNumber() {
        return ssn;
    }

    public GlobalTitle getGlobalTitle() {
        return gt;
    }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof SccpAddress)) {
            return false;
        }
        
        SccpAddress address = (SccpAddress) other;
        
        boolean res = false;
        
        if (address.gt != null) {
            res = gt != null && address.gt.equals(gt);
        }
        
        if (!res) {
            return false;
        }
        
        return address.ssn == ssn && address.pc == pc;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.gt != null ? this.gt.hashCode() : 0);
        hash = 37 * hash + this.pc;
        hash = 37 * hash + this.ssn;
        hash = 37 * hash + (this.ai != null ? this.ai.hashCode() : 0);
        return hash;
    }
}
