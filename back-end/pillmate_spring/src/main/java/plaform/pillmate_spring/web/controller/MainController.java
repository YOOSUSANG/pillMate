package plaform.pillmate_spring.web.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Slf4j
@Controller
public class MainController {
//
//
//    @GetMapping("/")
//    @ResponseBody
//    public String mainApi() {
//        return "main route";
//    }



    @RequestMapping({"/", "/generalsearch", "/imagesearch", "/imagesearch/detail", "/record",
            "/generalSearch/list", "/pill/detail", "/record/detail", "/Login",
            "/mypage", "/mypage/detail", "/map", "/order/success", "/pill_store",
            "/mypage/order_list", "/mypage/refund_list", "/orders/basket", "/**"})
    public String redirect() {
        return "forward:/index.html";
    }

}
