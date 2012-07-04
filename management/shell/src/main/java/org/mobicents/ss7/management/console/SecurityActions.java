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
