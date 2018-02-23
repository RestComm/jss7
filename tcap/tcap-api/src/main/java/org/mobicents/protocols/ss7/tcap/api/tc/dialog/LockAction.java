package org.mobicents.protocols.ss7.tcap.api.tc.dialog;

public interface LockAction {
    void doPreLockAction();
    void doPostLockAction();
    void doPreUnlockAction();
    void doPostUnlockAction();
}
