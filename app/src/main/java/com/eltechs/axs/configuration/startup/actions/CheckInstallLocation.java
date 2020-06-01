package com.eltechs.axs.configuration.startup.actions;

// import com.eltechs.ed.R;
import com.eltechs.axs.applicationState.ApplicationStateBase;

public class CheckInstallLocation extends AbstractStartupAction<ApplicationStateBase<?>> {
    public void execute() {
		/*
        NativeLibsConfiguration nativeLibsConfiguration = new NativeLibsConfiguration(getAppContext());
        if (isExecutablePermissionSet(nativeLibsConfiguration.getLibubtPath()) && isExecutablePermissionSet(nativeLibsConfiguration.getLibubt2GPath()) && isExecutablePermissionSet(nativeLibsConfiguration.getKillswitchPath()) && isExecutablePermissionSet(nativeLibsConfiguration.getSysVIPCEmulatorPath()) && isExecutablePermissionSet(nativeLibsConfiguration.getIsMemSplit3g1gPath())) {
            sendDone();
        } else {
            sendError(getAppContext().getString(R.string.cil_installed_to_sdcard));
        }
		*/
		
		sendDone();
    }
}
