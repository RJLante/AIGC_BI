package com.yupi.springbootinit.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

public class OpenAiAPI {

    public static void main(String[] args) {
        String url = "https://api.openai.com/v1/chat/completions";
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "");
        String json = JSONUtil.toJsonStr(hashMap);
        HttpRequest.post(url)
                .header("Authentication", "").body(json).execute().body();
    }
}
