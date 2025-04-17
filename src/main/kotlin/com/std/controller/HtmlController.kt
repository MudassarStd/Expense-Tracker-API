package com.std.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class WebPageController {

    @GetMapping("/")
    fun home(): String {
        return "index" // Assumes resources/templates/index.html (with Thymeleaf) or static/index.html
    }
}
