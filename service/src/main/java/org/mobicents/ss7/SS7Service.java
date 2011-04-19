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

package org.mobicents.ss7;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.RouterImpl;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetManager;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class SS7Service extends ServiceMBeanSupport implements SS7ServiceMBean {

    private ShellExecutor shellExecutor = null;
    private RemSignalingGateway remSignalingGateway = null;
    private LinksetManager linksetManager;
    private SccpStackImpl sccpStack;
    private RouterImpl routerImpl;

    private String jndiName;

    private Logger logger = Logger.getLogger(SS7Service.class);

    @Override
    public void startService() throws Exception {
        // starting sccp router
        logger.info("Starting SCCP stack...");

        sccpStack = new SccpStackImpl();
        sccpStack.setRouter(routerImpl);

        FastMap<String, Linkset> map = this.linksetManager.getLinksets();
        for (FastMap.Entry<String, Linkset> e = map.head(), end = map.tail(); (e = e.getNext()) != end;) {
            Linkset value = e.getValue();
            ((SccpStackImpl) sccpStack).add(value);
        }

        // sccpStack.setLinksets(linksets);
        sccpStack.start();
        rebind(sccpStack);

        logger.info("SCCP stack Started. SccpProvider bound to " + this.jndiName);

        if (this.shellExecutor != null) {
            this.shellExecutor.startService();
        }

        if (this.remSignalingGateway != null) {
            this.remSignalingGateway.startService();
        }

        logger.info("[[[[[[[[[ Mobicents SS7 service started ]]]]]]]]]");
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getJndiName() {
        return jndiName;
    }

    public ShellExecutor getShellExecutor() {
        return shellExecutor;
    }

    public void setShellExecutor(ShellExecutor shellExecutor) {
        this.shellExecutor = shellExecutor;
    }

    public RemSignalingGateway getRemSignalingGateway() {
        return remSignalingGateway;
    }

    public void setRemSignalingGateway(RemSignalingGateway remSignalingGateway) {
        this.remSignalingGateway = remSignalingGateway;
    }

    public RouterImpl getRouterImpl() {
        return routerImpl;
    }

    public void setRouterImpl(RouterImpl routerImpl) {
        this.routerImpl = routerImpl;
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
        if (this.remSignalingGateway != null) {
            this.remSignalingGateway.stopService();
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