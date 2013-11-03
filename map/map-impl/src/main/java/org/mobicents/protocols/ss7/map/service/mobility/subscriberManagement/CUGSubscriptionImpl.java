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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGSubscription;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.IntraCUGOptions;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
public class CUGSubscriptionImpl extends SequenceBase implements CUGSubscription {

    private static final int _TAG_extensionContainer = 0;

    private int cugIndex;
    private CUGInterlock cugInterlock = null;
    private IntraCUGOptions intraCugOptions = null;
    private ArrayList<ExtBasicServiceCode> basicService = null;
    private MAPExtensionContainer extensionContainer = null;

    public CUGSubscriptionImpl() {
        super("CUGSubscription");
    }

    /**
     *
     */
    public CUGSubscriptionImpl(int cugIndex, CUGInterlock cugInterlock, IntraCUGOptions intraCugOptions,
            ArrayList<ExtBasicServiceCode> basicService, MAPExtensionContainer extensionContainer) {
        super("CUGSubscription");

        this.cugIndex = cugIndex;
        this.cugInterlock = cugInterlock;
        this.intraCugOptions = intraCugOptions;
        this.basicService = basicService;
        this.extensionContainer = extensionContainer;
    }

    public int getCUGIndex() {
        return (int) this.cugIndex;
    }

    public CUGInterlock getCugInterlock() {
        return this.cugInterlock;
    }

    public IntraCUGOptions getIntraCugOptions() {
        return this.intraCugOptions;
    }

    public ArrayList<ExtBasicServiceCode> getBasicServiceGroupList() {
        return this.basicService;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.cugIndex = 0;
        this.cugInterlock = null;
        this.intraCugOptions = null;
        this.basicService = null;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0: // cugIndex
                    if (!ais.isTagPrimitive() || tag != Tag.INTEGER || ais.getTagClass() != Tag.CLASS_UNIVERSAL)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".cugIndex: bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.cugIndex = (int) ais.readInteger();
                    break;

                case 1: // cugInterlock
                    if (!ais.isTagPrimitive() || tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".cugInterlock: bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.cugInterlock = new CUGInterlockImpl();
                    ((CUGInterlockImpl) this.cugInterlock).decodeAll(ais);
                    break;

                case 2: // intraCugOptions
                    if (!ais.isTagPrimitive() || tag != Tag.ENUMERATED || ais.getTagClass() != Tag.CLASS_UNIVERSAL)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".intraCugOptions: bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    int i1 = (int) ais.readInteger();
                    this.intraCugOptions = IntraCUGOptions.getInstance(i1);
                    break;

                default:
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_CONTEXT_SPECIFIC:
                            switch (tag) {
                                case _TAG_extensionContainer:
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".extensionContainer: is primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.extensionContainer = new MAPExtensionContainerImpl();
                                    ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                                    break;
                                default:
                                    ais.advanceElement();
                                    break;
                            }
                            break;

                        case Tag.CLASS_UNIVERSAL:
                            switch (tag) {
                                case Tag.SEQUENCE:
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".basicService: Parameter is primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);

                                    this.basicService = new ArrayList<ExtBasicServiceCode>();
                                    AsnInputStream ais2a = ais.readSequenceStream();
                                    while (true) {
                                        if (ais2a.available() == 0)
                                            break;

                                        int tag2a = ais2a.readTag();

                                        ExtBasicServiceCode basicServiceItem = new ExtBasicServiceCodeImpl();
                                        ((ExtBasicServiceCodeImpl) basicServiceItem).decodeAll(ais2a);
                                        this.basicService.add(basicServiceItem);
                                    }

                                    if (this.basicService.size() < 1 || this.basicService.size() > 32) {
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".basicService: basicServiceGroupList must be from 1 to 32 size, found: "
                                                + this.basicService.size(),
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    }
                                    break;

                                default:
                                    ais.advanceElement();
                                    break;
                            }
                            break;

                        default:
                            ais.advanceElement();
                            break;
                    }
            }
            num++;
        }

        if (num < 3) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": cugIndex, cugInterlock and intraCugOptions required, found only mandatory parameters: " + num,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.cugInterlock == null)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": cugInterlock required.");

        if (this.intraCugOptions == null)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": intraCugOptions required.");

        try {

            asnOs.writeInteger(this.cugIndex);

            ((CUGInterlockImpl) this.cugInterlock).encodeAll(asnOs);

            asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.intraCugOptions.getCode());

            if (this.basicService != null) {
                if (this.basicService.size() < 1 || this.basicService.size() > 32) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + ".basicService: basicServiceGroupList must be from 1 to 32 size, found: "
                            + this.basicService.size());
                }

                asnOs.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
                int pos = asnOs.StartContentDefiniteLength();
                for (ExtBasicServiceCode be : this.basicService) {
                    ExtBasicServiceCodeImpl bee = (ExtBasicServiceCodeImpl) be;
                    bee.encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extensionContainer);
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        sb.append("cugIndex=");
        sb.append(this.cugIndex);
        sb.append(", ");

        if (this.cugInterlock != null) {
            sb.append("cugInterlock=");
            sb.append(this.cugInterlock.toString());
            sb.append(", ");
        }

        if (this.intraCugOptions != null) {
            sb.append("intraCugOptions=");
            sb.append(this.intraCugOptions.toString());
            sb.append(", ");
        }

        if (this.basicService != null) {
            sb.append("basicService=[");
            boolean firstItem = true;
            for (ExtBasicServiceCode be : this.basicService) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("], ");

        return sb.toString();
    }
}
