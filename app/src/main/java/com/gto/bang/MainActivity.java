package com.gto.bang;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {
//
//    SectionsPagerAdapter mSectionsPagerAdapter;
//    private ViewPager mPager;// 页卡内容
//    private ImageView cursor;// 动画图片
//    private TextView t1, t2,t3;// 页卡头标
//    private int offset = 0;// 动画图片偏移量
//    private int currIndex = 0;// 当前页卡编号
//    private int bmpW;// 动画图片宽度
//    static TextView college;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(false);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        setContentView(R.layout.act_mian);
//        InitImageView();
//        InitTextView();
//        InitViewPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        Intent intent;
//        Toast t = Toast.makeText(MainActivity.this, "sorry forbidden", Toast.LENGTH_SHORT);
//        switch (id) {
//            case R.id.action_message:
//                intent = new Intent(this, MessageActivity.class);
//                startActivity(intent);
//                return true;
//            case R.id.action_personal:
//                intent = new Intent(this, PMainActivity.class);
//                startActivity(intent);
//                return true;
//            case R.id.action_create_experience:
//                intent = new Intent(this, CreateMainActivity.class);
//                startActivity(intent);
//                return true;
//            case R.id.action_introduce:
//                t.show();
//                return true;
//            case R.id.action_feedback:
//                intent = new Intent(this, CreateMainActivity.class);
//                startActivity(intent);
//                t.show();
//                return true;
//
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

//    public static void showDialog(String msg, Context context) {
//        AlertDialog.Builder builder = new Builder(context);
//        builder.setMessage(msg);
//        builder.setTitle("message detail").setIcon(android.R.drawable.btn_star);
//        builder.setPositiveButton("ok I know", new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();
//
//    }



    /**
     * 初始化头标
     */
//    private void InitTextView() {
//        t1 = (TextView) findViewById(R.id.main_student_tv);
//        t2 = (TextView) findViewById(R.id.main_teacher_tv);
//        t3 = (TextView) findViewById(R.id.main_qa_tv);
//
//        t1.setOnClickListener(new MyOnClickListener(0));
//        t2.setOnClickListener(new MyOnClickListener(1));
//        t3.setOnClickListener(new MyOnClickListener(2));
//    }
//
//    /**
//     * 初始化ViewPager
//     */
//    private void InitViewPager() {
//        mPager = (ViewPager) findViewById(R.id.main_vp);
//        mPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
//        mPager.setCurrentItem(0);
//        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
//    }
//
//    /**
//     * 初始化动画
//     */
//    private void InitImageView() {
//        cursor = (ImageView) findViewById(R.id.main_cursor);
//        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.x)
//                .getWidth();// 获取图片宽度
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int screenW = dm.widthPixels;// 获取分辨率宽度
//        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
//        Matrix matrix = new Matrix();
//        matrix.postTranslate(offset, 0);
//        cursor.setImageMatrix(matrix);// 设置动画初始位置
//    }



//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//        String[] titles = new String[]{getString(R.string.title_section1), getString(R.string.title_section2),getString(R.string.title_section3)};
//
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//
//            if(position==0){
//                return new HExperienceFragment();
//            }else if(position==1){
//                return new HQuestionFragment();
//            }else{
//                return new HBangbangFragment();
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return titles.length;
//        }
//
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            Locale l = Locale.getDefault();
//            return titles[position];
//        }
//    }


//
//
//    /**
//     * 头标点击监听
//     */
//    public class MyOnClickListener implements View.OnClickListener {
//        private int index = 0;
//
//        public MyOnClickListener(int i) {
//            index = i;
//        }
//
//        @Override
//        public void onClick(View v) {
//            mPager.setCurrentItem(index);
//        }
//    };

    /**
     * 页卡切换监听
     */
//    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
//
//        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
//        int two = one * 2;// 页卡1 -> 页卡3 偏移量
////        int three = one * 3;
//
//        @Override
//        public void onPageSelected(int arg0) {
//            Animation animation = null;
//            switch (arg0) {
//                case 0:
//                    if (currIndex == 1) {
//                        animation = new TranslateAnimation(one, 0, 0, 0);
//                    } else if (currIndex == 2) {
//                        animation = new TranslateAnimation(two, 0, 0, 0);
//                    }
//                    break;
//                case 1:
//                    if (currIndex == 0) {
//                        animation = new TranslateAnimation(offset, one, 0, 0);
//                    } else if (currIndex == 2) {
//                        animation = new TranslateAnimation(two, one, 0, 0);
//                    }
//                    break;
//                case 2:
//                    if (currIndex == 0) {
//                        animation = new TranslateAnimation(offset, two, 0, 0);
//                    } else if (currIndex == 1) {
//                        animation = new TranslateAnimation(one, two, 0, 0);
//                    }
//                    break;
//
////                case 3:
////                    if (currIndex == 0) {
////                        animation = new TranslateAnimation(offset, three, 0, 0);
////                    } else if (currIndex == 2) {
////                        animation = new TranslateAnimation(two, three, 0, 0);
////                    }
////                    break;
//            }
//            currIndex = arg0;
//            animation.setFillAfter(true);// True:图片停在动画结束位置
//            animation.setDuration(200);
//            cursor.startAnimation(animation);
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//        }
//    }
}
