import java.util.*;

public class ShortestPathFinder {

    // класс для хранения координат точки
    private static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }
    }

    private static List<Point> findShortestPath(char[][] map) {
        Point start = null;
        Point end = null;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 's') {
                    start = new Point(i, j);
                } else if (map[i][j] == 'f') {
                    end = new Point(i, j);
                }
            }
        }

        // Проверяем, что нашли начальную и конечную точки
        if (start == null || end == null) {
            throw new IllegalArgumentException("Не найдены начальная и/или конечная точки");
        }

        // массивы для хранения расстояний до точек и предыдущих точек
        int[][] distances = new int[map.length][map[0].length];
        Point[][] previousPoints = new Point[map.length][map[0].length];

        // инициализация массивов
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                distances[i][j] = -1; // -1 означает, что точка еще не посещена
                previousPoints[i][j] = null;
            }
        }
        distances[start.x][start.y] = 0;

        // очередь для обхода графа в ширину
        Queue<Point> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Point current = queue.remove();
            if (current.equals(end)) { // если достигли конечной точки, заканчиваем поиск
                break;
            }
            // перебираем все соседние точки
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    // пропускаем точки, которые находятся за пределами карты или являются стенами
                    if (Math.abs(dx) + Math.abs(dy) != 1 || current.x + dx < 0 || current.x + dx >= map.length ||
                            current.y + dy < 0 || current.y + dy >= map[0].length || map[current.x + dx][current.y + dy] == '#') {
                        continue;
                    }
                    Point next = new Point(current.x + dx, current.y + dy);
                    if (distances[next.x][next.y] == -1) { // если точка еще не посещена
                        distances[next.x][next.y] = distances[current.x][current.y] + 1;
                        previousPoints[next.x][next.y] = current;
                        queue.add(next);
                    }
                }
            }
        }

        // восстанавливаем путь из предыдущих точек
        List<Point> path = new ArrayList<>();
        Point current = end;
        while (current != null) {
            path.add(current);
            current = previousPoints[current.x][current.y];
        }
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        char[][] map = {
                {'#', 's', '#', '#', '#'},
                {'#', '.', '.', '.', '#'},
                {'.', '.', '#', '.', '#'},
                {'.', '#', '.', '.', '#'},
                {'#', '#', '#', '.', 'f'}
        };

        List<Point> path = findShortestPath(map);
        // выводим найденный путь
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (path.contains(new Point(i, j)) && map[i][j] != 's' && map[i][j] != 'f') {
                    System.out.print("*");
                } else {
                    System.out.print(map[i][j]);
                }
            }
            System.out.println();
        }
    }
}