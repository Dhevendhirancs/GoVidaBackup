Project Name : GoVida 

Project Platform : Android

Project Platform Code in this Repo : Android

Project Language : Kotlin

Project Author : Dhevendhiran M

Project Conception  : 17 April 2017

Project: GoVida
Subject : Developer Guideline
Points to note when working as a contributor:
1. All class variable should start with camel case with first letter as m
	example : mBtnAccept, mRvChallenge, mIsUserLoggedIn
2. All class method should start with camel case.
	example : processAccept()
3. All the action method should be written as follows: Methods in presenter
	example : onLoginButtonClicked(), onRegisterButtonClicked() 
4. All the action method should be written as follows: Methods in Models
	example : processLogin(), processRegister() 
5.  All the UI action method should be written as follows: Methods in View
	example : onLoginFailed(), moveToNextPage()
6. Once the coding is done on class, analyse the Class using lint. :Option already present in android studio.
	Process: Right click on class name ->analyse->insept code-> resolve issue mentioned by process
7. On Class comment required as follows :
	/**
	 * @Class : ActivityBeConnected
	 * @Usage : This activity is used for providing permission information
	 * @Author : 1276
	 */ 
8. For methid comments are required as followig format: 
	 /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1276
     */

     /**
     *  @Function : onClick()
     *  @params   : View
     *  @Return   : void
     * 	@Usage	  : listener function definition
     *  @Author   : 1276
     */
9. Every class and method are required to have comments without comments pull requirest will be accepted.
10. Method varaible are only require to follow Camel case standard. Method variable will not have m and next Letter letetr capital making it look like class variable, 	
	is not permitted.
	example : val builder = AlertDialog.Builder(this)
11. Every string must be taken from string.xml and use concat function present in android, except for exceptional cases such as unicode character like % or $ 
12. id of the xml should be in folloeing format : 
	example: 	login_btn_login, login_iv_show_password, login_et_email

