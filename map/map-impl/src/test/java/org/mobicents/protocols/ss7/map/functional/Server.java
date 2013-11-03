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

package org.mobicents.protocols.ss7.map.functional;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class Server extends EventTestHarness {

    private static Logger logger = Logger.getLogger(Server.class);

    private MAPFunctionalTest runningTestCase;
    private SccpAddress thisAddress;
    private SccpAddress remoteAddress;

    private MAPStack mapStack;
    protected MAPProvider mapProvider;

    protected MAPParameterFactory mapParameterFactory;
    protected MAPErrorMessageFactory mapErrorMessageFactory;

    Server(MAPStack mapStack, MAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress) {
        super(logger);
        this.mapStack = mapStack;
        this.runningTestCase = runningTestCase;
        this.thisAddress = thisAddress;
        this.remoteAddress = remoteAddress;
        this.mapProvider = this.mapStack.getMAPProvider();

        this.mapParameterFactory = this.mapProvider.getMAPParameterFactory();
        this.mapErrorMessageFactory = this.mapProvider.getMAPErrorMessageFactory();

        this.mapProvider.addMAPDialogListener(this);
        this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);
        this.mapProvider.getMAPServiceSms().addMAPServiceListener(this);
        this.mapProvider.getMAPServiceMobility().addMAPServiceListener(this);
        this.mapProvider.getMAPServiceLsm().addMAPServiceListener(this);
        this.mapProvider.getMAPServiceCallHandling().addMAPServiceListener(this);

        this.mapProvider.getMAPServiceSupplementary().acivate();
        this.mapProvider.getMAPServiceSms().acivate();
        this.mapProvider.getMAPServiceMobility().acivate();
        this.mapProvider.getMAPServiceLsm().acivate();
        this.mapProvider.getMAPServiceCallHandling().acivate();
    }

    public void debug(String message) {
        this.logger.debug(message);
    }

    public void error(String message, Exception e) {
        this.logger.error(message, e);
    }

}
