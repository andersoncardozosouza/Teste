package br.com.integracaojr.filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.integracaojr.data.ConexaoElotech;
import br.com.integracaojr.data.ConexaoTerraRoxa;
import br.com.integracaojr.entidade.UserLogado;


@WebFilter(urlPatterns = { "/pages/*" })
public class Filter implements javax.servlet.Filter {
	
	private static Connection connection;
   
    public Filter() {
    }

	
	public void destroy() {
	
	}

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		try {
			connection.commit();
			
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
			
			
			String urlParaautenticar = req.getServletPath();
			
			UserLogado userLogado = (UserLogado) session.getAttribute("usuario");	
			
			if(userLogado == null && !urlParaautenticar.equalsIgnoreCase("/pages/ServletAutenticar")) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/autenticar.jsp?url="+urlParaautenticar);
				dispatcher.forward(request, response);
				
				
			}
			 chain.doFilter(request, response);
			 
		} catch (Exception e) {
			e.printStackTrace();
			
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		
		connection = ConexaoElotech.getConnection("elotech");
	}

}
