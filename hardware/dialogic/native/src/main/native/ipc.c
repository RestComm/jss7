#include "system.h"
#include "msg.h"
#include "sysgct.h"
#include "ss7_inc.h"    /* ss7 message & parameter definitions */

#include "org_mobicents_ss7_hardware_dialogic_InterProcessCommunicator.h"

/*
 * Class:     org_mobicents_gct_InterProcessCommunicator
 * Method:    receive
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_org_mobicents_ss7_hardware_dialogic_InterProcessCommunicator_receive
(JNIEnv *env, jobject obj, jint src_module_id, jbyteArray msg_buffer) {
	jclass cls;
	jfieldID fid;

	jsize len;
	jbyte *body;

    	u16   mlen;           /* message length */
    	u8    *pptr;          /* pointer into parameter area */

	HDR*  h;
	MSG *m;
	int i;

	// h = GCT_grab(src_module_id);
	h = GCT_receive(src_module_id);

	if (h == 0) {
		return -1;
	}

	m = (MSG *)h;

	len = (*env)->GetArrayLength(env, msg_buffer);
    	body = (*env)->GetByteArrayElements(env, msg_buffer, 0);

    	if ((mlen = ((MSG*) h)->len) > 0) {
		
		pptr = get_param((MSG *) h);
		i = 0;	

		switch (m->hdr.type)
		{
		case MTP_MSG_PAUSE :
		  body[i++] = 0; /* SI = 0 */
		  body[i++] = 3; /* PAUSE */
		  break;

		case MTP_MSG_RESUME :
		  body[i++] = 0; /* SI = 0 */
		  body[i++] = 4; /* RESUME */
		  break;

		case MTP_MSG_STATUS :
		  body[i++] = 0; /* SI = 0 */
		  body[i++] = 5; /* STATUS */
		  body[i++] = m->hdr.status; /* actual status. 1 = Remote User Unavailable 2 = Signaling Network Congestion */
		  break;
		
		/* Do we care for API_MSG_RX_IND and default or directly pass to upper layer?
		case API_MSG_RX_IND :
		  UPE_mtp_transfer_ind(m);
		  break;

		default :
		  printf("Rx MSG: type=0x%04x src=0x%02x status=0x%02x len=0x%04x\n", m->hdr.type, m->hdr.src, m->hdr.status, m->len);
		  break;
		*/
		}

	
		while (mlen--) {
			body[i++] = *pptr++;
		}

		(*env)->ReleaseByteArrayElements(env, msg_buffer, body, 0);

		/*
		printf("MTP Message id: %d", h->id);
		*/

		relm(h);
		return i;
	}


	(*env)->ReleaseByteArrayElements(env, msg_buffer, body, 0);
	relm(h);
	return -1;

}

/*
 * Class:     org_mobicents_gct_InterProcessCommunicator
 * Method:    send
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_org_mobicents_ss7_hardware_dialogic_InterProcessCommunicator_send
(JNIEnv *env, jobject obj, jint src_module_id, jint dest_module_id, jbyteArray msg_buffer) {
	jclass cls;
	jfieldID fid;

	jsize len;
	jbyte *body;
	int status;

	/* HDR*  h; */
    	u8  *pptr;
	int i;
	MSG   *m;
	
	
	printf("*********Preparing for sending data*******");

	len = (*env)->GetArrayLength(env, msg_buffer);
    body = (*env)->GetByteArrayElements(env, msg_buffer, 0);
	
	m = getm(0xcf00, 3, 0,len);

	if (m == 0) {
		return -1;
	}

	m->hdr.src = src_module_id;
      	m->hdr.dst = dest_module_id;
/*
	h->src = src_module_id;
	h->dst = dest_module_id;

	((MSG*) h)->len = len;
*/
	m->len = len;
	pptr = get_param(m);

	for(i = 0; i < len; i++) {
		*pptr++ = body[i];
	}

	status = GCT_send(dest_module_id,(HDR *)m);

	/* If the function does not return success then the calling program must release
	the message back to the system using relm().
	*/
	if(status!=0)
		relm(m);

	/*
	printf("Sent %d bytes, status %d", len, status);
	*/

	(*env)->ReleaseByteArrayElements(env, msg_buffer, body, 0);	

	return status;
}

