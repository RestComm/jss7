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
package org.mobicents.protocols.ss7.sccp;

import java.util.Map;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author Amit Bhayani
 *
 */
public interface Router {

    void addRoutingAddress(int id, SccpAddress routingAddress) throws Exception;

    void removeRoutingAddress(int id) throws Exception;

    void modifyRoutingAddress(int routingAddressId, SccpAddress routingAddress) throws Exception;

    Map<Integer, SccpAddress> getRoutingAddresses();

    SccpAddress getRoutingAddress(int id);

    void addMtp3ServiceAccessPoint(int id, int mtp3Id, int opc, int ni) throws Exception;

    void modifyMtp3ServiceAccessPoint(int id, int mtp3Id, int opc, int ni) throws Exception;

    void removeMtp3ServiceAccessPoint(int id) throws Exception;

    Mtp3ServiceAccessPoint getMtp3ServiceAccessPoint(int id);

    Map<Integer, Mtp3ServiceAccessPoint> getMtp3ServiceAccessPoints();

    void addMtp3Destination(int sapId, int destId, int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask)
            throws Exception;

    void modifyMtp3Destination(int sapId, int destId, int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask)
            throws Exception;

    void removeMtp3Destination(int sapId, int destId) throws Exception;

    void addLongMessageRule(int id, int firstSpc, int lastSpc, LongMessageRuleType ruleType) throws Exception;

    void modifyLongMessageRule(int id, int firstSpc, int lastSpc, LongMessageRuleType ruleType) throws Exception;

    void removeLongMessageRule(int id) throws Exception;

    LongMessageRule getLongMessageRule(int id);

    Map<Integer, LongMessageRule> getLongMessageRules();

    void addRule(int id, RuleType ruleType, LoadSharingAlgorithm algo, OriginationType originationType,
            SccpAddress pattern, String mask, int pAddressId, int sAddressId, Integer newCallingPartyAddressAddressId)
            throws Exception;

    void modifyRule(int id, RuleType ruleType, LoadSharingAlgorithm algo, OriginationType originationType,
            SccpAddress pattern, String mask, int pAddressId, int sAddressId, Integer newCallingPartyAddressAddressId)
            throws Exception;

    Rule getRule(int id);

    void removeRule(int id) throws Exception;

    Map<Integer, Rule> getRules();
}
