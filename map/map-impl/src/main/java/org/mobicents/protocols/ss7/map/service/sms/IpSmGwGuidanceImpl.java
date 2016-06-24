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

    public static final String PRIMITIVE_NAME = "IP-SM-GW-Guidance";

    private MAPExtensionContainer extensionContainer;
    private Integer minimumDeliveryTimeValue;
    private Integer recommendedDeliveryTimeValue;

    public IpSmGwGuidanceImpl() {
        super(PRIMITIVE_NAME);
    }

    public IpSmGwGuidanceImpl(Integer minimumDeliveryTimeValue, Integer recommendedDeliveryTimeValue, MAPExtensionContainer extensionContainer) {
        super(PRIMITIVE_NAME);
        this.extensionContainer = extensionContainer;
        this.minimumDeliveryTimeValue = minimumDeliveryTimeValue;
        this.recommendedDeliveryTimeValue = recommendedDeliveryTimeValue;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public Integer getMinimumDeliveryTimeValue() {
        return minimumDeliveryTimeValue;
    }

    public Integer getRecommendedDeliveryTimeValue() {
        return recommendedDeliveryTimeValue;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.minimumDeliveryTimeValue = null;
        this.recommendedDeliveryTimeValue = null;

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
                    break;

                case 1:
                    // recommendedDeliveryTimeValue
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.INTEGER)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".recommendedDeliveryTimeValue: Parameter 1 bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.recommendedDeliveryTimeValue = (int) ais.readInteger();
                    break;

                default:
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
            if (this.minimumDeliveryTimeValue == null)
                throw new MAPException("minimumDeliveryTimeValue parameter must not be null");
            if (this.recommendedDeliveryTimeValue == null)
                throw new MAPException("recommendedDeliveryTimeValue parameter must not be null");

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
}
