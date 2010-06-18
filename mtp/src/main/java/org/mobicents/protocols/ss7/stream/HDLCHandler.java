/**
 * 
 */
package org.mobicents.protocols.ss7.stream;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.mobicents.protocols.ss7.mtp.FastHDLC;
import org.mobicents.protocols.ss7.mtp.HdlcState;


/**
 * Util class to handle hdlc stuff. 
 * 
 * @author kulikof
 * @author vralev
 * @author baranowb
 * 
 */
public class HDLCHandler {

	/**
	 * Buffer which holds data to be sent
	 */
	private LinkedList<ByteBuffer> txBuffer = new LinkedList<ByteBuffer>();
	private boolean firstTx = true;
	// ////////////////////
	// Buffers and HDLC //
	// ////////////////////
	private FastHDLC hdlc = new FastHDLC();
	private HdlcState rxState = new HdlcState();
	private HdlcState txState = new HdlcState();

	private int doCRC = 0;
	private int rxCRC = 0xffff;
	private int txCRC = 0xffff;

	// private int rxLen = 0;
	// private int txLen = 0;
	// private int txOffset = 0;

	private int _BUFFER_SIZE_ = 650;
	// private ByteBuffer hdlcTxBuffer = ByteBuffer.wrap(new
	// byte[_BUFFER_SIZE_]);

	// private byte[] hdlcTxBuffer = new byte[_BUFFER_SIZE_];
	// private byte[] hdlcRxBuffer = new byte[_BUFFER_SIZE_];
	private ByteBuffer hdlcRxBuffer = ByteBuffer.allocate(_BUFFER_SIZE_);

	private final static int fcstab[] = new int[] { 0x0000, 0x1189, 0x2312, 0x329b, 0x4624, 0x57ad, 0x6536, 0x74bf, 0x8c48, 0x9dc1, 0xaf5a, 0xbed3,
			0xca6c, 0xdbe5, 0xe97e, 0xf8f7, 0x1081, 0x0108, 0x3393, 0x221a, 0x56a5, 0x472c, 0x75b7, 0x643e, 0x9cc9, 0x8d40, 0xbfdb, 0xae52, 0xdaed,
			0xcb64, 0xf9ff, 0xe876, 0x2102, 0x308b, 0x0210, 0x1399, 0x6726, 0x76af, 0x4434, 0x55bd, 0xad4a, 0xbcc3, 0x8e58, 0x9fd1, 0xeb6e, 0xfae7,
			0xc87c, 0xd9f5, 0x3183, 0x200a, 0x1291, 0x0318, 0x77a7, 0x662e, 0x54b5, 0x453c, 0xbdcb, 0xac42, 0x9ed9, 0x8f50, 0xfbef, 0xea66, 0xd8fd,
			0xc974, 0x4204, 0x538d, 0x6116, 0x709f, 0x0420, 0x15a9, 0x2732, 0x36bb, 0xce4c, 0xdfc5, 0xed5e, 0xfcd7, 0x8868, 0x99e1, 0xab7a, 0xbaf3,
			0x5285, 0x430c, 0x7197, 0x601e, 0x14a1, 0x0528, 0x37b3, 0x263a, 0xdecd, 0xcf44, 0xfddf, 0xec56, 0x98e9, 0x8960, 0xbbfb, 0xaa72, 0x6306,
			0x728f, 0x4014, 0x519d, 0x2522, 0x34ab, 0x0630, 0x17b9, 0xef4e, 0xfec7, 0xcc5c, 0xddd5, 0xa96a, 0xb8e3, 0x8a78, 0x9bf1, 0x7387, 0x620e,
			0x5095, 0x411c, 0x35a3, 0x242a, 0x16b1, 0x0738, 0xffcf, 0xee46, 0xdcdd, 0xcd54, 0xb9eb, 0xa862, 0x9af9, 0x8b70, 0x8408, 0x9581, 0xa71a,
			0xb693, 0xc22c, 0xd3a5, 0xe13e, 0xf0b7, 0x0840, 0x19c9, 0x2b52, 0x3adb, 0x4e64, 0x5fed, 0x6d76, 0x7cff, 0x9489, 0x8500, 0xb79b, 0xa612,
			0xd2ad, 0xc324, 0xf1bf, 0xe036, 0x18c1, 0x0948, 0x3bd3, 0x2a5a, 0x5ee5, 0x4f6c, 0x7df7, 0x6c7e, 0xa50a, 0xb483, 0x8618, 0x9791, 0xe32e,
			0xf2a7, 0xc03c, 0xd1b5, 0x2942, 0x38cb, 0x0a50, 0x1bd9, 0x6f66, 0x7eef, 0x4c74, 0x5dfd, 0xb58b, 0xa402, 0x9699, 0x8710, 0xf3af, 0xe226,
			0xd0bd, 0xc134, 0x39c3, 0x284a, 0x1ad1, 0x0b58, 0x7fe7, 0x6e6e, 0x5cf5, 0x4d7c, 0xc60c, 0xd785, 0xe51e, 0xf497, 0x8028, 0x91a1, 0xa33a,
			0xb2b3, 0x4a44, 0x5bcd, 0x6956, 0x78df, 0x0c60, 0x1de9, 0x2f72, 0x3efb, 0xd68d, 0xc704, 0xf59f, 0xe416, 0x90a9, 0x8120, 0xb3bb, 0xa232,
			0x5ac5, 0x4b4c, 0x79d7, 0x685e, 0x1ce1, 0x0d68, 0x3ff3, 0x2e7a, 0xe70e, 0xf687, 0xc41c, 0xd595, 0xa12a, 0xb0a3, 0x8238, 0x93b1, 0x6b46,
			0x7acf, 0x4854, 0x59dd, 0x2d62, 0x3ceb, 0x0e70, 0x1ff9, 0xf78f, 0xe606, 0xd49d, 0xc514, 0xb1ab, 0xa022, 0x92b9, 0x8330, 0x7bc7, 0x6a4e,
			0x58d5, 0x495c, 0x3de3, 0x2c6a, 0x1ef1, 0x0f78 };

	//filler buffer, we need this to properly use hdlc. It needs contant stream of data, otherwise it consumes valid buffers from ends.
	private static final byte[] _FILLER_BUFFER_ = new byte[] { 0x0,(byte) 0xFF,(byte) 0xFF,0x0};

	public HDLCHandler() {
		super();
		// TODO Auto-generated constructor stub
		// init HDLC
		hdlc.fasthdlc_precalc();
		hdlc.fasthdlc_init(rxState);
		hdlc.fasthdlc_init(txState);

		// copied
		// txOffset = 3;
		firstTx = true;
	}

	public final static int PPP_FCS(int fcs, int c) {
		return ((fcs) >> 8) ^ fcstab[((fcs) ^ (c)) & 0xff];
	}

	public void addToTxBuffer(ByteBuffer bb) {
		// required, to mimic full stream of data.
		this.txBuffer.add(ByteBuffer.wrap(_FILLER_BUFFER_));
		this.txBuffer.add(bb);
		// required, to mimic full stream of data.
		this.txBuffer.add(ByteBuffer.wrap(_FILLER_BUFFER_));
	}

	/**
	 * Sets RX buffer size, this generaly limits one ByteBuffer msg size.
	 * 
	 * @param rxBufferSize
	 */
	public void setRxBufferSize(int rxBufferSize) {
		// use this as buffer size
	}
	public boolean isTxBufferEmpty()
	{
		return this.txBuffer.isEmpty();
	}
	public void clearTxBuffer() {

		this.txBuffer.clear();
		doCRC = 0;
		txCRC = 0xffff;
	}
	/**
	 * Method which should be invoked to process tx. HDLCHandler will fetch
	 * contents of internal tx buffer, perform hdlc and fill passed object After
	 * that its valid to pass to network. Passed buffer should not be used
	 * afterwards. <br>
	 * NOTE: it should be big enough to store hdlc processed data passed in
	 * {@link #addToTxBuffer(ByteBuffer)}
	 * 
	 * @param txBufferToFill
	 *            - buffer which will be filled with hdlc processed bytes
	 */
	public void processTx(ByteBuffer txBufferToFill) {
		if (this.txBuffer.isEmpty()) {
			// nothing to do, we dont have a thing in buffer
			return;
		}
		if (firstTx) {
			firstTx = false;
			// shift offset
			txBufferToFill.put((byte) 0);
			txBufferToFill.put((byte) 0);
			txBufferToFill.put((byte) 0);
		}
		ByteBuffer txFrame = this.txBuffer.get(0);
		
		for (int i = txBufferToFill.position(); i < txBufferToFill.capacity() && !this.txBuffer.isEmpty(); i++) {
			if (txState.bits < 8) {
				// need more bits
				if (doCRC == 0 && txFrame.position() < txFrame.limit()) {
					int data = txFrame.get() & 0xff;
					hdlc.fasthdlc_tx_load(txState, data);
					txCRC = PPP_FCS(txCRC, data);
					if (txFrame.position() == txFrame.limit()) {
						doCRC = 1;
						txCRC ^= 0xffff;
					}
				} else if (doCRC == 1) {
					hdlc.fasthdlc_tx_load_nocheck(txState, (txCRC) & 0xff);
					doCRC = 2;
				} else if (doCRC == 2) {
					hdlc.fasthdlc_tx_load_nocheck(txState, (txCRC >> 8) & 0xff);
					doCRC = 0;
				} else {
					this.txBuffer.remove(0);

					txCRC = 0xffff;
					hdlc.fasthdlc_tx_frame_nocheck(txState);
					if (this.txBuffer.isEmpty()) {
						return;
					} else {
						txFrame = this.txBuffer.get(0);
						
					}
				}
			}

			txBufferToFill.put((byte) hdlc.fasthdlc_tx_run_nocheck(txState));
		}
		
	}

	/**
	 * Method which process received data. Once full msg is received it is
	 * returned, in other case, returns null. Returned buffers may be used
	 * freely.
	 * 
	 * @param rxBuffer
	 * @return
	 */
	public ByteBuffer[] processRx(ByteBuffer rxBuffer) {
		List<ByteBuffer> receivedFrames = new ArrayList<ByteBuffer>();
		int len = rxBuffer.limit();
		while (rxBuffer.position() < len) {
			while (rxState.bits <= 24 && rxBuffer.position() < len) {

				int b = rxBuffer.get() & 0xff;
				hdlc.fasthdlc_rx_load_nocheck(rxState, b);
				if (rxState.state == 0) {
					// octet counting mode
					// seek next frame
				}
			}

			int res = hdlc.fasthdlc_rx_run(rxState);

			switch (res) {
			case FastHDLC.RETURN_COMPLETE_FLAG:
				// frame received and we count it

				// checking length and CRC of the received frame
				if (hdlcRxBuffer.position() == 0) {
					// nothing, empty frame.
	
				} else if (rxCRC == 0xF0B8) {
					// good frame received
					hdlcRxBuffer.flip();
					byte[] buffArray = hdlcRxBuffer.array();

					// -2 for last 2 crc bytes
					byte[] b = new byte[hdlcRxBuffer.limit() -2];

					System.arraycopy(buffArray, 0, b, 0, b.length);
					if(!Arrays.equals(_FILLER_BUFFER_, b))
					{
						//we push empty frames sometimes.
						receivedFrames.add(ByteBuffer.wrap(b));
					}

					hdlcRxBuffer.clear();
				} else {

					// bad frame ignore.
					hdlcRxBuffer.clear();

				}

				rxCRC = 0xffff;
				break;
			case FastHDLC.RETURN_DISCARD_FLAG:
				// looking for next flag
				rxCRC = 0xffff;

				// eCount = 0;
				// bad frame ignore.
				hdlcRxBuffer.clear();

				break;
			case FastHDLC.RETURN_EMPTY_FLAG:

				// hdlcRxBuffer.clear();
				break;
			default:
				if (hdlcRxBuffer.position() > _BUFFER_SIZE_) {

					rxState.state = 0;
					rxCRC = 0xffff;

					hdlcRxBuffer.clear();

					throw new RuntimeException("RX buffer overflowed.");
				} else {

					// rxFrame[rxLen++] = res;

					hdlcRxBuffer.put((byte) res);
					rxCRC = PPP_FCS(rxCRC, res & 0xff);
				}
			}

		}

		if (receivedFrames.size() == 0) {
			return null;
		} else {
			ByteBuffer[] bufs = new ByteBuffer[receivedFrames.size()];
			bufs = receivedFrames.toArray(bufs);
			return bufs;
		}
	}

	
	
	
}
