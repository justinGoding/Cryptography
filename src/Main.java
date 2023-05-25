/**
 * SHA-3 derived functions and Keccak sponge for the purpose of implementing KMACXOF256
 * @author Justin Goding, Yeseong Jeon, Andrew Lau
 * Some code borrowed from/inspired by Markku-Juhani Saarinen's C implementation of SHA-3
 * functions at https://github.com/mjosaarinen/tiny_sha3/blob/master/sha3.c
 * Some code borrowed from Professor Paulo Barreto
 */

import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class Main {

    public static void main(String args[]) throws IOException {
        //Controller.start();
        Ed448GPoint G = new Ed448GPoint(Ed448GPoint.p.subtract(BigInteger.ONE), true);
        if (G.isOnCurve()) {
            //G = G.multiply(Ed448GPoint.r);
            byte[] gBytes = G.getBytes();
            Ed448GPoint S = Ed448GPoint.pointFromBytes(gBytes);
            if (G.equals(S)) {
                System.out.println("Test Passed");
            }
            else {
                System.out.println("Test Failed");
            }
        }
    }

    public static byte[] concat(byte[] s1, byte[] s2){
        int s1Len = s1.length;
        int s2Len = s2.length;
        byte[] concatS = new byte[s1Len + s2Len];
        System.arraycopy(s1, 0, concatS,0, s1Len);
        System.arraycopy(s2, 0, concatS, s1Len, s2Len);
        return concatS;
    }
}

