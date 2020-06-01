package com.kdt.eltechsaxs;

import android.support.v7.app.*;
import android.os.*;
import android.util.*;

public class XServerActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		com.eltechs.ed.startupActions.StartGuest.defaultScreenSize = new int[]{metrics.widthPixels, metrics.heightPixels};
	}
}
