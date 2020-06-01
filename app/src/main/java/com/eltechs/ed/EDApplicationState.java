package com.eltechs.ed;

import com.eltechs.axs.applicationState.ApplicationStateBase;
import com.eltechs.axs.applicationState.ExagearImageAware;
// import com.eltechs.axs.applicationState.PurchasableComponentsCollectionAware;
import com.eltechs.axs.applicationState.SelectedExecutableFileAware;
import com.eltechs.axs.applicationState.XServerDisplayActivityConfigurationAware;

public interface EDApplicationState extends ApplicationStateBase<EDApplicationState>, XServerDisplayActivityConfigurationAware, /* PurchasableComponentsCollectionAware, */ ExagearImageAware, SelectedExecutableFileAware<EDApplicationState>
{
	public static final String TAG = "ExagearDebug";
}
