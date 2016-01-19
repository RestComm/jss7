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

package org.mobicents.protocols.ss7.oam.common.jmxss7;

import java.util.List;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmListener;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmListenerCollection;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmMediator;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmMessage;
import org.mobicents.protocols.ss7.oam.common.alarm.CurrentAlarmList;
import org.mobicents.protocols.ss7.oam.common.alarm.CurrentAlarmListImpl;
import org.mobicents.protocols.ss7.oam.common.jmx.MBeanHostImpl;
import org.mobicents.protocols.ss7.oam.common.jmx.MBeanLayer;
import org.mobicents.protocols.ss7.oam.common.jmx.MBeanType;
import org.mobicents.protocols.ss7.oam.common.statistics.CounterProviderManagement;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterMediator;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 *
 */
public class Ss7Management extends MBeanHostImpl implements Ss7ManagementMBean, AlarmMediator, AlarmListener {
    protected final Logger logger = Logger.getLogger(Ss7Management.class.getCanonicalName());

    private AlarmListenerCollection alc = new AlarmListenerCollection();
    private List<AlarmMediator> alarmMediators = new FastList<AlarmMediator>();
    private CounterProviderManagement counterProvider;
    private FastMap<String, CounterMediator> counterMediatorsRegistered = new FastMap<String, CounterMediator>();

    public Ss7Management() {
        this.setAlarmProviderObjectPath("SS7_Alarms");
    }

    @Override
    public void start() {
        logger.info("Starting ...");

        super.start();

        counterProvider = null;
        counterMediatorsRegistered.clear();

        logger.info("Started ...");
    }

    @Override
    public void stop() {
        logger.info("Stoping ...");

        super.stop();

        logger.info("Stoped ...");
    }

    public void registerMBean(MBeanLayer layer, MBeanType type, String name, Object bean) {
        super.registerMBean(layer, type, name, bean);

        if (bean instanceof AlarmMediator) {
            AlarmMediator am = (AlarmMediator) bean;
            am.registerAlarmListener(this);
            this.registerAlarmMediator(am);
        }
        if (bean instanceof CounterProviderManagement) {
            CounterProviderManagement cp = (CounterProviderManagement) bean;
            this.counterProvider = cp;
            for (CounterMediator cm : counterMediatorsRegistered.values()) {
                cp.registerCounterMediator(cm);
            }
        }
        if (bean instanceof CounterMediator) {
            CounterMediator cm = (CounterMediator) bean;
            if (this.counterProvider != null) {
                this.counterProvider.registerCounterMediator(cm);
            }
            this.counterMediatorsRegistered.put(cm.getCounterMediatorName(), cm);
        }

        logger.info("Registered MBean: " + name);
    }

    public Object unregisterMBean(MBeanLayer layer, MBeanType type, String name) {
        Object bean = super.unregisterMBean(layer, type, name);

        if (bean instanceof AlarmMediator) {
            AlarmMediator am = (AlarmMediator) bean;
            am.unregisterAlarmListener(this);
            this.unregisterAlarmMediator(am);
        }
        if (bean instanceof CounterProviderManagement) {
            this.counterProvider = null;
        }
        if (bean instanceof CounterMediator) {
            CounterMediator cm = (CounterMediator) bean;
            if (this.counterProvider != null) {
                this.counterProvider.unRegisterCounterMediator(cm);
            }
            this.counterMediatorsRegistered.remove(cm.getCounterMediatorName());
        }

        logger.info("Unregistered MBean: " + name);

        return bean;
    }

    public void registerAlarmMediator(AlarmMediator am) {
        synchronized (this.alarmMediators) {
            this.alarmMediators.add(am);
        }
    }

    public void unregisterAlarmMediator(AlarmMediator am) {
        synchronized (this.alarmMediators) {
            this.alarmMediators.remove(am);
        }
    }

    @Override
    public void registerAlarmListener(AlarmListener listener) {
        this.alc.registerAlarmListener(listener);
    }

    @Override
    public void unregisterAlarmListener(AlarmListener listener) {
        this.alc.unregisterAlarmListener(listener);
    }

    @Override
    public String getAlarmProviderObjectPath() {
        return this.alc.getAlarmProviderObjectPath();
    }

    @Override
    public void setAlarmProviderObjectPath(String value) {
        this.alc.setAlarmProviderObjectPath(value);
    }

    @Override
    public CurrentAlarmList getCurrentAlarmList() {

        CurrentAlarmListImpl al = new CurrentAlarmListImpl();
        synchronized (this.alarmMediators) {
            for (AlarmMediator amr : this.alarmMediators) {
                CurrentAlarmList temp = amr.getCurrentAlarmList();
                for (AlarmMessage alm : temp.getCurrentAlarmList()) {
                    this.alc.prepareAlarm(alm);
                    al.addAlarm(alm);
                }
            }
        }

        al.sortAlarms();
        return al;
    }

    @Override
    public void onAlarm(AlarmMessage alarm) {
        this.alc.onAlarm(alarm);
    }
}
