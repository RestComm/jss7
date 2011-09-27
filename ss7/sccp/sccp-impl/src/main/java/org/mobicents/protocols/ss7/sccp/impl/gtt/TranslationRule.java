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

package org.mobicents.protocols.ss7.sccp.impl.gtt;

/**
 * Translation rule for the SCCP address.
 * 
 * One of the function of SCCP is to allow address to be translated to another address 
 * according to translation table. 
 * 
 * The rule is defined by three parameters: 
 * <ul>
 * <li>address</li>
 * <li>dpc</li>
 * <li>ssn</li>
 * </ul>
 * 
 * Parameter <code>address</code> is a text string which defines the pattern for selecting rule 
 * and at the same time defines the law of global title digits transformation.
 * 
 * The common format of the address field is as follows:
 * mask[/ins(pos, seq)][/rem(pos, len)]
 * 
 * where mask consist of digits and/or wild card symbols(x, *). The wildcard symbol x means 
 * any one digit in the current position. For example '01x' matches to the following sequence:
 * 010, 011, 012,..., 019. The '*' symbol means any digit sequence, for instance
 * 01* matches to the 010, 011, 01234, etc.
 * 
 * if digits of the GT to be translated match to the mask then transformation rules defined 
 * in the address field will be applied subsequently
 * 
 * ins (pos, seq) - inserts 'seq' into the postion specified by parameter <code>pos</code>
 * 
 * example:
 * 
 * 9023629581/ins(0,7) converts number 9023629581 to number 79023629581
 * 
 * @author kulikov
 */
public class TranslationRule {
    
    /** Digit transformation pattern */
    private String address;
    
    /** point code to apply to the translated address */
    private int dpc;
    
    /** subsystem number to apply to the translated address */
    private int ssn;
    
    /**
     * Gets the rule for digits translation.
     * 
     * @return the rule description.
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * Modifies digit translation rule.
     * 
     * @param address the translation rule.
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * Gets the Destination Point code assigned to this rule.
     * 
     * @return the point code value in decimal format
     */
    public int getDPC() {
        return dpc;
    }
    
    /**
     * Modifies destionation point code assigned to this rule.
     * 
     * @param dpc the new point code value in decimal format
     */
    public void setDPC(int dpc) {
        this.dpc = dpc;
    }
    
    /**
     * Gets the current subsytem value associated with this rule.
     * 
     * @return the subsytem number value.
     */
    public int getSSN() {
        return ssn;
    }
    
    /**
     * Modifies subsystem value.
     * 
     * @param ssn the subsytem value.
     */
    public void setSSN(int ssn) {
        this.ssn = ssn;
    }
}
