package com.onlan.mainlistViewObject;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onlan.lazymemo.APPConfigure;
import com.onlan.lazymemo.MainActivity;
import com.onlan.lazymemo.MyApplication;
import com.onlan.lazymemo.R;

public class MainListAdapter extends BaseExpandableListAdapter {
	private LayoutInflater inflater;
	private ArrayList<Group> groupList;
	private ArrayList<List<ListItem>> childList;

	public MainListAdapter(Context context, ArrayList<Group> mgroupList,
			ArrayList<List<ListItem>> mchildList) {
		inflater = LayoutInflater.from(context);
		groupList = mgroupList;
		childList = mchildList;
	}

	// 返回父列表个数
	@Override
	public int getGroupCount() {
		return groupList.size();
	}

	// 返回子列表个数
	@Override
	public int getChildrenCount(int groupPosition) {
		return childList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {

		return groupList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {

		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder groupHolder = null;
		if (convertView == null) {
			groupHolder = new GroupHolder();
			convertView = inflater.inflate(R.layout.group, null);
			groupHolder.textView = (TextView) convertView
					.findViewById(R.id.group);
			groupHolder.imageView = (ImageView) convertView
					.findViewById(R.id.image);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}

		groupHolder.textView.setText(((Group) getGroup(groupPosition))
				.getTitle());
		groupHolder.imageView.setAlpha(80);
		if (isExpanded)// ture is Expanded or false is not isExpanded
			groupHolder.imageView.setImageResource(R.drawable.icon_down);
		else
			groupHolder.imageView.setImageResource(R.drawable.icon_right);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder childHolder = null;
		if (convertView == null) {
			childHolder = new ChildHolder();
			convertView = inflater.inflate(R.layout.mianlist_item_layout, null);

			childHolder.time = (TextView) convertView.findViewById(R.id.time);
			childHolder.content = (TextView) convertView
					.findViewById(R.id.content);
			childHolder.img = (ImageView) convertView
					.findViewById(R.id.imageView1);
			childHolder.img_juan = (ImageView) convertView
					.findViewById(R.id.imageView2);
			childHolder.contentView = (RelativeLayout) convertView
					.findViewById(R.id.main_itemview1212);
			childHolder.fontView = (RelativeLayout) convertView
					.findViewById(R.id.main_itemview);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}

		childHolder.time.setText(((ListItem) getChild(groupPosition,
				childPosition)).time.trim());
		childHolder.content.setText(((ListItem) getChild(groupPosition,
				childPosition)).content.trim());
		RelativeLayout.LayoutParams rl;
		if (APPConfigure.Screen_Width > 720) {
			rl = new RelativeLayout.LayoutParams(80, 80);
		} else {
			rl = new RelativeLayout.LayoutParams(50, 50);
		}
		RelativeLayout.LayoutParams rlweek = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams rlcontentView = new RelativeLayout.LayoutParams(
				APPConfigure.Screen_Width * 3 / 4,
				LayoutParams.WRAP_CONTENT);

		rl.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		if (!((ListItem) getChild(groupPosition, childPosition)).isLocal) {
			String phone = "        来自:"
					+ ((ListItem) getChild(groupPosition, childPosition)).FormPhone;
			childHolder.time.setText(((ListItem) getChild(groupPosition,
					childPosition)).time + phone);
			rl.leftMargin = 10;
			rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			rlcontentView.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			rlweek.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			rlweek.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			rlweek.leftMargin = 5;

			childHolder.content.setGravity(Gravity.LEFT);
			MemoBgset
					.setBg(childHolder.fontView,
							((ListItem) getChild(groupPosition, childPosition)).bgcolor,
							childHolder.img_juan, true);
		} else {
			childHolder.time.setText(((ListItem) getChild(groupPosition,
					childPosition)).time);
			rl.rightMargin = 0;
			rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rlcontentView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rlweek.rightMargin = 5;
			rlweek.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rlweek.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			childHolder.content.setGravity(Gravity.RIGHT);
			MemoBgset
					.setBg(childHolder.fontView,
							((ListItem) getChild(groupPosition, childPosition)).bgcolor,
							childHolder.img_juan, false);
		}
		childHolder.img.setLayoutParams(rl);
		childHolder.contentView.setLayoutParams(rlcontentView);
		childHolder.time.setLayoutParams(rlweek);
		childHolder.content.setTextSize(APPConfigure.APP_Memo_FontSize);

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class GroupHolder {
		TextView textView;
		ImageView imageView;
	}

	class ChildHolder {
		TextView time;
		ImageView img;
		ImageView img_juan;
		TextView content;
		RelativeLayout contentView;
		RelativeLayout fontView;
	}
}
