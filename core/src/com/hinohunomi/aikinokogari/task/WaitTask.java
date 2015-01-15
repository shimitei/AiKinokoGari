package com.hinohunomi.aikinokogari.task;

import com.badlogic.gdx.Gdx;
import com.hinohunomi.aikinokogari.App;

/**
 * ウェイトありタスクはこのクラスをextendsして実装する。
 * 単にウェイトを入れるためにも使用することができる。
 * executeは複数回呼ばれる。
 * 一度のみ実行する場合はisDoneでチェックすること。
 * Created by shimizu on 15/01/12.
 */
public class WaitTask implements IGameTask {
    public static final String LOG_TAG = WaitTask.class.getSimpleName();

    /**
     * タスク実行済フラグ
     */
    protected boolean isDone = false;
    /**
     * 次のタスク実行待ち時間（sec）
     */
    protected float waitTime = 0;

    public WaitTask() {
    }
    public WaitTask(float _waitTime) {
        waitTime = _waitTime;
    }

    /**
     * タスクの実行.
     * タスクはexecuteを複数回呼ばれることがある。
     */
    @Override
    public void execute() {
        Gdx.app.debug(LOG_TAG, "execute");
        //must be override
        isDone = true;
    }

    @Override
    public boolean isWaiting(float deltaTime)
    {
        boolean result = false;
        waitTime -= deltaTime;
        if (waitTime > 0) {
            result = true;
        }
        return result;
    }
}
