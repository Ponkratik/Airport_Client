package com.ponkratov.airport.client.tcpconnection;

public class CommandType {
    public static String REGISTER = "REGISTER";
    public static String AUTHENTICATE = "AUTHENTICATE";
    public static String LOGOUT = "LOGOUT";

    public static String FINDALLUSERS = "FINDALLUSERS";
    public static String FINDUSERBYLOGIN = "FINDUSERBYLOGIN";
    public static String FINDUSERSBYLOGINREGEXP = "FINDUSERSBYLOGINREGEXP";
    public static String FINDUSERBYEMAIL = "FINDUSERBYEMAIL";
    public static String FINDUSERSBYROLE = "FINDUSERSBYROLE";
    public static String FINDUSERSBYNAMEREGEXP = "FINDUSERSBYNAMEREGEXP";
    public static String CHANGEPASSWORD = "CHANGEPASSWORD";
    public static String RESTOREPASSWORD = "RESTOREPASSWORD";
    public static String BLOCKUSER = "BLOCKUSER";
    public static String UPDATEUSERROLE = "UPDATEUSERROLE";
    public static String UPDATEUSER = "UPDATEUSER";

    public static String FINDALLROLES = "FINDALLROLES";
    public static String FINDROLEBYID = "FINDROLEBYID";
    public static String FINDROLEBYNAME = "FINDROLEBYNAME";

    public static String FINDALLPLANES = "FINDALLPLANES";
    public static String CREATEPLANE = "CREATEPLANE";
    public static String UPDATEPLANE = "UPDATEPLANE";
    public static String DELETEPLANE = "DELETEPLANE";
    public static String FINDPLANEBYNUMBER = "FINDPLANEBYNUMBER";
    public static String FINDPLANESBYMODELREGEXP = "FINDPLANESBYMODELREGEXP";

    public static String DEFAULT = "DEFAULT";
}
