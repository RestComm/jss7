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

import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;

/**
 * 
 * @author Oleg Kulikov
 */
public abstract class GlobalTitle {

    /**
     * Defines fields included into the global title.
     * @return
     */
    public abstract GlobalTitleIndicator getIndicator();

    /**
     * Address string.
     * 
     * @return
     */
    public abstract String getDigits();

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
        return new GT0100(tt, np,  noa, digits);
    }
    
    
    public String toString() {
        return getIndicator() + " " + getDigits();
    }
}
