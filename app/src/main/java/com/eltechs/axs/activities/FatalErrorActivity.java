package com.eltechs.axs.activities;

import android.content.*;
import android.os.*;
import android.text.*;
import android.text.method.*;
import android.widget.*;
import com.eltechs.axs.*;
import com.eltechs.axs.applicationState.*;
import com.kdt.eltechsaxs.*;

public class FatalErrorActivity extends FrameworkActivity<ApplicationStateBase<?>> {
    public static void showFatalError(String str) {
        Context context;
        ApplicationStateBase applicationStateBase = (ApplicationStateBase) Globals.getApplicationState();
        // int i = 0;
        boolean z = applicationStateBase.getCurrentActivity() != null;
        if (z) {
            context = applicationStateBase.getCurrentActivity();
        } else {
            context = applicationStateBase.getAndroidApplicationContext();
        }
        Intent intent = new Intent(context, FatalErrorActivity.class);
        if (!z) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        
        writeExtraParameter(intent, str);
        context.startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.fatal_error);
        TextView textView = (TextView) findViewById(R.id.fe_text_view);
        textView.setText((String) getExtraParameter());
		// Html.fromHtml((String) getExtraParameter()));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void finish() {
        super.finish();
        StartupActivity.shutdownAXSApplication();
    }
}
