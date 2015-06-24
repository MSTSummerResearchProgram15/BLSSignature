import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Dylan on 6/24/2015.
 */
public class FileSigner {
    Pairing pairing;
    Element privateKey;
    Element publicKey;
    Element sysParams;
    Signer signer;

    // full constructor
    public FileSigner(Pairing pairing, Element privateKey, Element publicKey, Element sysParams){
        this.pairing = pairing;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.sysParams = sysParams;
        signer = new Signer(pairing, sysParams, publicKey, privateKey);
    }

    // partial constructor, use for signing
    public FileSigner(Pairing pairing, Element privateKey){
        this(pairing, privateKey, null, null);
    }

    // partial constructor, use for verifying
    public FileSigner(Pairing pairing, Element publicKey, Element sysParams){
        this(pairing, null, publicKey, sysParams);
    }

    // takes file name and number of files with that name(iterator concatenated) and verifies using .signature files in same directory
    public boolean verifyFiles(String filePath, String fileName, int numberOfFiles) throws IOException
    {
        filePath = filePath.concat("\\").concat(fileName);
        String temp;
        int successes = 0;
        boolean allFilesGood = false;
        for (int i = 1; i <= numberOfFiles; i++) {
            temp = filePath.concat(Integer.toString(i)).concat(".txt");
            byte[] message = readFile(temp);
            temp = temp.concat(".signature");
            byte[] sig = readFile(temp);
            Element signature = pairing.getG1().newElementFromBytes(sig);
            boolean v = signer.verifySignature(signature, message);
            if(v){successes++;}
        }
        if(successes == numberOfFiles){
            allFilesGood = true;
        }

        return allFilesGood;
    }

    // takes a file name and number of files of that name(with iterator concatenated) and generates signatures for all of them
    // original file name is name of file without extensions
    public void signFiles(String filePath, String FileName,int numberOfFiles) throws IOException{
        filePath = filePath.concat("\\").concat(FileName);
        String temp;
        for (int i = 1; i < numberOfFiles; i++) {
            temp = filePath.concat(Integer.toString(i)).concat(".txt");
            byte[] messageBlock = readFile(temp);
            Element signature = signer.generateSignature(messageBlock);
            writeSignature(temp, signature);
        }
        temp = filePath.concat(Integer.toString(numberOfFiles)).concat(".txt");
        byte[] messageBlock = readFile(temp);
        Element signature = signer.generateSignature(messageBlock);
        writeSignature(temp, signature);


        //writeSignature(filePath, signature);
    }

    // takes a file path and generates signature with same file name but with .signature
    public void writeSignature(String filePath, Element signature) throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(filePath.concat(".signature"));
        os.write(signature.toBytes());
    }

    // reads file into byte array, don't use for 'large' files
    public byte[] readFile(String filePath) throws IOException{
        byte[] file;
        Path path = Paths.get(filePath);
        file = Files.readAllBytes(path);
        return file;
    }
}
