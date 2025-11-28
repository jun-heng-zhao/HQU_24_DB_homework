import javax.swing.*;
import java.sql.*;
import java.awt.*;

/**
 * SaveLoadService: 负责保存与加载存档。
 */
public class SaveLoadService {
    private final Database db;
    private final GameState state;
    private AppUI ui;

    public SaveLoadService(Database db, GameState state) {
        this.db = db;
        this.state = state;
    }

    public void setUIManager(AppUI ui) {
        this.ui = ui;
    }

    // ======================
    // 保存游戏（更新以保存行为历史和职业信息）
    // ======================
    public void saveGame(JFrame frame) {

        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "是否保存当前游戏进度？",
                "确认保存",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(frame, "已取消保存。", "提示", JOptionPane.INFORMATION_MESSAGE);
            return; // ❗拒绝保存 → 直接退出
        }

        String saveName = JOptionPane.showInputDialog(frame, "请输入存档名称：", "创建存档", JOptionPane.PLAIN_MESSAGE);

        if (saveName == null || saveName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "存档已取消（名称为空）。", "提示", JOptionPane.WARNING_MESSAGE);
            return; // ❗名称为空也不能保存
        }

        try {
            // 将行为历史转换为字符串（用特殊分隔符）
            String behaviorHistoryStr = String.join(";;", state.getBehaviorHistory());

            String insertSql = """
                    INSERT INTO player_saves 
                    (save_name, save_time, progress, current_age, connections, intelligence, physique, wealth, health, background_type, career_type, behavior_history) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            try (PreparedStatement pstmt = db.getConnection().prepareStatement(insertSql)) {
                pstmt.setString(1, saveName);
                pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                pstmt.setInt(3, state.getProgress());
                pstmt.setInt(4, state.getCurrentAge());
                pstmt.setInt(5, state.getConnections());
                pstmt.setInt(6, state.getIntelligence());
                pstmt.setInt(7, state.getPhysique());
                pstmt.setInt(8, state.getWealth());
                pstmt.setInt(9, state.getHealth());
                pstmt.setString(10, state.getCurrentBackground());
                pstmt.setString(11, state.getCurrentCareer()); // 新增职业字段
                pstmt.setString(12, behaviorHistoryStr);
                pstmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(frame, "存档成功！\n存档名称：" + saveName, "成功", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "存档失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ======================
    // 读档（更新以加载行为历史和职业信息）
    // ======================
    public void loadGame(JFrame frame) {
        try (Statement stmt = db.getConnection().createStatement()) {
            String querySavesSql = "SELECT save_name, save_time, progress, current_age, background_type, career_type FROM player_saves ORDER BY save_time DESC";

            try (ResultSet rs = stmt.executeQuery(querySavesSql)) {

                DefaultListModel<String> saveListModel = new DefaultListModel<>();
                while (rs.next()) {
                    String desc = switch (rs.getInt("progress")) {
                        case 1 -> "未选择出生地";
                        case 2 -> "已选择出生地";
                        case 3 -> "角色创建完成";
                        case 4 -> "1~5岁（" + rs.getInt("current_age") + "岁）";
                        case 5 -> "6~15岁（" + rs.getInt("current_age") + "岁）";
                        case 6 -> "16~25岁（" + rs.getInt("current_age") + "岁）";
                        case 7 -> "26~35岁（" + rs.getInt("current_age") + "岁）" +
                                (rs.getString("career_type") != null ? " [" + rs.getString("career_type") + "]" : "");
                        case 8 -> "游戏完成";
                        default -> "未知进度";
                    };

                    String info = String.format("存档名称：%s | 时间：%s | 进度：%s",
                            rs.getString("save_name"),
                            rs.getTimestamp("save_time").toString().substring(0, 19),
                            desc
                    );

                    saveListModel.addElement(info);
                }

                if (saveListModel.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "暂无存档！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                JList<String> saveList = new JList<>(saveListModel);
                saveList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                int choice = JOptionPane.showOptionDialog(
                        frame, new JScrollPane(saveList), "选择存档",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, null, null
                );

                if (choice != JOptionPane.OK_OPTION || saveList.getSelectedIndex() == -1)
                    return;

                String selected = saveList.getSelectedValue();
                String selectedName = selected.split("存档名称：")[1].split(" \\|")[0].trim();

                // ======================
                // ✔ 读取存档 → 用 set() 方法覆盖
                // ======================
                String loadSql = """
                        SELECT progress, current_age, connections, intelligence, physique, wealth, health, background_type, career_type, behavior_history
                        FROM player_saves WHERE save_name = ?
                        """;

                try (PreparedStatement pstmt = db.getConnection().prepareStatement(loadSql)) {
                    pstmt.setString(1, selectedName);

                    try (ResultSet loadRs = pstmt.executeQuery()) {
                        if (loadRs.next()) {

                            state.setProgress(loadRs.getInt("progress"));
                            state.setCurrentAge(loadRs.getInt("current_age"));

                            // === ❗覆盖，而不是累加 ===
                            state.setConnections(loadRs.getInt("connections"));
                            state.setIntelligence(loadRs.getInt("intelligence"));
                            state.setPhysique(loadRs.getInt("physique"));
                            state.setWealth(loadRs.getInt("wealth"));
                            state.setHealth(loadRs.getInt("health"));

                            state.setCurrentBackground(loadRs.getString("background_type"));
                            state.setCurrentCareer(loadRs.getString("career_type")); // 加载职业信息

                            // 加载行为历史
                            String behaviorHistoryStr = loadRs.getString("behavior_history");
                            if (behaviorHistoryStr != null && !behaviorHistoryStr.isEmpty()) {
                                String[] historyArray = behaviorHistoryStr.split(";;");
                                state.setBehaviorHistory(new java.util.ArrayList<>(java.util.Arrays.asList(historyArray)));
                            } else {
                                state.setBehaviorHistory(new java.util.ArrayList<>());
                            }

                            // ======================
                            // 跳转 UI - 更新以支持新的进度阶段
                            // ======================
                            if (ui != null) {
                                switch (state.getProgress()) {
                                    case 1 -> ui.showBirthplacePanel();
                                    case 2 -> ui.showBackgroundPanel();
                                    case 3 -> ui.showResultPanel();
                                    case 4, 5, 6, 7 -> ui.showAgePanel(); // 所有年龄阶段使用统一面板
                                    case 8 -> ui.showResultPanel();
                                }
                            }

                            JOptionPane.showMessageDialog(frame, "读档成功：" + selectedName, "成功", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "读档失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteSave(JFrame frame) {

        try (Statement stmt = db.getConnection().createStatement()) {
            String querySql = "SELECT save_name, save_time FROM player_saves ORDER BY save_time DESC";

            try (ResultSet rs = stmt.executeQuery(querySql)) {

                DefaultListModel<String> model = new DefaultListModel<>();

                while (rs.next()) {
                    String item = "存档名称：" + rs.getString("save_name")
                            + " | 时间：" + rs.getTimestamp("save_time").toString().substring(0, 19);
                    model.addElement(item);
                }

                if (model.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "没有可删除的存档。", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 存档列表
                JList<String> list = new JList<>(model);
                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                list.setFont(new Font("微软雅黑", Font.PLAIN, 12));

                int choice = JOptionPane.showOptionDialog(
                        frame,
                        new JScrollPane(list),
                        "选择要删除的存档",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null
                );

                if (choice != JOptionPane.OK_OPTION || list.getSelectedIndex() == -1) {
                    return; // 用户取消
                }

                // 提取存档名
                String selected = list.getSelectedValue();
                String saveName = selected.split("存档名称：")[1].split(" \\|")[0].trim();

                // 二次确认
                int confirm = JOptionPane.showConfirmDialog(
                        frame,
                        "确定要删除存档 \"" + saveName + "\" 吗？\n此操作不可撤销！",
                        "确认删除",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm != JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(frame, "操作已取消。");
                    return;
                }

                // 执行删除
                String deleteSql = "DELETE FROM player_saves WHERE save_name = ?";
                try (PreparedStatement pstmt = db.getConnection().prepareStatement(deleteSql)) {
                    pstmt.setString(1, saveName);
                    pstmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(frame,
                        "存档 \"" + saveName + "\" 已成功删除！",
                        "删除成功",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                    "删除存档失败：" + e.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ======================
    // 新增：检查存档名称是否重复
    // ======================
    public boolean isSaveNameExists(String saveName) {
        try {
            String checkSql = "SELECT COUNT(*) FROM player_saves WHERE save_name = ?";
            try (PreparedStatement pstmt = db.getConnection().prepareStatement(checkSql)) {
                pstmt.setString(1, saveName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======================
    // 新增：获取所有存档列表（用于其他功能）
    // ======================
    public DefaultListModel<String> getAllSaves() {
        DefaultListModel<String> model = new DefaultListModel<>();
        try (Statement stmt = db.getConnection().createStatement()) {
            String querySql = "SELECT save_name, save_time, progress FROM player_saves ORDER BY save_time DESC";
            try (ResultSet rs = stmt.executeQuery(querySql)) {
                while (rs.next()) {
                    String item = String.format("%s (创建于: %s)",
                            rs.getString("save_name"),
                            rs.getTimestamp("save_time").toString().substring(0, 16));
                    model.addElement(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    // ======================
    // 新增：快速保存（使用默认名称）
    // ======================
    public void quickSave(JFrame frame) {
        String defaultName = "快速存档_" + new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());

        try {
            // 将行为历史转换为字符串
            String behaviorHistoryStr = String.join(";;", state.getBehaviorHistory());

            String insertSql = """
                    INSERT INTO player_saves 
                    (save_name, save_time, progress, current_age, connections, intelligence, physique, wealth, health, background_type, career_type, behavior_history) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            try (PreparedStatement pstmt = db.getConnection().prepareStatement(insertSql)) {
                pstmt.setString(1, defaultName);
                pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                pstmt.setInt(3, state.getProgress());
                pstmt.setInt(4, state.getCurrentAge());
                pstmt.setInt(5, state.getConnections());
                pstmt.setInt(6, state.getIntelligence());
                pstmt.setInt(7, state.getPhysique());
                pstmt.setInt(8, state.getWealth());
                pstmt.setInt(9, state.getHealth());
                pstmt.setString(10, state.getCurrentBackground());
                pstmt.setString(11, state.getCurrentCareer());
                pstmt.setString(12, behaviorHistoryStr);
                pstmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(frame, "快速存档成功！\n存档名称：" + defaultName, "成功", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "快速存档失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}