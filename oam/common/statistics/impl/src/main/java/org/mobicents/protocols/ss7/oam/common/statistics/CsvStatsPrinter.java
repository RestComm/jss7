/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.mobicents.protocols.ss7.oam.common.statistics;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.oam.common.statistics.api.ComplexValue;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterCampaign;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterDef;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterType;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterValue;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterValueSet;

import java.text.SimpleDateFormat;

/**
 * @author mniemiec
 *
 */
public class CsvStatsPrinter {

    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String VALUES_SEPARATOR = ";";
    private static final String START_CURLY_BRACKETS = "{";
    private static final String END_CURLY_BRACKETS = "}";
    private static final String START_SQUARE_BRACKETS = "[";
    private static final String END_SQUARE_BRACKETS = "]";
    private static final String COMMA = ",";
    private static final String COLON = ":";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected final Logger logger = Logger.getLogger(CsvStatsPrinter.class);

    /**
     * Prints csv stats to log file as debug
     */
    public CsvStatsPrinter() {
    }

    protected void printCsvStats(CounterCampaign cc) {
        if (logger.isDebugEnabled()) {

            String name = cc.getName();
            String counterSetName = cc.getCounterSetName();
            int duration = cc.getDuration();
            CounterValueSet lastCounterValueSet = cc.getLastCounterValueSet();
            CounterValue[] counterValues;

            StringBuffer counterLog = new StringBuffer();

            counterLog.append(NEW_LINE);
            counterLog.append(name).append(VALUES_SEPARATOR).append(counterSetName).append(VALUES_SEPARATOR);
            counterLog.append(duration);

            if (lastCounterValueSet != null) {
                counterLog.append(VALUES_SEPARATOR);
                counterLog.append(DATE_FORMAT.format(lastCounterValueSet.getStartTime())).append(VALUES_SEPARATOR);
                counterLog.append(DATE_FORMAT.format(lastCounterValueSet.getEndTime())).append(VALUES_SEPARATOR);

                counterValues = lastCounterValueSet.getCounterValues();

                for (int count = 0; count < counterValues.length; count++) {
                    CounterValue counterValue = counterValues[count];
                    CounterDef counterDef = counterValue.getCounterDef();
                    CounterType counterType = counterDef.getCounterType();
                    switch (counterType) {
                        case Summary:
                        case Minimal:
                        case Maximal:
                        case Summary_Cumulative:
                            counterLog.append(counterValue.getLongValue());
                            break;
                        case SummaryDouble:
                            counterLog.append(counterValue.getDoubleValue());
                            break;
                        case ComplexValue:
                            ComplexValue[] complexValues = counterValue.getComplexValue();
                            counterLog.append(START_SQUARE_BRACKETS);
                            for (int i = 0; i < complexValues.length; i++) {
                                ComplexValue complexValue = complexValues[i];
                                counterLog.append(START_CURLY_BRACKETS).append(complexValue.getKey())
                                        .append(COLON).append(complexValue.getValue()).append(END_CURLY_BRACKETS);
                                if (i < (complexValues.length - 1)) {
                                    counterLog.append(COMMA);
                                }
                            }
                            counterLog.append(END_SQUARE_BRACKETS);
                            break;
                    }
                    if (count < counterValues.length -1) {
                        counterLog.append(VALUES_SEPARATOR);
                    }
                }
            }
            logger.debug(counterLog.toString());
        }
    }
}
