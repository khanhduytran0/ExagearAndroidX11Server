package com.eltechs.axs.activities.menus;

import com.eltechs.axs.*;
import com.eltechs.axs.activities.*;
import com.eltechs.axs.helpers.*;
import com.eltechs.axs.widgets.actions.*;
import com.kdt.eltechsaxs.*;

public class ToggleHorizontalStretch extends AbstractAction {
    public ToggleHorizontalStretch() {
        super(null);
    }

    public String getName() {
        if (getCurrentXServerDisplayActivity().isHorizontalStretchEnabled()) {
            return AndroidHelpers.getString(R.string.show_normal);
        }
        return AndroidHelpers.getString(R.string.show_stretched);
    }

    public void run() {
        XServerDisplayActivity currentXServerDisplayActivity = getCurrentXServerDisplayActivity();
        CommonApplicationConfigurationAccessor commonApplicationConfigurationAccessor = new CommonApplicationConfigurationAccessor();
        boolean z = !currentXServerDisplayActivity.isHorizontalStretchEnabled();
        currentXServerDisplayActivity.setHorizontalStretchEnabled(z);
        commonApplicationConfigurationAccessor.setHorizontalStretchEnabled(z);
    }
}
