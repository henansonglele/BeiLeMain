package com.dangdang.gx.ui.utils;

import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liuboyu on 2014/12/2.
 */
public class URLUtil {

	public static Map<String, String> splitQuery(String urlStr) {
		Map<String, String> queryPairs = new LinkedHashMap<String, String>();
		try {
			URL url = new URL(urlStr);
			String query = url.getQuery();
			String[] pairs = query.split("&");
			if (null != pairs && pairs.length > 0) {
				for (String pair : pairs) {
					if (!StringUtil.isEmpty(pair)) {
						int idx = pair.indexOf("=");
						queryPairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
					}

				}
			}
		} catch (Exception e) {
		}
		return queryPairs;
	}
}