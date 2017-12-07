package com.yztc.damai.ui.recommend;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yztc.core.utils.DensityUtil;
import com.yztc.core.utils.SPUtils;
import com.yztc.core.utils.ToastUtils;
import com.yztc.core.views.banner.BannerView;
import com.yztc.core.views.banner.BannerViewPager;
import com.yztc.damai.R;
import com.yztc.damai.config.NetConfig;
import com.yztc.damai.help.Constant;
import com.yztc.damai.help.Event;
import com.yztc.damai.http.HttpHandlerFactory;
import com.yztc.damai.http.NetResponse;
import com.yztc.damai.ui.others.ChoiceCityActivity;
import com.yztc.damai.view.ClassifyView;
import com.yztc.damai.view.Type10View;
import com.yztc.damai.view.Type11View;
import com.yztc.damai.view.Type12View;
import com.yztc.damai.view.Type1View;
import com.yztc.damai.view.Type2View;
import com.yztc.damai.view.Type3View;
import com.yztc.damai.view.Type4View;
import com.yztc.damai.view.Type6View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecommendFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.bannerView)
    BannerView bannerView;
    @BindView(R.id.classifyView)
    ClassifyView classifyView;
    @BindView(R.id.recommendContainer)
    LinearLayout recommendContainer;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.choiceCity)
    TextView choiceCity;
    private ArrayList<BannerBean> banners = new ArrayList<>();
    private ArrayList<String> bannerStr = new ArrayList<>();

    private ArrayList<ClassifyBean> classifys = new ArrayList<ClassifyBean>();
    private ArrayList<HeadLineBean> headLines = new ArrayList<>();

    private ViewGroup rootView;

    private Gson gson = new Gson();
    private int cityId;
    private String cityName;

    public RecommendFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recommend, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        cityId = (int) SPUtils.get(getContext(), Constant.SP_CURR_CITY, 852);
        cityName = (String) SPUtils.get(getContext(), Constant.SP_CURR_CITY_N, "北京");
        choiceCity.setText(cityName);


        initBannerView(view);
        loadBanner();

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(true, 0, DensityUtil.dip2px(getContext(), 100));
    }


    private void initBannerView(View v) {
        bannerView.setOnBannerViewClick(new BannerViewPager.OnBannerViewClick() {
            @Override
            public void onBannerViewClick(int position) {
                ToastUtils.getInstance().showToast(banners.get(position).getName());
            }
        });
    }

    private void loadData() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("cityId", cityId + "");
        HttpHandlerFactory.getHttpHandler().get("Proj/Panev3.aspx", maps, new NetResponse() {
            @Override
            public void onResponse(String response) {

                mSwipeRefreshLayout.setRefreshing(false);
                recommendContainer.removeAllViews();
                try {
                    JSONObject object = new JSONObject(response);

                    fillClassifyView(object);

                    JSONArray list = object.getJSONArray("list");
                    for (int i = 0; i < list.length(); i++) {
                        TypeViewBean typeViewBean = gson.fromJson(list.getString(i), TypeViewBean.class);
                        switch (typeViewBean.getType()) {
                            case 1:
                                Type1View v1 = new Type1View(getContext());
                                v1.setData(typeViewBean);
                                recommendContainer.addView(v1);
                                break;
                            case 2:
                                Type2View v2 = new Type2View(getContext());
                                v2.setData(typeViewBean);
                                recommendContainer.addView(v2);
                                break;
                            case 3:
                                Type3View v3 = new Type3View(getContext());
                                v3.setData(typeViewBean);
                                recommendContainer.addView(v3);
                                break;
                            case 4:
                                Type4View v4 = new Type4View(getContext());
                                v4.setData(typeViewBean);
                                recommendContainer.addView(v4);
                                break;
                            case 6:
                                Type6View v6 = new Type6View(getContext());
                                v6.setData(typeViewBean);
                                recommendContainer.addView(v6);
                                break;
                            case 10:
                                Type10View v10 = new Type10View(getContext());
                                v10.setData(typeViewBean);
                                recommendContainer.addView(v10);
                                break;
                            case 11:
                                Type11View v11 = new Type11View(getContext());
                                v11.setData(typeViewBean);
                                recommendContainer.addView(v11);
                                break;
                            case 12:
                                Type12View v12 = new Type12View(getContext());
                                v12.setData(typeViewBean);
                                recommendContainer.addView(v12);
                                break;

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String erroe) {

            }
        });
    }

    private void fillClassifyView(JSONObject object) throws JSONException {
        JSONArray btnCtx = object.getJSONArray("btnCtx");
        classifys.clear();
        for (int i = 0, len = btnCtx.length(); i < len; i++) {
            classifys.add(gson.fromJson(btnCtx.getString(i), ClassifyBean.class));
        }
        classifyView.setClassifys(classifys);

        headLines.clear();
        JSONArray headline = object.getJSONArray("headline");
        for (int i = 0, len = headline.length(); i < len; i++) {
            headLines.add(gson.fromJson(headline.getString(i), HeadLineBean.class));
        }
       // classifyView.setHeadLines(headLines);
    }


    private void loadBanner() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("cityId", cityId + "");

        HttpHandlerFactory.getHttpHandler().get(NetConfig.BASE_URL2,
                "index/banner/13/list.json",
                maps, new NetResponse() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            banners.clear();
                            bannerStr.clear();
                            for (int i = 0, len = array.length(); i < len; i++) {
                                banners.add(gson.fromJson(array.getString(i), BannerBean.class));
                                bannerStr.add(array.getJSONObject(i).getString("Pic"));
                            }
                            bannerView.setData(bannerStr);
                            //再加载页面数据
                            loadData();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String erroe) {

                    }
                });
    }

    @Override
    public void onRefresh() {
        loadBanner();
    }

    @OnClick(R.id.choiceCity)
    public void onClick() {
        ChoiceCityActivity.start(getContext());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onChange(Event event) {
        switch (event.getType()) {
            case Event.EVENT_CITY_CHANGE:
                //reload();
                mSwipeRefreshLayout.setRefreshing(true);
                cityName = (String) SPUtils.get(getContext(), Constant.SP_CURR_CITY_N, "北京");
                choiceCity.setText(cityName);
                cityId = (int) SPUtils.get(getContext(), Constant.SP_CURR_CITY, 852);
                loadBanner();
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}
