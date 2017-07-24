package objects


class EPError {
    var errorDesc = ""
    var error: Int? = 0

    enum class Error {
        OK,
        USER_NULL,
        INVALID_INFO,
        USER_EXISTS,

        WRONG_PASSWORD,

        USER_NOT_AUTHENTICATED,
        ACCOUNT_NOT_VALIDATED,

        MEMBER_NOT_FOUND,
        MEMBER_WRONG_CREDENTIALS,
        MEMBER_NAUGHTY,
        MEMBER_VAL_TOS,
        BAD_USERNAME,
        BAD_PASSWORD,

        DB_ERROR,
        SERVER_COMMUNICATION_ERROR,
        FILE_UPLOAD_ERROR,
        BAD_CAPTCHA
    }
}
