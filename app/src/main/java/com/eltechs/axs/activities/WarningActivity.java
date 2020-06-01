package com.eltechs.axs.activities;

import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import com.eltechs.axs.applicationState.*;
import com.kdt.eltechsaxs.*;

public class WarningActivity extends FrameworkActivity<ApplicationStateBase<?>> {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.warning);
        ((TextView) findViewById(R.id.warn_text_view)).setText(Html.fromHtml((String) getExtraParameter()));
    }

    public void onOKClicked(View view) {
        finish();
    }
}
