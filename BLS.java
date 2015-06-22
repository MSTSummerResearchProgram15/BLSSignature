/**
 * Created by Dylan on 6/16/2015.
 *
 *
 *
 */


import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.jce.provider.JDKMessageDigest;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;

public class BLS {

    public static void main(String args[]){

        // setup pairing
        Pairing pairing = PairingFactory.getPairing("a.properties");    // curve parameters
        Element g = pairing.getG1().newRandomElement().getImmutable();  // system parameters
        Element privateKey = pairing.getZr().newRandomElement();        // secret/private key
        Element pK = g.powZn(privateKey);                               // public key


        File dir = new File("output");
        dir.mkdir();

        try {
            File path = path("message.txt");
            System.out.println(splitFile(path, 32, "splitFile"));
        } catch(IOException ioe){
            ioe.printStackTrace();
        }






        /*// set values: "messages"
        BigInteger m = new BigInteger("6");
        BigInteger n = new BigInteger("7");
        BigInteger c = m.add(n); // combined value

        Element cSig = generateSignature(c, privateKey, pairing);

        //Element s = mapValue(c, pairing);

        if(verifySignature(cSig, g, c, pK, pairing)){
            System.out.println("verified");
        }else{
            System.out.println("not valid");
        }

        BigInteger message = new BigInteger("123");
        Element signature = generateSignature(message, privateKey, pairing);
        boolean valid = verifySignature(signature, g, message, pK, pairing);
        if(valid){
            System.out.println("Message verified");
        }*/







/*
        // begin recovery testing
        System.out.println("private key:");
        System.out.println(Arrays.toString(privateKey.toBytes()));
        System.out.println("");
        BigInteger testValue = new BigInteger("0");
        Element map = mapValue(testValue, pairing);
        Element signature1 = map.powZn(privateKey); // How can they be equal!? *********************

        System.out.println(Arrays.toString(map.toBytes()));
        System.out.println(Arrays.toString(signature1.toBytes()));

        System.out.println(map.hashCode());
        System.out.println(signature1.hashCode());

        System.out.println(map);
        System.out.println(signature1);

        if (map.equals(signature1))
        {
            System.out.println("they are equal");
        } else  {
            System.out.println("they're not equal");
        }
        if(map.isEqual(signature)){
            System.out.println("they are equal");
        } else {
            System.out.println("they're not equal");
        }*/

        /*
            get random file and split into blocks, then sign each block, then add blocks (and multiply signatures), and verify homomorphism



        */

        System.exit(0); // exit
        // collapsed code block (also commented out):
        /*//((new BigInteger(mHash)).multiply(new BigInteger(nHash))).toByteArray();


        // try using value instead of hash
        //Element h = pairing.getG1().newElement().setFromHash(m.toByteArray(), 0, m.toByteArray().length); // map hash to element on g1
        //Element i = pairing.getG1().newElement().setFromHash(n.toByteArray(), 0, n.toByteArray().length);
        // using hash:
        byte[] mHash = hash(m);
        byte[] nHash = hash(n);

        Element h = pairing.getG1().newElement().setFromHash(mHash, 0, mHash.length);
        Element i = pairing.getG1().newElement().setFromHash(nHash, 0, nHash.length);

        Element sigM = h.powZn(privateKey);  // generate signature
        Element sigN = i.powZn(privateKey);

        // verification
        byte[] mHashV = hash(m);    // recalculate hashes for messages
        byte[] nHashV = hash(n);

        Element j = pairing.getG1().newElement().setFromHash(mHashV, 0, mHashV.length); // map hash to element on g1
        Element k = pairing.getG1().newElement().setFromHash(nHashV, 0, nHashV.length);

        Element mTemp1 = pairing.pairing(sigM, g);
        Element mTemp2 = pairing.pairing(j,pK);

        Element nTemp1 = pairing.pairing(sigN, g);
        Element nTemp2 = pairing.pairing(k,pK);

       *//* if(mTemp1.equals(mTemp2)){
            System.out.println("m verified");
            System.out.println(sigM);
        }
        if(nTemp1.equals(nTemp2)){
            System.out.println("n verified");
            System.out.println(sigN);
        }*//*

        Element v = j.mul(k);

        // next generate mapping for 13, see if that works?

        Element sigCombined = sigM.mul(sigN);  // generate signature

        //Element cTemp1 = pairing.pairing(sigCombined, g);
        //Element cTemp2 = pairing.pairing(v,pK);

        //decrypt



        *//*if(cTemp1.equals(cTemp2)){
            System.out.println("combined sig verified");
            System.out.println(sigCombined);
        } else{
            System.out.println("combined sig failed");
        }*//*


        // multiply mapping and signature together, and it will verify for the messages added together
        */
    }

    // takes a string file path and creates File object
    public static File path(String filePath) throws IOException{
        File path = new File(filePath);
        return path;
    }

    // splits one file into multiple files of size blockSize(bytes) into \output folder, returns number of files, -1 if fail
    public static int splitFile(File file, int blockSize, String saveName) throws FileNotFoundException {
        int numberOfFiles = 1;
        String PathName = "output\\".concat(saveName);
        InputStream is = new FileInputStream(file);
        byte[] temp = new byte[blockSize];
        for (int i = 0; i < blockSize; i++) {
            try {
                is.read(temp);
                OutputStream os = new FileOutputStream(PathName.concat(Integer.toString(numberOfFiles)).concat(".txt"));
                numberOfFiles++;
                os.write(temp);
            } catch(Exception IOException){
                IOException.printStackTrace();
                return -1;
            }
        }
        return numberOfFiles;
    }
    /*// splits byte array into multiple blocks of size blockSize(bytes)
    public static byte[][] splitArray(byte[] file, int blockSize){
        int arrayLength = file.length/blockSize;
        if(file.length%blockSize != 0){
            arrayLength++;
        }
        byte[][] blocks = new byte[arrayLength][blockSize];

        int counter = file.length;
        int iterator = 0;
        int blockIterator = 0;
        while(counter > 0){
            for (int i = 0; i < blockSize; i++) {
                if(counter == 0){break;}
                blocks[blockIterator][i] = file[iterator];
                iterator++;
                counter--;
            }
            blockIterator++;
        }
        return blocks;
    }

    // reads file into byte array
    public static byte[] readFile(String filePath) throws IOException{
        byte[] file;
        Path path = Paths.get(filePath);
        file = Files.readAllBytes(path);
        return file;
    }*/

    // verifies that the signature is valid, given the necessary inputs
    public static boolean verifySignature(Element signature, Element sysParams, BigInteger message, Element publicKey, Pairing pairing){
        Element map = mapValue(message, pairing);
        Element temp1 = pairing.pairing(signature, sysParams);
        Element temp2 = pairing.pairing(map,publicKey);
        return temp1.equals(temp2);
    }

    // generates the signature using a value and the private key (uses hash of value)
    public static Element generateSignature(BigInteger value, Element privateKey, Pairing pairing) {
        Element sig;
        Element map = mapValue(value, pairing);
        sig = map.powZn(privateKey);
        return sig;
    }

    // takes the value, and maps the hash of the value
    public static Element mapValue(BigInteger value, Pairing pairing){
        Element map;
        byte[] hash = hash(value);
        map = pairing.getG1().newElement().setFromHash(hash, 0, hash.length);
        return map;
    }

    // computes hash of value with SHA256
    public static byte[] hash(BigInteger value){
        byte[] hash;
        byte[] temp = value.toByteArray();
        MessageDigest md = new JDKMessageDigest.SHA256();
        hash = md.digest(temp);
        return hash;
    }
}