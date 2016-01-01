package com.onlan.lazymemo.service;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.onlan.myView.ToastUitil;

public class GaodeLocationService extends Service implements
		AMapLocationListener, OnPoiSearchListener {
	private LocationManagerProxy mAMapLocManager = null;
	private WakeLock wakeLock;
	private String TAG = "GaodeLocation-server";
	private List<PoiItem> PoiItemlist;
	private AMapLocation getlocation;
	private static Boolean isPoiAroundSearch = false;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "-----------开始定位--------！");
		mAMapLocManager = LocationManagerProxy.getInstance(this);
		if (null != onGaodeLocationListener) {
			onGaodeLocationListener.onShow();
		}
		
		setGPSInfo();
	}

	private String setGPSInfo() {
		Criteria criteria = new Criteria();
		// 设置查询条件
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 设置准确而非粗糙的精度
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 设置相对省电而非耗电，一般高耗电量会换来更精确的位置信息
		criteria.setAltitudeRequired(false); // 不需要提供海拔信息
		criteria.setSpeedRequired(false); // 不需要速度信息
		criteria.setCostAllowed(false); // 不能产生费用
		// 第一个参数，传递criteria对象
		// 第二个参数，若为false,在所有Provider中寻找，不管该Provider是否处于可用状态，均使用该Provider。
		// 若为true，则在所有可用的Provider中寻找。比如GPS处于禁用状态，则忽略GPS Provider。
		String provider = "init";
		if (mAMapLocManager != null) {
			provider = mAMapLocManager.getBestProvider(criteria, true);
		}

		Log.d(TAG, "Best provider-->" + provider);
		return provider;

	}

	@Override
	public void onLocationChanged(Location location) {
		// AmapV2版本中定位不会回调此方法啦
	}

	public static void setIsPoiAroundSearch(Boolean isSearch) {
		isPoiAroundSearch = isSearch;
	}

	public static Boolean getIsPoiAroundSearch() {
		return isPoiAroundSearch;

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand-----------开始定位--------！");
		if (mAMapLocManager == null) {
			mAMapLocManager = LocationManagerProxy.getInstance(this);
		}
		mAMapLocManager.requestLocationData(LocationProviderProxy.AMapNetwork,
				10 * 1000, 10, this);
		return START_REDELIVER_INTENT;

	}

	private PoiSearch.Query query;// Poi查询条件类

	private void getNearlAddress(AMapLocation location) {
		int currentPage = 0;
		// 第一个参数表示搜索字符串，第二个参数表示POI搜索类型
		// 第三个参数表示POI搜索区域（空字符串代表全国）
		query = new PoiSearch.Query("", "", "021");
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页
		PoiSearch poiSearch = new PoiSearch(this, query);
		poiSearch.setBound(new SearchBound(new LatLonPoint(location
				.getLatitude(), location.getLongitude()), 1000));
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			double coordinateX = location.getLatitude();
			double coordinateY = location.getLongitude();
			String cityCode = "";// 定位当前城市的区号
			String desc = "";// 定位当前位置描述
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}

			String str = ("定位成功" + coordinateX + "" + coordinateY + ")"
					+ "\n精    度    :" + location.getAccuracy() + "米"
					+ "\n城市编码:" + cityCode + "\n位置描述:" + desc);
			Log.d(TAG, str + "setGPSInfo();" + setGPSInfo());
			if (isPoiAroundSearch) {
				getlocation = location;
				getNearlAddress(location);
			} else {
				if (null != onGaodeLocationListener) {
					onGaodeLocationListener.onGaodeLocationFinish(location,
							null);
					stopLocation();
				}
			}

		} else {
			if (null != onGaodeLocationListener) {
				onGaodeLocationListener.onError("定位失败");
			}
		}
		stopLocation();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "关闭定位--------！");
		stopLocation();
	}

	/**
	 * 销毁定位
	 */
	private void stopLocation() {

		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
			mAMapLocManager.destroy();
		}

		mAMapLocManager = null;
	}

	private static OnGaodeLocationListener onGaodeLocationListener;

	public static void setOnGaodeLocationListener(
			OnGaodeLocationListener monGaodeLocationListener) {
		onGaodeLocationListener = monGaodeLocationListener;
	}

	/**
	 * 下面是一个自定义的回调函数，用到回调定位结果
	 * 
	 * @author nanfeng
	 * 
	 */
	public static interface OnGaodeLocationListener {
		/**
		 * 请求完成
		 * 
		 * @param location
		 */
		void onGaodeLocationFinish(AMapLocation location, List<PoiItem> mPoiItem);

		/**
		 * 请求中
		 * 
		 * @param message
		 */
		void onError(String message);

		/**
		 * 开始请求
		 */
		void onShow();
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		// TODO Auto-generated method stub
		if (rCode == 0) {
			// 搜索POI的结果
			if (result != null && result.getQuery() != null) {
				// 是否是同一条
				if (result.getQuery().equals(query)) {
					// 取得第一页的poiitem数据，页数从数字0开始

					PoiItemlist = result.getPois();
					if (null != onGaodeLocationListener) {

						onGaodeLocationListener.onGaodeLocationFinish(
								getlocation, PoiItemlist);
						stopLocation();
					}
				}
			} else {
				ToastUitil.make("没有结果");
			}
		} else {
			ToastUitil.make("网络超时，请检查网络。");
		}
	}
}
