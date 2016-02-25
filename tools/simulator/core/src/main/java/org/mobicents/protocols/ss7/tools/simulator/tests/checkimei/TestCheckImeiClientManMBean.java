/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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


package org.mobicents.protocols.ss7.tools.simulator.tests.checkimei;

import org.mobicents.protocols.ss7.tools.simulator.level3.MapProtocolVersion;

/**
 * @author mnowa
 *
 */
public interface TestCheckImeiClientManMBean {
    String getImei();

    void setImei(String imei);

    MapProtocolVersion getMapProtocolVersion();

    String getMapProtocolVersion_Value();

    void setMapProtocolVersion(MapProtocolVersion val);

    void putMapProtocolVersion(String val);

    CheckImeiClientAction getCheckImeiClientAction();

    String getCheckImeiClientAction_Value();

    void setCheckImeiClientAction(CheckImeiClientAction val);

    void putCheckImeiClientAction(String val);

    int getMaxConcurrentDialogs();

    void setMaxConcurrentDialogs(int val);

    boolean isOneNotificationFor100Dialogs();

    void setOneNotificationFor100Dialogs(boolean val);

    String performCheckImeiRequest(String address);

    String getCurrentRequestDef();

    String closeCurrentDialog();

}
