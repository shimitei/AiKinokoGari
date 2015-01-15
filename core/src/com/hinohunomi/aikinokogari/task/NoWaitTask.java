package com.hinohunomi.aikinokogari.task;

import com.badlogic.gdx.Gdx;
import com.hinohunomi.aikinokogari.App;

/**
 * 描画ウェイトなしタスク.
 * Created by shimizu on 15/01/12.
 */
public class NoWaitTask implements IGameTask {
    public static final String LOG_TAG = NoWaitTask.class.getSimpleName();

    @Override
    public void execute() {
        Gdx.app.debug(LOG_TAG, "execute");
        //must be override
    }

    @Override
    public boolean isWaiting(float deltaTime) {
        return false;
    }
}
