package com.itheima.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class AIDeepseekService {

    private static final Logger log = LoggerFactory.getLogger(AIDeepseekService.class);

    @Value("${ai.model.deepseek.url}")
    private String deepSeekApiUrl;

    @Value("${ai.model.deepseek.model.name:deepseek-coder:8b}")
    private String modelName = "deepseek-coder:8b";

    @Autowired
    private RestTemplate restTemplate;

    public String getAIResponse(String question) {
        log.info("开始调用DeepSeek-R1模型进行回答, 问题: {}", question);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            String requestBody = createRequestBody(question);
            log.debug("请求体: {}", requestBody);

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<AIResponse> response = restTemplate.exchange(
                    deepSeekApiUrl,
                    HttpMethod.POST,
                    request,
                    AIResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
                String result = response.getBody().getChoices()[0].getMessage().getContent();
                log.info("DeepSeek模型返回结果: {}", result);
                return result;
            } else {
                log.error("调用失败，状态码：{}, 响应体: {}",
                        response.getStatusCodeValue(),
                        response.getBody() != null ? response.getBody().toString() : "无响应体");
                return "调用失败，状态码: " + response.getStatusCodeValue();
            }
        } catch (Exception e) {
            log.error("调用DeepSeek API异常", e);
            return "调用异常: " + e.getMessage();
        }
    }

    private String createRequestBody(String question) {
        return String.format(
                """
                {
                  "model": "%s",
                  "messages": [
                    {"role": "user", "content": "%s"}
                  ],
                  "parameters": {
                    "temperature": 0.7,
                    "max_tokens": 500
                  }
                }""",
                modelName, escapeJson(question)
        );
    }

    private String escapeJson(String input) {
        if (input == null || input.isEmpty()) {
            return "\"\"";
        }

        StringBuilder escaped = new StringBuilder();
        escaped.append('"');
        for (char c : input.toCharArray()) {
            switch (c) {
                case '"':
                    escaped.append("\\\"");
                    break;
                case '\\':
                    escaped.append("\\\\");
                    break;
                case '\b':
                    escaped.append("\\b");
                    break;
                case '\f':
                    escaped.append("\\f");
                    break;
                case '\n':
                    escaped.append("\\n");
                    break;
                case '\r':
                    escaped.append("\\r");
                    break;
                case '\t':
                    escaped.append("\\t");
                    break;
                default:
                    if (c < 32 || c > 126) {
                        escaped.append(String.format("\\u%04x", (int) c));
                    } else {
                        escaped.append(c);
                    }
            }
        }
        escaped.append('"');
        return escaped.toString();
    }

    private String extractResponseFromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            AIResponse response = mapper.readValue(json, AIResponse.class);
            return response.getChoices()[0].getMessage().getContent();
        } catch (Exception e) {
            log.error("解析响应失败", e);
            throw new RuntimeException("解析响应失败：" + e.getMessage());
        }
    }

    private static class AIResponse {
        private Choice[] choices;

        public Choice[] getChoices() {
            return choices;
        }

        public void setChoices(Choice[] choices) {
            this.choices = choices;
        }
    }

    private static class Choice {
        private Message message;

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }
    }

    private static class Message {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}