package com.basketbandit.rizumu.utility.extension;

import java.awt.geom.AffineTransform;

public class AffineTransformEx extends AffineTransform {

    @Override
    public void scale(double sx, double sy) {
        super.scale(sx, sy);
    }

    /**
     * Extends {@link #scale(double, double)} to return {@link AffineTransform} in a builder pattern
     * @param sx x axis position
     * @param sy y axis position
     * @return {@link AffineTransform} object with {@link #scale(double, double)} applied to it.
     */
    public AffineTransform inlineScale(double sx, double sy) {
        scale(sx, sy);
        return this;
    }
}
