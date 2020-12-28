package br.com.integracaojr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.integracaojr.data.ConexaoElotech;
import br.com.integracaojr.data.ConexaoTerraRoxa;
import br.com.integracaojr.entidade.Dotacao;

public class DotacaoDao {
	
	private static Connection connection;

	public DotacaoDao(String dataBase) {
		if (dataBase == "fpterraroxa") {
			this.connection = ConexaoTerraRoxa.getConnection(dataBase);
		} else {
			dataBase = "elotech";
			this.connection = ConexaoElotech.getConnection(dataBase);
		}
}
public boolean getDotacao(Integer entidade, Integer exercicio, Integer cliente) {
	if (cliente != null) {
		String sqlDotacao = 
				"select "+cliente+" entidade, \r\n"
				+ "       d.exercicio eloexercicio, \r\n"
				+ "	   d.programatica eloprogramatica,\r\n"
				+ "	   d.fonterecurso elofonterecurso,\r\n"
				+ "	   dd.despesa elodespesa, \r\n"
				+ "       dd.desdobramento elodesdobramento, \r\n"
				+ "	   dd.subdesdobramento elosubdesdobramento, \r\n"
				+ "	   dd.descricao elodescricao, \r\n"
				+ "	   dd.despesa||dd.desdobramento||dd.subdesdobramento ipmelemento, \r\n"
				+ "	   dd.descricao ipmdescricao,\r\n"
				+ "	   d.fonterecurso ipmvinculo,\r\n"
				+ "	   (select imptipo from intacao ia \r\n"
				+ "	      where ia.entidade = "+cliente+" \r\n"
				+ "		    and ia.eloprojetoatividade = substring(d.programatica,15,4))  ipmtipoacao\r\n"
				+ "   from siscop.despesa d\r\n"
				+ "      inner join siscop.desdobradesp dd\r\n"
				+ "	          on d.entidade = dd.entidade \r\n"
				+ "		     and d.exercicio = dd.exercicio \r\n"
				+ "		     and substring(d.programatica,19,6) = dd.despesa\r\n"
				+ "			 and dd.movsn = 'S'			  \r\n"
				+ "  where d.entidade = ? \r\n"
				+ "    and d.exercicio = ? \r\n"
				+ "	and d.movsn = 'S' \r\n"
				+ "	and not exists\r\n"
				+ "	(select * from intdotacao id\r\n"
				+ "           where id.entidade = "+cliente+" \r\n"
				+ "             and id.eloexercicio = d.exercicio \r\n"
				+ "             and id.eloprogramatica = d.programatica\r\n"
				+ "             and id.elofonterecurso = cast(d.fonterecurso as varchar(8))\r\n"
				+ "             and id.elodespesa = dd.despesa\r\n"
				+ "             and id.elodesdobramento = dd.desdobramento\r\n"
				+ "             and id.elosubdesdobramento = dd.subdesdobramento)";
		try {
			PreparedStatement psd = connection.prepareStatement(sqlDotacao);

			psd.setInt(1, entidade);
			psd.setInt(2, exercicio);

			ResultSet rsd = psd.executeQuery();
			while (rsd.next()) {
				Dotacao dotacao = new Dotacao();

				dotacao.setEntidade(cliente);
				dotacao.setEloexercicio(rsd.getInt("eloexercicio"));
				dotacao.setEloprogramatica(rsd.getString("eloprogramatica"));
				dotacao.setElofonterecurso(rsd.getInt("elofonterecurso"));
				dotacao.setElodespesa(rsd.getString("elodespesa"));
				dotacao.setElodesdobramento(rsd.getString("elodesdobramento"));
				dotacao.setElosubdesdobramento(rsd.getString("elosubdesdobramento"));
				dotacao.setElodescricao(rsd.getString("elodescricao"));
				dotacao.setIpmelemento(rsd.getString("ipmelemento"));
				dotacao.setIpmdescricao(rsd.getString("ipmdescricao"));
				dotacao.setIpmvinculo(rsd.getString("ipmvinculo"));
				dotacao.setIpmtipoacao(rsd.getString("ipmtipoacao"));

				String sqldsalvar = 
						"INSERT INTO public.intdotacao(\r\n"
						+ "            entidade, eloexercicio, eloprogramatica, elofonterecurso, elodespesa, \r\n"
						+ "            elodesdobramento, elosubdesdobramento, elodescricao, ipmelemento, \r\n"
						+ "            ipmdescricao, ipmvinculo, ipmtipoacao)\r\n"
						+ "    VALUES (?, ?, ?, ?, ?, \r\n"
						+ "            ?, ?, ?, ?, \r\n"
						+ "            ?, ?, ?)";
				try {
					PreparedStatement psid = connection.prepareStatement(sqldsalvar);

					psid.setInt(1, cliente);
					psid.setInt(2, rsd.getInt("eloexercicio"));
					psid.setString(3, rsd.getString("eloprogramatica"));
					psid.setInt(4, rsd.getInt("elofonterecurso"));
					psid.setString(5, rsd.getString("elodespesa"));
					psid.setString(6, rsd.getString("elodesdobramento"));
					psid.setString(7, rsd.getString("elosubdesdobramento"));
					psid.setString(8, rsd.getString("elodescricao"));
					psid.setString(9, rsd.getString("ipmelemento"));
					psid.setString(10, rsd.getString("ipmdescricao"));
					psid.setString(11, rsd.getString("ipmvinculo"));
					psid.setString(12, rsd.getString("ipmtipoacao"));
					
					psid.execute();
					psid.close();
					
					

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Não existe conta a ser cadastrado");
		return false;		

	}
	System.out.println("Gerado Com Sucesso Dotação");
	return true;
	}

}
