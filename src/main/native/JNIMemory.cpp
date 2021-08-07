#include "JNIMemory.h"

#include <cstring>
#include <stdint.h>
#include <memory>
#include <iostream>

/*
 * Class:     JNIMemory
 * Method:    allocateAndSetDirectMemoryFromCpp
 * Signature: (I)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_JNIMemory_allocateAndSetDirectMemoryFromCpp(JNIEnv *env, jclass thiz, jint size)
{
    auto *ptr = new uint8_t[size];
    memset(ptr, 1, size);
    jobject jbytebuffer = env->NewDirectByteBuffer(ptr, size);
    return jbytebuffer;
}

/*
 * Class:     JNIMemory
 * Method:    allocateAndSetMemoryFromCpp
 * Signature: (I)[B
 */
JNIEXPORT jbyteArray JNICALL Java_JNIMemory_allocateAndSetMemoryFromCpp(JNIEnv *env, jclass thiz, jint size)
{
    std::shared_ptr<uint8_t> data(new uint8_t[size], std::default_delete<uint8_t[]>());
    memset(data.get(), 1, size);
    jbyteArray array = env->NewByteArray(size);
    const auto *byte = reinterpret_cast<const jbyte*>(data.get());
    env->SetByteArrayRegion(array, 0, size, byte);
    return array;
}

/*
 * Class:     JNIMemory
 * Method:    setDirectByteBufferMemory
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_JNIMemory_setDirectByteBufferMemory(JNIEnv *env, jclass thiz, jobject jbytebuffer)
{
    auto *buffer = (uint8_t *)env->GetDirectBufferAddress(jbytebuffer);
    long capacity = env->GetDirectBufferCapacity(jbytebuffer);
    memset(buffer, 1, capacity);
}

/*
 * Class:     JNIMemory
 * Method:    setByteBufferMemory
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_JNIMemory_setByteBufferMemory(JNIEnv *env, jclass thiz, jbyteArray array)
{
    jbyte *data = env->GetByteArrayElements(array, 0);
    auto size = env->GetArrayLength(array);
    memset(data, 1, size);
}
