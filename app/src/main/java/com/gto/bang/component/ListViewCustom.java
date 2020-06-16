package com.gto.bang.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by shenjialong on 20/6/15 21:31.
 */
public class ListViewCustom extends ListView {

    public ListViewCustom(Context context) {
        super(context);
    }

    public ListViewCustom(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
