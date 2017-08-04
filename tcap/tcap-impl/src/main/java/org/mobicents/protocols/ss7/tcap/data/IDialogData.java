package org.mobicents.protocols.ss7.tcap.data;

import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;

import java.util.List;
import java.util.Set;

/**
 * Created by piotr.sokolowski on 2017-06-07.
 */
public interface IDialogData extends IDialogDataBase {
    /** Outgoing operations handling **/
    Set<? extends ITCAPOperation> listTCAPOpeartions();
    ITCAPOperation newTCAPOperation(InvokeImpl invoke) throws TCAPException;
    ITCAPOperation getTCAPOperation(Long invokeId);

    Long allocateInvokeId() throws TCAPException;

    List<Component> getScheduledComponentList();
    void addScheduledComponent(Component c);
    void clearScheduledComponentList();

    interface ScheduledComponentRemoveFilter {
        boolean filter(Component c);
    }
    boolean removeScheduledComponent(ScheduledComponentRemoveFilter f);
}
