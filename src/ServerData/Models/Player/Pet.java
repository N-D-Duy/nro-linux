package ServerData.Models.Player;

import Server.Data.Consts.ConstPlayer;
import ServerData.Boss.ListBoss.DHVT.BossDHVT;
import ServerData.Services.MapService;
import ServerData.Models.Map.Mob.Mob;
import ServerData.Models.Player.PlayerSkill.Skill;
import ServerData.Utils.SkillUtil;
import ServerData.Services.Service;
import ServerData.Utils.Util;
import com.girlkun.network.io.Message;
import ServerData.Server.Manager;
import ServerData.Services.ItemTimeService;
import ServerData.Services.PlayerService;
import ServerData.Models.Player.PlayerSkill.SkillService;
import ServerData.Services.ChangeMapService;
import ServerData.Utils.TimeUtil;

public class Pet extends Player {

    private static final short ARANGE_CAN_ATTACK = 300;
    private static final short ARANGE_ATT_SKILL1 = 100;

    private static final short[][] PET_ID = {{285, 286, 287}, {288, 289, 290}, {282, 283, 284}, {304, 305, 303}};

    public static final byte FOLLOW = 0;
    public static final byte PROTECT = 1;
    public static final byte ATTACK = 2;
    public static final byte GOHOME = 3;
    public static final byte FUSION = 4;

     public Player master;
    public byte status = 0;

    public byte typePet;
    public boolean isTransform;

    public long lastTimeDie;
 private boolean canUseSkill2;
    private boolean goingHome;
  boolean ANGRY;
    private long lastTimeAngry;
    private Mob mobAttack;
    private Player playerAttack;
    private static final int TIME_WAIT_AFTER_UNFUSION = 10000;
    private long lastTimeUnfusion;
    public long lastTimeUnfusion2;

    public byte getStatus() {
        return this.status;
    }

    public Pet(Player master) {
        this.master = master;
        this.isPet = true;
    }
    
    public byte thuctinh;
    public String getNameThuctinh(byte thuctinh) {
        switch (thuctinh) {
            case 1:
                return "[Nâng Cấp +1]";
            case 2:
                return "[Nâng Cấp +2]";
            case 3:
                return "[Nâng Cấp +3]";
            case 4:
                return "[Nâng Cấp +4]";
            case 5:
                return "[Nâng Cấp +5]";
            case 6:
                return "[Nâng Cấp +6]";
            case 7:
                return "[Nâng Cấp +7]";
            case 8:
                return "[Nâng Cấp +8]";
            case 9:
                return "[Nâng Cấp +9]";
            case 10:
                return "[Tiến Hoá]";
            case 11:
                return "[Tiến Hoá +1]";
            case 12:
                return "[Tiến Hoá +2]";
            case 13:
                return "[Tiến Hoá +3]";
            case 14:
                return "[Tiến Hoá +4]";
            case 15:
                return "[Tiến Hoá +5]";
            case 16:
                return "[Tiến Hoá +6]";
            case 17:
                return "[Tiến Hoá +7]";
            case 18:
                return "[Tiến Hoá +8]";
            case 19:
                return "[Tiến Hoá +9]";
            case 20:
                return "[Thức Tỉnh]";
            case 21:
                return "[Thức Tỉnh +1]";
            case 22:
                return "[Thức Tỉnh +2]";
            case 23:
                return "[Thức Tỉnh +3]";
            case 24:
                return "[Thức Tỉnh +4]";
            case 25:
                return "[Thức Tỉnh +5]";
            case 26:
                return "[Thức Tỉnh +6]";
            case 27:
                return "[Thức Tỉnh +7]";
            case 28:
                return "[Thức Tỉnh +8]";
            case 29:
                return "[Thức Tỉnh +9]";
            case 30:
                return "[Thần Linh]";
            default:
                return "[Chưa tiến cấp]";
        }
    }
    
    public void changeStatus(byte status) {
        if (goingHome || master.fusion.typeFusion != 0 || (this.isDie() && status == FUSION)) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        Service.getInstance().chatJustForMe(master, this, getTextStatus(status));
        if (status == GOHOME) {
            goHome();
        } else if (status == FUSION) {
            fusion(false);
        }
        this.status = status;
    }

    public void joinMapMaster() {
        if (status != GOHOME && status != FUSION && !isDie()) {
            this.location.x = master.location.x + Util.nextInt(-10, 10);
            this.location.y = master.location.y;
            ChangeMapService.gI().goToMap(this, master.zone);
            this.zone.load_Me_To_Another(this);
        }
    }

    public void goHome() {
        if (this.status == GOHOME) {
            return;
        }
        goingHome = true;
        new Thread(() -> {
            try {
                Pet.this.status = Pet.ATTACK;
                Thread.sleep(2000);
            } catch (Exception e) {
            }
            ChangeMapService.gI().goToMap(this, MapService.gI().getMapCanJoin(this, master.gender + 21, -1));
            this.zone.load_Me_To_Another(this);
            Pet.this.status = Pet.GOHOME;
            goingHome = false;
        }).start();
    }

    private String getTextStatus(byte status) {
        switch (status) {
            case FOLLOW:
                return "Ok con sẽ theo sư phụ";
            case PROTECT:
                return "Ok con sẽ bảo vệ sư phụ";
            case ATTACK:
                return "Ok con sẽ đi đám bọn nó";
            case GOHOME:
                return "Ok con về nhà đây";
            default:
                return "";
        }
    }
 public void ggtv4(boolean ggtv4) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion2, master.ltggtv4)) {
            if (ggtv4) {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.HOP_THE_GOGETA;
                ItemTimeService.gI().sendItemTime(master, 16597, Fusion.TIME_FUSION2 / 1000);
                master.getAura();
                Service.getInstance().sendFoot(master, 1305);
                Service.getInstance().sendFlagBag(master);
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.HOP_THE_GOGETA;
                ItemTimeService.gI().sendItemTime(master, 16597, Fusion.TIME_FUSION2 / 1000);
                master.getAura();
                Service.getInstance().sendFoot(master, 1305);
                Service.getInstance().sendFlagBag(master);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(4);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + Util.msToTime(master.ltggtv4)+" nữa");
        }
    }
 public void fusion(boolean porata) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790, Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }
    public void fusion2(boolean porata2) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata2) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA2;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790, Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }
    public void fusion3(boolean porata3) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata3) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA3;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790, Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }
    public void fusion4(boolean porata4) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata4) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA4;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790, Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }
    public void fusion5(boolean porata5) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata5) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA5;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790, Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }
    
    public void unFusion() {
        master.fusion.typeFusion = 0;
        this.status = PROTECT;
        Service.gI().point(master);
        joinMapMaster();
        fusionEffect(master.fusion.typeFusion);
        Service.gI().Send_Caitrang(master);
        Service.gI().point(master);
        this.lastTimeUnfusion = System.currentTimeMillis();
        if (master.ltggtv4 > 0) {
        Service.getInstance().sendThongBao(master, "|7|Hợp thể Gogeta\n|2|còn "+ Util.demgiay(master.ltggtv4));
        }
    }
    public void unFusion2() {
        master.fusion.typeFusion = 0;
        this.status = PROTECT;
        Service.gI().point(master);
        joinMapMaster();
        master.getAura();
        fusionEffect(master.fusion.typeFusion);
        Service.gI().Send_Caitrang(master);
        Service.gI().sendFlagBag(master);
        ItemTimeService.gI().removeItemTime(master, 16597);
        master.ltggtv4 = System.currentTimeMillis()+(10000 * 60 * 3);
        this.lastTimeUnfusion2 = System.currentTimeMillis();
        Service.getInstance().sendThongBao(master, "|7|Hợp thể Gogeta\n|2|còn "+ Util.demgiay(master.ltggtv4));
    }
    
    private void fusionEffect(int type) {
        Message msg;
        try {
            msg = new Message(125);
            msg.writer().writeByte(type);
            msg.writer().writeInt((int) master.id);
            Service.gI().sendMessAllPlayerInMap(master, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public long lastTimeMoveIdle;
    private int timeMoveIdle;
    public boolean idle;

    private void moveIdle() {
        if (status == GOHOME || status == FUSION) {
            return;
        }
        if (idle && Util.canDoWithTime(lastTimeMoveIdle, timeMoveIdle)) {
            int dir = this.location.x - master.location.x <= 0 ? -1 : 1;
            PlayerService.gI().playerMove(this, master.location.x
                    + Util.nextInt(dir == -1 ? 30 : -50, dir == -1 ? 50 : 30), master.location.y);
            lastTimeMoveIdle = System.currentTimeMillis();
            timeMoveIdle = Util.nextInt(5000, 8000);
        }
    }

    private long lastTimeMoveAtHome;
    private byte directAtHome = -1;

    @Override
    public void update() {
        try {
            super.update();
            increasePoint(); //cộng chỉ số
            updatePower(); //check mở skill...
            if (isDie()) {
                if (System.currentTimeMillis() - lastTimeDie > 50000) {
                    Service.gI().hsChar(this, nPoint.hpMax, nPoint.mpMax);
                } else {
                    return;
                }
            }
            if (justRevived && this.zone == master.zone) {
                Service.gI().chatJustForMe(master, this, "Sư phụ ơi, con đây nè!");
                justRevived = false;
            }

            if (this.zone == null || this.zone != master.zone) {
                joinMapMaster();
            }
            if (master.isDie() || this.isDie() || effectSkill.isHaveEffectSkill()) {
                return;
            }

            moveIdle();
             moveIdle();
            if (ANGRY) {
                Player pl = this.zone.getPlayerInMap((int) playerAttack.id);
                int disToPlayer = Util.getDistance(this, pl);
                if (pl.isDie() || pl == null && (pl.typePk != 3 || pl.typePk != 5)) {
                    playerAttack = null;
                    ANGRY = false;
                } else {
                    mobAttack = null;
                    if (playerAttack != null) {
                        if (disToPlayer <= ARANGE_ATT_SKILL1  && !canUseSkill2) {
                            // đấm
                            this.playerSkill.skillSelect = getSkill(1);
                            if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                if (SkillService.gI().canUseSkillWithMana(this)) {
                                    PlayerService.gI().playerMove(this, pl.location.x + Util.nextInt(-20, 20),
                                            pl.location.y);
                                    SkillService.gI().useSkill(this, pl, null, null);
                                } else {
                                    askPea();
                                }
                            }
                        } else {
                            if (disToPlayer <= ARANGE_CAN_ATTACK + 50) {
                                this.playerSkill.skillSelect = getSkill(2);
                                if (this.playerSkill.skillSelect.skillId != -1) {
                                    if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                        if (SkillService.gI().canUseSkillWithMana(this)) {
                                            SkillService.gI().useSkill(this, pl, null, null);
                                            this.canUseSkill2 = true;
                                        } else {
                                            askPea();
                                            this.canUseSkill2 = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            switch (status) {
                case FOLLOW:
                    followMaster(60);
                    break;
                case PROTECT:
                    if (useSkill3() || useSkill4()) {
                        break;
                    }
                    mobAttack = findMobAttack();
                    if (mobAttack != null) {
                         int disToMob = Util.getDistance(this, mobAttack);
                        if (disToMob <= ARANGE_ATT_SKILL1 && !canUseSkill2) {
                            // đấm
                            this.playerSkill.skillSelect = getSkill(1);
                            if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                if (SkillService.gI().canUseSkillWithMana(this)) {
                                    PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-60, 60),
                                            mobAttack.location.y);
                                    SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                } else {
                                    askPea();
                                }
                            }
                        } else {
                            // chưởng
                            this.playerSkill.skillSelect = getSkill(2);
                            if (this.playerSkill.skillSelect.skillId != -1) {
                                if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
                                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                        this.canUseSkill2 = true;
                                    } else {
                                        askPea();
                                        this.canUseSkill2 = false;

                                    }
                                }
                            }
                        }
                    } else {
                        idle = true;
                    }
                    break;
                case ATTACK:
                    if (useSkill3() || useSkill4()) {
                        break;
                    }
                    mobAttack = findMobAttack();
                    if (mobAttack != null) {
                         int disToMob = Util.getDistance(this, mobAttack);
                        if (disToMob <= ARANGE_ATT_SKILL1) {
                            this.playerSkill.skillSelect = getSkill(1);
                            if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                if (SkillService.gI().canUseSkillWithMana(this)) {
                                    PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20),
                                            mobAttack.location.y);
                                    SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                } else {
                                    askPea();
                                }
                            }
                        } else {
                            this.playerSkill.skillSelect = getSkill(2);
                            if (this.playerSkill.skillSelect.skillId != -1) {
                                if (SkillService.gI().canUseSkillWithMana(this)) {
                                    SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                }
                            } else {
                                this.playerSkill.skillSelect = getSkill(1);
                                if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
                                        PlayerService.gI().playerMove(this,
                                                mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                    } else {
                                        askPea();
                                    }
                                }
                            }
                        }
                    } else {
                        idle = true;
                    }
//                    playerAttack = findBossAttack();
//                    if (playerAttack != null) {
//                        int disToPl = Util.getDistance(this, playerAttack);
//                        if (disToPl <= ARANGE_ATT_SKILL1) {
//                            //đấm
//                            this.playerSkill.skillSelect = getSkill(1);
//                            if (SkillService.gI().canUseSkillWithCooldown(this)) {
//                                if (SkillService.gI().canUseSkillWithMana(this)) {
//                                    PlayerService.gI().playerMove(this, playerAttack.location.x + Util.nextInt(-60, 60), playerAttack.location.y);
//                                    SkillService.gI().useSkill(this, playerAttack, null,null);
//                                } else {
//                                    askPea();
//                                }
//                            }
//                        } else {
//                            //chưởng
//                            this.playerSkill.skillSelect = getSkill(2);
//                            if (this.playerSkill.skillSelect.skillId != -1) {
//                                if (SkillService.gI().canUseSkillWithMana(this)) {
//                                    SkillService.gI().useSkill(this, playerAttack, null, null);
//                                }
//                            } else {
//                                this.playerSkill.skillSelect = getSkill(1);
//                                if (SkillService.gI().canUseSkillWithCooldown(this)) {
//                                    if (SkillService.gI().canUseSkillWithMana(this)) {
//                                        PlayerService.gI().playerMove(this,
//                                        mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
//                                        SkillService.gI().useSkill(this, playerAttack, null, null);
//                                    } else {
//                                        askPea();
//                                    }
//                                }
//                            }
//                        }
//                    } else {
//                        idle = true;
//                    }
//                    break;

                case GOHOME:
                    if (this.zone != null && (this.zone.map.mapId == 21 || this.zone.map.mapId == 22 || this.zone.map.mapId == 23)) {
                        if (System.currentTimeMillis() - lastTimeMoveAtHome <= 5000) {
                            break;
                        } else {
                            if (this.zone.map.mapId == 21) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 250, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 200, 336);
                                    directAtHome = -1;
                                }
                            } else if (this.zone.map.mapId == 22) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 500, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 452, 336);
                                    directAtHome = -1;
                                }
                            } else if (this.zone.map.mapId == 22) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 250, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 200, 336);
                                    directAtHome = -1;
                                }
                            }
                            Service.gI().chatJustForMe(master, this, "Bibi sư phụ !");
                            lastTimeMoveAtHome = System.currentTimeMillis();
                        }
                    }
                    break;
            }
        } catch (Exception e) {
//            Logger.logException(Pet.class, e);
        }
    }

    private long lastTimeAskPea;

    public void askPea() {
        if (Util.canDoWithTime(lastTimeAskPea, 10000)) {
            Service.gI().chatJustForMe(master, this, "Sư phụ ơi cho con đậu thần đi, con đói sắp chết rồi !!");
            lastTimeAskPea = System.currentTimeMillis();
        }
    }

    private int countTTNL;

    private boolean useSkill3() {
        try {
            playerSkill.skillSelect = getSkill(3);
            if (playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.THAI_DUONG_HA_SAN:
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        Service.gI().chatJustForMe(master, this, "Bất ngờ chưa ông già");
                        return true;
                    }
                    return false;
                case Skill.TAI_TAO_NANG_LUONG:
                    if (this.effectSkill.isCharging && this.countTTNL < Util.nextInt(3, 5)) {
                        this.countTTNL++;
                        return true;
                    }
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)
                            && (this.nPoint.getCurrPercentHP() <= 20 || this.nPoint.getCurrPercentMP() <= 20)) {
                        SkillService.gI().useSkill(this, null, null,null);
                        this.countTTNL = 0;
                        return true;
                    }
                    return false;
                case Skill.KAIOKEN:
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        mobAttack = this.findMobAttack();
                        if (mobAttack == null) {
                            return false;
                        }
                        int dis = Util.getDistance(this, mobAttack);
                        if (dis > ARANGE_ATT_SKILL1) {
                            PlayerService.gI().playerMove(this, mobAttack.location.x, mobAttack.location.y);
                        } else {
                            if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                                PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                            }
                        }
                        SkillService.gI().useSkill(this, playerAttack, mobAttack,null);
                        getSkill(1).lastTimeUseThisSkill = System.currentTimeMillis();
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private boolean useSkill4() {
        try {
            this.playerSkill.skillSelect = getSkill(4);
            if (this.playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.BIEN_KHI:
                    if (!this.effectSkill.isMonkey && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null,null);
                        return true;
                    }
                    return false;
                case Skill.KHIEN_NANG_LUONG:
                    if (!this.effectSkill.isShielding && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null,null);
                        return true;
                    }
                    return false;
                case Skill.DE_TRUNG:
                    if (this.mobMe == null && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, playerAttack, mobAttack,null);
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

//========================BETA SKILL5=====================
  /*  private boolean useSkill5() {
        try {
            this.playerSkill.skillSelect = getSkill(5);
            if (this.playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.TU_SAT:
                    if (this.nPoint.hp < this.nPoint.hpMax*30/100 && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, playerAttack, mobAttack,null);
                        return true;
                    }
                    return false;
                case Skill.DICH_CHUYEN_TUC_THOI:
                        if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
                                        if (mobAttack != null) {
                                        PlayerService.gI().playerMove(this.pet, mobAttack.location.x, mobAttack.location.y);
                                        SkillService.gI().useSkill(this, playerAttack, mobAttack,null);
                                        EffectSkillService.gI().sendEffectMob(this, mobAttack, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);
                                        this.nPoint.isCrit100 = true;
                                        }
                                        SkillService.gI().affterUseSkill(this, this.playerSkill.skillSelect.template.id);
                                        return true;
                        } else {
                        askPea();
                        }}
                        return false;
                case Skill.TROI:
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                    int timeHold = SkillUtil.getTimeTroi(this.playerSkill.skillSelect.point);
                    if (mobAttack != null) {
                    EffectSkillService.gI().setUseTroi(this, System.currentTimeMillis(), timeHold);
                    EffectSkillService.gI().sendEffectMob(this, mobAttack, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.HOLD_EFFECT);
                    mobAttack.effectSkill.setTroi(System.currentTimeMillis(), timeHold);
                    }
                    SkillService.gI().affterUseSkill(this, this.playerSkill.skillSelect.template.id);
                    return true; 
                    }
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    } */

    //====================================================
    private long lastTimeIncreasePoint;
 public void upSkillPet(byte id,short cost){

        int tempId = this.playerSkill.skills.get(id).template.id;
        int level = this.playerSkill.skills.get(id).point + 1;
        if(level > 7){
            Service.gI().sendThongBao((Player) this.master,"Kĩ năng đệ đã đạt cấp tối đa!");
        }else if(((Player) this.master).inventory.gem < cost){
            Service.gI().sendThongBao((Player) this.master,"Bạn không đủ ngọc để nâng cấp!");
        }else{
            Skill skill = null;
            try {
                skill = SkillUtil.nClassTD.getSkillTemplate(tempId).skillss.get(level - 1);
            } catch (Exception e) {
                try {
                    skill = SkillUtil.nClassNM.getSkillTemplate(tempId).skillss.get(level - 1);
                } catch (Exception ex) {
                    skill = SkillUtil.nClassXD.getSkillTemplate(tempId).skillss.get(level - 1);
                }
            }
            skill = new Skill(skill);
            if(id == 1){
                skill.coolDown = 1000;
            }
            this.playerSkill.skills.set(id, skill);                                         
            ((Player)this.master).inventory.gem -= cost;
            Service.gI().sendMoney((Player)this.master);
        }
        
    }
 private void increasePoint() {
        if (this.nPoint != null && Util.canDoWithTime(lastTimeIncreasePoint, 500)) {
            if (Util.isTrue(30, 100)) {
                this.nPoint.increasePoint((byte) 2, (short) Manager.TNPET);
            } else {
                byte typepet = (byte) Util.nextInt(0, 1);
                short point = (short) Manager.TNPET;
                this.nPoint.increasePoint(typepet, point);
            }
            lastTimeIncreasePoint = System.currentTimeMillis();
        }
    }

    public void followMaster() {
        if (this.isDie() || effectSkill.isHaveEffectSkill()) {
            return;
        }
        switch (this.status) {
            case ATTACK:
                if ((mobAttack != null && Util.getDistance(this, master) <= 1500)) {
                    break;
                }
            case FOLLOW:
            case PROTECT:
                followMaster(60);
                break;
        }
    }

    private void followMaster(int dis) {
        int mX = master.location.x;
        int mY = master.location.y;
        int disX = this.location.x - mX;
        if (Math.sqrt(Math.pow(mX - this.location.x, 2) + Math.pow(mY - this.location.y, 2)) >= dis) {
            if (disX < 0) {
                this.location.x = mX - Util.nextInt(0, dis);
            } else {
                this.location.x = mX + Util.nextInt(0, dis);
            }
            this.location.y = mY;
            PlayerService.gI().playerMove(this, this.location.x, this.location.y);
        }
    }


    public short getAvatar() {
        switch (this.typePet) {
            case 1:
                return 297;
            case 2:
                return 427;
            case 3:
                return 1384;
            case 4:
                return 1402;
            case 5:
                return 1437;
            case 6:
                return 1440;
            default:
                return PET_ID[3][this.gender];
        }
    }

    @Override
    public short getHead() {
        if (effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } else if (effectSkill.isSocola) {
            return 412;
        } else if (this.typePet == 1 && !this.isTransform) {
            return 297;
        } else if (this.typePet == 2 && !this.isTransform) {
            return 427;
        }else if (this.typePet == 3 && !this.isTransform) {
            return 1384;
        } else if (this.typePet == 4 && !this.isTransform) {
            return 1402;
        } else if (this.typePet == 5 && !this.isTransform) {
            return 1437;
        }  else if (this.typePet == 6 && !this.isTransform) {
            return 1440;
        } else if (inventory.itemsBody.get(5).isNotNullItem()) {
            int head = inventory.itemsBody.get(5).template.head;
            if (head != -1) {
                return (short) head;
            }
        }
        if (this.nPoint.power < 1500000) {
            return PET_ID[this.gender][0];
        } else {
            return PET_ID[3][this.gender];
        }
    }

    @Override
    public short getBody() {
        if (effectSkill.isMonkey) {
            return 193;
        } else if (effectSkill.isSocola) {
            return 413;
        } else if (this.typePet == 1 && !this.isTransform) {
            return 298;
        } else if (this.typePet == 2 && !this.isTransform) {
            return 428;
        }else if (this.typePet == 3 && !this.isTransform) {
            return 1385;
        } else if (this.typePet == 4 && !this.isTransform) {
            return 1400;
        } else if (this.typePet == 5 && !this.isTransform) {
            return 1438;
        } else if (this.typePet == 6 && !this.isTransform) {
            return 1441;
        }else if (inventory.itemsBody.get(5).isNotNullItem()) {
            int body = inventory.itemsBody.get(5).template.body;
            if (body != -1) {
                return (short) body;
            }
        }
        if (inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        if (this.nPoint.power < 1500000) {
            return PET_ID[this.gender][1];
        } else {
            return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
        }
    }

    @Override
    public short getLeg() {
        if (effectSkill.isMonkey) {
            return 194;
        } else if (effectSkill.isSocola) {
            return 414;
        } else if (this.typePet == 1 && !this.isTransform) {
            return 299;
        } else if (this.typePet == 2 && !this.isTransform) {
            return 429;
        }else if (this.typePet == 3 && !this.isTransform) {
            return 1386;
        }else if (this.typePet == 4 && !this.isTransform) {
            return 1401;
        } else if (this.typePet == 5 && !this.isTransform) {
            return 1439;
        } else if (this.typePet == 6 && !this.isTransform) {
            return 1442;
        }else if (inventory.itemsBody.get(5).isNotNullItem()) {
            int leg = inventory.itemsBody.get(5).template.leg;
            if (leg != -1) {
                return (short) leg;
            }
        }
        if (inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }

        if (this.nPoint.power < 1500000) {
            return PET_ID[this.gender][2];
        } else {
            return (short) (gender == ConstPlayer.NAMEC ? 60 : 58);
        }
    }

    private Mob findMobAttack() {
        int dis = ARANGE_CAN_ATTACK;
        Mob mobAtt = null;
        for (Mob mob : zone.mobs) {
            if (mob.isDie()) {
                continue;
            }
            int d = Util.getDistance(this, mob);
            if (d <= dis) {
                dis = d;
                mobAtt = mob;
            }
        }
          if (ANGRY == true) {
            return null;
        }
        return mobAtt;
    }
     public void angry(Player plAtt) {

        if (plAtt != null && plAtt != this && plAtt != this.master && isTargerDe(plAtt)) {
            this.playerAttack = plAtt;
            if (System.currentTimeMillis() - lastTimeAngry > 5000) {
                if (this.playerAttack.isPet) {
                    this.chat( "Mi làm ta nổi giận rồi đệ " + playerAttack.name
                            .replace("$", ""));
                } else if (this.playerAttack.isBoss) {
                    this.chat( "Mi làm ta nổi giận rồi Boss " + playerAttack.name
                            .replace("$", ""));
                } else {
                    this.chat(
                            "Mi làm ta nổi giận rồi thằng " + playerAttack.name
                                    .replace("$", ""));
                }
            }
            lastTimeAngry = System.currentTimeMillis();

            ANGRY = true;
            this.mobAttack = null;
        } else {
            ANGRY = false;
            this.playerAttack = null;
        }
    }

    private Player findBossAttack() {
        int dis = ARANGE_CAN_ATTACK;
        Player bAtt = null;
        for (Player b : zone.getBosses()) {
            if (b.isDie()) {
                continue;
            }
            int d = Util.getDistance(this, b);
            if (d <= dis) {
                dis = d;
                bAtt = b;
            }
        }
        return bAtt;
    }
    private long lastTimeTargetPlayer;
    private int timeTargetPlayer;
    public Player findPlAttack() {
        if (master.TrieuHoiPlayerAttack != null) {
            if (master.TrieuHoiPlayerAttack != null
                    && (master.TrieuHoiPlayerAttack.isDie() || !this.zone.equals(master.TrieuHoiPlayerAttack.zone))) {
                master.TrieuHoiPlayerAttack = null;
                if (master.TrieuHoiPlayerAttack == null) {
                    if (this.playerAttack != null && (this.playerAttack.isDie() || !this.zone.equals(this.playerAttack.zone)
                            || (this.playerAttack.pvp == null || this.pvp == null)
                            || (this.playerAttack.typePk != ConstPlayer.PK_ALL
                            || this.typePk != ConstPlayer.PK_ALL)
                            || (!this.pvp.isInPVP(this.playerAttack) || !this.playerAttack.pvp.isInPVP(this))
                            || ((this.playerAttack.cFlag == 0 && this.cFlag == 0) && (this.playerAttack.cFlag != 8 || this.cFlag == this.playerAttack.cFlag)))
                            || this.playerAttack == this || this.playerAttack == master) {
                            this.playerAttack = null;
                    }
                    if (this.zone != null
                            && (this.playerAttack == null || this.playerAttack == this 
                            || this.playerAttack == master)
                            || Util.canDoWithTime(this.lastTimeTargetPlayer, this.timeTargetPlayer)) {
                        this.playerAttack = this.zone.PlayerPKinmap();
                        this.lastTimeTargetPlayer = System.currentTimeMillis();
                        this.timeTargetPlayer = Util.nextInt(5000, 10000);
                    }
                    return this.playerAttack;
                }
            }
            return master.TrieuHoiPlayerAttack;
        } else {
            if (this.playerAttack != null && (this.playerAttack.isDie() || !this.zone.equals(this.playerAttack.zone)
                    || (this.playerAttack.pvp == null || this.pvp == null)
                    || (this.playerAttack.typePk != ConstPlayer.PK_ALL || this.typePk != ConstPlayer.PK_ALL)
                    || (!this.pvp.isInPVP(this.playerAttack) || !this.playerAttack.pvp.isInPVP(this))
                    || ((this.playerAttack.cFlag == 0 && this.cFlag == 0)
                    && (this.playerAttack.cFlag != 8 || this.cFlag == this.playerAttack.cFlag)))
                    || this.playerAttack == this || this.playerAttack == master) {
                this.playerAttack = null;
            }
            if (this.zone != null
                    && (this.playerAttack == null || this.playerAttack == this || this.playerAttack == master)
                    || Util.canDoWithTime(this.lastTimeTargetPlayer, this.timeTargetPlayer)) {
                this.playerAttack = this.zone.PlayerPKinmap();
                this.lastTimeTargetPlayer = System.currentTimeMillis();
                this.timeTargetPlayer = Util.nextInt(40000, 45000);
            }
            return this.playerAttack;
        }
    }
    
    //Sức mạnh mở skill đệ
    private void updatePower() {
        if (this.playerSkill != null) {
            switch (this.playerSkill.getSizeSkill()) {
                case 1:
                    if (this.nPoint.power >= 150000000) {
                        openSkill2();
                    }
                    break;
                case 2:
                    if (this.nPoint.power >= 1500000000L) {
                        openSkill3();
                    }
                    break;
                case 3:
                    if (this.nPoint.power >= 20000000000L) {
                        openSkill4();
                    }
                    break;
               /* case 4:
                    if (this.nPoint.power >= 60000000000L) {
                        openSkill5();
                    }
                    break; */
            }
        }
    }

    public void openSkill2() {
        Skill skill = null;
        int tiLeKame = 40;
        int tiLeMasenko = 30;
        int tiLeAntomic = 30;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeKame) {
            skill = SkillUtil.createSkill(Skill.KAMEJOKO, 1);
        } else if (rd <= tiLeKame + tiLeMasenko) {
            skill = SkillUtil.createSkill(Skill.MASENKO, 1);
        } else if (rd <= tiLeKame + tiLeMasenko + tiLeAntomic) {
            skill = SkillUtil.createSkill(Skill.ANTOMIC, 1);
        }
        skill.coolDown = 1000;
        this.playerSkill.skills.set(1, skill);
    }

    public void openSkill3() {
        Skill skill = null;
        int tiLeTDHS = 30;
        int tiLeTTNL = 30;
        int tiLeKOK = 40;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeTDHS) {
            skill = SkillUtil.createSkill(Skill.THAI_DUONG_HA_SAN, 1);
        } else if (rd <= tiLeTDHS + tiLeTTNL) {
            skill = SkillUtil.createSkill(Skill.TAI_TAO_NANG_LUONG, 1);
        } else if (rd <= tiLeTDHS + tiLeTTNL + tiLeKOK) {
            skill = SkillUtil.createSkill(Skill.KAIOKEN, 1);
        }
        this.playerSkill.skills.set(2, skill);
    }

    public void openSkill4() {
        Skill skill = null;
        int tiLeBienKhi = 30;
        int tiLeDeTrung = 30;
        int tiLeKNL = 40;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeBienKhi) {
            skill = SkillUtil.createSkill(Skill.BIEN_KHI, 1);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung) {
            skill = SkillUtil.createSkill(Skill.DE_TRUNG, 1);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung + tiLeKNL) {
            skill = SkillUtil.createSkill(Skill.KHIEN_NANG_LUONG, 1);
        }
        this.playerSkill.skills.set(3, skill);
    }

    private void openSkill5() {
        Skill skill = null;
        int tiLeThoiMien = 10; //khi
        int tiLeSoCoLa = 70; //detrung
        int tiLeDCTT = 20; //khienNl
        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeThoiMien) {
            skill = SkillUtil.createSkill(Skill.TU_SAT, 1);
        } else if (rd <= tiLeThoiMien + tiLeSoCoLa) {
            skill = SkillUtil.createSkill(Skill.TROI, 1);
        } else if (rd <= tiLeThoiMien + tiLeSoCoLa + tiLeDCTT) {
            skill = SkillUtil.createSkill(Skill.DICH_CHUYEN_TUC_THOI, 1);
        }
        this.playerSkill.skills.set(4, skill);
    }

//    ========================================================
    private Skill getSkill(int indexSkill) {
        return this.playerSkill.skills.get(indexSkill - 1);
    }

    public void transform() {
        this.isTransform = !this.isTransform;
        Service.gI().Send_Caitrang(this);
        Service.gI().chat(this, "Ehe, có diện mạo mới rồi!");
    }

    @Override
    public void dispose() {
        if (zone !=null){
            ChangeMapService.gI().exitMap(this);
        }
        this.mobAttack = null;
        this.master = null;
        super.dispose();
    }
}
