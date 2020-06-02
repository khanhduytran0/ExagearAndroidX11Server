package com.eltechs.axs.activities.menus;

import com.eltechs.axs.activities.*;
import com.eltechs.axs.helpers.*;
import com.eltechs.axs.widgets.actions.*;
import com.kdt.eltechsaxs.*;

public class Quit extends AbstractAction {
    public Quit() {
        super(AndroidHelpers.getString(R.string.stop_Xserver));
    }

    public void run() {
		System.exit(0);
        // StartupActivity.shutdownAXSApplication(true);
    }
}
