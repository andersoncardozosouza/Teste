package br.com.integracaojr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.integracaojr.data.ConexaoElotech;
import br.com.integracaojr.data.ConexaoTerraRoxa;
import br.com.integracaojr.entidade.Acao;
import br.com.integracaojr.entidade.FonteRecurso;

public class AcaoDao {
	
	private static Connection connection;

	public AcaoDao(String dataBase) {
		if (dataBase == "fpterraroxa") {
			this.connection = ConexaoTerraRoxa.getConnection(dataBase);
		} else {
			dataBase = "elotech";
			this.connection = ConexaoElotech.getConnection(dataBase);
		}
	}

	public boolean getAcao(Integer entidade, Integer exercicio, Integer cliente) {

		if (entidade != null && exercicio != null && cliente != null) {
			String sqlAcao = "select " + cliente + " entidade, \r\n"
					+ "       substring(d.programatica,15,4) as eloprojetoatividade,\r\n"
					+ "       '' elodescricao,\r\n" + "       '' ipmtipo \r\n" + " from siscop.despesa d\r\n"
					+ "  where d.entidade = ? \r\n" + "    and d.exercicio = ? \r\n" + "    and d.movsn = 'S'\r\n"
					+ "    and not exists\r\n" + "    (select * from intacao ia\r\n"
					+ "        where ia.eloprojetoatividade = substring(d.programatica,15,4) \r\n"
					+ "          and ia.entidade = " + cliente + " )\r\n"
					+ "   group by substring(d.programatica,15,4)";
			try {
				PreparedStatement psa = connection.prepareStatement(sqlAcao);

				psa.setInt(1, entidade);
				psa.setInt(2, exercicio);

				ResultSet rsa = psa.executeQuery();

				while (rsa.next()) {
					Acao acao = new Acao();
					acao.setEntidade(cliente);
					acao.setEloprojetoatividade(rsa.getString("eloprojetoatividade"));
					acao.setElodescricao(rsa.getString("elodescricao"));
					acao.setIpmtipo(rsa.getString("ipmtipo"));

					String sqlarsalvar = "INSERT INTO intacao(entidade,eloprojetoatividade,elodescricao,ipmtipo) VALUES (?,?,?,?)";
					try {
						PreparedStatement psia = connection.prepareStatement(sqlarsalvar);

						psia.setInt(1, cliente);
						psia.setString(2, rsa.getString("eloprojetoatividade"));
						psia.setString(3, rsa.getString("elodescricao"));
						psia.setString(4, rsa.getString("ipmtipo"));

						psia.execute();
						psia.close();

					} catch (SQLException e) {
						e.printStackTrace();

					}

				}

				return true;

			} catch (SQLException e) {
				e.printStackTrace();

			}

			return false;
		}

		return true;
	}

}
