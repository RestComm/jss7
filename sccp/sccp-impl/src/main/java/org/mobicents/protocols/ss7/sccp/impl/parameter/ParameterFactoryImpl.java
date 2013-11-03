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

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCause;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.sccp.parameter.Segmentation;

/**
 *
 * @author kulikov
 */
public class ParameterFactoryImpl implements ParameterFactory {

    public SccpAddress createSccpAddress(RoutingIndicator ri, int dpc, GlobalTitle gt, int ssn) {
        return new SccpAddress(ri, dpc, gt, ssn);
    }

    public Importance createImportance(int value) {
        return new ImportanceImpl((byte) value);
    }

    public HopCounter createHopCounter(int hopCount) {
        return new HopCounterImpl(hopCount);
    }

    public ProtocolClass createProtocolClass(int pClass, boolean returnMessageOnError) {
        return new ProtocolClassImpl(pClass, returnMessageOnError);
    }

    public Segmentation createSegmentation() {
        return new SegmentationImpl();
    }

    public ReturnCause createReturnCause(ReturnCauseValue cause) {
        return new ReturnCauseImpl(cause);
    }

}
