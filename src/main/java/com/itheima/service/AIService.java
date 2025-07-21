package com.itheima.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class AIService {

    @Value("${ai.service.qwen.url}")
    private String qwenApiUrl;

    @Value("${ai.service.qwen.key}")
    private String qwenApiKey;

    @Autowired
    private RestTemplate restTemplate;

    public String getAIResponse(String question) {
        try {
            // 构建请求体 JSON 字符串
            String requestBody = String.format(
                    """
                    {
                      "model": "qwen-turbo",
                      "input": {
                        "messages": [
                          {"role": "user", "content": "%s"}
                        ]
                      },
                      "parameters": {
                        "result_format": "message",
                        "temperature": 0.7,
                        "max_tokens": 500
                      }
                    }
                    """, escapeJson(question) // 防止特殊字符引发 JSON 错误
            );

            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(qwenApiKey);

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // 发起 POST 请求
            ResponseEntity<String> response = restTemplate.postForEntity(qwenApiUrl, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String result = extractResponseFromJson(response.getBody());

                // ✅ 打印返回结果（开发日志）
                log.info("💬 Qwen 返回内容（发给前端）: {}", result);
                return result;
            } else {
                System.err.println("Qwen 返回非 200 状态码：" + response.getStatusCode());
                return "调用通义千问失败，状态码：" + response.getStatusCode();
            }
        } catch (Exception e) {
            System.err.println("Qwen 调用异常：" + e.getMessage());
            return "请求通义千问出错：" + e.getMessage();
        }
    }

    /**
     * 提取返回内容中的 assistant 回复字段
     */
    private String extractResponseFromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            return root.path("output").path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            return "解析通义千问响应出错：" + e.getMessage();
        }
    }

    /**
     * 转义特殊字符以避免 JSON 构建失败
     */
    private String escapeJson(String input) {
        return input.replace("\"", "\\\"").replace("\n", "\\n");
    }
}
