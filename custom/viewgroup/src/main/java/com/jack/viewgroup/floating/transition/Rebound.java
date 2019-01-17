package com.jack.viewgroup.floating.transition;

import com.facebook.rebound.Spring;

public interface Rebound {
    
    Spring createSpringByBouncinessAndSpeed(double bounciness, double speed);
    Spring createSpringByTensionAndFriction(double tension, double friction) ;
    float transition(double progress, float startValue, float endValue);
    
}
