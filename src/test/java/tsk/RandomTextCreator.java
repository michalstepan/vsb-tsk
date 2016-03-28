package tsk;

import java.util.Random;

/**
 * Created by Michal on 28.03.2016.
 */
public class RandomTextCreator {

    public String randomText(int size) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
}
