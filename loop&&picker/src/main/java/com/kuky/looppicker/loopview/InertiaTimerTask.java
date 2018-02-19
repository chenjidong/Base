package com.kuky.looppicker.loopview;

import java.util.TimerTask;

final class InertiaTimerTask extends TimerTask {

    private float a;
    private final float velocityY;
    private final LoopView mLoopView;

    InertiaTimerTask(LoopView loopView, float velocityY) {
        super();
        mLoopView = loopView;
        this.velocityY = velocityY;
        a = Integer.MAX_VALUE;
    }

    @Override
    public final void run() {
        if (a == Integer.MAX_VALUE) {
            if (Math.abs(velocityY) > 2000F) {
                if (velocityY > 0.0F) {
                    a = 2000F;
                } else {
                    a = -2000F;
                }
            } else {
                a = velocityY;
            }
        }
        if (Math.abs(a) >= 0.0F && Math.abs(a) <= 20F) {
            mLoopView.cancelFuture();
            mLoopView.handler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
            return;
        }
        int i = (int) ((a * 10F) / 1000F);
        mLoopView.totalScrollY = mLoopView.totalScrollY - i;
        if (!mLoopView.isLoop) {
            float itemHeight = mLoopView.itemHeight;
            float top = (-mLoopView.initPosition) * itemHeight;
            float bottom = (mLoopView.getItemsCount() - 1 - mLoopView.initPosition) * itemHeight;
            if (mLoopView.totalScrollY - itemHeight * 0.25 < top) {
                top = mLoopView.totalScrollY + i;
            } else if (mLoopView.totalScrollY + itemHeight * 0.25 > bottom) {
                bottom = mLoopView.totalScrollY + i;
            }

            if (mLoopView.totalScrollY <= top) {
                a = 40F;
                mLoopView.totalScrollY = (int) top;
            } else if (mLoopView.totalScrollY >= bottom) {
                mLoopView.totalScrollY = (int) bottom;
                a = -40F;
            }
        }
        if (a < 0.0F) {
            a = a + 20F;
        } else {
            a = a - 20F;
        }
        mLoopView.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
    }
}
