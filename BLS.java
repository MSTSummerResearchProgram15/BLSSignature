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

    public static final int BLOCK_SIZE = 128; // block size file splitter in bytes
    public static void main(String args[]){

        long startTime = System.currentTimeMillis(); // get start time (used to determine execution time)
        // setup pairing
        Pairing pairing = PairingFactory.getPairing("a.properties");    // curve parameters
        Element g = pairing.getG1().newRandomElement().getImmutable();  // system parameters
        Element privateKey = pairing.getZr().newRandomElement();        // secret/private key
        Element pK = g.powZn(privateKey);                               // public key

        File dir = new File("output");
        dir.mkdir();                        // creates folder 'output', so all of the new files are all in one spot


        FileSplitter fs = new FileSplitter("output");   // sets ouput folder
        int numberOfFiles = 0;                          // initialize number of files counter
        try{
            numberOfFiles = fs.splitFile("message.txt", "splitFile", BLOCK_SIZE);   // splits original file into new files of size blocksize
        } catch (FileNotFoundException e){
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println(numberOfFiles);  // check how many files were created
        FileSigner signer = new FileSigner(pairing, privateKey);    // initialize signer
        try {
            signer.signFiles("output", "splitFile", numberOfFiles);     // sign files
        } catch (IOException e){
            e.printStackTrace();
            System.exit(2);
        }
        FileSigner verifier = new FileSigner(pairing, pK, g);       // initialize signer in 'verify mode'
        boolean success = false;
        try {
            success = verifier.verifyFiles("output", "splitFile", numberOfFiles); // verify files
        } catch (IOException e){
            e.printStackTrace();
            System.exit(3);
        }
        if(success) {
            System.out.println("verified");     // find out if it worked
        } else{
            System.out.println("fail");
        }


        System.out.println("\nms to run:");
        System.out.println(System.currentTimeMillis()-startTime);   // output execution time
        System.exit(0); // end program
        // old code past here







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






        /*String saveFile = "splitFile";
        int numberOfFiles = 0;
        try {
            File path = path("message.txt");
            System.out.println("number of files created:");
            System.out.println(numberOfFiles = splitFile(path, BLOCK_SIZE, saveFile));
        } catch(IOException ioe){
            ioe.printStackTrace();
        }

        String folderPath = "output";
        String originalName = saveFile;
        try {
            signFiles(folderPath, originalName, numberOfFiles, privateKey, pairing);
        } catch (IOException e){
            e.printStackTrace();
        }
        boolean success = false;
        try{
            success = verifyFiles(folderPath, originalName, numberOfFiles, g, pK, pairing);
        }catch(IOException e){
            e.printStackTrace();
        }
        if(success){
            System.out.println("successfully verified");
        }else{
            System.out.println("not verified");
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

    /*// takes file name and number of files with that name(iterator concatenated) and verifies using .signature files in same directory
    public static boolean verifyFiles(String folderPath, String originalFileName,
                                   int numberOfFiles, Element sysParams,
                                   Element publicKey, Pairing pairing )
                                    throws IOException
    {
        String filePath = folderPath.concat("\\").concat(originalFileName);
        String temp;
        int successes = 0;
        boolean allFilesGood = false;
        for (int i = 1; i <= numberOfFiles; i++) {
            temp = filePath.concat(Integer.toString(i)).concat(".txt");
            byte[] message = readFile(temp);
            temp = temp.concat(".signature");
            byte[] sig = readFile(temp);
            Element signature = pairing.getG1().newElementFromBytes(sig);
            boolean v = verifySignature(signature, sysParams, message, publicKey, pairing);
            if(v){successes++;}
        }
        if(successes == numberOfFiles){
            allFilesGood = true;
        }

        return allFilesGood;
    }

    // takes a file name and number of files of that name(with iterator concatenated) and generates signatures for all of them
    // original file name is name of file without extensions
    public static void signFiles(String folderPath, String originalFileName,int numberOfFiles, Element privateKey, Pairing pairing) throws IOException{
        String filePath = folderPath.concat("\\").concat(originalFileName);
        String temp;
        for (int i = 1; i < numberOfFiles; i++) {
            temp = filePath.concat(Integer.toString(i)).concat(".txt");
            byte[] messageBlock = readFile(temp);
            Element signature = generateSignature(messageBlock, privateKey, pairing);
            writeSignature(temp, signature);
        }
        temp = filePath.concat(Integer.toString(numberOfFiles)).concat(".txt");
        byte[] messageBlock = readFile(temp);
        Element signature = generateSignature(messageBlock, privateKey, pairing);
        writeSignature(temp, signature);


        //writeSignature(filePath, signature);
    }

    // takes a file path and generates signature with same file name but with .signature
    public static void writeSignature(String filePath, Element signature) throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(filePath.concat(".signature"));
        os.write(signature.toBytes());
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
        try{
            while (is.available() >= blockSize) {

                is.read(temp);
                OutputStream os = new FileOutputStream(PathName.concat(Integer.toString(numberOfFiles)).concat(".txt"));
                numberOfFiles++;
                os.write(temp);
            }
            byte[] temp2 = new byte[is.available()];
            is.read(temp2);
            OutputStream os = new FileOutputStream(PathName.concat(Integer.toString(numberOfFiles)).concat(".txt"));
            os.write(temp);
        } catch(Exception IOException){
            IOException.printStackTrace();
            return -1;
        }

        return numberOfFiles;
    }
    // splits byte array into multiple blocks of size blockSize(bytes)
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

    // reads file into byte array, don't use for 'large' files
    public static byte[] readFile(String filePath) throws IOException{
        byte[] file;
        Path path = Paths.get(filePath);
        file = Files.readAllBytes(path);
        return file;
    }

    // verifies that the signature is valid, given the necessary inputs
    public static boolean verifySignature(Element signature, Element sysParams, byte[] message, Element publicKey, Pairing pairing){
        Element map = mapValue(message, pairing);
        Element temp1 = pairing.pairing(signature, sysParams);
        Element temp2 = pairing.pairing(map,publicKey);
        return temp1.equals(temp2);
    }

    // generates the signature using a value and the private key (uses hash of value)
    public static Element generateSignature(byte[] message, Element privateKey, Pairing pairing) {
        Element sig;
        Element map = mapValue(message, pairing);
        sig = map.powZn(privateKey);
        return sig;
    }

    // takes the value, and maps the hash of the value
    public static Element mapValue(byte[] value, Pairing pairing){
        Element map;
        byte[] hash = hash(value);
        map = pairing.getG1().newElement().setFromHash(hash, 0, hash.length);
        return map;
    }

    // computes hash of value with SHA256
    public static byte[] hash(byte[] value){
        byte[] hash;
        MessageDigest md = new JDKMessageDigest.SHA256();
        hash = md.digest(value);
        return hash;
    }*/
}