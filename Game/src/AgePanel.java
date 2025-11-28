import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * 统一的年龄阶段面板，处理所有年龄阶段
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
        } else if (currentAge >= 26 && currentAge <= 35) {
            state.setProgress(7); // 26-35岁职业发展阶段
        }

        // 主面板使用 BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // =============================
        // 右上角角色数据面板
        // =============================
        JPanel statsPanel = createStatsPanel();
        panel.add(statsPanel, BorderLayout.EAST);

        // =============================
        // 中间内容区域
        // =============================
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 当前阶段标题
        String ageTitleText = getAgeTitle(currentAge);
        JLabel ageTitle = new JLabel(ageTitleText, SwingConstants.CENTER);
        ageTitle.setFont(new Font("微软雅黑", Font.BOLD, 24));
        contentPanel.add(ageTitle, BorderLayout.NORTH);

        // 检查是否需要选择职业
        if (currentAge == 26 && state.getCurrentCareer() == null) {
            JPanel careerReminderPanel = createCareerReminderPanel();
            contentPanel.add(careerReminderPanel, BorderLayout.CENTER);

            JPanel btnPanel = createCareerButtonPanel();
            panel.add(btnPanel, BorderLayout.SOUTH);
        } else {
            // 正常行为选择流程
            try {
                BehaviorService.BehaviorData currentBehavior = getRandomBehavior(currentAge);

                if (currentBehavior != null) {
                    // 所有历史行为记录
                    JPanel historyPanel = createHistoryPanel();
                    contentPanel.add(historyPanel, BorderLayout.CENTER);

                    // 当前行为展示
                    JPanel currentBehaviorPanel = createCurrentBehaviorPanel(currentBehavior, currentAge);
                    contentPanel.add(currentBehaviorPanel, BorderLayout.SOUTH);

                    // 确认按钮
                    JPanel btnPanel = createButtonPanel(currentBehavior);
                    panel.add(btnPanel, BorderLayout.SOUTH);
                } else {
                    JOptionPane.showMessageDialog(ui.getFrame(),
                            "未找到当前年龄的行为数据。\n当前年龄: " + currentAge +
                                    (state.getCurrentCareer() != null ? "\n当前职业: " + state.getCurrentCareer() : ""),
                            "错误", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ui.getFrame(), "加载成长行为失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    /**
     * 获取年龄阶段标题
     */
    private String getAgeTitle(int currentAge) {
        if (currentAge >= 1 && currentAge <= 5) {
            return "幼儿期：" + currentAge + "岁";
        } else if (currentAge >= 6 && currentAge <= 15) {
            return "少年期：" + currentAge + "岁";
        } else if (currentAge >= 16 && currentAge <= 25) {
            return "青年期：" + currentAge + "岁";
        } else if (currentAge >= 26 && currentAge <= 35) {
            String careerInfo = state.getCurrentCareer() != null ?
                    " - " + state.getCurrentCareer() : " (未选择职业)";
            return "职业发展期：" + currentAge + "岁" + careerInfo;
        } else {
            return currentAge + "岁";
        }
    }

    /**
     * 创建职业提醒面板（当26岁未选择职业时显示）
     */
    private JPanel createCareerReminderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("职业选择提醒"));

        JTextArea reminderText = new JTextArea(
                "你已经26岁了，需要选择职业来继续发展！\n\n" +
                        "职业选择将决定你未来的发展方向和收入水平。\n" +
                        "请点击下方的\"选择职业\"按钮来开始你的职业生涯。\n\n" +
                        "当前属性评估：\n" +
                        "• 人脉: " + state.getConnections() + "\n" +
                        "• 智力: " + state.getIntelligence() + "\n" +
                        "• 财富: " + state.getWealth() + "\n\n" +
                        "根据你的属性，所有职业都对你开放！"
        );
        reminderText.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        reminderText.setEditable(false);
        reminderText.setLineWrap(true);
        reminderText.setWrapStyleWord(true);
        reminderText.setBackground(new Color(255, 255, 200));

        panel.add(new JScrollPane(reminderText), BorderLayout.CENTER);
        return panel;
    }

    /**
     * 创建职业选择按钮面板
     */
    private JPanel createCareerButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton careerBtn = new JButton("选择职业");
        careerBtn.setPreferredSize(new Dimension(150, 50));
        careerBtn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        careerBtn.addActionListener(e -> ui.showCareerPanel());

        panel.add(careerBtn);
        return panel;
    }

    /**
     * 创建右上角角色数据面板
     */
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(5, 1, 0, 0));
        statsPanel.setBorder(BorderFactory.createTitledBorder("角色属性"));
        statsPanel.setPreferredSize(new Dimension(140, 80));
        statsPanel.setBackground(new Color(240, 240, 240));

        Font smallFont = new Font("微软雅黑", Font.PLAIN, 12);

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
    private JPanel createCurrentBehaviorPanel(BehaviorService.BehaviorData behavior, int currentAge) {
        JPanel panel = new JPanel(new BorderLayout());

        String panelTitle = "当前行为";
        if (currentAge >= 26 && currentAge <= 35 && state.getCurrentCareer() != null) {
            panelTitle += " [" + state.getCurrentCareer() + "]";
        }

        panel.setBorder(BorderFactory.createTitledBorder(panelTitle));
        panel.setPreferredSize(new Dimension(600, 120));

        String behaviorText;
        if (currentAge >= 26 && currentAge <= 35 && state.getCurrentCareer() != null) {
            behaviorText = String.format("职业发展（%d岁）：\n%s\n\n属性变化：人脉+%d 智力+%d 体质+%d 财富+%d 健康+%d",
                    currentAge,
                    behavior.behavior,
                    behavior.connectionsChange,
                    behavior.intelligenceChange,
                    behavior.physiqueChange,
                    behavior.wealthChange,
                    behavior.healthChange);
        } else {
            behaviorText = String.format("当前%d岁的行为：\n%s\n\n属性变化：人脉+%d 智力+%d 体质+%d 财富+%d 健康+%d",
                    currentAge,
                    behavior.behavior,
                    behavior.connectionsChange,
                    behavior.intelligenceChange,
                    behavior.physiqueChange,
                    behavior.wealthChange,
                    behavior.healthChange);
        }

        JTextArea behaviorTextArea = new JTextArea(behaviorText);
        behaviorTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        behaviorTextArea.setEditable(false);
        behaviorTextArea.setLineWrap(true);
        behaviorTextArea.setWrapStyleWord(true);
        behaviorTextArea.setBackground(new Color(255, 255, 200));

        panel.add(new JScrollPane(behaviorTextArea), BorderLayout.CENTER);
        return panel;
    }

    /**
     * 创建历史行为记录面板
     */
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("所有历史行为记录"));
        panel.setPreferredSize(new Dimension(600, 250));

        DefaultListModel<String> historyModel = new DefaultListModel<>();
        List<String> allHistory = state.getBehaviorHistory();

        if (allHistory.isEmpty()) {
            historyModel.addElement("暂无历史记录");
        } else {
            for (int i = allHistory.size() - 1; i >= 0; i--) {
                historyModel.addElement(allHistory.get(i));
            }
        }

        JList<String> historyList = new JList<>(historyModel);
        historyList.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        historyList.setBackground(new Color(245, 245, 245));
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
            String behaviorInfo;
            if (state.getCurrentAge() >= 26 && state.getCurrentAge() <= 35 && state.getCurrentCareer() != null) {
                behaviorInfo = String.format(
                        "%d岁[%s]: %s （人脉+%d | 智力+%d | 体质+%d | 财富+%d | 健康+%d）",
                        state.getCurrentAge(),
                        state.getCurrentCareer(),
                        currentBehavior.behavior,
                        currentBehavior.connectionsChange,
                        currentBehavior.intelligenceChange,
                        currentBehavior.physiqueChange,
                        currentBehavior.wealthChange,
                        currentBehavior.healthChange
                );
            } else {
                behaviorInfo = String.format(
                        "%d岁: %s （人脉+%d | 智力+%d | 体质+%d | 财富+%d | 健康+%d）",
                        state.getCurrentAge(),
                        currentBehavior.behavior,
                        currentBehavior.connectionsChange,
                        currentBehavior.intelligenceChange,
                        currentBehavior.physiqueChange,
                        currentBehavior.wealthChange,
                        currentBehavior.healthChange
                );
            }

            // 添加到历史记录
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
        randomBtn.addActionListener(e -> ui.showAgePanel());

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
        } else if (currentAge >= 26 && currentAge <= 35) {
            // 26-35岁根据职业获取行为
            if (state.getCurrentCareer() == null) {
                throw new SQLException("未选择职业，无法获取26-35岁行为");
            }
            behaviors = behaviorService.load26to35ForCareer(state.getCurrentCareer());
        } else {
            throw new SQLException("不支持的年龄阶段: " + currentAge);
        }

        if (behaviors.isEmpty()) {
            return null;
        }

        int randomIndex = random.nextInt(behaviors.size());
        return behaviors.get(randomIndex);
    }

    /**
     * 根据年龄导航到下一个阶段
     */
    private void navigateToNextStage() {
        int currentAge = state.getCurrentAge();

        if (currentAge <= 5) {
            ui.showAgePanel();
        } else if (currentAge == 6) {
            ui.showAgePanel();
        } else if (currentAge <= 15) {
            ui.showAgePanel();
        } else if (currentAge == 16) {
            ui.showAgePanel();
        } else if (currentAge <= 25) {
            ui.showAgePanel();
        } else if (currentAge == 26) {
            // 26岁检查职业选择
            if (state.getCurrentCareer() == null) {
                ui.showAgePanel(); // 继续显示年龄面板，会显示职业选择提醒
            } else {
                ui.showAgePanel();
            }
        } else if (currentAge <= 35) {
            ui.showAgePanel();
        } else {
            // 26-35岁阶段完成
            JOptionPane.showMessageDialog(ui.getFrame(),
                    "职业发展阶段已完成！\n" +
                            "你已经成为一名成功的" + state.getCurrentCareer() + "！\n" +
                            "接下来将进入人生新阶段...",
                    "阶段完成", JOptionPane.INFORMATION_MESSAGE);
            ui.showResultPanel();
        }
    }
}