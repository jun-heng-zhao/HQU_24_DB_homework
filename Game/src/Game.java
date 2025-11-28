import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.sql.SQLException;

public class Game {
    public static void main(String[] args) {

        // 启用 FlatLaf
        FlatLightLaf.setup();

        // 让 FlatLaf 管理标题栏（自定义按钮）
        UIManager.put("TitlePane.useWindowDecorations", true);
        JFrame.setDefaultLookAndFeelDecorated(true);

        try {
            Database db = new Database();
            db.connectDatabase();

            GameState state = new GameState();
            BehaviorService behaviorService = new BehaviorService(db, state);
            SaveLoadService saveLoadService = new SaveLoadService(db, state);

            AppUI ui = new AppUI(state, db, behaviorService, saveLoadService);
            saveLoadService.setUIManager(ui);

            SwingUtilities.invokeLater(ui::showBirthplacePanel);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null, "数据库连接失败: " + ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
