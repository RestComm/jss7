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

package org.mobicents.ss7.service;

import java.util.LinkedList;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.jboss.as.controller.services.path.PathManager;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.sctp.ManagementImpl;
import org.mobicents.protocols.ss7.cap.CAPStackImpl;
import org.mobicents.protocols.ss7.cap.api.CAPStack;
import org.mobicents.protocols.ss7.isup.ISUPStack;
import org.mobicents.protocols.ss7.isup.impl.CircuitManagerImpl;
import org.mobicents.protocols.ss7.isup.impl.ISUPStackImpl;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.mobicents.protocols.ss7.m3ua.impl.oam.M3UAShellExecutor;
import org.mobicents.protocols.ss7.m3ua.impl.oam.SCTPShellExecutor;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.mtp.RoutingLabelFormat;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmProvider;
import org.mobicents.protocols.ss7.oam.common.jmxss7.Ss7Management;
import org.mobicents.protocols.ss7.oam.common.m3ua.M3uaManagementJmx;
import org.mobicents.protocols.ss7.oam.common.sccp.SccpManagementJmx;
import org.mobicents.protocols.ss7.oam.common.sctp.SctpManagementJmx;
import org.mobicents.protocols.ss7.oam.common.statistics.CounterProviderManagement;
import org.mobicents.protocols.ss7.oam.common.tcap.TcapManagementJmx;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.oam.SccpExecutor;
import org.mobicents.protocols.ss7.scheduler.DefaultClock;
import org.mobicents.protocols.ss7.scheduler.Scheduler;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.oam.TCAPExecutor;
import org.mobicents.ss7.SS7Service;
import org.mobicents.ss7.hardware.dialogic.DialogicMtp3UserPart;
import org.mobicents.ss7.management.console.ShellExecutor;
import org.mobicents.ss7.management.console.ShellServer;
import org.mobicents.ss7.management.console.ShellServerWildFly;

/**
*
* @author sergey povarnin
* @author sergey vetyutnev
*
*/
public class SS7ExtensionService implements SS7ServiceInterface,Service<SS7ServiceInterface> {

    // //
    public static final SS7ExtensionService INSTANCE = new SS7ExtensionService();

    private final Logger log = Logger.getLogger(SS7ExtensionService.class);

    public static ServiceName getServiceName() {
        return ServiceName.of("restcomm", "ss7");
    }

    private final InjectedValue<MBeanServer> mbeanServer = new InjectedValue<MBeanServer>();

    public InjectedValue<MBeanServer> getMbeanServer() {
        return mbeanServer;
    }

    private final InjectedValue<PathManager> pathManagerInjector = new InjectedValue<PathManager>();

    public InjectedValue<PathManager> getPathManagerInjector() {
        return pathManagerInjector;
    }

    private final LinkedList<String> registeredMBeans = new LinkedList<String>();

    private static final String DATA_DIR = "jboss.server.data.dir";

    private ModelNode fullModel;

    private FastMap<String, Management> beanSctpManagements = new FastMap<String, Management>();
    private SCTPShellExecutor beanSctpShellExecutor;
    private FastMap<String, SctpManagementJmx> beanSctpManagementJmxs = new FastMap<String, SctpManagementJmx>();
    private FastMap<String, RoutingLabelFormat> routingLabelFormats = new FastMap<String, RoutingLabelFormat>();
    private FastMap<String, Mtp3UserPart> beanMtp3UserParts = new FastMap<String, Mtp3UserPart>();
    private FastMap<String, M3UAManagementImpl> beanM3uaManagementImpls = new FastMap<String, M3UAManagementImpl>();
    private FastMap<String, DialogicMtp3UserPart> beanDialogicImpls = new FastMap<String, DialogicMtp3UserPart>();
    private M3UAShellExecutor beanM3uaShellExecutor;
    private FastMap<String, M3uaManagementJmx> beanM3uaManagementJmxs = new FastMap<String, M3uaManagementJmx>();

    private Scheduler schedulerMBean = null;

    private FastMap<String, ISUPStack> beanISUPStacks = new FastMap<String, ISUPStack>();
    private FastMap<String, SccpStackImpl> beanSccpStacks = new FastMap<String, SccpStackImpl>();
    private SccpExecutor beanSccpExecutor;
    private FastMap<String, SccpManagementJmx> beanSccpManagementJmxs = new FastMap<String, SccpManagementJmx>();
    private FastMap<String, TCAPStackImpl> beanTcapStacks = new FastMap<String, TCAPStackImpl>();
    private TCAPExecutor beanTcapExecutor;
    private FastMap<String, TcapManagementJmx> beanTcapManagementJmxs = new FastMap<String, TcapManagementJmx>();
    private FastMap<String, MAPStackImpl> beanMapStacks = new FastMap<String, MAPStackImpl>();
    private FastMap<String, CAPStackImpl> beanCapStacks = new FastMap<String, CAPStackImpl>();

    private FastMap<String, SS7Service> beanSS7Services = new FastMap<String, SS7Service>();

    private ShellServer shellExecutorMBean = null;
    private Ss7Management ss7ManagementMBean = null;
    private AlarmProvider restcommAlarmManagementMBean = null;
    private CounterProviderManagement restcommStatisticManagementMBean = null;

    public void setModel(ModelNode model) {
        this.fullModel = model;
    }

    private ModelNode peek(ModelNode node, String... args) {
        for (String arg : args) {
            if (!node.hasDefined(arg)) {
                return null;
            }
            node = node.get(arg);
        }
        return node;
    }

    private String getPropertyString(String mbeanName, String propertyName, String defaultValue) {
        String result = defaultValue;
        ModelNode propertyNode = peek(fullModel, "mbean", mbeanName, "property", propertyName);
        if (propertyNode != null && propertyNode.isDefined()) {
            // log.debug("propertyNode: "+propertyNode);
            // todo: test TYPE?
            result = propertyNode.get("value").asString();
        }
        return (result == null) ? defaultValue : result;
    }

    private int getPropertyInt(String mbeanName, String propertyName, int defaultValue) {
        int result = defaultValue;
        ModelNode propertyNode = peek(fullModel, "mbean", mbeanName, "property", propertyName);
        if (propertyNode != null && propertyNode.isDefined()) {
            // log.debug("propertyNode: "+propertyNode);
            // todo: test TYPE?
            result = propertyNode.get("value").asInt();
        }
        return result;
    }

    private boolean getPropertyBoolean(String mbeanName, String propertyName, boolean defaultValue) {
        boolean result = defaultValue;
        ModelNode propertyNode = peek(fullModel, "mbean", mbeanName, "property", propertyName);
        if (propertyNode != null && propertyNode.isDefined()) {
            result = propertyNode.get("value").asBoolean();
        }
        return result;
    }

    @Override
    public SS7ExtensionService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    @Override
    public void start(StartContext context) throws StartException {
        log.info("Starting SS7ExtensionService");

        String dataDir = pathManagerInjector.getValue().getPathEntry(DATA_DIR).resolvePath();
        log.info("dataDir: " + dataDir);

        createMtp3UserParts(dataDir);

        // DefaultClock
        DefaultClock ss7Clock = null;
        try {
            ss7Clock = new DefaultClock();
        } catch (Exception e) {
            throw new StartException("SS7Clock MBean creating is failed: " + e.getMessage(), e);
        }

        // Scheduler
        schedulerMBean = null;
        try {
            schedulerMBean = new Scheduler();
            schedulerMBean.setClock(ss7Clock);
        } catch (Exception e) {
            throw new StartException("SS7Scheduler MBean creating is failed: " + e.getMessage(), e);
        }

        createPayloadParts(dataDir);
        System.out.println("SS7 shellExecutorExists():" + shellExecutorExists());
        if(shellExecutorExists()) {
            shellExecutorMBean = null;
            try {
                FastList<ShellExecutor> shellExecutors = new FastList<ShellExecutor>();
                shellExecutors.add(beanSccpExecutor);
                shellExecutors.add(beanM3uaShellExecutor);
                shellExecutors.add(beanSctpShellExecutor);
                shellExecutors.add(beanTcapExecutor);

                String address = getPropertyString("ShellExecutor", "address", "127.0.0.1");
                int port = getPropertyInt("ShellExecutor", "port", 3435);
                String securityDomain = getPropertyString("ShellExecutor", "securityDomain", "jmx-console");

                shellExecutorMBean = new ShellServerWildFly(schedulerMBean, shellExecutors);
                shellExecutorMBean.setAddress(address);
                shellExecutorMBean.setPort(port);
                shellExecutorMBean.setSecurityDomain(securityDomain);
            } catch (Exception e) {
                throw new StartException("ShellExecutor MBean creating is failed: " + e.getMessage(), e);
            }
        }

        // Ss7Management
        try {
            ss7ManagementMBean = new Ss7Management();
            ss7ManagementMBean.setAgentId(getPropertyString("Ss7Management", "agentId", "jboss"));
            registerMBean(ss7ManagementMBean, "org.mobicents.ss7:service=Ss7Management");
        } catch (Exception e) {
            throw new StartException("Ss7Management MBean creating is failed: " + e.getMessage(), e);
        }

        // AlarmProvider
        try {
            restcommAlarmManagementMBean = new AlarmProvider(ss7ManagementMBean, ss7ManagementMBean);
        } catch (Exception e) {
            throw new StartException("AlarmProvider MBean creating is failed: " + e.getMessage(), e);
        }

        // CounterProviderManagement
        try {
            restcommStatisticManagementMBean = new CounterProviderManagement(ss7ManagementMBean);
            restcommStatisticManagementMBean.setPersistDir(dataDir);
        } catch (Exception e) {
            throw new StartException("CounterProviderManagement MBean creating is failed: " + e.getMessage(), e);
        }

        // SctpManagementJmx
        for (FastMap.Entry<String, Management> n = beanSctpManagements.head(), end = beanSctpManagements.tail(); (n = n
                .getNext()) != end;) {
            String beanName = n.getKey();
            Management sctpManagement = n.getValue();

            try {
                SctpManagementJmx restcommSctpManagementMBean = new SctpManagementJmx(ss7ManagementMBean, sctpManagement);
                this.beanSctpManagementJmxs.put(beanName, restcommSctpManagementMBean);
            } catch (Exception e) {
                throw new StartException("SctpManagementJmx MBean creating is failed: beanName=" + beanName + ", "
                        + e.getMessage(), e);
            }
        }

        // M3uaManagementJmx
        for (FastMap.Entry<String, M3UAManagementImpl> n = beanM3uaManagementImpls.head(), end = beanM3uaManagementImpls.tail(); (n = n
                .getNext()) != end;) {
            String beanName = n.getKey();
            M3UAManagementImpl m3uaManagement = n.getValue();

            try {
                M3uaManagementJmx restcommM3uaManagementMBean = new M3uaManagementJmx(ss7ManagementMBean, m3uaManagement);
                this.beanM3uaManagementJmxs.put(beanName, restcommM3uaManagementMBean);
            } catch (Exception e) {
                throw new StartException("M3uaManagementJmx MBean creating is failed: beanName=" + beanName + ", "
                        + e.getMessage(), e);
            }
        }

        // SccpManagementJmx
        for (FastMap.Entry<String, SccpStackImpl> n = beanSccpStacks.head(), end = beanSccpStacks.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            SccpStackImpl sccpStack = n.getValue();

            try {
                SccpManagementJmx restcommSccpManagementMBean = new SccpManagementJmx(ss7ManagementMBean, sccpStack);
                this.beanSccpManagementJmxs.put(beanName, restcommSccpManagementMBean);
            } catch (Exception e) {
                throw new StartException("SccpManagementJmx MBean creating is failed: beanName=" + beanName + ", "
                        + e.getMessage(), e);
            }
        }

        // TcapManagementJmx
        for (FastMap.Entry<String, TCAPStackImpl> n = beanTcapStacks.head(), end = beanTcapStacks.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            TCAPStackImpl tcapStack = n.getValue();

            try {
                TcapManagementJmx restcommTcapManagementMBean = new TcapManagementJmx(ss7ManagementMBean, tcapStack);
                this.beanTcapManagementJmxs.put(beanName, restcommTcapManagementMBean);
            } catch (Exception e) {
                throw new StartException("TcapManagementJmx MBean creating is failed: beanName=" + beanName + ", "
                        + e.getMessage(), e);
            }
        }

        // SCTPManagement - start
        for (FastMap.Entry<String, Management> n = beanSctpManagements.head(), end = beanSctpManagements.tail(); (n = n
                .getNext()) != end;) {
            String beanName = n.getKey();
            Management sctpManagement = n.getValue();
            try {
                sctpManagement.start();
            } catch (Exception e) {
                throw new StartException("SCTPManagement_" + beanName + " MBean starting is failed: " + e.getMessage(), e);
            }
        }

        // mtp3UserParts - start
        for (FastMap.Entry<String, Mtp3UserPart> n = beanMtp3UserParts.head(), end = beanMtp3UserParts.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            Mtp3UserPart mtp3UserPart = n.getValue();
            try {
                mtp3UserPart.start();
            } catch (Exception e) {
                throw new StartException("Mtp3UserPart_" + beanName + " MBean starting is failed: " + e.getMessage(), e);
            }
        }

        // scheduler - start
        try {
            schedulerMBean.start();
        } catch (Exception e) {
            throw new StartException("Scheduler MBean starting is failed: " + e.getMessage(), e);
        }

        // ISUP - start
        for (FastMap.Entry<String, ISUPStack> n = beanISUPStacks.head(), end = beanISUPStacks.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            ISUPStack isupStack = n.getValue();
            try {
                isupStack.start();
            } catch (Exception e) {
                throw new StartException("ISUPStack_" + beanName + " MBean starting is failed: " + e.getMessage(), e);
            }
        }

        // SCCP - start
        for (FastMap.Entry<String, SccpStackImpl> n = beanSccpStacks.head(), end = beanSccpStacks.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            SccpStackImpl sccpStack = n.getValue();
            try {
                sccpStack.start();
            } catch (Exception e) {
                throw new StartException("SCCPStack_" + beanName + " MBean starting is failed: " + e.getMessage(), e);
            }
        }

        // TCAP - start
        for (FastMap.Entry<String, TCAPStackImpl> n = beanTcapStacks.head(), end = beanTcapStacks.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            TCAPStackImpl tcapStack = n.getValue();
            try {
                tcapStack.start();
            } catch (Exception e) {
                throw new StartException("TCAPStack_" + beanName + " MBean starting is failed: " + e.getMessage(), e);
            }
        }

        // MAP - start
        for (FastMap.Entry<String, MAPStackImpl> n = beanMapStacks.head(), end = beanMapStacks.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            MAPStackImpl mapStack = n.getValue();
            try {
                mapStack.start();
            } catch (Exception e) {
                throw new StartException("MAPStack_" + beanName + " MBean starting is failed: " + e.getMessage(), e);
            }
        }

        // CAP - start
        for (FastMap.Entry<String, CAPStackImpl> n = beanCapStacks.head(), end = beanCapStacks.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            CAPStackImpl capStack = n.getValue();
            try {
                capStack.start();
            } catch (Exception e) {
                throw new StartException("CAPStack_" + beanName + " MBean starting is failed: " + e.getMessage(), e);
            }
        }

        // Starting of general beans
        try {
            if(shellExecutorMBean != null) {
                shellExecutorMBean.start();
            }
            ss7ManagementMBean.start();
            restcommAlarmManagementMBean.start();
            restcommStatisticManagementMBean.start();
        } catch (Exception e) {
            throw new StartException("Genaral MBean starting is failed: " + e.getMessage(), e);
        }

        // Services - start
        for (FastMap.Entry<String, SS7Service> n = beanSS7Services.head(), end = beanSS7Services.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            SS7Service ss7Service = n.getValue();
            try {
                ss7Service.create();
                ss7Service.start();
            } catch (Exception e) {
                throw new StartException("SS7Service_" + beanName + " MBean starting is failed: " + e.getMessage(), e);
            }
        }

        // SctpManagementMBean - start
        for (FastMap.Entry<String, SctpManagementJmx> n = beanSctpManagementJmxs.head(), end = beanSctpManagementJmxs.tail(); (n = n
                .getNext()) != end;) {
            String beanName = n.getKey();
            SctpManagementJmx sctpManagementJmx = n.getValue();
            try {
                sctpManagementJmx.start();
            } catch (Exception e) {
                throw new StartException("SCTPManagementJmx_" + beanName + " MBean starting is failed: " + e.getMessage(), e);
            }
        }

        // M3uaManagementMBean - start
        for (FastMap.Entry<String, M3uaManagementJmx> n = beanM3uaManagementJmxs.head(), end = beanM3uaManagementJmxs.tail(); (n = n
                .getNext()) != end;) {
            String beanName = n.getKey();
            M3uaManagementJmx m3uaManagementJmx = n.getValue();
            try {
                m3uaManagementJmx.start();
            } catch (Exception e) {
                throw new StartException("M3uaManagementJmx_" + beanName + " MBean starting is failed: " + e.getMessage(), e);
            }
        }

        // SccpManagementMBean - start
        for (FastMap.Entry<String, SccpManagementJmx> n = beanSccpManagementJmxs.head(), end = beanSccpManagementJmxs.tail(); (n = n
                .getNext()) != end;) {
            String beanName = n.getKey();
            SccpManagementJmx sccpManagementJmx = n.getValue();
            try {
                sccpManagementJmx.start();
            } catch (Exception e) {
                throw new StartException("SccpManagementJmx_" + beanName + " MBean starting is failed: " + e.getMessage(), e);
            }
        }

        // TcapManagementMBean - start
        for (FastMap.Entry<String, TcapManagementJmx> n = beanTcapManagementJmxs.head(), end = beanTcapManagementJmxs.tail(); (n = n
                .getNext()) != end;) {
            String beanName = n.getKey();
            TcapManagementJmx tcapManagementJmx = n.getValue();
            try {
                tcapManagementJmx.start();
            } catch (Exception e) {
                throw new StartException("TcapManagementJmx_" + beanName + " MBean starting is failed: " + e.getMessage(), e);
            }
        }
    }

    private boolean shellExecutorExists() {
        ModelNode shellExecutorNode = peek(fullModel, "mbean", "ShellExecutor");
        return shellExecutorNode != null;
    }

    @Override
    public void stop(StopContext context) {
        log.info("Stopping SS7ExtensionService");

        // TcapManagementMBean - stop
        for (FastMap.Entry<String, TcapManagementJmx> n = beanTcapManagementJmxs.head(), end = beanTcapManagementJmxs.tail(); (n = n
                .getNext()) != end;) {
            String beanName = n.getKey();
            TcapManagementJmx tcapManagementJmx = n.getValue();
            try {
                tcapManagementJmx.stop();
            } catch (Exception e) {
                log.warn("TcapManagementJmx_" + beanName + " MBean stopping is failed: " + e);
            }
        }

        // SccpManagementMBean - stop
        for (FastMap.Entry<String, SccpManagementJmx> n = beanSccpManagementJmxs.head(), end = beanSccpManagementJmxs.tail(); (n = n
                .getNext()) != end;) {
            String beanName = n.getKey();
            SccpManagementJmx sccpManagementJmx = n.getValue();
            try {
                sccpManagementJmx.stop();
            } catch (Exception e) {
                log.warn("SccpManagementJmx_" + beanName + " MBean stopping is failed: " + e);
            }
        }

        // M3uaManagementMBean - stop
        for (FastMap.Entry<String, M3uaManagementJmx> n = beanM3uaManagementJmxs.head(), end = beanM3uaManagementJmxs.tail(); (n = n
                .getNext()) != end;) {
            String beanName = n.getKey();
            M3uaManagementJmx m3uaManagementJmx = n.getValue();
            try {
                m3uaManagementJmx.stop();
            } catch (Exception e) {
                log.warn("M3uaManagementJmx_" + beanName + " MBean stopping is failed: " + e);
            }
        }

        // SctpManagementMBean - stop
        for (FastMap.Entry<String, SctpManagementJmx> n = beanSctpManagementJmxs.head(), end = beanSctpManagementJmxs.tail(); (n = n
                .getNext()) != end;) {
            String beanName = n.getKey();
            SctpManagementJmx sctpManagementJmx = n.getValue();
            try {
                sctpManagementJmx.stop();
            } catch (Exception e) {
                log.warn("SCTPManagementJmx_" + beanName + " MBean stopping is failed: " + e);
            }
        }

        // Services - stop
        for (FastMap.Entry<String, SS7Service> n = beanSS7Services.head(), end = beanSS7Services.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            SS7Service ss7Service = n.getValue();
            try {
                ss7Service.stop();
            } catch (Exception e) {
                log.warn("SS7Service_" + beanName + " MBean stopping is failed: " + e);
            }
        }

        // stopping of general beans
        try {
            if (restcommStatisticManagementMBean != null)
                restcommStatisticManagementMBean.stop();
            if (restcommAlarmManagementMBean != null)
                restcommAlarmManagementMBean.stop();
            if (ss7ManagementMBean != null)
                ss7ManagementMBean.stop();
            if (shellExecutorMBean != null)
                shellExecutorMBean.stop();
        } catch (Exception e) {
            log.warn("Genaral MBean stopping is failed: " + e);
        }

        // CAP - stop
        for (FastMap.Entry<String, CAPStackImpl> n = beanCapStacks.head(), end = beanCapStacks.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            CAPStackImpl capStack = n.getValue();
            try {
                capStack.stop();
            } catch (Exception e) {
                log.warn("CAPStack_" + beanName + " MBean stopping is failed: " + e);
            }
        }

        // MAP - stop
        for (FastMap.Entry<String, MAPStackImpl> n = beanMapStacks.head(), end = beanMapStacks.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            MAPStackImpl mapStack = n.getValue();
            try {
                mapStack.stop();
            } catch (Exception e) {
                log.warn("MAPStack_" + beanName + " MBean stopping is failed: " + e);
            }
        }

        // TCAP - stop
        for (FastMap.Entry<String, TCAPStackImpl> n = beanTcapStacks.head(), end = beanTcapStacks.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            TCAPStackImpl tcapStack = n.getValue();
            try {
                tcapStack.stop();
            } catch (Exception e) {
                log.warn("TCAPStack_" + beanName + " MBean stopping is failed: " + e);
            }
        }

        // SCCP - stop
        for (FastMap.Entry<String, SccpStackImpl> n = beanSccpStacks.head(), end = beanSccpStacks.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            SccpStackImpl sccpStack = n.getValue();
            try {
                sccpStack.stop();
            } catch (Exception e) {
                log.warn("SCCPStack_" + beanName + " MBean stopping is failed: " + e);
            }
        }

        // ISUP - stop
        for (FastMap.Entry<String, ISUPStack> n = beanISUPStacks.head(), end = beanISUPStacks.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            ISUPStack isupStack = n.getValue();
            try {
                isupStack.stop();
            } catch (Exception e) {
                log.warn("ISUPStack_" + beanName + " MBean stopping is failed: " + e);
            }
        }

        // scheduler - stop
        try {
            if (schedulerMBean != null)
                schedulerMBean.stop();
        } catch (Exception e) {
            log.warn("Scheduler MBean stopping is failed: " + e);
        }

        // mtp3UserParts - stop
        for (FastMap.Entry<String, Mtp3UserPart> n = beanMtp3UserParts.head(), end = beanMtp3UserParts.tail(); (n = n.getNext()) != end;) {
            String beanName = n.getKey();
            Mtp3UserPart mtp3UserPart = n.getValue();
            try {
                mtp3UserPart.stop();
            } catch (Exception e) {
                log.warn("Mtp3UserPart_" + beanName + " MBean stopping is failed: " + e);
            }
        }

        // SCTPManagement - stop
        for (FastMap.Entry<String, Management> n = beanSctpManagements.head(), end = beanSctpManagements.tail(); (n = n
                .getNext()) != end;) {
            String beanName = n.getKey();
            Management sctpManagement = n.getValue();
            try {
                sctpManagement.stop();
            } catch (Exception e) {
                log.warn("SCTPManagement_" + beanName + " MBean stopping is failed: " + e);
            }
        }

        while (!registeredMBeans.isEmpty()) {
            unregisterMBean(registeredMBeans.pop());
        }
    }

    private void registerMBean(Object mBean, String name) throws StartException {
        if (mBean == null) {
            return;
        }

        try {
            getMbeanServer().getValue().registerMBean(mBean, new ObjectName(name));
        } catch (Throwable e) {
            throw new StartException(e);
        }
        registeredMBeans.push(name);
    }

    private void unregisterMBean(String name) {
        try {
            getMbeanServer().getValue().unregisterMBean(new ObjectName(name));
        } catch (Throwable e) {
            log.error("failed to unregister mbean", e);
        }
    }

    private void createMtp3UserParts(String dataDir) throws StartException {
        // SCTPManagement
        ModelNode mbeansNode = peek(fullModel, "mbean");
        for (ModelNode node : mbeansNode.asList()) {
            for (Property prop : node.asPropertyList()) {
                String type = prop.getValue().get("type").asString();
                String beanName = prop.getValue().get("name").asString();

                if (type.equals("SCTPManagement")) {
                    createSctpStack(prop.getValue(), beanName, dataDir);
                }
            }
        }

        // SCTPShellExecutor
        if (beanSctpManagements.size() > 0) {
            try {
                beanSctpShellExecutor = new SCTPShellExecutor();
                beanSctpShellExecutor.setSctpManagements(beanSctpManagements);
            } catch (Exception e) {
                throw new StartException("SCTPShellExecutor MBean creating is failed: " + e.getMessage(), e);
            }
        } else {
            beanSctpShellExecutor = null;
        }

        // RoutingLabelFormat
        for (ModelNode node : mbeansNode.asList()) {
            for (Property prop : node.asPropertyList()) {
                String type = prop.getValue().get("type").asString();
                String beanName = prop.getValue().get("name").asString();

                if (type.equals("RoutingLabelFormat")) {
                    createRoutingLabelFormat(prop.getValue(), beanName);
                }
            }
        }

        // M3UAManagement
        for (ModelNode node : mbeansNode.asList()) {
            for (Property prop : node.asPropertyList()) {
                String type = prop.getValue().get("type").asString();
                String beanName = prop.getValue().get("name").asString();

                if (type.equals("M3UAManagement")) {
                    createM3uaStack(prop.getValue(), beanName, dataDir);
                }
            }
        }

        // M3UAShellExecutor
        if (beanM3uaManagementImpls.size() > 0) {
            beanM3uaShellExecutor = new M3UAShellExecutor();
            try {
                beanM3uaShellExecutor.setM3uaManagements(beanM3uaManagementImpls);
            } catch (Exception e) {
                throw new StartException("M3UAShellExecutor MBean creating is failed: " + e.getMessage(), e);
            }
        } else {
            beanM3uaShellExecutor = null;
        }

        // Dialogic
        for (ModelNode node : mbeansNode.asList()) {
            for (Property prop : node.asPropertyList()) {
                String type = prop.getValue().get("type").asString();
                String beanName = prop.getValue().get("name").asString();

                if (type.equals("DialogicMtp3UserPart")) {
                    createDialogicStack(prop.getValue(), beanName, dataDir);
                }
            }
        }
    }

    private void createPayloadParts(String dataDir) throws StartException {
        // ISUPStack
        ModelNode mbeansNode = peek(fullModel, "mbean");
        for (ModelNode node : mbeansNode.asList()) {
            for (Property prop : node.asPropertyList()) {
                String type = prop.getValue().get("type").asString();
                String beanName = prop.getValue().get("name").asString();
                if (type.equals("IsupStack")) {
                    createIsupStack(prop.getValue(), beanName, dataDir);
                }
            }
        }

        // ISUPService
        for (ModelNode node : mbeansNode.asList()) {
            for (Property prop : node.asPropertyList()) {
                String type = prop.getValue().get("type").asString();
                String beanName = prop.getValue().get("name").asString();
                if (type.equals("ISUPSS7Service")) {
                    createIsupService(prop.getValue(), beanName, dataDir);
                }
            }
        }

        // SCCPStack
        for (ModelNode node : mbeansNode.asList()) {
            for (Property prop : node.asPropertyList()) {
                String type = prop.getValue().get("type").asString();
                String beanName = prop.getValue().get("name").asString();
                if (type.equals("SccpStack")) {
                    createSccpStack(prop.getValue(), beanName, dataDir);
                }
            }
        }

        // SCCPShellExecutor
        if (beanSccpStacks.size() > 0) {
            beanSccpExecutor = new SccpExecutor();
            try {
                beanSccpExecutor.setSccpStacks(beanSccpStacks);
            } catch (Exception e) {
                throw new StartException("SccpExecutor MBean creating is failed: " + e.getMessage(), e);
            }
        } else {
            beanSccpExecutor = null;
        }

        // TCAPStack
        for (ModelNode node : mbeansNode.asList()) {
            for (Property prop : node.asPropertyList()) {
                String type = prop.getValue().get("type").asString();
                String beanName = prop.getValue().get("name").asString();
                if (type.equals("TcapStack")) {
                    createTcapStack(prop.getValue(), beanName, dataDir);
                }
            }
        }

        // TCAPShellExecutor
        if (beanTcapStacks.size() > 0) {
            beanTcapExecutor = new TCAPExecutor();
            try {
                beanTcapExecutor.setTcapStacks(beanTcapStacks);
            } catch (Exception e) {
                throw new StartException("TcapExecutor MBean creating is failed: " + e.getMessage(), e);
            }
        } else {
            beanTcapExecutor = null;
        }

        // TCAPService
        for (ModelNode node : mbeansNode.asList()) {
            for (Property prop : node.asPropertyList()) {
                String type = prop.getValue().get("type").asString();
                String beanName = prop.getValue().get("name").asString();
                if (type.equals("TCAPSS7Service")) {
                    createTcapService(prop.getValue(), beanName, dataDir);
                }
            }
        }

        // MAPStack
        for (ModelNode node : mbeansNode.asList()) {
            for (Property prop : node.asPropertyList()) {
                String type = prop.getValue().get("type").asString();
                String beanName = prop.getValue().get("name").asString();
                if (type.equals("MapStack")) {
                    createMapStack(prop.getValue(), beanName, dataDir);
                }
            }
        }

        // MAPService
        for (ModelNode node : mbeansNode.asList()) {
            for (Property prop : node.asPropertyList()) {
                String type = prop.getValue().get("type").asString();
                String beanName = prop.getValue().get("name").asString();
                if (type.equals("MAPSS7Service")) {
                    createMapService(prop.getValue(), beanName, dataDir);
                }
            }
        }

        // CAPStack
        for (ModelNode node : mbeansNode.asList()) {
            for (Property prop : node.asPropertyList()) {
                String type = prop.getValue().get("type").asString();
                String beanName = prop.getValue().get("name").asString();
                if (type.equals("CapStack")) {
                    createCapStack(prop.getValue(), beanName, dataDir);
                }
            }
        }

        // CAPService
        for (ModelNode node : mbeansNode.asList()) {
            for (Property prop : node.asPropertyList()) {
                String type = prop.getValue().get("type").asString();
                String beanName = prop.getValue().get("name").asString();
                if (type.equals("CAPSS7Service")) {
                    createCapService(prop.getValue(), beanName, dataDir);
                }
            }
        }
    }

    private void createSctpStack(ModelNode sctpNode, String beanName, String dataDir) throws StartException {
        // SCTPManagement
        ManagementImpl sctpManagement = null;
        try {
            sctpManagement = new ManagementImpl(beanName);
            sctpManagement.setPersistDir(dataDir);

            /*
            ModelNode prop = sctpNode.get("property");
            if (prop != null && prop.isDefined()) {
                List<Property> properties = prop.asPropertyList();

                for (Property property : properties) {
                    String pName = property.getName();
                    if (pName.equals("optionSctpDisableFragments")) {
                        boolean valb = property.getValue().asBoolean();
                        sctpManagement.setOptionSctpDisableFragments(valb);
                    } else if (pName.equals("optionSctpFragmentInterleave")) {
                        int vali = property.getValue().asInt();
                        sctpManagement.setOptionSctpFragmentInterleave(vali);
                    } else if (pName.equals("optionSctpNodelay")) {
                        boolean valb = property.getValue().asBoolean();
                        sctpManagement.setOptionSctpNodelay(valb);
                    } else if (pName.equals("optionSoSndbuf")) {
                        int vali = property.getValue().asInt();
                        sctpManagement.setOptionSoSndbuf(vali);
                    } else if (pName.equals("optionSoRcvbuf")) {
                        int vali = property.getValue().asInt();
                        sctpManagement.setOptionSoRcvbuf(vali);
                    } else if (pName.equals("optionSoLinger")) {
                        int vali = property.getValue().asInt();
                        sctpManagement.setOptionSoLinger(vali);
                    } else if (pName.equals("optionSctpInitMaxstreams_MaxOutStreams")) {
                        int vali = property.getValue().asInt();
                        sctpManagement.setOptionSctpInitMaxstreams_MaxOutStreams(vali);
                    } else if (pName.equals("optionSctpInitMaxstreams_MaxInStreams")) {
                        int vali = property.getValue().asInt();
                        sctpManagement.setOptionSctpInitMaxstreams_MaxInStreams(vali);
                    }
                }
            }
            */

            this.beanSctpManagements.put(beanName, sctpManagement);
        } catch (Exception e) {
            throw new StartException("SCTPManagement MBean creating is failed: name=" + beanName + ", " + e.getMessage(), e);
        }
    }

    private void createRoutingLabelFormat(ModelNode routingLabelFormatNode, String beanName) throws StartException {
        // RoutingLabelFormat
        RoutingLabelFormat routingLabelFormat = null;
        try {
            String format = getPropertyString(beanName, "format", "ITU");
            routingLabelFormat = RoutingLabelFormat.getInstance(format);
        } catch (Exception e) {
            throw new StartException("RoutingLabelFormat creating is failed: " + e.getMessage(), e);
        }

        this.routingLabelFormats.put(beanName, routingLabelFormat);
    }

    private void createM3uaStack(ModelNode m3uaNode, String beanName, String dataDir) throws StartException {
        // M3UAManagement
        String m3uaManagementPropertyName = getPropertyString(beanName, "productName", "Restcomm-jSS7");
        String transportManagementName = getPropertyString(beanName, "transportManagement", null);
        String routingLabelFormatName = getPropertyString(beanName, "routingLabelFormat", null);

        if (transportManagementName == null) {
            throw new StartException("M3UAManagement MBean creating is failed: name=" + beanName
                    + ", transportManagementName is null");
        }
        if (routingLabelFormatName == null) {
            throw new StartException("M3UAManagement MBean creating is failed: name=" + beanName
                    + ", routingLabelFormatName is null");
        }

        Management transportManagement = beanSctpManagements.get(transportManagementName);
        if (transportManagement == null) {
            throw new StartException("M3UAManagement MBean creating is failed: name=" + beanName
                    + ", transportManagement is not found: " + transportManagementName);
        }
        RoutingLabelFormat routingLabelFormat = routingLabelFormats.get(routingLabelFormatName);
        if (routingLabelFormat == null) {
            throw new StartException("M3UAManagement MBean creating is failed: name=" + beanName
                    + ", routingLabelFormat is not found: " + routingLabelFormat);
        }

        M3UAManagementImpl m3uaManagement = null;
        try {
            m3uaManagement = new M3UAManagementImpl(beanName, m3uaManagementPropertyName);
            m3uaManagement.setPersistDir(dataDir);

            m3uaManagement.setTransportManagement(transportManagement);
            m3uaManagement.setRoutingLabelFormat(routingLabelFormat);

            List<Property> properties = m3uaNode.get("property").asPropertyList();
            for (Property property : properties) {
                String pName = property.getName();
                if (pName.equals("maxSequenceNumber")) {
                    int vali = property.getValue().get("value").asInt();
                    m3uaManagement.setMaxSequenceNumber(vali);
                } else if (pName.equals("maxAsForRoute")) {
                    int vali = property.getValue().get("value").asInt();
                    m3uaManagement.setMaxAsForRoute(vali);
                } else if (pName.equals("deliveryMessageThreadCount")) {
                    int vali = property.getValue().get("value").asInt();
                    m3uaManagement.setDeliveryMessageThreadCount(vali);
                }
            }

            this.beanMtp3UserParts.put(beanName, m3uaManagement);
            this.beanM3uaManagementImpls.put(beanName, m3uaManagement);
        } catch (Exception e) {
            throw new StartException("M3UAManagement MBean creating is failed: name=" + beanName + ", " + e.getMessage(), e);
        }
    }

    private void createDialogicStack(ModelNode dialogicNode, String beanName, String dataDir) throws StartException {
        // DialogicStack
        String dialogicManagementPropertyName = getPropertyString(beanName, "productName", "Restcomm-jSS7");
        String routingLabelFormatName = getPropertyString(beanName, "routingLabelFormat", null);
        int sourceModuleId = getPropertyInt(beanName, "sourceModuleId", 61);
        int destinationModuleId = getPropertyInt(beanName, "destinationModuleId", 34);

        if (routingLabelFormatName == null) {
            throw new StartException("DialogicMtp3UserPart MBean creating is failed: name=" + beanName
                    + ", routingLabelFormatName is null");
        }

        RoutingLabelFormat routingLabelFormat = routingLabelFormats.get(routingLabelFormatName);
        if (routingLabelFormat == null) {
            throw new StartException("DialogicMtp3UserPart MBean creating is failed: name=" + beanName
                    + ", routingLabelFormat is not found: " + routingLabelFormat);
        }

        DialogicMtp3UserPart dialogicManagement = null;
        try {
            dialogicManagement = new DialogicMtp3UserPart(dialogicManagementPropertyName);
            dialogicManagement.setRoutingLabelFormat(routingLabelFormat);

            dialogicManagement.setSourceModuleId(sourceModuleId);
            dialogicManagement.setDestinationModuleId(destinationModuleId);

            this.beanMtp3UserParts.put(beanName, dialogicManagement);
            this.beanDialogicImpls.put(beanName, dialogicManagement);
        } catch (Exception e) {
            throw new StartException("DialogicMtp3UserPart MBean creating is failed: name=" + beanName + ", " + e.getMessage(), e);
        }
    }

    private void createIsupStack(ModelNode isupNode, String beanName, String dataDir) throws StartException {
        // CircuitManager
        CircuitManagerImpl circuitManager = null;
        try {
            circuitManager = new CircuitManagerImpl();
        } catch (Exception e) {
            throw new StartException("CircuitManager MBean creating is failed: name=" + beanName + ", " + e.getMessage(), e);
        }

        // ISUPStack
        String mtp3UserPartName = getPropertyString(beanName, "mtp3UserPart", null);
        int localSpc = getPropertyInt(beanName, "localSpc", 0);
        int ni = getPropertyInt(beanName, "ni", 2);

        if (mtp3UserPartName == null) {
            throw new StartException("IsupStack MBean creating is failed: name=" + beanName + ", mtp3UserPartName is null");
        }
        Mtp3UserPart mtp3UserPart = beanMtp3UserParts.get(mtp3UserPartName);
        if (mtp3UserPart == null) {
            throw new StartException("IsupStack MBean creating is failed: name=" + beanName + ", mtp3UserPart is not found"
                    + mtp3UserPartName);
        }

        ISUPStackImpl isupStack = null;
        try {
            isupStack = new ISUPStackImpl(schedulerMBean, localSpc, ni);
            isupStack.setMtp3UserPart(mtp3UserPart);
            isupStack.setCircuitManager(circuitManager);

            this.beanISUPStacks.put(beanName, isupStack);
        } catch (Exception e) {
            throw new StartException("IsupStack MBean creating is failed: name=" + beanName + ", " + e.getMessage(), e);
        }
    }

    private void createIsupService(ModelNode serviceNode, String beanName, String dataDir) throws StartException {
        String isupServiceName = getPropertyString(beanName, "serviceName", "ISUP");
        String isupStackName = getPropertyString(beanName, "isupStack", null);
        String jndiName = getPropertyString(beanName, "jndiName", "java:/restcomm/ss7/isup");

        if (isupStackName == null) {
            throw new StartException("IsupService MBean creating is failed: name=" + beanName + ", isupStackName is null");
        }
        ISUPStack isupStack = beanISUPStacks.get(isupStackName);
        if (isupStack == null) {
            throw new StartException("IsupService MBean creating is failed: name=" + beanName + ", isupStack is not found"
                    + isupStackName);
        }

        try {
            SS7Service isupSS7Service = new SS7Service(isupServiceName);
//            isupSS7Service.setJndiName(jndiName);
            isupSS7Service.setStack(isupStack.getIsupProvider());

            this.beanSS7Services.put(beanName, isupSS7Service);
            registerMBean(isupSS7Service, "org.mobicents.ss7:service=" + beanName);
        } catch (Exception e) {
            throw new StartException("IsupService MBean creating is failed: name=" + beanName + ", " + e.getMessage(), e);
        }
    }

    private void createSccpStack(ModelNode sccpNode, String beanName, String dataDir) throws StartException {
        // SccpStack
        FastMap<Integer, Mtp3UserPart> mtp3UserPartsTemp = new FastMap<Integer, Mtp3UserPart>();

        // ModelNode mtp3UserPartsNode = sccpNode.get("mtp3UserParts");
        ModelNode mtp3UserPartsNode = peek(fullModel, "mbean", beanName, "property", "mtp3UserParts");

        if (mtp3UserPartsNode != null) {
            for (Property prop : mtp3UserPartsNode.get("entry").asPropertyList()) {
                int key = prop.getValue().get("key").asInt();
                String mtp3UserPartsName = prop.getValue().get("value").asString();
                Mtp3UserPart mtp3UserPart = beanMtp3UserParts.get(mtp3UserPartsName);
                if (mtp3UserPart == null) {
                    throw new StartException("SccpService MBean creating is failed: name=" + beanName
                            + ", mtp3UserPart is not found" + mtp3UserPartsName);
                }
                mtp3UserPartsTemp.put(key, mtp3UserPart);
            }
        }

        SccpStackImpl sccpStack = null;
        try {
            sccpStack = new SccpStackImpl(beanName);
            sccpStack.setPersistDir(dataDir);
            sccpStack.setMtp3UserParts(mtp3UserPartsTemp);

            this.beanSccpStacks.put(beanName, sccpStack);
        } catch (Exception e) {
            throw new StartException("SccpStack MBean creating is failed: name=" + beanName + ", " + e.getMessage(), e);
        }
    }

    private void createTcapStack(ModelNode tcapNode, String beanName, String dataDir) throws StartException {
        // TcapStack
        String sccpStackName = getPropertyString(beanName, "sccpStack", null);
        if (sccpStackName == null) {
            throw new StartException("SccpStack MBean creating is failed: name=" + beanName + ", sccpStackName is null");
        }
        SccpStackImpl sccpStack = beanSccpStacks.get(sccpStackName);
        if (sccpStack == null) {
            throw new StartException("SccpStack MBean creating is failed: name=" + beanName + ", sccpStack is not found"
                    + sccpStackName);
        }

        int ssn = getPropertyInt(beanName, "ssn", 8);
        boolean previewMode = getPropertyBoolean(beanName, "previewMode", false);

        ModelNode extraSsnsNode = peek(fullModel, "mbean", beanName, "property", "extraSsns");
        List<Integer> extraSsnsNew = new FastList<Integer>();

        if (extraSsnsNode != null) {
            for (Property prop : extraSsnsNode.get("entry").asPropertyList()) {
                int key = prop.getValue().get("key").asInt();
                int ssnVal = prop.getValue().get("value").asInt();
                extraSsnsNew.add(ssnVal);
            }
        }

        TCAPStackImpl tcapStack = null;
        try {
            tcapStack = new TCAPStackImpl(beanName, sccpStack.getSccpProvider(), ssn);
            tcapStack.setPersistDir(dataDir);
            tcapStack.setPreviewMode(previewMode);
            if (extraSsnsNew != null && extraSsnsNew.size() > 0) {
                tcapStack.setExtraSsns(extraSsnsNew);
            }

            this.beanTcapStacks.put(beanName, tcapStack);
        } catch (Exception e) {
            throw new StartException("TcapStack MBean creating is failed: name=" + beanName + ", " + e.getMessage(), e);
        }
    }

    private void createTcapService(ModelNode serviceNode, String beanName, String dataDir) throws StartException {
        String tcapServiceName = getPropertyString(beanName, "serviceName", "TCAP");
        String tcapStackName = getPropertyString(beanName, "tcapStack", null);
        String jndiName = getPropertyString(beanName, "jndiName", "java:/restcomm/ss7/tcap");

        if (tcapStackName == null) {
            throw new StartException("TcapService MBean creating is failed: name=" + beanName + ", tcapStackName is null");
        }
        TCAPStack tcapStack = beanTcapStacks.get(tcapStackName);
        if (tcapStack == null) {
            throw new StartException("TcapService MBean creating is failed: name=" + beanName + ", tcapStack is not found"
                    + tcapStack);
        }

        try {
            SS7Service tcapSS7Service = new SS7Service(tcapServiceName);
//            tcapSS7Service.setJndiName(jndiName);
            tcapSS7Service.setStack(tcapStack.getProvider());

            this.beanSS7Services.put(beanName, tcapSS7Service);
            registerMBean(tcapSS7Service, "org.mobicents.ss7:service=" + beanName);
        } catch (Exception e) {
            throw new StartException("TcapService MBean creating is failed: name=" + beanName + ", " + e.getMessage(), e);
        }
    }

    private void createMapStack(ModelNode mapNode, String beanName, String dataDir) throws StartException {
        // MapStack
        String mapStackName = getPropertyString(beanName, "stackName", "MapStack");

        String tcapStackName = getPropertyString(beanName, "tcapStack", null);
        if (tcapStackName == null) {
            throw new StartException("MapStack MBean creating is failed: name=" + beanName + ", tcapStackName is null");
        }
        TCAPStackImpl tcapStack = beanTcapStacks.get(tcapStackName);
        if (tcapStack == null) {
            throw new StartException("MapStack MBean creating is failed: name=" + beanName + ", tcapStack is not found"
                    + tcapStack);
        }

        MAPStackImpl mapStack = null;
        try {
            mapStack = new MAPStackImpl(mapStackName, tcapStack.getProvider());
            this.beanMapStacks.put(beanName, mapStack);
        } catch (Exception e) {
            throw new StartException("MapService MBean creating is failed: name=" + beanName + ", " + e.getMessage(), e);
        }
    }

    private void createMapService(ModelNode serviceNode, String beanName, String dataDir) throws StartException {
        String mapServiceName = getPropertyString(beanName, "serviceName", "MAP");
        String mapStackName = getPropertyString(beanName, "mapStack", null);
        String jndiName = getPropertyString(beanName, "jndiName", "java:/restcomm/ss7/map");

        if (mapStackName == null) {
            throw new StartException("MapService MBean creating is failed: name=" + beanName + ", mapStackName is null");
        }
        MAPStack mapStack = beanMapStacks.get(mapStackName);
        if (mapStack == null) {
            throw new StartException("MapService MBean creating is failed: name=" + beanName + ", mapStack is not found"
                    + mapStack);
        }

        try {
            SS7Service mapSS7Service = new SS7Service(mapServiceName);
//            mapSS7Service.setJndiName(jndiName);
            mapSS7Service.setStack(mapStack.getMAPProvider());

            this.beanSS7Services.put(beanName, mapSS7Service);
            registerMBean(mapSS7Service, "org.mobicents.ss7:service=" + beanName);
        } catch (Exception e) {
            throw new StartException("MapService MBean creating is failed: name=" + beanName + ", " + e.getMessage(), e);
        }
    }

    private void createCapStack(ModelNode capNode, String beanName, String dataDir) throws StartException {
        // CapStack
        String capStackName = getPropertyString(beanName, "stackName", "CapStack");

        String tcapStackName = getPropertyString(beanName, "tcapStack", null);
        if (tcapStackName == null) {
            throw new StartException("CapStack MBean creating is failed: name=" + beanName + ", tcapStackName is null");
        }
        TCAPStackImpl tcapStack = beanTcapStacks.get(tcapStackName);
        if (tcapStack == null) {
            throw new StartException("CapStack MBean creating is failed: name=" + beanName + ", tcapStack is not found"
                    + tcapStack);
        }

        CAPStackImpl capStack = null;
        try {
            capStack = new CAPStackImpl(capStackName, tcapStack.getProvider());

            this.beanCapStacks.put(beanName, capStack);
        } catch (Exception e) {
            throw new StartException("CapStack MBean creating is failed: name=" + beanName + ", " + e.getMessage(), e);
        }
    }

    private void createCapService(ModelNode serviceNode, String beanName, String dataDir) throws StartException {
        String capServiceName = getPropertyString(beanName, "serviceName", "CAP");
        String capStackName = getPropertyString(beanName, "capStack", null);
        String jndiName = getPropertyString(beanName, "jndiName", "java:/restcomm/ss7/cap");

        if (capStackName == null) {
            throw new StartException("CapService MBean creating is failed: name=" + beanName + ", capStackName is null");
        }
        CAPStack capStack = beanCapStacks.get(capStackName);
        if (capStack == null) {
            throw new StartException("CapService MBean creating is failed: name=" + beanName + ", capStack is not found"
                    + capStack);
        }

        try {
            SS7Service capSS7Service = new SS7Service(capServiceName);
//            capSS7Service.setJndiName(jndiName);
            capSS7Service.setStack(capStack.getCAPProvider());

            this.beanSS7Services.put(beanName, capSS7Service);
            registerMBean(capSS7Service, "org.mobicents.ss7:service=" + beanName);
        } catch (Exception e) {
            throw new StartException("CapService MBean creating is failed: name=" + beanName + ", " + e.getMessage(), e);
        }
    }

    @Override
    public ShellExecutor getBeanSctpShellExecutor() {
        return beanSctpShellExecutor;
    }

    @Override
    public ShellExecutor getBeanM3uaShellExecutor() {
        return beanM3uaShellExecutor;
    }

    @Override
    public ShellExecutor getBeanSccpExecutor() {
        return beanSccpExecutor;
    }

    @Override
    public ShellExecutor getBeanTcapExecutor() {
        return beanTcapExecutor;
    }

    @Override
    public Ss7Management getSs7Management() {
        return ss7ManagementMBean;
    }
}
