package com.onlan.lazymemo.sqlData;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import com.onlan.lazymemo.MyApplication;
import com.onlan.mainlistViewObject.Group;
import com.onlan.mainlistViewObject.ListItem;

public class ReadLocaMemo {
	private static ArrayList<Group> HistorygroupList;
	private static ArrayList<List<ListItem>> HistorygchildList;
	private static MemoInfoService mMemoInfoService;
	private static ArrayList<Group> NowgroupList;
	private static ArrayList<List<ListItem>> NowchildList;
	boolean isHistory = false;

	public ReadLocaMemo() {

	}

	public void startRead(Activity activity) {
		mMemoInfoService = new MemoInfoService(activity);
		addnowData();
	}

	public ArrayList<Group> getHistorygroupList() {
		return HistorygroupList;
	}

	public ArrayList<Group> getNowgroupList() {
		return NowgroupList;
	}

	public ArrayList<List<ListItem>> getHistorygchildList() {
		return HistorygchildList;
	}

	public ArrayList<List<ListItem>> getNowchildList() {
		return NowchildList;
	}

	public void addnowData() {
		isHistory = false;
		List<MemoInfo> memoInfogroup = mMemoInfoService.getGroupDate();
		HistorygroupList = new ArrayList<Group>();
		HistorygchildList = new ArrayList<List<ListItem>>();
		NowgroupList = new ArrayList<Group>();

		int year = 0;
		int mouth = 0;
		int day = 0;
		if (memoInfogroup != null && memoInfogroup.size() > 0) {
			for (int i = 0; i < memoInfogroup.size(); i++) {
				String[] groupstr = memoInfogroup.get(i).getDate().toString().trim()
						.split("/");
				if (groupstr != null && groupstr.length == 3) {
					year = Integer.parseInt(groupstr[0]);
					mouth = Integer.parseInt(groupstr[1]);
					day = Integer.parseInt(groupstr[2]);

				}
				if (!isHistory(year, mouth, day)) {
					if (isaddGroup(memoInfogroup.get(i).getDate())) {
						Group group = new Group();
						group.setTitle(memoInfogroup.get(i).getDate());
						NowgroupList.add(group);
					}
				} else {
					if (ishisaddGroup(memoInfogroup.get(i).getDate())) {
						Group hisgroup = new Group();
						hisgroup.setTitle(memoInfogroup.get(i).getDate());
						HistorygroupList.add(hisgroup);
					}
				}

			}
		}
		List<MemoInfo> memochild = mMemoInfoService.getAllData(0,
				mMemoInfoService.getCount());

		NowchildList = new ArrayList<List<ListItem>>();
		if (NowgroupList.size() > 0) {
			for (Group mgroup : NowgroupList) {
				List<ListItem> childTemp = new ArrayList<ListItem>();

				if (memochild != null && memochild.size() > 0) {
					for (MemoInfo mMemoInfo : memochild) {
						if (mgroup.getTitle().equals(mMemoInfo.getDate())) {
							ListItem mlistitem = new ListItem();
							mlistitem.time = mMemoInfo.getTime();
							mlistitem.content = mMemoInfo.getContent();
							mlistitem.id = mMemoInfo.getid();
							mlistitem.isalarmclock = mMemoInfo
									.getIsalarmclock();
							mlistitem.type = mMemoInfo.getType();
							mlistitem.date = mMemoInfo.getDate();
							mlistitem.isLocal = true;

							mlistitem.bgcolor = mMemoInfo.getBgcorclor();
							mlistitem.FormPhone = "zizi";
							childTemp.add(mlistitem);

						}

					}
				}
				NowchildList.add(childTemp);

			}

		}

		if (HistorygroupList.size() > 0) {
			for (Group mgroup : HistorygroupList) {
				List<ListItem> hischildTemp = new ArrayList<ListItem>();

				if (memochild != null && memochild.size() > 0) {
					for (MemoInfo mMemoInfo : memochild) {
						if (mgroup.getTitle().equals(mMemoInfo.getDate())) {
							ListItem mlistitem = new ListItem();
							mlistitem.time = mMemoInfo.getTime();
							mlistitem.content = mMemoInfo.getContent();
							mlistitem.id = mMemoInfo.getid();
							mlistitem.isalarmclock = mMemoInfo
									.getIsalarmclock();
							mlistitem.type = mMemoInfo.getType();
							mlistitem.date = mMemoInfo.getDate();
							mlistitem.isLocal = true;
							mlistitem.FormPhone = "zizi";
							mlistitem.bgcolor = mMemoInfo.getBgcorclor();

							hischildTemp.add(mlistitem);

						}

					}
				}
				HistorygchildList.add(hischildTemp);

			}
		}

	}

	private Boolean isHistory(int year, int mouth, int day) {

		if (year == MyApplication.getMyApplication().mNowTime.year) {
			if (mouth == MyApplication.getMyApplication().mNowTime.month) {
				if (day >= MyApplication.getMyApplication().mNowTime.day) {
					return false;
				} else {
					return true;
				}

			} else if (mouth > MyApplication.getMyApplication().mNowTime.month) {
				return false;
			} else {
				return true;
			}

		} else if (year > MyApplication.getMyApplication().mNowTime.year) {
			return false;
		} else {
			return true;
		}

	}

	private Boolean isaddGroup(String date) {
		Boolean adddate = true;
		if (NowgroupList.size() > 0) {
			for (Group group : NowgroupList) {
				if (group.getTitle().toString().equals(date)) {
					adddate = false;
				} else {
					adddate = true;
				}
			}

		} else {
			adddate = true;
		}
		return adddate;

	}

	private Boolean ishisaddGroup(String date) {
		Boolean adddate = true;
		if (HistorygroupList.size() > 0) {
			for (Group group : HistorygroupList) {
				if (group.getTitle().toString().equals(date)) {
					adddate = false;
				} else {
					adddate = true;
				}
			}

		} else {
			adddate = true;
		}
		return adddate;

	}

}
