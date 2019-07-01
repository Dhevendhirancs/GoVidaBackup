/*
 * Copyright (C) 2019 GoVida
 * Author : 1276
 * Usage : Preference helper implementer for shared preference
 * Date : 15 April 19
 */

package com.govida.app_sharedpreference

import android.content.Context
import android.content.SharedPreferences
import com.govida.utility_section.AppConstants


class AppPreference(context: Context) : IPreferenceHelper {

    private val mSharedPreferences: SharedPreferences = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE)
    private val mEditor: SharedPreferences.Editor

    override var dayCheckInCount: String
        get() = ""+mSharedPreferences.getString("dayCheckInCount", "0")
        set(value) {
            mEditor.putString("dayCheckInCount", value)
            mEditor.commit()
        }

    override var daySteps: String
        get() = ""+mSharedPreferences.getString("daySteps", "0")
        set(value) {
            mEditor.putString("daySteps", value)
            mEditor.commit()
        }
    override var dayCheckIn: String
        get() = ""+mSharedPreferences.getString("dayCheckIn", "0")
        set(value) {
            mEditor.putString("dayCheckIn", value)
            mEditor.commit()
        }
    override var dayBonus: String
        get() = ""+mSharedPreferences.getString("dayBonus", "0")
        set(value) {
            mEditor.putString("dayBonus", value)
            mEditor.commit()
        }
    override var dayCharityEarning: String
        get() = ""+mSharedPreferences.getString("dayCharityEarning", "0")
        set(value) {
            mEditor.putString("dayCharityEarning", value)
            mEditor.commit()
        }
    override var dayChallenge: String
        get() = ""+mSharedPreferences.getString("dayChallenge", "0")
        set(value) {
            mEditor.putString("dayChallenge", value)
            mEditor.commit()
        }
    override var dayRedeem: String
        get() = ""+mSharedPreferences.getString("dayRedeem", "0")
        set(value) {
            mEditor.putString("dayRedeem", value)
            mEditor.commit()
        }
    override var weekCheckInCount: String
        get() = ""+mSharedPreferences.getString("weekCheckInCount", "0")
        set(value) {
            mEditor.putString("weekCheckInCount", value)
            mEditor.commit()
        }
    override var weekSteps: String
        get() = ""+mSharedPreferences.getString("weekSteps", "0")
        set(value) {
            mEditor.putString("weekSteps", value)
            mEditor.commit()
        }
    override var weekCheckIn: String
        get() = ""+mSharedPreferences.getString("weekCheckIn", "0")
        set(value) {
            mEditor.putString("weekCheckIn", value)
            mEditor.commit()
        }
    override var weekBonus: String
        get() = ""+mSharedPreferences.getString("weekBonus", "0")
        set(value) {
            mEditor.putString("weekBonus", value)
            mEditor.commit()
        }
    override var weekCharityEarning: String
        get() = ""+mSharedPreferences.getString("weekCharityEarning", "0")
        set(value) {
            mEditor.putString("weekCharityEarning", value)
            mEditor.commit()
        }
    override var weekChallenge: String
        get() = ""+mSharedPreferences.getString("weekChallenge", "0")
        set(value) {
            mEditor.putString("weekChallenge", value)
            mEditor.commit()
        }
    override var weekRedeem: String
        get() = ""+mSharedPreferences.getString("weekRedeem", "0")
        set(value) {
            mEditor.putString("weekRedeem", value)
            mEditor.commit()
        }
    override var monthCheckInCount: String
        get() = ""+mSharedPreferences.getString("monthCheckInCount", "0")
        set(value) {
            mEditor.putString("monthCheckInCount", value)
            mEditor.commit()
        }
    override var monthSteps: String
        get() = ""+mSharedPreferences.getString("monthSteps", "0")
        set(value) {
            mEditor.putString("monthSteps", value)
            mEditor.commit()
        }
    override var monthCheckIn: String
        get() = ""+mSharedPreferences.getString("monthCheckIn", "0")
        set(value) {
            mEditor.putString("monthCheckIn", value)
            mEditor.commit()
        }
    override var monthBonus: String
        get() = ""+mSharedPreferences.getString("monthBonus", "0")
        set(value) {
            mEditor.putString("monthBonus", value)
            mEditor.commit()
        }
    override var monthCharityEarning: String
        get() = ""+mSharedPreferences.getString("monthCharityEarning", "0")
        set(value) {
            mEditor.putString("monthCharityEarning", value)
            mEditor.commit()
        }
    override var monthChallenge: String
        get() = ""+mSharedPreferences.getString("monthChallenge", "0")
        set(value) {
            mEditor.putString("monthChallenge", value)
            mEditor.commit()
        }
    override var monthRedeem: String
        get() = ""+mSharedPreferences.getString("monthRedeem", "0")
        set(value) {
            mEditor.putString("monthRedeem", value)
            mEditor.commit()
        }
    override var yearCheckInCount: String
        get() = ""+mSharedPreferences.getString("yearCheckInCount", "0")
        set(value) {
            mEditor.putString("yearCheckInCount", value)
            mEditor.commit()
        }
    override var yearSteps: String
        get() = ""+mSharedPreferences.getString("yearSteps", "0")
        set(value) {
            mEditor.putString("yearSteps", value)
            mEditor.commit()
        }
    override var yearCheckIn: String
        get() = ""+mSharedPreferences.getString("yearCheckIn", "0")
        set(value) {
            mEditor.putString("yearCheckIn", value)
            mEditor.commit()
        }
    override var yearBonus: String
        get() = ""+mSharedPreferences.getString("yearBonus", "0")
        set(value) {
            mEditor.putString("yearBonus", value)
            mEditor.commit()
        }
    override var yearCharityEarning: String
        get() = ""+mSharedPreferences.getString("yearCharityEarning", "0")
        set(value) {
            mEditor.putString("yearCharityEarning", value)
            mEditor.commit()
        }
    override var yearChallenge: String
        get() = ""+mSharedPreferences.getString("yearChallenge", "0")
        set(value) {
            mEditor.putString("yearChallenge", value)
            mEditor.commit()
        }
    override var yearRedeem: String
        get() = ""+mSharedPreferences.getString("yearRedeem", "0")
        set(value) {
            mEditor.putString("yearRedeem", value)
            mEditor.commit()
        }
    override var totalCheckInCount: String
        get() = ""+mSharedPreferences.getString("totalCheckInCount", "0")
        set(value) {
            mEditor.putString("totalCheckInCount", value)
            mEditor.commit()
        }
    override var totalSteps: String
        get() = ""+mSharedPreferences.getString("totalSteps", "0")
        set(value) {
            mEditor.putString("totalSteps", value)
            mEditor.commit()
        }
    override var totalCheckIn: String
        get() = ""+mSharedPreferences.getString("totalCheckIn", "0")
        set(value) {
            mEditor.putString("totalCheckIn", value)
            mEditor.commit()
        }
    override var totalBonus: String
        get() = ""+mSharedPreferences.getString("totalBonus", "0")
        set(value) {
            mEditor.putString("totalBonus", value)
            mEditor.commit()
        }
    override var totalCharityEarning: String
        get() = ""+mSharedPreferences.getString("totalCharityEarning", "0")
        set(value) {
            mEditor.putString("totalCharityEarning", value)
            mEditor.commit()
        }
    override var totalChallenge: String
        get() = ""+mSharedPreferences.getString("totalChallenge", "0")
        set(value) {
            mEditor.putString("totalChallenge", value)
            mEditor.commit()
        }
    override var totalRedeem: String
        get() = ""+mSharedPreferences.getString("totalRedeem", "0")
        set(value) {
            mEditor.putString("totalRedeem", value)
            mEditor.commit()
        }

    /**
     *  @Function : isWelcomeSetupDone()
     *  @params   : void
     *  @Return   : Boolean
     * 	@Usage	  : Get status of welcome slider if visited or not
     *  @Author   : 1276
     */
    override val isWelcomeSetupDone: Boolean
        get() = mSharedPreferences.getBoolean(AppConstants.IS_WELCOME_DONE, false)

    /**
     *  @Function : userLastSyncActivityDate()
     *  @params   : String
     *  @Return   : String
     * 	@Usage	  : Store date in string when user logged it in systems
     *  @Author   : 1276
     */
    override var userLastSyncActivityDate: Long
        get() = mSharedPreferences.getLong(AppConstants.LOGGED_IN_USERDATE, 0)
        set(value) {
            mEditor.putLong(AppConstants.LOGGED_IN_USERDATE, value)
            mEditor.commit()
        }

    /**
     *  @Function : appInstallDate()
     *  @params   : String
     *  @Return   : String
     * 	@Usage	  : Store date in string when the app is run first time
     *  @Author   : 1276
     */
    override var appInstallDate: String
        get() = ""+mSharedPreferences.getString(AppConstants.APP_INSTALL_DATE, "")
        set(value) {
            mEditor.putString(AppConstants.APP_INSTALL_DATE, value)
            mEditor.commit()
        }

    /**
     *  @Function : isUserLoggedIn()
     *  @params   : void
     *  @Return   : Boolean
     * 	@Usage	  : Get status of user if he is logged-in in application
     *  @Author   : 1276
     */
    override val isUserLoggedIn: Boolean
        get() = mSharedPreferences.getBoolean(AppConstants.IS_PRIMARY_LOGIN, false)


    /**
     *  @Function : authorizationToken()
     *  @params   : String
     *  @Return   : String
     * 	@Usage	  : Get and set user token
     *  @Author   : 1276
     */
    override var authorizationToken: String
        get() = "Bearer " + mSharedPreferences.getString(AppConstants.AUHTORIZATION_TOKEN, "")!!
        set(authorizationToken) {
            mEditor.putString(AppConstants.AUHTORIZATION_TOKEN, authorizationToken)
            mEditor.commit()
        }


    /**
     *  @Function : notificationDeviceToken()
     *  @params   : String
     *  @Return   : String
     * 	@Usage	  : Get and set user notification token
     *  @Author   : 1276
     */
    override var notificationDeviceToken: String
        get() = mSharedPreferences.getString(AppConstants.NOTIFICATION_DEVICE_TOKEN, "")!!
        set(notificationToken) {
            mEditor.putString(AppConstants.NOTIFICATION_DEVICE_TOKEN, notificationToken)
            mEditor.commit()
        }

    /**
     *  @Function : notificationDeviceId()
     *  @params   : String
     *  @Return   : String
     * 	@Usage	  : Get and set user notification id
     *  @Author   : 1276
     */
    override var notificationDeviceId: String
        get() = mSharedPreferences.getString(AppConstants.NOTIFICATION_DEVICE_ID, "")!!
        set(notificationToken) {
            mEditor.putString(AppConstants.NOTIFICATION_DEVICE_ID, notificationToken)
            mEditor.commit()
        }


    /**
     *  @Function : isFirstChallengeCardShow()
     *  @params   : Boolean
     *  @Return   : Boolean
     * 	@Usage	  : Get and set user first challenge state
     *  @Author   : 1276
     */
    override var isFirstChallengeCardShow: Boolean
        get() = mSharedPreferences.getBoolean(AppConstants.IS_FIRST_CHALLENGE_ACCEPTED, false)
        set(state) {
            mEditor.putBoolean(AppConstants.IS_FIRST_CHALLENGE_ACCEPTED, state)
            mEditor.commit()
        }

    /**
     *  @Function : myTotalGoVps()
     *  @params   : Int
     *  @Return   : Int
     * 	@Usage	  : Get and set my GoVps
     *  @Author   : 1276
     */
    override var myTotalGoVps: Int
        get() = mSharedPreferences.getInt(AppConstants.MY_TOTAL_GOVPS, 0)
        set(value) {
            mEditor.putInt(AppConstants.MY_TOTAL_GOVPS, value)
            mEditor.commit()
        }

    /**
     *  @Function : checkinId()
     *  @params   : Int
     *  @Return   : Int
     * 	@Usage	  : Get and set my Checkin Id
     *  @Author   : 1769
     */
    override var checkinId: Int
        get() = mSharedPreferences.getInt(AppConstants.CHECKIN_ID, 0)
            set(checkinId) {
                mEditor.putInt(AppConstants.CHECKIN_ID, checkinId)
                mEditor.commit()
            }

    /**
     *  @Function : employeeId()
     *  @params   : Int
     *  @Return   : Int
     * 	@Usage	  : Get and set Employee Id
     *  @Author   : 1769
     */
    override var employeeId: Int
        get() = mSharedPreferences.getInt(AppConstants.EMPLOYEE_ID, 0)
        set(employeeId) {
            mEditor.putInt(AppConstants.EMPLOYEE_ID, employeeId)
            mEditor.commit()
        }

    /**
     *  @Function : refreshToken()
     *  @params   : String
     *  @Return   : String
     * 	@Usage	  : Get and set user  refresh token
     *  @Author   : 1276
     */
    override var refreshToken: String
        get() = ""+mSharedPreferences.getString(AppConstants.AUHTORIZATION_TOKEN, "")!!
        set(refreshToken) {
            mEditor.putString(AppConstants.REFRESH_TOKEN, refreshToken)
            mEditor.commit()
        }

    /**
     *  @Function : currentVenueName()
     *  @params   : String
     *  @Return   : String
     * 	@Usage	  : Get and set current venue name for checkin
     *  @Author   : 1276
     */
    override var currentVenueName: String
        get() = ""+mSharedPreferences.getString(AppConstants.VENUE_NAME, "")!!
            set(venueName) {
                mEditor.putString(AppConstants.VENUE_NAME, venueName)
                mEditor.commit()
            }

    /**
     *  @Function : isUserOnLoginPage()
     *  @params   : Boolean
     *  @Return   : Boolean
     * 	@Usage	  : Get and set is user visited login page
     *  @Author   : 1276
     */
    override var isUserOnLoginPage: Boolean
        get() = mSharedPreferences.getBoolean(AppConstants.IS_USER_ON_LOGIN_PAGE, false)
        set(isUserOnLoginPage) {
            mEditor.putBoolean(AppConstants.IS_USER_ON_LOGIN_PAGE, isUserOnLoginPage)
            mEditor.commit()
        }

    /**
     *  @Function : userEmailId()
     *  @params   : String
     *  @Return   : String
     * 	@Usage	  : Get and set for users email address
     *  @Author   : 1276
     */
    override var userEmailId: String
        get() = mSharedPreferences.getString(AppConstants.LOGGEDIN_USER_PRIMARY_MAIL_ID, "")
        set(primaryUserEmailId) {
            mEditor.putString(AppConstants.LOGGEDIN_USER_PRIMARY_MAIL_ID, primaryUserEmailId)
            mEditor.commit()
        }

    /**
     *  @Function : userFirstName()
     *  @params   : String
     *  @Return   : String
     * 	@Usage	  : Get and set for users first name
     *  @Author   : 1276
     */
    override var userFirstName: String
        get() = mSharedPreferences.getString(AppConstants.LOGGEDIN_USER_FIRST_NAME, "")
        set(userFirstName) {
            mEditor.putString(AppConstants.LOGGEDIN_USER_FIRST_NAME, userFirstName)
            mEditor.commit()
        }

    /**
     *  @Function : userLastName()
     *  @params   : String
     *  @Return   : String
     * 	@Usage	  : Get and set for users last name
     *  @Author   : 1276
     */
    override var userLastName: String
        get() = mSharedPreferences.getString(AppConstants.LOGGEDIN_USER_LAST_NAME, "")
        set(userLastName) {
            mEditor.putString(AppConstants.LOGGEDIN_USER_LAST_NAME, userLastName)
            mEditor.commit()
        }

    /**
     *  @Function : isHomeSetupDone()
     *  @params   : Void
     *  @Return   : Boolean
     * 	@Usage	  : Get if user visited home page
     *  @Author   : 1276
     */
    override val isHomeSetupDone: Boolean
        get() = mSharedPreferences.getBoolean(AppConstants.IS_USER_ON_HOME_PAGE, false)

    /**
     *  @Function : isGoogleFitPermissionGiven()
     *  @params   : Void
     *  @Return   : Boolean
     * 	@Usage	  : Get if user gives google fit permission
     *  @Author   : 1276
     */
    override val isGoogleFitPermissionGiven: Boolean
        get() = mSharedPreferences.getBoolean(AppConstants.IS_GOOGLE_FIT_ON, false)

    /**
     *  @Function : isGPSPermissionGiven()
     *  @params   : Void
     *  @Return   : Boolean
     * 	@Usage	  : Get if user gives GPS permission
     *  @Author   : 1276
     */
    override val isGPSPermissionGiven: Boolean
        get() = mSharedPreferences.getBoolean(AppConstants.IS_GPS_ON, false)

    /**
     *  @Function : isGPSPermissionGiven()
     *  @params   : Void
     *  @Return   : Boolean
     * 	@Usage	  : get and set Checkin Time
     *  @Author   : 1276
     */
    override var checkinTime: String
        get() = mSharedPreferences.getString(AppConstants.CHECKIN_TIME, "")
        set(checkinTimer) {
            mEditor.putString(AppConstants.CHECKIN_TIME, checkinTimer)
            mEditor.commit()
        }

    /**
     *  @Function : isGPSPointAlert()
     *  @params   : Void
     *  @Return   : Boolean
     * 	@Usage	  : Get gps alert status
     *  @Author   : 1276
     */
    override val isGPSPointAlert: Boolean
        get() = mSharedPreferences.getBoolean(AppConstants.ALERT_GPS, false)

    /**
     *  @Function : isOnboardingPointAlert()
     *  @params   : Void
     *  @Return   : Boolean
     * 	@Usage	  : Get onboarding status
     *  @Author   : 1276
     */

    override val isOnboardingPointAlert: Boolean
        get() = mSharedPreferences.getBoolean(AppConstants.ALERT_FIRST_LOGIN, false)

    /**
     *  @Function : isDeviceIntegrationPointAlert()
     *  @params   : Void
     *  @Return   : Boolean
     * 	@Usage	  : Get google fit alert status
     *  @Author   : 1276
     */
    override val isDeviceIntegrationPointAlert: Boolean
        get() = mSharedPreferences.getBoolean(AppConstants.ALERT_GOOGLE_FIT, false)

    /**
     *  @Function : isDeviceIntegrationPointAlert()
     *  @params   : Void
     *  @Return   : String
     * 	@Usage	  : get and set Personal Email
     *  @Author   : 1769
     */
    override var personalEmail: String
        get() = mSharedPreferences.getString(AppConstants.LOGGEDIN_USER_PERSONAL_MAIL_ID, "")
        set(personalEmail) {
            mEditor.putString(AppConstants.LOGGEDIN_USER_PERSONAL_MAIL_ID, personalEmail)
            mEditor.commit()
        }

    /**
     *  @Function : phone()
     *  @params   : Void
     *  @Return   : String
     * 	@Usage	  : get and set Phone
     *  @Author   : 1769
     */
    override var phone: String
        get() = mSharedPreferences.getString(AppConstants.LOGGEDIN_USER_PHONE, "")
        set(phone) {
            mEditor.putString(AppConstants.LOGGEDIN_USER_PHONE, phone)
            mEditor.commit()
        }

    /**
     *  @Function : gender()
     *  @params   : Void
     *  @Return   : String
     * 	@Usage	  : get and set Gender
     *  @Author   : 1769
     */
    override var gender: String
        get() = mSharedPreferences.getString(AppConstants.LOGGED_IN_USER_GENDER, "")
        set(gender) {
            mEditor.putString(AppConstants.LOGGED_IN_USER_GENDER, gender)
            mEditor.commit()
        }

    /**
     *  @Function : userDob()
     *  @params   : Void
     *  @Return   : String
     * 	@Usage	  : get and set Date of Birth
     *  @Author   : 1769
     */
    override var userDob: String
        get() = mSharedPreferences.getString(AppConstants.LOGGED_IN_USER_DOB, "")
        set(dob) {
            mEditor.putString(AppConstants.LOGGED_IN_USER_DOB, dob)
            mEditor.commit()
        }

    /**
     *  @Function : profilePicturePath()
     *  @params   : Void
     *  @Return   : String
     * 	@Usage	  : get and set Profile Picture Path
     *  @Author   : 1769
     */
    override var profilePicturePath: String
        get() = mSharedPreferences.getString(AppConstants.PROFILE_PICTURE, "")
        set(path) {
            mEditor.putString(AppConstants.PROFILE_PICTURE, path)
            mEditor.commit()
        }

    /**
     *  @Function : profileCompletionStatus()
     *  @params   : Void
     *  @Return   : Int
     * 	@Usage	  : get and set Profile Completion Status
     *  @Author   : 1769
     */
    override var profileCompletionStatus: Int
        get() = mSharedPreferences.getInt(AppConstants.PROFILE_COMPLETION_STATUS, 40)
        set(status) {
            mEditor.putInt(AppConstants.PROFILE_COMPLETION_STATUS, status)
            mEditor.commit()
        }

    /**
     *  @Function : isCheckInBonusReceived()
     *  @params   : Void
     *  @Return   : Boolean
     * 	@Usage	  : Get and post first check in bonus point status
     *  @Author   : 1276
     */
    override var isCheckInBonusReceived: Boolean
        get() = mSharedPreferences.getBoolean(AppConstants.CHECKIN_BONUS_RECEIVED, false)
        set(received) {
            mEditor.putBoolean(AppConstants.CHECKIN_BONUS_RECEIVED, received)
            mEditor.commit()
        }

    /**
     *  @Function : isChallengeBonusReceived()
     *  @params   : Void
     *  @Return   : Boolean
     * 	@Usage	  : Get and set first challenge bonus point status
     *  @Author   : 1276
     */
    override var isChallengeBonusReceived: Boolean
        get() = mSharedPreferences.getBoolean(AppConstants.CHALLENGE_BONUS_RECEIVED, false)
        set(received) {
            mEditor.putBoolean(AppConstants.CHALLENGE_BONUS_RECEIVED, received)
            mEditor.commit()
        }

    /**
     *  @Function : isCheckoutDone()
     *  @params   : Void
     *  @Return   : Boolean
     * 	@Usage	  : get and set Checkout Status
     *  @Author   : 1769
     */
    override var isCheckoutDone: Boolean
        get() = mSharedPreferences.getBoolean(AppConstants.IS_CHECKOUT_DONE, false)
        set(received) {
            mEditor.putBoolean(AppConstants.IS_CHECKOUT_DONE, received)
            mEditor.commit()
        }

    /**
     *  @Function : isProfileCompletionBonusReceived()
     *  @params   : Void
     *  @Return   : Boolean
     * 	@Usage	  : Get and set profile completion bonus point status
     *  @Author   : 1276
     */
    override var isProfileCompletionBonusReceived: Boolean
        get() = mSharedPreferences.getBoolean(AppConstants.PROFILE_COMPLETION_BONUS_RECEIVED, false)
        set(received) {
            mEditor.putBoolean(AppConstants.PROFILE_COMPLETION_BONUS_RECEIVED, received)
            mEditor.commit()
        }

    init {
        mEditor = mSharedPreferences.edit()
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(AppConstants.PREF_NAME, 0)
    }

    /**
     *  @Function : welcomeSetupDone()
     *  @params   : Boolean
     *  @Return   : Void
     * 	@Usage	  : Set welcome screen visit is done
     *  @Author   : 1276
     */
    override fun welcomeSetupDone(welcomeState: Boolean) {
        mEditor.putBoolean(AppConstants.IS_WELCOME_DONE, welcomeState)
        mEditor.commit()
    }

    /**
     *  @Function : setUserLogin()
     *  @params   : Boolean
     *  @Return   : Void
     * 	@Usage	  : Set user is logged in
     *  @Author   : 1276
     */
    override fun setUserLogin(isLogin: Boolean) {
        mEditor.putBoolean(AppConstants.IS_PRIMARY_LOGIN, isLogin)
        mEditor.commit()
    }


    /**
     *  @Function : clearAllPreferences()
     *  @params   : Void
     *  @Return   : Void
     * 	@Usage	  : Reset Shared Preferences
     *  @Author   : 1276
     */
    override fun clearAllPreferences() {
        mEditor.clear()
        mEditor.commit()
    }

    /**
     *  @Function : homeSetupDone()
     *  @params   : Boolean
     *  @Return   : Void
     * 	@Usage	  : Set if home page visited successfully
     *  @Author   : 1276
     */
    override fun homeSetupDone(isOnHomePage: Boolean) {
        mEditor.putBoolean(AppConstants.IS_USER_ON_HOME_PAGE, isOnHomePage)
        mEditor.commit()
    }

    /**
     *  @Function : setGPSPermission()
     *  @params   : Boolean
     *  @Return   : Void
     * 	@Usage	  : Set GPS permission status
     *  @Author   : 1769
     */
    override fun setGPSPermission(isGPSOn: Boolean) {
        mEditor.putBoolean(AppConstants.IS_GPS_ON, isGPSOn)
        mEditor.commit()
    }

    /**
     *  @Function : setGoogleFitPermission()
     *  @params   : Boolean
     *  @Return   : Void
     * 	@Usage	  : Set Google Fit permission status
     *  @Author   : 1769
     */
    override fun setGoogleFitPermission(isGoogleFit: Boolean) {
        mEditor.putBoolean(AppConstants.IS_GOOGLE_FIT_ON, isGoogleFit)
        mEditor.commit()
    }

    /**
     *  @Function : showGPSPointsAlert()
     *  @params   : Boolean
     *  @Return   : Void
     * 	@Usage	  : Set first time gps alert status
     *  @Author   : 1276
     */
    override fun showGPSPointsAlert(isGPSFirst: Boolean) {
        mEditor.putBoolean(AppConstants.ALERT_GPS, isGPSFirst)
        mEditor.commit()
    }

    /**
     *  @Function : showOnboardingPointsAlert()
     *  @params   : Boolean
     *  @Return   : Void
     * 	@Usage	  : Set first time login/onboarding alert status
     *  @Author   : 1276
     */
    override fun showOnboardingPointsAlert(isOnboardingFirst: Boolean) {
        mEditor.putBoolean(AppConstants.ALERT_FIRST_LOGIN, isOnboardingFirst)
        mEditor.commit()
    }

    /**
     *  @Function : showDeviceIntegrationPointsAlert()
     *  @params   : Boolean
     *  @Return   : Void
     * 	@Usage	  : Set google fit integration alert status
     *  @Author   : 1276
     */
    override fun showDeviceIntegrationPointsAlert(isDeviceIntegrationFirst: Boolean) {
        mEditor.putBoolean(AppConstants.ALERT_GOOGLE_FIT, isDeviceIntegrationFirst)
        mEditor.commit()
    }

    /**
     *  @Function : checkinId()
     *  @params   : Int
     *  @Return   : Int
     * 	@Usage	  : Get and set my Checkin Id
     *  @Author   : 1769
     */
    override var checkinHour: Int
        get() = mSharedPreferences.getInt(AppConstants.CHECKIN_HOUR, 25)
        set(checkinHour) {
            mEditor.putInt(AppConstants.CHECKIN_HOUR, checkinHour)
            mEditor.commit()
        }

    /**
     *  @Function : checkinId()
     *  @params   : Int
     *  @Return   : Int
     * 	@Usage	  : Get and set my Checkin Id
     *  @Author   : 1769
     */
    override var checkinMin: Int
        get() = mSharedPreferences.getInt(AppConstants.CHECKIN_MIN, 0)
        set(checkinMin) {
            mEditor.putInt(AppConstants.CHECKIN_MIN, checkinMin)
            mEditor.commit()
        }

    /**
     *  @Function : checkinId()
     *  @params   : Int
     *  @Return   : Int
     * 	@Usage	  : Get and set my Checkin Id
     *  @Author   : 1769
     */
    override var checkinSec: Int
        get() = mSharedPreferences.getInt(AppConstants.CHECKIN_SEC, 0)
        set(checkinSec) {
            mEditor.putInt(AppConstants.CHECKIN_SEC, checkinSec)
            mEditor.commit()
        }


}
