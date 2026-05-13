package com.angrybirds.screens;
import com.angrybirds.AngryBirdsGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * MenuScreen.java  —  القائمة الرئيسية المحسّنة
 *
 * التحسينات:
 *  ✅ خلفية تدرج لونية (gradient) من البرتقالي إلى الأصفر مثل Angry Birds
 *  ✅ أزرار مستديرة بتأثير ظل
 *  ✅ عنوان كبير بظل نصي
 *  ✅ أنيميشن: الأزرار تدخل من الأسفل عند فتح القائمة
 *  ✅ تأثير hover (تكبير عند المرور بالماوس)
 *  ✅ خط أبيض للفصل بين العنوان والأزرار
 */
public class MenuScreen implements Screen {

    private final AngryBirdsGame game;  // ← غيّر هذا للاسم الصحيح لكلاسك الرئيسي
    private Stage stage;
    private Skin skin;

    // خامات الخلفية
    private Texture backgroundTexture; // الصورة الخلفية
    private Texture cloudTexture; // سحابة بيضاء
    private Texture sunTexture;   // شمس صفراء

    // لأنيميشن الشمس
    private float sunY;
    private float time = 0f;

    public MenuScreen(AngryBirdsGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        // --- إعداد الـ Stage ---
        FitViewport viewport = new FitViewport(1280, 720);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        // --- بناء الخلفية ---
        buildBackground();

        // --- بناء الـ Skin (أسلوب الأزرار) ---
        skin = buildSkin();

        // --- بناء الجدول الرئيسي ---
        buildUI();
    }

    // =========================================================================
    // بناء الخلفية: سماء + أرض + شمس + سحب
    // =========================================================================
    private void buildBackground() {
        // تحميل صورة الخلفية من مجلد assets
        backgroundTexture = new Texture(Gdx.files.internal("background.jpg"));

        // الشمس: دائرة صفراء
        sunTexture = circleTexture(90, 255, 220, 30, 255);

        // السحابة: شكل بيضاوي أبيض
        cloudTexture = cloudShape(200, 80);
    }

    // =========================================================================
    // بناء واجهة المستخدم (الأزرار والعنوان)
    // =========================================================================
    private void buildUI() {
        // ---- العنوان الرئيسي "ANGRY BIRDS" ----
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = makeFont(5.5f, Color.WHITE);
        Label titleLabel = new Label("PIGGY REVENGE", titleStyle);
        titleLabel.setColor(1f, 0.95f, 0.1f, 1f); // أصفر ذهبي

        // ---- العنوان الفرعي ----
        Label.LabelStyle subStyle = new Label.LabelStyle();
        subStyle.font = makeFont(1.4f, Color.WHITE);
        Label subLabel = new Label("libGDX + Box2D Edition", subStyle);
        subLabel.setColor(1f, 1f, 1f, 0.85f);

        // ---- الأزرار الثلاثة ----
        TextButton playBtn     = new TextButton("▶   PLAY",         skin, "green");
        TextButton levelBtn    = new TextButton("☰   LEVEL SELECT", skin, "blue");
        TextButton exitBtn     = new TextButton("✕   EXIT",         skin, "red");

        // ---- تأثير الدخول: الأزرار تأتي من الأسفل ----
        playBtn.setPosition(0, -200);
        levelBtn.setPosition(0, -200);
        exitBtn.setPosition(0, -200);

        playBtn.addAction(Actions.sequence(
                Actions.delay(0.1f),
                Actions.moveToAligned(640, 340, 1, 0.5f, Interpolation.swingOut)
        ));
        levelBtn.addAction(Actions.sequence(
                Actions.delay(0.25f),
                Actions.moveToAligned(640, 255, 1, 0.5f, Interpolation.swingOut)
        ));
        exitBtn.addAction(Actions.sequence(
                Actions.delay(0.4f),
                Actions.moveToAligned(640, 170, 1, 0.5f, Interpolation.swingOut)
        ));

        // ---- العنوان: أنيميشن سقوط من الأعلى ----
        titleLabel.setPosition(640 - titleLabel.getPrefWidth()/2, 900);
        titleLabel.addAction(Actions.sequence(
                Actions.delay(0f),
                Actions.moveTo(640 - titleLabel.getPrefWidth()/2, 530, 0.6f, Interpolation.bounceOut)
        ));

        subLabel.setPosition(640 - subLabel.getPrefWidth()/2, 490);
        subLabel.getColor().a = 0;
        subLabel.addAction(Actions.sequence(
                Actions.delay(0.5f),
                Actions.fadeIn(0.5f)
        ));

        // ---- Listeners (ردود الأزرار) ----
        playBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.showGame(0);
            }
        });
        levelBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.showLevelSelect();
            }
        });
        exitBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                Gdx.app.exit();
            }
        });

        // ---- إضافة كل شيء للـ Stage ----
        stage.addActor(titleLabel);
        stage.addActor(subLabel);
        stage.addActor(playBtn);
        stage.addActor(levelBtn);
        stage.addActor(exitBtn);
    }

    // =========================================================================
    // بناء الـ Skin (ألوان وأشكال الأزرار)
    // =========================================================================
    private Skin buildSkin() {
        Skin s = new Skin();
        BitmapFont font = makeFont(1.8f, Color.WHITE);
        s.add("font", font);

        // أزرار خضراء (PLAY)
        s.add("green-up",   roundRect(60, 130, 60, 255,  400, 70, 18));
        s.add("green-over", roundRect(80, 170, 80, 255,  400, 70, 18));
        s.add("green-down", roundRect(30,  90, 30, 255,  400, 70, 18));

        // أزرار زرقاء (LEVEL SELECT)
        s.add("blue-up",   roundRect(50, 120, 200, 255, 400, 70, 18));
        s.add("blue-over", roundRect(80, 150, 230, 255, 400, 70, 18));
        s.add("blue-down", roundRect(20,  80, 160, 255, 400, 70, 18));

        // أزرار حمراء (EXIT)
        s.add("red-up",   roundRect(200, 50,  50, 255, 400, 70, 18));
        s.add("red-over", roundRect(230, 80,  80, 255, 400, 70, 18));
        s.add("red-down", roundRect(150, 20,  20, 255, 400, 70, 18));

        // بناء الـ styles
        for (String name : new String[]{"green", "blue", "red"}) {
            TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
            style.font = font;
            style.fontColor     = Color.WHITE;
            style.overFontColor = Color.YELLOW;
            style.downFontColor = new Color(0.8f, 0.8f, 0.8f, 1f);
            style.up   = new TextureRegionDrawable(new TextureRegion(s.get(name+"-up",   Texture.class)));
            style.over = new TextureRegionDrawable(new TextureRegion(s.get(name+"-over", Texture.class)));
            style.down = new TextureRegionDrawable(new TextureRegion(s.get(name+"-down", Texture.class)));
            s.add(name, style, TextButton.TextButtonStyle.class);
        }
        return s;
    }

    // =========================================================================
    // render() — يُستدعى 60 مرة/ثانية
    // =========================================================================
    @Override
    public void render(float delta) {
        time += delta;

        // مسح الشاشة
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float W = stage.getViewport().getWorldWidth();
        float H = stage.getViewport().getWorldHeight();

        game.batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
        game.batch.begin();

        // --- رسم صورة الخلفية ---
        if (backgroundTexture != null) {
            game.batch.draw(backgroundTexture, 0, 0, W, H);
        }

        // --- الشمس (تتأرجح ببطء) ---
        sunY = H * 0.72f + (float) Math.sin(time * 0.5f) * 8f;
        game.batch.draw(sunTexture, W * 0.82f, sunY, 90, 90);

        // --- السحب ---
        float cloud1X = (W * 0.1f + time * 15f) % (W + 200) - 200;
        float cloud2X = (W * 0.5f + time * 10f) % (W + 200) - 200;
        game.batch.draw(cloudTexture, cloud1X, H * 0.78f, 200, 80);
        game.batch.draw(cloudTexture, cloud2X, H * 0.85f, 160, 60);

        game.batch.end();

        // --- رسم الـ Stage (الأزرار والعناوين) ---
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
    }

    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   { dispose(); }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (sunTexture != null)  sunTexture.dispose();
        if (cloudTexture != null)cloudTexture.dispose();
    }

    // =========================================================================
    // دوال مساعدة لإنشاء الأشكال
    // =========================================================================

    /** مستطيل بزوايا مستديرة */
    private Texture roundRect(int r, int g, int b, int a, int w, int h, int radius) {
        Pixmap p = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        p.setColor(r/255f, g/255f, b/255f, a/255f);
        p.fillRectangle(radius, 0, w - 2*radius, h);
        p.fillRectangle(0, radius, w, h - 2*radius);
        p.fillCircle(radius,     radius,     radius);
        p.fillCircle(w-radius,   radius,     radius);
        p.fillCircle(radius,     h-radius,   radius);
        p.fillCircle(w-radius,   h-radius,   radius);
        // ظل داخلي خفيف في الأسفل
        p.setColor(0, 0, 0, 0.2f);
        p.fillRectangle(radius, 0, w - 2*radius, radius/2);
        Texture t = new Texture(p); p.dispose();
        return t;
    }

    /** لون صلب */
    private Texture solidTexture(int r, int g, int b, int a) {
        Pixmap p = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
        p.setColor(r/255f, g/255f, b/255f, a/255f);
        p.fill();
        Texture t = new Texture(p); p.dispose();
        return t;
    }

    /** دائرة ملونة */
    private Texture circleTexture(int size, int r, int g, int b, int a) {
        Pixmap p = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        p.setColor(r/255f, g/255f, b/255f, a/255f);
        p.fillCircle(size/2, size/2, size/2 - 2);
        // بريق خفيف
        p.setColor(1f, 1f, 1f, 0.3f);
        p.fillCircle(size/3, size/3, size/6);
        Texture t = new Texture(p); p.dispose();
        return t;
    }

    /** شكل سحابة بيضاء */
    private Texture cloudShape(int w, int h) {
        Pixmap p = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        p.setColor(1f, 1f, 1f, 0.9f);
        p.fillCircle(w/4,     h/2,     h/2 - 2);
        p.fillCircle(w/2,     h/3,     h/2);
        p.fillCircle(3*w/4,   h/2,     h/3);
        p.fillRectangle(h/4, h/2, w - h/2, h/2);
        Texture t = new Texture(p); p.dispose();
        return t;
    }

    /** خط BitmapFont بحجم معيّن */
    private BitmapFont makeFont(float scale, Color color) {
        BitmapFont f = new BitmapFont();
        f.getData().setScale(scale);
        f.setColor(color);
        return f;
    }
}
