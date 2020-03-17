package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.audio.AudioPlayer;
import com.basketbandit.rizumu.audio.AudioPlayerController;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.object.TickObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.HashMap;

public abstract class Scene {
    protected Logger log = LoggerFactory.getLogger(Scene.class);

    protected RenderObject renderObject;
    protected TickObject tickObject;

    protected MouseAdapter mouseAdapter;
    protected KeyAdapter keyAdapter;

    protected AudioPlayer audioPlayer = AudioPlayerController.getAudioPlayer("music");
    protected AudioPlayer effectPlayer = AudioPlayerController.getAudioPlayer("effect");

    protected HashMap<String, Button> buttons = new HashMap<>();

    protected int[] centerBoth;

    public RenderObject getRenderObject() {
        return renderObject;
    }

    public TickObject getTickObject() {
        return tickObject;
    }

    public HashMap<String, Button> getButtons() {
        return buttons;
    }

    public abstract Scene init(Object... object); // adding nullable object parameters allows passing in any kind of object and then just cast
}
