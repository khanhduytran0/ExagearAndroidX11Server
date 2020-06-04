package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Width;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.ParametersCollectionContext;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataRetrievalContext;
import com.eltechs.axs.integersign.*;

public abstract class PrimitiveTypeParameterReader extends ParameterReaderBase {
    private final boolean isZXT;
    private final int naturalWidth;
    private final int width;

    protected PrimitiveTypeParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor, int naturalWidth, boolean z) {
        super(requestDataReader);
        this.naturalWidth = naturalWidth;
		this.width = naturalWidth;
		
        Width width2 = parameterDescriptor.getAnnotation(Width.class);
        if (width2 != null) {
			for (int i = 0; i < width2.indexes().length; i++) {
				int index = width2.indexes()[i];
				if (index == parameterDescriptor.getIndex()) {
					this.width = width2.values()[i];
					break;
				}
			}
        }
		
		boolean signed = parameterDescriptor.getType() instanceof IntegerSigned;
		boolean unsigned = parameterDescriptor.getType() instanceof IntegerUnsigned;
        boolean z3 = width2 != null && naturalWidth > width;
        Assert.isTrue(z || !z3 || (!signed && unsigned) || (signed && !unsigned), "Primitive type with extension must be specified with extension type and extension type must be specified only once.");
        this.isZXT = z || (z3 && unsigned);
        Assert.isTrue(this.naturalWidth == 1 || this.naturalWidth == 2 || this.naturalWidth == 4, "Primitive types can only be 1, 2 or 4 bytes wide.");
        Assert.isTrue(this.width == 1 || this.width == 2 || this.width == 4, "Primitive types can only be 1, 2 or 4 bytes wide.");
    }

    protected final int getUnderlyingValue(ParametersCollectionContext parametersCollectionContext) throws XProtocolError {
        int i;
        RequestDataRetrievalContext dataRetrievalContext = parametersCollectionContext.getDataRetrievalContext();
        if (this.width > this.naturalWidth) {
            if (this.naturalWidth == 1) {
                i = ArithHelpers.extendAsUnsigned(this.dataReader.readByte(dataRetrievalContext));
            } else if (this.naturalWidth == 2) {
                i = ArithHelpers.extendAsUnsigned(this.dataReader.readShort(dataRetrievalContext));
            } else {
                i = this.dataReader.readInt(dataRetrievalContext);
            }
            this.dataReader.skip(dataRetrievalContext, this.width - this.naturalWidth);
            return i;
        } else if (this.width == 1) {
            byte readByte = this.dataReader.readByte(dataRetrievalContext);
            if (this.isZXT) {
                return ArithHelpers.extendAsUnsigned(readByte);
            }
            return readByte;
        } else if (this.width != 2) {
            return this.dataReader.readInt(dataRetrievalContext);
        } else {
            short readShort = this.dataReader.readShort(dataRetrievalContext);
            if (this.isZXT) {
                return ArithHelpers.extendAsUnsigned(readShort);
            }
            return readShort;
        }
    }
}
