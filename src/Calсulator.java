import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

public class Calсulator {

    /**
     Преобразование строки обратную польскую нотацию.
     https://habr.com/ru/post/282379/
     */
    public String revertPolishNotation(String data) throws Exception {
        StringBuilder stack = new StringBuilder();
        StringBuilder resultStack = new StringBuilder();
        char symb, temp;

        for (int i = 0; i < data.length(); i++) {
            symb = data.charAt(i);
            if (checkIfSymbolIsOperator(symb)) {
                while (stack.length() > 0) {
                    temp = stack.substring(stack.length() - 1).charAt(0);
                    if (checkIfSymbolIsOperator(temp) && (getOperationPriority(symb) <= getOperationPriority(temp))) {
                        resultStack.append(" ").append(temp).append(" ");
                        stack.setLength(stack.length() - 1);
                    } else {
                        resultStack.append(" ");
                        break;
                    }
                }
                resultStack.append(" ");
                stack.append(symb);
            } else if ('(' == symb) {
                stack.append(symb);
            } else if (')' == symb) {
                temp = stack.substring(stack.length() - 1).charAt(0);
                while ('(' != temp) {
                    if (stack.length() < 1) {
                        NewJFrame.printError("Проверьте введенное выражение");
                    }
                    resultStack.append(" ").append(temp);
                    stack.setLength(stack.length() - 1);
                    temp = stack.substring(stack.length() - 1).charAt(0);
                }
                stack.setLength(stack.length() - 1);
            } else {
                resultStack.append(symb);
            }
        }
        while (stack.length() > 0) {
            resultStack.append(" ").append(stack.substring(stack.length() - 1));
            stack.setLength(stack.length() - 1);
        }
        return resultStack.toString();
    }

    /**
     * Проверяем является ли символ оператором
     */
    private static boolean checkIfSymbolIsOperator(char symbol) {
        switch (symbol) {
            case '-':
            case '+':
            case '*':
            case '/':
            case '^':
                return true;
        }
        return false;
    }

    /**
     * Вычисление приоритета операции
     */
    private static byte getOperationPriority(char operation) {
        switch (operation) {
            case '^':
                return 3;
            case '*':
            case '/':
            case '%':
                return 2;
        }
        return 1;
    }

    /**
     * Подсчет выражения
     */
    public double calculate(String data){
        double firstArg = 0, secondArg = 0;
        String temp;
        Deque<Double> stack = new ArrayDeque<Double>();
        StringTokenizer tokenizer = new StringTokenizer(data);
        while (tokenizer.hasMoreTokens()) {
            try {
                temp = tokenizer.nextToken().trim();
                if (1 == temp.length() && checkIfSymbolIsOperator(temp.charAt(0))) {
                    if (stack.size() < 2) {
                        NewJFrame.printError("Неверное количество данных в стеке для операции " + temp);
                    }
                    secondArg = stack.pop();
                    firstArg = stack.pop();
                    switch (temp.charAt(0)) {
                        case '+':
                            firstArg += secondArg;
                            break;
                        case '-':
                            firstArg -= secondArg;
                            break;
                        case '/':
                            firstArg /= secondArg;
                            break;
                        case '*':
                            firstArg *= secondArg;
                            break;
                        case '%':
                            firstArg %= secondArg;
                            break;
                        case '^':
                            firstArg = Math.pow(firstArg, secondArg);
                            break;
                        default:
                            NewJFrame.printError("Недопустимая операция " + temp);
                    }
                    stack.push(firstArg);
                } else {
                    firstArg = Double.parseDouble(temp);
                    stack.push(firstArg);
                }
            } catch (Exception e) {
                NewJFrame.printError("Недопустимый символ в выражении или неправильно расставлены скобки");
            }
        }
        if (stack.size() > 1) {
            NewJFrame.printError("Количество операторов не соответствует количеству операндов");
        }
        return stack.pop();
    }
}
