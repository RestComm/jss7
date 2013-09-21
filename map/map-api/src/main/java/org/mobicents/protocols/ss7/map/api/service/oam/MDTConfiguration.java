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

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 MDT-Configuration ::= SEQUENCE { jobType JobType, areaScope AreaScope OPTIONAL, listOfMeasurements ListOfMeasurements
 * OPTIONAL, reportingTrigger [0] ReportingTrigger OPTIONAL, reportInterval ReportInterval OPTIONAL, reportAmount [1]
 * ReportAmount OPTIONAL, eventThresholdRSRP EventThresholdRSRP OPTIONAL, eventThresholdRSRQ [2] EventThresholdRSRQ OPTIONAL,
 * loggingInterval [3] LoggingInterval OPTIONAL, loggingDuration [4] LoggingDuration OPTIONAL, extensionContainer [5]
 * ExtensionContainer OPTIONAL, ... }
 *
 * EventThresholdRSRP ::= INTEGER (0..97) EventThresholdRSRQ ::= INTEGER (0..34)
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface MDTConfiguration extends Serializable {

    JobType getJobType();

    AreaScope getAreaScope();

    ListOfMeasurements getListOfMeasurements();

    ReportingTrigger getReportingTrigger();

    ReportInterval getReportInterval();

    ReportAmount getReportAmount();

    Integer getEventThresholdRSRP();

    Integer getEventThresholdRSRQ();

    LoggingInterval getLoggingInterval();

    LoggingDuration getLoggingDuration();

    MAPExtensionContainer getExtensionContainer();

}
