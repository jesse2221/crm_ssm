package com.lsx.crm.settings.web.controller;

import com.lsx.crm.commons.constants.Constant;
import com.lsx.crm.commons.domain.ReturnObject;
import com.lsx.crm.commons.utils.DateUtils;
import com.lsx.crm.settings.domain.User;
import com.lsx.crm.settings.service.UserService;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request,
                        HttpServletResponse response, HttpSession session){
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        map.put("isRemPwd",isRemPwd);
        //调用service层
        User user=userService.queryUserByLoginActAndPwd(map);
        ReturnObject returnObject = new ReturnObject();
        if(user==null){
            //登陆失败，账号密码错误
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("登陆失败，账号密码错误");
        }else{//进一步判断用户是否合法
            //账号密码正确，判定用户是否过期
            if(DateUtils.formateDateTime(new Date()).compareTo(user.getExpireTime())>0){
                //登陆失败，用户已过期
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("登陆失败，用户已过期");
            }else if("0".equals(user.getLockState())){
                //登录失败，用户已被锁定
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("登陆失败，用户已被锁定");
            }else if(!user.getAllowIps().contains(request.getRemoteAddr())){
                //登录ip受限
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("登陆失败，登录ip受限");
            }else{
                //登陆成功
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                //把user放入到session中
                session.setAttribute(Constant.SESSION_USER,user);
                //如果需要记住密码，则往外写cookie
                if("true".equals(isRemPwd)){
                    Cookie c1 = new Cookie("loginAct", user.getLoginAct());
                    c1.setMaxAge(10*60*60*24);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", user.getLoginPwd());
                    c2.setMaxAge(10*60*60*24);
                    response.addCookie(c2);
                }else{
                    Cookie c1 = new Cookie("loginAct", "1");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", "1");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }
        return returnObject;
    }
    @RequestMapping("/settings/qx/user/loginout.do")
    public String loginout(HttpServletResponse response,HttpSession session){
        //清除cookie
        Cookie c1 = new Cookie("loginAct", "1");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd", "1");
        c2.setMaxAge(0);
        response.addCookie(c2);
        //销毁session
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping("/workbench/myInfo.do")
    public String myInfo(HttpSession session,HttpServletRequest request){
        User user= (User) session.getAttribute(Constant.SESSION_USER);
        User us1=userService.queryUserDetailById(user.getId());
        request.setAttribute("user",us1);
        return "workbench/index";
    }

    @RequestMapping("/settings/index.do")
    public String index(){
        return "settings/index";
    }

    @RequestMapping("/settings/dept/index.do")
    public String deptIndex(){
        return "settings/dept/index";
    }

    @RequestMapping("/settings/qx/index.do")
    public String qxIndex(){
        return "settings/qx/index";
    }

    @RequestMapping("/settings/qx/permission/index.do")
    public String permissionIndex(){
        return "settings/qx/user/index";
    }
}
