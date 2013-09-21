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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;

import org.mobicents.protocols.asn.BitSetStrictLength;

/**
 *
 OfferedCamel4Functionalities ::= BIT STRING { initiateCallAttempt (0), splitLeg (1), moveLeg (2), disconnectLeg (3),
 * entityReleased (4), dfc-WithArgument (5), playTone (6), dtmf-MidCall (7), chargingIndicator (8), alertingDP (9),
 * locationAtAlerting (10), changeOfPositionDP (11), or-Interactions (12), warningToneEnhancements (13), cf-Enhancements (14),
 * subscribedEnhancedDialledServices (15), servingNetworkEnhancedDialledServices (16), criteriaForChangeOfPositionDP (17),
 * serviceChangeDP (18), collectInformation (19) } (SIZE (15..64)) -- A node supporting Camel phase 4 shall mark in the BIT
 * STRING all CAMEL4 -- functionalities it offers. -- Other values than listed above shall be discarded.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface OfferedCamel4Functionalities extends Serializable {

    BitSetStrictLength getData();

}
