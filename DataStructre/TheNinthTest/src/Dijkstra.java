import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Dijkstra {

    public static void main(String[] args) {
        // 创建Dijkstra对象
        Dijkstra dijkstra = new Dijkstra();
        // 生成10*10的随机邻接矩阵，权重范围1-10
        int[][] map = dijkstra.GetRandomMap(10, 10);
        // 打印原始邻接矩阵
        System.out.println("原始10*10邻接矩阵（-1表示不可达，0表示自身）：");
        for (int i = 0; i < map.length; i++) {
            System.out.printf("%2d: %s\n", i, Arrays.toString(map[i]));
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("\n请输入起始节点(0-9): ");
        int start = sc.nextInt();

        // 计算从起始点到所有其他节点的最短路径
        int[][] result = dijkstra.GetMinWay(map, start);

        // 打印最短路径结果
        System.out.println("\n从起始点 " + start + " 出发的最短路径结果：");
        System.out.println("节点\t最短距离\t路径");

        int[] dist = result[0]; // 最短距离数组
        int[] prev = result[1]; // 前驱节点数组
        // 遍历所有节点，打印最短路径信息
        for (int i = 0; i < dist.length; i++) {
            if (dist[i] == Integer.MAX_VALUE) {
                System.out.printf("%2d\t%-10s\t%s\n", i, "不可达", "无路径");
            } else {
                System.out.printf("%2d\t%-10d\t%s\n", i, dist[i], dijkstra.getPath(prev, start, i));
            }
        }
    }

    //获取一个邻接矩阵，随机数范围自己输入,随机赋值-1，表示不可达,默认10*10的矩阵
    public int[][] GetRandomMap(int a, int b) {
        Random random = new Random();
        double insertNeg1Probability = 0.01; // 设置-1出现的概率为1%
        int[][] map = new int[10][10]; // 创建10*10的邻接矩阵

        // 遍历矩阵的每个元素
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                int c = random.nextInt(a) + 1; // 生成上三角区域的随机权重
                int d = random.nextInt(b) + 1; // 生成下三角区域的随机权重

                if (i == j) {
                    map[i][j] = 0; // 对角线元素设为0，表示自身到自身的距离
                } else {
                    if (i < j) {
                        map[i][j] = c; // 上三角区域使用参数a生成的随机数
                    } else {
                        map[i][j] = d; // 下三角区域使用参数b生成的随机数
                    }
                    // 随机将某些边的权重设为-1，表示不可达
                    if (random.nextDouble() < insertNeg1Probability) {
                        map[i][j] = -1;
                    }
                }
            }
        }
        return map; // 返回生成的邻接矩阵
    }

    //获取最小路径，使用Dijkstra算法计算从起始点到所有其他节点的最短路径
    public int[][] GetMinWay(int[][] map, int start) {
        int n = map.length; // 获取节点数量

        // 检查起始点是否有效
        if (start < 0 || start >= n) {
            System.out.println("起始点不存在（有效范围：0~" + (n - 1) + "）");
            return new int[2][n];
        }

        int[] dist = new int[n]; // 存储从起始点到每个节点的最短距离
        int[] prev = new int[n]; // 存储最短路径中每个节点的前驱节点
        boolean[] visited = new boolean[n]; // 标记节点是否已被访问

        // 初始化距离、前驱和访问标记数组
        for (int i = 0; i < n; i++) {
            if (map[start][i] == -1) {
                dist[i] = Integer.MAX_VALUE; // 不可达节点距离设为最大值
            } else {
                dist[i] = map[start][i]; // 可达节点距离设为边的权重
            }
            prev[i] = -1; // 初始时没有前驱节点
            visited[i] = false; // 初始时所有节点都未访问
        }
        dist[start] = 0; // 起始点到自身的距离为0
        visited[start] = true; // 标记起始点已访问

        // 循环n-1次，每次找到一个最短路径节点
        for (int i = 1; i < n; i++) {
            int min = Integer.MAX_VALUE;
            int u = -1;
            // 在未访问节点中找到距离最小的节点
            for (int j = 0; j < n; j++) {
                if (!visited[j] && dist[j] < min) {
                    min = dist[j];
                    u = j;
                }
            }
            if (u == -1) break; // 如果没有可达节点，提前结束
            visited[u] = true; // 标记该节点已访问

            // 更新u节点的所有邻居节点的距离
            for (int v = 0; v < n; v++) {
                if (!visited[v] && map[u][v] != -1 && dist[u] != Integer.MAX_VALUE) {
                    int newDist = dist[u] + map[u][v]; // 计算经过u到v的新距离
                    if (newDist < dist[v]) {
                        dist[v] = newDist; // 更新最短距离
                        prev[v] = u; // 更新前驱节点
                    }
                }
            }
        }

        // 返回结果，包含距离数组和前驱数组
        int[][] result = new int[2][n];
        result[0] = dist;
        result[1] = prev;
        return result;
    }

    //根据前驱节点数组构建从起始点到目标节点的完整路径字符串
    public String getPath(int[] prev, int start, int end) {
        // 如果起始点和目标点相同，直接返回
        if (start == end) {
            return String.valueOf(start);
        }
        // 如果目标节点没有前驱，表示不可达
        if (prev[end] == -1) {
            return "无路径";
        }

        StringBuilder path = new StringBuilder();
        int current = end;
        java.util.ArrayList<Integer> pathList = new java.util.ArrayList<>();

        // 从目标节点反向追溯路径到起始节点
        while (current != -1) {
            pathList.add(current);
            current = prev[current];
        }

        // 反转路径，使其从起始点到目标点
        for (int i = pathList.size() - 1; i >= 0; i--) {
            path.append(pathList.get(i));
            if (i > 0) {
                path.append(" → "); // 添加箭头连接节点
            }
        }
        return path.toString(); // 返回路径字符串
    }
}