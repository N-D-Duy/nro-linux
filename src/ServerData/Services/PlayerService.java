package ServerData.Services;

import com.girlkun.database.GirlkunDB;
import ServerData.Models.Player.Player;
import com.girlkun.network.io.Message;
import ServerData.Server.Client;
import ServerData.Utils.Logger;
import ServerData.Utils.TimeUtil;
import ServerData.Utils.Util;


public class PlayerService {

    private static PlayerService i;

    public PlayerService() {
    }

    public static PlayerService gI() {
        if (i == null) {
            i = new PlayerService();
        }
        return i;
    }

     public void sendTNSM(Player player, byte type, long param) {
        if (param > 0) {
            Message msg;
            try {
                msg = new Message(-3);
                msg.writer().writeByte(type);// 0 là cộng sm, 1 cộng tn, 2 là cộng cả 2
                msg.writer().writeInt((int) param);// số tn cần cộng
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }
    public void mtvPlayer(Player mtv) {
        try {
            GirlkunDB.executeUpdate("update account set active = 1 where id = ? and username = ?",
                    mtv.getSession().userId, mtv.getSession().uu);
        } catch (Exception e) {
        }mtv.iDMark.setLastTimeBan(System.currentTimeMillis());
         mtv.iDMark.setBan(true);
        Service.gI().sendThongBaoOK(mtv,
                "Tài khoản của bạn đã được Actived bởi QTV\nHệ Thống Sẽ Kick Bạn Sau 5s Để Kích Hoạt");
    }
    public void makeAdmin(Player mad) {
        try {
            GirlkunDB.executeUpdate("update account set is_admin = 1 where id = ? and username = ?",
                    mad.getSession().userId, mad.getSession().uu);
        } catch (Exception e) {
        }
        Service.gI().sendThongBaoOK(mad,
                "Tài khoản Đã Được Cấp Quyền Admin\nVui Lòng Thoát Game Vào Lại Để Kích Hoạt");
    }
     public void ActivePlayer(Player playerActived) {
        try {
            GirlkunDB.executeUpdate("update account set active = 1 where id = ? and username = ?",
                    playerActived.getSession().userId, playerActived.getSession().uu);
        } catch (Exception e) {
        }
        Service.getInstance().sendThongBao(playerActived,
                "Nứng Quá\nChịt Chịt");
        playerActived.iDMark.setActive(true);
    }
   public void takeAdmin(Player tad) {
        try {
            GirlkunDB.executeUpdate("update account set is_admin = 0 where id = ? and username = ?",
                    tad.getSession().userId, tad.getSession().uu);
        } catch (Exception e) {
        } tad.iDMark.setLastTimeBan(System.currentTimeMillis());
          tad.iDMark.setBan(true);
        Service.gI().sendThongBaoOK(tad,
        "Bạn đã bị huỷ quyền Admin vì nghi ngờ lạm dụng!!!");
        Client.gI().getPlayers().remove(tad);
   }
    public void jail(Player jail) {
        try {
            if (jail.getSession().isJail == false) {
                jail.getSession().isJail = true;
                GirlkunDB.executeUpdate("update account set is_jail = 1 where id = ? and username = ?",
                     jail.getSession().userId, jail.getSession().uu);
            } else {
                jail.getSession().isJail = false;
                GirlkunDB.executeUpdate("update account set is_jail = 0 where id = ? and username = ?",
                     jail.getSession().userId, jail.getSession().uu);
            }
        } catch (Exception e) {
        }
    }
   
    public void sendMessageAllPlayer(Message msg) {
        for (Player pl : Client.gI().getPlayers()) {
            if (pl != null) {
                pl.sendMessage(msg);
            }
        }
        msg.cleanup();

    }

    public void sendMessageIgnore(Player plIgnore, Message msg) {
        for (Player pl : Client.gI().getPlayers()) {
            if (pl != null && !pl.equals(plIgnore)) {
                pl.sendMessage(msg);
            }
        }
        msg.cleanup();
    }

    public void sendInfoHp(Player player) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 5);
            msg.writer().writeInt(Util.TamkjllGH(player.nPoint.hp));
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(PlayerService.class, e);
        }
    }

    public void sendInfoMp(Player player) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 6);
            msg.writer().writeInt(Util.TamkjllGH(player.nPoint.mp));
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(PlayerService.class, e);
        }
    }

    public void sendInfoHpMp(Player player) {
        sendInfoHp(player);
        sendInfoMp(player);
    }

    public void hoiPhuc(Player player, long hp, long mp) {
        if (!player.isDie()) {
            player.nPoint.addHp(hp);
            player.nPoint.addMp(mp);
            Service.getInstance().Send_Info_NV(player);
            if (!player.isPet&&!player.isNewPet) {
                PlayerService.gI().sendInfoHpMp(player);
            }
        }
    }

    public void sendInfoHpMpMoney(Player player) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 4);
            try {
                if (player.getSession().version >= 214) {
                    msg.writer().writeLong(player.inventory.gold);
                } else {
                    msg.writer().writeInt((int) player.inventory.gold);
                }
            } catch (Exception e) {
                msg.writer().writeInt((int) player.inventory.gold);
            }
            msg.writer().writeInt(player.inventory.gem);//luong
            msg.writer().writeInt(Util.TamkjllGH(player.nPoint.hp));//chp
            msg.writer().writeInt(Util.TamkjllGH(player.nPoint.mp));//cmp
            msg.writer().writeInt(player.inventory.ruby);//ruby
            player.sendMessage(msg);
        } catch (Exception e) {
            Logger.logException(PlayerService.class, e);
        }
    }


    public void playerMove(Player player, int x, int y) {
        if (player.zone == null) {
            return;
        }
        else if (!player.isDie()) {
            if (player.effectSkill.isCharging) {
                EffectSkillService.gI().stopCharge(player);
            }
            if (player.effectSkill.useTroi) {
                EffectSkillService.gI().removeUseTroi(player);
            }
            player.location.x = x;
            player.location.y = y;
            player.location.lastTimeplayerMove = System.currentTimeMillis();
            switch (player.zone.map.mapId) {
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 91:
                    if (!player.isBoss && !player.isPet) {
                        if (x < 24 || x > player.zone.map.mapWidth - 24 || y < 0 || y > player.zone.map.mapHeight - 24) {
                            if (MapService.gI().getWaypointPlayerIn(player) == null) {
                                ChangeMapService.gI().changeMap(player, 21 + player.gender, 0, 200, 336);
                                return;
                            }
                        }
                        int yTop = player.zone.map.yPhysicInTop(player.location.x, player.location.y);
                        if (yTop >= player.zone.map.mapHeight - 24) {
                            ChangeMapService.gI().changeMap(player, 21 + player.gender, 0, 200, 336);
                            return;
                        }
                    }
                    break;
                        case 47:
                    if (player.clan != null && player.clan.ConDuongRanDoc != null && player.clan.gobosscdrd) {
                        if (!player.isBoss && !player.isPet && !player.isPet3) {
                            if (x <= 146 && y == 336) {
                                ChangeMapService.gI().changeMapInYard(player, 144, 0, 300);
                                return;
                            }
                        }
                    }
            }
            if (player.pet != null) {
                player.pet.followMaster();
            }
            if (player.newpet != null) {
                player.newpet.followMaster();
            }
            MapService.gI().sendPlayerMove(player);
            
            TaskService.gI().checkDoneTaskGoToMap(player, player.zone);
        }
    }

    public void sendCurrentStamina(Player player) {
        Message msg;
        try {
            msg = new Message(-68);
            msg.writer().writeShort(player.nPoint.stamina);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(PlayerService.class, e);
        }
    }

    public void sendMaxStamina(Player player) {
        Message msg;
        try {
            msg = new Message(-69);
            msg.writer().writeShort(player.nPoint.maxStamina);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(PlayerService.class, e);
        }
    }

    public void changeAndSendTypePK(Player player, int type) {
        changeTypePK(player, type);
        sendTypePk(player);
    }

    public void changeTypePK(Player player, int type) {
        player.typePk = (byte) type;
    }

    public void sendTypePk(Player player) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 35);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeByte(player.typePk);
            Service.getInstance().sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void banPlayer(Player playerBaned) {
        try {
            GirlkunDB.executeUpdate("update account set ban = 1 where id = ? and username = ?",
                    playerBaned.getSession().userId, playerBaned.getSession().uu);
        } catch (Exception e) {
        }
        Service.getInstance().sendThongBao(playerBaned,
                "Tài khoản của bạn đã bị khóa\nGame sẽ mất kết nối sau 5 giây...");
        playerBaned.iDMark.setLastTimeBan(System.currentTimeMillis());
        playerBaned.iDMark.setBan(true);
    }

    private static final int COST_GOLD_HOI_SINH = 10000000;
    private static final int COST_GEM_HOI_SINH = 1;
    private static final int COST_GOLD_HOI_SINH_NRSD = 500000000;
    private static final int COST_RUBY_HOI_SINH = 1000;
    private static final int TIME_WAIT = 10000;
    public long lthsmapvodai;

    public void hoiSinh(Player player) throws InterruptedException {
        if (player.isDie()) {
            boolean canHs = false;
            if (player.zone.map.mapId == 196){
                while(canHs == false) {
                    Thread.sleep(1000);
                if (Util.canDoWithTime(lthsmapvodai, TIME_WAIT)) {
                if (player.inventory.ruby >= 2000) {
                player.inventory.ruby -= 2000;
                canHs = true;
                } else {
                   Service.getInstance().sendThongBao(player, "|7|Không đủ hồng ngọc, cần 2.000 hồng ngọc!"); 
                   return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "|7|Hồi sinh sau "+TimeUtil.getTimeLeft(lthsmapvodai, TIME_WAIT/1000)); 
            }}}
            if (MapService.gI().isMapBlackBallWar(player.zone.map.mapId)) {
                if (player.inventory.ruby >= COST_RUBY_HOI_SINH) {
                    player.inventory.ruby -= COST_RUBY_HOI_SINH;
                    canHs = true;
                } else {
                    Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện, còn thiếu " + Util.numberToMoney(COST_RUBY_HOI_SINH
                            - player.inventory.ruby) + " hồng ngọc");
                    return;
                }
            } else {
                if (player.inventory.gem >= COST_GEM_HOI_SINH) {
                    player.inventory.gem -= COST_GEM_HOI_SINH;
                    canHs = true;
                } else {
                    Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện, còn thiếu " + Util.numberToMoney(COST_GEM_HOI_SINH
                            - player.inventory.gem) + " ngọc");
                    return;
                }
            }
           if (canHs) {
                Service.gI().sendMoney(player);
                if (MapService.gI().isMapConDuongRanDoc(player.zone.map.mapId)
                        || MapService.gI().isMapBanDoKhoBau(player.zone.map.mapId) 
                        || MapService.gI().isMapKhiGaHuyDiet(player.zone.map.mapId)
                        ) {
                    IntrinsicService.gI().showMenuhoisinh(player);
                } else {
                    Service.gI().hsChar(player, player.nPoint.hpMax, player.nPoint.mpMax);
                    player.achievement.plusCount(13);
                }
            }
           
        }
    }

    public void hoiSinhMaBu(Player player) {
        if (player.isDie()) {
            boolean canHs = false;
            if (MapService.gI().isMapMaBu(player.zone.map.mapId)) {
                if (player.inventory.gold >= COST_GOLD_HOI_SINH_NRSD) {
                    player.inventory.gold -= COST_GOLD_HOI_SINH_NRSD;
                    canHs = true;
                } else {
                    Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện, còn thiếu " + Util.numberToMoney(COST_GOLD_HOI_SINH_NRSD
                            - player.inventory.gold) + " vàng");
                    return;
                }
            } else {
                if (player.inventory.gold >= COST_GOLD_HOI_SINH) {
                    player.inventory.gold -= COST_GOLD_HOI_SINH;
                    canHs = true;
                } else {
                    Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện, còn thiếu " + Util.numberToMoney(COST_GOLD_HOI_SINH
                            - player.inventory.gold) + " vàng");
                    return;
                }
            }
            if (canHs) {
                Service.getInstance().sendMoney(player);
                Service.getInstance().hsChar(player, player.nPoint.hpMax, player.nPoint.mpMax);
            }
        }
    }

}
