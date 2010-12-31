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
package org.mobicents.ss7;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetExecutor;
import org.mobicents.ss7.linkset.oam.LinksetManager;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class SS7Service extends ServiceMBeanSupport implements SS7ServiceMBean {

    private ShellExecutor shellExecutor = null;

    private LinksetManager linksetManager;
    private SccpStackImpl sccpStack;

    private String path;
    private String jndiName;

    private Logger logger = Logger.getLogger(SS7Service.class);

    @Override
    public void startService() throws Exception {
        // starting sccp router
        logger.info("Starting SCCP stack...");

        sccpStack = new SccpStackImpl();
        sccpStack.setConfigPath(path);

        FastMap<String, Linkset> map = this.linksetManager.getLinksets();
        for (FastMap.Entry<String, Linkset> e = map.head(), end = map.tail(); (e = e
                .getNext()) != end;) {
            Linkset value = e.getValue();
            ((SccpStackImpl) sccpStack).add(value);
        }

        // sccpStack.setLinksets(linksets);
        sccpStack.start();
        rebind(sccpStack);

        logger.info("SCCP stack Started. SccpProvider bound to "
                + this.jndiName);

        if (this.shellExecutor != null) {
            LinksetExecutor linksetExec = new LinksetExecutor();
            linksetExec.setLinksetManager(this.linksetManager);
            this.shellExecutor.setLinksetExecutor(linksetExec);
            this.shellExecutor.startService();
        }

        logger.info("Started SS7 service");
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setConfigPath(String path) {
        this.path = path;
    }

    public ShellExecutor getShellExecutor() {
        return shellExecutor;
    }

    public void setShellExecutor(ShellExecutor shellExecutor) {
        this.shellExecutor = shellExecutor;
    }

    @Override
    public void stopService() {
        try {
            unbind(jndiName);
        } catch (Exception e) {

        }
        if (this.shellExecutor != null) {
            this.shellExecutor.stopService();
        }

        logger.info("Stopped SS7 service");
    }

    public void setLinksetManager(LinksetManager manager) {
        this.linksetManager = manager;
    }

    public LinksetManager getLinksetManager() {
        return linksetManager;
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

}