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

package org.mobicents.protocols.ss7.sccp.parameter;

import java.io.Serializable;

import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;

/**
 *
 * @author kulikov
 * @author baranowb
 */
public interface ParameterFactory extends Serializable {

    /**
     * Create SccpAddress parameter
     *
     * @param ri
     * @param dpc
     * @param gt
     * @param ssn
     * @return
     */
    SccpAddress createSccpAddress(RoutingIndicator ri, GlobalTitle gt, int dpc, int ssn);

    /**
     * Create Importance parameter.
     *
     * @param value the value of this parameter
     * @return parameter
     */
    Importance createImportance(int value);

    /**
     * Create hop counter parameter
     */
    HopCounter createHopCounter(int hopCount);

    ReturnCause createReturnCause(ReturnCauseValue cause);

    /**
     * Creates protocol class parameter.
     *
     * @param value the value of the parameter
     * @return parameter
     */
    ProtocolClass createProtocolClass(int pClass, boolean returnMessageOnError);

    /**
     * Create segemntation parameter
     *
     * @return parameter.
     */
    Segmentation createSegmentation();

    /**
     * Creates encoding scheme instance by its code. Which schemes are supported, is up to implementation.
     *
     * @param i
     * @return
     */
    EncodingScheme createEncodingScheme(byte i);

    GlobalTitle createGlobalTitle(String digits);

    GlobalTitle createGlobalTitle(GlobalTitleIndicator ind);

    GlobalTitle0001 createGlobalTitle(String digits, NatureOfAddress noa);

    GlobalTitle0010 createGlobalTitle(String digits, int translationType);

    GlobalTitle0011 createGlobalTitle(String digits, int translationType, NumberingPlan numberingPlan,
            EncodingScheme encodingScheme);

    GlobalTitle0100 createGlobalTitle(String digits, int translationType, NumberingPlan numberingPlan,
            EncodingScheme encodingScheme, NatureOfAddress noa);

}
