package ServerData.Boss.ListBoss.NHIEMVU;

import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossesData;
import ServerData.Models.Player.Player;
import ServerData.Models.Player.PlayerSkill.Skill;
import ServerData.Services.Service;
import ServerData.Utils.Util;
import java.util.Calendar;


public class FideSaga_TDST_NV extends Boss {

    public FideSaga_TDST_NV() throws Exception {
        super(BossID.TDST, BossesData.SO_4, BossesData.SO_3, BossesData.SO_2, BossesData.SO_1, BossesData.TIEU_DOI_TRUONG);
    }

    @Override
    public void moveTo(int x, int y) {
        if (this.currentLevel == 1) {
            return;
        }
        super.moveTo(x, y);
    }

    @Override
    public void reward(Player plKill) {
        super.reward(plKill);
        if (this.currentLevel == 1) {
            return;
        }
    }

    @Override
    public void notifyJoinMap() {
        if (this.currentLevel == 1) {
            return;
        }
        super.notifyJoinMap();
    }
    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        long dame = 0;
        if (this.isDie()) {
            return dame;
        } else {
            if (Util.isTrue(1, 5) && plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.TU_SAT:
                    case Skill.QUA_CAU_KENH_KHI:
                    case Skill.MAKANKOSAPPO:
                        break;
                    default:
                        return 0;
                }
            }
            int timenv = Calendar.HOUR_OF_DAY;
            if (timenv >= 12 && timenv < 13 || timenv >= 18 && timenv < 19) {
                int idNhanhNv;
                switch (name) {
                    case "Số 4 Guldo":
                        idNhanhNv = 1;
                        break;
                    case "Số 3 Recome":
                        idNhanhNv = 2;
                        break;
                    case "Số 2 Jeice":
                        idNhanhNv = 3;
                        break;
                    case "Số 1 Burter":
                        idNhanhNv = 4;
                        break;
                    case "Tiểu đội trưởng Ginyu":
                        idNhanhNv = 5;
                        break;
                }
                if (plAtt != null && plAtt.playerTask.taskMain.id != 20) {
                    damage = 0;
                    Service.getInstance().sendThongBao(plAtt, "Đang trong giờ hỗ trợ nhiệm vụ, boss miễn nhiễm với sát thương từ bạn!");
                } /*else if (plAtt != null && plAtt.playerTask.taskMain.id == 20) {
                    if (plAtt.playerTask.taskMain.index != idNhanhNv) {
                        damage = 0;
                        Service.getInstance().sendThongBao(plAtt, "Bây giờ là giờ nhiệm vụ, không phải nhiệm vụ hiện tại của bạn, boss miễn nhiễm sát thương");
                    }
                } */
            }
            dame = (long) super.injured(plAtt, damage, piercing, isMobAttack);
            return dame;
        }
    }
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! 
 */
