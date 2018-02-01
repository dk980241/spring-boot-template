package online.yuyanjia.template.mobile.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;

/**
 * DO层基类
 *
 * @author seer
 * @date 2018/1/29 16:09
 */
@Data
@EqualsAndHashCode
public class BaseDO {

    /**
     * 当前页，从1开始
     */
    @Transient
    protected Integer pageNum;

    /**
     * 分页数量
     */
    @Transient
    protected Integer pageSize;
}
