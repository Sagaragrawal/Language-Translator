import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 */
public class MTSystem {
    public static void main(String[] args) {
        Map<String,String> englishWordToSpanishWord = new HashMap<String, String>();
        try {
            BufferedReader bufferedReader = new BufferedReader( new FileReader("translations.txt"));
            String line;
            while ( (line = bufferedReader.readLine()) != null ) {
                String[] parts = line.split("\\t");
                String english = parts[0].trim();
                String spanish = parts[1].trim();
                englishWordToSpanishWord.put(english,spanish);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter sentence to translate");
        String englishSentence = scanner.nextLine();
        String[] words = englishSentence.split("\\s+");
        String translatedSentence = "";
        for (String word : words) {
            if (englishWordToSpanishWord.containsKey(word)) {
                translatedSentence += englishWordToSpanishWord.get(word) + " ";
            } else {
                translatedSentence += word + " ";
            }
        }
        System.out.println( "translated sentence is: " + translatedSentence);
    }
}