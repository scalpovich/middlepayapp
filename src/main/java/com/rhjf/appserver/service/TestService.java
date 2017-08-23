package com.rhjf.appserver.service;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/testService")
public class TestService extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2212473870465853452L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		Enumeration<String> enumeration = req.getHeaderNames();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			System.out.println("key:" + key + ", value :" + req.getHeader(key));
		}

		System.out.println("==============================================");

		Enumeration<String> params = req.getParameterNames();
		while (params.hasMoreElements()) {
			String key = params.nextElement();
			System.out.println("key:" + key + ", value :" + req.getParameter(key));
		}
		
		
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/plain; charset=utf-8");
		
		resp.getWriter().print("测试结果");
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
