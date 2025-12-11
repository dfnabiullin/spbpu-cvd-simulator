Консольная утилита для оптимизации размера графических файлов в рамках выпускной квалификационной работы бакалавра в Санкт-Петербургском политехническом университете Петра Великого.

Программа удаляет цветовую информацию, неразличимую для
людей с нарушениями цветовосприятия (дихромазией). В результате обработки происходит проекция цветов на плоскость неразличимости, что снижает энтропию изображения и
позволяет уменьшить объем занимаемых данных без визуальной потери качества для целевой аудитории.

- **Язык:** Java 8.
- **Форматы изображений:** JPG, PNG, BMP.

---

1. [Архитектура](#1-архитектура)
2. [Запуск проекта](#2-запуск-проекта)
3. [Документация](#3-документация)

---

### 1. Архитектура

В основе работы лежат алгоритмы моделирования дихроматического зрения (Vienot, 1999) и преобразования цветовых
пространств (Smith & Pokorny, 1975).

#### Конвейер обработки данных

```mermaid
flowchart
    Input([Исходное изображение sRGB])
    Linear[Linear RGB]
    LMS_T[LMS Trichromat]
    LMS_D[LMS Dichromat]
    Linear_D[Linear RGB Dichromat]
    Output([Изображение sRGB])
    Input -->|Дегаммирование| Linear
    Linear -->|Матричное преобразование| LMS_T
    LMS_T -->|Проекция на плоскость| LMS_D
    LMS_D -->|Обратное преобразование| Linear_D
    Linear_D -->|Гамма - коррекция| Output
```

#### Диаграмма классов

```mermaid
classDiagram
    class Main {
    }
    namespace simulate {
        class Simulator {
            <<abstract>>
        }
        class DichromacySimulator {
            <<abstract>>
        }
        class SimulatorVienot1999
    }
    namespace convert {
        class LMSModel {
            <<abstract>>
        }
        class LMSModelSRGBSmithPokorny75
        class ColorSpaceConverter
    }
    namespace util {
        class MatrixUtils
        class ImageUtils
    }
    namespace enums {
        class Deficiency {
            <<enumeration>>
        }
    }
    Main --> SimulatorVienot1999: creates & uses
    Main ..> ImageUtils: uses
    Simulator <|-- DichromacySimulator
    DichromacySimulator <|-- SimulatorVienot1999
    Simulator ..> ColorSpaceConverter: uses
    SimulatorVienot1999 --> LMSModel: has a
    SimulatorVienot1999 ..> Deficiency: uses
    SimulatorVienot1999 ..> MatrixUtils: uses
    LMSModel <|-- LMSModelSRGBSmithPokorny75
    LMSModel ..> MatrixUtils: uses
```

---

### 2. Запуск проекта

1. Клонирование репозитория:
```bash
git clone https://github.com/dfnabiullin/spbpu-cvd-simulator
cd spbpu-cvd-simulator
```

2. Компиляция:
```bash
mkdir out
javac -d out -sourcepath src src/Main.java
```

3. Запуск:
```bash
java -cp out Main <input_dir> <output_dir> [-d deficiency_type]
```

**Аргументы:**

- `<input_dir>` директория с исходными изображениями.
- `<output_dir>` директория для сохранения результатов.
- `-d` (опционально) тип симуляции: `protan`, `deutan` или `tritan`. По умолчанию используется `protan`.

---

### 3. Документация

Материалы выпускной квалификационной работы находятся в директории `docs/`:

- [Текст ВКР (PDF)](docs/ВКР_НабиуллинДФ.pdf)
- [Презентация к защите (PDF)](docs/Презентация_НабиуллинДФ.pdf)
- [Отзыв руководителя (PDF)](docs/Набиуллин%20ДФ%20отзыв.pdf)
- [Справка о проверке на плагиат (PDF)](docs/Certificate_1299_20250617_Набиуллин_Д_Ф.pdf)

---

### 4. Примеры

| Тип симуляции | Исходное фото               | Результат                             |
|---------------|-----------------------------|---------------------------------------|
| `-d protan`   | ![](docs/images/protan.jpg) | ![](docs/images/protan_simulated.jpg) |
| `-d tritan`   | ![](docs/images/tritan.jpg) | ![](docs/images/tritan_simulated.jpg) |
| `-d deutan`   | ![](docs/images/deutan.jpg) | ![](docs/images/deutan_simulated.jpg) |
