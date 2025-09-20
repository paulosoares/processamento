package br.jus.stf.processamento.service;

// JUnit Assert framework can be used for verification

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.openqa.selenium.WebDriver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.jus.stf.processamento.util.DataBaseConnection;

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

@Component
public class ProcessamentoService {

	private Connection conn;
	private String classeNumeroProcesso = "";
	private String setorOrigemProcesso = "";
	private String setorDestinoProcesso = "";
	private String numeroGuia = "";
	private String anoGuia = "";
	private String setorOrigemGuia = "";
		
	@Autowired private DataBaseConnection dataBaseConnection;
	
	public ProcessamentoService() {
	}
	
	@PostConstruct
	private void init() throws SQLException {
		conn = dataBaseConnection.connect();
	}
	
	private void setUp() throws Exception {
		//This method is executed before the scenario execution starts.
	}
	
	private void recuperarProcessoFisico() throws Exception {
		
		String classe = "";
		String numero = "";
		
		if (setorOrigemProcesso == null || setorOrigemProcesso.equals("")) setorOrigemProcesso = "600000679";
		
		String query = "select dp.sig_classe_proces, dp.num_processo" 
				+	" from stf.desloca_processos dp, judiciario.processo p "
				+	" where dp.cod_orgao_destino = " + setorOrigemProcesso
				+	" and dp.dat_recebimento is not null "
				+	" and dp.flg_ultimo_deslocamento = 'S' "
				+	" and p.tip_meio_processo = 'F' "
				+	" and p.seq_objeto_incidente = dp.seq_objeto_incidente "
				+	" and rownum < 5 "
				// retira os registros em trânsito
				+   " and p.seq_objeto_incidente not in (select a.seq_objeto_incidente from stf.desloca_processos a where a.cod_orgao_origem = dp.cod_orgao_destino and a.seq_objeto_incidente = dp.seq_objeto_incidente and a.flg_ultimo_deslocamento = 'N' and a.dat_recebimento is null ) "
				+   " and p.seq_objeto_incidente not in (select b.seq_objeto_incidente from stf.desloca_processos b where b.cod_orgao_destino = dp.cod_orgao_destino and b.seq_objeto_incidente = dp.seq_objeto_incidente and b.flg_ultimo_deslocamento = 'N' and b.dat_recebimento is null ) " 
				+	" order by dp.ano_guia desc, dp.num_guia desc";
	
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			System.out.println("*** RecuperarProcessoFisico: " +  query);
						
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				classe = result.getString(1);
				numero = result.getString(2);
			}
			
			result.close();
			stmt.close();
		} catch (Exception e){
			System.out.println("*** Exception: "+ e.getMessage());
			e.printStackTrace();
		}
		
		this.classeNumeroProcesso = classe + " " + numero;

	}

	
	private void recuperarGuia() throws Exception {
		
		String setorUsuario = "600000679";
		
		String query = "select g.cod_orgao_origem, g.num_guia, g.ano_guia " 
				+	" from stf.guias g , stf.desloca_processos d "
				+	" where g.cod_orgao_destino = " + setorUsuario
				+	" and g.tip_orgao_origem = '2' "
				+	" and g.num_guia = d.num_guia "
				+	" and g.ano_guia = d.ano_guia "
				+	" and g.cod_orgao_origem = d.cod_orgao_origem "
				+	" and d.dat_recebimento is null "
				+	" order by g.dat_inclusao desc ";
	
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			System.out.println("*** RecuperarGuia: " +  query);
						
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				this.setorOrigemGuia = result.getString(1);
				this.numeroGuia = result.getString(2);
				this.anoGuia = result.getString(3);
			}
			
			result.close();
			stmt.close();
		} catch (Exception e){
			System.out.println("*** Exception: "+ e.getMessage());
			e.printStackTrace();
		}
		
	}

	public void tearDown() throws Exception {
		//This method is executed after the scenario execution finishes.	
	}
	
	public String getClasseNumeroProcesso() throws Exception {
		if (this.classeNumeroProcesso.equals("")){
			recuperarProcessoFisico();
		}
		return this.classeNumeroProcesso;
	}

	public void setClasseNumeroProcesso(String classeNumeroProcesso) {
		this.classeNumeroProcesso = classeNumeroProcesso;
	}
	
	public String getAnoGuia() throws Exception {
		
		if (this.anoGuia.equals("")){
			recuperarGuia();
		}
		return this.anoGuia;
	}
	
	public void setAnoGuia(String ano){
		this.anoGuia = ano;
	}
	
	public String getNumeroGuia() throws Exception {
		
		if (this.numeroGuia.equals("")){
			recuperarGuia();
		}
		return this.numeroGuia;
	}
	
	public void setNumeroGuia(String numero){
		this.numeroGuia = numero;
	}	
	
	
	public String getSetorOrigemGuia() throws Exception {
		
		if (this.setorOrigemGuia.equals("")){
			recuperarGuia();
		}
		return this.setorOrigemGuia;
	}

	public void setSetorOrigemGuia(String setorOrigemGuia) {
		this.setorOrigemGuia = setorOrigemGuia;
	}
	
	
	public String getSetorOrigemProcesso() {
		return setorOrigemProcesso;
	}
	
	public void setSetorOrigemProcesso(String setorOrigemProcesso) {
		this.setorOrigemProcesso = setorOrigemProcesso;
	}

	public String getSetorDestinoProcesso() {
		return setorDestinoProcesso;
	}

	public void setSetorDestinoProcesso(String setorDestinoProcesso) {
		this.setorDestinoProcesso = setorDestinoProcesso;
	}

}
