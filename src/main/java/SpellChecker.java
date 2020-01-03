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

    private Set<String>  dictionary;
    private BloomFilter<String> filter;

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

    public double getProbability() {
        return (1-filter.getFalseProbability());
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

    private static boolean validateFile(String filePath) {
       File file = new File(filePath);
       return (file.exists() && file.canRead());
    }

    private static void  exit(Scanner scanner){
        System.out.println(String.format("Do you want to exit [Yes|No]"));
        String input = scanner.nextLine();
        input = input.toUpperCase();
        switch(input){
            case "Y":
            case "YES":
                System.exit(0);
            case "N":
            case "NO":
                break;
            default :
                System.out.println(String.format("Invalid choice will continue with application enter exit to exit again"));
                break;
        }

    }

    public static  void main (String[] args) throws Exception  {

        //TODO Provide an absolute check option , when set the if the filter returns true  will check against the
        // Dictionary to confirm

        String message = String.format("Welcome to Spell Checker Application \n"+
                                       "Usage enter the full path of the dictionary to load \n"+
                                       "Windows : C:\\\\users\\\\words.txt \n"+
                                       "Unix : /usr/share/words.txt \n"+
                                       "File must be in the format where in each line contains a single word in dictionary \n" +
                                       "To exit the application anytime enter exit in the console \n"+
                                       "Enter the dictionary full path to continue......");

        final String EXIT="EXIT";

        System.out.println(message);
        String filePath;
        Scanner scanner = new Scanner(System.in);

        filePath = scanner.nextLine();

        while(filePath == null || !validateFile(filePath)) {
            if(filePath ==null || filePath.length()==0 ) {
                System.out.print("Enter a  filePath of the dictionary to continue");
            }
            else if(filePath.equalsIgnoreCase(EXIT)){
                exit(scanner);
            }
            else
            {
                System.out.println(String.format("File Name %s doesn't exists or cannot read the file please check the file exists and readable" , filePath));
            }
            filePath = scanner.nextLine();
        }


        try {
            System.out.println(String.format("Loading the Dictionary"));
            SpellChecker checker = new SpellChecker(filePath);
            System.out.println(String.format("Dictionary Loading Completed , Enter a single word to spell check"));

            String word;
            while(true) {
                word = scanner.nextLine();
                if(word == null || word.length() ==0){
                    System.out.println(String.format("Enter non empty word to continue ..."));
                }

                if(word.equalsIgnoreCase(EXIT)){
                    exit(scanner);
                }


                if(checker.check(word)) {
                    System.out.println(String.format("Entered word : %s is present in the dictionary  with a probability %f" , word, checker.getProbability()));
                }
                else {
                    System.out.println(String.format("Entered word : %s is not present in the dictionary with a probability %f" , word ,1.0));
                }
            }


        //String[] words = new String[] {"Blatant" , "Yes" , "AFAIK", "ahghakhf", "Hello", "World", "fjhshhs"};

        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            System.out.println(String.format("Press Enter to exit......."));
            String input = scanner.nextLine();
            System.exit(0);
        }
    }

}
