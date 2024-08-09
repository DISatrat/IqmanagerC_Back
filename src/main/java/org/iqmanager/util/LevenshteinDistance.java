package org.iqmanager.util;

import org.javatuples.Pair;
import org.javatuples.Quartet;

import java.util.*;
import java.util.stream.Collectors;

public class LevenshteinDistance {

    private Map<Integer, List<Integer>> distanceCodeKey = new HashMap<>();
    private final Map<Character, Integer> codeKeysRus = new HashMap<>();
    private final Map<Character, Integer> codeKeysEng = new HashMap<>();
    private final Map<Character, String> translitRuEn = new HashMap<>();
    private List<LanguageSet> samples = new ArrayList<>();
    private Map<Character, List<Character>> phoneticGroupsRus = new HashMap<>();
    private Map<Character, List<Character>> phoneticGroupsEng = new HashMap<>();

    static class Word {

        private String text;
        private List<Integer> codes;

        public Word() {
        }

        public Word(String text, List<Integer> codes) {
            this.text = text;
            this.codes = codes;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<Integer> getCodes() {
            return codes;
        }

        public void setCodes(List<Integer> codes) {
            this.codes = codes;
        }
    }

    static class AnalizeObject {
        public String original;
        public List<Word> words = new ArrayList<>();

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public List<Word> getWords() {
            return words;
        }

        public void setWords(List<Word> words) {
            this.words = words;
        }
    }
    static class LanguageSet {
        public AnalizeObject rus = new AnalizeObject();
        public AnalizeObject eng = new AnalizeObject();

        public AnalizeObject getRus() {
            return rus;
        }

        public void setRus(AnalizeObject rus) {
            this.rus = rus;
        }

        public AnalizeObject getEng() {
            return eng;
        }

        public void setEng(AnalizeObject eng) {
            this.eng = eng;
        }
    }


    public void setData(List<String> datas) {
        Map<Character, Integer> codeKeys = new HashMap<>();
        codeKeys.putAll(codeKeysRus);
        codeKeys.putAll(codeKeysEng);
        for(String data : datas) {
            LanguageSet languageSet = new LanguageSet();
            languageSet.getRus().setOriginal(data);
            if (data.length() > 0) {
                languageSet.getRus().setWords(Arrays.stream(data.split(" "))
                                                    .map(x -> new Word(x.toLowerCase(Locale.ROOT), getKeyCodes(codeKeys, x)))
                                                    .collect(Collectors.toList()));
            }
            languageSet.getRus().setOriginal(data);
            this.samples.add(languageSet);
        }
    }


    public List<Quartet<String, String, Double, Integer>> search(String targetStr) {
        Map<Character, Integer> codeKeys = new HashMap<>();
        codeKeys.putAll(codeKeysRus);
        codeKeys.putAll(codeKeysEng);
        AnalizeObject originalSearchObj = new AnalizeObject();
        if (targetStr.length() > 0) {
            originalSearchObj.words = Arrays.stream(targetStr.split(" "))
                    .map(x -> new Word(x.toLowerCase(Locale.ROOT), getKeyCodes(codeKeys, x))).collect(Collectors.toList());
        }
        AnalizeObject translationSearchObj = new AnalizeObject();
        if (targetStr.length() > 0) {
            translationSearchObj.words = Arrays.stream(targetStr.split(" ")).map(x -> {
                    String translateStr = transliterate(x.toLowerCase(Locale.ROOT), translitRuEn);
            return new Word(translateStr, getKeyCodes(codeKeys, translateStr)) {

            };
                }).collect(Collectors.toList());
        }
        List<Quartet<String, String, Double, Integer>> result = new ArrayList<>();
        for(LanguageSet sampl : samples) {
            int languageType = 1;
            double cost = getRangePhrase(sampl.getRus(), originalSearchObj, false);
            double tempCost = getRangePhrase(sampl.getEng(), originalSearchObj, false);
            if (cost > tempCost) {
                cost = tempCost;
                languageType = 3;
            }
            // Проверка транслетерационной строки
            tempCost = getRangePhrase(sampl.getRus(), translationSearchObj, true);
            if (cost > tempCost) {
                cost = tempCost;
                languageType = 2;
            }
            tempCost = getRangePhrase(sampl.getEng(), translationSearchObj, true);
            if (cost > tempCost) {
                cost = tempCost;
                languageType = 3;
            }
            result.add(new Quartet<>(sampl.getRus().getOriginal(), sampl.getEng().getOriginal(), cost, languageType));
        }
        return result;
    }


    private double getRangePhrase(AnalizeObject source, AnalizeObject search, boolean translation) {
        if (!source.getWords().isEmpty()) {
            if (!search.getWords().isEmpty())
                return 0;

            return search.getWords().stream().mapToInt(x -> x.getText().length()).sum() * 2 * 100;
        }
        if (!search.getWords().isEmpty()) {
            return source.getWords().stream().mapToInt(x -> x.getText().length()).sum() * 2 * 100;
        }
        double result = 0;
        for (int i = 0; i < search.getWords().size(); i++) {
            double minRangeWord = Double.MAX_VALUE;
            int minIndex = 0;
            for (int j = 0; j < source.getWords().size(); j++) {
                double currentRangeWord = getRangeWord(source.getWords().get(i), search.getWords().get(i), translation);
                if (currentRangeWord < minRangeWord) {
                    minRangeWord = currentRangeWord;
                    minIndex = j;
                }
            }
            result += minRangeWord * 100 + (Math.abs(i - minIndex) / 10.0);
        }
        return result;
    }


    private double getRangeWord(Word source, Word target, boolean translation) {
        double minDistance = Double.MAX_VALUE;
        Word croppedSource = new Word();
        int length = Math.min(source.getText().length(), target.getText().length() + 1);
        for (int i = 0; i <= source.getText().length() - length; i++) {
            croppedSource.setText(source.getText().substring(i, length));
            croppedSource.setCodes(source.getCodes().stream().skip(i).limit(length).collect(Collectors.toList()));
            minDistance = Math.min(minDistance, levenshteinDistance(croppedSource, target, croppedSource.getText().length() == source.getText().length(), translation) + (i * 2 / 10.0));
        }
        return minDistance;
    }


    private int levenshteinDistance(Word source, Word target, boolean fullWord, boolean translation) {

        if(source.getText() == null || source.getText().equals("")) {
            if(target.getText() == null || target.getText().equals("")){
                return 0;
            }
            return target.getText().length();
        }

        if(target.getText() == null || target.getText().equals("")) {
            return source.getText().length();
        }


        int m = target.getText().length();
        int n = source.getText().length();
        int[][] distance = new int[2][m +1];
        for(int j = 1; j <= m; j++) {
            distance[0][j] = j * 2;
        }

        int currentRow = 0;
        for(int i = 1; i <= n; ++i) {
            currentRow = i % 3;
            distance[currentRow][0] = i * 2;
            int previousRow = (i - 1) % 3;
            for(int j = 1; j <= m; j++) {
                distance[currentRow][j] = min( distance[previousRow][j] + ((!fullWord && i == n) ? 2 - 1 : 2),
                                               distance[currentRow][j - 1] + ((!fullWord && i == n) ? 2 - 1 : 2),
                                               distance[previousRow][j - 1] + costDistanceSymbol(source, i - 1, target, j - 1, translation));

                if (i > 1 && j > 1 && source.getText().charAt(i - 1) == target.getText().charAt(j - 2) && source.getText().charAt(i - 2) == target.getText().charAt(j - 1)) {
                    distance[currentRow][j] =Math.min(distance[currentRow][j], distance[(i - 2) % 3][j - 2] + 2);
                }
            }
        }
        return distance[currentRow][m];
    }


    private static int min(int n1, int n2, int n3) {
        return Math.min(Math.min(n1, n2), n3);
    }

    List<Integer> nearKeys = new ArrayList<>();
    List<Character> phoneticGroups = new ArrayList<>();

    private int costDistanceSymbol(Word source, int sourcePosition, Word search, int searchPosition, boolean translation) {
        if (source.getText().charAt(sourcePosition) == search.getText().charAt(searchPosition))
            return 0;
        if (translation)
            return 2;
        if (source.getCodes().get(sourcePosition) != 0 && Objects.equals(source.getCodes().get(sourcePosition), search.getCodes().get(searchPosition)))
            return 0;
        int resultWeight = 0;

        if (!distanceCodeKey.containsKey(source.getCodes().get(searchPosition))) {
            resultWeight = 2;
        } else {
            resultWeight = nearKeys.contains(search.codes.get(searchPosition)) ? 1 : 2;
        }
        if (phoneticGroupsRus.containsKey(search.text.charAt(searchPosition))) {
            resultWeight = Math.min(resultWeight, phoneticGroups.contains(source.text.charAt(sourcePosition)) ? 1 : 2);
        }
        if (phoneticGroupsEng.containsKey(search.text.charAt(searchPosition))) {
            resultWeight = Math.min(resultWeight, phoneticGroups.contains(source.text.charAt(sourcePosition)) ? 1 : 2);
        }
        return resultWeight;
    }

    private List<Integer> getKeyCodes(Map<Character, Integer> codeKeys, String word) {

        List<Integer> keyCodes = new ArrayList<>();
        char[] chars = word.toLowerCase(Locale.ROOT).toCharArray();
        for(char c : chars) {
            keyCodes.add(codeKeys.get(c));
        }

        return keyCodes;
    }


    private String transliterate(String text, Map<Character, String> translitRuEn) {
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < text.length(); i++) {
            if(translitRuEn.containsKey(text.charAt(i))){
                result.append(translitRuEn.get(text.charAt(i)));
            } else {
                result.append(text.charAt(i));
            }
        }
        return result.toString();
    }


    public LevenshteinDistance() {
        phoneticGroupsRus.put('ы',Arrays.asList('и', 'й'));
        phoneticGroupsRus.put('и', Arrays.asList('ы', 'й', 'е'));
        phoneticGroupsRus.put('й', Arrays.asList('и', 'ы'));
        phoneticGroupsRus.put('э', List.of('е'));
        phoneticGroupsRus.put('е', Arrays.asList('э', 'о', 'ё', 'и'));
        phoneticGroupsRus.put('а', Arrays.asList('я', 'о'));
        phoneticGroupsRus.put('я', List.of('а'));
        phoneticGroupsRus.put('о', Arrays.asList('ё', 'е', 'а'));
        phoneticGroupsRus.put('ё', Arrays.asList('о', 'е'));
        phoneticGroupsRus.put('у', List.of('ю'));
        phoneticGroupsRus.put('ю', List.of('у'));
        phoneticGroupsRus.put('ш', List.of('щ'));
        phoneticGroupsRus.put('щ', List.of('ш'));



        phoneticGroupsEng.put('a', Arrays.asList('e', 'i', 'o', 'u', 'y'));
        phoneticGroupsEng.put('e', Arrays.asList('a', 'i', 'o', 'u', 'y'));
        phoneticGroupsEng.put('i', Arrays.asList('a', 'e', 'o', 'u', 'y'));
        phoneticGroupsEng.put('o', Arrays.asList('a', 'e', 'i', 'u', 'y'));
        phoneticGroupsEng.put('u', Arrays.asList('a', 'e', 'i', 'o', 'y'));
        phoneticGroupsEng.put('y', Arrays.asList('a', 'e', 'i', 'o', 'u'));
        phoneticGroupsEng.put('b', List.of('p'));
        phoneticGroupsEng.put('p', Arrays.asList('b', 'f', 'v'));
        phoneticGroupsEng.put('c', Arrays.asList('k', 'q', 's', 'z'));
        phoneticGroupsEng.put('k', Arrays.asList('c', 'q'));
        phoneticGroupsEng.put('q', Arrays.asList('c', 'k'));
        phoneticGroupsEng.put('d', List.of('t'));
        phoneticGroupsEng.put('t', List.of('d'));
        phoneticGroupsEng.put('l', List.of('r'));
        phoneticGroupsEng.put('r', List.of('l'));
        phoneticGroupsEng.put('m', List.of('n'));
        phoneticGroupsEng.put('n', List.of('m'));
        phoneticGroupsEng.put('g', List.of('j'));
        phoneticGroupsEng.put('j', List.of('g'));
        phoneticGroupsEng.put('f', Arrays.asList('p', 'v'));
        phoneticGroupsEng.put('v', Arrays.asList('f', 'p'));
        phoneticGroupsEng.put('s', Arrays.asList('x', 'z', 'c'));
        phoneticGroupsEng.put('x', Arrays.asList('s', 'z'));
        phoneticGroupsEng.put('z', Arrays.asList('s', 'z'));




        /* '`' */  distanceCodeKey.put(192, List.of(49));
        /* '1' */  distanceCodeKey.put(49, Arrays.asList( 50, 87, 81));
        /* '2' */  distanceCodeKey.put(50, Arrays.asList( 49, 81, 87, 69, 51));
        /* '3' */  distanceCodeKey.put(51, Arrays.asList( 50, 87, 69, 82, 52));
        /* '4' */  distanceCodeKey.put(52, Arrays.asList( 51, 69, 82, 84, 53));
        /* '5' */  distanceCodeKey.put(53, Arrays.asList( 52, 82, 84, 89, 54));
        /* '6' */  distanceCodeKey.put(54, Arrays.asList( 53, 84, 89, 85, 55));
        /* '7' */  distanceCodeKey.put(55, Arrays.asList( 54, 89, 85, 73, 56));
        /* '8' */  distanceCodeKey.put(56, Arrays.asList( 55, 85, 73, 79, 57));
        /* '9' */  distanceCodeKey.put(57, Arrays.asList( 56, 73, 79, 80, 48));
        /* '0' */  distanceCodeKey.put(48, Arrays.asList( 57, 79, 80, 219, 189));
        /* '-' */  distanceCodeKey.put(189, Arrays.asList( 48, 80, 219, 221, 187));
        /* '+' */  distanceCodeKey.put(187, Arrays.asList( 189, 219, 221));
        /* 'q' */  distanceCodeKey.put(81, Arrays.asList( 49, 50, 87, 83, 65));
        /* 'w' */  distanceCodeKey.put(87, Arrays.asList( 49, 81, 65, 83, 68, 69, 51, 50));
        /* 'e' */  distanceCodeKey.put(69, Arrays.asList( 50, 87, 83, 68, 70, 82, 52, 51));
        /* 'r' */  distanceCodeKey.put(82, Arrays.asList( 51, 69, 68, 70, 71, 84, 53, 52));
        /* 't' */  distanceCodeKey.put(84, Arrays.asList( 52, 82, 70, 71, 72, 89, 54, 53));
        /* 'y' */  distanceCodeKey.put(89, Arrays.asList( 53, 84, 71, 72, 74, 85, 55, 54));
        /* 'u' */  distanceCodeKey.put(85, Arrays.asList( 54, 89, 72, 74, 75, 73, 56, 55));
        /* 'i' */  distanceCodeKey.put(73, Arrays.asList( 55, 85, 74, 75, 76, 79, 57, 56));
        /* 'o' */  distanceCodeKey.put(79, Arrays.asList( 56, 73, 75, 76, 186, 80, 48, 57));
        /* 'p' */  distanceCodeKey.put(80, Arrays.asList( 57, 79, 76, 186, 222, 219, 189, 48));
        /* '[' */  distanceCodeKey.put(219, Arrays.asList( 48, 186, 222, 221, 187, 189));
        /* ']' */  distanceCodeKey.put(221, Arrays.asList( 189, 219, 187));
        /* 'a' */  distanceCodeKey.put(65, Arrays.asList( 81, 87, 83, 88, 90));
        /* 's' */  distanceCodeKey.put(83, Arrays.asList( 81, 65, 90, 88, 67, 68, 69, 87, 81));
        /* 'd' */  distanceCodeKey.put(68, Arrays.asList( 87, 83, 88, 67, 86, 70, 82, 69));
        /* 'f' */  distanceCodeKey.put(70, Arrays.asList( 69, 68, 67, 86, 66, 71, 84, 82));
        /* 'g' */  distanceCodeKey.put(71, Arrays.asList( 82, 70, 86, 66, 78, 72, 89, 84));
        /* 'h' */  distanceCodeKey.put(72, Arrays.asList( 84, 71, 66, 78, 77, 74, 85, 89));
        /* 'j' */  distanceCodeKey.put(74, Arrays.asList( 89, 72, 78, 77, 188, 75, 73, 85));
        /* 'k' */  distanceCodeKey.put(75, Arrays.asList( 85, 74, 77, 188, 190, 76, 79, 73));
        /* 'l' */  distanceCodeKey.put(76, Arrays.asList( 73, 75, 188, 190, 191, 186, 80, 79));
        /* ';' */  distanceCodeKey.put(186, Arrays.asList( 79, 76, 190, 191, 222, 219, 80));
        /* '\''*/  distanceCodeKey.put(222, Arrays.asList( 80, 186, 191, 221, 219));
        /* 'z' */  distanceCodeKey.put(90, Arrays.asList( 65, 83, 88));
        /* 'x' */  distanceCodeKey.put(88, Arrays.asList( 90, 65, 83, 68, 67));
        /* 'c' */  distanceCodeKey.put(67, Arrays.asList( 88, 83, 68, 70, 86));
        /* 'v' */  distanceCodeKey.put(86, Arrays.asList( 67, 68, 70, 71, 66));
        /* 'b' */  distanceCodeKey.put(66, Arrays.asList( 86, 70, 71, 72, 78));
        /* 'n' */  distanceCodeKey.put(78, Arrays.asList( 66, 71, 72, 74, 77));
        /* 'm' */  distanceCodeKey.put(77, Arrays.asList( 78, 72, 74, 75, 188));
        /* '<' */  distanceCodeKey.put(188, Arrays.asList( 77, 74, 75, 76, 190));
        /* '>' */  distanceCodeKey.put(190, Arrays.asList( 188, 75, 76, 186, 191));
        /* '?' */  distanceCodeKey.put(191, Arrays.asList( 190, 76, 186, 222));


        codeKeysRus.put( 'ё' , 192 );
        codeKeysRus.put( '1' , 49  );
        codeKeysRus.put( '2' , 50  );
        codeKeysRus.put( '3' , 51  );
        codeKeysRus.put( '4' , 52  );
        codeKeysRus.put( '5' , 53  );
        codeKeysRus.put( '6' , 54  );
        codeKeysRus.put( '7' , 55  );
        codeKeysRus.put( '8' , 56  );
        codeKeysRus.put( '9' , 57  );
        codeKeysRus.put( '0' , 48  );
        codeKeysRus.put( '-' , 189 );
        codeKeysRus.put( '=' , 187 );
        codeKeysRus.put( 'й' , 81  );
        codeKeysRus.put( 'ц' , 87  );
        codeKeysRus.put( 'у' , 69  );
        codeKeysRus.put( 'к' , 82  );
        codeKeysRus.put( 'е' , 84  );
        codeKeysRus.put( 'н' , 89  );
        codeKeysRus.put( 'г' , 85  );
        codeKeysRus.put( 'ш' , 73  );
        codeKeysRus.put( 'щ' , 79  );
        codeKeysRus.put( 'з' , 80  );
        codeKeysRus.put( 'х' , 219 );
        codeKeysRus.put( 'ъ' , 221 );
        codeKeysRus.put( 'ф' , 65  );
        codeKeysRus.put( 'ы' , 83  );
        codeKeysRus.put( 'в' , 68  );
        codeKeysRus.put( 'а' , 70  );
        codeKeysRus.put( 'п' , 71  );
        codeKeysRus.put( 'р' , 72  );
        codeKeysRus.put( 'о' , 74  );
        codeKeysRus.put( 'л' , 75  );
        codeKeysRus.put( 'д' , 76  );
        codeKeysRus.put( 'ж' , 186 );
        codeKeysRus.put( 'э' , 222 );
        codeKeysRus.put( 'я' , 90  );
        codeKeysRus.put( 'ч' , 88  );
        codeKeysRus.put( 'с' , 67  );
        codeKeysRus.put( 'м' , 86  );
        codeKeysRus.put( 'и' , 66  );
        codeKeysRus.put( 'т' , 78  );
        codeKeysRus.put( 'ь' , 77  );
        codeKeysRus.put( 'б' , 188 );
        codeKeysRus.put( 'ю' , 190 );
        codeKeysRus.put( '.' , 191 );
        codeKeysRus.put( '!' , 49  );
        codeKeysRus.put( '"' , 50  );
        codeKeysRus.put( '№' , 51  );
        codeKeysRus.put( ';' , 52  );
        codeKeysRus.put( '%' , 53  );
        codeKeysRus.put( ':' , 54  );
        codeKeysRus.put( '?' , 55  );
        codeKeysRus.put( '*' , 56  );
        codeKeysRus.put( '(' , 57  );
        codeKeysRus.put( ')' , 48  );
        codeKeysRus.put( '_' , 189 );
        codeKeysRus.put( '+' , 187 );
        codeKeysRus.put( ',' , 191 );


        codeKeysEng.put('`', 192 );
        codeKeysEng.put('1', 49  );
        codeKeysEng.put('2', 50  );
        codeKeysEng.put('3', 51  );
        codeKeysEng.put('4', 52  );
        codeKeysEng.put('5', 53  );
        codeKeysEng.put('6', 54  );
        codeKeysEng.put('7', 55  );
        codeKeysEng.put('8', 56  );
        codeKeysEng.put('9', 57  );
        codeKeysEng.put('0', 48  );
        codeKeysEng.put('-', 189 );
        codeKeysEng.put('=', 187 );
        codeKeysEng.put('q', 81  );
        codeKeysEng.put('w', 87  );
        codeKeysEng.put('e', 69  );
        codeKeysEng.put('r', 82  );
        codeKeysEng.put('t', 84  );
        codeKeysEng.put('y', 89  );
        codeKeysEng.put('u', 85  );
        codeKeysEng.put('i', 73  );
        codeKeysEng.put('o', 79  );
        codeKeysEng.put('p', 80  );
        codeKeysEng.put('[', 219 );
        codeKeysEng.put(']', 221 );
        codeKeysEng.put('a', 65  );
        codeKeysEng.put('s', 83  );
        codeKeysEng.put('d', 68  );
        codeKeysEng.put('f', 70  );
        codeKeysEng.put('g', 71  );
        codeKeysEng.put('h', 72  );
        codeKeysEng.put('j', 74  );
        codeKeysEng.put('k', 75  );
        codeKeysEng.put('l', 76  );
        codeKeysEng.put(';', 186 );
        codeKeysEng.put('\'', 222);
        codeKeysEng.put('z', 90  );
        codeKeysEng.put('x', 88  );
        codeKeysEng.put('c', 67  );
        codeKeysEng.put('v', 86  );
        codeKeysEng.put('b', 66  );
        codeKeysEng.put('n', 78  );
        codeKeysEng.put('m', 77  );
        codeKeysEng.put(',', 188 );
        codeKeysEng.put('.', 190 );
        codeKeysEng.put('/', 191 );
        codeKeysEng.put('~' , 192);
        codeKeysEng.put('!' , 49 );
        codeKeysEng.put('@' , 50 );
        codeKeysEng.put('#' , 51 );
        codeKeysEng.put('$' , 52 );
        codeKeysEng.put('%' , 53 );
        codeKeysEng.put('^' , 54 );
        codeKeysEng.put('&' , 55 );
        codeKeysEng.put('*' , 56 );
        codeKeysEng.put('(' , 57 );
        codeKeysEng.put(')' , 48 );
        codeKeysEng.put('_' , 189);
        codeKeysEng.put('+' , 187);
        codeKeysEng.put('{', 219 );
        codeKeysEng.put('}', 221 );
        codeKeysEng.put(':', 186 );
        codeKeysEng.put('"', 222 );
        codeKeysEng.put('<', 188 );
        codeKeysEng.put('>', 190 );
        codeKeysEng.put('?', 191 );


        translitRuEn.put('а', "a");
        translitRuEn.put('б', "b");
        translitRuEn.put('в', "v");
        translitRuEn.put('г', "g");
        translitRuEn.put('д', "d");
        translitRuEn.put('е', "e");
        translitRuEn.put('ё', "yo");
        translitRuEn.put('ж', "zh");
        translitRuEn.put('з', "z");
        translitRuEn.put('и', "i");
        translitRuEn.put('й', "i");
        translitRuEn.put('к', "k");
        translitRuEn.put('л', "l");
        translitRuEn.put('м', "m");
        translitRuEn.put('н', "n");
        translitRuEn.put('о', "o");
        translitRuEn.put('п', "p");
        translitRuEn.put('р', "r");
        translitRuEn.put('с', "s");
        translitRuEn.put('т', "t");
        translitRuEn.put('у', "u");
        translitRuEn.put('ф', "f");
        translitRuEn.put('х', "x");
        translitRuEn.put('ц', "c");
        translitRuEn.put('ч', "ch");
        translitRuEn.put('ш', "sh");
        translitRuEn.put('щ', "shh");
        translitRuEn.put('ъ', "");
        translitRuEn.put('ы', "y");
        translitRuEn.put('ь', "'");
        translitRuEn.put('э', "e");
        translitRuEn.put('ю', "yu");
        translitRuEn.put('я', "ya");
    }


}






















