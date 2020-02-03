package com.basketbandit.rizumu.scene;

import com.basketbandit.rizumu.score.Statistics;

import java.awt.*;

public class ResultsScene implements Scene {
    private final Statistics statistics;

    ResultsScene(Statistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public RenderObject getRenderObject() {
        return null;
    }

    @Override
    public TickObject getTickObject() {
        return null;
    }

    private class ResultsRenderer implements RenderObject {
        @Override
        public void render(Graphics2D g) {
        }
    }

    private class ResultsTicker implements TickObject {
        @Override
        public void tick() {
        }
    }
}
