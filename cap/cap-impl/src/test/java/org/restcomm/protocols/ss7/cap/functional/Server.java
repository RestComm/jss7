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

package org.restcomm.protocols.ss7.cap.functional;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPParameterFactory;
import org.restcomm.protocols.ss7.cap.api.CAPProvider;
import org.restcomm.protocols.ss7.cap.api.CAPStack;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageFactory;
import org.restcomm.protocols.ss7.cap.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.cap.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.RequestReportGPRSEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEvent;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.RequestReportBCSMEventRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.RequestReportGPRSEventRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSEventImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.restcomm.protocols.ss7.inap.api.INAPParameterFactory;
import org.restcomm.protocols.ss7.inap.api.primitives.LegID;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class Server extends EventTestHarness {

    private static Logger logger = Logger.getLogger(Server.class);

    private CAPFunctionalTest runningTestCase;
    private SccpAddress thisAddress;
    private SccpAddress remoteAddress;

    protected CAPStack capStack;
    protected CAPProvider capProvider;

    protected CAPParameterFactory capParameterFactory;
    protected CAPErrorMessageFactory capErrorMessageFactory;
    protected MAPParameterFactory mapParameterFactory;
    protected INAPParameterFactory inapParameterFactory;
    protected ISUPParameterFactory isupParameterFactory;

    protected CAPDialog serverCscDialog;

    // private boolean _S_recievedDialogRequest;
    // private boolean _S_recievedInitialDp;
    //
    // private int dialogStep;
    // private long savedInvokeId;
    // private String unexpected = "";
    //
    // private FunctionalTestScenario step;

    Server(CAPStack capStack, CAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress) {
        super(logger);
        this.capStack = capStack;
        this.runningTestCase = runningTestCase;
        this.thisAddress = thisAddress;
        this.remoteAddress = remoteAddress;
        this.capProvider = this.capStack.getCAPProvider();

        this.capParameterFactory = this.capProvider.getCAPParameterFactory();
        this.capErrorMessageFactory = this.capProvider.getCAPErrorMessageFactory();
        this.mapParameterFactory = this.capProvider.getMAPParameterFactory();
        this.inapParameterFactory = this.capProvider.getINAPParameterFactory();
        this.isupParameterFactory = this.capProvider.getISUPParameterFactory();

        this.capProvider.addCAPDialogListener(this);
        this.capProvider.getCAPServiceCircuitSwitchedCall().addCAPServiceListener(this);
        this.capProvider.getCAPServiceGprs().addCAPServiceListener(this);
        this.capProvider.getCAPServiceSms().addCAPServiceListener(this);

        this.capProvider.getCAPServiceCircuitSwitchedCall().acivate();
        this.capProvider.getCAPServiceGprs().acivate();
        this.capProvider.getCAPServiceSms().acivate();
    }

    public RequestReportBCSMEventRequest getRequestReportBCSMEventRequest() {

        ArrayList<BCSMEvent> bcsmEventList = new ArrayList<BCSMEvent>();
        BCSMEvent ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.routeSelectFailure,
                MonitorMode.notifyAndContinue, null, null, false);
        bcsmEventList.add(ev);
        ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oCalledPartyBusy, MonitorMode.interrupted, null, null,
                false);
        bcsmEventList.add(ev);
        ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oNoAnswer, MonitorMode.interrupted, null, null, false);
        bcsmEventList.add(ev);
        ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oAnswer, MonitorMode.notifyAndContinue, null, null, false);
        bcsmEventList.add(ev);
        LegID legId = this.inapParameterFactory.createLegID(true, LegType.leg1);
        ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oDisconnect, MonitorMode.notifyAndContinue, legId, null,
                false);
        bcsmEventList.add(ev);
        legId = this.inapParameterFactory.createLegID(true, LegType.leg2);
        ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oDisconnect, MonitorMode.interrupted, legId, null, false);
        bcsmEventList.add(ev);
        ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oAbandon, MonitorMode.notifyAndContinue, null, null, false);
        bcsmEventList.add(ev);

        RequestReportBCSMEventRequestImpl res = new RequestReportBCSMEventRequestImpl(bcsmEventList, null);

        return res;
    }

    public RequestReportGPRSEventRequest getRequestReportGPRSEventRequest() {
        ArrayList<GPRSEvent> gprsEvent = new ArrayList<GPRSEvent>();
        GPRSEvent event = new GPRSEventImpl(GPRSEventType.attachChangeOfPosition, MonitorMode.notifyAndContinue);
        gprsEvent.add(event);
        PDPID pdpID = new PDPIDImpl(2);

        RequestReportGPRSEventRequestImpl res = new RequestReportGPRSEventRequestImpl(gprsEvent, pdpID);
        return res;
    }

    public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
        super.onDialogRequest(capDialog, capGprsReferenceNumber);
        serverCscDialog = capDialog;
    }

    public void sendAccept() {
        try {
            serverCscDialog.send();
        } catch (CAPException e) {
            this.error("Error while trying to send/close() Dialog", e);
        }
    }

    public void debug(String message) {
        this.logger.debug(message);
    }

    public void error(String message, Exception e) {
        this.logger.error(message, e);
    }
}
