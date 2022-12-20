package com.hmdp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IUserService extends IService<User> {

    /**
     * 发送手机验证码
     * @param phone 手机号
     * @param session 为了保存到session里
     * @return 成功失败
     */
    Result sendCode(String phone, HttpSession session);

    /**
     * 登录
     * @param loginForm 登录DTO
     * @param session session
     * @return 成功失败
     */
    Result login(LoginFormDTO loginForm, HttpSession session);
}
