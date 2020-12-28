package br.com.integracaojr.teste;

import java.io.FileNotFoundException;

import br.com.integracaojr.upload.UploadTodos;



public class TodosUpload {
	public static void main(String[] args) {

		UploadTodos vUpload = new UploadTodos("fpterraroxa");	
			
				try {
					vUpload.getTodosUpload(1, 2020, 11, 8787);
					System.out.println("Gerado Com Sucesso");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

}
