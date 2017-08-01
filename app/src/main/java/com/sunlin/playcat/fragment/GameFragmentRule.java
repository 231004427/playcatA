package com.sunlin.playcat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sunlin.playcat.R;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.view.CircleTitleView;
import com.sunlin.playcat.view.NestedScrollWebView;

/**
 * Created by sunlin on 2017/7/30.
 */

public class GameFragmentRule extends Fragment {
    int id;
    private NestedScrollWebView webView;
    private CircleTitleView loadTextView;
    public static GameFragmentRule newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt("id",id);
        GameFragmentRule fragment = new GameFragmentRule();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments() != null ? getArguments().getInt("id") : 0;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_rule, null);

        loadTextView=(CircleTitleView)view.findViewById(R.id.loadTextView);
        webView=(NestedScrollWebView)view.findViewById(R.id.webView);

        loadTextView.setVisibility(View.VISIBLE);

        webView.loadUrl(CValues.SERVER_IMG+"/game/rule/"+String.valueOf(id));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //网页加载完成
                    loadTextView.setVisibility(View.INVISIBLE);
                } else {
                    //网页加载中
                }
            }
        });

        return view;
    }
}

