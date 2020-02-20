package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.object.TickObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Scene {
    Logger log = LoggerFactory.getLogger(Scene.class);

    RenderObject getRenderObject();
    TickObject getTickObject();
    Scene init();
}
