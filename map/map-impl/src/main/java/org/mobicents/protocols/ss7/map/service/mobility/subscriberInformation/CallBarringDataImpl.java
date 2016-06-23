/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallBarringData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.mobicents.protocols.ss7.map.api.service.supplementary.Password;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtCallBarringFeatureImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.PasswordImpl;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by vsubbotin on 25/05/16.
 */
public class CallBarringDataImpl extends SequenceBase implements CallBarringData {
    public static final String _PrimitiveName = "CallBarringData";

    private ArrayList<ExtCallBarringFeature> callBarringFeatureList;
    private Password password;
    private Integer wrongPasswordAttemptsCounter;
    private boolean notificationToCSE;
    private MAPExtensionContainer extensionContainer;

    public CallBarringDataImpl() {
        super(_PrimitiveName);
    }

    public CallBarringDataImpl(ArrayList<ExtCallBarringFeature> callBarringFeatureList, Password password,
            Integer wrongPasswordAttemptsCounter, boolean notificationToCSE, MAPExtensionContainer extensionContainer) {
        super(_PrimitiveName);
        this.callBarringFeatureList = callBarringFeatureList;
        this.password = password;
        this.wrongPasswordAttemptsCounter = wrongPasswordAttemptsCounter;
        this.notificationToCSE = notificationToCSE;
        this.extensionContainer = extensionContainer;
    }

    public ArrayList<ExtCallBarringFeature> getCallBarringFeatureList() {
        return this.callBarringFeatureList;
    }

    public Password getPassword() {
        return this.password;
    }

    public Integer getWrongPasswordAttemptsCounter() {
        return this.wrongPasswordAttemptsCounter;
    }

    public boolean getNotificationToCSE() {
        return this.notificationToCSE;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.callBarringFeatureList = null;
        this.password = null;
        this.wrongPasswordAttemptsCounter = null;
        this.notificationToCSE = false;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int tag = ais.readTag();
        if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL || ais.isTagPrimitive())
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parameter callBarringFeatureList has bad tag or tag class or not primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        // decode callBarringFeatureList
        AsnInputStream ais2 = ais.readSequenceStream();
        ExtCallBarringFeature extCallBarringFeature;
        this.callBarringFeatureList = new ArrayList<ExtCallBarringFeature>();
        while (true) {
            if (ais2.available() == 0)
                break;

            tag = ais2.readTag();
            if (tag != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                        + ".extCallBarringFeature: Parameter extCallBarringFeature is primitive",
                        MAPParsingComponentExceptionReason.MistypedParameter);

            extCallBarringFeature = new ExtCallBarringFeatureImpl();
            ((ExtCallBarringFeatureImpl)extCallBarringFeature).decodeAll(ais2);
            this.callBarringFeatureList.add(extCallBarringFeature);
        }

        if (this.callBarringFeatureList.size() < 1 || this.callBarringFeatureList.size() > 32) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parameter callBarringFeatureList size must be from 1 to 32, found: "
                    + this.callBarringFeatureList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
        }

        while (true) {
            if (ais.available() == 0)
                break;

            tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                switch (tag) {
                // decode extensionContainer
                    case Tag.SEQUENCE:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".extensionContainer: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                        break;
                    case Tag.STRING_NUMERIC:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".password: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.password = new PasswordImpl();
                        ((PasswordImpl) this.password).decodeAll(ais);
                        break;
                    case Tag.INTEGER:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".password: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.wrongPasswordAttemptsCounter = (int) ais.readInteger();

                        if (this.wrongPasswordAttemptsCounter < 0 || this.wrongPasswordAttemptsCounter > 4) {
                            throw new MAPParsingComponentException("Error while encoding " + _PrimitiveName
                                    + " parameter wrongPasswordAttemptsCounter is out of range (0..4). Actual value: "
                                    + this.wrongPasswordAttemptsCounter, MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        break;
                    case Tag.NULL:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".notificationToCSE: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        ais.readNull();
                        this.notificationToCSE = Boolean.TRUE;
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

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.callBarringFeatureList == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter callBarringFeatureList is not defined");
        }

        if (this.callBarringFeatureList.size() < 1 || this.callBarringFeatureList.size() > 32) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " parameter callBarringFeatureList is out of range (0..32). Actual size: " + this.callBarringFeatureList.size());
        }

        try {
            asnOs.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int pos = asnOs.StartContentDefiniteLength();
            for (ExtCallBarringFeature extCallBarringFeature: callBarringFeatureList) {
                ((ExtCallBarringFeatureImpl)extCallBarringFeature).encodeAll(asnOs);
            }
            asnOs.FinalizeContent(pos);

            if (this.password != null) {
                ((PasswordImpl)this.password).encodeAll(asnOs);
            }

            if (this.wrongPasswordAttemptsCounter != null) {
                if (this.wrongPasswordAttemptsCounter < 0 || this.wrongPasswordAttemptsCounter > 4) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + " parameter wrongPasswordAttemptsCounter is out of range (0..4). Actual value: " + this.wrongPasswordAttemptsCounter);
                }

                asnOs.writeInteger(this.wrongPasswordAttemptsCounter);
            }

            if (this.notificationToCSE) {
                asnOs.writeNull();
            }

            if (this.extensionContainer != null) {
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
            }
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

        if (this.callBarringFeatureList != null) {
            sb.append("callBarringFeatureList=[");
            boolean firstItem = true;
            for (ExtCallBarringFeature extCallBarringFeature: callBarringFeatureList) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(extCallBarringFeature);
            }
            sb.append("]");
        }
        if (this.password != null) {
            sb.append(", password=");
            sb.append(this.password);
        }
        if (this.wrongPasswordAttemptsCounter != null) {
            sb.append(", wrongPasswordAttemptsCounter=");
            sb.append(this.wrongPasswordAttemptsCounter);
        }
        if (this.notificationToCSE) {
            sb.append(", notificationToCSE");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        sb.append("]");
        return sb.toString();
    }
}
