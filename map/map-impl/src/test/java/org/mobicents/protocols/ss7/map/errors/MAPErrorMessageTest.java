/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.map.errors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

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
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.mobicents.protocols.ss7.map.api.smstpdu.FailureCause;
import org.mobicents.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsDeliverReportTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserData;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TeleserviceCodeImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSStatusImpl;
import org.mobicents.protocols.ss7.map.smstpdu.DataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.smstpdu.FailureCauseImpl;
import org.mobicents.protocols.ss7.map.smstpdu.ProtocolIdentifierImpl;
import org.mobicents.protocols.ss7.map.smstpdu.SmsDeliverReportTpduImpl;
import org.mobicents.protocols.ss7.map.smstpdu.UserDataImpl;
import org.mobicents.protocols.ss7.tcap.asn.ParameterImpl;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
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

    private Parameter getDataSmDeliveryFailureV1() {
        Parameter par = new ParameterImpl();
        par.setData(new byte[] { 1 });
        par.setPrimitive(true);
        par.setTagClass(Tag.CLASS_UNIVERSAL);
        par.setTag(Tag.ENUMERATED);
        return par;
    }

    private Parameter getDataSmDeliveryFailureFull() {
        Parameter par = new ParameterImpl();
//        par.setData(new byte[] { 10, 1, 4, 4, 5, 1, 3, 5, 7, 9, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
//                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 });
        par.setData(new byte[] { 10, 1, 4, 4, 14, 0, -43, 7, 127, -10, 8, 1, 2, 0, 0, 0, 9, 9, 9, 48, 39, -96, 32, 48, 10, 6,
                3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33 });
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
        par.setData(new byte[] { (byte) 129, 1, 33, (byte) 131, 1, 17, (byte) 132, 1, 9 });
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

    private byte[] uData = { 1, 2, 0, 0, 0, 9, 9, 9 };

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
        assertNull(emSMDeliveryFailure.getSmsDeliverReportTpdu());
        assertNull(emSMDeliveryFailure.getExtensionContainer());
        assertEquals(emSMDeliveryFailure.getMapProtocolVersion(), 3);

        p = getDataSmDeliveryFailureFull();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.smDeliveryFailure);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmSMDeliveryFailure());
        emSMDeliveryFailure = em.getEmSMDeliveryFailure();
        assertEquals(emSMDeliveryFailure.getSMEnumeratedDeliveryFailureCause(), SMEnumeratedDeliveryFailureCause.scCongestion);
        assertNotNull(emSMDeliveryFailure.getSignalInfo());
        assertNotNull(emSMDeliveryFailure.getExtensionContainer());
        SmsDeliverReportTpdu tpdu = emSMDeliveryFailure.getSmsDeliverReportTpdu();
        assertEquals(tpdu.getFailureCause().getCode(), 0xd5);
        assertEquals(tpdu.getParameterIndicator().getCode(), 7);
        assertEquals(tpdu.getProtocolIdentifier().getCode(), 127);
        assertEquals(tpdu.getDataCodingScheme().getCode(), 246);
        assertEquals(tpdu.getUserData().getEncodedData(), uData);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emSMDeliveryFailure.getExtensionContainer()));
        assertEquals(emSMDeliveryFailure.getMapProtocolVersion(), 3);

        p = getDataSmDeliveryFailureV1();
        em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.smDeliveryFailure);
        ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
        em.decodeData(ais, p.getData().length);
        assertTrue(em.isEmSMDeliveryFailure());
        emSMDeliveryFailure = em.getEmSMDeliveryFailure();
        assertEquals(emSMDeliveryFailure.getSMEnumeratedDeliveryFailureCause(),
                SMEnumeratedDeliveryFailureCause.equipmentProtocolError);
        assertNull(emSMDeliveryFailure.getSignalInfo());
        assertNull(emSMDeliveryFailure.getExtensionContainer());
        assertEquals(emSMDeliveryFailure.getMapProtocolVersion(), 1);

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
        assertEquals(emSsIncompatibility.getSSCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
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

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageSMDeliveryFailure(3,
                SMEnumeratedDeliveryFailureCause.invalidSMEAddress, null, null);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataSmDeliveryFailure(), p);

        MAPErrorMessageSMDeliveryFailure smDeliveryFailure = fact.createMAPErrorMessageSMDeliveryFailure(3,
                SMEnumeratedDeliveryFailureCause.scCongestion, null, MAPExtensionContainerTest.GetTestExtensionContainer());
        FailureCause failureCause = new FailureCauseImpl(213);
        ProtocolIdentifier protocolIdentifier = new ProtocolIdentifierImpl(127);
        DataCodingScheme dataCodingScheme = new DataCodingSchemeImpl(246);
        UserData userData = new UserDataImpl(uData, dataCodingScheme, uData.length, false, null);
        SmsDeliverReportTpdu tpdu = new SmsDeliverReportTpduImpl(failureCause, protocolIdentifier, userData);
        smDeliveryFailure.setSmsDeliverReportTpdu(tpdu);
        em = (MAPErrorMessageImpl) smDeliveryFailure;
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataSmDeliveryFailureFull(), p);

        em = (MAPErrorMessageImpl) fact.createMAPErrorMessageSMDeliveryFailure(1,
                SMEnumeratedDeliveryFailureCause.equipmentProtocolError, null, null);
        aos = new AsnOutputStream();
        em.encodeData(aos);
        p = new ParameterImpl();
        p.setTagClass(em.getTagClass());
        p.setTag(em.getTag());
        p.setPrimitive(em.getIsPrimitive());
        p.setData(aos.toByteArray());
        assertParameter(getDataSmDeliveryFailureV1(), p);

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

        SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.cfu);
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

    @Test(groups = { "functional.xml.serialize", "dialog.message" })
    public void testXMLSerialize() throws Exception {
        MAPErrorMessageFactoryImpl fact = new MAPErrorMessageFactoryImpl();

        // MAPErrorMessageAbsentSubscriber
        MAPErrorMessageAbsentSubscriberImpl em = (MAPErrorMessageAbsentSubscriberImpl) fact
                .createMAPErrorMessageAbsentSubscriber(MAPExtensionContainerTest.GetTestExtensionContainer(),
                        AbsentSubscriberReason.purgedMS);
        em.setMwdSet(true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em, "mapErrorMessageAbsentSubscriber", MAPErrorMessageAbsentSubscriberImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageAbsentSubscriberImpl copy = reader.read("mapErrorMessageAbsentSubscriber",
                MAPErrorMessageAbsentSubscriberImpl.class);
        assertEquals(copy.getAbsentSubscriberReason(), em.getAbsentSubscriberReason());
        assertEquals(copy.getExtensionContainer(), em.getExtensionContainer());
        assertEquals(copy.getMwdSet(), em.getMwdSet());
        // assertNull(em.getMwdSet());

        // MAPErrorMessageAbsentSubscriberSM
        MAPErrorMessageAbsentSubscriberSMImpl em1 = (MAPErrorMessageAbsentSubscriberSMImpl) fact
                .createMAPErrorMessageAbsentSubscriberSM(AbsentSubscriberDiagnosticSM.IMSIDetached,
                        MAPExtensionContainerTest.GetTestExtensionContainer(), AbsentSubscriberDiagnosticSM.MSPurgedForGPRS);

        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em1, "mapErrorMessageAbsentSubscriberSM", MAPErrorMessageAbsentSubscriberSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageAbsentSubscriberSMImpl copy1 = reader.read("mapErrorMessageAbsentSubscriberSM",
                MAPErrorMessageAbsentSubscriberSMImpl.class);
        assertEquals(copy1.getAbsentSubscriberDiagnosticSM(), em1.getAbsentSubscriberDiagnosticSM());
        assertEquals(copy1.getAdditionalAbsentSubscriberDiagnosticSM(), em1.getAdditionalAbsentSubscriberDiagnosticSM());
        assertEquals(copy1.getExtensionContainer(), em1.getExtensionContainer());

        // MAPErrorMessageBusySubscriber
        MAPErrorMessageBusySubscriberImpl em2 = (MAPErrorMessageBusySubscriberImpl) fact.createMAPErrorMessageBusySubscriber(
                MAPExtensionContainerTest.GetTestExtensionContainer(), true, true);
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em2, "mapErrorMessageBusySubscriber", MAPErrorMessageBusySubscriberImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageBusySubscriberImpl copy2 = reader.read("mapErrorMessageBusySubscriber",
                MAPErrorMessageBusySubscriberImpl.class);
        assertEquals(copy2.getCcbsPossible(), em2.getCcbsPossible());
        assertEquals(copy2.getCcbsBusy(), em2.getCcbsBusy());

        // MAPErrorMessageCallBarred
        MAPErrorMessageCallBarredImpl em3 = (MAPErrorMessageCallBarredImpl) fact.createMAPErrorMessageCallBarred(3L,
                CallBarringCause.operatorBarring, null, null);

        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em3, "mapErrorMessageCallBarred", MAPErrorMessageCallBarredImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageCallBarredImpl copy3 = reader.read("mapErrorMessageCallBarred", MAPErrorMessageCallBarredImpl.class);
        assertEquals(copy3.getMapProtocolVersion(), em3.getMapProtocolVersion());
        assertEquals(copy3.getCallBarringCause(), em3.getCallBarringCause());

        // MAPErrorMessageCUGReject
        MAPErrorMessageCUGRejectImpl em4 = (MAPErrorMessageCUGRejectImpl) fact.createMAPErrorMessageCUGReject(
                CUGRejectCause.subscriberNotMemberOfCUG, MAPExtensionContainerTest.GetTestExtensionContainer());

        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em4, "mapErrorMessageCUGReject", MAPErrorMessageCUGRejectImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageCUGRejectImpl copy4 = reader.read("mapErrorMessageCUGReject", MAPErrorMessageCUGRejectImpl.class);
        assertEquals(copy4.getCUGRejectCause(), em4.getCUGRejectCause());
        assertEquals(copy4.getExtensionContainer(), em4.getExtensionContainer());

        // MAPErrorMessageExtensionContainer
        MAPErrorMessageExtensionContainerImpl em5 = (MAPErrorMessageExtensionContainerImpl) fact
                .createMAPErrorMessageExtensionContainer(36L, MAPExtensionContainerTest.GetTestExtensionContainer());

        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em5, "mapErrorMessageExtensionContainer", MAPErrorMessageExtensionContainerImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageExtensionContainerImpl copy5 = reader.read("mapErrorMessageExtensionContainer",
                MAPErrorMessageExtensionContainerImpl.class);
        assertEquals(copy5.getExtensionContainer(), em5.getExtensionContainer());
        assertEquals(copy5.getErrorCode(), em5.getErrorCode());

        // MAPErrorMessageFacilityNotSup
        MAPErrorMessageFacilityNotSupImpl em6 = (MAPErrorMessageFacilityNotSupImpl) fact.createMAPErrorMessageFacilityNotSup(
                MAPExtensionContainerTest.GetTestExtensionContainer(), true, true);

        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em6, "mapErrorMessageFacilityNotSup", MAPErrorMessageFacilityNotSupImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageFacilityNotSupImpl copy6 = reader.read("mapErrorMessageFacilityNotSup",
                MAPErrorMessageFacilityNotSupImpl.class);
        assertEquals(copy6.getShapeOfLocationEstimateNotSupported(), em6.getShapeOfLocationEstimateNotSupported());
        assertEquals(copy6.getNeededLcsCapabilityNotSupportedInServingNode(),
                em6.getNeededLcsCapabilityNotSupportedInServingNode());
        assertEquals(copy6.getErrorCode(), em6.getErrorCode());
        assertEquals(copy6.getExtensionContainer(), em6.getExtensionContainer());

        // MAPErrorMessageParameterless
        MAPErrorMessageParameterlessImpl em7 = (MAPErrorMessageParameterlessImpl) fact.createMAPErrorMessageParameterless(1l);

        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em7, "mapErrorMessageParameterless", MAPErrorMessageParameterlessImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageParameterlessImpl copy7 = reader.read("mapErrorMessageParameterless",
                MAPErrorMessageParameterlessImpl.class);
        assertEquals(copy7.isEmParameterless(), em7.isEmParameterless());
        assertEquals(copy7.getErrorCode(), em7.getErrorCode());

        // MAPErrorMessagePositionMethodFailure
        MAPErrorMessagePositionMethodFailureImpl em8 = (MAPErrorMessagePositionMethodFailureImpl) fact
                .createMAPErrorMessagePositionMethodFailure(PositionMethodFailureDiagnostic.locationProcedureNotCompleted,
                        MAPExtensionContainerTest.GetTestExtensionContainer());
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em8, "mapErrorMessagePositionMethodFailure", MAPErrorMessagePositionMethodFailureImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessagePositionMethodFailureImpl copy8 = reader.read("mapErrorMessagePositionMethodFailure",
                MAPErrorMessagePositionMethodFailureImpl.class);
        assertEquals(copy8.getErrorCode(), em8.getErrorCode());
        assertEquals(copy8.getPositionMethodFailureDiagnostic(), em8.getPositionMethodFailureDiagnostic());
        assertEquals(copy8.getExtensionContainer(), em8.getExtensionContainer());

        // MAPErrorMessagePwRegistrationFailure
        MAPErrorMessagePwRegistrationFailureImpl em9 = (MAPErrorMessagePwRegistrationFailureImpl) fact
                .createMAPErrorMessagePwRegistrationFailure(PWRegistrationFailureCause.newPasswordsMismatch);
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em9, "mapErrorMessagePwRegistrationFailure", MAPErrorMessagePwRegistrationFailureImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessagePwRegistrationFailureImpl copy9 = reader.read("mapErrorMessagePwRegistrationFailure",
                MAPErrorMessagePwRegistrationFailureImpl.class);
        assertEquals(copy9.getErrorCode(), em9.getErrorCode());
        assertEquals(copy9.getPWRegistrationFailureCause(), em9.getPWRegistrationFailureCause());

        // AdditionalRoamingNotAllowedCause
        MAPErrorMessageRoamingNotAllowedImpl em10 = (MAPErrorMessageRoamingNotAllowedImpl) fact
                .createMAPErrorMessageRoamingNotAllowed(RoamingNotAllowedCause.plmnRoamingNotAllowed,
                        MAPExtensionContainerTest.GetTestExtensionContainer(),
                        AdditionalRoamingNotAllowedCause.supportedRATTypesNotAllowed);
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em10, "mapErrorMessageRoamingNotAllowed", MAPErrorMessageRoamingNotAllowedImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageRoamingNotAllowedImpl copy10 = reader.read("mapErrorMessageRoamingNotAllowed",
                MAPErrorMessageRoamingNotAllowedImpl.class);
        assertEquals(copy10.getErrorCode(), em10.getErrorCode());
        assertEquals(copy10.getRoamingNotAllowedCause(), em10.getRoamingNotAllowedCause());
        assertEquals(copy10.getAdditionalRoamingNotAllowedCause(), em10.getAdditionalRoamingNotAllowedCause());
        assertEquals(copy10.getExtensionContainer(), em10.getExtensionContainer());

        // MAPErrorMessageSMDeliveryFailureImpl
        MAPErrorMessageSMDeliveryFailureImpl em11 = (MAPErrorMessageSMDeliveryFailureImpl) fact
                .createMAPErrorMessageSMDeliveryFailure(3, SMEnumeratedDeliveryFailureCause.invalidSMEAddress, null, null);
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em11, "mapErrorMessageSMDeliveryFailure", MAPErrorMessageSMDeliveryFailureImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageSMDeliveryFailureImpl copy11 = reader.read("mapErrorMessageSMDeliveryFailure",
                MAPErrorMessageSMDeliveryFailureImpl.class);
        assertEquals(copy11.getErrorCode(), em11.getErrorCode());
        assertEquals(copy11.getSMEnumeratedDeliveryFailureCause(), em11.getSMEnumeratedDeliveryFailureCause());
        assertEquals(copy11.getMapProtocolVersion(), em11.getMapProtocolVersion());

        MAPErrorMessageSMDeliveryFailure smDeliveryFailure = fact.createMAPErrorMessageSMDeliveryFailure(3,
                SMEnumeratedDeliveryFailureCause.scCongestion, null, MAPExtensionContainerTest.GetTestExtensionContainer());
        FailureCause failureCause = new FailureCauseImpl(213);
        ProtocolIdentifier protocolIdentifier = new ProtocolIdentifierImpl(127);
        DataCodingScheme dataCodingScheme = new DataCodingSchemeImpl(246);
        UserData userData = new UserDataImpl(uData, dataCodingScheme, uData.length, false, null);
        SmsDeliverReportTpdu tpdu = new SmsDeliverReportTpduImpl(failureCause, protocolIdentifier, userData);
        smDeliveryFailure.setSmsDeliverReportTpdu(tpdu);
        em11 = (MAPErrorMessageSMDeliveryFailureImpl) smDeliveryFailure;
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em11, "mapErrorMessageSMDeliveryFailure", MAPErrorMessageSMDeliveryFailureImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        copy11 = reader.read("mapErrorMessageSMDeliveryFailure", MAPErrorMessageSMDeliveryFailureImpl.class);
        assertEquals(copy11.getErrorCode(), em11.getErrorCode());
        assertEquals(copy11.getSMEnumeratedDeliveryFailureCause(), em11.getSMEnumeratedDeliveryFailureCause());
        assertEquals(copy11.getMapProtocolVersion(), em11.getMapProtocolVersion());
        assertEquals(copy11.getSignalInfo(), em11.getSignalInfo());

        // MAPErrorMessageSsErrorStatus
        MAPErrorMessageSsErrorStatusImpl em12 = (MAPErrorMessageSsErrorStatusImpl) fact.createMAPErrorMessageSsErrorStatus(
                false, true, true, false);
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em12, "mapErrorMessageSsErrorStatus", MAPErrorMessageSsErrorStatusImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageSsErrorStatusImpl copy12 = reader.read("mapErrorMessageSsErrorStatus",
                MAPErrorMessageSsErrorStatusImpl.class);
        assertEquals(copy12.getErrorCode(), em12.getErrorCode());
        assertEquals(copy12.getData(), em12.getData());
        assertEquals(copy12.getQBit(), em12.getQBit());
        assertEquals(copy12.getPBit(), em12.getPBit());
        assertEquals(copy12.getRBit(), em12.getRBit());
        assertEquals(copy12.getABit(), em12.getABit());

        // MAPErrorMessageSsIncompatibility
        SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.cfu);
        TeleserviceCode teleservice = new TeleserviceCodeImpl(TeleserviceCodeValue.telephony);
        BasicServiceCode basicService = new BasicServiceCodeImpl(teleservice);
        SSStatus ssStatus = new SSStatusImpl(true, false, false, true);
        MAPErrorMessageSsIncompatibilityImpl em13 = (MAPErrorMessageSsIncompatibilityImpl) fact
                .createMAPErrorMessageSsIncompatibility(ssCode, basicService, ssStatus);

        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em13, "mapErrorMessageSsIncompatibility", MAPErrorMessageSsIncompatibilityImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageSsIncompatibilityImpl copy13 = reader.read("mapErrorMessageSsIncompatibility",
                MAPErrorMessageSsIncompatibilityImpl.class);
        assertEquals(copy13.getErrorCode(), em13.getErrorCode());
        assertEquals(copy13.getSSCode(), em13.getSSCode());
        assertEquals(copy13.getBasicService(), em13.getBasicService());

        // MAPErrorMessageSubscriberBusyForMtSms
        MAPErrorMessageSubscriberBusyForMtSmsImpl em14 = (MAPErrorMessageSubscriberBusyForMtSmsImpl) fact
                .createMAPErrorMessageSubscriberBusyForMtSms(MAPExtensionContainerTest.GetTestExtensionContainer(), true);

        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em14, "mapErrorMessageSubscriberBusyForMtSms", MAPErrorMessageSubscriberBusyForMtSmsImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageSubscriberBusyForMtSmsImpl copy14 = reader.read("mapErrorMessageSubscriberBusyForMtSms",
                MAPErrorMessageSubscriberBusyForMtSmsImpl.class);
        assertEquals(copy14.getErrorCode(), em14.getErrorCode());
        assertEquals(copy14.getGprsConnectionSuspended(), em14.getGprsConnectionSuspended());

        // MAPErrorMessageSystemFailure
        MAPErrorMessageSystemFailureImpl em15 = (MAPErrorMessageSystemFailureImpl) fact.createMAPErrorMessageSystemFailure(2,
                NetworkResource.plmn, null, null);
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em15, "mapErrorMessageSystemFailure", MAPErrorMessageSystemFailureImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageSystemFailureImpl copy15 = reader.read("mapErrorMessageSystemFailure",
                MAPErrorMessageSystemFailureImpl.class);
        assertEquals(copy15.getErrorCode(), em15.getErrorCode());
        assertEquals(copy15.getMapProtocolVersion(), em15.getMapProtocolVersion());
        assertEquals(copy15.getNetworkResource(), em15.getNetworkResource());
        assertEquals(copy15.getAdditionalNetworkResource(), em15.getAdditionalNetworkResource());

        // MAPErrorMessageUnauthorizedLCSClient
        MAPErrorMessageUnauthorizedLCSClientImpl em16 = (MAPErrorMessageUnauthorizedLCSClientImpl) fact
                .createMAPErrorMessageUnauthorizedLCSClient(UnauthorizedLCSClientDiagnostic.callToClientNotSetup,
                        MAPExtensionContainerTest.GetTestExtensionContainer());
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em16, "mapErrorMessageUnauthorizedLCSClient", MAPErrorMessageUnauthorizedLCSClientImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageUnauthorizedLCSClientImpl copy16 = reader.read("mapErrorMessageUnauthorizedLCSClient",
                MAPErrorMessageUnauthorizedLCSClientImpl.class);
        assertEquals(copy16.getErrorCode(), em16.getErrorCode());
        assertEquals(copy16.getUnauthorizedLCSClientDiagnostic(), em16.getUnauthorizedLCSClientDiagnostic());
        assertEquals(copy16.getExtensionContainer(), em16.getExtensionContainer());

        // MAPErrorMessageUnknownSubscriberImpl
        MAPErrorMessageUnknownSubscriberImpl em17 = (MAPErrorMessageUnknownSubscriberImpl) fact
                .createMAPErrorMessageUnknownSubscriber(MAPExtensionContainerTest.GetTestExtensionContainer(),
                        UnknownSubscriberDiagnostic.gprsSubscriptionUnknown);
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(em17, "mapErrorMessageUnknownSubscriber", MAPErrorMessageUnknownSubscriberImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        MAPErrorMessageUnknownSubscriberImpl copy17 = reader.read("mapErrorMessageUnknownSubscriber",
                MAPErrorMessageUnknownSubscriberImpl.class);
        assertEquals(copy17.getErrorCode(), em17.getErrorCode());
        assertEquals(copy17.getUnknownSubscriberDiagnostic(), em17.getUnknownSubscriberDiagnostic());
        assertEquals(copy17.getExtensionContainer(), em17.getExtensionContainer());
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
