package com.yupi.springbootinit.manager;

import io.github.briqt.spark4j.SparkClient;
import io.github.briqt.spark4j.constant.SparkApiVersion;
import io.github.briqt.spark4j.model.SparkMessage;
import io.github.briqt.spark4j.model.SparkSyncChatResponse;
import io.github.briqt.spark4j.model.request.SparkRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OldAiManager {

    @Resource
    private SparkClient sparkClient;


    /**
     * 向 AI 发送请求
     *
     * @param isNeedTemplate 是否使用模板，进行 AI 生成； true 使用 、false 不使用 ，false 的情况是只想用 AI 不只是生成前端代码
     * @param content        内容
     *                       分析需求：
     *                       分析网站用户的增长情况
     *                       原始数据：
     *                       日期,用户数
     *                       1号,10
     *                       2号,20
     *                       3号,30
     * @return AI 返回的内容
     * '【【【【【'
     * <p>
     * '【【【【【'
     */
    public String sendMsgToXingHuo(boolean isNeedTemplate, String content) {
        if (isNeedTemplate) {
            // AI 生成问题的预设条件
            String predefinedInformation = "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\n" +
                    "分析需求：\n" +
                    "{数据分析的需求或者目标}\n" +
                    "原始数据：\n" +
                    "{csv格式的原始数据，用,作为分隔符}\n" +
                    "请根据这两部分内容，严格按照以下指定格式生成内容：" +
                    "（**此外不要输出任何多余的开头、结尾、注释或额外文字**）" +
                    "并且**所有分隔符必须保留**，尤其不要把 '【【【【【' 换成其他字符。这里是5个【\n" +
                    "输出格式举例：\n" +
                    "```\n" +
                    "【【【【【\n" +
                    "{前端 Echarts V5 的 option 配置对象 JSON 代码}\n" +
                    "【【【【【\n" +
                    "{明确的数据分析结论，越详细越好}\n" +
                    "```\n" +
                    "请严格按照上述格式进行输出（不允许多或少任何字符）。\n";

            content = predefinedInformation + "\n" + content;
        }
        List<SparkMessage> messages = new ArrayList<>();
        messages.add(SparkMessage.userContent(content));
        // 构造请求
        SparkRequest sparkRequest = SparkRequest.builder()
                // 消息列表
                .messages(messages)
                // 模型回答的tokens的最大长度,非必传,取值为[1,4096],默认为2048
                .maxTokens(2048)
                // 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高 非必传,取值为[0,1],默认为0.5
                .temperature(0.2)
                // 指定请求版本
                // 具体版本看官方文档 https://www.xfyun.cn/doc/spark/Web.html#_1-%E6%8E%A5%E5%8F%A3%E8%AF%B4%E6%98%8E
                .apiVersion(SparkApiVersion.V4_0)
                .build();
        // 同步调用
        SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
        String responseContent = chatResponse.getContent();
        log.info("星火 AI 返回的结果 {}", responseContent);
        return responseContent;
    }
}
