package com.eltechs.axs.activities.menus;

import com.eltechs.axs.*;
import com.eltechs.axs.activities.*;
import com.eltechs.axs.applicationState.*;
import com.eltechs.axs.configuration.startup.*;
import com.eltechs.axs.helpers.*;
import com.eltechs.axs.widgets.actions.*;
import com.kdt.eltechsaxs.*;

public class ShowUsage extends AbstractAction {
    public ShowUsage() {
        super(AndroidHelpers.getString(R.string.show_tutorial));
    }

    public void run() {
        XServerDisplayActivity currentXServerDisplayActivity = getCurrentXServerDisplayActivity();
        SelectedExecutableFileAware selectedExecutableFileAware = (SelectedExecutableFileAware) Globals.getApplicationState();
        DetectedExecutableFile selectedExecutableFile = selectedExecutableFileAware.getSelectedExecutableFile();
        if (false) { // selectedExecutableFile.getControlsInfoDialog() == null) {
            // currentXServerDisplayActivity.startActivity(FrameworkActivity.createIntent(currentXServerDisplayActivity, UsageActivity.class, Integer.valueOf(selectedExecutableFileAware.getSelectedExecutableFile().getEffectiveCustomizationPackage().getInfoResId())));
        } else {
            selectedExecutableFile.getControlsInfoDialog().show(currentXServerDisplayActivity.getSupportFragmentManager(), "CONTROLS_INFO");
        }
    }
}
