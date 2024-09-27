package ServerData.Models.Player.PlayerSkill;

import Server.Data.Consts.ConstPlayer;
import ServerData.Boss.Boss;
import ServerData.Models.Intrinsic.Intrinsic;
import ServerData.Models.Map.Mob.Mob;
import ServerData.Models.Map.Mob.MobMe;
import ServerData.Models.Player.Pet;
import ServerData.Models.Player.Player;
import ServerData.Services.EffectSkillService;
import ServerData.Services.ItemTimeService;
import ServerData.Services.MapService;
import ServerData.Services.PlayerService;
import ServerData.Services.Service;
import com.girlkun.network.io.Message;
import ServerData.Utils.Logger;
import ServerData.Utils.SkillUtil;
import ServerData.Utils.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SkillService {

    private static SkillService i;

    private SkillService() {

    }

    public static SkillService gI() {
        if (i == null) {
            i = new SkillService();
        }
        return i;
    }

//    public boolean useSkill(Player player, Player plTarget, Mob mobTarget , Message message) {
//        if(player == null){
//            return false;
//        }
//        if (player.effectSkill.isHaveEffectSkill()) {
//            return false;
//        }
//        if (player.playerSkill == null) {
//            return false;
//        }
//        if (player.playerSkill.skillSelect.template.type == 2 && canUseSkillWithMana(player) && canUseSkillWithCooldown(player)) {
//            useSkillBuffToPlayer(player, plTarget);
//            return true;
//        }
//        if ((player.effectSkill.isHaveEffectSkill()
//                && (player.playerSkill.skillSelect.template.id != Skill.TU_SAT
//                && player.playerSkill.skillSelect.template.id != Skill.QUA_CAU_KENH_KHI
//                && player.playerSkill.skillSelect.template.id != Skill.MAKANKOSAPPO))
//                || (plTarget != null && !canAttackPlayer(player, plTarget))
//                || (mobTarget != null && mobTarget.isDie())
//                || !canUseSkillWithMana(player) || !canUseSkillWithCooldown(player)) {
//            return false;
//        }
//        if (player.effectSkill.useTroi) {
//            EffectSkillService.gI().removeUseTroi(player);
//        }
//        if (player.effectSkill.isCharging) {
//            EffectSkillService.gI().stopCharge(player);
//        }
//        if (player.isPet) {
////            ((Pet) player).lastTimeMoveIdle = System.currentTimeMillis();
//        }
//        switch (player.playerSkill.skillSelect.template.type) {
//            case 1:
//                useSkillAttack(player, plTarget, mobTarget);
////                Service.getInstance().releaseCooldownSkill(player);
//                break;
//            case 3:
//                useSkillAlone(player);
//                break;
//                case 4:
//                userSkillSpecial(player, message);
//                break;
//            default:
//                return false;
//        }
//        return true;
//    }
    public boolean useSkill(Player player, Player plTarget, Mob mobTarget, Message message) {
        if (player.effectSkill.isHaveEffectSkill()) {
            return false;
        }

        if (player.playerSkill == null) {
            return false;
        }
        if (player.playerSkill.skillSelect.template.type == 2 && canUseSkillWithMana(player) && canUseSkillWithCooldown(player)) {
            useSkillBuffToPlayer(player, plTarget);
            return true;
        }
        if ((player.effectSkill.isHaveEffectSkill()
                && (player.playerSkill.skillSelect.template.id != Skill.TU_SAT
                && player.playerSkill.skillSelect.template.id != Skill.QUA_CAU_KENH_KHI
                && player.playerSkill.skillSelect.template.id != Skill.MAKANKOSAPPO))
                || (plTarget != null && !canAttackPlayer(player, plTarget))
                || (mobTarget != null && mobTarget.isDie())
                || !canUseSkillWithMana(player) || !canUseSkillWithCooldown(player)) {
            return false;
        }
        if (player.effectSkill.useTroi) {
            EffectSkillService.gI().removeUseTroi(player);
        }
        if (player.effectSkill.isCharging) {
            EffectSkillService.gI().stopCharge(player);
        }
        switch (player.playerSkill.skillSelect.template.type) {
            case 1:
                useSkillAttack(player, plTarget, mobTarget);
                break;
            case 3:
                useSkillAlone(player);
                break;
            case 4:
                userSkillSpecial(player, message);
                break;
            default:
                return false;
        }
        return true;
    }

    private void userSkillSpecial(Player player, Message message) {
        if (message == null) {
            return;
        }
        try {
            byte st = message.reader().readByte();
            byte skillId = message.reader().readByte();
            Short dx = message.reader().readShort();
            Short dy = message.reader().readShort();
            byte dir = message.reader().readByte();
            Short x = message.reader().readShort();
            Short y = message.reader().readShort();
            switch (skillId) {
                case Skill.SUPER_KAME:
                    sendEffSkillSpecialID24(player, dir);
                    break;
                case Skill.LIEN_HOAN_CHUONG:
                    sendEffSkillSpecialID25(player, dir);
                    break;
                case Skill.MA_PHONG_BA:
                    sendEffSkillSpecialID26(player, dir);
                    break;
            }
            player.iDMark.setLastTimeSkillSpecial(System.currentTimeMillis());
            affterUseSkill(player, player.playerSkill.skillSelect.template.id);
            player.skillSpecial.setSkillSpecial(dir, dx, dy, x, y);
        } catch (Exception e) {
        }
    }

    public void updateSkillSpecial(Player player) {
        try {
            if (player.isDie() || player.effectSkill.isHaveEffectSkill()) {
                player.skillSpecial.closeSkillSpecial();
                return;
            }
            if (player.skillSpecial.skillSpecial.template.id == Skill.MA_PHONG_BA) {
                if (player.skillSpecial.stepSkillSpecial == 0 && Util.canDoWithTime(player.skillSpecial.lastTimeSkillSpecial, SkillSpecial.TIME_GONG)) {
                    player.skillSpecial.stepSkillSpecial = 1;
                    player.skillSpecial.lastTimeSkillSpecial = System.currentTimeMillis();
                    for (Player playerMap : player.zone.getHumanoids()) { // cho hut boss thi player.zone.getHumanoids()
                        if (playerMap == null || playerMap.id == player.id) {
                            continue;
                        }
                        if (player.skillSpecial.dir == -1 && !playerMap.isDie() && Util.getDistance(player, playerMap) <= 75 && this.canAttackPlayer(player, playerMap)) {
                            player.skillSpecial.playersTaget.add(playerMap);
                        } else if (player.skillSpecial.dir == 1 && !playerMap.isDie() && Util.getDistance(player, playerMap) <= 75 && this.canAttackPlayer(player, playerMap)) {
                            player.skillSpecial.playersTaget.add(playerMap);
                        }
                    }
                    for (Mob mobMap : player.zone.mobs) {
                        if (mobMap == null) {
                            continue;
                        }
                        if (player.skillSpecial.dir == -1 && !mobMap.isDie() && Util.getDistance(player, mobMap) <= 75) {
                            player.skillSpecial.mobsTaget.add(mobMap);
                        } else if (player.skillSpecial.dir == 1 && !mobMap.isDie() && Util.getDistance(player, mobMap) <= 75) {
                            player.skillSpecial.mobsTaget.add(mobMap);
                        }
                    }
                    this.startSkillSpecialID26(player);
                     Thread.sleep(3000);// nghỉ 3s
                } else if (player.skillSpecial.stepSkillSpecial == 1 && Util.canDoWithTime(player.skillSpecial.lastTimeSkillSpecial, SkillSpecial.TIME_END)) {
                    for (Player playerMap : player.skillSpecial.playersTaget) {
                        EffectSkillService.gI().sendPlayerToCaiBinh(playerMap, 11000);
                    }
                    for (Mob mobMap : player.skillSpecial.mobsTaget) {
                        mobMap.effectSkill.setCaiBinhChua(System.currentTimeMillis(), 11000);
                        EffectSkillService.gI().sendMobToCaiBinh(player, mobMap, 11000, (short) 16427);
                    }
                    player.skillSpecial.closeSkillSpecial();
                }
            } else {
                if (player.skillSpecial.stepSkillSpecial == 0 && Util.canDoWithTime(player.skillSpecial.lastTimeSkillSpecial, SkillSpecial.TIME_GONG)) {
                    player.skillSpecial.lastTimeSkillSpecial = System.currentTimeMillis();
                    player.skillSpecial.stepSkillSpecial = 1;
                    if (player.skillSpecial.skillSpecial.template.id == Skill.SUPER_KAME) {
                        this.startSkillSpecialID24(player);
                    } else if (player.skillSpecial.skillSpecial.template.id == Skill.LIEN_HOAN_CHUONG) {
                        this.startSkillSpecialID25(player);
                    }
                } else if (player.skillSpecial.stepSkillSpecial == 1 && !Util.canDoWithTime(player.skillSpecial.lastTimeSkillSpecial, SkillSpecial.TIME_END)) {
                    for (Player playerMap : player.zone.getHumanoids()) {
                        if (playerMap == null) {
                            continue;
                        }
                        if (player.skillSpecial.dir == -1 && !playerMap.isDie()
                                && playerMap.location.x <= player.location.x - 15
                                && Math.abs(playerMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                && Math.abs(playerMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget
                                && this.canAttackPlayer(player, playerMap)) {
                            this.playerAttackPlayer(player, playerMap, false);
                        }
                        if (player.skillSpecial.dir == 1 && !playerMap.isDie()
                                && playerMap.location.x >= player.location.x + 15
                                && Math.abs(playerMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                && Math.abs(playerMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget
                                && this.canAttackPlayer(player, playerMap)) {
                            this.playerAttackPlayer(player, playerMap, false);
                        }
                    }
                    for (Mob mobMap : player.zone.mobs) {
                        if (mobMap == null) {
                            continue;
                        }
                        if (player.skillSpecial.dir == -1 && !mobMap.isDie()
                                && mobMap.location.x <= player.skillSpecial._xPlayer - 15
                                && Math.abs(mobMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                && Math.abs(mobMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget) {
                            this.playerAttackMob(player, mobMap, false, false);
                        }
                        if (player.skillSpecial.dir == 1 && !mobMap.isDie()
                                && mobMap.location.x >= player.skillSpecial._xPlayer + 15
                                && Math.abs(mobMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                && Math.abs(mobMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget) {
                            this.playerAttackMob(player, mobMap, false, false);
                        }
                    }
                } else if (player.skillSpecial.stepSkillSpecial == 1 && Util.canDoWithTime(player.skillSpecial.lastTimeSkillSpecial, SkillSpecial.TIME_END)) {
                    player.skillSpecial.closeSkillSpecial();
                }
            }
        } catch (Exception e) {
        }
    }

    public void sendCurrLevelSpecial(Player player, Skill skill) {
        Message message = null;
        try {
            message = Service.getInstance().messageSubCommand((byte) 62);
            message.writer().writeShort(skill.skillId);
            message.writer().writeByte(0);
            message.writer().writeShort(skill.currLevel);
            player.sendMessage(message);
        } catch (final Exception ex) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    //============================================================================
    // Skill SuperKame
    public void sendEffSkillSpecialID24(Player player, byte dir) {
        Message message = null;
        try {
            message = new Message(-45);// 
            message.writer().writeByte(20);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(24);
            message.writer().writeByte(1);
            message.writer().writeByte(dir); // -1 trai | 1 phai
            message.writer().writeShort(2000);
            message.writer().writeByte(0);
            message.writer().writeByte(player.gender);
            message.writer().writeByte(0);
            Service.getInstance().sendMessAllPlayerInMap(player, message);
            message.cleanup();
        } catch (Exception e) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    // Skill liên hoàn chưởng
    public void sendEffSkillSpecialID25(Player player, byte dir) {
        Message message = null;
        try {
            message = new Message(-45);// passt code k dc vcb 
            message.writer().writeByte(20);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(25);
            message.writer().writeByte(2);
            message.writer().writeByte(dir); // -1 trai | 1 phai
            message.writer().writeShort(2000);
            message.writer().writeByte(0);
            message.writer().writeByte(player.gender);
            message.writer().writeByte(0);
            Service.getInstance().sendMessAllPlayerInMap(player, message);
            message.cleanup();
        } catch (Exception e) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    // Skill Ma phong ba
    public void sendEffSkillSpecialID26(Player player, byte dir) {
        Message message = null;
        try {
            message = new Message(-45);// passt code k dc vcb 
            message.writer().writeByte(20);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(26);
            message.writer().writeByte(3);
            message.writer().writeByte(dir); // -1 trai | 1 phai
            message.writer().writeShort(SkillSpecial.TIME_GONG);
            message.writer().writeByte(0);
            message.writer().writeByte(player.gender);
            message.writer().writeByte(0);
            Service.getInstance().sendMessAllPlayerInMap(player, message);
            message.cleanup();
        } catch (Exception e) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    public void startSkillSpecialID24(Player player) {
        Message message = null;
        try {
            message = new Message(-45);
            message.writer().writeByte(21);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(player.skillSpecial.skillSpecial.template.id);
            message.writer().writeShort(player.skillSpecial._xPlayer + ((player.skillSpecial.dir == -1) ? (-player.skillSpecial._xObjTaget) : player.skillSpecial._xObjTaget));
            message.writer().writeShort(player.skillSpecial._xPlayer);
            message.writer().writeShort(3000); // thời gian skill chưởng chưởng nè
            message.writer().writeShort(player.skillSpecial._yObjTaget);
            message.writer().writeByte(player.gender);
            message.writer().writeByte(0);
            message.writer().writeByte(0);
            Service.getInstance().sendMessAllPlayerInMap(player, message);
            message.cleanup();
        } catch (final Exception ex) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    public void startSkillSpecialID25(Player player) {
        Message message = null;
        try {
            message = new Message(-45);
            message.writer().writeByte(21);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(player.skillSpecial.skillSpecial.template.id);
            message.writer().writeShort(player.skillSpecial._xPlayer + ((player.skillSpecial.dir == -1) ? (-player.skillSpecial._xObjTaget) : player.skillSpecial._xObjTaget));
            message.writer().writeShort(player.skillSpecial._yPlayer);
            message.writer().writeShort(3000); // thời gian skill chưởng chưởng nè
            message.writer().writeShort(25);
            message.writer().writeByte(player.gender);
            message.writer().writeByte(0);
            message.writer().writeByte(0);
            Service.getInstance().sendMessAllPlayerInMap(player, message);
            message.cleanup();
        } catch (final Exception ex) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    public void startSkillSpecialID26(Player player) {
        Message message = null;
        try {
            message = new Message(-45);
            message.writer().writeByte(21);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(26);
            message.writer().writeShort(player.skillSpecial._xPlayer + ((player.skillSpecial.dir == -1) ? (-75) : 75));
            message.writer().writeShort(player.skillSpecial._yPlayer);
            message.writer().writeShort(3000);
            message.writer().writeShort(player.skillSpecial._yObjTaget);
            message.writer().writeByte(player.gender);
            final byte size = (byte) (player.skillSpecial.playersTaget.size() + player.skillSpecial.mobsTaget.size());
            message.writer().writeByte(size);
            for (Player playerMap : player.skillSpecial.playersTaget) {
                message.writer().writeByte(1);
                message.writer().writeInt((int) playerMap.id);

            }
            for (Mob mobMap : player.skillSpecial.mobsTaget) {
                message.writer().writeByte(0);
                message.writer().writeByte(mobMap.id);
            }
            message.writer().writeByte(0);
            Service.getInstance().sendMessAllPlayerInMap(player, message);
            message.cleanup();
        } catch (final Exception ex) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    // này học skill nha
    public void learSkillSpecial(Player player, byte skillID) {
        Message message = null;
        try {
            Skill curSkill = SkillUtil.createSkill(skillID, 1);
            SkillUtil.setSkill(player, curSkill);
            message = Service.getInstance().messageSubCommand((byte) 23);
            message.writer().writeShort(curSkill.skillId);
            player.sendMessage(message);
            message.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }

        }
    }

    //=============================================================================
    private void useSkillAttack(Player player, Player plTarget, Mob mobTarget) {
        if (!player.isBoss) {
            if (player.isPet) {
                if (player.nPoint.stamina > 0) {
                    player.nPoint.numAttack++;
                    boolean haveCharmPet = ((Pet) player).master.charms.tdDeTu > System.currentTimeMillis();
                    if (haveCharmPet ? player.nPoint.numAttack >= 5 : player.nPoint.numAttack >= 2) {
                        player.nPoint.numAttack = 0;
                        player.nPoint.stamina--;
                    }
                } else {
                    ((Pet) player).askPea();
                    return;
                }
            } else {
                if (player.nPoint.stamina > 0 ) {
                    if (player.charms.tdDeoDai < System.currentTimeMillis()) {
                        player.nPoint.numAttack++;
                        if (player.nPoint.numAttack == 5) {
                            player.nPoint.numAttack = 0;
                            player.nPoint.stamina--;
                            PlayerService.gI().sendCurrentStamina(player);
                        }
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Thể lực đã cạn kiệt, hãy nghỉ ngơi để lấy lại sức");
                    return;
                }
            }
        }
        List<Mob> mobs;
        boolean miss = false;
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.KAIOKEN: //kaioken
                long hpUse = player.nPoint.hpMax / 100 * 10;
                if (player.nPoint.hp <= hpUse) {
                    canUseSkillWithMana(player);
                } else {
                    player.nPoint.setHp(player.nPoint.hp - hpUse);
                    PlayerService.gI().sendInfoHpMpMoney(player);
                    Service.getInstance().Send_Info_NV(player);
                }
            case Skill.DRAGON:
            case Skill.DEMON:
            case Skill.GALICK:
            case Skill.LIEN_HOAN:
                if (plTarget != null && Util.getDistance(player, plTarget) > Skill.RANGE_ATTACK_CHIEU_DAM) {
                    miss = true;
                }
                if (mobTarget != null && Util.getDistance(player, mobTarget) > Skill.RANGE_ATTACK_CHIEU_DAM) {
                    miss = true;
                }
            case Skill.KAMEJOKO:
            case Skill.MASENKO:
            case Skill.ANTOMIC:
                if (plTarget != null) {
                    playerAttackPlayer(player, plTarget, miss);
                }
                if (mobTarget != null) {
                    playerAttackMob(player, mobTarget, miss, false);
                }
                if (player.mobMe != null) {
                    player.mobMe.attack(plTarget, mobTarget);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            //******************************************************************
            case Skill.QUA_CAU_KENH_KHI:
                if (!player.playerSkill.prepareQCKK) {
                    // bắt đầu tụ quả cầu
                    player.playerSkill.prepareQCKK = !player.playerSkill.prepareQCKK;
                    player.playerSkill.lastTimePrepareQCKK = System.currentTimeMillis();
                    sendPlayerPrepareSkill(player, 4000);
                } else {
                    // ném cầu
                    player.playerSkill.prepareQCKK = !player.playerSkill.prepareQCKK;
                    mobs = new ArrayList<>();
                    if (plTarget != null) {
                        playerAttackPlayer(player, plTarget, false);
                        for (Mob mob : player.zone.mobs) {
                            if (!mob.isDie()
                                    && Util.getDistance(plTarget, mob) <= SkillUtil
                                            .getRangeQCKK(player.playerSkill.skillSelect.point)) {
                                mobs.add(mob);
                            }
                        }
                    }
                    if (mobTarget != null) {
                        playerAttackMob(player, mobTarget, false, true);
                        for (Mob mob : player.zone.mobs) {
                            if (!mob.equals(mobTarget) && !mob.isDie()
                                    && Util.getDistance(mob, mobTarget) <= SkillUtil
                                            .getRangeQCKK(player.playerSkill.skillSelect.point)) {
                                mobs.add(mob);
                            }
                        }
                    }
                    for (Mob mob : mobs) {
                        // mob.injured(player, player.point.getDameAttack(), true);
                    }
                    PlayerService.gI().sendInfoHpMpMoney(player);
                    affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                }
                break;
            case Skill.MAKANKOSAPPO:
                if (!player.playerSkill.prepareLaze) {
                    //bắt đầu nạp laze
                    player.playerSkill.prepareLaze = !player.playerSkill.prepareLaze;
                    player.playerSkill.lastTimePrepareLaze = System.currentTimeMillis();
                    sendPlayerPrepareSkill(player, 2000);
                } else {
                    //bắn laze
                    player.playerSkill.prepareLaze = !player.playerSkill.prepareLaze;
                    if (plTarget != null) {
                        playerAttackPlayer(player, plTarget, false);
                    }
                    if (mobTarget != null) {
                        playerAttackMob(player, mobTarget, false, true);
                    }
                    affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                }
                PlayerService.gI().sendInfoHpMpMoney(player);
                break;
            case Skill.SOCOLA:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.SOCOLA);
                int timeSocola = SkillUtil.getTimeSocola();
                if (plTarget != null) {
                    EffectSkillService.gI().setSocola(plTarget, System.currentTimeMillis(), timeSocola);
                    Service.getInstance().Send_Caitrang(plTarget);
                    ItemTimeService.gI().sendItemTime(plTarget, 3780, timeSocola / 1000);
                }
                if (mobTarget != null) {
                    EffectSkillService.gI().sendMobToSocola(player, mobTarget, timeSocola);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.DICH_CHUYEN_TUC_THOI:
                int timeChoangDCTT = SkillUtil.getTimeDCTT(player.playerSkill.skillSelect.point);
                if (plTarget != null) {
                    Service.getInstance().setPos(player, plTarget.location.x, plTarget.location.y);
                    playerAttackPlayer(player, plTarget, false);
                    EffectSkillService.gI().setBlindDCTT(plTarget, System.currentTimeMillis(), timeChoangDCTT);
                    EffectSkillService.gI().sendEffectPlayer(player, plTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);
                    PlayerService.gI().sendInfoHpMpMoney(plTarget);
                    ItemTimeService.gI().sendItemTime(plTarget, 3779, timeChoangDCTT / 1000);
                }
                if (mobTarget != null) {
                    Service.getInstance().setPos(player, mobTarget.location.x, mobTarget.location.y);
                    playerAttackMob(player, mobTarget, false, false);
                    mobTarget.effectSkill.setStartBlindDCTT(System.currentTimeMillis(), timeChoangDCTT);
                    EffectSkillService.gI().sendEffectMob(player, mobTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);
                }
                player.nPoint.isCrit100 = true;
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.THOI_MIEN:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.THOI_MIEN);
                int timeSleep = SkillUtil.getTimeThoiMien(player.playerSkill.skillSelect.point);
                if (plTarget != null) {
                    EffectSkillService.gI().setThoiMien(plTarget, System.currentTimeMillis(), timeSleep);
                    EffectSkillService.gI().sendEffectPlayer(player, plTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.SLEEP_EFFECT);
                    ItemTimeService.gI().sendItemTime(plTarget, 3782, timeSleep / 1000);
                }
                if (mobTarget != null) {
                    mobTarget.effectSkill.setThoiMien(System.currentTimeMillis(), timeSleep);
                    EffectSkillService.gI().sendEffectMob(player, mobTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.SLEEP_EFFECT);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.TROI:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.TROI);
                int timeHold = SkillUtil.getTimeTroi(player.playerSkill.skillSelect.point);
                EffectSkillService.gI().setUseTroi(player, System.currentTimeMillis(), timeHold);
                if (plTarget != null && (!plTarget.playerSkill.prepareQCKK && !plTarget.playerSkill.prepareLaze && !plTarget.playerSkill.prepareTuSat)) {
                    player.effectSkill.plAnTroi = plTarget;
                    EffectSkillService.gI().sendEffectPlayer(player, plTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.HOLD_EFFECT);
                    EffectSkillService.gI().setAnTroi(plTarget, player, System.currentTimeMillis(), timeHold);
                }
                if (mobTarget != null) {
                    player.effectSkill.mobAnTroi = mobTarget;
                    EffectSkillService.gI().sendEffectMob(player, mobTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.HOLD_EFFECT);
                    mobTarget.effectSkill.setTroi(System.currentTimeMillis(), timeHold);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                if (plTarget != null
                        && plTarget.isBoss
                        && MapService.gI().isMapHuyDiet(player.zone.map.mapId)
                        && Util.isTrue(40, 100)) {
                    EffectSkillService.gI().removeUseTroi(player);
                    EffectSkillService.gI().removeAnTroi(plTarget);
                    Service.getInstance().chat(plTarget, "Chiêu đó không có tác dụng đâu haha");
                }
                break;
        }
        if (!player.isBoss) {
            player.effectSkin.lastTimeAttack = System.currentTimeMillis();
        }
    }

    private void useSkillAlone(Player player) {
        List<Mob> mobs;
        List<Player> players;
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.THAI_DUONG_HA_SAN:
                int timeStun = SkillUtil.getTimeStun(player.playerSkill.skillSelect.point);
                if (player.setClothes.thienXinHang == 5) {
                    timeStun *= 2;
                }
                if (player.setClothes.thienXinHang2 == 5) {
                    timeStun *= 2.5;
                }
                if (player.setClothes.thienXinHang3 == 5) {
                    timeStun *= 3;
                }
                mobs = new ArrayList<>();
                players = new ArrayList<>();
                if (!MapService.gI().isMapOffline(player.zone.map.mapId)) {
                    List<Player> playersMap = player.zone.getHumanoids();
                    for (Player pl : playersMap) {
                      if (pl != null && !player.equals(pl) && pl.nPoint != null && !pl.nPoint.khangTDHS) {
                            if (Util.getDistance(player, pl) <= SkillUtil.getRangeStun(player.playerSkill.skillSelect.point) 
                                    && canAttackPlayer(player, pl)) {
                                if (player.isPet && ((Pet) player).master.equals(pl)) {
                                    continue;
                                }
                                if (player.isBoss && ((Boss) player).equals(pl)) {
                                    continue;
                                }
                                EffectSkillService.gI().startStun(pl, System.currentTimeMillis(), timeStun);
                                players.add(pl);
                            }
                        }
                    }
                }
                for (Mob mob : player.zone.mobs) {
                    if (Util.getDistance(player, mob) <= SkillUtil.getRangeStun(player.playerSkill.skillSelect.point)) {
                        mob.effectSkill.startStun(System.currentTimeMillis(), timeStun);
                        mobs.add(mob);
                    }
                }
                EffectSkillService.gI().sendEffectBlindThaiDuongHaSan(player, players, mobs, timeStun);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.DE_TRUNG:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.DE_TRUNG);
                if (player.mobMe != null) {
                    player.mobMe.mobMeDie();
                }
                player.mobMe = new MobMe(player);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.BIEN_KHI:
                EffectSkillService.gI().sendEffectMonkey(player);
                EffectSkillService.gI().setIsMonkey(player);
                EffectSkillService.gI().sendEffectMonkey(player);

                Service.getInstance().sendSpeedPlayer(player, 0);
                Service.getInstance().Send_Caitrang(player);
                Service.getInstance().sendSpeedPlayer(player, -1);
                if (!player.isPet) {
                    PlayerService.gI().sendInfoHpMp(player);
                }
                Service.getInstance().point(player);
                Service.getInstance().Send_Info_NV(player);
                Service.getInstance().sendInfoPlayerEatPea(player);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.KHIEN_NANG_LUONG:
                EffectSkillService.gI().setStartShield(player);
                EffectSkillService.gI().sendEffectPlayer(player, player, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.SHIELD_EFFECT);
                ItemTimeService.gI().sendItemTime(player, 3784, player.effectSkill.timeShield / 1000);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.HUYT_SAO:
                int tileHP = SkillUtil.getPercentHPHuytSao(player.playerSkill.skillSelect.point);
                if (player.zone != null) {
                    if (!MapService.gI().isMapOffline(player.zone.map.mapId)) {
                        List<Player> playersMap = player.zone.getPlayers();
                        for (Player pl : playersMap) {
                            if (pl.effectSkill.useTroi) {
                                EffectSkillService.gI().removeUseTroi(pl);
                            }
                            if (!pl.isBoss && pl.gender != ConstPlayer.NAMEC
                                    && player.cFlag == pl.cFlag) {
                                EffectSkillService.gI().setStartHuytSao(pl, tileHP);
                                EffectSkillService.gI().sendEffectPlayer(pl, pl, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.HUYT_SAO_EFFECT);
                                pl.nPoint.calPoint();
                                pl.nPoint.setHp(pl.nPoint.hp + ((long) pl.nPoint.hp * tileHP / 100));
                                Service.getInstance().point(pl);
                                Service.getInstance().Send_Info_NV(pl);
                                ItemTimeService.gI().sendItemTime(pl, 3781, 30);
                                PlayerService.gI().sendInfoHpMp(pl);
                            }

                        }
                    } else {
                        EffectSkillService.gI().setStartHuytSao(player, tileHP);
                        EffectSkillService.gI().sendEffectPlayer(player, player, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.HUYT_SAO_EFFECT);
                        player.nPoint.calPoint();
                        player.nPoint.setHp(player.nPoint.hp + (int) ((long) player.nPoint.hp * tileHP / 100));
                        Service.getInstance().point(player);
                        Service.getInstance().Send_Info_NV(player);
                        ItemTimeService.gI().sendItemTime(player, 3781, 30);
                        PlayerService.gI().sendInfoHpMp(player);
                    }
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.TAI_TAO_NANG_LUONG:
                EffectSkillService.gI().startCharge(player);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.TU_SAT:
                if (!player.playerSkill.prepareTuSat) {
                    //gồng
                    player.playerSkill.prepareTuSat = !player.playerSkill.prepareTuSat;
                    player.playerSkill.lastTimePrepareTuSat = System.currentTimeMillis();
                    sendPlayerPrepareBom(player, (player.isAdmin2() ? 5 : 2000));
                } else {
                    if (!player.isBoss && !Util.canDoWithTime(player.playerSkill.lastTimePrepareTuSat, (player.isAdmin2() ? 5 : 1500))) {
                        player.playerSkill.skillSelect.lastTimeUseThisSkill = System.currentTimeMillis();
                        player.playerSkill.prepareTuSat = false;
                        return;
                    }
                    //nổ
                    player.playerSkill.prepareTuSat = !player.playerSkill.prepareTuSat;
                    int rangeBom = SkillUtil.getRangeBom(player.playerSkill.skillSelect.point);
                    double dame = Util.TamkjllGH(player.nPoint.hpMax);
                    for (Mob mob : player.zone.mobs) {
                        if (Util.getDistance(player, mob) <= rangeBom) {
                            mob.injured(player, dame, true);
                        }
                    }
                    List<Player> playersMap;
                    if (player.isBoss) {
                        playersMap = player.zone.getNotBosses();
                    } else {
                        playersMap = player.zone.getHumanoids();
                    }
                    if (!MapService.gI().isMapOffline(player.zone.map.mapId)) {
                        for (Player pl : playersMap) {
                           if (!player.equals(pl) && canAttackPlayer(player, pl)
                                    && Util.getDistance(player, pl) <= rangeBom) {
                                pl.injured(player, pl.isBoss ? dame /3 : dame, false, false);
                                PlayerService.gI().sendInfoHpMpMoney(pl);
                                Service.gI().Send_Info_NV(pl);
                            }
                        }
                    }
                    affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                    player.injured(null, 2100000000000000000L, true, false);
                    if (player.effectSkill.tiLeHPHuytSao != 0 || player.effectSkill.timeMonkey != 0) {
                        player.effectSkill.tiLeHPHuytSao = 0;
                        player.effectSkill.timeMonkey = 0;
                        ItemTimeService.gI().removeItemTime(player, 3781);
                        EffectSkillService.gI().removeHuytSao(player);
                        EffectSkillService.gI().monkeyDown(player);
                    }
                }
                break;
        }
    }

  public void useSkillBuffToPlayer(Player player, Player plTarget) {
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.TRI_THUONG:
                List<Player> playersMap = null;
                playersMap = player.zone.getNotBosses();
                Message msg = null;
                Player plTriThuong = null;
                for (Player pl : playersMap) {
                    if (pl != null && player != null && plTarget != null  && pl.zone != null && pl.zone.zoneId == player.zone.zoneId && !pl.isNewPet && !pl.isBoss && !plTarget.isBoss && !plTarget.isNewPet && canHsPlayer(player, plTarget)) {
                        if (player.playerSkill.skillSelect.point > 1) {
                            plTriThuong = pl;
                        } else if (player.playerSkill.skillSelect.point == 1) {
                            plTriThuong = plTarget;
                        }
                        if (plTriThuong != null) {
                            int percentTriThuong = SkillUtil.getPercentTriThuong(player.playerSkill.skillSelect.point);
                            Service.gI().chat(plTriThuong, "Cảm ơn " + player.name + " đã cứu mình");
                            try {
                                msg = new Message(-60);
                                msg.writer().writeInt((int) player.id); //id pem
                                msg.writer().writeByte(player.playerSkill.skillSelect.skillId); //skill pem
                                msg.writer().writeByte(1); //số người pem
                                msg.writer().writeInt((int) plTriThuong.id); //id ăn pem
                                msg.writer().writeByte(0); //read continue
                                msg.writer().writeByte(player.playerSkill.skillSelect.template.type); //type skill
                                Service.gI().sendMessAllPlayerInMap(plTriThuong.zone, msg);
                                msg.cleanup();
                                boolean isDie = plTriThuong.isDie();
                                player.nPoint.addHp((player.nPoint.hpMax * percentTriThuong / 100));
                                plTriThuong.nPoint.addHp((plTriThuong.nPoint.hpMax * percentTriThuong / 100));
                                plTriThuong.nPoint.addMp((plTriThuong.nPoint.mpMax * percentTriThuong / 100));
                                if (isDie) {
                                    Service.gI().Send_Info_NV(player);
                                    Service.gI().hsChar(plTriThuong,Util.TamkjllGH( plTriThuong.nPoint.getHP()),Util.TamkjllGH( plTriThuong.nPoint.getMP()));
                                    PlayerService.gI().sendInfoHpMpMoney(plTriThuong);
                                    PlayerService.gI().sendInfoHpMp(player);
                                } else {
                                    Service.gI().Send_Info_NV(player);
                                    PlayerService.gI().sendInfoHpMpMoney(plTriThuong);
                                    PlayerService.gI().sendInfoHpMp(player);
                                }
                                Service.gI().Send_Info_NV(plTriThuong);
                            } catch (IOException e) {
                                //                            e.printStackTrace();
                            }
                        }
                    }
                }
                if (player.isPl()) {
                    player.achievement.plusCount(14);
                }

                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                playersMap = null;
                break;
        }
    }

    public void phanSatThuong(Player plAtt, Player plTarget, double dame) {
        int percentPST = plTarget.nPoint.tlPST;
        if (percentPST != 0) {
            double damePST = (dame * percentPST / 100);
            Message msg;
            try {
                msg = new Message(56);
                msg.writer().writeInt((int) plAtt.id);
                if (damePST >= plAtt.nPoint.hp) {
                    damePST = plAtt.nPoint.hp - 1;
                }
                if (plAtt.isBoss && !plAtt.name.equals("Broly")) {
                    damePST = Util.nextInt(5);
                }
               damePST = plAtt.injured(null, damePST, true, false);
                msg.writer().writeInt(Util.TamkjllGH(plAtt.nPoint.hp));
                msg.writer().writeInt(Util.TamkjllGH(damePST));
                msg.writer().writeBoolean(false);
                msg.writer().writeByte(36);
                Service.getInstance().sendMessAllPlayerInMap(plAtt, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(SkillService.class, e);
            }
        }
    }

    private void hutHPMP(Player player, double dame, boolean attackMob) {
        int tiLeHutHp = player.nPoint.getTileHutHp(attackMob);
        int tiLeHutMp = player.nPoint.getTiLeHutMp();
        long hpHoi = (long)(dame * tiLeHutHp / 100);
        long mpHoi = (long)(dame * tiLeHutMp / 100);
        if (hpHoi > 0 || mpHoi > 0) {
            PlayerService.gI().hoiPhuc(player, hpHoi, mpHoi);
        }
    }

      private void playerAttackPlayer(Player plAtt, Player plInjure, boolean miss) {
        
       if (plInjure.effectSkill.anTroi) {
            plAtt.nPoint.isCrit100 = true;
        }
        double dameHit = plInjure.injured(plAtt, miss ? 0 : plAtt.nPoint.getDameAttack(false), false, false);
        phanSatThuong(plAtt, plInjure, dameHit);
        hutHPMP(plAtt, dameHit, false);
//        if (dameHit > 2123456789){
//                dameHit = 2123456000;
//            }
        Message msg;
        try {
            msg = new Message(-60);
            msg.writer().writeInt((int) plAtt.id); //id pem
            msg.writer().writeByte(plAtt.playerSkill.skillSelect.skillId); //skill pem
            msg.writer().writeByte(1); //số người pem
            msg.writer().writeInt((int) plInjure.id); //id ăn pem
            byte typeSkill = SkillUtil.getTyleSkillAttack(plAtt.playerSkill.skillSelect);
            msg.writer().writeByte(typeSkill == 2 ? 0 : 1); //read continue
            msg.writer().writeByte(0); //type skill
            if (dameHit > 2123456789) {
                if (plAtt.isPl()) {
                    plAtt.Hppl = "\n|8|Name Boss:\b|4|" + plInjure.name;
                    plAtt.Hppl += "\n|8|Hp Boss:\b|7|" + Util.powerToString(plInjure.nPoint.hp);
                    if (dameHit > 1) {
                        plAtt.Hppl += "\b|8|Dame lên Boss:\b|7|" + Util.powerToString(dameHit);
                    } else {
                        plAtt.Hppl += "\nDame lên Boss: HỤT";
                    }
                }
               
                if (dameHit > 2123455999 ) {
                    Service.getInstance().sendThongBao(plAtt, "|4|Dame thật: \b|5|" + Util.format(dameHit));
                }
                dameHit = 2123456000;
                msg.writer().writeInt(Util.TamkjllGH(dameHit)); //dame ăn
            } else {
                if (plAtt.isPl()) {
                    plAtt.Hppl = "\n|8|Name Boss:\b|4|" + plInjure.name;
                    plAtt.Hppl += "\n|8|Hp Boss:\b|7|" + Util.powerToString(plInjure.nPoint.hp);
                    if (dameHit > 1) {
                        plAtt.Hppl += "\b|8|Dame lên Boss:\b|7|" + Util.powerToString(dameHit);
                    } else {
                        plAtt.Hppl += "\nDame lên Boss: HỤT";
                    }
                }

               
                msg.writer().writeInt(Util.TamkjllGH(dameHit)); //dame ăn
            }

            msg.writer().writeBoolean(plInjure.isDie()); //is die
            msg.writer().writeBoolean(plAtt.nPoint.isCrit); //crit
            if (typeSkill != 1) {
                Service.getInstance().sendMessAllPlayerInMap(plAtt, msg);
                msg.cleanup();
            } else {
                plInjure.sendMessage(msg);
                msg.cleanup();
                msg = new Message(-60);
                msg.writer().writeInt((int) plAtt.id); //id pem
                msg.writer().writeByte(plAtt.playerSkill.skillSelect.skillId); //skill pem
                msg.writer().writeByte(1); //số người pem
                msg.writer().writeInt((int) plInjure.id); //id ăn pem
                msg.writer().writeByte(typeSkill == 2 ? 0 : 1); //read continue
                msg.writer().writeByte(0); //type skill
                msg.writer().writeInt(Util.TamkjllGH(dameHit)); //dame ăn
                msg.writer().writeBoolean(plInjure.isDie()); //is die
                msg.writer().writeBoolean(plAtt.nPoint.isCrit); //crit
                Service.getInstance().sendMessAnotherNotMeInMap(plInjure, msg);
                msg.cleanup();
            }
            try {
                msg = Service.getInstance().messageSubCommand((byte) 14);
                msg.writer().writeInt((int) plInjure.id);
                msg.writer().writeInt(Util.TamkjllGH(plInjure.nPoint.hp));
                msg.writer().writeByte(0);
                msg.writer().writeInt(Util.TamkjllGH(plInjure.nPoint.hpMax));
                Service.getInstance().sendMessAnotherNotMeInMap(plInjure, msg);
                msg.cleanup();
            } catch (Exception e) {

            }
            Service.getInstance().addSMTN(plInjure, (byte) 1, 1, false);
            if (plInjure.isDie() && !plAtt.isBoss) {
                plAtt.fightMabu.changePoint((byte) 5);
            }

        } catch (Exception e) {
            Logger.logException(SkillService.class, e);
        }

        if (plInjure.isBoss && plInjure.nPoint.hp >= 1000000000) {
            try {
                msg = new Message(44);
                msg.writer().writeInt((int) plInjure.id);
                msg.writer().writeUTF("Hp của ta:\b|7| " + Util.format(plInjure.nPoint.hp));
                Service.getInstance().sendMessAllPlayerInMap(plInjure, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

  private void playerAttackMob(Player plAtt, Mob mob, boolean miss, boolean dieWhenHpFull) {
        if (!mob.isDie()) {
            double dameHit = plAtt.nPoint.getDameAttack(true);
            if (plAtt.charms.tdBatTu > System.currentTimeMillis() && plAtt.nPoint.hp == 1) {
                dameHit = 0;
            }
            if (plAtt.charms.tdManhMe > System.currentTimeMillis()) {
                dameHit += (dameHit * 150 / 100);
            }
            if (plAtt.isPet) {
                if (((Pet) plAtt).charms.tdDeTu > System.currentTimeMillis()) {
                    dameHit *= 2;
                }
            }
            if (miss) {
                dameHit = 0;
            }
            hutHPMP(plAtt, dameHit, true);
            sendPlayerAttackMob(plAtt, mob);
            mob.injured(plAtt, dameHit, dieWhenHpFull);
//            plAtt.tt = "HP Đối Thủ: " + Util.powerToString(mob.point.hp) + "\nDAME gây ra: " + Util.powerToString(dameHit);
        }
    }

 private void SetTamkjll(Player p) {
        if (p != null) {
//            int lb = Util.nextInt(0, p.LbTamkjll);
            int ETk = Util.nextInt(30, 200);
            // ETk *= 2;
            
//            Calendar rightNow = Calendar.getInstance();
//            if (rightNow.get(11) >= 18 && rightNow.get(11) <= 24) {
//                ETk *= 2;
//            }
//           
//            p.expCS += ETk;
//            p.ctk = ETk;
//            long dk = p.capCS + 1;
//            dk *= 20000L;
//            p.exprank += ETk;
//           
//            long dk1 = p.rank + 1;
//            dk1 *= 100000L;
//            
//            if (p.autonangcapchuyensinh) {
//                if (dk > 1 && p.expCS > dk && p.capCS < 10000) {
//                    p.expCS -= dk;
//                    p.capCS++;
//                    Service.gI().sendThongBao(p, "Cấp Chuyển Sinh vừa lên 1 cấp. Cấp hiện tại: " + p.capCS);
//                }
//                if (dk1 > 1 && p.exprank > dk1 && p.rank < 1000) {
//                    p.exprank -= dk1;
//                    p.rank++;
//                    Service.gI().sendThongBao(p, "Cấp rank vừa lên 1 cấp. Cấp hiện tại: " + p.rank);
//                }
//               
//            
//        }
    }
    }
    private void sendPlayerPrepareSkill(Player player, int affterMiliseconds) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(4);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(player.playerSkill.skillSelect.skillId);
            msg.writer().writeShort(affterMiliseconds);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendPlayerPrepareBom(Player player, int affterMiliseconds) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(7);
            msg.writer().writeInt((int) player.id);
//            msg.writer().writeShort(player.playerSkill.skillSelect.skillId);
            msg.writer().writeShort(104);
            msg.writer().writeShort(affterMiliseconds);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public boolean canUseSkillWithMana(Player player) {
        if (player.playerSkill.skillSelect != null) {
            if (player.playerSkill.skillSelect.template.id == Skill.KAIOKEN) {
                long hpUse = player.nPoint.hpMax / 100 * 10;
                if (player.nPoint.hp <= hpUse) {
                    return false;
                }
            }
            switch (player.playerSkill.skillSelect.template.manaUseType) {
                case 0:
                    if (player.nPoint.mp >= player.playerSkill.skillSelect.manaUse) {
                        return true;
                    } else {
                        return false;
                    }
                case 1:
                    int mpUse = (int) (player.nPoint.mpMax * player.playerSkill.skillSelect.manaUse / 100);
                    if (player.nPoint.mp >= mpUse) {
                        return true;
                    } else {
                        return false;
                    }
                case 2:
                    if (player.nPoint.mp > 0) {
                        return true;
                    } else {
                        return false;
                    }
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean canUseSkillWithCooldown(Player player) {
        return Util.canDoWithTime(player.playerSkill.skillSelect.lastTimeUseThisSkill,
                player.playerSkill.skillSelect.coolDown - 50);
    }

    public void affterUseSkill(Player player, int skillId) {
        Intrinsic intrinsic = player.playerIntrinsic.intrinsic;
        switch (skillId) {
            case Skill.DICH_CHUYEN_TUC_THOI:
                if (intrinsic.id == 6) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
            case Skill.THOI_MIEN:
                if (intrinsic.id == 7) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
            case Skill.SOCOLA:
                if (intrinsic.id == 14) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
            case Skill.TROI:
                if (intrinsic.id == 22) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
        }
        setMpAffterUseSkill(player);
        setLastTimeUseSkill(player, skillId);
    }

    private void setMpAffterUseSkill(Player player) {
        if (player.playerSkill.skillSelect != null) {
            switch (player.playerSkill.skillSelect.template.manaUseType) {
                case 0:
                    if (player.nPoint.mp >= player.playerSkill.skillSelect.manaUse) {
                        player.nPoint.setMp(player.nPoint.mp - player.playerSkill.skillSelect.manaUse);
                    }
                    break;
                case 1:
                    int mpUse = (int) (player.nPoint.mpMax * player.playerSkill.skillSelect.manaUse / 100);
                    if (player.nPoint.mp >= mpUse) {
                        player.nPoint.setMp(player.nPoint.mp - mpUse);
                    }
                    break;
                case 2:
                    player.nPoint.setMp(0);
                    break;
            }
            PlayerService.gI().sendInfoHpMpMoney(player);
        }
    }

    private void setLastTimeUseSkill(Player player, int skillId) {
        Intrinsic intrinsic = player.playerIntrinsic.intrinsic;
        int subTimeParam = 0;
        switch (skillId) {
            case Skill.TRI_THUONG:
                if (intrinsic.id == 10) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.THAI_DUONG_HA_SAN:
                if (intrinsic.id == 3) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.QUA_CAU_KENH_KHI:
                if (intrinsic.id == 4) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.KHIEN_NANG_LUONG:
                if (intrinsic.id == 5 || intrinsic.id == 15 || intrinsic.id == 20) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.MAKANKOSAPPO:
                if (intrinsic.id == 11) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.DE_TRUNG:
                if (intrinsic.id == 12) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.TU_SAT:
                if (intrinsic.id == 19) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.HUYT_SAO:
                if (intrinsic.id == 21) {
                    subTimeParam = intrinsic.param1;
                }
                break;
        }
        int coolDown = player.playerSkill.skillSelect.coolDown;
        player.playerSkill.skillSelect.lastTimeUseThisSkill = System.currentTimeMillis() - (coolDown * subTimeParam / 100);
        if (subTimeParam != 0) {
            Service.getInstance().sendTimeSkill(player);
        }
    }

    private boolean canHsPlayer(Player player, Player plTarget) {
        if (plTarget == null) {
            return false;
        }
        if (plTarget.isBoss) {
            return false;
        }
        if (plTarget.typePk == ConstPlayer.PK_ALL) {
            return false;
        }
        if (plTarget.typePk == ConstPlayer.PK_PVP) {
            return false;
        }
        if (player.cFlag != 0) {
            if (plTarget.cFlag != 0 && plTarget.cFlag != player.cFlag) {
                return false;
            }
        } else if (plTarget.cFlag != 0) {
            return false;
        }
        return true;
    }

    private boolean canAttackPlayer(Player p1, Player p2) {
        if (p1.isDie() || p2.isDie()) {
            return false;
        }
        if (p1.zone.map.mapId == 129 && p1.typePk > 0 && p2.typePk > 0) {
            return true;
        }

        if (p1.typePk == ConstPlayer.PK_ALL || p2.typePk == ConstPlayer.PK_ALL) {
            return true;
        }
        if ((p1.cFlag != 0 && p2.cFlag != 0)
                && (p1.cFlag == 8 || p2.cFlag == 8 || p1.cFlag != p2.cFlag)) {
            return true;
        }
        if (p1.pvp == null || p2.pvp == null) {
            return false;
        }
        if (p1.pvp.isInPVP(p2) || p2.pvp.isInPVP(p1)) {
            return true;
        }
        return false;
    }

    private void sendPlayerAttackMob(Player plAtt, Mob mob) {
        Message msg;
        try {
            msg = new Message(54);
            msg.writer().writeInt((int) plAtt.id);
            msg.writer().writeByte(plAtt.playerSkill.skillSelect.skillId);
            msg.writer().writeByte(mob.id);
            Service.getInstance().sendMessAllPlayerInMap(plAtt, msg);
            msg.cleanup();

        } catch (IOException e) {
        }
    }

    public void selectSkill(Player player, int skillId) {
        Skill skillBefore = player.playerSkill.skillSelect;
        for (Skill skill : player.playerSkill.skills) {
            if (skill.skillId != -1 && skill.template.id == skillId) {
                player.playerSkill.skillSelect = skill;
                switch (skillBefore.template.id) {
                    case Skill.DRAGON:
                    case Skill.KAMEJOKO:
                    case Skill.DEMON:
                    case Skill.MASENKO:
                    case Skill.LIEN_HOAN:
                    case Skill.GALICK:
                    case Skill.ANTOMIC:
                        switch (skill.template.id) {
                            case Skill.DRAGON:
                            case Skill.KAMEJOKO:
                            case Skill.DEMON:
                            case Skill.MASENKO:
                            case Skill.LIEN_HOAN:
                            case Skill.GALICK:
                            case Skill.ANTOMIC:
//                                skill.lastTimeUseThisSkill = System.currentTimeMillis() + (skill.coolDown / 100);
                                break;
                        }
                        break;
                }
                break;
            }
        }
    }
}
