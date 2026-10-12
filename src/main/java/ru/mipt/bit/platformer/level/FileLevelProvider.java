
package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileLevelProvider implements LevelProvider {

    private final String fileName;

    public FileLevelProvider(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public LevelData createLevel(int width, int height) {
        InputStream stream = getClass()
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (stream == null) {
            throw new IllegalArgumentException(
                    "Файл уровня не найден: " + fileName
            );
        }

        List<GridPoint2> trees = new ArrayList<>();
        GridPoint2 playerPosition = null;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        stream,
                        StandardCharsets.UTF_8
                ))) {

            List<String> lines = reader.lines().toList();

            if (lines.size() != height) {
                throw new IllegalArgumentException(
                        "Высота уровня не совпадает с размером поля"
                );
            }

            for (int y = 0; y < lines.size(); y++) {
                String line = lines.get(y);

                if (line.length() != width) {
                    throw new IllegalArgumentException(
                            "Неверная ширина строки: " + y
                    );
                }

                for (int x = 0; x < line.length(); x++) {
                    char cell = line.charAt(x);

                    // Переводим строки файла в координаты поля:
                    // первая строка файла соответствует верхнему ряду.
                    GridPoint2 position = new GridPoint2(
                            x,
                            height - 1 - y
                    );

                    if (cell == 'T') {
                        trees.add(position);
                    } else if (cell == 'X') {
                        if (playerPosition != null) {
                            throw new IllegalArgumentException(
                                    "На уровне несколько стартовых позиций"
                            );
                        }

                        playerPosition = position;
                    } else if (cell != '_') {
                        throw new IllegalArgumentException(
                                "Неизвестный символ: " + cell
                        );
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Не удалось прочитать уровень",
                    e
            );
        }

        if (playerPosition == null) {
            throw new IllegalArgumentException(
                    "На уровне отсутствует стартовая позиция X"
            );
        }

        return new LevelData(playerPosition, trees);
    }
}
