package com.eltechs.axs.configuration.startup.actions;

import com.eltechs.axs.ExagearImageConfiguration.ExagearImage;
import com.eltechs.axs.applicationState.EnvironmentAware;
import com.eltechs.axs.applicationState.ExagearImageAware;
import com.eltechs.axs.applicationState.SelectedExecutableFileAware;
import com.eltechs.axs.configuration.XServerViewConfiguration;
import com.eltechs.axs.configuration.startup.EnvironmentCustomisationParameters;
import com.eltechs.axs.environmentService.AXSEnvironment;
import com.eltechs.axs.environmentService.components.ALSAServerComponent;
import com.eltechs.axs.environmentService.components.DirectSoundServerComponent;
import com.eltechs.axs.environmentService.components.XServerComponent;
import com.eltechs.axs.network.SocketPaths;
import com.eltechs.axs.productsRegistry.ProductIDs;
import com.eltechs.axs.xconnectors.epoll.UnixSocketConfiguration;

public class CreateTypicalEnvironmentConfiguration<StateClass extends EnvironmentAware & ExagearImageAware & SelectedExecutableFileAware<StateClass>> extends AbstractStartupAction<StateClass> {
    private final boolean forceUseAbstractSockets;
    private final int productId;
    private final XServerViewConfiguration xServerConf;

    public CreateTypicalEnvironmentConfiguration(int i, boolean z) {
        this(i, XServerViewConfiguration.DEFAULT, z);
    }

    public CreateTypicalEnvironmentConfiguration(int i, XServerViewConfiguration xServerViewConfiguration, boolean z) {
        this.productId = i;
        this.xServerConf = xServerViewConfiguration;
        this.forceUseAbstractSockets = z;
    }

    public void execute() {
        EnvironmentAware environmentAware = (EnvironmentAware) getApplicationState();
        EnvironmentCustomisationParameters environmentCustomisationParameters = ((SelectedExecutableFileAware) environmentAware).getSelectedExecutableFile().getEnvironmentCustomisationParameters();
        AXSEnvironment aXSEnvironment = new AXSEnvironment(getAppContext());
        int i = this.productId;
        // aXSEnvironment.addComponent(new SysVIPCEmulatorComponent(ProductIDs.getPackageName(this.productId)));
        aXSEnvironment.addComponent(new XServerComponent(environmentCustomisationParameters.getScreenInfo(), i, createXServerSocketConf()));
		
		android.util.Log.d("ExagearAXS", "FIXME implement socket of ALSA Server, DirectSound Server or PulseAudio Server!");
        aXSEnvironment.addComponent(new ALSAServerComponent(createALSASocketConf()));
        aXSEnvironment.addComponent(new DirectSoundServerComponent(createDSoundServerSocketConf()));
		
        // aXSEnvironment.addComponent(new GuestApplicationsTrackerComponent(createGATServerSocketConf()));
        environmentAware.setEnvironment(aXSEnvironment);
        environmentAware.setXServerViewConfiguration(this.xServerConf);
        sendDone();
    }

    private UnixSocketConfiguration createVirglServerSocketConf() {
        if (this.forceUseAbstractSockets) {
            return UnixSocketConfiguration.createAbstractSocket(String.format("%s%d", new Object[]{SocketPaths.VIRGL_SERVER, Integer.valueOf(this.productId)}));
        }
        return UnixSocketConfiguration.createRegularSocket(((ExagearImageAware) ((EnvironmentAware) getApplicationState())).getExagearImage().getPath().getAbsolutePath(), String.format("%s%d", new Object[]{SocketPaths.VIRGL_SERVER, Integer.valueOf(this.productId)}));
    }

    private UnixSocketConfiguration createALSASocketConf() {
        if (this.forceUseAbstractSockets) {
            return UnixSocketConfiguration.createAbstractSocket(String.format("%s%d", new Object[]{SocketPaths.ALSA_SERVER, Integer.valueOf(this.productId)}));
        }
        return UnixSocketConfiguration.createRegularSocket(((ExagearImageAware) ((EnvironmentAware) getApplicationState())).getExagearImage().getPath().getAbsolutePath(), String.format("%s%d", new Object[]{SocketPaths.ALSA_SERVER, Integer.valueOf(this.productId)}));
    }

    private UnixSocketConfiguration createXServerSocketConf() {
        if (this.forceUseAbstractSockets) {
            return UnixSocketConfiguration.createAbstractSocket(String.format("%s%d", new Object[]{SocketPaths.XSERVER, Integer.valueOf(this.productId)}));
        }
        return UnixSocketConfiguration.createRegularSocket(((ExagearImageAware) ((EnvironmentAware) getApplicationState())).getExagearImage().getPath().getAbsolutePath(), String.format("%s%d", new Object[]{SocketPaths.XSERVER, Integer.valueOf(this.productId)}));
    }

    private UnixSocketConfiguration createDSoundServerSocketConf() {
        if (this.forceUseAbstractSockets) {
            return UnixSocketConfiguration.createAbstractSocket(String.format("%s%d", new Object[]{SocketPaths.DSOUND_SERVER, Integer.valueOf(this.productId)}));
        }
        return UnixSocketConfiguration.createRegularSocket(((ExagearImageAware) ((EnvironmentAware) getApplicationState())).getExagearImage().getPath().getAbsolutePath(), String.format("%s%d", new Object[]{SocketPaths.DSOUND_SERVER, Integer.valueOf(this.productId)}));
    }

    private UnixSocketConfiguration createGATServerSocketConf() {
        StringBuilder sb = new StringBuilder();
        sb.append(SocketPaths.GUEST_APPLICATIONS_TRACKER);
        sb.append(this.productId);
        return UnixSocketConfiguration.createAbstractSocket(sb.toString());
    }
}
