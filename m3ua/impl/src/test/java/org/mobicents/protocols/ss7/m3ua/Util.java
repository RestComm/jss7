package org.mobicents.protocols.ss7.m3ua;

import java.io.File;

public class Util {

    public static String getTmpTestDir() {
        try {
            final String[] paths = System.getProperty("surefire.test.class.path").split(File.pathSeparator);
            if (paths.length > 0) {
                // should be xxxxx/target/test-classes
                return paths[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
