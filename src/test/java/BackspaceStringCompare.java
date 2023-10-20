import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BackspaceStringCompare {
    //BEGIN_TESTS
    @Test
    public void test1() {
        Assertions.assertEquals(true, new Solution().backspaceCompare("ab#c", "ad#c"));
    }

    @Test
    public void test2() {
        Assertions.assertEquals(true, new Solution().backspaceCompare("ab##", "c#d#"));
    }

    @Test
    public void test3() {
        Assertions.assertEquals(false, new Solution().backspaceCompare("a#c", "b"));
    }
//END_TESTS

    //BEGIN_SOLUTION
    class Solution {
        public boolean backspaceCompare(String s, String t) {
            return false;
        }
    }
//END_SOLUTION
}
