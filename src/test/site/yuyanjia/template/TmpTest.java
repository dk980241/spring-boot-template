package site.yuyanjia.template;

import org.junit.Test;
import site.yuyanjia.template.common.model.WebUserDO;

/**
 * TmpTest
 *
 * @author seer
 * @date 2018/6/26 17:15
 */
public class TmpTest {

    @Test
    public void t1() {
        Object obj = new WebUserDO();
        WebUserDO webUserDO = ((WebUserDO) obj);
    }
}
