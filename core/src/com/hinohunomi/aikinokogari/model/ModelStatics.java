package com.hinohunomi.aikinokogari.model;

/**
 * モデルの定数関連.
 * Created by shimizu on 15/01/12.
 */
final public class ModelStatics {
    public static final String LOG_TAG = ModelStatics.class.getSimpleName();

    /**
     * キノコの種類数.
     */
    final public static int KINOKO_KIND_COUNT = 5;

    /**
     * キノコの種類定義.
     */
    public enum KinokoTypes {
        NONE,   //nothing
        BLACK,  //爆破
        BLUE,
        WHITE,
        GREEN,
        RED,
        YELLOW,
    }

    /**
     * 数値をキノコ定義に変換する.
     * @param randomInt ランダムな数値を想定
     * @return 何色かのキノコ定義にを返す。黒にはならない。
     */
    static public KinokoTypes makeKinokoType(final int randomInt) {
        //o:爆破 12345:5色
        final int type = ((randomInt >>> 1) % KINOKO_KIND_COUNT) +1;
        final KinokoTypes result = getKinokoType(type);
        return  result;
    }

    /**
     * 数値に対応するキノコ定義を取得する.
     * @param type
     * @return
     */
    static private KinokoTypes getKinokoType(final int type) {
        KinokoTypes result = KinokoTypes.NONE;
        switch (type) {
            case 0: result = KinokoTypes.BLACK; break;
            case 1: result = KinokoTypes.BLUE; break;
            case 2: result = KinokoTypes.WHITE; break;
            case 3: result = KinokoTypes.GREEN; break;
            case 4: result = KinokoTypes.RED; break;
            case 5: result = KinokoTypes.YELLOW; break;
        }
        return result;
    }

    /**
     * 生え度テーブル.
     * 爆破個数に応じた上昇値の算出済テーブル。
     */
    final public static int[] HAEDO = {
            0,
            1, 2, 3,
            3, 4, 5,
            5, 6, 7,
            7, 8, 9,
            9, 10, 11,
            11, 12, 13,
            13, 14, 15,
            15, 16, 17,
            17, 18, 19,
            19, 20, 21,
            21, 22, 23,
            23, 24, 25,
            25, 26, 27,
            27, 28, 29,
            29, 30, 31,
            31, 32, 33,
            33, 34, 35,
            35, 36, 37,
            37, 38, 39,
            39, 40, 41,
            41, 42, 43,
            43, 44, 45,
            45, 46, 47,
            47, 48, 49,
    };
}
