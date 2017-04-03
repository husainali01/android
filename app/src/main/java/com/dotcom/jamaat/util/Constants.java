package com.dotcom.jamaat.util;import com.dotcom.jamaat.BuildConfig;/** * Created by anjanik on 19/4/16. */public class Constants {    // public static String API_BASE_URL = "http://192.168.29.104:8000/v1/api/";// public static String API_BASE_URL = "https://rc.indianangelnetwork.com/v1/api/";    public static String API_BASE_URL = "http://www.dotcomsolutiononline.com/android/bau";    // public static String API_BASE_URL = "http://192.168.29.102/v1/api/";    public static String LOCAL_SPLUNK_KEY = "20ae2b4f";    public static String BASE_URL = "BASEURL";    public static String UNSEEN_NOTIFICATION_COUNT ="Unseen Notification Count";    public static String DEALSLIST = "deals/?workflow_step=";    public static String GET_TIME = "/gettime.php";    public static String CHANGEPASSWORD = "accounts/change-password/";    public static String FORGOTPASSWORD = "assets/global/php/forget_password.php";    public static String LOGIN = "/login.php";    public static String LOGOUT = "accounts/logout/";    public static String DASHBOARD = "assets/custom/dashboard/retreive.php";    public static String CHECK_FOLIO = "assets/android/checkfn.php";    public static String RETRIVE_MOHALLA = "/assets/android/fillMohalla.php";    public static String CANCEL_THALI = "assets/android/cancel_thali/create.php";    public static String DEALID = "dealid";    public static String USERID = "userid";    public static String COMMITTEDUSERID="committeduserId";    public static String COMMENTID = "commentId";    public static String MEETINGID = "meetingId";    public static String ISLIKE = "islike";    public static String WORKFLOWSTEP = "workFlowStep";    public static String ISFAVOURITE = "ISFAVOURITE";    public static String MEETING ="deals/"+DEALID +"/meetings/";    public static String MEETING_GLOBAL ="meetings/";    public static String PAST_MEETINGS = "meetings/past_meetings/";    public static String DISCUSSION ="deals/"+DEALID +"/comments/";    public static String POST_DISCUSSION ="deals/"+DEALID +"/comments/";    public static String EDIT_DISCUSSION ="deals/"+DEALID+"/comments/"+COMMENTID+"/";    public static String MEETING_DETAILS="meetings/"+MEETINGID;    public static String MEETING_COMMENTS = "meetings/"+MEETINGID+"/comments/";    public static String POST_MEETING_COMMENTS ="meetings/"+MEETINGID +"/comments/";    public static String NOTIFICATIONS_LIST = "/refresh.php";    public static String DISCUSSION_DETAILS ="deals/"+DEALID+"/comments/"+WORKFLOWSTEP;    public static String DELETE_COMMENT  = "deals/"+DEALID+"/comments/"+COMMENTID;    public static String MAKE_DEAL_FAVOURITE = "deals/"+DEALID+"/favourite/"+ISFAVOURITE;    public static String COMMENT_LIKE = "deals/"+DEALID+"/comments/"+COMMENTID+"/like/"+ISLIKE+"/";    public static String DELETE_COMMITMENT = "deals/"+DEALID+"/subscription/commitment/"+COMMITTEDUSERID+"/delete/";    public static String EDIT_COMMITMENT = "deals/"+DEALID+"/subscription/commitment/"+COMMITTEDUSERID+"/edit/";    public static String SUBSCRIPTIONDETAIL ="deals/"+DEALID +"/subscription/commitment/?";    public static String SUBSCRIPTIONDETAIL_FILTER ="subscriptionFilter";    public static String COMMITTED_DETAIL_FILTER ="committedFilter";    public static String SUBSCRIPTIONDETAILSORT ="deals/"+DEALID +"/subscription/commitment/?sortBy=";    public static String COMMITTEDDETAIL ="deals/"+DEALID +"/subscription/committed/?";    public static String SUBSCRIPTION ="deals/"+DEALID +"/subscription";    public static String DEALLEADS ="deals/"+DEALID +"/subscription/lead_investors/";    public static String ADD_SUBSCRIPTION ="deals/"+DEALID +"/subscription/commitment/term_and_condition/";    public static String ADD_SUBSCRIPTION_DETAIL ="deals/"+DEALID +"/subscription/commitment/add/";    public static String ADD_SUBSCRIPTION_DETAIL_POST ="CommitmentPost";    public static String TRANCHES_DETAIL = "deals/"+DEALID +"/subscription/"+ USERID+"/trenches";    public static String DEALS_FILTER = "deals/filter/";    public static String DEALS_FILTER_APPLY ="deals/?";    public static String MEMBERSHIP_LIST = "membership/?status=";    public static String MEMBERDETAILVIEW = "membership/";    public static String MEMBERID = "memberId";    public static String WORKFLOW_STATUS = "deals/workflow_status/";    public static String NOTIFICATION_READ = "notification_read/";    public static String DEALSORT ="&sortBy=";    public static String MARK_ALL_READ = "marked_all_read/";    public static String COMMITMENTSORT ="sortBy=";    public static String FILTER_DEALSORT ="deals/?sortBy=";    public static int FILTER_REQUEST_CODE = 2;    public static String RECOMMEND_MEMBERSHIP = "membership/";    public static String RECOMMEND_MEMBERSHIP_TAG = "recommend";    // v1/api/deals/<dealId>/subscription/    //http://192.168.10.235:8000/v1/api/deals/<dealId>/comments/    public final static String API_BASE_URL_KEY = "API_BASE_URL_KEY";    public final static String IANAPP_PREFERENCE_FILE_KEY = "IANAPP_PREFERENCE_FILE_KEY";    public final static String CHARSET = "utf-8";    public final static String CONTENT_TYPE_JSON = "application/json";    public final static int NETWORK_SOCKET_TIMEOUT = 30000;    public final static String TOKEN = "token";    public final static String SORT = "sort";    public final static String USERTYPE = "userType";    public final static String ALLOWEDSTATUS = "allowedStatus";    public final static String SAVEPASSWORD = "savepassword";    public final static String DEFVALUE = "IANAPP";    public static String device_id = null;    public final static String BROADCAST_ACTION_NOTIFICATION_RECIEVED = "BROADCAST_ACTION_NOTIFICATION_RECIEVED";    public final static String EMPTY_STRING = "";    public final static int NOTIFICATION_ID = 1;    public final static String GCM_REGISTRATION_TOKEN = "GCM_REGISTRATION_TOKEN";    public static final int REQUEST_CODE_TAKE_PICTURE = 1034;// camera    public static String getDeviceId() {        return (device_id == null || device_id.isEmpty()) ? "1" : device_id;    }    public static void updateAPIUrl(String apiUrl) {        API_BASE_URL = apiUrl;        SharedPreferencesManager.setPreference(API_BASE_URL_KEY, API_BASE_URL);    }    public static String getBaseAPIUrl() {        return API_BASE_URL;    }    public static String getAPIUrl(String apiUrl) {        return API_BASE_URL + apiUrl;    }}