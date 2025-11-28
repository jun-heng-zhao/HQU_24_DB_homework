import java.util.ArrayList;
import java.util.List;

public class GameState {

    // 玩家属性
    private int connections = 0;
    private int intelligence = 0;
    private int physique = 0;
    private int wealth = 0;
    private int health = 0;

    // 进度与年龄、背景
    private int progress = 1;
    private int currentAge = 1;
    private String currentBackground;
    private String currentCareer; // 新增：当前职业

    // 历史行为记录
    private List<String> behaviorHistory = new ArrayList<>();

    // ======== Getter / Setter ========

    public int getConnections() { return connections; }
    public void setConnections(int v) { this.connections = v; }
    public void addConnections(int v) { this.connections += v; }

    public int getIntelligence() { return intelligence; }
    public void setIntelligence(int v) { this.intelligence = v; }
    public void addIntelligence(int v) { this.intelligence += v; }

    public int getPhysique() { return physique; }
    public void setPhysique(int v) { this.physique = v; }
    public void addPhysique(int v) { this.physique += v; }

    public int getWealth() { return wealth; }
    public void setWealth(int v) { this.wealth = v; }
    public void addWealth(int v) { this.wealth += v; }

    public int getHealth() { return health; }
    public void setHealth(int v) { this.health = v; }
    public void addHealth(int v) { this.health += v; }

    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }

    public int getCurrentAge() { return currentAge; }
    public void setCurrentAge(int currentAge) { this.currentAge = currentAge; }

    public String getCurrentBackground() { return currentBackground; }
    public void setCurrentBackground(String currentBackground) { this.currentBackground = currentBackground; }

    public String getCurrentCareer() { return currentCareer; }
    public void setCurrentCareer(String currentCareer) { this.currentCareer = currentCareer; }

    // ======== 历史行为记录 ========
    public List<String> getBehaviorHistory() { return behaviorHistory; }
    public void addBehaviorHistory(String behavior) { this.behaviorHistory.add(0, behavior); }
    public void setBehaviorHistory(List<String> history) { this.behaviorHistory = history; }

    // ======== 一键加属性 ========
    public void increaseAttributes(int connChange, int intelChange, int phyChange, int wealthChange, int healthChange) {
        this.connections += connChange;
        this.intelligence += intelChange;
        this.physique += phyChange;
        this.wealth += wealthChange;
        this.health += healthChange;
    }

    // ======== 检查职业资格 ========
    public boolean canChooseCareer(String requiredBackground, int minIntelligence, int minConnections, int minWealth) {
        // 检查背景匹配
        if (requiredBackground != null && !requiredBackground.contains(this.currentBackground)) {
            return false;
        }

        // 检查属性要求
        return this.intelligence >= minIntelligence &&
                this.connections >= minConnections &&
                this.wealth >= minWealth;
    }
}