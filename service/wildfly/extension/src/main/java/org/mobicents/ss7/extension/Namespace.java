package org.mobicents.ss7.extension;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sergeypovarnin on 26.01.17.
 */
enum Namespace {

    // must be first
    UNKNOWN(null),

    SS7_1_0("urn:org.restcomm:ss7-wildfly:1.0"), ;

    /**
     * The current namespace version.
     */
    public static final Namespace CURRENT = SS7_1_0;

    private final String name;

    Namespace(final String name) {
        this.name = name;
    }

    /**
     * Get the URI of this namespace.
     *
     * @return the URI
     */
    public String getUriString() {
        return name;
    }

    private static final Map<String, Namespace> MAP;

    static {
        final Map<String, Namespace> map = new HashMap<String, Namespace>();
        for (Namespace namespace : values()) {
            final String name = namespace.getUriString();
            if (name != null)
                map.put(name, namespace);
        }
        MAP = map;
    }

    public static Namespace forUri(String uri) {
        final Namespace element = MAP.get(uri);
        return element == null ? UNKNOWN : element;
    }

}
