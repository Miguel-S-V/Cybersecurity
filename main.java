import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
// BEGIN SOLUTION
// please import only standard libraries and make sure that your code compiles and runs without unhandled exceptions 
// END SOLUTION
 
public class Main {    
  static void P1() throws Exception {

    byte[] cipherBMP = Files.readAllBytes(Paths.get("cipher1.bmp"));
    //Create a Cipher object with specifications given

    Cipher ciph = Cipher.getInstance("AES/CBC/ISO10126Padding");
    
    // BEGIN SOLUTION

    //Create Key using 1 ,0 .. alternating seqeuence 
    byte[] key = new byte[] { 1, 0, 1, 0, 
                              1, 0, 1, 0, 
                              1, 0, 1, 0, 
                              1, 0, 1, 0 };
    //create Byte arary IV given 
    byte[] IV = new byte[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    //Create SecretKey Object using byte []key 0, offset, 16 length IV, AES algorithm

    SecretKey secret = new SecretKeySpec(key, 0, 16,"AES");

    //Create IvParameter Object from given IV byte array

    IvParameterSpec ivSpec = new IvParameterSpec(IV);

    //Call memeber function init and pass paramaeters decrypt, secret key, and ivParamaeter
    ciph.init(Cipher.DECRYPT_MODE,secret,ivSpec);
    //store return value of doFinal function passing byte[] ciphertext as a paramaeters
    byte[] plainBMP = ciph.doFinal(cipherBMP);

    // END SOLUTION
    
    Files.write(Paths.get("plain1.bmp"), plainBMP);
  }

  static void P2() throws Exception {
    byte[] cipherBMP = Files.readAllBytes(Paths.get("cipher2.bmp"));
    byte[] otherBMP = Files.readAllBytes(Paths.get("plain1.bmp"));

    
    byte[] modifiedBMP = cipherBMP;


    
    // BEGIN SOLUTION

    for(int i =0; i < otherBMP.length; i++)
    {
      if( i == 5000)
      {
        
        break;
      }
      //Overright the first n bytes 
      modifiedBMP[i] = otherBMP[i];

    }

    // END SOLUTION

    Files.write(Paths.get("cipher2_modified.bmp"), modifiedBMP);
  }

  static void P3() throws Exception {
    // BEGIN SOLUTION
    Scanner scanner = new Scanner(new File("links.txt"));

    //Create object MessageDigest passing algorithm type SHA-256 to compute hash of each String

    MessageDigest md = MessageDigest.getInstance("SHA-256");
		while (scanner.hasNextLine()) {
		  String link = scanner.nextLine();
      //store the value of hash into byte[] checklink 
      byte[] checkLink = md.digest(link.getBytes());
      if(checkLink[0] == 92 && checkLink[1] == 72)
      {
        //Found match printing 
        String hashMatch = link;
        System.out.print(hashMatch);
        break;
      }

	  }
    
		scanner.close();
    // END SOLUTION
  }

  static void P4() throws Exception {
    byte[] cipher = Files.readAllBytes(Paths.get("cipher4.bin"));
    // BEGIN SOLUTION

    
    byte[] modifiedCipher = cipher;
    //Create 3 ArrayList<Byte> for each cipherblock in byte[] modifiedcipher
    //Since each block is 16 bytes and there are 48 bytes then there are 3 blocks 
    ArrayList<Byte> mcA = new ArrayList<Byte>();
    ArrayList<Byte> mcB = new ArrayList<Byte>();
    ArrayList<Byte> mcC = new ArrayList<Byte>();

    for(int i = 0; i < modifiedCipher.length; i++)
    {
      if(i < 16)
      {
        //Store first cipherBlock into A
        mcA.add(modifiedCipher[i]);
      }
      if(i >= 16 && i <= 31)
      {
        //Store Second cipherblock into B
       mcB.add(modifiedCipher[i]);
      }
      if(i > 31)
      {
        //Store third cipherblock into C
        mcC.add(modifiedCipher[i]);
      }
    }
    
    //Try all permutations until ciphertext is decrypted
    ArrayList<Byte> blocks = new ArrayList<Byte>();
    blocks.addAll(mcB);
    blocks.addAll(mcA);
    blocks.addAll(mcC);
    
   byte[] modCiph = new byte[blocks.size()];
   for(int i =0; i < blocks.size(); i++)
   {
     //write blocks into modCiph 
     modCiph[i] = blocks.get(i);
   }


    // Decrypt process like in problem 1 except with noPadding 

    Cipher ciph = Cipher.getInstance("AES/CBC/NoPadding");
    
    // BEGIN SOLUTION
    byte[] key = new byte[] { 1, 0, 1, 0, 
                              1, 0, 1, 0, 
                              1, 0, 1, 0, 
                              1, 0, 1, 0 };

    byte[] IV = new byte[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    SecretKey secret = new SecretKeySpec(key, 0, 16,"AES");
    IvParameterSpec ivSpec = new IvParameterSpec(IV);
    ciph.init(Cipher.DECRYPT_MODE,secret,ivSpec);

    byte[] plain = ciph.doFinal(modCiph);
    
    // END SOLUTION
    //byte[] plain = modifiedCipher;
    Files.write(Paths.get("plain4.txt"), plain);
  }

  static void P5() throws Exception {
    byte[] plainA = Files.readAllBytes(Paths.get("plain1.bmp"));
    byte[] cipherA = Files.readAllBytes(Paths.get("cipher5A.bmp"));
    byte[] cipherB = Files.readAllBytes(Paths.get("cipher5B.bmp"));
    byte[] plainB = new byte[plainA.length];
    //Compute plaintext XOR ciphertext to get key

    for(int i = 0; i < plainA.length; i++)
    {
       byte xor = (byte)(plainA[i] ^ cipherA[i]);
       //Compute Key XOR ciphertext to retrieve plaintext 
       //Store plaintext into plainB

       plainB[i] = (byte)(xor ^ cipherB[i]); 
    }


    // BEGIN SOLUTION
    
    // END SOLUTION
    
    Files.write(Paths.get("plain5B.bmp"), plainB);
  }

  public static void main(String [] args) {
    try {  
      P1();
      P2();
      P3();
      P4();
      P5();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}
