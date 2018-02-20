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

package org.restcomm.protocols.ss7.sccp.impl.router;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.sccp.LongMessageRule;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class LongMessageRuleImpl implements LongMessageRule, XMLSerializable {
    private static final String FIRST_SPC = "firstSpc";
    private static final String LAST_SPC = "lastSpc";
    private static final String RULE_TYPE = "ruleType";

    private int firstSpc;
    private int lastSpc;
    private LongMessageRuleType ruleType;

    public LongMessageRuleImpl() {
    }

    public LongMessageRuleImpl(int firstSpc, int lastSpc, LongMessageRuleType ruleType) {
        this.firstSpc = firstSpc;
        this.lastSpc = lastSpc;
        this.ruleType = ruleType;
    }

    public LongMessageRuleType getLongMessageRuleType() {
        return ruleType;
    }

    public int getFirstSpc() {
        return firstSpc;
    }

    public int getLastSpc() {
        return lastSpc;
    }

    public boolean matches(int dpc) {
        if (dpc >= this.firstSpc && dpc <= this.lastSpc)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("firstSpc=").append(this.firstSpc).append(", lastSpc=").append(this.lastSpc).append(", ruleType=")
                .append(this.ruleType);
        return sb.toString();
    }

    protected static final XMLFormat<LongMessageRuleImpl> XML = new XMLFormat<LongMessageRuleImpl>(LongMessageRuleImpl.class) {

        public void write(LongMessageRuleImpl ai, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(FIRST_SPC, ai.firstSpc);
            xml.setAttribute(LAST_SPC, ai.lastSpc);
            xml.setAttribute(RULE_TYPE, ai.ruleType.toString());
        }

        public void read(InputElement xml, LongMessageRuleImpl ai) throws XMLStreamException {
            ai.firstSpc = xml.getAttribute(FIRST_SPC).toInt();
            ai.lastSpc = xml.getAttribute(LAST_SPC).toInt();
            String ruleT = xml.getAttribute(RULE_TYPE).toString();
            ai.ruleType = LongMessageRuleType.valueOf(ruleT);
        }
    };
}
