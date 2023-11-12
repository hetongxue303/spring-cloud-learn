package com.hetongxue.cloud.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hy
 * @version 1.0
 */
@FeignClient("provider-server")
//@FeignClient(name = "provider-server", path = "/test")
public interface ProviderFeign {

    @GetMapping("/test/hello")
    String test();

}
