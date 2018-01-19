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
package org.mobicents.protocols.ss7.oam.common.sccp;

import org.mobicents.protocols.ss7.sccp.Router;

/**
 * @author Amit Bhayani
 *
 */
public interface SccpRouterJmxMBean extends Router {

    void addRoutingAddress(int id, int ai, int pc, int ssn, int tt, int np, int nao, String digits) throws Exception;

    void addRule(int id, String ruleType, String algo, String originationType, int ai, int pc, int ssn, int tt, int np, int nao, String digits, String mask,
            int pAddressId, int sAddressId, int newCallingPartyAddressAddressId, int networkId, int callingai,
                 int callingpc, int callingssn, int callingtt, int callingnp,int callingnao, String callingdigits) throws Exception;

    void addSccpLongMessageRule(int id, int firstSpc, int lastSpc, String ruleType) throws Exception;

    void modifySccpLongMessageRule(int id, int firstSpc, int lastSpc, String ruleType) throws Exception;

    void modifySccpRoutingAddress(int id, int ai, int pc, int ssn, int tt, int np, int nao, String digits) throws Exception;

    void modifySccpRule(int id, String ruleType, String algo, String originationType, int ai, int pc, int ssn, int tt, int np, int nao, String digits, String mask,
            int pAddressId, int sAddressId, int newCallingPartyAddressAddressId, int networkId, int callingai,
            int callingpc, int callingssn, int callingtt, int callingnp,int callingnao, String callingdigits) throws Exception;

}
