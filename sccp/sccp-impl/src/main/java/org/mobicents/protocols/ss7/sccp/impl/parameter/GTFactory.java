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
package org.mobicents.protocols.ss7.sccp.impl.parameter;

import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;

/**
 * Constructs Global title instance.
 * 
 * @author kulikov
 */
public class GTFactory {
    /**
     * Global tite contains nature of address indicator only.
     * 
     * @param noa nature of address indicator.
     * @param digits the address string
     * @return Global title instance.
     */
    public static GlobalTitle getInstance(NatureOfAddress noa, String digits) {
        return new GT0001(noa, digits);
    }
    
    /**
     * Global title contains translation type only.
     * 
     * @param tt translation type.
     * @param digits the address string
     * @return Global title instance
     */
    public static GlobalTitle getInstance(int tt, String digits) {
        return new GT0010(tt, digits);
    }
    
    /**
     * Global title contains translation type, numbering plan and encoding scheme.
     *  
     * @param tt translation type
     * @param np numbering plan
     * @param digits the address string, if number of digits even the BCD even encoding scheme 
     * is used and BCD odd otherwise.
     * @return Global title instance.
     */
    public static GlobalTitle getInstance(int tt, NumberingPlan np, String digits) {
        return new GT0011(tt, np, digits);
    }

    /**
     * Global title contains translation type, numbering plan, encoding scheme and 
     * nature of address indicator.
     * 
     * @param tt translation type.
     * @param np numbering plan
     * @param noa nature of address indicator.
     * @param digits the address string, if number of digits even the BCD even encoding scheme 
     * is used and BCD odd otherwise.
     * @return Global title instance.
     */
    public static GlobalTitle getInstance(int tt, NumberingPlan np, NatureOfAddress noa, String digits) {
        //FIXME: this construction is wrong
        return new GT0100(tt, np.getValue(), 0, noa.getValue(), digits);
    }
    
}
