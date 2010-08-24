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
    private GTIndicator gti;
    //point code indicator
    private boolean pcPresent;
    //ssn indicator
    private boolean ssnPresent;
    //routing indicator             
    private RtgIndicator rtg;
    
    public AddressIndicator(byte v) {
        pcPresent = (v & 0x01) == 0x01;
        ssnPresent = (v & 0x02) == 0x02;
        gti = GTIndicator.valueOf((v >> 2) & 0x0f);
        
         rtg = ((v >> 6) & 0x01) == 0x01 ? 
             RtgIndicator.ROUTING_BASED_ON_DPC_AND_SSN :
             RtgIndicator.ROUTING_BASED_ON_GLOBAL_TITLE;
    }
    
    public GTIndicator getGlobalTitleIndicator() {
        return gti;
    }
    
    public boolean pcPresent() {
        return pcPresent;
    }
    
    public boolean ssnPresent() {
        return ssnPresent;
    }
    
    public RtgIndicator getRoutingIndicator() {
        return rtg;
    }
}
