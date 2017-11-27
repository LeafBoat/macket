package com.qi.market.module.main.bean

/**
 * Created by Qi on 2017/11/18.
 */
class MerchandiseBean {
    var start: Int? = null
    var rows: Int? = null
    var currentPage: Int? = null
    var pageSize: Int? = null
    var id: Long? = null//主键
    var title: String? = null//商品标题
    var sellnums: String? = ""//销量
    var price: Double = 0.0//价格
    var typeid: Long = 0//商品分类
    var picpath: String = ""//图片地址
    var description: String = ""//商品描述
    var createTime: String = ""//创建时间
    var num = 0
}