package ServerData.Models.Map.ListMap;

import ServerData.Models.Item.Item;
import ServerData.Boss.ListBoss.KGHD.DrLychee;
//import ServerData.Boss.ListBoss.KGHD.Hatchiyack;
import ServerData.Models.Map.ListMap.KhiGasHuyDiet;
import static ServerData.Models.Map.ListMap.KhiGasHuyDiet.TIME_KHI_GA_HUY_DIET;
import ServerData.Models.Map.Zone;
import ServerData.Models.Map.Mob.Mob;
import ServerData.Models.Player.Player;
import ServerData.Services.InventoryServiceNew;
import ServerData.Services.MapService;
import ServerData.Services.Service;
import ServerData.Services.ChangeMapService;
import ServerData.Utils.Logger;
import ServerData.Utils.Util;
import java.util.List;

/**
 *
 * @author TTS
 *
 */
public class KhiGasHuyDietService {

    private static KhiGasHuyDietService i;

    private KhiGasHuyDietService() {

    }

    public static KhiGasHuyDietService gI() {
        if (i == null) {
            i = new KhiGasHuyDietService();
        }
        return i;
    }
    
    public void openKhiGaHuyDiet(Player player, byte level) {
        if (level >= 1 && level <= 110) {
            if (player.clan != null && player.clan.KhiGaHuyDiet == null) {
                Item item = InventoryServiceNew.gI().findItemBag(player, 1246);
                if (item != null && item.quantity > 0) {
                    KhiGasHuyDiet khiGaHuyDiet = null;
                    for (KhiGasHuyDiet kghd : KhiGasHuyDiet.KHI_GA_HUY_DIETS) {
                        if (!kghd.isOpened) {
                            khiGaHuyDiet = kghd;
                            break;
                        }
                    }
                    if (khiGaHuyDiet != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                        InventoryServiceNew.gI().sendItemBags(player);
                        khiGaHuyDiet.openKhiGaHuyDiet(player, player.clan, level);
                        try {
                            long bossDamage = (20 * level);
                            long bossMaxHealth = (2 * level);
                            bossDamage = Math.min(bossDamage, 200000000L);
                            bossMaxHealth = Math.min(bossMaxHealth, 2000000000L);
                            
                            DrLychee boss = new DrLychee(
                                    player.clan.KhiGaHuyDiet.getMapById(148),
                                    player.clan.KhiGaHuyDiet.level,
                                    (int) bossDamage,
                                    (int) bossMaxHealth
                            );
//                            Hatchiyack boss2 = new Hatchiyack(
//                                    player.clan.KhiGaHuyDiet.getMapById(148),
//                                    player.clan.KhiGaHuyDiet.level,
//                                    (int) bossDamage,
//                                    (int) bossMaxHealth
//                            );
                        } catch (Exception exception) {
                            Logger.logException(KhiGasHuyDietService.class, exception, "Error initializing boss");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Bản đồ kho báu đã đầy, vui lòng quay lại sau");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Yêu cầu có viên nén khí Gas");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }
}

