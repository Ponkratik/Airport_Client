package com.ponkratov.airport.client.tcpconnection;

public class CommandType {
    public static String REGISTER = "REGISTER";
    public static String AUTHENTICATE = "AUTHENTICATE";
    public static String LOGOUT = "LOGOUT";

    public static String FINDALLUSERS = "FINDALLUSERS";
    public static String CHANGEPASSWORD = "CHANGEPASSWORD";
    public static String RESTOREPASSWORD = "RESTOREPASSWORD";
    public static String BLOCKUSER = "BLOCKUSER";
    public static String UPDATEUSERROLE = "UPDATEUSERROLE";

    public static String FINDALLROLES = "FINDALLROLES";
    public static String FINDROLEBYID = "FINDROLEBYID";
    public static String FINDROLEBYNAME = "FINDROLEBYNAME";

    public static String DEFAULT = "DEFAULT";
}
