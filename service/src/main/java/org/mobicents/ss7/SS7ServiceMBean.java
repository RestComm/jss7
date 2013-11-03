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

package org.mobicents.ss7;

/**
 *
 * @author kulikov
 */
public interface SS7ServiceMBean {
    String ONAME = "org.mobicents.ss7:service=SS7Service";

    void start() throws Exception;

    void stop();

    /**
     * Returns SCCP Provider jndi name.
     */
    String getJndiName();

    /**
     * Returns SS7 Name for this release
     *
     * @return
     */
    String getSS7Name();

    /**
     * Get Vendor supporting this SS7
     *
     * @return
     */
    String getSS7Vendor();

    /**
     * Return SS7 version of this release
     *
     * @return
     */
    String getSS7Version();
}
