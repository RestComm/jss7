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

package org.mobicents.protocols.ss7.sccp.impl.router;

/**
 * Class for exception concerning rule.
 *
 * @author baranowb
 *
 */
public class RuleException extends RuntimeException {

    /**
     */
    private static final long serialVersionUID = 6024579057121898624L;
    private RuleImpl rule;

    /**
     *
     */
    public RuleException(RuleImpl rule) {
        this.rule = rule;
    }

    /**
     * @param message
     */
    public RuleException(String message, RuleImpl rule) {
        super(message);
        this.rule = rule;
    }

    /**
     * @param cause
     */
    public RuleException(Throwable cause, RuleImpl rule) {
        super(cause);
        this.rule = rule;
    }

    /**
     * @param message
     * @param cause
     */
    public RuleException(String message, Throwable cause, RuleImpl rule) {
        super(message, cause);
        this.rule = rule;
    }

    public String toString() {
        return "RuleException [rule=" + rule + ", toString()=" + super.toString() + "]";
    }

}
