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

package org.mobicents.protocols.ss7.oam.common.statistics;

import java.util.Date;
import java.util.UUID;

import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterCampaign;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterDefSet;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterOutputFormat;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterValueSet;
import org.mobicents.protocols.ss7.oam.common.statistics.api.SourceValueSet;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
*
* @author sergey vetyutnev
*
*/
public class CounterCampaignImpl implements CounterCampaign {

    private static final long serialVersionUID = -185667602668518572L;
    private static final String NAME = "name";
    private static final String COUNTER_SET_NAME = "counterSetName";
    private static final String DURATION = "duration";
    private static final String OUTPUT_FORMAT = "outputFormat";
    private static final String SHORT_CAMPAIGN = "shortCampaign";

    private String name;
    private CounterDefSet counterSet;
    private String counterSetName;
    private int duration;
    private CounterOutputFormat outputFormat = CounterOutputFormat.VERBOSE;
    private boolean shortCampaign;

    private Date startTime;
    private UUID lastSessionId;
    private CounterValueSet lastCounterValueSet;
    private SourceValueSet lastSourceValueSet;

    public CounterCampaignImpl() {
    }

    public CounterCampaignImpl(String name, String counterSetName, CounterDefSet counterSet, int duration, boolean shortCampaign, CounterOutputFormat outputFormat) {
        this.name = name;
        this.counterSetName = counterSetName;
        this.counterSet = counterSet;
        this.duration = duration;
        if (outputFormat != null)
            this.outputFormat = outputFormat;
        else
            this.outputFormat = CounterOutputFormat.VERBOSE;
        this.shortCampaign = shortCampaign;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCounterSetName() {
        return counterSetName;
    }

    @Override
    public int getDuration() {
       return duration;
    }

    @Override
    public CounterOutputFormat getOutputFormat() {
        return outputFormat;
    }

    @Override
    public int getOutputFormatInt() {
        return outputFormat.getCode();
    }

    @Override
    public boolean isShortCampaign() {
        return shortCampaign;
    }

    @Override
    public CounterDefSet getCounterSet() {
        return counterSet;
    }

    public void setCounterSet(CounterDefSet counterSet) {
        this.counterSet = counterSet;
    }

    @Override
    public CounterValueSet getLastCounterValueSet() {
        return lastCounterValueSet;
    }

    public void setCounterValueSet(CounterValueSet val) {
        lastCounterValueSet = val;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public UUID getLastSessionId() {
        return lastSessionId;
    }

    public void setLastSessionId(UUID lastSessionId) {
        this.lastSessionId = lastSessionId;
    }

    public SourceValueSet getLastSourceValueSet() {
        return lastSourceValueSet;
    }

    public void setLastSourceValueSet(SourceValueSet lastSourceValueSet) {
        this.lastSourceValueSet = lastSourceValueSet;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CounterCampaignImpl> COUNTER_CAMPAIGN_XML = new XMLFormat<CounterCampaignImpl>(CounterCampaignImpl.class) {

        public void read(javolution.xml.XMLFormat.InputElement xml, CounterCampaignImpl counterCampaign) throws XMLStreamException {
            counterCampaign.name = xml.getAttribute(NAME, "");
            counterCampaign.counterSetName = xml.getAttribute(COUNTER_SET_NAME, "");
            counterCampaign.shortCampaign = xml.getAttribute(SHORT_CAMPAIGN, false);
            counterCampaign.duration = xml.getAttribute(DURATION, 60);

            String val = xml.getAttribute(OUTPUT_FORMAT, "VERBOSE");
            try {
                counterCampaign.outputFormat = Enum.valueOf(CounterOutputFormat.class, val);
            } catch (Exception e) {
                counterCampaign.outputFormat = CounterOutputFormat.VERBOSE;
            }
        }

        public void write(CounterCampaignImpl counterCampaign, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(NAME, counterCampaign.getName());
            xml.setAttribute(COUNTER_SET_NAME, counterCampaign.getCounterSetName());
            xml.setAttribute(SHORT_CAMPAIGN, counterCampaign.isShortCampaign());
            xml.setAttribute(DURATION, counterCampaign.getDuration());
            xml.setAttribute(OUTPUT_FORMAT, counterCampaign.getOutputFormat().toString());
        }
    };

}
