package com.hinohunomi.aikinokogari;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hinohunomi.aikinokogari.model.Field;
import com.hinohunomi.aikinokogari.task.BombKinokoTask;
import com.hinohunomi.aikinokogari.task.NewGameTask;
import com.hinohunomi.aikinokogari.task.NoWaitTask;

/*
 * ルール
 * フィールドサイズW:H=8個:9ライン.
 * 初期数 4ライン.
 * キノコを選択.縦横斜め同色のキノコを巻き込んで消える.
 * キノコの色：(黒:爆破)青白緑赤黄
 * 得点は消した数の二乗.
 * 消した数に応じて"生え度"アップ.
 * 生え度6匹分で1ライン上昇.
 * 10ライン目にかかるとゲームオーバー.
 * 配列座標は左下が0
 */
final public class Main extends ApplicationAdapter {
    public static final String LOG_TAG = Main.class.getSimpleName();
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;
    private OrthographicCamera uiCamera;
    //タッチ位置
    private Vector2 touchPos;

    @Override
	public void create() {
//        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug(LOG_TAG, "create");
        App.init();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setScale(2);

        camera = new OrthographicCamera(App.SCREEN_WIDTH, App.SCREEN_HEIGHT);
        camera.setToOrtho(false, App.SCREEN_WIDTH, App.SCREEN_HEIGHT);
        viewport = new FitViewport(App.SCREEN_WIDTH, App.SCREEN_HEIGHT, camera);

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, App.SCREEN_WIDTH, App.SCREEN_HEIGHT);

        touchPos = new Vector2();

        //start
        final App app = App.getInstance();
        app.task.exec(new FixScreenTask());
        app.task.exec(new NewGameTask());
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.debug(LOG_TAG, "resize");
        viewport.update(width, height);
    }

	@Override
	public void render() {
//        Gdx.app.debug(LOG_TAG, "render");
        update();
        draw();
    }

    private void update() {
//        Gdx.app.debug(LOG_TAG, "update");
        final App app = App.getInstance();
        final float deltaTime = Gdx.graphics.getDeltaTime();
        if (!app.task.execNext(deltaTime)) {
            //実行タスクが空の場合に入力処理
            if (Gdx.input.justTouched()) {
                if (app.isGameover) {
                    // ゲームオーバー時はどこかをタッチでニューゲーム開始
                    app.task.exec(new NewGameTask());
                } else {
                    if (Gdx.input.isTouched(0)) {
                        touchPos.set(Gdx.input.getX(0), Gdx.input.getY(0));
                        viewport.unproject(touchPos);
                        //物理的なタッチ位置から論理的な位置を算出
                        final int numX = FixScreenTask.calcNumX(touchPos.x);
                        final int numY = FixScreenTask.calcNumY(touchPos.y);
                        app.task.exec(new BombKinokoTask(numX, numY));
                    }
                }
            }
        }
    }

    private void draw() {
//        Gdx.app.debug(LOG_TAG, "draw");
        final App app = App.getInstance();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        camera.update(); // ワールドからスクリーンまでのマトリックスを生成する。
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // draw fields
        Sprite sprite;
        final int countCol = App.MAX_COLS;
        final int countRow = App.MAX_ROWS;
        final Field[][] fields = app.fields;
        for (int numX = 0; numX < countCol; numX++) {
            for (int numY= 0; numY < countRow; numY++) {
                sprite = fields[numX][numY].getSprite();
                if (sprite != null) {
                    sprite.draw(batch);
                }
            }
        }
        batch.end();

        // draw borderline
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);
        //FIXME 描画位置が何故かずれる
        shapeRenderer.line(FixScreenTask.getLeft(), FixScreenTask.getTop(),
                FixScreenTask.getRight(), FixScreenTask.getTop());
        shapeRenderer.end();

        // draw UI
//        uiCamera.update(); // uiCameraを動かさないのであれば、必要ない。
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        // draw score
        font.draw(batch, "SCORE: " + String.valueOf(app.score), 20, 30);
        // draw 生え度
        font.draw(batch, "POWER: " + makePower(app.power), 20, 70);
        batch.end();
    }

    private String makePower(final int power) {
        Gdx.app.debug(LOG_TAG, "makePower");
        String result = "";
        for (int i = 0; i < power; i++) {
            result += "@";
        }
//        Gdx.app.debug(LOG_TAG, "makePower result:" + result);
        return result;
    }
}

/**
 * 表示位置調整タスク.
 */
final class FixScreenTask extends NoWaitTask {
    public static final String LOG_TAG = FixScreenTask.class.getSimpleName();

    final private static float offsetX = 60;
    final private static float offsetY = 100;
    static public float getLeft() {
        return offsetX;
    }
    static public float getBottom() {
        return offsetY;
    }
    static private float right = 0;
    static public float getRight() {
        return right;
    }
    static private float top = 0;
    static public float getTop() {
        return top;
    }
    static private int w = -1;
    static private int h = -1;
    static public int calcNumX(float x) {
        Gdx.app.debug(LOG_TAG, "calcNumX");
        int result = -1;
        if (x > offsetX
            && x < right) {
            result = (int)(x - offsetX) / w;
        }
        return result;
    }
    static public int calcNumY(float y) {
        Gdx.app.debug(LOG_TAG, "calcNumY");
        int result = -1;
        if (y > offsetY
                && y < top) {
            result = (int)(y - offsetY) / h;
        }
        return result;
    }

    @Override
    public void execute() {
        Gdx.app.debug(LOG_TAG, "execute");
        final App app = App.getInstance();
        //左下原点　右上＋座標
        final int countCol = App.MAX_COLS;
        final int countRow = App.MAX_ROWS;
        final Field[][] fields = app.fields;
        for (int numX = 0; numX < countCol; numX++) {
            final float posX = numX * App.IMAGE_WIDTH + offsetX;
            for (int numY= 0; numY < countRow; numY++) {
                final float posY = numY * App.IMAGE_HEIGHT + offsetY;
                fields[numX][numY].setupPosition(posX, posY);
            }
        }
        w = App.IMAGE_WIDTH;
        h = App.IMAGE_HEIGHT;
        right = offsetX + countCol * App.IMAGE_WIDTH;
        top = offsetY + countRow * App.IMAGE_HEIGHT;
    }
}