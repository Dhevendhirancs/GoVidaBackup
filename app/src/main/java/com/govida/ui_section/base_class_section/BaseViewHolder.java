/*
 * Copyright (C) 2019 GoVida
 * Author : 1276
 * Usage : BaseViewHolder abstract class for holding recycelrview data for viewholder
 * Date : 15 April 19
 */

package com.govida.ui_section.base_class_section;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;


public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    private int mCurrentPosition;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected abstract void clear();

    public void onBind(int position) {
        mCurrentPosition = position;
        clear();
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }
}
