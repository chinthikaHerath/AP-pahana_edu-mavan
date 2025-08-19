<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pahanaedu.constant.SystemConstants" %>
<%
    // Redirect to login page
    response.sendRedirect(request.getContextPath() + SystemConstants.URL_LOGIN);
%>