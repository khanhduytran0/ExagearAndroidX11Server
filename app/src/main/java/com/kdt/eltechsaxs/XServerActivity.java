package com.kdt.eltechsaxs;

import android.content.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.View.*;
import com.eltechs.axs.ExagearImageConfiguration.*;
import com.eltechs.axs.activities.*;
import com.eltechs.axs.configuration.startup.*;
import com.eltechs.axs.configuration.startup.actions.*;
import com.eltechs.ed.*;
import com.eltechs.ed.startupActions.*;

public class XServerActivity extends StartupActivity<EDApplicationState>
{
	private static final String GENERIC_IMAGE_DIRECTORY_NAME = "image";
	public XServerActivity() {
		super(EDApplicationState.class);
	}
	
	public void initialiseStartupActions() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		com.eltechs.ed.startupActions.StartGuest.defaultScreenSize = new int[]{metrics.widthPixels, metrics.heightPixels};
		
        EDApplicationState eDApplicationState = (EDApplicationState) getApplicationState();
		eDApplicationState.setExagearImage(ExagearImage.find(getApplicationContext(), GENERIC_IMAGE_DIRECTORY_NAME, true));
		
        StartupActionsCollection startupActionsCollection = eDApplicationState.getStartupActionsCollection();
        startupActionsCollection.addAction(new RequestPermissions(this, StartupActivity.REQUEST_CODE_GET_PERMISSIONS));
		startupActionsCollection.addAction(new StartGuest());
        // signalUserInteractionFinished(UserRequestedAction.GO_FURTHER);
		// startupActionsCollection.addAction(new WDesktop());
    }
	
	public void finish() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle((CharSequence) "WARNING!!!");
		builder.setIcon((int) R.drawable.ic_warning_24dp);
		builder.setMessage((CharSequence) "Shutdown while startup in progress may corrupt application state!\n\nAre you sure you want to exit?");
		builder.setPositiveButton((CharSequence) "OK", new DialogInterface.OnClickListener() {
									  public void onClick(DialogInterface dialogInterface, int i) {
										  StartupActivity.shutdownAXSApplication(false);
										  dialogInterface.dismiss();
									  }
								  });
		builder.setNegativeButton((CharSequence) "Cancel", new DialogInterface.OnClickListener() {
									  public void onClick(DialogInterface dialogInterface, int i) {
										  dialogInterface.dismiss();
									  }
								  });
		builder.show();
	}
}

