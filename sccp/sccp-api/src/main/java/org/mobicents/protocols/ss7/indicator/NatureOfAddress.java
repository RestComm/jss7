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
 * Nature of address indicator.
 * 
 * @author kulikov
 */
public enum NatureOfAddress {
    SPARE(0), 
    SUBSCRIBER(1), 
    UNKNOWN(2), 
    NATIONAL(3), 
    INTERNATIONAL(4);
    
    private int value;
    
    private NatureOfAddress(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public static NatureOfAddress valueOf(int v) {
        switch (v) {
            case 0 : return SPARE;
            case 1 : return SUBSCRIBER;
            case 2 : return UNKNOWN;
            case 3 : return NATIONAL;
            case 4 : return INTERNATIONAL;
            default : return UNKNOWN;
        }
    }
}
