package com.hinohunomi.aikinokogari.task;

import com.badlogic.gdx.Gdx;
import com.hinohunomi.aikinokogari.App;
import com.hinohunomi.aikinokogari.model.Field;
import com.hinohunomi.aikinokogari.model.FieldUtils;
import com.hinohunomi.aikinokogari.model.IFieldCallback;
import com.hinohunomi.aikinokogari.model.ModelStatics;

/**
 * キノコ爆破処理
 * Created by shimizu on 15/01/13.
 */
final public class BombKinokoTask extends NoWaitTask {
    public static final String LOG_TAG = BombKinokoTask.class.getSimpleName();
    private int numX;
    private int numY;

    /**
     * @param _numX 爆破するキノコのX位置
     * @param _numY 爆破するキノコのY位置
     */
    public BombKinokoTask(final int _numX, final int _numY) {
        numX = _numX;
        numY = _numY;
    }

    @Override
    public void execute() {
        Gdx.app.debug(LOG_TAG, "execute");
//        Gdx.app.debug(LOG_TAG, String.format("X:%d Y:%d", numX, numY));
        if (numX >= 0 && numY >= 0) {
            final App app = App.getInstance();
            final ModelStatics.KinokoTypes kinokoType = app.fields[numX][numY].type;
            if (kinokoType != ModelStatics.KinokoTypes.NONE) {
                bombKinoko(numX, numY, kinokoType);
                // 爆破数カウント
                final int bombedCount = countBombedField();
                // 得点加算
                app.score += bombedCount * bombedCount;
                // 生え度加算
                app.power += App.powerTable[bombedCount];
                // 爆破を見せる（ウェイト）
                app.task.push(new WaitTask(0.3f));
                // 爆破済キノコ消去
                app.task.push(new FreeFallTask());
                // 生え度に応じてライン上昇
                while (app.power >= 6) {
                    app.task.push(new LineUpTask());
                    app.power -= 6;
                }
            }
        }
    }

    private int countBombedField() {
        final CountBombedField cbf = new CountBombedField(ModelStatics.KinokoTypes.BLACK);
        App.getInstance().fieldForEach(cbf);
        return cbf.getCount();
    }

    /**
     * キノコの爆破.
     * 指定色のキノコを爆破する（黒に変更する）。
     * 爆破はまわり８近傍の同色のキノコにも及ぶ。
     * 爆破位置のまわり８近傍を再帰処理する。
     * @param numX X位置
     * @param numY Y位置
     * @param kinokoType 爆破するキノコ種別
     */
    private void bombKinoko(final int numX, final int numY, final ModelStatics.KinokoTypes kinokoType) {
        if ( (numX < 0) || (App.MAX_COLS <= numX)
             || (numY < 0) || (App.MAX_ROWS <= numY)
                ) {
            // 範囲外
            return;
        }
        final Field field = App.getInstance().fields[numX][numY];
        if (field.type == kinokoType) {
            //黒カラー(爆破)
            FieldUtils.setupKinoko(field, ModelStatics.KinokoTypes.BLACK);
            // 8近傍に再帰
            bombKinoko(numX - 1, numY - 1, kinokoType);
            bombKinoko(numX - 1, numY    , kinokoType);
            bombKinoko(numX - 1, numY + 1, kinokoType);
            bombKinoko(numX,     numY - 1, kinokoType);
            bombKinoko(numX,     numY + 1, kinokoType);
            bombKinoko(numX + 1, numY - 1, kinokoType);
            bombKinoko(numX + 1, numY    , kinokoType);
            bombKinoko(numX + 1, numY + 1, kinokoType);
        }

    }
}

/**
 * 爆破したキノコのカウント.
 */
final class CountBombedField implements IFieldCallback {
//    public static final String LOG_TAG = CountBombedField.class.getSimpleName();

    /**
     * 爆破したキノコのカウント数.
     */
    private int count = 0;
    public int getCount() {
        return count;
    }

    /**
     * カウントするキノコの種類.
     */
    private ModelStatics.KinokoTypes countType;

    /**
     * コンストラクタ.
     * @param type カウントするキノコの種類.
     */
    public CountBombedField(final ModelStatics.KinokoTypes type) {
        countType = type;
    }

    public void applyField(final Field field) {
        if (field.type == countType) {
            count++;
        }
    }

}

/**
 * 爆破したキノコの消去と空いた隙間を詰める.
 */
final class FreeFallTask extends WaitTask {
    public static final String LOG_TAG = FreeFallTask.class.getSimpleName();

    public void execute() {
        Gdx.app.debug(LOG_TAG, "execute");
        final int countCol = App.MAX_COLS;
        final int countRow = App.MAX_ROWS;
        final Field[][] fields = App.getInstance().fields;
        for (int numX = 0; numX < countCol; ++numX) {
            int skip = 0;
            for (int numY = 0; numY < countRow; ++numY) {
                final Field target = fields[numX][numY];
                if (target.type == ModelStatics.KinokoTypes.BLACK) {
                    FieldUtils.clearKinoko(target);
                    skip++;
                } else if (skip > 0) {
                    final Field dest = fields[numX][numY - skip];
                    //FIXME なるべくnewしないように
                    FieldUtils.setupKinoko(dest, target.type);
                    FieldUtils.clearKinoko(target);
                }
            }
        }
    }

}
