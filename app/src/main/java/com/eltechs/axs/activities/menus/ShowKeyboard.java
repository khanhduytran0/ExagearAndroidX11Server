package com.eltechs.axs.activities.menus;

import com.eltechs.axs.helpers.*;
import com.eltechs.axs.widgets.actions.*;
import com.kdt.eltechsaxs.*;

public class ShowKeyboard extends AbstractAction {
    public ShowKeyboard() {
        super(AndroidHelpers.getString(R.string.show_keyboard));
    }

    public void run() {
        AndroidHelpers.toggleSoftInput();
    }
}
