package com.hinohunomi.aikinokogari.controller;

import com.badlogic.gdx.utils.Array;
import com.hinohunomi.aikinokogari.task.IGameTask;
import com.hinohunomi.aikinokogari.task.WaitTask;

/**
 * 単純なタスクのキュー.
 * キューを１つずつ実行、タスク完了にウェイトを入れる。
 * Created by shimizu on 15/01/12.
 */
final public class TaskQueue {
    public static final String LOG_TAG = TaskQueue.class.getSimpleName();
    protected Array<IGameTask> queue = new Array<IGameTask>();

    public TaskQueue() {
    }

    /**
     * キューにタスクを追加.
     * @param task
     */
    public void push(final WaitTask task) {
        queue.add(task);
    }

    /**
     * タスクを実行.
     * キューには追加されない。
     * @param task
     */
    public void exec(final IGameTask task) {
        executeTask(task);
    }

    /**
     * タスクを実行。
     * @param task
     */
    private void executeTask(final IGameTask task) {
//        if (task != null) {
            task.execute();
//        }
    }

    /**
     * キュー先頭のタスクを実行する.
     * @param deltaTime 前回のタスク実行からの経過時間(sec)
     * @return タスクを実行した場合true、タスクが空の場合falseを返す
     */
    public boolean execNext(final float deltaTime) {
        boolean result = false;
        if (queue.size > 0) {
            result = true;
            final IGameTask task = queue.first();
            task.execute();
            if (!task.isWaiting(deltaTime)) {
                queue.removeIndex(0);
            }
        }
        return result;
    }

    /**
     *
     */
    public void clearTask() {
        queue.clear();
        //FIXME
        //execNextでタスク実行中に呼ばれた場合の対策にダミーのタスク挿入
        push(new WaitTask());
    }
}
