package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.Staff;
import com.a243.magicbazaar.dao.entity.StaffRole;
import com.a243.magicbazaar.dao.mapper.StaffMapper;
import com.a243.magicbazaar.dao.mapper.StaffRoleMapper;
import com.a243.magicbazaar.service.StaffService;
import com.a243.magicbazaar.util.EmailUtil;
import com.a243.magicbazaar.util.PhoneUtil;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.StaffConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.StaffThreadLocal;
import com.a243.magicbazaar.util.token.JwtUtil;
import com.a243.magicbazaar.view.param.StaffParam;
import com.a243.magicbazaar.view.vo.StaffVo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {
    private final StaffMapper staffMapper;
    private final StaffConvert staffConvert;
    private RedisTemplate<String, String> redisTemplate;
    private EmailUtil emailUtil;

    private StaffRoleMapper staffRoleMapper;

    @Autowired
    public StaffServiceImpl(StaffMapper staffMapper,
                            StaffConvert staffConvert) {
        this.staffMapper = staffMapper;
        this.staffConvert = staffConvert;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setEmailUtil(EmailUtil emailUtil) {
        this.emailUtil = emailUtil;
    }

    @Autowired
    public void setStaffRoleMapper(StaffRoleMapper staffRoleMapper) {
        this.staffRoleMapper = staffRoleMapper;
    }

    @Override
    public <T> Result<T> findStaffByNameAndPassword(Staff staff, String autoLogin, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<Staff> staffQueryWrapper = new QueryWrapper<Staff>()
                .eq("username", staff.getUsername())
                .eq("password", staff.getPassword());
        Staff one = staffMapper.selectOne(staffQueryWrapper);

        if (one == null) {
            return Result.fail(BusinessCode.USERNAME_OR_PASSWORD_ERROR);
        }

        if(one.getPublishStatus() == 1) {
            return Result.fail("该用户已被禁用");
        }

        if (one.getDeleteStatus() == 1) {
            return Result.fail("该用户已被删除");
        }

        if (!StrUtil.isBlank(autoLogin)) {
            if ("on".equals(autoLogin)) {
                String token = JwtUtil.createToken(one.getId());
                redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(one), 1, TimeUnit.DAYS);

                Cookie cookie = new Cookie("token", token);
                cookie.setMaxAge(60 * 60 * 24);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        } else {
            String token = JwtUtil.createToken(one.getId());
            redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(one), 3, TimeUnit.HOURS);

            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 3);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return Result.ok(BusinessCode.LOGIN_SUCCESS);
    }

    @Override
    public Staff checkStaff(String token) {
        if (StrUtil.isBlank(token)) {
            return null;
        }

        Map<String, Object> map = JwtUtil.checkToken(token);
        if (map == null) {
            return null;
        }

        String staffJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StrUtil.isBlank(staffJson)) {
            return null;
        }

        return JSON.parseObject(staffJson, Staff.class);
    }

    @Override
    public <T> Result<T> logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        redisTemplate.delete("TOKEN_" + token);
        return Result.ok(BusinessCode.LOGOUT_SUCCESS);
    }

    @Override
    public Result<List<StaffVo>> list(StaffParam staffParam) {
        if (staffParam.getPageParam() != null) {
            IPage<Staff> iPage = staffMapper.selectPage(new Page<>(staffParam.getPageParam().getPage(), staffParam.getPageParam().getPageSize()), new QueryWrapper<Staff>()
                    .like(!StrUtil.isBlank(staffParam.getUsername()), "username", staffParam.getUsername())
                    .like(!StrUtil.isBlank(staffParam.getPhone()), "phone", staffParam.getPhone())
                    .like(!StrUtil.isBlank(staffParam.getEmail()), "email", staffParam.getEmail())
                    .eq(!StrUtil.isBlank(staffParam.getPublishStatus()), "publish_status", staffParam.getPublishStatus())
                    .eq(!StrUtil.isBlank(staffParam.getDeleteStatus()), "delete_status", staffParam.getDeleteStatus()));

            return Result.ok(Math.toIntExact(iPage.getTotal()), staffConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(staffConvert.convert(staffMapper.selectList(new QueryWrapper<>())));
        }
    }

    @Override
    public void phoneCode(HttpServletResponse response, String phone) {
        PhoneUtil.phoneCode(response, phone);
    }

    @Override
    public void emailCode(HttpServletResponse response, String email) {
        emailUtil.emailCode(response, email);
    }

    @Override
    public <T> Result<T> add(HttpServletRequest request, HttpServletResponse response, StaffParam staffParam) {
        Cookie[] cookies = request.getCookies();

        String phone = null;
        String email = null;

        if (cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("phoneCode".equals(cookie.getName())) {
                    phone = cookie.getValue();
                }
                if ("emailCode".equals(cookie.getName())) {
                    email = cookie.getValue();
                }
            }
        }

        if (StrUtil.isBlank(phone) || StrUtil.isBlank(email)) {
            return Result.fail("验证码已过期");
        }

        if (!phone.equals(staffParam.getPhoneCode())) {
            return Result.fail("手机验证码不正确");
        }

        if (!email.equals(staffParam.getEmailCode())) {
            return Result.fail("邮箱验证码不正确");
        }

        int i = staffMapper.insert(new Staff().setUsername(staffParam.getUsername()).setPhone(staffParam.getPhone())
                .setPhone(staffParam.getPhone()).setEmail(staffParam.getEmail()).setDeleteStatus(0).setPublishStatus(0));

        if (i == 1) {
            for (Cookie cookie : cookies) {
                if ("phoneCode".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
                if ("emailCode".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
            return Result.ok(BusinessCode.ADD_SUCCESS);
        } else {
            return Result.fail(BusinessCode.ADD_ERROR);
        }
    }

    @Override
    public <T> Result<T> update(HttpServletRequest request, HttpServletResponse response, StaffParam staffParam) {
        Staff staff = staffMapper.selectById(staffParam.getId());

        // 检测手机号是否和原来一样
        Staff selectOne = staffMapper.selectOne(new QueryWrapper<Staff>().eq("phone", staffParam.getPhone())
                .ne("phone", staff.getPhone()));

        // 说明手机号唯一
        if (selectOne == null) {
            Cookie[] cookies = request.getCookies();

            String phone = null;
            String email = null;

            if (cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if ("phoneCode".equals(cookie.getName())) {
                        phone = cookie.getValue();
                    }
                    if ("emailCode".equals(cookie.getName())) {
                        email = cookie.getValue();
                    }
                }
            }

            if (StrUtil.isBlank(phone) || StrUtil.isBlank(email)) {
                return Result.fail("验证码已过期");
            }

            if (!phone.equals(staffParam.getPhoneCode())) {
                return Result.fail("手机验证码不正确");
            }

            if (!email.equals(staffParam.getEmailCode())) {
                return Result.fail("邮箱验证码不正确");
            }

            int i = staffMapper.updateById(new Staff().setUsername(staffParam.getUsername()).setPhone(staffParam.getPhone())
                    .setPhone(staffParam.getPhone()).setEmail(staffParam.getEmail()).setId(Long.parseLong(staffParam.getId())));

            if (i == 1) {
                for (Cookie cookie : cookies) {
                    if ("phoneCode".equals(cookie.getName())) {
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                    if ("emailCode".equals(cookie.getName())) {
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                }
                return Result.ok(BusinessCode.ADD_SUCCESS);
            } else {
                return Result.fail(BusinessCode.ADD_ERROR);
            }
        } else {
            return Result.fail("手机号已被占用");
        }
    }

    @Override
    public <T> Result<T> on(StaffParam staffParam) {
        int i = staffMapper.updateById(new Staff().setId(Long.parseLong(staffParam.getId())).setPublishStatus(0));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> off(StaffParam staffParam) {
        int i = staffMapper.updateById(new Staff().setId(Long.parseLong(staffParam.getId())).setPublishStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> delete(StaffParam staffParam) {
        int i = staffMapper.updateById(new Staff().setId(Long.parseLong(staffParam.getId())).setDeleteStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.DELETE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.DELETE_ERROR);
        }
    }

    @Override
    public <T> Result<T> role(StaffParam staffParam) {
        List<String> id = Arrays.stream(staffParam.getIdList().split(",")).toList();
        List<StaffRole> staffRoleList = staffRoleMapper.selectList(new QueryWrapper<StaffRole>().eq("staff_id ", staffParam.getId()));
        int i = 0;
        for (StaffRole staffRole : staffRoleList) {
            i += staffRoleMapper.deleteById(staffRole);
        }
        if (i == staffRoleList.size()) {
            i = 0;
            for (String string : id) {
                i += staffRoleMapper.insert(new StaffRole().setStaffId(Long.parseLong(staffParam.getId())).setRoleId(Long.parseLong(string)).setDeleteStatus(0));
            }
            if (i == id.size()) {
                return Result.ok(BusinessCode.UPDATE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.UPDATE_ERROR);
            }
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public Result<StaffVo> info() {
        return Result.ok(staffConvert.convert(StaffThreadLocal.get()));
    }

    @Override
    public <T> Result<T> checkName(StaffParam staffParam) {
        Staff username = staffMapper.selectOne(new QueryWrapper<Staff>().eq("username", staffParam.getUsername()));
        if (username != null) {
            return Result.fail("用户名重复");
        } else {
            return Result.ok();
        }
    }

    @Override
    public <T> Result<T> emailCheck(StaffParam staffParam) {
        Staff email = staffMapper.selectOne(new QueryWrapper<Staff>().eq("email", staffParam.getEmail()));
        if (email != null) {
            return Result.fail("邮箱重复");
        } else {
            return Result.ok();
        }
    }

    @Override
    public <T> Result<T> checkPassword(StaffParam staffParam) {
        Staff staff = staffMapper.selectOne(new QueryWrapper<Staff>().eq("id", StaffThreadLocal.get().getId()).eq("password", staffParam.getPassword()));
        if (staff != null) {
            return Result.ok();
        } else {
            return Result.fail("输入的原密码有误");
        }
    }

    @Override
    public <T> Result<T> changePassword(StaffParam staffParam) {
        int i = staffMapper.updateById(new Staff().setId(StaffThreadLocal.get().getId()).setPassword(staffParam.getPassword()));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }
}
