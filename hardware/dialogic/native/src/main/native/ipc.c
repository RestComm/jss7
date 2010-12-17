#include "system.h"
#include "msg.h"
#include "sysgct.h"

#include "org_mobicents_protocols_ss7_hardware_dialogic_InterProcessCommunicator.h"

/*
 * Class:     org_mobicents_gct_InterProcessCommunicator
 * Method:    receive
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_org_mobicents_protocols_ss7_hardware_dialogic_InterProcessCommunicator_receive
(JNIEnv *env, jobject obj, jint src_module_id, jbyteArray msg_buffer) {
	jclass cls;
	jfieldID fid;

	jsize len;
	jbyte *body;

    u16   mlen;           /* message length */
    u8    *pptr;          /* pointer into parameter area */

	HDR*  h;
	int i;

	h = GCT_receive(src_module_id);

	if (h == 0) {
		return -1;
	}

	len = (*env)->GetArrayLength(env, msg_buffer);
    body = (*env)->GetByteArrayElements(env, msg_buffer, 0);

    if ((mlen = ((MSG*) h)->len) > 0) {
		pptr = get_param((MSG *) h);
		i = 0;		
		while (mlen--) {
			body[i++] = *pptr++;
		}

		(*env)->ReleaseByteArrayElements(env, msg_buffer, body, 0);

		printf("MTP Message id: %d", h->id);

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
JNIEXPORT jint JNICALL Java_org_mobicents_protocols_ss7_hardware_dialogic_InterProcessCommunicator_send
(JNIEnv *env, jobject obj, jint src_module_id, jint dest_module_id, jbyteArray msg_buffer) {
	jclass cls;
	jfieldID fid;

	jsize len;
	jbyte *body;
	int status;

	HDR*  h;
    u8    *pptr;
	int i;
	
	
	printf("*********Preparing for sending data*******");

	len = (*env)->GetArrayLength(env, msg_buffer);
    body = (*env)->GetByteArrayElements(env, msg_buffer, 0);
	
	((MSG*) h) = getm(0xcf00, 3, 0,len);

	if (h == 0) {
		return -1;
	}


	h->src = src_module_id;
	h->dst = dest_module_id;

	((MSG*) h)->len = len;

	pptr = get_param((MSG *) h);

	for(i = 0; i < len; i++) {
		*pptr++ = body[i];
	}

	status = GCT_send(dest_module_id,h);

	printf("Sent %d bytes, status %d", len, status);

	(*env)->ReleaseByteArrayElements(env, msg_buffer, body, 0);	

	return status;
}

