package com.eltechs.ed.startupActions;

import android.util.Log;
import com.eltechs.axs.applicationState.ApplicationStateBase;
import com.eltechs.axs.applicationState.SelectedExecutableFileAware;
import com.eltechs.axs.applicationState.XServerDisplayActivityConfigurationAware;
import com.eltechs.axs.configuration.startup.AsyncStartupAction;
import com.eltechs.axs.configuration.startup.DetectedExecutableFile;
import com.eltechs.axs.configuration.startup.EnvironmentCustomisationParameters;
import com.eltechs.axs.configuration.startup.actions.AbstractStartupAction;
import com.eltechs.axs.configuration.startup.actions.CreateTypicalEnvironmentConfiguration;
import com.eltechs.axs.configuration.startup.actions.StartEnvironmentService;
import com.eltechs.axs.configuration.startup.actions.WaitForXClientConnection;
import com.eltechs.axs.environmentService.TrayConfiguration;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.helpers.FileHelpers;
import com.eltechs.axs.helpers.UiThread;
import com.eltechs.axs.xserver.ScreenInfo;
import com.eltechs.ed.ContainerPackage;
import com.eltechs.ed.InstallRecipe;
import com.kdt.eltechsaxs.R;
import com.eltechs.ed.controls.Controls;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartGuest<StateClass extends ApplicationStateBase<StateClass> & SelectedExecutableFileAware<StateClass> & XServerDisplayActivityConfigurationAware> extends AbstractStartupAction<StateClass> implements AsyncStartupAction<StateClass> {
	public static int[] defaultScreenSize;
	
    private static final String TAG = "StartGuest";
    private static final File mUserAreaDir = new File(AndroidHelpers.getMainSDCard(), "Download");
    // private GuestContainer mCont;
    private String mContStartupActions;
    /* access modifiers changed from: private */
    public Controls mControls;
    // private List<String> mEnv = new ArrayList();
    // private List<String> mExeArgv = new ArrayList();
    private File mExeWorkDir;
    private boolean mForceUseDefaultContols = false;
    private boolean mForceUseDefaultResolution = false;
    // private final GuestContainersManager mGcm = GuestContainersManager.getInstance(((ApplicationStateBase) getApplicationState()).getAndroidApplicationContext());
    private boolean mHideTaskbar = false;
    private boolean mHideXServerImage = false;
    private String mLocaleName;
    // private String mRunArguments;
    private String mRunGuide;
    // private File mRunScriptToCopy;
    private ScreenInfo mScreenInfo;
	
	public StartGuest() {
		this.mHideXServerImage = true;
		this.mForceUseDefaultContols = true;
		this.mForceUseDefaultResolution = false;
	}

    public void execute() {
        if (this.mForceUseDefaultContols) {
            this.mControls = Controls.getDefault();
        }
        if (this.mForceUseDefaultResolution) {
            this.mScreenInfo = setScreenInfoDefaultResolution(this.mScreenInfo);
        }
        final ArrayList arrayList = new ArrayList();
        // arrayList.add(new PrepareGuestImage(str, mUserAreaDir));
        final EnvironmentCustomisationParameters environmentCustomisationParameters = new EnvironmentCustomisationParameters();
        environmentCustomisationParameters.setScreenInfo(this.mScreenInfo);
        environmentCustomisationParameters.setLocaleName(this.mLocaleName);
        UiThread.post(new Runnable() {
            public void run() {
                ((SelectedExecutableFileAware) ((ApplicationStateBase) StartGuest.this.getApplicationState())).setSelectedExecutableFile(new DetectedExecutableFile(environmentCustomisationParameters, StartGuest.this.mControls.getId(), StartGuest.this.mControls.createInfoDialog()));
                ((XServerDisplayActivityConfigurationAware) ((ApplicationStateBase) StartGuest.this.getApplicationState())).setXServerDisplayActivityInterfaceOverlay(StartGuest.this.mControls.create());
            }
        });
        arrayList.add(new CreateTypicalEnvironmentConfiguration(12, false));
        arrayList.add(new WaitForXClientConnection(this.mHideXServerImage));
        UiThread.post(new Runnable() {
            public void run() {
                ((ApplicationStateBase) StartGuest.this.getApplicationState()).getStartupActionsCollection().addActions(arrayList);
                StartGuest.this.sendDone();
            }
        });
    }

    private static ScreenInfo setScreenInfoDefaultResolution(ScreenInfo screenInfo) {
        int i = defaultScreenSize[0];
        int i2 = defaultScreenSize[1];
        ScreenInfo screenInfo2 = new ScreenInfo(i, i2, i / 10, i2 / 10, screenInfo.depth);
        return screenInfo2;
    }
}
