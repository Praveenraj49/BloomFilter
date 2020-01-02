

/**
 * Created by Praveen on 01/02/2020.
 */

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;


/**
 * Bloom Filter data structure implementation provides a ability to quickly check if an
 * Object is present in the set in an highly memory efficient way.
 * Bloom filter is probabilistic  data structure meaning  returns false positive I.e an object
 * may not be present but may return true but never false negative if an object is not present then its 100% true.
 *
 * @param <E>
 */
//TODO make thread safe
public class BloomFilter<E> {

    private BitSet bitSet;
    private int bitSetSize;
    private int numberOfElements;
    private int numberOfElementsAdded;
    private int bitsPerElement;
    private MessageDigest messageDigest;
    private static int numHashes;  // number of hashes to use
    private static String hashName = "MD5";
    private static final Charset charset = Charset.forName("UTF-8"); // encoding used for hash values as string

   // TODO use a non crytographic hash function like MurMur , FNV or Hashmix

    /***
     * Create a Bloom Filter with number of elements , bits per element and hash function to use.
     * @param numberOfElements
     * @param bitsPerElement
     * @param hash
     * @throws Exception
     */
    public BloomFilter(int numberOfElements, int bitsPerElement, String hash) throws Exception {
        bitSetSize = bitsPerElement * numberOfElements;
        bitSet = new BitSet(bitSetSize);
        this.numberOfElements = numberOfElements;
        this.bitsPerElement = bitsPerElement;
        this.hashName = hash;
        this.numHashes = (int)Math.round((bitSetSize / (double)numberOfElements)*Math.log(2.0));
        numberOfElementsAdded =0;
        try {
            messageDigest = MessageDigest.getInstance(hash);
            hashName = hash;
        } catch (NoSuchAlgorithmException ex) {

            throw ex;
        }

    }

    /***
     * Create a Bloom Filter with number of elements , bits per element  default hash function MD5 will be used.
     * @param numberOfElements
     * @param bitsPerElement
     * @throws Exception
     */

    public BloomFilter(int numberOfElements, int bitsPerElement) throws Exception {
        this(numberOfElements, bitsPerElement, hashName);
    }

    //TODO  Add a constructor  with a user defined false probability


    private int[] createHash(E element) {
        byte[] data = element.toString().getBytes(charset);
        return createHash(data, numHashes);
    }

    private int[] createHash(byte[] bytes, int numHashes) {
        int[] hashes = new int[numHashes];
        int k = 0;
        byte salt = 0;

        while (k < numHashes) {
            messageDigest.update(salt);
            byte[] digest = messageDigest.digest(bytes);
            for (int i = 0; i < digest.length / 4 && k < numHashes; i++) {
                int hash = 0;
                for (int j = (i * 4); j < (i * 4) + 4; j++) {
                    hash <<= 8;
                    hash |= ((int) digest[j]) & 0xff;
                }
                hashes[k] = Math.abs(hash % bitSetSize);
                k++;
            }

        }

        return hashes;
    }

    /**
     * Add an element to Bloom filter
     * @param element
     */


    public void add(E element) {
        int[] hashes = createHash(element);
        for (int hash : hashes) {
            bitSet.set(hash, true);
        }
        numberOfElementsAdded++;

    }

    //TODO Add a method  to add Collections of elements

    /**
     * Check if the Bloom filter contains the element , returns false postive but never false negative.
     * @param element
     * @return
     */

    public boolean contains(E element) {
        int[] hashes = createHash(element);
        for (int hash : hashes) {
            if (!bitSet.get(hash))
                return false;
        }
        return true;
    }


    /**
     * Clear the bloom filter and elements will be removed.
     */


    public void clear(){
        bitSet.clear();
        numberOfElementsAdded =0;
    }

    /**
     * Return the count of number elements added in the Bloom filter.
     * @return
     */
    public int getNumberOfElementsAdded() {
        return numberOfElementsAdded;
    }

    /**
     * Return the number bits used per element in the Bloom filter
     * @return
     */
    public int getBitsPerElement() {
        return bitsPerElement;
    }

    /**
     * Calculate and return the false Probability rate of bloom filter
     * @return
     */

    public double getFalseProbability() {
        return Math.pow(1- Math.exp(-numHashes * (double) numberOfElementsAdded / (double) bitSetSize), numHashes);
    }

}