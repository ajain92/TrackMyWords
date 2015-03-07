package com.trackmywords.backend;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/ValidateQuery")
public class QueryFilter implements Filter {

	private String username, password;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (!validUserPassCombo(request)) {
			HttpServletResponse http = (HttpServletResponse) response;
			http.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			http.setContentType("text/html");
			request.setAttribute("title", "Unauthorized Access");
			request.setAttribute("message", "An invalid username/password combination provided.");
			request.getRequestDispatcher("/pages/invalid/simple.jsp").forward(request, response);
		} else {
			// do validation of post parameters here
			
			chain.doFilter(request, response);
		}
	}

	private boolean validUserPassCombo(ServletRequest request) {
		String auth = request.getParameter("auth");
		if (auth == null || auth.trim().equals("")) {
			return false;
		}
		auth = auth.trim();
		if (!auth.contains(":")) {
			return false;
		}
		String[] parts = auth.split(":");
		if (parts.length != 2) {
			return false;
		}
		if (!parts[0].equals(username) || !parts[1].equals(password)) {
			return false;
		}
		return true;
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		username = fConfig.getInitParameter("username");
		password = fConfig.getInitParameter("password");
	}

	@Override
	public void destroy() {
	}

}