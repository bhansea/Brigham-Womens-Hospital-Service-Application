package edu.wpi.punchy_pegasi.frontend.animations;

import animatefx.animation.AnimateFXInterpolator;
import animatefx.animation.AnimationFX;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class Bobbing extends AnimationFX {
    /**
     * Create new Bounce
     *
     * @param node The node to affect
     */
    public Bobbing(Node node) {
        super(node);
    }

    public Bobbing() {
        super();
    }

    @Override
    public AnimationFX resetNode() {
        getNode().setTranslateY(-4);
        return this;
    }

    @Override
    protected void initTimeline() {
        setTimeline(
                new Timeline(
                        new KeyFrame(Duration.millis(0),
                                new KeyValue(getNode().translateYProperty(), -5, AnimateFXInterpolator.EASE)
                        ),
                        new KeyFrame(Duration.millis(750),
                                new KeyValue(getNode().translateYProperty(), 5, AnimateFXInterpolator.EASE)
                        ),
                        new KeyFrame(Duration.millis(1500),
                                new KeyValue(getNode().translateYProperty(), -5, AnimateFXInterpolator.EASE)
                        )
                ));
        getTimeline().setCycleCount(INDEFINITE);
    }
}
