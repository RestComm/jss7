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
package org.mobicents.protocols.ss7.m3ua.impl;

import java.util.Arrays;

import javolution.util.FastList;
import javolution.util.FastSet;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.As;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.oam.M3UAOAMMessages;
import org.mobicents.protocols.ss7.mtp.RoutingLabelFormat;

/**
 * <p>
 * Management class to manage the route.
 * </p>
 * <p>
 * The DPC, OPC and SI of Message Signaling unit (MSU) transfered by M3UA-User to M3UA layer for routing is checked against
 * configured key. If found, the corresponding {@link AsImpl} is checked for state and if ACTIVE, message will be delivered via
 * this {@link AsImpl}. If multiple {@link AsImpl} are configured and at-least 2 or more are ACTIVE, then depending on
 * {@link TrafficModeType} configured load-sharing is achieved by using SLS from received MSU.
 * </p>
 * <p>
 * For any given key (combination of DPC, OPC and SI) maximum {@link AsImpl} can be configured which acts as route for these key
 * combination.
 * </p>
 * <p>
 * Same {@link AsImpl} can serve multiple key combinations.
 * </p>
 * <p>
 * MTP3 Primitive RESUME is delivered to M3UA-User when {@link AsImpl} becomes ACTIVE and PAUSE is delivered when {@link AsImpl}
 * becomes INACTIVE
 * </p>
 *
 * @author amit bhayani
 *
 */
public class M3UARouteManagement {

    private static final Logger logger = Logger.getLogger(M3UARouteManagement.class);

    private static final String KEY_SEPARATOR = ":";
    private static final int WILDCARD = -1;

    private static final int BIT_ONE = 0x01;

    private M3UAManagementImpl m3uaManagement = null;

    private final int asSelectionMask;
    private int asSlsShiftPlaces = 0x00;

    /**
     * persists key vs corresponding As that servers for this key
     */
    protected RouteMap<String, As[]> route = new RouteMap<String, As[]>();

    /**
     * Persists DPC vs As's serving this DPC. Used for notifying M3UA-user of MTP3 primitive PAUSE, RESUME.
     */
    private FastSet<RouteRow> routeTable = new FastSet<RouteRow>();

    private int count = 0;

    // Stores the Set of AS that can route traffic (irrespective of OPC or NI)
    // for given DPC
    protected M3UARouteManagement(M3UAManagementImpl m3uaManagement) {
        this.m3uaManagement = m3uaManagement;

        RoutingLabelFormat routingLabelFormat = this.m3uaManagement.getRoutingLabelFormat();

        switch (this.m3uaManagement.getMaxAsForRoute()) {
            case 1:
            case 2:
                if (this.m3uaManagement.isUseLsbForLinksetSelection()) {
                    this.asSelectionMask = 0x01;
                    this.asSlsShiftPlaces = 0x00;
                } else {
                    this.asSelectionMask = 0x80;
                    this.asSlsShiftPlaces = 0x07;
                }
                break;
            case 3:
            case 4:
                if (this.m3uaManagement.isUseLsbForLinksetSelection()) {
                    this.asSelectionMask = 0x03;
                    this.asSlsShiftPlaces = 0x00;
                } else {
                    this.asSelectionMask = 0xc0;
                    this.asSlsShiftPlaces = 0x06;
                }
                break;
            case 5:
            case 6:
            case 7:
            case 8:
                if (this.m3uaManagement.isUseLsbForLinksetSelection()) {
                    this.asSelectionMask = 0x07;
                    this.asSlsShiftPlaces = 0x00;
                } else {
                    this.asSelectionMask = 0xe0;
                    this.asSlsShiftPlaces = 0x05;
                }
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
                if (this.m3uaManagement.isUseLsbForLinksetSelection()) {
                    this.asSelectionMask = 0x0f;
                    this.asSlsShiftPlaces = 0x00;
                } else {
                    this.asSelectionMask = 0xf0;
                    this.asSlsShiftPlaces = 0x04;
                }
                break;
            default:
                if (this.m3uaManagement.isUseLsbForLinksetSelection()) {
                    this.asSelectionMask = 0x01;
                    this.asSlsShiftPlaces = 0x00;
                } else {
                    this.asSelectionMask = 0x80;
                    this.asSlsShiftPlaces = 0x07;
                }
                break;

        }
    }

    /**
     * Reset the routeTable. Called after the persistance state of route is read from xml file.
     */
    protected void reset() {
        for (RouteMap.Entry<String, As[]> e = this.route.head(), end = this.route.tail(); (e = e.getNext()) != end;) {
            String key = e.getKey();
            As[] asList = e.getValue();

            try {
                String[] keys = key.split(KEY_SEPARATOR);
                int dpc = Integer.parseInt(keys[0]);
                for (count = 0; count < asList.length; count++) {
                    AsImpl asImpl = (AsImpl) asList[count];
                    if (asImpl != null) {
                        this.addAsToDPC(dpc, asImpl);
                    }
                }
            } catch (Exception ex) {
                logger.error(String.format("Error while adding key=%s to As list=%s", key, Arrays.toString(asList)));
            }
        }
    }

    /**
     * Creates key (combination of dpc:opc:si) and adds instance of {@link AsImpl} represented by asName as route for this key
     *
     * @param dpc
     * @param opc
     * @param si
     * @param asName
     * @throws Exception If corresponding {@link AsImpl} doesn't exist or {@link AsImpl} already added
     */
    protected void addRoute(int dpc, int opc, int si, String asName) throws Exception {
        AsImpl asImpl = null;
        for (FastList.Node<As> n = this.m3uaManagement.appServers.head(), end = this.m3uaManagement.appServers.tail(); (n = n
                .getNext()) != end;) {
            if (n.getValue().getName().compareTo(asName) == 0) {
                asImpl = (AsImpl) n.getValue();
                break;
            }
        }

        if (asImpl == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_AS_FOUND, asName));
        }

        String key = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(opc).append(KEY_SEPARATOR).append(si))
                .toString();

        As[] asArray = route.get(key);

        if (asArray != null) {
            // check is this As is already added
            for (int count = 0; count < asArray.length; count++) {
                AsImpl asTemp = (AsImpl) asArray[count];
                if (asTemp != null && asImpl.equals(asTemp)) {
                    throw new Exception(String.format("As=%s already added for dpc=%d opc=%d si=%d", asImpl.getName(), dpc,
                            opc, si));
                }
            }
        } else {
            asArray = new AsImpl[this.m3uaManagement.maxAsForRoute];
            route.put(key, asArray);
        }

        // Add to first empty slot
        for (int count = 0; count < asArray.length; count++) {
            if (asArray[count] == null) {
                asArray[count] = asImpl;
                this.m3uaManagement.store();

                this.addAsToDPC(dpc, asImpl);
                return;
            }

        }

        throw new Exception(String.format("dpc=%d opc=%d si=%d combination already has maximum possible As", dpc, opc, si));
    }

    /**
     * Removes the {@link AsImpl} from key (combination of DPC:OPC:Si)
     *
     * @param dpc
     * @param opc
     * @param si
     * @param asName
     * @throws Exception If no As found, or this As is not serving this key
     *
     */
    protected void removeRoute(int dpc, int opc, int si, String asName) throws Exception {
        AsImpl asImpl = null;
        for (FastList.Node<As> n = this.m3uaManagement.appServers.head(), end = this.m3uaManagement.appServers.tail(); (n = n
                .getNext()) != end;) {
            if (n.getValue().getName().compareTo(asName) == 0) {
                asImpl = (AsImpl) n.getValue();
                break;
            }
        }

        if (asImpl == null) {
            throw new Exception(String.format(M3UAOAMMessages.NO_AS_FOUND, asName));
        }

        String key = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(opc).append(KEY_SEPARATOR).append(si))
                .toString();

        As[] asArray = route.get(key);

        if (asArray == null) {
            throw new Exception(String.format("No AS=%s configured  for dpc=%d opc=%d si=%d", asImpl.getName(), dpc, opc, si));
        }

        for (int count = 0; count < asArray.length; count++) {
            AsImpl asTemp = (AsImpl) asArray[count];
            if (asTemp != null && asImpl.equals(asTemp)) {
                asArray[count] = null;
                this.m3uaManagement.store();

                this.removeAsFromDPC(dpc, asImpl);
                return;
            }
        }

        throw new Exception(String.format("No AS=%s configured  for dpc=%d opc=%d si=%d", asImpl.getName(), dpc, opc, si));
    }

    /**
     * <p>
     * Get {@link AsImpl} that is serving key (combination DPC:OPC:SI). It can return null if no key configured or all the
     * {@link AsImpl} are INACTIVE
     * </p>
     * <p>
     * If two or more {@link AsImpl} are active and {@link TrafficModeType} configured is load-shared, load is configured
     * between each {@link AsImpl} depending on SLS
     * </p>
     *
     * @param dpc
     * @param opc
     * @param si
     * @param sls
     * @return
     */
    protected AsImpl getAsForRoute(int dpc, int opc, int si, int sls) {
        // TODO : Loadsharing needs to be implemented

        String key = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(opc).append(KEY_SEPARATOR).append(si))
                .toString();
        As[] asArray = route.get(key);

        if (asArray == null) {
            key = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(opc).append(KEY_SEPARATOR).append(WILDCARD))
                    .toString();

            asArray = route.get(key);

            if (asArray == null) {
                key = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(WILDCARD).append(KEY_SEPARATOR)
                        .append(WILDCARD)).toString();

                asArray = route.get(key);
            }
        }

        if (asArray == null) {
            return null;
        }

        int count = (sls & this.asSelectionMask);
        count = (count >> this.asSlsShiftPlaces);

        // First attempt
        AsImpl asImpl = (AsImpl) asArray[count];
        if (this.isAsActive(asImpl)) {
            return asImpl;
        }

        // Second recursive Attempt
        for (int i = 0; i < this.m3uaManagement.getMaxAsForRoute(); i++) {
            count = count + 1;
            if (count == this.m3uaManagement.getMaxAsForRoute()) {
                // If count reaches same value as total As available for route,
                // restart from 0
                count = 0;
            }
            asImpl = (AsImpl) asArray[count];
            if (this.isAsActive(asImpl)) {
                return asImpl;
            }

        }
        return null;
    }

    private boolean isAsActive(AsImpl asImpl) {
        FSM fsm = null;
        if (asImpl != null) {
            if (asImpl.getFunctionality() == Functionality.AS
                    || (asImpl.getFunctionality() == Functionality.SGW && asImpl.getExchangeType() == ExchangeType.DE)
                    || (asImpl.getFunctionality() == Functionality.IPSP && asImpl.getExchangeType() == ExchangeType.DE)
                    || (asImpl.getFunctionality() == Functionality.IPSP && asImpl.getExchangeType() == ExchangeType.SE && asImpl
                            .getIpspType() == IPSPType.CLIENT)) {
                fsm = asImpl.getPeerFSM();
            } else {
                fsm = asImpl.getLocalFSM();
            }

            AsState asState = AsState.getState(fsm.getState().getName());

            return (asState == AsState.ACTIVE);
        }// if (as != null)
        return false;
    }

    private void addAsToDPC(int dpc, AsImpl asImpl) {
        RouteRow row = null;
        for (FastSet.Record r = routeTable.head(), end = routeTable.tail(); (r = r.getNext()) != end;) {
            RouteRow value = routeTable.valueOf(r);
            if (value.getDpc() == dpc) {
                row = value;
                break;
            }
        }

        if (row == null) {
            row = new RouteRow(dpc, this.m3uaManagement);
            this.routeTable.add(row);
        }

        row.addServedByAs(asImpl);

    }

    private void removeAsFromDPC(int dpc, AsImpl asImpl) {

        // Now decide if we should remove As from RouteRow? If the same As is
        // assigned as route for different key combination we shouldn't remove
        // it from RouteRow
        for (RouteMap.Entry<String, As[]> e = this.route.head(), end = this.route.tail(); (e = e.getNext()) != end;) {
            String key = e.getKey();
            String[] keys = key.split(KEY_SEPARATOR);
            if (keys[0].equals(Integer.toString(dpc))) {
                As[] asList = e.getValue();
                for (count = 0; count < asList.length; count++) {
                    AsImpl asTemp = (AsImpl) asList[count];
                    if (asTemp != null && asTemp.equals(asImpl)) {
                        return;
                    }
                }
            }
        }

        // We reached here means time to remove this As from RouteRow.
        RouteRow row = null;
        for (FastSet.Record r = routeTable.head(), end = routeTable.tail(); (r = r.getNext()) != end;) {
            RouteRow value = routeTable.valueOf(r);
            if (value.getDpc() == dpc) {
                row = value;
                break;
            }
        }

        if (row == null) {
            logger.error(String.format("Removing route As=%s from DPC=%d failed. No RouteRow found!", asImpl, dpc));
        } else {
            row.removeServedByAs(asImpl);
            if (row.servedByAsSize() == 0) {
                this.routeTable.remove(row);
            }
        }
    }

    public void removeAllResourses() throws Exception {
        this.route.clear();
        this.routeTable.clear();
    }
}
