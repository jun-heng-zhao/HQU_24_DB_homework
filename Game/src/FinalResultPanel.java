import javax.swing.*;
import java.awt.*;

/**
 * FinalResultPanel: 显示人生最终结果
 */
public class FinalResultPanel {
    private final AppUI ui;
    private final GameState state;

    public FinalResultPanel(AppUI ui, GameState state) {
        this.ui = ui;
        this.state = state;
    }

    public JPanel createPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 240, 240));

        // 标题
        JLabel title = new JLabel("=== 人生圆满 ===", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 32));
        title.setForeground(new Color(70, 130, 180));
        panel.add(title, BorderLayout.NORTH);

        // 主要内容区域
        JPanel contentPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        contentPanel.setBackground(new Color(240, 240, 240));

        // 人生总结
        JTextArea summaryArea = new JTextArea(generateLifeSummary());
        summaryArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        summaryArea.setEditable(false);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setBackground(new Color(240, 240, 240));
        summaryArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 最终属性
        JPanel statsPanel = createFinalStatsPanel();

        contentPanel.add(summaryArea);
        contentPanel.add(statsPanel);

        panel.add(contentPanel, BorderLayout.CENTER);

        // 按钮区域
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton newGameBtn = new JButton("开始新的人生");
        newGameBtn.setPreferredSize(new Dimension(180, 50));
        newGameBtn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        newGameBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    ui.getFrame(),
                    "确定要开始新的人生吗？",
                    "确认新游戏",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                // 重置游戏状态并重新开始
                resetGameState();
                ui.showBirthplacePanel();
            }
        });

        JButton exitBtn = new JButton("退出游戏");
        exitBtn.setPreferredSize(new Dimension(150, 50));
        exitBtn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        exitBtn.addActionListener(e -> {
            ui.getFrame().dispose();
        });

        buttonPanel.add(newGameBtn);
        buttonPanel.add(exitBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * 生成人生总结
     */
    private String generateLifeSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("恭喜你完成了精彩的一生！\n\n");

        // 根据最终属性评价人生
        int totalScore = state.getConnections() + state.getIntelligence() +
                state.getPhysique() + state.getWealth() + state.getHealth();

        if (totalScore >= 400) {
            summary.append("你度过了非凡的一生，在各个领域都取得了辉煌成就！\n");
        } else if (totalScore >= 300) {
            summary.append("你度过了成功的一生，在多个方面都取得了显著成就！\n");
        } else if (totalScore >= 200) {
            summary.append("你度过了充实的一生，在很多方面都有不错的收获！\n");
        } else {
            summary.append("你度过了平凡但幸福的一生！\n");
        }

        summary.append("\n人生轨迹：\n");
        summary.append("• 职业发展：").append(state.getCurrentCareer() != null ? state.getCurrentCareer() : "无").append("\n");
        summary.append("• 最终年龄：").append(state.getCurrentAge()).append("岁\n");
        summary.append("• 人生经历：").append(state.getBehaviorHistory().size()).append("个重要时刻\n");

        return summary.toString();
    }

    /**
     * 创建最终属性面板
     */
    private JPanel createFinalStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("最终人生属性"));
        panel.setBackground(new Color(220, 230, 240));

        Font statFont = new Font("微软雅黑", Font.BOLD, 16);

        // 人脉
        JLabel connLabel = new JLabel("人脉:", SwingConstants.RIGHT);
        connLabel.setFont(statFont);
        JLabel connValue = new JLabel(String.valueOf(state.getConnections()), SwingConstants.LEFT);
        connValue.setFont(statFont);

        // 智力
        JLabel intelLabel = new JLabel("智力:", SwingConstants.RIGHT);
        intelLabel.setFont(statFont);
        JLabel intelValue = new JLabel(String.valueOf(state.getIntelligence()), SwingConstants.LEFT);
        intelValue.setFont(statFont);

        // 体质
        JLabel physLabel = new JLabel("体质:", SwingConstants.RIGHT);
        physLabel.setFont(statFont);
        JLabel physValue = new JLabel(String.valueOf(state.getPhysique()), SwingConstants.LEFT);
        physValue.setFont(statFont);

        // 财富
        JLabel wealthLabel = new JLabel("财富:", SwingConstants.RIGHT);
        wealthLabel.setFont(statFont);
        JLabel wealthValue = new JLabel(String.valueOf(state.getWealth()), SwingConstants.LEFT);
        wealthValue.setFont(statFont);

        // 健康
        JLabel healthLabel = new JLabel("健康:", SwingConstants.RIGHT);
        healthLabel.setFont(statFont);
        JLabel healthValue = new JLabel(String.valueOf(state.getHealth()), SwingConstants.LEFT);
        healthValue.setFont(statFont);

        panel.add(connLabel);
        panel.add(connValue);
        panel.add(intelLabel);
        panel.add(intelValue);
        panel.add(physLabel);
        panel.add(physValue);
        panel.add(wealthLabel);
        panel.add(wealthValue);
        panel.add(healthLabel);
        panel.add(healthValue);

        return panel;
    }

    /**
     * 重置游戏状态
     */
    private void resetGameState() {
        state.setConnections(0);
        state.setIntelligence(0);
        state.setPhysique(0);
        state.setWealth(0);
        state.setHealth(0);
        state.setProgress(1);
        state.setCurrentAge(1);
        state.setCurrentBackground(null);
        state.setCurrentCareer(null);
        state.setBehaviorHistory(new java.util.ArrayList<>());
    }
}