package com.example.fioni.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by fioni on 9/17/2017.
 */

public class BakingAppRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new BakingAppRemoteViewsFactory(this, intent);
    }
}
