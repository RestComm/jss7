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

package org.mobicents.protocols.ss7.sccp.impl.router;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * The default implementation for the SCCP router.
 * 
 * The SCCP router allows to add/remove/list routing rules and implements persistance for 
 * the routing rules.
 * 
 * @author kulikov
 */
public class RouterImpl {
    private final static String STORAGE = "/sccp-routing.cfg";
    private Route route;
    
    private File file;
    
    //rule list
    private ArrayList rules = new ArrayList();
    
    /**
     * Adds new rule for routing.
     * 
     * @param rule the new rule to be added.
     */
    public void add(Rule rule) {
        rule.no = rules.size();
        rules.add(rule);
        //TODO add persistance
    }
    
    /**
     * Removes routing rule from router.
     * 
     * @param rule
     */
    public void remove(int no) {
        rules.remove(no);
        //TODO add persistance
    }
    
    /**
     * Gets the list of all rules.
     * 
     * @return list of rules.
     */
    public Collection<Rule> list() {
        return rules;
    }
    
    public Route route(SccpAddress calledPartyAddress) {
        return route;
    }
    
    private void store(Rule rule) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(rule.toString());
    }
}
