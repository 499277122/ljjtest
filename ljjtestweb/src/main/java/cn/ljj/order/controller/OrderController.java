package cn.ljj.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {
    @RequestMapping("/test")
    public String test(Model model){
        model.addAttribute("title","123");
        return "/order/test";
    }
}
