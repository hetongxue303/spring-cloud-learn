package com.hetongxue.cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 *
 * @author hy
 * @version 1.0
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("{id}")
    public Long getById(@PathVariable Long id) {
        return id;
    }

}
