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

package org.mobicents.protocols.ss7.m3ua.impl;

import java.util.HashMap;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;

/**
 *
 * @author amit bhayani
 *
 */
public class TransitionState {
    public static final String COMM_UP = "commup";
    public static final String COMM_DOWN = "commdown";
    public static final String PAYLOAD = "payload";

    public static final String DUNA = "duna";
    public static final String DAVA = "dava";
    public static final String DAUD = "daud";
    public static final String SCON = "scon";
    public static final String DUPU = "dupu";
    public static final String DRST = "drst";

    public static final String ASP_UP_SENT = "aspupsent";
    public static final String ASP_UP = "aspup";
    public static final String ASP_UP_ACK = "aspupack";

    public static final String ASP_DOWN_SENT = "aspdownsent";
    public static final String ASP_DOWN = "aspdown";
    public static final String ASP_DOWN_ACK = "aspdownack";

    public static final String HEARTBEAT = "heartbeat";
    public static final String HEARTBEAT_ACK = "heartbeatack";

    public static final String ASP_INACTIVE_SENT = "aspinactivesent";
    public static final String ASP_INACTIVE = "aspinactive";
    public static final String ASP_INACTIVE_ACK = "aspinactiveack";

    public static final String ASP_ACTIVE = "aspactive";
    public static final String ASP_ACTIVE_SENT = "aspactivesent";
    public static final String ASP_ACTIVE_ACK = "aspactiveack";

    public static final String AS_DOWN = "asdown";
    public static final String AS_INACTIVE = "asinactive";

    public static final String AS_STATE_CHANGE_RESERVE = "ntfyreserve";
    public static final String AS_STATE_CHANGE_INACTIVE = "ntfyasinactive";
    public static final String AS_STATE_CHANGE_ACTIVE = "ntfyasactive";
    public static final String AS_STATE_CHANGE_PENDING = "ntfyaspending";

    public static final String OTHER_INSUFFICIENT_ASP = "ntfyinsuffasp";
    public static final String OTHER_ALTERNATE_ASP_ACTIVE = "ntfyaltaspact";
    public static final String OTHER_ALTERNATE_ASP_FAILURE = "ntfyaltaspfail";

    public static final String INVALID_RC = "invalidrc";

    private static HashMap<Integer, HashMap<Integer, String>> transContainer = new HashMap<Integer, HashMap<Integer, String>>();

    static {

        // Transfer
        HashMap<Integer, String> trfrTransCont = new HashMap<Integer, String>();
        trfrTransCont.put(MessageType.PAYLOAD, PAYLOAD);

        transContainer.put(MessageClass.TRANSFER_MESSAGES, trfrTransCont);

        // SSNM
        HashMap<Integer, String> ssnmTransCont = new HashMap<Integer, String>();
        ssnmTransCont.put(MessageType.DESTINATION_UNAVAILABLE, DUNA);
        ssnmTransCont.put(MessageType.DESTINATION_AVAILABLE, DAVA);
        ssnmTransCont.put(MessageType.DESTINATION_STATE_AUDIT, DAUD);
        ssnmTransCont.put(MessageType.SIGNALING_CONGESTION, SCON);
        ssnmTransCont.put(MessageType.DESTINATION_USER_PART_UNAVAILABLE, DUPU);
        ssnmTransCont.put(MessageType.DESTINATION_RESTRICTED, DRST);

        transContainer.put(MessageClass.SIGNALING_NETWORK_MANAGEMENT, ssnmTransCont);

        // ASPSM
        HashMap<Integer, String> aspsmTransCont = new HashMap<Integer, String>();
        aspsmTransCont.put(MessageType.ASP_UP, ASP_UP);
        aspsmTransCont.put(MessageType.ASP_UP_ACK, ASP_UP_ACK);
        aspsmTransCont.put(MessageType.ASP_DOWN, ASP_DOWN);
        aspsmTransCont.put(MessageType.ASP_DOWN_ACK, ASP_DOWN_ACK);
        aspsmTransCont.put(MessageType.HEARTBEAT, HEARTBEAT);
        aspsmTransCont.put(MessageType.HEARTBEAT_ACK, HEARTBEAT_ACK);

        transContainer.put(MessageClass.ASP_STATE_MAINTENANCE, aspsmTransCont);

        // ASPTM
        HashMap<Integer, String> asptmTransCont = new HashMap<Integer, String>();
        asptmTransCont.put(MessageType.ASP_ACTIVE, ASP_ACTIVE);
        asptmTransCont.put(MessageType.ASP_ACTIVE_ACK, ASP_ACTIVE_ACK);
        asptmTransCont.put(MessageType.ASP_INACTIVE, ASP_INACTIVE);
        asptmTransCont.put(MessageType.ASP_INACTIVE_ACK, ASP_INACTIVE_ACK);

        transContainer.put(MessageClass.ASP_TRAFFIC_MAINTENANCE, asptmTransCont);

        // MGMT
        HashMap<Integer, String> mgmtTransCont = new HashMap<Integer, String>();

        // NTFY
        mgmtTransCont.put((Status.STATUS_AS_State_Change << 16 | Status.INFO_Reserved), AS_STATE_CHANGE_RESERVE);
        mgmtTransCont.put((Status.STATUS_AS_State_Change << 16 | Status.INFO_AS_INACTIVE), AS_STATE_CHANGE_INACTIVE);
        mgmtTransCont.put((Status.STATUS_AS_State_Change << 16 | Status.INFO_AS_ACTIVE), AS_STATE_CHANGE_ACTIVE);
        mgmtTransCont.put((Status.STATUS_AS_State_Change << 16 | Status.INFO_AS_PENDING), AS_STATE_CHANGE_PENDING);

        mgmtTransCont.put((Status.STATUS_Other << 16 | Status.INFO_Insufficient_ASP_Resources_Active), OTHER_INSUFFICIENT_ASP);
        mgmtTransCont.put((Status.STATUS_Other << 16 | Status.INFO_Alternate_ASP_Active), OTHER_ALTERNATE_ASP_ACTIVE);
        mgmtTransCont.put((Status.STATUS_Other << 16 | Status.INFO_Alternate_ASP_Failure), OTHER_ALTERNATE_ASP_FAILURE);
        // Err cde
        mgmtTransCont.put(ErrorCode.Invalid_Routing_Context, INVALID_RC);

        transContainer.put(MessageClass.MANAGEMENT, mgmtTransCont);
    }

    public static String getTransition(M3UAMessage message) {
        switch (message.getMessageClass()) {
            case MessageClass.MANAGEMENT:
                switch (message.getMessageType()) {
                    case MessageType.ERROR:
                    case MessageType.NOTIFY:
                        Status status = ((Notify) message).getStatus();
                        return transContainer.get(message.getMessageClass()).get((status.getType() << 16 | status.getInfo()));
                }
            default:
                return transContainer.get(message.getMessageClass()).get(message.getMessageType());
        }
    }
}
