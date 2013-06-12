package org.mobicents.protocols.ss7.cap;

import static org.testng.Assert.assertEquals;

import java.util.EnumSet;

import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.testng.annotations.Test;

public class FirstTest {

    @Test(groups = { "FirstTest", "A" })
    public void TestATest() {
        EnumSet<EventTypeBCSM> all = EnumSet.allOf(EventTypeBCSM.class);

        for (EventTypeBCSM en : all) {
            EventTypeBCSM en2 = EventTypeBCSM.getInstance(en.getCode());
            assertEquals(en, en2);
        }

        int ff = 0;
        ff++;
    }
}
