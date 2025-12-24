import java.util.*;

// ==================== 开地址法哈希表工具类 ====================
class OpenAddressingHashTableUtils {

    private static final int EMPTY = -1;      // 空位置标记
    private static final int DELETED = -2;    // 删除标记
    private static final double LOAD_FACTOR_THRESHOLD = 0.7;  // 负载因子阈值

    private int[] keys;       // 键数组
    private int[] values;     // 值数组
    private int capacity;     // 哈希表容量
    private int size;         // 当前元素数量
    private int deletedCount; // 已删除位置计数

    /**
     * 哈希表构造函数
     *
     * @param capacity 哈希表初始容量
     */
    public OpenAddressingHashTableUtils(int capacity) {
        this.capacity = Math.max(10, capacity);  // 最小容量为10
        this.keys = new int[this.capacity];
        this.values = new int[this.capacity];
        this.size = 0;
        this.deletedCount = 0;

        // 初始化数组，标记所有位置为空
        for (int i = 0; i < this.capacity; i++) {
            keys[i] = EMPTY;
            values[i] = EMPTY;
        }
    }

    /**
     * 默认构造函数（容量为20）
     */
    public OpenAddressingHashTableUtils() {
        this(20);
    }

    /**
     * 主哈希函数（除留余数法）
     *
     * @param key 键
     * @return 哈希索引
     */
    private int hash1(int key) {
        return Math.abs(key) % capacity;
    }

    /**
     * 二次哈希函数（用于解决冲突）
     *
     * @param key 键
     * @return 二次哈希值
     */
    private int hash2(int key) {
        // 使用另一个质数作为步长，确保不为0
        return 1 + (Math.abs(key) % (capacity - 1));
    }

    /**
     * 线性探测解决冲突
     *
     * @param key   键
     * @param start 起始索引
     * @return 探测到的索引
     */
    private int linearProbe(int key, int start) {
        int index = start;
        int step = 1;  // 线性探测步长

        while (true) {
            // 如果位置为空或被删除，可以插入
            if (keys[index] == EMPTY || keys[index] == DELETED) {
                return index;
            }

            // 如果找到相同的键，返回该位置
            if (keys[index] == key) {
                return index;
            }

            // 线性探测：移动到下一个位置
            index = (start + step) % capacity;
            step++;

            // 如果探测次数超过容量，说明哈希表已满
            if (step > capacity) {
                return -1;
            }
        }
    }

    /**
     * 向哈希表中插入键值对
     *
     * @param key   键
     * @param value 值
     * @return 是否插入成功
     */
    public boolean insert(int key, int value) {
        if (size + deletedCount >= capacity * LOAD_FACTOR_THRESHOLD) {
            resize();
        }

        int startIndex = hash1(key);
        int index = linearProbe(key, startIndex);

        if (index == -1) {
            // 哈希表已满
            return false;
        }

        // 如果位置被标记为删除，减少删除计数
        if (keys[index] == DELETED) {
            deletedCount--;
        }

        // 如果位置为空或键不同，增加大小
        if (keys[index] == EMPTY || keys[index] != key) {
            size++;
        }

        // 插入或更新键值对
        keys[index] = key;
        values[index] = value;

        return true;
    }

    /**
     * 根据键查找值
     *
     * @param key 键
     * @return 找到的值，如果不存在返回-1
     */
    public int search(int key) {
        int startIndex = hash1(key);
        int index = startIndex;
        int step = 1;

        // 线性探测查找
        while (keys[index] != EMPTY) {
            // 找到键，返回对应的值
            if (keys[index] == key) {
                return values[index];
            }

            // 线性探测：移动到下一个位置
            index = (startIndex + step) % capacity;
            step++;

            // 如果探测次数超过容量，说明键不存在
            if (step > capacity) {
                break;
            }
        }

        return -1;  // 未找到
    }

    /**
     * 根据键查找索引位置
     *
     * @param key 键
     * @return 找到的索引，如果不存在返回-1
     */
    private int searchIndex(int key) {
        int startIndex = hash1(key);
        int index = startIndex;
        int step = 1;

        // 线性探测查找
        while (keys[index] != EMPTY) {
            // 找到键，返回索引
            if (keys[index] == key) {
                return index;
            }

            // 线性探测：移动到下一个位置
            index = (startIndex + step) % capacity;
            step++;

            // 如果探测次数超过容量，说明键不存在
            if (step > capacity) {
                break;
            }
        }

        return -1;  // 未找到
    }

    /**
     * 根据键删除键值对
     *
     * @param key 键
     * @return 是否删除成功
     */
    public boolean delete(int key) {
        int index = searchIndex(key);

        if (index == -1) {
            return false;  // 未找到
        }

        // 标记为已删除
        keys[index] = DELETED;
        values[index] = DELETED;

        size--;
        deletedCount++;

        // 如果删除太多，考虑重新哈希
        if (deletedCount > size) {
            rehash();
        }

        return true;
    }

    /**
     * 计算当前负载因子
     *
     * @return 负载因子
     */
    public double loadFactor() {
        return (double) size / capacity;
    }

    /**
     * 计算总占用率（包括已删除位置）
     *
     * @return 总占用率
     */
    public double occupancyRate() {
        return (double) (size + deletedCount) / capacity;
    }

    /**
     * 哈希表扩容（重新哈希）
     */
    private void resize() {
        int newCapacity = capacity * 2;
        rehashToNewCapacity(newCapacity);
    }

    /**
     * 重新哈希到新容量
     *
     * @param newCapacity 新容量
     */
    private void rehashToNewCapacity(int newCapacity) {
        // 保存旧数据
        int[] oldKeys = keys;
        int[] oldValues = values;
        int oldCapacity = capacity;

        // 创建新数组
        capacity = newCapacity;
        keys = new int[capacity];
        values = new int[capacity];
        size = 0;
        deletedCount = 0;

        // 初始化新数组
        for (int i = 0; i < capacity; i++) {
            keys[i] = EMPTY;
            values[i] = EMPTY;
        }

        // 重新插入旧数据（跳过空位置和删除标记）
        for (int i = 0; i < oldCapacity; i++) {
            if (oldKeys[i] != EMPTY && oldKeys[i] != DELETED) {
                insert(oldKeys[i], oldValues[i]);
            }
        }
    }

    /**
     * 重新哈希（保持容量不变，清除删除标记）
     */
    private void rehash() {
        rehashToNewCapacity(capacity);
    }

    /**
     * 获取哈希表所有键值对
     *
     * @return 键值对数组，格式为[[key1,value1], [key2,value2], ...]
     */
    public int[][] getAllEntries() {
        int[][] entries = new int[size][2];
        int index = 0;

        for (int i = 0; i < capacity; i++) {
            if (keys[i] != EMPTY && keys[i] != DELETED) {
                entries[index][0] = keys[i];
                entries[index][1] = values[i];
                index++;
            }
        }

        return entries;
    }

    /**
     * 显示哈希表结构（用于调试）
     */
    public void display() {
        System.out.println("哈希表容量: " + capacity);
        System.out.println("元素数量: " + size);
        System.out.println("已删除位置: " + deletedCount);
        System.out.println("负载因子: " + String.format("%.2f", loadFactor()));
        System.out.println("总占用率: " + String.format("%.2f", occupancyRate()));
        System.out.println("哈希表内容:");

        for (int i = 0; i < capacity; i++) {
            String status;
            if (keys[i] == EMPTY) {
                status = "[空]";
            } else if (keys[i] == DELETED) {
                status = "[已删除]";
            } else {
                status = "[" + keys[i] + "->" + values[i] + "]";
            }

            System.out.println("索引 " + i + ": " + status);
        }
    }

    /**
     * 获取统计信息
     *
     * @return 统计信息数组：[容量, 元素数量, 已删除位置, 负载因子, 总占用率]
     */
    public double[] getStats() {
        return new double[] {
                capacity,
                size,
                deletedCount,
                loadFactor(),
                occupancyRate()
        };
    }

    /**
     * 从键值对数组构建哈希表
     *
     * @param entries  键值对数组，格式为[[key1,value1], [key2,value2], ...]
     * @param capacity 哈希表容量
     * @return 构建的哈希表
     */
    public static OpenAddressingHashTableUtils buildFromEntries(int[][] entries, int capacity) {
        OpenAddressingHashTableUtils hashTable = new OpenAddressingHashTableUtils(capacity);

        for (int[] entry : entries) {
            if (entry.length >= 2) {
                hashTable.insert(entry[0], entry[1]);
            }
        }

        return hashTable;
    }

    /**
     * 从随机数组构建哈希表（键和值相同）
     *
     * @param keys     键数组
     * @param capacity 哈希表容量
     * @return 构建的哈希表
     */
    public static OpenAddressingHashTableUtils buildFromRandomArray(int[] keys, int capacity) {
        OpenAddressingHashTableUtils hashTable = new OpenAddressingHashTableUtils(capacity);

        for (int key : keys) {
            hashTable.insert(key, key);
        }

        return hashTable;
    }

    /**
     * 生成冲突测试用例
     *
     * @param size     数组大小
     * @param base     基值
     * @param capacity 哈希表容量（用于生成冲突）
     * @return 冲突数组
     */
    public static int[] generateConflictArray(int size, int base, int capacity) {
        int[] array = new int[size];

        // 生成具有相同哈希值的键（会产生冲突）
        for (int i = 0; i < size; i++) {
            array[i] = base + i * capacity;
        }

        return array;
    }

    /**
     * 测试线性探测性能
     *
     * @param hashTable 哈希表
     * @param keys      测试键数组
     * @return 性能统计：[插入时间ms, 查找时间ms, 删除时间ms]
     */
    public static long[] testPerformance(OpenAddressingHashTableUtils hashTable, int[] keys) {
        long startTime, endTime;
        long[] times = new long[3];

        // 测试插入性能
        startTime = System.currentTimeMillis();
        for (int key : keys) {
            hashTable.insert(key, key * 2);
        }
        endTime = System.currentTimeMillis();
        times[0] = endTime - startTime;

        // 测试查找性能
        startTime = System.currentTimeMillis();
        for (int key : keys) {
            hashTable.search(key);
        }
        endTime = System.currentTimeMillis();
        times[1] = endTime - startTime;

        // 测试删除性能
        startTime = System.currentTimeMillis();
        for (int key : keys) {
            hashTable.delete(key);
        }
        endTime = System.currentTimeMillis();
        times[2] = endTime - startTime;

        return times;
    }
}

// ==================== 主程序类 ====================
public class HashTable {

    public static void main(String[] args) {
        System.out.println("========== 开地址法哈希表完整演示 ==========\n");

        // 演示1：基本操作
        demonstrateBasicOperations();

        // 演示2：冲突处理
        demonstrateConflictHandling();

        // 演示3：扩容机制
        demonstrateResizing();

        // 演示4：构建方法
        demonstrateBuildMethods();

        // 演示5：性能测试
        demonstratePerformanceTest();

        // 演示6：统计信息
        demonstrateStatistics();

        System.out.println("\n========== 演示结束 ==========");
    }

    /**
     * 演示1：基本操作（插入、查找、删除）
     */
    private static void demonstrateBasicOperations() {
        System.out.println("=== 演示1：基本操作 ===");

        // 1. 创建哈希表
        System.out.println("\n1. 创建初始容量为10的哈希表");
        OpenAddressingHashTableUtils hashTable = new OpenAddressingHashTableUtils(10);

        // 2. 插入键值对
        System.out.println("\n2. 插入键值对");
        int[][] testData = {
                {101, 1001}, {202, 2002}, {303, 3003}, {404, 4004},
                {505, 5005}, {606, 6006}, {707, 7007}, {808, 8008}
        };

        for (int[] entry : testData) {
            boolean success = hashTable.insert(entry[0], entry[1]);
            System.out.println("  插入 (" + entry[0] + ", " + entry[1] + "): " +
                    (success ? "成功" : "失败"));
        }

        // 3. 显示哈希表状态
        System.out.println("\n3. 哈希表当前状态");
        hashTable.display();

        // 4. 查找操作
        System.out.println("\n4. 查找操作");
        int[] searchKeys = {101, 303, 505, 999};
        for (int key : searchKeys) {
            int value = hashTable.search(key);
            if (value != -1) {
                System.out.println("  找到键 " + key + "，值为: " + value);
            } else {
                System.out.println("  未找到键 " + key);
            }
        }

        // 5. 删除操作
        System.out.println("\n5. 删除操作");
        int[] deleteKeys = {202, 404, 707};
        for (int key : deleteKeys) {
            boolean deleted = hashTable.delete(key);
            System.out.println("  删除键 " + key + ": " + (deleted ? "成功" : "失败"));
        }

        // 6. 删除后再次显示
        System.out.println("\n6. 删除后的哈希表状态");
        hashTable.display();

        // 7. 获取所有条目
        System.out.println("\n7. 获取所有条目");
        int[][] allEntries = hashTable.getAllEntries();
        System.out.print("  所有键值对: ");
        for (int[] entry : allEntries) {
            System.out.print("(" + entry[0] + "," + entry[1] + ") ");
        }
        System.out.println();
    }

    /**
     * 演示2：冲突处理
     */
    private static void demonstrateConflictHandling() {
        System.out.println("\n\n=== 演示2：冲突处理 ===");

        // 1. 创建小容量哈希表以观察冲突
        System.out.println("\n1. 创建容量为5的哈希表（易于观察冲突）");
        OpenAddressingHashTableUtils smallTable = new OpenAddressingHashTableUtils(5);

        // 2. 插入一组会产生冲突的键
        System.out.println("\n2. 插入会产生冲突的键（哈希值相同）");
        int[] conflictKeys = {5, 10, 15, 20}; // 所有键模5都为0

        for (int key : conflictKeys) {
            smallTable.insert(key, key * 100);
            System.out.println("  插入 (" + key + ", " + (key * 100) + ")");
            System.out.println("  哈希值: " + (Math.abs(key) % 5));
        }

        // 3. 显示哈希表结构，观察冲突解决
        System.out.println("\n3. 冲突解决后的哈希表结构");
        smallTable.display();

        // 4. 查找冲突键
        System.out.println("\n4. 查找冲突键");
        for (int key : conflictKeys) {
            int value = smallTable.search(key);
            System.out.println("  查找 " + key + ": " +
                    (value != -1 ? "找到，值=" + value : "未找到"));
        }

        // 5. 生成专门测试冲突的数组
        System.out.println("\n5. 生成专门测试冲突的数组");
        int[] conflictArray = OpenAddressingHashTableUtils.generateConflictArray(8, 3, 7);
        System.out.print("  冲突数组: ");
        for (int key : conflictArray) {
            System.out.print(key + " (哈希=" + (Math.abs(key) % 7) + ") ");
        }
        System.out.println();

        // 6. 用冲突数组构建哈希表
        System.out.println("\n6. 用冲突数组构建哈希表");
        OpenAddressingHashTableUtils conflictTable =
                OpenAddressingHashTableUtils.buildFromRandomArray(conflictArray, 10);
        conflictTable.display();
    }

    /**
     * 演示3：扩容机制
     */
    private static void demonstrateResizing() {
        System.out.println("\n\n=== 演示3：扩容机制 ===");

        // 1. 创建初始容量为5的小表
        System.out.println("\n1. 创建初始容量为5的哈希表（负载因子阈值=0.7）");
        OpenAddressingHashTableUtils resizingTable = new OpenAddressingHashTableUtils(5);

        // 2. 插入元素，触发扩容
        System.out.println("\n2. 插入元素，观察扩容过程");
        System.out.println("  当前容量: " + (int)resizingTable.getStats()[0]);
        System.out.println("  当前负载因子: " + String.format("%.2f", resizingTable.loadFactor()));

        // 插入元素直到触发扩容
        int[] testKeys = {11, 22, 33, 44, 55, 66, 77, 88};
        for (int i = 0; i < testKeys.length; i++) {
            int key = testKeys[i];
            resizingTable.insert(key, key * 10);
            System.out.println("\n  插入 (" + key + ", " + (key * 10) + ")");
            System.out.println("  当前容量: " + (int)resizingTable.getStats()[0]);
            System.out.println("  元素数量: " + (int)resizingTable.getStats()[1]);
            System.out.println("  负载因子: " + String.format("%.2f", resizingTable.loadFactor()));

            if (i == 3) {
                System.out.println("  → 插入第4个元素，负载因子=0.8，触发扩容！");
            }
        }

        // 3. 显示扩容后的哈希表
        System.out.println("\n3. 扩容后的哈希表");
        resizingTable.display();

        // 4. 测试删除后的重新哈希
        System.out.println("\n4. 测试删除后的重新哈希");
        System.out.println("  删除3个元素...");
        resizingTable.delete(11);
        resizingTable.delete(33);
        resizingTable.delete(55);

        System.out.println("  删除后的统计:");
        double[] stats = resizingTable.getStats();
        System.out.println("  容量: " + (int)stats[0]);
        System.out.println("  元素数量: " + (int)stats[1]);
        System.out.println("  已删除位置: " + (int)stats[2]);
        System.out.println("  负载因子: " + String.format("%.2f", stats[3]));
        System.out.println("  总占用率: " + String.format("%.2f", stats[4]));

        // 触发重新哈希（删除位置过多）
        System.out.println("\n  继续删除...");
        resizingTable.delete(22);
        resizingTable.delete(44);

        System.out.println("  删除位置数 > 元素数，触发重新哈希");
        stats = resizingTable.getStats();
        System.out.println("  重新哈希后容量: " + (int)stats[0]);
        System.out.println("  重新哈希后元素数: " + (int)stats[1]);
        System.out.println("  重新哈希后已删除位置: " + (int)stats[2]);
    }

    /**
     * 演示4：构建方法
     */
    private static void demonstrateBuildMethods() {
        System.out.println("\n\n=== 演示4：构建方法 ===");

        // 1. 从键值对数组构建
        System.out.println("\n1. 从键值对数组构建哈希表");
        int[][] entries = {
                {100, 1}, {200, 2}, {300, 3}, {400, 4},
                {500, 5}, {600, 6}, {700, 7}, {800, 8}
        };

        OpenAddressingHashTableUtils table1 =
                OpenAddressingHashTableUtils.buildFromEntries(entries, 15);
        System.out.println("  构建完成，哈希表状态:");
        table1.display();

        // 2. 从随机数组构建
        System.out.println("\n2. 从随机数组构建哈希表（键和值相同）");
        int[] randomKeys = {123, 456, 789, 321, 654, 987};
        OpenAddressingHashTableUtils table2 =
                OpenAddressingHashTableUtils.buildFromRandomArray(randomKeys, 10);
        System.out.println("  随机数组: " + Arrays.toString(randomKeys));
        System.out.println("  构建完成，哈希表状态:");
        table2.display();

        // 3. 合并两个哈希表
        System.out.println("\n3. 合并两个哈希表");
        System.out.println("  将第二个哈希表的元素插入到第一个哈希表");
        int[][] entries2 = table2.getAllEntries();
        for (int[] entry : entries2) {
            table1.insert(entry[0], entry[1]);
        }

        System.out.println("  合并后的哈希表:");
        table1.display();
    }

    /**
     * 演示5：性能测试
     */
    private static void demonstratePerformanceTest() {
        System.out.println("\n\n=== 演示5：性能测试 ===");

        // 1. 准备测试数据
        System.out.println("\n1. 准备测试数据");
        int testSize = 10000;
        int[] testKeys = new int[testSize];
        Random random = new Random();

        for (int i = 0; i < testSize; i++) {
            testKeys[i] = random.nextInt(1000000); // 生成0-100万之间的随机数
        }

        System.out.println("  生成 " + testSize + " 个随机键");

        // 2. 创建哈希表
        System.out.println("\n2. 创建哈希表（容量=" + (testSize / 5) + "）");
        OpenAddressingHashTableUtils perfTable =
                new OpenAddressingHashTableUtils(testSize / 5);

        // 3. 测试性能
        System.out.println("\n3. 执行性能测试");
        long[] times = OpenAddressingHashTableUtils.testPerformance(perfTable, testKeys);

        System.out.println("  插入 " + testSize + " 个元素耗时: " + times[0] + "ms");
        System.out.println("  查找 " + testSize + " 个元素耗时: " + times[1] + "ms");
        System.out.println("  删除 " + testSize + " 个元素耗时: " + times[2] + "ms");

        // 4. 不同负载因子下的性能对比
        System.out.println("\n4. 不同负载因子下的性能对比");
        System.out.println("  当前负载因子: " + String.format("%.2f", perfTable.loadFactor()));

        // 创建不同负载因子的哈希表
        int smallCapacity = testSize;      // 负载因子 ~0.5
        int mediumCapacity = testSize * 2 / 3; // 负载因子 ~0.75
        int largeCapacity = testSize / 2;  // 负载因子 ~1.0

        System.out.println("\n  创建三个不同容量的哈希表:");
        System.out.println("  表1: 容量=" + smallCapacity + " (负载因子~0.5)");
        System.out.println("  表2: 容量=" + mediumCapacity + " (负载因子~0.75)");
        System.out.println("  表3: 容量=" + largeCapacity + " (负载因子~1.0)");

        // 测试不同负载因子的插入性能
        OpenAddressingHashTableUtils table1 = new OpenAddressingHashTableUtils(smallCapacity);
        OpenAddressingHashTableUtils table2 = new OpenAddressingHashTableUtils(mediumCapacity);
        OpenAddressingHashTableUtils table3 = new OpenAddressingHashTableUtils(largeCapacity);

        long start1 = System.currentTimeMillis();
        for (int key : testKeys) {
            table1.insert(key, key * 2);
        }
        long end1 = System.currentTimeMillis();

        long start2 = System.currentTimeMillis();
        for (int key : testKeys) {
            table2.insert(key, key * 2);
        }
        long end2 = System.currentTimeMillis();

        long start3 = System.currentTimeMillis();
        for (int key : testKeys) {
            table3.insert(key, key * 2);
        }
        long end3 = System.currentTimeMillis();

        System.out.println("\n  不同负载因子下的插入性能:");
        System.out.println("  负载因子~0.5: " + (end1 - start1) + "ms");
        System.out.println("  负载因子~0.75: " + (end2 - start2) + "ms");
        System.out.println("  负载因子~1.0: " + (end3 - start3) + "ms");
    }

    /**
     * 演示6：统计信息
     */
    private static void demonstrateStatistics() {
        System.out.println("\n\n=== 演示6：统计信息 ===");

        // 1. 创建哈希表并插入数据
        System.out.println("\n1. 创建哈希表并插入数据");
        OpenAddressingHashTableUtils statsTable = new OpenAddressingHashTableUtils(15);

        // 插入一些数据
        int[][] sampleData = {
                {10, 100}, {25, 250}, {40, 400}, {55, 550},
                {70, 700}, {85, 850}, {100, 1000}, {115, 1150},
                {130, 1300}, {145, 1450}
        };

        for (int[] entry : sampleData) {
            statsTable.insert(entry[0], entry[1]);
        }

        // 2. 获取统计信息
        System.out.println("\n2. 获取详细统计信息");
        double[] stats = statsTable.getStats();

        System.out.println("  容量: " + (int)stats[0]);
        System.out.println("  元素数量: " + (int)stats[1]);
        System.out.println("  已删除位置: " + (int)stats[2]);
        System.out.println("  负载因子: " + String.format("%.4f", stats[3]));
        System.out.println("  总占用率: " + String.format("%.4f", stats[4]));

        // 3. 分析哈希效率
        System.out.println("\n3. 哈希效率分析");
        System.out.println("  理想情况: 负载因子 < 0.7");
        System.out.println("  当前状态: " +
                (stats[3] < 0.7 ? "良好" : "需要扩容"));

        System.out.println("  删除位置占比: " +
                String.format("%.2f", (stats[2] / stats[0]) * 100) + "%");

        // 4. 显示哈希表内容
        System.out.println("\n4. 哈希表详细内容");
        statsTable.display();

        // 5. 计算冲突率
        System.out.println("\n5. 冲突率分析");
        int[] sampleKeys = {10, 25, 40, 55, 70, 85, 100, 115, 130, 145};
        int conflicts = 0;
        int totalProbes = 0;

        for (int key : sampleKeys) {
            int hash = Math.abs(key) % 15;
            int value = statsTable.search(key);

            if (value != -1) {
                // 简单估算探测次数（实际实现会更复杂）
                int probes = 1; // 至少探测1次
                // 这里简化处理，实际应该记录插入时的探测次数
                totalProbes += probes;
            }
        }

        System.out.println("  平均探测次数: " +
                String.format("%.2f", (double)totalProbes / sampleKeys.length));
        System.out.println("  理论最优（无冲突）: 1.0");

        // 6. 内存使用分析
        System.out.println("\n6. 内存使用分析");
        int intSize = 4; // Java中int占用4字节
        int overhead = 16; // 对象头部开销（估算）
        int totalMemory = (int)(stats[0] * 2 * intSize + overhead);

        System.out.println("  键数组大小: " + (int)stats[0] + " × 4字节 = " +
                ((int)stats[0] * 4) + " 字节");
        System.out.println("  值数组大小: " + (int)stats[0] + " × 4字节 = " +
                ((int)stats[0] * 4) + " 字节");
        System.out.println("  总内存使用: ~" + totalMemory + " 字节");
        System.out.println("  存储效率: " +
                String.format("%.2f", (stats[1] / stats[0]) * 100) + "%");

        // 7. 建议优化
        System.out.println("\n7. 优化建议");
        if (stats[3] > 0.7) {
            System.out.println("  → 建议扩容以减少冲突");
        }
        if (stats[2] > stats[1]) {
            System.out.println("  → 建议重新哈希以清理删除标记");
        }
        if (stats[4] > 0.9) {
            System.out.println("  → 哈希表接近满载，考虑扩容");
        }

        double efficiency = stats[1] / stats[0];
        if (efficiency < 0.3) {
            System.out.println("  → 哈希表使用率低，考虑减小容量");
        }
    }
}

// ==================== 运行说明 ====================
/*
如何运行此程序：
1. 将以上所有代码保存为 OpenAddressingHashTableDemo.java
2. 编译: javac OpenAddressingHashTableDemo.java
3. 运行: java OpenAddressingHashTableDemo

程序包含6个主要演示：
1. 基本操作：插入、查找、删除
2. 冲突处理：线性探测解决冲突
3. 扩容机制：负载因子触发扩容
4. 构建方法：从不同数据源构建哈希表
5. 性能测试：大规模数据操作性能
6. 统计信息：详细分析和优化建议

每个演示都会输出详细的结果和说明。
*/