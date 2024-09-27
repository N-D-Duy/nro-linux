package ServerData.Models.Map.ListMap;

import ServerData.Boss.ListBoss.DTDN.NinjaTim;
import ServerData.Boss.ListBoss.DTDN.RobotVeSi;
import ServerData.Boss.ListBoss.DTDN.TrungUyThep;
import ServerData.Boss.ListBoss.DTDN.TrungUyTrang;
import ServerData.Boss.ListBoss.DTDN.TrungUyXanhLo;
import ServerData.Models.Player.Player;
import ServerData.Services.Service;


public class DoanhTraiService {
    private static DoanhTraiService i;
    private DoanhTraiService() {
    }
    
    public static DoanhTraiService gI() {
        if (i == null) {
            i = new DoanhTraiService();
        }
        return i;
    }
    
    public void opendoanhtrai(Player player) {
        if (player.clan != null && player.clan.doanhTrai == null) {
            DoanhTrai doanhtrai = null;
                for (DoanhTrai dt : DoanhTrai.DOANH_TRAI) {
                  if (!dt.isOpened) {
                            doanhtrai= dt;
                            break;
                        }
                    }   
                if (doanhtrai != null) {
                          doanhtrai.opendoanhtrai(player, player.clan);
                        try {
                            long totalDame = 0;
                            long totalHp = 0;
                            for (Player play : player.clan.membersInGame) {
                                totalDame += play.nPoint.dame;
                                totalHp += play.nPoint.hpMax;
                            }
                            long dame = ( totalHp / 50) ;
                            long hp = (  totalDame  * 200);
                            if (dame >= 2000000000L) {
                                dame = 2000000000L;
                            }
                            if (hp >= 2000000000L) {
                                hp = 2000000000L;
                            }
                              long bossDamage = dame;
                           long bossMaxHealth = hp ;
                           bossDamage = Math.min(bossDamage, 200000000L);
                            bossMaxHealth = Math.min(bossMaxHealth, 2000000000L);
                             new TrungUyTrang(player.clan.doanhTrai.getMapById(59),
                                        (int) bossDamage,
                                    (int) bossMaxHealth
                 );
                   
                   new TrungUyXanhLo(player.clan.doanhTrai.getMapById(62),
                                        (int) bossDamage,
                                    (int) bossMaxHealth
                 );
                       new TrungUyThep(player.clan.doanhTrai.getMapById(55),
                                        (int) bossDamage,
                                    (int) bossMaxHealth
                 );
                         new NinjaTim(player.clan.doanhTrai.getMapById(54),
                                        (int) bossDamage,
                                    (int) bossMaxHealth
                 );    
                        new RobotVeSi(player.clan.doanhTrai.getMapById(57),
                                        (int) bossDamage,
                                    (int) bossMaxHealth
                 );    
                } catch (Exception e) {
                }
            } else {
                Service.getInstance().sendThongBao(player, "Doanh Trại đã đầy, vui lòng quay lại sau");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Bạn không có bang hội nên không thể đi");
        }
    }
}