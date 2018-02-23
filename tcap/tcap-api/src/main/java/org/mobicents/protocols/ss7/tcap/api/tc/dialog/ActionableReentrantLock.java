package org.mobicents.protocols.ss7.tcap.api.tc.dialog;

import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("serial")
public class ActionableReentrantLock extends ReentrantLock {
    protected LockAction lockAction;

    public ActionableReentrantLock() {
        super();
    }

    public LockAction getLockAction() {
        return lockAction;
    }

    public void setLockAction(LockAction lockAction) {
        this.lockAction = lockAction;
    }

    public void lockAndTriggerActions() {
        if (lockAction != null) {
            lockAction.doPreLockAction();
            lock();
            lockAction.doPostLockAction();
        } else {
            lock();
        }
    }

    public void unlockAndTriggerActions() {
        if (lockAction != null) {
            lockAction.doPreUnlockAction();
            unlock();
            lockAction.doPostUnlockAction();
        } else {
            unlock();
        }
    }
}

