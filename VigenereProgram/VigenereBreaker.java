import java.util.*;
import edu.duke.*;
import java.io.File;
public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        //REPLACE WITH YOUR CODE
        StringBuilder sliceStr = new StringBuilder();
        for (int k=whichSlice;k< message.length(); k+=totalSlices) {
            sliceStr.append(message.charAt(k));
        }
        return sliceStr.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        //WRITE YOUR CODE HERE
        for (int k=0;k< klength; k++) {
            String word = sliceString(encrypted,k,klength);
            CaesarCracker cc = new CaesarCracker(mostCommon);
            int keyValue = cc.getKey(word);
            // System.out.println(cc.decrypt(word));
            key[k] = keyValue;
        }
        return key;
    }

    public void breakVigenere () {
        //WRITE YOUR CODE HERE
        HashMap<String,HashSet<String>> languages = new HashMap<String,HashSet<String>>();
        DirectoryResource f = new DirectoryResource();
        for (File fi : f.selectedFiles()){
            String fileName = fi.getName();
            //System.out.println(fileName);
            FileResource dic = new FileResource(fi);
            HashSet<String> dictionary = readDictionary(dic);
            languages.put(fileName,dictionary);
        }
        
        FileResource fr = new FileResource();
        String encrypted = fr.asString();
        breakForAllLangs(encrypted,languages);
        //printHalf(breakForLanguage(encrypted,dictionary));
    }

    public HashSet<String> readDictionary(FileResource fr){
        HashSet<String> dic = new HashSet<String>();
        for (String line : fr.lines()) {
            String crrLine = line.toLowerCase();
            dic.add(crrLine);            
        }
        return dic;
    }

    public void printHalf(String message){
        StringBuilder answer = new StringBuilder();
        int length = message.length();
        if (length > 200) {
            length = 200;
        }
        for(int k=0; k < length ; k++){
            answer.append(message.charAt(k)); 
        }
        System.out.print(answer.toString());
    }
    public int countWords(String message, HashSet<String> dictionary){
        String[] words = message.split("\\W+");
        int counter = 0;
        for (int k=0; k< words.length ; k++) {
            if (dictionary.contains(words[k].toLowerCase())) {
                counter++;
             }
        }
        return counter;
    }

    public char mostCommonCharIn(HashSet<String> dictionary){
        int[] alphaIndex = new int[26];
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for (String word: dictionary) {
            for (int k=0;k < word.length();k++) {
                char ch = Character.toLowerCase(word.charAt(k));
                int currIndex = alphabet.indexOf(ch);
                if(currIndex != -1){
                    alphaIndex[currIndex] += 1;
                }
            }
        }
        int max = alphaIndex[0];
        int index = 0;
        for (int k =1; k< alphaIndex.length ;k++ ) {
            if (max < alphaIndex[k] ) {
                max = alphaIndex[k];
                index = k;
            }
        }
        printKey(alphaIndex);
        System.out.println(alphabet.charAt(index));
        return alphabet.charAt(index);
    }

    public void breakForAllLangs(String encrypted, HashMap<String,HashSet<String>> languages){
        int maxCountWords = 0;
        String message = "NO FOUND";
        for (String language : languages.keySet()) {
            System.out.println("***"+language+"***");
            HashSet<String> dictionary = languages.get(language);
            String currDecryp =  breakForLanguage(encrypted,dictionary);
            int currCountWords = countWords(currDecryp,dictionary);
            if (maxCountWords < currCountWords) {
                message = currDecryp;
                maxCountWords = currCountWords;
            }
        }
        System.out.println(message);

    }
    public String breakForLanguage(String encrypted,HashSet<String> dictionary ){
        int maxCountWords = 0;
        String decryp = "NO FOUND";
        int[] finalKey = null;
        char ch = mostCommonCharIn(dictionary);
        for (int k=1 ;k<100 ; k++ ) {
            int[] currKey = tryKeyLength(encrypted,k,ch);
            VigenereCipher vigCip  =  new VigenereCipher(currKey);
            String currDecryp = vigCip.decrypt(encrypted);
            int currCountWords = countWords(currDecryp,dictionary);
            if (maxCountWords < currCountWords) {
                maxCountWords = currCountWords;
                decryp = currDecryp;
                finalKey = currKey;
            }
        }
        System.out.println("Count Words : "+maxCountWords);
        System.out.print("key : ");
        printKey(finalKey);
        System.out.println("key length : "+finalKey.length);
        return decryp;
    }
    public void testerBreakForLanguage(){
        FileResource dic = new FileResource();
        FileResource fr = new FileResource();
        String encrypted = fr.asString();
        HashSet<String> dictionary = readDictionary(dic);
        System.out.println(breakForLanguage(encrypted,dictionary));
    }

    public void printKey(int[] key){
         for (int k = 0; k<key.length ; k++) {
            if (k == 0) {
                  System.out.print("[");
            }
            System.out.print(key[k]);
            if (k != key.length-1) {
                  System.out.print(",");
            }

            if (k == key.length-1) {
                  System.out.print("]\n");
            }
        }

    }
    
    public void tester(){
        FileResource dic = new FileResource();
        FileResource fr = new FileResource();
        String encrypted = fr.asString();
        int[] key = tryKeyLength(encrypted,38,'e');
        VigenereCipher vigCip  =  new VigenereCipher(key);
        String message = vigCip.decrypt(encrypted);
        System.out.println("Count Words : "+countWords(message,readDictionary(dic)));
        System.out.print("key : ");
        printKey(key);
        System.out.println("key length : "+key.length);
        /*
        System.out.println(sliceString("abcdefghijklm", 0, 3));
        System.out.println(sliceString("abcdefghijklm", 1, 3));
        System.out.println(sliceString("abcdefghijklm", 2, 3));
        System.out.println(sliceString("abcdefghijklm", 0, 4));
        System.out.println(sliceString("abcdefghijklm", 1, 4));
        System.out.println(sliceString("abcdefghijklm", 2, 4));
        System.out.println(sliceString("abcdefghijklm", 3, 4));
        System.out.println(sliceString("abcdefghijklm", 0, 5));
        System.out.println(sliceString("abcdefghijklm", 1, 5));
        System.out.println(sliceString("abcdefghijklm", 2, 5));
        System.out.println(sliceString("abcdefghijklm", 3, 5));
        System.out.println(sliceString("abcdefghijklm", 4, 5));
        */
    }
}
