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

package org.mobicents.protocols.ss7.tools.simulator.management;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface TesterHostMBean {

    boolean isStarted();

    Instance_L1 getInstance_L1();

    String getInstance_L1_Value();

    void setInstance_L1(Instance_L1 val);

    Instance_L2 getInstance_L2();

    String getInstance_L2_Value();

    void setInstance_L2(Instance_L2 val);

    Instance_L3 getInstance_L3();

    String getInstance_L3_Value();

    void setInstance_L3(Instance_L3 val);

    Instance_TestTask getInstance_TestTask();

    String getInstance_TestTask_Value();

    void setInstance_TestTask(Instance_TestTask val);

    String getL1State();

    String getL2State();

    String getL3State();

    String getTestTaskState();

    void start();

    void stop();

    void quit();

    void putInstance_L1Value(String val);

    void putInstance_L2Value(String val);

    void putInstance_L3Value(String val);

    void putInstance_TestTaskValue(String val);

}
