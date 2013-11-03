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

package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.TerminationCause;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class DeferredmtlrDataImpl extends SequenceBase implements DeferredmtlrData {

    private static final int _TAG_TERMINATION_CAUSE = 0;
    private static final int _TAG_LCS_LOCATION_INFO = 1;

    private DeferredLocationEventType deferredLocationEventType;
    private TerminationCause terminationCause;
    private LCSLocationInfo lcsLocationInfo;

    /**
     *
     */
    public DeferredmtlrDataImpl() {
        super("DeferredmtlrData");
    }

    /**
     * @param deferredLocationEventType
     * @param terminationCause
     * @param lcsLocationInfo
     */
    public DeferredmtlrDataImpl(DeferredLocationEventType deferredLocationEventType, TerminationCause terminationCause,
            LCSLocationInfo lcsLocationInfo) {
        super("DeferredmtlrData");
        this.deferredLocationEventType = deferredLocationEventType;
        this.terminationCause = terminationCause;
        this.lcsLocationInfo = lcsLocationInfo;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData# getDeferredLocationEventType()
     */
    public DeferredLocationEventType getDeferredLocationEventType() {
        return this.deferredLocationEventType;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData# getTerminationCause()
     */
    public TerminationCause getTerminationCause() {
        return this.terminationCause;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData# getLCSLocationInfo()
     */
    public LCSLocationInfo getLCSLocationInfo() {
        return this.lcsLocationInfo;
    }

    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.deferredLocationEventType = null;
        this.terminationCause = null;
        this.lcsLocationInfo = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int tag = ais.readTag();

        if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_BIT) {
            throw new MAPParsingComponentException(
                    "Error while decoding "
                            + _PrimitiveName
                            + ": Parameter [deferredLocationEventType DeferredLocationEventType] bad tag, tag class, tag or not primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        this.deferredLocationEventType = new DeferredLocationEventTypeImpl();
        ((DeferredLocationEventTypeImpl) this.deferredLocationEventType).decodeAll(ais);

        while (true) {
            if (ais.available() == 0)
                break;

            tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_TERMINATION_CAUSE:
                        // terminationCause [0] TerminationCause OPTIONAL,
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Decoding of " + _PrimitiveName
                                    + " failed. Decoding of parameter[terminationCause [0] TerminationCause] is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        int i1 = (int) ais.readInteger();
                        this.terminationCause = TerminationCause.getTerminationCause(i1);
                        break;
                    case _TAG_LCS_LOCATION_INFO:
                        // lcsLocationInfo [1] LCSLocationInfo
                        if (ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Decoding of " + _PrimitiveName
                                    + " failed. Decoding of parameter[lcsLocationInfo [1] LCSLocationInfo] is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.lcsLocationInfo = new LCSLocationInfoImpl();
                        ((LCSLocationInfoImpl) this.lcsLocationInfo).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                }
            } else {
                ais.advanceElement();
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
     * (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.deferredLocationEventType == null) {
            throw new MAPException("Encdoing of " + _PrimitiveName
                    + " failed. Missing mandatory parameter deferredLocationEventType DeferredLocationEventType");
        }

        ((DeferredLocationEventTypeImpl) this.deferredLocationEventType).encodeAll(asnOs);

        if (this.terminationCause != null) {
            try {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_TERMINATION_CAUSE, this.terminationCause.getCause());
            } catch (IOException e) {
                throw new MAPException("IOException when encoding " + _PrimitiveName + ".terminationCause: " + e.getMessage(),
                        e);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding " + _PrimitiveName + ".terminationCause: " + e.getMessage(),
                        e);
            }
        }

        if (this.lcsLocationInfo != null) {
            ((LCSLocationInfoImpl) this.lcsLocationInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_LOCATION_INFO);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((deferredLocationEventType == null) ? 0 : deferredLocationEventType.hashCode());
        result = prime * result + ((lcsLocationInfo == null) ? 0 : lcsLocationInfo.hashCode());
        result = prime * result + ((terminationCause == null) ? 0 : terminationCause.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DeferredmtlrDataImpl other = (DeferredmtlrDataImpl) obj;
        if (deferredLocationEventType == null) {
            if (other.deferredLocationEventType != null)
                return false;
        } else if (!deferredLocationEventType.equals(other.deferredLocationEventType))
            return false;
        if (lcsLocationInfo == null) {
            if (other.lcsLocationInfo != null)
                return false;
        } else if (!lcsLocationInfo.equals(other.lcsLocationInfo))
            return false;
        if (terminationCause != other.terminationCause)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.deferredLocationEventType != null) {
            sb.append("deferredLocationEventType=");
            sb.append(this.deferredLocationEventType);
        }
        if (this.terminationCause != null) {
            sb.append(", terminationCause=");
            sb.append(this.terminationCause.toString());
        }
        if (this.lcsLocationInfo != null) {
            sb.append(", lcsLocationInfo=");
            sb.append(this.lcsLocationInfo.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
