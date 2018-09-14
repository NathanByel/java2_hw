package com.java2.lesson_7_8;

public enum CmdRsp {
    CMD_ALIVE,
    CMD_END ,
    CMD_AUTH ,
    CMD_TO_USER,
    CMD_GET_USERS,

    RSP_OK ,
    RSP_ERR,
    RSP_WRONG_CMD,
    RSP_WRONG_PARAM,
    RSP_NEED_AUTH,
    RSP_OK_AUTH,
    RSP_WRONG_AUTH,
    RSP_NICK_BUSY,
    RSP_USER_NOT_FOUND,
    RSP_USERS_LIST

   /* public static final String CMD_ALIVE           = "/alive";
    public static final String CMD_END             = "/end";
    public static final String CMD_AUTH            = "/auth";
    public static final String CMD_TO_USER         = "/to_user";
    public static final String CMD_GET_USERS       = "/get_users";

    public static final String RSP_OK              = "/ok";
    public static final String RSP_ERR             = "/err";
    public static final String RSP_WRONG_CMD       = "/wrong_cmd";
    public static final String RSP_WRONG_PARAM     = "/wrong_param";
    public static final String RSP_NEED_AUTH       = "/need_auth";
    public static final String RSP_OK_AUTH         = "/ok_auth";
    public static final String RSP_WRONG_AUTH      = "/wrong_auth";
    public static final String RSP_NICK_BUSY       = "/nick_busy";
    public static final String RSP_USER_NOT_FOUND  = "/not_found";
    public static final String RSP_USERS_LIST      = "/users_list";*/
}
