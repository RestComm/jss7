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

package org.mobicents.protocols.ss7.map.api.service.lsm;

import java.io.Serializable;

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
public interface PeriodicLDRInfo extends Serializable {

    int getReportingAmount();

    int getReportingInterval();

}
