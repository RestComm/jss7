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
package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.VolumeIfTariffSwitch;
import org.restcomm.protocols.ss7.cap.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class VolumeIfTariffSwitchImpl extends SequenceBase implements VolumeIfTariffSwitch {

    public static final int _ID_volumeSinceLastTariffSwitch = 0;
    public static final int _ID_volumeTariffSwitchInterval = 1;

    public static final int _ID_VolumeIfTariffSwitch = 1;

    private long volumeSinceLastTariffSwitch;
    private Long volumeTariffSwitchInterval;

    public VolumeIfTariffSwitchImpl() {
        super("VolumeIfTariffSwitch");
    }

    public VolumeIfTariffSwitchImpl(long volumeSinceLastTariffSwitch, Long volumeTariffSwitchInterval) {
        super("VolumeIfTariffSwitch");
        this.volumeSinceLastTariffSwitch = volumeSinceLastTariffSwitch;
        this.volumeTariffSwitchInterval = volumeTariffSwitchInterval;
    }

    public long getVolumeSinceLastTariffSwitch() {
        return this.volumeSinceLastTariffSwitch;
    }

    public Long getVolumeTariffSwitchInterval() {
        return this.volumeTariffSwitchInterval;
    }

    @Override
    public int getTag() throws CAPException {
        return _ID_VolumeIfTariffSwitch;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        boolean isVolumeSinceLastTariffSwitchIncluded = false;

        this.volumeSinceLastTariffSwitch = -1L;
        this.volumeTariffSwitchInterval = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_volumeSinceLastTariffSwitch:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".volumeSinceLastTariffSwitch: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.volumeSinceLastTariffSwitch = ais.readInteger();
                        isVolumeSinceLastTariffSwitchIncluded = true;
                        break;
                    case _ID_volumeTariffSwitchInterval:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".volumeTariffSwitchInterval: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.volumeTariffSwitchInterval = ais.readInteger();
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (!isVolumeSinceLastTariffSwitchIncluded)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": volumeSinceLastTariffSwitch is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {
        try {
            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_volumeSinceLastTariffSwitch, this.volumeSinceLastTariffSwitch);

            if (this.volumeTariffSwitchInterval != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_volumeTariffSwitchInterval,
                        this.volumeTariffSwitchInterval.longValue());

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        sb.append("volumeSinceLastTariffSwitch=");
        sb.append(this.volumeSinceLastTariffSwitch);
        sb.append(", ");

        if (this.volumeTariffSwitchInterval != null) {
            sb.append("volumeTariffSwitchInterval=");
            sb.append(this.volumeTariffSwitchInterval);
        }

        sb.append("]");

        return sb.toString();
    }

}
