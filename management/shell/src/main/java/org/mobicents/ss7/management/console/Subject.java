package org.mobicents.ss7.management.console;

/**
 * 
 * @author amit bhayani
 *
 */
public enum Subject {

    LINKSET("linkset");

    private String subject = null;

    private Subject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public static Subject getSubject(String subject) {
        if (subject.compareTo(LINKSET.getSubject()) == 0) {
            return LINKSET;
        }
        return null;
    }

}
