package app.utilidades;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Algortimo_AES {

    public static SecureRandom sr = new SecureRandom();

    public static String encriptar(String clave, byte[] iv, String value) {
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

    public static String decriptar(String clave, byte[] iv, String encriptado) {
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

    public static void main(String[] args) throws Exception {
    	
        String clave = "FooBar1234567890"; // 128 bit ,192 o 256bit y esta ya en 128bit considerado muy seguro 
        byte[] iv = new byte[16];
        //System.out.println("ivANT: "+new String(iv));
        sr.nextBytes(iv);
        //System.out.println("ivNEW: "+new String(iv));
        String encriptar = encriptar(clave, iv, "Demasiados Secretos!");
        System.out.println(String.format("encriptado: %s", encriptar));
        String desencriptar=decriptar(clave, iv, encriptar);
        System.out.println("desemcriptado: "+desencriptar);
        
        //PROBANDO CLASE EXTERNA
        AlgoritmoEcriptDesencript_AES alg=new AlgoritmoEcriptDesencript_AES();
        String enc=alg.encriptar("Demasiados Secretos!");
        System.out.println("Enc: "+enc);
        String des=alg.decriptar(enc);
        System.out.println("Desc: "+des);
        
        //ALGORITMO 3DES
        ALGORITMO3DES_Ecript_Desencript Tresdes=new ALGORITMO3DES_Ecript_Desencript();
        String ecript=Tresdes.Encriptar("Demasiados Secretos");
        System.out.println("ecript: "+ecript);
        String descript=Tresdes.Desencriptar(ecript);
        System.out.println("ecript: "+descript);
    }
}