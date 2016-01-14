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

    void addMtp3ServiceAccessPoint(int id, int mtp3Id, int opc, int ni, int networkId) throws Exception;

    void modifyMtp3ServiceAccessPoint(int id, int mtp3Id, int opc, int ni, int networkId) throws Exception;

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

    void addRule(int id, RuleType ruleType, LoadSharingAlgorithm algo, OriginationType originationType, SccpAddress pattern, String mask, int pAddressId,
            int sAddressId, Integer newCallingPartyAddressAddressId, int networkId) throws Exception;

    void modifyRule(int id, RuleType ruleType, LoadSharingAlgorithm algo, OriginationType originationType, SccpAddress pattern, String mask, int pAddressId,
            int sAddressId, Integer newCallingPartyAddressAddressId, int networkId) throws Exception;

    Rule getRule(int id);

    void removeRule(int id) throws Exception;

    Map<Integer, Rule> getRules();

}
