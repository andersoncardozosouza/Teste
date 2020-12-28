package br.com.integracaojr.teste;

import br.com.integracaojr.dao.FonteRecursoDao;

public class TesteFonteRecurso {

	public static void main(String[] args) {
		
		FonteRecursoDao fonteRecursoDao = new FonteRecursoDao("fpterraroxa");	
		
		fonteRecursoDao.getFonteRecurso(326);
		System.out.println("Gerado Com Sucesso");
	
		

	}

}
