package com.eltechs.axs.xconnectors.nio.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.impl.ProtoHelpers;
import com.eltechs.axs.xconnectors.BufferFiller;
import com.eltechs.axs.xconnectors.XOutputStream;
import com.eltechs.axs.xconnectors.XStreamLock;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.locks.ReentrantLock;

public class NioXOutputStream implements XOutputStream {
    private ByteBuffer buffer;
    private int bufferSizeHardLimit = 2097152;
    private int bufferSizeLimit = 65536;
    private final ReentrantLock lock;
    private final SocketChannel socket;

    public class OutputStreamLock implements XStreamLock {
        public OutputStreamLock() {
            NioXOutputStream.this.lock.lock();
        }

        public void close() throws IOException {
            try {
                NioXOutputStream.this.flush();
            } finally {
                NioXOutputStream.this.lock.unlock();
            }
        }
    }

    public NioXOutputStream(SocketChannel socket, ByteBuffer outputBuffer) {
        this.socket = socket;
        this.buffer = outputBuffer;
        this.lock = new ReentrantLock();
    }

    public void setBufferSizeLimit(int bufferSizeLimit) {
        Assert.isTrue(bufferSizeLimit > 0, "Buffer capacity must be positive.");
        this.bufferSizeLimit = bufferSizeLimit;
    }

    public void setBufferSizeHardLimit(int bufferSizeHardLimit) {
        this.bufferSizeHardLimit = bufferSizeHardLimit;
    }

    public void setByteOrder(ByteOrder byteOrder) {
        Assert.state(this.buffer.position() == 0, "Byte order of XOutputStream may not be changed when it contains unsent data.");
        this.buffer.order(byteOrder);
    }

    public void writeByte(byte val) throws IOException {
        Assert.state(this.lock.isLocked(), "XOutputStream must be locked when used.");
        ensureSpaceIsAvailable(1);
        this.buffer.put(val);
    }

    public void writeShort(short val) throws IOException {
        Assert.state(this.lock.isLocked(), "XOutputStream must be locked when used.");
        ensureSpaceIsAvailable(2);
        this.buffer.putShort(val);
    }

    public void writeInt(int val) throws IOException {
        Assert.state(this.lock.isLocked(), "XOutputStream must be locked when used.");
        ensureSpaceIsAvailable(4);
        this.buffer.putInt(val);
    }

    public void writeString8(String string) throws IOException {
        Assert.state(this.lock.isLocked(), "XOutputStream must be locked when used.");
        byte[] data = string.getBytes(Charset.forName("latin1"));
        byte[] padding = new byte[ProtoHelpers.calculatePad(string.length())];
        ensureSpaceIsAvailable(data.length + padding.length);
        this.buffer.put(data);
        this.buffer.put(padding);
    }

    public void write(byte[] data) throws IOException {
        write(data, 0, data.length);
    }

    public void write(byte[] data, int offset, int len) throws IOException {
        Assert.state(this.lock.isLocked(), "XOutputStream must be locked when used.");
        ensureSpaceIsAvailable(len);
        this.buffer.put(data, offset, len);
    }

    public void write(int len, BufferFiller filler) throws IOException {
        Assert.state(this.lock.isLocked(), "XOutputStream must be locked when used.");
        ensureSpaceIsAvailable(len);
        this.buffer.limit(this.buffer.position() + len);
        ByteBuffer outputBuffer = this.buffer.slice();
        outputBuffer.order(this.buffer.order());
        this.buffer.position(this.buffer.position() + len);
        this.buffer.limit(this.buffer.capacity());
        filler.write(outputBuffer);
    }

    public void flush() throws IOException {
        drainBuffer();
    }

    public XStreamLock lock() {
        return new OutputStreamLock();
    }

    private void ensureSpaceIsAvailable(int requiredSpace) throws IOException {
        if ((this.buffer.capacity() - this.buffer.position()) + requiredSpace > this.bufferSizeLimit) {
            drainBuffer();
        }
        if (this.buffer.capacity() - this.buffer.position() < requiredSpace) {
            ByteBuffer newBuffer = ByteBuffer.allocate(this.buffer.capacity() + requiredSpace);
            newBuffer.order(this.buffer.order());
            this.buffer.rewind();
            newBuffer.put(this.buffer);
            this.buffer = newBuffer;
        }
    }

    private void drainBuffer() throws IOException {
        if (this.buffer.position() != 0) {
            this.buffer.flip();
            this.socket.write(this.buffer);
            this.buffer.compact();
        }
    }
}

