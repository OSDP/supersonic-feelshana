package com.tencent.supersonic.auth.authentication.service;

import com.tencent.supersonic.auth.api.authentication.config.AuthenticationConfig;
import com.tencent.supersonic.auth.api.authentication.constant.UserConstants;
import com.tencent.supersonic.auth.api.authentication.pojo.Organization;
import com.tencent.supersonic.auth.api.authentication.pojo.UserToken;
import com.tencent.supersonic.auth.api.authentication.request.UserReq;
import com.tencent.supersonic.auth.api.authentication.service.UserService;
import com.tencent.supersonic.auth.api.authentication.service.UserStrategy;
import com.tencent.supersonic.auth.api.authentication.utils.UserHolder;
import com.tencent.supersonic.auth.authentication.persistence.dataobject.UserDO;
import com.tencent.supersonic.auth.authentication.strategy.HttpHeaderUserStrategy;
import com.tencent.supersonic.auth.authentication.utils.ComponentFactory;
import com.tencent.supersonic.auth.authentication.utils.TokenService;
import com.tencent.supersonic.common.config.SystemConfig;
import com.tencent.supersonic.common.pojo.User;
import com.tencent.supersonic.common.service.SystemConfigService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService
        implements com.tencent.supersonic.auth.api.authentication.service.UserService {

    private SystemConfigService sysParameterService;

    @Autowired
    private AuthenticationConfig authenticationConfig;

    public UserService(SystemConfigService sysParameterService) {
        this.sysParameterService = sysParameterService;
    }

    @Override
    public User getCurrentUser(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        // 分析云登录校验参数是否存在
        if (ComponentFactory.getUserAdaptor().verifyParameters(httpServletRequest)) {
            return loginByAnalysisCloud(httpServletRequest);
        }
        User user = UserHolder.findUser(httpServletRequest, httpServletResponse);
        if (user != null) {
            SystemConfig systemConfig = sysParameterService.getSystemConfig();
            if (!CollectionUtils.isEmpty(systemConfig.getAdmins())
                    && systemConfig.getAdmins().contains(user.getName())) {
                user.setIsAdmin(1);
            }
        }
        return user;
    }

    @Override
    public List<String> getUserNames() {
        return ComponentFactory.getUserAdaptor().getUserNames();
    }

    @Override
    public List<User> getUserList() {
        return ComponentFactory.getUserAdaptor().getUserList();
    }

    @Override
    public Set<String> getUserAllOrgId(String userName) {
        return ComponentFactory.getUserAdaptor().getUserAllOrgId(userName);
    }

    @Override
    public List<User> getUserByOrg(String key) {
        return ComponentFactory.getUserAdaptor().getUserByOrg(key);
    }

    @Override
    public List<Organization> getOrganizationTree() {
        return ComponentFactory.getUserAdaptor().getOrganizationTree();
    }

    @Override
    public void register(UserReq userReq) {
        ComponentFactory.getUserAdaptor().register(userReq);
    }

    @Override
    public String login(UserReq userReq, HttpServletRequest request) {
        return ComponentFactory.getUserAdaptor().login(userReq, request);
    }

    @Override
    public String login(UserReq userReq, String appKey) {
        return ComponentFactory.getUserAdaptor().login(userReq, appKey);
    }

    @Override
    public User loginByAnalysisCloud(HttpServletRequest request) {
        return ComponentFactory.getUserAdaptor().loginByAnalysisCloud(request);
    }

    @Override
    public String getPassword(String userName) {
        return ComponentFactory.getUserAdaptor().getPassword(userName);
    }

    @Override
    public void resetPassword(String userName, String password, String newPassword) {
        ComponentFactory.getUserAdaptor().resetPassword(userName, password, newPassword);
    }

    @Override
    public UserToken generateToken(String name, String userName, long expireTime) {
        return ComponentFactory.getUserAdaptor().generateToken(name, userName, expireTime);
    }

    @Override
    public List<UserToken> getUserTokens(String userName) {
        return ComponentFactory.getUserAdaptor().getUserTokens(userName);
    }

    @Override
    public UserToken getUserToken(Long id) {
        return ComponentFactory.getUserAdaptor().getUserToken(id);
    }

    @Override
    public void deleteUserToken(Long id) {
        ComponentFactory.getUserAdaptor().deleteUserToken(id);
    }
}
