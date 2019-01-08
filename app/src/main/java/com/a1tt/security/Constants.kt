package com.a1tt.security

class Constants {

    companion object {
        const val APIKEY_STR: String = "apikey"
        const val RESOURCE_STR: String = "resource"
        const val ALLINFO_BOOL: String = "allinfo"
        const val URL_STR: String = "url"

        const val RESPONCE_CODE_INT: String = "response_code"
        const val SCAN_ID_STR: String = "scan_id"
        const val PERMALINK_STR: String = "permalink"

        const val SCAN_DATE_STR: String = "scan_date"
        const val VERBOSE_MSG_STR: String = "verbose_msg"
        const val POSITIVES_STR: String = "positives"
        const val TOTAL_STR: String = "total"
        const val SCANS_ARR: String = "scans"

        const val DETECT_BOOL: String = "detected"
        const val RESULT_STR: String = "result"

        const val GET_ALL_APPS: Int = 1

        const val SUCCESED_WRITE_URL_TO_DB: Int = 2
        const val GET_SCAN_URL_RESULT: Int = 3
        const val GOT_SCAN_URL_RESULT: Int = 4
        const val SUCCESED_READ_URL_FROM_DB: Int = 5
        const val FAILED_GET_SCAN_URL_RESULT: Int = 6
        const val GET_SCAN_FILE_RESULT: Int = 7
        const val GOT_SCAN_FILE_RESULT: Int = 8
        const val SUCCESED_WRITE_FILE_TO_DB: Int = 9
        const val SUCCESED_READ_FILE_FROM_DB: Int = 10
    }
}