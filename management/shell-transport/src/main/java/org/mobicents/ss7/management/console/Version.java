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
package org.mobicents.ss7.management.console;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * Version class reads the version.properties packaged with cli.jar for run time display of Version
 *
 * @author amit.bhayani
 *
 */
public final class Version {

    /**
     * The single instance.
     */
    public static final  Version instance = new Version();

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
                sb.append(",");
            }
            sb.append(key).append('=').append(props.get(key));
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
