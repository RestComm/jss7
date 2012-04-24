package org.mobicents.protocols.ss7.tcap;

import java.util.Stack;

import org.mobicents.protocols.ss7.tcap.api.TCAPException;

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
    
    private String no_more_dialog_error_message;

    DialogIdIndex() {
        fillIndex();
    }

    public DialogIdIndex(int initialSize) {
        this.initialSize = initialSize;
        fillIndex();
        no_more_dialog_error_message ="Can't create more TCAP Dialog. Exceeding max concurrent TCAP Dialog's allowed="+initialSize;
    }

    protected void fillIndex() {
        LIFO = new Stack<Long>();
        for (long id = 1; id < initialSize; id++) {
            LIFO.add(id);
        }
    }

    public Long pop() throws TCAPException {
    	if(LIFO.isEmpty()){
    		throw new TCAPException(this.no_more_dialog_error_message);
    	}
    	return LIFO.pop();
    }

    public void push(Long id) {
        this.LIFO.push(id);
    }
}
