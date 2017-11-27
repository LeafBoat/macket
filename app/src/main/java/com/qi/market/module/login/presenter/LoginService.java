package com.qi.market.module.login.presenter;

import com.qi.market.module.login.bean.resopnse.LoginResponseBody;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Qi on 2017/11/21.
 */

public interface LoginService {

    @POST("appuser/userLogin")
    Observable<LoginResponseBody> login(@Body Object Any);

    @POST("appuser/register_user")
    Observable<LoginResponseBody> register(@Body Object Any);
}
