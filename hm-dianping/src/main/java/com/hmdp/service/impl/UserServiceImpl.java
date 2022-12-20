package com.hmdp.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static com.hmdp.utils.SystemConstants.USER_NICK_NAME_PREFIX;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * 发送手机验证码
     * @param phone 手机号
     * @param session 为了保存到session里
     * @return 成功失败
     */
    @Override
    public Result sendCode(String phone, HttpSession session) {
        //1 校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            //1.1 如果不合法，返回错误信息
            return Result.fail("手机号非法");
        } // else
            //1.2 合法手机号，发送验证码
            String code = RandomUtil.randomNumbers(6);

        //2 保存验证码到session
        session.setAttribute("code", code);

        //3 发送验证码(因为是练习项目，所以只模拟)
        log.debug("验证码: " + code);

        //4 返回结果
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        //1 校验手机号
        String phone = loginForm.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            //1.1 如果不合法，返回错误信息
            return Result.fail("手机号非法");
        }

        //2 校验验证码
        String cacheCode = (String) session.getAttribute("code");
        String code = loginForm.getCode();
        if (cacheCode == null || !cacheCode.equals(code)) {
            //2.1 验证码错误，报错
            return Result.fail("验证码错误");
        } // else
            //2.2 验证码正确，查询用户
            User user = query().eq("phone", phone).one();

        //3 判断用户是否存在
        if (user == null) {
            //3.1 不存在则创建新用户
            user = createUserWithPhone(phone);
        }

        //4 保存用户到session
        session.setAttribute("user", user);

        return Result.ok();
    }

    /**
     * 创建新用户
     * @param phone 手机号
     * @return 用户对象
     */
    private User createUserWithPhone(String phone) {
        //创建用户,设置手机号，设置随机名称
        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomNumbers(8));

        //保存用户到数据库
        save(user);
        
        return user;
    }
}
