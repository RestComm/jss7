/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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
package org.mobicents.ss7;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author amit bhayani
 *
 */
public final class Version {

    /**
     * The single instance.
     */
    public static final Version instance = new Version();

    /**
     * The version properties.
     */
    private Properties props;

    /**
     * Do not allow direct public construction.
     */
    private Version() {
        props = loadProperties();
    }

    /**
     * Returns an unmodifiable map of version properties.
     *
     * @return
     */
    public Map getProperties() {
        return Collections.unmodifiableMap(props);
    }

    /**
     * Returns the value for the given property name.
     *
     * @param name - The name of the property.
     * @return The property value or null if the property is not set.
     */
    public String getProperty(final String name) {
        return props.getProperty(name);
    }

    /**
     * Returns the version information as a string.
     *
     * @return Basic information as a string.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object key : props.keySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(" , ");
            }
            sb.append(key).append(" = ").append(props.get(key));
        }
        return sb.toString();
    }

    /**
     * Load the version properties from a resource.
     */
    private Properties loadProperties() {

        props = new Properties();

        try {
            InputStream in = Version.class.getResourceAsStream("version.properties");
            props.load(in);
            in.close();
        } catch (Exception e) {
            throw new Error("Missing version.properties");
        }

        return props;
    }

}
