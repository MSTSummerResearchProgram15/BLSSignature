/**
 * Created by Dylan on 6/16/2015.
 *
 *
 *
 * Needs cleaning...
 */


import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.jce.provider.JDKMessageDigest;

import java.math.BigInteger;
import java.security.MessageDigest;

public class BLS {

    public static void main(String args[]){

        // setup
        Pairing pairing = PairingFactory.getPairing("a.properties");    // curve parameters
        Element g = pairing.getG1().newRandomElement().getImmutable();  // system parameters
        Element privateKey = pairing.getZr().newRandomElement();        // secret/private key
        Element pK = g.powZn(privateKey);                               // public key

        // set values "messages"
        BigInteger m = new BigInteger("6");
        BigInteger n = new BigInteger("7");
        BigInteger c = m.add(n); // combined value

        Element cSig = generateSignature(c, privateKey, pairing);

        Element s = mapValue(c, pairing);

        if(verifySignature(cSig, g, c, pK, pairing)){
            System.out.println("verified");
        }else{
            System.out.println("not valid");
        }



        System.exit(0);//**************
        //((new BigInteger(mHash)).multiply(new BigInteger(nHash))).toByteArray();


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

       /* if(mTemp1.equals(mTemp2)){
            System.out.println("m verified");
            System.out.println(sigM);
        }
        if(nTemp1.equals(nTemp2)){
            System.out.println("n verified");
            System.out.println(sigN);
        }*/

        Element v = j.mul(k);

        // next generate mapping for 13, see if that works?

        Element sigCombined = sigM.mul(sigN);  // generate signature

        //Element cTemp1 = pairing.pairing(sigCombined, g);
        //Element cTemp2 = pairing.pairing(v,pK);

        //decrypt



        /*if(cTemp1.equals(cTemp2)){
            System.out.println("combined sig verified");
            System.out.println(sigCombined);
        } else{
            System.out.println("combined sig failed");
        }*/


        // multiply mapping and signature together, and it will verify for the messages added together

    }

    public static boolean verifySignature(Element signature, Element sysParams, BigInteger message, Element publicKey, Pairing pairing){
        Element map = mapValue(message, pairing);
        Element temp1 = pairing.pairing(signature, sysParams);
        Element temp2 = pairing.pairing(map,publicKey);
        if(temp1.equals(temp2)){
            return true;
        }
        return false;
    }

    public static Element generateSignature(BigInteger value, Element privateKey, Pairing pairing) {
        Element sig;
        Element map = mapValue(value, pairing);
        sig = map.powZn(privateKey);
        return sig;
    }

    public static Element mapValue(BigInteger value, Pairing pairing){
        Element map;
        byte[] hash = hash(value);
        map = pairing.getG1().newElement().setFromHash(hash, 0, hash.length);
        return map;
    }

    public static byte[] hash(BigInteger value){
        byte[] hash;
        byte[] temp = value.toByteArray();
        MessageDigest md = new JDKMessageDigest.MD5();
        hash = md.digest(temp);
        return hash;
    }
}