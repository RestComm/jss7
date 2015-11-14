/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package com.telscale.protocols.ss7.oam.common.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MBeanHostImpl implements MBeanHost {

    protected static final Logger logger = Logger.getLogger(MBeanHostImpl.class);

    public static final String DEFAULT_DOMAIN_NAME = "com.telscale.ss7";

    private String agentId;
    private int rmiPort;
    private String domainName = DEFAULT_DOMAIN_NAME;

    private MBeanServer mBeanServer;
    private Registry reg;
    private JMXConnectorServer cs;
    protected ArrayList<ObjectName> lstRegisteredBeans = new ArrayList<ObjectName>();

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String val) {
        agentId = val;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public int getRmiPort() {
        return rmiPort;
    }

    public void setRmiPort(int val) {
        rmiPort = val;
    }

    public MBeanServer getMBeanServer() {
        return mBeanServer;
    }

    public void start() {
        boolean servFound = false;

        List<MBeanServer> servers = MBeanServerFactory.findMBeanServer(null);
        if (servers != null && servers.size() > 0) {
            for (MBeanServer server : servers) {
                String defaultDomain = server.getDefaultDomain();

                if (defaultDomain != null && defaultDomain.equals(agentId)) {
                    mBeanServer = server;
                    servFound = true;
                    logger.info(String.format("Found MBeanServer matching for agentId=%s", this.agentId));
                } else {
                    logger.warn(String.format("Found non-matching MBeanServer with default domian = %s", defaultDomain));
                }
            }
        }

        if (!servFound) {
            mBeanServer = ManagementFactory.getPlatformMBeanServer();

            if (rmiPort > 0) {
                try {
                    reg = LocateRegistry.createRegistry(rmiPort);
                    JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + rmiPort + "/server");
                    cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mBeanServer);
                    cs.start();
                } catch (RemoteException e) {
                    logger.error("RemoteException when creating and starting JMXConnectorServer", e);
                } catch (MalformedURLException e) {
                    logger.error("MalformedURLException when creating and starting JMXConnectorServer", e);
                } catch (IOException e) {
                    logger.error("IOException when creating and starting JMXConnectorServer", e);
                }
            }
        }
    }

    public void stop() {
        if (cs != null) {
            try {
                this.cs.stop();
            } catch (IOException e) {
                logger.error("IOException when stopping JMXConnectorServer", e);
            }
            cs = null;
        }
        for (ObjectName on : lstRegisteredBeans) {
            try {
                this.mBeanServer.unregisterMBean(on);
            } catch (MBeanRegistrationException e) {
                logger.error("MBeanRegistrationException when unregisterMBean", e);
            } catch (InstanceNotFoundException e) {
                logger.error("InstanceNotFoundException when unregisterMBean", e);
            }
        }
        if (reg != null) {
            try {
                UnicastRemoteObject.unexportObject(reg, true);
            } catch (NoSuchObjectException e) {
                logger.error("NoSuchObjectException when unexportObject", e);
            }
            reg = null;
        }
    }

    protected ObjectName createObjectName(MBeanLayer layer, MBeanType type, String name) throws MalformedObjectNameException {
        return new ObjectName(this.domainName + ":layer=" + layer.getName() + ",type=" + type.getName() + ",name=" + name);
    }

    public void registerMBean(MBeanLayer layer, MBeanType type, String name, Object bean) {
        if (mBeanServer != null) {
            try {
                ObjectName on = createObjectName(layer, type, name);
                this.mBeanServer.registerMBean(bean, on);
                lstRegisteredBeans.add(on);
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("Registered MBean with ObjectName=%s", on));
                }
            } catch (Exception e) {
                logger.error(
                        String.format("Error while trying to regisetr MBean for Layer=%s Type=%s name=%s", layer, type, name),
                        e);
            }
        } else {
            logger.error(String.format("mBeanServer is null. Cannot register MBean for Layer=%s Type=%s name=%s", layer, type,
                    name));
        }
    }

    public Object unregisterMBean(MBeanLayer layer, MBeanType type, String name) {
        Object bean = null;
        if (mBeanServer != null) {
            try {
                ObjectName on = createObjectName(layer, type, name);
                this.mBeanServer.unregisterMBean(on);
                bean = lstRegisteredBeans.remove(on);
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("Unregistered MBean with ObjectName=%s", on));
                }
            } catch (Exception e) {
                logger.error(
                        String.format("Error while trying to unregisetr MBean for Layer=%s Type=%s name=%s", layer, type, name),
                        e);
            }
        } else {
            logger.error(String.format("mBeanServer is null. Cannot register MBean for Layer=%s Type=%s name=%s", layer, type,
                    name));
        }

        return bean;
    }

}
