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

package org.mobicents.protocols.ss7.sccp.parameter;

import org.mobicents.protocols.ss7.indicator.AddressIndicator;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;

/**
 * 
 * 
 * @author baranowb
 * @author kulikov
 */
public class SccpAddress implements Parameter{  //impl? pfff

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
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED);
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
    
    
    public boolean equals(Object other) {
        if (!(other instanceof SccpAddress)) {
            return false;
        }
        
        SccpAddress address = (SccpAddress) other;
        
        boolean res = false;
        
        if (address.gt != null) {
            res = gt != null && address.gt.equals(gt);
            return res;
        }
        
        return address.ssn == ssn && address.pc == pc;
    }

    
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.gt != null ? this.gt.hashCode() : 0);
        hash = 37 * hash + this.pc;
        hash = 37 * hash + this.ssn;
        return hash;
    }
    
    
    public String toString() {
        return "pc=" + pc + ",ssn=" + ssn + ",gt=" + gt;
    }
}
