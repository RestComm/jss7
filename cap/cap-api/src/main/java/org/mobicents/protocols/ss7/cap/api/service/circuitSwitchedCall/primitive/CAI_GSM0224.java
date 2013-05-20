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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

/**
 *
 CAI-GSM0224 ::= SEQUENCE { e1 [0] INTEGER (0..8191) OPTIONAL, e2 [1] INTEGER (0..8191) OPTIONAL, e3 [2] INTEGER (0..8191)
 * OPTIONAL, e4 [3] INTEGER (0..8191) OPTIONAL, e5 [4] INTEGER (0..8191) OPTIONAL, e6 [5] INTEGER (0..8191) OPTIONAL, e7 [6]
 * INTEGER (0..8191) OPTIONAL } -- Indicates Charge Advice Information to the Mobile Station. For information regarding --
 * parameter usage, refer to 3GPP TS 22.024 [2].
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface CAI_GSM0224 {

    Integer getE1();

    Integer getE2();

    Integer getE3();

    Integer getE4();

    Integer getE5();

    Integer getE6();

    Integer getE7();

}