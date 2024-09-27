package ServerData.Boss.ListBoss.DHVT;

import Server.Data.Consts.ConstPlayer;
import Server.Data.Consts.ConstRatio;
import ServerData.Boss.BossData;
import ServerData.Boss.BossStatus;
import static ServerData.Boss.BossStatus.ACTIVE;
import static ServerData.Boss.BossStatus.JOIN_MAP;
import static ServerData.Boss.BossStatus.RESPAWN;
import ServerData.Boss.BossesData;
import ServerData.Models.PVP.Matches.MartialCongressService;
import ServerData.Models.Player.Player;
import ServerData.Services.EffectSkillService;
import ServerData.Services.PlayerService;
import ServerData.Models.Player.PlayerSkill.SkillService;
import ServerData.Utils.SkillUtil;
import ServerData.Utils.Util;

/**
 * @author TTS  
 */
public class ThienXinHangClone extends BossDHVT {
    private int timeLive;
    private long lastUpdate = System.currentTimeMillis();


    public ThienXinHangClone(byte id, Player player) throws Exception {
        super(id, BossesData.THIEN_XIN_HANG_CLONE);
        this.playerAtt = player;
        timeLive = 10;
//        this.bossStatus = BossStatus.JOIN_MAP;
//        this.bossStatus = BossStatus.ACTIVE;
//        this.typePk = 3;
//        this.nPoint.khangTDHS = true;
//        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_PVP);
//        MartialCongressService.gI().sendTypePK(playerAtt, this);
        this.timeJoinMap = System.currentTimeMillis() + 10000;
    }

    @Override
    public void attack() {
        try {
            if (playerAtt.location != null && playerAtt != null && playerAtt.zone != null && this.zone != null && this.zone.equals(playerAtt.zone)) {
                if (this.isDie()) {
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                if (Util.getDistance(this, playerAtt) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                        goToXY(playerAtt.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)), Util.nextInt(10) % 2 == 0 ? playerAtt.location.y : playerAtt.location.y - Util.nextInt(0, 50), false);
                    }
//                        System.err.println("attack player: " + playerAtt.name + "use skill: " + SkillService.gI().useSkill(this, playerAtt, null));
                    SkillService.gI().useSkill(this, playerAtt, null,null);
                    checkPlayerDie(playerAtt);
                } else {
                    goToPlayer(playerAtt, false);
                }
            } else {
                this.leaveMap();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update() {
//        super.update();
        try {
            EffectSkillService.gI().removeStun(this);
            switch (this.bossStatus) {
                case RESPAWN:
                    this.respawn();
                    this.changeStatus(BossStatus.JOIN_MAP);
                case JOIN_MAP:
                    joinMap();
                    if (this.zone != null) {
                        changeStatus(BossStatus.ACTIVE);
                        timeJoinMap = System.currentTimeMillis();
                        this.immortalMp();
                        this.typePk = 3;
                        MartialCongressService.gI().sendTypePK(playerAtt, this);
                        PlayerService.gI().changeAndSendTypePK(playerAtt, ConstPlayer.PK_PVP);
                        this.changeStatus(BossStatus.ACTIVE);
                    }
                    break;
                case ACTIVE:
                    if (this.playerSkill.prepareTuSat || this.playerSkill.prepareLaze || this.playerSkill.prepareQCKK) {
                        break;
                    } else {
                        this.attack();
                    }
                    break;
            }
            if (Util.canDoWithTime(lastUpdate, 1000)) {
                lastUpdate = System.currentTimeMillis();
                if (timeLive > 0) {
                    timeLive--;
                } else {
                    super.leaveMap();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
