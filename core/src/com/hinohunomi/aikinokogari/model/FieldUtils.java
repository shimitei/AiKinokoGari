package com.hinohunomi.aikinokogari.model;

import com.hinohunomi.aikinokogari.App;

/**
 * Field操作.
 * Created by shimizu on 15/01/12.
 */
final public class FieldUtils {
    public static final String LOG_TAG = FieldUtils.class.getSimpleName();

    /**
     * キノコの消去.
     * @param field
     */
    static public void clearKinoko(final Field field) {
        field.type = ModelStatics.KinokoTypes.NONE;
        field.setSprite(null);
    }

    /**
     * ランダムなキノコを設置.
     * @param field
     */
    static public void setupNextKinoko(final Field field) {
        final int randomType = App.getRandomNextInt();
        final ModelStatics.KinokoTypes kinokoType = ModelStatics.makeKinokoType(randomType);
        setupKinoko(field, kinokoType);
    }

    /**
     * キノコを設定.
     * @param field
     * @param kinokoType
     */
    static public void setupKinoko(final Field field, final ModelStatics.KinokoTypes kinokoType) {
        field.type = kinokoType;
        field.setSprite(App.getInstance().createKinokoSprite(kinokoType));
    }

}
