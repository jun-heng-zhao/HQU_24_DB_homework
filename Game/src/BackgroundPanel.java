import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * BackgroundPanel: 对应原 showBackgroundPanel + free/random background logic。
 * 修改：角色数据在左上角，适应新布局
 */
public class BackgroundPanel {
    private final AppUI ui;
    private final Database db;
    private final BehaviorService behaviorService;
    private final GameState state;

    public BackgroundPanel(AppUI ui, Database db, BehaviorService behaviorService, GameState state) {
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
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel title = new JLabel("第二步：选择家庭背景");
        title.setFont(new Font("微软雅黑", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(title, gbc);

        JButton freeBtn = new JButton("自由选择背景");
        freeBtn.setPreferredSize(new Dimension(220, 60));
        freeBtn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        freeBtn.addActionListener(e -> freeChooseBackground());
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        contentPanel.add(freeBtn, gbc);

        JButton randomBtn = new JButton("随机选择背景");
        randomBtn.setPreferredSize(new Dimension(220, 60));
        randomBtn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        randomBtn.addActionListener(e -> randomChooseBackground());
        gbc.gridx = 1;
        contentPanel.add(randomBtn, gbc);

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

    private void freeChooseBackground() {
        try {
            ResultSet rs = behaviorService.getBackgroundResultSet();
            DefaultListModel<String> listModel = behaviorService.loadBackgroundListModel();
            JList<String> bgList = new JList<>(listModel);
            bgList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            bgList.setVisibleRowCount(10);
            bgList.setFont(new Font("微软雅黑", Font.PLAIN, 14));

            int choice = JOptionPane.showOptionDialog(
                    ui.getFrame(), new JScrollPane(bgList), "选择家庭背景",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null
            );

            if (choice == JOptionPane.OK_OPTION && bgList.getSelectedIndex() != -1) {
                rs.absolute(bgList.getSelectedIndex() + 1);
                String backgroundName = rs.getString("template_name");
                state.setCurrentBackground(backgroundName);
                state.addConnections(rs.getInt("connections"));
                state.addIntelligence(rs.getInt("intelligence"));
                state.addPhysique(rs.getInt("physique"));
                state.addWealth(rs.getInt("wealth"));
                state.addHealth(rs.getInt("health"));
                rs.close();

                // 添加选择记录到历史
                state.addBehaviorHistory("家庭背景: " + backgroundName);

                ui.showAgePanel(); // 修改：直接进入年龄阶段
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ui.getFrame(), "背景选择失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void randomChooseBackground() {
        try {
            ResultSet rs = behaviorService.getBackgroundResultSet();
            int randomIndex = behaviorService.randomIndexFromResultSet(rs);
            if (randomIndex != -1) {
                rs.absolute(randomIndex);
                String backgroundName = rs.getString("template_name");
                state.setCurrentBackground(backgroundName);
                state.addConnections(rs.getInt("connections"));
                state.addIntelligence(rs.getInt("intelligence"));
                state.addPhysique(rs.getInt("physique"));
                state.addWealth(rs.getInt("wealth"));
                state.addHealth(rs.getInt("health"));

                // 添加选择记录到历史
                state.addBehaviorHistory("家庭背景: " + backgroundName);

                JOptionPane.showMessageDialog(ui.getFrame(),
                        "随机选择了: " + backgroundName + "\n" +
                                "人脉+" + rs.getInt("connections") + " 智力+" + rs.getInt("intelligence") +
                                " 体质+" + rs.getInt("physique") + " 财富+" + rs.getInt("wealth") +
                                " 健康+" + rs.getInt("health"),
                        "随机选择结果", JOptionPane.INFORMATION_MESSAGE);
            }
            rs.close();
            ui.showAgePanel(); // 修改：直接进入年龄阶段
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ui.getFrame(), "背景选择失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}