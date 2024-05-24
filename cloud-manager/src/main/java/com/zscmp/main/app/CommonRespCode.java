package com.zscmp.main.app;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

/**
 * Created by Xmm on 2018/08/23.
 */
@Getter
public enum CommonRespCode {

    SUCCESS(0, "成功"),
    SERVER_UNAUTHORIZED(401, "未授权，拒绝访问"),
    SERVER_UNAUTHENTICATED(402, "未登录，拒绝访问"),
    SERVER_ERROR(500, "服务器内部错误"),
    CHK_ENTITY_EXIST(704, "数据已存在 [%s]"),
    CHK_LIMIT(710, "超出分页最大上限 [%s]"),
    CHK_DELETE_DENY(711, "无法删除 [%s]"),
    CHK_TIME_RANGE(705, "时间段不正确 [%s] [%s]"),
    CHK_PARAM_OVERSIZE(706, "参数长度过长 [%s]"),
    CHK_QUEUE_EXIST(707, "队列中已存在"),
    CHK_QUEUE_OVERSIZE(708, "超出队列上限 [%s]"),
    CHK_CANCEL_DENY(709, "无法取消 [%s]"),
    CHK_OVERSIZE(712, "超出最大上限 [%s]"),
    NO_DATA(742, "数据不存在[%s]"),
    CHK_EDIT_DENY(715, "无法编辑 [%s]"),
    CHK_ACTION_DENY(724, "不支持此类操作[%s]"),
    CHK_UNIT(735, "单位错误[%s]"),


    CHK_CLOUD_ACCOUNT_NOT_EXISTED(714, "不存在的云账号 [%s]"),
    CHK_CLOUD_ACCOUNT_ENV_NOT_EXIST(751, "云环境不存在[%s]"),


    CHK_ACCOUNT(703, "用户名或密码错误"),
    CHK_ACCOUNT_SIGNATURE(704, "用户签名错误"),
    CHK_ENTITY_USER_NOT_EXIST(753, "用户不存在[%s]"),
    CHK_ENTITY_ROLE_NOT_EXIST(754, "角色不存在[%s]"),
    CHK_ENTITY_VDC_NOT_EXIST(762, "组织不存在[%s]"),
    CHK_ENTITY_SYSTEM_NOT_EXIST(763, "业务系统不存在"),

    CHK_ENTITY_MANAGED_OBJECT_NOT_EXIST(701, "ManagedObject不存在[%s]"),
    CHK_ENTITY_SERVICE_CATALOGUE_NOT_EXIST(771, "服务目录不存在[%s]"),
    CHK_ENTITY_FLOW_CONFIG_NOT_EXIST(777, "审批配置不存在[%s]"),
    CHK_ENTITY_APPLY_ORDER_NOT_EXIST(902, "申请单不存在[%s]"),
    CHK_ENTITY_FLOW_PROCESS_RECORD_NOT_EXIST(903, "审批操作记录不存在[%s]"),

    CHK_ENTITY_BILLING_RULE_NOT_EXIST(760, "账单计费规则不存在[%s]"),


    COMMON_MISSING_PARAMS(1001, "缺少参数[%s]"),
    COMMON_INVALID_PARAMS(1002, "无效参数[%s]"),
    COMMON_ACTION_DENIED(1003, "操作被禁止[%s]"),
    COMMON_STATUS_NOT_AVAILABLE(1004, "当前状态为不可用[%s]"),
    COMMON_INVALID_REFRESH_TOKEN(1005, "无效的刷新令牌"),
    COMMON_INVALID_ENCRYPT_INFO(1006, "无效的加密信息"),


    /**
     * 使用
     * COMMON_MISSING_PARAMS 或 COMMON_INVALID_PARAMS
     * 进行代替
     */
    @Deprecated
    CHK_PARAMS(700, "参数错误[%s]"),


    THD_CLOUD_CLIENT_ERROR(801, "云账号网络异常"),
    THD_CLOUD_SERVER_ERROR(802, "云账号请求响应错误"),
    FLOW_ERROR(811, "审批异常"),
    ;

    private final String code;
    private final String msg;

    CommonRespCode(int code, String msg) {
        this.code = String.format("COMMON.%s", code);
        this.msg = msg;
    }

    @Override
    public String toString() {
        JSONObject data = new JSONObject();
        data.put("code", this.code);
        data.put("msg", this.msg);
        return data.toJSONString();
    }
}
