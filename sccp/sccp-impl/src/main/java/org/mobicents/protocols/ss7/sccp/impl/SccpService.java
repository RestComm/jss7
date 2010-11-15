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

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.protocols.ss7.mtp.provider.MtpProvider;
import org.mobicents.protocols.ss7.sccp.Router;
import org.mobicents.protocols.ss7.sccp.impl.router.RouterImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.Rule;
import org.mobicents.protocols.ss7.sccp.impl.router.RuleException;

/**
 * 
 * @author kulikov
 */
public class SccpService extends ServiceMBeanSupport implements SccpServiceMBean {

    private static final Logger logger = Logger.getLogger(SccpService.class);
    private SccpStackImpl stack;
    private String jndiName;
    private String path;
    private List<MtpProvider> linksets;

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setLinksets(List<MtpProvider> linksets) {
        this.linksets = linksets;
    }

    public void setConfigPath(String path) {
        this.path = path;
    }
    
    @Override
    public void startService() throws Exception {
        logger.info("Starting SCCP stack...");
        stack = new SccpStackImpl();
        stack.setConfigPath(path);
        stack.setLinksets(linksets);
        stack.start();
        rebind(stack);
    }

    @Override
    public void stopService() {
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
        logger.info("SCCP stack Started. SccpProvider bound to " + this.jndiName);
    }

    /**
     * Unbounds object under specified name.
     * 
     * @param jndiName
     *            the JNDI name of the object to be unbound.
     */
    private void unbind(String jndiName) throws NamingException {
        InitialContext initialContext = new InitialContext();
        initialContext.unbind(jndiName);
    }

    @Override
    public void addRule(Rule r) throws RuleException {
        Router router = stack.getRouter(); //this is null... ech
        if (router != null && router instanceof RouterImpl) {
            RouterImpl dr = (RouterImpl) router;

        }

    }

    @Override
    public boolean removeRule(int num) {
        return false;

    }

    @Override
    public Rule[] getRules() {
        return new Rule[]{};
    }
}
