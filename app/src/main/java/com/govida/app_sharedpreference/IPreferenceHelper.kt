/*
 * Copyright (C) 2019 GoVida
 * Author : 1276
 * Usage : Preference Helper interface
 * Date : 15 April 19
 */

package com.govida.app_sharedpreference

interface IPreferenceHelper {
    val isWelcomeSetupDone: Boolean
    val isUserLoggedIn: Boolean
    var authorizationToken: String
    var refreshToken: String
    var isUserOnLoginPage: Boolean
    var notificationDeviceToken:String
    var notificationDeviceId:String
    var userEmailId: String
    var userLastSyncActivityDate: Long
    var appInstallDate: String
    var userFirstName: String
    var userLastName: String
    var personalEmail: String
    var phone: String
    var gender: String
    var employeeId: Int
    val isHomeSetupDone: Boolean
    val isGPSPermissionGiven: Boolean
    val isGoogleFitPermissionGiven: Boolean
    var userDob:String
    var checkinTime: String
    val isGPSPointAlert: Boolean
    val isOnboardingPointAlert: Boolean
    val isDeviceIntegrationPointAlert: Boolean
    val isFirstChallengeCardShow: Boolean
    var profilePicturePath: String
    var profileCompletionStatus: Int
    var isCheckInBonusReceived: Boolean
    var isChallengeBonusReceived: Boolean
    var isProfileCompletionBonusReceived: Boolean
    var isCheckoutDone: Boolean
    var myTotalGoVps: Int
    var currentVenueName: String
    var checkinId: Int
    var checkinHour: Int
    var checkinMin: Int
    var checkinSec: Int

    var dayCheckInCount:String
    var daySteps:String
    var dayCheckIn:String
    var dayBonus:String
    var dayCharityEarning:String
    var dayChallenge:String
    var dayRedeem:String

    var weekCheckInCount:String
    var weekSteps:String
    var weekCheckIn:String
    var weekBonus:String
    var weekCharityEarning:String
    var weekChallenge:String
    var weekRedeem:String

    var monthCheckInCount:String
    var monthSteps:String
    var monthCheckIn:String
    var monthBonus:String
    var monthCharityEarning:String
    var monthChallenge:String
    var monthRedeem:String

    var yearCheckInCount:String
    var yearSteps:String
    var yearCheckIn:String
    var yearBonus:String
    var yearCharityEarning:String
    var yearChallenge:String
    var yearRedeem:String

    var totalCheckInCount:String
    var totalSteps:String
    var totalCheckIn:String
    var totalBonus:String
    var totalCharityEarning:String
    var totalChallenge:String
    var totalRedeem:String

    fun welcomeSetupDone(isFirstTime: Boolean)
    fun setUserLogin(isLogin: Boolean)
    fun clearAllPreferences()
    fun homeSetupDone(isOnHomePage: Boolean)
    fun setGPSPermission(isGPSOn: Boolean)
    fun setGoogleFitPermission(isGoogleFit: Boolean)
    fun showGPSPointsAlert(isGPSFirst: Boolean)
    fun showOnboardingPointsAlert(isOnboardingFirst: Boolean)
    fun showDeviceIntegrationPointsAlert(isDeviceIntegrationFirst: Boolean)
}
