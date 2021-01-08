package com.dangdang.gx.ui.eventBus;

import com.alibaba.fastjson.JSON;
import com.dangdang.gx.ui.flutterbase.DDFlutterManager;
import java.util.HashMap;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;

public class DDEventBus {
    public static void post(Object event) {
        EventBus.getDefault().post(event);
        // 发送到flutter
        Map<String, String> params = new HashMap<>();
        params.put("name", event.getClass().getSimpleName());
        params.put("event", JSON.toJSONString(event));
        DDFlutterManager.invokeMethod("ddreader/event_bus_post", params, null);
    }
}
