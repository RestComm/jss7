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

package org.mobicents.protocols.ss7.sccp.impl.gtt;

import java.util.ArrayList;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * Global Title translation table.
 * 
 * To define the GT translation the translation rule must be 
 * added to the table.
 * 
 * @author kulikov
 */
public class GTT {
    /** List of translation rules */
    private ArrayList<TranslationRule> rules = new ArrayList();
    
    /**
     * Adds new rule to the translation table.
     * 
     * @param rule the rule to add
     */
    public void addEntry(TranslationRule rule) {
        this.rules.add(rule);
    }
    
    /**
     * Removes specified rule from translation table.
     * 
     * @param rule the rule to be removed.
     */
    public void removeEntry(TranslationRule rule) {
        this.rules.remove(rule);
    }
    
    /**
     * Perform translation of the specified sccp address.
     * 
     * @param address the address to be translated.
     * @return the translated address;
     */
    public SccpAddress translate(SccpAddress address) {
        String digits = address.getGlobalTitle().getDigits();
        for (TranslationRule rule : rules) {
            if (rule.getAddress().matches(digits)) {
                //@TODO do translations here
                return null;
            }
        }
        
        //leave address as is
        return address;
    }
}
