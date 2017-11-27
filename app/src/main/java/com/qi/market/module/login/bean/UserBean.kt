package com.qi.market.module.login.bean

/**
 * 用户信息
 * Created by Qi on 2017/11/16.
 */
class UserBean {
    var id: Long = -1
    var username: String = ""// 用户名
    var password: String = ""// 密码
    var createtime: String = "" // 创建时间

    var passwordview: String = ""//明文密码

    var outDate: Long = 0//找回密码，过时的日期

    var email: String = ""//邮件

    var mobilePhone: String = ""//手机号

    var qq: String = ""//qq号

    var activatesCode: String = ""//激活码

    var activateType: Int = 0//激活状态，0，未激活，1 ，激活
}