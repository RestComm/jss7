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

package org.mobicents.protocols.ss7.sccp.impl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 *
 * @author kulikov
 */
public class SccpService {

	private static final Logger logger = Logger.getLogger(SccpService.class);
	
    private SccpStackImpl stack;
    private String jndiName;
    
    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }
    
    public String getJndiName() {
        return jndiName;
    }
    
    public void start() throws Exception {
    	logger.info("Starting SCCP stack...");
        stack = new SccpStackImpl();
        rebind(stack);
    }
    
    public void stop() {
        try {
            unbind(jndiName);
        } catch (Exception e) {
        }
    }
    
/**
     * Binds trunk object to the JNDI under the jndiName.
     */
    private void rebind(SccpStackImpl stack) throws NamingException {
        Context ctx = new InitialContext();
        String tokens[] = jndiName.split("/");

        for (int i = 0; i < tokens.length - 1; i++) {
            if (tokens[i].trim().length() > 0) {
                try {
                    ctx = (Context) ctx.lookup(tokens[i]);
                } catch (NamingException e) {
                    ctx = ctx.createSubcontext(tokens[i]);
                }
            }
        }

        ctx.bind(tokens[tokens.length - 1], stack.getSccpProvider());
        logger.info("SCCP stack Started. SccpProvider bound to "+ this.jndiName);
    }
    
    /**
     * Unbounds object under specified name.
     *
     * @param jndiName the JNDI name of the object to be unbound.
     */
    private void unbind(String jndiName) throws NamingException {
        InitialContext initialContext = new InitialContext();
        initialContext.unbind(jndiName);
    }
    
}
