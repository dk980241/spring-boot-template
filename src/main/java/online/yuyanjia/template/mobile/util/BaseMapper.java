package online.yuyanjia.template.mobile.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 通用mapper基类
 *
 * @author seer
 * @date 2018/1/30 14:35
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
    /**
     * Mapper：基本的增、删、改、查方法
     * MySqlMapper：针对MySQL的额外补充接口，支持批量插入
     *
     * 此接口不能被扫描到
     */
}
