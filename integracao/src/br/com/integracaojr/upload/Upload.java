package br.com.integracaojr.upload;

import java.util.Arrays;

import javax.persistence.Lob;

public class Upload {
	private int id;
	private String descricao;
	private String ano;
	private String mes;

	@Lob
	private byte[] arquivo;

	public Upload() {
		super();

	}

	@Override
	public String toString() {
		return "Upload [id=" + id + ", descricao=" + descricao + ", ano=" + ano + ", mes=" + mes + ", arquivo="
				+ Arrays.toString(arquivo) + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}


}
