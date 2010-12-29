package org.mobicents.ss7.management.console;

/**
 * Represents the Subject of command formated as "subject <options>".
 * 
 * @author amit bhayani
 * 
 */
public enum Subject {

    /**
     * linkset subject. Any command to manipulate the linkset, should begin with
     * linkset subject
     */
    LINKSET("linkset");

    private String subject = null;

    private Subject(String subject) {
        this.subject = subject;
    }

    /**
     * get the string representation of this subject enum
     * 
     * @return
     */
    public String getSubject() {
        return subject;
    }

    /**
     * get the corresponding enum for passed subject String. Returns null if
     * there is no subject enum defined for passed subject string
     * 
     * @param subject
     * @return
     */
    public static Subject getSubject(String subject) {
        if (subject.compareTo(LINKSET.getSubject()) == 0) {
            return LINKSET;
        }
        return null;
    }

}
