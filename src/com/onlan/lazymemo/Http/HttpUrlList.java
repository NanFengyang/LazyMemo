package com.onlan.lazymemo.Http;

public class HttpUrlList {
	/**
	 * 测试：http://27.115.102.122:8080/
	 */
	private final static String Ip = "http://27.115.102.122:8080/";

	public final static String httpLoginURl = Ip + "memo/user/login";

	public final static String httpgetVcodeURl = Ip + "memo/user/getVcode";

	public final static String httpaddMemoURl = Ip + "memo/memo/addArray";
	
	public final static String httpaddCommonMemoURl = Ip + "memo/memo/addPublic";

	public final static String httpgetMemoListURl = Ip + "memo/memo/query";

	public final static String httpAppInstallURl = Ip + "memo/app/install";

	public final static String httpDeleteMemoURl = Ip + "memo/memo/delete";

	public final static String httpMyMemoURl = Ip + "memo/memo/fromMe";

	public final static String httpAppVersionURl = Ip + "memo/app/version";

	public final static String httpRespondMemoURl = Ip + "memo/memo/respond";

	public final static String httpAcceptListMemoURl = Ip
			+ "memo/memo/acceptList";

	public final static String httpAppThemeMemoURl = Ip + "memo/app/theme";
	
	public final static String httpCommonMemoURl = Ip + "memo/memo/queryPublic";

}
