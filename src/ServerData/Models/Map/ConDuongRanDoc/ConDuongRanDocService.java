package ServerData.Models.Map.ConDuongRanDoc;

import ServerData.Boss.ListBoss.ConDuongRanDoc.*;
import  ServerData.Models.Player.Player;

import ServerData.Utils.Logger;
import ServerData.Services.InventoryServiceNew;
import ServerData.Services.Service;

public class ConDuongRanDocService {

    private static ConDuongRanDocService i;

    private ConDuongRanDocService() {

    }

    public static ConDuongRanDocService gI() {
        if (i == null) {
            i = new ConDuongRanDocService();
        }
        return i;
    }

    public void openConDuongRanDoc(Player player, byte level) {
        if (level >= 1 && level <= 110) {
            if (player.clan != null && player.clan.ConDuongRanDoc == null) {
                    ConDuongRanDoc conDuongRanDoc = null;
                    for (ConDuongRanDoc cdrd : ConDuongRanDoc.CON_DUONG_RAN_DOCS) {
                        if (!cdrd.isOpened) {
                            conDuongRanDoc = cdrd;
                            break;
                        }
                    }
                    if (conDuongRanDoc != null) {
                        InventoryServiceNew.gI().sendItemBags(player);
                        conDuongRanDoc.openConDuongRanDoc(player, player.clan, level);
                        try {
                            long bossDamage = (20 * level);
                            long bossMaxHealth = (2 * level);
                            bossDamage = Math.min(bossDamage, 200000000L);
                            bossMaxHealth = Math.min(bossMaxHealth, 2000000000L);


                            Saibamen boss1  = new Saibamen(
                                    player.clan.ConDuongRanDoc.getMapById(144),
                                    player.clan.ConDuongRanDoc.level,
                                    (int) bossDamage,
                                    (int) bossMaxHealth
                            );
//                            Saibamen2 boss2 = new Saibamen2(
//                                    player.clan.ConDuongRanDoc.getMapById(144),
//                                    player.clan.ConDuongRanDoc.level,
//                                    (int) bossDamage,
//                                    (int) bossMaxHealth
//                            );
//                            Saibamen3 boss3 = new Saibamen3(
//                                    player.clan.ConDuongRanDoc.getMapById(144),
//                                    player.clan.ConDuongRanDoc.level,
//                                    (int) bossDamage,
//                                    (int) bossMaxHealth
//                            );
//                            Saibamen4 boss4 = new Saibamen4(
//                                    player.clan.ConDuongRanDoc.getMapById(144),
//                                    player.clan.ConDuongRanDoc.level,
//                                    (int) bossDamage,
//                                    (int) bossMaxHealth
//                            );
//                            Saibamen5 boss5 = new Saibamen5(
//                                    player.clan.ConDuongRanDoc.getMapById(144),
//                                    player.clan.ConDuongRanDoc.level,
//                                    (int) bossDamage,
//                                    (int) bossMaxHealth
//                            );
//                            Saibamen6 boss6 = new Saibamen6(
//                                    player.clan.ConDuongRanDoc.getMapById(144),
//                                    player.clan.ConDuongRanDoc.level,
//                                    (int) bossDamage,
//                                    (int) bossMaxHealth
//                            );
//                            Nappa boss = new Nappa(
//                                    player.clan.ConDuongRanDoc.getMapById(144),
//                                    player.clan.ConDuongRanDoc.level,
//                                    (int) bossDamage,
//                                    (int) bossMaxHealth
//                            );
//                            Vegeta boss7 = new Vegeta(
//                                    player.clan.ConDuongRanDoc.getMapById(144),
//                                    player.clan.ConDuongRanDoc.level,
//                                    (int) bossDamage,
//                                    (int) bossMaxHealth
//                            );
                        } catch (Exception exception) {
                            Logger.logException(ConDuongRanDocService.class, exception, "Error initializing boss");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Con Đường Rắn Độc Đã Đầy, vui lòng quay lại sau");
                    }

            } else {
                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
            }

        }
    }
}

