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

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author amit bhayani
 *
 */
public class SecurityActions {
    private interface TCLAction {
        class UTIL {
            static TCLAction getTCLAction() {
                return System.getSecurityManager() == null ? NON_PRIVILEGED : PRIVILEGED;
            }

            public static ClassLoader getClassLoader(Class<?> cls) {
                return getTCLAction().getClassLoader(cls);
            }

            public static String getSystemProperty(String name) {
                return getTCLAction().getSystemProperty(name);
            }

            public static String getEnvironmentVariable(String name) {
                return getTCLAction().getEnvironmentVariable(name);
            }
        }

        TCLAction NON_PRIVILEGED = new TCLAction() {

            @Override
            public ClassLoader getClassLoader(Class<?> cls) {
                return cls.getClassLoader();
            }

            @Override
            public String getSystemProperty(String name) {
                return System.getProperty(name);
            }

            @Override
            public String getEnvironmentVariable(String name) {
                return System.getenv(name);
            }
        };

        TCLAction PRIVILEGED = new TCLAction() {

            @Override
            public ClassLoader getClassLoader(final Class<?> cls) {
                return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return cls.getClassLoader();
                    }
                });
            }

            @Override
            public String getSystemProperty(final String name) {
                return (String) AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return System.getProperty(name);
                    }
                });
            }

            @Override
            public String getEnvironmentVariable(final String name) {
                return (String) AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return System.getenv(name);
                    }
                });
            }
        };

        ClassLoader getClassLoader(Class<?> cls);

        String getEnvironmentVariable(String name);

        String getSystemProperty(String name);
    }

    protected static String getSystemProperty(String name) {
        return TCLAction.UTIL.getSystemProperty(name);
    }

    protected static String getEnvironmentVariable(String name) {
        return TCLAction.UTIL.getEnvironmentVariable(name);
    }

    protected static ClassLoader getClassLoader(Class<?> cls) {
        return TCLAction.UTIL.getClassLoader(cls);
    }
}
