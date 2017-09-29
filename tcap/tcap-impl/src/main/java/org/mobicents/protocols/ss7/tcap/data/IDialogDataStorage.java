package org.mobicents.protocols.ss7.tcap.data;

import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;

import java.io.IOException;

/**
 * Created by piotr.sokolowski on 2017-06-07.
 */
public interface IDialogDataStorage {
    IDialog createDialog(SccpAddress localAddress, SccpAddress remoteAddress, Long origTransactionId, boolean structured, int seqControl) throws TCAPException;
    IDialog getDialog(Long dialogId);
    void removeDialog(IDialog d);

    int getSize();
    void clear();
    void start();
    void stop();

    void init(TCAPStackImpl tcapStack);
    void beginTransaction() throws Exception;
    void commitTransaction() throws Exception;

    void sendToSccp(SccpProvider sccpProvider, SccpDataMessage msg) throws IOException;
}
