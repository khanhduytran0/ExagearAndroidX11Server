package com.eltechs.axs.proto.input.parameterReaders.impl;

import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Width;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.ParametersCollectionContext;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataRetrievalContext;
import android.util.*;
import java.nio.charset.*;
import java.util.*;
import com.eltechs.axs.proto.input.annotations.*;

public abstract class PrimitiveTypeParameterReader extends ParameterReaderBase {
    private final boolean isZXT;
    private final int naturalWidth;
    private final int width;

    protected PrimitiveTypeParameterReader(RequestDataReader requestDataReader, ParameterDescriptor parameterDescriptor, int naturalWidth, boolean ignoreErr) {
        super(requestDataReader);
        this.naturalWidth = naturalWidth;
		this.width = naturalWidth;
		
        Width width2 = parameterDescriptor.getOwnerMethod().getAnnotation(Width.class);
        if (width2 != null) {
			for (int i = 0; i < width2.indexes().length; i++) {
				int index = width2.indexes()[i];
				if (index == parameterDescriptor.getIndex()) {
					this.width = width2.values()[i];
					break;
				}
			}
        }
/*
		if (width == 4 && naturalWidth == 1) {
			Log.e("Exagear", "Method " + parameterDescriptor.getOwnerMethod().getDeclaringClass().getName() + ":" + parameterDescriptor.getOwnerMethod().getName() + "() has wrong param width at index " + parameterDescriptor.getIndex());
		}
*/
		boolean signed = false;
		boolean unsigned = false;
		IntSign intSign = parameterDescriptor.getOwnerMethod().getAnnotation(IntSign.class);
		if (intSign != null) {
			int[] signedIndexes = intSign.signedIndexes();
			int[] unsignedIndexes = intSign.unsignedIndexes();
			if (signedIndexes != null) {
				for (int i = 0; i < signedIndexes.length; i++) {
					if (parameterDescriptor.getIndex() == signedIndexes[i]) {
						signed = true;
						break;
					}
				}
			} if (unsignedIndexes != null) {
				for (int i = 0; i < unsignedIndexes.length; i++) {
					if (parameterDescriptor.getIndex() == unsignedIndexes[i]) {
						unsigned = true;
						break;
					}
				}
			}
		}
		
        boolean z3 = width2 != null && naturalWidth > width;
        Assert.isTrue(ignoreErr || !z3 || (!signed && unsigned) || (signed && !unsigned), "Primitive type with extension must be specified with extension type and extension type must be specified only once.");
        this.isZXT = ignoreErr || (z3 && unsigned);
        Assert.isTrue(this.naturalWidth == 1 || this.naturalWidth == 2 || this.naturalWidth == 4, "Primitive types can only be 1, 2 or 4 bytes wide.");
        Assert.isTrue(this.width == 1 || this.width == 2 || this.width == 4, "Primitive types can only be 1, 2 or 4 bytes wide.");
    }

    protected final int getUnderlyingValue(ParametersCollectionContext parametersCollectionContext) throws XProtocolError {
        int returningValue;
        RequestDataRetrievalContext dataRetrievalContext = parametersCollectionContext.getDataRetrievalContext();
		Log.d("Exagear", "getUnderlyingValue has width=" + width + ",naturalWidth=" + naturalWidth);
        if (this.width > this.naturalWidth) {
            if (this.naturalWidth == 1) {
                returningValue = ArithHelpers.extendAsUnsigned(this.dataReader.readByte(dataRetrievalContext));
				if (returningValue == 255) {
					returningValue = 0 & 255;
				}
            } else if (this.naturalWidth == 2) {
                returningValue = ArithHelpers.extendAsUnsigned(this.dataReader.readShort(dataRetrievalContext));
            } else {
                returningValue = this.dataReader.readInt(dataRetrievalContext);
            }
			
            this.dataReader.skip(dataRetrievalContext, this.width - this.naturalWidth);
        } else if (this.width == 1) {
            byte readByte = this.dataReader.readByte(dataRetrievalContext);
			returningValue = readByte;
            if (this.isZXT) {
                returningValue = ArithHelpers.extendAsUnsigned(readByte);
            }
        } else if (this.width != 2) {
            returningValue = this.dataReader.readInt(dataRetrievalContext);
        } else { // this.width == 2
            short readShort = this.dataReader.readShort(dataRetrievalContext);
			returningValue = readShort;
            if (this.isZXT) {
                returningValue = ArithHelpers.extendAsUnsigned(readShort);
            }
        }
		Log.d("Exagear", "getUnderlyingValue returing " + returningValue);
		return returningValue;
    }
}
