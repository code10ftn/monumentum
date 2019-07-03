package io.github.code10ftn.monumentum.pager;

import android.content.Context;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

public class SmoothScroller extends Scroller {

    SmoothScroller(Context context) {
        super(context, new DecelerateInterpolator());
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, 350);
    }
}
