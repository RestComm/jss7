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

package org.mobicents.protocols.ss7.map.api.service.oam;

/**
 *
 TraceEventList ::= SEQUENCE { msc-s-List [0] MSC-S-EventList OPTIONAL, mgw-List [1] MGW-EventList OPTIONAL, sgsn-List [2]
 * SGSN-EventList OPTIONAL, ggsn-List [3] GGSN-EventList OPTIONAL, bmsc-List [4] BMSC-EventList OPTIONAL, ..., mme-List [5]
 * MME-EventList OPTIONAL, sgw-List [6] SGW-EventList OPTIONAL, pgw-List [7] PGW-EventList OPTIONAL}
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface TraceEventList {

    MSCSEventList getMscSList();

    MGWEventList getMgwList();

    SGSNEventList getSgsnList();

    GGSNEventList getGgsnList();

    BMSCEventList getBmscList();

    MMEEventList getMmeList();

    SGWEventList getSgwList();

    PGWEventList getPgwList();

}
