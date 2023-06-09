/**
 * SHA-3 derived functions and Keccak sponge for the purpose of implementing KMACXOF256
 * @author Yeseong Jeon, Justin Goding
 */


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Symmetric {
	
	static int choice;
	 public static void start(){
		 while (true) {
			 Console.printMainScreenSymmetric();
			 final String mainInput = Console.inputMain(5);

			 switch (mainInput) {
				 case "1":
					 System.out.println(">> Option 1:\"Compute a plain cryptographic hash\" selected");
					 choice = 1;
					 computeOption();
					 break;
				 case "2":
					 System.out.println(">> Option 2:\"Compute a MAC under a given passphrase\" selected");
					 choice = 2;
					 computeOption();
					 break;
				 case "3":
					 System.out.println(">> Option 3:\"Encrypt a given data file symmetrically\" selected");
					 encrypt();
					 break;
				 case "4":
					 System.out.println(">> Option 4:\"Decrypt a given data file symmetrically\" selected");
					 decrypt();
					 break;
				 case "5":
					 return;
			 }
			 System.out.println();
		 }
	 }
	 
	 public static void computeOption(){
		 Console.printOptionScreen();
		 final String optionInput = Console.inputOption();
		 
	     switch (optionInput) {
            case "a":
                System.out.println(">> Option a:\"Enter a file\" selected");
                if(choice == 1) {
                	computeTextPlain(true);
                }else if(choice == 2) {
                	computeMAC(true);
                }
                break;
            case "b":       
                System.out.println(">> Option b:\"Enter text directly\" selected");
                if(choice == 1) {
                	computeTextPlain(false);
                }else if(choice == 2) {
                	computeMAC(false);
                }
                break;
        }
	 }

	 public static void computeTextPlain(boolean file){ //Compute plain cryptographic hash of a text
		 byte[] m = {};
		 if (file) {
			 boolean fnf = true;
			 while (fnf) {
				 try {
					 String fileName = Console.getFileName();
					 m = Files.readAllBytes(Paths.get(fileName));
					 fnf = false;
				 } catch (IOException e) {
					 System.out.println("!The file does not exist!");
				 }
			 }
		 }
		 else {
			 m = Console.getText().getBytes();
		 }

		 byte[] hash = KMACXOF256.KMACXOF256("".getBytes(), m, 512, "D".getBytes());
		 writeHash(hash);
	 }

	 private static void writeHash(byte[] hash) {
		 try (FileOutputStream fos = new FileOutputStream("encrypted.txt")) {
			 fos.write(hash);
		 }
		 catch (IOException ioe) { System.out.println("Could not write hash to file"); }
		 for (byte b : hash) {
			 System.out.printf("%02X ", b);
		 }
		 System.out.println();
	 }
	 
	 public static void computeMAC(boolean file){ //Compute a MAC of a text from a given file under a given passphrase
		 System.out.println("***chose number 2 insert file***");

		 byte[] m = {};
		 if (file) {
			 boolean fnf = true;
			 while (fnf) {
				 try {
					 String fileName = Console.getFileName();
					 m = Files.readAllBytes(Paths.get(fileName));
					 fnf = false;
				 } catch (IOException e) {
					 System.out.println("!The file does not exist!");
				 }
			 }
		 }
		 else {
			 m = Console.getText().getBytes();
		 }

		 String pw = Console.getPassword();

		 byte[] mac = KMACXOF256.KMACXOF256(pw.getBytes(), m, 512, "T".getBytes());
		 writeHash(mac);
	 }

	 public static void encrypt() {
		 byte[] m = {};
		 boolean fnf = true;
		 while (fnf) {
			 try {
				 String inputFileName = Console.getFileName();
				 m = Files.readAllBytes(Paths.get(inputFileName));
				 fnf = false;
			 } catch (IOException e) {
				 System.out.println("!The file does not exist!");
			 }
		 }

		 String pw = Console.getPassword();

		 SymmetricCryptogram crypt = KMACXOF256.encrypt(m, pw); // since xof has to return value, txt will contain nothing
		 try (FileOutputStream fos = new FileOutputStream("encrypted.txt")) {
			 fos.write(crypt.getZ());
			 fos.write(crypt.getC());
			 fos.write(crypt.getT());
		 } catch (IOException e) {
			 System.out.println("Could not write cryptogram to file");
		 }
	 }
	 
	 public static void decrypt() {
		 // get the text from the input file
		 // let user to out pw through the console
		 // decrypt.

		 byte[] bytes = {};
		 boolean fnf = true;
		 while (fnf) {
			 try {
				 String inputFileName = Console.getFileName();
				 bytes = Files.readAllBytes(Paths.get(inputFileName));
				 fnf = false;
			 } catch (IOException e) {
				 System.out.println("!The file does not exist!");
			 }
		 }

		 String pw = Console.getPassword();
		 byte[] z;
		 byte[] c;
		 byte[] t;
		 byte[] m = {};

		if (bytes.length > 128) {
			z = Arrays.copyOfRange(bytes, 0, 64);
			c = Arrays.copyOfRange(bytes, 64, bytes.length - 64);
			t = Arrays.copyOfRange(bytes, bytes.length - 64, bytes.length);
			m = KMACXOF256.decrypt(z, c, t, pw.getBytes());
		}
		else {
			System.out.println("Given file does not contain a Symmetric Cryptogram");
			return;
		}

		if (m != null) {
			try (FileOutputStream fos = new FileOutputStream("decrypted.txt")) {
				fos.write(m);
			} catch (IOException ioe) {
				System.out.println("Could not write hash to file");
			}

			String s = new String(m, StandardCharsets.UTF_8);
			System.out.println(s);
		}
	 }
	 
}
