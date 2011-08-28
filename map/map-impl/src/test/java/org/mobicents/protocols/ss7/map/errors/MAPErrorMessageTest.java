/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import java.util.Arrays;

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.errors.AbsentSubscriberReason;
import org.mobicents.protocols.ss7.map.api.errors.AdditionalNetworkResource;
import org.mobicents.protocols.ss7.map.api.errors.CallBarringCause;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriber;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriberSM;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageCallBarred;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageFacilityNotSup;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessagePositionMethodFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSubscriberBusyForMtSms;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSystemFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageUnauthorizedLCSClient;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageUnknownSubscriber;
import org.mobicents.protocols.ss7.map.api.errors.NetworkResource;
import org.mobicents.protocols.ss7.map.api.errors.PositionMethodFailureDiagnostic;
import org.mobicents.protocols.ss7.map.api.errors.SMEnumeratedDeliveryFailureCause;
import org.mobicents.protocols.ss7.map.api.errors.UnauthorizedLCSClientDiagnostic;
import org.mobicents.protocols.ss7.map.api.errors.UnknownSubscriberDiagnostic;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.tcap.asn.ParameterImpl;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MAPErrorMessageTest extends TestCase  {

	private Parameter getDataExtContainerFull() {
		Parameter par = new ParameterImpl();
		par.setData(new byte[] { 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
				24, 25, 26, (byte) 161, 3, 31, 32, 33 });
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
		par.setData(new byte[] { 10, 1, 4, 4, 5, 1, 3, 5, 7, 9, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6,
				3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 });
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
		par.setData(new byte[] { 2, 1, 11, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
				21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 1, 22 });
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
		par.setData(new byte[] { 10, 1, 1, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
				21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 129, 0 });
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
		par.setData(new byte[] { 10, 1, 2, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
				21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 1, 3 });
		par.setPrimitive(false);
		par.setTagClass(Tag.CLASS_UNIVERSAL);
		par.setTag(Tag.SEQUENCE);
		return par;
	}

	private Parameter getDataFacilityNotSupFull() {
		Parameter par = new ParameterImpl();
		par.setData(new byte[] { 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
				24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 0, (byte) 129, 0 });
		par.setPrimitive(false);
		par.setTagClass(Tag.CLASS_UNIVERSAL);
		par.setTag(Tag.SEQUENCE);
		return par;
	}

	private Parameter getDataUnknownSubscriberFull() {
		Parameter par = new ParameterImpl();
		par.setData(new byte[] { 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
				24, 25, 26, (byte) 161, 3, 31, 32, 33, 10, 1, 1 });
		par.setPrimitive(false);
		par.setTagClass(Tag.CLASS_UNIVERSAL);
		par.setTag(Tag.SEQUENCE);
		return par;
	}

	private Parameter getDataSubscriberBusyForMTSMSFull() {
		Parameter par = new ParameterImpl();
		par.setData(new byte[] { 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
				24, 25, 26, (byte) 161, 3, 31, 32, 33, 5, 0 });
		par.setPrimitive(false);
		par.setTagClass(Tag.CLASS_UNIVERSAL);
		par.setTag(Tag.SEQUENCE);
		return par;
	}

	private Parameter getDataAbsentSubscriberFull() {
		Parameter par = new ParameterImpl();
		par.setData(new byte[] { 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
				24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 1, 3 });
		par.setPrimitive(false);
		par.setTagClass(Tag.CLASS_UNIVERSAL);
		par.setTag(Tag.SEQUENCE);
		return par;
	}

	private Parameter getDataUnauthorizedLCSClientFull() {
		Parameter par = new ParameterImpl();
		par.setData(new byte[] { (byte) 128, 1, 2, (byte) 161, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11,
				6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 });
		par.setPrimitive(false);
		par.setTagClass(Tag.CLASS_UNIVERSAL);
		par.setTag(Tag.SEQUENCE);
		return par;
	}

	private Parameter getDataPositionMethodFailureFull() {
		Parameter par = new ParameterImpl();
		par.setData(new byte[] { (byte) 128, 1, 4, (byte) 161, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11,
				6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 });
		par.setPrimitive(false);
		par.setTagClass(Tag.CLASS_UNIVERSAL);
		par.setTag(Tag.SEQUENCE);
		return par;
	}
	
	@org.junit.Test
	public void testDecode() throws Exception {

		MAPErrorMessageFactoryImpl fact = new MAPErrorMessageFactoryImpl();

		Parameter p = getDataSmDeliveryFailure();
		MAPErrorMessageImpl em = (MAPErrorMessageImpl)fact.createMessageFromErrorCode((long) MAPErrorCode.smDeliveryFailure);
		AsnInputStream ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
		em.decodeData(ais, p.getData().length);
		assertTrue(em.isEmSMDeliveryFailure());
		MAPErrorMessageSMDeliveryFailure emSMDeliveryFailure = em.getEmSMDeliveryFailure();
		assertEquals(SMEnumeratedDeliveryFailureCause.invalidSMEAddress, emSMDeliveryFailure.getSMEnumeratedDeliveryFailureCause());
		assertNull(emSMDeliveryFailure.getSignalInfo());
		assertNull(emSMDeliveryFailure.getExtensionContainer());

		p = getDataSmDeliveryFailureFull();
		em = (MAPErrorMessageImpl)fact.createMessageFromErrorCode((long) MAPErrorCode.smDeliveryFailure);
		ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
		em.decodeData(ais, p.getData().length);
		assertTrue(em.isEmSMDeliveryFailure());
		emSMDeliveryFailure = em.getEmSMDeliveryFailure();
		assertEquals(SMEnumeratedDeliveryFailureCause.scCongestion, emSMDeliveryFailure.getSMEnumeratedDeliveryFailureCause());
		assertNotNull(emSMDeliveryFailure.getSignalInfo());
		assertNotNull(emSMDeliveryFailure.getExtensionContainer());
		assertTrue(Arrays.equals(emSMDeliveryFailure.getSignalInfo(), new byte[] { 1, 3, 5, 7, 9 }));
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emSMDeliveryFailure.getExtensionContainer()));

		p = getDataAbsentSubscriberSM();
		em = (MAPErrorMessageImpl)fact.createMessageFromErrorCode((long) MAPErrorCode.absentSubscriberSM);
		ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
		em.decodeData(ais, p.getData().length);
		assertTrue(em.isEmAbsentSubscriberSM());
		MAPErrorMessageAbsentSubscriberSM emAbsentSubscriberSMImpl = em.getEmAbsentSubscriberSM();
		assertEquals(1, (int) emAbsentSubscriberSMImpl.getAbsentSubscriberDiagnosticSM());
		assertNull(emAbsentSubscriberSMImpl.getAdditionalAbsentSubscriberDiagnosticSM());
		assertNull(emAbsentSubscriberSMImpl.getExtensionContainer());

		p = getDataAbsentSubscriberSMFull();
		em = (MAPErrorMessageImpl)fact.createMessageFromErrorCode((long) MAPErrorCode.absentSubscriberSM);
		ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
		em.decodeData(ais, p.getData().length);
		assertTrue(em.isEmAbsentSubscriberSM());
		emAbsentSubscriberSMImpl = em.getEmAbsentSubscriberSM();
		assertEquals(11, (int) emAbsentSubscriberSMImpl.getAbsentSubscriberDiagnosticSM());
		assertEquals(22, (int) emAbsentSubscriberSMImpl.getAdditionalAbsentSubscriberDiagnosticSM());
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emAbsentSubscriberSMImpl.getExtensionContainer()));

		p = getDataSystemFailure();
		em = (MAPErrorMessageImpl)fact.createMessageFromErrorCode((long) MAPErrorCode.systemFailure);
		ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
		em.decodeData(ais, p.getData().length);
		assertTrue(em.isEmSystemFailure());
		MAPErrorMessageSystemFailure emSystemFailure = em.getEmSystemFailure();
		assertEquals(2, emSystemFailure.getMapProtocolVersion());
		assertEquals(NetworkResource.plmn, emSystemFailure.getNetworkResource());
		assertNull(emSystemFailure.getAdditionalNetworkResource());
		assertNull(emSystemFailure.getExtensionContainer());

		p = getDataSystemFailureFull();
		em = (MAPErrorMessageImpl)fact.createMessageFromErrorCode((long) MAPErrorCode.systemFailure);
		ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
		em.decodeData(ais, p.getData().length);
		assertTrue(em.isEmSystemFailure());
		emSystemFailure = em.getEmSystemFailure();
		assertEquals(3, emSystemFailure.getMapProtocolVersion());
		assertEquals(NetworkResource.vlr, emSystemFailure.getNetworkResource());
		assertEquals(AdditionalNetworkResource.gsmSCF, emSystemFailure.getAdditionalNetworkResource());
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emAbsentSubscriberSMImpl.getExtensionContainer()));

		p = getDataCallBarred();
		em = (MAPErrorMessageImpl)fact.createMessageFromErrorCode((long) MAPErrorCode.callBarred);
		ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
		em.decodeData(ais, p.getData().length);
		assertTrue(em.isEmCallBarred());
		MAPErrorMessageCallBarred emCallBarred = em.getEmCallBarred();
		assertEquals(3, emCallBarred.getMapProtocolVersion());
		assertEquals(CallBarringCause.operatorBarring, emCallBarred.getCallBarringCause());
		assertEquals(false, (boolean)emCallBarred.getUnauthorisedMessageOriginator());
		assertNull(emCallBarred.getExtensionContainer());

		p = getDataCallBarredFull();
		em = (MAPErrorMessageImpl)fact.createMessageFromErrorCode((long) MAPErrorCode.callBarred);
		ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
		em.decodeData(ais, p.getData().length);
		assertTrue(em.isEmCallBarred());
		emCallBarred = em.getEmCallBarred();
		assertEquals(3, emCallBarred.getMapProtocolVersion());
		assertEquals(CallBarringCause.operatorBarring, emCallBarred.getCallBarringCause());
		assertEquals(true, (boolean)emCallBarred.getUnauthorisedMessageOriginator());
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emCallBarred.getExtensionContainer()));

		p = getDataFacilityNotSupFull();
		em = (MAPErrorMessageImpl)fact.createMessageFromErrorCode((long) MAPErrorCode.facilityNotSupported);
		ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
		em.decodeData(ais, p.getData().length);
		assertTrue(em.isEmFacilityNotSup());
		MAPErrorMessageFacilityNotSup emFacilityNotSup = em.getEmFacilityNotSup();
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emFacilityNotSup.getExtensionContainer()));
		assertEquals(true, (boolean)emFacilityNotSup.getShapeOfLocationEstimateNotSupported());
		assertEquals(true, (boolean)emFacilityNotSup.getNeededLcsCapabilityNotSupportedInServingNode());

		p = getDataUnknownSubscriberFull();
		em = (MAPErrorMessageImpl)fact.createMessageFromErrorCode((long) MAPErrorCode.unknownSubscriber);
		ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
		em.decodeData(ais, p.getData().length);
		assertTrue(em.isEmUnknownSubscriber());
		MAPErrorMessageUnknownSubscriber emUnknownSubscriber = em.getEmUnknownSubscriber();
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emUnknownSubscriber.getExtensionContainer()));
		assertEquals(UnknownSubscriberDiagnostic.gprsSubscriptionUnknown, emUnknownSubscriber.getUnknownSubscriberDiagnostic());

		p = getDataSubscriberBusyForMTSMSFull();
		em = (MAPErrorMessageImpl)fact.createMessageFromErrorCode((long) MAPErrorCode.subscriberBusyForMTSMS);
		ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
		em.decodeData(ais, p.getData().length);
		assertTrue(em.isEmSubscriberBusyForMtSms());
		MAPErrorMessageSubscriberBusyForMtSms emSubscriberBusyForMtSms = em.getEmSubscriberBusyForMtSms();
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emSubscriberBusyForMtSms.getExtensionContainer()));
		assertEquals(true, (boolean)emSubscriberBusyForMtSms.getGprsConnectionSuspended());

		p = getDataAbsentSubscriberFull();
		em = (MAPErrorMessageImpl)fact.createMessageFromErrorCode((long) MAPErrorCode.absentSubscriber);
		ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
		em.decodeData(ais, p.getData().length);
		assertTrue(em.isEmAbsentSubscriber());
		MAPErrorMessageAbsentSubscriber emAbsentSubscriber = em.getEmAbsentSubscriber();
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emAbsentSubscriber.getExtensionContainer()));
		assertEquals(AbsentSubscriberReason.purgedMS, emAbsentSubscriber.getAbsentSubscriberReason());

		p = getDataUnauthorizedLCSClientFull();
		em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.unauthorizedLCSClient);
		ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
		em.decodeData(ais, p.getData().length);
		assertTrue(em.isEmUnauthorizedLCSClient());
		MAPErrorMessageUnauthorizedLCSClient emUnauthorizedLCSClient = em.getEmUnauthorizedLCSClient();
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emUnauthorizedLCSClient.getExtensionContainer()));
		assertEquals(UnauthorizedLCSClientDiagnostic.callToClientNotSetup, emUnauthorizedLCSClient.getUnauthorizedLCSClientDiagnostic());

		p = getDataPositionMethodFailureFull();
		em = (MAPErrorMessageImpl) fact.createMessageFromErrorCode((long) MAPErrorCode.positionMethodFailure);
		ais = new AsnInputStream(p.getData(), p.getTagClass(), p.isPrimitive(), p.getTag());
		em.decodeData(ais, p.getData().length);
		assertTrue(em.isEmPositionMethodFailure());
		MAPErrorMessagePositionMethodFailure emPositionMethodFailure = em.getEmPositionMethodFailure();
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emPositionMethodFailure.getExtensionContainer()));
		assertEquals(PositionMethodFailureDiagnostic.locationProcedureNotCompleted, emPositionMethodFailure.getPositionMethodFailureDiagnostic());
	}

	@org.junit.Test
	public void testEncode() throws Exception {

		MAPErrorMessageFactoryImpl fact = new MAPErrorMessageFactoryImpl();

		MAPErrorMessageImpl em = (MAPErrorMessageImpl)fact.createMAPErrorMessageExtensionContainer(36L, MAPExtensionContainerTest.GetTestExtensionContainer());
		AsnOutputStream aos = new AsnOutputStream();
		em.encodeData(aos);
		Parameter p = new ParameterImpl();
		p.setTagClass(em.getTagClass());
		p.setTag(em.getTag());
		p.setPrimitive(em.getIsPrimitive());
		p.setData(aos.toByteArray());
		assertParameter(p, getDataExtContainerFull());

		em = (MAPErrorMessageImpl)fact.createMAPErrorMessageSMDeliveryFailure(SMEnumeratedDeliveryFailureCause.invalidSMEAddress, null, null);
		aos = new AsnOutputStream();
		em.encodeData(aos);
		p = new ParameterImpl();
		p.setTagClass(em.getTagClass());
		p.setTag(em.getTag());
		p.setPrimitive(em.getIsPrimitive());
		p.setData(aos.toByteArray());
		assertParameter(p, getDataSmDeliveryFailure());

		em = (MAPErrorMessageImpl)fact.createMAPErrorMessageSMDeliveryFailure(SMEnumeratedDeliveryFailureCause.scCongestion, new byte[] { 1, 3, 5, 7, 9 },
				MAPExtensionContainerTest.GetTestExtensionContainer());
		aos = new AsnOutputStream();
		em.encodeData(aos);
		p = new ParameterImpl();
		p.setTagClass(em.getTagClass());
		p.setTag(em.getTag());
		p.setPrimitive(em.getIsPrimitive());
		p.setData(aos.toByteArray());
		assertParameter(p, getDataSmDeliveryFailureFull());

		em = (MAPErrorMessageImpl)fact.createMAPErrorMessageAbsentSubscriberSM(1, null, null);
		aos = new AsnOutputStream();
		em.encodeData(aos);
		p = new ParameterImpl();
		p.setTagClass(em.getTagClass());
		p.setTag(em.getTag());
		p.setPrimitive(em.getIsPrimitive());
		p.setData(aos.toByteArray());
		assertParameter(p, getDataAbsentSubscriberSM());

		em = (MAPErrorMessageImpl)fact.createMAPErrorMessageAbsentSubscriberSM(11, MAPExtensionContainerTest.GetTestExtensionContainer(), 22);
		aos = new AsnOutputStream();
		em.encodeData(aos);
		p = new ParameterImpl();
		p.setTagClass(em.getTagClass());
		p.setTag(em.getTag());
		p.setPrimitive(em.getIsPrimitive());
		p.setData(aos.toByteArray());
		assertParameter(p, getDataAbsentSubscriberSMFull());

		em = (MAPErrorMessageImpl)fact.createMAPErrorMessageSystemFailure(2, NetworkResource.plmn, null, null);
		aos = new AsnOutputStream();
		em.encodeData(aos);
		p = new ParameterImpl();
		p.setTagClass(em.getTagClass());
		p.setTag(em.getTag());
		p.setPrimitive(em.getIsPrimitive());
		p.setData(aos.toByteArray());
		assertParameter(p, getDataSystemFailure());

		em = (MAPErrorMessageImpl)fact.createMAPErrorMessageSystemFailure(3, NetworkResource.vlr, AdditionalNetworkResource.gsmSCF,
				MAPExtensionContainerTest.GetTestExtensionContainer());
		aos = new AsnOutputStream();
		em.encodeData(aos);
		p = new ParameterImpl();
		p.setTagClass(em.getTagClass());
		p.setTag(em.getTag());
		p.setPrimitive(em.getIsPrimitive());
		p.setData(aos.toByteArray());
		assertParameter(p, getDataSystemFailureFull());

		em = (MAPErrorMessageImpl)fact.createMAPErrorMessageCallBarred(3L, CallBarringCause.operatorBarring, null, null);
		aos = new AsnOutputStream();
		em.encodeData(aos);
		p = new ParameterImpl();
		p.setTagClass(em.getTagClass());
		p.setTag(em.getTag());
		p.setPrimitive(em.getIsPrimitive());
		p.setData(aos.toByteArray());
		assertParameter(p, getDataCallBarred());

		em = (MAPErrorMessageImpl) fact.createMAPErrorMessageCallBarred(3L, CallBarringCause.operatorBarring,
				MAPExtensionContainerTest.GetTestExtensionContainer(), true);
		aos = new AsnOutputStream();
		em.encodeData(aos);
		p = new ParameterImpl();
		p.setTagClass(em.getTagClass());
		p.setTag(em.getTag());
		p.setPrimitive(em.getIsPrimitive());
		p.setData(aos.toByteArray());
		assertParameter(p, getDataCallBarredFull());

		em = (MAPErrorMessageImpl) fact.createMAPErrorMessageFacilityNotSup(MAPExtensionContainerTest.GetTestExtensionContainer(), true, true);
		aos = new AsnOutputStream();
		em.encodeData(aos);
		p = new ParameterImpl();
		p.setTagClass(em.getTagClass());
		p.setTag(em.getTag());
		p.setPrimitive(em.getIsPrimitive());
		p.setData(aos.toByteArray());
		assertParameter(p, getDataFacilityNotSupFull());

		em = (MAPErrorMessageImpl) fact.createMAPErrorMessageUnknownSubscriber(MAPExtensionContainerTest.GetTestExtensionContainer(),
				UnknownSubscriberDiagnostic.gprsSubscriptionUnknown);
		aos = new AsnOutputStream();
		em.encodeData(aos);
		p = new ParameterImpl();
		p.setTagClass(em.getTagClass());
		p.setTag(em.getTag());
		p.setPrimitive(em.getIsPrimitive());
		p.setData(aos.toByteArray());
		assertParameter(p, getDataUnknownSubscriberFull());

		em = (MAPErrorMessageImpl) fact.createMAPErrorMessageSubscriberBusyForMtSms(MAPExtensionContainerTest.GetTestExtensionContainer(), true);
		aos = new AsnOutputStream();
		em.encodeData(aos);
		p = new ParameterImpl();
		p.setTagClass(em.getTagClass());
		p.setTag(em.getTag());
		p.setPrimitive(em.getIsPrimitive());
		p.setData(aos.toByteArray());
		assertParameter(p, getDataSubscriberBusyForMTSMSFull());

		em = (MAPErrorMessageImpl) fact.createMAPErrorMessageAbsentSubscriber(MAPExtensionContainerTest.GetTestExtensionContainer(),
				AbsentSubscriberReason.purgedMS);
		aos = new AsnOutputStream();
		em.encodeData(aos);
		p = new ParameterImpl();
		p.setTagClass(em.getTagClass());
		p.setTag(em.getTag());
		p.setPrimitive(em.getIsPrimitive());
		p.setData(aos.toByteArray());
		assertParameter(p, getDataAbsentSubscriberFull());

		em = (MAPErrorMessageImpl) fact.createMAPErrorMessageUnauthorizedLCSClient(UnauthorizedLCSClientDiagnostic.callToClientNotSetup,
				MAPExtensionContainerTest.GetTestExtensionContainer());
		aos = new AsnOutputStream();
		em.encodeData(aos);
		p = new ParameterImpl();
		p.setTagClass(em.getTagClass());
		p.setTag(em.getTag());
		p.setPrimitive(em.getIsPrimitive());
		p.setData(aos.toByteArray());
		assertParameter(p, getDataUnauthorizedLCSClientFull());

		em = (MAPErrorMessageImpl) fact.createMAPErrorMessagePositionMethodFailure(PositionMethodFailureDiagnostic.locationProcedureNotCompleted,
				MAPExtensionContainerTest.GetTestExtensionContainer());
		aos = new AsnOutputStream();
		em.encodeData(aos);
		p = new ParameterImpl();
		p.setTagClass(em.getTagClass());
		p.setTag(em.getTag());
		p.setPrimitive(em.getIsPrimitive());
		p.setData(aos.toByteArray());
		assertParameter(p, getDataPositionMethodFailureFull());
	}
	
	private void assertParameter(Parameter p1, Parameter p2) {
		assertNotNull(p1);
		assertNotNull(p2);
		assertEquals(p1.getTagClass(), p2.getTagClass());
		assertEquals(p1.getTag(), p2.getTag());
		assertEquals(p1.isPrimitive(), p2.isPrimitive());
		assertTrue(Arrays.equals(p1.getData(), p2.getData()));
	}
}
