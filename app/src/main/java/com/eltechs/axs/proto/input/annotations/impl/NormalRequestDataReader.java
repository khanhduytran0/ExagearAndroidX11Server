package com.eltechs.axs.proto.input.annotations.impl;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.errors.BadRequest;
import android.util.*;

public class NormalRequestDataReader implements RequestDataReader {
    public static final RequestDataReader INSTANCE = new NormalRequestDataReader();

    private NormalRequestDataReader() {
    }

    public byte readByte(RequestDataRetrievalContext requestDataRetrievalContext) throws XProtocolError {
        updateRemainingBytesCount(requestDataRetrievalContext, 1);
        return requestDataRetrievalContext.req.readByte();
    }

    public short readShort(RequestDataRetrievalContext requestDataRetrievalContext) throws XProtocolError {
        updateRemainingBytesCount(requestDataRetrievalContext, 2);
        return requestDataRetrievalContext.req.readShort();
    }

    public int readInt(RequestDataRetrievalContext requestDataRetrievalContext) throws XProtocolError {
        updateRemainingBytesCount(requestDataRetrievalContext, 4);
        return requestDataRetrievalContext.req.readInt();
    }

    public byte[] read(RequestDataRetrievalContext requestDataRetrievalContext, int i) throws XProtocolError {
        updateRemainingBytesCount(requestDataRetrievalContext, i);
        byte[] bArr = new byte[i];
        requestDataRetrievalContext.req.read(bArr);
        return bArr;
    }

    public void skip(RequestDataRetrievalContext requestDataRetrievalContext, int i) throws XProtocolError {
        updateRemainingBytesCount(requestDataRetrievalContext, i);
        requestDataRetrievalContext.req.skip(i);
    }

    private void updateRemainingBytesCount(RequestDataRetrievalContext requestDataRetrievalContext, int i) throws XProtocolError {
        if (requestDataRetrievalContext.remainingBytesCount < i) {
			Log.e("Exagear", requestDataRetrievalContext.remainingBytesCount + " remaining bytes is below than the update request " + i + " bytes");
            throw new BadRequest();
        }
        requestDataRetrievalContext.remainingBytesCount -= i;
    }
}
