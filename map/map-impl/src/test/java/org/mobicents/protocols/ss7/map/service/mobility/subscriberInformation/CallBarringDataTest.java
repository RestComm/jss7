package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.api.service.supplementary.Password;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtCallBarringFeatureImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.PasswordImpl;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author vadim subbotin
 *
 */
public class CallBarringDataTest {
    private byte[] data = {48, 62, 48, 8, 48, 6, -126, 1, 0, -124, 1, 8, 18, 4, 48, 48, 48, 48, 2, 1, 3, 5, 0, 48, 39,
            -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
            23, 24, 25, 26, -95, 3, 31, 32, 33};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
        AsnInputStream asn = new AsnInputStream(data);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        CallBarringDataImpl callBarringData = new CallBarringDataImpl();
        callBarringData.decodeAll(asn);

        assertEquals(callBarringData.getWrongPasswordAttemptsCounter().intValue(), 3);
        assertTrue(callBarringData.getNotificationToCSE());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(callBarringData.getExtensionContainer()));

        ArrayList<ExtCallBarringFeature> extCallBarringFeatures = callBarringData.getCallBarringFeatureList();
        assertNotNull(extCallBarringFeatures);
        assertEquals(extCallBarringFeatures.size(), 1);

        ExtCallBarringFeature extCallBarringFeature = extCallBarringFeatures.get(0);
        ExtBasicServiceCode extBasicServiceCode = extCallBarringFeature.getBasicService();
        ExtSSStatus extSSStatus = extCallBarringFeature.getSsStatus();
        assertEquals(extBasicServiceCode.getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.allBearerServices);
        assertTrue(extSSStatus.getBitQ());
        assertFalse(extSSStatus.getBitP());
        assertFalse(extSSStatus.getBitR());
        assertFalse(extSSStatus.getBitA());

        Password password = callBarringData.getPassword();
        assertNotNull(password);
        assertEquals(password.getData(), "0000");
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
        ExtBasicServiceCode extBasicServiceCode = new ExtBasicServiceCodeImpl(new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allBearerServices));
        final ExtCallBarringFeatureImpl extCallBarringFeature = new ExtCallBarringFeatureImpl(extBasicServiceCode,
                new ExtSSStatusImpl(true, false, false, false), null);
        CallBarringDataImpl callBarringData = new CallBarringDataImpl(new ArrayList<ExtCallBarringFeature>(){{add(extCallBarringFeature);}},
                new PasswordImpl("0000"), 3, true, MAPExtensionContainerTest.GetTestExtensionContainer());

        AsnOutputStream asnOS = new AsnOutputStream();
        callBarringData.encodeAll(asnOS);
        byte[] raw = asnOS.toByteArray();
        assertTrue(Arrays.equals(raw, data));
    }
}
