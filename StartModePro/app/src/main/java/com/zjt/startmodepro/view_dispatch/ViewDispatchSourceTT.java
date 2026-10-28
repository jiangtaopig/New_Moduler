package com.zjt.startmodepro.view_dispatch;

/**
 * Created by ${za.zhu.jiangtao}
 * on 2019/12/26
 */
public class ViewDispatchSourceTT {


    /**
     *  View 的 dispatchTouchEvent
     * @param event
     * @return
     */
//    public boolean dispatchTouchEvent (MotionEvent event) {
//        if (mOnTouchListener != null && ( mViewFlags & ENABLED_MASK) == ENABLED &&
//                mOnTouchListener.onTouch( this, event)) {
//            return true;
//        }
//        return onTouchEvent(event);
//    }


    /**
     *
     *
     *  View 的 onTouchEvent 源码
     *
     *
     */

//    public boolean onTouchEvent(MotionEvent event) {
//        final int viewFlags = mViewFlags;
//        if ((viewFlags & ENABLED_MASK) == DISABLED) {
//            // A disabled view that is clickable still consumes the touch
//            // events, it just doesn't respond to them.
//            return (((viewFlags & CLICKABLE) == CLICKABLE ||
//                    (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE));
//        }
//        if (mTouchDelegate != null) {
//            if (mTouchDelegate.onTouchEvent(event)) {
//                return true;
//            }
//        }

          /**  只要是可点击 View 都返回 true */
//        if (((viewFlags & CLICKABLE) == CLICKABLE ||
//                (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE)) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_UP:
//                    boolean prepressed = (mPrivateFlags & PREPRESSED) != 0;
//                    if ((mPrivateFlags & PRESSED) != 0 || prepressed) {
//                        // take focus if we don't have it already and we should in
//                        // touch mode.
//                        boolean focusTaken = false;
//                        if (isFocusable() && isFocusableInTouchMode() && !isFocused()) {
//                            focusTaken = requestFocus();
//                        }
//                        if (!mHasPerformedLongPress) {
//                            // This is a tap, so remove the longpress check
//                            removeLongPressCallback();
//                            // Only perform take click actions if we were in the pressed state
//                            if (!focusTaken) {
//                                // Use a Runnable and post this rather than calling
//                                // performClick directly. This lets other visual state
//                                // of the view update before click actions start.
//                                if (mPerformClick == null) {
//                                    mPerformClick = new PerformClick();
//                                }
//                                if (!post(mPerformClick)) {
//                                    performClick();
//                                }
//                            }
//                        }
//                        if (mUnsetPressedState == null) {
//                            mUnsetPressedState = new UnsetPressedState();
//                        }
//                        if (prepressed) {
//                            mPrivateFlags |= PRESSED;
//                            refreshDrawableState();
//                            postDelayed(mUnsetPressedState,
//                                    ViewConfiguration.getPressedStateDuration());
//                        } else if (!post(mUnsetPressedState)) {
//                            // If the post failed, unpress right now
//                            mUnsetPressedState.run();
//                        }
//                        removeTapCallback();
//                    }
//                    break;
//                case MotionEvent.ACTION_DOWN:
//                    if (mPendingCheckForTap == null) {
//                        mPendingCheckForTap = new CheckForTap();
//                    }
//                    mPrivateFlags |= PREPRESSED;
//                    mHasPerformedLongPress = false;
//                    postDelayed(mPendingCheckForTap, ViewConfiguration.getTapTimeout());
//                    break;
//                case MotionEvent.ACTION_CANCEL:
//                    mPrivateFlags &= ~PRESSED;
//                    refreshDrawableState();
//                    removeTapCallback();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    final int x = (int) event.getX();
//                    final int y = (int) event.getY();
//                    // Be lenient about moving outside of buttons
//                    int slop = mTouchSlop;
//                    if ((x < 0 - slop) || (x >= getWidth() + slop) ||
//                            (y < 0 - slop) || (y >= getHeight() + slop)) {
//                        // Outside button
//                        removeTapCallback();
//                        if ((mPrivateFlags & PRESSED) != 0) {
//                            // Remove any future long press/tap checks
//                            removeLongPressCallback();
//                            // Need to switch from pressed to not pressed
//                            mPrivateFlags &= ~PRESSED;
//                            refreshDrawableState();
//                        }
//                    }
//                    break;
//            }
//            return true;
//        }
//        return false;
//    }



    /**
     * ViewGroup 的 dispatchTouchEvent
     * @param ev
     * @return
     */
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        final int action = ev.getAction();
//        final float xf = ev.getX();
//        final float yf = ev.getY();
//        final float scrolledXFloat = xf + mScrollX;
//        final float scrolledYFloat = yf + mScrollY;
//        final Rect frame = mTempRect;
//
        /** 这个值默认是false, 然后我们可以通过requestDisallowInterceptTouchEvent(boolean disallowIntercept)方法来改变 disallowIntercept的值 */
//        boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
        /** 这里是ACTION_DOWN的处理逻辑 */
//        if (action == MotionEvent.ACTION_DOWN) {
//            //清除mMotionTarget, 每次ACTION_DOWN都很设置mMotionTarget为null
//            if (mMotionTarget != null) {
//                mMotionTarget = null;
//            }
//
            /** disallowIntercept默认是false, 就看ViewGroup的onInterceptTouchEvent()方法 */
//            if (disallowIntercept || !onInterceptTouchEvent(ev)) {
//                ev.setAction(MotionEvent.ACTION_DOWN);
//                final int scrolledXInt = (int) scrolledXFloat;
//                final int scrolledYInt = (int) scrolledYFloat;
//                final View[] children = mChildren;
//                final int count = mChildrenCount;
//                //遍历其子View
//                for (int i = count - 1; i >= 0; i--) {
//                    final View child = children[i];
//
//                    //如果该子View是VISIBLE或者该子View正在执行动画, 表示该View才
//                    //可以接受到Touch事件
//                    if ((child.mViewFlags & VISIBILITY_MASK) == VISIBLE
//                            || child.getAnimation() != null) {
//                        //获取子View的位置范围
//                        child.getHitRect(frame);
//
//                        //如Touch到屏幕上的点在该子View上面
//                        if (frame.contains(scrolledXInt, scrolledYInt)) {
//                            // offset the event to the view's coordinate system
//                            final float xc = scrolledXFloat - child.mLeft;
//                            final float yc = scrolledYFloat - child.mTop;
//                            ev.setLocation(xc, yc);
//                            child.mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;
//
                            /** 调用该子View的dispatchTouchEvent()方法 */
//                            if (child.dispatchTouchEvent(ev))  {
                                /** 如果child.dispatchTouchEvent(ev)返回true表示 */
//                                //该事件被消费了，设置mMotionTarget为该子View
//                                mMotionTarget = child;
//                                //直接返回true
//                                return true;
//                            }
//                            // The event didn't get handled, try the next view.
//                            // Don't reset the event's location, it's not
//                            // necessary here.
//                        }
//                    }
//                }
//            }
//        }

    /** ------------------------------------------------------------------------ ACTION_DOWN 事件结束 ---------------------------------------------------------------------- */

        /** 判断是否为ACTION_UP或者ACTION_CANCEL */
 //        boolean isUpOrCancel = (action == MotionEvent.ACTION_UP) ||
//                (action == MotionEvent.ACTION_CANCEL);
//
//        if (isUpOrCancel) {
            /** 如果是ACTION_UP或者ACTION_CANCEL, 将disallowIntercept设置为默认的false
            假如我们调用了requestDisallowInterceptTouchEvent()方法来设置disallowIntercept为true
            当我们抬起手指或者取消Touch事件的时候要将disallowIntercept重置为false
            所以说上面的disallowIntercept默认在我们每次ACTION_DOWN的时候都是false */
//            mGroupFlags &= ~FLAG_DISALLOW_INTERCEPT;
//        }
//
//        // The event wasn't an ACTION_DOWN, dispatch it to our target if
//        // we have one.
//        final View target = mMotionTarget;
        /** mMotionTarget为null意味着没有找到消费点击事件的 View, 所以我们需要调用 ViewGroup 父类的
            dispatchTouchEvent()方法，也就是View的dispatchTouchEvent()方法 */
//        if (target == null) {
//            // We don't have a target, this means we're handling the
//            // event as a regular view.
//            ev.setLocation(xf, yf);
//            if ((mPrivateFlags & CANCEL_NEXT_UP_EVENT) != 0) {
//                ev.setAction(MotionEvent.ACTION_CANCEL);
//                mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//
        /** 这个 if 里面的代码 ACTION_DOWN 不会执行，只有 ACTION_MOVE
         ACTION_UP 才会走到这里, 假如在ACTION_MOVE或者ACTION_UP拦截的Touch事件, 将 ACTION_CANCEL 派发给 target，
         然后直接返回 true,表示消费了此 Touch 事件.
         注意如果子 View 中调用 getParent().requestDisallowInterceptTouchEvent(true)，那么下面的 disallowIntercept 就被设置为 true。

         比如，在 My_LinearLayout 的 onInterceptTouchEvent 的 ACTION_MOVE 中返回 true，那么 My_Button 中的点击事件由于被 My_LinearLayout 拦截，导致点击事件无法被 My_Button 消费。
         但是，如果在 My_Button 的 dispatchTouchEvent 中 调用  getParent().requestDisallowInterceptTouchEvent(true)，
         那么下面的 disallowIntercept = true ，点击事件依然能够被My_Button 消费
         */
//        if (!disallowIntercept && onInterceptTouchEvent(ev)) {
//            final float xc = scrolledXFloat - (float) target.mLeft;
//            final float yc = scrolledYFloat - (float) target.mTop;
//            mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;
//            ev.setAction(MotionEvent.ACTION_CANCEL);
//            ev.setLocation(xc, yc);

               /** 执行 view 点击事件的 ACTION_CANCEL **/
//            if (!target.dispatchTouchEvent(ev)) {
//            }
//            // clear the target
//            mMotionTarget = null;
//            // Don't dispatch this event to our own view, because we already
//            // saw it when intercepting; we just want to give the following
//            // event to the normal onTouchEvent().
//            return true;
//        }
//
//        if (isUpOrCancel) {
//            mMotionTarget = null;
//        }
//
//        // finally offset the event to the target's coordinate system and
//        // dispatch the event.
//        final float xc = scrolledXFloat - (float) target.mLeft;
//        final float yc = scrolledYFloat - (float) target.mTop;
//        ev.setLocation(xc, yc);
//
//        if ((target.mPrivateFlags & CANCEL_NEXT_UP_EVENT) != 0) {
//            ev.setAction(MotionEvent.ACTION_CANCEL);
//            target.mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;
//            mMotionTarget = null;
//        }
//
        /** 如果没有拦截ACTION_MOVE, ACTION_DOWN的话，直接将Touch事件派发给target */
//        return target.dispatchTouchEvent(ev);
//    }
}
