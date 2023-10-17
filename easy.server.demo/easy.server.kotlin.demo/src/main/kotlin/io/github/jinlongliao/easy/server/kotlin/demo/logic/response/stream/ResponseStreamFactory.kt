package io.github.jinlongliao.easy.server.kotlin.demo.logic.response.stream

import io.github.jinlongliao.easy.server.extend.response.ICommonResponse
import io.github.jinlongliao.easy.server.extend.response.IResponseStreamFactory
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils
import io.github.jinlongliao.easy.server.utils.common.HexUtil
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles
import java.nio.BufferOverflowException
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.math.min

class ResponseStreamFactory @JvmOverloads constructor(
    size: Int = 1024,
    /**
     * 快速模式
     */
    private val fastMode: Boolean = false
) : IResponseStreamFactory {
    private var byteBuff: ByteBuffer

    init {
        byteBuff = ByteBuffer.allocate(size)
    }

    /**
     * 写入Byte数值
     *
     * @param iByte
     */
    override fun writeByte(iByte: Byte) {
        if (!fastMode) {
            ensureCapacityInternal(1)
        }
        try {
            byteBuff.put(iByte)
        } catch (e: BufferOverflowException) {
            if (fastMode) {
                ensureCapacityInternal(1)
                byteBuff.put(iByte)
            } else {
                throw e
            }
        }
    }

    override fun writeChar(c: Char) {
        writeByte(c.code.toByte())
    }

    override fun writeBool(bool: Boolean) {
        writeByte((if (bool) 1 else 0).toByte())
    }

    override fun writeDate(date: Date) {
        if (Objects.isNull(date)) {
            writeLong(0L)
        } else {
            writeLong(date.time)
        }
    }

    /**
     * 写入short数值
     *
     * @param iShort
     */
    override fun writeShort(iShort: Short) {
        val shortBytes = ByteArray(2)
        shortBytes[0] = iShort.toByte()
        shortBytes[1] = (iShort.toInt() ushr 8).toByte()
        this.write(shortBytes)
    }

    /**
     * 写入整型数值
     *
     * @param iInt
     */
    override fun writeInt(iInt: Int) {
        val intBytes = ByteArray(4)
        for (index in 0..3) {
            intBytes[index] = (iInt ushr index * 8).toByte()
        }
        this.write(intBytes)
    }

    /**
     * 写入Long型数值
     *
     * @param iLong
     */
    override fun writeLong(iLong: Long) {
        val longBytes = ByteArray(8)
        for (index in 0..7) {
            longBytes[index] = (iLong ushr index * 8).toByte()
        }
        this.write(longBytes)
    }

    /**
     * 写入整型数组
     *
     * @param iIntBytes
     */
    override fun write(iIntBytes: IntArray) {
        for (iIntByte in iIntBytes) {
            writeInt(iIntByte)
        }
    }

    /**
     * 写入二进制数组
     *
     * @param bytes
     */
    private fun write(bytes: ByteArray) {
        if (!fastMode) {
            ensureCapacityInternal(bytes.size)
        }
        try {
            byteBuff.put(bytes)
        } catch (e: BufferOverflowException) {
            if (fastMode) {
                ensureCapacityInternal(bytes.size)
                byteBuff.put(bytes)
            } else {
                throw e
            }
        }
    }

    private fun ensureCapacityInternal(length: Int) {
        val limit = byteBuff.limit()
        val position = byteBuff.position()
        // 进行扩容
        val needCap = position + length
        if (needCap > limit) {
            val newLimit = tableSizeFor(needCap)
            val newByteBuff = ByteBuffer.allocate(newLimit)
            newByteBuff.put(toBytes())
            byteBuff = newByteBuff
        }
    }

    /**
     * 写字符串
     *
     * @param msg
     * @param iBytes
     */
    override fun writeString(msg: String, iBytes: Int) {
        if (Objects.nonNull(msg)) {
            val infoBytes = msg.toByteArray(StandardCharsets.UTF_8)
            val len = min(infoBytes.size, iBytes)
            if (!fastMode) {
                ensureCapacityInternal(len)
            }
            try {
                byteBuff.put(infoBytes, 0, len)
            } catch (e: BufferOverflowException) {
                if (fastMode) {
                    ensureCapacityInternal(infoBytes.size)
                    byteBuff.put(infoBytes)
                } else {
                    throw e
                }
            }
        }
    }

    /**
     * 获取十六进制输出
     *
     * @return
     */
    override fun getHexResponse(): String {
        val bytes = toBytes()
        return if (Objects.isNull(bytes) || bytes.size == 0) {
            ""
        } else HexUtil.byte2Hex(
            bytes
        )
    }

    /**
     * 获取byte数组
     *
     * @return
     */
    override fun toBytes(): ByteArray {
        byteBuff.flip()
        if (byteBuff.limit() == 0) {
            return EMPTY
        }
        val bytes = ByteArray(byteBuff.limit())
        byteBuff[bytes]
        return bytes
    }

    /**
     * 清空
     */
    override fun resetByte() {
        byteBuff.flip()
        byteBuff.clear()
    }

    override fun skip(step: Int) {
        byteBuff.position(byteBuff.position() + step)
    }

    override fun writeBytes(bytes: ByteArray) {
        this.write(bytes)
    }

    override fun writeDynamicString(str: String) {
        if (Objects.nonNull(str)) {
            val bytes = str.toByteArray(StandardCharsets.UTF_8)
            writeInt(bytes.size)
            this.write(bytes)
        } else {
            writeInt(0)
        }
    }

    override fun writeResponse(response: ICommonResponse) {
        try {
            response.genResHex(this)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override fun writeResponses(responses: Collection<Any>) {
        if (Objects.isNull(responses)) {
            writeInt(0)
        } else {
            writeInt(responses.size)
            val aClass: Class<*> = responses.toTypedArray()[0].javaClass
            if (CLassUtils.isInteger(aClass)) {
                responses.stream().map { n: Any -> n as Int }.forEach { iInt: Int -> writeInt(iInt) }
            } else if (ICommonResponse::class.java.isAssignableFrom(aClass)) {
                responses.stream().map { n: Any -> n as ICommonResponse }
                    .forEach { n: ICommonResponse -> n.genResHex(this) }
            } else if (CLassUtils.isStringClass(aClass)) {
                responses.stream().map { n: Any -> n as String }.forEach { str: String -> writeDynamicString(str) }
            } else if (CLassUtils.isBool(aClass)) {
                responses.stream().map { n: Any -> n as Boolean }.forEach { bool: Boolean -> writeBool(bool) }
            } else if (CLassUtils.isByte(aClass)) {
                responses.stream().map { n: Any -> n as Byte }.forEach { iByte: Byte -> writeByte(iByte) }
            } else if (CLassUtils.isCharacter(aClass)) {
                responses.stream().map { n: Any -> n as Char }.forEach { c: Char -> writeChar(c) }
            } else if (CLassUtils.isShort(aClass)) {
                responses.stream().map { n: Any -> n as Short }.forEach { iShort: Short -> writeShort(iShort) }
            } else if (CLassUtils.isLong(aClass)) {
                responses.stream().map { n: Any -> n as Long }.forEach { iLong: Long -> writeLong(iLong) }
            }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
        private val EMPTY = ByteArray(0)
    }
}
