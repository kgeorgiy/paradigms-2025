package expression.generic;

import base.Selector;
import expression.parser.FullOperations;

import java.math.BigInteger;
import java.util.function.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class GenericTest {
    private static final Consumer<GenericTester> ADD = binary("+", 200);
    private static final Consumer<GenericTester> SUBTRACT = binary("-", -200);
    private static final Consumer<GenericTester> MULTIPLY = binary("*", 301);
    private static final Consumer<GenericTester> DIVIDE = binary("/", -300);
    private static final Consumer<GenericTester> NEGATE = unary("-");

    // === Checked integers
    private static Integer i(final long v) {
        if (v != (int) v) {
            throw new ArithmeticException("Overflow");
        }
        return (int) v;
    }

    private static final GenericTester.Mode.Builder<Integer> INTEGER_CHECKED = mode("i", c -> c)
            .binary("+", (a, b) -> i(a + (long) b))
            .binary("-", (a, b) -> i(a - (long) b))
            .binary("*", (a, b) -> i(a * (long) b))
            .binary("/", (a, b) -> i(a / (long) b))
            .unary("-", a -> i(- (long) a))

            .binary("<", (a, b) -> a < b ? 1 : 0)
            .binary(">", (a, b) -> a > b ? 1 : 0)
            .binary("<=", (a, b) -> a <= b ? 1 : 0)
            .binary(">=", (a, b) -> a >= b ? 1 : 0)

            .binary("==", (a, b) -> a.equals(b) ? 1 : 0)
            .binary("!=", (a, b) -> !a.equals(b) ? 1 : 0)

            ;

    // === Doubles

    private static BinaryOperator<Double> d(final IntPredicate p) {
        return (a, b) -> p.test(a.compareTo(b)) ? 1.0 : 0.0;
    }

    private static final GenericTester.Mode.Builder<Double> DOUBLE = mode("d", c -> (double) c)
            .binary("+", Double::sum)
            .binary("-", (a, b) -> a - b)
            .binary("*", (a, b) -> a * b)
            .binary("/", (a, b) -> a / b)
            .unary("-", a -> -a)

            .binary("<", d(v -> v < 0))
            .binary(">", d(v -> v > 0))
            .binary("<=", d(v -> v <= 0))
            .binary(">=", d(v -> v >= 0))
            .binary("==", d(v -> v == 0))
            .binary("!=", d(v -> v != 0))

            ;

    // === BigIntegers

    private static BinaryOperator<BigInteger> bi(final IntPredicate p) {
        return (a, b) -> p.test(a.compareTo(b)) ? BigInteger.ONE : BigInteger.ZERO;
    }

    private static final GenericTester.Mode.Builder<BigInteger> BIG_INTEGER = mode("bi", BigInteger::valueOf)
            .binary("+", BigInteger::add)
            .binary("-", BigInteger::subtract)
            .binary("*", BigInteger::multiply)
            .binary("/", BigInteger::divide)
            .unary("-", BigInteger::negate)

            .binary("<", bi(v -> v < 0))
            .binary(">", bi(v -> v > 0))
            .binary("<=", bi(v -> v <= 0))
            .binary(">=", bi(v -> v >= 0))
            .binary("==", bi(v -> v == 0))
            .binary("!=", bi(v -> v != 0))

            ;


    // === Parens
    private static final Consumer<GenericTester> PARENS = tester -> tester.parens("{", "}", "[", "]");


    // === Compare

    private static final Consumer<GenericTester> LESS = binary("<", 30);
    private static final Consumer<GenericTester> GREATER = binary(">", 30);
    private static final Consumer<GenericTester> LESS_EQ = binary("<=", 30);
    private static final Consumer<GenericTester> GREATER_EQ = binary(">=", 30);
    private static final Consumer<GenericTester> EQUAL = binary("==", 20);
    private static final Consumer<GenericTester> NOT_EQUAL = binary("!=", 20);


    // === Unchecked integers

    @SuppressWarnings("Convert2MethodRef")
    private static final GenericTester.Mode.Builder<Integer> INTEGER_UNCHECKED = mode("u", c -> c)
            .binary("+", (a, b) -> a + b)
            .binary("-", (a, b) -> a - b)
            .binary("*", (a, b) -> a * b)
            .binary("/", (a, b) -> a / b)
            .unary("-", a -> -a)

            .binary("<", (a, b) -> a < b ? 1 : 0)
            .binary(">", (a, b) -> a > b ? 1 : 0)
            .binary("<=", (a, b) -> a <= b ? 1 : 0)
            .binary(">=", (a, b) -> a >= b ? 1 : 0)
            .binary("==", (a, b) -> a.equals(b) ? 1 : 0)
            .binary("!=", (a, b) -> !a.equals(b) ? 1 : 0)

            ;


    // === Longs

    private static final GenericTester.Mode.Builder<Long> LONG = mode("l", c -> (long) c)
            .binary("+", Long::sum)
            .binary("-", (a, b) -> a - b)
            .binary("*", (a, b) -> a * b)
            .binary("/", (a, b) -> a / b)
            .unary("-", a -> -a)

            .binary("<", (a, b) -> a < b ? 1L : 0)
            .binary(">", (a, b) -> a > b ? 1L : 0)
            .binary("<=", (a, b) -> a <= b ? 1L : 0)
            .binary(">=", (a, b) -> a >= b ? 1L : 0)

            .binary("==", (a, b) -> a.equals(b) ? 1L : 0)
            .binary("!=", (a, b) -> !a.equals(b) ? 1L : 0)

            ;


    // === Fixed-point integers

    /* package-private */ static final int FIXED = 16;
    @SuppressWarnings("Convert2MethodRef")
    private static final GenericTester.Mode.Builder<Integer> INTEGER_FIXED = mode("ifix", a -> a << FIXED)
            .binary("+", (a, b) -> a + b)
            .binary("-", (a, b) -> a - b)
            .binary("*", (a, b) -> (a * b) >> FIXED)
            .binary("/", (a, b) -> (a / b) << FIXED)
            .unary("-", a -> -a)

            .binary("<", (a, b) -> a < b ? 1 << FIXED : 0)
            .binary(">", (a, b) -> a > b ? 1 << FIXED : 0)
            .binary("<=", (a, b) -> a <= b ? 1 << FIXED : 0)
            .binary(">=", (a, b) -> a >= b ? 1 << FIXED : 0)
            .binary("==", (a, b) -> a.equals(b) ? 1 << FIXED : 0)
            .binary("!=", (a, b) -> !a.equals(b) ? 1 << FIXED : 0)

            ;


    // === Common

    private GenericTest() {
    }

    /* package-private */ static Consumer<GenericTester> unary(final String name) {
        return tester -> tester.unary(name, 1);
    }

    /* package-private */ static Consumer<GenericTester> binary(final String name, final int priority) {
        return tester -> tester.binary(name, priority);
    }

    public static final Selector SELECTOR = Selector.composite(GenericTest.class, GenericTester::new, "easy", "hard")
            .variant("Base", INTEGER_CHECKED, DOUBLE, BIG_INTEGER, ADD, SUBTRACT, MULTIPLY, DIVIDE, NEGATE)
            .variant("3637", PARENS, LESS, LESS_EQ, GREATER, GREATER_EQ, EQUAL, NOT_EQUAL, INTEGER_UNCHECKED, LONG)
            .selector();

    private static <T> GenericTester.Mode.Builder<T> mode(final String mode, final IntFunction<T> constant) {
        return GenericTester.Mode.builder(mode, constant, IntUnaryOperator.identity());
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
