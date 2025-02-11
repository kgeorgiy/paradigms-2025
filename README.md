# Тесты к курсу «Парадигмы программирования»

[Условия домашних заданий](https://www.kgeorgiy.info/courses/paradigms/homeworks.html)

## Домашнее задание 2. Вычисление в различных типах

Модификации
 * *Base*
    * Класс `GenericTabulator` должен реализовывать интерфейс
      [Tabulator](java/expression/generic/Tabulator.java) и
      строить трехмерную таблицу значений заданного выражения.
        * `mode` – режим вычислений:
           * `i` – вычисления в `int` с проверкой на переполнение;
           * `d` – вычисления в `double` без проверки на переполнение;
           * `bi` – вычисления в `BigInteger`.
        * `expression` – выражение, для которого надо построить таблицу;
        * `x1`, `x2` – минимальное и максимальное значения переменной `x` (включительно)
        * `y1`, `y2`, `z1`, `z2` – аналогично для `y` и `z`.
        * Результат: элемент `result[i][j][k]` должен содержать
          значение выражения для `x = x1 + i`, `y = y1 + j`, `z = z1 + k`.
          Если значение не определено (например, по причине переполнения),
          то соответствующий элемент должен быть равен `null`.
    * [Исходный код тестов](java/expression/generic/GenericTest.java)
        * Первый аргумент: `easy` или `hard`
        * Последующие аргументы: модификации
 * *Parens* (36-37)
    * Дополнительно реализуйте поддержку квадратных и фигурных скобок:
        * `([{1 + 2} * 3] + 5)` равно 14;
        * скобки должны быть парными, `(1 + 2]` — ошибка.
 * *CmpEq* (36-37)
    * Дополнительно реализуйте поддержку сравнений (`<`, `>`, `<=`, `>=`, `==`, `!=`)
        * результат: 1 при истине, 0 при лжи:
        * `2 < 2` равно 0, `2 <= 2` равно 1;
        * приоритеры как в Java.
 * *Ul* (36-37)
     * Дополнительно реализуйте поддержку режимов:
        * `u` – вычисления в `int` без проверки на переполнение;
        * `l` – вычисления в `long` без проверки на переполнение.


## Домашнее задание 1. Обработка ошибок

Модификации
 * *Base*
    * Класс `ExpressionParser` должен реализовывать интерфейс
        [TripleParser](java/expression/exceptions/TripleParser.java)
    * Классы `CheckedAdd`, `CheckedSubtract`, `CheckedMultiply`,
        `CheckedDivide` и `CheckedNegate` должны реализовывать интерфейс
        [TripleExpression](java/expression/TripleExpression.java)
    * Нельзя использовать типы `long` и `double`
    * Нельзя использовать методы классов `Math` и `StrictMath`
    * [Исходный код тестов](java/expression/exceptions/ExceptionsTest.java)
        * Первый аргумент: `easy` или `hard`
        * Последующие аргументы: модификации
 * *Parens* (36-37)
    * Дополнительно реализуйте поддержку квадратных и фигурных скобок:
        * `([{1 + 2} * 3] + 5)` равно 14;
        * скобки должны быть парными, `(1 + 2]` — ошибка.
 * *CmpEq* (36-37)
    * Дополнительно реализуйте поддержку сравнений (`<`, `>`, `<=`, `>=`, `==`, `!=`)
        * результат: 1 при истине, 0 при лжи:
        * `2 < 2` равно 0, `2 <= 2` равно 1;
        * приоритеры как в Java.
