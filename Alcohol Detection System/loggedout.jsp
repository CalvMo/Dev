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
<jsp:useBean id="userbean" class="com.remedy.arsys.stubs.ARServerUserBean" scope="request">
</jsp:useBean>
<%
        //next line in conjuction with <meta http-equiv="Pragma" content="no-cache"> is to
        //patch for those users who bookmark the logout JSP
        // Trying to fix those automation test failures because of logout.jsp problem, 
        // for those invalid session with goto URL string, don't go back to midtier to avoid the loop.
    String locale = SessionData.getLocale(request);
    String shortVersion = Configuration.getInstance().getShortClientVersion();
    String titleStr     = "BMC&nbsp;Remedy&nbsp;Mid&nbsp;Tier&nbsp;" + com.remedy.arsys.share.HTMLWriter.escape(shortVersion);

    String bgimg = "images/bkgd_image.gif";
	String logoimg = "images/bmc_logo.gif";

    // a block of code moved to ARServerUserBean.java to work around the issue in weblogic
    String iframeurl = userbean.calculateIframeURLForLogout(request, session, locale);
%>
<head>
<meta http-equiv="Pragma" content="no-cache">
<style type="text/css">
    <!--
    BODY      {font-family:Tahoma,arial,helvetica,verdana;}
    .Logout   {font-size:16pt; font-weight:bolder;}
    iframe { width:100%; height:100%; border:none}
    .ReturnHome {font-size:10pt;}
#product {position: absolute; top:20px; left: 0px; font: 16px "Arial", "Lucida Grande", "Verdana", "Helvetica", sans-serif; font-weight: bold; color: #ffffff; padding-left:18px; text-align:left;}
#logo {position: absolute; top:15px; right:0px}
    -->
</style>

<%@ page contentType="text/html; charset=utf-8"%>
<link rel="SHORTCUT ICON" href="<%=request.getContextPath() + "/resources/images/favicon.ico"%>">
<title><%=titleStr%> - <%= MessageTranslation.getLocalizedText(locale, "Logout")%></title><!--;-->
 </head>

<body style="background-repeat: no-repeat; margin:0px;" onload="setTimeout(function(){document.body.focus()},0)">
<table border="0px" cellpadding="0px" cellspacing="0px" width="100%">
	<!-- header table -->
	<tr>
		<td width="100%">
			<table border="0px" cellpadding="0px" cellspacing="0px" width="100%" height="60px">
			<tr>
				<td height="60px" background="<%=bgimg%>">
				<!--span id="product"><%=titleStr%></span-->
				<div id="logo" align="right">
					<!--img src="<%=logoimg%>" width="118px" height="26px" hspace="20" alt="BMC logo"--> 
				</div>
				</td>
			</tr>
			</table>
		</td>
	</tr>
</table>

<table>
    <tr >
        <td width="250px" height="115px" ></td>
        <td></td><td><br></td>
    </tr>
    <tr>
        <td class="Logout" colspan="3" align="center" ><%= MessageTranslation.getLocalizedText(locale, "You have successfully logged out")%><!--;--></td>
    </tr>
    
    <tr >
    <td width="250px" height="25px" ></td>
    	<td></td><td><br></td>
    </tr>
    
    <tr>
     <td>
    &nbsp;
    </td>
    <td class="ReturnHome" align='center'>
    <%
        // put in a link to the home page if user has one
        String localizedLink = null;
        String homeurl = null;
        try {
          String hs = HomeServlet.getHomeServer(request);
          if (hs != null && HomeServlet.getHomeForm(hs) != null) {
            homeurl=request.getContextPath() + "/home";
            localizedLink = MessageTranslation.getLocalizedText(locale, "Return to home page");
          }
        } catch (Throwable e) {
          //ignore, we don't care error here because we are already in a error processing.
        }

        if (localizedLink != null) {
    %>
            <a href=<%=homeurl%>><%=localizedLink%></a>
    <%
        }
        else
        {
    %>
            &nbsp;
    <%
        }
    %>
    </td>
    </tr>    
    
    <tr>
        <td width="33%"></td>
        <td width="33%"><br></td>
        <td width="33%"></td>
    </tr>
    <tr>
        <td width="33%"></td>
        <td width="33%"><br></td>
        <td width="33%"></td>
    </tr>
        <tr>
          <td colspan="3">
        <% if(iframeurl != null) {%>
        <iframe align="middle" frameborder="1" scrolling="no" src="<%=iframeurl%>"></iframe>
        <%}%>
          </td>

        </tr>
</table>
</body>
</html>
