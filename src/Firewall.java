/*
 * NAME: Zhaoyi Guo
 * PID: A15180402
 */
import java.io.*;
import java.util.Scanner;

/**
 * This program takes in 3 command line arguments. The first one is the malicious URL file. The second one is the
 * mixed URL file, which contains malicious URLs from the given malicious URL file, and other safe URLs as well.
 * The third one is the name of the file to write your output to, which contains every URL from the mixed URL file
 * that are for sure known to be safe after using bloom filter (think about what that means about the
 * false positives), one URL per line.
 *
 * Note that you should only use at most 3 bytes for each 2 malicious URLs in your Bloom Filter. For example, if the
 * malicious URL file contains 30000 URLs, then you should use at most 45000 bytes in your bloom filter.
 */
public class Firewall {
    static double changeInput = 1.5;
    static int PERCENTAGE = 100;
    /**
     * Main method that drives this program
     * @param args the command line argument
     */

    public static void main(String args[]) {

        // Get the size of badURL in bytes
        File badURL = new File(args[0]);
        long inputSize = badURL.length();
        long actualLength = 0;
        int falseInputs = 0;
        double allInput = 0;
        long bloomSize = 0;
        try {
            Scanner scLine = new Scanner(badURL);    //scan lines in file
            while (scLine.hasNextLine()) { //if the bad url file has next line
                String singleBad = scLine.nextLine();
                falseInputs++;
            }
            scLine.close();
        }
        catch (FileNotFoundException e) {
        }
        BloomFilter bf = new BloomFilter((int) (falseInputs * changeInput));
        try {
            Scanner scLine = new Scanner(badURL);    //scan lines in file
            while (scLine.hasNextLine()) {//if the file has next line
                String singleBad = scLine.nextLine();
                bf.insert(singleBad);

            }
            scLine.close();
        }
        catch (FileNotFoundException e) {
        }
        // Get the size of all urls in bytes
        File allURL = new File(args[1]);
        try {
            Scanner scLine = new Scanner(allURL);    //scan lines in file
            while (scLine.hasNextLine()) { //if the file has next line
                String single = scLine.nextLine();
                allInput++;
            }
            bloomSize = bf.numSlots;
            scLine.close();
        }
        catch (FileNotFoundException e) {
        }
        // PrintWriter must be put in try catch block to catch
        // potential exception
        File goodURL = new File(args[2]);
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(goodURL,true));
            Scanner scLine = new Scanner(allURL);    //scan lines in file
            while (scLine.hasNextLine()) { //if the file has next line
                String singleURL = scLine.nextLine();
                if (!bf.find(singleURL)) {
                    actualLength++;
                    pw.println(singleURL);

                }
            }
            scLine.close();
            pw.close();
        }
        catch (IOException e) { // If the given file doesn’t exist
            System.out.println("File not found!");
        }



        // print statistics
        System.out.println("False positive rate: "
                + PERCENTAGE *(((allInput - falseInputs) - actualLength ) / (allInput - falseInputs)));
        System.out.println("Saved memory ratio: " + inputSize / (falseInputs * changeInput));
    }

}
