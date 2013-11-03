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

package org.mobicents.protocols.ss7.map.errors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.errors.AbsentSubscriberDiagnosticSM;
import org.mobicents.protocols.ss7.map.api.errors.AbsentSubscriberReason;
import org.mobicents.protocols.ss7.map.api.errors.AdditionalNetworkResource;
import org.mobicents.protocols.ss7.map.api.errors.AdditionalRoamingNotAllowedCause;
import org.mobicents.protocols.ss7.map.api.errors.CUGRejectCause;
import org.mobicents.protocols.ss7.map.api.errors.CallBarringCause;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriber;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriberSM;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageBusySubscriber;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageCUGReject;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageCallBarred;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageFacilityNotSup;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessagePositionMethodFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessagePwRegistrationFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageRoamingNotAllowed;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSsErrorStatus;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSsIncompatibility;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSubscriberBusyForMtSms;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSystemFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageUnauthorizedLCSClient;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageUnknownSubscriber;
import org.mobicents.protocols.ss7.map.api.errors.PWRegistrationFailureCause;
import org.mobicents.protocols.ss7.map.api.errors.PositionMethodFailureDiagnostic;
import org.mobicents.protocols.ss7.map.api.errors.RoamingNotAllowedCause;
import org.mobicents.protocols.ss7.map.api.errors.SMEnumeratedDeliveryFailureCause;
import org.mobicents.protocols.ss7.map.api.errors.UnauthorizedLCSClientDiagnostic;
import org.mobicents.protocols.ss7.map.api.errors.UnknownSubscriberDiagnostic;
import org.mobicents.protocols.ss7.map.api.primitives.NetworkResource;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TeleserviceCodeImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSStatusImpl;
import org.mobicents.protocols.ss7.tcap.asn.ParameterImpl;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPErrorMessageTest {

    private Parameter getDataExtContainerFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48,
                11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataSmDeliveryFailure() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 10, 1, 5 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
        // 10, 1, 0
    }

    private Parameter getDataSmDeliveryFailureFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 10, 1, 4, 4, 5, 1, 3, 5, 7, 9, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataAbsentSubscriberSM() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 2, 1, 1 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
        // 2, 1, 0
        // 2, 1, 4
    }

    private Parameter getDataAbsentSubscriberSMFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 2, 1, 0, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
                3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 1, 6 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataCallBarred() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 10, 1, 1 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataCallBarredFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 10, 1, 1, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
                3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 129, 0 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataSystemFailure() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 0 });
        par.setPrimitive(true);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.ENUMERATED);
        return par;
    }

    private Parameter getDataSystemFailureFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 10, 1, 2, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
                3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 1, 3 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataFacilityNotSupFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48,
                11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 0, (byte) 129, 0 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataUnknownSubscriberFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48,
                11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, 10, 1, 1 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataSubscriberBusyForMTSMSFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48,
                11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, 5, 0 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataAbsentSubscriberFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48,
                11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 1, 3 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataAbsentSubscriberV1() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { (byte) 255 });
        par.setPrimitive(true);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.BOOLEAN);
        return par;
    }

    private Parameter getDataUnauthorizedLCSClientFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { (byte) 128, 1, 2, (byte) 161, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataPositionMethodFailureFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { (byte) 128, 1, 4, (byte) 161, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataBusySubscriberFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48,
                11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 0, (byte) 129, 0 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataCUGRejectFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 10, 1, 1, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
                3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataRoamingNotAllowedFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 10, 1, 0, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
                3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 1, 0 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataSsErrorStatusFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 6 });
        par.setPrimitive(true);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.STRING_OCTET);
        return par;
    }

    private Parameter getDataSsIncompatibilityFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { (byte) 129, 1, 35, (byte) 131, 1, 17, (byte) 132, 1, 9 });
        par.setPrimitive(false);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.SEQUENCE);
        return par;
    }

    private Parameter getDataPwRegistrationFailureFull() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 2 });
        par.setPrimitive(true);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.ENUMERATED);
        return par;
    }

    @Test(groups = { "functional.decode", "dialog.message" })
    public void testDecode() throws Exception {

        MAPErrorMessageFactoryImpl fact = new MAPErrorMessageFactoryImpl();

        Parameter p = getDataSmDeliveryFailure();
        MAPErrorMessageImpl em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.smDeliveryFailure);
        AsnInputStream ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmSMDeliveryFailure());
        MAPErrorMessageSMDeliveryFailure emSMDeliveryFailure = em.getEmSMDeliveryFailure();
        assertEquals(emSMDeliveryFailure.getSMEnumeratedDeliveryFailureCause(),
                SMEnumeratedDeliveryFailureCause.invalidSMEAddress);
        assertNull(emSMDeliveryFailure.getSignalInfo());
        assertNull(emSMDeliveryFailure.getExtensionContainer());

        p = getDataSmDeliveryFailureFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.smDeliveryFailure);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmSMDeliveryFailure());
        emSMDeliveryFailure = em.getEmSMDeliveryFailure();
        assertEquals(emSMDeliveryFailure.getSMEnumeratedDeliveryFailureCause(), SMEnumeratedDeliveryFailureCause.scCongestion);
        assertNotNull(emSMDeliveryFailure.getSignalInfo());
        assertNotNull(emSMDeliveryFailure.getExtensionContainer());
        assertTrue(Arrays.equals(emSMDeliveryFailure.getSignalInfo(), new byte[] { 1, 3, 5, 7, 9 }));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emSMDeliveryFailure.getExtensionContainer()));

        p = getDataAbsentSubscriberSM();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.absentSubscriberSM);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmAbsentSubscriberSM());
        MAPErrorMessageAbsentSubscriberSM emAbsentSubscriberSMImpl = em.getEmAbsentSubscriberSM();
        assertEquals(emAbsentSubscriberSMImpl.getAbsentSubscriberDiagnosticSM(), AbsentSubscriberDiagnosticSM.IMSIDetached);
        assertNull(emAbsentSubscriberSMImpl.getAdditionalAbsentSubscriberDiagnosticSM());
        assertNull(emAbsentSubscriberSMImpl.getExtensionContainer());

        p = getDataAbsentSubscriberSMFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.absentSubscriberSM);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmAbsentSubscriberSM());
        emAbsentSubscriberSMImpl = em.getEmAbsentSubscriberSM();
        assertEquals(emAbsentSubscriberSMImpl.getAbsentSubscriberDiagnosticSM(),
                AbsentSubscriberDiagnosticSM.NoPagingResponseViaTheMSC);
        assertEquals(emAbsentSubscriberSMImpl.getAdditionalAbsentSubscriberDiagnosticSM(),
                AbsentSubscriberDiagnosticSM.GPRSDetached);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emAbsentSubscriberSMImpl.getExtensionContainer()));

        p = getDataSystemFailure();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.systemFailure);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmSystemFailure());
        MAPErrorMessageSystemFailure emSystemFailure = em.getEmSystemFailure();
        assertEquals(emSystemFailure.getMapProtocolVersion(), 2);
        assertEquals(emSystemFailure.getNetworkResource(), NetworkResource.plmn);
        assertNull(emSystemFailure.getAdditionalNetworkResource());
        assertNull(emSystemFailure.getExtensionContainer());

        p = getDataSystemFailureFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.systemFailure);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmSystemFailure());
        emSystemFailure = em.getEmSystemFailure();
        assertEquals(emSystemFailure.getMapProtocolVersion(), 3);
        assertEquals(emSystemFailure.getNetworkResource(), NetworkResource.vlr);
        assertEquals(emSystemFailure.getAdditionalNetworkResource(), AdditionalNetworkResource.gsmSCF);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emAbsentSubscriberSMImpl.getExtensionContainer()));

        p = getDataCallBarred();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.callBarred);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmCallBarred());
        MAPErrorMessageCallBarred emCallBarred = em.getEmCallBarred();
        assertEquals(emCallBarred.getMapProtocolVersion(), 3);
        assertEquals(emCallBarred.getCallBarringCause(), CallBarringCause.operatorBarring);
        assertEquals((boolean) emCallBarred.getUnauthorisedMessageOriginator(), false);
        assertNull(emCallBarred.getExtensionContainer());

        p = getDataCallBarredFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.callBarred);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmCallBarred());
        emCallBarred = em.getEmCallBarred();
        assertEquals(emCallBarred.getMapProtocolVersion(), 3);
        assertEquals(emCallBarred.getCallBarringCause(), CallBarringCause.operatorBarring);
        assertEquals((boolean) emCallBarred.getUnauthorisedMessageOriginator(), true);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emCallBarred.getExtensionContainer()));

        p = getDataFacilityNotSupFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.facilityNotSupported);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmFacilityNotSup());
        MAPErrorMessageFacilityNotSup emFacilityNotSup = em.getEmFacilityNotSup();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emFacilityNotSup.getExtensionContainer()));
        assertEquals((boolean) emFacilityNotSup.getShapeOfLocationEstimateNotSupported(), true);
        assertEquals((boolean) emFacilityNotSup.getNeededLcsCapabilityNotSupportedInServingNode(), true);

        p = getDataUnknownSubscriberFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.unknownSubscriber);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmUnknownSubscriber());
        MAPErrorMessageUnknownSubscriber emUnknownSubscriber = em.getEmUnknownSubscriber();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emUnknownSubscriber.getExtensionContainer()));
        assertEquals(emUnknownSubscriber.getUnknownSubscriberDiagnostic(), UnknownSubscriberDiagnostic.gprsSubscriptionUnknown);

        p = getDataSubscriberBusyForMTSMSFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.subscriberBusyForMTSMS);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmSubscriberBusyForMtSms());
        MAPErrorMessageSubscriberBusyForMtSms emSubscriberBusyForMtSms = em.getEmSubscriberBusyForMtSms();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emSubscriberBusyForMtSms.getExtensionContainer()));
        assertEquals((boolean) emSubscriberBusyForMtSms.getGprsConnectionSuspended(), true);

        p = getDataAbsentSubscriberFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.absentSubscriber);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmAbsentSubscriber());
        MAPErrorMessageAbsentSubscriber emAbsentSubscriber = em.getEmAbsentSubscriber();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emAbsentSubscriber.getExtensionContainer()));
        assertEquals(emAbsentSubscriber.getAbsentSubscriberReason(), AbsentSubscriberReason.purgedMS);
        assertNull(emAbsentSubscriber.getMwdSet());

        p = getDataAbsentSubscriberV1();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.absentSubscriber);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmAbsentSubscriber());
        emAbsentSubscriber = em.getEmAbsentSubscriber();
        assertNull(emAbsentSubscriber.getExtensionContainer());
        assertNull(emAbsentSubscriber.getAbsentSubscriberReason());
        assertTrue(emAbsentSubscriber.getMwdSet());

        p = getDataUnauthorizedLCSClientFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.unauthorizedLCSClient);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmUnauthorizedLCSClient());
        MAPErrorMessageUnauthorizedLCSClient emUnauthorizedLCSClient = em.getEmUnauthorizedLCSClient();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emUnauthorizedLCSClient.getExtensionContainer()));
        assertEquals(emUnauthorizedLCSClient.getUnauthorizedLCSClientDiagnostic(),
                UnauthorizedLCSClientDiagnostic.callToClientNotSetup);

        p = getDataPositionMethodFailureFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.positionMethodFailure);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmPositionMethodFailure());
        MAPErrorMessagePositionMethodFailure emPositionMethodFailure = em.getEmPositionMethodFailure();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emPositionMethodFailure.getExtensionContainer()));
        assertEquals(emPositionMethodFailure.getPositionMethodFailureDiagnostic(),
                PositionMethodFailureDiagnostic.locationProcedureNotCompleted);

        p = getDataBusySubscriberFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.busySubscriber);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmBusySubscriber());
        MAPErrorMessageBusySubscriber emBusySubscriber = em.getEmBusySubscriber();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emBusySubscriber.getExtensionContainer()));
        assertTrue(emBusySubscriber.getCcbsPossible());
        assertTrue(emBusySubscriber.getCcbsBusy());

        p = getDataCUGRejectFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.cugReject);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmCUGReject());
        MAPErrorMessageCUGReject emCUGReject = em.getEmCUGReject();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emCUGReject.getExtensionContainer()));
        assertEquals(emCUGReject.getCUGRejectCause(), CUGRejectCause.subscriberNotMemberOfCUG);

        p = getDataRoamingNotAllowedFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.roamingNotAllowed);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmRoamingNotAllowed());
        MAPErrorMessageRoamingNotAllowed emRoamingNotAllowed = em.getEmRoamingNotAllowed();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emRoamingNotAllowed.getExtensionContainer()));
        assertEquals(emRoamingNotAllowed.getRoamingNotAllowedCause(), RoamingNotAllowedCause.plmnRoamingNotAllowed);
        assertEquals(emRoamingNotAllowed.getAdditionalRoamingNotAllowedCause(),
                AdditionalRoamingNotAllowedCause.supportedRATTypesNotAllowed);

        p = getDataSsErrorStatusFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.ssErrorStatus);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmSsErrorStatus());
        MAPErrorMessageSsErrorStatus emSsErrorStatus = em.getEmSsErrorStatus();
        assertFalse(emSsErrorStatus.getQBit());
        assertTrue(emSsErrorStatus.getPBit());
        assertTrue(emSsErrorStatus.getRBit());
        assertFalse(emSsErrorStatus.getABit());
        assertEquals(emSsErrorStatus.getData(), 6);

        p = getDataSsIncompatibilityFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.ssIncompatibility);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmSsIncompatibility());
        MAPErrorMessageSsIncompatibility emSsIncompatibility = em.getEmSsIncompatibility();
        assertEquals(emSsIncompatibility.getSSCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cellBroadcast);
        assertEquals(emSsIncompatibility.getBasicService().getTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.telephony);
        assertTrue(emSsIncompatibility.getSSStatus().getQBit());
        assertFalse(emSsIncompatibility.getSSStatus().getPBit());
        assertFalse(emSsIncompatibility.getSSStatus().getRBit());
        assertTrue(emSsIncompatibility.getSSStatus().getABit());

        p = getDataPwRegistrationFailureFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.pwRegistrationFailure);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmPwRegistrationFailure());
        MAPErrorMessagePwRegistrationFailure emPwRegistrationFailure = em.getEmPwRegistrationFailure();
        assertEquals(emPwRegistrationFailure.getPWRegistrationFailureCause(), PWRegistrationFailureCause.newPasswordsMismatch);
    }

    @Test(groups = { "functional.encode", "dialog.message" })
    public void testEncode() throws Exception {

        MAPErrorMessageFactoryImpl fact = new MAPErrorMessageFactoryImpl();

        MAPErrorMessageImpl em = (MAPErrorMessageImpl) fact.createMAPErrorMessageExtensionContainer(36L,
                MAPExtensionContainerTest.GetTestExtensionContainer());
        AsnOutputStream aos = new AsnOutputStream();
        em.encodeData(aos);
        Parameter p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataExtContainerFull(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageSMDeliveryFailure(
                SMEnumeratedDeliveryFailureCause.invalidSMEAddress, null, null);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataSmDeliveryFailure(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageSMDeliveryFailure(SMEnumeratedDeliveryFailureCause.scCongestion,
                new byte[] { 1, 3, 5, 7, 9 }, MAPExtensionContainerTest.GetTestExtensionContainer());
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataSmDeliveryFailureFull(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageAbsentSubscriberSM(AbsentSubscriberDiagnosticSM.IMSIDetached,
                null, null);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataAbsentSubscriberSM(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageAbsentSubscriberSM(
                AbsentSubscriberDiagnosticSM.NoPagingResponseViaTheMSC, MAPExtensionContainerTest.GetTestExtensionContainer(),
                AbsentSubscriberDiagnosticSM.GPRSDetached);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataAbsentSubscriberSMFull(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageSystemFailure(2, NetworkResource.plmn, null, null);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataSystemFailure(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageSystemFailure(3, NetworkResource.vlr,
                AdditionalNetworkResource.gsmSCF, MAPExtensionContainerTest.GetTestExtensionContainer());
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataSystemFailureFull(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageCallBarred(3L, CallBarringCause.operatorBarring, null, null);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataCallBarred(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageCallBarred(3L, CallBarringCause.operatorBarring,
                MAPExtensionContainerTest.GetTestExtensionContainer(), true);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataCallBarredFull(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageFacilityNotSup(
                MAPExtensionContainerTest.GetTestExtensionContainer(), true, true);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataFacilityNotSupFull(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageUnknownSubscriber(
                MAPExtensionContainerTest.GetTestExtensionContainer(), UnknownSubscriberDiagnostic.gprsSubscriptionUnknown);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataUnknownSubscriberFull(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageSubscriberBusyForMtSms(
                MAPExtensionContainerTest.GetTestExtensionContainer(), true);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataSubscriberBusyForMTSMSFull(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageAbsentSubscriber(
                MAPExtensionContainerTest.GetTestExtensionContainer(), AbsentSubscriberReason.purgedMS);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataAbsentSubscriberFull(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageAbsentSubscriber(true);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataAbsentSubscriberV1(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageUnauthorizedLCSClient(
                UnauthorizedLCSClientDiagnostic.callToClientNotSetup, MAPExtensionContainerTest.GetTestExtensionContainer());
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataUnauthorizedLCSClientFull(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessagePositionMethodFailure(
                PositionMethodFailureDiagnostic.locationProcedureNotCompleted,
                MAPExtensionContainerTest.GetTestExtensionContainer());
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataPositionMethodFailureFull(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageBusySubscriber(
                MAPExtensionContainerTest.GetTestExtensionContainer(), true, true);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataBusySubscriberFull(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageCUGReject(CUGRejectCause.subscriberNotMemberOfCUG,
                MAPExtensionContainerTest.GetTestExtensionContainer());
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataCUGRejectFull(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageRoamingNotAllowed(RoamingNotAllowedCause.plmnRoamingNotAllowed,
                MAPExtensionContainerTest.GetTestExtensionContainer(),
                AdditionalRoamingNotAllowedCause.supportedRATTypesNotAllowed);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataRoamingNotAllowedFull(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageSsErrorStatus(false, true, true, false);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataSsErrorStatusFull(), p);

        SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.cellBroadcast);
        TeleserviceCode teleservice = new TeleserviceCodeImpl(TeleserviceCodeValue.telephony);
        BasicServiceCode basicService = new BasicServiceCodeImpl(teleservice);
        SSStatus ssStatus = new SSStatusImpl(true, false, false, true);
        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageSsIncompatibility(ssCode, basicService, ssStatus);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataSsIncompatibilityFull(), p);

        em = (MAPErrorMessageImpl) fact
                .createMAPErrorMessagePwRegistrationFailure(PWRegistrationFailureCause.newPasswordsMismatch);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataPwRegistrationFailureFull(), p);
    }

    private void assertParameter(Parameter p2, Parameter p1) {
        assertNotNull(p1);
        assertNotNull(p2);
        assertEquals(p2.getTagClass(), p1.getTagClass());
        assertEquals(p2.getTag(), p1.getTag());
        assertEquals(p2.isPrimitive(), p1.isPrimitive());
        assertTrue(Arrays.equals(p1.getData(), p2.getData()));
    }
}
