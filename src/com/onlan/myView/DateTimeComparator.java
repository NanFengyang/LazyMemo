package com.onlan.myView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.onlan.mainlistViewObject.Group;
import com.onlan.mainlistViewObject.ListItem;

public class DateTimeComparator {

	@SuppressWarnings("unchecked")
	public static void comparatorDateAndTime(ArrayList<List<ListItem>> childList) {
		for (int i = 0; i < childList.size(); i++) {
			StepChildComparator comparator = new StepChildComparator();
			Collections.sort(childList.get(i), comparator);

		}
		StepChildGoupComparator comparator = new StepChildGoupComparator();
		Collections.sort(childList, comparator);
	}
	@SuppressWarnings("unchecked")
	public static void comparatorbeforeDateAndTime(ArrayList<List<ListItem>> childList) {
		for (int i = 0; i < childList.size(); i++) {
			StepChildComparator comparator = new StepChildComparator();
			Collections.sort(childList.get(i), comparator);

		}
		StepChildafterGoupComparator comparator = new StepChildafterGoupComparator();
		Collections.sort(childList, comparator);
	}
	public static void comparatorafterDate(ArrayList<Group> groupList) {
		StepGroupafterComparator comparator = new StepGroupafterComparator();
		Collections.sort(groupList, comparator);
	}

	public static void comparatorbeforeDate(ArrayList<Group> groupList) {
		StepGroupbeforeComparator comparator = new StepGroupbeforeComparator();
		Collections.sort(groupList, comparator);
	}

	static class StepGroupafterComparator implements Comparator<Group> {
		private String DateTimeFormatPattern = "yyyy/MM/dd";

		/**
		 * 如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
		 */
		@Override
		public int compare(Group o1, Group o2) {
			SimpleDateFormat format = new SimpleDateFormat(
					DateTimeFormatPattern);
			Date acceptTime1 = null;
			Date acceptTime2 = null;
			try {
				acceptTime1 = format.parse(o1.getTitle());
				acceptTime2 = format.parse(o2.getTitle());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 对日期字段进行升序，如果欲降序可采用before方法
			if (acceptTime1.after(acceptTime2)) {
				return 1;
			}
			return -1;
		}
	}

	static class StepGroupbeforeComparator implements Comparator<Group> {
		private String DateTimeFormatPattern = "yyyy/MM/dd";

		/**
		 * 如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
		 */
		@Override
		public int compare(Group o1, Group o2) {
			SimpleDateFormat format = new SimpleDateFormat(
					DateTimeFormatPattern);
			Date acceptTime1 = null;
			Date acceptTime2 = null;
			try {
				acceptTime1 = format.parse(o1.getTitle());
				acceptTime2 = format.parse(o2.getTitle());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 对日期字段进行升序，如果欲降序可采用before方法
			if (acceptTime1.before(acceptTime2)) {
				return 1;
			}
			return -1;
		}
	}

	static class StepChildComparator implements Comparator<ListItem> {
		private String DateTimeFormatPattern = "yyyy/MM/dd HH:mm";

		/**
		 * 如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
		 */
		@Override
		public int compare(ListItem o1, ListItem o2) {
			SimpleDateFormat format = new SimpleDateFormat(
					DateTimeFormatPattern);
			Date acceptTime1 = null;
			Date acceptTime2 = null;
			try {
				acceptTime1 = format.parse(o1.date + " " + o1.time.trim());
				acceptTime2 = format.parse(o2.date + " " + o2.time.trim());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 对日期字段进行升序，如果欲降序可采用before方法
			if (acceptTime1.after(acceptTime2)) {
				return 1;
			}
			return -1;
		}
	}

	@SuppressWarnings("rawtypes")
	static class StepChildGoupComparator implements Comparator {
		private String DateTimeFormatPattern = "yyyy/MM/dd";

		/**
		 * 如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
		 */
		@SuppressWarnings("unchecked")
		@Override
		public int compare(Object o1, Object o2) {

			List<ListItem> List1 = (List<ListItem>) o1;
			List<ListItem> List2 = (List<ListItem>) o2;
			SimpleDateFormat format = new SimpleDateFormat(
					DateTimeFormatPattern);
			Date acceptTime1 = null;
			Date acceptTime2 = null;
			try {
				acceptTime1 = format.parse(List1.get(0).date.trim());
				acceptTime2 = format.parse(List2.get(0).date.trim());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 对日期字段进行升序，如果欲降序可采用before方法
			if (acceptTime1.before(acceptTime2)) {
				return 1;
			}
			return -1;
		}
	}
	@SuppressWarnings("rawtypes")
	static class StepChildafterGoupComparator implements Comparator {
		private String DateTimeFormatPattern = "yyyy/MM/dd";

		/**
		 * 如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
		 */
		@SuppressWarnings("unchecked")
		@Override
		public int compare(Object o1, Object o2) {

			List<ListItem> List1 = (List<ListItem>) o1;
			List<ListItem> List2 = (List<ListItem>) o2;
			SimpleDateFormat format = new SimpleDateFormat(
					DateTimeFormatPattern);
			Date acceptTime1 = null;
			Date acceptTime2 = null;
			try {
				acceptTime1 = format.parse(List1.get(0).date.trim());
				acceptTime2 = format.parse(List2.get(0).date.trim());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 对日期字段进行升序，如果欲降序可采用before方法
			if (acceptTime1.after(acceptTime2)) {
				return 1;
			}
			return -1;
		}
	}
}
