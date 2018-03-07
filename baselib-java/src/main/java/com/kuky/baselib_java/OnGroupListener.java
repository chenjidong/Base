package com.kuky.baselib_java;

import android.view.View;

/**
 * @author Kuky
 */
public interface OnGroupListener {
    String getGroupName(int position);

    View getGroupView(int position);
}
