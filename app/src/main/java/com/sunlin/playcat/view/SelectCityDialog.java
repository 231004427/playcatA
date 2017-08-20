package com.sunlin.playcat.view;

import android.app.Dialog;
import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunlin.playcat.R;
import com.sunlin.playcat.SetAddressActivity;
import com.sunlin.playcat.common.ScreenUtil;
import com.sunlin.playcat.domain.Area;
import com.sunlin.playcat.fragment.AreaFragmentList;
import com.sunlin.playcat.fragment.AreaFragmentPageAdapter;
import com.sunlin.playcat.fragment.AreaListAdapter;
import com.sunlin.playcat.fragment.IndexFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlin on 2017/8/12.
 */

public class SelectCityDialog extends DialogFragment implements View.OnClickListener,AreaListAdapter.OnItemClickListener {

    private String TAG="SelectCityDialog";
    private TabLayout mTabLayout;
    private Window window = null;
    private AreaFragmentPageAdapter mAdapter;
    private ImageView closeImg;
    private ViewPager mViewPager;
    private List<Area>  areaList;
    private Area[] selectAreas;

    @Override //在onCreate中设置对话框的风格、属性等
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        setCancelable(true);
        //可以设置dialog的显示风格，如style为STYLE_NO_TITLE，将被显示title。遗憾的是，我没有在DialogFragment中找到设置title内容的方法。theme为0，表示由系统选择合适的theme。
        int theme = 0;
        setStyle(DialogFragment.STYLE_NO_TITLE,theme);
        areaList=new ArrayList<>();
        selectAreas=new Area[4];
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置dialog的宽高为屏幕的宽高
        View view = inflater.inflate(R.layout.dialog_city,container,false);
        mTabLayout=(TabLayout)view.findViewById(R.id.tabs);
        mViewPager=(ViewPager)view.findViewById(R.id.viewPager);
        closeImg=(ImageView)view.findViewById(R.id.closeImg);

        //设置Tab
        Area area=new Area();
        area.setParent_id(-1);
        area.setId(86);
        area.setName("省");
        areaList.add(area);
        selectAreas[0]=new Area();
        selectAreas[0].setParent_id(0);
        selectAreas[0].setId(86);
        selectAreas[0].setName("中国");

        //关闭对话框
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        FragmentManager fm =getChildFragmentManager();
        mAdapter=new AreaFragmentPageAdapter(fm,areaList,this);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(4);//设置缓存view 的个数（实际有3个，缓存2个+正在显示的1个）
        mViewPager.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));

        //TabLayout
        mTabLayout = (TabLayout)view.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        return view;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setWindowAnimations(R.style.dialogBottomAnim);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        lp.x = 0;
        lp.y = 0;
        lp.width = lp.MATCH_PARENT;
        lp.height=(ScreenUtil.getScreenHeightPixels(this.getContext())/2);
        dialogWindow.setBackgroundDrawable(null);
        dialogWindow.setAttributes(lp);


    }

    @Override
    public void onItemClick(View view) {

        TextView nameText=(TextView) view.findViewById(R.id.nameText);
        Area area=(Area) nameText.getTag();

        int size=areaList.size();
        selectAreas[size]=area;

        if(area.getType()==3){
            dismiss();
            ((SetAddressActivity)getActivity()).SetArea(selectAreas);
            return;
        }

        for(int i=area.getType();i<size;i++){
                areaList.remove(1);
        }

        Area areaTo=new Area();
        areaTo.setParent_id(area.getId());
        areaTo.setId(area.getId());
        switch (areaList.size()){
            case 1:
                areaTo.setName("市");break;
            case 2:areaTo.setName("区/县");break;
        }
        areaList.add(areaTo);
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(areaList.size());

        //获取当前Frament
        AreaFragmentList areaFragmentList=mAdapter.getCurrentFragment();
        areaFragmentList.Reload(area.getId());


        //Log.e(TAG,"onItemClick:"+area.getId()+"|"+area.getType());
    }
}
