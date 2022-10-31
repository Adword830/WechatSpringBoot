package cn.percent.common.utils;

/**
 * @author: zhangpengju
 * Date: 2021/11/19
 * Time: 14:23
 * Description:
 */
public enum ApiCodeUtils {

    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    UNAUTHORIZED(401, "非法访问"),

    FORBIDDEN(403, "没有权限"),

    NOT_FOUND(404, "你请求的路径不存在"),

    METHOD_NOT_ALLOWED(406, "不支持当前请求方法"),

    NOT_ACCEPTABLE(406, "参数解析失败"),

    MISSING_PARAMETER(407, "缺少请求参数"),

    UNSUPPORTED_MEDIA_TYPE(415, "不支持当前媒体类型"),

    EXPECTATION_FAILED(417, "请求参数异常"),

    EXPECTATION_CHECK_DEPARTMENT(418, "街道异常"),

    EXPECTATION_CHECK_MESSAGE(419, "提示"),

    FAIL(500, "系统异常"),

    BAIDU_API_FAIL(5001, "百度API请求失败"),
    PAY_FAIL(5008, "支付API请求失败"),

    PARAMETER_EXCEPTION(5002, "请求参数校验异常"),
    API_EXPIRED(5007, "接口链接失效"),
    API_SIGN_ERROR(5007, "接口签名校验失败"),


    /**
     * {@code 401 Token过期}
     */
    JWT_LOGIN_FAILURE(1001, "用户名或密码错误"),

    JWT_USER_NOT_EXISTS(1002, "用户名或密码错误"),

    JWT_EXPIRED(1003, "Token过期"),

    JWT_PARSE(1004, "Token解析失败"),

    /**
     * 登录错误次数超限
     */
    JWT_LOGIN_LIMIT(1005, "用户名或密码错误，1小时后再试"),

    JWT_CAPTCHA_ERROR(1006, "验证码错误"),

    ROLE_EXISTS(1100, "角色已经存在"),
    ROLE_NOT_EXISTS(1101, "角色不存在"),
    ROLE_FORBIDDEN_DEL(1102, "此角色不允许删除"),
    ROLE_FORBIDDEN_EDIT(1102, "此角色不允许修改"),

    TOOL_SERVER(5000, "tool无服务"),
    QUOTA_DELETE_FORBIDEEN(5002, "此配额还未抽样或未完成数据同步，请稍后再试"),


    FILE_UPLOAD_FIELD(6000, "file upload validate field error,error file type"),
    FILE_UPLOAD_FIELD_ERROR_TEMPLATE(6000, "file upload validate field error,error template"),
    ERROR_REQUEST_PARAM(6001, "error request param"),

    /**
     * 参数格式验证
     */
    ERROR_CLIENTID_EMPTY(7003, "请选择客户"),
    ERROR_QUANTITY_EMPTY(7004, "请填写推送人数"),
    ERROR_CONDITION_SAME(7005, "导入条件和导出结果不能相同"),
    ERROR_KEYWORD_CAT(7006, "关键字和第三级分类id不能同时为空!"),

    /**
     * 上传文件验证
     */
    FILE_ERROR_EXISTS(8000, "文件为空"),
    FILE_ERROR_EMPTY(8001, "上传的文件没有数据"),
    FILE_ERROR_TYPE(8002, "上传的文件格式错误"),
    STORE_ERROR_EMPTY(8003, "创建失败"),
    FILE_ERROR_LIMIT(8004, "每次上传最多1000条数据"),
    FILE_ERROR_NUMBER(8005, "请检查文件中的数据是否符合要求"),
    FILE_ERROR_ISNUMBER(8006, "请检查文件中的数据是否为正常的数值"),
    FILE_ERROR_ISSTRING(8007, "请检查文件中的数据是否为数字类型"),
    FILE_TOTAL(8008, "上传最多10000条"),
    FILE_HEAD(8009, "上传文件头或文件格式不正确"),
    FILE_LIMIT(8010, "上传条数最多%d条"),
    FILE_HEAD_TEMPLATE(8011, "上传文件头和模板不一致"),
    FILE_NOT_EXIST(8012, "文件不存在"),
    /**
     * 验证错误
     */
    SIGN_ERROR(9001, "验证失败"),
    PROJECT_EMPTY(9002, "未查询到该项目"),
    MQ_SEND_ERROR(9003, "样本同步失败"),
    MEMBER_EMPTY(9004, "未查询到该用户"),
    MEMBER_EMPTY_ERROR(9005, "用户同步失败"),
    TASK_EMPTY(9006, "未查询到该任务"),
    EVENT_EMPTY(9007, "事件ID不可为空"),
    TASK_NO_CLIENT(9008, "必选选择一个客户端"),
    BENEFIT_TIME_ERROR(9009, "翻倍券倍数必须为1"),


    STAFF_NOT_EXISTS(9300, "职员不存在"),
    STAFF_CHECK_PWD(9301, "两次密码不一致"),
    STAFF_OLD_PWD(9302, "旧密码不正确"),
    STAFF_MOBILE_REPEAT(9303, "手机号重复"),
    STAFF_EMAIL_REPEAT(9304, "邮箱重复"),
//    STAFF_ID_CARD_REPEAT(9305, "身份证号重复"),


    ROLE_REPEAT(9100, "角色名称重复"),
    DEPARTMENT_REPEAT(9200, "部门名称重复"),
    TAG_REPEAT(9100, "标签名称重复"),
    TAG_TASK_REPEAT(9100, "该任务下标签%d名称重复"),
    TAG_NOT_EXIST(9101, "标签不存在"),
    URL_TOKEN(9300, "令牌不存在"),
    URL_FILE(9301, "文件链接令牌不存在"),
    CLIENT_REPEAT(9100, "客户名称重复"),
    CLIENT_STAFF_REPEAT(9100, "客户职员名称重复"),
    AGENT_REPEAT(9100, "代理名称重复"),
    AGENT_ALIAS_REPEAT(9100, "代理别名重复"),
    AGENT_STAFF_REPEAT(9100, "代理职员名称重复"),

    BENEFIT_TIMES(9400, "翻倍券倍数不能为0，请在0-1之间选择"),
    BENEFIT_TEMPLATE(9402, "当选抽奖时,抽奖模板不能为空!"),
    GIFT_OVER_NUM(9403, "转盘模板下的奖品,最多有8个!"),
    GIFT_NINE_OVER_NUM(9403, "九宫格模板下的奖品,有且仅能有10个!"),
    GIFT_PROBABILITY(9404, "所有奖品的中奖概率和必须为100现在是%d"),
    GIFT_FORBIDDEN(9405, "目前抽奖奖品支持金币"),

    PRIZE_MOBILE_VALUE(9401, "奖品值请在10,20,30间选择!"),

    USER_CREAT_TIMES(9500, "起止时间不能大于等于截止时间"),
    USER_CREAT_TIMES_INTERVAL(9501, "最小时长要小于最大时长"),
    USER_CREAT_TIMES_CHECK(9502, "指定时间段格式不正确"),
    USER_CREAT_CODE_CHECK(9503, "会员转换失败"),
    USER_MOBILE_REPEAT(9504, "手机号已存在"),
    USER_ID_CARD_REPEAT(9505, "身份证号已存在"),
    USER_ID_CARD_EMPTY(9506, "修改验证为已验证时,身份证号不可为空"),


    CONFIG_SETTING(9600, "设置值必须是数值"),
    CONFIG_MOBILE_SETTING(9601, "请查看安卓或者IOS设置值是否符合规范"),
    CONFIG_USER_SETTING(9602, "请查看用户协议版本设置值是否符合规范"),
    EXCHANGE_STATE_REOEAT(9603, "请勿重复审核"),
    EXCHANGE_BOUND_WECHAT(9604, "此会员未绑定微信"),
    EXCHANGE_BOUND_ALIPAY(9605, "此会员未绑定支付宝"),
    EXCHANGE_PRIZE(9606, "兑换的奖品不存在，无法通过审核"),
    EXCHANGE_ID_CARD(9607, "此会员未实名认证，无法通过审核"),
    EXCHANGE_CATEGORY(9608, "此类型无法发放奖励"),
    EXCHANGE_QC(9609, "请不要重复质检"),
    EXCHANGE_PAYMENT_CLEAR(9610, "支付已提交，无法清空数据"),
    EXCHANGE_CANCEL(9611, "此兑换用户已撤销，无法审核"),
    EXCHANGE_MOBILE(9612, "此兑换用户未绑定手机号"),
    EXCHANGE_WEIXIN_FOLLOW(9613, "未关注赚点公众号"),
    EXCHANGE_CONSIGGER_BIND(9614, "未设置收货人信息"),
    EXCHANGE_CONSIGGER_NAME(9615, "姓名必填"),
    EXCHANGE_CONSIGGER_MOBILE(9616, "手机号填写错误"),
    EXCHANGE_CONSIGGER_CITY(9617, "城市信息不完整"),
    EXCHANGE_CONSIGGER_ADDRESS(9618, "详细地址必填，且地址中不能有#号、空格"),
    EXCHANGE_WEIXIN_BIND(9619, "未绑定微信公众号"),
    EXCHANGE_CARD(9620, "无剩余卡券"),
    EXCHANGE_CARD_TIME(9621, "当前分配人数较多，请稍后再试"),
    EXCHANGE_USER(9622, "当前会员不存在"),

    MASS_TEMPLATE_NOT_EXIST(9701, "模板不存在或未通过审核"),
    MASS_TEMPLATE_HEAD(9702, "文件参数和模板参数不一致"),
    MASS_TEMPLATE_ERROR(9703, "模板变量不正确"),
    MASS_STATE(9704, "暂不能更改为此状态"),
    MASS_TEMPLATE_SIGN(9705, "修改签名时，需要修改内容"),

    FOCUS_SECTION_NOT_EXIT(9801, "名称为%s的讨论组版块不存在"),
    FOCUS_SECTION_EXIT(9802, "名称为%s的讨论组版块已存在,请更换名称"),
    FOCUS_SECTION_NO_EXIT(9805, "名称为%s的讨论组版块不存在,请查证后再上传"),
    FOCUS_PASSWORD(9803, "老会员不能修改密码"),
    FOCUS_USER_NOT_EXIST(9804, "会员已被删除，不能编辑"),



    NOT_LOGGED_IN(4000,"您当前未登录请登录后再试"),
    NOT_LOGGED_ABNORMAL(4001,"登录状态异常请重新登录"),
    LOGON__FAILURE(4002,"登录失效请重新登录"),
    NUMBER_OF_LOGIN_ERROR(4003,"失败次数超过限制"),
    LOGIN_IMG_CODE_NOT_NULL(4004,"验证码不能为空"),
    LOGIN_IMG_CODE_FAIL(4005,"验证码错误"),
    LOGIN_USERNAME_OR_PASSWORD(4006,"用户名或者密码不能为空"),
    LOGIN_USERNAME_NULL(4007,"当前用户暂未注册"),

    /**
     * 二维码
     */
    QRCODE_SCAN_SUCCESS(3000, "扫描成功");

    private final int code;
    private String msg;

    ApiCodeUtils(final int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public  ApiCodeUtils getApiCode(int code) {
        ApiCodeUtils[] ecs = ApiCodeUtils.values();
        for (ApiCodeUtils ec : ecs) {
            if (ec.getCode() == code) {
                return ec;
            }
        }
        return SUCCESS;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
