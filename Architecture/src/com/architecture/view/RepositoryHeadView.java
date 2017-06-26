package com.architecture.view;

import android.content.Context;
import android.util.AttributeSet;

import com.architecture.R;
import com.architecture.widget.layoutview.MLinearLayout;

/**
 * Created by 20141022 on 2016/11/21.
 */
public class RepositoryHeadView extends MLinearLayout {

    public RepositoryHeadView(Context context) {
        super(context);
    }
    
    public RepositoryHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_knowledge_title;
    }

    @Override
    protected void onApplyData() {
        
    }
}
