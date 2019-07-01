/**
 * @Interface :  ApiInterface
 * @Usage : This interface is used for accumulating all the api calls
 * @Author : 1276
 */

package com.govida.api_section

import com.govida.push_notification.model.PushTokenRequest
import com.govida.push_notification.model.PushTokenResponse
import com.govida.ui_section.registration_section.models.RegisterUserResponse
import com.govida.ui_section.be_connected.model.BonusPointRequestObject
import com.govida.ui_section.be_connected.model.BonusPointResponseObject
import com.govida.ui_section.forgot_password_section.models.ForgotPasswordResponse
import com.govida.ui_section.forgot_password_section.models.ForgotPasswordRequest
import com.govida.ui_section.home_section.challenges_section.models.ChallengeAcceptRequest
import com.govida.ui_section.home_section.challenges_section.models.ChallengeAcceptResponse
import com.govida.ui_section.home_section.checkin_section.model.CheckinResponse
import com.govida.ui_section.home_section.checkin_section.model.CheckoutRequest
import com.govida.ui_section.home_section.checkin_section.model.VenueResponse
import com.govida.ui_section.home_section.model.ActivitySyncRequest
import com.govida.ui_section.home_section.model.ActivitySyncResponse
import com.govida.ui_section.home_section.model.ChallengeResponse
import com.govida.ui_section.home_section.profile_section.model.UserProfileEditRequest
import com.govida.ui_section.home_section.profile_section.model.UserProfileEditResponse
import com.govida.ui_section.home_section.rewards_section.models.AllRewardResponse
import com.govida.ui_section.home_section.rewards_section.models.MyRewardsResponse
import com.govida.ui_section.home_section.rewards_section.models.RedeemRewardsResponse
import com.govida.ui_section.leaderboard_section.model.LeaderboardResponse
import com.govida.ui_section.login_section.models.*
import com.govida.ui_section.registration_section.models.RegisterUserRequest
import com.govida.ui_section.setting_section.model.ChangePasswordRequest
import com.govida.ui_section.setting_section.model.ChangePasswordResponse
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded

interface ApiInterface {

    @FormUrlEncoded
    @POST("oauth/token")
    fun getToken(
        @HeaderMap headers: Map<String, String>, @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<TokenResponse>

    @FormUrlEncoded
    @POST("oauth/token")
    fun getAccessTokenFromRefresh(
        @HeaderMap headers: Map<String, String>, @Field("grant_type") grantType: String, @Field("refresh_token") refreshToken: String
    ): Call<TokenResponse>


    @Headers(value = [
        "Accept: application/json",
        "Content-type:application/json"]
    )
    @POST("v1/employees")
    fun registerUser(@Body user: RegisterUserRequest): Call<RegisterUserResponse>


    @Headers(value = [
        "Accept: application/json",
        "Content-type:application/json"]
    )
    @POST("v1/password/forgot")
    fun forgotPassword(@Body username: ForgotPasswordRequest): Call<ForgotPasswordResponse>


    @GET("api/v1/employees/me")
    fun getEmployeeDetails(@Header("Authorization") authHeader:String): Call<UserDetailResponse>


    @Headers(value = [
        "Accept: application/json",
        "Content-type:application/json"]
    )
    @POST("api/v1/employees/bonus/status")
    fun postBonusPoints(@Header("Authorization") authHeader:String,@Body bonusPointRequestObject: BonusPointRequestObject): Call<BonusPointResponseObject>


    @GET("api/v1/employees/bonus/status")
    fun getBonusPoints(@Header("Authorization") authHeader:String): Call<BonusPointResponseObject>

    @GET("api/v1/challengs/me")
    fun getChallengeList(@Header("Authorization") authHeader:String): Call<ChallengeResponse>

    @Headers(value = [
        "Accept: application/json",
        "Content-type:application/json"]
    )
    @POST("api/v1/challenges/accept")
    fun acceptChallenges(@Header("Authorization") authHeader:String,@Body challengeId: ChallengeAcceptRequest): Call<ChallengeAcceptResponse>

    @GET("api/v1/rewards")
    fun getAllRewards(@Header("Authorization") authHeader: String): Call<AllRewardResponse>

    @GET("api/v1/employeerewards")
    fun getMyRewards(@Header("Authorization") authHeader: String): Call<MyRewardsResponse>

    @Headers(value = [
        "Accept: application/json",
        "Content-type:application/json"]
    )
    @POST("api/v1/employee/sync/steps")
    fun postActivitySyncData(@Header("Authorization") authHeader:String,@Body syncData: ActivitySyncRequest): Call<ActivitySyncResponse>

    @GET("api/v1/rewards/redeem/{id}")
    fun redeemReward(@Header("Authorization") authHeader: String, @Path("id") userId: Int): Call<RedeemRewardsResponse>

    @Headers(value = [
        "Accept: application/json",
        "Content-type:application/json"]
    )

    @GET("api/v1/venue")
    fun getVenues(@Header("Authorization") authHeader: String,
                  @Query("searchString") name: String,
                  @Query("latitude") lat: String,
                  @Query("longitude") long: String): Call<VenueResponse>

    @GET("api/v1/venue/preferred")
    fun getpreferredVenues(@Header("Authorization") accessToken:String): Call<VenueResponse>

    @GET("api/v1/venue/verified")
    fun getVerifiedVenues(@Header("Authorization") authHeader: String,
                  @Query("latitude") lat: String,
                  @Query("longitude") long: String): Call<VenueResponse>

    @POST("api/v1/employees/venue/checkin")
    fun checkin(@Header("Authorization") authHeader:String, @Body data: VenueResponse.Data): Call<CheckinResponse>

    @POST("api/v1/employees/venue/checkout")
    fun checkout(@Header("Authorization") authHeader:String, @Body checkoutRequest: CheckoutRequest): Call<CheckinResponse>

    @PUT("api/v1/employees")
    fun editProfile(@Header("Authorization") authHeader:String, @Body userProfileEditRequest: UserProfileEditRequest): Call<UserProfileEditResponse>

    @PUT("api/v1/employees")
    fun updateUser(@Header("Authorization") authHeader:String,@Body user: UserProfileEditRequest): Call<UserProfileEditResponse>


    @GET("api/v1/leaderboard")
    fun getLeaderboard(@Header("Authorization") authHeader: String): Call<LeaderboardResponse>

    @Headers(value = [
        "Accept: application/json",
        "Content-type:application/json"]
    )
    @POST("api/v1/password/change")
    fun changePasswordRequest(@Header("Authorization") authHeader:String,@Body passwordRequest: ChangePasswordRequest): Call<ChangePasswordResponse>


    @Headers(value = [
        "Accept: application/json",
        "Content-type:application/json"]
    )
    @POST("api/v1/employee/deviceToken")
    fun sendFCMTokenToServer(@Header("Authorization") authHeader:String,@Body pushTokenRequest: PushTokenRequest): Call<PushTokenResponse>


}
