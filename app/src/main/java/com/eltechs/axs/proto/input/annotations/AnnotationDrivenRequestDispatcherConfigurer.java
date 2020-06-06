package com.eltechs.axs.proto.input.annotations;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.ConfigurableRequestsDispatcher;
import com.eltechs.axs.proto.input.annotations.impl.AnnotationDrivenOpcodeHandler;
import com.eltechs.axs.proto.input.annotations.impl.AnnotationDrivenRequestParser;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.parameterReaders.ParameterReader;
import com.eltechs.axs.xserver.LocksManager.Subsystem;
import java.lang.reflect.Method;
import com.eltechs.axs.xserver.client.*;
import com.eltechs.axs.xconnectors.*;

public class AnnotationDrivenRequestDispatcherConfigurer {
    private final RequestContextParamReadersFactory reqCtxParamReadersFactory;
    private final RequestParamReadersFactory reqParamReadersFactory;
    private final ConfigurableRequestsDispatcher target;

    public AnnotationDrivenRequestDispatcherConfigurer(ConfigurableRequestsDispatcher target, RequestContextParamReadersFactory reqCtxParamReadersFactory, RequestParamReadersFactory reqParamReadersFactory) {
        this.target = target;
        this.reqCtxParamReadersFactory = reqCtxParamReadersFactory;
        this.reqParamReadersFactory = reqParamReadersFactory;
    }

    public void configureDispatcher(Object... handlers) {
        for (Object h : handlers) {
            for (Method method : h.getClass().getMethods()) {
                RequestHandler rh = (RequestHandler) method.getAnnotation(RequestHandler.class);
                if (rh != null) {
					Object realObj = h;
                    processOneHandler(rh.opcode(), realObj, method);
                }
            }
        }
    }

    private void processOneHandler(int opcode, Object handlerObject, Method handlerMethod) {
        this.target.installRequestHandler(opcode, new AnnotationDrivenOpcodeHandler(handlerObject, handlerMethod, getNeededLocks(handlerMethod), buildRequestParser(handlerMethod)));
    }

    private AnnotationDrivenRequestParser buildRequestParser(Method handler) {
        ParameterDescriptor[] parameterDescriptors = ParameterDescriptor.getMethodParameters(handler);
        int parametersCount = parameterDescriptors.length;
        ParameterReader[] parameterReaders = new ParameterReader[parametersCount];
        for (int i = 0; i < parametersCount; i++) {
            parameterReaders[i] = configureParameterReader(handler, parameterDescriptors, i);
        }
        return new AnnotationDrivenRequestParser(parameterReaders);
    }

    private ParameterReader configureParameterReader(final Method handlerMethod, final ParameterDescriptor[] parameterDescriptors, int idx) {
        ParameterDescriptor pd = parameterDescriptors[idx];
        ConfigurationContext confCtx = new ConfigurationContext() {
            public String getHandlerMethodName() {
                return String.format("%s::%s()", new Object[]{handlerMethod.getDeclaringClass().getSimpleName(), handlerMethod.getName()});
            }

            public int getParametersCount() {
                return parameterDescriptors.length;
            }

            public ParameterDescriptor getParameter(int idx) {
                return parameterDescriptors[idx];
            }

            public ParameterDescriptor findNamedParameter(String name) {
                return AnnotationDrivenRequestDispatcherConfigurer.this.findNamedParameter(parameterDescriptors, name);
            }
        };
		
		/*
        ParameterReader reader = ((RequestParam) pd.getAnnotation(RequestParam.class)) == null ?
			this.reqCtxParamReadersFactory.createReader(pd, confCtx) :
			this.reqParamReadersFactory.createReader(pd, confCtx);
        z = reader != null;
        Assert.state(z, String.format("Resolved no parameter reader for the context parameter %d of the request handler method %s.", new Object[]{Integer.valueOf(pd.getIndex()), confCtx.getHandlerMethodName()}));
		*/
		
		ParameterReader reader = this.reqCtxParamReadersFactory.createReader(pd, confCtx);
		if (reader == null) {
			reader = this.reqParamReadersFactory.createReader(pd, confCtx);
		}
		
		Assert.state(reader != null, String.format("Resolved no parameter reader for the context parameter %d of the request handler method %s.", new Object[]{Integer.valueOf(pd.getIndex()), confCtx.getHandlerMethodName()}));
		
        return reader;
    }

    private ParameterDescriptor findNamedParameter(ParameterDescriptor[] parameterDescriptors, String name) {
        for (ParameterDescriptor pd : parameterDescriptors) {
            ParamName pn = (ParamName) pd.getOwnerMethod().getAnnotation(ParamName.class);
            if (pn != null && pn.index() == pd.getIndex() && name.equals(pn.value())) {
                return pd;
            }
        }
        return null;
    }

    private Subsystem[] getNeededLocks(Method handlerMethod) {
        if (handlerMethod.getAnnotation(GiantLocked.class) != null) {
            return Subsystem.values();
        }
        Locks description = (Locks) handlerMethod.getAnnotation(Locks.class);
        if (description == null) {
            return new Subsystem[0];
        }
        String[] lockNames = description.value();
        int nLocks = lockNames.length;
        Subsystem[] locks = new Subsystem[nLocks];
        for (int i = 0; i < nLocks; i++) {
            locks[i] = Subsystem.valueOf(lockNames[i]);
        }
        return locks;
    }
}

