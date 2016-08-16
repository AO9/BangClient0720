package com.gto.bang.message;

import com.gto.bang.base.BaseActivity;

/**
 * Created by shenjialong on 16/6/10.
 * 消息列表
 */
public class MessageActivity extends BaseActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.bang_message);
//        ListView listView=(ListView)findViewById(R.id.bang_message_lv);
//        SimpleAdapter adapter = new SimpleAdapter(MessageActivity.this, getData1(), R.layout.bang_pmymessage_item, new String[]{
//                "bang_mtitle_tv"},
//                new int[]{R.id.bang_mtitle_tv});
//        listView.setAdapter(adapter);
//
//    }
//
//
//    //------------------------------------------------以下是测试数据－－－－－－－－－－－－－－
//
//    String[] tags=new String[]{"经验","问答","经验","问答"};
//    String[] names=new String[]{"论文届大佬","北交大学生","小弟","大连理工的学生"};
//    String[] dates=new String[]{"20160609","20160605","20160605","20160605"};
//    String[] titles=new String[]{"论文初稿","论文答辩","匿名评审","正式答辩"};
//    String[] comtents=new String[]{"是一个好文章","测试超级长的评论内容","好评","啦啦啦"};
//
//    public List<Map<String, Object>> getData1() {
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        Map<String, Object> map;
//
//        for(int j=0;j<3;j++){
//            for(int i=0;i<titles.length;i++){
//                map= new HashMap<String, Object>();
//                map.put("bang_mtag_tv", tags[i]);
//                map.put("bang_mcomment_tv", comtents[i]);
//                map.put("bang_mdate_tv",dates[i]);
//                map.put("bang_mname_tv",names[i]);
//                map.put("bang_mtitle_tv",titles[i]);
//                list.add(map);
//            }
//        }
//
//        return list;
//    }

}
