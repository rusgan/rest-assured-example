package org.sora.rest.account;

public final class AccountEndPoints {

    public static final String USER = "/user";
    public static final String USER_INVITED = "/user/invited";
    public static final String USER_REGISTER = "/user/register";
    public static final String USER_CREATE = "/user/create";
    public static final String userValues = "/user/values";

    public static final String ADMIN_APPLICATIONFORMS = "/admin/applicationforms";
    public static final String ADMIN_APP_FORM_GRANT = "/admin/applicationforms/grant/{appFormId}";

    public static final String APPLICATIONFORMS = "/applicationforms";
    public static final String APPLICATIONSFORM_APP_FORM_ID = "/applicationforms/{appFromId}";

    public static final String INVITATION = "/invitation";
    public static final String INVITATION_IS_SENT_INVITATION_CODE = "/invitation/is_sent/{invitationCode}";
    public static final String INVITATIONS_INVITATION_CODE = "/invitations/{invitationCode}";

    public static final String SMSCODE_SEND = "/smscode/send";
    public static final String SMSCODE_VERIFY = "/smscode/verify";
}
