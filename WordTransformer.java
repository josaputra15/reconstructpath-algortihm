import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordTransformer {

    public static Set<String> readThreeLetterWords(String filePath) throws IOException {
        Set<String> wordCollectionSet = new HashSet<>();
        BufferedReader wordsReader = new BufferedReader(new FileReader(filePath));
        String wordEntry;

        while ((wordEntry = wordsReader.readLine()) != null) {
            wordEntry = wordEntry.trim();
            if (wordEntry.length() == 3) {
                wordCollectionSet.add(wordEntry);
            }
        }
        wordsReader.close();
        return wordCollectionSet;
    }

    public static List<String> exploreSimilarWords(String word, Set<String> wordCollectionSet) {
        List<String> similarWords = new ArrayList<>();

        for (int i = 0; i < word.length(); i++) {
            char[] wordTrimmed = word.toCharArray();

            for (char c = 'a'; c <= 'z'; c++) {
                if (c != wordTrimmed[i]) {
                    wordTrimmed[i] = c;
                    String newWord = new String(wordTrimmed);

                    if (wordCollectionSet.contains(newWord)) {
                        similarWords.add(newWord);
                    }
                }
            }
        }
        return similarWords;
    }

 
    public static List<String> BFSPath(Set<String> wordCollectionSet, String source, String destination) {
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parentsMap = new HashMap<>();

        queue.add(source);
        parentsMap.put(source, null);

        while (!queue.isEmpty()) {
            String currentWord = queue.poll();

            if (currentWord.equals(destination)) {
                List<String> path = new ArrayList<>();
                while (currentWord != null) {
                    path.add(currentWord);
                    currentWord = parentsMap.get(currentWord);
                }
                Collections.reverse(path);
                return path;
            }

            for (String similarWords : exploreSimilarWords(currentWord, wordCollectionSet)) {
                if (!parentsMap.containsKey(similarWords)) {
                    parentsMap.put(similarWords, currentWord);
                    queue.add(similarWords);
                }
            }
        }

        return null;
    }


    public static void findWordPath(String filePath, String source, String destination) {
        try {
            Set<String> wordCollectionSet = readThreeLetterWords(filePath);
            
        
            if (!wordCollectionSet.contains(source) || !wordCollectionSet.contains(destination)) {
                System.out.println("Source | destination word is not in the list.");
                return;
            }

            List<String> path = BFSPath(wordCollectionSet, source, destination);
            if (path != null) {
                System.out.println(String.join(" → ", path));
            } else {
                System.out.printf("No path found", source, destination);
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    public static void main(String[] args) {
        findWordPath("threeletterwords.txt", "red", "air");
    }
}

