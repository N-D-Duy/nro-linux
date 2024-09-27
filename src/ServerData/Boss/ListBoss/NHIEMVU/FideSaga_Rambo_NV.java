package ServerData.Boss.ListBoss.NHIEMVU;

import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossStatus;
import ServerData.Boss.BossesData;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Models.Player.PlayerSkill.Skill;
import ServerData.Services.PetService;
import ServerData.Services.Service;
import ServerData.Services.TaskService;
import ServerData.Utils.Util;
import java.util.Calendar;

public class FideSaga_Rambo_NV extends Boss {

    public FideSaga_Rambo_NV() throws Exception {
        super(BossID.RAMBO, BossesData.RAMBO);
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
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.TU_SAT:
                    case Skill.QUA_CAU_KENH_KHI:
                    case Skill.MAKANKOSAPPO:
                        Service.getInstance().sendThongBao(plAtt, "Boss miễn nhiễm sát thương với skill này!");
                        break;
                }
            }
            int timenv = Calendar.HOUR_OF_DAY;
            if (timenv >= 12 && timenv < 13 || timenv >= 18 && timenv < 19) {
                if (plAtt != null && plAtt.playerTask.taskMain.id != 19) {
                    damage = 0;
                    Service.getInstance().sendThongBao(plAtt, "Đang trong giờ hỗ trợ nhiệm vụ, boss miễn nhiễm với sát thương từ bạn!");
                }
            }
            dame = (long) super.injured(plAtt, damage, piercing, isMobAttack);
            return dame;
        }
    }
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
