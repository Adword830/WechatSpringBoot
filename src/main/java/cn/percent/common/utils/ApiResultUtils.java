package cn.percent.common.utils;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhangpengju
 * Date: 2021/11/19
 * Time: 14:20
 * Description:
 */
@Slf4j
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
public class ApiResultUtils<T>{


    private int code;

    private T data;

    private String msg;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    public ApiResultUtils() {

    }

    public static ApiResultUtils result(boolean flag) {
        if (flag) {
            return ok();
        }
        return fail("");
    }

    public static ApiResultUtils result(ApiCodeUtils apiCode) {
        return result(apiCode, null);
    }

    public static <T> ApiResultUtils<T> result(ApiCodeUtils apiCode, T data) {
        return result(apiCode, null, data);
    }


    public static <T> ApiResultUtils<T> result(ApiCodeUtils apiCode, String msg, T data) {
        String message = apiCode.getMsg();
        if (StrUtil.isNotBlank(msg)) {
            message = msg;
        }
        return ApiResultUtils.<T>builder()
                .code(apiCode.getCode())
                .msg(message)
                .data(data)
                .time(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResultUtils<T> ok() {
        return ok(null);
    }

    public static <T> ApiResultUtils<T> ok(T data) {
        return result(ApiCodeUtils.SUCCESS, data);
    }

    public static <T> ApiResultUtils<T> ok(String msg) {
        return result(ApiCodeUtils.SUCCESS, msg,null);
    }

    public static ApiResultUtils ok(String key, Object value) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(key, value);
        return ok(map);
    }

    public static ApiResultUtils fail(ApiCodeUtils apiCode) {
        return result(apiCode, null);
    }

    public static ApiResultUtils fail(String msg) {
        return result(ApiCodeUtils.FAIL, msg, null);
    }

    public static ApiResultUtils fail(ApiCodeUtils apiCode, Object data) {
        if (ApiCodeUtils.SUCCESS == apiCode) {
            throw new RuntimeException("失败结果状态码不能为" + ApiCodeUtils.SUCCESS.getCode());
        }
        return result(apiCode, data);
    }

    public static ApiResultUtils fail(String key, Object value) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(key, value);
        return result(ApiCodeUtils.FAIL, map);
    }

    public static void write(HttpServletResponse response, String body) {
        write(response, null, body);
    }

    public static void write(HttpServletResponse response, Integer status, String body) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        if (status == null) {
            response.setStatus(HttpStatus.OK.value());
        } else {
            response.setStatus(status);
        }

        if (body != null) {
            PrintWriter writer = null;
            try {
                writer =response.getWriter();
                writer.write(body);
            } catch (IOException e) {
                log.error("response write error:", e);
            }finally {
                writer.flush();
                writer.close();
            }
        }
    }
}
