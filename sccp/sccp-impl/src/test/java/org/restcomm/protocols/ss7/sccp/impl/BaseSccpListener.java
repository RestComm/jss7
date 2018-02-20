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

package org.restcomm.protocols.ss7.sccp.impl;

import org.restcomm.protocols.ss7.sccp.NetworkIdState;
import org.restcomm.protocols.ss7.sccp.RemoteSccpStatus;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.SignallingPointStatus;
import org.restcomm.protocols.ss7.sccp.message.MessageFactory;
import org.restcomm.protocols.ss7.sccp.message.SccpAddressedMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.ErrorCause;
import org.restcomm.protocols.ss7.sccp.parameter.Importance;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.RefusalCause;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCause;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCause;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseSccpListener implements SccpListener {
    public BaseSccpListener() {
    }

    @Override
    public void onMessage(SccpDataMessage message) {

    }

    @Override
    public void onNotice(SccpNoticeMessage message) {

    }

    @Override
    public void onCoordResponse(int ssn, int multiplicityIndicator) {

    }

    @Override
    public void onState(int dpc, int ssn, boolean inService, int multiplicityIndicator) {

    }

    @Override
    public void onPcState(int dpc, SignallingPointStatus status, Integer restrictedImportanceLevel, RemoteSccpStatus remoteSccpStatus) {

    }

    @Override
    public void onNetworkIdState(int networkId, NetworkIdState networkIdState) {

    }

    @Override
    public void onConnectIndication(SccpConnection conn, SccpAddress calledAddress, SccpAddress callingAddress, ProtocolClass clazz, Credit credit, byte[] data, Importance importance) throws Exception {

    }

    @Override
    public void onConnectConfirm(SccpConnection conn, byte[] data) {

    }

    @Override
    public void onDisconnectIndication(SccpConnection conn, ReleaseCause reason, byte[] data) {

    }

    @Override
    public void onDisconnectIndication(SccpConnection conn, RefusalCause reason, byte[] data) {

    }

    @Override
    public void onDisconnectIndication(SccpConnection conn, ErrorCause errorCause) {

    }

    @Override
    public void onResetIndication(SccpConnection conn, ResetCause reason) {

    }

    @Override
    public void onResetConfirm(SccpConnection conn) {

    }

    @Override
    public void onData(SccpConnection conn, byte[] data) {

    }

    @Override
    public void onDisconnectConfirm(SccpConnection conn) {
    }
}
