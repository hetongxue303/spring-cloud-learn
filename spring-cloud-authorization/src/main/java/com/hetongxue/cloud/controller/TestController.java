package com.hetongxue.cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 接口
 *
 * @author hy
 * @version 1.0
 */
@RestController
public class TestController {

    @GetMapping("hello")
    public String hello() {
        return "hello word";
    }

}
