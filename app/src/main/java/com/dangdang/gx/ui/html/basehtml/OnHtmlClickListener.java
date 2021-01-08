package com.dangdang.gx.ui.html.basehtml;


public interface OnHtmlClickListener {
	/**
	 * 显示土司
	 * 
	 * @param msg
	 *            土司信息
	 */
	void onShowToast(String msg);

	/**
	 * 
	 * @param methodName
	 *            方法名
	 * @param methodParam
	 *            方法参数
	 */
	void callHandler(String methodName, String methodParam);

	/**
	 * 客户端提供内置字体url
	 * @return  url 内置字体可访问的url地址
	 */
	String getServerFont();

	/**
	 * 客户端缓存图片接口
	 * @param srcImgUrl 图片在服务器端的地址
	 * @return localImgUrl：客户端缓存后的本地图片地址
	 */
	String localStorageImg(String srcImgUrl);

	/**
	 * 完成下拉刷新的回调接口
	 * @param isFinish true:完成下拉刷新 false:下拉刷新失败

	 */
	void refreshFinished(boolean isFinish);
	
	/**
	 * 获取参数
	 * @return 参数
	 */
	String getParam();
	
	/**
	 * 获取内部活动状态
	 * @param num
	 * @return
	 */
	void getNativeScrollState(int num);

	/**
	 * native 不处理滑动区域
	 * @param start
	 * @param end
	 */
	void setNotScrollHeight(int start, int end);

    /**
     * native 不处理滑动区域们
     * @param
     */
    void addNotScrollHeightArray(int start, int end);
    /**
     * 清除  native 不处理滑动区域们
     *
     */
    void clearNotScrollHeightArray();

    /**
     *  原生是否支持h5调用的当前接口
     *
     *  callbackMethodName 接口名字
     *
     *  原生判断是否支持 callbackMethodName 接口，支持给h5返回 1 ，不支持给h5返回0。
     */
    int checkSupport(String callbackMehtodName);

}
