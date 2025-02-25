# Тесты к курсу «Парадигмы программирования»

[Условия домашних заданий](https://www.kgeorgiy.info/courses/paradigms/homeworks.html)

## Домашнее задание 4. Очередь на массиве

Модификации
 * *Базовая*
    * Классы должны находиться в пакете `queue`
    * [Исходный код тестов](java/queue/ArrayQueueTest.java)
    * [Откомпилированные тесты](artifacts/queue/ArrayQueueTest.jar)
    * Для работы тестов необходимо добавить опцию JVM `--add-opens java.base/java.util=ALL-UNNAMED`
 * *Deque*
    * Дополнительно реализовать методы
        * `push` – добавить элемент в начало очереди;
        * `peek` – вернуть последний элемент в очереди;
        * `remove` – вернуть и удалить последний элемент из очереди.
 * *3637*
    * Реализовать модификацию *Deque*
    * Реализовать методы
        * `get` – получить элемент по индексу, отсчитываемому с хвоста;
        * `set` – заменить элемент по индексу, отсчитываемому с хвоста.
 * *3839*
    * Реализовать модификацию *Deque*
    * Реализовать методы
        * `get` – получить элемент по индексу, отсчитываемому с хвоста;
        * `set` – заменить элемент по индексу, отсчитываемому с хвоста;
        * `toStr` – вернуть строковое представление
          очереди в виде '`[`' _голова_ '`, `' ... '`, `' _хвост_ '`]`'.


## Домашнее задание 3. Бинарный поиск

Модификации
 * *Базовая*
    * Класс `BinarySearch` должен находиться в пакете `search`
    * [Исходный код тестов](java/search/BinarySearchTest.java)
    * [Откомпилированные тесты](artifacts/search/BinarySearchTest.jar)
 * *Choice* (36-39)
    * Если число чисел во входе чётное, то должна быть использована
      рекурсивная версия, иначе — итеративная.
 * *3637* (36, 37)
    * На вход подается число `x` и массив `a` полученный приписыванием
      в конец массива отсортированного (строго) по убыванию,
      массива отсортированного (строго) по возрастанию.
    * Требуется вывести индекс первого вхождения `x` в `a` или `-1`, если `x` не входит в `a`.
    * Класс должен иметь имя `BinarySearch3637`
 * *3839* (38, 39)
    * На вход подается массив полученный приписыванием
      отсортированного (строго) по убыванию массива
      в конец массива отсортированного (строго) по возрастанию
    * Требуется найти в нём минимальный индекс максимального значения.
    * Класс должен иметь имя `BinarySearch3839`
 * *3435* (34, 35)
    * На вход подается циклический сдвиг
      отсортированного (строго) по возрастанию массива.
    * Требуется найти в нём минимальное значение.
    * Класс должен иметь имя `BinarySearch3435`
 * *3233* (32, 33)
    * На вход подаётся число `x` и массив `a`, отсортированный по невозрастанию.
    * Если в массиве `a` отсутствует элемент, равный `x`, то требуется
      вывести индекс вставки в формате, определенном в
      [`Arrays.binarySearch`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html#binarySearch(int%5B%5D,int)).
    * Класс должен иметь имя `BinarySearch3233`


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
 * *Parens* (32-39)
    * Дополнительно реализуйте поддержку квадратных и фигурных скобок:
        * `([{1 + 2} * 3] + 5)` равно 14;
        * скобки должны быть парными, `(1 + 2]` — ошибка.
 * *CmpEq* (36-39)
    * Дополнительно реализуйте поддержку сравнений (`<`, `>`, `<=`, `>=`, `==`, `!=`)
        * результат: 1 при истине, 0 при лжи:
        * `2 < 2` равно 0, `2 <= 2` равно 1;
        * приоритеты как в Java.
 * *Ul* (36-37)
     * Дополнительно реализуйте поддержку режимов:
        * `u` – вычисления в `int` без проверки на переполнение;
        * `l` – вычисления в `long` без проверки на переполнение.
 * *If* (38, 39)
    * Дополнительно реализуйте поддержку режимов:
        * `u` – вычисления в `int` без проверки на переполнение;
        * `ifix` – вычисления с фиксированной точкой (сдвиг 16 бит) в `int`
          без проверки на переполнение.
 * *MinMax* (38, 39)
    * Дополнительно реализуйте бинарные операции (приоритет между сложением и сравнениями):
        * `<?` – минимум, `2 <? 3` равно 2;
        * `>?` – максимум, `2 >? 3` равно 3.
 * *Geom* (32-35)
    * Дополнительно реализуйте бинарные операции (минимальный приоритет):
        * `area` – площадь прямоугольного треугольника по двум катетам, `5 area 3` равно 7;
        * `perimeter` – периметр прямоугольника по двум сторонам, `4 perimeter 3` равно 14.

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
 * *Parens* (32-39)
    * Дополнительно реализуйте поддержку квадратных и фигурных скобок:
        * `([{1 + 2} * 3] + 5)` равно 14;
        * скобки должны быть парными, `(1 + 2]` — ошибка.
 * *CmpEq* (36-39)
    * Дополнительно реализуйте поддержку сравнений (`<`, `>`, `<=`, `>=`, `==`, `!=`)
        * результат: 1 при истине, 0 при лжи:
        * `2 < 2` равно 0, `2 <= 2` равно 1;
        * приоритеты как в Java.
 * *MinMax* (38, 39)
    * Дополнительно реализуйте бинарные операции (приоритет между сложением и сравнениями):
        * `<?` – минимум, `2 <? 3` равно 2;
        * `>?` – максимум, `2 >? 3` равно 3.
 * *Geom* (32-35)
    * Дополнительно реализуйте бинарные операции (минимальный приоритет):
        * `area` – площадь прямоугольного треугольника по двум катетам, `5 area 3` равно 7;
        * `perimeter` – периметр прямоугольника по двум сторонам, `4 perimeter 3` равно 14.
 * *Sqrt* (34, 35)
    * Дополнительно реализуйте унарную операцию
        * `sqrt` – квадратный корень, `sqrt 24` равно 4.