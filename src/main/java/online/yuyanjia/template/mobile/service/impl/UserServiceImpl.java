package online.yuyanjia.template.mobile.service.impl;

import online.yuyanjia.template.mobile.mapper.UserDOMapper;
import online.yuyanjia.template.mobile.model.DO.UserDO;
import online.yuyanjia.template.mobile.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 用户操作实现类
 *
 * @author seer
 * @date 2018/1/29 16:21
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    private static Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    UserDOMapper userDOMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register() {
        UserDO userDO = UserDO.builder()
                .mobile("18766590265")
                .password("12345")
                .salt("123123")
                .gmtCreate(new Date())
                .gmtModified(new Date())
                .build();
        userDOMapper.insert(userDO);
    }

    @Override
    public void login() {

    }

    @Override
    public void logout() {

    }
}
