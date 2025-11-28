import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * 统一的年龄阶段面板，处理 1-5岁、6-15岁、16-25岁 所有阶段
 * 修改：角色属性在右上角，当前行为在历史行为下面，历史记录按时间顺序显示
 */
public class AgePanel {
    private final AppUI ui;
    private final Database db;
    private final BehaviorService behaviorService;
    private final GameState state;
    private final Random random = new Random();

    public AgePanel(AppUI ui, Database db, BehaviorService behaviorService, GameState state) {
        this.ui = ui;
        this.db = db;
        this.behaviorService = behaviorService;
        this.state = state;
    }

    public JPanel createPanel() {
        int currentAge = state.getCurrentAge();

        // 根据当前年龄设置进度
        if (currentAge >= 1 && currentAge <= 5) {
            state.setProgress(4);
        } else if (currentAge >= 6 && currentAge <= 15) {
            state.setProgress(5);
        } else if (currentAge >= 16 && currentAge <= 25) {
            state.setProgress(6);
        }

        // 主面板使用 BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // =============================
        // 右上角角色数据面板 - 紧凑布局
        // =============================
        JPanel statsPanel = createStatsPanel();
        panel.add(statsPanel, BorderLayout.EAST);

        // =============================
        // 中间内容区域
        // =============================
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 当前阶段标题
        JLabel ageTitle = new JLabel("当前阶段：" + currentAge + "岁", SwingConstants.CENTER);
        ageTitle.setFont(new Font("微软雅黑", Font.BOLD, 24));
        contentPanel.add(ageTitle, BorderLayout.NORTH);

        // 随机获取当前年龄的行为
        try {
            BehaviorService.BehaviorData currentBehavior = getRandomBehavior(currentAge);

            if (currentBehavior != null) {
                // 所有历史行为记录
                JPanel historyPanel = createHistoryPanel();
                contentPanel.add(historyPanel, BorderLayout.CENTER);

                // 当前行为展示 - 放在历史记录下面
                JPanel currentBehaviorPanel = createCurrentBehaviorPanel(currentBehavior);
                contentPanel.add(currentBehaviorPanel, BorderLayout.SOUTH);

                // 确认按钮 - 放在底部
                JPanel btnPanel = createButtonPanel(currentBehavior);
                panel.add(btnPanel, BorderLayout.SOUTH);
            } else {
                JOptionPane.showMessageDialog(ui.getFrame(), "未找到当前年龄的行为数据", "错误", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(ui.getFrame(), "加载成长行为失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    /**
     * 创建右上角角色数据面板 - 紧凑布局
     */
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(5, 1, 0, 0)); // 减少行间距到1
        statsPanel.setBorder(BorderFactory.createTitledBorder("角色属性"));
        statsPanel.setPreferredSize(new Dimension(140, 80)); // 进一步缩小尺寸
        statsPanel.setBackground(new Color(240, 240, 240));

        Font smallFont = new Font("微软雅黑", Font.PLAIN, 12); // 字体再小一点

        JLabel connectionsLabel = new JLabel("人脉:");
        connectionsLabel.setFont(smallFont);
        connectionsLabel.setVerticalAlignment(SwingConstants.CENTER);
        JLabel connectionsValue = new JLabel(String.valueOf(state.getConnections()));
        connectionsValue.setFont(smallFont);
        connectionsValue.setVerticalAlignment(SwingConstants.CENTER);

        JLabel intelligenceLabel = new JLabel("智力:");
        intelligenceLabel.setFont(smallFont);
        intelligenceLabel.setVerticalAlignment(SwingConstants.CENTER);
        JLabel intelligenceValue = new JLabel(String.valueOf(state.getIntelligence()));
        intelligenceValue.setFont(smallFont);
        intelligenceValue.setVerticalAlignment(SwingConstants.CENTER);

        JLabel physiqueLabel = new JLabel("体质:");
        physiqueLabel.setFont(smallFont);
        physiqueLabel.setVerticalAlignment(SwingConstants.CENTER);
        JLabel physiqueValue = new JLabel(String.valueOf(state.getPhysique()));
        physiqueValue.setFont(smallFont);
        physiqueValue.setVerticalAlignment(SwingConstants.CENTER);

        JLabel wealthLabel = new JLabel("财富:");
        wealthLabel.setFont(smallFont);
        wealthLabel.setVerticalAlignment(SwingConstants.CENTER);
        JLabel wealthValue = new JLabel(String.valueOf(state.getWealth()));
        wealthValue.setFont(smallFont);
        wealthValue.setVerticalAlignment(SwingConstants.CENTER);

        JLabel healthLabel = new JLabel("健康:");
        healthLabel.setFont(smallFont);
        healthLabel.setVerticalAlignment(SwingConstants.CENTER);
        JLabel healthValue = new JLabel(String.valueOf(state.getHealth()));
        healthValue.setFont(smallFont);
        healthValue.setVerticalAlignment(SwingConstants.CENTER);

        statsPanel.add(connectionsLabel);
        statsPanel.add(connectionsValue);
        statsPanel.add(intelligenceLabel);
        statsPanel.add(intelligenceValue);
        statsPanel.add(physiqueLabel);
        statsPanel.add(physiqueValue);
        statsPanel.add(wealthLabel);
        statsPanel.add(wealthValue);
        statsPanel.add(healthLabel);
        statsPanel.add(healthValue);

        return statsPanel;
    }

    /**
     * 创建当前行为展示面板
     */
    private JPanel createCurrentBehaviorPanel(BehaviorService.BehaviorData behavior) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("当前行为"));
        panel.setPreferredSize(new Dimension(600, 120));

        JTextArea behaviorText = new JTextArea(
                String.format("当前%d岁的行为：\n%s\n\n属性变化：人脉+%d 智力+%d 体质+%d 财富+%d 健康+%d",
                        state.getCurrentAge(),
                        behavior.behavior,
                        behavior.connectionsChange,
                        behavior.intelligenceChange,
                        behavior.physiqueChange,
                        behavior.wealthChange,
                        behavior.healthChange)
        );
        behaviorText.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        behaviorText.setEditable(false);
        behaviorText.setLineWrap(true);
        behaviorText.setWrapStyleWord(true);
        behaviorText.setBackground(new Color(255, 255, 200)); // 浅黄色背景突出显示

        panel.add(new JScrollPane(behaviorText), BorderLayout.CENTER);
        return panel;
    }

    /**
     * 创建历史行为记录面板 - 显示所有历史行为，按时间顺序（从出生到现在）
     */
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("所有历史行为记录"));
        panel.setPreferredSize(new Dimension(600, 250));

        // 从 GameState 获取所有历史记录
        DefaultListModel<String> historyModel = new DefaultListModel<>();
        List<String> allHistory = state.getBehaviorHistory();

        if (allHistory.isEmpty()) {
            historyModel.addElement("暂无历史记录");
        } else {
            // 显示所有历史记录，按时间顺序（从出生到现在）
            // 由于 GameState 中历史记录是新的在前面，我们需要反转顺序
            for (int i = allHistory.size() - 1; i >= 0; i--) {
                historyModel.addElement(allHistory.get(i));
            }
        }

        JList<String> historyList = new JList<>(historyModel);
        historyList.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        historyList.setBackground(new Color(245, 245, 245));

        // 设置固定行高，确保每行都能完整显示
        historyList.setFixedCellHeight(30);

        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * 创建按钮面板
     */
    private JPanel createButtonPanel(BehaviorService.BehaviorData currentBehavior) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton confirmBtn = new JButton("确认行为");
        confirmBtn.setPreferredSize(new Dimension(120, 50));
        confirmBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        confirmBtn.addActionListener(e -> {
            // 创建行为信息字符串
            String behaviorInfo = String.format(
                    "%d岁: %s （人脉+%d | 智力+%d | 体质+%d | 财富+%d | 健康+%d）",
                    state.getCurrentAge(),
                    currentBehavior.behavior,
                    currentBehavior.connectionsChange,
                    currentBehavior.intelligenceChange,
                    currentBehavior.physiqueChange,
                    currentBehavior.wealthChange,
                    currentBehavior.healthChange
            );

            // 添加到历史记录（新记录会添加到列表开头）
            state.addBehaviorHistory(behaviorInfo);

            // 应用属性变化
            state.increaseAttributes(
                    currentBehavior.connectionsChange,
                    currentBehavior.intelligenceChange,
                    currentBehavior.physiqueChange,
                    currentBehavior.wealthChange,
                    currentBehavior.healthChange
            );

            // 年龄递增并处理阶段转换
            state.setCurrentAge(state.getCurrentAge() + 1);
            navigateToNextStage();
        });

        JButton randomBtn = new JButton("重新随机");
        randomBtn.setPreferredSize(new Dimension(120, 50));
        randomBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        randomBtn.addActionListener(e -> {
            // 重新加载当前面板，获取新的随机行为
            ui.showAgePanel();
        });

        panel.add(confirmBtn);
        panel.add(randomBtn);

        return panel;
    }

    /**
     * 根据当前年龄随机获取一个行为
     */
    private BehaviorService.BehaviorData getRandomBehavior(int currentAge) throws SQLException {
        List<BehaviorService.BehaviorData> behaviors;

        if (currentAge >= 1 && currentAge <= 5) {
            behaviors = behaviorService.load1to5ForAge(currentAge);
        } else if (currentAge >= 6 && currentAge <= 15) {
            behaviors = behaviorService.load6to15ForBackground(state.getCurrentBackground());
        } else if (currentAge >= 16 && currentAge <= 25) {
            behaviors = behaviorService.load16to25ForBackground(state.getCurrentBackground());
        } else {
            throw new SQLException("不支持的年龄阶段: " + currentAge);
        }

        if (behaviors.isEmpty()) {
            return null;
        }

        // 随机选择一个行为
        int randomIndex = random.nextInt(behaviors.size());
        return behaviors.get(randomIndex);
    }

    /**
     * 根据年龄导航到下一个阶段
     */
    private void navigateToNextStage() {
        int currentAge = state.getCurrentAge();

        if (currentAge <= 5) {
            // 1-5岁阶段，继续显示当前面板
            ui.showAgePanel();
        } else if (currentAge == 6) {
            // 进入6-15岁阶段
            ui.showAgePanel();
        } else if (currentAge <= 15) {
            // 6-15岁阶段，继续显示当前面板
            ui.showAgePanel();
        } else if (currentAge == 16) {
            // 进入16-25岁阶段
            ui.showAgePanel();
        } else if (currentAge <= 25) {
            // 16-25岁阶段，继续显示当前面板
            ui.showAgePanel();
        } else {
            // 所有阶段完成
            JOptionPane.showMessageDialog(ui.getFrame(), "所有成长阶段已完成！", "游戏结束", JOptionPane.INFORMATION_MESSAGE);
            ui.showResultPanel();
        }
    }
}