package com.hinohunomi.aikinokogari;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.hinohunomi.aikinokogari.controller.TaskQueue;
import com.hinohunomi.aikinokogari.model.Field;
import com.hinohunomi.aikinokogari.model.IFieldCallback;
import com.hinohunomi.aikinokogari.model.ModelStatics;

import java.util.Random;

/**
 * アプリのリソース管理.
 * Created by shimizu on 15/01/12.
 */
final public class App {
    public static final String LOG_TAG = App.class.getSimpleName();

    // 画面サイズ
    final static public int SCREEN_WIDTH = 640;
    final static public int SCREEN_HEIGHT = 800;
    // 画像サイズ
    final static public int IMAGE_HEIGHT = 40;
    final static public int IMAGE_WIDTH = 40;
    // 画像数
    final static public int IMAGE_COUNT = 6;
    // マス目
    final static public int MAX_COLS = 8;
    // ライン数
    final static public int MAX_ROWS = 9;
    final static public int FIELD_HEIGHT = (MAX_ROWS * IMAGE_HEIGHT);

    // タスク管理
    public TaskQueue task;
    // 画像リソース
    public Texture img_kinoko[];
    // フィールド [col][row] 縦を良く使うため
    public Field fields[][];
    //乱数
    static private Random random = new Random();

    // ゲームオーバーフラグ
    public boolean isGameover = false;
    // 生え度
    public int power = 0;
    // 得点
    public int score = 0;
    // 生え度計算表
    static public int[] powerTable = {
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

    static private App app;
    static public App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }
    static public void init() {
        App.getInstance();
    }
    private App() {
        task = new TaskQueue();
        loadImageResouce();
        fields = createFields();
    }

    private void loadImageResouce() {
        img_kinoko = new Texture[IMAGE_COUNT];
        img_kinoko[0] = new Texture("0black.png");
        img_kinoko[1] = new Texture("1blue.png");
        img_kinoko[2] = new Texture("2white.png");
        img_kinoko[3] = new Texture("3green.png");
        img_kinoko[4] = new Texture("4red.png");
        img_kinoko[5] = new Texture("5yellow.png");
    }

    public Texture getKinokoTexture(ModelStatics.KinokoTypes kinokoType) {
        Texture result = null;
        switch (kinokoType) {
            case BLACK: result = img_kinoko[0]; break;
            case BLUE: result = img_kinoko[1]; break;
            case WHITE: result = img_kinoko[2]; break;
            case GREEN: result = img_kinoko[3]; break;
            case RED: result = img_kinoko[4]; break;
            case YELLOW: result = img_kinoko[5]; break;
        }
        return result;
    }

    public Sprite createKinokoSprite(ModelStatics.KinokoTypes kinokoType) {
        Sprite result = null;
        Texture texture = getKinokoTexture(kinokoType);
        if (texture != null) {
            result = new Sprite(texture);
            result.setScale(2);
        }
        return result;
    }

    private Field[][] createFields() {
        Field[][] result = new Field[MAX_COLS][MAX_ROWS];
        for (int numX = 0; numX < MAX_COLS; numX++) {
            for (int numY = 0; numY < MAX_ROWS; numY++) {
                result[numX][numY] = new Field();
            }
        }
        return result;
    }

    public void fieldForEach(IFieldCallback callback) {
        for (int numX = 0; numX < MAX_COLS; numX++) {
            for (int numY = 0; numY < MAX_ROWS; numY++) {
                Field field = fields[numX][numY];
                callback.applyField(field);
            }
        }
    }

    static public int getRandomNextInt() {
        final int result = random.nextInt();
        return result;
    }
}
