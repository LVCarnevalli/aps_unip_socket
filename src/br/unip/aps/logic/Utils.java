package br.unip.aps.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class Utils {

	public static String getHoraAtual() {
		Date date = new Date(); 
		SimpleDateFormat mask = new SimpleDateFormat("HH:mm:ss");	
		return mask.format(date);	
	}
	
	public static void alertaErro(String mensagem) {
		JOptionPane.showMessageDialog(null, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void alertaAviso(String mensagem) {
		JOptionPane.showMessageDialog(null, mensagem, "Aviso", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void setCookie(String cookie) {
		try {
			File arquivo = new File(System.getProperty("java.io.tmpdir") , "chatcookie");
			if (!arquivo.exists()) {
				arquivo.createNewFile();
			}
			FileWriter fw = new FileWriter(arquivo, false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(cookie);
			bw.close();
			fw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}	
	}
	
	public static String getCookie() {
		String chatcookie = "";
		try {
			File arquivo = new File(System.getProperty("java.io.tmpdir") , "chatcookie");
			if (arquivo.exists()) {
				FileReader reader = new FileReader(arquivo);
				BufferedReader buffer = new BufferedReader(reader);
				chatcookie = buffer.readLine();
				buffer.close();
				reader.close();
			}	
		} catch (IOException ex) {
			ex.printStackTrace();
		}	
		return chatcookie;
	}
	
	public static void deleteCookie() {
		try {
			File arquivo = new File(System.getProperty("java.io.tmpdir") , "chatcookie");
			if (arquivo.exists()) {
				arquivo.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@SuppressWarnings("unused")
	public static boolean existFile(String file) {
		try {
			File arquivo = new File(file);
			return false;
		} catch (Exception e) {
			return true;
		}		
	}
	
}
