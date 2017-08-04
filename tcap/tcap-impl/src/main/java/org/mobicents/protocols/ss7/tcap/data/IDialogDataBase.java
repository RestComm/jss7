package org.mobicents.protocols.ss7.tcap.data;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by piotr.sokolowski on 2017-06-07.
 */
public interface IDialogDataBase {
    int _REMOVE_TIMEOUT = 30000;

    Object getUserObject();
    void setUserObject(Object userObject);

    ReentrantLock getDialogLock();

    /* long getRemoveTaskTimeout();
    void setRemoveTaskTimeout(long removeTaskTimeout);*/

    long getIdleTaskTimeout();
    void setIdleTaskTimeout(long idleTaskTimeout);

    ApplicationContextName getLastACN();
    void setLastACN(ApplicationContextName lastACN);

    UserInformation getLastUI();
    void setLastUI(UserInformation lastUI);

    Long getLocalTransactionIdObject();
    void setLocalTransactionIdObject(Long localTransactionIdObject);

    long getLocalTransactionId();
    void setLocalTransactionId(long localTransactionId);

    byte[] getRemoteTransactionId();
    void setRemoteTransactionId(byte[] remoteTransactionId);

    Long getRemoteTransactionIdObject();
    void setRemoteTransactionIdObject(Long remoteTransactionIdObject);

    SccpAddress getLocalAddress();
    void setLocalAddress(SccpAddress localAddress);

    SccpAddress getRemoteAddress();
    void setRemoteAddress(SccpAddress remoteAddress);

    Object getIdleTimerHandle();
    void setIdleTimerHandle(Object idleTimerHandle);

    boolean isIdleTimerActionTaken();
    void setIdleTimerActionTaken(boolean idleTimerActionTaken);

    boolean isIdleTimerInvoked();
    void setIdleTimerInvoked(boolean idleTimerInvoked);

    TRPseudoState getState();
    void setState(TRPseudoState state);

    boolean isStructured();
    void setStructured(boolean structured);

    /** Incoming operations handling **/
    boolean addIncomingInvoke(Long invokeId);
    boolean removeIncomingInvoke(Long invokeId);

    int getSeqControl();
    void setSeqControl(int seqControl);

    boolean isDpSentInBegin();
    void setDpSentInBegin(boolean dpSentInBegin);

    long getStartDialogTime();
    void setStartDialogTime(long startDialogTime);

    int getNetworkId();
    void setNetworkId(int networkId);

    Long getLocalRelayedTransactionIdObject();
    void setLocalRelayedTransactionIdObject(Long l);

    int getLocalSsn();
    void setLocalSsn(int newSsn);
}
