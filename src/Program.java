import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 
 */

/**
 * @author Ravi
 *
 */
public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length >= 2) {
			input = args[0];
			output = args[1];
		}
		
		List<File> files = getFileNames(input);
		countWords(files);
		writeCountWords();
		writeMedainWords();
	}

	
	/**
	 * this method splits the line based on certain characters
	 * @param tweet
	 */
	private static String[] split(String tweet) {
		return tweet.split(" ");
	}
	
	/**
	 * this method is used to find the token present in the string
	 * @param line
	 */
	private static void tokennize(String tweet) {
		String[] words = split(tweet);
		TreeSet<String> localSet = new TreeSet<String>();
		for (String word : words) {
			if (word == null || word.equals(""))
				continue;
			String key = word.trim();
			
			if(!localSet.contains(word)) {
				localSet.add(word);
			}
			
			if (!wordsMap.containsKey(key)) {
				wordsMap.put(key, 0L);
			}
			wordsMap.put(key, wordsMap.get(key) + 1);
		}
		
		uniqueWordsPerLine.add(localSet.size());
		
		int count = uniqueWordsPerLine.size(); 
		if((count - 1) % 2 == 0) {
			median.add(uniqueWordsPerLine.get(count / 2) * 1.0f);
		} else {
			median.add((uniqueWordsPerLine.get(count / 2)  + uniqueWordsPerLine.get(count / 2 - 1)) / 2.0f);
		}
	}
	
	/**
	 * this method is used to count the words in all the files
	 * @param files
	 */
	private static void countWords(List<File> files) {
		for (File file : files) {
			try (Reader reader = new FileReader(file);
					BufferedReader bufferedReader = new BufferedReader(reader)) {
				String tweet = "";
				while (true) {
					tweet = bufferedReader.readLine();
					if (tweet == null)
						break;
					tokennize(tweet);
				}
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
		}
	}
	
	/**
	 * this method is used to print the words and their count into a file 
	 */
	private static void writeCountWords() {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(output + "/ft1.txt"), "utf-8"))) {
			while(!wordsMap.isEmpty()) {
				Entry<String, Long> temp = wordsMap.pollFirstEntry();
				writer.write(temp.getKey() + "    " + temp.getValue());
				writer.write("\n");
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * this method is used to print the words and their count into a file 
	 */
	private static void writeMedainWords() {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(output + "/ft2.txt"), "utf-8"))) {
			for(Float f : median) {
				writer.write(f.toString());
				writer.write("\n");
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * this method is used for getting the files present in the directory
	 * @param folderName
	 * @return
	 */
	private static List<File> getFileNames(String folderName) {
		File inputFolder = new File(folderName);
		List<File> retVal = new ArrayList<File>();
		for (File file : inputFolder.listFiles()) {
			if (file.isDirectory()) {
				retVal.addAll(getFileNames(file.getPath()));
			} else {
				retVal.add(file);
			}
		}
		return retVal;
	}
	
	private static TreeMap<String, Long> wordsMap = new TreeMap<String, Long>();
	private static List<Integer> uniqueWordsPerLine = new ArrayList<Integer>();
	private static List<Float> median = new ArrayList<Float>();
	private static String input = "tweet_input";
	private static String output = "tweet_output";
}
