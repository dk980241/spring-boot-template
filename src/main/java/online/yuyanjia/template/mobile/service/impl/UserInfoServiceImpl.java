package online.yuyanjia.template.mobile.service.impl;

import online.yuyanjia.template.mobile.mapper.UserInfoDOMapper;
import online.yuyanjia.template.mobile.model.DO.UserInfoDO;
import online.yuyanjia.template.mobile.service.UserInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 用户操作实现类
 *
 * @author seer
 * @date 2018/1/29 16:21
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
    private static Logger LOGGER = LogManager.getLogger(UserInfoServiceImpl.class);

    @Autowired
    UserInfoDOMapper userInfoDOMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register() {

    }

    @Override
    public void login() {

    }

    @Override
    public void logout() {

    }


    @Override
    public UserInfoDO findUserInfoByMobile(String mobile) {
        UserInfoDO userInfoDO = userInfoDOMapper.selectOne(UserInfoDO.builder().mobile(mobile).build());
        return userInfoDO;
    }
}
