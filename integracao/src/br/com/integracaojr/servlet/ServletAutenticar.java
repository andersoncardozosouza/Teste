package br.com.integracaojr.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.integracaojr.entidade.UserLogado;




@WebServlet("/ServletAutenticar")
public class ServletAutenticar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ServletAutenticar() {
        super();
    }
    
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String entidade = request.getParameter("entidade");		

		
		if(login.equalsIgnoreCase("admin") && senha.equalsIgnoreCase("123") && entidade.equalsIgnoreCase("8787")) {
			
			UserLogado userLogado = new UserLogado();
			userLogado.setLogin(login);
			userLogado.setSenha(senha);
			
			
			
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
			
			session.setAttribute("usuario", userLogado);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/arapongas.jsp");
			dispatcher.forward(request, response);
			
		}else if(login.equalsIgnoreCase("admin") && senha.equalsIgnoreCase("123") && entidade.equalsIgnoreCase("326")) {
			
			UserLogado userLogado = new UserLogado();
			userLogado.setLogin(login);
			userLogado.setSenha(senha);
			
			
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
			
			session.setAttribute("usuario", userLogado);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/terraroxa.jsp");
			dispatcher.forward(request, response);
			
		}else if(login.equalsIgnoreCase("admin") && senha.equalsIgnoreCase("123") && entidade.equalsIgnoreCase("12634")) {
			
			UserLogado userLogado = new UserLogado();
			userLogado.setLogin(login);
			userLogado.setSenha(senha);
			
			
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
			
			session.setAttribute("usuario", userLogado);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/guaira.jsp");
			dispatcher.forward(request, response);
			
		}else {
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/autenticar.jsp");
			dispatcher.forward(request, response);
		}
	}

}
