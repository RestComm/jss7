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

package org.mobicents.protocols.ss7.cap.primitives;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.primitives.BCSMEvent;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.MonitorMode;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DpSpecificCriteria;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DpSpecificCriteriaImpl;
import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.primitives.LegIDImpl;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class BCSMEventImpl extends SequenceBase implements BCSMEvent {

    public static final int _ID_eventTypeBCSM = 0;
    public static final int _ID_monitorMode = 1;
    public static final int _ID_legID = 2;
    public static final int _ID_dpSpecificCriteria = 30;
    public static final int _ID_automaticRearm = 50;

    private static final String EVENT_TYPE_BCSM = "eventTypeBCSM";
    private static final String MONITOR_MODE = "monitorMode";
    private static final String LEG_ID = "legID";
    private static final String DP_SPECIFIC_CRITERIA = "dpSpecificCriteria";
    private static final String AUTOMATIC_REARM = "automaticRearm";

    private EventTypeBCSM eventTypeBCSM;
    private MonitorMode monitorMode;
    private LegID legID;
    private DpSpecificCriteria dpSpecificCriteria;
    private boolean automaticRearm;

    public BCSMEventImpl() {
        super("BCSMEvent");
    }

    public BCSMEventImpl(EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegID legID,
            DpSpecificCriteria dpSpecificCriteria, boolean automaticRearm) {
        super("BCSMEvent");
        this.eventTypeBCSM = eventTypeBCSM;
        this.monitorMode = monitorMode;
        this.legID = legID;
        this.dpSpecificCriteria = dpSpecificCriteria;
        this.automaticRearm = automaticRearm;
    }

    @Override
    public EventTypeBCSM getEventTypeBCSM() {
        return eventTypeBCSM;
    }

    @Override
    public MonitorMode getMonitorMode() {
        return monitorMode;
    }

    @Override
    public LegID getLegID() {
        return legID;
    }

    @Override
    public DpSpecificCriteria getDpSpecificCriteria() {
        return dpSpecificCriteria;
    }

    @Override
    public boolean getAutomaticRearm() {
        return automaticRearm;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, MAPParsingComponentException,
            IOException, AsnException, INAPParsingComponentException {

        this.eventTypeBCSM = null;
        this.monitorMode = null;
        this.legID = null;
        this.dpSpecificCriteria = null;
        this.automaticRearm = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();
            int i1;

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_eventTypeBCSM:
                        i1 = (int) ais.readInteger();
                        this.eventTypeBCSM = EventTypeBCSM.getInstance(i1);
                        break;
                    case _ID_monitorMode:
                        i1 = (int) ais.readInteger();
                        this.monitorMode = MonitorMode.getInstance(i1);
                        break;
                    case _ID_legID:
                        this.legID = new LegIDImpl();
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        ((LegIDImpl) this.legID).decodeAll(ais2);
                        break;
                    case _ID_dpSpecificCriteria:
                        ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        this.dpSpecificCriteria = new DpSpecificCriteriaImpl();
                        ((DpSpecificCriteriaImpl) this.dpSpecificCriteria).decodeAll(ais2);
                        break;
                    case _ID_automaticRearm:
                        ais.readNull();
                        this.automaticRearm = true;
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.eventTypeBCSM == null || this.monitorMode == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": eventTypeBCSM and monitorMode are mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeData(AsnOutputStream aos) throws CAPException {

        try {
            if (this.eventTypeBCSM == null || this.monitorMode == null)
                throw new CAPException("Error while encoding " + _PrimitiveName
                        + ": eventTypeBCSM and monitorMode must not be null");

            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_eventTypeBCSM, this.eventTypeBCSM.getCode());
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_monitorMode, this.monitorMode.getCode());

            if (this.legID != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_legID);
                int pos = aos.StartContentDefiniteLength();
                ((LegIDImpl) this.legID).encodeAll(aos);
                aos.FinalizeContent(pos);
            }

            if (this.dpSpecificCriteria != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_dpSpecificCriteria);
                int pos = aos.StartContentDefiniteLength();
                ((DpSpecificCriteriaImpl) this.dpSpecificCriteria).encodeAll(aos);
                aos.FinalizeContent(pos);
            }

            if (this.automaticRearm) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_automaticRearm);
            }
        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (INAPException e) {
            throw new CAPException("INAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.eventTypeBCSM != null) {
            sb.append("eventTypeBCSM=");
            sb.append(eventTypeBCSM);
        }
        if (this.monitorMode != null) {
            sb.append(", monitorMode=");
            sb.append(monitorMode);
        }
        if (this.legID != null) {
            sb.append(", legID=");
            sb.append(legID.toString());
        }
        if (this.dpSpecificCriteria != null) {
            sb.append(", dpSpecificCriteria=");
            sb.append(dpSpecificCriteria.toString());
        }
        if (this.automaticRearm) {
            sb.append(", automaticRearm");
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<BCSMEventImpl> BCSM_EVENT_XML = new XMLFormat<BCSMEventImpl>(BCSMEventImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, BCSMEventImpl bcsmEvent) throws XMLStreamException {
            String str = xml.get(EVENT_TYPE_BCSM, String.class);
            if (str != null)
                bcsmEvent.eventTypeBCSM = Enum.valueOf(EventTypeBCSM.class, str);
            str = xml.get(MONITOR_MODE, String.class);
            if (str != null)
                bcsmEvent.monitorMode = Enum.valueOf(MonitorMode.class, str);
            bcsmEvent.legID = xml.get(LEG_ID, LegIDImpl.class);
            bcsmEvent.dpSpecificCriteria = xml.get(DP_SPECIFIC_CRITERIA, DpSpecificCriteriaImpl.class);
            Boolean bval = xml.get(AUTOMATIC_REARM, Boolean.class);
            if (bval != null)
                bcsmEvent.automaticRearm = bval;
        }

        @Override
        public void write(BCSMEventImpl bcsmEvent, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (bcsmEvent.getEventTypeBCSM() != null)
                xml.add((String) bcsmEvent.getEventTypeBCSM().toString(), EVENT_TYPE_BCSM, String.class);
            if (bcsmEvent.getMonitorMode() != null)
                xml.add((String) bcsmEvent.getMonitorMode().toString(), MONITOR_MODE, String.class);
            if (bcsmEvent.getLegID() != null)
                xml.add((LegIDImpl) bcsmEvent.getLegID(), LEG_ID, LegIDImpl.class);
            if (bcsmEvent.getDpSpecificCriteria() != null)
                xml.add((DpSpecificCriteriaImpl) bcsmEvent.getDpSpecificCriteria(), DP_SPECIFIC_CRITERIA,
                        DpSpecificCriteriaImpl.class);
            if (bcsmEvent.getAutomaticRearm())
                xml.add((Boolean) bcsmEvent.getAutomaticRearm(), AUTOMATIC_REARM, Boolean.class);
        }
    };
}
