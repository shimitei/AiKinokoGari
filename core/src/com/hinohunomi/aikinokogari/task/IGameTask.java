package com.hinohunomi.aikinokogari.task;

/**
 * Taskインターフェース
 * タスクの実行と、次のタスク実行までの待ち時間。
 * Created by shimizu on 15/01/12.
 */
public interface IGameTask {
    void execute();
    boolean isWaiting(float deltaTime);
}
