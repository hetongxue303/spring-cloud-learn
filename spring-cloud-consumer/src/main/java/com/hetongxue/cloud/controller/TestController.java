package com.hetongxue.cloud.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 测试接口
 *
 * @author hy
 * @version 1.0
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("hello")
    public String test() {
        return "hello consumer！";
    }

    @GetMapping("res")
    public String testRest() {
        String url = "http://provider-server/test/hello";
        return restTemplate.getForObject(url, String.class);
    }

}