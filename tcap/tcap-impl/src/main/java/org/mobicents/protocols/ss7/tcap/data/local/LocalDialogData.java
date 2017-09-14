package org.mobicents.protocols.ss7.tcap.data.local;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.data.IDialog;
import org.mobicents.protocols.ss7.tcap.data.IDialogData;
import org.mobicents.protocols.ss7.tcap.data.ITCAPOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by piotr.sokolowski on 2017-06-07.
 */
public class LocalDialogData implements IDialogData {
    public static final boolean _INVOKEID_TAKEN = true;
    public static final boolean _INVOKEID_FREE = false;
    public static final int _INVOKE_TABLE_SHIFT=128;
    private final HashMap<Long,LocalTCAPOperation> invokes=new HashMap<>();
    final LocalDialogDataStorage storage;
    private IDialog dialog;
    private Object userObject;
    // lock... ech
    private ReentrantLock dialogLock = new ReentrantLock();
    // values for timer timeouts
    private long removeTaskTimeout = IDialogData._REMOVE_TIMEOUT;
    private long idleTaskTimeout;
    // sent/received acn, holds last acn/ui.
    private ApplicationContextName lastACN;
    private UserInformation lastUI; // optional
    private Long localTransactionIdObject;
    private long localTransactionId;
    private byte[] remoteTransactionId;
    private Long remoteTransactionIdObject;
    private Long localRelayedTransactionIdObject;
    private SccpAddress localAddress;
    private SccpAddress remoteAddress;
    private Object idleTimerHandle;
    private boolean idleTimerActionTaken = false;
    private boolean idleTimerInvoked = false;
    private TRPseudoState state = TRPseudoState.Idle;
    private boolean structured = true;
    private boolean[] invokeIDTable = new boolean[256];
    // only originating side keeps FSM, see: Q.771 - 3.1.5
    protected InvokeImpl[] operationsSent = new InvokeImpl[invokeIDTable.length];
    private int freeCount = invokeIDTable.length;
    // invokde ID space :)
    private int lastInvokeIdIndex = _INVOKE_TABLE_SHIFT - 1;
    private Set<Long> incomingInvokeList = new HashSet<Long>();
    // scheduled components list
    private List<Component> scheduledComponentList = new ArrayList<Component>();
    private int seqControl;
    // If the Dialogue Portion is sent in TCBegin message, the first received
    // Continue message should have the Dialogue Portion too
    private boolean dpSentInBegin = false;
    private boolean previewMode = false;
    private long startDialogTime;
    private int networkId;
    private int localSsn;

    LocalDialogData(LocalDialogDataStorage storage) {
        this.storage=storage;

    }

    @Override
    public int getLocalSsn() {
        return localSsn;
    }

    public void setLocalSsn(int newSsn) {
        localSsn=newSsn;
    }

    private static int getIndexFromInvokeId(Long l) {
        int tmp = l.intValue();
        return tmp + _INVOKE_TABLE_SHIFT;
    }

    private static Long getInvokeIdFromIndex(int index) {
        int tmp = index - _INVOKE_TABLE_SHIFT;
        return new Long(tmp);
    }

    IDialog getDialog() {
        return dialog;
    }

    void setDialog(IDialog d){
        dialog=d;
    }

    @Override
    public Object getUserObject() {
        return userObject;
    }

    @Override
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    @Override
    public ReentrantLock getDialogLock() {
        return dialogLock;
    }

    /*
    @Override
    public long getRemoveTaskTimeout() {
        return removeTaskTimeout;
    }

    @Override
    public void setRemoveTaskTimeout(long removeTaskTimeout) {
        this.removeTaskTimeout = removeTaskTimeout;
    }
    */

    @Override
    public long getIdleTaskTimeout() {
        return idleTaskTimeout;
    }

    @Override
    public void setIdleTaskTimeout(long idleTaskTimeout) {
        this.idleTaskTimeout = idleTaskTimeout;
    }

    @Override
    public ApplicationContextName getLastACN() {
        return lastACN;
    }

    @Override
    public void setLastACN(ApplicationContextName lastACN) {
        this.lastACN = lastACN;
    }

    @Override
    public UserInformation getLastUI() {
        return lastUI;
    }

    @Override
    public void setLastUI(UserInformation lastUI) {
        this.lastUI = lastUI;
    }

    @Override
    public Long getLocalTransactionIdObject() {
        return localTransactionIdObject;
    }

    @Override
    public void setLocalTransactionIdObject(Long localTransactionIdObject) {
        this.localTransactionIdObject = localTransactionIdObject;
    }

    @Override
    public long getLocalTransactionId() {
        return localTransactionId;
    }

    @Override
    public void setLocalTransactionId(long localTransactionId) {
        this.localTransactionId = localTransactionId;
    }

    @Override
    public byte[] getRemoteTransactionId() {
        return remoteTransactionId;
    }

    @Override
    public void setRemoteTransactionId(byte[] remoteTransactionId) {
        this.remoteTransactionId = remoteTransactionId;
    }

    @Override
    public Long getRemoteTransactionIdObject() {
        return remoteTransactionIdObject;
    }

    @Override
    public void setRemoteTransactionIdObject(Long remoteTransactionIdObject) {
        this.remoteTransactionIdObject = remoteTransactionIdObject;
    }

    @Override
    public Long getLocalRelayedTransactionIdObject() {
        return localRelayedTransactionIdObject;
    }

    @Override
    public void setLocalRelayedTransactionIdObject(Long localRelayedTransactionIdObject) {
        this.localRelayedTransactionIdObject = localRelayedTransactionIdObject;
    }

    @Override
    public SccpAddress getLocalAddress() {
        return localAddress;
    }

    @Override
    public void setLocalAddress(SccpAddress localAddress) {
        this.localAddress = localAddress;
    }

    @Override
    public SccpAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public void setRemoteAddress(SccpAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    public boolean isIdleTimerActionTaken() {
        return idleTimerActionTaken;
    }

    @Override
    public void setIdleTimerActionTaken(boolean idleTimerActionTaken) {
        this.idleTimerActionTaken = idleTimerActionTaken;
    }

    @Override
    public boolean isIdleTimerInvoked() {
        return idleTimerInvoked;
    }

    @Override
    public void setIdleTimerInvoked(boolean idleTimerInvoked) {
        this.idleTimerInvoked = idleTimerInvoked;
    }

    @Override
    public TRPseudoState getState() {
        return state;
    }

    @Override
    public void setState(TRPseudoState state) {
        this.state = state;
    }

    @Override
    public boolean isStructured() {
        return structured;
    }

    @Override
    public void setStructured(boolean structured) {
        this.structured = structured;
    }

    @Override
    public Set<? extends ITCAPOperation> listTCAPOpeartions() {
        return new HashSet<>(invokes.values());
    }

    @Override
    public ITCAPOperation newTCAPOperation(InvokeImpl invoke) throws TCAPException {
        if(invoke.getInvokeId()==null) {
            invoke.setInvokeId(allocateInvokeId());
        }
        LocalTCAPOperation to=new LocalTCAPOperation(this,invoke);
        if(invokes.containsKey(invoke.getInvokeId()))
            throw new TCAPException("Invoke ID already used");
        this.invokes.put(invoke.getInvokeId(),to);
        return to;
    }

    @Override
    public ITCAPOperation getTCAPOperation(Long invokeId) {
        return invokes.get(invokeId);
    }

    @Override
    public boolean removeScheduledComponent(ScheduledComponentRemoveFilter f) {
        for(int index = 0; index < scheduledComponentList.size() ;index++) {
            Component cr = this.scheduledComponentList.get(index);
            if (f.filter(cr)) {
                this.scheduledComponentList.remove(index);
                /** just remove - cancelling timer already handled in Dialog **/
                return true;
            }
        }
        return false;
    }

    @Override
    public Long allocateInvokeId() throws TCAPException {
        try {
            getDialogLock().lock();
            if (this.freeCount == 0) {
                throw new TCAPException("No free invokeId");
            }

            int tryCnt = 0;
            while (true) {
                if (++this.lastInvokeIdIndex >= this.invokeIDTable.length)
                    this.lastInvokeIdIndex = 0;
                if (this.invokeIDTable[this.lastInvokeIdIndex] == _INVOKEID_FREE) {
                    freeCount--;
                    this.invokeIDTable[this.lastInvokeIdIndex] = _INVOKEID_TAKEN;
                    return getInvokeIdFromIndex(this.lastInvokeIdIndex);
                }
                if (++tryCnt >= 256)
                    throw new TCAPException("No free invokeId");
            }

        } finally {
            getDialogLock().unlock();
        }
    }

    void releaseInvokeId(Long i) {
        this.invokeIDTable[getIndexFromInvokeId(i)]=_INVOKEID_FREE;
    }

    @Override
    public boolean addIncomingInvoke(Long invokeId) {
        synchronized (this.incomingInvokeList) {
            if (this.incomingInvokeList.contains(invokeId))
                return false;
            else {
                this.incomingInvokeList.add(invokeId);
                return true;
            }
        }

    }

    @Override
    public boolean removeIncomingInvoke(Long invokeId) {
        synchronized (this.incomingInvokeList) {
            return this.incomingInvokeList.remove(invokeId);
        }
    }

    @Override
    public List<Component> getScheduledComponentList() {
        return scheduledComponentList;
    }

    @Override
    public void addScheduledComponent(Component c) {
        scheduledComponentList.add(c);
    }

    public void clearScheduledComponentList() {
        scheduledComponentList.clear();
    }

    public int getSeqControl() {
        return seqControl;
    }

    public void setSeqControl(int seqControl) {
        this.seqControl = seqControl;
    }

    public boolean isDpSentInBegin() {
        return dpSentInBegin;
    }

    public void setDpSentInBegin(boolean dpSentInBegin) {
        this.dpSentInBegin = dpSentInBegin;
    }

    public long getStartDialogTime() {
        return startDialogTime;
    }

    public void setStartDialogTime(long startDialogTime) {
        this.startDialogTime = startDialogTime;
    }

    public int getNetworkId() {
        return networkId;
    }

    public void setNetworkId(int networkId) {
        this.networkId = networkId;
    }

    public void freeTcapOperation(LocalTCAPOperation op) {
        invokes.remove(op.getInvokeId());
    }

    private Object getIdleTimerHandle() {
        return idleTimerHandle;
    }
    private void setIdleTimerHandle(Object idleTimerHandle) {
        this.idleTimerHandle=idleTimerHandle;
    }

    public void startIdleTimer() {
        if (!isStructured())
            return;

        try {
            getDialogLock().lock();
            if (getIdleTimerHandle() != null) {
                throw new IllegalStateException();
            }
            IdleTimerTask t = new IdleTimerTask(getLocalTransactionIdObject());
            setIdleTimerHandle(storage.getTimerFacility().schedule(t, getIdleTaskTimeout(), TimeUnit.MILLISECONDS));
        } finally {
            getDialogLock().unlock();
        }
    }

    @Override
    public void cancelIdleTimer() {
        if (!isStructured())
            return;

        try {
            getDialogLock().lock();
            Object o=getIdleTimerHandle();
            if(o==null)
                return;
            storage.getTimerFacility().cancel(o);
            setIdleTimerHandle(null);
        } finally {
            getDialogLock().unlock();
        }
    }
}
