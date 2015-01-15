package com.hinohunomi.aikinokogari.task;

import com.badlogic.gdx.Gdx;
import com.hinohunomi.aikinokogari.App;
import com.hinohunomi.aikinokogari.model.Field;
import com.hinohunomi.aikinokogari.model.FieldUtils;
import com.hinohunomi.aikinokogari.model.IFieldCallback;
import com.hinohunomi.aikinokogari.model.ModelStatics;

/**
 * 1ライン追加タスク.
 * Created by shimizu on 15/01/12.
 */
final class LineUpTask extends WaitTask {
    public static final String LOG_TAG = LineUpTask.class.getSimpleName();

    public LineUpTask() {
        this.waitTime = 0.3f;
    }

    @Override
    public void execute() {
        Gdx.app.debug(LOG_TAG, "execute");
        if (!isDone) {
            if (!checkGameover()) {
                upLine();
                newLine();
            }
            isDone = true;
        }
    }

    /**
     * ゲームオーバーチェック.
     * 最上段にキノコが存在する場合にゲームオーバーフラグをセット、ゲームオーバー処理を行う。
     * @return ゲームオーバーの場合にtrueを返す
     */
    private boolean checkGameover() {
        boolean result = false;
        final int countCol = App.MAX_COLS;
        final int topRowIndex = App.MAX_ROWS -1;
        final Field[][] fields = App.getInstance().fields;
        for (int numX = 0; numX < countCol; ++numX) {
            final Field field = fields[numX][topRowIndex];
            if (field.type != ModelStatics.KinokoTypes.NONE) {
                result = true;
                App.getInstance().task.exec(new GameOverTask());
                break;
            }
        }
        return result;
    }

    private void upLine() {
        final int countCol = App.MAX_COLS;
        final int countRow = App.MAX_ROWS;
        final Field[][] fields = App.getInstance().fields;
        for (int numX = 0; numX < countCol; ++numX) {
            for (int numY = countRow -2; numY >= 0; --numY) {
                final Field src = fields[numX][numY];
                final Field dest = fields[numX][numY +1];
                dest.type = src.type;
                dest.setSprite(src.getSprite());
            }
        }
    }

    private void newLine() {
        final int countCol = App.MAX_COLS;
        final Field[][] fields = App.getInstance().fields;
        for (int numX = 0; numX < countCol; ++numX) {
            final Field field = fields[numX][0];
            FieldUtils.setupNextKinoko(field);
        }
    }
}

/**
 * ゲームオーバー処理タスク.
 * ゲームオーバーフラグのセットする。
 * すべての既存のタスクをクリアする。
 * すべてのキノコを黒色に変更する。
 */
final class GameOverTask extends NoWaitTask {
    public void execute() {
        final App app = App.getInstance();
        app.isGameover = true;
        app.task.clearTask();
        bombAllKinoko();
    }

    private void bombAllKinoko() {
        App.getInstance().fieldForEach(new BombAllKinoko());
    }

}

/**
 * 存在するすべてのキノコを黒色に変更する.
 */
final class BombAllKinoko implements IFieldCallback {
    public void applyField(final Field field) {
        if (field.type != ModelStatics.KinokoTypes.NONE) {
            FieldUtils.setupKinoko(field, ModelStatics.KinokoTypes.BLACK);
        }
    }
}
