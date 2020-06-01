package com.eltechs.ed;

import android.text.TextUtils;
import com.eltechs.axs.xserver.ScreenInfo;
import com.eltechs.ed.controls.Controls;
import java.util.Arrays;
import java.util.List;

public class InstallRecipe {
    public static final List<InstallRecipe> LIST = Arrays.asList(new InstallRecipe[]{
		new InstallRecipe("Other app (not from the list)")
	});
	
    public Controls mControls = null;
    public String mDownloadURL = null;
    public String mInstallScriptName = "simple.sh";
    public String mLocaleName = null;
    public final String mName;
    public String mRunArguments = null;
    public String mRunGuide = null;
	public String mAdvancedName = "undefined";
    public String mRunScriptName = "run/simple.sh";
    public ScreenInfo mScreenInfo = null;
    public String mStartupActions = null;

    public InstallRecipe(String str) {
        this.mName = str;
    }

    public InstallRecipe setAdvancedName(String str) {
        this.mAdvancedName = str;
        return this;
    }
	
    public InstallRecipe setInstallScript(String str) {
        this.mInstallScriptName = str;
        return this;
    }

    public InstallRecipe setRunScript(String str) {
        this.mRunScriptName = str;
        return this;
    }

    public InstallRecipe setScreenInfo(ScreenInfo screenInfo) {
        this.mScreenInfo = screenInfo;
        return this;
    }

    public InstallRecipe setControls(Controls controls) {
        this.mControls = controls;
        return this;
    }

    public InstallRecipe setLocaleName(String str) {
        this.mLocaleName = str;
        return this;
    }

    public InstallRecipe setRunArguments(String str) {
        this.mRunArguments = str;
        return this;
    }

    public InstallRecipe setDownloadURL(String str) {
        this.mDownloadURL = str;
        return this;
    }

    public InstallRecipe setStartupActions(String... strArr) {
        this.mStartupActions = TextUtils.join(" ", strArr);
        return this;
    }

    public InstallRecipe setRunGuide(String str) {
        this.mRunGuide = str;
        return this;
    }

    public String getName() {
        return this.mName;
    }

    public String toString() {
        return getName();
    }

    public String getDownloadURL() {
        return this.mDownloadURL;
    }
}
