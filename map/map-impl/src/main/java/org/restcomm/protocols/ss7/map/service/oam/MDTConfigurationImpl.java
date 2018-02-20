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

package org.restcomm.protocols.ss7.map.service.oam;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.oam.AreaScope;
import org.restcomm.protocols.ss7.map.api.service.oam.JobType;
import org.restcomm.protocols.ss7.map.api.service.oam.ListOfMeasurements;
import org.restcomm.protocols.ss7.map.api.service.oam.LoggingDuration;
import org.restcomm.protocols.ss7.map.api.service.oam.LoggingInterval;
import org.restcomm.protocols.ss7.map.api.service.oam.MDTConfiguration;
import org.restcomm.protocols.ss7.map.api.service.oam.ReportAmount;
import org.restcomm.protocols.ss7.map.api.service.oam.ReportInterval;
import org.restcomm.protocols.ss7.map.api.service.oam.ReportingTrigger;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.primitives.SequenceBase;

/**
*
* @author sergey vetyutnev
*
*/
public class MDTConfigurationImpl extends SequenceBase implements MDTConfiguration {
    public static final int _ID_reportingTrigger = 0;
    public static final int _ID_reportAmount = 1;
    public static final int _ID_eventThresholdRSRQ = 2;
    public static final int _ID_loggingInterval = 3;
    public static final int _ID_loggingDuration = 4;
    public static final int _ID_extensionContainer = 5;

    private JobType jobType;
    private AreaScope areaScope;
    private ListOfMeasurements listOfMeasurements;
    private ReportingTrigger reportingTrigger;
    private ReportInterval reportInterval;
    private ReportAmount reportAmount;
    private Integer eventThresholdRSRP;
    private Integer eventThresholdRSRQ;
    private LoggingInterval loggingInterval;
    private LoggingDuration loggingDuration;
    private MAPExtensionContainer extensionContainer;

    public MDTConfigurationImpl() {
        super("MDTConfiguration");
    }

    public MDTConfigurationImpl(JobType jobType, AreaScope areaScope, ListOfMeasurements listOfMeasurements, ReportingTrigger reportingTrigger,
            ReportInterval reportInterval, ReportAmount reportAmount, Integer eventThresholdRSRP, Integer eventThresholdRSRQ, LoggingInterval loggingInterval,
            LoggingDuration loggingDuration, MAPExtensionContainer extensionContainer) {
        super("MDTConfiguration");

        this.jobType = jobType;
        this.areaScope = areaScope;
        this.listOfMeasurements = listOfMeasurements;
        this.reportingTrigger = reportingTrigger;
        this.reportInterval = reportInterval;
        this.reportAmount = reportAmount;
        this.eventThresholdRSRP = eventThresholdRSRP;
        this.eventThresholdRSRQ = eventThresholdRSRQ;
        this.loggingInterval = loggingInterval;
        this.loggingDuration = loggingDuration;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public JobType getJobType() {
        return jobType;
    }

    @Override
    public AreaScope getAreaScope() {
        return areaScope;
    }

    @Override
    public ListOfMeasurements getListOfMeasurements() {
        return listOfMeasurements;
    }

    @Override
    public ReportingTrigger getReportingTrigger() {
        return reportingTrigger;
    }

    @Override
    public ReportInterval getReportInterval() {
        return reportInterval;
    }

    @Override
    public ReportAmount getReportAmount() {
        return reportAmount;
    }

    @Override
    public Integer getEventThresholdRSRP() {
        return eventThresholdRSRP;
    }

    @Override
    public Integer getEventThresholdRSRQ() {
        return eventThresholdRSRQ;
    }

    @Override
    public LoggingInterval getLoggingInterval() {
        return loggingInterval;
    }

    @Override
    public LoggingDuration getLoggingDuration() {
        return loggingDuration;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.jobType = null;
        this.areaScope = null;
        this.listOfMeasurements = null;
        this.reportingTrigger = null;
        this.reportInterval = null;
        this.reportAmount = null;
        this.eventThresholdRSRP = null;
        this.eventThresholdRSRQ = null;
        this.loggingInterval = null;
        this.loggingDuration = null;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
            case 0:
                // JobType
                if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.ENUMERATED)
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ".jobType: Parameter 0 bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                int i1 = (int) ais.readInteger();
                this.jobType = JobType.getInstance(i1);
                break;

            default:
                if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    switch (tag) {
                    case Tag.SEQUENCE:
                        // areaScope
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".areaScope: Parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        this.areaScope = new AreaScopeImpl();
                        ((AreaScopeImpl) this.areaScope).decodeAll(ais);
                        break;
                    case Tag.STRING_OCTET:
                        // listOfMeasurements
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".listOfMeasurements: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        this.listOfMeasurements = new ListOfMeasurementsImpl();
                        ((ListOfMeasurementsImpl) this.listOfMeasurements).decodeAll(ais);
                        break;
                    case Tag.ENUMERATED:
                        // reportInterval
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".reportInterval: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        i1 = (int) ais.readInteger();
                        this.reportInterval = ReportInterval.getInstance(i1);
                        break;
                    case Tag.INTEGER:
                        // eventThresholdRSRP
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".eventThresholdRSRP: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        this.eventThresholdRSRP = (int) ais.readInteger();
                        break;
                    default:
                        ais.advanceElement();
                        break;
                    }
                } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                    switch (tag) {
                    case _ID_reportingTrigger:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".reportingTrigger: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.reportingTrigger = new ReportingTriggerImpl();
                        ((ReportingTriggerImpl) this.reportingTrigger).decodeAll(ais);
                        break;
                    case _ID_reportAmount:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".reportAmount: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        i1 = (int) ais.readInteger();
                        this.reportAmount = ReportAmount.getInstance(i1);
                        break;
                    case _ID_eventThresholdRSRQ:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".eventThresholdRSRQ: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.eventThresholdRSRQ = (int) ais.readInteger();
                        break;
                    case _ID_loggingInterval:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".loggingInterval: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        i1 = (int) ais.readInteger();
                        this.loggingInterval = LoggingInterval.getInstance(i1);
                        break;
                    case _ID_loggingDuration:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".loggingDuration: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        i1 = (int) ais.readInteger();
                        this.loggingDuration = LoggingDuration.getInstance(i1);
                        break;
                    case _ID_extensionContainer:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".extensionContainer: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                    }
                } else {
                    ais.advanceElement();
                }
                break;
            }

            num++;
        }

        if (num < 1)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": JobType is mandatory but it is null ",
                    MAPParsingComponentExceptionReason.MistypedParameter);

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        try {
            if (this.jobType == null)
                throw new MAPException("jobType parameter must not be null");

            asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.jobType.getCode());

            if (this.areaScope != null)
                ((AreaScopeImpl) this.areaScope).encodeAll(asnOs);
            if (this.listOfMeasurements != null)
                ((ListOfMeasurementsImpl) this.listOfMeasurements).encodeAll(asnOs);
            if (this.reportingTrigger != null)
                ((ReportingTriggerImpl) this.reportingTrigger).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_reportingTrigger);
            if (this.reportInterval != null)
                asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.reportInterval.getCode());
            if (this.reportAmount != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_reportAmount, this.reportAmount.getCode());
            if (this.eventThresholdRSRP != null)
                asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.INTEGER, this.eventThresholdRSRP);
            if (this.eventThresholdRSRQ != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_eventThresholdRSRQ, this.eventThresholdRSRQ);
            if (this.loggingInterval != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_loggingInterval, this.loggingInterval.getCode());
            if (this.loggingDuration != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_loggingDuration, this.loggingDuration.getCode());
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensionContainer);

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.jobType != null) {
            sb.append("jobType=");
            sb.append(this.jobType.toString());
            sb.append(", ");
        }
        if (this.areaScope != null) {
            sb.append("areaScope=");
            sb.append(this.areaScope.toString());
            sb.append(", ");
        }
        if (this.listOfMeasurements != null) {
            sb.append("listOfMeasurements=");
            sb.append(this.listOfMeasurements.toString());
            sb.append(", ");
        }
        if (this.reportingTrigger != null) {
            sb.append("reportingTrigger=");
            sb.append(this.reportingTrigger.toString());
            sb.append(", ");
        }
        if (this.reportInterval != null) {
            sb.append("reportInterval=");
            sb.append(this.reportInterval.toString());
            sb.append(", ");
        }
        if (this.reportAmount != null) {
            sb.append("reportAmount=");
            sb.append(this.reportAmount.toString());
            sb.append(", ");
        }
        if (this.eventThresholdRSRP != null) {
            sb.append("eventThresholdRSRP=");
            sb.append(this.eventThresholdRSRP.toString());
            sb.append(", ");
        }
        if (this.eventThresholdRSRQ != null) {
            sb.append("eventThresholdRSRQ=");
            sb.append(this.eventThresholdRSRQ.toString());
            sb.append(", ");
        }
        if (this.loggingInterval != null) {
            sb.append("loggingInterval=");
            sb.append(this.loggingInterval.toString());
            sb.append(", ");
        }
        if (this.loggingDuration != null) {
            sb.append("loggingDuration=");
            sb.append(this.loggingDuration.toString());
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
