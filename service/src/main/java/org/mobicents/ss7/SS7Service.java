/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.ss7;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class SS7Service extends ServiceMBeanSupport implements SS7ServiceMBean {

    private final String serviceName;
    private Object stack;

    private String jndiName;

    private Logger logger = Logger.getLogger(SS7Service.class);

    private static final String rLogo = " ]]]]]]]]] ";
    private static final String lLogo = " [[[[[[[[[ ";

    public SS7Service(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public void startService() throws Exception {
        // starting
        rebind(stack);
        logger.info(generateMessageWithLogo("service started"));
    }

    private String generateMessageWithLogo(String message) {
        return lLogo + getSS7Name() + " " + getSS7Version() + " " + this.serviceName + " " + message + rLogo;
    }

    public String getSS7Name() {
        String name = Version.instance.getProperty("name");
        if (name != null) {
            return name;
        } else {
            return "Mobicents jSS7 Service";
        }
    }

    public String getSS7Vendor() {
        String vendor = Version.instance.getProperty("vendor");
        if (vendor != null) {
            return vendor;
        } else {
            return "TeleStax Inc";
        }
    }

    public String getSS7Version() {
        String version = Version.instance.getProperty("version");
        if (version != null) {
            return version;
        } else {
            return "2.0";
        }
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getJndiName() {
        return jndiName;
    }

    public Object getStack() {
        return stack;
    }

    public void setStack(Object stack) {
        this.stack = stack;
    }

    @Override
    public void stopService() {
        try {
            unbind(jndiName);
        } catch (Exception e) {

        }

        logger.info(generateMessageWithLogo("service stopped"));
    }

    /**
     * Binds trunk object to the JNDI under the jndiName.
     */
    private void rebind(Object stack) throws NamingException {
        Context ctx = new InitialContext();
        String[] tokens = jndiName.split("/");

        for (int i = 0; i < tokens.length - 1; i++) {
            if (tokens[i].trim().length() > 0) {
                try {
                    ctx = (Context) ctx.lookup(tokens[i]);
                } catch (NamingException e) {
                    ctx = ctx.createSubcontext(tokens[i]);
                }
            }
        }

        ctx.bind(tokens[tokens.length - 1], stack);
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