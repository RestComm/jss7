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

import java.net.InetAddress;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.protocols.ss7.mtp.oam.LinksetManager;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class SS7Service extends ServiceMBeanSupport implements SS7ServiceMBean {

    private ShellExecutor shell = null;

    private LinksetManager linksetManager;
    private SccpStackImpl sccpStack;

    private String path;
    private String jndiName;

    private String shellAddress = "127.0.0.1";
    private int shellPort = 3435;

    private Logger logger = Logger.getLogger(SS7Service.class);

    @Override
    public void startService() throws Exception {
        // starting sccp router
        logger.info("Starting SCCP stack...");

        sccpStack = new SccpStackImpl();
        sccpStack.setConfigPath(path);

        // sccpStack.setLinksets(linksets);
        sccpStack.start();
        rebind(sccpStack);

        logger.info("SCCP stack Started. SccpProvider bound to "
                + this.jndiName);

        logger.info("Starting SS7 management shell environment");
        shell = new ShellExecutor(InetAddress.getByName(this.shellAddress),
                this.shellPort);
        shell.start();

        logger.info("Started SS7 service");
    }

    public String getShellAddress() {
        return shellAddress;
    }

    public void setShellAddress(String shellAddress) {
        this.shellAddress = shellAddress;
    }

    public int getShellPort() {
        return shellPort;
    }

    public void setShellPort(int shellPort) {
        this.shellPort = shellPort;
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

    @Override
    public void stopService() {
        try {
            unbind(jndiName);
        } catch (Exception e) {
        }

        shell.stop();

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