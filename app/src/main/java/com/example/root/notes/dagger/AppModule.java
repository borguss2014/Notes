package com.example.root.notes.dagger;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * TODO: Add a class header comment!
 */

@Module
public class AppModule
{
    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext()
    {
        return application;
    }
}
