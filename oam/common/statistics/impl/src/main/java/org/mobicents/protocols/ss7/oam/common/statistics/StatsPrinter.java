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
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterDefSet;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterType;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterValue;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterValueSet;

/**
 * @author amit bhayani
 *
 */
public class StatsPrinter {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String NAME_VALUE_SEPARATOR = " -> ";
    private static final String NAME = "Name";
    private static final String COUNTER_SET_NAME = "CounterSetName";
    private static final String DURATION = "Duration";
    private static final String START_TIME = "StartTime";
    private static final String END_TIME = "EndTime";
    private static final String START_CURLY_BRACKETS = "{";
    private static final String END_CURLY_BRACKETS = "}";
    private static final String START_SQUARE_BRACKETS = "[";
    private static final String END_SQUARE_BRACKETS = "]";
    private static final String COMMA = ",";
    private static final String SEMI_COLON = ":";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String SINGLE_SPACE = " ";

    protected final Logger logger = Logger.getLogger(StatsPrinter.class);

    /**
     * Prints stats to log file as debug
     */
    public StatsPrinter() {
    }

    protected void printStats(CounterCampaign cc) {
        if (logger.isDebugEnabled()) {

            String name = cc.getName();
            String counterSetName = cc.getCounterSetName();
            int duration = cc.getDuration();
            boolean isShortCampaign = cc.isShortCampaign();

            CounterDefSet counterDefSet = cc.getCounterSet();
            CounterValueSet lastCounterValueSet = cc.getLastCounterValueSet();

            CounterValue[] counterValues = null;

            StringBuffer counterLog = new StringBuffer();

            counterLog.append(NEW_LINE);
            counterLog.append(NAME).append(NAME_VALUE_SEPARATOR).append(name).append(NEW_LINE);
            counterLog.append(COUNTER_SET_NAME).append(NAME_VALUE_SEPARATOR).append(counterSetName).append(NEW_LINE);
            counterLog.append(DURATION).append(NAME_VALUE_SEPARATOR).append(duration).append(NEW_LINE);

            if (lastCounterValueSet != null) {
                counterLog.append(START_TIME).append(NAME_VALUE_SEPARATOR).append(lastCounterValueSet.getStartTime())
                        .append(NEW_LINE);
                counterLog.append(END_TIME).append(NAME_VALUE_SEPARATOR).append(lastCounterValueSet.getEndTime())
                        .append(NEW_LINE);

                counterValues = lastCounterValueSet.getCounterValues();

                for (int count = 0; count < counterValues.length; count++) {
                    CounterValue counterValue = counterValues[count];
                    CounterDef counterDef = counterValue.getCounterDef();
                    String counterName = counterDef.getCounterName();
                    counterLog.append(counterName).append(NAME_VALUE_SEPARATOR);
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
                                counterLog.append(START_CURLY_BRACKETS).append(KEY).append(SEMI_COLON)
                                        .append(complexValue.getKey()).append(SINGLE_SPACE).append(VALUE).append(SEMI_COLON)
                                        .append(complexValue.getValue()).append(END_CURLY_BRACKETS);
                                if (i < (complexValues.length - 1)) {
                                    counterLog.append(COMMA);
                                }
                            }
                            counterLog.append(END_SQUARE_BRACKETS);
                            break;

                    }
                    counterLog.append(NEW_LINE);
                }
            }
            counterLog.append(NEW_LINE);

            logger.debug(counterLog.toString());
        }
    }

}
