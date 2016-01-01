package com.onlan.lazymemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.onlan.myView.ToastLoding;

public class AboutUsActivity extends Activity {
	private ToastLoding mToastLoding;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about_us);
		MyApplication.addActivity(this);
		init();
		APPConfigure.setAPPBackGroup(
				(LinearLayout) findViewById(R.id.AboutUsActivityID), null,
				APPConfigure.APPBgImgUrl, APPConfigure.APPBgImgID);
	}

	@TargetApi(11)
	private void init() {
		mToastLoding = new ToastLoding(this, "加载中");
		ImageView back = (ImageView) findViewById(R.id.left_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		final WebView webview = (WebView) findViewById(R.id.webview);
		// webview.getSettings()
		// .setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webview.setHorizontalScrollBarEnabled(false);
		webview.setVerticalScrollBarEnabled(true);
		webview.getSettings().setDefaultTextEncodingName("UTF-8");
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setBackgroundColor(0); // 设置背景色
		webview.getBackground().setAlpha(2); // 设置填充透明度 范围：0-255
		webview.clearCache(true);
		webview.setClickable(false);
		webview.setEnabled(false);
		webview.loadUrl("http://27.115.102.122:8080/lazymanager/about.html");
		mToastLoding.show();
		webview.setVisibility(View.GONE);
		webview.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);

				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				webview.setVisibility(view.VISIBLE);
				mToastLoding.cancel();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_about_us, menu);
		return true;
	}
}
