#include "JNIByteBuffer.h"

#include <iostream>
#include <string>

/*
 * Class:     JNIByteBuffer
 * Method:    initByteBuffer
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_JNIByteBuffer_initByteBuffer(JNIEnv *env, jclass, jobject obj)
{
    int *buffer = (int*)env->GetDirectBufferAddress(obj);
    long capacity = env->GetDirectBufferCapacity(obj);
    buffer[0] = 0;
    buffer[1] = 1;
    buffer[2] = 2;
    buffer[3] = 3;
    buffer[4] = 4;
    std::cout << "buffer capacity: " << std::to_string(capacity) << std::endl;
}