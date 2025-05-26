package com.yupi.springbootinit.manager;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.lkeap.v20240522.LkeapClient;
import com.tencentcloudapi.lkeap.v20240522.models.ChatCompletionsRequest;
import com.tencentcloudapi.lkeap.v20240522.models.ChatCompletionsResponse;
import com.tencentcloudapi.lkeap.v20240522.models.Message;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class AiManager {

    @Resource
    private LkeapClient deepSeekClient;

    public String doChat(String message) {
        final String SYSTEM_PROMPT = "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\n" +
                "分析需求：\n" +
                "{数据分析的需求或者目标}\n" +
                "原始数据：\n" +
                "{csv格式的原始数据，用,作为分隔符}\n" +
                "请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）" +
                "【【【【【\n" +
                "{前端 Echarts V5 的 option 配置对象js代码（输出 json 格式），合理地将数据进行可视化，不要生成任何多余的内容，比如注释}\n" +
                "【【【【【\n" +
                "{明确的数据分析结论，越详细越好，不要生成多余的注释}\n" +
                "【【【【【";


        try {
            // 实例化一个请求对象,每个接口都会对应一个request对象
            ChatCompletionsRequest req = new ChatCompletionsRequest();
            req.setModel("deepseek-v3");
            req.setStream(false);

            // 系统消息
            Message[] messages = new Message[2];
            Message message0 = new Message();
            message0.setRole("system");
            message0.setContent(SYSTEM_PROMPT);
            messages[0] = message0;

            Message message1 = new Message();
            message1.setRole("user");
            message1.setContent(message);
            messages[1] = message1;

            req.setMessages(messages);
            // 返回的resp是一个ChatCompletionsResponse的实例，与请求对象对应
            ChatCompletionsResponse resp = deepSeekClient.ChatCompletions(req);
            String content = resp.getChoices()[0].getMessage().getContent();
            // 输出json格式的字符串回包
            return content;
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            log.error("AI 对话失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI 生成错误");
        }
    }

//    /**
//     * 向 AI 发送请求
//     *
//     * @param isNeedTemplate 是否使用模板，进行 AI 生成； true 使用 、false 不使用 ，false 的情况是只想用 AI 不只是生成前端代码
//     * @param content        内容
//     *                       分析需求：
//     *                       分析网站用户的增长情况
//     *                       原始数据：
//     *                       日期,用户数
//     *                       1号,10
//     *                       2号,20
//     *                       3号,30
//     * @return AI 返回的内容
//     * '【【【【【'
//     * <p>
//     * '【【【【【'
//     */
//    public String sendMsgToXingHuo(boolean isNeedTemplate, String content) {
//        if (isNeedTemplate) {
//            // AI 生成问题的预设条件
//            String predefinedInformation = "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\n" +
//                    "分析需求：\n" +
//                    "{数据分析的需求或者目标}\n" +
//                    "原始数据：\n" +
//                    "{csv格式的原始数据，用,作为分隔符}\n" +
//                    "请根据这两部分内容，严格按照以下指定格式生成内容：" +
//                    "（**此外不要输出任何多余的开头、结尾、注释或额外文字**）" +
//                    "并且**所有分隔符必须保留**，尤其不要把 '【【【【【' 换成其他字符。这里是5个【\n" +
//                    "输出格式举例：\n" +
//                    "```\n" +
//                    "【【【【【\n" +
//                    "{前端 Echarts V5 的 option 配置对象 JSON 代码}\n" +
//                    "【【【【【\n" +
//                    "{明确的数据分析结论，越详细越好}\n" +
//                    "```\n" +
//                    "请严格按照上述格式进行输出（不允许多或少任何字符）。\n";
//
//            content = predefinedInformation + "\n" + content;
//        }
//        List<SparkMessage> messages = new ArrayList<>();
//        messages.add(SparkMessage.userContent(content));
//        // 构造请求
//        SparkRequest sparkRequest = SparkRequest.builder()
//                // 消息列表
//                .messages(messages)
//                // 模型回答的tokens的最大长度,非必传,取值为[1,4096],默认为2048
//                .maxTokens(2048)
//                // 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高 非必传,取值为[0,1],默认为0.5
//                .temperature(0.2)
//                // 指定请求版本
//                // 具体版本看官方文档 https://www.xfyun.cn/doc/spark/Web.html#_1-%E6%8E%A5%E5%8F%A3%E8%AF%B4%E6%98%8E
//                .apiVersion(SparkApiVersion.V4_0)
//                .build();
//        // 同步调用
//        SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
//        String responseContent = chatResponse.getContent();
//        log.info("星火 AI 返回的结果 {}", responseContent);
//        return responseContent;
//    }


}
