import java.util.Stack;
import java.util.HashMap;
import java.util.Map;

public class InfixToSuffix {


    private static final Map<Character, Integer> PRECEDENCE = new HashMap<>();

    static {
        PRECEDENCE.put('+', 1);
        PRECEDENCE.put('-', 1);
        PRECEDENCE.put('*', 2);
        PRECEDENCE.put('/', 2);
        PRECEDENCE.put('^', 3);
    } //运算符优先级

    public static String convert(String infix) {
        if (infix == null || infix.isEmpty()) {
            return "";
        }

        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < infix.length(); i++) {
            char ch = infix.charAt(i);

            // 如果是操作数（数字或字母），直接添加到输出
            if (Character.isLetterOrDigit(ch)) {
                postfix.append(ch);
            }
            // 如果是左括号，压入栈
            else if (ch == '(') {
                stack.push(ch);
            }
            // 如果是右括号，弹出栈中元素直到遇到左括号
            else if (ch == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                stack.pop(); // 弹出左括号
            }
            // 如果是运算符
            else if (isOperator(ch)) {
                // 弹出优先级大于或等于当前运算符的所有运算符
                while (!stack.isEmpty() && stack.peek() != '(' &&
                        precedence(stack.peek()) >= precedence(ch)) {
                    postfix.append(stack.pop());
                }
                stack.push(ch);
            }
            // 忽略空格

        }

        // 弹出栈中所有剩余运算符
        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }

        return postfix.toString();
    }//转

    private static boolean isOperator(char ch) {
        return PRECEDENCE.containsKey(ch);
    }//判断字符是否为运算符

    private static int precedence(char operator) {
        return PRECEDENCE.getOrDefault(operator, 0);
    }//优先级

    public static void main(String[] args) {
        String[] testCases = {
                "a+b",
                "a+b*c",
                "a+b*(c^d-e)^(f+g*h)-i",
        };
        System.out.println("中缀表达式转后缀表达式测试");

        for (String infix : testCases) {
            String postfix = convert(infix);
            System.out.println("中缀: " + infix);
            System.out.println("后缀: " + postfix);

        }
    }
}