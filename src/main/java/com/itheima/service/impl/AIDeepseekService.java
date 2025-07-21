package com.itheima.service.impl;

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
public class AIDeepseekService {

    @Value("${ai.service.deepseek.url}")
    private String deepseekApiUrl;

    @Value("${ai.service.deepseek.model}")
    private String deepseekModel;

    @Autowired
    private RestTemplate restTemplate;

    public String getAIResponse(String question) {
        try {
            // 构建DeepSeek模型的请求体
            String requestBody = String.format(
                    """
                    {
                      "model": "%s",
                      "prompt": "%s",
                      "stream": false,
                      "options": {
                        "temperature": 0.7,
                        "max_tokens": 500
                      }
                    }
                    """, deepseekModel, escapeJson(question)
            );

            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // 发起POST请求到DeepSeek模型
            ResponseEntity<String> response = restTemplate.postForEntity(deepseekApiUrl, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String result = extractResponseFromJson(response.getBody());
                log.info("💬 DeepSeek 返回内容: {}", result);
                return result;
            } else {
                log.error("DeepSeek 返回非200状态码：{}", response.getStatusCode());
                return "调用DeepSeek失败，状态码：" + response.getStatusCode();
            }
        } catch (Exception e) {
            log.error("DeepSeek调用异常：{}", e.getMessage(), e);
            return "请求DeepSeek出错：" + e.getMessage();
        }
    }

    /**
     * 提取DeepSeek返回内容中的回复字段
     */
    private String extractResponseFromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            return root.path("response").asText();
        } catch (Exception e) {
            log.error("解析DeepSeek响应出错：{}", e.getMessage(), e);
            return "解析DeepSeek响应出错：" + e.getMessage();
        }
    }

    /**
     * 转义特殊字符以避免JSON构建失败
     */
    private String escapeJson(String input) {
        return input.replace("\"", "\\\"").replace("\n", "\\n");
    }
}
