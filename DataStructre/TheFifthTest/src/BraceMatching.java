import java.util.Stack;

public class BraceMatching {

    public static boolean isParenthesisMatched(String input) {
        if (input == null || input.isEmpty()) {
            return true;
        }
        Stack<Character> stack = new Stack<>();

        for (char ch : input.toCharArray()) {
            if (ch == '(') {
                // 遇到左括号，压入栈
                stack.push(ch);
            } else if (ch == ')') {
                // 遇到右括号，检查栈是否为空
                if (stack.isEmpty()) {
                    return false; // 右括号多余
                }
                // 弹出匹配的左括号
                stack.pop();
            }
            // 其他字符忽略
        }
        // 如果栈为空，说明所有括号都匹配
        return stack.isEmpty();
    }//匹配

    public static void main(String[] args) {
        String testCase = "(a + b * (c - d))";

        System.out.println("测试字符串: \"" + testCase + "\"");
        System.out.println("括号匹配结果: " +
                (isParenthesisMatched(testCase) ? "匹配成功" : "匹配失败"));

    }
}
