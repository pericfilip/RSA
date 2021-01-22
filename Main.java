import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.sql.SQLOutput;
import javax.crypto.*;
import java.util.Scanner;

public class Main {

   public static void saveKey(String fileName, KeyPair key) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(key);
            out.close();
            System.out.println("Saved key as " + fileName);
        } catch (IOException i) {
            i.printStackTrace();
        }
   }

   public static KeyPair readKey(String fileName) {
       KeyPair key = null;
       try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            key = (KeyPair) in.readObject();
            in.close();
            //System.out.println("Read key from " + fileName);
           System.out.println(key);
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
        return key;

   }

   public static void generateKeys(String fileName, int bitLength) {
        SecureRandom rand = new SecureRandom();

        BigInteger p = new BigInteger(bitLength / 2, 100, rand);
        BigInteger q = new BigInteger(bitLength / 2, 100, rand);
        BigInteger n = p.multiply(q);
        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = new BigInteger("3");
        while (phiN.gcd(e).intValue() > 1) {
            e = e.add(new BigInteger("2"));
        }
        
        BigInteger d = e.modInverse(phiN);
        KeyPair publicKey = new KeyPair(e, n);
        KeyPair privateKey = new KeyPair(d, n);
        saveKey(fileName + "_pub.key", publicKey);
        saveKey(fileName + "_priv.key", privateKey);

   }

   public static String encrypt(String message, KeyPair key) {
       return (new BigInteger(message.getBytes(StandardCharsets.UTF_8))).modPow(key.getKey(), key.getN()).toString();
   } 

   public static String decrypt(String message, KeyPair key) {
       String msg = new String(message.getBytes(StandardCharsets.UTF_8));
        return new String((new BigInteger(msg)).modPow(key.getKey(), key.getN()).toByteArray());
   }

   public static void main(String[] args) {
       //meny
       while(true){
           while (true){
       System.out.println(" ");
       System.out.println("Hello, Please decide what you want to do");
       System.out.println("1. Create key pair");
       System.out.println("2. Load keypair");
       System.out.println("3. Encrypt message");
       System.out.println("4. Decrypt message");
       System.out.println("5. to exit");
       Scanner scm = new Scanner(System.in);
       int userSelect = scm.nextInt();
        String testText = "hej jag ska krypteras";

      int bitLength = 4096;

//-------------------------1----------------------------------------------
       if(userSelect == 1){

           Scanner sc = new Scanner(System.in);
           System.out.println("\n" + "please name file for keyPair");
           generateKeys(sc.nextLine(),bitLength);

       }
//-------------------------2-----------------------------------------------
       if(userSelect == 2){


               Scanner scan = new Scanner(System.in);
               System.out.println("Please enter the key you want to load");
               String fileNames = scan.nextLine();
               readKey(fileNames);

       }
//-------------------------3-----------------------------------------------
       if(userSelect == 3){
           System.out.println("Do you want do encrypt a 1. String or 2. File?");
           Scanner scs = new Scanner(System.in);
           int select = scs.nextInt();
           if(select == 1){
               Scanner scanString = new Scanner(System.in);
               System.out.println("Please enter a word or a sentance that you want to encrypt");
               String messageToEncrypt = scanString.nextLine();
               System.out.println(encrypt(messageToEncrypt, readKey("Filip_pub.key")));
               System.out.println(messageToEncrypt);
              // scanString.close();
           }
           if(select == 2){
               try {
                   Scanner scanToFile = new Scanner(System.in);
                   System.out.println("Write something to encrypt to file");
                   FileWriter fw = new FileWriter("EncryptedText.txt");
                   String fileString = scanToFile.nextLine();

                   fw.write(encrypt(fileString, readKey("Filip_pub.key")));
                    fw.close();
               } catch (Exception e){

               }
               }
       }
//-------------------------4-----------------------------------------------
       if(userSelect == 4){
           String currentFile = "";
           System.out.println("Do you want do Decrypt a 1. String or 2. File?");
           Scanner sc = new Scanner(System.in);
           int select = sc.nextInt();
           if(select == 1){
               System.out.println("Please enter something to decrypt");
               Scanner scanString = new Scanner(System.in);
               String decryptString = scanString.nextLine();
              // System.out.println(encrypt(testText, readKey("Filip_priv.key")));
               System.out.println(decrypt(decryptString, readKey("Filip_priv.key")));
           }
           if(select == 2){
               try {
                   Scanner scanFromFile = new Scanner(System.in);
                   System.out.println("what file do you want to decrypt from");
                   String whatFile = scanFromFile.nextLine();
                   FileReader fr = new FileReader(whatFile);
                   BufferedReader br = new BufferedReader(fr);

                   String stringFromFile = br.readLine();
                   System.out.println(decrypt(stringFromFile, readKey("Filip_priv.key")));

                    fr.close();
               } catch (Exception e){

               }
               }
       }
       if(userSelect == 5){
           System.exit(0);
       }

   }


}}}