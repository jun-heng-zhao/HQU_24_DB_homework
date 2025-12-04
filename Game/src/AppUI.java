import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AppUI {

    private final GameState state;
    private final Database db;
    private final BehaviorService behaviorService;
    private final SaveLoadService saveLoadService;

    private JFrame frame;
    private JPanel contentPanel;

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
        frame = new JFrame("人生模拟器 - 全阶段成长");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 650);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(false);
        frame.getRootPane().putClientProperty("flatlaf.useWindowDecorations", true);
        frame.setLayout(new BorderLayout());

        // 顶部按钮面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton saveBtn = new JButton("存档");
        JButton loadBtn = new JButton("读档");
        JButton deleteBtn = new JButton("删除存档");
        JButton fullBtn = new JButton("⛶");

        saveBtn.addActionListener(e -> saveLoadService.saveGame(frame));
        loadBtn.addActionListener(e -> saveLoadService.loadGame(frame));
        deleteBtn.addActionListener(e -> saveLoadService.deleteSave(frame));
        fullBtn.addActionListener(e -> toggleFullscreen());

        fullBtn.putClientProperty("JButton.buttonType", "toolBar");
        fullBtn.setFocusable(false);
        fullBtn.setPreferredSize(new Dimension(36, 28));

        topPanel.add(saveBtn);
        topPanel.add(loadBtn);
        topPanel.add(deleteBtn);
        topPanel.add(fullBtn);

        frame.add(topPanel, BorderLayout.NORTH);

        // 内容区
        contentPanel = new JPanel();
        frame.add(contentPanel, BorderLayout.CENTER);

        // 注册 F11 全屏快捷键
        frame.getRootPane().registerKeyboardAction(
                e -> toggleFullscreen(),
                KeyStroke.getKeyStroke("F11"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        frame.setVisible(true);
    }

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

    // 职业选择面板
    public void showCareerPanel() {
        try {
            refreshContent(new CareerPanel(this, db, behaviorService, state).createPanel());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "加载职业选择面板失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 统一的年龄阶段面板
    public void showAgePanel() {
        try {
            refreshContent(new AgePanel(this, db, behaviorService, state).createPanel());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "加载年龄面板失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 保持向后兼容的旧方法
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
            state.setProgress(3);
            refreshContent(new ResultPanel(this, state).createPanel());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 最终结果面板
    public void showFinalResultPanel() {
        try {
            refreshContent(new FinalResultPanel(this, state).createPanel());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "加载最终结果面板失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    public JFrame getFrame() {
        return frame;
    }
}