<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<!--

  Copyright (c) 2001-2015 BMC Software, Inc.
  All rights reserved.

  This software is the confidential and proprietary information of
  BMC Software, Inc ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement between
  you and BMC Software, Inc.
-->

<html>
<%@ page import = "com.remedy.arsys.share.MessageTranslation" %>
<%@ page import = "com.remedy.arsys.config.Configuration" %>
<%@ page import = "com.remedy.arsys.stubs.SessionData" %>
<%@ page import="com.remedy.arsys.session.HttpSessionKeys" %>
<%@ page import="com.remedy.arsys.session.Params" %>
<%@ page import="com.remedy.arsys.stubs.HomeServlet" %>
<%@ page import="com.remedy.arsys.stubs.ARServerUserBean" %>
<%@ page import="com.remedy.arsys.session.Login" %>
<jsp:useBean id="userbean" class="com.remedy.arsys.stubs.ARServerUserBean" scope="request">
</jsp:useBean>
<%
        //next line in conjuction with <meta http-equiv="Pragma" content="no-cache"> is to
        //patch for those users who bookmark the logout JSP
        // Trying to fix those automation test failures because of logout.jsp problem, 
        // for those invalid session with goto URL string, don't go back to midtier to avoid the loop.
    String queryString = request.getQueryString();
	String gotoURL = request.getParameter(Params.GOTO_URL);
	if (session.getAttribute(SessionData.SESSION_DATA)!=null || ((queryString != null) && (queryString.length() > 0))) {
    	if (session.getAttribute(SessionData.SESSION_DATA)!=null) {
    		// if we still have session_data, we need to go back to midtier to logout this user from AR server
        	response.sendRedirect(request.getContextPath()+"/servlet/LogoutServlet?" + queryString);
    	} else if ((gotoURL != null) && (gotoURL.length() > 0) && !gotoURL.equals("null")) {
    		// we don't have the session_data, we don't have to go back to midtier to logout, just go to the URL directory if we can
    		// (From Venkat)Refine the previous version, it is better to check for relative path and assume everything else as absolute path.  
    		// In previous version if someone gives URL that starts with HTTPS (uppercase) or FTP or some other protocol, it doesn’t work.
    		String nextURL = gotoURL;
    		String contextpath = request.getContextPath();
    		if (gotoURL.startsWith("/") && !gotoURL.startsWith(contextpath))
    			nextURL = contextpath + nextURL;
    		response.sendRedirect(nextURL);
    	}
    } else {
    	response.sendRedirect(request.getContextPath() + Login.LOGGEDOUT_URL);
    }
	
    
%>
<head>
<meta http-equiv="Pragma" content="no-cache">

<%@ page contentType="text/html; charset=utf-8"%>
 </head>

<body style="background-repeat: no-repeat; margin:0px;">
</body>
</html>
