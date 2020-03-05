package com.basketbandit.rizumu.utility.extension;

import java.awt.geom.AffineTransform;

public class AffineTransformEx extends AffineTransform {

    @Override
    public void scale(double sx, double sy) {
        super.scale(sx, sy);
    }

    public AffineTransform inlineScale(double sx, double sy) {
        scale(sx, sy);
        return this;
    }
}
