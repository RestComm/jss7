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

package org.mobicents.protocols.ss7.mtp;

/**
 *
 *
 * @author kulikov
 */
public class FastHDLC {

    public static final int RETURN_COMPLETE_FLAG = 0x1000;
    public static final int RETURN_DISCARD_FLAG = 0x2000;
    public static final int RETURN_EMPTY_FLAG = 0x4000;
    /*
     * Unlike most HDLC implementations, we define only two states, when we are in a valid frame, and when we are searching for
     * a frame header
     */
    public static final int FRAME_SEARCH = 0;
    public static final int PROCESS_FRAME = 1;
    /*
     *
     * HDLC Search State table -- Look for a frame header. The return value of this table is as follows:
     *
     * |---8---|---7---|---6---|---5---|---4---|---3---|---2---|---1---| | Z E R O E S | Next | Bits Consumed |
     * |-------|-------|-------|-------|-------|-------|-------|-------|
     *
     * The indexes for this table are the state (0 or 1) and the next 8 bits of the stream.
     *
     * Note that this table is only used for state 0 and 1.
     *
     * The user should discard the top "bits consumed" bits of data before the next call. "Next state" represents the actual
     * next state for decoding.
     */
    private int[] hdlc_search = new int[256];

    /*
     * HDLC Data Table
     *
     * The indexes to this table are the number of one's we've seen so far (0-5) and the next 10 bits of input (which is enough
     * to guarantee us that we will retrieve at least one byte of data (or frame or whatever).
     *
     * The format for the return value is:
     *
     * Bits 15: Status (1=Valid Data, 0=Control Frame (see bits 7-0 for type)) Bits 14-12: Number of ones in a row, so far Bits
     * 11-8: The number of bits consumed (0-10) Bits 7-0: The return data (if appropriate)
     *
     * The next state is simply bit #15
     */
    public static final int CONTROL_COMPLETE = 1;
    public static final int CONTROL_ABORT = 2;
    public static final int STATUS_MASK = (1 << 15);
    public static final int STATUS_VALID = (1 << 15);
    public static final int STATUS_CONTROL = (0 << 15);
    public static final int STATE_MASK = (1 << 15);
    public static final int ONES_MASK = (7 << 12);
    public static final int DATA_MASK = (0xff);
    private int[][] hdlc_frame = new int[6][1024];
    private int[] minbits = new int[] { 8, 10 };
    /*
     * Last, but not least, we have the encoder table. It takes as its indices the number of ones so far and a byte of data and
     * returns an int composed of the following fields:
     *
     * Bots 31-22: Actual Data Bits 21-16: Unused Bits 15-8: Number of ones Bits 3-0: Number of bits of output (13-4) to use
     *
     * Of course we could optimize by reducing to two tables, but I don't really think it's worth the trouble at this point.
     */
    private int[][] hdlc_encode = new int[6][256];

    private int hdlc_search_precalc(int c) {
        int x, p = 0;
        /*
         * Look for a flag. If this isn't a flag, line us up for the next possible shot at a flag
         */

        /*
         * If it's a flag, we go to state 1, and have consumed 8 bits
         */
        if (c == 0x7e) {
            return 0x10 | 8;

            /*
             * If it's an abort, we stay in the same state and have consumed 8 bits
             */
        }
        if (c == 0x7f) {
            return 0x00 | 8;

            /*
             * If it's all 1's, we state in the same state and have consumed 8 bits
             */
        }
        if (c == 0xff) {
            return 0x00 | 8;

            /*
             * If we get here, we must have at least one zero in us but we're not the flag. So, start at the end (LSB) and work
             * our way to the top (MSB) looking for a zero. The position of that 0 is most optimistic start of a real frame
             * header
             */
        }
        x = 1;
        p = 7;
        while ((p != 0) && ((c & x) != 0)) {
            x <<= 1;
            p--;
        }
        return p;
    }

    private int HFP(int status, int ones, int bits, int data) {
        return ((status) | ((ones) << 12) | ((bits) << 8) | (data));
    }

    private int hdlc_frame_precalc(int x, int c) {
        /*
         * Assume we have seen 'x' one's so far, and have read the bottom 10 bytes of c (MSB first). Now, we HAVE to have a byte
         * of data or a frame or something. We are assumed to be at the beginning of a byte of data or something
         */
        int ones = x;
        int data = 0;
        int bits = 0;
        int consumed = 0;
        while (bits < 8) {
            data >>>= 1;
            consumed++;
            if (ones == 5) {
                /* We've seen five ones */
                if ((c & 0x0200) != 0) {
                    /* Another one -- Some sort of signal frame */
                    if ((!((c & 0x0100) != 0)) && (bits == 6)) {
                        /* This is a frame terminator (10) */
                        return HFP(0, 0, 8, CONTROL_COMPLETE);
                    } else {
                        /*
                         * Yuck! It's something else... Abort this entire frame, and start looking for a good frame
                         */
                        return HFP(0, 0, consumed + 1, CONTROL_ABORT);
                    }
                } else {
                    /* It's an inserted zero, just skip it */
                    ones = 0;
                    data <<= 1;
                }
            } else {
                /*
                 * Add it to our bit list, LSB to MSB
                 */
                if ((c & 0x0200) != 0) {
                    data |= 0x80;
                    ones++;
                } else {
                    ones = 0;
                }
                bits++;
            }
            c <<= 1;
        }
        /* Consume the extra 0 now rather than later. */
        if (ones == 5) {
            ones = 0;
            consumed++;
        }
        return HFP(STATUS_VALID, ones, consumed, data);
    }

    private int hdlc_encode_precalc(int x, int y) {
        int bits = 0;
        int ones = x;
        int data = 0;
        int z;
        for (z = 0; z < 8; z++) {
            /* Zero-stuff if needed */
            if (ones == 5) {
                /* Stuff a zero */
                data <<= 1;
                ones = 0;
                bits++;
            }
            if ((y & 0x01) != 0) {
                /* There's a one */
                data <<= 1;
                data |= 0x1;
                ones++;
                bits++;
            } else {
                data <<= 1;
                ones = 0;
                bits++;
            }
            y >>= 1;
        }
        /* Special case -- Stuff the zero at the end if appropriate */
        if (ones == 5) {
            /* Stuff a zero */
            data <<= 1;
            ones = 0;
            bits++;
        }
        data <<= (10 - bits);
        return (data << 22) | (ones << 8) | (bits);
    }

    public void fasthdlc_precalc() {
        int x;
        int y;
        /* First the easy part -- the searching */
        for (x = 0; x < 256; x++) {
            hdlc_search[x] = hdlc_search_precalc(x);
        }
        /* Now the hard part -- the frame tables */
        for (x = 0; x < 6; x++) {
            /*
             * Given the # of preceeding ones, process the next byte of input (up to 10 actual bits)
             */
            for (y = 0; y < 1024; y++) {
                hdlc_frame[x][y] = hdlc_frame_precalc(x, y);
            }
        }
        /* Now another not-so-hard part, the encoding table */
        for (x = 0; x < 6; x++) {
            for (y = 0; y < 256; y++) {
                hdlc_encode[x][y] = hdlc_encode_precalc(x, y);
            }
        }
    }

    public void fasthdlc_init(HdlcState h) {
        /* Initializes all states appropriately */
        h.state = 0;
        h.bits = 0;
        h.data = 0;
        h.ones = 0;

    }

    public int fasthdlc_tx_load_nocheck(HdlcState h, int c) {
        int res;
        res = hdlc_encode[h.ones][c];
        h.ones = ((res & 0xf00) >> 8);
        h.data |= ((res & 0xffc00000) >>> h.bits);
        h.bits += (res & 0xf);
        return 0;
    }

    public int fasthdlc_tx_load(HdlcState h, int c) {
        /* Gotta have at least 10 bits left */
        if (h.bits > 22) {
            return -1;
        }
        return fasthdlc_tx_load_nocheck(h, c);
    }

    public int fasthdlc_tx_frame_nocheck(HdlcState h) {
        h.ones = 0;
        h.data |= (0x7e000000 >> h.bits);
        h.bits += 8;
        return 0;
    }

    public int fasthdlc_tx_frame(HdlcState h) {
        if (h.bits > 24) {
            return -1;
        }
        return fasthdlc_tx_frame_nocheck(h);
    }

    public int fasthdlc_tx_run_nocheck(HdlcState h) {
        int b;
        b = h.data >> 24;
        h.bits -= 8;
        h.data <<= 8;
        return b;
    }

    public int fasthdlc_tx_run(HdlcState h) {
        if (h.bits < 8) {
            return -1;
        }
        return fasthdlc_tx_run_nocheck(h);
    }

    public int fasthdlc_rx_load_nocheck(HdlcState h, int b) {
        /* Put the new byte in the data stream */
        h.data |= b << (24 - h.bits);
        h.bits += 8;
        return 0;
    }

    public int fasthdlc_rx_load(HdlcState h, int b) {
        /* Make sure we have enough space */
        if (h.bits > 24) {
            return -1;
        }
        return fasthdlc_rx_load_nocheck(h, b);
    }

    /*
     * Returns a data character if available, logical OR'd with zero or more of RETURN_COMPLETE_FLAG, RETURN_DISCARD_FLAG, and
     * RETURN_EMPTY_FLAG, signifying a complete frame, a discarded frame, or there is nothing to return.
     */
    public int fasthdlc_rx_run(HdlcState h) {
        int next;
        int retval = RETURN_EMPTY_FLAG;
        while ((h.bits >= minbits[h.state]) && (retval == RETURN_EMPTY_FLAG)) {
            /*
             * Run until we can no longer be assured that we will have enough bits to continue
             */
            switch (h.state) {
                case FRAME_SEARCH:
                    /*
                     * Look for an HDLC frame, keying from the top byte.
                     */
                    next = hdlc_search[(h.data >> 24) & 0xff];
                    h.bits -= next & 0x0f;
                    h.data <<= next & 0x0f;
                    h.state = (next >> 4) & 0xff;
                    h.ones = 0;
                    break;
                case PROCESS_FRAME:
                    /* Process as much as the next ten bits */
                    next = hdlc_frame[h.ones][(h.data >>> 22) & 0x3ff]; // Must be 10 bits here, not 8, that's all
                    // next = hdlc_frame_precalc(h.ones, (h.data >> 22)& 0x3ff);
                    h.bits -= (((next & 0x0f00) >> 8) & 0xff);
                    h.data <<= (((next & 0x0f00) >> 8) & 0xff);
                    h.state = ((next & STATE_MASK) >> 15) & 0xff;
                    h.ones = (((next & ONES_MASK) >> 12) & 0xff);
                    switch (next & STATUS_MASK) {
                        case STATUS_CONTROL:
                            if ((next & CONTROL_COMPLETE) != 0) {
                                /* A complete, valid frame received */
                                retval = (RETURN_COMPLETE_FLAG);
                                /* Stay in this state */
                                h.state = 1;
                            } else {
                                /* An abort (either out of sync of explicit) */
                                retval = (RETURN_DISCARD_FLAG);
                            }
                            break;
                        case STATUS_VALID:
                            retval = (next & DATA_MASK);
                    }
            }
        }
        return retval;
    }
}
