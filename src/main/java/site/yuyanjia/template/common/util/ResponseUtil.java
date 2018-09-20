package site.yuyanjia.template.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.util.ObjectUtils;
import site.yuyanjia.template.common.contant.ResponseEnum;
import site.yuyanjia.template.common.contant.ResultEnum;
import site.yuyanjia.template.common.dto.BaseResponseArrayDTO;
import site.yuyanjia.template.common.dto.BaseResponseDTO;

import java.util.List;

/**
 * 响应工具
 *
 * @author seer
 * @date 2018/9/1 10:29
 */
public class ResponseUtil {

    /**
     * 处理成功
     *
     * @return
     */
    public static String resultSuccess() {
        return baseResponse(ResponseEnum.SUCCESS, ResultEnum.处理成功, null, 0);
    }

    /**
     * 处理成功
     *
     * @param data
     * @return
     */
    public static String resultSuccess(List<?> data, int count) {
        return baseResponse(ResponseEnum.SUCCESS, ResultEnum.处理成功, data, count);
    }

    /**
     * 处理失败
     *
     * @param resultEnum
     * @return
     */
    public static String resultFailed(ResultEnum resultEnum) {
        return baseResponse(ResponseEnum.SUCCESS, resultEnum, null, 0);
    }

    /**
     * 处理失败
     *
     * @param resultEnum
     * @param data
     * @return
     */
    public static String resultFailed(ResultEnum resultEnum, List<Object> data, int count) {
        return baseResponse(ResponseEnum.SUCCESS, resultEnum, data, count);
    }

    /**
     * 响应失败
     *
     * @return
     */
    public static String responseFailed() {
        return baseResponse(ResponseEnum.FAILED, null, null, 0);
    }


    /**
     * 响应
     *
     * @param responseEnum
     * @param resultEnum
     * @param data
     * @return
     */
    private static String baseResponse(ResponseEnum responseEnum, ResultEnum resultEnum, List<?> data, int count) {
        BaseResponseArrayDTO baseResponseArrayDTO = new BaseResponseArrayDTO();
        baseResponseArrayDTO.setResponseCode(responseEnum.alias);
        baseResponseArrayDTO.setResponseMsg(responseEnum.toString());

        if (!ObjectUtils.isEmpty(resultEnum)) {
            baseResponseArrayDTO.setResultCode(resultEnum.alias);
            baseResponseArrayDTO.setResultMsg(resultEnum.toString());
        }

        baseResponseArrayDTO.setData(data);
        baseResponseArrayDTO.setAmount(count);

        SerializeConfig serializeConfig = FastJsonUtil.getSerializeConfig();
        return JSON.toJSONString(baseResponseArrayDTO, serializeConfig, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 扩展响应
     *
     * @param responseEnum
     * @param resultEnum
     * @param baseResponseDTO
     * @return
     */
    public static String extendResponse(ResponseEnum responseEnum, ResultEnum resultEnum, BaseResponseDTO baseResponseDTO) {
        if (!ObjectUtils.isEmpty(resultEnum)) {
            baseResponseDTO.setResultCode(resultEnum.alias);
            baseResponseDTO.setResultMsg(resultEnum.toString());
        }
        baseResponseDTO.setResponseCode(responseEnum.alias);
        baseResponseDTO.setResponseMsg(responseEnum.toString());

        SerializeConfig serializeConfig = FastJsonUtil.getSerializeConfig();
        return JSON.toJSONString(baseResponseDTO, serializeConfig, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 扩展响应
     * <p>
     * 满足需要将第三方的结果返回给调用方，但是无法获取第三方的返回枚举情景的处理
     *
     * @param responseEnum
     * @param resuleCode
     * @param resultMsg
     * @param baseResponseDTO
     * @return
     */
    public static String carefulResponse(ResponseEnum responseEnum, String resuleCode, String resultMsg, BaseResponseDTO baseResponseDTO) {
        baseResponseDTO.setResultCode(resuleCode);
        baseResponseDTO.setResultMsg(resultMsg);
        baseResponseDTO.setResponseCode(responseEnum.alias);
        baseResponseDTO.setResponseMsg(responseEnum.toString());

        SerializeConfig serializeConfig = FastJsonUtil.getSerializeConfig();
        return JSON.toJSONString(baseResponseDTO, serializeConfig, SerializerFeature.WriteMapNullValue);
    }
}

