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

package org.mobicents.protocols.ss7.map.service.mobility.authentication;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.CK;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.IK;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.KSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.UMTSSecurityContextData;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class UMTSSecurityContextDataImpl extends SequenceBase implements UMTSSecurityContextData {

    private CK ck;
    private IK ik;
    private KSI ksi;

    public UMTSSecurityContextDataImpl() {
        super("UMTSSecurityContextData");
    }

    public UMTSSecurityContextDataImpl(CK ck, IK ik, KSI ksi) {
        super("UMTSSecurityContextData");
        this.ck = ck;
        this.ik = ik;
        this.ksi = ksi;
    }

    @Override
    public CK getCK() {
        return this.ck;
    }

    @Override
    public IK getIK() {
        return this.ik;
    }

    @Override
    public KSI getKSI() {
        return this.ksi;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();
            switch (num) {
                case 0:
                    if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive()) {
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".ck: Parameter 0 bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);

                    }
                    this.ck = new CKImpl();
                    ((CKImpl) this.ck).decodeAll(ais);
                    break;
                case 1:
                    if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive()) {
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".ik: Parameter 1 bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);

                    }
                    this.ik = new IKImpl();
                    ((IKImpl) this.ik).decodeAll(ais);
                    break;
                case 2:
                    if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive()) {
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".ck: Parameter 2 bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);

                    }
                    this.ksi = new KSIImpl();
                    ((KSIImpl) this.ksi).decodeAll(ais);
                    break;
                default:
                    ais.advanceElement();
                    break;
            }
            num++;
        }
        if (num < 3)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Needs at least 3 mandatory parameters, found " + num,
                    MAPParsingComponentExceptionReason.MistypedParameter);

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.ck == null || this.ik == null || this.ksi == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter ck,ik or ksi is not defined");
        }
        if (this.ck != null)
            ((CKImpl) this.ck).encodeAll(asnOs);

        if (this.ik != null)
            ((IKImpl) this.ik).encodeAll(asnOs);

        if (this.ksi != null)
            ((KSIImpl) this.ksi).encodeAll(asnOs);

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.ck != null) {
            sb.append("ck=");
            sb.append(this.ck.toString());
            sb.append(", ");
        }

        if (this.ik != null) {
            sb.append("ik=");
            sb.append(this.ik.toString());
            sb.append(", ");
        }

        if (this.ksi != null) {
            sb.append("ksi=");
            sb.append(this.ksi.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();

    }

}
