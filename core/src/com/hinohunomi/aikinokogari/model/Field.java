package com.hinohunomi.aikinokogari.model;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * フィールドのマス.
 * Created by shimizu on 15/01/12.
 */
final public class Field {
    public static final String LOG_TAG = Field.class.getSimpleName();
    /**
     * 描画位置X.
     */
    private float x;
    /**
     * 描画位置Y.
     */
    private float y;

    /**
     * 描画位置の設定.
     * @param _x
     * @param _y
     */
    public void setupPosition(final float _x, final float _y) {
        x = _x;
        y = _y;
    }

    /**
     * きのこ種別.
     */
    public ModelStatics.KinokoTypes type = ModelStatics.KinokoTypes.NONE;

    /**
     * 描画するスプライト.
     */
    private Sprite sprite;
    public void setSprite(final Sprite _sprite) {
        sprite = _sprite;
        if (sprite != null) {
            sprite.setPosition(x, y);
        }
    }
    public Sprite getSprite() {
        return sprite;
    }
}
