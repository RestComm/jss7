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

package org.mobicents.protocols.ss7.tcapAnsi.asn;

import java.io.IOException;
import java.util.concurrent.Future;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.DialogImpl;
import org.mobicents.protocols.ss7.tcapAnsi.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcapAnsi.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.EncodeException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.component.OperationState;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class InvokeImpl implements Invoke {

    // local to stack
    private InvokeClass invokeClass = InvokeClass.Class1;
    private long invokeTimeout = TCAPStackImpl._EMPTY_INVOKE_TIMEOUT;
    private OperationState state = OperationState.Idle;
    private Future timerFuture;
    private OperationTimerTask operationTimerTask = new OperationTimerTask(this);
    private TCAPProviderImpl provider;
    private DialogImpl dialog;

    private Long invokeId;
    private Long correlationId;
    private Invoke correlationInvoke;
    private OperationCode operationCode;
    private Parameter parameter;
    private boolean notLast;

    public InvokeImpl() {
        // Set Default Class
        this.invokeClass = InvokeClass.Class1;
    }

    public InvokeImpl(InvokeClass invokeClass) {
        if (invokeClass == null) {
            this.invokeClass = InvokeClass.Class1;
        } else {
            this.invokeClass = invokeClass;
        }
    }


    @Override
    public InvokeClass getInvokeClass() {
        return this.invokeClass;
    }

    @Override
    public boolean isNotLast() {
        return notLast;
    }

    @Override
    public void setNotLast(boolean val) {
        notLast = val;
    }

    @Override
    public Long getInvokeId() {
        return this.invokeId;
    }

    @Override
    public void setInvokeId(Long i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.invokeId = i;
    }

    @Override
    public Long getCorrelationId() {
        return this.correlationId;
    }

    @Override
    public void setCorrelationId(Long i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Correlation ID our of range: <-128,127>: " + i);
        }
        this.correlationId = i;
    }

    @Override
    public Invoke getCorrelationInvoke() {
        return this.correlationInvoke;
    }

    @Override
    public void setCorrelationInvoke(Invoke val) {
        this.correlationInvoke = val;
    }

    @Override
    public OperationCode getOperationCode() {
        return this.operationCode;
    }

    @Override
    public void setOperationCode(OperationCode i) {
        this.operationCode = i;

    }

    @Override
    public Parameter getParameter() {
        return this.parameter;
    }

    @Override
    public void setParameter(Parameter p) {
        this.parameter = p;
    }

    @Override
    public ComponentType getType() {
        if (this.isNotLast())
            return ComponentType.InvokeNotLast;
        else
            return ComponentType.InvokeLast;
    }


    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {

        this.correlationId = null;
        this.correlationInvoke = null;
        this.operationCode = null;
        this.parameter = null;

        try {
            if (ais.getTag() == Invoke._TAG_INVOKE_NOT_LAST)
                this.setNotLast(true);
            else
                this.setNotLast(false);

            AsnInputStream localAis = ais.readSequenceStream();

            // invokeId & correlationId
            byte[] buf = TcapFactory.readComponentId(localAis, 0, 2);
            if (buf.length >= 1)
                this.setInvokeId((long) buf[0]);
            if (buf.length >= 2)
                this.setCorrelationId((long) buf[1]);

            // operationCode
            if (localAis.available() == 0)
                throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "OperationCode is not found when decoding Invoke");
            int tag = localAis.readTag();
            if ((tag != OperationCode._TAG_NATIONAL && tag != OperationCode._TAG_PRIVATE) || localAis.getTagClass() != Tag.CLASS_PRIVATE
                    || !localAis.isTagPrimitive()) {
                throw new ParseException(RejectProblem.generalIncorrectComponentPortion,
                        "OperationCode in Invoke has bad tag or tag class or is not primitive: tag=" + tag + ", tagClass=" + localAis.getTagClass());
            }
            this.operationCode = TcapFactory.createOperationCode(localAis);

            // Parameter
            this.parameter = TcapFactory.readParameter(localAis);
        } catch (IOException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion,
                    "IOException while decoding Invoke: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion,
                    "AsnException while decoding Invoke: " + e.getMessage(), e);
        } catch (ParseException e) {
            e.setInvokeId(this.invokeId);
            throw e;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {
        if (this.operationCode == null)
            throw new EncodeException("Error encoding Invoke: operationCode is mandatory but is not set");

        try {
            // tag
            if (this.notLast)
                aos.writeTag(Tag.CLASS_PRIVATE, false, Invoke._TAG_INVOKE_NOT_LAST);
            else
                aos.writeTag(Tag.CLASS_PRIVATE, false, Invoke._TAG_INVOKE_LAST);
            int pos = aos.StartContentDefiniteLength();

            // invokeId and correlationId
            byte[] buf;
            if (this.invokeId != null) {
                if (this.correlationId != null) {
                    buf = new byte[2];
                    buf[0] = (byte) (long) this.invokeId;
                    buf[1] = (byte) (long) this.correlationId;
                } else {
                    buf = new byte[1];
                    buf[0] = (byte) (long) this.invokeId;
                }
            } else {
                buf = new byte[0];
            }
            aos.writeOctetString(Tag.CLASS_PRIVATE, Component._TAG_INVOKE_ID, buf);

            // operationCode
            this.operationCode.encode(aos);

            // parameters
            if (this.parameter != null)
                this.parameter.encode(aos);
            else
                ParameterImpl.encodeEmptyParameter(aos);

            aos.FinalizeContent(pos);

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding Invoke: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding Invoke: " + e.getMessage(), e);
        }
    }

    /**
     * @return the invokeTimeout
     */
    public long getTimeout() {
        return invokeTimeout;
    }

    /**
     * @param invokeTimeout the invokeTimeout to set
     */
    public void setTimeout(long invokeTimeout) {
        this.invokeTimeout = invokeTimeout;
    }

    // ////////////////////
    // set methods for //
    // relevant data //
    // ///////////////////
    /**
     * @return the provider
     */
    public TCAPProviderImpl getProvider() {
        return provider;
    }

    /**
     * @param provider the provider to set
     */
    public void setProvider(TCAPProviderImpl provider) {
        this.provider = provider;
    }

    /**
     * @return the dialog
     */
    public DialogImpl getDialog() {
        return dialog;
    }

    /**
     * @param dialog the dialog to set
     */
    public void setDialog(DialogImpl dialog) {
        this.dialog = dialog;
    }

    /**
     * @return the state
     */
    public OperationState getState() {
        return state;
    }

    static int ccccnt = 0;

    /**
     * @param state the state to set
     */
    public void setState(OperationState state) {

        if (this.dialog == null) {
            // bad call on server side.
            return;
        }
        OperationState old = this.state;
        this.state = state;
        if (old != state) {

            switch (state) {
            case Sent:
                // start timer
                this.startTimer();
                break;
            case Idle:
            case Reject_W:
                this.stopTimer();
                dialog.operationEnded(this);
            }
            if (state == OperationState.Sent) {

            } else if (state == OperationState.Idle || state == OperationState.Reject_W) {

            }

        }
    }

    public void onReturnResultLast() {
        this.setState(OperationState.Idle);

    }

    public void onError() {
        this.setState(OperationState.Idle);

    }

    public void onReject() {
        this.setState(OperationState.Idle);
    }

    public synchronized void startTimer() {
        if (this.dialog == null || this.dialog.getPreviewMode())
            return;

        this.stopTimer();
        if (this.invokeTimeout > 0)
            this.timerFuture = this.provider.createOperationTimer(this.operationTimerTask, this.invokeTimeout);
    }

    public synchronized void stopTimer() {

        if (this.timerFuture != null) {
            this.timerFuture.cancel(false);
            this.timerFuture = null;
        }

    }

    public boolean isErrorReported() {
        if (this.invokeClass == InvokeClass.Class1 || this.invokeClass == InvokeClass.Class2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSuccessReported() {
        if (this.invokeClass == InvokeClass.Class1 || this.invokeClass == InvokeClass.Class3) {
            return true;
        } else {
            return false;
        }
    }

    private class OperationTimerTask implements Runnable {
        InvokeImpl invoke;

        OperationTimerTask(InvokeImpl invoke) {
            this.invoke = invoke;
        }

        public void run() {

            try {
                dialog.getDialogLock().lock();

                // op failed, we must delete it from dialog and notify!
                timerFuture = null;
                setState(OperationState.Idle);
                // TC-L-CANCEL
                ((DialogImpl) invoke.dialog).operationTimedOut(invoke);
            } finally {
                dialog.getDialogLock().unlock();
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.isNotLast())
            sb.append("InvokeNotLast[");
        else
            sb.append("InvokeLast[");
        if (this.getInvokeId() != null) {
            sb.append("InvokeId=");
            sb.append(this.getInvokeId());
            sb.append(", ");
        }
        if (this.getCorrelationId() != null) {
            sb.append("CorrelationId=");
            sb.append(this.getCorrelationId());
            sb.append(", ");
        }
        if (this.getOperationCode() != null) {
            sb.append("OperationCode=");
            sb.append(this.getOperationCode());
            sb.append(", ");
        }
        if (this.getParameter() != null) {
            sb.append("Parameter=[");
            sb.append(this.getParameter());
            sb.append("], ");
        }
        if (this.getInvokeClass() != null) {
            sb.append("InvokeClass=");
            sb.append(this.getInvokeClass());
            sb.append(", ");
        }

        sb.append("State=");
        sb.append(this.state);
        sb.append("]");

        return sb.toString();
    }

}
