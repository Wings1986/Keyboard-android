package com.hbconsulting.ui;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;


/**
 * Created by iGold on 11/18/2018.
 */
public class MyAnimation {

    public static void animationList(ListView list) {
        AnimationSet set = new AnimationSet(true);
        Animation rtl = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f,Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f
        );
        rtl.setDuration(500);
        set.addAnimation(rtl);

        Animation alpha = new AlphaAnimation(0.0f, 1.0f);
        alpha.setDuration(500);
        set.addAnimation(alpha);

        LayoutAnimationController controller =
                new LayoutAnimationController(set, 0.5f);
        list.setLayoutAnimation(controller);
    }

    public static void animationScalDown(View view) {
        Animation anim = new ScaleAnimation(1, 1.2f, 1, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }
    
    public static void animationScalUp(View view) {
        Animation anim = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    public static void animationRotate(View view) {
        Animation anim = new RotateAnimation(0, -360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(100);
        view.startAnimation(anim);
    }

    public static void animationUp(View view) {
        Animation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);

        anim.setFillAfter(true);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }
    public static void animationDown(View view) {
        Animation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);

        anim.setFillAfter(true);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

}
