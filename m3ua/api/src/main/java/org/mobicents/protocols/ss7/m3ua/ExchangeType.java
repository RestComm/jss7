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
package org.mobicents.protocols.ss7.m3ua;

/**
 * <p>
 * Two modes of handshake is defined
 * </p>
 * <ol>
 * <li>
 * Single Exchange (SE) model : Only a single exchange of ASP Traffic Maintenance (ASPTM) Messages and ASP State Maintenance
 * (ASPSM) Messages is needed to change the {@link As} states. This means that a set of requests from one end and
 * acknowledgements from the other will be enough.</li>
 * <li>
 * Double Exchange (DE) model : A double exchange of ASPTM and ASPSM messages is normally needed. When using double exchanges
 * for ASPSM messages, the management of the connection in the two directions is considered independent. This means that
 * connections from As-A to As-B is handled independently of connections from As-B to As-A. Therefore, it could happen that only
 * one of the two directions is activated or closed, while the other remains in the same state as it was.</li>
 * </ol>
 *
 * @author amit bhayani
 *
 */
public enum ExchangeType {
    SE("SE"), DE("DE");

    private static final String TYPE_SE = "SE";
    private static final String TYPE_DE = "DE";

    private String type = null;

    private ExchangeType(String type) {
        this.type = type;
    }

    public static ExchangeType getExchangeType(String type) {
        if (TYPE_SE.equals(type)) {
            return SE;
        } else if (TYPE_DE.equals(type)) {
            return DE;
        } else {
            return null;
        }
    }

    public String getType() {
        return this.type;
    }
}
