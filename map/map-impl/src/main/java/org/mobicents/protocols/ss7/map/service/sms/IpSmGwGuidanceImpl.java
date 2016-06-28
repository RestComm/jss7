package org.mobicents.protocols.ss7.map.service.sms;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.IpSmGwGuidance;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

import java.io.IOException;

/**
 * @author eva ogallar
 */
public class IpSmGwGuidanceImpl  extends SequenceBase implements IpSmGwGuidance {

    public static final String PRIMITIVE_NAME = "IpSmGwGuidance";

    private int minimumDeliveryTimeValue;
    private int recommendedDeliveryTimeValue;
    private MAPExtensionContainer extensionContainer;

    public IpSmGwGuidanceImpl() {
        super(PRIMITIVE_NAME);
    }

    public IpSmGwGuidanceImpl(int minimumDeliveryTimeValue, int recommendedDeliveryTimeValue, MAPExtensionContainer extensionContainer) {
        super(PRIMITIVE_NAME);
        this.extensionContainer = extensionContainer;
        this.minimumDeliveryTimeValue = minimumDeliveryTimeValue;
        this.recommendedDeliveryTimeValue = recommendedDeliveryTimeValue;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public int getMinimumDeliveryTimeValue() {
        return minimumDeliveryTimeValue;
    }

    public int getRecommendedDeliveryTimeValue() {
        return recommendedDeliveryTimeValue;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.minimumDeliveryTimeValue = 0;
        this.recommendedDeliveryTimeValue = 0;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    // minimumDeliveryTimeValue
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.INTEGER)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".minimumDeliveryTimeValue: Parameter 0 bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.minimumDeliveryTimeValue = (int) ais.readInteger();
                    if (this.minimumDeliveryTimeValue < 30 || this.minimumDeliveryTimeValue > 600) {
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".minimumDeliveryTimeValue: Parameter must have value 30-600 but found: "
                                + this.minimumDeliveryTimeValue, MAPParsingComponentExceptionReason.MistypedParameter);
                    }
                    break;

                case 1:
                    // recommendedDeliveryTimeValue
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.INTEGER)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".recommendedDeliveryTimeValue: Parameter 1 bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.recommendedDeliveryTimeValue = (int) ais.readInteger();
                    if (this.recommendedDeliveryTimeValue < 30 || this.recommendedDeliveryTimeValue > 600) {
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".recommendedDeliveryTimeValue: Parameter must have value 30-600 but found: "
                                + this.recommendedDeliveryTimeValue, MAPParsingComponentExceptionReason.MistypedParameter);
                    }
                    break;

                default: {
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_UNIVERSAL:
                            switch (tag) {
                                case Tag.SEQUENCE:
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".extensionContainer: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.extensionContainer = new MAPExtensionContainerImpl();
                                    ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
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
                    break;
                }
            }

            num++;
        }

        if (num < 2)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": minimumDeliveryTimeValue " +
                    "and recommendedDeliveryTimeValue are mandatory but are not filled both ",
                    MAPParsingComponentExceptionReason.MistypedParameter);


    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {
            if (this.minimumDeliveryTimeValue < 30 || this.minimumDeliveryTimeValue > 600)
                throw new MAPException("minimumDeliveryTimeValue parameter must have value 30-600 but found: "
                        + this.minimumDeliveryTimeValue);
            if (this.recommendedDeliveryTimeValue < 30 || this.recommendedDeliveryTimeValue > 600)
                throw new MAPException("recommendedDeliveryTimeValue parameter must have value 30-600 but found: "
                        + this.recommendedDeliveryTimeValue);

            asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.INTEGER, this.minimumDeliveryTimeValue);
            asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.INTEGER, this.recommendedDeliveryTimeValue);
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);

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

        sb.append("minimumDeliveryTimeValue=");
        sb.append(minimumDeliveryTimeValue);
        sb.append(", ");

        sb.append("recommendedDeliveryTimeValue=");
        sb.append(recommendedDeliveryTimeValue);
        sb.append(", ");

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
