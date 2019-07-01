/**
 * @Class : AppConstants
 * @Usage : This class is used to define constants
 * @Author : 1276
 */

package com.govida.utility_section

import com.govida.ui_section.leaderboard_section.model.LeaderboardResponse

object AppConstants {

    const val STATUS_CODE_SUCCESS = "SUCCESS"

    const val DB_NAME = "govida.db"
    const val PREF_NAME = "govida_pref"

    const val TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss"
    const val IS_PRIMARY_LOGIN = "IsPrimaryLogin"
    const val IS_FIRST_CHALLENGE_ACCEPTED= "IsFirstChallengeAccepted"
    const val IS_USER_ON_LOGIN_PAGE = "IsOnLoginPage"
    const val IS_USER_ON_HOME_PAGE = "IsOnHOmePage"
    const val IS_GPS_ON = "IsGPSOn"
    const val IS_GOOGLE_FIT_ON = "IsGoogleFitOn"
    const val AUHTORIZATION_TOKEN = "authorization_token"
    const val NOTIFICATION_DEVICE_TOKEN = "notificationDeviceToken"
    const val NOTIFICATION_DEVICE_ID = "notificationDeviceId"
    const val REFRESH_TOKEN = "refresh_token"
    const val LOGGEDIN_USER_PRIMARY_MAIL_ID = "PrimaryEmailId"
    const val LOGGEDIN_USER_FIRST_NAME = "FirstName"
    const val LOGGEDIN_USER_LAST_NAME = "LastName"
    const val IS_WELCOME_DONE = "IsWelcomeDone"
    const val LOGGED_IN_USERDATE = "LoggedInUserDate"
    const val APP_INSTALL_DATE = "AppInstallDate"
    const val MY_TOTAL_GOVPS = "myTotalGoVps"
    const val FROM_SCREEN = "fromScreen"
    const val SEND_ID = "sendId"
    const val TEMP_EMAIL = "tempEmail"
    const val IS_CHECKOUT_DONE = "IsCheckoutDone"

    const val CHECKIN_TIME = "checkinTime"

    const val ALERT_GOOGLE_FIT = "googleFit"
    const val ALERT_GPS = "GPS"
    const val ALERT_FIRST_LOGIN = "firstLogin"

    //leaderboard
    const val DAY = "Day"
    const val WEEK = "Week"
    const val MONTH = "Month"
    const val YEAR = "Year"


    // API Constants
    const val HTTP_STATUS_CREATED_CODE = 200
    const val HTTP_STATUS_CREATED_CODE_202 = 202

    const val LOGGEDIN_USER_PERSONAL_MAIL_ID = "PersonalEmailId"
    const val LOGGEDIN_USER_PHONE = "Phone"
    const val LOGGED_IN_USER_GENDER = "Gender"
    const val LOGGED_IN_USER_DOB = "DOB"
    const val PROFILE_PICTURE = "ProfilePicture"
    const val PROFILE_COMPLETION_STATUS = "ProfileStatus"
    const val CHECKIN_BONUS_RECEIVED = "CheckInBonusReceived"
    const val CHALLENGE_BONUS_RECEIVED = "ChallengeBonusReceived"
    const val PROFILE_COMPLETION_BONUS_RECEIVED = "ProfileCompletionBonusReceived"

    const val REWARD_FLAG = "RewardFlag"
    const val ALL_REWARDS = "AllRewards"
    const val MY_REWARDS = "MyRewards"
    const val BALANCE = "Balance"

    const val TITLE = "title"
    const val SUB_TITLE = "subTitle"
    const val POINTS_REQUIRED = "pointsRequired"
    const val DESCRIPTION = "description"
    const val IMAGE_URL = "imageUrl"
    const val STATUS = "status"
    const val VENUE_NAME = "VenueName"
    const val CHECKIN_ID = "CheckinId"
    const val DOB = "dob"
    const val EMPLOYEE_ID = "EmployeeId"
    const val LATITUDE = "Latitude"
    const val LONGITUDE = "Longitude"

    //Notification
    const val NOTIFICATION_FLAG = "NotificationFlag"
    const val FROM_CHECKIN = "FromCheckin"
    const val FROM_CHALLENGE = "FromChallenge"
    const val CHALLENGE_24_HOUR_ID = 1

    //checkin
    const val CHECKIN_HOUR = "CheckinHour"
    const val CHECKIN_MIN = "CheckinMin"
    const val CHECKIN_SEC = "CheckinSec"

    const val INTENT_NOTIFICATION_DATA = "IntentNotificationData"
}// This utility class is not publicly instantiable
