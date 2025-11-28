import javax.swing.*;
import java.awt.*;

public class ResultPanel {
    private final AppUI ui;
    private final GameState state;

    public ResultPanel(AppUI ui, GameState state) {
        this.ui = ui;
        this.state = state;
    }

    public JPanel createPanel() {
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
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("=== 角色创建完成 ===", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 28));
        contentPanel.add(title, BorderLayout.NORTH);

        // 属性展示区
        JPanel resultAttrPanel = new JPanel(new GridLayout(5, 2, 30, 20));
        resultAttrPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        resultAttrPanel.add(new JLabel("人脉:", SwingConstants.RIGHT));
        resultAttrPanel.add(new JLabel(String.valueOf(state.getConnections()), SwingConstants.LEFT));

        resultAttrPanel.add(new JLabel("智力:", SwingConstants.RIGHT));
        resultAttrPanel.add(new JLabel(String.valueOf(state.getIntelligence()), SwingConstants.LEFT));

        resultAttrPanel.add(new JLabel("体质:", SwingConstants.RIGHT));
        resultAttrPanel.add(new JLabel(String.valueOf(state.getPhysique()), SwingConstants.LEFT));

        resultAttrPanel.add(new JLabel("财富:", SwingConstants.RIGHT));
        resultAttrPanel.add(new JLabel(String.valueOf(state.getWealth()), SwingConstants.LEFT));

        resultAttrPanel.add(new JLabel("健康:", SwingConstants.RIGHT));
        resultAttrPanel.add(new JLabel(String.valueOf(state.getHealth()), SwingConstants.LEFT));

        for (Component comp : resultAttrPanel.getComponents()) {
            ((JLabel) comp).setFont(new Font("微软雅黑", Font.PLAIN, 18));
        }

        contentPanel.add(resultAttrPanel, BorderLayout.CENTER);

        // 按钮区
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton growBtn = new JButton("进入成长阶段");
        growBtn.setPreferredSize(new Dimension(200, 50));
        growBtn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        growBtn.addActionListener(e -> ui.showAgePanel()); // 修改：使用统一的年龄面板

        JButton exitBtn = new JButton("退出游戏");
        exitBtn.setPreferredSize(new Dimension(150, 50));
        exitBtn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        exitBtn.addActionListener(e -> {
            try {
                ui.getFrame().dispose();  // 正确退出
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnPanel.add(growBtn);
        btnPanel.add(exitBtn);

        contentPanel.add(btnPanel, BorderLayout.SOUTH);

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

        // 使用更小的字体
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
}