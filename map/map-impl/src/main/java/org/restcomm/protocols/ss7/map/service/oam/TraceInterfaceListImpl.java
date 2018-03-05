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
import org.restcomm.protocols.ss7.map.api.service.oam.BMSCInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.ENBInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.GGSNInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.MGWInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.MMEInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.MSCSInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.PGWInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.RNCInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.SGSNInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.SGWInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceInterfaceList;
import org.restcomm.protocols.ss7.map.primitives.SequenceBase;

/**
*
* @author sergey vetyutnev
*
*/
public class TraceInterfaceListImpl extends SequenceBase implements TraceInterfaceList {
    public static final int _ID_mscSList = 0;
    public static final int _ID_mgwList = 1;
    public static final int _ID_sgsnList = 2;
    public static final int _ID_ggsnList = 3;
    public static final int _ID_rncList = 4;
    public static final int _ID_bmscList = 5;
    public static final int _ID_mmeList = 6;
    public static final int _ID_sgwList = 7;
    public static final int _ID_pgwList = 8;
    public static final int _ID_eNBList = 9;

    private MSCSInterfaceList mscSList;
    private MGWInterfaceList mgwList;
    private SGSNInterfaceList sgsnList;
    private GGSNInterfaceList ggsnList;
    private RNCInterfaceList rncList;
    private BMSCInterfaceList bmscList;
    private MMEInterfaceList mmeList;
    private SGWInterfaceList sgwList;
    private PGWInterfaceList pgwList;
    private ENBInterfaceList enbList;

    public TraceInterfaceListImpl() {
        super("TraceInterfaceList");
    }

    public TraceInterfaceListImpl(MSCSInterfaceList mscSList, MGWInterfaceList mgwList, SGSNInterfaceList sgsnList, GGSNInterfaceList ggsnList,
            RNCInterfaceList rncList, BMSCInterfaceList bmscList, MMEInterfaceList mmeList, SGWInterfaceList sgwList, PGWInterfaceList pgwList,
            ENBInterfaceList enbList) {
        super("TraceInterfaceList");

        this.mscSList = mscSList;
        this.mgwList = mgwList;
        this.sgsnList = sgsnList;
        this.ggsnList = ggsnList;
        this.rncList = rncList;
        this.bmscList = bmscList;
        this.mmeList = mmeList;
        this.sgwList = sgwList;
        this.pgwList = pgwList;
        this.enbList = enbList;
    }

    @Override
    public MSCSInterfaceList getMscSList() {
        return this.mscSList;
    }

    @Override
    public MGWInterfaceList getMgwList() {
        return mgwList;
    }

    @Override
    public SGSNInterfaceList getSgsnList() {
        return sgsnList;
    }

    @Override
    public GGSNInterfaceList getGgsnList() {
        return ggsnList;
    }

    @Override
    public RNCInterfaceList getRncList() {
        return rncList;
    }

    @Override
    public BMSCInterfaceList getBmscList() {
        return bmscList;
    }

    @Override
    public MMEInterfaceList getMmeList() {
        return mmeList;
    }

    @Override
    public SGWInterfaceList getSgwList() {
        return sgwList;
    }

    @Override
    public PGWInterfaceList getPgwList() {
        return pgwList;
    }

    @Override
    public ENBInterfaceList getEnbList() {
        return enbList;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.mscSList = null;
        this.mgwList = null;
        this.sgsnList = null;
        this.ggsnList = null;
        this.rncList = null;
        this.bmscList = null;
        this.mmeList = null;
        this.sgwList = null;
        this.pgwList = null;
        this.enbList = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                switch (tag) {
                case _ID_mscSList:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " mscSList: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.mscSList = new MSCSInterfaceListImpl();
                    ((MSCSInterfaceListImpl) this.mscSList).decodeAll(ais);
                    break;
                case _ID_mgwList:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " mgwList: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.mgwList = new MGWInterfaceListImpl();
                    ((MGWInterfaceListImpl) this.mgwList).decodeAll(ais);
                    break;
                case _ID_sgsnList:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " mgwList: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.sgsnList = new SGSNInterfaceListImpl();
                    ((SGSNInterfaceListImpl) this.sgsnList).decodeAll(ais);
                    break;
                case _ID_ggsnList:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " ggsnList: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.ggsnList = new GGSNInterfaceListImpl();
                    ((GGSNInterfaceListImpl) this.ggsnList).decodeAll(ais);
                    break;
                case _ID_rncList:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " rncList: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.rncList = new RNCInterfaceListImpl();
                    ((RNCInterfaceListImpl) this.rncList).decodeAll(ais);
                    break;
                case _ID_bmscList:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " bmscList: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.bmscList = new BMSCInterfaceListImpl();
                    ((BMSCInterfaceListImpl) this.bmscList).decodeAll(ais);
                    break;
                case _ID_mmeList:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " mmeList: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.mmeList = new MMEInterfaceListImpl();
                    ((MMEInterfaceListImpl) this.mmeList).decodeAll(ais);
                    break;
                case _ID_sgwList:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " sgwList: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.sgwList = new SGWInterfaceListImpl();
                    ((SGWInterfaceListImpl) this.sgwList).decodeAll(ais);
                    break;
                case _ID_pgwList:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " pgwList: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.pgwList = new PGWInterfaceListImpl();
                    ((PGWInterfaceListImpl) this.pgwList).decodeAll(ais);
                    break;
                case _ID_eNBList:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " enbList: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.enbList = new ENBInterfaceListImpl();
                    ((ENBInterfaceListImpl) this.enbList).decodeAll(ais);
                    break;

                default:
                    ais.advanceElement();
                    break;
                }
            } else {
                ais.advanceElement();
            }
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.mscSList != null)
            ((MSCSInterfaceListImpl) this.mscSList).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_mscSList);
        if (this.mgwList != null)
            ((MGWInterfaceListImpl) this.mgwList).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_mgwList);
        if (this.sgsnList != null)
            ((SGSNInterfaceListImpl) this.sgsnList).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_sgsnList);
        if (this.ggsnList != null)
            ((GGSNInterfaceListImpl) this.ggsnList).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_ggsnList);
        if (this.rncList != null)
            ((RNCInterfaceListImpl) this.rncList).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_rncList);
        if (this.bmscList != null)
            ((BMSCInterfaceListImpl) this.bmscList).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_bmscList);
        if (this.mmeList != null)
            ((MMEInterfaceListImpl) this.mmeList).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_mmeList);
        if (this.sgwList != null)
            ((SGWInterfaceListImpl) this.sgwList).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_sgwList);
        if (this.pgwList != null)
            ((PGWInterfaceListImpl) this.pgwList).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_pgwList);
        if (this.enbList != null)
            ((ENBInterfaceListImpl) this.enbList).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_eNBList);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.mscSList != null) {
            sb.append("mscSList=");
            sb.append(this.mscSList);
            sb.append(", ");
        }
        if (this.mgwList != null) {
            sb.append("mgwList=");
            sb.append(this.mgwList);
            sb.append(", ");
        }
        if (this.sgsnList != null) {
            sb.append("sgsnList=");
            sb.append(this.sgsnList);
            sb.append(", ");
        }
        if (this.ggsnList != null) {
            sb.append("ggsnList=");
            sb.append(this.ggsnList);
            sb.append(", ");
        }
        if (this.rncList != null) {
            sb.append("rncList=");
            sb.append(this.rncList);
            sb.append(", ");
        }
        if (this.bmscList != null) {
            sb.append("bmscList=");
            sb.append(this.bmscList);
            sb.append(", ");
        }
        if (this.mmeList != null) {
            sb.append("mmeList=");
            sb.append(this.mmeList);
            sb.append(", ");
        }
        if (this.sgwList != null) {
            sb.append("sgwList=");
            sb.append(this.sgwList);
            sb.append(", ");
        }
        if (this.pgwList != null) {
            sb.append("pgwList=");
            sb.append(this.pgwList);
            sb.append(", ");
        }
        if (this.enbList != null) {
            sb.append("enbList=");
            sb.append(this.enbList);
            sb.append(", ");
        }

        sb.append("]");
        return sb.toString();
    }

}
