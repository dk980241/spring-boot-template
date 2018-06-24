package site.yuyanjia.template.website.service.impl;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.yuyanjia.template.ApplicationTest;
import site.yuyanjia.template.common.mapper.WebUserMapper;

import java.util.UUID;

public class UserServiceImplTest extends ApplicationTest{
    @Autowired
    WebUserMapper webUserMapper;

    @Test
    public void mybatisCache() {
        webUserMapper.selectByMobile("18766590265");
        webUserMapper.selectByUsername("yuyanjia");
    }

    @Test
    public void generaterPassword() {
        String salt = UUID.randomUUID().toString().trim().replaceAll("-", "").toUpperCase();
        String password = "123456";

        SimpleHash simpleHash = new SimpleHash("MD5", password, salt, 2);

        System.out.printf("原始密码 %s \n 密文密码 %s \n 盐 %s \n", password, simpleHash.toHex(), salt);
        System.err.println(simpleHash.toString());

    }

}