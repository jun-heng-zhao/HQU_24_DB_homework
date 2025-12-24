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