package ServerData.Models.NPC;

import Server.Connect.NhiemVuThanhTich.ThanhTichPlayer;
import ServerData.Services.NpcService;
import ServerData.Services.PetService;
import ServerData.Services.InventoryServiceNew;
import ServerData.Services.TaskService;
import ServerData.Services.ClanService;
import ServerData.Services.OpenPowerService;
import ServerData.Services.ItemService;
import ServerData.Services.Service;
import ServerData.Services.PlayerService;
import ServerData.Services.ItemMapService;
import ServerData.Services.IntrinsicService;
//import ServerData.Services.NgocRongNamecService;
import ServerData.Services.FriendAndEnemyService;
import ServerData.Services.MapService;
import ServerData.Services.Giftcode.GiftcodeManager;
import Server.Data.Consts.ConstMap;
import ServerData.Models.Map.ListMap.BanDoKhoBau;
import ServerData.Models.Map.ListMap.BanDoKhoBauService;
import ServerData.Models.PVP.Matches.MartialCongressService;
import Server.Data.Consts.ConstNpc;
import Server.Data.Consts.ConstPlayer;
import Server.Data.Consts.ConstTask;
import com.girlkun.database.GirlkunDB;
import Server.Connect.PlayerDAO;
import Server.Data.Consts.ConstEvent;
import ServerData.Models.Item.Template;
import ServerData.Boss.Boss;
import ServerData.Boss.BossData;
import ServerData.Boss.BossID;
import ServerData.Boss.BossManager;
import ServerData.Boss.ListBoss.NhanBan;
import ServerData.Boss.BossStatus;
import ServerData.Boss.ListBoss.DuongTank;
import ServerData.Models.Clan.Clan;
import ServerData.Models.Clan.ClanMember;

import java.util.HashMap;
import java.util.List;

import ServerData.Services.ChangeMapService;
import ServerData.Services.Func.SummonDragon;
import static ServerData.Services.Func.SummonDragon.SHENRON_1_STAR_WISHES_1;
import static ServerData.Services.Func.SummonDragon.SHENRON_1_STAR_WISHES_2;
import static ServerData.Services.Func.SummonDragon.SHENRON_SAY;

import ServerData.Models.Player.Player;
import ServerData.Models.Item.Item;
import ServerData.Models.Item.Item.ItemOption;
import ServerData.Models.Map.Map;
import ServerData.Models.Map.Zone;
import ServerData.Models.Map.ListMap.NgocRongSaoDen;
import ServerData.Models.Map.ListMap.MapMaBu;
import ServerData.Models.Map.ListMap.DoanhTrai;
import ServerData.Models.Map.ListMap.DoanhTraiService;
import ServerData.Models.Map.ListMap.KhiGasHuyDiet;
import ServerData.Models.Map.ListMap.KhiGasHuyDietService;
import ServerData.Models.Player.NPoint;
import ServerData.Models.PVP.PVPService;
import ServerData.Models.PVP.Matches.DaiHoiVoThuat;
import ServerData.Models.PVP.Matches.DaiHoiVoThuatService;
import ServerData.Models.Shop.ShopServiceNew;
import ServerData.Models.Player.PlayerSkill.Skill;
import com.girlkun.network.server.GirlkunSessionManager;
import ServerData.Server.Client;
import ServerData.Server.Maintenance;
import ServerData.Server.Manager;
import ServerData.Server.ServerManager;
import ServerData.Server.ServerNotify;
import ServerData.Services.Func.CombineServiceNew;
import ServerData.Services.Func.Input;
import ServerData.Services.Func.LuckyRound;
import ServerData.Utils.Logger;
import ServerData.Utils.TimeUtil;
import ServerData.Utils.Util;
import java.util.ArrayList;
//import ServerData.Services.Func.TaiXiu.ChonAiDay;
//import ServerData.Services.Func.TaiXiu.TaiXiu_Old;
import ServerData.Utils.SkillUtil;
import ServerData.Models.Shop.ShopKyGuiService;
import static ServerData.Services.Func.CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP4;
import static ServerData.Services.Func.CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP5;
import static ServerData.Services.Func.CombineServiceNew.NANG_CAP_BONG_TAI_3_LEN_4;
import static ServerData.Services.Func.CombineServiceNew.NANG_CAP_BONG_TAI_CAP5;
import ServerData.Services.Func.UseItem;
import ServerData.Models.Map.ConDuongRanDoc.ConDuongRanDoc;
import ServerData.Models.Map.ConDuongRanDoc.ConDuongRanDocService;
import ServerData.Models.PVP.TOP;
import static ServerData.Services.Func.CombineServiceNew.PHAN_RA_DO_SKH_TL;
import ServerData.Services.TopService;
import com.girlkun.result.GirlkunResultSet;
import java.sql.Connection;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class NpcFactory {

    private static final int COST_HD = 50000000;

    //playerid - object
    /**
     *
     */
    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private NpcFactory() {
    }

    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            public void Npcchat(Player player) {
                String[] chat = {
                    "Giúp Ta đẫn Rồng Lộn Về Nhà",
                    "Em buông tay anh vì lí do gì ",
                    "Người hãy nói đi , đừng Bắt Anh phải nghĩ suy"
                };
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    int index = 0;

                    @Override
                    public void run() {
                        npcChat(player, chat[index]);
                        index = (index + 1) % chat.length;
                    }
                }, 6000, 6000);
            }

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu cần trang bị gì cứ đến chỗ tôi nhé\n Bạn đang có\b|7| " + player.diemhotong + " Điểm đi buôn", "Cửa\nhàng", "Hộ tống", "TOP Hộ Tống", "hướng dẫn\nhộ tống");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.TRAI_DAT) {
                                    ShopServiceNew.gI().opendShop(player, "BUNMA", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
                                }
                                break;

                            case 1:
                                Boss oldDuongTank = BossManager.gI().getBossById(Util.createIdDuongTank((int) player.id));
                                if (oldDuongTank != null) {
                                    this.npcChat(player, " Bé rồng đang được hộ tống" + oldDuongTank.zone.zoneId);
                                } else if (player.inventory.ruby < 10000 || player.playerTask.taskMain.id < 30) {
                                    //  player.inventory.ruby -= 2000;
                                    Service.gI().sendThongBao(player, "Nhà ngươi không đủ 10k hồng ngọc và đạt tới nhiệm vụ 30");
                                } else {

                                    BossData bossDataClone = new BossData(
                                            "Bé rồng do" + " " + player.name + " hộ tống",
                                            (byte) 2,
                                            new short[]{1489, 1490, 1491, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                                            100000,
                                            new long[]{player.nPoint.hpMax * 2},
                                            new int[]{103},
                                            new int[][]{
                                                {Skill.TAI_TAO_NANG_LUONG, 7, 15000}},
                                            new String[]{}, //text chat 1
                                            new String[]{}, //text chat 2
                                            new String[]{}, //text chat 3
                                            60
                                    );

                                    try {
                                        DuongTank dt = new DuongTank(Util.createIdDuongTank((int) player.id), bossDataClone, player.zone, player.location.x - 20, player.location.y);
                                        dt.playerTarger = player;
                                        int[] map = {6, 29, 30, 4, 5, 27, 28};
                                        dt.mapCongDuc = map[Util.nextInt(map.length)];
                                        player.haveDuongTang = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //trừ vàng khi gọi boss
                                    player.inventory.ruby -= 10000;
                                    Service.getInstance().sendMoney(player);
                                    break;
                                }
//                                case 2://Shop
//                                    ShopServiceNew.gI().opendShop(player, "QUA_BOSS", true);
//                                //     NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CHUA_UPDATE);
//                                
//                                break;
                            case 2:
                                Service.getInstance().sendThongBaoOK(player, TopService.gethotong());
                                break;
                            case 3:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_HO_TONG);

                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc kamui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                new Thread(() -> {
                    try {
                        while (true) {
                            String[] textLucy = {"|7|-----YTB KhanhDTK-----!", "|7|Chào mừng đến với NRO KAMUi !",
                                "|7|-----YTB KhanhDTK-----!"};
                            Thread.sleep(5000);
                            new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                    this.npcChat(player, textLucy[Util.nextInt(0, textLucy.length - 1)]);
                                } catch (Exception e) {
                                }
                            }).start();
                        }
                    } catch (Exception e) {
                    }
                }).start();
                if (canOpenNpc(player)) {

                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào mừng đến với NRO KAMUI ! Cậu muốn tìm hiểu gì?",
                            "Sự kiện", "Vàng", "Hồng ngọc", "Cơ chế SKH", "Phó bản", "Leo tháp",
                            "Đệ tử", "Cải trang", "Hợp Thể GOGETA", "Boss khung giờ", "Hộ tống", "Võ đài", "Bông tai",
                            "Đua top");
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                Service.gI().sendPopUpMultiLine(player, tempId, avartar, "Sự Kiện Đang Cập Nhật");
                                break;
                            case 1:
                                Service.gI().sendPopUpMultiLine(player, tempId, avartar, "Khi đánh quái ở tất cả các map có tỉ lệ rơi vàng ngẫu nhiên, đánh boss cũng có tỉ lệ rơi ra thỏi vàng");
                                break;
                            case 2:
                                Service.gI().sendPopUpMultiLine(player, tempId, avartar, "Nhập Code, Đánh Boss để nhận hồng ngọc miễn phí");
                                break;
                            case 3:
                                Service.gI().sendPopUpMultiLine(player, tempId, avartar, "up skh thường ở 3 map đầu, có 2 loại SKH ( Thường - Thần Linh ), mỗi loại chia ra 3 level, mỗi level sẽ có 1 nội tại riêng, khi mặc đủ 5 món sẽ hiện danh hiệu đặc quyền");
                                break;
                            case 4:
                                Service.gI().sendPopUpMultiLine(player, tempId, avartar, "Phó Bản Hiện Tại Đang Cập Nhật");
                                break;
                            case 5:
                                Service.gI().sendPopUpMultiLine(player, tempId, avartar, "Đang cập nhật thêm... ");
                                break;
                            case 6:
                                Service.gI().sendPopUpMultiLine(player, tempId, avartar, "Có 6 loại đệ tử, 3 loại ở Shop, 3 loại săn broly, 3 loại ở shop là cumber (30% chỉ số), vegeta ssj4 (50% chỉ số), goku ssj4 (50% chỉ số)" + " Đệ săn từ broly gồm mabu mập (20), ra đảo kame thức tỉnh đệ mabu mập có tỉ lệ ra 2 đệ sau bư han(25%), bư gầy (45%)");
                                break;
                            case 7:
                                Service.gI().sendPopUpMultiLine(player, tempId, avartar, "Cải trang mới cực nhiều");
                                break;
                            case 8:
                                Service.gI().sendPopUpMultiLine(player, tempId, avartar, "Hợp thể GOGETA khi sử dụng bông tai khi đủ điều kiện sau:\n -Có đệ vegeta ssj4 hoặc goku ssj4\n - Sư phụ phải mặc cải trang ssj4 hoặc có option Gogeta\n -chỉ số sau khi hợp thể Gogeta tăng 20%");
                                break;
                            default:
                                this.npcChat(player, "Đang cập nhật thêm... ");
                                break;
                        }
                    }

                }
            }
        };
    }

    private static Npc chubedan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104 || this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|6|Bạn muốn đổi hồn linh thú cho mình sao?:\b|7|Cần có X99 Hồn Linh Thú + 5 thỏi vàng", "Đổi Trứng\nLinh thú", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104 || this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Item honLinhThu = null;
                                    Item thoivang = null;
                                    try {
                                        thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 2029);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 Hồn Linh thú");
                                    } else if (thoivang == null || thoivang.quantity < 5) {
                                        this.npcChat(player, "Bạn không đủ 5 Thỏi vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 5);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                        Service.getInstance().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 2028);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Trứng Linh thú");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

 private static Npc trongTai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                try {
                  if (this.mapId == 113) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đại hội võ thuật Siêu Hạng \ndiễn ra 24/7 kể cả ngày lễ và chủ nhật"
                                + "\nHãy thi đấu để khẳng định đẳng cấp của mình nhé",
                                "Top 100\nCao thủ", "Top Siêu Hạng", "Đấu ngay","SHOP", "Về\nĐại Hội\nVõ Thuật");
                    } else if (this.mapId == 166) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ta có thể giúp gì cho ngươi?", "Nhận thưởng", "Quay về");
                    }
                } catch (Exception ex) {
                    Logger.error("Lỗi mở menu trọng tài");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
              
                    if (this.mapId == 113) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    try (Connection con = GirlkunDB.getConnection()) {
                                    Manager.topSieuHang = Manager.realTopSieuHang2(con);
                                } catch (Exception ignored) {
                                    Logger.error("Lỗi đọc top");
                                }
                                Service.gI().showListTop(player, Manager.topSieuHang, (byte) 1);
                                break;
                                case 1:
                                    this.createOtherMenu(player, 1, "|7|Ngươi ở top " + player.rankSieuHang + " ,Mỗi ngày sẽ chốt Top giải siêu hạng một lần !",
                                            "Xem \nGiải Thưởng");
                                    break;
                                case 2:
                                try {
                                    List<TOP> tops = Manager.realTopSieuHang2(player);

                                    if (tops != null && !tops.isEmpty()) {
                                        Service.gI().showListTop(player, tops, (byte) 1);
                                        tops.clear();
                                    } else {
                                        System.out.println("Danh sách TOP trống hoặc không có.");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                                    case 3: // shop
                                    ShopServiceNew.gI().opendShop(player, "SHOP_SIEU_HANG", false);
                                    break;
                                case 4:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, -1, 432);
                                    break;
                                    
                            }
                        } else if (player.iDMark.getIndexMenu() == 1) {
                            switch (select) {
//                                case 0:
//                                    if (!player.getSession().isnhanthuong1) {
//                                        LocalDate currentDate = LocalDate.now();
//                                        LocalDate lastClaimDate = Instant.ofEpochMilli(player.getSession().timesieuhang).atZone(ZoneId.systemDefault()).toLocalDate();
//        
//                                        if (lastClaimDate.isEqual(currentDate)) {
//                                            Service.gI().sendThongBao(player, "Bạn đã nhận thưởng rồi trong ngày hôm nay");
//                                       } else {
//                                            
//                                            if (player.rankSieuHang ==1) {
//                                                
//                                                player.inventory.ruby += 200000;
//                                                
//                                            } else
//                                            if (player.rankSieuHang >=2 && player.rankSieuHang <=3) {
//                                                player.inventory.ruby += 100000;
//                                                  
//                                               
//                                            } else if (player.rankSieuHang >=4 && player.rankSieuHang <=10) {
//                                                player.inventory.ruby += 70000;
//                                                 
//                                                
//                                            } else if (player.rankSieuHang >=11 && player.rankSieuHang <=100) {
//                                                player.inventory.ruby += 50000;
//                                                   
//                                            }
//                                        
//            PlayerService.gI().sendInfoHpMpMoney(player);
//            Service.gI().sendThongBao(player, "Nhận thưởng thành công");
//            
//            // Cập nhật thông tin ngày nhận thưởng
//            player.getSession().timesieuhang = System.currentTimeMillis();
//            player.getSession().isnhanthuong1 = true;
//            PlayerDAO.nhanthuong1(player);
//        }
//        if (!lastClaimDate.isEqual(currentDate)) {
//    player.getSession().isnhanthuong1 = false;
//}
//    } else {
//        Service.gI().sendThongBao(player, "Bạn đã nhận thưởng rồi");
//    }
//    break;
                                    
                            }
                        }

                    }
                }

            }
        ;
    }

    ;
    }

//    private static Npc maygap(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 5) {
//                        this.createOtherMenu(player, 1234, "|7|- •⊹٭DragonBall Kamui٭⊹• -\n|2|MÁY GẮP PET HAY, LINH THÚ\nGẮP THƯỜNG : 1 XU/LƯỢT\nGẮP CAO CẤP : 3 XU/LƯỢT\nGẮP VIP : 5 XU/LƯỢT\n"
//                                +"CHỌN CÁC TÙY CHỌN BÊN DƯỚI ĐỂ XEM THÊM THÔNG TIN CHI TIẾT\n|7|MỌI ITEM SẼ ĐƯỢC ĐẨY VÀO RƯƠNG PHỤ NẾU HÀNH TRANG ĐẦY!",
//                        "Gắp Thường","Gắp Cao Cấp","Gắp VIP","Cửa Hàng","Xem Top","Rương Đồ");
//                    }
//                }
//            }
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 5) {
//                        if (player.iDMark.getIndexMenu()==1234) {
//                            switch (select) {
//                                case 0:
//                                    this.createOtherMenu(player, 12345, "|7|- •⊹٭DragonBall Kamui٭⊹• -\nGẮP THƯỜNG : 5-10% CHỈ SỐ\n|3|GẮP X1 : GẮP THỦ CÔNG\nGẮP X10 : AUTO X10 LẦN GẮP\nGẮP X100 : AUTO X100 LẦN GẮP\n"+"|7|LƯU Ý : MỌI CHỈ SỐ ĐỀU RANDOM KHÔNG CÓ OPTION NHẤT ĐỊNH\nNẾU MUỐN NGƯNG AUTO GẤP CHỈ CẦN THOÁT GAME VÀ VÀO LẠI!",
//                                     "Gắp x1","Gắp x10","Gắp x100","Rương Đồ");
//                                    break;
//                                case 1:
//                                    this.createOtherMenu(player, 12346, "|7|- •⊹٭DragonBall Kamui٭⊹• -\n"+"GẮP CAO CẤP : 10-20% CHỈ SỐ\n|3|GẮP X1 : GẮP THỦ CÔNG\nGẮP X10 : AUTO X10 LẦN GẮP\nGẮP X100 : AUTO X100 LẦN GẮP\n"+"|7|LƯU Ý : MỌI CHỈ SỐ ĐỀU RANDOM KHÔNG CÓ OPTION NHẤT ĐỊNH\nNẾU MUỐN NGƯNG AUTO GẤP CHỈ CẦN THOÁT GAME VÀ VÀO LẠI!",
//                                     "Gắp x1","Gắp x10","Gắp x100","Rương Đồ");
//                                    break;
//                                case 2:
//                                    this.createOtherMenu(player, 12347, "|7|- •⊹٭DragonBall Kamui٭⊹• -\nGẮP VIP : 15-25% CHỈ SỐ\n|3|GẮP X1 : GẮP THỦ CÔNG\nGẮP X10 : AUTO X10 LẦN GẮP\nGẮP X100 : AUTO X100 LẦN GẮP\n"+"|7|LƯU Ý : MỌI CHỈ SỐ ĐỀU RANDOM KHÔNG CÓ OPTION NHẤT ĐỊNH\nNẾU MUỐN NGƯNG AUTO GẤP CHỈ CẦN THOÁT GAME VÀ VÀO LẠI!",
//                                     "Gắp x1","Gắp x10","Gắp x100","Rương Đồ");
//                                    break;
//                                case 3: //
//                                    ShopServiceNew.gI().opendShop(player, "PET", true);
//                                    break;
//                                case 4:
//                                    Service.gI().sendThongBaoFromAdmin(player, "Số điểm đã gắp của bạn : "+player.gtPoint);
//                                    break;
//                                case 5:
//                                    this.createOtherMenu(player, ConstNpc.RUONG_PHU,
//                                            "|1|Tình yêu như một dây đàn\n" +
//                                            "Tình vừa được thì đàn đứt dây\n" +
//                                            "Đứt dây này anh thay dây khác\n" +
//                                            "Mất em rồi anh biết thay ai?",
//                                            "Rương Phụ\n("+ (player.inventory.itemsBoxCrackBall.size()
//                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
//                                            + "/200)",
//                                            "Xóa Hết\nRương Phụ", "Đóng");
//                                    break;
//                                }
//                            } else if (player.iDMark.getIndexMenu() == 12345) {
//                            switch (select) { 
//                                case 0:
//                                    Item xuthuong = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386);
//                                    if (xuthuong == null) {
//                                        this.createOtherMenu(player, 12345, "|2|HẾT TIỀN!\n|7|CẦN TỐI THIỂU 1 XU GẮP THÚ, HÃY QUAY LẠI SAU!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                    }
//                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                        Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
//                                        return;
//                                    }
//                                        InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag,xuthuong, 1);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                    Item gapt = Util.petrandom(Util.nextInt(1143,1154));
//                                    if(Util.isTrue(1, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createNewItem((short)1385));
//                                        InventoryServiceNew.gI().sendItemBags(player);  
//                                        this.createOtherMenu(player, 12345, "|6|Gắp hụt rồi, bạn bỏ cuộc sao?"+"\nSố xu còn : "+xuthuong.quantity+"\n|7|Chiến tiếp ngay!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                    }
//                                    if(Util.isTrue(10, 100)) {
//                                        player.gtPoint += 1;
//                                        InventoryServiceNew.gI().addItemBag(player, gapt);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        this.createOtherMenu(player, 12345, "|2|Bạn vừa gắp được : "+gapt.template.name+"\nSố xu còn : "+xuthuong.quantity+"\n|7|Chiến tiếp ngay!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                    } else {
//                                        this.createOtherMenu(player, 12345, "|6|Gắp hụt rồi, bạn bỏ cuộc sao?"+"\nSố xu còn : "+xuthuong.quantity+"\n|7|Chiến tiếp ngay!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                    }
//                                    break;
//                                case 1:
//                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386) == null) {
//                                        this.createOtherMenu(player, 12345, "|2|HẾT TIỀN!\n|7|CẦN TỐI THIỂU 1 XU GẮP THÚ, HÃY QUAY LẠI SAU!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                    }
//                                    try {
//                                    Service.gI().sendThongBao(player, "Tiến hành auto gắp x10 lần");
//                                    int timex10 = 10;
//                                    int count = 0;
//                                    while (timex10 > 0) {
//                                        timex10--;
//                                        count++;
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386),1);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386) == null) {
//                                        this.createOtherMenu(player, 12345, "|7|HẾT XU!\nSỐ LƯỢT ĐÃ GẮP : "+count,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                        }
//                                        if(1+player.inventory.itemsBoxCrackBall.size() > 200) {
//                                        this.createOtherMenu(player, 12345, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT GẮP : "+count+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                        }
//                                        Thread.sleep(100);
//                                        Item gapx10 = Util.petrandom(Util.nextInt(1143,1154));  
//                                        if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                        if(Util.isTrue(1, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createNewItem((short)1385));
//                                        InventoryServiceNew.gI().sendItemBags(player);    
//                                        }    
//                                        if(Util.isTrue(10, 100)) {
//                                        player.gtPoint += 1;    
//                                        InventoryServiceNew.gI().addItemBag(player, gapx10);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã gắp được : "+gapx10.template.name+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        } else {
//                                        this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        }}
//                                        if(InventoryServiceNew.gI().getCountEmptyBag(player) == 0){
//                                        if(Util.isTrue(1, 100)) {
//                                        player.inventory.itemsBoxCrackBall.add(ItemService.gI().createNewItem((short)1385));
//                                        }    
//                                        if(Util.isTrue(10, 100)) {
//                                        player.gtPoint += 1;
//                                        player.inventory.itemsBoxCrackBall.add(gapx10);
//                                        this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã gắp được : "+gapx10.template.name+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        } else {
//                                        this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                    }}}} catch (Exception e) {
//                                    }
//                                    break;
//                                case 2:
//                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386) == null) {
//                                        this.createOtherMenu(player, 12345, "|2|HẾT TIỀN!\n|7|CẦN TỐI THIỂU 1 XU GẮP THÚ, HÃY QUAY LẠI SAU!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                    }
//                                    try {
//                                    Service.gI().sendThongBao(player, "Tiến hành auto gắp x10 lần");
//                                    int timex100 = 100;
//                                    int count = 0;
//                                    while (timex100 > 0) {
//                                        timex100--;
//                                        count++;
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386),1);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386) == null) {
//                                        this.createOtherMenu(player, 12345, "|7|HẾT XU!\nSỐ LƯỢT ĐÃ GẮP : "+count,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                        }
//                                        if(1+player.inventory.itemsBoxCrackBall.size() > 200) {
//                                        this.createOtherMenu(player, 12345, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT GẮP : "+count+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                        }
//                                        Thread.sleep(100);
//                                        Item gapx100 = Util.petrandom(Util.nextInt(1143,1154));  
//                                        if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                        if(Util.isTrue(1, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createNewItem((short)1385));
//                                        InventoryServiceNew.gI().sendItemBags(player);    
//                                        }
//                                        if(Util.isTrue(10, 100)) {
//                                        player.gtPoint += 1;
//                                        InventoryServiceNew.gI().addItemBag(player, gapx100);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã gắp được : "+gapx100.template.name+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        } else {
//                                        this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        }}
//                                        if(InventoryServiceNew.gI().getCountEmptyBag(player) == 0){
//                                        if(Util.isTrue(1, 100)) {
//                                        player.inventory.itemsBoxCrackBall.add(ItemService.gI().createNewItem((short)1385));
//                                        }
//                                        if(Util.isTrue(10, 100)) {
//                                        player.gtPoint += 1;
//                                        player.inventory.itemsBoxCrackBall.add(gapx100);
//                                        this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã gắp được : "+gapx100.template.name+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        } else {
//                                        this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                    }}}} catch (Exception e) {
//                                    }
//                                    break;
//                                case 3:
//                                    this.createOtherMenu(player, ConstNpc.RUONG_PHU,
//                                            "|1|Tình yêu như một dây đàn\n" +
//                                            "Tình vừa được thì đàn đứt dây\n" +
//                                            "Đứt dây này anh thay dây khác\n" +
//                                            "Mất em rồi anh biết thay ai?",
//                                            "Rương Phụ\n("+ (player.inventory.itemsBoxCrackBall.size()
//                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
//                                            + "/200)",
//                                            "Xóa Hết\nRương Phụ", "Đóng");
//                                    break;
//                            }
//                        } else if (player.iDMark.getIndexMenu() == 12346) {
//                            switch (select) { 
//                                case 0:
//                                    Item xucc = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386);
//                                    if (xucc == null || xucc.quantity < 3) {
//                                        this.createOtherMenu(player, 12346, "|2|HẾT TIỀN!\n|7|CẦN TỐI THIỂU 3 XU GẮP THÚ, HÃY QUAY LẠI SAU!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                    }
//                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                        Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
//                                        return;
//                                    }
//                                        InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag,xucc, 3);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                    Item gapt = Util.petccrandom(Util.nextInt(1167,1178));
//                                    if(Util.isTrue(5, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createNewItem((short)1385));
//                                        InventoryServiceNew.gI().sendItemBags(player);    
//                                        this.createOtherMenu(player, 12346, "|6|Gắp hụt rồi, bạn bỏ cuộc sao?"+"\nSố xu còn : "+xucc.quantity+"\n|7|Chiến tiếp ngay!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                    }
//                                    if(Util.isTrue(1, 300)) {
//                                        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createNewItem((short)1384));
//                                        InventoryServiceNew.gI().sendItemBags(player);  
//                                        this.createOtherMenu(player, 12346, "|6|Gắp hụt rồi, bạn bỏ cuộc sao?"+"\nSố xu còn : "+xucc.quantity+"\n|7|Chiến tiếp ngay!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                    }
//                                    if(Util.isTrue(10, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, gapt);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        player.gtPoint += 1;
//                                        this.createOtherMenu(player, 12346, "|2|Bạn vừa gắp được : "+gapt.template.name+"\nSố xu còn : "+xucc.quantity+"\n|7|Chiến tiếp ngay!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                    } else {
//                                        this.createOtherMenu(player, 12346, "|6|Gắp hụt rồi, bạn bỏ cuộc sao?"+"\nSố xu còn : "+xucc.quantity+"\n|7|Chiến tiếp ngay!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                    }
//                                    break;
//                                case 1:
//                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386) == null || InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity < 3) {
//                                        this.createOtherMenu(player, 12346, "|2|HẾT TIỀN!\n|7|CẦN TỐI THIỂU 3 XU GẮP THÚ, HÃY QUAY LẠI SAU!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                    }
//                                    try {
//                                    Service.gI().sendThongBao(player, "Tiến hành auto gắp x10 lần");
//                                    int timex10 = 10;
//                                    int count = 0;
//                                    while (timex10 > 0) {
//                                        timex10--;
//                                        count++;
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386),3);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386) == null || InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity < 3) {
//                                        this.createOtherMenu(player, 12346, "|7|HẾT XU!\nSỐ LƯỢT ĐÃ GẮP : "+count,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                        }
//                                        if(1+player.inventory.itemsBoxCrackBall.size() > 200) {
//                                        this.createOtherMenu(player, 12346, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT GẮP : "+count+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                        }
//                                        Thread.sleep(100);
//                                        Item gapx10 = Util.petccrandom(Util.nextInt(1167,1178));  
//                                        if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                        if(Util.isTrue(5, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createNewItem((short)1385));
//                                        InventoryServiceNew.gI().sendItemBags(player);    
//                                        }
//                                        if(Util.isTrue(1, 300)) {
//                                        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createNewItem((short)1384));
//                                        InventoryServiceNew.gI().sendItemBags(player);    
//                                        }
//                                        if(Util.isTrue(10, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, gapx10);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        player.gtPoint += 1;
//                                        this.createOtherMenu(player, 12346, "|7|ĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã gắp được : "+gapx10.template.name+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        } else {
//                                        this.createOtherMenu(player, 12346, "|7|ĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        }}
//                                        if(InventoryServiceNew.gI().getCountEmptyBag(player) == 0){
//                                        if(Util.isTrue(5, 100)) {
//                                        player.inventory.itemsBoxCrackBall.add(ItemService.gI().createNewItem((short)1385));
//                                        } 
//                                        if(Util.isTrue(1, 300)) {
//                                        player.inventory.itemsBoxCrackBall.add(ItemService.gI().createNewItem((short)1384));
//                                        }
//                                        if(Util.isTrue(10, 100)) {
//                                        player.gtPoint += 1;
//                                        player.inventory.itemsBoxCrackBall.add(gapx10);
//                                        this.createOtherMenu(player, 12346, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã gắp được : "+gapx10.template.name+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        } else {
//                                        this.createOtherMenu(player, 12346, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                    }}}} catch (Exception e) {
//                                    }
//                                    break;
//                                case 2:
//                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386) == null || InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity < 5) {
//                                        this.createOtherMenu(player, 12346, "|2|HẾT TIỀN!\n|7|CẦN TỐI THIỂU 3 XU GẮP THÚ, HÃY QUAY LẠI SAU!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                    }
//                                    try {
//                                    Service.gI().sendThongBao(player, "Tiến hành auto gắp x100 lần");
//                                    int timex100 = 100;
//                                    int count = 0;
//                                    while (timex100 > 0) {
//                                        timex100--;
//                                        count++;
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386),3);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386) == null || InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity < 3) {
//                                        this.createOtherMenu(player, 12346, "|7|HẾT XU!\nSỐ LƯỢT ĐÃ GẮP : "+count,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                        }
//                                        if(1+player.inventory.itemsBoxCrackBall.size() > 200) {
//                                        this.createOtherMenu(player, 12346, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT GẮP : "+count+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                        }
//                                        Thread.sleep(100);
//                                        Item gapx100 = Util.petccrandom(Util.nextInt(1167,1178));  
//                                        if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                        if(Util.isTrue(5, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createNewItem((short)1385));
//                                        InventoryServiceNew.gI().sendItemBags(player);    
//                                        }
//                                        if(Util.isTrue(1, 300)) {
//                                        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createNewItem((short)1384));
//                                        InventoryServiceNew.gI().sendItemBags(player);    
//                                        }
//                                        if(Util.isTrue(10, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, gapx100);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        player.gtPoint += 1;
//                                        this.createOtherMenu(player, 12346, "|7|ĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã gắp được : "+gapx100.template.name+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        } else {
//                                        this.createOtherMenu(player, 12346, "|7|ĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        }}
//                                        if(InventoryServiceNew.gI().getCountEmptyBag(player) == 0){
//                                        if(Util.isTrue(5, 100)) {
//                                        player.inventory.itemsBoxCrackBall.add(ItemService.gI().createNewItem((short)1385));
//                                        } 
//                                        if(Util.isTrue(1, 300)) {
//                                        player.inventory.itemsBoxCrackBall.add(ItemService.gI().createNewItem((short)1384));
//                                        }    
//                                        if(Util.isTrue(10, 100)) {
//                                        player.gtPoint += 1;
//                                        player.inventory.itemsBoxCrackBall.add(gapx100);
//                                        this.createOtherMenu(player, 12346, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã gắp được : "+gapx100.template.name+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        } else {
//                                        this.createOtherMenu(player, 12346, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                    }}}} catch (Exception e) {
//                                    }
//                                    break;
//                                case 3:
//                                    this.createOtherMenu(player, ConstNpc.RUONG_PHU,
//                                            "|1|Tình yêu như một dây đàn\n" +
//                                            "Tình vừa được thì đàn đứt dây\n" +
//                                            "Đứt dây này anh thay dây khác\n" +
//                                            "Mất em rồi anh biết thay ai?",
//                                            "Rương Phụ\n("+ (player.inventory.itemsBoxCrackBall.size()
//                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
//                                            + "/200)",
//                                            "Xóa Hết\nRương Phụ", "Đóng");
//                                    break;
//                            }
//                        } else if (player.iDMark.getIndexMenu() == 12347) {
//                            switch (select) { 
//                                case 0:
//                                    Item xuvip = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386);
//                                    if (xuvip == null || xuvip.quantity < 5) {
//                                        this.createOtherMenu(player, 12347, "|2|HẾT TIỀN!\n|7|CẦN TỐI THIỂU 5 XU GẮP THÚ, HÃY QUAY LẠI SAU!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                    }
//                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                        Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
//                                        return;
//                                    }
//                                        InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag,xuvip, 5);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.gI().sendMoney(player);
//                                    Item gapt = Util.petviprandom(Util.nextInt(1280,1295));
//                                    Item gapt1 = Util.petviprandom(Util.nextInt(1417,1422));
//                                    if(Util.isTrue(5, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, gapt1);
//                                        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createNewItem((short)1385));
//                                        InventoryServiceNew.gI().sendItemBags(player);    
//                                        this.createOtherMenu(player, 12347, "|6|Gắp được Xu Bạc!!"+"\nSố xu còn : "+xuvip.quantity+"\n|7|Chiến tiếp ngay!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        }
//                                        if(Util.isTrue(1, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createNewItem((short)1384));
//                                        InventoryServiceNew.gI().sendItemBags(player);  
//                                        this.createOtherMenu(player, 12347, "|6|Gắp được Xu Vàng!!"+"\nSố xu còn : "+xuvip.quantity+"\n|7|Chiến tiếp ngay!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        }
//                                    if(Util.isTrue(10, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, gapt);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        player.gtPoint += 1;
//                                        this.createOtherMenu(player, 12347, "|2|Bạn vừa gắp được : "+gapt.template.name+"\nSố xu còn : "+xuvip.quantity+"\n|7|Chiến tiếp ngay!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                    } else {
//                                        this.createOtherMenu(player, 12347, "|6|Gắp hụt rồi, bạn bỏ cuộc sao?"+"\nSố xu còn : "+xuvip.quantity+"\n|7|Chiến tiếp ngay!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                    }
//                                    break;
//                                case 1:
//                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386) == null || InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity < 5) {
//                                        this.createOtherMenu(player, 12347, "|2|HẾT TIỀN!\n|7|CẦN TỐI THIỂU 5 XU GẮP THÚ, HÃY QUAY LẠI SAU!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                    }
//                                    try {
//                                    Service.gI().sendThongBao(player, "Tiến hành auto gắp x10 lần");
//                                    int timex10 = 10;
//                                    int count = 0;
//                                    while (timex10 > 0) {
//                                        timex10--;
//                                        count++;
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386),5);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386) == null || InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity < 3) {
//                                        this.createOtherMenu(player, 12347, "|7|HẾT XU!\nSỐ LƯỢT ĐÃ GẮP : "+count,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                        }
//                                        if(1+player.inventory.itemsBoxCrackBall.size() > 200) {
//                                        this.createOtherMenu(player, 12347, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT GẮP : "+count+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                        }
//                                        Thread.sleep(100);
//                                        Item gapx10 = Util.petviprandom(Util.nextInt(1280,1295));  
//                                        Item gapx101 = Util.petviprandom(Util.nextInt(1417,1422));
//                                        if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                        if(Util.isTrue(5, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, gapx101);    
//                                        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createNewItem((short)1385));
//                                        InventoryServiceNew.gI().sendItemBags(player);    
//                                        this.createOtherMenu(player, 12347, "|6|Gắp được Xu Bạc!!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|Chiến tiếp ngay!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        }
//                                        if(Util.isTrue(1, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createNewItem((short)1384));
//                                        InventoryServiceNew.gI().sendItemBags(player);  
//                                        this.createOtherMenu(player, 12347, "|6|Gắp được Xu Vàng!!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|Chiến tiếp ngay!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        }
//                                        if(Util.isTrue(10, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, gapx10);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        player.gtPoint += 1;
//                                        this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã gắp được : "+gapx10.template.name+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        } else {
//                                        this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        }}
//                                        if(InventoryServiceNew.gI().getCountEmptyBag(player) == 0){
//                                        if(Util.isTrue(5, 100)) {
//                                        player.inventory.itemsBoxCrackBall.add(ItemService.gI().createNewItem((short)1385));
//                                        } 
//                                        if(Util.isTrue(1, 100)) {
//                                        player.inventory.itemsBoxCrackBall.add(ItemService.gI().createNewItem((short)1384));
//                                        }    
//                                        if(Util.isTrue(10, 100)) {
//                                        player.gtPoint += 1;
//                                        player.inventory.itemsBoxCrackBall.add(gapx10);
//                                        this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã gắp được : "+gapx10.template.name+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        } else {
//                                        this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                    }}}} catch (Exception e) {
//                                    }
//                                    break;
//                                case 2:
//                                    if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386) == null || InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity < 5) {
//                                        this.createOtherMenu(player, 12347, "|2|HẾT TIỀN!\n|7|CẦN TỐI THIỂU 5 XU GẮP THÚ, HÃY QUAY LẠI SAU!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                    }
//                                    try {
//                                    Service.gI().sendThongBao(player, "Tiến hành auto gắp x100 lần");
//                                    int timex100 = 100;
//                                    int count = 0;
//                                    while (timex100 > 0) {
//                                        timex100--;
//                                        count++;
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386),5);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        if (InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386) == null || InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity < 3) {
//                                        this.createOtherMenu(player, 12347, "|7|HẾT XU!\nSỐ LƯỢT ĐÃ GẮP : "+count,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                        }
//                                        if(1+player.inventory.itemsBoxCrackBall.size() > 200) {
//                                        this.createOtherMenu(player, 12347, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT GẮP : "+count+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        break;
//                                        }
//                                        Thread.sleep(100);
//                                        Item gapx100 = Util.petviprandom(Util.nextInt(1280,1295));  
//                                        Item gapx1001 = Util.petviprandom(Util.nextInt(1417,1422));
//                                        if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                        if(Util.isTrue(5, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, gapx1001);
//                                        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createNewItem((short)1385));
//                                        InventoryServiceNew.gI().sendItemBags(player);    
//                                        this.createOtherMenu(player, 12347, "|6|Gắp được Xu Bạc!!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|Chiến tiếp ngay!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        }
//                                        if(Util.isTrue(1, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createNewItem((short)1384));
//                                        InventoryServiceNew.gI().sendItemBags(player);  
//                                        this.createOtherMenu(player, 12347, "|6|Gắp được Xu Vàng!!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|Chiến tiếp ngay!",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        }
//                                        if(Util.isTrue(10, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(player, gapx100);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        player.gtPoint += 1;
//                                        this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã gắp được : "+gapx100.template.name+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        } else {
//                                        this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        }}
//                                        if(InventoryServiceNew.gI().getCountEmptyBag(player) == 0){
//                                        if(Util.isTrue(5, 100)) {
//                                        player.inventory.itemsBoxCrackBall.add(ItemService.gI().createNewItem((short)1385));
//                                        } 
//                                        if(Util.isTrue(1, 100)) {
//                                        player.inventory.itemsBoxCrackBall.add(ItemService.gI().createNewItem((short)1384));
//                                        }     
//                                        if(Util.isTrue(10, 100)) {
//                                        player.gtPoint += 1;
//                                        player.inventory.itemsBoxCrackBall.add(gapx100);
//                                        this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã gắp được : "+gapx100.template.name+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                        } else {
//                                        this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO CAO CẤP X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố xu còn : "+InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1386).quantity+"\n|7|TỔNG ĐIỂM : "+player.gtPoint,
//                                        "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
//                                    }}}} catch (Exception e) {
//                                    }
//                                    break;
//                                case 3:
//                                    this.createOtherMenu(player, ConstNpc.RUONG_PHU,
//                                            "|4|Tình yêu như một dây đàn\n" +
//                                            "Tình vừa được thì đàn đứt dây\n" +
//                                            "Đứt dây này anh thay dây khác\n" +
//                                            "Mất em rồi anh biết thay ai?",
//                                            "Rương Phụ\n("+ (player.inventory.itemsBoxCrackBall.size()
//                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
//                                            + "/200)",
//                                            "Xóa Hết\nRương Phụ", "Đóng");
//                                    break;
//                            }
//                        } else if (player.iDMark.getIndexMenu() == ConstNpc.RUONG_PHU) {
//                            switch (select) {
//                                case 0:
//                                    ShopServiceNew.gI().opendShop(player, "RUONG_PHU", true);
//                                    break;
//                                case 1:
//                                    NpcService.gI().createMenuConMeo(player,
//                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
//                                            "|3|Bạn chắc muốn xóa hết vật phẩm trong rương phụ?\n"
//                                                    +"|7|Sau khi xóa sẽ không thể khôi phục!",
//                                            "Đồng ý", "Hủy bỏ");
//                                    break;
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }
    private static Npc kyGui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, 0, "Cửa hàng chúng tôi chuyên mua bán hàng hiệu, hàng độc, cảm ơn bạn đã ghé thăm.", "Hướng\ndẫn\nthêm", "Mua bán\nKý gửi", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    switch (select) {
                        case 0:
                            Service.getInstance().sendPopUpMultiLine(pl, tempId, avartar, "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bChỉ với 3000 hồng ngọc\bGiá trị ký gửi 1-1000 thỏi vàng hoặc 1-100k hồng ngọc\bMột người bán, vạn người mua, mại dô, mại dô");
                            break;
                        case 1:

                            if (pl.nPoint.power < 17000000000L) {
                                Service.gI().sendThongBao(pl, "Vui lòng đạt 17 tỷ sức mạnh và đạt nhiệm vụ 22");
                            } else if (pl.playerTask.taskMain.id < 30) {
                                Service.gI().sendThongBao(pl, "Vui lòng đạt nhiệm vụ 30");
                            } else {
                                ShopKyGuiService.gI().openShopKyGui(pl);
                            }
                            break;

                    }
                }
            }
        };
    }

    private static Npc poTaGe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đa vũ trụ song song \b|7|Con muốn gọi con trong đa vũ trụ \b|1|Với giá 200tr vàng không?", "Gọi Boss\nNhân bản", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.nhanban >= 5) {
                                        Service.gI().sendThongBao(player, "Đã quá lượt gọi nhân bản trong ngày hôm nay");
                                        return;
                                    }
                                    Boss oldBossClone = BossManager.gI().getBossById(Util.createIdBossClone((int) player.id));
                                    if (oldBossClone != null) {
                                        this.npcChat(player, "Nhà ngươi hãy tiêu diệt Boss lúc trước gọi ra đã, con boss đó đang ở khu " + oldBossClone.zone.zoneId);
                                        return;
                                    }
                                    if (player.inventory.gold < 200_000_000) {
                                        this.npcChat(player, "Nhà ngươi không đủ 200 Triệu vàng ");
                                    } else {
                                        player.nhanban++;
                                        List<Skill> skillList = new ArrayList<>();
                                        for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                            Skill skill = player.playerSkill.skills.get(i);
                                            if (skill.point > 0) {
                                                skillList.add(skill);
                                            }
                                        }
                                        int[][] skillTemp = new int[skillList.size()][3];
                                        for (byte i = 0; i < skillList.size(); i++) {
                                            Skill skill = skillList.get(i);
                                            if (skill.point > 0) {
                                                skillTemp[i][0] = skill.template.id;
                                                skillTemp[i][1] = skill.point;
                                                skillTemp[i][2] = skill.coolDown;
                                            }
                                        }
                                        BossData bossDataClone = new BossData(
                                                "Nhân Bản " + player.name,
                                                player.gender,
                                                new short[]{player.getHead(), player.getBody(), player.getLeg(), player.getFlagBag(), player.idAura, player.getEffFront()},
                                                player.nPoint.dame,
                                                new long[]{player.nPoint.hpMax},
                                                new int[]{140},
                                                skillTemp,
                                                new String[]{"|-2|Boss nhân bản đã xuất hiện rồi"}, //text chat 1
                                                new String[]{"|-1|Ta sẽ chiếm lấy thân xác của ngươi hahaha!"}, //text chat 2
                                                new String[]{"|-1|Lần khác ta sẽ xử đẹp ngươi"}, //text chat 3
                                                60);
                                        try {
                                            new NhanBan(Util.createIdBossClone((int) player.id), bossDataClone, player.zone);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //trừ vàng khi gọi boss
                                        player.inventory.gold -= 200_000_000;
                                        Service.gI().sendMoney(player);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc quyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.getSession().is_gift_box) {
//                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp gì nào?", "Giải tán bang hội", "Nhận quà\nđền bù");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, Ta có thể hỗ trợ nhiệm vụ 11, 13, 27",
                                    "Kho Báu\nDưới Biển", "Chức Năng\nBang Hội", "Hỗ Trợ\nNhiệm Vụ", "Nhận Quà\nTích Luỹ", "Hồi đến\n80% Skill","Đóng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.clan != null) {
                                    if (player.clan.BanDoKhoBau != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                                "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                + player.clan.BanDoKhoBau.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                                "Đây là bản đồ kho báu \nCác con cứ yên tâm lên đường\n"
                                                + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                            case 1:
                                this.createOtherMenu(player, ConstNpc.CHUC_NANG_BANG_HOI,
                                        "Ta có hỗ trợ những chức năng Bang hội, nhà ngươi cần gì?", "Giải tán\nBang Hội", "Nâng cấp\nBang Hội", "Quyên Góp\nCapsule", "Lãnh địa\nBang Hội", "Từ chối");
                                break;
//                            case 2:
//                                if (player.getSession().player.nPoint.power == 1) {
//                                    Input.gI().createFormChooseLevelBDKB(player);
//                                } else {
//                                    this.npcChat(player, "Con đã lấy được thứ ta cần chưa?");
//                                }
//                                break;
                            case 2:
                                switch (player.playerTask.taskMain.id) {
                                    case 11:
                                        switch (player.playerTask.taskMain.index) {
                                            case 0:
                                                TaskService.gI().doneTask(player, ConstTask.TASK_11_0);
                                                Service.gI().sendThongBao(player, "Nhiệm vụ hiện tại là "
                                                        + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                                                break;
                                            case 1:
                                                for (int i = player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count; i < 25; i++) {
                                                    TaskService.gI().doneTask(player, ConstTask.TASK_11_1);
                                                    Service.gI().sendThongBao(player, "Nhiệm vụ hiện tại là "
                                                            + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                                                }
                                                break;
                                            case 2:
                                                for (int i = player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count; i < 25; i++) {
                                                    TaskService.gI().doneTask(player, ConstTask.TASK_11_2);
                                                    Service.gI().sendThongBao(player, "Nhiệm vụ hiện tại là "
                                                            + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                                                }
                                                break;
                                            default:
                                                Service.getInstance().sendThongBao(player, "Ta đã giúp con hoàn thành nhiệm vụ rồi mau đi trả nhiệm vụ");
                                                break;
                                        }
                                        break;
                                    case 13:
                                        if (player.playerTask.taskMain.index == 0) {
                                            TaskService.gI().doneTask(player, ConstTask.TASK_13_0);
                                            Service.gI().sendThongBao(player, "Nhiệm vụ hiện tại là "
                                                    + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Ta đã giúp con hoàn thành nhiệm vụ rồi mau đi trả nhiệm vụ");
                                        }
                                        break;
                                    case 27:
                                        switch (player.playerTask.taskMain.index) {
                                            case 0:
                                                for (int i = player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count; i < 15; i++) {
                                                    TaskService.gI().doneTask(player, ConstTask.TASK_27_0);
                                                    Service.gI().sendThongBao(player, "Nhiệm vụ hiện tại là "
                                                            + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                                                }
                                                break;
                                            case 1:
                                                for (int i = player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count; i < 15; i++) {
                                                    TaskService.gI().doneTask(player, ConstTask.TASK_27_1);
                                                    Service.gI().sendThongBao(player, "Nhiệm vụ hiện tại là "
                                                            + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                                                }
                                                break;
                                            case 2:
                                                for (int i = player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count; i < 15; i++) {
                                                    TaskService.gI().doneTask(player, ConstTask.TASK_27_2);
                                                    Service.gI().sendThongBao(player, "Nhiệm vụ hiện tại là "
                                                            + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                                                }
                                                break;
                                            default:
                                                Service.getInstance().sendThongBao(player, "Nhiệm vụ hiện tại không thuộc diện hỗ trợ");
                                                break;
                                        }
                                        break;
                                    default:
                                        Service.getInstance().sendThongBao(player, "Nhiệm vụ hiện tại không thuộc diện hỗ trợ");
                                        break;
                                }
                            case 3:
                                createOtherMenu(player, 0,
                                        "Xin chào\n|2|Hiện tại con đã nạp : " + Util.format(player.getSession().tongnap) + " VNĐ"
                                        + "\nVà đang có: " + player.pointSb + " Điểm Săn Boss"
                                        + "\n|5|Đã nhận quà mốc nạp: " + (player.mocnap > 0 ? Util.format(player.mocnap) + " VNĐ" : "Hiện chưa nhận mốc nạp nào")
                                        + "\nĐã nhận mốc săn boss: " + (player.mocsanboss > 0 ? Util.format(player.mocsanboss) + " Điểm" : "Hiện chưa nhận mốc điểm nào")
                                        + "\n|7|(Các Mốc Sẽ Được Resert Vào Chủ Nhật Hàng Tuần)"
                                        + "\n|-1|Con muốn nhận phần thường nào?",
                                        "Nhận Quà\nMốc Nạp", "Nhận Quà\nSăn Boss");
                                break;
                            case 4:
                                this.createOtherMenu(player, ConstNpc.HOI_SKILL,
                                        "Đưa ta 50 thỏi vàng để hồi skill cấp tốc",
                                        "Dạ", "Hông");
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.CHUC_NANG_BANG_HOI) {
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                if (clan != null) {
                                    ClanMember cm = clan.getClanMember((int) player.id);
                                    if (cm != null) {
                                        if (!clan.isLeader(player)) {
                                            Service.gI().sendThongBao(player, "Yêu cầu phải là bang chủ!");
                                            break;
                                        }
                                        if (clan.members.size() > 1) {
                                            Service.gI().sendThongBao(player, "Yêu cầu bang hội chỉ còn một thành viên!");
                                            break;
                                        }
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Bạn có chắc chắn muốn giải tán bang hội?\n( Yêu cầu sẽ không thể hoàn tác )",
                                                "Đồng ý", "Từ chối!");
                                        break;
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                break;
                            case 1:
                                if (player.clan != null) {
                                    if (!player.clan.isLeader(player)) {
                                        Service.gI().sendThongBao(player, "Yêu cầu phải là bang chủ!");
                                        break;
                                    }
                                    if (player.clan.level >= 0 && player.clan.level <= 10) {
                                        this.createOtherMenu(player, ConstNpc.CHUC_NANG_BANG_HOI2,
                                                "Bạn có muốn Nâng cấp lên " + (player.clan.maxMember + 1) + " thành viên không?\n"
                                                + "Cần 2000 Capsule Bang\n"
                                                + "(Thu thập Capsule Bang bằng cách tiêu diệt quái tại Map Ngũ Hành Sơn \n"
                                                + "cùng các thành viên khác)", "Nâng cấp\n(20K Ruby)", "Từ chối");
                                    } else {
                                        Service.gI().sendThongBao(player, "Bang của bạn đã đạt cấp tối đa!");
                                        break;
                                    }
                                    break;
                                } else if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                    break;
                                }
                                break;
                            case 2:
                                if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                    break;
                                }
                                Input.gI().DonateCsbang(player);
                                break;
                            case 3:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                } else if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                    break;
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 4:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                } else if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                    break;
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.HOI_SKILL) {
                        switch (select) {
                            case 0:
                                if (InventoryServiceNew.gI().findItemBag(player, 457).isNotNullItem()
                                        && InventoryServiceNew.gI().findItemBag(player, 457).quantity >= 50) {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player,
                                            InventoryServiceNew.gI().findItemBag(player, 457), 50);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    int subTimeParam=80;
                                    int coolDown = player.playerSkill.skillSelect.coolDown;
                                    player.playerSkill.skillSelect.lastTimeUseThisSkill = System.currentTimeMillis() - (coolDown * subTimeParam / 100);
                                    if (subTimeParam != 0) {
                                        Service.getInstance().sendTimeSkill(player);
                                    }
                                    return;
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Không có tiền mà đòi hít ... thơm");

                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.CHUC_NANG_BANG_HOI2) {
                        Clan clan = player.clan;
                        switch (select) {
                            case 0:
                                if (player.clan.capsuleClan >= 2000 && clan.isLeader(player) && player.inventory.ruby >= 20000) {
                                    player.clan.level += 1;
                                    player.clan.maxMember += 1;
                                    player.clan.capsuleClan -= 2000;
                                    player.inventory.subRuby(20000);
                                    player.clan.update();
                                    Service.gI().sendThongBao(player, "Yêu cầu nâng cấp bang hội thành công");
                                    break;
                                } else if (player.inventory.ruby < 20000) {
                                    Service.gI().sendThongBaoOK(player, "Bạn còn thiều " + (20000 - player.inventory.ruby) + " Hồng Ngọc");
                                    break;
                                } else if (player.clan.capsuleClan < 1000) {
                                    Service.gI().sendThongBaoOK(player, "Bang của bạn còn thiều " + (2000 - player.clan.capsuleClan) + " Capsule bang");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                        switch (select) {
                            case 0:
                                if ( player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    ChangeMapService.gI().goToDBKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                        switch (select) {
                            case 0:
                                if ( player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    Input.gI().createFormChooseLevelBDKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                        switch (select) {
                            case 0:
                                BanDoKhoBauService.gI().openBanDoKhoBau(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 0) {
                        switch (select) {
                            case 0:
                                createOtherMenu(player, 1,
                                        "Xin chào"
                                        + "\n|2|Hiện tại con đã nạp được: " + Util.format(player.getSession().tongnap) + " VNĐ"
                                        + "\n|-1|Con hãy chọn mốc nạp muốn nhận?",
                                        "Xem Mốc\n100K VNĐ", "Xem Mốc\n200K VNĐ", "Xem Mốc\n300K VNĐ",
                                        "Xem Mốc\n500K VNĐ", "Xem Mốc\n800K VNĐ", "Xem Mốc\n1 TRIỆU VNĐ", "Xem Mốc\n1 TRIỆU 5 VNĐ","Xem Mốc\n 2TR VND");
                                break;
                            case 1:
                                createOtherMenu(player, 3,
                                        "Xin chào"
                                        + "\n|2|Hiện tại con có: " + player.pointSb + " điểm Săn Boss"
                                        + "\n|-1|Con hãy chọn mốc thưởng săn boss muốn nhận?",
                                        "Xem Mốc\n100 ĐIỂM", "Xem Mốc\n200 ĐIỂM", "Xem Mốc\n300 ĐIỂM",
                                        "Xem Mốc\n500 ĐIỂM", "Xem Mốc\n800 ĐIỂM", "Xem Mốc\n1000 ĐIỂM");
                                break;
                        }
                        /*Mốc Nạp*/                    } else if (player.iDMark.getIndexMenu() == 1) {
                        switch (select) {
                            case 0:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 100.000 VNĐ Tuần 1 Gồm:"
                                        + "\n|2|- 200 Thỏi Vàng"
                                        + "\n- 10 Máy Dò Boss"
                                        + "\n- 10 Hộp Đồ Kích Hoạt Thường (Random)"
                                        + "\n- 5 Vé Ngũ Hành Sơn"
                                        + "\n|7|Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                break;
                            case 1:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 200.000 VNĐ Tuần 1 Gồm:"
                                        + "\n|2|- 300 Thỏi Vàng"
                                  
                                        + "\n- 10 Máy Dò Boss"
                                        + "\n- 40 Đá Nâng SKH Thường"
                                        + "\n- 5 Vé Ngũ Hành Sơn"
                                        + "\n|7|Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                break;
                            case 2:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 300.000 VNĐ Tuần 1 Gồm:"
                                        + "\n|2|- 400 Thỏi Vàng"
                                    
                                        + "\n- 10 Máy Dò Boss"
                                        + "\n- 300 Đá Nâng SKH Thường"
                                        + "\n- 5 Vé Ngũ Hành Sơn"
                                        + "\n|7|Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                break;
                            case 3:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 500.000 VNĐ Tuần 1 Gồm:"
                                        + "\n|2|- 500 Thỏi Vàng"
                                        + "\n- 20 Sao Pha Lê Đặc Biệt (Random)"
                                
                                        + "\n- 20 Máy Dò Boss"
                                        + "\n- 500 Đá Nâng SKH Thường"
                                        + "\n- 10 Vé Ngũ Hành Sơn"
                                        + "\n|7|Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                break;
                            case 4:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 800.000 VNĐ Tuần 1 Gồm:"
                                        + "\n|2|- 700 Thỏi Vàng"
                                        + "\n- 30 Sao Pha Lê Đặc Biệt (Random)"
                                  
                                        + "\n- 20 Máy Dò Boss"
                                        + "\n- 800 Đá Nâng SKH Thường"
                                        + "\n- 15 Vé Ngũ Hành Sơn"
                                        + "\n|7|Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                break;
                            case 5:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 1.000.000 VNĐ Tuần 1 Gồm:"
                                        + "\n|2|- 1000 Thỏi Vàng"
                                        + "\n- 50 Sao Pha Lê Đặc Biệt (Random)"
                                        + "\n- Danh Hiệu Đại Gia Mới Nhú (10% all chỉ số)" 
                                        + "\n- 50 Đá Homet"        
                                        + "\n- 50 Máy Dò Boss"
                                        + "\n- 1 Hòm SKH Thần Linh [Level 0] (Tự Chọn) "
                                        + "\n- 30 Vé Ngũ Hành Sơn"
                                        + "\n|7| Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                break;
                            case 6:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 1.500.000 VNĐ Tuần 1 Gồm:"
                                        + "\n|2|- 1500 Thỏi Vàng"
                                        + "\n- 80 Sao Pha Lê Đặc Biệt (Random)"
                                        + "\n- 150 Đá Homet"        
                                        + "\n- 70 Máy Dò Boss"
                                        + "\n- 300 Đá Nâng SKH VIP"
                                        + "\n- 50 Vé Ngũ Hành Sơn"
                                        + "\n|7| Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                break;
                                   case 7:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 2.000.000 VNĐ Tuần 1 Gồm:"
                                        + "\n|2|- 2000 Thỏi Vàng"
                                        + "\n- 100 Sao Pha Lê Đặc Biệt (Random)"
                                        + "\n- 200 Đá Homet"              
                                        + "\n- 100 Máy Dò Boss"
                                        + "\n- 500 Đá Nâng SKH VIP"
                                        + "\n- 70 Vé Ngũ Hành Sơn"
                                        + "\n|7| Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                break;
                        }
                        /*Mốc Săn Boss*/                    } else if (player.iDMark.getIndexMenu() == 3) {
                        switch (select) {
                            case 0:
                                createOtherMenu(player, 4,
                                        "|7|Phần Thưởng Mốc Diệt Boss Đạt 100 Điểm Gồm:"
                                        + "\n|2|- 55 Đá Hoàng Kim"
                                        + "\n- 111 Đá Thức Tỉnh"
                                        + "\n- 20 Thỏi Vàng"
                                        + "\n- 5 Máy Dò Boss"
                                        + "\n|7|Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                break;
                            case 1:
                                createOtherMenu(player, 4,
                                        "|7|Phần Thưởng Mốc Diệt Boss Đạt 200 Điểm Gồm:"
                                        + "\n|2|- 111 Đá Hoàng Kim"
                                        + "\n- 222 Đá Thức Tỉnh"
                                        + "\n- 30 Thỏi Vàng"
                                        + "\n- 5 Máy Dò Boss"
                                        + "\n|7| Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                break;
                            case 2:
                                createOtherMenu(player, 4,
                                        "|7|Phần Thưởng Mốc Diệt Boss Đạt 300 Điểm Gồm:"
                                        + "\n|2|- 161 Đá Hoàng Kim"
                                        + "\n- 333 Đá Thức Tỉnh"
                                        + "\n- 50 Thỏi Vàng"
                                        + "\n- 5 Máy Dò Boss"
                                        + "\n|7| Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                break;
                            case 3:
                                createOtherMenu(player, 4,
                                        "|7|Phần Thưởng Mốc Diệt Boss Đạt 500 Điểm Gồm:"
                                        + "\n|2|- 252 Đá Hoàng Kim"
                                        + "\n- 555 Đá Thức Tỉnh"
                                        + "\n- 150 Thỏi Vàng"
                                        + "\n- 15 Máy Dò Boss"
                                        + "\n|7| Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                break;
                            case 4:
                                createOtherMenu(player, 4,
                                        "|7|Phần Thưởng Mốc Diệt Boss Đạt 800 Điểm Gồm:"
                                        + "\n|2|- 444 Đá Hoàng Kim"
                                        + "\n- 888 Đá Thức Tỉnh"
                                        + "\n- 300 Thỏi Vàng"
                                        + "\n- 15 Máy Dò Boss"
                                        + "\n|7| Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                break;
                            case 5:
                                createOtherMenu(player, 4,
                                        "|7|Phần Thưởng Mốc Diệt Boss Đạt 1000 Điểm Gồm:"
                                        + "\n|2|- 499 Đá Hoàng Kim"
                                        + "\n- 999 Đá Thức Tỉnh"
                                        + "\n- 500 Thỏi Vàng"
                                        + "\n- 15 Máy Dò Boss"
                                        + "\n|7| Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                break;
                        }
                        /*Nhận Mốc Nạp*/                    } else if (player.iDMark.getIndexMenu() == 2) {
                        switch (select) {
                            case 0:
                                UseItem.gI().ComfirmMocNap(player);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 4) {
                        switch (select) {
                            case 0:
                                UseItem.gI().ComfirmMocSb(player);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc truongLaoGuru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.zone.map.mapId == 13) {

                            String npcSay = "Chào con, con cần ta giúp gì?\nTa có thể nâng giới hạn vàng cho con. Mỗi lần nâng tốn 500 Tr vàng và giới hạn vàng của con sẽ tăng lên 1 bậc";
                            String[] menu = {
                                "Mở giới\nhạn vàng", "Đóng"
                            };
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, npcSay, menu);
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.zone.map.mapId == 13) {
                        switch (select) {
                            case 0:
                                if (player.inventory.gold < 100000000) {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ vàng để thực hiện!");
                                } else if (player.inventory.Elon_Musk >= 9) {
                                    Service.getInstance().sendThongBao(player, "Giới hạn vàng của bạn đã tối đa");
                                } else {
                                    player.inventory.gold -= 500000000;
                                    Service.getInstance().sendMoney(player);
                                    player.inventory.Elon_Musk++;
                                    String notify = "Giới hạn vàng của con đã tăng lên : "
                                            + Util.powerToString(InventoryServiceNew.gI().getGodMax(player)) + " Gold";
                                    Service.getInstance().sendThongBaoOK(player, notify.replace("con", player.name.toUpperCase()));
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc vuaVegeta(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc ongGohan_ongMoori_ongParagus(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Chơi vui vẻ nhé con!"
                                        .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru" : "Vua Vegeta"),
                                "Điểm Danh", "Nhận\n Ngọc Xanh", "Kích hoạt\n Tài khoản", "Nhập\nGiftcode", "Nhận đệ tử");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.diemdanh < 1) {
                                    int tv = 0;
                                    int hn = 0;
                                    switch (player.vip) {
                                        case 0:
                                            hn = 5000;
                                            break;
                                        case 1:
                                            tv = 10;
                                            hn = 10000;
                                            break;
                                        case 2:
                                            tv = 15;
                                            hn = 15000;
                                            break;
                                        case 3:
                                            tv = 20;
                                            hn = 20000;
                                            break;
                                        case 4:
                                            tv = 25;
                                            hn = 25000;
                                            break;
                                    }
                                    player.inventory.ruby += hn;
                                    Item thoivang = ItemService.gI().createNewItem((short) 457, tv);
                                    InventoryServiceNew.gI().addItemBag(player, thoivang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendMoney(player);
                                    player.diemdanh++;
                                    Service.getInstance().sendThongBao(player, "|7|Điểm danh thành công!\nNhận được " + tv + " Thỏi vàng và " + Util.format(hn) + " Hồng ngọc");
                                } else {
                                    this.npcChat(player, "Hôm nay đã nhận rồi mà !!!");
                                }
                                break;
                            case 1:
                                if (player.inventory.gem >= 2000000000) {
                                    this.npcChat(player, "Dùng hết rồi hãy nhận tiếp, đừng tham lam nhé con!");
                                    break;
                                }
                                player.inventory.gem = 2000000000;
                                Service.gI().sendMoney(player);
                                Service.gI().sendThongBao(player, "Bạn vừa nhận được 2 Tỉ Ngọc Xanh");
                                break;
                            case 2:
                                this.createOtherMenu(player, 0, "|7|Kích Hoạt Tài Khoản\n"
                                        + "|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
                                        + "\n|5|Trạng thái tài khoản : " + (player.getSession().actived == 0 ? "Chưa kích hoạt" : "Đã kích hoạt")
                                        + "\n|2|Trạng thái VIP : " + (player.vip == 1 ? "VIP" : player.vip == 2 ? "VIP2" : player.vip == 3 ? "VIP3" : player.vip == 4 ? "SVIP" : "Chưa Kích Hoạt")
                                        + (player.timevip > 0 ? "\n|5|Hạn còn : " + Util.msToThang(player.timevip) : ""),
                                        "Kích Hoạt\nTài Khoản", "Kích Hoạt\nVIP", "Đóng");
                                break;
                            case 3:
                                Input.gI().createFormGiftCode(player);
                                break;
                            case 4:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                    Service.getInstance().sendThongBao(player,
                                            "Con vừa nhận được đệ tử! Hãy chăm sóc nó nhé");
                                } else {
                                    this.npcChat(player, "Đã có đệ tử rồi mà!");

                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 0) {
                        switch (select) {
                            case 1:
                                this.createOtherMenu(player, 1, "|7|MUA THẺ VIP THÁNG\n"
                                        + "|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
                                        + "\n|2|Trạng thái VIP : " + (player.vip == 1 ? "VIP" : player.vip == 2 ? "VIP2" : player.vip == 3 ? "VIP3" : player.vip == 4 ? "SVIP" : "Chưa Kích Hoạt")
                                        + (player.timevip > 0 ? "\n|5|Hạn còn : " + Util.msToThang(player.timevip) : ""),
                                        "Kích Hoạt\nVIP1\n50.000Đ", "Kích Hoạt\nVIP2\n70.000Đ", "Kích Hoạt\nVIP3\n100.000Đ", "Kích Hoạt\nSVIP\n150.000Đ", "Đóng");
                                break;
                            case 0:
                                this.createOtherMenu(player, ConstNpc.CONFIRM_ACTIVE, "|5|Mở thành viên Free\n" + "|5|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ" + "\n|7|Trạng thái : " + (player.getSession().actived == 0 ? "Chưa kích hoạt" : "Đã kích hoạt"), "Kích hoạt", "Đóng");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 2, "|7|VIP1\n"
                                        + "|2|Quyền lợi đi kèm\n"
                                        + "|5|Nhận 10 Thỏi Vàng/ngày"
                                        + "\nNhận 10.000 Hồng Ngọc/ngày"
                                        + "\nTăng 10% TNSM"
                                        + "\nDanh hiệu VIP1"
                                        + "\n|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
                                        + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1" : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2" : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3" : player.vip == 4 ? "\n|7|Trạng thái VIP : SVIP" : "")
                                        + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""), "Kích Hoạt", "Đóng");
                                break;
                            case 1:
                                this.createOtherMenu(player, 3, "|7|VIP2\n"
                                        + "|2|Quyền lợi đi kèm\n"
                                        + "|5|Nhận 15 Thỏi Vàng/ngày"
                                        + "\nNhận 15.000 Hồng Ngọc/ngày"
                                        + "\nTăng 10% TNSM"
                                        + "\nTăng 5% Rơi Ngọc Rồng"
                                        + "\nTăng 10% Dò Pha Lê"
                                        + "\nDanh hiệu VIP2"
                                        + "\n|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
                                        + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1" : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2" : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3" : player.vip == 4 ? "\n|7|Trạng thái VIP : SVIP" : "")
                                        + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""), "Kích Hoạt", "Đóng");
                                break;
                            case 2:
                                this.createOtherMenu(player, 4, "|7|VIP3\n"
                                        + "|2|Quyền lợi đi kèm\n"
                                        + "|5|Nhận 20 Thỏi Vàng/ngày"
                                        + "\nNhận 20.000 Hồng Ngọc/ngày"
                                        + "\nTăng 15% TNSM"
                                        + "\nTăng 10% Rơi Ngọc Rồng"
                                        + "\nTăng 15% Dò Pha Lê"
                                        + "\nDanh hiệu VIP3"
                                        + "\n|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
                                        + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1" : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2" : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3" : player.vip == 4 ? "\n|7|Trạng thái VIP : SVIP" : "")
                                        + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""), "Kích Hoạt", "Đóng");
                                break;
                            case 3:
                                this.createOtherMenu(player, 5, "|7|SVIP\n"
                                        + "|2|Quyền lợi đi kèm\n"
                                        + "|5|Nhận 25 Thỏi Vàng/ngày"
                                        + "\nNhận 25.000 Hồng Ngọc/ngày"
                                        + "\nTăng 20% TNSM"
                                        + "\nTăng 15% Rơi Ngọc Rồng"
                                        + "\nTăng 20% Dò Pha Lê"
                                        + "\nDanh hiệu VIP4"
                                        + "\n|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
                                        + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1" : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2" : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3" : player.vip == 4 ? "\n|7|Trạng thái VIP : SVIP" : "")
                                        + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""), "Kích Hoạt", "Đóng");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 2) {
                        switch (select) {
                            case 0:
                                if (player.vip >= 1) {
                                    this.npcChat(player, "|7|Bạn đang là thành viên " + (player.vip == 4 ? "SVIP" : "VIP" + player.vip) + " rồi");
                                    return;
                                }
                                if (player.getSession().vnd >= 50000) {
                                    player.vip = 1;
                                    player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                                    PlayerDAO.subvndmua(player, 50000);
                                    Service.gI().sendMoney(player);
                                    this.npcChat(player, "|6|Đã mở thành công\n|7|VIP1");
                                } else {
                                    this.npcChat(player, "Bạn không đủ tiền");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 3) {
                        switch (select) {
                            case 0:
                                if (player.vip >= 2) {
                                    this.npcChat(player, "|7|Bạn đang là thành viên " + (player.vip == 4 ? "SVIP" : "VIP" + player.vip) + " rồi");
                                    return;
                                }
                                if (player.getSession().vnd >= 70000) {
                                    player.vip = 2;
                                    player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                                    PlayerDAO.subvndmua(player, 70000);
                                    Service.gI().sendMoney(player);
                                    this.npcChat(player, "|6|Đã mở thành công\n|7|VIP2");
                                } else {
                                    this.npcChat(player, "Bạn không đủ tiền");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 4) {
                        switch (select) {
                            case 0:
                                if (player.vip >= 3) {
                                    this.npcChat(player, "|7|Bạn đang là thành viên " + (player.vip == 4 ? "SVIP" : "VIP" + player.vip) + " rồi");
                                    return;
                                }
                                if (player.getSession().vnd >= 100000) {
                                    player.vip = 3;
                                    player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                                    PlayerDAO.subvndmua(player, 100000);
                                    Service.gI().sendMoney(player);
                                    this.npcChat(player, "|6|Đã mở thành công\n|7|VIP3");
                                } else {
                                    this.npcChat(player, "Bạn không đủ tiền");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 5) {
                        switch (select) {
                            case 0:
                                if (player.vip >= 4) {
                                    this.npcChat(player, "|7|Bạn đang là thành viên " + (player.vip == 4 ? "SVIP" : "VIP" + player.vip) + " rồi");
                                    return;
                                }
                                if (player.getSession().vnd >= 150000) {
                                    player.vip = 4;
                                    player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
                                    PlayerDAO.subvndmua(player, 150000);
                                    Service.gI().sendMoney(player);
                                    this.npcChat(player, "|6|Đã mở thành công\n|7|SVIP");
                                } else {
                                    this.npcChat(player, "Bạn không đủ tiền");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.CONFIRM_ACTIVE) {
                        switch (select) {
                            case 0:
                                if (select == 0) {
                                    if (player.getSession().actived == 0) {
                                        if (player.inventory.ruby >= 10000) {
                                            try {
                                                int sum = player.inventory.ruby -= 10000;
                                                GirlkunDB.executeUpdate(
                                                        "update account set active = ?, vnd = ? where id = ?", 1, sum,
                                                        player.account_id);
                                                player.getSession().actived = 1;

                                                player.inventory.ruby = sum;
                                                Service.gI().sendThongBaoOK(player, "Kích Hoạt Thành Công, Đăng Nhập Lại Để Kích Hoạt\nHệ Thống Sẽ Tự Kick Bạn Sau 5s");
                                            } catch (Exception e) {
                                                this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                            }
                                            player.iDMark.setLastTimeBan(System.currentTimeMillis());
                                            player.iDMark.setBan(true);
                                        } else {
                                            this.npcChat(player, "Không đủ tiền để kích hoạt!");
                                        }

                                    } else {
                                        this.npcChat(player, "Bạn đã kích hoạt thành viên rồi!");
                                    }
                                }
                                break;
                            case 1:
                                this.npcChat(player, "Lần sau tiếp lúa cho ta nữa nha con!!!");
                                break;
                        }
                    }
                }

            }

        };
    }

//    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng");
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (player.iDMark.isBaseMenu()) {
//                        switch (select) {
//                            case 0://Shop
//                                if (player.gender == ConstPlayer.TRAI_DAT) {
//                                    ShopServiceNew.gI().opendShop(player, "BUNMA", true);
//                                } else {
//                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
//                                }
//                                break;
//                        }
//                    }
//                }
//            }
//        };
//    }
    public static Npc dende(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.idNRNM != -1) {
                            if (player.zone.map.mapId == 7) {
                                this.createOtherMenu(player, 1, "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước", "Hướng\ndẫn\nGọi Rồng", "Gọi rồng", "Từ chối");
                            }
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.NAMEC) {
                                    ShopServiceNew.gI().opendShop(player, "DENDE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc", "Đóng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        if (player.zone.map.mapId == 7 && player.idNRNM != -1) {
                            if (player.idNRNM == 353) {
//                                NgocRongNamecService.gI().tOpenNrNamec = System.currentTimeMillis() + 86400000;
//                                NgocRongNamecService.gI().firstNrNamec = true;
//                                NgocRongNamecService.gI().timeNrNamec = 0;
//                                NgocRongNamecService.gI().doneDragonNamec();
//                                NgocRongNamecService.gI().initNgocRongNamec((byte) 1);
//                                NgocRongNamecService.gI().reInitNrNamec((long) 86399000);
//                                SummonDragon.gI().summonNamec(player);
                            } else {
                                Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc appule(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.XAYDA) {
                                    ShopServiceNew.gI().opendShop(player, "APPULE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc drDrief(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 84) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất" : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                    } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 84) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cargo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nTrái Đất", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_FIND_BOSS = 50000000;

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang bị bắt cóc rồi");
                        } else {
                            if (this.mapId == 19) {

                                int taskId = TaskService.gI().getIdTask(pl);
                                switch (taskId) {
                                    case ConstTask.TASK_19_0:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_KUKU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_1:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nMập đầu đinh\n(" + Util.numberToMoney(COST_FIND_BOSS)
                                                + " vàng)",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_2:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_RAMBO,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    default:
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");

                                        break;
                                }
                            } else if (this.mapId == 68) {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                            } else {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Tàu vũ trụ Xayda sử dụng công nghệ mới nhất, "
                                        + "có thể đưa ngươi đi bất kỳ đâu, chỉ cần trả tiền là được.",
                                        "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 26) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 19) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().player.nPoint.power >= 41000000000L
                                            && player.playerTask.taskMain.id > 24) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    } // break;
                                    else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ 24 Và Đạt 41 Tỷ Sức Mạnh");
                                    }
                                    break;
                                case 1:
                                    if (player.playerTask.taskMain.id >= 17) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ Đi");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.KUKU);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                    boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                        boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold)
                                                    + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    if (player.getSession().player.nPoint.power >= 41000000000L
                                            && player.playerTask.taskMain.id > 21) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    } // break;
                                    else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ 21 Và Đạt 41 Tỷ Sức Mạnh");
                                    }
                                    break;
                                case 2:
                                    if (player.playerTask.taskMain.id >= 17) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ Đi");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.MAP_DAU_DINH);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                    boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                        boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold)
                                                    + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    if (player.getSession().player.nPoint.power >= 41000000000L
                                            && player.playerTask.taskMain.id > 24) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    } // break;
                                    else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ 24 Và Đạt 41 Tỷ Sức Mạnh");
                                    }
                                    break;
                                case 2:
                                    if (player.playerTask.taskMain.id >= 17) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ Đi");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.RAMBO);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                    boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                        boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold)
                                                    + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    if (player.getSession().player.nPoint.power >= 41000000000L
                                            && player.playerTask.taskMain.id > 24) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    } // break;
                                    else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ 24 Và Đạt 41 Tỷ Sức Mạnh");
                                    }
                                    break;
                                case 2:
                                    if (player.playerTask.taskMain.id >= 17) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ Đi");
                                    }
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 68) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                            "Cửa hàng", "Tiệm\nBách Hóa", "Tiệm\nTạp Hoá", "Tiệm\nItem", "Tiệm\n XU");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "SANTA", false);
                                    break;
                                case 1: //tiệm hồng ngọc
                                    ShopServiceNew.gI().opendShop(player, "SANTA_RUBY", false);
                                    break;
                                case 2: //
                                    ShopServiceNew.gI().opendShop(player, "SANTA_RUBY2", true);
                                    break;
                                case 3:
                                    ShopServiceNew.gI().opendShop(player, "SHOP_LUOT", true);
                                    break;
                                case 4:
                                    ShopServiceNew.gI().opendShop(player, "SHOP_TET", true);
                                    break;
                                case 5: //
                                    ShopServiceNew.gI().opendShop(player, "PET", true);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    /**
     * **************************************************************************************************************************************************************************
     */
    public Template.ItemTemplate getTemplate(int id) {
        return Manager.ITEM_TEMPLATES.get(id);
    }

    public short getItemIdByIcon(short IconID) {
        for (int i = 0; i < Manager.ITEM_TEMPLATES.size(); i++) {
            if (Manager.ITEM_TEMPLATES.get(i).iconID == IconID) {
                return Manager.ITEM_TEMPLATES.get(i).id;
            }
        }
        return -1;
    }

    public Item createItemNull() {
        Item item = new Item();
        return item;
    }

    public Item createItem(int tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.itemOptions = createItemNull().itemOptions;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

//    public static Npc bulmabip(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//              return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                createOtherMenu(player, 0, "\b|8|Trò chơi Tài Xỉu đang được diễn ra\n\n|6|Thử vận may của bạn với trò chơi Tài Xỉu! Đặt cược và dự đoán đúng"
//                        + "\n kết quả, bạn sẽ được nhận thưởng lớn. Hãy tham gia ngay và\n cùng trải nghiệm sự hồi hộp, thú vị trong trò chơi này!"
//                        + "\n\n|7|(Điều kiện tham gia : mở thành viên)\n\n|2|Đặt tối thiểu: 1.000 Hồng ngọc\n Tối đa: 1.000.000.000.000 Hồng ngọc"
//                        + "\n\n|7| Lưu ý : Thoát game khi chốt Kết quả sẽ MẤT Tiền cược và Tiền thưởng", "Thể lệ", "Tham gia");
//            }
//
//            @Override
//            public void confirmMenu(Player pl, int select) {
//                if (canOpenNpc(pl)) {
//                    String time = ((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
//                    if (pl.iDMark.getIndexMenu() == 0) {
//                        if (select == 0) {
//                            createOtherMenu(pl, ConstNpc.IGNORE_MENU, "|5|Có 2 nhà cái Tài và Xĩu, bạn chỉ được chọn 1 nhà để tham gia"
//                                    + "\n\n|6|Sau khi kết thúc thời gian đặt cược. Hệ thống sẽ tung xí ngầu để biết kết quả Tài Xỉu"
//                                    + "\n\nNếu Tổng số 3 con xí ngầu <=10 : XỈU\nNếu Tổng số 3 con xí ngầu >10 : TÀI\nNếu 3 Xí ngầu cùng 1 số : TAM HOA (Nhà cái lụm hết)"
//                                    + "\n\n|7|Lưu ý: Số Hồng ngọc nhận được sẽ bị nhà cái lụm đi 20%. Trong quá trình diễn ra khi đặt cược nếu thoát game trong lúc phát thưởng phần quà sẽ bị HỦY", "Ok");
//                        } else if (select == 1) {
//                            if (TaiXiu_Old.gI().baotri == false) {
//                                if (pl.goldTai == 0 && pl.goldXiu == 0) {
//                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ ĐÉO NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu_Old.gI().PlayersTai.size() + " người"
//                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu_Old.gI().PlayersXiu.size() + " người"
//                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
//                                } else if (pl.goldTai > 0) {
//                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI TÀI XỈU---\n\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu_Old.gI().PlayersTai.size() + " người"
//                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu_Old.gI().PlayersXiu.size() + " người"
//                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
//                                } else {
//                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ ĐÉO NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu_Old.gI().PlayersTai.size() + " người"
//                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu_Old.gI().PlayersXiu.size() + " người"
//                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
//                                }
//                            } else {
//                                if (pl.goldTai == 0 && pl.goldXiu == 0) {
//                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ ĐÉO NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu_Old.gI().PlayersTai.size() + " người"
//                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu_Old.gI().PlayersXiu.size() + " người"
//                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
//                                } else if (pl.goldTai > 0) {
//                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ ĐÉO NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu_Old.gI().PlayersTai.size() + " người"
//                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu_Old.gI().PlayersXiu.size() + " người"
//                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
//                                } else {
//                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ ĐÉO NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(pl.goldXiu) + " Hồng ngọc" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
//                                }
//                            }
//                        }
//                    } else if (pl.iDMark.getIndexMenu() == 1) {
//                        if (((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai == 0 && pl.goldXiu == 0 && TaiXiu_Old.gI().baotri == false) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ ĐÉO NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu_Old.gI().PlayersTai.size() + " người"
//                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu_Old.gI().PlayersXiu.size() + " người"
//                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
//                                    break;
//                                case 1:
//                                    if (pl.getSession().actived!=1 && pl.playerTask.taskMain.id <= 26) {
//                                        Service.gI().sendThongBao(pl, "Vui lòng kích hoạt tài khoản và đạt nhiệm vụ 26 để sử dụng chức năng này");
//                                    } else {
//                                        Input.gI().TAI_taixiu(pl);
//                                    }
//                                    break;
//                                case 2:
//                                    if (pl.getSession().actived!=1 && pl.playerTask.taskMain.id <= 26) {
//                                        Service.gI().sendThongBao(pl, "Vui lòng kích hoạt tài khoản và đạt nhiệm vụ 26 để sử dụng chức năng này");
//                                    } else {
//                                        Input.gI().XIU_taixiu(pl);
//                                    }
//                                    break;
//                            }
//                        } else if (((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai > 0 && TaiXiu_Old.gI().baotri == false) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ ĐÉO NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu_Old.gI().PlayersTai.size() + " người"
//                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu_Old.gI().PlayersXiu.size() + " người"
//                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
//
//                                    break;
//                            }
//                        } else if (((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu > 0 && TaiXiu_Old.gI().baotri == false) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ ĐÉO NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu_Old.gI().PlayersTai.size() + " người"
//                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu_Old.gI().PlayersXiu.size() + " người"
//                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
//                                    break;
//                            }
//                        } else if (((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai > 0 && TaiXiu_Old.gI().baotri == true) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ ĐÉO NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu_Old.gI().PlayersTai.size() + " người"
//                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu_Old.gI().PlayersXiu.size() + " người"
//                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
//
//                                    break;
//                            }
//                        } else if (((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu > 0 && TaiXiu_Old.gI().baotri == true) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ ĐÉO NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu_Old.gI().PlayersTai.size() + " người"
//                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu_Old.gI().PlayersXiu.size() + " người"
//                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
//
//                                    break;
//                            }
//                        } else if (((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu == 0 && pl.goldTai == 0 && TaiXiu_Old.gI().baotri == true) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(pl, 1, "\n|7|---ĐỠ THẾ ĐÉO NÀO ĐƯỢC CÁC ÔNG À---\n\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu_Old.gI().PlayersTai.size() + " người"
//                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc"
//                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu_Old.gI().PlayersXiu.size() + " người"
//                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
//
//                                    break;
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }

//    public static Npc minuongevent(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 5 || this.mapId == 46) {
//                        switch (ConstEvent.EVENT) {
//                        case 1:
//                        this.createOtherMenu(player, ConstEvent.MUAHE, "|7|-Sự Kiện Mè Hùa-\n"
//                            + "Chỉ Có Thể Up Vật Phẩm Tại Các Map Sau : Nam Kame, Nam Guru\n"
//                            + "Yêu Cầu : Mang trên người (Quần Đi Biển)\n\n"
//                            + "|1| x999 Sao Biển: Ngọc Rồng 2-5 Sao Ngẫu Nhiên\n"
//                            + " x999 Con Cua: Cải Trang, Có Khả Năng Vĩnh Viễn\n"
//                            + " x999 Vỏ Sò: Pet Ngãu Nhiên, Có Khả Năng Vĩnh Viễn\n"
//                            + " x999 Vỏ Ốc: Thỏi Vàng (Có thể đổi số lượng lớn)\n",
//                            "Đổi\nSao Biển", "Đổi\nCon Cua","Đổi\nVỏ Sò","Đổi\nVỏ Ốc");
//                        break;
//                        case 2:
//                        this.createOtherMenu(player, ConstEvent.HUNGVUONG, "|7|-Sự Kiện Hùng Vương-\n"
//                            + "Có Thể Up Vật Phẩm Tại Tất Cả Các Map\n"
//                            + "Yêu Cầu : Mang trên người (Cải Trang Mị Nương)\n"
//                            + "\n|2|Dâng Dưa Hấu Để Đổi Lấy Điểm Sự Kiện"
//                            + "\n|2|Sử Dụng Điểm Để Đổi Công Thức Chế Tạo Đồ Thiên Sứ"
//                            + "\n|2|Sử Dụng Điểm Để Để Thử Vận May Nhận Item SC"
//                            + "\n|2|Sử Dụng Điểm Để Đổi Cải Trang Vip (Có Khả Năng Vĩnh Viễn)"
//                            + "\n|2|Sử Dụng Điểm Để Thử Vận May Nhận Ngọc Rồng",
//                            "Dâng\nDưa Hấu", "Xem Điểm\nSự Kiện", "Đổi\nCông Thức", "Đổi\nItem SC", "Đổi\nCải Trang", "Thử\nVận May", "Từ chối");
//                        break;
//                        case 3:
//                        this.createOtherMenu(player, ConstEvent.TRUNGTHU, "|7| SỰ KIỆN TRUNG THU"
//                            + "\n\n|2|Nguyên liệu cần nấu bánh Trung thu"
//                            + "\n|-1|- Bánh Hạt sen : 99 Hạt sen + 50 Bột nếp + 2 Mồi lửa"
//                            + "\n|-1|- Bánh Đậu xanh : 99 Đậu xanh + 50 Bột nếp + 2 Mồi lửa"
//                            + "\n|-1|- Bánh Thập cẩm : 99 Hạt sen + 99 Đậu xanh + 99 Bột nếp + 5 Mồi lửa"
//                            + "\n|7|Làm bánh sẽ tốn phí 2Ty Vàng/lần"
//                            + "\n\n|1|Điểm sự kiện : " + Util.format(player.nhsPoint) + " Điểm",
//                            "Thể lệ", "Làm bánh", "Đổi điểm\nTrung thu");
//                        break;
//                        case 4:
//                        this.createOtherMenu(player, ConstEvent.HLWEEN,
//                            "Sự kiện Halloween chính thức tại Ngọc Rồng Kamui\n"
//                            + "Chuẩn bị x10 nguyên liệu Kẹo, Bánh Quy, Bí ngô để đổi Giỏ Kẹo cho ta nhé\n"
//                            + "Nguyên Liệu thu thập bằng cách đánh quái tại các hành tinh được chỉ định\n"
//                            + "Tích lũy 3 Giỏ Kẹo +  3 Vé mang qua đây ta sẽ cho con 1 Hộp Ma Quỷ\n"
//                            + "Tích lũy 3 Giỏ Kẹo, 3 Hộp Ma Quỷ + 3 Vé \nmang qua đây ta sẽ cho con 1 hộp quà thú vị.",
//                            "Đổi\nGiỏ Kẹo", "Đổi Hộp\nMa Quỷ", "Đổi Hộp\nQuà Halloween",
//                            "Từ chối");
//                        break;
//                        case 5:
//                        this.createOtherMenu(player, ConstEvent.NHAGIAO, 
//                            "Sự kiện 20/11 chính thức tại Ngọc Rồng Kamui\n"
//                            + "Số điểm hiện tại của bạn là : "+ player.inventory.event
//                            + "\nTổng số hoa đã tặng "+player.inventory.event+ "/999"
//                            + "\nToàn bộ máy chủ được nhân đôi số vàng rơi ra từ quái,thời gian còn lại "
//                            + "5" + " phút."
//                            + "\nKhi tặng đủ 999 bông hoa toàn bộ máy chủ được nhân đôi số vàng rơi ra từ quái trong 60 phút",
//                            "Tặng 1\n Bông hoa", "Tặng\n10 Bông", "Tặng\n99 Bông",
//                            "Đổi\nHộp quà");
//                        break;
//                        case 6:
//                        this.createOtherMenu(player, ConstEvent.NOEL,
//                            "Sự kiên giáng sinh Ngọc Rồng Kamui"
//                            + "\nKhi đội mũ len bất kì đánh quái sẽ có cơ hội nhận được kẹo giáng sinh"
//                            + "\nĐem 99 kẹo giáng sinh tới đây để đổi 1 Vớ,tất giáng sinh"
//                            + "\nChúc bạn một mùa giáng sinh vui vẻ",
//                            "Đổi\nTất giáng sinh");
//                        break;
//                        case 7:
//                        this.createOtherMenu(player, ConstEvent.TET,
//                            "Mừng Ngày Tết Nguyên Đán Nro Kamui"
//                            + "\nBạn đang có: " + player.inventory.event + " điểm sự kiện"
//                            + "\n" + (ConstEvent.TONGSOBANH >= 500 ? "|7|HIỆN TẠI ĐANG X2 EXP TRÊN TOÀN MÁY CHỦ\nTHỜI GIAN CÒN: " + Util.tinhgio(ConstEvent.X2) : "|7|Tổng số bánh server: " + ConstEvent.TONGSOBANH
//                            + "\nKhi số bánh nấu đạt đủ 500 bánh toàn máy chủ sẽ được X2 EXP")
//                            + "\n|-1|Chúc bạn năm mới dui dẻ",
//                            "Nhận Lìxì", "Đổi Điểm\nSự Kiện","Cửa Hàng\nSự Kiện","Nhận Quần\nHoa Văn");
//                        break;
//                        case 8:
//                        this.createOtherMenu(player, ConstEvent.PHUNU,
//                            "Sự kiện 8/3 chính thức tại Ngọc Rồng Kamui"
//                            + "\nBạn đang có: " + player.inventory.event
//                            + " điểm sự kiện\nChúc bạn chơi game dui dẻ",
//                            "Tặng 1\n Bông hoa", "Tặng\n10 Bông", "Tặng\n99 Bông","Đổi Capsule");
//                        break;
//                        default:
//                        Service.gI().sendThongBaoFromAdmin(player, "|7|Hiện chưa mở sự kiện nào!");
//                        break;
//                        }
//                    } 
//                }
//            }
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 5 || this.mapId == 46) {
//                        switch (player.iDMark.getIndexMenu()) {
//                            case ConstEvent.MUAHE:
//                                switch (select) {
//                                    case 0:
//                                        Item SaoBien = null;
//                                        try {
//                                            SaoBien = InventoryServiceNew.gI().findItemBag(player, 698);
//                                        } catch (Exception e) {
//                                        }
//                                        if (SaoBien == null || SaoBien.quantity < 999) {
//                                            this.npcChat(player, "Bạn không đủ x999 sao biển");
//                                        } else if (player.inventory.gold < 1_000_000_000) {
//                                            this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                            this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                        } else {
//                                            player.inventory.gold -= 1_000_000_000;
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, SaoBien, 999);
//                                            Service.gI().sendMoney(player);
//                                            if (Util.isTrue(99, 100)){
//                                                Item ngocrong = ItemService.gI().createNewItem((short) Util.nextInt(16,18));
//                                                InventoryServiceNew.gI().addItemBag(player, ngocrong);
//                                                InventoryServiceNew.gI().sendItemBags(player);
//                                                this.npcChat(player, "Bạn nhận được " + ngocrong);
//                                            }
//                                            else if (Util.isTrue(1, 100)){
//                                                Item ngocrong2 = ItemService.gI().createNewItem((short) Util.nextInt(14,15));
//                                                InventoryServiceNew.gI().addItemBag(player, ngocrong2);
//                                                InventoryServiceNew.gI().sendItemBags(player);
//                                                this.npcChat(player, "Bạn nhận được " + ngocrong2);
//                                            }}
//                                    break;
//                                    case 1:
//                                        Item ConCua = null;
//                                        try {
//                                            ConCua = InventoryServiceNew.gI().findItemBag(player, 697);
//                                        } catch (Exception e) {
//                                        }
//                                        if (ConCua == null || ConCua.quantity < 999) {
//                                            this.npcChat(player, "Bạn không đủ x999 con cua");
//                                        } else if (player.inventory.gold < 1_000_000_000) {
//                                            this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                            this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                        } else {
//                                            player.inventory.gold -= 1_000_000_000;
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, ConCua, 999);
//                                            Service.gI().sendMoney(player);
//                                            Item caitrang = ItemService.gI().createNewItem((short) (1248+player.gender));
//                                            caitrang.itemOptions.add(new Item.ItemOption(50, Util.nextInt(15,35)));
//                                            caitrang.itemOptions.add(new Item.ItemOption(14, Util.nextInt(1,10)));
//                                            caitrang.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20,35)));
//                                            caitrang.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20,35)));
//                                            caitrang.itemOptions.add(new Item.ItemOption(101, Util.nextInt(20,50)));
//                                            if (Util.isTrue(999, 1000)){
//                                                caitrang.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3,15)));
//                                            }
//                                            InventoryServiceNew.gI().addItemBag(player, caitrang);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "Bạn nhận được Cải Trang Sự Kiện");
//                                        }
//                                    break;
//                                    case 2:
//                                        Item VoSo = null;
//                                        try {
//                                            VoSo = InventoryServiceNew.gI().findItemBag(player, 696);
//                                        } catch (Exception e) {
//                                        }
//                                        if (VoSo == null || VoSo.quantity < 999) {
//                                            this.npcChat(player, "Bạn không đủ x999 vỏ sò");
//                                        } else if (player.inventory.gold < 1_000_000_000) {
//                                            this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                            this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                        } else {
//                                            player.inventory.gold -= 1_000_000_000;
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, VoSo, 999);
//                                            Service.gI().sendMoney(player);
//                                            Item pet = ItemService.gI().createNewItem((short) Util.nextInt(2085,2090));
//                                            pet.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10,30)));
//                                            pet.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10,30)));
//                                            pet.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10,30)));
//                                            if (Util.isTrue(99, 100)){
//                                                pet.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1,15)));
//                                            }
//                                            InventoryServiceNew.gI().addItemBag(player, pet);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "Bạn nhận được pet");
//                                        }
//                                    break;
//                                    case 3:
//                                        Item VoOc = null;
//                                        try {
//                                            VoOc = InventoryServiceNew.gI().findItemBag(player, 695);
//                                        } catch (Exception e) {
//                                        }
//                                        if (VoOc == null || VoOc.quantity < 999) {
//                                            this.npcChat(player, "Bạn không đủ x999 vỏ ốc");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                            this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                        } else {
//                                            Item tv = ItemService.gI().createNewItem((short)457, 1);
//                                            tv.itemOptions.add(new Item.ItemOption(30, 0));
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, VoOc, 999);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            InventoryServiceNew.gI().addItemBag(player, tv);
//                                            this.npcChat(player, "Bạn nhận được 1 thỏi vàng");
//                                        }
//                                        break;
//                                }   
//                            break;
//                            case ConstEvent.HUNGVUONG:
//                                switch (select) {
//                                    case 0: //doi diem
//                                       Input.gI().duahau(player);
//                                        break;
//                                    case 1:
//                                        this.createOtherMenu(player, ConstNpc.NAP_THE, "|2|Khó Đã Có Viettinbank :3 \nNgươi đang có: " + player.inventory.coupon + " điểm sự kiện", "Đóng");
//                                        break;
//                                    case 2:
//                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_DIEM_DUA, -1, "Đổi Công Thức Chế Tạo Đồ Thiên Sứ?\nTa Cần 200 điểm sự kiện đấy... ",
//                                                "Đồng ý", "Từ chối");
//                                        break;
//                                    case 3:
//                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_DIEM_ITEMC2, -1, "Ta Sẽ Cho Con Item siêu cấp ngẫu nhiên?\nTa Cần 100 Điểm Sự Kiện... ",
//                                                "Đồng ý", "Từ chối");
//                                        break;
//                                    case 4:
//                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_DIEM_CT, -1, "Cần 999 Điểm Sự Kiện Để Lấy Cải Trang Random \nCó Tỉ Lệ May Mắn Được Vĩnh Viễn...Thử Ngay Nào ",
//                                                "Đồng ý", "Từ chối");
//                                        break;
//                                    case 5:
//                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_ITEM_NR, -1, "Còn Thở Còn Gỡ Còn Điểm Còn Đổi ..?\nPhải giao cho ta 200 điểm sự kiện đấy...\nNếu May Mắn Sẽ Nhận Được Nro Torobo random Sao, Bông Tai Porata 3,... ",
//                                                "Đồng ý", "Từ chối");
//                                        break;
//                                }   
//                            break;
//                            case ConstEvent.TRUNGTHU:
//                                switch (select) {
//                                    case 0: {
//                                        this.createOtherMenu(player, ConstEvent.TRUNGTHU_HD,
//                                                "|7|SỰ KIỆN TRUNG THU"
//                                                        + "\n\n|2|Cách thức tìm nguyên liệu nấu bánh Trung thu"
//                                                        + "\n|4|- Hạt sen : Đánh các quái bay trên không"
//                                                        + "\n- Đậu xanh : Đánh các quái dưới đất"
//                                                        + "\n- Bột nếp : Đánh quái Sên ở Tương lai"
//                                                        + "\n- Mồi lửa : Giết Boss Thỏ trắng (5 phút xuất hiện 1 lần)"
//                                                        + "\n\n|5|Ăn bánh trung thu nhận được Điểm tích lũy Sự kiện Trung thu đổi được các phần quà hấp dẫn"
//                                                        + "\n|-1|- Bánh Hạt sen : Nhận 2 Điểm Sự kiện"
//                                                        + "\n|-1|- Bánh Đậu xanh : Nhận 2 Điểm Sự kiện"
//                                                        + "\n|-1|- Bánh Thập cẩm : Nhận 5 Điểm Sự kiện"
//                                                        + "\n|5|- Quy đổi tiền 1.000Đ nhận thêm 1 Điểm Sự kiện (Không tính đổi Xu)"
//                                                        + "\n\n|7|Chị hiểu hôn ???",
//                                                "Đã hiểu");
//                                        break;
//                                    }
//                                    case 1: {
//                                        this.createOtherMenu(player, ConstEvent.TRUNGTHU_LAMBANH,
//                                                "|7|SỰ KIỆN TRUNG THU"
//                                                        + "\n\n|2|Bạn muốn làm bánh gì?",
//                                                "Bánh\nHạt sen", "Bánh\nĐậu xanh", "Bánh\nThập cẩm");
//                                        break;
//                                    }
//                                    case 2: {
//                                        this.createOtherMenu(player, ConstEvent.TRUNGTHU_DOIDIEM,
//                                                "|7|TÍCH ĐIỂM SỰ KIỆN TRUNG THU"
//                                                        + "\n\n|1|Khi đổi điểm thì sẽ được cộng điểm đua top trung thu\n"
//                                                        + "|2|Mốc 500 Điểm\n"
//                                                        + "|4|200 Mảnh thiên sứ ngẫu nhiên, 20 rương thần linh, 15 Hộp quà Trung thu, 10 Thẻ gia hạn, 1 Phiếu giảm giá + 250k HN \n"
//                                                        + "|2|Mốc 300 Điểm\n"
//                                                        + "|4|100 Mảnh thiên sứ ngẫu nhiên, 15 rương thần linh, 10 Thẻ gia hạn, 15 Hộp Trung thu + 150k HN \n"
//                                                        + "|2|Mốc 200 Điểm\n"
//                                                        + "|4|50 Mảnh thiên sứ ngẫu nhiên, 10 rương thần linh, 5 Thẻ gia hạn, 10 Hộp Trung thu + 100k HN \n"
//                                                        + "|2|Mốc 50 Điểm\n"
//                                                        + "|4|10 Mảnh thiên sứ ngẫu nhiên, 5 rương thần linh + 25k HN"
//                                                        + "\n\n|7|Điểm sự kiện : " + Util.format(player.inventory.coupon) + " Điểm"
//                                                                + "\n|1|Điểm Top Trung thu : " + Util.format(player.inventory.event) + " Điểm",
//                                                "500 Điểm", "300 Điểm", "200 Điểm", "50 Điểm");
//                                        break;
//                                    }
//                                }       
//                                break;
//                            case ConstEvent.TRUNGTHU_LAMBANH:
//                                switch (select) {
//                                    case 0: {
//                                        Item hatsen = null;
//                                        Item botnep = null;
//                                        Item moilua = null;
//                                        try {
//                                            hatsen = InventoryServiceNew.gI().findItemBag(player, 1340);
//                                            botnep = InventoryServiceNew.gI().findItemBag(player, 1338);
//                                            moilua = InventoryServiceNew.gI().findItemBag(player, 1341);
//                                        } catch (Exception e) {
////                                        throw new RuntimeException(e);
//                                        }
//                                        if (hatsen == null || hatsen.quantity < 99) {
//                                            this.npcChat(player, "|7|Bạn không đủ 99 Hạt sen");
//                                        } else if (botnep == null || botnep.quantity < 50) {
//                                            this.npcChat(player, "|7|Bạn không đủ 50 Bột nếp");
//                                        } else if (moilua == null || moilua.quantity < 2) {
//                                            this.npcChat(player, "|7|Bạn không đủ 2 Mồi lửa");
//                                        } else if (player.inventory.gold < 2_000_000_000) {
//                                            this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                            this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
//                                        } else {
//                                            player.inventory.gold -= 2_000_000_000;
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, hatsen, 99);
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, botnep, 50);
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, moilua, 2);
//                                            Service.getInstance().sendMoney(player);
//                                            Item banhtrungthu = ItemService.gI().createNewItem((short) 1336);
//                                            InventoryServiceNew.gI().addItemBag(player, banhtrungthu);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "|4|Bạn nhận được Bánh trung thu Hạt sen");
//                                        }
//                                        break;
//                                    }
//                                    case 1: {
//                                        Item dauxanh = null;
//                                        Item botnep = null;
//                                        Item moilua = null;
//                                        try {
//                                            dauxanh = InventoryServiceNew.gI().findItemBag(player, 1339);
//                                            botnep = InventoryServiceNew.gI().findItemBag(player, 1338);
//                                            moilua = InventoryServiceNew.gI().findItemBag(player, 1341);
//                                        } catch (Exception e) {
////                                        throw new RuntimeException(e);
//                                        }
//                                        if (dauxanh == null || dauxanh.quantity < 99) {
//                                            this.npcChat(player, "|7|Bạn không đủ 99 Đậu xanh");
//                                        } else if (botnep == null || botnep.quantity < 50) {
//                                            this.npcChat(player, "|7|Bạn không đủ 50 Bột nếp");
//                                        } else if (moilua == null || moilua.quantity < 2) {
//                                            this.npcChat(player, "|7|Bạn không đủ 2 Mồi lửa");
//                                        } else if (player.inventory.gold < 2_000_000_000) {
//                                            this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                            this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
//                                        } else {
//                                            player.inventory.gold -= 2_000_000_000;
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dauxanh, 99);
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, botnep, 50);
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, moilua, 2);
//                                            Service.getInstance().sendMoney(player);
//                                            Item banhtrungthu = ItemService.gI().createNewItem((short) 1335);
//                                            InventoryServiceNew.gI().addItemBag(player, banhtrungthu);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "|4|Bạn nhận được Bánh trung thu Đậu xanh");
//                                        }
//                                        break;
//                                    }
//                                    case 2: {
//                                        Item hatsen = null;
//                                        Item dauxanh = null;
//                                        Item botnep = null;
//                                        Item moilua = null;
//                                        try {
//                                            hatsen = InventoryServiceNew.gI().findItemBag(player, 1340);
//                                            dauxanh = InventoryServiceNew.gI().findItemBag(player, 1339);
//                                            botnep = InventoryServiceNew.gI().findItemBag(player, 1338);
//                                            moilua = InventoryServiceNew.gI().findItemBag(player, 1341);
//                                        } catch (Exception e) {
////                                        throw new RuntimeException(e);
//                                        }
//                                        if (hatsen == null || hatsen.quantity < 99) {
//                                            this.npcChat(player, "|7|Bạn không đủ 99 Hạt sen");
//                                        } else if (botnep == null || botnep.quantity < 99) {
//                                            this.npcChat(player, "|7|Bạn không đủ 99 Bột nếp");
//                                        } else if (dauxanh == null || dauxanh.quantity < 99) {
//                                            this.npcChat(player, "|7|Bạn không đủ 99 Đậu xanh");
//                                        } else if (moilua == null || moilua.quantity < 5) {
//                                            this.npcChat(player, "|7|Bạn không đủ 5 Mồi lửa");
//                                        } else if (player.inventory.gold < 2_000_000_000) {
//                                            this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                            this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
//                                        } else {
//                                            player.inventory.gold -= 2_000_000_000;
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, hatsen, 99);
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dauxanh, 99);
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, botnep, 99);
//                                            InventoryServiceNew.gI().subQuantityItemsBag(player, moilua, 5);
//                                            Service.getInstance().sendMoney(player);
//                                            Item banhtrungthu = ItemService.gI().createNewItem((short) 1337);
//                                            InventoryServiceNew.gI().addItemBag(player, banhtrungthu);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "|4|Bạn nhận được Bánh trung thu Thập cẩm");
//                                        }
//                                        break;
//                                    }
//                                }       
//                            break;
//                            case ConstEvent.TRUNGTHU_DOIDIEM:
//                                switch (select) {
//                                    case 0: {
//                                        byte randommanh = (byte) new Random().nextInt(Manager.manhts.length);
//                                        int manh = Manager.manhts[randommanh];
//                                        if (player.inventory.coupon < 500) {
//                                            this.npcChat(player, "|7|Bạn không đủ 500 Điểm sự kiện");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 5) {
//                                            this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 5 ô trống");
//                                        } else {
//                                            player.inventory.coupon -= 500;
//                                            player.inventory.event += 500;
//                                            player.inventory.ruby += 250_000;
//                                            Service.getInstance().sendMoney(player);
//                                            Item manhthiensu = ItemService.gI().createNewItem((short) manh);
//                                            Item ruongthan = ItemService.gI().createNewItem((short) 1334);
//                                            Item hoptt = ItemService.gI().createNewItem((short) 1342);
//                                            Item phieugg = ItemService.gI().createNewItem((short) 459);
//                                            Item thegh = ItemService.gI().createNewItem((short) 1346);
//                                            phieugg.itemOptions.add(new Item.ItemOption(30, 1));
//                                            manhthiensu.quantity = 200;
//                                            ruongthan.quantity = 20;
//                                            hoptt.quantity = 15;
//                                            thegh.quantity = 30;
//                                            InventoryServiceNew.gI().addItemBag(player, manhthiensu);
//                                            InventoryServiceNew.gI().addItemBag(player, ruongthan);
//                                            InventoryServiceNew.gI().addItemBag(player, hoptt);
//                                            InventoryServiceNew.gI().addItemBag(player, phieugg);
//                                            InventoryServiceNew.gI().addItemBag(player, thegh);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "|4|Bạn nhận được 200 " + manhthiensu.template.name
//                                                    + ", 20 Rương thần linh, 15 Hộp Trung thu, 30 Thẻ gia hạn, 1 Phiếu giảm giá và 250k Hồng ngọc");
//                                        }
//                                        break;
//                                    }
//                                    case 1: {
//                                        byte randommanh = (byte) new Random().nextInt(Manager.manhts.length);
//                                        int manh = Manager.manhts[randommanh];
//                                        if (player.inventory.coupon < 300) {
//                                            this.npcChat(player, "|7|Bạn không đủ 300 Điểm sự kiện");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 4) {
//                                            this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 4 ô trống");
//                                        } else {
//                                            player.inventory.coupon -= 300;
//                                            player.inventory.event += 300;
//                                            player.inventory.ruby += 150_000;
//                                            Service.getInstance().sendMoney(player);
//                                            Item manhthiensu = ItemService.gI().createNewItem((short) manh);
//                                            Item ruongthan = ItemService.gI().createNewItem((short) 1334);
//                                            Item hoptt = ItemService.gI().createNewItem((short) 1342);
//                                            Item thegh = ItemService.gI().createNewItem((short) 1346);
//                                            manhthiensu.quantity = 100;
//                                            ruongthan.quantity = 15;
//                                            hoptt.quantity = 15;
//                                            thegh.quantity = 10;
//                                            InventoryServiceNew.gI().addItemBag(player, manhthiensu);
//                                            InventoryServiceNew.gI().addItemBag(player, ruongthan);
//                                            InventoryServiceNew.gI().addItemBag(player, hoptt);
//                                            InventoryServiceNew.gI().addItemBag(player, thegh);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "|4|Bạn nhận được 100 " + manhthiensu.template.name
//                                                    + ", 15 Rương thần linh, 15 Hộp Trung thu, 10 Thẻ gia hạn và 150k Hồng ngọc");
//                                        }
//                                        break;
//                                    }
//                                    case 2: {
//                                        byte randommanh = (byte) new Random().nextInt(Manager.manhts.length);
//                                        int manh = Manager.manhts[randommanh];
//                                        if (player.inventory.coupon < 200) {
//                                            this.npcChat(player, "|7|Bạn không đủ 200 Điểm sự kiện");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 4) {
//                                            this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 4 ô trống");
//                                        } else {
//                                            player.inventory.coupon -= 200;
//                                            player.inventory.event += 200;
//                                            player.inventory.ruby += 100_000;
//                                            Service.getInstance().sendMoney(player);
//                                            Item manhthiensu = ItemService.gI().createNewItem((short) manh);
//                                            Item ruongthan = ItemService.gI().createNewItem((short) 1334);
//                                            Item hoptt = ItemService.gI().createNewItem((short) 1342);
//                                            Item thegh = ItemService.gI().createNewItem((short) 1346);
//                                            manhthiensu.quantity = 50;
//                                            ruongthan.quantity = 10;
//                                            hoptt.quantity = 10;
//                                            thegh.quantity = 5;
//                                            InventoryServiceNew.gI().addItemBag(player, manhthiensu);
//                                            InventoryServiceNew.gI().addItemBag(player, ruongthan);
//                                            InventoryServiceNew.gI().addItemBag(player, hoptt);
//                                            InventoryServiceNew.gI().addItemBag(player, thegh);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "|4|Bạn nhận được 50 " + manhthiensu.template.name
//                                                    + ", 10 Rương thần linh, 10 Hộp Trung thu, 5 Thẻ gia hạn và 100k Hồng ngọc");
//                                        }
//                                        break;
//                                    }
//                                    case 3: {
//                                        byte randommanh = (byte) new Random().nextInt(Manager.manhts.length);
//                                        int manh = Manager.manhts[randommanh];
//                                        if (player.inventory.coupon < 50) {
//                                            this.npcChat(player, "|7|Bạn không đủ 50 Điểm sự kiện");
//                                        } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 2) {
//                                            this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 2 ô trống");
//                                        } else {
//                                            player.inventory.coupon -= 50;
//                                            player.inventory.event += 50;
//                                            player.inventory.ruby += 25_000;
//                                            Service.getInstance().sendMoney(player);
//                                            Item manhthiensu = ItemService.gI().createNewItem((short) manh);
//                                            Item ruongthan = ItemService.gI().createNewItem((short) 1334);
//                                            manhthiensu.quantity = 10;
//                                            ruongthan.quantity = 5;
//                                            InventoryServiceNew.gI().addItemBag(player, manhthiensu);
//                                            InventoryServiceNew.gI().addItemBag(player, ruongthan);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.npcChat(player, "|4|Bạn nhận được 10 " + manhthiensu.template.name
//                                                    + ", 5 Rương thần linh và 25k Hồng ngọc");
//                                        }
//                                        break;
//                                    }
//                                }  
//                                break;
//                                case ConstEvent.HLWEEN:
//                                switch (select) {
//                                case 0:
//                                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                Item keo = InventoryServiceNew.gI().finditemnguyenlieuKeo(player);
//                                Item banh = InventoryServiceNew.gI().finditemnguyenlieuBanh(player);
//                                Item bingo = InventoryServiceNew.gI().finditemnguyenlieuBingo(player);
//                                if (keo != null && banh != null && bingo != null) {
//                                Item GioBingo = ItemService.gI().createNewItem((short) 2016, 1);
//                                // - Số item sự kiện có trong rương
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, keo, 10);
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, banh, 10);
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, bingo, 10);
//
//                                GioBingo.itemOptions.add(new ItemOption(74, 0));
//                                InventoryServiceNew.gI().addQuantityItemsBag(player, GioBingo, 0);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendThongBao(player, "Đổi quà sự kiện thành công");
//                            } else {
//                                Service.getInstance().sendThongBao(player,
//                                        "Vui lòng chuẩn bị x10 Nguyên Liệu Kẹo, Bánh Quy, Bí Ngô để đổi vật phẩm sự kiện");
//                            }
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                        }
//                        break;
//                    case 1:
//                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                            Item ve = InventoryServiceNew.gI().finditemnguyenlieuVe(player);
//                            Item giokeo = InventoryServiceNew.gI().finditemnguyenlieuGiokeo(player);
//
//                            if (ve != null && giokeo != null) {
//                                Item Hopmaquy = ItemService.gI().createNewItem((short) 2017, 1);
//                                // - Số item sự kiện có trong rương
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, ve, 3);
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, giokeo, 3);
//
//                                Hopmaquy.itemOptions.add(new ItemOption(74, 0));
//                                InventoryServiceNew.gI().addQuantityItemsBag(player, Hopmaquy, 0);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendThongBao(player, "Đổi quà sự kiện thành công");
//                            } else {
//                                Service.getInstance().sendThongBao(player,
//                                        "Vui lòng chuẩn bị x3 Vé đổi Kẹo và x3 Giỏ kẹo để đổi vật phẩm sự kiện");
//                            }
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                        }
//                        break;
//                    case 2:
//                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                            Item ve = InventoryServiceNew.gI().finditemnguyenlieuVe(player);
//                            Item giokeo = InventoryServiceNew.gI().finditemnguyenlieuGiokeo(player);
//                            Item hopmaquy = InventoryServiceNew.gI().finditemnguyenlieuHopmaquy(player);
//
//                            if (ve != null && giokeo != null && hopmaquy != null) {
//                                Item HopQuaHLW = ItemService.gI().createNewItem((short) 2012, 1);
//                                // - Số item sự kiện có trong rương
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, ve, 3);
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, giokeo, 3);
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, hopmaquy, 3);
//
//                                HopQuaHLW.itemOptions.add(new ItemOption(74, 0));
//                                HopQuaHLW.itemOptions.add(new ItemOption(30, 0));
//                                InventoryServiceNew.gI().addQuantityItemsBag(player, HopQuaHLW, 0);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendThongBao(player,
//                                        "Đổi quà hộp quà sự kiện Halloween thành công");
//                            } else {
//                                Service.getInstance().sendThongBao(player,
//                                        "Vui lòng chuẩn bị x3 Hộp Ma Quỷ, x3 Vé đổi Kẹo và x3 Giỏ kẹo để đổi vật phẩm sự kiện");
//                            }
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                        }
//                        break;
//                }
//                break;
//                case ConstEvent.NHAGIAO:// 20/11
//                switch (select) {
//                    case 3:
//                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                            if (player.inventory.event >= 999) {
//                                Item HopQua = ItemService.gI().createNewItem((short) 763, 1);
//                                player.inventory.event -= 999;
//                                HopQua.itemOptions.add(new ItemOption(207, 0));
//                                HopQua.itemOptions.add(new ItemOption(30, 0));
//                                InventoryServiceNew.gI().addItemBag(player, HopQua);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendThongBao(player, "Bạn nhận được Hộp Quà Teacher Day");
//                            } else {
//                                Service.getInstance().sendThongBao(player, "Cần 999 điểm tích lũy để đổi");
//                            }
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                        }
//                        break;
//                    default:
//                        int n = 0;
//                        switch (select) {
//                            case 0:
//                                n = 1;
//                                break;
//                            case 1:
//                                n = 10;
//                                break;
//                            case 2:
//                                n = 99;
//                                break;
//                        }
//                        if (n > 0) {
//                            Item bonghoa = InventoryServiceNew.gI().finditemBongHoa(player, n);
//                            if (bonghoa != null) {
//                                player.inventory.event += n;
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, bonghoa, n);
//                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + n + " điểm sự kiện");
//                            } else {
//                                Service.getInstance().sendThongBao(player,
//                                        "Cần ít nhất " + n + " bông hoa để có thể tặng");
//                            }
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Cần ít nhất " + n + " bông hoa để có thể tặng");
//                        }
//                }
//                break;
//                case ConstEvent.NOEL:
//                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                    Item keogiangsinh = InventoryServiceNew.gI().finditemKeoGiangSinh(player);
//
//                    if (keogiangsinh != null && keogiangsinh.quantity >= 99) {
//                        Item tatgiangsinh = ItemService.gI().createNewItem((short) 649, 1);
//                        // - Số item sự kiện có trong rương
//                        InventoryServiceNew.gI().subQuantityItemsBag(player, keogiangsinh, 99);
//
//                        tatgiangsinh.itemOptions.add(new ItemOption(74, 0));
//                        tatgiangsinh.itemOptions.add(new ItemOption(30, 0));
//                        InventoryServiceNew.gI().addQuantityItemsBag(player, tatgiangsinh, 0);
//                        InventoryServiceNew.gI().sendItemBags(player);
//                        Service.getInstance().sendThongBao(player, "Bạn nhận được Tất,vớ giáng sinh");
//                    } else {
//                        Service.getInstance().sendThongBao(player,
//                                "Vui lòng chuẩn bị x99 kẹo giáng sinh để đổi vớ tất giáng sinh");
//                    }
//                } else {
//                    Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                }
//                break;
//                case ConstEvent.TET:
//                switch (select) {
//                    case 0:
//                        if (player.diemdanhtet < 1) {
//                            Calendar cal = Calendar.getInstance();
//                            int day = cal.get(Calendar.DAY_OF_MONTH);
//                            if (day >= 8 && day <= 15) {
//                                player.diemdanhtet++;
//                                int[] hn = {1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000};
//                                int slhn = hn[Util.nextInt(hn.length)];
//                                int sltv = Util.nextInt(1, 10);
//                                Item tv = ItemService.gI().createNewItem((short) 457);
//                                player.inventory.ruby += slhn;
//                                tv.quantity = sltv;
//                                InventoryServiceNew.gI().addItemBag(player, tv);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                PlayerService.gI().sendInfoHpMpMoney(player);
//                                Service.getInstance().sendThongBaoOK(player, "NroKamui lì xì cho bạn\nx" + slhn + " hồng ngọc\nx" + sltv + " thỏi vàng");
//                                Service.getInstance().sendThongBao(player, "Nhận lì xì thành công,chúc bạn năm mới dui dẻ");
//                            } else if (day > 15) {
//                                Service.getInstance().sendThongBao(player, "Hết tết rồi còn đòi lì xì");
//                            } else {
//                                Service.getInstance().sendThongBao(player, "Đã tết đâu mà đòi lì xì");
//                            }
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Bạn đã nhận lì xì rồi");
//                        }
//                        break;
//                    case 1:
//                        ShopServiceNew.gI().opendShop(player, "SHOP_LUOT", true);
//                        break;
//                    case 2:
//                        ShopServiceNew.gI().opendShop(player, "SHOP_TET", true);
//                        break;
//                    case 3:
//                        if (InventoryServiceNew.gI().findItemBag(player, 1436) == null && InventoryServiceNew.gI().findItemBody(player, 1436) == null) {
//                            Item qdbm = ItemService.gI().createNewItem((short) 1436);
//                            qdbm.itemOptions.add(new ItemOption(101, 12));
//                            qdbm.itemOptions.add(new ItemOption(30, 0));
//                            InventoryServiceNew.gI().addItemBag(player, qdbm);
//                            InventoryServiceNew.gI().sendItemBags(player);
//                            Service.getInstance().sendThongBao(player, "Bạn nhận được Quần Hoa Văn");
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Bạn đã nhận Quần Hoa Văn rồi.");
//                        }
//                        break;
//                }
//                break;
//            case ConstEvent.PHUNU:
//                switch (select) {
//                    case 3:
//                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                            Input.gI().bonghoa(player);
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                        }
//                        break;
//                    default:
//                        int n = 0;
//                        switch (select) {
//                            case 0:
//                                n = 1;
//                                break;
//                            case 1:
//                                n = 10;
//                                break;
//                            case 2:
//                                n = 99;
//                                break;
//                        }
//                        if (n > 0) {
//                            Item bonghoa = InventoryServiceNew.gI().finditemBongHoa(player, n);
//                            if (bonghoa != null) {
//                                player.inventory.event += n;
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, bonghoa, n);
//                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + n + " điểm sự kiện");
//                            } else {
//                                Service.getInstance().sendThongBao(player,
//                                        "Cần ít nhất " + n + " bông hoa để có thể tặng");
//                            }
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Cần ít nhất " + n + " bông hoa để có thể tặng");
//                        }
//                    }
//                    break;
//                    default:
//                        break;
//                        }
//                    }
//                }
//            }
//        };
//    }

    /**
     * **************************************************************************************************************************************************************************
     */
    public static Npc pilap(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ngươi có muốn đổi ngọc rồng cho ta không?",
                            "Đổi\n Ngọc rồng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop ngoc rong
                                    ShopServiceNew.gI().opendShop(player, "PILAP_NGOCRONG", false);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc tuongred(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta có rất nhiều linh thú ngươi có muốn đổi với ta?",
                            "Đổi\nLinh thú ");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop linh thu
                                    ShopServiceNew.gI().opendShop(player, "TUONGRED_LINHTHU", false);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc uron(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    ShopServiceNew.gI().opendShop(pl, "URON", false);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc thoren(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|2|Ta sẽ cho ngươi thuê các trang bị vip, hsd từ 3-5 ngày\n"
                                + "Ngươi có thể gia hạn được trang bị đó thông qua vé gia hạn\n"
                                + "Mỗi trang bị khi mua ra sẽ random hsd từ 3-5 ngày hãy nhớ rõ\n"
                                + "Ta sẽ liên tục cập nhật shop của ta cho ngươi lựa chọn\n"
                                + "Ngươi có muốn mua không?",
                                "Thuê\nTrang Bị", "Gia Hạn\nTrang Bị");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.session.actived == 1) {
                                        ShopServiceNew.gI().opendShop(player, "THUEDO", false);
                                        return;
                                    }
                                    Service.gI().sendThongBao(player, "Bạn hãy mở thành viên!!");
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.THUE_DO);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.THO_REN) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.THUE_DO:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc noibanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 5) {
                    if (ConstEvent.EVENT == 7) {
                        if (player.BanhChung > 0 && NauBanh.gI().NauXong == false || player.BanhTet > 0 && NauBanh.gI().NauXong == false || player.BanhChung > 0 && player.BanhTet > 0 && NauBanh.gI().NauXong == false) {
                            this.createOtherMenu(player, 0, "|2|Nồi Bánh Toàn Server Đợt " + NauBanh.Count
                                    + "\n|-1|Đang đang có bánh chưa nhận"
                                    + "\nBạn đã nấu: " + player.BanhChung + " Bánh Chưng và " + player.BanhTet + " Bánh Tét"
                                    + "\nHãy nhận bánh trước!",
                                    "Lấy Bánh");
                        } else if (NauBanh.gI().NauXong == true) {
                            this.createOtherMenu(player, 0, "|2|Nồi Bánh Toàn Server Đợt " + NauBanh.Count
                                    + "\n|-1|Đang trong thời gian chờ nhận bánh"
                                    + "\n|7|Thời gian chờ nhận bánh còn: " + Util.tinhgio(NauBanh.ThoiGianChoLayBanh)
                                    + "\nBạn đã nấu: " + player.BanhChung + " Bánh Chưng và " + player.BanhTet + " Bánh Tét"
                                    + "\nHãy mau chóng nhận bánh, nếu hết thời gian"
                                    + "\nHoặc bạn offline số bánh sẽ được tính vào đợt sau!",
                                    "Lấy Bánh");
                        } else if (NauBanh.gI().ChoXong == true) {
                            this.createOtherMenu(player, 1, "|2|Nồi Bánh Toàn Server Đợt " + NauBanh.Count
                                    + "\n|-1|Đang trong thời gian nấu"
                                    + "\n|7|Thời gian nấu bánh còn: " + Util.tinhgio(NauBanh.ThoiGianNau)
                                    + "\n|-1|Hiện có: " + NauBanh.gI().ListPlNauBanh.size() + " người đã tham gia"
                                    + " (" + (NauBanh.SoLuongBanhDangNau) + " bánh đang nấu)"
                                    + ((player.BanhChungDaNau + player.BanhTetDaNau) > 0 ? "\nTrong đó bạn có: " + (player.BanhChungDaNau + player.BanhTetDaNau) + " bánh mới" : "")
                                    + ((player.BanhChung + player.BanhTet) > 0 ? "\n(Và " + (player.BanhChung + player.BanhTet) + " bánh đã chín chưa lấy)" : "")
                                    + "\nHãy chú ý đến củi, nước có thể hao hụt trong thời gian nấu"
                                    + "\nMực nước trong nồi: " + Util.format(NauBanh.Nuoc / 500 * 100) + "/100% "
                                    + (NauBanh.Nuoc >= 100 && NauBanh.Nuoc < 300 ? "(Thấp)" : NauBanh.Nuoc >= 300 && NauBanh.Nuoc < 500 ? "(Trung Bình)" : NauBanh.Nuoc >= 500 ? "(Đã đầy)" : "(Rất ít)")
                                    + "\nSố củi đã thêm: " + NauBanh.Cui + "/500 que"
                                    + "\nKhi củi, nước quá thấp số bánh có thể sẽ bị cháy",
                                    "Nấu bánh chưng", "Nấu bánh tét", "Thêm củi", "Thêm nước");
                        } else if (NauBanh.gI().ChoXong == false) {
                            this.createOtherMenu(player, 4, "|2|Nồi Bánh Toàn Server Đợt " + NauBanh.Count
                                    + "\n|-1|Đang trong thời gian chờ"
                                    + "\n|7|Thời gian chờ còn: " + Util.tinhgio(NauBanh.ThoiGianCho)
                                    + "\n|-1|Mực nước trong nồi: " + Util.format(NauBanh.Nuoc / 500 * 100) + "/100% "
                                    + (NauBanh.Nuoc >= 100 && NauBanh.Nuoc < 300 ? "(Thấp)" : NauBanh.Nuoc >= 300 && NauBanh.Nuoc < 500 ? "(Trung Bình)" : NauBanh.Nuoc >= 500 ? "(Đã đầy)" : "(Rất ít)")
                                    + "\nSố củi đã thêm: " + NauBanh.Cui + "/500 que"
                                    + "\nĐủ số lượng củi vả nước sẽ bắt đầu nấu",
                                    "Thêm nước", "Them cu!");
                        }
                    } else {
                        Service.gI().sendBigMessage(player, 25516, "Hiện chưa mở!");
                        return;
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        switch (player.iDMark.getIndexMenu()) {
                            case 0:
                                if (player.BanhChung == 0 && player.BanhTet == 0) {
                                    Service.gI().sendThongBao(player, "Có nấu gì đéo đâu mà đòi nhận");
                                    return;
                                }
                                Item BanhChung = ItemService.gI().createNewItem((short) 1446, player.BanhChung);
                                Item BanhTet = ItemService.gI().createNewItem((short) 1445, player.BanhTet);
                                Service.gI().sendThongBaoOK(player, "Hoàn Thành Nấu Bánh Đợt " + NauBanh.Count
                                        + "\nBạn nhận được"
                                        + (player.BanhChung > 0 ? "\nx" + player.BanhChung + " Bánh Chưng" : "")
                                        + (player.BanhTet > 0 ? "\nx" + player.BanhTet + " Bánh Tét" : ""));
                                player.inventory.event += (player.BanhChung + player.BanhTet);
                                if (player.BanhChung > 0) {
                                    InventoryServiceNew.gI().addItemBag(player, BanhChung);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.BanhChung = 0;
                                    player.BanhChungDaNau = 0;
                                }
                                if (player.BanhTet > 0) {
                                    InventoryServiceNew.gI().addItemBag(player, BanhTet);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.BanhTet = 0;
                                    player.BanhTetDaNau = 0;
                                }
                                break;
                            case 1:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, 2, "Bánh chưng: 12 Lá giong, 2 Gạo nếp, 1 Đậu xanh, 12 Gióng tre, 3 Thịt lợn, 1 Muối và 03 Nước nấu.", "Nấu", "Đóng");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, 3, "Bánh tết: 12 Lá chuối, 2 Gạo nếp, 1 Đậu xanh, 12 Giống tre, 3 Thịt lợn, 1 Muối và 03 Nước nấu.", "Nấu", "Đóng");
                                        break;
                                }
                                break;
                            case 2:
                                Input.gI().createFormNauBanhChung(player);
                                break;
                            case 3:
                                Input.gI().createFormNauBanhTet(player);
                                break;
                            case 4:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, 5, "|2|Nồi Bánh Toàn Server Đợt " + NauBanh.Count
                                                + "\n|-1|Đang trong thời gian chờ"
                                                + "\n|7|Thời gian chờ còn: " + Util.tinhgio(NauBanh.ThoiGianCho)
                                                + "\n|-1|Mực nước trong nồi: " + Util.format(NauBanh.Nuoc / 500 * 100) + "/100% "
                                                + (NauBanh.Nuoc >= 100 && NauBanh.Nuoc < 300 ? "(Thấp)" : NauBanh.Nuoc >= 300 && NauBanh.Nuoc < 500 ? "(Trung Bình)" : NauBanh.Nuoc >= 500 ? "(Đã đầy)" : "(Rất ít)")
                                                + "\nSố củi đã thêm: " + NauBanh.Cui + "/500 que"
                                                + "\nĐủ số lượng củi vả nước sẽ bắt đầu nấu",
                                                "Thêm\n1 Gáo", "Thêm\n5 Gáo", "Thêm\n 10 Gáo");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, 6, "|2|Nồi Bánh Toàn Server Đợt " + NauBanh.Count
                                                + "\n|-1|Đang trong thời gian chờ"
                                                + "\n|7|Thời gian chờ còn: " + Util.tinhgio(NauBanh.ThoiGianCho)
                                                + "\n|-1|Mực nước trong nồi: " + Util.format(NauBanh.Nuoc / 500 * 100) + "/100% "
                                                + (NauBanh.Nuoc >= 100 && NauBanh.Nuoc < 300 ? "(Thấp)" : NauBanh.Nuoc >= 300 && NauBanh.Nuoc < 500 ? "(Trung Bình)" : NauBanh.Nuoc >= 500 ? "(Đã đầy)" : "(Rất ít)")
                                                + "\nSố củi đã thêm: " + NauBanh.Cui + "/500 que"
                                                + "\nĐủ số lượng củi vả nước sẽ bắt đầu nấu",
                                                "Thêm\n1 Que", "Thêm\n3 Que", "Thêm\n10 Que");
                                        break;
                                }
                                break;
                            case 5:
                                Item nuocNau = InventoryServiceNew.gI().findItemBag(player, 1443);
                                switch (select) {
                                    case 0:
                                        if (nuocNau == null || nuocNau.quantity < 1) {
                                            Service.gI().sendThongBao(player, "Có nước đâu.");
                                            return;
                                        }
                                        if (NauBanh.Nuoc < 500) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, nuocNau, 1);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            NauBanh.Nuoc++;
                                            Service.gI().sendThongBao(player, "Đã thêm\n1 gáo nước mưa");
                                        } else {
                                            Service.gI().sendThongBao(player, "Đủ nước rồi.");
                                        }
                                        break;
                                    case 1:
                                        if (nuocNau == null || nuocNau.quantity < 5) {
                                            Service.gI().sendThongBao(player, "Có nước đâu.");
                                            return;
                                        }
                                        if (NauBanh.Nuoc < 500) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, nuocNau, 5);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            NauBanh.Nuoc += 5;
                                            Service.gI().sendThongBao(player, "Đã thêm\n5 gáo nước mưa");
                                        } else {
                                            Service.gI().sendThongBao(player, "Đủ nước rồi.");
                                        }
                                        break;
                                    case 2:
                                        if (nuocNau == null || nuocNau.quantity < 10) {
                                            Service.gI().sendThongBao(player, "Có nước đâu.");
                                            return;
                                        }
                                        if (NauBanh.Nuoc < 500) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, nuocNau, 10);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            NauBanh.Nuoc += 10;
                                            Service.gI().sendThongBao(player, "Đã thêm\n10 gáo nước mưa");
                                        } else {
                                            Service.gI().sendThongBao(player, "Đủ nước rồi.");
                                        }
                                        break;
                                }
                                break;
                            case 6:
                                Item cuiLua = InventoryServiceNew.gI().findItemBag(player, 1444);
                                switch (select) {
                                    case 0:
                                        if (cuiLua == null || cuiLua.quantity < 1) {
                                            Service.gI().sendThongBao(player, "Có củi đâu.");
                                            return;
                                        }
                                        if (NauBanh.Cui < 500) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, cuiLua, 1);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            NauBanh.Cui++;
                                            Service.gI().sendThongBao(player, "Da them cu!");
                                        } else {
                                            Service.gI().sendThongBao(player, "Đủ củi rồi.");
                                        }
                                        break;
                                    case 1:
                                        if (cuiLua == null || cuiLua.quantity < 3) {
                                            Service.gI().sendThongBao(player, "Có củi đâu.");
                                            return;
                                        }
                                        if (NauBanh.Cui < 500) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, cuiLua, 3);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            NauBanh.Cui += 3;
                                            Service.gI().sendThongBao(player, "Da them cu!");
                                        } else {
                                            Service.gI().sendThongBao(player, "Đủ củi rồi.");
                                        }
                                        break;
                                    case 2:
                                        if (cuiLua == null || cuiLua.quantity < 10) {
                                            Service.gI().sendThongBao(player, "Có củi đâu.");
                                            return;
                                        }
                                        if (NauBanh.Cui < 500) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, cuiLua, 10);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            NauBanh.Cui += 10;
                                            Service.gI().sendThongBao(player, "Da them cu!");
                                        } else {
                                            Service.gI().sendThongBao(player, "Đủ củi rồi.");
                                        }
                                        break;
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc baHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Ép sao\ntrang bị", "Pha Lê Hóa\nThường","Pha Lê Hóa\nVIP","Nâng Cấp\nSKH", "Nâng Cấp\nSKH Thần Linh", "Nâng Cấp\nChân Mệnh", "Phân rã\nĐồ Thần","Nhập Đá Skh","Nhập SPL VIP");
                    } else if (this.mapId == 121) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Về đảo\nrùa");
                    } else {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm",
                                "Chế tạo\nBông tai\nCấp 2", "Mở chỉ số\nBông tai\nCấp 2",
                                "Chế tạo\nBông tai\nCấp 3", "Mở chỉ số\nBông tai\nCấp 3",
                                "Chế tạo\nBông tai\nCấp 4", "Mở chỉ số\nBông tai\nCấp 4",
                                //        "Chế tạo\nBông tai\nCấp 5", "Mở chỉ số\nBông tai\nCấp 5",
                                "Nhập\nNgọc Rồng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                    break;
                                        case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI_VIP);
                                    break;
                                case 3: // phân rã đồ thần linh
//                                    CombineServiceNew.gI().openTabCombine(player,
//                                            CombineServiceNew.PHAN_RA_DO_THAN_LINH);
                                    createOtherMenu(player, ConstNpc.MENU_NANG_SKH_LVL,
                                            "Ngươi tìm ta có việc gì?\n",
                                            "Nâng\nSKH [LVL1]", "Nâng\nSKH [LVL2]", "Từ Chối");
                                    break;
                                case 4:
                                    createOtherMenu(player, ConstNpc.MENU_NANG_SKH_TL_LVL,
                                            "Ngươi tìm ta có việc gì?\n",
                                            "Nâng\nSKH Thần [LVL0]", "Nâng\nSKH Thần [LVL1]", "Nâng\nSKH Thần [LVL2]", "Từ Chối");
                                    break;
                                case 5:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_CHAN_MENH);
                                    break;
                                case 6:
                                    createOtherMenu(player, ConstNpc.MENU_PHAN_RA,
                                            "Ngươi tìm ta có việc gì?\n",
                                            "Đồ Thần X1","Đồ Thần X3", "Đồ Thần X5","Từ Chối");
                                    break;
                                     case 7:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_DA_SKH);
                                    break;
                                      case 8:
                                 if  (player.playerTask.taskMain.id >= 22) {
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_SPL);
                                     } else {
                                               this.npcChat(player, "Làm tới nhiệm vụ 22 để nhập spl vip");
                                     }
                                    break;
                                   
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } //                                case 6:
                        //                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.LUYEN_HOA_CHIEN_LINH);
                        //                                    break;
                        //                                case 7:
                        //                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_GIOI_HAN_CHIEN_LINH);
                        //                                    break;
                        else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                case CombineServiceNew.NANG_CAP_SKH_THUONG:
                                case CombineServiceNew.NANG_CAP_SKH_VIP:
                                case CombineServiceNew.NANG_CAP_CHAN_MENH:
                                case CombineServiceNew.NANG_SKH_LVL1:
                                case CombineServiceNew.NANG_SKH_LVL2:
                                case CombineServiceNew.NANG_SKH_TL_LVL0:
                                case CombineServiceNew.NANG_SKH_TL_LVL1:
                                case CombineServiceNew.NANG_SKH_TL_LVL2:
                             case CombineServiceNew.PHAN_RA_DO_THAN_LINH_X5:
                                    case CombineServiceNew.PHAN_RA_DO_THAN_LINH_X3:
                                     case CombineServiceNew.PHAN_RA_DO_THAN_LINH:
                                            case CombineServiceNew.NHAP_DA_SKH:
                                                case CombineServiceNew.NHAP_SPL:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                    switch (select) {
                                        case 0:
                                            if (!player.autodapdo) {
                                                if (player.vip >= 0) {
                                                    CombineServiceNew.gI().phaLeHoaTrangBiVIP(player, 1);
                                                } else {
                                                    CombineServiceNew.gI().startCombine(player);
                                                }
                                            } else {
                                                CombineServiceNew.gI().startCombine(player);
                                            }
                                            break;
                                        case 1:
                                            if (!player.autodapdo) {
                                                if (player.vip >= 0) {
                                                    CombineServiceNew.gI().phaLeHoaTrangBiVIP(player, 10);
                                                } else {
                                                    CombineServiceNew.gI().startCombine(player);
                                                }
                                            } else {
                                                CombineServiceNew.gI().startCombine(player);
                                            }
                                            break;
                                        case 2:
                                            if (!player.autodapdo) {
                                                if (player.vip > 0) {
                                                    CombineServiceNew.gI().phaLeHoaTrangBiVIP(player, 100);
                                                } else {
                                                    CombineServiceNew.gI().startCombine(player);
                                                }
                                            } else {
                                                CombineServiceNew.gI().startCombine(player);
                                            }
                                            break;
                                    }
                                    break;
                                      case CombineServiceNew.PHA_LE_HOA_TRANG_BI_VIP:
                                    switch (select) {
                                        case 0:
                                            if (!player.autodapdo) {
                                                
                                                    CombineServiceNew.gI().startCombine(player);
                                               
                                            } else {
                                                CombineServiceNew.gI().startCombine(player);
                                            }
                                            break;
                                        case 1:
                                            if (!player.autodapdo) {
                                               
                                                    CombineServiceNew.gI().startCombine(player);
                                                
                                            } else {
                                                CombineServiceNew.gI().startCombine(player);
                                            }
                                            break;
                                        case 2:
                                            if (!player.autodapdo) {
                                                
                                                    CombineServiceNew.gI().startCombine(player);
                                                
                                            } else {
                                                CombineServiceNew.gI().startCombine(player);
                                            }
                                            break;
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHAN_RA_DO_THAN_LINH);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHAN_RA_DO_THAN_LINH_X3);
                                    break;
                                      case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHAN_RA_DO_THAN_LINH_X5);
                                    break;
                                    

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_SKH_LVL) {
                            switch (select) {

                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_SKH_LVL1);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_SKH_LVL2);
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_SKH_TL_LVL) {
                            switch (select) {

                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_SKH_TL_LVL0);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_SKH_TL_LVL1);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_SKH_TL_LVL2);
                                    break;

                            }
                        }
                    } else if (this.mapId == 112) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                    break;
                            }
                        }
                    } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                            "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                            + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                            "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng", "Đóng");
                                    break;
                                case 1://nâng cấp vật phẩm
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                    break;
                                case 2: //nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_1_LEN_2);
                                    break;
                                case 3: //
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP2);
                                    break;
                                case 4: //nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_2_LEN_3);
                                    break;
                                case 5: //
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP3);
                                    break;

                                case 6: //nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_3_LEN_4);
                                    break;
                                case 7: //
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP4);
                                    break;
                                case 8: //
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                    break;

//                                case 9: //nâng cấp bông tai
//                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP5);
//                                    break;
//                                case 10: //
//                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP5);
//                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", true);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "BUA_8H", true);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_BONG_TAI_1_LEN_2:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP2:
                                case CombineServiceNew.NANG_CAP_BONG_TAI_2_LEN_3:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP3:
                                case CombineServiceNew.NANG_CAP_BONG_TAI_3_LEN_4:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP4:
                                case CombineServiceNew.NANG_CAP_BONG_TAI_CAP5:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP5:

                                case CombineServiceNew.NHAP_NGOC_RONG:

                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                    switch (select) {
                                        case 0:
                                            CombineServiceNew.gI().nangCapVatPham(player, 1);
                                            break;
                                        case 1:
                                            CombineServiceNew.gI().nangCapVatPham(player, 10);
                                            break;
                                        case 2:
                                            CombineServiceNew.gI().nangCapVatPham(player, 100);
                                            break;
                                    }
                                    break;
                            }
//                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
//                            if (select == 0) {
//                                CombineServiceNew.gI().startCombine(player);
//                            }
//                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
//                            if (select == 0) {
//                                CombineServiceNew.gI().startCombine(player);
//                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_LINH_THU) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc ruongDo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    InventoryServiceNew.gI().sendItemBox(player);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc duongtank(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 5 || mapId == 0 || mapId == 7 || mapId == 14) {
                        try {
                            MapMaBu.gI().setTimeJoinMapMaBu();
                            if (this.mapId == 5 || mapId == 0 || mapId == 7 || mapId == 14) {
                                long now2 = System.currentTimeMillis();
                                if (now2 > MapMaBu.TIME_OPEN_TN && now2 < MapMaBu.TIME_CLOSE_TN || player.itemTime.VENGOC == true) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_TN, "|1|Map Up Tiềm Năng đã mở cửa, tham gia ngay?",
                                            "Chiến Ngay", "XEM TOP");
                                } else if (now2 > MapMaBu.TIME_OPEN_TN2 && now2 < MapMaBu.TIME_CLOSE_TN2 | player.itemTime.VENGOC == true) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_TN2, "|1|Map Up Tiềm Năng đã mở cửa, tham gia ngay?",
                                            "Chiến Ngay", "XEM TOP");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_TN,
                                            "|7|HIỆN CHƯA ĐẾN THỜI GIAN MỞ MAP\n|2|Thời Gian Mở : 9-12H SÁNG và 9-12H TỐI\n"
                                            + "Không giới hạn số lần vào map trong thời gian mở map\n"
                                            + "|7|Có thể up capsule bang hội tại đây!\n"
                                            + "[ Khi Map Đóng, Chỉ Những Ai Đang Sử Dụng Vé Mới Có Thể Ở Lại ]", "Đóng", "Xem Top");
                                }
                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu DUONG TANG");
                        }
                    }
                    if (mapId == 123 || mapId == 205) {
                        this.createOtherMenu(player, 0, "Bạn Muốn Quay Trở Lại Làng Aru?", "OK", "Từ chối");

                    }
                    if (mapId == 122 || mapId == 204) {
                        this.createOtherMenu(player, 0, "Xia xia thua phùa\b|7|Thí chủ đang có: " + player.nhsPoint + " điểm ngũ hành sơn\b|1|Thí chủ có muốn đổi 500 điểm tu luyện của mình lấy cải trang không?", "Đổi cải trang", "Top Ngũ Hành Sơn", "Không");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (select) {
                        case 0:
                            if (this.mapId == 5 || this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.MENU_OPEN_TN:
                                        if (select == 0) {
                                            ChangeMapService.gI().changeMapInYard(player, 123, -1, 174);
                                        }
                                        break;
                                    case ConstNpc.MENU_OPEN_TN2:
                                        if (select == 0) {
                                            ChangeMapService.gI().changeMapInYard(player, 206, -1, 174);
                                        }
                                        break;
                                    case ConstNpc.MENU_NOT_OPEN_TN:
                                        if (select == 0) {
                                            this.npcChat(player, "Hẹn gặp lại");
                                        }
                                        break;
                                }
                            }
                            if (mapId == 123 || mapId == 205) {
                                ChangeMapService.gI().changeMapInYard(player, 0, -1, 469);
                            }
                            if (mapId == 122 || mapId == 204) {
                                if (select == 0) {
                                    if (player.nhsPoint <= 499) {
                                        this.npcChat(player, "Thí chủ không đủ điểm");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                        return;
                                    } else {
                                        player.nhsPoint -= 500;
                                        Item item = ItemService.gI().createNewItem((short) (711));
                                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 20)));// hp 30%
                                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 20)));// ki 15%
                                        item.itemOptions.add(new Item.ItemOption(50, new Random().nextInt(10) + 10));// sd 20%
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(9, 10)));//hsd
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Chúc Mừng Thí Chủ Đổi Vật Phẩm Thành Công!");
                                    }
                                    break;
                                }
                            }
                            break;
                        case 1:
                            Service.getInstance().showListTop(player, Manager.topNHS);
                            break;
                        case 2:
                            this.npcChat(player, "Thí chủ hãy theo ta lấy chân kinh");
                            break;
                    }
                }
            }
        };
    }

    public static Npc wukong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Lão Tôn sẽ giúp ngươi có những thứ ngươi muốn khẹt khẹt !!", "Dâng\nđào", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 122 || this.mapId == 204) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Item daotien = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1536);
                                    if (daotien != null && daotien.quantity >= 9999) {
                                        ShopServiceNew.gI().opendShop(player, "DAO_TIEN", true);
                                        break;
                                    } else {
                                        Service.gI().sendThongBao(player, "Mang 9999 quả đào đến cho lão Tôn mau lên!");
                                        break;
                                    }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc whis56(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                    if (canOpenNpc(player)) {
                        if (this.mapId == 154) {
                            createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Ngươi có muốn học chiêu mới không?",
                                    "Học Chiêu\nTrái Đất", "Học Chiêu\nNamếc", "Học Chiêu\nXay da", "Đóng");
                        } else {
                            createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ngươi muốn gì ở ta?", "Đóng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.gender == 0) {
                                        this.createOtherMenu(player, ConstNpc.HOC_SKILL_TD, "|7|Học Chiêu Siêu Cấp Trái Đất\n"
                                                + "|2|[ YÊU CẦU BAN ĐẦU ]\n"
                                                + "|5|+ 60 tỉ tiềm năng\n+ 50.000 hồng ngọc\n+ 1 tỉ vàng\n"
                                                + "|2|Mỗi cấp học sẽ tăng thêm 10 tỉ tiềm năng và 90% thành thạo!\n"
                                                + "|7|Chọn Level Skill Muốn Học",
                                                "Cấp 1", "Cấp 2", "Cấp 3", "Cấp 4", "Cấp 5", "Cấp 6", "Cấp 7", "Đóng");
                                        break;
                                    } else {
                                        Service.gI().sendThongBao(player, "Học chiêu đúng với hành tinh của mình");
                                    }
                                    break;
                                case 1:
                                    if (player.gender == 1) {
                                        this.createOtherMenu(player, ConstNpc.HOC_SKILL_NM, "|7|Học Chiêu Siêu Cấp Namek\n"
                                                + "|2|[ YÊU CẦU BAN ĐẦU ]\n"
                                                + "|5|+ 60 tỉ tiềm năng\n+ 50.000 hồng ngọc\n+ 1 tỉ vàng\n"
                                                + "|2|Mỗi cấp học sẽ tăng thêm 10 tỉ tiềm năng và 90% thành thạo!\n"
                                                + "|7|Chọn Level Skill Muốn Học", "Cấp 1", "Cấp 2", "Cấp 3", "Cấp 4", "Cấp 5", "Cấp 6", "Cấp 7", "Đóng");
                                        break;
                                    } else {
                                        Service.gI().sendThongBao(player, "Học chiêu đúng với hành tinh của mình");
                                    }
                                    break;
                                case 2:
                                    if (player.gender == 2) {
                                        this.createOtherMenu(player, ConstNpc.HOC_SKILL_XD, "|7|Học Chiêu Siêu Cấp Xayda\n"
                                                + "|2|[ YÊU CẦU BAN ĐẦU ]\n"
                                                + "|5|+ 60 tỉ tiềm năng\n+ 50.000 hồng ngọc\n+ 1 tỉ vàng\n"
                                                + "|2|Mỗi cấp học sẽ tăng thêm 10 tỉ tiềm năng và 90% thành thạo!\n"
                                                + "|7|Chọn Level Skill Muốn Học", "Cấp 1", "Cấp 2", "Cấp 3", "Cấp 4", "Cấp 5", "Cấp 6", "Cấp 7", "Đóng");
                                        break;
                                    } else {
                                        Service.gI().sendThongBao(player, "Học chiêu đúng với hành tinh của mình");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.HOC_SKILL_TD) {
                            switch (select) {
                                case 0:
                                    Item td1 = InventoryServiceNew.gI().findItemBag(player, 1388);
                                    if (td1 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 0 && td1 == null && player.nPoint.tiemNang >= 60000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 60000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1388));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 1:
                                    Item td2 = InventoryServiceNew.gI().findItemBag(player, 1389);
                                    if (td2 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 0 && td2 == null && player.nPoint.tiemNang >= 70000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 70000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1389));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 2:
                                    Item td3 = InventoryServiceNew.gI().findItemBag(player, 1390);
                                    if (td3 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 0 && td3 == null && player.nPoint.tiemNang >= 80000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 80000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1390));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 3:
                                    Item td4 = InventoryServiceNew.gI().findItemBag(player, 1391);
                                    if (td4 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 0 && td4 == null && player.nPoint.tiemNang >= 90000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 90000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1391));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 4:
                                    Item td5 = InventoryServiceNew.gI().findItemBag(player, 1392);
                                    if (td5 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 0 && td5 == null && player.nPoint.tiemNang >= 100000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 100000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1392));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 5:
                                    Item td6 = InventoryServiceNew.gI().findItemBag(player, 1393);
                                    if (td6 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 0 && td6 == null && player.nPoint.tiemNang >= 110000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 110000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1393));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 6:
                                    Item td7 = InventoryServiceNew.gI().findItemBag(player, 1394);
                                    if (td7 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 0 && td7 == null && player.nPoint.tiemNang >= 120000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 120000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1394));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.HOC_SKILL_NM) {
                            switch (select) {
                                case 0:
                                    Item nm1 = InventoryServiceNew.gI().findItemBag(player, 1402);
                                    if (nm1 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 1 && nm1 == null && player.nPoint.tiemNang >= 60000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 60000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1402));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 1:
                                    Item nm2 = InventoryServiceNew.gI().findItemBag(player, 1403);
                                    if (nm2 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 1 && nm2 == null && player.nPoint.tiemNang >= 70000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 70000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1403));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 2:
                                    Item nm3 = InventoryServiceNew.gI().findItemBag(player, 1404);
                                    if (nm3 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 1 && nm3 == null && player.nPoint.tiemNang >= 80000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 80000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1404));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 3:
                                    Item nm4 = InventoryServiceNew.gI().findItemBag(player, 1405);
                                    if (nm4 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 1 && nm4 == null && player.nPoint.tiemNang >= 90000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 90000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1405));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 4:
                                    Item nm5 = InventoryServiceNew.gI().findItemBag(player, 1406);
                                    if (nm5 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 1 && nm5 == null && player.nPoint.tiemNang >= 100000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 100000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1406));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 5:
                                    Item nm6 = InventoryServiceNew.gI().findItemBag(player, 1407);
                                    if (nm6 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 1 && nm6 == null && player.nPoint.tiemNang >= 110000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 110000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1407));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 6:
                                    Item nm7 = InventoryServiceNew.gI().findItemBag(player, 1408);
                                    if (nm7 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 1 && nm7 == null && player.nPoint.tiemNang >= 120000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 120000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1408));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.HOC_SKILL_XD) {
                            switch (select) {
                                case 0:
                                    Item xd1 = InventoryServiceNew.gI().findItemBag(player, 1395);
                                    if (xd1 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 2 && xd1 == null && player.nPoint.tiemNang >= 60000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 60000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1395));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 1:
                                    Item xd2 = InventoryServiceNew.gI().findItemBag(player, 1396);
                                    if (xd2 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 2 && xd2 == null && player.nPoint.tiemNang >= 70000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 70000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1396));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 2:
                                    Item xd3 = InventoryServiceNew.gI().findItemBag(player, 1397);
                                    if (xd3 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 2 && xd3 == null && player.nPoint.tiemNang >= 80000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 80000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1397));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 3:
                                    Item xd4 = InventoryServiceNew.gI().findItemBag(player, 1398);
                                    if (xd4 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 2 && xd4 == null && player.nPoint.tiemNang >= 90000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 90000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1398));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 4:
                                    Item xd5 = InventoryServiceNew.gI().findItemBag(player, 1399);
                                    if (xd5 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 2 && xd5 == null && player.nPoint.tiemNang >= 100000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 100000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1399));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 5:
                                    Item xd6 = InventoryServiceNew.gI().findItemBag(player, 1400);
                                    if (xd6 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 2 && xd6 == null && player.nPoint.tiemNang >= 110000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 110000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1400));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                                case 6:
                                    Item xd7 = InventoryServiceNew.gI().findItemBag(player, 1401);
                                    if (xd7 != null) {
                                        Service.gI().sendThongBao(player, "Người có rồi mà");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                        Service.gI().sendThongBao(player, "Cần trống 1 ô hành trang!");
                                        return;
                                    }
                                    if (player.gender == 2 && xd7 == null && player.nPoint.tiemNang >= 120000000000L && player.inventory.ruby >= 49999 && player.inventory.gold >= 1000000000L) {
                                        player.nPoint.tiemNang -= 120000000000L;
                                        player.inventory.ruby -= 50000;
                                        player.inventory.gold -= 1000000000L;
                                        Service.gI().point(player);
                                        Item item = ItemService.gI().createNewItem((short) (1401));
                                        item.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Học thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Người không đủ điều kiện");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc dauThan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.magicTree.openMenuTree();
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                if (player.magicTree.level == 10) {
                                    player.magicTree.fastRespawnPea();
                                } else {
                                    player.magicTree.showConfirmUpgradeMagicTree();
                                }
                            } else if (select == 2) {
                                player.magicTree.fastRespawnPea();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUpgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                            if (select == 0) {
                                player.magicTree.upgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_UPGRADE:
                            if (select == 0) {
                                player.magicTree.fastUpgradeMagicTree();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUnuppgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                            if (select == 0) {
                                player.magicTree.unupgradeMagicTree();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc calick(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final byte COUNT_CHANGE = 50;
            private int count;

            private void changeMap() {
                if (this.mapId != 102) {
                    count++;
                    if (this.count >= COUNT_CHANGE) {
                        count = 0;
                        this.map.npcs.remove(this);
                        Map map = MapService.gI().getMapForCalich();
                        this.mapId = map.mapId;
                        this.cx = Util.nextInt(100, map.mapWidth - 100);
                        this.cy = map.yPhysicInTop(this.cx, 0);
                        this.map = map;
                        this.map.npcs.add(this);
                    }
                }
            }

            @Override
            public void openBaseMenu(Player player) {
                player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    Service.gI().hideWaitDialog(player);
                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                    return;
                }
                if (this.mapId != player.zone.map.mapId) {
                    Service.gI().sendThongBao(player, "Calích đã rời khỏi map!");
                    Service.gI().hideWaitDialog(player);
                    return;
                }

                if (this.mapId == 102) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?",
                            "Kể\nChuyện", "Quay về\nQuá khứ");
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?", "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (this.mapId == 102) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            //kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                        } else if (select == 1) {
                            //về quá khứ
                            ChangeMapService.gI().changeMapBySpaceShip(player, 27, -1, 127);
                        }
                    }
                } else if (player.iDMark.isBaseMenu()) {
                    if (select == 0) {
                        //kể chuyện
                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                    } else if (select == 1) {
                        //đến tương lai
//                                    changeMap();
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_20_0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 102, -1, 167);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                }
            }
        };
    }

    public static Npc jaco(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay", "Đến \nPotaufeu");
                    } else if (this.mapId == 139) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        if (player.getSession().player.nPoint.power >= 800000000L) {

                            ChangeMapService.gI().goToPotaufeu(player);
                        } else {
                            this.npcChat(player, "Bạn chưa đủ 800tr sức mạnh để vào!");
                        }
                    } else if (this.mapId == 139) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về trạm vũ trụ
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24 + player.gender, -1, -1);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc Potage(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 149) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "tét", "Gọi nhân bản");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (select == 0) {
                        BossManager.gI().createBoss(-214);
                    }
                }
            }
        };
    }

//    public static Npc npclytieunuong54(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                this.npcChat(player, "|1|Chơi may rủi nè mấy anh ơiii");
//                createOtherMenu(player, 0, "\b|8|Trò chơi Chẵn Lẻ may rủi đang diễn ra\n\n|6|Trò chơi sẽ chọn ra chỉ duy nhất 1 người may mắn nhất giành chiến thắng để trao giải, giải thưởng sẽ là tổng thỏi vàng tất cả người chơi đặt vào trong thời gian diễn ra giải.\n\n|2|ĐẶT CHẴN : 20 Thỏi Vàng/1 Lượt\nĐẶT LẺ : 200 Thỏi Vàng/ 1 Lượt\n\n|7|Nếu bạn tin tưởng mình đang tràn đầy may mắn\nThì có thể tham gia thử, may mắn sẽ mĩm cười",
//                        "Thể lệ", "Chơi Ngay");
//            }
//
//            @Override
//            public void confirmMenu(Player pl, int select) {
//                if (canOpenNpc(pl)) {
//                    String time = ((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
//                    if (pl.iDMark.getIndexMenu() == 0) {
//                        if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && ChonAiDay.gI().baotri == false) {
//                            if (select == 0) {
//                                createOtherMenu(pl, ConstNpc.IGNORE_MENU, "|8|Thời gian giữa các giải là 5 phút\n|2|Khi hết giờ, hệ thống sẽ ngẫu nhiên chọn ra 1 người may mắn.\n|5|Lưu ý: Số thỏi vàng nhận được sẽ bị nhà cái lụm đi 5%!\n|7|Trong quá trình diễn ra khi đặt cược nếu thoát game mọi phần đặt đều sẽ bị hủy", "Ok");
//                            } else if (select == 1) {
//                                if (pl.goldNormar > 0) {
//                                    createOtherMenu(pl, 2, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT CHẴN: " + pl.goldNormar + "\n\n|5|Thời gian còn lại: " + time,
//                                            "Cập nhập", "ĐẶT CHẴN\n5TV", "ĐẶT CHẴN\n10TV", "ĐẶT CHẴN\n20TV", "ĐẶT CHẴN\n50TV", "Đóng");
//                                }
//                                if (pl.goldVIP > 0) {
//                                    createOtherMenu(pl, 3, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT LẺ: " + pl.goldVIP + "\n\n|5|Thời gian còn lại: " + time,
//                                            "Cập nhập", "ĐẶT LẺ\n5TV", "ĐẶT LẺ\n10TV", "ĐẶT LẺ\n20TV", "ĐẶT LẺ\n50TV", "Đóng");
//                                }
//                                if (pl.goldVIP == 0 && pl.goldNormar == 0) {
//                                    createOtherMenu(pl, 1, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT CHẴN: " + pl.goldNormar + "\nSố thỏi vàng ĐẶT LẺ: " + pl.goldVIP + "\n\n|5|Thời gian còn lại: " + time,
//                                            "Cập nhập", "ĐẶT CHẴN", "ĐẶT LẺ", "Đóng");
//                                }
//                            }
//                        } else {
//                            if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && ChonAiDay.gI().baotri == true) {
//                                if (select == 0) {
//                                    createOtherMenu(pl, ConstNpc.IGNORE_MENU, "|8|Thời gian giữa các giải là 5 phút\n|2|Khi hết giờ, hệ thống sẽ ngẫu nhiên chọn ra 1 người may mắn.\n|5|Lưu ý: Số thỏi vàng nhận được sẽ bị nhà cái lụm đi 5%!Trong quá trình diễn ra khi đặt cược nếu thoát game mọi phần đặt đều sẽ bị hủy", "Ok");
//                                } else if (select == 1) {
//                                    createOtherMenu(pl, 1, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT CHẴN: " + pl.goldNormar + "\nSố thỏi vàng ĐẶT LẺ: " + pl.goldVIP + "\n\n|5|Thời gian còn lại: " + time + "\n\n|7|HỆ THỐNG SẮP BẢO TRÌ",
//                                            "Cập nhập", "Đóng");
//                                }
//                            }
//                        }
//                    } else if (pl.iDMark.getIndexMenu() == 1) {
//                        if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldVIP == 0 && pl.goldNormar == 0 && ChonAiDay.gI().baotri == false) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(pl, 1, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT CHẴN: " + pl.goldNormar + "\nSố thỏi vàng ĐẶT LẺ: " + pl.goldVIP + "\n\n|5|Thời gian còn lại: " + time,
//                                            "Cập nhập", "ĐẶT CHẴN", "ĐẶT LẺ", "Đóng");
//                                    break;
//                                case 1:
//                                    createOtherMenu(pl, 2, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT CHẴN: " + pl.goldNormar + "\n\n|5|Thời gian còn lại: " + time,
//                                            "Cập nhập", "ĐẶT CHẴN\n5TV", "ĐẶT CHẴN\n10TV", "ĐẶT CHẴN\n20TV", "ĐẶT CHẴN\n50TV", "Đóng");
//                                    break;
//                                case 2:
//                                    createOtherMenu(pl, 3, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT LẺ: " + pl.goldVIP + "\n\n|5|Thời gian còn lại: " + time,
//                                            "Cập nhập", "ĐẶT LẺ\n5TV", "ĐẶT LẺ\n10TV", "ĐẶT LẺ\n20TV", "ĐẶT LẺ\n50TV", "Đóng");
//                                    break;
//                            }
//                        }
//                    } else if (pl.iDMark.getIndexMenu() == 2) {
//                        if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldNormar >= 0 && ChonAiDay.gI().baotri == false) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(pl, 2, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT CHẴN: " + pl.goldNormar + "\n\n|5|Thời gian còn lại: " + time,
//                                            "Cập nhập", "ĐẶT CHẴN\n5TV", "ĐẶT CHẴN\n10TV", "ĐẶT CHẴN\n20TV", "ĐẶT CHẴN\n50TV", "Đóng");
//                                    break;
//                                case 1:
//                                    if (pl.goldNormar >= 1000) {
//                                        Service.gI().sendThongBao(pl, "Số thỏi vàng không quá 1.000");
//                                        return;
//                                    }
//                                    if (ChonAiDay.gI().PlayersNormar.equals(pl) == false) {
//                                        ChonAiDay.gI().addPlayerNormar(pl);
//                                    }
//                                    try {
//                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 20) {
//                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 5);
//                                            InventoryServiceNew.gI().sendItemBags(pl);
//                                            pl.goldNormar += 5;
//                                            ChonAiDay.gI().goldNormar += 5;
//                                            createOtherMenu(pl, 2, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT CHẴN: " + pl.goldNormar + "\n\n|5|Thời gian còn lại: " + time,
//                                                    "Cập nhập", "ĐẶT CHẴN\n5TV", "ĐẶT CHẴN\n10TV", "ĐẶT CHẴN\n20TV", "ĐẶT CHẴN\n50TV", "Đóng");
//                                        } else {
//                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
//                                        }
//                                    } catch (Exception ex) {
//                                    }
//                                    break;
//                                case 2:
//                                    if (pl.goldNormar >= 1000) {
//                                        Service.gI().sendThongBao(pl, "Số thỏi vàng không quá 1.000");
//                                        return;
//                                    }
//                                    if (ChonAiDay.gI().PlayersNormar.equals(pl) == false) {
//                                        ChonAiDay.gI().addPlayerNormar(pl);
//                                    }
//                                    try {
//                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 10) {
//                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 10);
//                                            InventoryServiceNew.gI().sendItemBags(pl);
//                                            pl.goldNormar += 10;
//                                            ChonAiDay.gI().goldNormar += 10;
//                                            createOtherMenu(pl, 2, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT CHẴN: " + pl.goldNormar + "\n\n|5|Thời gian còn lại: " + time,
//                                                    "Cập nhập", "ĐẶT CHẴN\n5TV", "ĐẶT CHẴN\n10TV", "ĐẶT CHẴN\n20TV", "ĐẶT CHẴN\n50TV", "Đóng");
//                                        } else {
//                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
//                                        }
//                                    } catch (Exception ex) {
//                                    }
//                                    break;
//                                case 3:
//                                    if (pl.goldNormar >= 1000) {
//                                        Service.gI().sendThongBao(pl, "Số thỏi vàng không quá 1.000");
//                                        return;
//                                    }
//                                    if (ChonAiDay.gI().PlayersNormar.equals(pl) == false) {
//                                        ChonAiDay.gI().addPlayerNormar(pl);
//                                    }
//                                    try {
//                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 20) {
//                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 20);
//                                            InventoryServiceNew.gI().sendItemBags(pl);
//                                            pl.goldNormar += 20;
//                                            ChonAiDay.gI().goldNormar += 20;
//                                            createOtherMenu(pl, 2, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT CHẴN: " + pl.goldNormar + "\n\n|5|Thời gian còn lại: " + time,
//                                                    "Cập nhập", "ĐẶT CHẴN\n5TV", "ĐẶT CHẴN\n10TV", "ĐẶT CHẴN\n20TV", "ĐẶT CHẴN\n50TV", "Đóng");
//                                        } else {
//                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
//                                        }
//                                    } catch (Exception ex) {
//                                    }
//                                    break;
//                                case 4:
//                                    if (pl.goldNormar >= 1000) {
//                                        Service.gI().sendThongBao(pl, "Số thỏi vàng không quá 1.000");
//                                        return;
//                                    }
//                                    if (ChonAiDay.gI().PlayersNormar.equals(pl) == false) {
//                                        ChonAiDay.gI().addPlayerNormar(pl);
//                                    }
//                                    try {
//                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 50) {
//                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 50);
//                                            InventoryServiceNew.gI().sendItemBags(pl);
//                                            pl.goldNormar += 50;
//                                            ChonAiDay.gI().goldNormar += 50;
//                                            createOtherMenu(pl, 2, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT CHẴN: " + pl.goldNormar + "\n\n|5|Thời gian còn lại: " + time,
//                                                    "Cập nhập", "ĐẶT CHẴN\n5TV", "ĐẶT CHẴN\n10TV", "ĐẶT CHẴN\n20TV", "ĐẶT CHẴN\n50TV", "Đóng");
//                                        } else {
//                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
//                                        }
//                                    } catch (Exception ex) {
//                                    }
//                                    break;
//                            }
//                        }
//                    } else if (pl.iDMark.getIndexMenu() == 3) {
//                        if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldVIP >= 0 && ChonAiDay.gI().baotri == false) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(pl, 3, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT LẺ: " + pl.goldVIP + "\n\n|5|Thời gian còn lại: " + time,
//                                            "Cập nhập", "ĐẶT LẺ\n5TV", "ĐẶT LẺ\n10TV", "ĐẶT LẺ\n20TV", "ĐẶT LẺ\n50TV", "Đóng");
//                                    break;
//                                case 1:
//                                    if (pl.goldVIP >= 1000) {
//                                        Service.gI().sendThongBao(pl, "Số thỏi vàng không quá 1.000");
//                                        return;
//                                    }
//                                    if (ChonAiDay.gI().PlayersVIP.equals(pl) == false) {
//                                        ChonAiDay.gI().addPlayerVIP(pl);
//                                    }
//                                    try {
//                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 5) {
//                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 5);
//                                            InventoryServiceNew.gI().sendItemBags(pl);
//                                            pl.goldVIP += 5;
//                                            ChonAiDay.gI().goldVip += 5;
//                                            createOtherMenu(pl, 3, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT LẺ: " + pl.goldVIP + "\n\n|5|Thời gian còn lại: " + time,
//                                                    "Cập nhập", "ĐẶT LẺ\n5TV", "ĐẶT LẺ\n10TV", "ĐẶT LẺ\n20TV", "ĐẶT LẺ\n50TV", "Đóng");
//                                        } else {
//                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
//                                        }
//                                    } catch (Exception ex) {
//                                    }
//                                    break;
//                                case 2:
//                                    if (pl.goldVIP >= 1000) {
//                                        Service.gI().sendThongBao(pl, "Số thỏi vàng không quá 1.000");
//                                        return;
//                                    }
//                                    if (ChonAiDay.gI().PlayersVIP.equals(pl) == false) {
//                                        ChonAiDay.gI().addPlayerVIP(pl);
//                                    }
//                                    try {
//                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 10) {
//                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 10);
//                                            InventoryServiceNew.gI().sendItemBags(pl);
//                                            pl.goldVIP += 10;
//                                            ChonAiDay.gI().goldVip += 10;
//                                            createOtherMenu(pl, 3, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT LẺ: " + pl.goldVIP + "\n\n|5|Thời gian còn lại: " + time,
//                                                    "Cập nhập", "ĐẶT LẺ\n5TV", "ĐẶT LẺ\n10TV", "ĐẶT LẺ\n20TV", "ĐẶT LẺ\n50TV", "Đóng");
//                                        } else {
//                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
//                                        }
//                                    } catch (Exception ex) {
//                                    }
//                                    break;
//                                case 3:
//                                    if (pl.goldVIP >= 1000) {
//                                        Service.gI().sendThongBao(pl, "Số thỏi vàng không quá 1.000");
//                                        return;
//                                    }
//                                    if (ChonAiDay.gI().PlayersVIP.equals(pl) == false) {
//                                        ChonAiDay.gI().addPlayerVIP(pl);
//                                    }
//                                    try {
//                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 20) {
//                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 20);
//                                            InventoryServiceNew.gI().sendItemBags(pl);
//                                            pl.goldVIP += 20;
//                                            ChonAiDay.gI().goldVip += 20;
//                                            createOtherMenu(pl, 3, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT LẺ: " + pl.goldVIP + "\n\n|5|Thời gian còn lại: " + time,
//                                                    "Cập nhập", "ĐẶT LẺ\n5TV", "ĐẶT LẺ\n10TV", "ĐẶT LẺ\n20TV", "ĐẶT LẺ\n50TV", "Đóng");
//                                        } else {
//                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
//                                        }
//                                    } catch (Exception ex) {
//                                    }
//                                    break;
//                                case 4:
//                                    if (pl.goldVIP >= 1000) {
//                                        Service.gI().sendThongBao(pl, "Số thỏi vàng không quá 1.000");
//                                        return;
//                                    }
//                                    if (ChonAiDay.gI().PlayersVIP.equals(pl) == false) {
//                                        ChonAiDay.gI().addPlayerVIP(pl);
//                                    }
//                                    try {
//                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 50) {
//                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 50);
//                                            InventoryServiceNew.gI().sendItemBags(pl);
//                                            pl.goldVIP += 50;
//                                            ChonAiDay.gI().goldVip += 50;
//                                            createOtherMenu(pl, 3, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n\n|2|Số thỏi vàng ĐẶT LẺ: " + pl.goldVIP + "\n\n|5|Thời gian còn lại: " + time,
//                                                    "Cập nhập", "ĐẶT LẺ\n5TV", "ĐẶT LẺ\n10TV", "ĐẶT LẺ\n20TV", "ĐẶT LẺ\n50TV", "Đóng");
//                                        } else {
//                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
//                                        }
//                                    } catch (Exception ex) {
//                                    }
//                                    break;
//                            }
//                        } else if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && ChonAiDay.gI().baotri == true) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(pl, 1, "\n|8|- NHÀ CÁI CHẴN LẺ Kamui -\n\n|1|Tổng giải CHẴN: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải LẺ: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\n|2|Số thỏi vàng đặt chẵn: " + pl.goldNormar + "\nSố thỏi vàng đặt lẻ: " + pl.goldVIP + "\n\n|5|Thời gian còn lại: " + time + "\n\n|7|HỆ THỐNG SẮP BẢO TRÌ",
//                                            "Cập nhập", "Đóng");
//                                    break;
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }

    public static Npc thuongDe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Đến Kaio", "Quay số\nmay mắn");
                    }
                    if (this.mapId == 141) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hãy nắm lấy tay ta mau!", "Về\nthần điện");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                            "Con muốn làm gì nào?", "Quay bằng\nvàng",
                                            "Rương phụ\n("
                                            + (player.inventory.itemsBoxCrackBall.size()
                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                            "Xóa hết\ntrong rương", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                            switch (select) {
                                case 0:
                                    LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GEM);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                            + "sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 141) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapInYard(player, 45, 0, 408);
                                    Service.gI().sendThongBao(player, "Hãy xuống dưới gặp thần\nmèo Karin");
                                    player.clan.gobosscdrd = true;
                                    break;
                            }
                        }
                    }

                }
            }
        };
    }

    public static Npc thanVuTru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Di chuyển");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                            "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio", "Từ chối");

                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
//                                case 2:
//                                    if (player.clan != null) {
//                                        if (player.clan.ConDuongRanDoc != null) {
//                                            this.createOtherMenu(player, ConstNpc.MENU_OPENED_CDRD,
//                                                    "Bang hội của con đang đi con đường rắn độc cấp độ "
//                                                    + player.clan.ConDuongRanDoc.level + "\nCon có muốn đi theo không?",
//                                                    "Đồng ý", "Từ chối");
//                                        } else {
//
//                                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_CDRD,
//                                                    "Đây là Con đường rắn độc \nCác con cứ yên tâm lên đường\n"
//                                                    + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
//                                                    "Chọn\ncấp độ", "Từ chối");
//                                        }
//                                    } else {
//                                        this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
//                                    }
//                                    break;
                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_CDRD) {
                            switch (select) {
                                case 0:
                                    if (player.clan.haveGoneConDuongRanDoc) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Bang hội của ngươi đã đi con đường rắn độc lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenConDuongRanDoc, "HH:mm:ss") + " hôm nay. Người mở\n"
                                                + "(" + player.clan.playerOpenConDuongRanDoc.name + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                                        return;
//                                    } else if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
//                                        Service.gI().sendThongBao(player, "Yêu cầu tham gia bang hội trên 2 ngày!");
//                                        return;
                                    } else if (player.nPoint.power < ConDuongRanDoc.POWER_CAN_GO_TO_CDRD) {
                                        this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                + Util.numberToMoney(ConDuongRanDoc.POWER_CAN_GO_TO_CDRD));
                                        return;
                                    } else {
                                        ChangeMapService.gI().goToCDRD(player);
                                    }
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_CDRD) {
                            switch (select) {
                                case 0:
                                    if (player.clan.haveGoneConDuongRanDoc) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Bang hội của ngươi đã đi con đường rắn độc lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenConDuongRanDoc, "HH:mm:ss") + " hôm nay. Người mở\n"
                                                + "(" + player.clan.playerOpenConDuongRanDoc.name + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                                        return;
//                                    } else if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
//                                        Service.gI().sendThongBao(player, "Yêu cầu tham gia bang hội trên 2 ngày!");
//                                        return;
                                    } else if (player.nPoint.power < ConDuongRanDoc.POWER_CAN_GO_TO_CDRD) {
                                        this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                + Util.numberToMoney(ConDuongRanDoc.POWER_CAN_GO_TO_CDRD));
                                        return;
                                    } else {
                                        Input.gI().createFormChooseLevelCDRD(player);
                                    }
                                    break;
                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_CDRD) {
                            switch (select) {
                                case 0:
                                    ConDuongRanDocService.gI().openConDuongRanDoc(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                    break;
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc kibit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi?",
                                "Nhận kiếm Z", "Từ chối");
                    }
                    if (this.mapId == 114) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().player.nPoint.power <= 79999999999L) {
                                        this.npcChat(player, "Ngươi chưa đủ 80 tỷ sức mạnh để nhấc được thanh kiếm");
                                        return;
                                    }
                                    ;
                                    Item thanhkiemz = InventoryServiceNew.gI().findItemBag(player, 865);
                                    if (!(thanhkiemz == null)) {
                                        this.npcChat(player, "Ngươi đã sở hữu nó");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của ngươi không đủ chỗ trống");
                                        return;
                                    } else if (player.getSession().player.nPoint.power >= 80000000000L) {
                                        this.npcChat(player, "Ngươi chưa đủ 80 tỷ sức mạnh để nâng được thanh kiếm");
                                        return;
                                    } else {
                                        Item VatPham = ItemService.gI().createNewItem((short) (865));
                                        VatPham.itemOptions.add(new Item.ItemOption(50, 20));
                                        VatPham.itemOptions.add(new Item.ItemOption(77, 10));
                                        VatPham.itemOptions.add(new Item.ItemOption(103, 10));
                                        VatPham.itemOptions.add(new Item.ItemOption(14, 5));
                                        VatPham.itemOptions.add(new Item.ItemOption(93, 7));
                                        InventoryServiceNew.gI().addItemBag(player, VatPham);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Nhận thành công");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc osin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                    } else if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về\nThánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
                    } else if (this.mapId == 155) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else if (this.mapId == 52) {
                        try {
                            MapMaBu.gI().setTimeJoinMapMaBu();
                            if (this.mapId == 52) {
                                long now = System.currentTimeMillis();
                                if (now > MapMaBu.TIME_OPEN_MABU && now < MapMaBu.TIME_CLOSE_MABU) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB, "Đại chiến Ma Bư đã mở, "
                                            + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }

                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu osin");
                        }

                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX) {
                            this.createOtherMenu(player, ConstNpc.GO_UPSTAIRS_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Lên Tầng!", "Quay về", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Quay về", "Từ chối");
                        }
                    } else if (this.mapId == 120) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 383, 240);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 154, -1, 776, 312);
                                    break;
                            }
                        }
                    } else if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 1:
                                    if (player.getSession().player.nPoint.power <= 60000000000L) {
                                        this.npcChat(player, "Người chưa đủ 60 tỷ sức mạnh để đến đây");
                                        return;
                                    }
                                    ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                    break;
                                case 2:
                                    if (player.getSession().player.nPoint.power <= 120000000000L) {
                                        this.npcChat(player, "Người chưa đủ 120 tỷ sức mạnh để đến đây");
                                        return;
                                    }
                                    ;
                                    ChangeMapService.gI().changeMap(player, 174, -1, 177, 144);
                                    break;
                            }
                        }
                    } else if (this.mapId == 155) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 776, 312);
                            }
                        }
                    } else if (this.mapId == 52) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                                break;
                            case ConstNpc.MENU_OPEN_MMB:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                } else if (select == 1) {
//                                    if (!player.getSession().actived) {
//                                        Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//                                    } else
                                    ChangeMapService.gI().changeMap(player, 114, -1, 318, 336);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                }
                                break;
                        }
                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.GO_UPSTAIRS_MENU) {
                            if (select == 0) {
                                player.fightMabu.clear();
                                ChangeMapService.gI().changeMap(player, this.map.mapIdNextMabu((short) this.mapId), -1, this.cx, this.cy);
                            } else if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        } else {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    } else if (this.mapId == 120) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.getMembers().size() < DoanhTrai.N_PLAYER_CLAN) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai != null) {
                        createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                                "Bang hội của ngươi đang đánh trại độc nhãn\n"
                                + "Thời gian còn lại là "
                                + TimeUtil.getMinLeft(player.clan.timeOpenDoanhTrai, DoanhTrai.TIME_DOANH_TRAI / 1000)
                                + ". Ngươi có muốn tham gia không?",
                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    int nPlSameClan = 0;
                    for (Player pl : player.zone.getPlayers()) {
                        if (!pl.equals(player) && pl.clan != null
                                && pl.clan.equals(player.clan) && pl.location.x >= 1285
                                && pl.location.x <= 1645) {
                            nPlSameClan++;
                        }
                    }
                    if (nPlSameClan < DoanhTrai.N_PLAYER_MAP) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ngươi phải có ít nhất " + DoanhTrai.N_PLAYER_MAP + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                                + "Hahaha.", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Doanh trại chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (player.clan.haveGoneDoanhTrai) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội của ngươi đã đi trại lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenDoanhTrai, "HH:mm:ss") + " hôm nay. Người mở\n"
                                + "(" + player.clan.playerOpenDoanhTrai + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                            "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                            + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                            "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().opendoanhtrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc quaTrung(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_AP_TRUNG_NHANH = 1000000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == (21 + player.gender)) {
                        player.mabuEgg.sendMabuEgg();
                        if (player.mabuEgg.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Bư bư bư...",
                                    "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Bư bư bư...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
                        }
                    }
                    if (this.mapId == 7) {
                        player.billEgg.sendBillEgg();
                        if (player.billEgg.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Bư bư bư...",
                                    "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Bư bư bư...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == (21 + player.gender)) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_EGG:
                                if (select == 0) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                } else if (select == 1) {
                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                        player.mabuEgg.timeDone = 0;
                                        Service.gI().sendMoney(player);
                                        player.mabuEgg.sendMabuEgg();
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold)) + " vàng");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                                "Bạn có chắc chắn cho trứng nở?\n"
                                                + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                                                "Đệ mabư\nTrái Đất", "Đệ mabư\nNamếc", "Đệ mabư\nXayda", "Từ chối");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                                "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        break;
                                    case 1:
                                        player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                        break;
                                    case 2:
                                        player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_EGG:
                                if (select == 0) {
                                    player.mabuEgg.destroyEgg();
                                }
                                break;
                        }
                    }
                    /*  if (this.mapId == 7) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_BILL:
                                if (select == 0) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                } else if (select == 1) {
                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                        player.billEgg.timeDone = 0;
                                        Service.gI().sendMoney(player);
                                        player.billEgg.sendBillEgg();
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold)) + " vàng");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_BILL,
                                                "Bạn có chắc chắn cho trứng nở?\n"
                                                + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                                                "Đệ mabư\nTrái Đất", "Đệ mabư\nNamếc", "Đệ mabư\nXayda", "Từ chối");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                                "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_BILL:
                                switch (select) {
                                    case 0:
                                        player.billEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        break;
                                    case 1:
                                        player.billEgg.openEgg(ConstPlayer.NAMEC);
                                        break;
                                    case 2:
                                        player.billEgg.openEgg(ConstPlayer.XAYDA);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_BILL:
                                if (select == 0) {
                                    player.billEgg.destroyEgg();
                                }
                                break;
                        }
                    }
                     */
                }
            }
        };
    }

    public static Npc duahau(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final int COST_AP_TRUNG_NHANH = 1000000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7 * player.gender) {
                        player.billEgg.sendBillEgg();
                        //if (player.billEgg == null)return;
                        if (player.billEgg.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Mang Đến Gặp Vua Hùng Để Được Những Món Quà Vô Giá...",
                                    "Thu Hoạch\nSớm " + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Mau thu hoạch nào...", "Thu Hoạch", "Đóng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7 * player.gender) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_BILL:
                                if (select == 0) {
                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                        player.billEgg.timeDone = 0;
                                        Service.gI().sendMoney(player);
                                        player.billEgg.sendBillEgg();
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold)) + " vàng");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_BILL,
                                                "ôi bạn ơi?\n"
                                                + "Chọn Một Trong Những Món Quà Giá Trị Nào",
                                                "Dưa\nHấu", "Ngọc\nBội", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_BILL:
                                switch (select) {
                                    case 0:
                                        ItemService.gI().openBoxdua(player);
                                        player.billEgg.destroyEgg();
                                        break;
                                    case 1://2072
                                        // ItemService.gI().openBoxngocboi(player);
                                        player.billEgg.destroyEgg();
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_BILL:
                                if (select == 0) {
                                    player.billEgg.destroyEgg();
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc quocVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                        "Bản thân", "Đệ tử", "Từ chối");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                            + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                            "Nâng\ngiới hạn\nsức mạnh",
                                            "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Sức mạnh của con đã đạt tới giới hạn",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Sức mạnh của đệ con đã đạt tới giới hạn",
                                                "Đóng");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                                }
                                //giới hạn đệ tử
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                        switch (select) {
                            case 0:
                                OpenPowerService.gI().openPowerBasic(player);
                                break;
                            case 1:
                                if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
                                        player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                        Service.gI().sendMoney(player);
                                    }
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ vàng để mở, còn thiếu "
                                            + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                        if (select == 0) {
                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                    Service.gI().sendMoney(player);
                                }
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Bạn không đủ vàng để mở, còn thiếu "
                                        + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaTL(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng", "Đóng");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_FUTURE", true);
                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc rongOmega(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    NgocRongSaoDen.gI().setTime();
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        try {
                            long now = System.currentTimeMillis();
                            if (now > NgocRongSaoDen.TIME_OPEN && now < NgocRongSaoDen.TIME_CLOSE) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW, "Đường đến với ngọc rồng sao đen đã mở, "
                                        + "ngươi có muốn tham gia không?",
                                        "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                            } else {
                                String[] optionRewards = new String[7];
                                int index = 0;
                                for (int i = 0; i < 7; i++) {
                                    if (player.rewardBlackBall.timeOutOfDateReward[i] > System.currentTimeMillis()) {
                                        String quantily = player.rewardBlackBall.quantilyBlackBall[i] > 1 ? "x" + player.rewardBlackBall.quantilyBlackBall[i] + " " : "";
                                        optionRewards[index] = quantily + (i + 1) + " sao";
                                        index++;
                                    }
                                }
                                if (index != 0) {
                                    String[] options = new String[index + 1];
                                    for (int i = 0; i < index; i++) {
                                        options[i] = optionRewards[i];
                                    }
                                    options[options.length - 1] = "Từ chối";
                                    this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW, "Ngươi có một vài phần thưởng ngọc "
                                            + "rồng sao đen đây!",
                                            options);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }
                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu rồng Omega");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_REWARD_BDW:
                            player.rewardBlackBall.getRewardSelect((byte) select);
                            break;
                        case ConstNpc.MENU_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            } else if (select == 1) {
                                player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                ChangeMapService.gI().openChangeMapTab(player);
                            }
                            break;
                        case ConstNpc.MENU_NOT_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            }
                            break;
                    }
                }
            }

        };
    }

    public static Npc rong1_to_7s(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isHoldBlackBall()) {
                        this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?", "Phù hộ", "Từ chối");
                    } else {
                        if (BossManager.gI().existBossOnPlayer(player)
                                || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                                || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối", "Gọi BOSS");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                    "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                    "x3 HP\n" + Util.numberToMoney(NgocRongSaoDen.COST_X3) + " vàng",
                                    "x5 HP\n" + Util.numberToMoney(NgocRongSaoDen.COST_X5) + " vàng",
                                    "x7 HP\n" + Util.numberToMoney(NgocRongSaoDen.COST_X7) + " vàng",
                                    "Từ chối"
                            );
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                        } else if (select == 2) {
                            BossManager.gI().callBoss(player, mapId);
                        } else if (select == 1) {
                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                        if (player.effectSkin.xHPKI > 1) {
                            Service.gI().sendThongBao(player, "Bạn đã được phù hộ rồi!");
                            return;
                        }
                        switch (select) {
                            case 0:
                                NgocRongSaoDen.gI().xHPKI(player, NgocRongSaoDen.X3);
                                break;
                            case 1:
                                NgocRongSaoDen.gI().xHPKI(player, NgocRongSaoDen.X5);
                                break;
                            case 2:
                                NgocRongSaoDen.gI().xHPKI(player, NgocRongSaoDen.X7);
                                break;
                            case 3:
                                this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc npcThienSu64(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 7) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 0) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 146) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 147) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 148) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 48) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!",
                            "Hướng Dẫn",
                            "Từ Chối");
                }
                if (this.mapId == 5) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!",
                            "Chế Tạo trang bị thiên sứ", "Shop\nnguyên liệu", "Từ Chối");
                }
            }

            //if (player.inventory.gold < 500000000) {
//                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
//                return;
//            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 7) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 146, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 14) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 148, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 0) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 146, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 147) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 148) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 14, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 146) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 450);
                        }
                        if (select == 1) {
                        }

                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 48) {
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOI_SKH_VIP);
                        }
                        /*if (select == 1) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIP);
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        } */
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 5) {
                        switch (select) {
                            case 0:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_BI_TS);
                                break;
                            case 1: //shop thoi vang
                             
                                    ShopServiceNew.gI().opendShop(player, "SHOP_TS", false);
                                    return;
                                
                             
                                
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }
                    }
                }
            }

        };
    }

    public static Npc bill(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Mang x999 Thức Ăn Đến Đây Ta Sẽ Đổi Đá Hủy Diệt Cho Ngươi\n"
                            + "Sau Đó Dùng Đá Đó Để Mua Trang Bị Hủy Diệt Từ Ta!!",
                            "Đổi Phiều\nHủy Diệt",
                            "Shop\nBerus", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.DOI_DIEM);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "HUY_DIET", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.BILL) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.DOI_DIEM:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc boMong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|2|Cậu muốn làm nhiệm vụ giúp tôi sao!", "Nhiệm vụ\nhàng ngày", "Thành Tích", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.playerTask.sideTask.template != null) {
                                        String npcSay = "Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName() + " ("
                                                + player.playerTask.sideTask.getLevel() + ")"
                                                + "\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/"
                                                + player.playerTask.sideTask.maxCount + " ("
                                                + player.playerTask.sideTask.getPercentProcess() + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK;
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                                npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                                "Tôi có vài nhiệm vụ theo cấp bậc, "
                                                + "sức cậu có thể làm được cái nào?",
                                                "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
                                    }
                                    break;
                                case 1:
                                    ThanhTichPlayer.SendThanhTich(player);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    TaskService.gI().changeSideTask(player, (byte) select);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                    TaskService.gI().paySideTask(player);
                                    break;
                                case 1:
                                    TaskService.gI().removeSideTask(player);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    /*public static Npc karin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "|1|Cửa Hàng Của Ta Sưu Tầm Nhưng Đồ Vip Độc Lạ"
                                    + "\n|2|Ngươi Đã Có Ngọc Víp Để Đổi Chưa"
                                    + "\n|3|Cách Kiếm Ngọc"
                                    + "\n|3|Ngọc Rồng Namec"
                                    + "\n|4|Sự Kiện Hè"
                                    + "\n|4|Nhiệm Vụ Hàng Ngày"
                                    + "\n|5|Đổi Hồng Ngọc "
                                    + "\n|6|Có Rồi Hả? Giỏi,Hãy Mang Cho Ta nào", "Cửa Hàng", "Đóng");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "KARIN", true);
                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }*/
    public static Npc vados(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                    if (canOpenNpc(player)) {
                        if (this.mapId == 5) {
                            if (player.topnap > 0 || player.topnv > 0 || player.topsm > 0) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "|2|Bạn đang sở hữu Top, hãy nhận thưởng ngay!!!", "Nhận Ngay");
                            } else {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "|2|Người Muốn Xem Tóp Gì?",
                                        "Top\nSức Mạnh", "Top\nHP", "Top\nKI", "Top\nNhiệm Vụ", "Top\nĐại Gia", "Top\nHồng Ngọc", "Top\nNHS", "Top\nPVP", "Top\nNV");
                            }
                        } else {
                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|2|Người muốn gì ở ta?", "Đóng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (player.topnap > 0 || player.topnv > 0 || player.topsm > 0) {
                                UseItem.gI().TOP(player);
                            } else {
                                if (select == 0) {
                                    Service.gI().showListTop(player, Manager.topSM);
                                    //Service.getInstance().sendThongBaoOK(player, TopService.getTopSM());
                                    break;
                                }
                                /*  if (select == 1) {
                                Service.gI().showListTop(player, Manager.topSD);
                             //   Service.getInstance().sendThongBaoOK(player, TopService.getTopSD());
                                break;
                            }*/
                                if (select == 1) {
                                    Service.gI().showListTop(player, Manager.topHP);
                                    break;
                                }
                                if (select == 2) {
                                    Service.gI().showListTop(player, Manager.topKI);
                                    break;
                                }
                                if (select == 3) {
                                    Service.gI().showListTop(player, Manager.topNV);
                                    //    Service.getInstance().sendThongBaoOK(player, TopService.getTopNV());
                                    break;
                                }
                                if (select == 4) {
                                    Service.gI().showListTop(player, Manager.topVND);
                                    //  Service.getInstance().sendThongBaoFromAdmin(player, TopService.getTopNap());
                                    break;
                                }
                                if (select == 5) {
                                    Service.gI().showListTop(player, Manager.topHN);
                                    //   Service.getInstance().sendThongBaoOK(player, TopService.getTopHONGNGOC());
                                    break;
                                }
                                if (select == 6) {
                                    Service.gI().showListTop(player, Manager.topNHS);
                                    //  Service.getInstance().sendThongBaoFromAdmin(player, TopService.getTopNap());
                                    break;
                                }
                                if (select == 7) {
                                    Service.gI().showListTop(player, Manager.topPVP);
                                    //  Service.getInstance().sendThongBaoFromAdmin(player, TopService.getTopNap());
                                    break;
                                }
                                
                                 if (select == 8) {
                                    Service.gI().showListTop(player, Manager.topSD);
                                    //  Service.getInstance().sendThongBaoFromAdmin(player, TopService.getTopNap());
                                    break;
                                }
                                break;
                            }
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 80) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Tới hành tinh\nYardart", "Từ chối");
                    } else if (this.mapId == 131) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (this.mapId == 80) {
                                if (select == 0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 131, -1, 870);
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc mavuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, tôi có thể giúp gì cho cậu?", "Đến\nTây thánh địa", "Từ chối");
                    } else if (this.mapId == 156) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    } else if (this.mapId == 157) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi đủ sức đến Bắc thánh địa sao?", "Đến ngay", "Quay về\nTây thánh địa");
                    } else if (this.mapId == 158) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi đủ sức đến Nam thánh địa sao?", "Đến ngay", "Quay về\nĐông thánh địa");
                    } else if (this.mapId == 159) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về\nNam thánh địa");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                if (player.nPoint.power > 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 360);
                                }else{
                                    Service.gI().sendThongBao(player, "Bạn Phải Đạt 80 Tỷ");
                                }
                            }
                        }
                    } else if (this.mapId == 156) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về đông karin
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 6, -1, 1011);
                                    break;
                            }
                        }
                    } else if (this.mapId == 157) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 158, -1, 303);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 821);
                                    break;
                            }
                        }
                    } else if (this.mapId == 158) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 159, -1, 206);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 157, -1, 210);
                                    break;
                            }
                        }
                    } else if (this.mapId == 159) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 158, -1, 303);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private boolean rating;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Hãy cố gắng tập luyện" + "\n" + "Thu thập 9.999 bí kiếp để đổi Võ phục Yardrat nhé",
                            "Nhận\nthưởng", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 133 || this.mapId == 0) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Item biKiep = null;
                                    Item caitrangyardrat = ItemService.gI().createNewItem((short) (player.gender == 0 ? 592 : player.gender == 1 ? 593 : 594));
                                    try {
                                        biKiep = InventoryServiceNew.gI().findItemBag(player, 590);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (biKiep == null || biKiep.quantity < 9999) {
                                        this.npcChat(player, "Bạn không đủ bí kiếp hãy luyện tập thêm");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                        return;
                                    } else {
                                        caitrangyardrat.itemOptions.add(new Item.ItemOption(47, 700));// giáp +700
                                        caitrangyardrat.itemOptions.add(new Item.ItemOption(108, 20));// né đòn 20%
                                        caitrangyardrat.itemOptions.add(new Item.ItemOption(33, 1));// dich chuyen tuc thoi
                                        InventoryServiceNew.gI().addItemBag(player, caitrangyardrat);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, biKiep, 9999);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được Võ phục của người Yardrat");
                                    }
                                    break;
                                }
                                case 1:
                                    this.npcChat(player, "Hãy cố gắng luyện tập nhé!");
                                    break;
                            }
                        }
                    }
                }
            }
        };

    }

//    public static Npc gokuSSJ_2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 133) {
//                        try {
//                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
//                        if (biKiep != null) {
//                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + biKiep.quantity + " bí kiếp.\n"
//                                    + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart", "Học dịch\nchuyển", "Đóng");
//                        }
//
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//
//                    }
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 133) {
//                        if (player.iDMark.isBaseMenu()) {
//                            try {
//                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
//                        if (biKiep != null) {
//                            if (biKiep.quantity >= 10000 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
//                                yardart.itemOptions.add(new Item.ItemOption(47, 400));
//                                yardart.itemOptions.add(new Item.ItemOption(108, 10));
//                                InventoryServiceNew.gI().addItemBag(player, yardart);
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, biKiep, 10000);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.gI().sendThongBao(player, "Bạn vừa nhận được trang phục tộc Yardart");
//                            }
//                        }
//                    } catch (Exception ex) {
//                    }
//                        }
//                    }
//                }
//            }
//        };
//    }
//        public static Npc gokuSSJ_2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    try {
//                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
//                        if (biKiep != null) {
//                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + biKiep.quantity + " bí kiếp.\n"
//                                    + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart", "Học dịch\nchuyển", "Đóng");
//                        }
//
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    try {
//                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
//                        if (biKiep != null) {
//                            if (biKiep.quantity >= 10000 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
//                                yardart.itemOptions.add(new Item.ItemOption(47, 400));
//                                yardart.itemOptions.add(new Item.ItemOption(108, 10));
//                                InventoryServiceNew.gI().addItemBag(player, yardart);
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, biKiep, 10000);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.gI().sendThongBao(player, "Bạn vừa nhận được trang phục tộc Yardart");
//                            }
//                        }
//                    } catch (Exception ex) {
//                    }
//                }
//            }
//        };
//    }
    public static Npc khidaumoi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Bạn muốn nâng cấp khỉ ư?", "Nâng cấp\nkhỉ", "Shop của Khỉ", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 14) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 1,
                                            "|7|Cần Khỉ Lv1 hoặc 2,4,6 để nâng cấp lên lv8\b|2|Mỗi lần nâng cấp tiếp thì mỗi cấp cần thêm 10 đá ngũ sắc",
                                            "Khỉ\ncấp 2",
                                            "Khỉ\ncấp 4",
                                            "Khỉ\ncấp 6",
                                            "Khỉ\ncấp 8",
                                            "Từ chối");
                                    break;
                                case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "KHI", false);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 1) { // action đổi dồ húy diệt
                            switch (select) {
                                case 0: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                    Item klv1 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1137);
                                    int soLuong = 0;
                                    if (dns != null) {
                                        soLuong = dns.quantity;
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1137 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 1137 + i) && soLuong >= 20) {
                                            CombineServiceNew.gI().khilv2(player, 1138 + i);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 20);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                            this.npcChat(player, "Upgrede Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần cái trang khỉ cấp 1 với 20 đá ngũ sắc");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 1: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                    Item klv2 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1138);
                                    int soLuong = 0;
                                    if (dns != null) {
                                        soLuong = dns.quantity;
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1138 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 1138 + i) && soLuong >= 30) {
                                            CombineServiceNew.gI().khilv3(player, 1139 + i);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 30);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                            this.npcChat(player, "Upgrede Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần cái trang khỉ cấp 2 với 30 đá ngũ sắc");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 2: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                    Item klv2 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1139);
                                    int soLuong = 0;
                                    if (dns != null) {
                                        soLuong = dns.quantity;
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1139 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 1139 + i) && soLuong >= 40) {
                                            CombineServiceNew.gI().khilv4(player, 1140 + i);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 40);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                            this.npcChat(player, "Upgrede Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần cái trang khỉ cấp 3 với 40 đá ngũ sắc");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 3: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                    Item klv2 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1140);
                                    int soLuong = 0;
                                    if (dns != null) {
                                        soLuong = dns.quantity;
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1140 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 1140 + i) && soLuong >= 50) {
                                            CombineServiceNew.gI().khilv5(player, 1136 + i);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 50);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                            this.npcChat(player, "Upgrede Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần cái trang khỉ cấp 3 với 50 đá ngũ sắc");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;

                                case 5: // canel
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc unkonw(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    long now = System.currentTimeMillis();
                    if (this.mapId == 5) {
                        if (now > MapMaBu.TIME_OPEN_PVP && now < MapMaBu.TIME_CLOSE_PVP) {
                            this.createOtherMenu(player, 0,
                                    "Bạn muốn gì nào?\n|7|MAP ĐẤU TRƯỜNG PVP MỞ CỬA VÀO 20-22H MỖI NGÀY\n"
                                    + "|6|Để đến được võ đài bạn cần phải làm đến nhiệm vụ 25"
                                    + "\n|2|Bạn đang có : " + player.pointPvp + " điểm Đấu Trường", "Đến\nVõ Đài\nSiêu Cấp", "Đổi\nCải trang", "Top PvP");
                        } else {
                            this.createOtherMenu(player, 0,
                                    "Bạn muốn gì nào?\n|7|MAP ĐẤU TRƯỜNG PVP MỞ CỬA VÀO 20-22H MỖI NGÀY\n"
                                    + "|6|Để đến được võ đài bạn cần phải làm đến nhiệm vụ 25"
                                    + "\n|2|Bạn đang có : " + player.pointPvp + " điểm Đấu Trường", "Đến\nVõ Đài\nSiêu Cấp", "Đổi\nCải trang", "Top PvP");
                        }
                    }
                    if (this.mapId == 196) {
                        this.createOtherMenu(player, 0,
                                "Bạn muốn gì nào?\nBạn đang có : " + player.pointPvp + " điểm Đấu Trường", "Về\nĐảo Kame");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        long now = System.currentTimeMillis();
                        if (player.iDMark.getIndexMenu() == 0) {
                            switch (select) {
                                case 0:
                                    if (player.playerTask.taskMain.id >= 25) {
                                        if (now > MapMaBu.TIME_OPEN_PVP && now < MapMaBu.TIME_CLOSE_PVP) {
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 196, -1, 168);
                                            Service.getInstance().changeFlag(player, Util.nextInt(8));
                                            break; // đến võ đài siêu cấp
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Chưa đến thời gian mở map");
                                            break;
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Thực lực của bạn chưa đủ để đến đây");
                                        break; // đến võ đài siêu cấp
                                    }
                                case 1:  // 
                                    this.createOtherMenu(player, 1,
                                            "Bạn có muốn đổi 500 điểm PvP lấy \n|6|Cải trang Gohan Văn Phòng với tất cả chỉ số là 30%\n ", "Đổi", "Đóng");
                                    // bat menu doi item
                                    break;

                                case 2:
                                    Service.gI().showListTop(player, Manager.topPVP);
                                    break;

                            }
                        }

                        if (player.iDMark.getIndexMenu() == 1) { // action doi item
                            switch (select) {
                                case 0: // trade
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        if (player.pointPvp >= 500) {
                                            player.pointPvp -= 500;
                                            Item item = ItemService.gI().createNewItem((short) (1369));
                                            item.itemOptions.add(new Item.ItemOption(50, 30));
                                            item.itemOptions.add(new Item.ItemOption(77, 30));
                                            item.itemOptions.add(new Item.ItemOption(103, 30));
                                            item.itemOptions.add(new Item.ItemOption(207, 0));
                                            item.itemOptions.add(new Item.ItemOption(33, 0));
//                                      
                                            InventoryServiceNew.gI().addItemBag(player, item);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Đổi thành công, hãy thoát ra vào lại để cảm nhận!");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ điểm bạn còn " + (500 - player.pointPvp) + " Điểm nữa");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Hàng trang đã đầy");
                                    }
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 196) {
                        switch (select) {
                            case 0: // quay ve
                                ChangeMapService.gI().changeMapInYard(player, 5, -1, 354);
                                Service.getInstance().changeFlag(player, Util.nextInt(1));
                                break;
                        }
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                    switch (select) {
                        case 0:
                            LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
                            break;
                        case 1:
                            ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                            break;
                        case 2:
                            NpcService.gI().createMenuConMeo(player,
                                    ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                    "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                    + "sẽ không thể khôi phục!",
                                    "Đồng ý", "Hủy bỏ");
                            break;
                    }
                }
            }
        };
    }

    public static Npc toribot(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BANKING, "|7|- •⊹٭DragonBall Kamui٭⊹• -\n"
                                + "|2|Thông Tin Tổng\b" + "Chào bạn : " + player.name + " | ID: (" + player.id + ") | " + "Map : " + player.zone.map.mapName + "\n"
                                + "Số dư : " + Util.format(player.getSession().vnd) + " VNĐ"
                                + "\nTrạng thái : " + (player.getSession().actived == 1 ? " Đã mở thành viên" : " Chưa mở thành viên")
                                + (player.vip == 1 ? " [VIP]" : player.vip == 2 ? " [VIP2]" : player.vip == 3 ? " [VIP3]" : player.vip == 4 ? " [SVIP]" : ""),
                                "QUY\nĐỔI", "MUA\nĐỆ TỬ", "ĐỔI SKILL\nĐỆ TỬ");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BANKING) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.QUY_DOI, 12639, "|7|DragonBall Kamui" + "\n"
                                            + "ĐỔI TIỀN\n"
                                            + "|2|Chào bạn : " + player.name
                                            + "\nSố Dư : " + Util.format(player.getSession().vnd) + " VNĐ "
                                            + "\n|7|Tỉ Lệ Quy Đổi Hiện Tại Là : Thỏi Vàng X" + Manager.TLDOITV + ", Hồng Ngọc X" + Util.format(Manager.TLDOIHN) + ", Xu BẠC X" + Util.format(Manager.TLDOIXUBAC) + ", Xu VÀNG X" + Util.format(Manager.TLDOIXUVANG) + ", Thăng Tinh Thạch X" + Util.format(Manager.TLDOITHANGTINHTHACH)
                                            + "\n|5|(Giới Hạn Quy Đồi Là 1000 tức không quá 1 triệu vnđ)"
                                            + "\n" + "Ví Dụ : Nhập vào 1 = 1.000VNĐ = " + Manager.TLDOITV + " Thỏi Vàng "
                                            + "\n" + "Ví Dụ : Nhập vào 1 = 1.000VNĐ = " + Util.format(Manager.TLDOIHN * 1000) + " Hồng Ngọc"
                                            + "\n" + "Ví Dụ : Nhập vào 1 = 1.000VNĐ = " + Util.format(Manager.TLDOIXUVANG) + " XU Vàng"
                                            + "\n" + "Ví Dụ : Nhập vào 1 = 1.000VNĐ = " + Util.format(Manager.TLDOIXUBAC) + " XU Bạc"
                                            + "\n" + "Ví Dụ : Nhập vào 1 = 1.000VNĐ = " + Util.format(Manager.TLDOITHANGTINHTHACH) + " Thanh Tinh Thạch",
                                            "ĐỔI\nTHỎI VÀNG", "ĐỔI\nHỒNG NGỌC", "Đổi\n Xu Vàng", "Đổi\n Xu Bạc","Đổi\nThăng\nTinhThach");
                                    break;
                                case 1:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.MUA_DE, 12639, "|7|DragonBall Kamui" + "\n"
                                            + "MUA ĐỆ TỬ\n"
                                            + "|2|Chào bạn : " + player.name
                                            + "\nSố Dư : " + Util.format(player.getSession().vnd) + " VNĐ\n"
                                            + "|7|Mua đệ tử bất kì sẽ auto kích hoạt thành viên\n"
                                            + "Bạn có chăc muốn mua đệ tử không?\n",
                                            "CUMBER SSJ2\n100.000VNĐ", "GOKU SSJ4\n150.000VNĐ", "CADIC SSJ4\n150.000VNĐ");
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.DOI_SKILL, 12639, "|7|DragonBall Kamui" + "\n"
                                            + "ĐỔI SKILL ĐỆ TỬ\n"
                                            + "|2|Chào bạn : " + player.name
                                            + "\nSố Dư : " + Util.format(player.getSession().vnd) + " VNĐ\n"
                                            + "|7|Bạn muốn đổi skill nào?",
                                            "SKILL 2,3\n10.000VNĐ", "SKILL 3,4\n20.000VNĐ");
                                    break;
                                /*case 3:
                                if (player.playerTask.taskMain.id < 21) {
                                    TaskService.gI().getTaskMainById(player, player.playerTask.taskMain.id +=1);
                                    TaskService.gI().sendNextTaskMain(player);
                                    this.npcChat(player, "Làm nhiệm vụ mới thôi nào!");
                                } else {
                                    this.npcChat(player, "Con không còn là tân thủ nữa");
                                }
                                break; */
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc GhiDanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            String[] menuselect = new String[]{};

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 52) {
                        createOtherMenu(pl, 0, DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).Giai(pl), "Thông tin\nChi tiết", DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).CanReg(pl) ? "Đăng ký" : "OK", "Đại Hội\nVõ Thuật\nLần thứ\n23");
                    } else if (this.mapId == 129) {
                        int goldchallenge = pl.goldChallenge;
                        int goldchallenge1 = pl.gemChallenge;
                        if (pl.levelWoodChest == 0) {
                            menuselect = new String[]{"Hướng\ndẫn\nthêm", "Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Thi đấu\n" + Util.numberToMoney(goldchallenge1) + " Ngọc", "Về\nĐại Hội\nVõ Thuật"};
                        } else {
                            menuselect = new String[]{"Hướng\ndẫn\nthêm", "Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Thi đấu\n" + Util.numberToMoney(goldchallenge1) + " Ngọc", "Nhận thưởng\nRương cấp\n" + pl.levelWoodChest, "Về\nĐại Hội\nVõ Thuật"};
                        }
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm, ngày nghỉ, ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào", menuselect, "Từ chối");
                    } else {
                        super.openBaseMenu(pl);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 52) {
                        switch (select) {
                            case 0:
                                Service.gI().sendPopUpMultiLine(player, tempId, avartar, DaiHoiVoThuat.gI().Info());
                                break;
                            case 1:
                                if (DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).CanReg(player)) {
                                    DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).Reg(player);
                                }
                                break;
                            //case 2:
                            //    ChangeMapService.gI().changeMapNonSpaceship(player, 113, player.location.x, 360);
                            //    break;
                            case 2:
                                ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                break;
                        }
                    } else if (this.mapId == 129) {
                        int goldchallenge = player.goldChallenge;
                        int goldchallenge1 = player.gemChallenge;
                        if (player.levelWoodChest == 0) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.NPC_DHVT23);
                                    break;
                                case 1:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 50000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 2:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.ruby >= goldchallenge1) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.ruby -= (goldchallenge1);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.gemChallenge += 10000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 3:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.NPC_DHVT23);
                                    break;
                                case 1:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 50000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 2:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.ruby >= goldchallenge1) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.ruby -= (goldchallenge1);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.gemChallenge += 10000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 3:
                                    if (!player.receivedWoodChest) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item it = ItemService.gI().createNewItem((short) 570);
                                            it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
                                            it.itemOptions.add(new Item.ItemOption(30, 0));
                                            it.createTime = System.currentTimeMillis();
                                            InventoryServiceNew.gI().addItemBag(player, it);
                                            InventoryServiceNew.gI().sendItemBags(player);

                                            player.receivedWoodChest = true;
                                            player.levelWoodChest = 0;
                                            Service.getInstance().sendThongBao(player, "Bạn nhận được rương gỗ");
                                        } else {
                                            this.npcChat(player, "Hành trang đã đầy");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                    }
                                    break;
                                case 4:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc monaito(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7) {
                        this.createOtherMenu(player, 0,
                                "Chào bạn tôi sẽ đưa bạn đến hành tinh Cereal?", "Đồng ý", "Từ chối");
                    }
                    if (this.mapId == 170) {
                        this.createOtherMenu(player, 0,
                                "Ta ở đây để đưa con về", "Về Làng Mori", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 170, -1, 264);
                                    break; // den hanh tinh cereal
                            }
                        }
                    }
                    if (this.mapId == 170) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 432);
                                    break; // quay ve

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc minuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player) && this.mapId == 46) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        String message;

                        if (player.clan != null && player.clan.ConDuongRanDoc != null && player.clan.gobosscdrd) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hãy cầm lấy hai hạt đậu cuối cùng của ta đây\nCố giữ mình nhé " + player.name, "Cám ơn\nsư phụ");
                            Service.gI().sendThongBao(player, "Hãy mau bay xuống\nchân tháp Karin");
                        } else {
                            message = "Con hãy bay theo cây Gậy Như Ý trên đỉnh tháp để đến Thần Điện gặp Thượng Đế\nCon rất xứng đáng để làm đệ tự của ông ấy";
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động", "Tập luyện với Yajirô", "Tập luyện với thần mèo");
                        }

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player) && this.mapId == 46) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            if (player.clan != null && player.clan.ConDuongRanDoc != null && player.clan.gobosscdrd) {
                                player.nPoint.setFullHpMp();
                                //                           ChangeMapService.gI().changeMapInYard(player, 144, 0, 131);
                            }
                        }
                    }
                }

            }
        };
    }

    public static Npc buhan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 170 || this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|6|Ta sẽ giúp ngươi thức tỉnh chỉ số của ngươi và đệ tử",
                                "Thức Tỉnh");
                    } else if (this.mapId == 128) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi sợ rồi sao?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 170 || this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.THUC_TINH_DT);
                                    break;
                                case 1:
                                    this.npcChat(player, "Chức năng đang phát triển");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.THUC_TINH_DT:
                                    switch (select) {
                                        case 0:
                                            CombineServiceNew.gI().thuctinhDT(player, 1);
                                            break;
                                        case 1:
                                            CombineServiceNew.gI().thuctinhDT(player, 10);
                                            break;
                                        case 2:
                                            CombineServiceNew.gI().thuctinhDT(player, 100);
                                            break;
                                    }
                                    break;
                            }
                        }
                    } else if (this.mapId == 128) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về trạm vũ trụ
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1225);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc popo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.getSession().is_gift_box) {
//                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp gì nào?", "Giải tán bang hội", "Nhận quà\nđền bù");
                    } else {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Thượng Đế vừa phát hiện 1 loại khí đang âm thầm\nhủy diệt mọi mầm sống trên Trái Đất,\nnó được gọi là Destron Gas.\nTa sẽ đưa các cậu đến nơi ấy, các cậu sẵn sàng chưa?", "Thông tin chi tiết", "Top 100 bang hội", "OK", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                return;
                            case 1:
                                return;
                            case 2:
                                if (player.clan != null) {
                                    if (player.clan.KhiGaHuyDiet != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_KGHD,
                                                "Bang hội của con đang đi khí ga hủy diệt cấp độ "
                                                + player.clan.KhiGaHuyDiet.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {

                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_KGHD,
                                                "Đây là khí ga hủy diệt \nCác con cứ yên tâm lên đường\n"
                                                + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                            case 3:
                                return;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_KGHD) {
                        switch (select) {
                            case 0:
                                if ( player.isAdmin2() || player.nPoint.power >= KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD) {
                                    ChangeMapService.gI().goToKhiGas(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_KGHD) {
                        switch (select) {
                            case 0:
                                if ( player.isAdmin2() || player.nPoint.power >= KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD) {
                                    Input.gI().createFormChooseLevelKGHD(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_KGHD) {
                        switch (select) {
                            case 0:
                                KhiGasHuyDietService.gI().openKhiGaHuyDiet(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                }
            }
        ;
    }

    ;
    }

    public static Npc thodaika(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                this.npcChat(player, "|2|Chơi tài xỉu nè mấy anh ơiii");
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "\n|8|- TÀI XỈU XIĐA - \n\n|2|Đưa cho ta thỏi vàng và ngươi sẽ mua đc nhà cửa, ôtô\n|5|Đây không phải chẵn lẻ tài xỉu đâu=)))",
                            "TÀI", "XỈU");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().actived == 0) {
                                        Service.gI().sendThongBao(player, "Mở thành viên mới chơi được");
                                        break;
                                    }
                                    Input.gI().TAI(player);
                                    break;
                                case 1:
                                    if (player.getSession().actived == 0) {
                                        Service.gI().sendThongBao(player, "Mở thành viên mới chơi được");
                                        break;
                                    }
                                    Input.gI().XIU(player);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
        int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
        try {
            switch (tempId) {
                case ConstNpc.KAMUI:
                    return kamui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.UNKOWN:
                    return unkonw(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TORIBOT:
                    return toribot(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GHI_DANH:
                    return GhiDanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POTAGE:
                    return poTaGe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUY_LAO_KAME:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POPO:
                    return popo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BuHan:
                    return buhan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUONG_LAO_GURU:
                    return truongLaoGuru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VUA_VEGETA:
                    return vuaVegeta(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    return ongGohan_ongMoori_ongParagus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA:
                    return bulmaQK(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUA_HAU:
                    return duahau(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DENDE:
                    return dende(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.APPULE:
                    return appule(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DR_DRIEF:
                    return drDrief(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CARGO:
                    return cargo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUI:
                    return cui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SANTA:
                    return santa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.URON:
                    return uron(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT:
                    return baHatMit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NOI_BANH:
                    return noibanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RUONG_DO:
                    return ruongDo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAU_THAN:
                    return dauThan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CALICK:
                    return calick(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.JACO:
                    return jaco(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUONG_DE:
                    return thuongDe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUA_HANG_KY_GUI:
                    return kyGui(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.Granola:
//                    return granala(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GIUMA_DAU_BO:
                    return mavuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MONAITO:
                    return monaito(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VADOS:
                    return vados(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KHI_DAU_MOI:
                    return khidaumoi(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_VU_TRU:
                    return thanVuTru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KIBIT:
                    return kibit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OSIN:
                    return osin(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.LY_TIEU_NUONG:
//                    return npclytieunuong54(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.BULMABIP:
//                    return bulmabip(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LINH_CANH:
                    return linhCanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUA_TRUNG:
                    return quaTrung(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUOC_VUONG:
                    return quocVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA_TL:
                    return bulmaTL(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_OMEGA:
                    return rongOmega(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    return rong1_to_7s(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPC_64:
                    return npcThienSu64(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.tuongred:
                    return tuongred(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.hungvuong:
//                    return hungvuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MI_NUONG:
                    return minuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BILL:
                    return bill(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BO_MONG:
                    return boMong(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.MI_NUONGSK:
//                    return minuongevent(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ:
                    return gokuSSJ_1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ_2:
                    return gokuSSJ_2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUONG_TANG:
                    return duongtank(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.chubedan:
                    return chubedan(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.MAYGAP:
//                    return maygap(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.pilap:
                    return pilap(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.WHIS:
                    return whis56(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THO_REN:
                    return thoren(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THO_DAI_CA:
                    return thodaika(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NGO_KHONG:
                    return wukong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRONG_TAI:
                    return trongTai(mapId, status, cx, cy, tempId, avatar);
                default:
                    return new Npc(mapId, status, cx, cy, tempId, avatar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
//                                ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0, player.gender);
                            }
                        }
                    };

            }
        } catch (Exception e) {
            Logger.logException(NpcFactory.class,
                    e, "Lỗi load npc");
            return null;
        }
    }

    //girlbeo-mark
    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1 && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2 && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc;
        npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                OUTER:
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:
                        break;
                    case ConstNpc.MAKE_MATCH_PVP: {
                        if (Maintenance.isRuning) {
                            break;
                        }
                        PVPService.gI().sendInvitePVP(player, (byte) select);
                        break;
                    }
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPService.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGONTRB://TRB
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TRB);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRONTRB:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TRB);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenronTRB(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        switch (select) {
                            case 0:
                                IntrinsicService.gI().sattd(player);
                                break;
                            case 1:
                                IntrinsicService.gI().satnm(player);
                                break;
                            case 2:
                                IntrinsicService.gI().setxd(player);
                                break;
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2000:
                    case ConstNpc.MENU_OPTION_USE_ITEM2001:
                    case ConstNpc.MENU_OPTION_USE_ITEM2002:
                    case ConstNpc.MENU_OPTION_USE_ITEM2003:
                    case ConstNpc.MENU_OPTION_USE_ITEM2004:
                    case ConstNpc.MENU_OPTION_USE_ITEM2005:
                    case ConstNpc.MENU_OPTION_USE_ITEM2006:
                        try {
                        ItemService.gI().OpenSetTongHop(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                        try {
                        ItemService.gI().OpenSetTuyChon(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1498:
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, 0, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
                                break;
                            case 1:
                                NpcService.gI().createMenuConMeo(player, 1, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
                                break;
                            case 2:
                                NpcService.gI().createMenuConMeo(player, 2, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
                                break;
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1499:
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, 3, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
                                break;
                            case 1:
                                NpcService.gI().createMenuConMeo(player, 4, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
                                break;
                            case 2:
                                NpcService.gI().createMenuConMeo(player, 5, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
                                break;
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1500:
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, 6, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
                                break;
                            case 1:
                                NpcService.gI().createMenuConMeo(player, 7, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
                                break;
                            case 2:
                                NpcService.gI().createMenuConMeo(player, 8, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
                                break;
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1501:
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, 9, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
                                break;
                            case 1:
                                NpcService.gI().createMenuConMeo(player, 10, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
                                break;
                            case 2:
                                NpcService.gI().createMenuConMeo(player, 11, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
                                break;
                        }
                        break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            if (((Player) PLAYERID_OBJECT.get(player.id)).isAdmin2()) {
                                Service.gI().sendThongBaoOK((Player) PLAYERID_OBJECT.get(player.id), "Thằng [" + player.name + "] đang muốn ban mày");
                                Service.gI().sendThongBao(player, "Không thể khóa tài khoản Admin");
                                break;
                            }
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player, "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;
                    case ConstNpc.ACTIVE:
                        if (select == 0) {
                            PlayerService.gI().mtvPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player, "Active Player : " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;
                    case ConstNpc.JAIL:
                        try {
                        if (select == 0) {
                            if (((Player) PLAYERID_OBJECT.get(player.id)).getSession().isJail == false) {
                                PlayerService.gI().jail((Player) PLAYERID_OBJECT.get(player.id));
                                ChangeMapService.gI().changeMap(((Player) PLAYERID_OBJECT.get(player.id)), 49, 0, 620, 320);
                                Service.gI().sendThongBaoOK(player, "Giam người chơi : " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                                Thread.sleep(3000);
                                Service.gI().sendThongBaoFromAdmin(((Player) PLAYERID_OBJECT.get(player.id)), "Bạn Đã Bị Giam Bởi " + player.name);
                                ServerNotify.gI().sendThongBaoBenDuoi("người chơi : " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " đã vào tù vì phạm tội");
                            } else {
                                PlayerService.gI().jail((Player) PLAYERID_OBJECT.get(player.id));
                                ChangeMapService.gI().changeMap(((Player) PLAYERID_OBJECT.get(player.id)), 5, -1, 1040, 169);
                                Service.gI().sendThongBaoOK(player, "Mở giam người chơi : " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                                Thread.sleep(3000);
                                Service.gI().sendThongBaoFromAdmin(((Player) PLAYERID_OBJECT.get(player.id)), "Bạn Đã Được Mở Giam Bởi " + player.name);
                                ServerNotify.gI().sendThongBaoBenDuoi("Người chơi : (" + ((Player) PLAYERID_OBJECT.get(player.id)).name + ") đã được mãn hạn tù");
                            }
                        }
                    } catch (Exception e) {
                    }
                    break;
                    case ConstNpc.NEXTNV:
                        switch (select) {
                            case 0:
                                Input.gI().nextnhiemvu(player);
                                break;
                            case 1:
                                if (((Player) PLAYERID_OBJECT.get(player.id)).playerTask.taskMain.id > 33) {
                                    Service.gI().sendThongBao(player, "NHIỆM VỤ QUÁ GIỚI HẠN");
                                    break;
                                }
                                if (((Player) PLAYERID_OBJECT.get(player.id)).playerTask.taskMain.id < 33) {
                                    int nvp = ((Player) PLAYERID_OBJECT.get(player.id)).playerTask.taskMain.id;
                                    TaskService.gI().sendNextTaskMain(((Player) PLAYERID_OBJECT.get(player.id)));
                                    Service.gI().sendThongBaoOK(((Player) PLAYERID_OBJECT.get(player.id)), "Bạn vừa được next nhiệm vụ bởi ADMIN");
                                    Service.gI().sendThongBaoOK(player, "Next NV player [" + ((Player) PLAYERID_OBJECT.get(player.id)).name + "] đến " + nvp + " thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Player không online");
                                }
                                break;
                            case 2:
                                if (((Player) PLAYERID_OBJECT.get(player.id)).playerTask.taskMain.id > 33) {
                                    Service.gI().sendThongBao(player, "NHIỆM VỤ QUÁ GIỚI HẠN");
                                    break;
                                }
                                if (((Player) PLAYERID_OBJECT.get(player.id)).playerTask.taskMain.id < 33) {
                                    String nvp = ((Player) PLAYERID_OBJECT.get(player.id)).playerTask.taskMain.subTasks.get(((Player) PLAYERID_OBJECT.get(player.id)).playerTask.taskMain.index).name;
                                    TaskService.gI().addDoneSubTask(((Player) PLAYERID_OBJECT.get(player.id)), ((Player) PLAYERID_OBJECT.get(player.id)).playerTask.taskMain.subTasks.get(((Player) PLAYERID_OBJECT.get(player.id)).playerTask.taskMain.index).maxCount);
                                    Service.gI().sendThongBaoOK(player, "Next NV player [" + ((Player) PLAYERID_OBJECT.get(player.id)).name + "] đến " + nvp + " thành công");
                                } else {
                                    Service.gI().sendThongBao(player, "Player không online");
                                }
                                break;
                        }
                        break;
                    case ConstNpc.MAKEADMIN:
                        if (select == 0) {
                            PlayerService.gI().makeAdmin((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player, "Cấp Quyền Admin " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        if (select == 1) {
                            PlayerService.gI().takeAdmin((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player, "Hủy Quyền Admin " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;
                    case ConstNpc.MODE_PET:
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.TYPE_PET, 12639, "|7|Ngọc Rồng Kamui Service" + "\n"
                                        + "\n" + "|1|[ CHANGE PET FOR PLAYER ]\n" + "Đổi Pet Giữ Lại Đồ Và Skill\n"
                                        + "Nếu player chưa có đệ, chọn Pet Mabu sẽ auto tạo đệ thường\n"
                                        + "|7|Chọn Pet Muốn Đổi Cho Player : " + ((Player) PLAYERID_OBJECT.get(player.id)).name + "?",
                                        "PET\nMABU", "PET\nGOHAN BUU", "PET\nEVIL BUU", "PET\nCUMBER V2", "PET\nGOKU V4", "PET\nVEGETA V4");
                                break;
                            case 1:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.BUFF_PET, 12639, "|7|Ngọc Rồng Kamui Service" + "\n"
                                        + "\n" + "|1|[ CHANGE GENDER PET FOR PLAYER ]\n" + "Đổi Gender Pet\n"
                                        + "|7|Chọn Hành Tinh Pet Muốn Đổi Cho Player : " + ((Player) PLAYERID_OBJECT.get(player.id)).name + "?",
                                        "GENDER\nTD", "GENDER\nNM", "GENDER\nXD");
                                break;
                            case 2:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.SKILL_PET, 12639, "|7|Ngọc Rồng Kamui Service" + "\n"
                                        + "\n" + "|1|[ CHANGE SKILL PET FOR PLAYER ]\n" + "Đổi Skill Pet\n"
                                        + "|7|Chọn Skill Pet Muốn Đổi Cho Player : " + ((Player) PLAYERID_OBJECT.get(player.id)).name + "?",
                                        "SKILL 2", "SKILL 3", "SKILL 4", "SKILL 5");
                                break;
                            case 3:
                                Input.gI().buffcspet(player);
                                break;
                            case 4:
                                Input.gI().subcspet(player);
                                break;
                        }
                        break;
                    case ConstNpc.BUFFDHIEU:
                        Player nhandhieu = (Player) PLAYERID_OBJECT.get(player.id);
                        switch (select) {
                            case 0:
                                if (nhandhieu.timedh1 == 0) {
                                    nhandhieu.timedh1 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7);
                                    nhandhieu.usedh1 = true;
                                    Service.gI().sendTitle(nhandhieu, "DH1");
                                    Service.gI().sendThongBao(player, "Đã " + (nhandhieu.usedh1 == true ? "Buff" : "Tắt") + " Danh Hiệu!");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Player đã có DHIEU");
                                }
                                break;
                            case 1:
                                if (nhandhieu.timedh2 == 0) {
                                    nhandhieu.timedh2 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7);
                                    nhandhieu.usedh2 = true;
                                    Service.gI().sendTitle(nhandhieu, "DH2");
                                    Service.gI().sendThongBao(player, "Đã " + (nhandhieu.usedh2 == true ? "Buff" : "Tắt") + " Danh Hiệu!");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Player đã có DHIEU");
                                }
                                break;
                            case 2:
                                if (nhandhieu.timedh3 == 0) {
                                    nhandhieu.timedh3 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7);
                                    nhandhieu.usedh3 = true;
                                    Service.gI().sendTitle(nhandhieu, "DH3");
                                    Service.gI().sendThongBao(player, "Đã " + (nhandhieu.usedh3 == true ? "Buff" : "Tắt") + " Danh Hiệu!");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Player đã có DHIEU");
                                }
                                break;
                            case 3:
                                if (nhandhieu.dhtang1 == false) {
                                    Service.gI().sendTitle(nhandhieu, "VECHAI");
                                    nhandhieu.titlett = true;
                                    nhandhieu.dhtang1 = true;
                                    Service.gI().sendThongBao(player, "Đã " + (nhandhieu.titlett == true ? "Buff" : "Tắt") + " Danh Hiệu!");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Player đã có DHIEU");
                                }
                                break;
                            case 4:
                                if (nhandhieu.dhtang2 == false) {
                                    Service.gI().sendTitle(nhandhieu, "FANCUNG");
                                    nhandhieu.titlett = true;
                                    nhandhieu.dhtang2 = true;
                                    Service.gI().sendThongBao(player, "Đã " + (nhandhieu.titlett == true ? "Buff" : "Tắt") + " Danh Hiệu!");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Player đã có DHIEU");
                                }
                                break;
                            case 5:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.BUFFDHIEU, 12639,
                                        "|7|- •⊹٭DragonBall Kamui٭⊹• -\n|2|TẶNG DANH HIỆU\nPlayer name : " + nhandhieu.name
                                        + "\n|5|Đại Thần : " + (nhandhieu.timedh1 > 0 ? Util.msToTime(player.timedh1) : nhandhieu.timedh1 <= 0 ? "Chưa Sở Hữu" : nhandhieu.usedh1 == true ? "Đang Bật" : "Đang Tắt")
                                        + "\nTrùm Cuối : " + (nhandhieu.timedh2 > 0 ? Util.msToTime(player.timedh2) : nhandhieu.timedh2 <= 0 ? "Chưa Sở Hữu" : nhandhieu.usedh2 == true ? "Đang Bật" : "Đang Tắt")
                                        + "\nBất Bại : " + (nhandhieu.timedh3 > 0 ? Util.msToTime(player.timedh3) : nhandhieu.timedh3 <= 0 ? "Chưa Sở Hữu" : nhandhieu.usedh3 == true ? "Đang Bật" : "Đang Tắt")
                                        + "\nBuôn Ve Chai : " + (nhandhieu.dhtang1 == true ? "Đang Bật" : nhandhieu.titlett == false ? "Chưa Sở Hữu" : "Đang Tắt")
                                        + "\nFan Cứng : " + (nhandhieu.dhtang2 == true ? "Đang Bật" : nhandhieu.titlett == false ? "Chưa Sở Hữu" : "Đang Tắt"),
                                        "Đại Thần", "Trùm Cuối", "Bất Bại", "Ve Chai", "Fan Cứng", "Làm Mới");
                                break;
                        }
                        break;
                    case ConstNpc.HOISINH:
                        if (select == 0) {
                            if (player.inventory.ruby >= 1000) {
                                player.inventory.ruby -= 1000;
                                Service.gI().sendMoney(player);
                                Service.gI().hsChar(player, player.nPoint.hpMax, player.nPoint.mpMax);
                                player.achievement.plusCount(13);
                            } else {
                                Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc");
                            }
                        }
                        break;
                    case ConstNpc.TYPE_PET:
                        Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                        if (select == 0) {
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl, (int) pl.gender, pl.nPoint.limitPower);
                                Service.gI().sendThongBaoOK(player, "Đã tạo đệ thường cho Player " + pl.name);
                                break;
                            }
                            pl.pet.name = "$" + "Mabu";
                            pl.pet.typePet = 1;
                            pl.pet.nPoint.power = 1500000;
                            pl.pet.nPoint.tiemNang = 0;
                            pl.pet.nPoint.hpg = Util.nextInt(100, 200) * 3;
                            pl.pet.nPoint.mpg = Util.nextInt(100, 200) * 3;
                            pl.pet.nPoint.dameg = Util.nextInt(20, 45);
                            pl.pet.nPoint.defg = Util.nextInt(5, 90);
                            pl.pet.nPoint.critg = Util.nextInt(0, 4);
                            for (int i = 0; i < 4; i++) {
                                pl.pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                            }
                            pl.pet.nPoint.setFullHpMp();
                            Service.gI().Send_Caitrang(pl.pet);
                            ChangeMapService.gI().exitMap(pl.pet);
                            Service.gI().sendThongBaoOK(pl, "Change Pet Mabu by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi đệ Mabu cho " + pl.name + " thành công");
                        }
                        if (select == 1) {
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl.name + " chưa có đệ");
                                break;
                            }
                            pl.pet.name = "$" + "Gohan Buu";
                            pl.pet.typePet = 2;
                            pl.pet.nPoint.power = 1500000;
                            pl.pet.nPoint.tiemNang = 0;
                            pl.pet.nPoint.hpg = Util.nextInt(100, 200) * 5;
                            pl.pet.nPoint.mpg = Util.nextInt(100, 200) * 5;
                            pl.pet.nPoint.dameg = Util.nextInt(20, 45);
                            pl.pet.nPoint.defg = Util.nextInt(5, 90);
                            pl.pet.nPoint.critg = Util.nextInt(0, 4);
                            for (int i = 0; i < 4; i++) {
                                pl.pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                            }
                            pl.pet.nPoint.setFullHpMp();
                            Service.gI().Send_Caitrang(pl.pet);
                            ChangeMapService.gI().exitMap(pl.pet);
                            Service.gI().sendThongBaoOK(pl, "Change Pet Buu Han by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi đệ BuuHan cho " + pl.name + " thành công");
                        }
                        if (select == 2) {
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl.name + " chưa có đệ");
                                break;
                            }
                            pl.pet.name = "$" + "Evil Buu";
                            pl.pet.typePet = 3;
                            pl.pet.nPoint.power = 1500000;
                            pl.pet.nPoint.tiemNang = 0;
                            pl.pet.nPoint.hpg = Util.nextInt(100, 200) * 10;
                            pl.pet.nPoint.mpg = Util.nextInt(100, 200) * 10;
                            pl.pet.nPoint.dameg = Util.nextInt(100, 300);
                            pl.pet.nPoint.defg = Util.nextInt(5, 90);
                            pl.pet.nPoint.critg = Util.nextInt(0, 5);
                            for (int i = 0; i < 4; i++) {
                                pl.pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                            }
                            pl.pet.nPoint.setFullHpMp();
                            Service.gI().Send_Caitrang(pl.pet);
                            ChangeMapService.gI().exitMap(pl.pet);
                            Service.gI().sendThongBaoOK(pl, "Change Pet Evil Buu by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi đệ Evil Buu cho " + pl.name + " thành công");
                        }
                        if (select == 3) {
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl.name + " chưa có đệ");
                                break;
                            }
                            pl.pet.name = "$" + "Cumber SSJ2";
                            pl.pet.typePet = 4;
                            pl.pet.nPoint.power = 1500000;
                            pl.pet.nPoint.tiemNang = 0;
                            pl.pet.nPoint.hpg = Util.nextInt(80, 200) * 20;
                            pl.pet.nPoint.mpg = Util.nextInt(80, 200) * 20;
                            pl.pet.nPoint.dameg = Util.nextInt(80, 500);
                            pl.pet.nPoint.defg = Util.nextInt(100, 200);
                            pl.pet.nPoint.critg = Util.nextInt(0, 6);
                            for (int i = 0; i < 4; i++) {
                                pl.pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                            }
                            pl.pet.nPoint.setFullHpMp();
                            Service.gI().Send_Caitrang(pl.pet);
                            ChangeMapService.gI().exitMap(pl.pet);
                            Service.gI().sendThongBaoOK(pl, "Change Pet Cumber by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi đệ Cumber cho " + pl.name + " thành công");
                        }
                        if (select == 4) {
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl.name + " chưa có đệ");
                                break;
                            }
                            pl.pet.name = "$" + "Kakarot SSJ4";
                            pl.pet.typePet = 5;
                            pl.pet.nPoint.power = 1500000;
                            pl.pet.nPoint.tiemNang = 0;
                            pl.pet.nPoint.hpg = Util.nextInt(100, 200) * 20;
                            pl.pet.nPoint.mpg = Util.nextInt(100, 200) * 20;
                            pl.pet.nPoint.dameg = Util.nextInt(200, 500);
                            pl.pet.nPoint.defg = Util.nextInt(100, 200);
                            pl.pet.nPoint.critg = Util.nextInt(0, 6);
                            for (int i = 0; i < 4; i++) {
                                pl.pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                            }
                            pl.pet.nPoint.setFullHpMp();
                            Service.gI().Send_Caitrang(pl.pet);
                            ChangeMapService.gI().exitMap(pl.pet);
                            Service.gI().sendThongBaoOK(pl, "Change Pet Goku SSJ4 by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi đệ Goku SSJ4 cho " + pl.name + " thành công");
                        }
                        if (select == 5) {
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl.name + " chưa có đệ");
                                break;
                            }
                            pl.pet.name = "$" + "Vegeta SSJ4";
                            pl.pet.typePet = 6;
                            pl.pet.nPoint.power = 1500000;
                            pl.pet.nPoint.tiemNang = 0;
                            pl.pet.nPoint.hpg = Util.nextInt(100, 200) * 20;
                            pl.pet.nPoint.mpg = Util.nextInt(100, 200) * 20;
                            pl.pet.nPoint.dameg = Util.nextInt(200, 500);
                            pl.pet.nPoint.defg = Util.nextInt(100, 200);
                            pl.pet.nPoint.critg = Util.nextInt(0, 6);
                            for (int i = 0; i < 4; i++) {
                                pl.pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                            }
                            pl.pet.nPoint.setFullHpMp();
                            Service.gI().Send_Caitrang(pl.pet);
                            ChangeMapService.gI().exitMap(pl.pet);
                            Service.gI().sendThongBaoOK(pl, "Change Pet Vegeta SSJ4 by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi đệ Vegeta SSJ4 cho " + pl.name + " thành công");
                        }
                        break;

                    case ConstNpc.BUFF_PET:
                        Player pl1 = (Player) PLAYERID_OBJECT.get(player.id);
                        if (select == 0) {
                            if (pl1.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl1.name + " chưa có đệ");
                            }
                            pl1.pet.gender = 0;
                            ChangeMapService.gI().exitMap(pl1.pet);
                            Service.gI().sendThongBaoOK(pl1, "Change Gender Pet Trái Đất by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi hành tinh đệ TD cho " + pl1.name + " thành công");
                        }
                        if (select == 1) {
                            if (pl1.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " chưa có đệ");
                            }
                            pl1.pet.gender = 1;
                            ChangeMapService.gI().exitMap(pl1.pet);
                            Service.gI().sendThongBaoOK(pl1, "Change Gender Pet Namek by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đổi hành tinh đệ NM cho " + pl1.name + " thành công");
                        }
                        if (select == 2) {
                            if (pl1.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl1.name + " chưa có đệ");
                            }
                            pl1.pet.gender = 2;
                            ChangeMapService.gI().exitMap(pl1.pet);
                            Service.gI().sendThongBaoOK(pl1, "Change Gemder Pet Xayda by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Đồi hành tinh đệ XD cho " + pl1.name + " thành công");
                        }
                        break;

                    case ConstNpc.TANG_PET:
                        Player phatde = ((Player) PLAYERID_OBJECT.get(player.id));
                        int gender = ((Player) PLAYERID_OBJECT.get(player.id)).gender;
                        if (select == 0) {
                            PetService.gI().createMabuPet(phatde, gender);
                            Service.gI().sendThongBaoOK(phatde, "Buff Pet Mabu by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Buff đệ Mabu cho " + phatde.name + " thành công");
                            break;
                        }
                        if (select == 1) {
                            PetService.gI().createPicPet(phatde, gender);
                            Service.gI().sendThongBaoOK(phatde, "Buff Pet Kefla by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Buff đệ Kefla cho " + phatde.name + " thành công");
                            break;
                        }
                        if (select == 2) {
                            PetService.gI().createCumberPet(phatde, gender);
                            Service.gI().sendThongBaoOK(phatde, "Buff Pet Cumber by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Buff đệ Cumber cho " + phatde.name + " thành công");
                            break;
                        }
                        if (select == 3) {
                            PetService.gI().createGokuPet(phatde, gender);
                            Service.gI().sendThongBaoOK(phatde, "Buff Pet Goku by Admin [" + player.name + "]");
                            Service.gI().sendThongBaoOK(player, "Buff đệ Goku cho " + phatde.name + " thành công");
                            break;
                        }
                        break;

                    case ConstNpc.SKILL_PET:
                        Player pl2 = (Player) PLAYERID_OBJECT.get(player.id);
                        if (select == 0) {
                            if (pl2.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl2.name + " chưa có đệ");
                                return;
                            }
                            NpcService.gI().createMenuConMeo(player, ConstNpc.SKILL2_PET, 12639, "|7|Ngọc Rồng Kamui Service" + "\n"
                                    + "\n" + "|7|[ CHANGE SKILL 2 PET FOR PLAYER ]\n" + "|1|Đổi Skill Pet\n"
                                    + "Bạn có muốn Đổi Skill 2 Đệ Tử cho : " + ((Player) PLAYERID_OBJECT.get(player.id)).name + "?",
                                    "SKILL\nKAMEJOKO", "SKILL\nAUTOMIC", "SKILL\nMASENKO");
                        }
                        if (select == 1) {
                            if (pl2.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl2.name + " chưa có đệ");
                                return;
                            }
                            NpcService.gI().createMenuConMeo(player, ConstNpc.SKILL3_PET, 12639, "|7|Ngọc Rồng Kamui Service" + "\n"
                                    + "\n" + "|7|[ CHANGE SKILL 3 PET FOR PLAYER ]\n" + "|1|Đổi Skill Pet\n"
                                    + "Bạn có muốn Đổi Skill 3 Đệ Tử cho : " + ((Player) PLAYERID_OBJECT.get(player.id)).name + "?",
                                    "SKILL\nTDHS", "SKILL\nTTNL", "SKILL\nKAIOKEN");
                        }
                        if (select == 2) {
                            if (pl2.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl2.name + " chưa có đệ");
                                return;
                            }
                            NpcService.gI().createMenuConMeo(player, ConstNpc.SKILL4_PET, 12639, "|7|Ngọc Rồng Kamui Service" + "\n"
                                    + "\n" + "|7|[ CHANGE SKILL 4 PET FOR PLAYER ]\n" + "|1|Đổi Skill Pet\n"
                                    + "Bạn có muốn Đổi Skill 4 Đệ Tử cho : " + ((Player) PLAYERID_OBJECT.get(player.id)).name + "?",
                                    "SKILL\nHÓA KHỈ", "SKILL\nKHIÊN", "SKILL\nĐẺ TRỨNG");
                        }
                        if (select == 3) {
                            if (pl2.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + pl2.name + " chưa có đệ");
                                return;
                            }
                            NpcService.gI().createMenuConMeo(player, ConstNpc.SKILL5_PET, 12639, "|7|Ngọc Rồng Kamui Service" + "\n"
                                    + "\n" + "|7|[ CHANGE SKILL 5 PET FOR PLAYER ]\n" + "|1|Đổi Skill Pet\n"
                                    + "Bạn có muốn Đổi Skill 4 Đệ Tử cho : " + ((Player) PLAYERID_OBJECT.get(player.id)).name + "?",
                                    "SKILL\nDCTT", "SKILL\nTRÓI", "SKILL\nTỰ SÁT");
                        }
                        break;
                    case ConstNpc.SKILL2_PET:
                        Player plsk2 = (Player) PLAYERID_OBJECT.get(player.id);
                        if (select == 0) {
                            if (plsk2.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk2.name + " chưa có đệ");
                            }
                            plsk2.pet.playerSkill.skills.set(1, SkillUtil.createSkill(Skill.KAMEJOKO, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 2 Kamejoko cho Player : " + plsk2.name);
                        }
                        if (select == 1) {
                            if (plsk2.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk2.name + " chưa có đệ");
                            }
                            plsk2.pet.playerSkill.skills.set(1, SkillUtil.createSkill(Skill.ANTOMIC, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 2 Automic cho Player : " + plsk2.name);
                        }
                        if (select == 2) {
                            if (plsk2.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk2.name + " chưa có đệ");
                            }
                            plsk2.pet.playerSkill.skills.set(1, SkillUtil.createSkill(Skill.MASENKO, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 2 Masenko cho Player : " + plsk2.name);
                        }
                        break;
                    case ConstNpc.SKILL3_PET:
                        Player plsk3 = (Player) PLAYERID_OBJECT.get(player.id);
                        if (select == 0) {
                            if (plsk3.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk3.name + " chưa có đệ");
                            }
                            plsk3.pet.playerSkill.skills.set(2, SkillUtil.createSkill(Skill.THAI_DUONG_HA_SAN, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 3 TDHS cho Player : " + plsk3.name);
                        }
                        if (select == 1) {
                            if (plsk3.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk3.name + " chưa có đệ");
                            }
                            plsk3.pet.playerSkill.skills.set(2, SkillUtil.createSkill(Skill.TAI_TAO_NANG_LUONG, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 3 TTNL cho Player : " + plsk3.name);
                        }
                        if (select == 2) {
                            if (plsk3.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk3.name + " chưa có đệ");
                            }
                            plsk3.pet.playerSkill.skills.set(2, SkillUtil.createSkill(Skill.KAIOKEN, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 3 KOK cho Player : " + plsk3.name);
                        }
                        break;
                    case ConstNpc.SKILL4_PET:
                        Player plsk4 = (Player) PLAYERID_OBJECT.get(player.id);
                        if (select == 0) {
                            if (plsk4.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk4.name + " chưa có đệ");
                            }
                            plsk4.pet.playerSkill.skills.set(3, SkillUtil.createSkill(Skill.BIEN_KHI, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 4 Biến Khỉ cho Player : " + plsk4.name);
                        }
                        if (select == 1) {
                            if (plsk4.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk4.name + " chưa có đệ");
                            }
                            plsk4.pet.playerSkill.skills.set(3, SkillUtil.createSkill(Skill.KHIEN_NANG_LUONG, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 4 Khiên Năng Lượng cho Player : " + plsk4.name);
                        }
                        if (select == 2) {
                            if (plsk4.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk4.name + " chưa có đệ");
                            }
                            plsk4.pet.playerSkill.skills.set(3, SkillUtil.createSkill(Skill.DE_TRUNG, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 4 Đẻ Trứng cho Player : " + plsk4.name);
                        }
                        if (select == 3) {
                            if (plsk4.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk4.name + " chưa có đệ");
                            }
                            plsk4.pet.playerSkill.skills.set(3, SkillUtil.createSkill(Skill.TU_SAT, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 4 Tự Sát cho Player : " + plsk4.name);
                        }
                        break;
                    case ConstNpc.SKILL5_PET:
                        Player plsk5 = (Player) PLAYERID_OBJECT.get(player.id);
                        if (select == 0) {
                            if (plsk5.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk5.name + " chưa có đệ");
                            }
                            plsk5.pet.playerSkill.skills.set(4, SkillUtil.createSkill(Skill.DICH_CHUYEN_TUC_THOI, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 5 DCTT cho Player : " + plsk5.name);
                        }
                        if (select == 1) {
                            if (plsk5.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk5.name + " chưa có đệ");
                            }
                            plsk5.pet.playerSkill.skills.set(4, SkillUtil.createSkill(Skill.TROI, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 5 Trói cho Player : " + plsk5.name);
                        }
                        if (select == 2) {
                            if (plsk5.pet == null) {
                                Service.gI().sendThongBao(player, "Player " + plsk5.name + " chưa có đệ");
                            }
                            plsk5.pet.playerSkill.skills.set(4, SkillUtil.createSkill(Skill.TU_SAT, 1));
                            Service.gI().sendThongBaoFromAdmin(player, "|7|Đã Mở Skill 5 Tự Sát cho Player : " + plsk5.name);
                        }
                        break;

                    case ConstNpc.UP_TOP_ITEM:
                        if (player.inventory.ruby < 50) {
                            Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc");
                            break;
                        }
                        player.inventory.ruby -= 50;
                        Service.gI().sendMoney(player);
                        Service.gI().sendThongBao(player, "Up top thành công");
                        break;
                    case ConstNpc.MENU_PLAYER:
                        switch (select) {
                            case 0:
                                Input.gI().createFormGiftCode(player);
                                break;
                            case 1:
                                Input.gI().createFormChangePassword(player);
                                break;
                            case 2:
                                Input.gI().slvang(player);
                                break;
                            case 3:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.CHISO_NHANH, 12639,
                                        "|7|AUTO CỘNG CHỈ SỐ NHANH"
                                        + "\n\n|2| Bạn muốn cộng nhanh chỉ số nào?",
                                        "HP\n" + (player.autoHP == true ? "[ BẬT ]" : "[ TẮT ]"),
                                        "KI\n" + (player.autoKI == true ? "[ BẬT ]" : "[ TẮT ]"),
                                        "SD\n" + (player.autoSD == true ? "[ BẬT ]" : "[ TẮT ]"),
                                        "Giáp\n" + (player.autoGiap == true ? "[ BẬT ]" : "[ TẮT ]"));
                                break;
                        }
                        break;

                    case ConstNpc.QUY_DOI:
                        switch (select) {
                            case 0:
                                Input.gI().createFormQDTV(player);
                                break;
                            case 1:
                                Input.gI().createFormQDHN(player);
                                break;
                            case 2:
                                Input.gI().createFormQDXUVANG(player);
                                break;
                            case 3:
                                Input.gI().createFormQDXUBAC(player);
                                break;
                                  case 4:
                                Input.gI().createFormQDTHANGTINHTHACH(player);
                                break;
                        }
                        break;
                    case ConstNpc.BANKING:
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.QUY_DOI, 12639, "|7|DragonBall Kamui" + "\n"
                                        + "VNĐ CHANGE\n"
                                        + "|2|\nChào bạn : " + player.name
                                        + "\nSố Dư Khả Dụng : " + Util.format(player.getSession().vnd) + " VNĐ "
                                        + "\n|7|Tỉ Lệ Quy Đổi Hiện Tại Là : Thỏi Vàng X" + Manager.TLDOITV + ", Hồng Ngọc X" + Util.format(Manager.TLDOIHN)
                                        + "\n|5|(Giới Hạn Quy Đồi Là 1000 tức không quá 1 triệu Vnđ)"
                                        + "\n" + "Ví Dụ : Nhập 1 = 1.000 Vnđ = " + Manager.TLDOITV + " Thỏi Vàng "
                                        + "\n" + "Ví Dụ : Nhập 1 = 1.000 Vnđ = " + Util.format(Manager.TLDOIHN * 1000) + " Hồng Ngọc",
                                        "ĐỔI\nTHỎI VÀNG", "ĐỔI\nHỒNG NGỌC", "Quay lại");
                                break;
                            case 3:
                                if (player.playerTask.taskMain.id < 21) {
                                    TaskService.gI().getTaskMainById(player, player.playerTask.taskMain.id += 1);
                                    TaskService.gI().sendNextTaskMain(player);
                                    Service.gI().sendThongBao(player, "Làm nhiệm vụ mới thôi nào!");
                                } else {
                                    Service.gI().sendThongBao(player, "Cúc!");
                                }
                                break;
                        }
                        break;
                    case ConstNpc.INFO:
                        switch (select) {
//                            case 2:
//                                NpcService.gI().createMenuConMeo(player, ConstNpc.CHECK_DH, 12639,
//                                        "|7|- •⊹٭DragonBall Kamui٭⊹• -\nTITLE SERVICE\n"
//                                        + "|2|Danh Hiệu Top Event"
//                                        + "\n|5|Đại Thần (SD +15%, HP,KI +20%) : " + (player.timedh1 > 0 ? Util.msToTime(player.timedh1) : player.usedh1 == false ? "Có thể nhận qua event đua top" : "Đang Bật")
//                                        + "\nTrùm Cuối (SD +15%, HP,KI +20%) : " + (player.timedh2 > 0 ? Util.msToTime(player.timedh2) : player.usedh2 == false ? "Có thể nhận qua event đua top" : "Đang Bật")
//                                        + "\nBất Bại (SD +15%, HP,KI +20%) : " + (player.timedh3 > 0 ? Util.msToTime(player.timedh3) : player.usedh3 == false ? "Có thể nhận qua event đua top" : "Đang Bật")
//                                        + "\n|2|Danh Hiệu Event"
//                                        + "\n|5|Buôn Ve Chai (5% SD,HP,KI): " + (player.dhtang1 == true ? "Đang Bật" : player.titlett == false ? "Có thể nhận được khi tham gia event" : "Đang Tắt")
//                                        + "\nFan Cứng (5% SD,HP,KI) : " + (player.dhtang2 == true ? "Đang Bật" : player.titlett == false ? "Có thể nhận được khi tham gia event" : "Đang Tắt")
//                                        + "\nSức Mạnh (5% SD,HP,KI) : " + (player.dhhanhtinh == true ? "Đang Bật" : player.titlett == false ? "Có thể nhận được khi đạt 180 Tỉ" : "Đang Tắt")
//                                        + "\nPhong Ba (Cập nhật) : " + (player.dhphongba == true ? "Đang Bật" : player.titlett == false ? "Có thể nhận được khi tham gia event" : "Đang Tắt")
//                                        + "\nTuổi Thơ (Cập nhật) : " + (player.dhtuoitho == true ? "Đang Bật" : player.titlett == false ? "Có thể nhận được khi tham gia event" : "Đang Tắt"),
//                                        ("Đại Thần\n" + (player.usedh1 == true ? "'ON'" : "'OFF'")),
//                                        ("Trùm Cuối\n" + (player.usedh2 == true ? "'ON'" : "'OFF'")),
//                                        ("Bất Bại\n" + (player.usedh3 == true ? "'ON'" : "'OFF'")),
//                                        ("Ve Chai\n" + (player.dhtang1 == true ? "'ON'" : "'OFF'")),
//                                        ("Fan Cứng\n" + (player.dhtang2 == true ? "'ON'" : "'OFF'")),
//                                        ("Sức Mạnh\n" + (player.dhhanhtinh == true ? "'ON'" : "'OFF'")),
//                                        ("Phong Ba\n" + (player.dhphongba == true ? "'ON'" : "'OFF'")),
//                                        ("Tuổi Thơ\n" + (player.dhtuoitho == true ? "'ON'" : "'OFF'")),
//                                        "Làm Mới");
//                                break;
                            case 1:
                                if (player.pet != null) {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.IGNORE_MENU, 12639,
                                            "|7|[ Info Player Pet : " + player.pet.getNameThuctinh(player.pet.thuctinh) + player.pet.name.substring(1) + " ]\n"
                                            + "Trạng thái : " + (player.pet.status == 1 ? "Bảo Vệ" : player.pet.status == 2 ? "Tấn Công" : player.pet.status == 3 ? "Về Nhà" : player.pet.status == 4 ? "Hợp Thể" : "Đi Theo")
                                            + "\n|2|Chỉ Số Tổng"
                                            + "\n|5|HP : " + Util.format(player.pet.nPoint.hp) + "/" + Util.format(player.pet.nPoint.hpMax)
                                            + "\nKI : " + Util.format(player.pet.nPoint.mp) + "/" + Util.format(player.pet.nPoint.mpMax)
                                            + "\nSĐ : " + Util.format(player.pet.nPoint.dame)
                                            + "\nDEF : " + Util.format(player.pet.nPoint.def)
                                            + "\nCRIT : " + Util.format(player.pet.nPoint.crit)
                                            + "\n|2|Chỉ Số Gốc"
                                            + "\n|5|HPG : " + Util.format(player.pet.nPoint.hpg)
                                            + "\nKIG : " + Util.format(player.pet.nPoint.mpg)
                                            + "\nSĐG : " + Util.format(player.pet.nPoint.dameg)
                                            + "\nDEFG : " + Util.format(player.pet.nPoint.defg)
                                            + "\nCRITG : " + Util.format(player.pet.nPoint.critg) + "%", "Đóng");
                                } else {
                                    Service.gI().sendThongBao(player, "Bạn chưa có đệ");
                                }
                                break;
                            case 0:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_PLAYER, 12639, "|7|[ Info Player : " + player.name + " ]"
                                        + "\nTrạng thái tài khoản : " + (player.getSession().actived == 1 ? " Đã mở thành viên" : " Chưa mở thành viên")
                                        + (player.vip == 1 ? " [VIP]" : player.vip == 2 ? " [VIP2]" : player.vip == 3 ? " [VIP3]" : player.vip == 4 ? " [SVIP]" : "")
                                        + "\n|2|Chỉ Số Tổng"
                                        + "\n|5|HP : " + Util.format(player.nPoint.hp) + "/" + Util.format(player.nPoint.hpMax)
                                        + "\nKI : " + Util.format(player.nPoint.mp) + "/" + Util.format(player.nPoint.mpMax)
                                        + "\nSĐ : " + Util.format(player.nPoint.dame)
                                        + "\nDEF : " + Util.format(player.nPoint.def)
                                        + "\nCRIT : " + player.nPoint.crit + "%"
                                        + "\n|2|Chỉ Số Gốc"
                                        + "\n|5|HPG : " + player.nPoint.hpg
                                        + "\nKIG : " + player.nPoint.mpg
                                        + "\nSĐG : " + player.nPoint.dameg
                                        + "\nDEFG : " + player.nPoint.defg
                                        + "\nCRITG : " + player.nPoint.critg + "%"
                                        + "\n|2|Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName() + " ("
                                        + player.playerTask.sideTask.getLevel() + ")"
                                        + "\n|5|Hiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/"
                                        + player.playerTask.sideTask.maxCount + " ("
                                        + player.playerTask.sideTask.getPercentProcess() + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                        + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK,
                                        "Nhập\nGiftcode", "Đổi\nMật Khẩu", "Auto\nBán Vàng", "Auto\nChỉ Số");
                                break;
                            case 3:
                                Item vecall = InventoryServiceNew.gI().findItemBag(player, 1993);
                                if (vecall != null) {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_CALL, 20673, "Call Boss\n",
                                            "Call Boss", "Đóng");
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, vecall, 1);
                                    InventoryServiceNew.gI().sendItemBags(player);

                                    return;

                                }
                        }
                        break;
                    case ConstNpc.CHECK_DH:
                        switch (select) {
                            case 0:
                                if (player.timedh1 > 0) {
                                    player.usedh1 = !player.usedh1;
                                    Service.gI().removedh(player);
                                    Service.gI().sendThongBao(player, "Đã " + (player.usedh1 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Ngươi làm gì có danh hiệu");
                                }
                                break;
                            case 1:
                                if (player.timedh2 > 0) {
                                    player.usedh2 = !player.usedh2;
                                    Service.gI().removedh(player);
                                    Service.gI().sendThongBao(player, "Đã " + (player.usedh2 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Ngươi làm gì có danh hiệu");
                                }
                                break;
                            case 2:
                                if (player.timedh3 > 0) {
                                    player.usedh3 = !player.usedh3;
                                    Service.gI().removedh(player);
                                    Service.gI().sendThongBao(player, "Đã " + (player.usedh3 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Ngươi làm gì có danh hiệu");
                                }
                                break;
                            case 3:
                                if (player.titlett == true) {
                                    player.dhtang1 = !player.dhtang1;
                                    player.dhhanhtinh = false;
                                    player.dhphongba = false;
                                    player.dhtuoitho = false;
                                    Service.gI().removedh(player);
                                    Service.gI().sendThongBao(player, "Đã " + (player.dhtang1 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Ngươi làm gì có danh hiệu");
                                }
                                break;
                            case 4:
                                if (player.titlett == true) {
                                    player.dhtang2 = !player.dhtang2;
                                    Service.gI().removedh(player);
                                    Service.gI().sendThongBao(player, "Đã " + (player.dhtang2 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Ngươi làm gì có danh hiệu");
                                }
                                break;
                            case 5:
                                if (player.titlett == true) {
                                    player.dhhanhtinh = !player.dhhanhtinh;
                                    player.dhphongba = false;
                                    player.dhtuoitho = false;
                                    player.dhtang1 = false;
                                    Service.gI().removedh(player);
                                    Service.gI().sendThongBao(player, "Đã " + (player.dhhanhtinh == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Ngươi làm gì có danh hiệu");
                                }
                                break;
                            case 6:
                                if (player.titlett == true) {
                                    player.dhphongba = !player.dhphongba;
                                    player.dhtang1 = false;
                                    player.dhhanhtinh = false;
                                    player.dhtuoitho = false;
                                    Service.gI().removedh(player);
                                    Service.gI().sendThongBao(player, "Đã " + (player.dhphongba == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Ngươi làm gì có danh hiệu");
                                }
                                break;
                            case 7:
                                if (player.titlett == true) {
                                    player.dhtuoitho = !player.dhtuoitho;
                                    player.dhtang1 = false;
                                    player.dhhanhtinh = false;
                                    player.dhphongba = false;
                                    Service.gI().removedh(player);
                                    Service.gI().sendThongBao(player, "Đã " + (player.dhphongba == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Ngươi làm gì có danh hiệu");
                                }
                                break;
                            case 8:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.CHECK_DH, 12639,
                                        "|7|- •⊹٭DragonBall Kamui٭⊹• -\nTITLE SERVICE\n"
                                        + "|2|Danh Hiệu Top Event"
                                        + "\n|5|Đại Thần (SD +15%, HP,KI +20%) : " + (player.timedh1 > 0 ? Util.msToTime(player.timedh1) : player.usedh1 == false ? "Có thể nhận qua event đua top" : "Đang Bật")
                                        + "\nTrùm Cuối (SD +15%, HP,KI +20%) : " + (player.timedh2 > 0 ? Util.msToTime(player.timedh2) : player.usedh2 == false ? "Có thể nhận qua event đua top" : "Đang Bật")
                                        + "\nBất Bại (SD +15%, HP,KI +20%) : " + (player.timedh3 > 0 ? Util.msToTime(player.timedh3) : player.usedh3 == false ? "Có thể nhận qua event đua top" : "Đang Bật")
                                        + "\n|2|Danh Hiệu Event"
                                        + "\n|5|Buôn Ve Chai (5% SD,HP,KI): " + (player.dhtang1 == true ? "Đang Bật" : player.titlett == false ? "Có thể nhận được khi tham gia event" : "Đang Tắt")
                                        + "\nFan Cứng (5% SD,HP,KI) : " + (player.dhtang2 == true ? "Đang Bật" : player.titlett == false ? "Có thể nhận được khi tham gia event" : "Đang Tắt")
                                        + "\nSức Mạnh (5% SD,HP,KI) : " + (player.dhhanhtinh == true ? "Đang Bật" : player.titlett == false ? "Có thể nhận được khi đạt 180 Tỉ" : "Đang Tắt")
                                        + "\nPhong Ba (Cập nhật) : " + (player.dhphongba == true ? "Đang Bật" : player.titlett == false ? "Có thể nhận được khi tham gia event" : "Đang Tắt")
                                        + "\nTuổi Thơ (Cập nhật) : " + (player.dhtuoitho == true ? "Đang Bật" : player.titlett == false ? "Có thể nhận được khi tham gia event" : "Đang Tắt"),
                                        ("Đại Thần\n" + (player.usedh1 == true ? "'ON'" : "'OFF'")),
                                        ("Trùm Cuối\n" + (player.usedh2 == true ? "'ON'" : "'OFF'")),
                                        ("Bất Bại\n" + (player.usedh3 == true ? "'ON'" : "'OFF'")),
                                        ("Ve Chai\n" + (player.dhtang1 == true ? "'ON'" : "'OFF'")),
                                        ("Fan Cứng\n" + (player.dhtang2 == true ? "'ON'" : "'OFF'")),
                                        ("Sức Mạnh\n" + (player.dhhanhtinh == true ? "'ON'" : "'OFF'")),
                                        ("Phong Ba\n" + (player.dhphongba == true ? "'ON'" : "'OFF'")),
                                        ("Tuổi Thơ\n" + (player.dhtuoitho == true ? "'ON'" : "'OFF'")),
                                        "Làm Mới");
                                break;
                        }
                        break;

//                    case ConstNpc.MINI_GAME:
//                        switch (select) {
//                            case 0:
//                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "|5|Có 2 nhà cái Tài và Xĩu, bạn chỉ được chọn 1 nhà để tham gia"
//                                        + "\n\n|6|Sau khi kết thúc thời gian đặt cược. Hệ thống sẽ tung xí ngầu để biết kết quả Tài Xỉu"
//                                        + "\n\nNếu Tổng số 3 con xí ngầu <=10 : XỈU\nNếu Tổng số 3 con xí ngầu >10 : TÀI\nNếu 3 Xí ngầu cùng 1 số : TAM HOA (Nhà cái lụm hết)"
//                                        + "\n\n|7|Lưu ý: Số Hồng ngọc nhận được sẽ bị nhà cái lụm đi 20%. Trong quá trình diễn ra khi đặt cược nếu thoát game sẽ bị MẤT TIỀN ĐẶT CƯỢC", "Ok");
//                                break;
//                            case 1:
//                                String time = ((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
//                                if (TaiXiu_Old.gI().baotri == false) {
//                                    if (player.goldTai < 500000) {
//                                        createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|- NHÀ CÁI TÀI XỈU Kamui -\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z + " Tổng : " + (TaiXiu_Old.gI().x + TaiXiu_Old.gI().y + TaiXiu_Old.gI().z)
//                                                + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                                + "\nTổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time,
//                                                "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
//                                    }
//                                    if (player.goldXiu < 500000) {
//                                        createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|- NHÀ CÁI TÀI XỈU Kamui -\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z + " Tổng : " + (TaiXiu_Old.gI().x + TaiXiu_Old.gI().y + TaiXiu_Old.gI().z)
//                                                + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                                + "\nTổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time,
//                                                "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
//                                    } else if (player.goldTai >= 500000) {
//                                        createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|- NHÀ CÁI TÀI XỈU Kamui -\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z + " Tổng : " + (TaiXiu_Old.gI().x + TaiXiu_Old.gI().y + TaiXiu_Old.gI().z)
//                                                + "\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                                + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Tài : " + Util.format(player.goldTai) + " Hồng ngọc",
//                                                "Cập nhập", "Đóng");
//                                    } else if (player.goldXiu >= 500000) {
//                                        createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|- NHÀ CÁI TÀI XỈU Kamui -\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z + " Tổng : " + (TaiXiu_Old.gI().x + TaiXiu_Old.gI().y + TaiXiu_Old.gI().z)
//                                                + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                                + "\nTổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(player.goldXiu) + " Hồng ngọc",
//                                                "Cập nhập", "Đóng");
//                                    }
//                                } else {
//                                    if (player.goldTai == 0 && player.goldXiu == 0) {
//                                        createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|- NHÀ CÁI TÀI XỈU Kamui -\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z + " Tổng : " + (TaiXiu_Old.gI().x + TaiXiu_Old.gI().y + TaiXiu_Old.gI().z)
//                                                + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                                + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Hệ thống sắp bảo trì",
//                                                "Cập nhập", "Đóng");
//                                    } else if (player.goldTai > 0) {
//                                        createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|- NHÀ CÁI TÀI XỈU Kamui -\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z + " Tổng : " + (TaiXiu_Old.gI().x + TaiXiu_Old.gI().y + TaiXiu_Old.gI().z)
//                                                + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                                + "\nTổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Tài : " + Util.format(player.goldTai) + " Hồng ngọc" + "\n\n|7|Hệ thống sắp bảo trì",
//                                                "Cập nhập", "Đóng");
//                                    } else {
//                                        createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|- NHÀ CÁI TÀI XỈU Kamui -\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z + " Tổng : " + (TaiXiu_Old.gI().x + TaiXiu_Old.gI().y + TaiXiu_Old.gI().z)
//                                                + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                                + "\nTổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(player.goldXiu) + " Hồng ngọc" + "\n\n|7|Hệ thống sắp bảo trì",
//                                                "Cập nhập", "Đóng");
//                                    }
//                                }
//                                break;
//                        }
//                        break;
//                    case ConstNpc.TAIXIU:
//                        String time = ((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
//                        if (((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldTai == 0 && player.goldXiu == 0 && TaiXiu_Old.gI().baotri == false) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|- NHÀ CÁI TÀI XỈU Kamui -\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z + " Tổng : " + (TaiXiu_Old.gI().x + TaiXiu_Old.gI().y + TaiXiu_Old.gI().z)
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\nTổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
//                                    break;
//                                case 1:
//                                    if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
//                                        Input.gI().TAI_taixiu(player);
//                                    } else {
//                                        Service.getInstance().sendThongBao(player, "Bạn chưa đủ điều kiện để chơi");
//                                    }
//                                    break;
//                                case 2:
//                                    if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
//                                        Input.gI().XIU_taixiu(player);
//                                    } else {
//                                        Service.getInstance().sendThongBao(player, "Bạn chưa đủ điều kiện để chơi");
//                                    }
//                                    break;
//                            }
//                        } else if (((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldTai > 0 && TaiXiu_Old.gI().baotri == false) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|- NHÀ CÁI TÀI XỈU Kamui -\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z + " Tổng : " + (TaiXiu_Old.gI().x + TaiXiu_Old.gI().y + TaiXiu_Old.gI().z)
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\nTổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Tài : " + Util.format(player.goldTai) + " Hồng ngọc", "Cập nhập", "Đóng");
//                                    break;
//                            }
//                        } else if (((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldXiu > 0 && TaiXiu_Old.gI().baotri == false) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|- NHÀ CÁI TÀI XỈU Kamui -\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z + " Tổng : " + (TaiXiu_Old.gI().x + TaiXiu_Old.gI().y + TaiXiu_Old.gI().z)
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\nTổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(player.goldXiu) + " Hồng ngọc", "Cập nhập", "Đóng");
//                                    break;
//                            }
//                        } else if (((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldTai > 0 && TaiXiu_Old.gI().baotri == true) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|- NHÀ CÁI TÀI XỈU Kamui -\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z + " Tổng : " + (TaiXiu_Old.gI().x + TaiXiu_Old.gI().y + TaiXiu_Old.gI().z)
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\nTổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Tài : " + Util.format(player.goldTai) + " Hồng ngọc" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
//                                    break;
//                            }
//                        } else if (((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldXiu > 0 && TaiXiu_Old.gI().baotri == true) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|- NHÀ CÁI TÀI XỈU Kamui -\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z + " Tổng : " + (TaiXiu_Old.gI().x + TaiXiu_Old.gI().y + TaiXiu_Old.gI().z)
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\nTổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(player.goldXiu) + " Hồng ngọc" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
//                                    break;
//                            }
//                        } else if (((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldXiu == 0 && player.goldTai == 0 && TaiXiu_Old.gI().baotri == true) {
//                            switch (select) {
//                                case 0:
//                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|- NHÀ CÁI TÀI XỈU Kamui -\n|3|Kết quả kì trước:  " + TaiXiu_Old.gI().x + " : " + TaiXiu_Old.gI().y + " : " + TaiXiu_Old.gI().z + " Tổng : " + (TaiXiu_Old.gI().x + TaiXiu_Old.gI().y + TaiXiu_Old.gI().z)
//                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Old.gI().goldTai) + " Hồng ngọc"
//                                            + "\nTổng nhà XỈU: " + Util.format(TaiXiu_Old.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
//                                    break;
//                            }
//                        }
//                        break;
                    case ConstNpc.MUA_DE:
                        switch (select) {
                            /*  case 0:
                            this.createOtherMenu(player, ConstNpc.CONFIRM_MUA_DE_BERUS, "Bạn có chắc muốn mua đệ Berus không?", "Đồng ý", "Không");
                            break; */
                            case 0:
                                this.createOtherMenu(player, ConstNpc.CONFIRM_MUA_DE_KEFLA, "Bạn có chắc muốn mua đệ Cumber SSJ2(30%) không?", "Đồng ý", "Không");
                                break;
                            case 1:
                                //  Service.gI().sendThongBao(player, "Hiện tại chưa mở bán");
                                this.createOtherMenu(player, ConstNpc.CONFIRM_MUA_DE_CUMBER, "Bạn có chắc muốn mua đệ Goku SSJ4 (50%) không?\nMua đệ này kèm theo cải trang ssj4 hoặc cải trang có option Gogeta\nkhi hợp thể tăng 70%", "Đồng ý", "Không");
                                break;
                            case 2:
                                //  Service.gI().sendThongBao(player, "Hiện tại chưa mở bán");
                                this.createOtherMenu(player, ConstNpc.CONFIRM_MUA_DE_GOKU, "Bạn có chắc muốn mua đệ Goku SSJ4 (50%) không?\nMua đệ này kèm theo cải trang ssj4 hoặc cải trang có option Gogeta\nkhi hợp thể tăng 70%", "Đồng ý", "Không");
                                break;
                        }
                        break;
                    case ConstNpc.DOIGENDER:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd < 50000) {
                                    Service.gI().sendThongBao(player, "Bạn không dủ tiền, hãy nạp tiền để mua");
                                } else {
                                    if (player.pet != null) {
                                        try {
                                            int sum = player.getSession().vnd - 50000;
                                            GirlkunDB.executeUpdate(
                                                    "update account set vnd = ? where id = ?", sum,
                                                    player.account_id);
                                            player.getSession().vnd = sum;
                                        } catch (Exception e) {
                                            this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                        }
                                        player.pet.gender = 0;
                                        ChangeMapService.gI().exitMap(player.pet);
                                        Service.gI().sendThongBao(player, "Đã đổi thành công hành tinh Trái Đất");
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn làm gì có đệ");
                                    }
                                }
                                break;
                            case 1:
                                if (player.getSession().vnd < 50000) {
                                    Service.gI().sendThongBao(player, "Bạn không dủ tiền, hãy nạp tiền để mua");
                                } else {
                                    if (player.pet != null) {
                                        try {
                                            int sum = player.getSession().vnd - 50000;
                                            GirlkunDB.executeUpdate(
                                                    "update account set vnd = ? where id = ?", sum,
                                                    player.account_id);
                                            player.getSession().vnd = sum;
                                        } catch (Exception e) {
                                            this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                        }
                                        player.pet.gender = 1;
                                        ChangeMapService.gI().exitMap(player.pet);
                                        Service.gI().sendThongBao(player, "Đã đổi thành công hành tinh Namek");
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn làm gì có đệ");
                                    }
                                }
                                break;
                            case 2:
                                if (player.getSession().vnd < 50000) {
                                    Service.gI().sendThongBao(player, "Bạn không dủ tiền, hãy nạp tiền để mua");
                                } else {
                                    if (player.pet != null) {
                                        try {
                                            int sum = player.getSession().vnd - 50000;
                                            GirlkunDB.executeUpdate(
                                                    "update account set vnd = ? where id = ?", sum,
                                                    player.account_id);
                                            player.getSession().vnd = sum;
                                        } catch (Exception e) {
                                            this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                        }
                                        player.pet.gender = 2;
                                        ChangeMapService.gI().exitMap(player.pet);
                                        Service.gI().sendThongBao(player, "Đã đổi thành công đệ hành tinh Xayda");
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn làm gì có đệ");
                                    }
                                }
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_MUA_DE_PIC:
                        switch (select) {
                            case 0:
                                if (player.getSession().actived == 0) {
                                    Service.gI().sendThongBao(player, "Vui Lòng Active Tài Khoản");
                                } else {
                                    if (player.pet != null) {
                                        int gender1 = player.gender;
                                        PetService.gI().changePicPet(player, gender1);
                                        Service.gI().sendThongBao(player, "Nhânn thành công đệ Pic");
                                    } else {
                                        Service.gI().sendThongBao(player, "Hiện bạn chưa có đệ tử, hãy săn đệ và quay lại sau");
                                    }
                                }
                                break;
                            case 1:
                                this.npcChat(player, "Cảm ơn đã quý khách đã ghé shop");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_MUA_DE_BERUS:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd < 100000) {
                                    Service.gI().sendThongBao(player, "Bạn không dủ tiền, hãy nạp tiền để mua");
                                } else {
                                    if (player.pet != null) {
                                        try {
                                            int sum = player.getSession().vnd - 100000;
                                            GirlkunDB.executeUpdate(
                                                    "update account set active = ?, vnd = ? where id = ?", 1, sum,
                                                    player.account_id);
                                            player.getSession().vnd = sum;
                                        } catch (Exception e) {
                                            this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                        }
                                        int gender1 = player.gender;
                                        PetService.gI().changeBerusPet(player, gender1);
                                        Service.gI().sendThongBao(player, "Bạn mua thành công đệ Berus");
                                    } else {
                                        Service.gI().sendThongBao(player, "Hiện bạn chưa có đệ tử, hãy săn đệ và quay lại sau");
                                    }
                                }
                                break;
                            case 1:
                                this.npcChat(player, "Cảm ơn đã quý khách đã ghé shop");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_MUA_DE_CUMBER:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd < 150000) {
                                    Service.gI().sendThongBao(player, "Bạn không dủ tiền, hãy nạp tiền để mua");
                                } else {
                                    if (player.pet == null) {
                                        try {
                                            int sum = player.getSession().vnd - 150000;
                                            GirlkunDB.executeUpdate(
                                                    "update account set active = ?, vnd = ? where id = ?", 1, sum,
                                                    player.account_id);
                                            player.getSession().vnd = sum;
                                            player.getSession().actived = 1;
                                        } catch (Exception e) {
                                            this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                        }
                                        int gender1 = player.gender;
                                        PetService.gI().createGokuPet(player, gender1);
                                        Service.gI().sendThongBao(player, "Bạn mua thành công đệ Goku SSJ4");
                                    }
                                    if (player.pet != null && player.pet.inventory.itemsBody.get(0).isNotNullItem() == false && player.pet.inventory.itemsBody.get(1).isNotNullItem() == false && player.pet.inventory.itemsBody.get(2).isNotNullItem() == false && player.pet.inventory.itemsBody.get(3).isNotNullItem() == false && player.pet.inventory.itemsBody.get(4).isNotNullItem() == false && player.pet.inventory.itemsBody.get(5).isNotNullItem() == false && player.pet.inventory.itemsBody.get(6).isNotNullItem() == false && player.pet.inventory.itemsBody.get(7).isNotNullItem() == false ) {
                                        try {
                                            int sum = player.getSession().vnd - 150000;
                                            GirlkunDB.executeUpdate(
                                                    "update account set active = ?, vnd = ? where id = ?", 1, sum,
                                                    player.account_id);
                                            player.getSession().vnd = sum;
                                            player.getSession().actived = 1;
                                        } catch (Exception e) {
                                            this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                        }
                                        int gender1 = player.gender;
                                        PetService.gI().changeGokuPet(player, gender1);
                                        Service.gI().sendThongBao(player, "Bạn mua thành công đệ Goku SSJ4");
                                    } else {
                                        Service.gI().sendThongBao(player, "Vui lòng tháo đồ!");
                                    }
                                }
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_MUA_DE_GOKU:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd < 150000) {
                                    Service.gI().sendThongBao(player, "Bạn không dủ tiền, hãy nạp tiền để mua");
                                } else {
                                    if (player.pet == null) {
                                        try {
                                            int sum = player.getSession().vnd - 150000;
                                            GirlkunDB.executeUpdate(
                                                    "update account set active = ?, vnd = ? where id = ?", 1, sum,
                                                    player.account_id);
                                            player.getSession().vnd = sum;
                                            player.getSession().actived = 1;
                                        } catch (Exception e) {
                                            this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                        }
                                        int gender1 = player.gender;
                                        PetService.gI().createCadicPet(player, gender1);
                                        Service.gI().sendThongBao(player, "Bạn mua thành công đệ Cadic SSJ4");
                                    }
                                    if (player.pet != null && player.pet.inventory.itemsBody.get(0).isNotNullItem() == false && player.pet.inventory.itemsBody.get(1).isNotNullItem() == false && player.pet.inventory.itemsBody.get(2).isNotNullItem() == false && player.pet.inventory.itemsBody.get(3).isNotNullItem() == false && player.pet.inventory.itemsBody.get(4).isNotNullItem() == false && player.pet.inventory.itemsBody.get(5).isNotNullItem() == false && player.pet.inventory.itemsBody.get(6).isNotNullItem() == false && player.pet.inventory.itemsBody.get(7).isNotNullItem() == false) {
                                        try {
                                            int sum = player.getSession().vnd - 150000;
                                            GirlkunDB.executeUpdate(
                                                    "update account set active = ?, vnd = ? where id = ?", 1, sum,
                                                    player.account_id);
                                            player.getSession().vnd = sum;
                                            player.getSession().actived = 1;
                                        } catch (Exception e) {
                                            this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                        }
                                        int gender1 = player.gender;
                                        PetService.gI().changeCadicPet(player, gender1);
                                        Service.gI().sendThongBao(player, "Bạn mua thành công đệ Cadic SSJ4");
                                    } else {
                                        Service.gI().sendThongBao(player, "Vui lòng tháo đồ!");
                                    }
                                }
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_MUA_DE_KEFLA:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd < 100000) {
                                    Service.gI().sendThongBao(player, "Bạn không dủ tiền, hãy nạp tiền để mua");
                                } else {
                                    if (player.pet == null) {
                                        try {
                                            int sum = player.getSession().vnd - 100000;
                                            GirlkunDB.executeUpdate(
                                                    "update account set active = ?, vnd = ? where id = ?", 1, sum,
                                                    player.account_id);
                                            player.getSession().vnd = sum;
                                            player.getSession().actived = 1;
                                        } catch (Exception e) {
                                            this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                        }
                                        int gender1 = player.gender;
                                        PetService.gI().createCumberPet(player, gender1);
                                        Service.gI().sendThongBao(player, "Bạn mua thành công đệ Cumber");
                                    }
                                    if (player.pet != null && player.pet.inventory.itemsBody.get(0).isNotNullItem() == false && player.pet.inventory.itemsBody.get(1).isNotNullItem() == false && player.pet.inventory.itemsBody.get(2).isNotNullItem() == false && player.pet.inventory.itemsBody.get(3).isNotNullItem() == false && player.pet.inventory.itemsBody.get(4).isNotNullItem() == false && player.pet.inventory.itemsBody.get(5).isNotNullItem() == false && player.pet.inventory.itemsBody.get(6).isNotNullItem() == false && player.pet.inventory.itemsBody.get(7).isNotNullItem() == false) {
                                        try {
                                            int sum = player.getSession().vnd - 100000;
                                            GirlkunDB.executeUpdate(
                                                    "update account set active = ?, vnd = ? where id = ?", 1, sum,
                                                    player.account_id);
                                            player.getSession().vnd = sum;
                                            player.getSession().actived = 1;
                                        } catch (Exception e) {
                                            this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                        }
                                        int gender1 = player.gender;
                                        PetService.gI().changeCumberPet(player, gender1);
                                        Service.gI().sendThongBao(player, "Bạn mua thành công đệ Cumber");
                                    } else {
                                        Service.gI().sendThongBao(player, "Vui lòng tháo đồ!");
                                    }
                                }
                                break;
                        }
                        break;
                    case ConstNpc.DOI_SKILL:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd < 10000) {
                                    Service.gI().sendThongBao(player, "Còn thiếu " + (10000 - player.getSession().vnd));
                                } else {
                                    if (player.pet == null) {
                                        Service.gI().sendThongBao(player, "Ngươi làm gì có đệ tử?");
                                        break;
                                    }
                                    if (player.pet.playerSkill.skills.get(1).skillId != -1 && player.pet.playerSkill.skills.get(2).skillId != -1) {
                                        try {
                                            int sum = player.getSession().vnd - 10000;
                                            GirlkunDB.executeUpdate(
                                                    "update account set vnd = ? where id = ?", sum,
                                                    player.account_id);
                                            player.getSession().vnd = sum;
                                        } catch (Exception e) {
                                            this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                        }
                                        player.pet.openSkill2();
                                        player.pet.openSkill3();
                                        Service.gI().sendThongBao(player, "Đã đổi thành công chiêu 2,3 đệ tử");
                                    } else if (player.pet.playerSkill.skills.get(1).skillId != -1) {
                                        try {
                                            int sum = player.getSession().vnd - 15000;
                                            GirlkunDB.executeUpdate(
                                                    "update account set vnd = ? where id = ?", sum,
                                                    player.account_id);
                                            player.getSession().vnd = sum;
                                        } catch (Exception e) {
                                            this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                        }
                                        player.pet.openSkill2();
                                        Service.gI().sendThongBao(player, "Đã đổi thành công chiêu 2,3 đệ tử");
                                    } else {
                                        Service.gI().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                                    }
                                }
                                break;
                            case 1:
                                if (player.getSession().vnd < 20000) {
                                    Service.gI().sendThongBao(player, "Còn thiếu " + (20000 - player.getSession().vnd));
                                } else {
                                    if (player.pet == null) {
                                        Service.gI().sendThongBao(player, "Ngươi làm gì có đệ tử?");
                                        break;
                                    }
                                    if (player.pet.playerSkill.skills.get(2).skillId != -1 && player.pet.playerSkill.skills.get(3).skillId != -1) {
                                        try {
                                            int sum = player.getSession().vnd - 20000;
                                            GirlkunDB.executeUpdate("update account set vnd = ? where id = ?", sum, player.account_id);
                                            player.getSession().vnd = sum;
                                        } catch (Exception e) {
                                            this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                        }
                                        player.pet.openSkill3();
                                        player.pet.openSkill4();
                                        Service.gI().sendThongBao(player, "Đã đổi thành công chiêu 3,4 đệ tử");
                                    } else if (player.pet.playerSkill.skills.get(2).skillId != -1) {
                                        try {
                                            int sum = player.getSession().vnd - 30000;
                                            GirlkunDB.executeUpdate("update account set vnd = ? where id = ?", sum, player.account_id);
                                            player.getSession().vnd = sum;
                                        } catch (Exception e) {
                                            this.npcChat(player, "Đã có lỗi xảy ra!" + e);
                                        }
                                        player.pet.openSkill3();
                                        Service.gI().sendThongBao(player, "Đã đổi thành công chiêu 3,4 đệ tử");
                                    } else {
                                        Service.gI().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 3 chứ!");
                                    }
                                }
                                break;
                        }
                        break;
                    case ConstNpc.CHISO_NHANH:
                        switch (select) {
                            case 0:
                                player.autoHP = !player.autoHP;
                                Service.gI().sendThongBao(player, "|1|Đã " + (player.autoHP == true ? "Bật" : "Tắt") + " Auto cộng chỉ số HP");
                                try {
                                    while (player.autoHP == true) {
                                        if (player.nPoint != null && player.nPoint.hpg + 20000 <= player.nPoint.getHpMpLimit()) {
                                            player.nPoint.increasePoint((byte) 0, (short) 1000);
                                            Service.gI().sendThongBao(player, "|8|Auto cộng HP!\n|2|" + player.nPoint.hpg);
                                        } else if (player.nPoint != null && player.nPoint.hpg + 2000 <= player.nPoint.getHpMpLimit()) {
                                            player.nPoint.increasePoint((byte) 0, (short) 100);
                                            Service.gI().sendThongBao(player, "|8|Auto cộng HP!\n|2|" + player.nPoint.hpg);
                                        } else if (player.nPoint != null && player.nPoint.hpg + 200 <= player.nPoint.getHpMpLimit()) {
                                            player.nPoint.increasePoint((byte) 0, (short) 10);
                                            Service.gI().sendThongBao(player, "|8|Auto cộng HP!\n|2|" + player.nPoint.hpg);
                                        } else if (player.nPoint != null && player.nPoint.hpg + 20 <= player.nPoint.getHpMpLimit()) {
                                            player.nPoint.increasePoint((byte) 0, (short) 1);
                                            Service.gI().sendThongBao(player, "|8|Auto cộng HP!\n|2|" + player.nPoint.hpg);
                                        } else {
                                            player.autoHP = false;
                                            Service.gI().sendThongBaoFromAdmin(player, "|8|Auto cộng HP đã dừng!\n|2|Bạn đã đạt giới hạn sức mạnh");
                                            break;
                                        }
                                    }
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                player.autoKI = !player.autoKI;
                                Service.gI().sendThongBao(player, "|1|Đã " + (player.autoKI == true ? "Bật" : "Tắt") + " Auto cộng chỉ số KI");
                                if (player.autoKI == true) {
                                    try {
                                        while (player.autoKI == true) {
                                            if (player.nPoint != null && player.nPoint.mpg + 20000 <= player.nPoint.getHpMpLimit()) {
                                                player.nPoint.increasePoint((byte) 1, (short) 1000);
                                                Service.gI().sendThongBao(player, "|8|Auto cộng KI!\n|2|" + player.nPoint.mpg);
                                            } else if (player.nPoint != null && player.nPoint.mpg + 2000 <= player.nPoint.getHpMpLimit()) {
                                                player.nPoint.increasePoint((byte) 1, (short) 100);
                                                Service.gI().sendThongBao(player, "|8|Auto cộng KI!\n|2|" + player.nPoint.mpg);
                                            } else if (player.nPoint != null && player.nPoint.mpg + 200 <= player.nPoint.getHpMpLimit()) {
                                                player.nPoint.increasePoint((byte) 1, (short) 10);
                                                Service.gI().sendThongBao(player, "|8|Auto cộng KI!\n|2|" + player.nPoint.mpg);
                                            } else if (player.nPoint != null && player.nPoint.mpg + 20 <= player.nPoint.getHpMpLimit()) {
                                                player.nPoint.increasePoint((byte) 1, (short) 1);
                                                Service.gI().sendThongBao(player, "|8|Auto cộng KI!\n|2|" + player.nPoint.mpg);
                                            } else {
                                                player.autoKI = false;
                                                Service.gI().sendThongBaoFromAdmin(player, "|8|Auto cộng KI đã dừng!\n|2|Bạn đã đạt giới hạn sức mạnh");
                                                break;
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                                break;
                            case 2:
                                player.autoSD = !player.autoSD;
                                Service.gI().sendThongBao(player, "|1|Đã " + (player.autoSD == true ? "Bật" : "Tắt") + " Auto cộng chỉ số KI");
                                try {
                                    while (player.autoSD == true) {
                                        if (player.nPoint != null && player.nPoint.dameg + 1000 <= player.nPoint.getDameLimit()) {
                                            player.nPoint.increasePoint((byte) 2, (short) 1000);
                                            Service.gI().sendThongBao(player, "|8|Auto cộng SĐ!\n|2|" + player.nPoint.dameg);
                                        } else if (player.nPoint != null && player.nPoint.dameg + 100 <= player.nPoint.getDameLimit()) {
                                            player.nPoint.increasePoint((byte) 2, (short) 100);
                                            Service.gI().sendThongBao(player, "|8|Auto cộng SĐ!\n|2|" + player.nPoint.dameg);
                                        } else if (player.nPoint != null && player.nPoint.dameg + 10 <= player.nPoint.getDameLimit()) {
                                            player.nPoint.increasePoint((byte) 2, (short) 10);
                                            Service.gI().sendThongBao(player, "|8|Auto cộng SĐ!\n|2|" + player.nPoint.dameg);
                                        } else if (player.nPoint != null && player.nPoint.dameg + 1 <= player.nPoint.getDameLimit()) {
                                            player.nPoint.increasePoint((byte) 2, (short) 1);
                                            Service.gI().sendThongBao(player, "|8|Auto cộng SĐ!\n|2|" + player.nPoint.dameg);
                                        } else {
                                            player.autoSD = false;
                                            Service.gI().sendThongBaoFromAdmin(player, "|8|Auto cộng SĐ đã dừng!\n|2|Bạn đã đạt giới hạn sức mạnh");
                                            break;
                                        }
                                    }
                                } catch (Exception e) {
                                }
                                break;
                            case 3:
                                player.autoGiap = !player.autoGiap;
                                Service.gI().sendThongBao(player, "|1|Đã " + (player.autoGiap == true ? "Bật" : "Tắt") + " Auto cộng chỉ số DEF");
                                try {
                                    while (player.autoGiap == true) {
                                        if (player.nPoint != null && player.nPoint.defg + 100 <= player.nPoint.getDefLimit()) {
                                            player.nPoint.increasePoint((byte) 3, (short) 100);
                                            Service.gI().sendThongBao(player, "|8|Auto cộng DEF!\n|2|" + player.nPoint.defg);
                                        } else if (player.nPoint != null && player.nPoint.defg + 10 <= player.nPoint.getDefLimit()) {
                                            player.nPoint.increasePoint((byte) 3, (short) 10);
                                            Service.gI().sendThongBao(player, "|8|Auto cộng DEF!\n|2|" + player.nPoint.defg);
                                        } else if (player.nPoint != null && player.nPoint.defg + 1 <= player.nPoint.getDefLimit()) {
                                            player.nPoint.increasePoint((byte) 3, (short) 1);
                                            Service.gI().sendThongBao(player, "|8|Auto cộng DEF!\n|2|" + player.nPoint.defg);
                                        } else {
                                            player.autoGiap = false;
                                            Service.gI().sendThongBaoFromAdmin(player, "|8|Auto cộng DEF đã dừng!\n|2|Bạn đã đạt giới hạn sức mạnh");
                                            break;
                                        }
                                    }
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;
                    case ConstNpc.MENU_CALL:
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, ConstNpc.CALL_BOSS,
                                        "Chọn Boss?", "Full Cụm\nANDROID", "BLACK", "BROLY", "Cụm\nCell",
                                        "Cụm\nDoanh trại", "DOREMON", "FIDE", "FIDE\nBlack", "Cụm\nGINYU", "Cụm\nNAPPA", "Gắp Thú");
                                break;

                        }
                        break;
                    case ConstNpc.CALL_BOSS:
                        switch (select) {
                            case 0:
                                BossManager.gI().createBoss(BossID.ANDROID_13);
                                BossManager.gI().createBoss(BossID.ANDROID_14);
                                BossManager.gI().createBoss(BossID.ANDROID_15);
                                BossManager.gI().createBoss(BossID.ANDROID_19);
                                BossManager.gI().createBoss(BossID.DR_KORE);
                                BossManager.gI().createBoss(BossID.KING_KONG);
                                BossManager.gI().createBoss(BossID.PIC);
                                BossManager.gI().createBoss(BossID.POC);
                                break;
                            case 1:
                                BossManager.gI().createBoss(BossID.BLACK);
                                break;
                            case 2:
                                BossManager.gI().createBoss(BossID.BROLY);
                                break;
                            case 3:
                                BossManager.gI().createBoss(BossID.SIEU_BO_HUNG);
                                BossManager.gI().createBoss(BossID.XEN_BO_HUNG);
//                                BossManager.gI().createBoss(BossID.XEN_CON);
                                break;
                            case 4:
                                Service.getInstance().sendThongBao(player, "Không có boss");
                                break;
                            case 5:
                                BossManager.gI().createBoss(BossID.CHAIEN);
                                BossManager.gI().createBoss(BossID.XEKO);
                                BossManager.gI().createBoss(BossID.XUKA);
                                BossManager.gI().createBoss(BossID.NOBITA);
                                BossManager.gI().createBoss(BossID.DORAEMON);
                                break;
                            case 6:
                                BossManager.gI().createBoss(BossID.FIDE);
                                break;
                            case 7:
                                BossManager.gI().createBoss(BossID.FIDE_ROBOT);
                                BossManager.gI().createBoss(BossID.VUA_COLD);
                                break;
                            case 8:
                                BossManager.gI().createBoss(BossID.TDST);
                                break;
                            case 9:
                                BossManager.gI().createBoss(BossID.KUKU);
                                BossManager.gI().createBoss(BossID.MAP_DAU_DINH);
                                BossManager.gI().createBoss(BossID.RAMBO);
                                break;
                        }
                        break;
                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:
                                if (player.isAdmin2()) {
                                    System.out.println(player.name);
                                    Maintenance.gI().start(20);
                                    System.out.println(player.name);
                                }
                                break;
                            case 1:
                                Input.gI().buffvnd(player);
                                break;
                            case 2:
                                Input.gI().createFormGiveItem(player);
                                break;
                            case 3:
                                Input.gI().createFormSenditem1(player);
                                break;
                            case 4:
                                Input.gI().createFormGiveIte2m(player);
                                break;
                            case 5:
                                Input.gI().createFormEXP(player);
                                break;
                            case 6:
//                                if (player.isAdmin2()) {
//                                try {
//                                GirlkunResultSet rs = GirlkunDB.executeQuery("SELECT * FROM a_server_sukien WHERE id_event > 0");
//                                String text = "|7|[ - Event Manager - ]\n";
//                                    while (rs.next()) {
//                                        String name = rs.getString("name_event");
//                                        int id = rs.getInt("id_event");
//                                        text += "|6|Sự kiện: " + name + "(" + id +")\n";
//                                    }
//                                    NpcService.gI().createMenuConMeo(player, ConstNpc.THAYEVENT, 12639,
//                                        text + "|2|Sự kiện hiện tại : "
//                                        + ConstEvent.gI().getNameEv(),
//                                        "NHẬP ID");
//                                rs.dispose();
//                                } catch (Exception ex) {
//                                java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
//                                }}
                                break;
                            case 7:
//                                TaiXiu_Old.gI().baotri = !TaiXiu_Old.gI().baotri;
//                                Service.gI().sendThongBao(player, "Đã " + (TaiXiu_Old.gI().baotri == true ? "Tắt" : "Bật") + " Tài Xỉu!");
                                break;
                            case 8:
                               // ChonAiDay.gI().baotri = !ChonAiDay.gI().baotri;
                              //  Service.gI().sendThongBao(player, "Đã " + (ChonAiDay.gI().baotri == true ? "Tắt" : "Bật") + " Lý Tiểu Nương!");
                                break;
                            case 9:
                                Input.gI().TNPET(player);
                                break;
                            case 10:
                                Input.gI().thayTileNap(player);
                                break;
                            /*try {
                    if(BossManager.gI().bosses.isEmpty()){
                    Service.gI().sendThongBaoFromAdmin(player, "|7|YÊU CẦU THẤT BẠI");
                    break;
                    }
                    int DemNguoc = 10;
                    while (DemNguoc > 0) {
                    DemNguoc--;
                    Thread.sleep(1000);
                    Service.gI().sendThongBao(player, "Xóa List Boss Sau "+DemNguoc+" Giây");
                    }
                    BossManager.gI().bosses.remove(BossManager.gI().bosses.size());
                    Service.gI().sendThongBaoFromAdmin(player, "|7|XÓA LIST BOSS THÀNH CÔNG!");
                    break;
                    } catch (InterruptedException e) {
                    Service.gI().sendThongBao(player, "Reset Lỗi");
                    }
                    break;
                    case 8:
                    try {
                    if(BossManager.gI().bosses.isEmpty()){
                    int DemNguoc = 10;
                    while (DemNguoc > 0) {
                    DemNguoc--;
                    Thread.sleep(1000);
                    Service.gI().sendThongBao(player, "Tạo List Boss Sau "+DemNguoc+" Giây");
                    }
                    BossManager.gI().createBoss(BossID.ANDROID_14);
                    BossManager.gI().createBoss(BossID.ALONG);
                    BossManager.gI().createBoss(BossID.SUPER_ANDROID_17);
                    BossManager.gI().createBoss(BossID.AN_TROM);
                    BossManager.gI().createBoss(BossID.KAMIOREN);
                    BossManager.gI().createBoss(BossID.SUPER_XEN);
                    BossManager.gI().createBoss(BossID.TDST);
                    BossManager.gI().createBoss(BossID.PIC);
                    BossManager.gI().createBoss(BossID.POC);
                    BossManager.gI().createBoss(BossID.KING_KONG);
                    BossManager.gI().createBoss(BossID.SONGOKU_TA_AC);
                    BossManager.gI().createBoss(BossID.CUMBER);
                    BossManager.gI().createBoss(BossID.COOLER);
                    BossManager.gI().createBoss(BossID.COOLER_GOLD);
                    BossManager.gI().createBoss(BossID.XEN_BO_HUNG);
                    BossManager.gI().createBoss(BossID.SIEU_BO_HUNG);
                    BossManager.gI().createBoss(BossID.XEN_CON_1);
                    BossManager.gI().createBoss(BossID.XEN_CON_1);
                    BossManager.gI().createBoss(BossID.XEN_CON_1);
                    BossManager.gI().createBoss(BossID.XEN_CON_1);
                    BossManager.gI().createBoss(BossID.THIEN_SU_VADOS);
                    BossManager.gI().createBoss(BossID.THIEN_SU_WHIS);
                    BossManager.gI().createBoss(BossID.THIEN_SU_VADOS);
                    BossManager.gI().createBoss(BossID.THIEN_SU_WHIS);
                    BossManager.gI().createBoss(BossID.THIEN_SU_VADOS);
                    BossManager.gI().createBoss(BossID.THIEN_SU_WHIS);
                    BossManager.gI().createBoss(BossID.THIEN_SU_VADOS);
                    BossManager.gI().createBoss(BossID.THIEN_SU_WHIS);
                    BossManager.gI().createBoss(BossID.DORAEMON);
                    BossManager.gI().createBoss(BossID.NOBITA);
                    BossManager.gI().createBoss(BossID.XUKA);
                    BossManager.gI().createBoss(BossID.CHAIEN);
                    BossManager.gI().createBoss(BossID.XEKO);
                    BossManager.gI().createBoss(BossID.BLACK);
                    BossManager.gI().createBoss(BossID.ZAMASZIN);
                    BossManager.gI().createBoss(BossID.BLACK2);
                    BossManager.gI().createBoss(BossID.ZAMASMAX);
                    BossManager.gI().createBoss(BossID.BLACK);
                    BossManager.gI().createBoss(BossID.BLACK3);
                    BossManager.gI().createBoss(BossID.KUKU);
                    BossManager.gI().createBoss(BossID.MAP_DAU_DINH);
                    BossManager.gI().createBoss(BossID.RAMBO);
                    BossManager.gI().createBoss(BossID.FIDE);
                    BossManager.gI().createBoss(BossID.DR_KORE);
                    BossManager.gI().createBoss(BossID.SON_TINH);
                    BossManager.gI().createBoss(BossID.THUY_TINH);
                    BossManager.gI().createBoss(BossID.ELEC);
                    BossManager.gI().createBoss(BossID.OIL);
                    BossManager.gI().createBoss(BossID.MACKI);
                    BossManager.gI().createBoss(BossID.GAS);
                    BossManager.gI().createBoss(BossID.TAPSU4);
                    BossManager.gI().createBoss(BossID.TAPSU4);
                    BossManager.gI().createBoss(BossID.TAPSU4);
                    for (int i = 1; i <= 3; i++) {
                    BossManager.gI().createBoss(BossID.BROLY_THUONG + i);
                    }
                    BossManager.gI().loadBoss();
                    Manager.MAPS.forEach(com.girlkun.models.map.Map::initBoss);
                    Service.gI().sendThongBaoFromAdmin(player, "|7|RESET LIST BOSS THÀNH CÔNG!");
                    break;
                    }
                    Service.gI().sendThongBaoFromAdmin(player, "|5|YÊU CẦU XÓA ALL LIST BOSS TRƯỚC!");
                    break;
                    } catch (InterruptedException e) {
                    Service.gI().sendThongBao(player, "Reset Lỗi");
                    }*/ /*try {
                            if(BossManager.gI().bosses.isEmpty()){
                            Service.gI().sendThongBaoFromAdmin(player, "|7|YÊU CẦU THẤT BẠI");
                            break;
                            }
                            int DemNguoc = 10;
                            while (DemNguoc > 0) {
                            DemNguoc--;
                            Thread.sleep(1000);
                            Service.gI().sendThongBao(player, "Xóa List Boss Sau "+DemNguoc+" Giây");
                            }
                            BossManager.gI().bosses.remove(BossManager.gI().bosses.size());
                            Service.gI().sendThongBaoFromAdmin(player, "|7|XÓA LIST BOSS THÀNH CÔNG!");
                            break;
                            } catch (InterruptedException e) {
                            Service.gI().sendThongBao(player, "Reset Lỗi");
                            }
                            break;

                            case 8:
                            try {
                            if(BossManager.gI().bosses.isEmpty()){
                            int DemNguoc = 10;
                            while (DemNguoc > 0) {
                            DemNguoc--;
                            Thread.sleep(1000);
                            Service.gI().sendThongBao(player, "Tạo List Boss Sau "+DemNguoc+" Giây");
                            }
                            BossManager.gI().createBoss(BossID.ANDROID_14);
                            BossManager.gI().createBoss(BossID.ALONG);
                            BossManager.gI().createBoss(BossID.SUPER_ANDROID_17);
                            BossManager.gI().createBoss(BossID.AN_TROM);
                            BossManager.gI().createBoss(BossID.KAMIOREN);
                            BossManager.gI().createBoss(BossID.SUPER_XEN);
                            BossManager.gI().createBoss(BossID.TDST);
                            BossManager.gI().createBoss(BossID.PIC);
                            BossManager.gI().createBoss(BossID.POC);
                            BossManager.gI().createBoss(BossID.KING_KONG);
                            BossManager.gI().createBoss(BossID.SONGOKU_TA_AC);
                            BossManager.gI().createBoss(BossID.CUMBER);
                            BossManager.gI().createBoss(BossID.COOLER);
                            BossManager.gI().createBoss(BossID.COOLER_GOLD);
                            BossManager.gI().createBoss(BossID.XEN_BO_HUNG);
                            BossManager.gI().createBoss(BossID.SIEU_BO_HUNG);
                            BossManager.gI().createBoss(BossID.XEN_CON_1);
                            BossManager.gI().createBoss(BossID.XEN_CON_1);
                            BossManager.gI().createBoss(BossID.XEN_CON_1);
                            BossManager.gI().createBoss(BossID.XEN_CON_1);
                            BossManager.gI().createBoss(BossID.THIEN_SU_VADOS);
                            BossManager.gI().createBoss(BossID.THIEN_SU_WHIS);
                            BossManager.gI().createBoss(BossID.THIEN_SU_VADOS);
                            BossManager.gI().createBoss(BossID.THIEN_SU_WHIS);
                            BossManager.gI().createBoss(BossID.THIEN_SU_VADOS);
                            BossManager.gI().createBoss(BossID.THIEN_SU_WHIS);
                            BossManager.gI().createBoss(BossID.THIEN_SU_VADOS);
                            BossManager.gI().createBoss(BossID.THIEN_SU_WHIS);
                            BossManager.gI().createBoss(BossID.DORAEMON);
                            BossManager.gI().createBoss(BossID.NOBITA);
                            BossManager.gI().createBoss(BossID.XUKA);
                            BossManager.gI().createBoss(BossID.CHAIEN);
                            BossManager.gI().createBoss(BossID.XEKO);
                            BossManager.gI().createBoss(BossID.BLACK);
                            BossManager.gI().createBoss(BossID.ZAMASZIN);
                            BossManager.gI().createBoss(BossID.BLACK2);
                            BossManager.gI().createBoss(BossID.ZAMASMAX);
                            BossManager.gI().createBoss(BossID.BLACK);
                            BossManager.gI().createBoss(BossID.BLACK3);
                            BossManager.gI().createBoss(BossID.KUKU);
                            BossManager.gI().createBoss(BossID.MAP_DAU_DINH);
                            BossManager.gI().createBoss(BossID.RAMBO);
                            BossManager.gI().createBoss(BossID.FIDE);
                            BossManager.gI().createBoss(BossID.DR_KORE);
                            BossManager.gI().createBoss(BossID.SON_TINH);
                            BossManager.gI().createBoss(BossID.THUY_TINH);
                            BossManager.gI().createBoss(BossID.ELEC);
                            BossManager.gI().createBoss(BossID.OIL);
                            BossManager.gI().createBoss(BossID.MACKI);
                            BossManager.gI().createBoss(BossID.GAS);
                            BossManager.gI().createBoss(BossID.TAPSU4);
                            BossManager.gI().createBoss(BossID.TAPSU4);
                            BossManager.gI().createBoss(BossID.TAPSU4);
                            for (int i = 1; i <= 3; i++) {
                            BossManager.gI().createBoss(BossID.BROLY_THUONG + i);
                            }
                            BossManager.gI().loadBoss();
                            Manager.MAPS.forEach(com.girlkun.models.map.Map::initBoss);
                            Service.gI().sendThongBaoFromAdmin(player, "|7|RESET LIST BOSS THÀNH CÔNG!");
                            break;
                            }
                            Service.gI().sendThongBaoFromAdmin(player, "|5|YÊU CẦU XÓA ALL LIST BOSS TRƯỚC!");
                            break;
                            } catch (InterruptedException e) {
                            Service.gI().sendThongBao(player, "Reset Lỗi");
                            }*/
                        }
                        break;
//                    case ConstNpc.THAYEVENT:
//                    switch (select) {
//                        case 0:
//                            Input.gI().thayevent(player);
//                        break;
//                       }
//                    break;
                    case ConstNpc.MAUTEN:
                        if (player.name.startsWith("%")) {
                            player.name = player.name.substring(2);
                        }
                        switch (select) {
                            case 0:
                                player.name = "%1" + player.name;
                                break;
                            case 1:
                                player.name = "%2" + player.name;
                                break;
                            case 2:
                                player.name = player.name;
                                break;
                            case 3:
                                player.name = "%4" + player.name;
                                break;
                            case 4:
                                player.name = "%5" + player.name;
                                break;
                            case 5:
                                player.name = "%6" + player.name;
                                break;
                            case 6:
                                player.name = "%7" + player.name;
                                break;
                        }
                        Service.gI().player(player);
                        Service.gI().Send_Caitrang(player);
                        break;
                 case ConstNpc.MOD:
                        switch (select) {
                            case 0:
                              Input.gI().createFormFindPlayer(player);
                                break;
                            
                            case 1:
                            try {
                                GiftcodeManager.gI().checkInfomationGiftCode(player);
                            } catch (Exception ex) {
                            }
                            break;
                            case 2:
                                Input.gI().ChatAll(player);
                                break;
                                
                        }
                        break;
                    case ConstNpc.QUANTRI1:
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || (player.isAdmin2())) {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.MOD, 12639, "|7|[ - Menu Key - ]" + "\n"
                                            + "|2|Số Người Chơi Đang Online : " + Client.gI().getPlayers().size()
                                            + "\nServer Run Time : " + ServerManager.timeStart
                                            + "\n|1|Thread : " + Thread.activeCount() + ", Session : " + GirlkunSessionManager.gI().getSessions().size()
                                            + "\n|7|[KhanhDTK]",
                                            "Check\nPlayer", "Check\nGiftcode", "Chat all","Đóng");
                                }
                                break;
                            case 1:
                                if (player.isAdmin()) {
                                    Service.gI().sendThongBao(player, "Bạn không phải Admin Cấp Cao");
                                    break;
                                } else {
                                    if (player.isAdmin2()) {
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_ADMIN, 12639, "|7|[ - Menu Quản Trị - ]" + "\n"
                                                + "|2|Số Người Chơi Đang Online : " + Client.gI().getPlayers().size()
                                                + "\nServer Run Time : " + ServerManager.timeStart
                                                + "\n|1|Thread : " + Thread.activeCount() + ", Session : " + GirlkunSessionManager.gI().getSessions().size()
                                                + "\n|7|KhanhDTK",
                                                "BẢO TRÌ", "BUFF VNĐ", "BUFF ITEM", "BUFF\nCHUỖI OPT", "BUFF\nCHUỖI ITEM", "EXP SERVER\n(EXP: X" + Manager.RATE_EXP_SERVER + ")",
                                                "SỰ KIỆN\n" + (ConstEvent.EVENT == 1 ? "(MÙA HÈ)" : ConstEvent.EVENT == 2 ? "(H.VƯƠNG)" : ConstEvent.EVENT == 3 ? "(TRUNG THU)" : ConstEvent.EVENT == 4 ? "(HLWEEN)"
                                                                                : ConstEvent.EVENT == 5 ? "(20/11)" : ConstEvent.EVENT == 6 ? "(NOEL)" : ConstEvent.EVENT == 7 ? "(TẾT)" : ConstEvent.EVENT == 8 ? "(8/3)" : "(ĐÓNG)"),
                                          //      "TÀI XỈU\n" + (TaiXiu_Old.gI().baotri == true ? "(OFF)" : "(ON)"), "CHẴN LẺ\n" + (ChonAiDay.gI().baotri == true ? "(OFF)" : "(ON)"),
                                                "EXP PET\nX" + Manager.TNPET + "\n(" + Util.format(Manager.TNPET * 20) + "p/s)", "TỈ LỆ NẠP\nTV : X" + Manager.TLDOITV + "\nHN : X" + Util.format(Manager.TLDOIHN));
                                    }
                                }
                                break;
                            case 2:
                                Input.gI().ChatAll(player);
                                break;
                            case 4:
                                Service.gI().menuAd(player);
                                break;
                            case 3:
                                if (player.typePk == ConstPlayer.NON_PK) {
                                    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_ALL);
                                    Service.gI().sendThongBao(player, "|7|Bật tàn sát");
                                } else {
                                    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
                                    Service.gI().sendThongBao(player, "|7|Tắt tàn sát");
                                }
                                break;
                        }
                        break;
                    case ConstNpc.menutd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().settaiyoken(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgenki(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setkamejoko(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;
                    case ConstNpc.menunm:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodki(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgoddam(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setsummon(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;
                    case ConstNpc.menuxd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodgalick(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setmonkey(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setgodhp(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;
                    //skh new
                    case ConstNpc.menutdtl:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().settaiyokentl(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgenkitl(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setkamejokotl(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menunmtl:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodkitl(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgoddamtl(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setsummontl(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menuxdtl:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodgalicktl(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setmonkeytl(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setgodhptl(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;
                    case ConstNpc.CONFIRM_DISSOLUTION_CLAN:
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                clan.deleteDB(clan.id);
                                Manager.CLANS.remove(clan);
                                player.clan = null;
                                player.clanMember = null;
                                ClanService.gI().sendMyClan(player);
                                ClanService.gI().sendClanId(player);
                                Service.gI().sendThongBao(player, "Đã giải tán bang hội.");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.gI().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
//                    case ConstNpc.MENU_FIND_PLAYER:
//                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
//                        if (p != null) {
//                            switch (select) {
//                                case 0:
//                                    if (p.zone != null) {
//                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x, p.location.y);
//                                    }
//                                    break;
//                                case 1:
//                                    if (p.zone != null) {
//                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x, player.location.y);
//                                    }
//                                    break;
//                                case 2:
//                                    Input.gI().createFormChangeName(player, p);
//                                    break;
//                                case 3:
//                                    String[] selects = new String[]{"Đồng ý", "Hủy"};
//                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, 12639,
//                                            "|7|[ BANED PLAYER ]\n" + "Bạn có chắc chắn muốn ban : " + p.name, selects, p);
//                                    break;
//                                case 4:
//                                    if (p.isAdmin2()) {
//                                        Service.gI().sendThongBao(player, "Không thể kick Admin Cấp Cao");
//                                        Service.gI().sendThongBaoOK(p, "Key [" + player.name + "] đang cố kick bạn");
//                                        break;
//                                    }
//                                    Service.gI().sendThongBao(player, "Kick người chơi " + p.name + " thành công");
//                                    Client.gI().getPlayers().remove(p);
//                                    Client.gI().kickSession(p.getSession());
//                                    break;
//                                case 7:
//                                    break;
//                            }
//                        }
//                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1502:
                        if (select == 0) {
                            IntrinsicService.gI().sattdtl(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnmtl(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxdtl(player);
                        }
                        break;
                    case ConstNpc.QUANLYTK:
                        Player plql = (Player) PLAYERID_OBJECT.get(player.id);
                        if (plql != null) {
                            switch (select) {
                                case 0:
                                    Input.gI().createFormChangeName(player, plql);
                                    break;
                                case 1:
                                    String[] selects = new String[]{"Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, 12639,
                                            "|7|[ BANDED PLAYER ]\n" + "Bạn có chắc chắn muốn ban : " + plql.name + "\n", selects, plql);
                                    break;
                                case 2:
                                    if (plql.isAdmin2()) {
                                        Service.gI().sendThongBao(player, "Không thể kick Admin Cấp Cao");
                                        Service.gI().sendThongBaoOK(plql, "Key [" + player.name + "] đang cố kick bạn");
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Kick người chơi " + plql.name + " thành công");
                                    Client.gI().getPlayers().remove(plql);
                                    Client.gI().kickSession(plql.getSession());
                                    break;
                                case 3:
                                    String[] selectss = new String[]{"Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.ACTIVE, 12639,
                                            "|7| [ ACTIVE PLAYER ]\n" + "Bạn có chắc chắn muốn Mở Thành Viên cho : " + plql.name, selectss, plql);
                                    break;
                                case 4:
                                    if (plql.pet == null) {
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.MODE_PET, 12639, "|7|Ngọc Rồng Kamui Service" + "\n"
                                                + "\n" + "|7|[ CHANGE ALL MODE FOR PLAYER PET ]\n" + "|1|Player : " + plql.name + "\n"
                                                + "(Player Chưa Có Đệ Tử)" + "\n"
                                                + "|7|Chọn tùy chọn cho : " + plql.name + "?",
                                                "CHANGE\nTYPE", "CHANGE\nGENDER", "CHANGE\nSKILL", "BUFF\nCHỈ SỐ", "GIẢM\nCHỈ SỐ");
                                        return;
                                    }
                                    if (plql.pet != null) {
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.MODE_PET, 12639, "|7|Ngọc Rồng Kamui Service" + "\n"
                                                + "\n" + "|7|[ CHANGE ALL FOR PLAYER PET ]\n" + "|1|Player : " + plql.name + "\n"
                                                + "Player Pet : (" + plql.pet.typePet + ") " + plql.pet.name.substring(1) + "\n"
                                                + "Tiềm Năng : " + plql.pet.nPoint.tiemNang + "\n"
                                                + "Sức Mạnh : " + plql.pet.nPoint.power + "\n"
                                                + "Dame : " + plql.pet.nPoint.dame + "\n"
                                                + "HP : " + plql.pet.nPoint.hp + "\n"
                                                + "MP : " + plql.pet.nPoint.mp + "\n"
                                                + "Crit : " + plql.pet.nPoint.crit + "\n"
                                                + "|7|Chọn tùy chọn cho : " + plql.name + "?",
                                                "CHANGE\nTYPE", "CHANGE\nGENDER", "CHANGE\nSKILL", "BUFF\nCHỈ SỐ", "GIẢM\nCHỈ SỐ");
                                    }
                                    break;
                                case 5:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BUFFDHIEU, 12639,
                                            "|7|- •⊹٭DragonBall Kamui٭⊹• -\n|2|TẶNG DANH HIỆU\nPlayer name : " + plql.name
                                            + "\n|5|Đại Thần : " + (plql.timedh1 > 0 ? Util.msToTime(plql.timedh1) : plql.timedh1 <= 0 ? "Chưa Sở Hữu" : plql.usedh1 == true ? "Đang Bật" : "Đang Tắt")
                                            + "\nTrùm Cuối : " + (plql.timedh2 > 0 ? Util.msToTime(plql.timedh2) : plql.timedh2 <= 0 ? "Chưa Sở Hữu" : plql.usedh2 == true ? "Đang Bật" : "Đang Tắt")
                                            + "\nBất Bại : " + (plql.timedh3 > 0 ? Util.msToTime(plql.timedh3) : plql.timedh3 <= 0 ? "Chưa Sở Hữu" : plql.usedh3 == true ? "Đang Bật" : "Đang Tắt")
                                            + "\nBuôn Ve Chai : " + (plql.dhtang1 == true ? "Đang Bật" : plql.titlett == false ? "Chưa Sở Hữu" : "Đang Tắt")
                                            + "\nFan Cứng : " + (plql.dhtang2 == true ? "Đang Bật" : plql.titlett == false ? "Chưa Sở Hữu" : "Đang Tắt"),
                                            "Đại Thần", "Trùm Cuối", "Bất Bại", "Ve Chai", "Fan Cứng", "Làm Mới");
                                    break;
                                case 6:
                                    String[] selectssss = new String[]{"TRỰC TIẾP\nID < 32", "NEXT MAIN\n" + "ID NV : " + plql.playerTask.taskMain.id, "NEXT SUB\n" + "ID SUB : " + plql.playerTask.taskMain.index};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.NEXTNV, 12639,
                                            "|7| [ NEXT TASK PLAYER ]\n" + "Bạn có chắc chắn muốn NEXT NHIỆM VỤ player : " + plql.name + " không?"
                                            + "\n" + "|1|TRỰC TIẾP : NEXT TRỰC TIẾP ĐẾN ID ĐÃ CHỌN"
                                            + "\n" + "NEXT MAIN : NEXT 1 NHIỆM VỤ CHÍNH TIẾP THEO"
                                            + "\n" + "NEXT SUB : NEXT 1 NHIỆM VỤ NHÁNH TIẾP THEO",
                                            selectssss, plql);
                                    break;
                                case 7:
//                                    String[] selectsss = new String[]{(plql.isJail() ? "MỞ GIAM" : "GIAM GIỮ"), "Đóng"};
//                                    NpcService.gI().createMenuConMeo(player, ConstNpc.JAIL, 12639,
//                                            "|7| [ PRISON ]\n" + "Player " + plql.name + " đang : [" + (plql.getSession().isJail ? "Ở Tù Xám Hối]" : "Chơi Vơi Giữa Cuộc Đời]"), selectsss, plql);
//                                    break;
                                case 8:
                                    if (player.isAdmin()) {
                                        Service.gI().sendThongBao(player, "Bạn không phải Admin Cấp Cao");
                                    } else {
                                        String[] selectsssss = new String[]{"CẤP QUYỀN", "HỦY QUYỀN"};
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.MAKEADMIN, 12639,
                                                "|7|NÂNG KEY TRỰC TIẾP CHO PLAYER : " + plql.name + "?", selectsssss, plql);
                                    }
                                    break;
                                case 9:
                                    Input.gI().thuitem(player);
                                    break;
                            }
                        }
                        break;
                     
                    case ConstNpc.QUANLYBOSS:
                        Boss b = BossManager.gI().getBossById(player.idboss);
                        switch (select) {
                            case 0:
                                Service.gI().sendThongBao(player, "Call Boss\n|7|(" + b.name + ")\nSuccess!");
                                BossManager.gI().createBoss((int) b.id);
                                break;
                            case 1:
                                if (player.idboss > 135) {
                                    b.die(null);
                                    b.changeStatus(BossStatus.LEAVE_MAP);
                                    ChangeMapService.gI().exitMap(b);
                                    Service.gI().sendThongBao(player, "Remove Boss\n|7|(" + b.name + ")\nSuccess!");
                                    BossManager.gI().bosses.remove(b);
                                } else {
                                    Service.gI().sendThongBao(player, "Remove Boss\n|7|(" + b.name + ")\nFail Default!");
                                }
                                break;
                            case 2:
                                if (b.zone != null) {
                                    b.die(null);
                                    b.changeStatus(BossStatus.DIE);
                                    Service.gI().sendThongBao(player, "Kill Boss\n|7|(" + b.name + ")\nSuccess!");
                                } else {
                                    Service.gI().sendThongBao(player, " Boss đã chết!");
                                }
                                break;
                            case 3:
                                if (b.typePk == 0) {
                                    b.active();
                                    b.changeStatus(BossStatus.ACTIVE);
                                    Service.gI().sendThongBao(player, "Active Boss\n|7|(" + b.name + ")\nSuccess!");
                                } else {
                                    b.changeToTypeNonPK();
                                    b.changeStatus(BossStatus.REST);
                                    Service.gI().sendThongBao(player, "reActive Boss\n|7|(" + b.name + ")\nSuccess!");
                                }
                                break;
                            case 4:
                                b.changeStatus(BossStatus.REST);
                                b.changeStatus(BossStatus.JOIN_MAP);
                                Service.gI().sendThongBao(player, "Respawn Boss\n|7|(" + b.name + ")\nSuccess!");
                                break;
                        }
                        break;
                    case ConstNpc.MENU_EVENT:
                        switch (select) {
                            case 0:
                                Service.gI().sendThongBaoOK(player, "Điểm sự kiện: " + player.inventory.event + " ngon ngon...");
                                break;
                            case 1:
                                Service.gI().showListTop(player, Manager.topSK);
                                break;
                            case 2:
                                Service.gI().sendThongBao(player, "Sự kiện đã kết thúc...");
//                                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_GIAO_BONG, -1, "Người muốn giao bao nhiêu bông...",
//                                        "100 bông", "1000 bông", "10000 bông");
                                break;
                            case 3:
                                Service.gI().sendThongBao(player, "Sự kiện đã kết thúc...");
//                                NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN, -1, "Con có thực sự muốn đổi thưởng?\nPhải giao cho ta 3000 điểm sự kiện đấy... ",
//                                        "Đồng ý", "Từ chối");
                                break;

                        }
                        break;
                    case ConstNpc.MENU_GIAO_BONG:
                        ItemService.gI().giaobong(player, (int) Util.tinhLuyThua(10, select + 2));
                        break;
                    case ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN:
                        if (select == 0) {
                            ItemService.gI().openBoxVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_DOI_DIEM_DUA:
                        if (select == 0) {
                            ItemService.gI().openBoxCongThuc(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_DOI_DIEM_ITEMC2:
                        if (select == 0) {
                            ItemService.gI().openBoxitemc2(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_DOI_ITEM_NR:
                        if (select == 0) {
                            ItemService.gI().openBoxitemnr(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_DOI_DIEM_CT:
                        if (select == 0) {
                            ItemService.gI().openBoxCt(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_TELE_NAMEC:
                        if (select == 0) {
//                            NgocRongNamecService.gI().teleportToNrNamec(player);
                            player.inventory.subGemAndRuby(50);
                            Service.gI().sendMoney(player);
                        }
                        break;
                }
            }
        };
    }

}
