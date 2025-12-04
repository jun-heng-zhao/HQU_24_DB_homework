import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * BehaviorService: 负责从 DB 查询行为选项
 */
public class BehaviorService {
    private final Database db;
    private final GameState state;
    private final Random random = new Random();

    public static class BehaviorData {
        public int id;
        public String behavior;
        public int connectionsChange;
        public int intelligenceChange;
        public int physiqueChange;
        public int wealthChange;
        public int healthChange;
    }

    public static class CareerData {
        public String careerName;
        public String requiredBackground;
        public int minIntelligence;
        public int minConnections;
        public int minWealth;
        public String description;
    }

    public BehaviorService(Database db, GameState state) {
        this.db = db;
        this.state = state;
    }

    // 查询 1~5 岁（按年龄）
    public List<BehaviorData> load1to5ForAge(int age) throws SQLException {
        List<BehaviorData> out = new ArrayList<>();
        String sql = "SELECT behavior_id, behavior_text, connections_change, intelligence_change, physique_change, wealth_change, health_change " +
                "FROM age_behaviors WHERE age_group = '1_5' AND age = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, age);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BehaviorData d = new BehaviorData();
                    d.id = rs.getInt("behavior_id");
                    d.behavior = rs.getString("behavior_text");
                    d.connectionsChange = rs.getInt("connections_change");
                    d.intelligenceChange = rs.getInt("intelligence_change");
                    d.physiqueChange = rs.getInt("physique_change");
                    d.wealthChange = rs.getInt("wealth_change");
                    d.healthChange = rs.getInt("health_change");
                    out.add(d);
                }
            }
        }
        return out;
    }

    // 查询 6~15 岁（按 background_type）
    public List<BehaviorData> load6to15ForBackground(String backgroundType) throws SQLException {
        List<BehaviorData> out = new ArrayList<>();
        String sql = "SELECT behavior_id, behavior_text, connections_change, intelligence_change, physique_change, wealth_change, health_change " +
                "FROM age_behaviors WHERE age_group = '6_15' AND background_type = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, backgroundType);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BehaviorData d = new BehaviorData();
                    d.id = rs.getInt("behavior_id");
                    d.behavior = rs.getString("behavior_text");
                    d.connectionsChange = rs.getInt("connections_change");
                    d.intelligenceChange = rs.getInt("intelligence_change");
                    d.physiqueChange = rs.getInt("physique_change");
                    d.wealthChange = rs.getInt("wealth_change");
                    d.healthChange = rs.getInt("health_change");
                    out.add(d);
                }
            }
        }
        return out;
    }

    // 查询 16~25 岁（按 background_type）
    public List<BehaviorData> load16to25ForBackground(String backgroundType) throws SQLException {
        List<BehaviorData> out = new ArrayList<>();
        String sql = "SELECT behavior_id, behavior_text, connections_change, intelligence_change, physique_change, wealth_change, health_change " +
                "FROM age_behaviors WHERE age_group = '16_25' AND background_type = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, backgroundType);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BehaviorData d = new BehaviorData();
                    d.id = rs.getInt("behavior_id");
                    d.behavior = rs.getString("behavior_text");
                    d.connectionsChange = rs.getInt("connections_change");
                    d.intelligenceChange = rs.getInt("intelligence_change");
                    d.physiqueChange = rs.getInt("physique_change");
                    d.wealthChange = rs.getInt("wealth_change");
                    d.healthChange = rs.getInt("health_change");
                    out.add(d);
                }
            }
        }
        return out;
    }

    // 查询 26~35 岁（按 career_type）
    public List<BehaviorData> load26to35ForCareer(String careerType) throws SQLException {
        List<BehaviorData> out = new ArrayList<>();
        String sql = "SELECT behavior_id, behavior_text, connections_change, intelligence_change, physique_change, wealth_change, health_change " +
                "FROM age_behaviors WHERE age_group = '26_35' AND background_type = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, careerType);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BehaviorData d = new BehaviorData();
                    d.id = rs.getInt("behavior_id");
                    d.behavior = rs.getString("behavior_text");
                    d.connectionsChange = rs.getInt("connections_change");
                    d.intelligenceChange = rs.getInt("intelligence_change");
                    d.physiqueChange = rs.getInt("physique_change");
                    d.wealthChange = rs.getInt("wealth_change");
                    d.healthChange = rs.getInt("health_change");
                    out.add(d);
                }
            }
        }
        return out;
    }

    // 查询 36~45 岁（按 career_type）
    public List<BehaviorData> load36to45ForCareer(String careerType) throws SQLException {
        List<BehaviorData> out = new ArrayList<>();
        String sql = "SELECT behavior_id, behavior_text, connections_change, intelligence_change, physique_change, wealth_change, health_change " +
                "FROM age_behaviors WHERE age_group = '36_45' AND background_type = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, careerType);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BehaviorData d = new BehaviorData();
                    d.id = rs.getInt("behavior_id");
                    d.behavior = rs.getString("behavior_text");
                    d.connectionsChange = rs.getInt("connections_change");
                    d.intelligenceChange = rs.getInt("intelligence_change");
                    d.physiqueChange = rs.getInt("physique_change");
                    d.wealthChange = rs.getInt("wealth_change");
                    d.healthChange = rs.getInt("health_change");
                    out.add(d);
                }
            }
        }
        return out;
    }

    // 查询 46~55 岁（按 career_type）
    public List<BehaviorData> load46to55ForCareer(String careerType) throws SQLException {
        List<BehaviorData> out = new ArrayList<>();
        String sql = "SELECT behavior_id, behavior_text, connections_change, intelligence_change, physique_change, wealth_change, health_change " +
                "FROM age_behaviors WHERE age_group = '46_55' AND background_type = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, careerType);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BehaviorData d = new BehaviorData();
                    d.id = rs.getInt("behavior_id");
                    d.behavior = rs.getString("behavior_text");
                    d.connectionsChange = rs.getInt("connections_change");
                    d.intelligenceChange = rs.getInt("intelligence_change");
                    d.physiqueChange = rs.getInt("physique_change");
                    d.wealthChange = rs.getInt("wealth_change");
                    d.healthChange = rs.getInt("health_change");
                    out.add(d);
                }
            }
        }
        return out;
    }

    // 查询 56~65 岁（按 career_type，如无特定职业行为则使用通用行为）
    public List<BehaviorData> load56to65ForCareer(String careerType) throws SQLException {
        List<BehaviorData> out = new ArrayList<>();

        // 先查询特定职业的行为
        String sql = "SELECT behavior_id, behavior_text, connections_change, intelligence_change, physique_change, wealth_change, health_change " +
                "FROM age_behaviors WHERE age_group = '56_65' AND background_type = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, careerType);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BehaviorData d = new BehaviorData();
                    d.id = rs.getInt("behavior_id");
                    d.behavior = rs.getString("behavior_text");
                    d.connectionsChange = rs.getInt("connections_change");
                    d.intelligenceChange = rs.getInt("intelligence_change");
                    d.physiqueChange = rs.getInt("physique_change");
                    d.wealthChange = rs.getInt("wealth_change");
                    d.healthChange = rs.getInt("health_change");
                    out.add(d);
                }
            }
        }

        // 如果没有找到特定职业行为，使用通用行为
        if (out.isEmpty()) {
            sql = "SELECT behavior_id, behavior_text, connections_change, intelligence_change, physique_change, wealth_change, health_change " +
                    "FROM age_behaviors WHERE age_group = '56_65' AND background_type = '通用'";
            try (Statement stmt = db.getConnection().createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    BehaviorData d = new BehaviorData();
                    d.id = rs.getInt("behavior_id");
                    d.behavior = rs.getString("behavior_text");
                    d.connectionsChange = rs.getInt("connections_change");
                    d.intelligenceChange = rs.getInt("intelligence_change");
                    d.physiqueChange = rs.getInt("physique_change");
                    d.wealthChange = rs.getInt("wealth_change");
                    d.healthChange = rs.getInt("health_change");
                    out.add(d);
                }
            }
        }

        return out;
    }

    // 查询 66岁及以上（使用通用行为）
    public List<BehaviorData> load66Plus() throws SQLException {
        List<BehaviorData> out = new ArrayList<>();
        String sql = "SELECT behavior_id, behavior_text, connections_change, intelligence_change, physique_change, wealth_change, health_change " +
                "FROM age_behaviors WHERE age_group = '66_plus'";
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                BehaviorData d = new BehaviorData();
                d.id = rs.getInt("behavior_id");
                d.behavior = rs.getString("behavior_text");
                d.connectionsChange = rs.getInt("connections_change");
                d.intelligenceChange = rs.getInt("intelligence_change");
                d.physiqueChange = rs.getInt("physique_change");
                d.wealthChange = rs.getInt("wealth_change");
                d.healthChange = rs.getInt("health_change");
                out.add(d);
            }
        }
        return out;
    }

    // 获取可选择的职业列表
    public List<CareerData> loadAvailableCareers() throws SQLException {
        List<CareerData> careers = new ArrayList<>();
        String sql = "SELECT career_name, required_background, min_intelligence, min_connections, min_wealth, description FROM career_paths";

        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                CareerData career = new CareerData();
                career.careerName = rs.getString("career_name");
                career.requiredBackground = rs.getString("required_background");
                career.minIntelligence = rs.getInt("min_intelligence");
                career.minConnections = rs.getInt("min_connections");
                career.minWealth = rs.getInt("min_wealth");
                career.description = rs.getString("description");
                careers.add(career);
            }
        }
        return careers;
    }

    // 以下方法用于省份 / 背景的自由/随机选择
    public DefaultListModel<String> loadProvincesListModel() throws SQLException {
        DefaultListModel<String> model = new DefaultListModel<>();
        String sql = "SELECT template_name, connections, intelligence, physique, wealth, health FROM attribute_templates WHERE template_type = 'PROVINCE'";
        try (Statement stmt = db.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(sql)) {
            int index = 1;
            while (rs.next()) {
                model.addElement(String.format("%d. %s (人脉:%d 智力:%d 体质:%d 财富:%d 健康:%d)",
                        index++, rs.getString("template_name"), rs.getInt("connections"),
                        rs.getInt("intelligence"), rs.getInt("physique"), rs.getInt("wealth"), rs.getInt("health")));
            }
        }
        return model;
    }

    public ResultSet getProvincesResultSet() throws SQLException {
        String sql = "SELECT template_name, connections, intelligence, physique, wealth, health FROM attribute_templates WHERE template_type = 'PROVINCE'";
        Statement stmt = db.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stmt.executeQuery(sql);
    }

    public DefaultListModel<String> loadBackgroundListModel() throws SQLException {
        DefaultListModel<String> model = new DefaultListModel<>();
        String sql = "SELECT template_name, connections, intelligence, physique, wealth, health FROM attribute_templates WHERE template_type = 'BACKGROUND'";
        try (Statement stmt = db.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(sql)) {
            int idx = 1;
            while (rs.next()) {
                model.addElement(String.format("%d. %s (人脉:%d 智力:%d 体质:%d 财富:%d 健康:%d)",
                        idx++, rs.getString("template_name"), rs.getInt("connections"),
                        rs.getInt("intelligence"), rs.getInt("physique"), rs.getInt("wealth"), rs.getInt("health")));
            }
        }
        return model;
    }

    public ResultSet getBackgroundResultSet() throws SQLException {
        String sql = "SELECT template_name, connections, intelligence, physique, wealth, health FROM attribute_templates WHERE template_type = 'BACKGROUND'";
        Statement stmt = db.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stmt.executeQuery(sql);
    }

    // 随机选择（用于随机省份 / 背景）
    public int randomIndexFromResultSet(ResultSet rs) throws SQLException {
        rs.last();
        int count = rs.getRow();
        if (count <= 0) return -1;
        return random.nextInt(count) + 1;
    }

}