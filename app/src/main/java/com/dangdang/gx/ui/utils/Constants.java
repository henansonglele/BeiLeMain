package com.dangdang.gx.ui.utils;

public interface Constants {

	String USER_UNDEFINE = "undefine";

	String BROWSER_PATH = "browser_path";
	String WIFI_CONNECTING = "wifi_connecting";

	/**
	 * 广播常量
	 */
	String ACTION_LOGIN_CANCEL = "com.dangdang.reader.action.login.cancel";
	String ACTION_LOGIN_SUCCESS = "com.dangdang.reader.action.login.success";
	String ACTION_LOGOUT_SUCCESS = "com.dangdang.reader.action.logout.success";
	String ACTION_MODIFY_USER_INFO_SUCCESS = "com.dangdang.reader.action.modify.user.info.success";
	String BROADCAST_DELETE_BOOK = "com.dangdang.reader.broadcast.delete.book";
	String BROADCAST_REFRESH_LIST = "com.dangdang.reader.broadcast.refresh.list";
	String BROADCAST_FINISH_MOVE_BOOK = "com.dangdang.reader.finish_move_book";
	String BROADCAST_GROUP_TO_EDIT_MODE = "com.dangdang.reader.group_to_edit_mode";
//	String BROADCAST_DOWNLOAD_BOOK = "com.dangdang.reader.broadcast.download.book";
	String BROADCAST_DOWNLOAD_BOOK_FINISH = "com.dangdang.reader.broadcast.download.book.finish";
	String BROADCAST_IMPORT_BOOKLIST = "com.dangdang.reader.broadcast.import.booklist";
	String BROADCAST_SEND_BAIDU_DATA = "com.dangdang.reader.broadcast.send_baidu_data";
	String BROADCAST_RECHARGE_SUCCESS = "com.dangdang.reader.broadcast.recharge_success";
	String BROADCAST_GET_WEIXIN_CODE_SUCCESS = "com.dangdang.reader.broadcast.get.weixincode.success";
	String BROADCAST_BUY_DIALOG_CANCEL = "com.dangdang.reader.broadcast.buy_dialog_cancel";
	String BROADCAST_SHOW_ANNOUNCEMENT = "com.dangdang.reader.broadcast.show.announcement";
	String BROADCAST_START_AND_GOLD = "com.dangdang.reader.broadcast.start.and.gold";
	String BROADCAST_REFRESH_COMMENT_PRAISED = "com.dangdang.reader.broadcast.start.refresh.praised";
	String BROADCAST_REFRESH_COMMENT_COUNT = "com.dangdang.reader.broadcast.start.refresh.count";
	String BROADCAST_REFRESH_GET_GOLD = "com.dangdang.reader.broadcast.get.gold";
	String BROADCAST_REFRESH_DELETE_COLLECT = "com.dangdang.reader.broadcast.delete.collect";
	String BROADCAST_REFRESH_ADD_COLLECT = "com.dangdang.reader.broadcast.add.collect";
	String ACTION_REFRESH_FINDREAD = "com.dangdang.reader.action.findread";
	String ACTION_JUMP_FANPIAN = "com.dangdang.reader.action.jumpfanpian";
	String ACTION_JUMP_QIANGXIANDU = "com.dangdang.reader.action.jumpqiangxiandu";
	String ACTION_JUMP_GOTOCOLUMN = "com.dangdang.reader.action.gotocolumn";
//	String ACTION_DOWNLOAD_FULLREAD = "com.dangdang.reader.action.download.fullread";
//	String ACTION_DOWNLOAD_TRYREAD = "com.dangdang.reader.action.download.tryread";
	String ACTION_REFRESH_USER_INFO = "android.dang.action.refresh.user.info";
	String ACTION_TO_SHELF = "android.dang.action.to.shelf";
	String ACTION_PLUGIN_CHANNEL_SUBSCRIBE_OP_SUCCESS = "android.dangdang.reader.action.channel.subscribe.op.success";
	String ACTION_PLUGIN_CHANNEL_DELETE_ARTICLE_SUCCESS = "android.dangdang.reader.action.channel.delete.article.success";
	String ACTION_SHARE_TO_BAR_SUCCESS = "share_to_bar_success";
	String ACTION_CHANNEL_SUB_STATE_CHANGE = "ACTION_CHANNEL_SUB_STATE_CHANGE";
	String ACTION_ARTICLE_COLLECT_STATE_CHANGE = "com.dangdang.reader.action.article.collect.state.change";
	String ACTION_ARTICLE_REFRESH_READPLAN_LIST = "com.dangdang.reader.action.refresh.readplan.list";
	String ACTION_ARTICLE_REFRESH_READPLAN_DETAIL = "com.dangdang.reader.action.refresh.readplan.detail";
	String ACTION_ARTICLE_REFRESH_SHELF_READPLAN_LIST = "com.dangdang.reader.action.refresh.shelf.readplan.list";
	String ACTION_ARTICLE_SYN_READ_PROGRESS_SUCCESS = "com.dangdang.reader.action.syn.read.progress.success";
	String ACTION_STOP_TTS = "com.dangdang.reader.broadcast.stop_tts";

	//token Invalidate
	String ACTION_TOKEN_INVALIDATE = "com.dangdang.reader.action.token.invalidate";

	String DANGDANG_DEFAULT_USER = "dangdang_default_user";

    String FINISH_LABEL_ACTIVITY = "finish_label_activity";

	int FILE_MAX_SIZE = 1024;
	int UNKNOW_TYPE = 0;

	/**
	 * 书架数据库常量
	 */
	String JSON_SALEID = "saleid";
	// String JSON_GROUP = "group";
	String JSON_SIZE = "bookSize";
	String JSON_AUTHOR = "author";
	String JSON_DATE = "publishDate";
	String JSON_COVER = "cover";
	String JSON_OVERDUE = "overdue";
	String JSON_DESC = "desc";
	String JSON_LOCAL = "local";
	String JSON_SERVER = "server";
	String JSON_PRELOAD = "preload";
	String JSON_DEADLINE = "deadline";
	String JSON_DOWN_STATUS = "downStatus";
	String JSON_IS_SPLIT_EPUB = "isSplitEPub";
	String JSON_NEW_CHAPTER_COUNT = "newChapterCount";
	String JSON_GET_NEW_CHAPTER_TIME = "getNewChapterTime";

	String BORROW_TYPE = "1003";
	String BORROW_BEGIN_DATE = "createDate"; // 借阅开始时间
	String BORROW_DURATION = "borrowDuration";// 借阅时长
	String BORROW_APPEND = "canBorrow"; // 可以续借
	String BORROW_END_DATE = "endDate"; // 可以续借

	String SHELF_PRE = "shelf_pre";
	String SHELF_ORDER_TIME = "shelf_order_time";
	String SHELF_GROUP_NAME = "shelf_group_name";
	String SHELF_GROUP_SELECT_INDEX = "shelf_group_select_index";
	String INDEX = "index";
	String EDIT_MODE = "edit_mode";
	String ORDER_TYPE = "order_type";
	String EDIT_TYPE = "edit_type";

//	int MSG_DELETE_ONE_BOOK = 0;
//	int MSG_DELETE_ONE_GROUP = 1;
//	int MSG_UPDATE_SERVER_MAX = 2;

	int MSG_WHAT_DELETE_RECORD = 3;
	int MSG_WHAT_SET_LOGIN_INFO = 4;

	int MSG_WHAT_REQUEST_DATA_ERROR = 5;
	int MSG_WHAT_REQUEST_DATA_SUCCESS = 6;

	int MSG_WHAT_GETCERT_SUCCESS = 0x1001;
	int MSG_WHAT_GETCERT_FAILED = 0x1002;

	String PERSONAL_UPDATE_INFOMATION = "personal_update_infomation";
	float OLD_FILE_VERSION_CODE = 1.2f;

	/**
	 * 统计常量
	 */
	String BROWSE_BOOK = "browse";
	/**
	 * 跳转应用相关常量
	 */
	String ACTION_ORIGINAL = "com.dangdang.reader.MAIN";
	String ACTION_KEY_SOURCE = "source";
	String ACTION_KEY_EXTRA_CONTENT = "extra_content";
	// html 打开客户端
	String ACTION_ANDROID_SCHEME = "androidddxxhtml";

	long serverTime = 0;
	long localTime = 0;

	String OTHERS = "others"; // 偷来的
	String STEAL_PERCENT = "steal_percent"; // 偷来的比例
	String BOOK_ID = "book_id";
	String BOOK_NAME = "book_name";
	String BOOK_DIR = "book_dir";

	String MONTHLY_CHANNEL_NAME = "monthly_channel_name";//包月专栏名称
	String MONTHLY_SYNC_TIME = "monthly_sys_time";//包月的书籍的上次同步时间
	String MONTHLY_CHANNEL_ID = "monthly_channel_id";//包月的专栏信息
	String MONTHLY_AUTH_STATUS="monthly_auth_status";//包月的状态，还了0， 包月中1， 购买2
	String CHANNEL_END_TIME="channel_end_time";//包月专栏的结束时间
	/**
	 * static
	 */

	String EXTRA_MAIN_TAG = "EXTRA_MAIN_TAG";
	String EXTRA_MAIN_TAG_HOME = "EXTRA_MAIN_TAG_HOME";
	String EXTRA_MAIN_TAG_BOOK_STORE = "EXTRA_MAIN_TAG_BOOK_STORE";
	String EXTRA_MAIN_TAG_FIND = "EXTRA_MAIN_TAG_FIND";
	String EXTRA_MAIN_TAG_LEAD_READING = "EXTRA_MAIN_TAG_LEAD_READING";
	String EXTRA_MAIN_TAG_PERSONAL = "EXTRA_MAIN_TAG_PERSONAL";
    String EXTRA_MAIN_TAG_SHELF = "EXTRA_MAIN_TAG_SHELF";
    String EXTRA_MAIN_TAG_LISTENBOOK = "EXTRA_MAIN_TAG_LISTENBOOK";
	String EXTRA_START_MAIN_TYPE = "extra_start_main_type";
	String EXTRA_START_MAIN_TYPE_IM= "extra_start_main_type_im";
	String EXTRA_START_MAIN_TYPE_PUSH = "extra_start_main_type_push";
	String EXTRA_START_MAIN_TYPE_CHECKIN = "extra_start_main_type_checkin";
	String EXTRA_START_MAIN_PUSHMESSAGE = "extra_start_main_pushmessage";
	String EXTRA_START_MAIN_PUSHMESSAGE_PLAN_ID = "extra_start_main_pushmessage_plan_id";
	String EXTRA_START_MAIN_PUSHMESSAGE_PLAN_TYPE = "extra_start_main_pushmessage_plan_type";
//	String EXTRA_START_MAIN_PUSHMESSAGE_HUAWEI="extra_start_main_pushmessage_huawei";
	String EXTRA_PUSHMESSAGE_IS_OPPO="is_oppo";
    String ACTION_PUSH_PUSH="action_push_push";

	String EXTRA_START_MAIN_BOOT_IMG_ACTIVITY_PARAMS = "extra_start_main_boot_img_activity_params";

	String ACTION_NOTIFICATION_PRESSED="action_notification_pressed";
    String ACTION_IM_PUSH="action_im_push";

    String ACTION_DD_PRIASE_NUM = "action_dd_praise_num";
	String ACTION_DD_COMMENT_NUM = "action_dd_comment_num";
	String ACTION_DD_REDUCE_COMMENT_NUM = "action_dd_reduce_comment_num";
    String ACTION_CHANNEL_TOP = "action_channel_top";

	String ACTION_BAR_DEL_ARTICLE="ACTION_BAR_DEL_ARTICLE";
	String ACTION_BAR_UPDATE_COMMENT_PRAISE="ACTION_BAR_UPDATE_COMMENT_PRAISE";
	String EXTRA_BAR_ARTICLE_INFO ="EXTRA_BAR_ARTICLE_INFO";
	String EXTRA_BAR_DIGEST_ID="EXTRA_BAR_DIGEST_ID";
	String ACTION_CHANNEL_DETAIL_PRAISE_NUM="ACTION_CHANNEL_DETAIL_PRAISE_NUM";
	String ACTION_VOTE_INFO_CHANGE="ACTION_VOTE_INFO_CHANGE";
	String EXTRA_VOTE_ARTICLE_INFO ="EXTRA_VOTE_ARTICLE_INFO";
    String ACTION_FAVOR_POST = "ACTION_FAVOR_POST";
    String EXTRA_FAVOR_STATE_POST = "EXTRA_FAVOR_STATE_POST";
    String ACTION_FAVOR_EBOOK = "ACTION_FAVOR_EBOOK";
    String EXTRA_FAVOR_STATE_EBOOK = "EXTRA_FAVOR_STATE_EBOOK";
    String ACTION_FAVOR_PAPERBOOK = "ACTION_FAVOR_PAPERBOOK";
    String EXTRA_FAVOR_STATE_PAPERBOOK = "EXTRA_FAVOR_STATE_PAPERBOOK";
    String ACTION_READ_ACTIVITY_INFO_CHANGE="ACTION_READ_ACTIVITY_INFO_CHANGE";
    String EXTRA_READ_ACTIVITY_INFO="EXTRA_READ_ACTIVITY_INFO";
    String ACTION_HONOR_CHANGE="ACTION_HONOR_CHANGE";
    String ACTION_SHARE_SETTING_SUCCESS="ACTION_SHARE_SETTING_SUCCESS";
    String ACTION_SEARCH_SETTING_SUCCESS="ACTION_SEARCH_SETTING_SUCCESS";
	String ACTION_SHARE_SEARCH_SETTING_SUCCESS="ACTION_SHARE_SEARCH_SETTING_SUCCESS";
    String EXTRA_HONOR_CHANGE="EXTRA_HONOR_CHANGE";
	String ACTION_UPDATE_HOME_SYSTEM_MESSAGE_NUMBER="ACTION_UPDATE_HOME_SYSTEM_MESSAGE_NUMBER";//更新IM系统消息红点广播
	String ACTION_CANCEL_PAPER_ORDER_SUCCESS="ACTION_CANCEL_PAPER_ORDER_SUCCESS";//取消纸书订单成功广播


	// 电子书授权信息key
    String KEY_AUTHORITY_INFO = "authorityInfo";
	String KEY_DDTTS = "ddTTS";

    // 拉新code
    String INVITATION_CODE = "lxV670_1002_android";

    /**
     * 签名串 md5 加密需要的key ，该key将作为统一的数字后台key
     */
    String SignKey = "BCB85603ED5961A280FAD963EBABF4DE";

     String URL_DOWNLOAD_WEIXIN = "http://weixin.qq.com/";

}
