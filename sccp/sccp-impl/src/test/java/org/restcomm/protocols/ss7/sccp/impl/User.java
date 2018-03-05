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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.restcomm.protocols.ss7.sccp.NetworkIdState;
import org.restcomm.protocols.ss7.sccp.RemoteSccpStatus;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.SignallingPointStatus;
import org.restcomm.protocols.ss7.sccp.impl.parameter.RefusalCauseImpl;
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
import org.restcomm.protocols.ss7.sccp.parameter.RefusalCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCause;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCause;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author baranowb
 * @author abhayani
 */
public class User extends BaseSccpListener implements SccpListener {
    protected SccpProvider provider;
    protected SccpAddress address;
    protected SccpAddress dest;
    protected int ssn;
    // protected SccpMessage msg;
    protected List<SccpMessage> messages = new ArrayList<SccpMessage>();
    protected List<byte[]> receivedData = Collections.synchronizedList(new ArrayList<byte[]>());
    protected boolean refuseConnections;
    protected UserStats stats = new UserStats();
    private UserOptions options = new UserOptions();

    public User(SccpProvider provider, SccpAddress address, SccpAddress dest, int ssn) {
        this.provider = provider;
        this.address = address;
        this.dest = dest;
        this.ssn = ssn;
    }

    public void register() {
        provider.registerSccpListener(ssn, this);
    }

    public void deregister() {
        provider.deregisterSccpListener(ssn);
    }

    public boolean check() { // override if required.
        System.err.println("SIZE: "+messages.size());
        if (messages.size() == 0) {
            return false;
        }
        SccpMessage msg = messages.get(0);
        if (msg.getType() != SccpMessage.MESSAGE_TYPE_UDT) {
            System.err.println("WRONG TYPE: "+msg.getType());
            return false;
        }

        if (!matchCalledPartyAddress()) {
            System.err.println("WRONG matchCalledPartyAddress");
            return false;
        }

        if (!matchCallingPartyAddress()) {
            System.err.println("WRONG matchCallingPartyAddress");
            return false;
        }

        return true;
    }

    protected boolean matchCalledPartyAddress() {
        SccpMessage msg = messages.get(0);
        SccpAddressedMessage udt = (SccpAddressedMessage) msg;
        if (!address.equals(udt.getCalledPartyAddress())) {
            return false;
        }
        return true;
    }

    protected boolean matchCallingPartyAddress() {
        SccpMessage msg = messages.get(0);
        SccpAddressedMessage udt = (SccpAddressedMessage) msg;
        if (!dest.equals(udt.getCallingPartyAddress())) {
            return false;
        }
        return true;
    }

    public void send() throws IOException {
        MessageFactory messageFactory = provider.getMessageFactory();

        // ParameterFactory paramFactory = provider.getParameterFactory();
        // ProtocolClass pClass = paramFactory.createProtocolClass(0, 0);
        // UnitData udt = messageFactory.createUnitData(pClass, dest, address);
        // udt.setData(new byte[10]);
        // provider.send(udt,1);

        SccpDataMessage udt = messageFactory.createDataMessageClass0(dest, address, new byte[10], ssn, false, null, null);
        provider.send(udt);
    }

    SccpAddress localAddress;

    public void onMessage(SccpDataMessage message) {
        this.messages.add(message);
        System.out.println(String.format("SccpDataMessage=%s seqControl=%d", message, message.getSls()));

        // localAddress = message.getCalledPartyAddress();
        // SccpAddress remoteAddress = message.getCallingPartyAddress();
        //
        // // now decode content
        //
        // byte[] data = message.getData();
        //
        // // some data encoded in
        // CallRequest cr = new CallRequest(data);
        //
        // byte[] answerData;
        //
        // if (cr.getCallee().equals(this.localAddress)) {
        // EstablihsCallAnswer eca = new EstablihsCallAnswer(cr);
        // answerData = eca.encode();
        //
        // } else {
        // TearDownCallAnswer tdca = new TearDownCallAnswer(cr);
        // answerData = tdca.encode();
        // }
        //
        // HopCounter hc = this.sccpProvider.getParameterFactory().createHopCounter(5);
        //
        // // XUnitData sccpAnswer = this.sccpProvider.getMessageFactory().createXUnitData(hc, xudt.getProtocolClass(),
        // message.getCallingPartyAddress(),
        // // this.localAddress);
        // SccpDataMessage sccpAnswer =
        // this.provider.getMessageFactory().createDataMessageClass0(message.getCallingPartyAddress(), this.localAddress,
        // answerData, localSsn, returnMessageOnError, hopCounter, importance);
        //
        // this.sccpProvider.send(sccpAnswer);

    }

    public List<SccpMessage> getMessages() {
        return messages;
    }

    @Override
    public void onNotice(SccpNoticeMessage message) {
        this.messages.add(message);
        System.out.println(String.format("SccpNoticeMessage=%s seqControl=%d", message, message.getSls()));
    }

    @Override
    public void onCoordResponse(int ssn, int multiplicityIndicator) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onState(int dpc, int ssn, boolean inService, int multiplicityIndicator) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPcState(int dpc, SignallingPointStatus status, Integer restrictedImportanceLevel,
                          RemoteSccpStatus remoteSccpStatus) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNetworkIdState(int networkId, NetworkIdState networkIdState) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectIndication(SccpConnection conn, SccpAddress calledAddress, SccpAddress callingAddress, ProtocolClass clazz, Credit credit, byte[] data, Importance importance) throws Exception {
        if (!refuseConnections) {
            conn.confirm(calledAddress, (options.confirmCredit != null) ? options.confirmCredit : credit, options.sendConfirmData);
        } else {
            conn.refuse(new RefusalCauseImpl(RefusalCauseValue.END_USER_ORIGINATED), new byte[] { 0x31, 0x32, 0x33, 0x34 });
            stats.refusedCount++;
        }
    }

    @Override
    public void onConnectConfirm(SccpConnection conn, byte[] data) {
        if (data != null && data.length > 0) {
            this.receivedData.add(data);
        }
    }

    @Override
    public void onResetIndication(SccpConnection conn, ResetCause reason) {
        stats.resetCount++;
    }

    @Override
    public void onResetConfirm(SccpConnection conn) {
        stats.resetCount++;
    }

    @Override
    public void onDisconnectIndication(SccpConnection conn, RefusalCause reason, byte[] data) {
        stats.refusedCount++;
    }

    @Override
    public void onDisconnectIndication(SccpConnection conn, ReleaseCause reason, byte[] data) {
        stats.releaseCause = reason;
    }

    @Override
    public void onDisconnectIndication(SccpConnection conn, ErrorCause errorCause) {
        stats.disconnectError = errorCause;
    }

    public int getResetCount() {
        return stats.resetCount;
    }

    public int getRefusedCount() {
        return stats.refusedCount;
    }

    public void setRefuseConnections(boolean refuseConnections) {
        this.refuseConnections = refuseConnections;
    }

    @Override
    public void onData(SccpConnection conn, byte[] data) {
        receivedData.add(data);
    }

    public List<byte[]> getReceivedData() {
        return receivedData;
    }

    public UserStats getStats() {
        return stats;
    }

    public UserOptions getOptions() {
        return options;
    }

    public static class UserStats {
        protected int resetCount;
        protected int refusedCount;
        protected int errorCount;
        private ReleaseCause releaseCause;
        private ErrorCause disconnectError;

        public ReleaseCause getReleaseCause() {
            return releaseCause;
        }

        public ErrorCause getDisconnectError() {
            return disconnectError;
        }
    }

    public static class UserOptions {
        private byte[] sendConfirmData;
        public Credit confirmCredit;

        public void setSendConfirmData(byte[] data) {
            this.sendConfirmData = data;
        }
    }
}
