package com.codepath.apps.bluebirdone.dialogs;

import android.support.v4.app.DialogFragment;

import com.codepath.apps.bluebirdone.activities.BaseBlueBirdOneActivity;
import com.codepath.apps.bluebirdone.dagger.NetComponent;

/**
 * Created by jan_spidlen on 9/29/17.
 */

public abstract class BaseBlueBirdOneDialog extends DialogFragment {

    protected NetComponent getComponent() {
        return ((BaseBlueBirdOneActivity)getActivity()).getNetComponent();
    }
}
