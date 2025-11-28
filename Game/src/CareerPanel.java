import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * CareerPanel: 职业选择面板，在26岁时触发
 */
public class CareerPanel {
    private final AppUI ui;
    private final Database db;
    private final BehaviorService behaviorService;
    private final GameState state;

    public CareerPanel(AppUI ui, Database db, BehaviorService behaviorService, GameState state) {
        this.ui = ui;
        this.db = db;
        this.behaviorService = behaviorService;
        this.state = state;
    }

    public JPanel createPanel() {
        // 主面板使用 BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // =============================
        // 左上角角色数据面板
        // =============================
        JPanel statsPanel = createStatsPanel();
        panel.add(statsPanel, BorderLayout.NORTH);

        // =============================
        // 中间内容区域
        // =============================
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("职业选择：26-35岁发展阶段", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 28));
        contentPanel.add(title, BorderLayout.NORTH);

        // 职业选择说明
        JTextArea descriptionArea = new JTextArea(
                "恭喜你完成学业，现在进入职业生涯发展阶段！\n" +
                        "请根据你的属性选择合适的职业道路。\n" +
                        "不同的职业将影响你未来的发展和收入。\n\n" +
                        "提示：所有职业都对您开放，请根据兴趣选择！"
        );
        descriptionArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(new Color(240, 240, 240));
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(descriptionArea, BorderLayout.NORTH);

        // 职业选择按钮区域
        JPanel careerPanel = createCareerSelectionPanel();
        contentPanel.add(careerPanel, BorderLayout.CENTER);

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    /**
     * 创建左上角角色数据面板
     */
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(5, 2, 5, 2));
        statsPanel.setBorder(BorderFactory.createTitledBorder("角色属性"));
        statsPanel.setPreferredSize(new Dimension(200, 120));
        statsPanel.setBackground(new Color(240, 240, 240));

        Font smallFont = new Font("微软雅黑", Font.PLAIN, 12);

        JLabel connectionsLabel = new JLabel("人脉:");
        connectionsLabel.setFont(smallFont);
        JLabel connectionsValue = new JLabel(String.valueOf(state.getConnections()));
        connectionsValue.setFont(smallFont);

        JLabel intelligenceLabel = new JLabel("智力:");
        intelligenceLabel.setFont(smallFont);
        JLabel intelligenceValue = new JLabel(String.valueOf(state.getIntelligence()));
        intelligenceValue.setFont(smallFont);

        JLabel physiqueLabel = new JLabel("体质:");
        physiqueLabel.setFont(smallFont);
        JLabel physiqueValue = new JLabel(String.valueOf(state.getPhysique()));
        physiqueValue.setFont(smallFont);

        JLabel wealthLabel = new JLabel("财富:");
        wealthLabel.setFont(smallFont);
        JLabel wealthValue = new JLabel(String.valueOf(state.getWealth()));
        wealthValue.setFont(smallFont);

        JLabel healthLabel = new JLabel("健康:");
        healthLabel.setFont(smallFont);
        JLabel healthValue = new JLabel(String.valueOf(state.getHealth()));
        healthValue.setFont(smallFont);

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
     * 创建职业选择面板
     */
    private JPanel createCareerSelectionPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("选择职业 - 所有职业均可选择"));
        panel.setPreferredSize(new Dimension(600, 300));

        try {
            List<BehaviorService.CareerData> careers = behaviorService.loadAvailableCareers();

            if (careers.isEmpty()) {
                JLabel noCareersLabel = new JLabel("暂无可用职业数据", SwingConstants.CENTER);
                noCareersLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
                panel.add(noCareersLabel);
            } else {
                for (BehaviorService.CareerData career : careers) {
                    JButton careerBtn = createCareerButton(career);
                    panel.add(careerBtn);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("加载职业数据失败: " + e.getMessage(), SwingConstants.CENTER);
            errorLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            errorLabel.setForeground(Color.RED);
            panel.add(errorLabel);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane, BorderLayout.CENTER);

        // 添加返回按钮
        JButton backButton = new JButton("返回");
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        backButton.addActionListener(e -> {
            // 返回年龄面板
            ui.showAgePanel();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        wrapper.add(buttonPanel, BorderLayout.SOUTH);

        return wrapper;
    }

    /**
     * 创建职业选择按钮 - 移除背景限制
     */
    private JButton createCareerButton(BehaviorService.CareerData career) {
        // 创建按钮文本
        String buttonText = String.format(
                "<html><div style='text-align: center; width: 250px;'><b>%s</b><br>" +
                        "智力: %d | 人脉: %d | 财富: %d<br>" +
                        "%s</div></html>",
                career.careerName,
                career.minIntelligence,
                career.minConnections,
                career.minWealth,
                career.description.length() > 30 ? career.description.substring(0, 30) + "..." : career.description
        );

        JButton button = new JButton(buttonText);
        button.setPreferredSize(new Dimension(280, 100));
        button.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        // 移除背景限制，所有职业都可以选择
        boolean canChoose = true; // 直接设为true，移除所有限制

        // 设置按钮样式和状态
        if (canChoose) {
            button.setBackground(new Color(200, 255, 200)); // 绿色背景表示可选
            button.setEnabled(true);
            String tooltip = String.format(
                    "%s\n\n详细描述: %s\n\n属性要求: 智力%d 人脉%d 财富%d\n\n当前属性: 智力%d 人脉%d 财富%d",
                    career.careerName,
                    career.description,
                    career.minIntelligence, career.minConnections, career.minWealth,
                    state.getIntelligence(), state.getConnections(), state.getWealth()
            );
            button.setToolTipText(tooltip);

            // 添加点击事件监听器
            button.addActionListener(e -> {
                chooseCareer(career);
            });

        } else {
            button.setBackground(new Color(255, 200, 200)); // 红色背景表示不可选
            button.setEnabled(false);
            button.setToolTipText("此职业暂时不可选择");
        }

        return button;
    }

    /**
     * 选择职业
     */
    private void chooseCareer(BehaviorService.CareerData career) {
        // 创建详细的确认消息
        String confirmMessage = String.format(
                "确定选择职业: %s？\n\n" +
                        "职业描述: %s\n\n" +
                        "这将开始你的职业生涯发展，确定要选择这个职业吗？",
                career.careerName,
                career.description
        );

        int confirm = JOptionPane.showConfirmDialog(
                ui.getFrame(),
                confirmMessage,
                "确认职业选择",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // 设置职业
            state.setCurrentCareer(career.careerName);

            // 添加到历史记录
            state.addBehaviorHistory("职业选择: " + career.careerName);

            // 显示成功消息
            JOptionPane.showMessageDialog(
                    ui.getFrame(),
                    String.format(
                            "职业选择成功！\n\n" +
                                    "你已成为: %s\n" +
                                    "开始你的职业生涯发展！\n\n" +
                                    "现在你将进入26-35岁的职业发展阶段。",
                            career.careerName
                    ),
                    "职业选择成功",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // 确保进度设置为26-35岁阶段
            state.setProgress(7);

            // 进入26-35岁发展阶段
            ui.showAgePanel();
        }
    }
}