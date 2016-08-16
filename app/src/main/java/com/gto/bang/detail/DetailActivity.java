package com.gto.bang.detail;

import com.gto.bang.base.BaseActivity;

public class DetailActivity extends BaseActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act_detail);
//        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        initViewPager();
//        //        actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//        case android.R.id.home:
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void initViewPager() {
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//        LayoutInflater inflater = LayoutInflater.from(this);
//        ImageView view1 = (ImageView) inflater.inflate(R.layout.act_detail_image, null);
//        ImageView view2 = (ImageView) inflater.inflate(R.layout.act_detail_image, null);
//        ImageView view3 = (ImageView) inflater.inflate(R.layout.act_detail_image, null);
//        view1.setBackgroundResource(R.drawable.rav1);
//        view1.setBackgroundResource(R.drawable.rav2);
//        view1.setBackgroundResource(R.drawable.rav3);
//        ArrayList<ImageView> views = new ArrayList<ImageView>();
//        views.add(view1);
//        views.add(view2);
//        views.add(view3);
//        viewPager.setAdapter(new ImageAdapter(views));
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int arg0) {
//                //                handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, arg0, 0));
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//                switch (arg0) {
//                case ViewPager.SCROLL_STATE_DRAGGING:
//                    //                    handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
//                    break;
//                case ViewPager.SCROLL_STATE_IDLE:
//                    //                    handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
//                    break;
//                default:
//                    break;
//                }
//            }
//        });
//        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
//    }
//
//    private class ImageAdapter extends PagerAdapter {
//
//        private ArrayList<ImageView> viewlist;
//
//        public ImageAdapter(ArrayList<ImageView> viewlist) {
//            this.viewlist = viewlist;
//        }
//
//        @Override
//        public int getCount() {
//            return Integer.MAX_VALUE;
//        }
//
//        @Override
//        public boolean isViewFromObject(View arg0, Object arg1) {
//            return arg0 == arg1;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            position %= viewlist.size();
//            if (position < 0) {
//                position = viewlist.size() + position;
//            }
//            ImageView view = viewlist.get(position);
//            ViewParent vp = view.getParent();
//            if (vp != null) {
//                ViewGroup parent = (ViewGroup) vp;
//                parent.removeView(view);
//            }
//            container.addView(view);
//            return view;
//        }
//    }

}
