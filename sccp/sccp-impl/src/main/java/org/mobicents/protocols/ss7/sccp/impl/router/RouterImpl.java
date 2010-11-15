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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;
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
    private Route route;
    
    private File file;
    
    private Logger logger = Logger.getLogger(RouterImpl.class);
    //rule list
    private ArrayList<Rule> rules = new ArrayList();
    
    public RouterImpl(String path) {
        try {
            file = new File(path);
            load();
        } catch (Exception e) {
            logger.warn("Can not load rules from config: " + path, e);
        }
    }
    /**
     * Adds new rule for routing.
     * 
     * @param rule the new rule to be added.
     */
    public void add(Rule rule) throws IOException {
        rule.setNo(rules.size());
        rules.add(rule);
        try {
            store(rule);
        } catch (Exception e) {
            rules.remove(rule);
            throw new IOException(e.getMessage());
        }
    }
    
    /**
     * Removes routing rule from router.
     * 
     * @param rule
     */
    public void remove(int no) throws IOException {
        rules.remove(no);
        //reorder rules
        int i = 0;
        for (Rule rule: rules) {
            rule.setNo(i++);
        }
        
        clean();
        for (Rule rule: rules) {
            store(rule);
        }        
    }
    
    /**
     * Looks up rule for translation.
     * 
     * @param calledParty called party address
     * @return the rule with match to the called party address
     */
    public Rule find(SccpAddress calledParty) {
        for (Rule rule : rules) {
            if (rule.matches(calledParty)) {
                return rule;
            }
        }
        return null;
    }
    
    /**
     * Gets the list of all rules.
     * 
     * @return list of rules.
     */
    public Collection<Rule> list() {
        return rules;
    }
    
    public void clean() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        writer.write("");
        writer.flush();
        writer.close();
    }
    
    public Route route(SccpAddress calledPartyAddress) {
        return route;
    }
    
    private void store(Rule rule) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(rule.toString());
        writer.newLine();
        writer.flush();
        writer.close();
    }
    
    private void load() throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = reader.readLine()) != null) {
            rules.add(Rule.getInstance(line));
        }
        reader.close();
    }
}
