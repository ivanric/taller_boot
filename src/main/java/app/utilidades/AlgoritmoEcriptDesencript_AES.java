package app.utilidades;



import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AlgoritmoEcriptDesencript_AES {
	   	private  SecureRandom sr = new SecureRandom();
		private byte[] iv = new byte[16];
		public AlgoritmoEcriptDesencript_AES() {
			// TODO Auto-generated constructor stub
			sr.nextBytes(iv);
		}
		
	    public  String encriptar(String value) {
	    	String clave="FooBar1234567890";
	    	try {
	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	            SecretKeySpec sks = new SecretKeySpec(clave.getBytes("UTF-8"), "AES");
	            cipher.init(Cipher.ENCRYPT_MODE, sks, new IvParameterSpec(iv));

	            byte[] encriptado = cipher.doFinal(value.getBytes());
	            return DatatypeConverter.printBase64Binary(encriptado);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    public  String decriptar(String encriptado) {
	    	String clave="FooBar1234567890";
	    	try {
	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	            SecretKeySpec sks = new SecretKeySpec(clave.getBytes("UTF-8"), "AES");
	            cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(iv));

	            byte[] dec = cipher.doFinal(DatatypeConverter.parseBase64Binary(encriptado));
	            return new String(dec);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        return null;
	    }
	
}
