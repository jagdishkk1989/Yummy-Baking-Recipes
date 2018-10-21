package com.jagdish.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

public class RecipeRemoteViewsService extends RemoteViewsService {

    public static Intent getIntent(Context context) {
        return new Intent(context, RecipeRemoteViewsService.class);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(getApplicationContext());
    }
}
