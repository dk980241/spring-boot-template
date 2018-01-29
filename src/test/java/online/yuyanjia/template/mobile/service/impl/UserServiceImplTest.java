package online.yuyanjia.template.mobile.service.impl;

import online.yuyanjia.template.mobile.mapper.UserDOMapper;
import online.yuyanjia.template.mobile.model.DO.UserDO;
import online.yuyanjia.template.mobile.util.BaseJunitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

public class UserServiceImplTest extends BaseJunitTest {

    @Autowired
    UserDOMapper userDOMapper;


    @Test
    public void register() throws Exception {
        System.out.println("==== == = = == = =");
        UserDO userDO = UserDO.builder()
                .mobile("18766590265")
                .password("12345")
                .salt("123123")
                .gmtCreate(new Date())
                .gmtModified(new Date())
                .build();
        userDOMapper.insert(userDO);
    }

    @Test
    public void login() throws Exception {
    }

    @Test
    public void logout() throws Exception {
    }

}