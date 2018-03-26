package org.restcomm.protocols.ss7.sccp.impl;

import java.io.IOException;

import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

public class AdvancedUser extends User {

    private static final long serialVersionUID = 1L;

    public AdvancedUser(SccpProvider provider, SccpAddress address, SccpAddress dest, int ssn) {
        super(provider, address, dest, ssn);
    }

    @Override
    public void onMessage(SccpDataMessage message) {
        this.messages.add(message);
        System.out.println(String.format("SccpDataMessage=%s seqControl=%d", message, message.getSls()));
        SccpAddress calledAddress = message.getCalledPartyAddress();
        SccpAddress callingAddress = message.getCallingPartyAddress();
        SccpDataMessage newMessage = provider.getMessageFactory().createDataMessageClass1(callingAddress, calledAddress, message.getData(),
                message.getSls(),message.getOriginLocalSsn(), true, message.getHopCounter(), message.getImportance());
        ((SccpMessageImpl) newMessage).setOutgoingDpc(message.getIncomingOpc());
        try {
            this.provider.send(newMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
