package br.com.integracaojr.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.integracaojr.upload.UploadTodos;


@WebServlet("/pages/ArapongasServlet")
public class ArapongasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public ArapongasServlet() {
        super();
    }



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int Entidade = Integer.valueOf(request.getParameter("entidade"));
		int Exercicio = Integer.valueOf(request.getParameter("exercicio"));
		int Mes = Integer.valueOf(request.getParameter("mes"));
		int Cliente = Integer.valueOf(request.getParameter("entidade"));
		
		String dataBase = "";
		
		if(Entidade == 326) {
			dataBase = "fpterraroxa";			
		}else if(Entidade == 8787) {
			dataBase = "elotech";			
		}	
		
		
		UploadTodos vTodosUpload = new UploadTodos(dataBase);

		vTodosUpload.getTodosUpload(Entidade, Exercicio, Mes, Cliente);
		System.out.println("Gerado com sucesso!!");

		ServletContext context = request.getServletContext();
		String meses = null;
		
		if (Mes == 1) {
			meses = "Janeiro";
			
		}else if (Mes == 2) {
			meses = "Fevereiro";
			
		}else if (Mes == 3) {
			meses = "Marco";
			
		}else if (Mes == 4) {
			meses = "Abril";
			
		}else if (Mes == 5) {
			meses = "Maio";
			
		}else if (Mes == 6) {
			meses = "Junho";
			
		}else if (Mes == 7) {
			meses = "Julho";
			
		}else if (Mes == 8) {
			meses = "Agosto";
			
		}else if (Mes == 9) {
			meses = "Setembro";
			
		}else if (Mes == 10) {
			meses = "Outubro";
			
		}else if (Mes == 11) {
			meses = "Novembro";
			
		}else if (Mes == 12) {
			meses = "Dezembro";
			
		}

		String fileUrl = "C:\\integracao\\"+Exercicio+"\\"+meses+".zip";

		// Construir o caminho completo e absoluto do arquivo
		File downoadFIle = new File(fileUrl);
		FileInputStream inputStream = new FileInputStream(downoadFIle);

		// Obter o tipo MIME do arquivo
		String mimeType = context.getMimeType(fileUrl);
		if (mimeType == null) {
			// define como tipo binário se mapeamento mime não for encontrado
			mimeType = "application/octet-stream";
		}
		// define atributos pra resposta
		response.setContentType(mimeType);
		response.setContentLength((int) downoadFIle.length());

		// Definir cabeçalhos para a resposta
		String headerKey = "Content-Disposition";
		String headerValeu = String.format("attachment; filename=\"%s\"", downoadFIle.getName());
		

		response.setHeader(headerKey, headerValeu);
		
		// Obter fluxo de saida da resposta
		OutputStream outputStream = response.getOutputStream();
		
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		
		// Escrever bytes lidos a partir do fluxo de entrada para o fluxo de saida
		
		while ((bytesRead = inputStream.read(buffer)) != -1){
			outputStream.write(buffer,0, bytesRead);
		}
		
		inputStream.close();
		outputStream.close();

	}

	}
