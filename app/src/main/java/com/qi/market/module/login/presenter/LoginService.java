package com.qi.market.module.login.presenter;

import com.qi.market.module.login.bean.LoginBodyBean;
import com.qi.market.module.login.bean.RegisterBodyBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Qi on 2017/11/21.
 */

public interface LoginService {

    @POST("appuser/userLogin")
    Observable<LoginBodyBean> login(@Body Object Any);

    @POST("appuser/register_user")
    Observable<RegisterBodyBean> register(@Body Object Any);
}
