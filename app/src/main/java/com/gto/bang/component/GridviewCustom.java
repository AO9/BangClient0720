package com.gto.bang.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Created by shenjialong on 20/7/05.
 */
public class GridviewCustom extends GridView {

    public GridviewCustom(Context context) {
        super(context);
    }

    public GridviewCustom(Context context, AttributeSet attributeSet) {
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
