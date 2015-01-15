package com.hinohunomi.aikinokogari.task;

import com.hinohunomi.aikinokogari.App;
import com.hinohunomi.aikinokogari.model.Field;
import com.hinohunomi.aikinokogari.model.FieldUtils;
import com.hinohunomi.aikinokogari.model.IFieldCallback;

/**
 * 新規ゲーム開始タスク.
 * Created by shimizu on 15/01/12.
 */
final public class NewGameTask extends NoWaitTask {
    @Override
    public void execute() {
        final App app = App.getInstance();
        // ゲームオーバーフラグのクリア
        app.isGameover = false;
        // フィールドのクリア
        app.fieldForEach(new CleanField());
        // スコア等のクリア
        app.score = 0;
        app.power = 0;
        // 4ライン追加
        app.task.push(new LineUpTask());
        app.task.push(new LineUpTask());
        app.task.push(new LineUpTask());
        app.task.push(new LineUpTask());
    }
}

/**
 * 全フィールドからキノコをクリアするタスク.
 */
final class CleanField implements IFieldCallback {
    public void applyField(final Field field) {
        FieldUtils.clearKinoko(field);
    }

}
