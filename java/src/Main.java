/**
 * A simple expression engine
 * Author: Yancey
 * License: MIT Licence
 * Website: https://github.com/Yancey2023/ExpressionEngine
 */

import yancey.expression.ast.Expression;

import java.util.Map;

public class Main {

    private static void test(String formula, Map<String, Double> variables, double answer) {
        double calculateResult = new Expression(formula).calculate(variables);
        if (calculateResult != answer) {
            System.err.println("test failed: " + formula + " = " + calculateResult + " != " + answer);
        } else {
            System.out.println("test successful: " + formula + " == " + answer);
        }
    }

    public static void main(String[] args) {
        test("3*a+5", Map.of("a", 8.0, "b", 11.0, "c", 20.0, "d", 17.0), 29.0);
        test("1/2*(a)^2+3*a-4", Map.of("a", 8.0, "b", 11.0, "c", 20.0, "d", 17.0), 52.0);
        test("3*b+5", Map.of("a", 8.0, "b", 11.0, "c", 20.0, "d", 17.0), 38.0);
        test("1/2*(b)^2+3*b-4", Map.of("a", 8.0, "b", 11.0, "c", 20.0, "d", 17.0), 89.5);
        test("3*(b-a)", Map.of("a", 8.0, "b", 11.0, "c", 20.0, "d", 17.0), 9.0);
        test("1/2*((b)^2-(a)^2)+3(b-a)", Map.of("a", 8.0, "b", 11.0, "c", 20.0, "d", 17.0), 37.5);
        test("3", Map.of("a", 8.0, "b", 11.0, "c", 20.0, "d", 17.0), 3);
        test("c+3", Map.of("a", 8.0, "b", 11.0, "c", 20.0, "d", 17.0), 23.0);
        test("1", Map.of("a", 8.0, "b", 11.0, "c", 20.0, "d", 17.0), 1);
        test("4*a+(3/2)*(a)^2", Map.of("a", 16.0), 448.0);
        test("2*(a)^2+(1/2)*(a)^3+5", Map.of("a", 16.0), 2565.0);
        test("2*(a)^3-4.5*(a)^2-(2*(a-1)^3-4.5*(a-1)^2)", Map.of("a", 11.0), 567.5);
        test("6*(a)^2-9*a", Map.of("a", 11.0), 627.0);
        test("2*sqrt((a)^3+a+25)", Map.of("a", 20.0), 179.38784797192923);
        test("2*3.14*a", Map.of("a", 3.0), 18.84);
        test("6.28", Map.of("a", 3.0), 6.28);
        test("4*3.14*3.14*(a)^2", Map.of("a", 3.0), 354.9456);
        test("2(a)^2", Map.of("a", 1.4, "b", 1.5), 3.9199999999999995);
        test("2*a", Map.of("a", 1.4, "b", 1.5), 2.8);
        test("2*(a)^(4)", Map.of("a", 1.4, "b", 1.5), 7.6831999999999985);
        test("(2/3)*(b)^(3)", Map.of("a", 1.4, "b", 1.5), 2.25);
        test("18*a", Map.of("a", 5.0, "b", 43.0), 90.0);
        test("81*(a)^(4)", Map.of("a", 5.0, "b", 43.0), 50625.0);
        test("3*(2/(9*tan(b*3.14159/180)))", Map.of("a", 5.0, "b", 43.0), 0.7149133819484943);

        test("-a/2", Map.of("a", 4.0, "b", 4.0), -2.0);
        test("a", Map.of("a", 4.0, "b", 4.0), 4.0);
        test("(a)^2*b/4", Map.of("a", 4.0, "b", 4.0), 16.0);
        test("sqrt(20*b)*(c-a)/(a+c)", Map.of("a", 0.5, "b", 1.0, "c", 5.0), 3.659020326817838);
        test("sqrt(20*b)*2*a/(a+c)", Map.of("a", 0.5, "b", 1.0, "c", 5.0), 0.8131156281817418);
        test("sqrt(a*c/(10*(a+c)))*b", Map.of("a", 4.0, "b", 3.0, "c", 0.8), 0.7745966692414833);
        test("(a^2-b^2)/(sqrt(3)*(a^2+b^2))", Map.of("a", 16.0, "b", 8.0), 0.3464101615137755);
        test("b/a", Map.of("a", 10.0, "b", 20.0, "c", 3.0), 2.0);
        test("b/a", Map.of("a", 10.0, "b", 20.0, "c", 3.0), 2.0);
        test("sqrt((-c*a+d*b)^2)", Map.of("a", 5.0, "b", 7.0, "c", 4.0, "d", 8.0, "e", 1.0), 36.0);
        test("sqrt((-c*a+d*b)^2)", Map.of("a", 5.0, "b", 7.0, "c", 4.0, "d", 8.0, "e", 1.0), 36.0);
        test("b/c", Map.of("a", 306.0, "b", 143.0, "c", 404.0), 0.35396039603960394);
        test("(b)^(2)/(2*c)", Map.of("a", 306.0, "b", 143.0, "c", 404.0), 25.308168316831683);
        test("1000*(b^2)/(2*a*c)", Map.of("a", 306.0, "b", 143.0, "c", 404.0), 82.70643240794668);

        test("3*a*b^2/(2*(b+3*c)^2*d)", Map.of("a", 1.0, "b", 5.5, "c", 2.2, "d", 0.28), 1.1068476977567883);
        test("2*c", Map.of("a", 4.6, "b", 4.0, "c", 5.2), 10.4);
        test("4*c/b", Map.of("a", 4.6, "b", 4.0, "c", 5.2), 5.2);
        test("3*a*c^2/2", Map.of("a", 4.6, "b", 4.0, "c", 5.2), 186.576);
        test("0.5*a*b*6.28*c/(60*2*0.4*100*(0.5+0.75))", Map.of("a", 61.0, "b", 0.3, "c", 905.0), 8.667185);
        test("6.28*c*0.5*a*b/(60*8*0.4*(0.5+0.75))", Map.of("a", 61.0, "b", 0.3, "c", 905.0), 216.67962500000002);
        test("b*c/2/(0.5(a+b))", Map.of("a", 2.0, "b", 0.2, "c", 248.0), 22.545454545454543);
        test("b*(1/2)^2/(a+b)", Map.of("a", 2.0, "b", 0.2, "c", 248.0), 0.022727272727272728);
        test("2*a*sqrt(10*b*(1-sqrt(2)/2)/3)/0.2", Map.of("a", 2.0, "b", 1.0), 19.7616874722598);
        test("50*10/(20+50+a/2)", Map.of("a", 11.0, "b", 0.1), 6.622516556291391);
        test("6*c*d/((a+3*c)*b)", Map.of("a", 1.1, "b", 0.7, "c", 0.016, "d", 264.0), 31.538078646092583);
        test("a*10*0.5", Map.of("a", 1.0, "b", 0.5), 5.0);
        test("5(10-b/2)", Map.of("a", 1.0, "b", 0.5), 48.75);
        test("0.5(5(10-b/2)*0.5-a*10*0.5)/(b/2)", Map.of("a", 1.0, "b", 0.5), 38.75);
        test("0.5ab*6.28*c/(60*2*0.4*100*(0.5+0.75))", Map.of("a", 61.0, "b", 0.3, "c", 905.0), 8.667185);
        test("2 * a", Map.of("a", 2.0), 4);
    }

}