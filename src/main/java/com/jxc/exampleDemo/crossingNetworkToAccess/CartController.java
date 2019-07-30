package com.jxc.exampleDemo.crossingNetworkToAccess;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * crossingNetworkToAccess CORS
 *
 * js 代码
 * withCredentials:true 允许携带cookie
 *   axios.get("http://cart.pinyougou.com/cart/addCart?itemId=" + id + "&num=1",{withCredentials:true}).then(function(response){
 *                     if (response.data){
 *                         location.href = "http://cart.pinyougou.com";
 *                     } else {
 *                         alert("添加购物车失败")
 *                     }
 *                 })
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    /**
     * 方案一
     * CrossOrigin：允许跨域访问
     * origins : 允许跨域访问的url
     * allowCredentials ： 是否可以访问cookie
     *
     * */
    @GetMapping("/addCart")
    @CrossOrigin(origins = "http://search.pinyougou.com", allowCredentials = "true")
    public boolean addCart(Long itemId, Integer num, HttpServletResponse response){
        /** 方案二 */
        // 允许跨域访问
        response.setHeader("Access-Control-Allow-Origin","http://item.pinyougou.com");
        //允许访问 cookie
        response.setHeader("Access-Control-Allow-Credentials","true");
        return false;
    }


}
