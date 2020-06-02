package com.eltechs.axs.xconnectors.nio.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xconnectors.XInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NioXInputStream implements XInputStream {
    private ByteBuffer activeRegion;
    private int bufferSizeHardLimit = 2097152;
    private ByteBuffer inputBuffer;

    public NioXInputStream(ByteBuffer inputBuffer) {
        this.inputBuffer = inputBuffer;
    }

    public void setBufferSizeHardLimit(int bufferSizeHardLimit) {
        this.bufferSizeHardLimit = bufferSizeHardLimit;
    }

    public ByteBuffer getInputBuffer() {
        return this.inputBuffer;
    }

    public ByteBuffer getActiveRegion() {
        return this.activeRegion;
    }

    public void growInputBufferIfNecessary() throws IOException {
        Assert.state(this.activeRegion == null, "Can't resize an input buffer while processing messages contained in it.");
        if (this.inputBuffer.position() != this.inputBuffer.capacity()) {
            return;
        }
        if (this.inputBuffer.capacity() > this.bufferSizeHardLimit) {
            throw new IOException("Input buffer size has exceeded the hard limit.");
        }
        ByteBuffer newBuffer = ByteBuffer.allocateDirect(this.inputBuffer.capacity() * 2);
        newBuffer.order(this.inputBuffer.order());
        this.inputBuffer.rewind();
        newBuffer.put(this.inputBuffer);
        this.inputBuffer = newBuffer;
    }

    public void setByteOrder(ByteOrder byteOrder) {
        this.inputBuffer.order(byteOrder);
        if (this.activeRegion != null) {
            this.activeRegion.order(byteOrder);
        }
    }

    public void prepareForReading() {
        int p = this.inputBuffer.position();
        this.inputBuffer.position(0);
        this.inputBuffer.limit(p);
        this.activeRegion = this.inputBuffer.slice();
        this.activeRegion.order(this.inputBuffer.order());
        this.inputBuffer.limit(this.inputBuffer.capacity());
        this.inputBuffer.position(p);
    }

    public void doneWithReading(int nBytesProcessed) {
        Assert.isTrue(nBytesProcessed <= this.inputBuffer.position(), "NioProcessorThread claims to have processed more data than is available.");
        this.activeRegion = null;
        if (nBytesProcessed == this.inputBuffer.position()) {
            this.inputBuffer.clear();
        } else if (nBytesProcessed > 0) {
            int p = this.inputBuffer.position();
            this.inputBuffer.position(nBytesProcessed);
            this.inputBuffer.limit(p);
            this.inputBuffer.compact();
        }
    }

    public int getAvailableBytesCount() {
        return this.activeRegion.remaining();
    }

    public byte getByte() {
        return this.activeRegion.get();
    }

    public short getShort() {
        return this.activeRegion.getShort();
    }

    public int getInt() {
        return this.activeRegion.getInt();
    }

    public void get(byte[] data) {
        this.activeRegion.get(data);
    }

    public ByteBuffer getAsByteBuffer(int nBytes) {
        ByteBuffer result = this.activeRegion.slice();
        result.limit(nBytes);
        result.order(this.activeRegion.order());
        this.activeRegion.position(this.activeRegion.position() + nBytes);
        return result;
    }

    public void skip(int nBytes) {
        this.activeRegion.position(this.activeRegion.position() + nBytes);
    }
}

