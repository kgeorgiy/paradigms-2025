package expression;

import base.*;
import expression.common.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static base.Asserts.assertTrue;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ExpressionTester<E extends ToMiniString, C> extends Tester {
    private final List<Integer> VALUES = IntStream.rangeClosed(-10, 10).boxed().toList();
    private final ExpressionKind<E, C> kind;

    private final List<Test> basic = new ArrayList<>();
    private final List<Test> advanced = new ArrayList<>();
    private final Set<String> used = new HashSet<>();
    private final GeneratorBuilder generator;

    private final List<Pair<ToMiniString, String>> prev = new ArrayList<>();
    private final Map<String, C> mappings;

    protected ExpressionTester(
            final TestCounter counter,
            final ExpressionKind<E, C> kind,
            final Function<C, E> expectedConstant,
            final Binary<C, E> binary,
            final BinaryOperator<C> add,
            final BinaryOperator<C> sub,
            final BinaryOperator<C> mul,
            final BinaryOperator<C> div,
            final Map<String, C> mappings
    ) {
        super(counter);
        this.kind = kind;
        this.mappings = mappings;

        generator = new GeneratorBuilder(expectedConstant, kind::constant, binary, kind::randomValue);
        generator.binary("+",  1600, add, Add.class);
        generator.binary("-", 1602, sub, Subtract.class);
        generator.binary("*",  2001, mul, Multiply.class);
        generator.binary("/", 2002, div, Divide.class);
    }

    protected ExpressionTester(
            final TestCounter counter,
            final ExpressionKind<E, C> kind,
            final Function<C, E> expectedConstant,
            final Binary<C, E> binary,
            final BinaryOperator<C> add,
            final BinaryOperator<C> sub,
            final BinaryOperator<C> mul,
            final BinaryOperator<C> div
    ) {
        this(counter, kind, expectedConstant, binary, add, sub, mul, div, Map.of());
    }

    @Override
    public String toString() {
        return kind.getName();
    }

    @Override
    public void test() {
        counter.scope("Basic tests", () -> basic.forEach(Test::test));
        counter.scope("Advanced tests", () -> advanced.forEach(Test::test));
        counter.scope("Random tests", generator::testRandom);
    }

    @SuppressWarnings({"ConstantValue", "EqualsWithItself"})
    private void checkEqualsAndToString(final String full, final String mini, final ToMiniString expression, final ToMiniString copy) {
        checkToString("toString", full, expression.toString());
        if (mode() > 0) {
            checkToString("toMiniString", mini, expression.toMiniString());
        }

        counter.test(() -> {
            assertTrue("Equals to this", expression.equals(expression));
            assertTrue("Equals to copy", expression.equals(copy));
            assertTrue("Equals to null", !expression.equals(null));
            assertTrue("Copy equals to null", !copy.equals(null));
        });

        final String expressionToString = Objects.requireNonNull(expression.toString());
        for (final Pair<ToMiniString, String> pair : prev) {
            counter.test(() -> {
                final ToMiniString prev = pair.first();
                final String prevToString = pair.second();
                final boolean equals = prevToString.equals(expressionToString);
                assertTrue("Equals to " + prevToString, prev.equals(expression) == equals);
                assertTrue("Equals to " + prevToString, expression.equals(prev) == equals);
                assertTrue("Inconsistent hashCode for " + prev + " and " + expression, (prev.hashCode() == expression.hashCode()) == equals);
            });
        }
    }

    private void checkToString(final String method, final String expected, final String actual) {
        counter.test(() -> assertTrue(String.format("Invalid %s\n     expected: %s\n       actual: %s", method, expected, actual), expected.equals(actual)));
    }

    private void check(
            final String full,
            final E expected,
            final E actual,
            final List<String> variables,
            final List<C> values
    ) {
        final String vars = IntStream.range(0, variables.size())
                .mapToObj(i -> variables.get(i) + "=" + values.get(i))
                .collect(Collectors.joining(","));
        counter.test(() -> {
            final Object expectedResult = evaluate(expected, variables, values);
            final Object actualResult = evaluate(actual, variables, values);
            final String reason = String.format(
                    "%s:%n     expected `%s`,%n       actual `%s`",
                    String.format("f(%s)\nwhere f is %s", vars, full),
                    Asserts.toString(expectedResult),
                    Asserts.toString(actualResult)
            );
            if (
                    expectedResult != null && actualResult != null &&
                    expectedResult.getClass() == actualResult.getClass()
                            && (expectedResult.getClass() == Double.class || expectedResult.getClass() == Float.class)
            ) {
                final double expectedValue = ((Number) expectedResult).doubleValue();
                final double actualValue = ((Number) actualResult).doubleValue();
                Asserts.assertEquals(reason, expectedValue, actualValue, 1e-6);
            } else {
                assertTrue(reason, Objects.deepEquals(expectedResult, actualResult));
            }
        });
    }

    private Object evaluate(final E expression, final List<String> variables, final List<C> values) {
        try {
            return kind.evaluate(expression, variables, values);
        } catch (final Exception e) {
            return e.getClass().getName();
        }
    }

    protected ExpressionTester<E, C> basic(final String full, final String mini, final E expected, final E actual) {
        return basicF(full, mini, expected, vars -> actual);
    }

    protected ExpressionTester<E, C> basicF(final String full, final String mini, final E expected, final Function<List<String>, E> actual) {
        return basic(new Test(full, mini, expected, actual));
    }

    private ExpressionTester<E, C> basic(final Test test) {
        Asserts.assertTrue(test.full, used.add(test.full));
        basic.add(test);
        return this;
    }

    protected ExpressionTester<E, C> advanced(final String full, final String mini, final E expected, final E actual) {
        return advancedF(full, mini, expected, vars -> actual);
    }

    protected ExpressionTester<E, C> advancedF(final String full, final String mini, final E expected, final Function<List<String>, E> actual) {
        Asserts.assertTrue(full, used.add(full));
        advanced.add(new Test(full, mini, expected, actual));
        return this;
    }

    protected static <E> Named<E> variable(final String name, final E expected) {
        return Named.of(name, expected);
    }

    @FunctionalInterface
    public interface Binary<C, E> {
        E apply(BinaryOperator<C> op, E a, E b);
    }

    private final class Test {
        private final String full;
        private final String mini;
        private final E expected;
        private final Function<List<String>, E> actual;

        private Test(final String full, final String mini, final E expected, final Function<List<String>, E> actual) {
            this.full = full;
            this.mini = mini;
            this.expected = expected;
            this.actual = actual;
        }

        private void test() {
            final List<Pair<String, E>> variables = kind.variables().generate(random(), 3);
            final List<String> names = Functional.map(variables, Pair::first);
            final E actual = kind.cast(this.actual.apply(names));
            final String full = mangle(this.full, names);
            final String mini = mangle(this.mini, names);

            counter.test(() -> {
                kind.allValues(variables.size(), VALUES).forEach(values -> check(mini, expected, actual, names, values));
                checkEqualsAndToString(full, mini, actual, actual);
                prev.add(Pair.of(actual, full));
            });
        }

        private String mangle(String string, final List<String> names) {
            for (int i = 0; i < names.size(); i++) {
                string = string.replace("$" + (char) ('x' + i), names.get(i));
            }
            for (final Map.Entry<String, C> mapping : mappings.entrySet()) {
                string = string.replace(mapping.getKey(), mapping.getValue().toString());
            }
            return string;
        }
    }

    private final class GeneratorBuilder {
        private final Generator.Builder<C> generator;
        private final NodeRendererBuilder<C> renderer = new NodeRendererBuilder<>(random());
        private final Renderer.Builder<C, Unit, E> expected;
        private final Renderer.Builder<C, Unit, E> actual;
        private final Renderer.Builder<C, Unit, E> copy;
        private final Binary<C, E> binary;

        private GeneratorBuilder(
                final Function<C, E> expectedConstant,
                final Function<? super C, E> actualConstant,
                final Binary<C, E> binary,
                final Function<ExtendedRandom, C> randomValue
        ) {
            generator = Generator.builder(() -> randomValue.apply(random()), random());
            expected = Renderer.builder(expectedConstant::apply);
            actual = Renderer.builder(actualConstant::apply);
            copy = Renderer.builder(actualConstant::apply);

            this.binary = binary;
        }

        private void binary(final String name, final int priority, final BinaryOperator<C> op, final Class<?> type) {
            generator.add(name, 2);
            renderer.binary(name, priority);

            expected.binary(name, (unit, a, b) -> binary.apply(op, a, b));

            @SuppressWarnings("unchecked") final Constructor<? extends E> constructor = (Constructor<? extends E>) Arrays.stream(type.getConstructors())
                    .filter(cons -> Modifier.isPublic(cons.getModifiers()))
                    .filter(cons -> cons.getParameterCount() == 2)
                    .findFirst()
                    .orElseGet(() -> counter.fail("%s(..., ...) constructor not found", type.getSimpleName()));
            final Renderer.BinaryOperator<Unit, E> actual = (unit, a, b) -> {
                try {
                    return constructor.newInstance(a, b);
                } catch (final Exception e) {
                    return counter.fail(e);
                }
            };
            this.actual.binary(name, actual);
            copy.binary(name, actual);
        }

        private void testRandom() {
            final NodeRenderer<C> renderer = this.renderer.build();
            final Renderer<C, Unit, E> expectedRenderer = this.expected.build();
            final Renderer<C, Unit, E> actualRenderer = this.actual.build();
            final expression.common.Generator<C, E> generator = this.generator.build(kind.variables(), List.of());
            generator.testRandom(counter, 1, expr -> {
                final String full = renderer.render(expr, NodeRenderer.FULL);
                final String mini = renderer.render(expr, NodeRenderer.MINI);
                final E expected = expectedRenderer.render(expr, Unit.INSTANCE);
                final E actual = actualRenderer.render(expr, Unit.INSTANCE);

                final List<Pair<String, E>> variables = expr.variables();
                final List<String> names = Functional.map(variables, Pair::first);
                final List<C> values = Stream.generate(() -> kind.randomValue(random()))
                        .limit(variables.size())
                        .toList();

                checkEqualsAndToString(full, mini, actual, copy.build().render(expr, Unit.INSTANCE));
                check(full, expected, actual, names, values);
            });
        }
    }
}
