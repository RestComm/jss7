package org.mobicents.protocols.ss7.tcap;

import java.util.Stack;

/**
 * For now a simple class to hide how dialogs are managed
 * @author baranowb
 * 
 */
class DialogIdIndex {

    //5k seems reasonable size.
    public static final int INITIAL_SIZE = 5000;
    // public static final int

    protected Stack<Long> LIFO;
    protected int initialSize = INITIAL_SIZE;

    DialogIdIndex() {
        fillIndex();
    }

    public DialogIdIndex(int initialSize) {
        this.initialSize = initialSize;
        fillIndex();
    }

    protected void fillIndex() {
        LIFO = new Stack<Long>();
        for (long id = 1; id < initialSize; id++) {
            LIFO.add(id);
        }
    }

    public Long pop() {
        Long id = LIFO.pop();
        return id;
    }

    public void push(Long id) {
        this.LIFO.push(id);
    }

}
