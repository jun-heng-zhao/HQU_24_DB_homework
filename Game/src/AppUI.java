import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class AppUI {

    private final GameState state;
    private final Database db;
    private final BehaviorService behaviorService;
    private final SaveLoadService saveLoadService;

    private JFrame frame;
    private JPanel contentPanel;

    // 移除右侧属性栏的标签，因为现在属性显示在左上角

    private boolean fullscreen = false;
    private Rectangle windowBounds;

    public AppUI(GameState state, Database db, BehaviorService behaviorService, SaveLoadService saveLoadService) {
        this.state = state;
        this.db = db;
        this.behaviorService = behaviorService;
        this.saveLoadService = saveLoadService;

        SwingUtilities.invokeLater(this::initGUI);
    }

    private void initGUI() {

        // =============================
        // 基础 Frame 设置（启用 FlatLaf 自定义标题栏）
        // =============================
        frame = new JFrame("人生模拟器 - 全阶段成长");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 650);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(false);   // 使用 FlatLaf 自绘标题栏
        frame.getRootPane().putClientProperty("flatlaf.useWindowDecorations", true);
        frame.setLayout(new BorderLayout());

        // =============================
        // 顶部按钮（存档、读档 + 删除存档 + 全屏按钮）
        // =============================
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton saveBtn = new JButton("存档");
        JButton loadBtn = new JButton("读档");
        JButton deleteBtn = new JButton("删除存档");  // 新增删除存档按钮
        JButton fullBtn = new JButton("⛶");

        saveBtn.addActionListener(e -> saveLoadService.saveGame(frame));
        loadBtn.addActionListener(e -> saveLoadService.loadGame(frame));
        deleteBtn.addActionListener(e -> saveLoadService.deleteSave(frame));  // 绑定删除存档功能
        fullBtn.addActionListener(e -> toggleFullscreen());

        // 全屏按钮样式
        fullBtn.putClientProperty("JButton.buttonType", "toolBar");
        fullBtn.setFocusable(false);
        fullBtn.setPreferredSize(new Dimension(36, 28));

        topPanel.add(saveBtn);
        topPanel.add(loadBtn);
        topPanel.add(deleteBtn);  // 添加删除存档按钮
        topPanel.add(fullBtn);

        frame.add(topPanel, BorderLayout.NORTH);

        // =============================
        // 内容区 - 移除了右侧属性栏
        // =============================
        contentPanel = new JPanel();
        frame.add(contentPanel, BorderLayout.CENTER);

        // =============================
        // 注册 F11 作为全屏快捷键
        // =============================
        frame.getRootPane().registerKeyboardAction(
                e -> toggleFullscreen(),
                KeyStroke.getKeyStroke("F11"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        frame.setVisible(true);
    }

    // =============================
    // 全屏/退出全屏 逻辑
    // =============================
    private void toggleFullscreen() {
        if (!fullscreen) {
            windowBounds = frame.getBounds();
            frame.dispose();
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        } else {
            frame.dispose();
            frame.setUndecorated(false);
            frame.setBounds(windowBounds);
            frame.setVisible(true);
        }
        fullscreen = !fullscreen;
    }

    // =============================
    // 内容区更新
    // =============================
    private void refreshContent(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // =============================
    // 面板切换函数
    // =============================

    public void showBirthplacePanel() {
        try {
            state.setProgress(1);
            refreshContent(new BirthplacePanel(this, db, behaviorService, state).createPanel());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showBackgroundPanel() {
        try {
            state.setProgress(2);
            refreshContent(new BackgroundPanel(this, db, behaviorService, state).createPanel());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 统一的年龄阶段面板
    public void showAgePanel() {
        try {
            refreshContent(new AgePanel(this, db, behaviorService, state).createPanel());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 保持向后兼容的旧方法（可以删除，但为了安全暂时保留）
    @Deprecated
    public void showChildhoodPanel() {
        showAgePanel();
    }

    @Deprecated
    public void showAdolescencePanel() {
        showAgePanel();
    }

    @Deprecated
    public void showYouthPanel() {
        showAgePanel();
    }

    public void showResultPanel() {
        try {
            state.setProgress(7); // 设置为完成状态
            refreshContent(new ResultPanel(this, state).createPanel());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JFrame getFrame() {
        return frame;
    }
}