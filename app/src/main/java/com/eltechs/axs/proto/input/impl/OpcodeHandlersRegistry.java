package com.eltechs.axs.proto.input.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.OpcodeHandler;
import android.util.*;

public class OpcodeHandlersRegistry {
    private OpcodeHandler[] opcodeHandlers = new OpcodeHandler[0];

    public void installRequestHandler(int i, OpcodeHandler opcodeHandler) {
        if (this.opcodeHandlers.length <= i) {
            OpcodeHandler[] opcodeHandlerArr = new OpcodeHandler[(i + 1)];
            if (opcodeHandlers.length > 0) System.arraycopy((Object) this.opcodeHandlers, 0, (Object) opcodeHandlerArr, 0, this.opcodeHandlers.length);
            this.opcodeHandlers = opcodeHandlerArr;
        }
        Assert.state(this.opcodeHandlers[i] == null, String.format("A handler for the opcode %d is already registered.", new Object[]{Integer.valueOf(i)}));
        this.opcodeHandlers[i] = opcodeHandler;
    }

    public OpcodeHandler getHandler(int i) {
		// Log.d("Exagear", "Debug: i(" + i + ") < handlerLength(" + opcodeHandlers.length + ")?");
        if (i < this.opcodeHandlers.length) {
			// Log.d("Exagear", "Selected index " + i + " isNull=" + (opcodeHandlers[i] == null));
            return this.opcodeHandlers[i];
        }
        return null; // this.opcodeHandlers[0];
    }
}
