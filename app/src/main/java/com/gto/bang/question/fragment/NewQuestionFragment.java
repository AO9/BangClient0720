package com.gto.bang.question.fragment;

import com.gto.bang.home.HQuestionFragment;
import com.gto.bang.util.Constant;

/**
 * Created by shenjialong on 20/6/19 19:54.
 */
public class NewQuestionFragment extends HQuestionFragment {

    @Override
    public String getArticleType() {
        return Constant.ARTICLETYPE_NEW;
    }

    @Override
    public String getRequestTag() {
        return NewQuestionFragment.class.getName();
    }
}
