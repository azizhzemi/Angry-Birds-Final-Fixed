package com.angrybirds.game;

import com.angrybirds.entities.Slingshot;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameInputProcessor extends InputAdapter {

    private final Viewport viewport;      // final لأنها لا تتغير بعد الإنشاء
    private final Slingshot slingshot;    // final لأنها لا تتغير بعد الإنشاء
    private boolean isDragging = false;

    // Vector3 مُعاد استخدامه لتجنب إنشاء كائنات جديدة كل frame
    // هذا مهم جداً في الألعاب لتجنب بطء الـ Garbage Collector
    private final Vector3 tempVec3 = new Vector3();

    public GameInputProcessor(Viewport viewport, Slingshot slingshot) {
        this.viewport  = viewport;
        this.slingshot = slingshot;
    }

    /**
     * تحويل إحداثيات الشاشة (pixels) إلى إحداثيات العالم (meters/units)
     * مهم لأن LibGDX يعكس محور Y (الشاشة من الأعلى، العالم من الأسفل)
     */
    private Vector2 unproject(int screenX, int screenY) {
        tempVec3.set(screenX, screenY, 0);       // أعد استخدام نفس الكائن
        viewport.unproject(tempVec3);             // تحويل في مكانه
        return new Vector2(tempVec3.x, tempVec3.y);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // تجاهل اللمس المتعدد (pointer 0 = الإصبع الأول فقط)
        if (pointer != 0) return false;

        Vector2 worldCoords = unproject(screenX, screenY);

        if (slingshot.isTouchOnBird(worldCoords)) {
            isDragging = true;
            slingshot.pullBird(worldCoords);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer != 0) return false;   // الإصبع الأول فقط

        if (isDragging) {
            Vector2 worldCoords = unproject(screenX, screenY);
            slingshot.pullBird(worldCoords);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer != 0) return false;   // الإصبع الأول فقط

        if (isDragging) {
            isDragging = false;
            slingshot.releaseBird();
            return true;
        }
        return false;
    }
}