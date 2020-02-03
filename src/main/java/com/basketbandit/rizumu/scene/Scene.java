package com.basketbandit.rizumu.scene;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Scene {
    Logger log = LoggerFactory.getLogger(Scene.class);

    RenderObject getRenderObject();
    TickObject getTickObject();
}
