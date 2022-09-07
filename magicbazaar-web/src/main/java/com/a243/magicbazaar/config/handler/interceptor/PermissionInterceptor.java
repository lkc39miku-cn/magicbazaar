package com.a243.magicbazaar.config.handler.interceptor;

import com.a243.magicbazaar.dao.entity.Permission;
import com.a243.magicbazaar.dao.entity.RolePermission;
import com.a243.magicbazaar.dao.entity.StaffRole;
import com.a243.magicbazaar.dao.mapper.PermissionMapper;
import com.a243.magicbazaar.dao.mapper.RolePermissionMapper;
import com.a243.magicbazaar.dao.mapper.StaffRoleMapper;
import com.a243.magicbazaar.util.thread.StaffThreadLocal;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PermissionInterceptor implements HandlerInterceptor {
    private StaffRoleMapper staffRoleMapper;
    private RolePermissionMapper rolePermissionMapper;
    private PermissionMapper permissionMapper;

    @Autowired
    public void setStaffRoleMapper(StaffRoleMapper staffRoleMapper) {
        this.staffRoleMapper = staffRoleMapper;
    }

    @Autowired
    public void setRolePermissionMapper(RolePermissionMapper rolePermissionMapper) {
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Autowired
    public void setPermissionMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String url = request.getRequestURI();

        List<Long> roleId = staffRoleMapper.selectList(new QueryWrapper<StaffRole>().eq("staff_id", StaffThreadLocal.get().getId())).stream().map(StaffRole::getRoleId).collect(Collectors.toList());
        List<Long> permissionId = rolePermissionMapper.selectList(new QueryWrapper<RolePermission>().in("role_id", roleId)).stream().map(RolePermission::getPermissionId).collect(Collectors.toList());

        Permission permission = permissionMapper.selectOne(new QueryWrapper<Permission>().eq("path", url));
        if (permission != null) {
            if (!permissionId.contains(permission.getId())) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("success", false);
                jsonObject.put("code", 1);
                jsonObject.put("msg", "你没有该权限使用该功能");
                @Cleanup PrintWriter printWriter = response.getWriter();
                printWriter.append(jsonObject.toString());
                return false;
            }
        }

        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {

    }
}
