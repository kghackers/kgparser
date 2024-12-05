`stats-etalon` — реальная сгенерированная 2024.11.11 статистика через GitHub Actions, взятая из S3 Bucket.

`stats` — статистика, сгенерированная локально на основе базы от 2024.11.11. 
В эту директорию выполняется локальная перегенерация для тестирования того, что ничего не поломалось
при рефакторинге кода статистики.
А также для тестирования обновлений в статистике, чтобы увидеть, что изменится на том же датасете.

Для контроля можно выполнять сравнение директорий `stats-etalon` и `stats`.

:exclamation: Для Excel файлов и их zip-архивов с одинаковым контентом могут быть отличия в несколько байт.
К сожалению, эти форматы не текстовые, и нормально сравнивать их диффом не получается.
Поэтому ориентируемся на условно одинаковые размеры файлов.

# Перезапуск генерации

Обычно я делаю что-то вроде такого:

Установить переменные окружения:
```bash
source ./run-statistics-process-my-windows-set-env.sh
```

Удалить старую статистику:
```bash
source ./run-statistics-clear-stats-my-windows.sh
```

Скопировать статические ресурсы в директорию генерации:
```bash
source ./run-statistics-copy-static-files.sh
```

Сгенерировать файлы статистики

Запускаю в IDEA приложение `StatisticsApplication` 
со следующими аргументами командной строки: 
```
GENERATE_STATISTICS_FROM_DATABASE
C:\java\kgparser\kgstats-generated\2024.11.11\config-output.json
C:\java\kgparser\kgstatsSrv\src\main\resources\statistics\statistics-generator-config-generate-all.json
```

и следующими переменными окружения:
```
-Dspring.profiles.active=database
-Dspring.config.location=C:\java\kgparser\kgstatsSrv\src\main\resources\application.actions.properties
```

