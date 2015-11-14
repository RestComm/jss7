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

package org.mobicents.protocols.ss7.map.service.supplementary;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.service.supplementary.CCBSFeature;
import org.mobicents.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.mobicents.protocols.ss7.map.api.service.supplementary.GenericServiceInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
*
* @author sergey vetyutnev
*
*/
public class GenericServiceInfoImpl extends SequenceBase implements GenericServiceInfo {
    public static final int _ID_maximumEntitledPriority = 0;
    public static final int _ID_defaultPriority = 1;
    public static final int _ID_ccbsFeatureList = 2;
    public static final int _ID_nbrSB = 3;
    public static final int _ID_nbrUser = 4;
    public static final int _ID_nbrSN = 5;

    private SSStatus ssStatus;
    private CliRestrictionOption cliRestrictionOption;
    private EMLPPPriority maximumEntitledPriority;
    private EMLPPPriority defaultPriority;
    private ArrayList<CCBSFeature> ccbsFeatureList;
    private Integer nbrSB;
    private Integer nbrUser;
    private Integer nbrSN;

    public GenericServiceInfoImpl() {
        super("GenericServiceInfo");
    }

    public GenericServiceInfoImpl(SSStatus ssStatus, CliRestrictionOption cliRestrictionOption, EMLPPPriority maximumEntitledPriority,
            EMLPPPriority defaultPriority, ArrayList<CCBSFeature> ccbsFeatureList, Integer nbrSB, Integer nbrUser, Integer nbrSN) {
        super("GenericServiceInfo");

        this.ssStatus = ssStatus;
        this.cliRestrictionOption = cliRestrictionOption;
        this.maximumEntitledPriority = maximumEntitledPriority;
        this.defaultPriority = defaultPriority;
        this.ccbsFeatureList = ccbsFeatureList;
        this.nbrSB = nbrSB;
        this.nbrUser = nbrUser;
        this.nbrSN = nbrSN;
    }

    @Override
    public SSStatus getSsStatus() {
        return ssStatus;
    }

    @Override
    public CliRestrictionOption getCliRestrictionOption() {
        return cliRestrictionOption;
    }

    @Override
    public EMLPPPriority getMaximumEntitledPriority() {
        return maximumEntitledPriority;
    }

    @Override
    public EMLPPPriority getDefaultPriority() {
        return defaultPriority;
    }

    @Override
    public ArrayList<CCBSFeature> getCcbsFeatureList() {
        return ccbsFeatureList;
    }

    @Override
    public Integer getNbrSB() {
        return nbrSB;
    }

    @Override
    public Integer getNbrUser() {
        return nbrUser;
    }

    @Override
    public Integer getNbrSN() {
        return nbrSN;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.ssStatus = null;
        this.cliRestrictionOption = null;
        this.maximumEntitledPriority = null;
        this.defaultPriority = null;
        this.ccbsFeatureList = null;
        this.nbrSB = null;
        this.nbrUser = null;
        this.nbrSN = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
            case 0:
                // ssStatus
                if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ".ssStatus: Parameter 0 bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                this.ssStatus = new SSStatusImpl();
                ((SSStatusImpl) this.ssStatus).decodeAll(ais);
                break;

            default:
                if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    switch (tag) {

                    case Tag.ENUMERATED:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".cliRestrictionOption: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        int i1 = (int) ais.readInteger();
                        this.cliRestrictionOption = CliRestrictionOption.getInstance(i1);
                        break;

                    default:
                        ais.advanceElement();
                        break;
                    }
                }
                else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    switch (tag) {
                    case _ID_maximumEntitledPriority:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".maximumEntitledPriority: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        int i1 = (int) ais.readInteger();
                        this.maximumEntitledPriority = EMLPPPriority.getEMLPPPriority(i1);
                        break;
                    case _ID_defaultPriority:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".defaultPriority: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        i1 = (int) ais.readInteger();
                        this.defaultPriority = EMLPPPriority.getEMLPPPriority(i1);
                        break;
                    case _ID_ccbsFeatureList:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".ccbsFeatureList: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        AsnInputStream ais2 = ais.readSequenceStream();
                        this.ccbsFeatureList = new ArrayList<CCBSFeature>();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            ais2.readTag();

                            CCBSFeature ccBSFeature = new CCBSFeatureImpl();
                            ((CCBSFeatureImpl) ccBSFeature).decodeAll(ais2);
                            this.ccbsFeatureList.add(ccBSFeature);
                        }
                        if (this.ccbsFeatureList.size() < 1 || this.ccbsFeatureList.size() > 5) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter ccbsFeatureList size must be from 1 to 5, found: " + this.ccbsFeatureList.size(),
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        break;
                    case _ID_nbrSB:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".nbrSB: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        this.nbrSB = (int) ais.readInteger();
                        break;
                    case _ID_nbrUser:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".nbrUser: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        this.nbrUser = (int) ais.readInteger();
                        break;
                    case _ID_nbrSN:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".nbrSN: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        this.nbrSN = (int) ais.readInteger();
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
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Needs at least 1 mandatory parameter, found " + num,
                    MAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {

            if (this.ssStatus == null)
                throw new MAPException("ssStatus parameter must not be null");

            ((SSStatusImpl) this.ssStatus).encodeAll(asnOs);

            if (this.cliRestrictionOption != null)
                asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.cliRestrictionOption.getCode());

            if (this.maximumEntitledPriority != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_maximumEntitledPriority, this.maximumEntitledPriority.getCode());
            if (this.defaultPriority != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_defaultPriority, this.defaultPriority.getCode());

            if (this.ccbsFeatureList != null) {
                try {
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_ccbsFeatureList);
                    int pos = asnOs.StartContentDefiniteLength();
                    for (CCBSFeature item : this.ccbsFeatureList) {
                        ((CCBSFeatureImpl) item).encodeAll(asnOs);
                    }
                    asnOs.FinalizeContent(pos);
                } catch (AsnException e) {
                    throw new MAPException("AsnException when encoding " + _PrimitiveName + ".ccbsFeatureList: " + e.getMessage(), e);
                }
            }

            if (this.nbrSB != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_nbrSB, this.nbrSB);
            if (this.nbrUser != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_nbrUser, this.nbrUser);
            if (this.nbrSN != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_nbrSN, this.nbrSN);

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

        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(ssStatus);
            sb.append(", ");
        }
        if (this.cliRestrictionOption != null) {
            sb.append("cliRestrictionOption=");
            sb.append(cliRestrictionOption);
            sb.append(", ");
        }
        if (this.maximumEntitledPriority != null) {
            sb.append("maximumEntitledPriority=");
            sb.append(maximumEntitledPriority);
            sb.append(", ");
        }
        if (this.defaultPriority != null) {
            sb.append("defaultPriority=");
            sb.append(defaultPriority);
            sb.append(", ");
        }
        if (this.ccbsFeatureList != null) {
            sb.append("ccbsFeatureList=[");
            boolean firstItem = true;
            for (CCBSFeature be : this.ccbsFeatureList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }
        if (this.nbrSB != null) {
            sb.append("nbrSB=");
            sb.append(nbrSB);
            sb.append(", ");
        }
        if (this.nbrUser != null) {
            sb.append("nbrUser=");
            sb.append(nbrUser);
            sb.append(", ");
        }
        if (this.nbrSN != null) {
            sb.append("nbrSN=");
            sb.append(nbrSN);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
