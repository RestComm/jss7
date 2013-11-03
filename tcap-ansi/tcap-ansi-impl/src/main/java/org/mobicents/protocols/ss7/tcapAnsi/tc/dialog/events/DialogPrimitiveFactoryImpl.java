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

package org.mobicents.protocols.ss7.tcapAnsi.tc.dialog.events;

import org.mobicents.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcapAnsi.api.DialogPrimitiveFactory;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryRequest;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationRequest;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseRequest;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniRequest;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortRequest;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcapAnsi.tc.component.ComponentPrimitiveFactoryImpl;

/**
 * @author baranowb
 * @author amit bhayani
 *
 */
public class DialogPrimitiveFactoryImpl implements DialogPrimitiveFactory {

    private ComponentPrimitiveFactoryImpl componentPrimitiveFactory;

    public DialogPrimitiveFactoryImpl(ComponentPrimitiveFactory componentPrimitiveFactory) {
        this.componentPrimitiveFactory = (ComponentPrimitiveFactoryImpl) componentPrimitiveFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.DialogPrimitiveFactory
     * #createBegin(org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog)
     */
    public TCQueryRequest createQuery(Dialog d, boolean dialogTermitationPermission) {
        if (d == null) {
            throw new NullPointerException("Dialog is null");
        }
        TCQueryRequestImpl tcbr = new TCQueryRequestImpl(dialogTermitationPermission);
        tcbr.setDialog(d);
        tcbr.setDestinationAddress(d.getRemoteAddress());
        tcbr.setOriginatingAddress(d.getLocalAddress());
        return tcbr;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.DialogPrimitiveFactory
     * #createContinue(org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog)
     */
    public TCConversationRequest createConversation(Dialog d, boolean dialogTermitationPermission) {
        if (d == null) {
            throw new NullPointerException("Dialog is null");
        }
        TCConversationRequestImpl tccr = new TCConversationRequestImpl(dialogTermitationPermission);
        tccr.setDialog(d);
        tccr.setOriginatingAddress(d.getLocalAddress());

        return tccr;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.DialogPrimitiveFactory
     * #createEnd(org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog)
     */
    public TCResponseRequest createResponse(Dialog d) {
        if (d == null) {
            throw new NullPointerException("Dialog is null");
        }
        TCResponseRequestImpl tcer = new TCResponseRequestImpl();
        tcer.setDialog(d);
        return tcer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.DialogPrimitiveFactory#createUAbort
     * (org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog)
     */
    public TCUserAbortRequest createUAbort(Dialog d) {
        if (d == null) {
            throw new NullPointerException("Dialog is null");
        }
        TCUserAbortRequestImpl tcer = new TCUserAbortRequestImpl();
        tcer.setDialog(d);
        return tcer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.DialogPrimitiveFactory
     * #createUni(org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog)
     */
    public TCUniRequest createUni(Dialog d) {
        if (d == null) {
            throw new NullPointerException("Dialog is null");
        }
        TCUniRequestImpl tcur = new TCUniRequestImpl();
        tcur.setDialog(d);
        tcur.setDestinationAddress(d.getRemoteAddress());
        tcur.setOriginatingAddress(d.getLocalAddress());
        return tcur;
    }

    public TCQueryIndication createQueryIndication(Dialog d, boolean dialogTermitationPermission) {

        if (d == null) {
            throw new NullPointerException("Dialog is null");
        }
        TCQueryIndicationImpl tcbi = new TCQueryIndicationImpl(dialogTermitationPermission);
        tcbi.setDialog(d);
        return tcbi;
    }

    public TCConversationIndication createConversationIndication(Dialog d, boolean dialogTermitationPermission) {
        if (d == null) {
            throw new NullPointerException("Dialog is null");
        }
        TCConversationIndicationImpl tcbi = new TCConversationIndicationImpl(dialogTermitationPermission);
        tcbi.setDialog(d);

        return tcbi;
    }

    public TCResponseIndication createResponseIndication(Dialog d) {
        if (d == null) {
            throw new NullPointerException("Dialog is null");
        }
        TCResponseIndicationImpl tcbi = new TCResponseIndicationImpl();
        tcbi.setDialog(d);
        return tcbi;
    }

    public TCUserAbortIndication createUAbortIndication(Dialog d) {
        if (d == null) {
            throw new NullPointerException("Dialog is null");
        }
        TCUserAbortIndicationImpl tcbi = new TCUserAbortIndicationImpl();
        tcbi.setDialog(d);
        return tcbi;
    }

    public TCPAbortIndication createPAbortIndication(Dialog d) {
        if (d == null) {
            throw new NullPointerException("Dialog is null");
        }
        TCPAbortIndicationImpl tcbi = new TCPAbortIndicationImpl();
        tcbi.setDialog(d);
        return tcbi;
    }

    public TCUniIndication createUniIndication(Dialog d) {
        if (d == null) {
            throw new NullPointerException("Dialog is null");
        }
        TCUniIndicationImpl tcbi = new TCUniIndicationImpl();
        tcbi.setDialog(d);
        return tcbi;
    }

    // Aditionals for APDU

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.DialogPrimitiveFactory #createApplicationContextName()
     */
    public ApplicationContext createApplicationContext(long[] oid) {
        return TcapFactory.createApplicationContext(oid);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.DialogPrimitiveFactory #createApplicationContextName()
     */
    public ApplicationContext createApplicationContext(long val) {
        return TcapFactory.createApplicationContext(val);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.DialogPrimitiveFactory #createUserInformation()
     */
    public UserInformation createUserInformation() {
        return TcapFactory.createUserInformation();
    }

    @Override
    public UserInformationElement createUserInformationElement() {
        return TcapFactory.createUserInformationElement();
    }

}
