package online.yuyanjia.template.mobile.util;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import online.yuyanjia.template.mobile.mapper.UserInfoDOMapper;
import online.yuyanjia.template.mobile.model.DO.UserInfoDO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 测试demo
 *
 * @author seer
 * @date 2018/1/31 14:35
 */
public class DemoTest extends BaseJunitTest {
    private static Logger LOGGER = LogManager.getLogger(DemoTest.class);

    @Autowired
    UserInfoDOMapper userInfoDOMapper;

    @Test
    public void test_mapper() {
        List<UserInfoDO> list = userInfoDOMapper.selectAll();
        list.forEach(userInfoDO -> LOGGER.error(userInfoDO.toString()));
    }

    @Test
    public void test_pagehelper() {
        PageHelper.startPage(0, 0);
        List<UserInfoDO> list = userInfoDOMapper.selectAll();
        PageInfo<UserInfoDO> pageInfo = new PageInfo<UserInfoDO>(list);
        LOGGER.warn(pageInfo.getList());
    }
}
