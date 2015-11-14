/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.m3ua.impl;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.As;
import org.mobicents.protocols.ss7.m3ua.Asp;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;

/**
 *
 * @author amit bhayani
 *
 */
public class AspImpl implements XMLSerializable, Asp {

    private static final Logger logger = Logger.getLogger(AspImpl.class);

    protected static final String NAME = "name";

    protected String name;

    /**
     * Local FSM is such that it sends ASP_UP to other side
     **/
    protected FSM localFSM;

    /**
     * Peer FSM is such that it receives ASP_UP from other side
     **/
    protected FSM peerFSM;

    protected AspFactoryImpl aspFactoryImpl;

    protected AsImpl asImpl;

    protected ASPIdentifier aSPIdentifier;

    private MessageFactory messageFactory = new MessageFactoryImpl();

    protected State state = AspState.DOWN;

    public AspImpl() {

    }

    public AspImpl(String name, AspFactoryImpl aspFactroy) {
        this.name = name;
        this.aspFactoryImpl = aspFactroy;
        this.init();
    }

    private void init() {

        switch (this.aspFactoryImpl.functionality) {
            case IPSP:
                if (this.aspFactoryImpl.exchangeType == ExchangeType.SE) {
                    if (this.aspFactoryImpl.ipspType == IPSPType.CLIENT) {
                        // If this ASP is IPSP and client side, it should send the
                        // ASP_UP to other side
                        this.initLocalFSM();
                    } else {
                        // else this will receive ASP_UP from other side
                        this.initPeerFSM();
                    }
                } else {
                    // If this is IPSP and DE, it should maintain two states. One
                    // for sending ASP_UP to other side and other for receiving
                    // ASP_UP form
                    // other side
                    this.initPeerFSM();
                    this.initLocalFSM();
                }
                break;
            case AS:
                if (this.aspFactoryImpl.exchangeType == ExchangeType.SE) {
                    // If this is AS side, it should always send the ASP_UP to other
                    // side
                    this.initLocalFSM();
                } else {
                    // If DE it should maintain two states
                    this.initPeerFSM();
                    this.initLocalFSM();
                }
                break;
            case SGW:
                if (this.aspFactoryImpl.exchangeType == ExchangeType.SE) {
                    // If this is SGW, it should always receive ASP_UP from other
                    // side
                    this.initPeerFSM();
                } else {
                    // If DE it should maintain two states
                    this.initPeerFSM();
                    this.initLocalFSM();
                }
                break;
        }
    }

    private void initLocalFSM() {
        this.localFSM = new FSM(this.name + "_LOCAL");
        // Define states
        this.localFSM.createState(AspState.DOWN_SENT.toString()).setOnEnter(new AspStateEnterDown(this));
        this.localFSM.createState(AspState.DOWN.toString()).setOnEnter(new AspStateEnterDown(this));
        this.localFSM.createState(AspState.UP_SENT.toString()).setOnEnter(new AspStateEnterDown(this));
        this.localFSM.createState(AspState.INACTIVE.toString()).setOnEnter(new AspStateEnterInactive(this));
        this.localFSM.createState(AspState.ACTIVE_SENT.toString()).setOnEnter(new AspStateEnterInactive(this));
        this.localFSM.createState(AspState.ACTIVE.toString()).setOnEnter(new AspStateEnterActive(this));
        this.localFSM.createState(AspState.INACTIVE_SENT.toString()).setOnEnter(new AspStateEnterInactive(this));

        this.localFSM.setStart(AspState.DOWN.toString());
        this.localFSM.setEnd(AspState.DOWN.toString());

        // Define Transitions

        // ******************************************************************/
        // DOWN /
        // ******************************************************************/
        this.localFSM.createTransition(TransitionState.COMM_UP, AspState.DOWN.toString(), AspState.UP_SENT.toString());
        // .setHandler(new AspTransDwnToAspUpSnt(this, this.fsm));

        this.localFSM.createTransition(TransitionState.COMM_DOWN, AspState.DOWN.toString(), AspState.DOWN.toString());

        // ******************************************************************/
        // UP_SENT/
        // ******************************************************************/
        // Keep sending ASP_UP if timeout occurs.
        this.localFSM.createTimeoutTransition(AspState.UP_SENT.toString(), AspState.UP_SENT.toString(), 2000).setHandler(
                new THLocalAspDwnToAspUpSnt(this, this.localFSM));

        this.localFSM.createTransition(TransitionState.ASP_INACTIVE, AspState.UP_SENT.toString(), AspState.INACTIVE.toString());

        this.localFSM.createTransition(TransitionState.ASP_ACTIVE_SENT, AspState.UP_SENT.toString(),
                AspState.ACTIVE_SENT.toString());

        this.localFSM.createTransition(TransitionState.ASP_DOWN_SENT, AspState.UP_SENT.toString(),
                AspState.DOWN_SENT.toString());

        this.localFSM.createTransition(TransitionState.COMM_DOWN, AspState.UP_SENT.toString(), AspState.DOWN.toString());

        // ******************************************************************/
        // ACTIVE_SENT/
        // ******************************************************************/
        // TODO Keep sending ASP_ACTIVE ?
        this.localFSM.createTimeoutTransition(AspState.ACTIVE_SENT.toString(), AspState.ACTIVE_SENT.toString(), 2000);

        this.localFSM.createTransition(TransitionState.ASP_ACTIVE_ACK, AspState.ACTIVE_SENT.toString(),
                AspState.ACTIVE.toString());
        // .setHandler(new AspTransActSntToAct(this, this.fsm));

        this.localFSM.createTransition(TransitionState.ASP_DOWN_SENT, AspState.ACTIVE_SENT.toString(),
                AspState.DOWN_SENT.toString());

        this.localFSM.createTransition(TransitionState.COMM_DOWN, AspState.ACTIVE_SENT.toString(), AspState.DOWN.toString());

        // ******************************************************************/
        // ACTIVE/
        // ******************************************************************/
        this.localFSM.createTransition(TransitionState.ASP_INACTIVE_SENT, AspState.ACTIVE.toString(),
                AspState.INACTIVE_SENT.toString());
        // .setHandler(new AspTransActToInactSnt(this, this.fsm));

        this.localFSM.createTransition(TransitionState.OTHER_ALTERNATE_ASP_ACTIVE, AspState.ACTIVE.toString(),
                AspState.INACTIVE.toString());

        this.localFSM
                .createTransition(TransitionState.ASP_DOWN_SENT, AspState.ACTIVE.toString(), AspState.DOWN_SENT.toString());

        this.localFSM.createTransition(TransitionState.COMM_DOWN, AspState.ACTIVE.toString(), AspState.DOWN.toString());
        // .setHandler(new AspTransActToDwn(this, this.fsm));

        // ******************************************************************/
        // INACTIVE/
        // ******************************************************************/
        this.localFSM.createTransition(TransitionState.COMM_DOWN, AspState.INACTIVE.toString(), AspState.DOWN.toString());
        // .setHandler(new AspTransInactToDwn(this, this.fsm));

        this.localFSM.createTransition(TransitionState.ASP_ACTIVE_SENT, AspState.INACTIVE.toString(),
                AspState.ACTIVE_SENT.toString());

        this.localFSM.createTransition(TransitionState.ASP_DOWN_SENT, AspState.INACTIVE.toString(),
                AspState.DOWN_SENT.toString());

        // ******************************************************************/
        // INACTIVE_SENT/
        // ******************************************************************/
        // TODO keep sending INACTIVE ASP ?
        this.localFSM.createTimeoutTransition(AspState.INACTIVE_SENT.toString(), AspState.INACTIVE_SENT.toString(), 2000);
        // TODO Take care of this .setHandler(new
        // AspTransActToInactSnt(this,
        // this.fsm));

        this.localFSM.createTransition(TransitionState.ASP_INACTIVE_ACK, AspState.INACTIVE_SENT.toString(),
                AspState.INACTIVE.toString());

        this.localFSM.createTransition(TransitionState.ASP_DOWN_SENT, AspState.INACTIVE_SENT.toString(),
                AspState.DOWN_SENT.toString());

        this.localFSM.createTransition(TransitionState.COMM_DOWN, AspState.INACTIVE_SENT.toString(), AspState.DOWN.toString());

        // ******************************************************************/
        // DOWN_SENT/
        // ******************************************************************/
        this.localFSM.createTransition(TransitionState.ASP_DOWN_ACK, AspState.DOWN_SENT.toString(), AspState.DOWN.toString());

        this.localFSM.createTransition(TransitionState.COMM_DOWN, AspState.DOWN_SENT.toString(), AspState.DOWN.toString());
    }

    private void initPeerFSM() {
        this.peerFSM = new FSM(this.name + "_PEER");
        // Define states
        this.peerFSM.createState(AspState.DOWN.toString()).setOnEnter(new AspStateEnterDown(this));
        this.peerFSM.createState(AspState.ACTIVE.toString()).setOnEnter(new AspStateEnterActive(this));
        this.peerFSM.createState(AspState.INACTIVE.toString()).setOnEnter(new AspStateEnterInactive(this));

        this.peerFSM.setStart(AspState.DOWN.toString());
        this.peerFSM.setEnd(AspState.DOWN.toString());

        // Define Transitions

        // ******************************************************************/
        // STATE DOWN /
        // ******************************************************************/
        this.peerFSM.createTransition(TransitionState.COMM_UP, AspState.DOWN.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.COMM_DOWN, AspState.DOWN.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.ASP_UP, AspState.DOWN.toString(), AspState.INACTIVE.toString());

        // If the SGP receives any other M3UA messages before an ASP Up message
        // is received (other than ASP Down; see Section 4.3.4.2), the SGP MAY
        // discard them.
        this.peerFSM.createTransition(TransitionState.DAUD, AspState.DOWN.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.ASP_ACTIVE, AspState.DOWN.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.ASP_INACTIVE, AspState.DOWN.toString(), AspState.DOWN.toString());
        this.peerFSM.createTransition(TransitionState.PAYLOAD, AspState.DOWN.toString(), AspState.DOWN.toString());

        this.peerFSM.createTransition(TransitionState.ASP_DOWN, AspState.DOWN.toString(), AspState.DOWN.toString());
        // TODO : For REG_REQ/DREG_REQ we don't support dynamic adding of
        // key.
        // Handle this

        // ******************************************************************/
        // STATE INACTIVE /
        // ******************************************************************/
        this.peerFSM.createTransition(TransitionState.COMM_DOWN, AspState.INACTIVE.toString(), AspState.DOWN.toString());
        // Mo transition needed? .setHandler(new RemAspTransInactToDwn(this,
        // this.fsm));

        this.peerFSM.createTransition(TransitionState.ASP_UP, AspState.INACTIVE.toString(), AspState.INACTIVE.toString());

        this.peerFSM.createTransition(TransitionState.ASP_DOWN, AspState.INACTIVE.toString(), AspState.DOWN.toString());

        this.peerFSM.createTransition(TransitionState.ASP_ACTIVE, AspState.INACTIVE.toString(), AspState.ACTIVE.toString());

        // TODO: Ignore payload if Remote ASP is still INACTIVE. may be log
        // it?
        this.peerFSM.createTransition(TransitionState.PAYLOAD, AspState.INACTIVE.toString(), AspState.INACTIVE.toString());

        // ******************************************************************/
        // STATE ACTIVE /
        // ******************************************************************/
        this.peerFSM.createTransition(TransitionState.COMM_DOWN, AspState.ACTIVE.toString(), AspState.DOWN.toString());

        this.peerFSM.createTransition(TransitionState.ASP_UP, AspState.ACTIVE.toString(), AspState.INACTIVE.toString());

        this.peerFSM.createTransition(TransitionState.ASP_DOWN, AspState.ACTIVE.toString(), AspState.DOWN.toString());

        this.peerFSM.createTransition(TransitionState.ASP_INACTIVE, AspState.ACTIVE.toString(), AspState.INACTIVE.toString());
        // No transition handler .setHandler(new RemAspTransActToInact(this,
        // this.fsm));

        this.peerFSM.createTransition(TransitionState.PAYLOAD, AspState.ACTIVE.toString(), AspState.ACTIVE.toString());

        // This transition will be signaled from SGW
        this.peerFSM.createTransition(TransitionState.OTHER_ALTERNATE_ASP_ACTIVE, AspState.ACTIVE.toString(),
                AspState.INACTIVE.toString());
    }

    public String getName() {
        return this.name;
    }

    public boolean isStarted() {
        return this.aspFactoryImpl.started;
    }

    public boolean isConnected() {
        return this.isStarted() && this.isUp();
    }

    public boolean isUp() {
        return this.state.getName().equals(State.STATE_ACTIVE);
    }

    public State getState() {
        return this.state;
    }

    public FSM getLocalFSM() {
        return localFSM;
    }

    public FSM getPeerFSM() {
        return peerFSM;
    }

    public As getAs() {
        return asImpl;
    }

    public void setAs(AsImpl asImpl) {
        this.asImpl = asImpl;
    }

    public AspFactoryImpl getAspFactory() {
        return this.aspFactoryImpl;
    }

    public ASPIdentifier getASPIdentifier() {
        return aSPIdentifier;
    }

    public void setASPIdentifier(ASPIdentifier identifier) {
        aSPIdentifier = identifier;
    }

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<AspImpl> ASP_XML = new XMLFormat<AspImpl>(AspImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, AspImpl aspImpl) throws XMLStreamException {
            aspImpl.name = xml.getAttribute(NAME, "");
        }

        @Override
        public void write(AspImpl aspImpl, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(NAME, aspImpl.name);
        }
    };
}
