import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Praveen on 12/25/2019.
 */
public class BloomFilterTest {
    public static  BloomFilter<String> bloomFilter;

    @BeforeClass
    public static void setup() throws Exception
    {
        bloomFilter = new BloomFilter<String>(100,4);

    }

    @Test
    public void add() throws Exception {
        bloomFilter.clear();
        assertEquals(0, bloomFilter.getNumberOfElementsAdded());
        bloomFilter.add("Hello");
        bloomFilter.add("World");
        assertEquals(2, bloomFilter.getNumberOfElementsAdded());

    }

    @Test
    public void contains() throws Exception {
        bloomFilter.clear();
        assertEquals(0, bloomFilter.getNumberOfElementsAdded());
        bloomFilter.add("Hello");
        bloomFilter.add("World");

        assertTrue(!bloomFilter.contains("HelloWorld"));
    }

    @Test
    public void addRandomAlphabetString() throws Exception {

        int left = 97;// Alphabet 'a'
        int right = 122; //Alphabet 'z'
        int len = 10;
        int numOfWords = 10;
        BloomFilter  bloomFilter1 = new BloomFilter(numOfWords,4);
        Random random = new Random();
        String[] words = new String[numOfWords];
        for(int i=0;i<numOfWords;i++) {
            words[i] = random.ints(left, right+1)
                       .limit(len)
                        .collect(StringBuilder::new , StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
        }

        for(String word : words){
            bloomFilter1.add(word);
        }

        int truePositive =0;
        for(String word : words) {
            if(bloomFilter1.contains(word))
                ++truePositive;
        }

        double falseProb = 0.0 ;
        int  falsePositive  = len- truePositive;
        if(falsePositive >0)
        {
            falseProb = len/falsePositive;
        }
        System.out.print( String.format("Calculated false Prob : %.4f from the  filter:  %.4f " , falseProb, bloomFilter1.getFalseProbability()));



    }

}