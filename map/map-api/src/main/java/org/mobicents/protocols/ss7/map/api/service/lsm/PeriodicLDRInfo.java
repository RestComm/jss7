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

package org.mobicents.protocols.ss7.map.api.service.lsm;

/**
 *
 PeriodicLDRInfo ::= SEQUENCE { reportingAmount ReportingAmount, reportingInterval ReportingInterval, ...} --
 * reportingInterval x reportingAmount shall not exceed 8639999 (99 days, 23 hours, -- 59 minutes and 59 seconds) for
 * compatibility with OMA MLP and RLP
 *
 * ReportingAmount ::= INTEGER (1..8639999)
 *
 * ReportingInterval ::= INTEGER (1..8639999) -- ReportingInterval is in seconds
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface PeriodicLDRInfo {

    int getReportingAmount();

    int getReportingInterval();

}
