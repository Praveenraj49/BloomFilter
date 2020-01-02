/**
 * Created by Praveen on 12/25/2019.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
public class SpellChecker {

    public Set<String>  dictionary;
    public BloomFilter<String> filter;

    public SpellChecker(String filePath ) throws Exception {
        read(filePath);

        filter  = new BloomFilter<String>(dictionary.size(), 4);
        for(String word : dictionary) {
            filter.add(word);
        }

    }

    private  void  read(String filePath) throws Exception {
        File file = new File(filePath);
        BufferedReader reader  = new BufferedReader(new FileReader(file));
        String word;
        dictionary = new HashSet<>();
        while(( word = reader.readLine()) != null) {
            dictionary.add(word);
        }
    }

    public boolean check(String word) {
         return filter.contains(word);
    }


    public boolean absCheck(String word) {
        return dictionary.contains(word);
    }


    /**
     * Test Main Method to check the spell checker , takes  full file path as input
     * @param args
     * @throws Exception
     */

    public static  void main (String[] args) throws Exception  {

        String filePath = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Dictionary Full path Example  Windows path  C:\\\\users\\\\word.txt ");
        filePath = scanner.nextLine();


        try {
            SpellChecker checker = new SpellChecker(filePath);


        String[] words = new String[] {"Blatant" , "Yes" , "AFAIK", "ahghakhf", "Hello", "World", "fjhshhs"};
        for(String word : words)
        {
            if(checker.check(word))
            {
                System.out.println(word + " Is Present in the filter");
                if(checker.absCheck(word))
                {
                    System.out.println("Returned True");
                }
                else
                {
                    System.out.println("Returned False Positive");
                }
            }
            else
            {
                System.out.println(word + " Is NOT Present in the filter");
            }
        }

        }

        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            System.exit(0);
        }
    }

}
