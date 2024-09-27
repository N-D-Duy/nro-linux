package ServerData.Services.Func;

import ServerData.Services.ChangeMapService;
import ServerData.Services.InventoryServiceNew;
import ServerData.Services.Service;
import ServerData.Services.ItemService;
import ServerData.Services.RewardService;
import Server.Data.Consts.ConstNpc;
import ServerData.Models.Item.Item;
import ServerData.Models.Item.Item.ItemOption;
import ServerData.Models.NPC.Npc;
import ServerData.Models.NPC.NpcManager;
import ServerData.Models.Player.Player;
import ServerData.Server.Manager;
import ServerData.Server.ServerNotify;
import com.girlkun.network.io.Message;
import ServerData.Utils.Util;
import java.io.IOException;

import java.util.*;
import java.util.stream.Collectors;

public class CombineServiceNew {

    private static final int COST_DOI_VE_DOI_DO_HUY_DIET = 500000000;
    private static final int COST_DAP_DO_KICH_HOAT = 500000000;
    private static final int COST_DOI_MANH_KICH_HOAT = 500000000;

    private static final int COST = 500000000;

    private static final byte MAX_STAR_ITEM = 7;
    private static final byte MAX_STAR_ITEM_VIP = 8;
    private static final byte MAX_LEVEL_ITEM = 8;

    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte COMBINE_SUCCESS = 2;
    private static final byte COMBINE_FAIL = 3;
    private static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;
    public static final int NANG_SKH_LVL1 = 12312;
    public static final int NANG_SKH_LVL2 = 12313;
    public static final int NANG_SKH_TL_LVL0 = 12314;
    public static final int NANG_SKH_TL_LVL1 = 12315;
    public static final int NANG_SKH_TL_LVL2 = 12316;
    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int PHA_LE_HOA_TRANG_BI_VIP = 3113;
    public static final int CHUYEN_HOA_TRANG_BI = 502;

    public static final int NANG_CAP_VAT_PHAM = 510;
    public static final int NANG_CAP_BONG_TAI_1_LEN_2 = 511;
    public static final int MO_CHI_SO_BONG_TAI_CAP2 = 519;
    public static final int NANG_CAP_BONG_TAI_2_LEN_3 = 517;
    public static final int MO_CHI_SO_BONG_TAI_CAP3 = 518;
    public static final int NANG_CAP_BONG_TAI_3_LEN_4 = 523;
    public static final int MO_CHI_SO_BONG_TAI_CAP4 = 524;
    public static final int NANG_CAP_BONG_TAI_CAP5 = 525;
    public static final int MO_CHI_SO_BONG_TAI_CAP5 = 526;
    public static final int NANG_CAP_LINH_THU = 512;
    public static final int NHAP_NGOC_RONG = 513;
    public static final int NHAP_DA_SKH = 1321;
    public static final int NHAP_SPL = 1314;
    public static final int LAM_PHEP_NHAP_DA = 524;
    public static final int PHAN_RA_DO_THAN_LINH = 514;
    public static final int PHAN_RA_DO_THAN_LINH_X3 = 145;
    public static final int PHAN_RA_DO_THAN_LINH_X5 = 456;
    public static final int NANG_CAP_DO_TS = 515;
    public static final int NANG_CAP_SKH_VIP = 516;
    public static final int NANG_CAP_SKH_THUONG = 527;
    public static final int CHE_TAO_TRANG_BI_TS = 520;
    public static final int LUYEN_HOA_CHIEN_LINH = 521;
    public static final int MO_GIOI_HAN_CHIEN_LINH = 522;
    public static final int THUE_DO = 701;
    public static final int DOI_DIEM = 702;
    public static final int PHAN_RA_DO_SKH_TL = 10011;
    private static final int GOLD_BONG_TAI = 500_000_000;
    private static final int GEM_BONG_TAI = 5_000;
    private static final int RATIO_BONG_TAI_1_LEN_2 = 50;
    private static final int RATIO_BONG_TAI_2_LEN_3 = 40;
    private static final int RATIO_BONG_TAI_3_LEN_4 = 30;
    private static final int RATIO_BONG_TAI_4_LEN_5 = 20;
    private static final int RATIO_NANG_CAP = 45;
    public static final int NANG_CAP_CHAN_MENH = 528;
    public static final int THUC_TINH_DT = 529;
    public static final int THUC_TINH_SP = 530;

    private final Npc baHatMit;
    private final Npc buhan;
    private final Npc bill;
    private final Npc npsthiensu64;

    private static CombineServiceNew i;

    public CombineServiceNew() {
        this.buhan = NpcManager.getNpc(ConstNpc.BuHan);
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.bill = NpcManager.getNpc(ConstNpc.BILL);
        this.npsthiensu64 = NpcManager.getNpc(ConstNpc.NPC_64);
    }

    public static CombineServiceNew gI() {
        if (i == null) {
            i = new CombineServiceNew();
        }
        return i;
    }

    /**
     * Mở tab đập đồ
     *
     * @param player player
     * @param type kiểu đập đồ
     */
    public void openTabCombine(Player player, int type) {
        player.combineNew.setTypeCombine(type);
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            if (player.iDMark.getNpcChose() != null) {
                msg.writer().writeShort(player.iDMark.getNpcChose().tempId);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    /**
     * Hiển thị thông tin đập đồ
     *
     * @param player player
     * @param index index
     */
    public void showInfoCombine(Player player, int[] index) {
        player.combineNew.clearItemCombine();
        if (index.length > 0) {
            for (int i = 0; i < index.length; i++) {
                player.combineNew.itemsCombine.add(player.inventory.itemsBag.get(index[i]));
            }
        }
        switch (player.combineNew.typeCombine) {
            case NANG_CAP_BONG_TAI_1_LEN_2:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 454) {
                            bongTai = item;
                        } else if (item.template.id == 933) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 9999) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI_1_LEN_2;

                        String npcSay = "|2|Bông tai Porata cấp 2" + "\n";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|2|Tỉ lệ thành công: 50%  \n"
                                + "|2|Cần 9999 Mảnh vỡ bông tai \n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|2|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng\n"
                                    + "|2|Cần " + player.combineNew.gemCombine + " hồng ngọc\n";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata và x9999 Mảnh vỡ bông tai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata và x9999 Mảnh vỡ bông tai", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI_CAP2:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 921) {
                            bongTai = item;
                        } else if (item.template.id == 934) {
                            manhHon = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 2, x99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 2, x99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI_2_LEN_3:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item mvbt = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 921) {
                            bongTai = item;
                        } else if (item.template.id == 1427) {
                            mvbt = item;
                        }
                    }
                    if (bongTai != null && mvbt != null && mvbt.quantity >= 9999) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI_2_LEN_3;

                        String npcSay = "|2|Bông tai Porata cấp 3" + "\n";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|2|Tỉ lệ thành công: 30%  \n"
                                + "|2|Cần 9999 Mảnh vỡ bông tai 2 \n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|2|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng\n"
                                    + "|2|Cần " + player.combineNew.gemCombine + " ngọc\n";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 2 và x9999 Mảnh vỡ bông tai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata 2 và x9999 Mảnh vỡ bông tai", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI_CAP3:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item thachPhu = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1165) {
                            bongTai = item;
                        } else if (item.template.id == 1429) {
                            thachPhu = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && thachPhu != null && thachPhu.quantity >= 999 && daXanhLam != null && thachPhu.quantity >= 99) {
                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI_2_LEN_3;
                        String npcSay = "Bông tai Porata cấp 3" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 3, 999 Mảnh Hồn và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "|2|Cần 1 Bông tai Porata cấp 3, 999 Mảnh Hồn và 1 Đá xanh lam", "Đóng");
                }
                break;

            case NANG_CAP_BONG_TAI_3_LEN_4:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item mvbt = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1165) {
                            bongTai = item;
                        } else if (item.template.id == 1428) {
                            mvbt = item;
                        }
                    }
                    if (bongTai != null && mvbt != null && mvbt.quantity >= 999) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI_3_LEN_4;

                        String npcSay = "|2|Bông tai Porata cấp 3" + "\n";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|2|Tỉ lệ thành công: 40%  \n"
                                + "|2|Cần 999 Mảnh vỡ bông tai 3 \n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|2|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng\n"
                                    + "|2|Cần " + player.combineNew.gemCombine + " ngọc\n";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 3 và 999 Mảnh vỡ bông tai cấp 4", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata 3 và 999 Mảnh vỡ bông tai 4", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI_CAP4:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item thachPhu = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1129) {
                            bongTai = item;
                        } else if (item.template.id == 1429) {
                            thachPhu = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && thachPhu != null && daXanhLam != null && thachPhu.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI_3_LEN_4;

                        String npcSay = "Bông tai Porata cấp 3" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 3, X99 Thạch Phù và 5 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 3, X99 Thạch Phù và 5 Đá xanh lam", "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI_CAP5:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item mvbt = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1129) {
                            bongTai = item;
                        } else if (item.template.id == 1424) {
                            mvbt = item;
                        }
                    }
                    if (bongTai != null && mvbt != null && mvbt.quantity >= 999) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI_4_LEN_5;

                        String npcSay = "Bông tai Porata cấp 4" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 4 và 999 Mảnh vỡ bông tai cấp 4", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata 4 và 999 Mảnh vỡ bông tai 4", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI_CAP5:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item thachPhu = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1423) {
                            bongTai = item;
                        } else if (item.template.id == 1425) {
                            thachPhu = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && thachPhu != null && daXanhLam != null && thachPhu.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Bông tai Porata cấp 4" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 5, X99 Thạch Phù và 10 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 5, X99 Thạch Phù và 10 Đá xanh lam", "Đóng");
                }
                break;

            case CHE_TAO_TRANG_BI_TS:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Méo có đồ sao mà nâng", "Yes");
                    return;
                }
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 5) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).count() < 1) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Công thức Vip", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).count() < 1) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Mảnh đồ thiên sứ", "Đóng");
                        return;
                    }
//                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá nâng cấp", "Đóng");
//                        return;
//                    }
//                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá may mắn", "Đóng");
//                        return;
//                    }
                    Item mTS = null, daNC = null, daMM = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.isManhTS()) {
                                mTS = item;
                            } else if (item.isDaNangCap()) {
                                daNC = item;
                            } else if (item.isDaMayMan()) {
                                daMM = item;
                            }
                        }
                    }
                    int tilemacdinh = 35;
                    int tilenew = tilemacdinh;
//                    if (daNC != null) {
//                        tilenew += (daNC.template.id - 1073) * 10;                     
//                    }

                    String npcSay = "|2|Chế tạo " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get().typeNameManh() + " Thiên sứ "
                            + player.combineNew.itemsCombine.stream().filter(Item::isCongThucVip).findFirst().get().typeHanhTinh() + "\n"
                            + "|7|Mảnh ghép " + mTS.quantity + "/999\n";
//                            + "|2|Đá nâng cấp " + player.combineNew.itemsCombine.stream().filter(Item::isDaNangCap).findFirst().get().typeDanangcap()
//                            + " (+" + (daNC.template.id - 1073) + "0% tỉ lệ thành công)\n"
//                            + "|2|Đá may mắn " + player.combineNew.itemsCombine.stream().filter(Item::isDaMayMan).findFirst().get().typeDaMayman()
//                            + " (+" + (daMM.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)\n"
//                            + "|2|Tỉ lệ thành công: " + tilenew + "%\n"
//                            + "|7|Phí nâng cấp: 500 triệu vàng";

                    if (daNC != null) {

                        npcSay += "|2|Đá nâng cấp " + player.combineNew.itemsCombine.stream().filter(Item::isDaNangCap).findFirst().get().typeDanangcap()
                                + " (+" + (daNC.template.id - 1073) + "0% tỉ lệ thành công)\n";
                    }
                    if (daMM != null) {
                        npcSay += "|2|Đá may mắn " + player.combineNew.itemsCombine.stream().filter(Item::isDaMayMan).findFirst().get().typeDaMayman()
                                + " (+" + (daMM.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)\n";
                    }
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;
                        npcSay += "|2|Tỉ lệ thành công: " + tilenew + "%\n";
                    } else {
                        npcSay += "|2|Tỉ lệ thành công: " + tilemacdinh + "%\n";
                    }
                    npcSay += "|7|Phí nâng cấp: 500 triệu vàng";
                    if (player.inventory.gold < 500000000) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ vàng", "Đóng");
                        return;
                    }
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.MENU_DAP_DO,
                            npcSay, "Nâng cấp\n500 Tr vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ nguyên liệu, mời quay lại sau", "Đóng");
                }
                break;
            case NANG_CAP_LINH_THU:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item linhthu = null;
                    Item thangtinhthach = null;
                    Item thucan = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id >= 2019 && item.template.id <= 2026) {
                            linhthu = item;
                        } else if (item.template.id == 2030) {
                            thangtinhthach = item;
                        } else if (item.template.id >= 663 && item.template.id <= 667) {
                            thucan = item;
                        }
                    }
                    if (linhthu != null && thangtinhthach != null && thucan != null && thangtinhthach.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Linh Thú Siêu Cấp" + "\n|2|";
                        for (Item.ItemOption io : linhthu.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            if (player.combineNew.gemCombine <= player.inventory.gem) {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_LINH_THU, npcSay,
                                        "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                            } else {

                                npcSay += "\n Còn thiếu " + Util.numberToMoney(player.combineNew.gemCombine - player.inventory.gem) + " Ngọc";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";

                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }

                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Linh Thú, X99 Đá Ma Thuât và 1 Thức Ăn", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Linh Thú, X99 Đá Ma Thuât và 1 Thức Ăn", "Đóng");
                }
                break;
            case EP_SAO_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item trangBi = null;
                    Item daPhaLe = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (isTrangBiPhaLeHoa(item)) {
                            trangBi = item;
                        } else if (isDaPhaLe(item)) {
                            daPhaLe = item;
                        }
                    }
                    int star = 0; //sao pha lê đã ép
                    int starEmpty = 0; //lỗ sao pha lê
                    if (trangBi != null && daPhaLe != null) {
                        for (Item.ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 102) {
                                star = io.param;
                            } else if (io.optionTemplate.id == 107) {
                                starEmpty = io.param;
                            }
                        }
                        if (star < starEmpty) {
                            player.combineNew.gemCombine = getGemEpSao(star);
                            String npcSay = trangBi.template.name + "\n|2|";
                            for (Item.ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (daPhaLe.template.type == 30) {
                                for (Item.ItemOption io : daPhaLe.itemOptions) {
                                    npcSay += "|7|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name.replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                                }
                            } else {
                                npcSay += "|7|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name.replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                            }
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " ngọc";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                }
                break;
            case PHA_LE_HOA_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        int star = 0;
                        int param = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                            player.combineNew.gemCombine = getGemPhaLeHoa(star);
                            String npcSay = item.template.name + "\n|2|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|7|Tỉ lệ thành công : " + CombineServiceNew.gI().getRatioPhaLeHoa(star) + "% " + "\n";
                            for (Item.ItemOption ts : item.itemOptions) {
                                if (ts.optionTemplate.id == 238) {
                                    param = ts.param;
                                }
                            }
                            player.combineNew.ratioCombine = CombineServiceNew.gI().getRatioPhaLeHoa(star) + CombineServiceNew.gI().Tilecongdon(param);
                            npcSay += "|7|(Bonus " + CombineServiceNew.gI().Tilecongdon(param) + "%)" + "\n";
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\nVIP X1\n" + player.combineNew.gemCombine + " Ngọc",
                                        "Nâng cấp\nVIP X10\n" + player.combineNew.gemCombine * 10 + " Ngọc",
                                        "Nâng cấp\nVIP X100\n" + player.combineNew.gemCombine * 100 + " Ngọc");
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                                break;
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa", "Đóng");
                }
                break;
            case PHA_LE_HOA_TRANG_BI_VIP:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    Item item1 = player.combineNew.itemsCombine.get(1);
                    Item item2 = player.combineNew.itemsCombine.get(2);
                    if (isTrangBiPhaLeHoa(item) && isDuiDuc(item1) && isDaHomet(item2)) {
                        if (isTrangBiPhaLeHoa(item)) {
                            int star = 7;
                            int param = 0;
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id == 107) {
                                    star = io.param;
                                    break;
                                }
                            }
                            if (star < MAX_STAR_ITEM_VIP) {
                                player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                                player.combineNew.gemCombine = getGemPhaLeHoa(star);
                                String npcSay = item.template.name + "\n|2|";
                                for (Item.ItemOption io : item.itemOptions) {
                                    if (io.optionTemplate.id != 102) {
                                        npcSay += io.getOptionString() + "\n";
                                    }
                                }
                                npcSay += "|7|Tỉ lệ thành công : " + "5% " + "\n";
                                for (Item.ItemOption ts : item.itemOptions) {
                                    if (ts.optionTemplate.id == 238) {
                                        param = ts.param;
                                    }
                                }
                                player.combineNew.ratioCombine = CombineServiceNew.gI().getRatioPhaLeHoa(star) + CombineServiceNew.gI().Tilecongdon(param);
                                npcSay += "|7|(Bonus " + CombineServiceNew.gI().Tilecongdon(param) + "%)" + "\n";
                                if (player.combineNew.goldCombine <= player.inventory.gold) {
                                    npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                    baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                            "Nâng cấp\n" + player.combineNew.gemCombine + " Ngọc");
                                } else {
                                    npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                                    break;
                                }
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang Bị không phù hợp", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa", "Đóng");
                }
                break;
            case NHAP_NGOC_RONG:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                            String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n"
                                    + "1 viên " + ItemService.gI().getTemplate((short) (item.template.id - 1)).name + "\n"
                                    + "|7|Cần 7 " + item.template.name;
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case NHAP_SPL:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem() && (item.template.id == 20) && item.quantity >= 7) {
                            String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n"
                                    + "1 viên " + ItemService.gI().getTemplate((short) (1413)).name + "\n"
                                    + "|7|Cần 7 " + item.template.name;
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                        } else if (item != null && item.isNotNullItem() && (item.template.id == 19) && item.quantity >= 7) {
                            String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n"
                                    + "1 viên " + ItemService.gI().getTemplate((short) (1414)).name + "\n"
                                    + "|7|Cần 7 " + item.template.name;
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                        } else if (item != null && item.isNotNullItem() && (item.template.id == 16) && item.quantity >= 7) {
                            String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n"
                                    + "1 viên " + ItemService.gI().getTemplate((short) (1412)).name + "\n"
                                    + "|7|Cần 7 " + item.template.name;
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                        } else if (item != null && item.isNotNullItem() && (item.template.id == 15) && item.quantity >= 7) {
                            String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n"
                                    + "1 viên " + ItemService.gI().getTemplate((short) (1416)).name + "\n"
                                    + "|7|Cần 7 " + item.template.name;
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                        } else if (item != null && item.isNotNullItem() && (item.template.id == 14) && item.quantity >= 7) {
                            String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n"
                                    + "1 viên " + ItemService.gI().getTemplate((short) (1415)).name + "\n"
                                    + "|7|Cần 7 " + item.template.name;
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần đủ viên ngọc rồng", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần đủ viên ngọc rồng", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case NHAP_DA_SKH:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem() && (item.template.id == 1465) && item.quantity >= 10) {
                            String npcSay = "|2|Con có muốn biến 10 " + item.template.name + " thành\n"
                                    + "1 viên " + ItemService.gI().getTemplate((short) (1466)).name + "\n"
                                    + "|7|Cần 10 " + item.template.name;
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 10 đá skh thường", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 10 đá skh thường", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case NANG_CAP_VAT_PHAM:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                        int level = 0;
                        for (Item.ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 47
                                        || io.optionTemplate.id == 6
                                        || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7
                                        || io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 22
                                        || io.optionTemplate.id == 23) {
                                    option = io.optionTemplate.name;
                                    param = io.param + (io.param * 10 / 100);
                                    break;
                                }
                            }
                            npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\n%s ĐBV", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng cấp\nX1\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC,
                                        "Nâng cấp\nX10\n" + Util.numberToMoney(player.combineNew.goldCombine * 10) + " vàng" + daNPC,
                                        "Nâng cấp\nX100\n" + Util.numberToMoney(player.combineNew.goldCombine * 100) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                }
                break;
            case DOI_DIEM:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.bill.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con hãy đưa cho ta x99 thức ăn", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 1) {
                    List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(663, 664, 665, 666, 667));
                    int couponAdd = 0;
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (item.isNotNullItem()) {
                        if (item.template.id >= 663 && item.template.id <= 667) {
                            couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 2 : item.template.id <= 665 ? 1 : 1;
                        }
                    }
                    if (item.quantity < 99) {
                        this.bill.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần x99 thức ăn!", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Sau khi ép x99 thức ăn " + item.template.name + "\n|7|"
                            + "Ngươi sẽ nhận được : x" + couponAdd + " mảnh đồ hủy diệt\n"
                            + (500000000 > player.inventory.gold ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(500000000) + " vàng";
                    if (player.inventory.gold < 500000000L) {
                        this.bill.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nTa cần ít nhất là 500tr vàng mau nạp donate cho ta");
                        return;
                    }
                    this.bill.createOtherMenu(player, ConstNpc.BILL,
                            npcSay, "Nâng Cấp", "Từ chối");
                } else {
                    this.bill.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đem thức ăn đến cho ta", "Đóng");
                }
                break;
            case PHAN_RA_DO_SKH_TL:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Con hãy đưa ta đồ SKH Thần để phân rã", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 1) {

                    List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
                    int couponAdd = 0;
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (item.isNotNullItem()) {
                        if (item.template.id >= 555 && item.template.id <= 567 && item.isSKHTLLVL0()) {
                            couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 10
                                    : 10;
                        }
                    }
                    if (couponAdd == 0) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta chỉ có thể phân rã Thần SKH thôi", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Sau khi phân rã vật phẩm\n|7|"
                            + "Bạn sẽ nhận được : " + couponAdd + " Đá Nâng SKH Vip\n"
                            + (50000000 > player.inventory.gold ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(50000000) + " vàng";

                    if (player.inventory.gold < 50000000) {
                        this.baHatMit.npcChat(player, "Hết tiền rồi\nẢo ít thôi con");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_PHAN_RA_DO_THAN_LINH,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(50000000) + " vàng", "Từ chối");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Ta chỉ có thể phân rã 1 lần 1 món đồ SKH Thần", "Đóng");
                }
                break;
            case NANG_CAP_DO_TS:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 2 món Hủy Diệt bất kì và 1 món Thần Linh cùng loại", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thần linh", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() < 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ hủy diệt", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu mảnh thiên sứ", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get().typeNameManh() + " thiên sứ tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";

                    if (player.inventory.gold < COST) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_DO_TS,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_CAP_SKH_VIP:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 trang bị Kích Hoạt và 2 trang bị Huỷ Diệt bất kì", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() < 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ Huỷ Diệt", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ Kích Hoạt ", "Đóng");
                        return;
                    }
                    String npcSay = "|7|Ngoc Rong Kamui\n" + "NÂNG CẤP TRANG BỊ KÍCH HOẠT VIP\n"
                            + "|2|Đã đạt đủ số lượng nguyên liệu, bạn sẽ nhận được : \n"
                            + "(" + player.combineNew.itemsCombine.stream().filter(Item::isSKH).findFirst().get().typeName() + " kích hoạt)\n" + "[ VIP ]\n"
                            + "|7|Nâng Cấp Ngay?";

                    if (player.inventory.gold < COST) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_DOI_SKH_VIP,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;

            case NANG_CAP_SKH_THUONG:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 2 món thần linh và 500tr vàng", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 2) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() != 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ Thần Linh", "Đóng");
                        return;
                    }
                    String npcSay = "|7|Ngoc Rong Kamui\n" + "|7|NÂNG CẤP TRANG BỊ KÍCH HOẠT\n"
                            + "|2|Đã đạt đủ số lượng nguyên liệu, bạn sẽ nhận được : \n"
                            + "(" + player.combineNew.itemsCombine.stream().filter(Item::isDTL).findFirst().get().typeName() + " kích hoạt)\n" + "[ THƯỜNG ]\n"
                            + "|7|Nâng Cấp Ngay?";

                    if (player.inventory.gold < COST) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_DOI_SKH_VIP,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;

            case THUE_DO:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item dothue = null;
                    Item vethuedo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isDoThue()) {
                            dothue = item;
                        } else if (item.template.id == 1257) {
                            vethuedo = item;
                        }
                    }
                    if (dothue != null && vethuedo != null && vethuedo.quantity >= 1) {
                        player.combineNew.goldCombine = 500000000;
                        player.combineNew.ratioCombine = 100;
                        String npcSay = "[ Gia Hạn Đồ ]" + "\n" + player.combineNew.ratioCombine + "%\nMất 1 vé gia hạn đồ\nBạn có muốn gia hạn không??";
                        for (Item.ItemOption io : dothue.itemOptions) {
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                this.buhan.createOtherMenu(player, ConstNpc.THO_REN, npcSay,
                                        "GIA HẠN NGAY");
                            } else {
                                this.buhan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Thiếu Vàng", "Đóng");
                            }
                        }
                    } else {
                        this.buhan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị được thuê và 1 vé gia hạn", "Đóng");
                    }
                } else {
                    this.buhan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị được thuê và 1 vé gia hạn", "Đóng");
                }
                break;
            case PHAN_RA_DO_THAN_LINH:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Con hãy đưa ta đồ thần linh để phân rã", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 1) {
                    List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
                    int couponAdd = 0;
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (item.isNotNullItem()) {
                        if (item.template.id >= 555 && item.template.id <= 567) {
                            couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 1
                                    : item.template.id == 561 ? 1 : 1;
                        }
                    }
                    if (couponAdd == 0) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta chỉ có thể phân rã đồ thần linh thôi", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Sau khi phân rã vật phẩm\n|7|"
                            + "Bạn sẽ nhận được : " + couponAdd + " đá nâng skh\n"
                            + (50000000 > player.inventory.gold ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(50000000) + " vàng";

                    if (player.inventory.gold < 50000000) {
                        this.baHatMit.npcChat(player, "Hết tiền rồi\nẢo ít thôi con");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_PHAN_RA_DO_THAN_LINH,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(50000000) + " vàng", "Từ chối");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Ta chỉ có thể phân rã 1 lần 1 món đồ thần linh", "Đóng");
                }
                break;
            case PHAN_RA_DO_THAN_LINH_X3:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Con hãy đưa ta đồ thần linh để phân rã", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
                    int couponAdd = 0;
                    Item item = player.combineNew.itemsCombine.get(0);
                    Item item1 = player.combineNew.itemsCombine.get(1);
                    Item item2 = player.combineNew.itemsCombine.get(2);
                    if (item.isNotNullItem()) {
                        if (item.template.id >= 555 && item.template.id <= 567 && item1.template.id >= 555 && item1.template.id <= 567 &&item2.template.id >= 555 && item2.template.id <= 567) {
                            couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 1
                                    : item.template.id == 561 ? 1 : 1;
                        }
                    }
                    if (couponAdd == 0) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta chỉ có thể phân rã đồ thần linh thôi", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Sau khi phân rã vật phẩm\n|7|"
                            + "Bạn sẽ nhận được 3" + " đá nâng skh\n"
                            + (50000000 > player.inventory.gold ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(50000000) + " vàng";

                    if (player.inventory.gold < 50000000) {
                        this.baHatMit.npcChat(player, "Hết tiền rồi\nẢo ít thôi con");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_PHAN_RA_DO_THAN_LINH,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(50000000) + " vàng", "Từ chối");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Ta chỉ có thể phân rã 1 lần 3 món đồ thần linh", "Đóng");
                }
                break;
            case PHAN_RA_DO_THAN_LINH_X5:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Con hãy đưa ta đồ thần linh để phân rã", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 5) {
                    List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
                    int couponAdd = 0;
                    Item item = player.combineNew.itemsCombine.get(0);
                    Item item1 = player.combineNew.itemsCombine.get(1);
                    Item item2 = player.combineNew.itemsCombine.get(2);
                    Item item3 = player.combineNew.itemsCombine.get(3);
                    Item item4 = player.combineNew.itemsCombine.get(4);

                    if (item.isNotNullItem()) {
                        if (item.template.id >= 555 && item.template.id <= 567 && item1.template.id >= 555 && item1.template.id <= 567 &&item2.template.id >= 555 && item2.template.id <= 567 &&item3.template.id >= 555 && item3.template.id <= 567 &&item4.template.id >= 555 && item4.template.id <= 567 ) {
                            couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 1
                                    : item.template.id == 561 ? 1 : 1;
                        }
                    }
                    if (couponAdd == 0) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta chỉ có thể phân rã đồ thần linh thôi", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Sau khi phân rã vật phẩm\n|7|"
                            + "Bạn sẽ nhận được 5" + " đá nâng skh\n"
                            + (50000000 > player.inventory.gold ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(50000000) + " vàng";

                    if (player.inventory.gold < 50000000) {
                        this.baHatMit.npcChat(player, "Hết tiền rồi\nẢo ít thôi con");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_PHAN_RA_DO_THAN_LINH,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(50000000) + " vàng", "Từ chối");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Ta chỉ có thể phân rã 1 lần 5 món đồ thần linh", "Đóng");
                }
                break;
            case NANG_CAP_CHAN_MENH:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    int star = 0;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1318 && item.isNotNullItem()) {
                            manhVo = item;
                        } else if (item.template.id >= 1300 && item.template.id <= 1308) {
                            bongTai = item;
                            star = item.template.id - 1300;
                        }
                    }
                    if (bongTai != null && bongTai.template.id == 1308) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chân Mệnh đã đạt cấp tối đa", "Đóng");
                        return;
                    }
                    player.combineNew.DiemNangcap = getDiemNangcapChanmenh(star);
                    player.combineNew.DaNangcap = getDaNangcapChanmenh(star);
                    player.combineNew.TileNangcap = getTiLeNangcapChanmenh(star);
                    if (bongTai != null && manhVo != null && (bongTai.template.id >= 1300 && bongTai.template.id < 1308)) {
                        String npcSay = bongTai.template.name + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.TileNangcap + "%" + "\n";
                        if (player.combineNew.DiemNangcap <= player.pointSb) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.DiemNangcap) + " Điểm Săn Boss";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.DaNangcap + " Đá Hoàng Kim");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.DiemNangcap - player.pointSb) + " Điểm Săn Boss";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Chân Mệnh và Đá Hoàng Kim", "Đóng");
                    }
                } else if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhVo = null;
                    Item tv = null;
                    int star = 0;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1318) {
                            manhVo = item;
                        } else if (item.template.id == 457) {
                            tv = item;
                        } else if (item.template.id >= 1300 && item.template.id <= 1308) {
                            bongTai = item;
                            star = item.template.id - 1300;
                        }
                    }
                    if (bongTai != null && bongTai.template.id == 1308) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chân Mệnh đã đạt cấp tối đa", "Đóng");
                        return;
                    }
                    player.combineNew.DiemNangcap = getDiemNangcapChanmenh(star) - 5;
                    player.combineNew.DaNangcap = getDaNangcapChanmenh(star) - 5;
                    player.combineNew.TileNangcap = getTiLeNangcapChanmenh(star) + 5;
                    if (tv != null && tv.quantity >= 1 && bongTai != null && manhVo != null && manhVo.quantity >= player.combineNew.DaNangcap - 5 && (bongTai.template.id >= 1300 && bongTai.template.id < 1308)) {
                        String npcSay = bongTai.template.name + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.TileNangcap + "%" + "\n";
                        if (player.combineNew.DiemNangcap <= player.pointSb - 5) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.DiemNangcap) + " Điểm Săn Boss";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.DaNangcap + " Đá Hoàng Kim");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.DiemNangcap - player.pointSb) + " Điểm Săn Boss";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Chân Mệnh và Đá Hoàng Kim", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Chân Mệnh và Đá Hoàng Kim", "Đóng");
                }
                break;
            case NANG_SKH_TL_LVL0:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 5 món hủy diệt và 10 đá nâng skh....", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 6) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDNS1() && item.quantity >= 10).count() != 1) {
                        Service.getInstance().sendThongBao(player, "Thiếu Đá nâng SKH Thần");
                        return;
                    }

                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() != 5) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ hủy diệt rồi", "Đóng");
                        return;
                    }
                    Item item1 = player.combineNew.itemsCombine.get(0);
                    Item item2 = player.combineNew.itemsCombine.get(1);
                    Item item3 = player.combineNew.itemsCombine.get(2);
                    Item item4 = player.combineNew.itemsCombine.get(3);
                    Item item5 = player.combineNew.itemsCombine.get(4);

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDHD).findFirst().get().typeName() + " kích hoạt [Lever 0] tương ứng\n";

                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 6) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_SKH_TL_LVL1:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món SKH và 7 món hủy diệt....", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 7) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDNS1() && item.quantity >= 20).count() != 1) {
                        Service.getInstance().sendThongBao(player, "Thiếu Đá nâng SKH Thần");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKHTLLVL0()).count() != 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ SKH Thần rồi", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() != 5) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ hủy diệt rồi", "Đóng");
                        return;
                    }
                    Item item1 = player.combineNew.itemsCombine.get(0);
                    Item item2 = player.combineNew.itemsCombine.get(1);
                    Item item3 = player.combineNew.itemsCombine.get(2);
                    Item item4 = player.combineNew.itemsCombine.get(3);
                    Item item5 = player.combineNew.itemsCombine.get(4);
                    Item item6 = player.combineNew.itemsCombine.get(5);

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7| Tỉ Lệ: 30%\n"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isSKHTLLVL0).findFirst().get().typeName() + " kích hoạt [Lever 1] tương ứng\n";

                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 7) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_SKH_TL_LVL2:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món SKH và 10 món hủy diệt....", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 7) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDNS1() && item.quantity >= 50).count() != 1) {
                        Service.getInstance().sendThongBao(player, "Thiếu Đá nâng SKH Thần");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKHTLLVL1()).count() != 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ SKH Thần rồi", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() != 5) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ hủy diệt rồi", "Đóng");
                        return;
                    }
                    Item item1 = player.combineNew.itemsCombine.get(0);
                    Item item2 = player.combineNew.itemsCombine.get(1);
                    Item item3 = player.combineNew.itemsCombine.get(2);
                    Item item4 = player.combineNew.itemsCombine.get(3);
                    Item item5 = player.combineNew.itemsCombine.get(4);
                    Item item6 = player.combineNew.itemsCombine.get(5);
                    Item item7 = player.combineNew.itemsCombine.get(6);

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7| Tỉ lệ: 30%\n"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isSKHTLLVL1).findFirst().get().typeName() + " kích hoạt [Lever 2] tương ứng\n";

                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 7) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_SKH_LVL1:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món SKH và 5 món Thần....", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 7) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDNS() && item.quantity >= 20).count() != 1) {
                        Service.getInstance().sendThongBao(player, "Thiếu Đá nâng SKH");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() != 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ SKH rồi", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() != 5) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ Thần Linh rồi", "Đóng");
                        return;
                    }
                    Item item1 = player.combineNew.itemsCombine.get(0);
                    Item item2 = player.combineNew.itemsCombine.get(1);
                    Item item3 = player.combineNew.itemsCombine.get(2);
                    Item item4 = player.combineNew.itemsCombine.get(3);
                    Item item5 = player.combineNew.itemsCombine.get(4);
                    Item item6 = player.combineNew.itemsCombine.get(5);

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|Tỉ lệ: 30%\n"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isSKH).findFirst().get().typeName() + " kích hoạt [Lever 1] tương ứng\n";

                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 7) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_SKH_LVL2:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món SKH và 5 món Thần....", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 7) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDNS() && item.quantity >= 50).count() != 1) {
                        Service.getInstance().sendThongBao(player, "Thiếu Đá nâng SKH");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKHLVL1()).count() != 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ SKH rồi", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() != 5) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ Thần Linh rồi", "Đóng");
                        return;
                    }
                    Item item1 = player.combineNew.itemsCombine.get(0);
                    Item item2 = player.combineNew.itemsCombine.get(1);
                    Item item3 = player.combineNew.itemsCombine.get(2);
                    Item item4 = player.combineNew.itemsCombine.get(3);
                    Item item5 = player.combineNew.itemsCombine.get(4);
                    Item item6 = player.combineNew.itemsCombine.get(5);

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|Tỉ lệ: 30%\n"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isSKHLVL1).findFirst().get().typeName() + " kích hoạt [Lever 2] tương ứng\n";

                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 7) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case THUC_TINH_DT:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item dathuctinh = null;
                    Item ngocrong = null;
                    Item tv = null;
                    Item ngocrongsp = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 16) {
                            ngocrong = item;
                        } else if (item.template.id == 14) {
                            ngocrongsp = item;
                        } else if (item.template.id == 1262) {
                            dathuctinh = item;
                        } else if (item.template.id == 457) {
                            tv = item;
                        }
                    }
                    if (ngocrongsp != null && ngocrongsp.quantity >= 1 && dathuctinh != null && dathuctinh.quantity >= 20
                            && tv != null && tv.quantity >= 3) {
                        if (player.nPoint.hpg >= 680000 && player.nPoint.mpg >= 680000 && player.nPoint.dameg >= 36000) {
                            this.buhan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Bạn đã đạt cấp độ thức tỉnh tối đa tối đa", "Đóng");
                            return;
                        }
                        if (player.nPoint.dameg >= 30000 && player.nPoint.hpg >= 500000
                                && player.nPoint.mpg >= 500000 && player.nPoint.power >= 120000000000L) {
                            String npcSay = "|7|[ THỨC TỈNH BẢN THÂN ]"
                                    + "\n|5|HP: " + player.nPoint.hpg
                                    + "\nKI: " + player.nPoint.mpg
                                    + "\nSD: " + player.nPoint.dameg
                                    + "\n|7|Tỉ lệ thành công: "
                                    + (player.nPoint.hpg >= 600000 && player.nPoint.hpg < 625000
                                    && player.nPoint.mpg >= 600000 && player.nPoint.mpg < 625000
                                    && player.nPoint.dameg >= 32000 && player.nPoint.dameg < 33000 ? "50%"
                                            : player.nPoint.hpg >= 625000 && player.nPoint.hpg < 650000
                                            && player.nPoint.mpg >= 625000 && player.nPoint.mpg < 650000
                                            && player.nPoint.dameg >= 33000 && player.nPoint.dameg < 34500 ? "30%" : "10%") + "\n";
                            this.buhan.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng X1\nCần 100\nHồng Ngọc", "Nâng X10\nCần 1.000\nHồng Ngọc", "Nâng X100\nCần 10.000\nHồng Ngọc");
                        } else {
                            this.buhan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Ngươi chưa đạt đủ điều kiện để thức tỉnh\n"
                                    + "Yêu cầu : Max chỉ số HP, KI, SD và sức mạnh đạt 120Tỷ trở lên", "Đóng");
                        }
                    } else if (ngocrong != null && ngocrong.quantity >= 1
                            && dathuctinh != null && dathuctinh.quantity >= 10
                            && tv != null && tv.quantity >= 1) {
                        if (player.pet.thuctinh == 30 && player.pet.nPoint.hpg >= 680000 && player.pet.nPoint.mpg >= 680000 && player.pet.nPoint.dameg >= 36000) {
                            this.buhan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Bạn đã đạt cấp độ thức tỉnh tối đa tối đa", "Đóng");
                            return;
                        }
                        if (player.pet.nPoint.dameg >= 31000 && player.pet.nPoint.hpg >= 360000
                                && player.pet.nPoint.mpg >= 360000 && player.pet.nPoint.power >= 120000000000L) {
                            String tsSay = "|7|[ THỨC TỈNH ĐỆ TỬ ]\n" + player.pet.name.substring(1)
                                    + "\n|5|Cấp bậc : " + player.pet.getNameThuctinh(player.pet.thuctinh)
                                    + "\nHP: " + (player.pet.nPoint.hpg >= 680000 ? Util.format(player.pet.nPoint.hpg)
                                            : Util.format(player.pet.nPoint.hpg) + " Cấp Sau: " + Util.format(player.pet.nPoint.hpg + 200))
                                    + "\nKI: " + (player.pet.nPoint.mpg >= 680000 ? Util.format(player.pet.nPoint.mpg)
                                            : Util.format(player.pet.nPoint.mpg) + " Cấp Sau: " + Util.format(player.pet.nPoint.mpg + 200))
                                    + "\nSD: " + (player.pet.nPoint.dameg >= 36000 ? Util.format(player.pet.nPoint.dameg)
                                            : Util.format(player.pet.nPoint.dameg) + " Cấp Sau: " + Util.format(player.pet.nPoint.dameg + 100))
                                    + "\n|7|Tỉ lệ thành công: "
                                    + (player.pet.nPoint.hpg >= 600000 && player.pet.nPoint.hpg < 625000
                                    && player.pet.nPoint.mpg >= 600000 && player.pet.nPoint.mpg < 625000
                                    && player.pet.nPoint.dameg >= 32000 && player.pet.nPoint.dameg < 33000 ? "50%"
                                            : player.pet.nPoint.hpg >= 625000 && player.pet.nPoint.hpg < 650000
                                            && player.pet.nPoint.mpg >= 625000 && player.pet.nPoint.mpg < 650000
                                            && player.pet.nPoint.dameg >= 33000 && player.pet.nPoint.dameg < 34500 ? "30%" : "10%") + "\n";
                            this.buhan.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, tsSay,
                                    "Nâng X1\nCần 100\nHồng Ngọc", "Nâng X10\nCần 1.000\nHồng Ngọc", "Nâng X100\nCần 10.000\nHồng Ngọc");
                        } else {
                            this.buhan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Đệ tử ngươi chưa đạt đủ điều kiện để thức tỉnh\n"
                                    + "Yêu cầu : Max chỉ số HP, KI, SD và sức mạnh đạt 120Tỷ trở lên", "Đóng");
                        }
                    } else {
                        this.buhan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Đối với đệ tử: Cần 30 Đá Thức Tỉnh, 3 viên ngọc rồng 3s và 1 thỏi vàng"
                                + "\nĐối với sư phụ: Cần 50 Đá Thức Tỉnh, 3 viên ngọc rồng 1s và 3 thỏi vàng", "Đóng");
                    }
                } else {
                    this.buhan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Đối với đệ tử: Cần 30 Đá Thức Tỉnh, 3 viên ngọc rồng 3s và 1 thỏi vàng"
                            + "\nĐối với sư phụ: Cần 50 Đá Thức Tỉnh, 3 viên ngọc rồng 1s và 3 thỏi vàng", "Đóng");
                }
                break;
            case THUC_TINH_SP:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item dathuctinh = null;
                    Item ngocrong = null;
                    Item tv = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 14) {
                            ngocrong = item;
                        } else if (item.template.id == 1262) {
                            dathuctinh = item;
                        } else if (item.template.id == 457) {
                            tv = item;
                        }
                    }
                    if (player.nPoint.hpg >= 420000 && player.nPoint.mpg >= 420000 && player.nPoint.dameg >= 36000) {
                        this.buhan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bạn đã đạt cấp độ thức tỉnh tối đa tối đa", "Đóng");
                        return;
                    }
                    if (ngocrong != null && ngocrong.quantity >= 3
                            && dathuctinh != null && dathuctinh.quantity >= 50
                            && tv != null && tv.quantity >= 5) {
                        String npcSay = "|7|[ THỨC TỈNH BẢN THÂN ]"
                                + "\n|5|HP: " + player.nPoint.hpg
                                + "\nKI: " + player.nPoint.mpg
                                + "\nSD: " + player.nPoint.dameg
                                + "\n|7|Tỉ lệ thành công: "
                                + (player.nPoint.hpg >= 360000 && player.nPoint.hpg < 380000
                                && player.nPoint.mpg >= 360000 && player.nPoint.mpg < 380000
                                && player.nPoint.dameg >= 32000 && player.nPoint.dameg < 34000 ? "50%"
                                        : player.nPoint.hpg >= 380000 && player.nPoint.hpg < 400000
                                        && player.nPoint.mpg >= 380000 && player.nPoint.mpg < 400000
                                        && player.nPoint.dameg >= 34000 && player.nPoint.dameg < 35000 ? "30%" : "10%") + "\n";
                        this.buhan.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Nâng Cấp\nCần 100\nHồng Ngọc");
                    } else {
                        this.buhan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 50 Đá Thức Tỉnh, 3 viên ngọc rồng 1s và 5 thỏi vàng", "Đóng");
                    }
                } else {
                    this.buhan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 50 Đá Thức Tỉnh, 3 viên ngọc rồng 1s và 5 thỏi vàng", "Đóng");
                }
                break;
        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player người chơi
     */
    public void startCombine(Player player) {
        switch (player.combineNew.typeCombine) {
            case EP_SAO_TRANG_BI:
                epSaoTrangBi(player);
                break;
            case NANG_SKH_LVL1:
                nangcapskhlvl1(player);
                break;
            case NANG_SKH_LVL2:
                nangcapskhlvl2(player);
                break;
            case NANG_SKH_TL_LVL0:
                nangcapskhtllvl0(player);
                break;
            case NANG_SKH_TL_LVL1:
                nangcapskhtllvl1(player);
                break;
            case NANG_SKH_TL_LVL2:
                nangcapskhtllvl2(player);
                break;
            case PHAN_RA_DO_SKH_TL:
                phanradoskhthanlinh(player);
            case PHA_LE_HOA_TRANG_BI:
                phaLeHoaTrangBi(player);
                break;
            case PHA_LE_HOA_TRANG_BI_VIP:
                phaLeHoaTrangBi8s(player);
                break;
            case CHUYEN_HOA_TRANG_BI:

                break;
            case NHAP_NGOC_RONG:
                nhapNgocRong(player);
                break;
            case NHAP_SPL:
                nhapspl(player);
                break;
            case NHAP_DA_SKH:
                nhapdaskh(player);
                break;

            case PHAN_RA_DO_THAN_LINH:
                phanradothanlinhx1(player);
                break;
            case PHAN_RA_DO_THAN_LINH_X3:
                phanradothanlinhx3(player);
                break;
            case PHAN_RA_DO_THAN_LINH_X5:
                phanradothanlinhx5(player);
                break;
            case NANG_CAP_DO_TS:
                openDTS(player);
                break;
            case CHE_TAO_TRANG_BI_TS:
                openCreateItemAngel(player);
                break;
            case NANG_CAP_SKH_VIP:
                openSKHVIP(player);
                break;
            case NANG_CAP_SKH_THUONG:
                openSKHT(player);
                break;
//            case NANG_CAP_VAT_PHAM:
//                nangCapVatPham(player);
//                break;

            case NANG_CAP_BONG_TAI_1_LEN_2:
                nangCapBongTai(player);
                break;
            case MO_CHI_SO_BONG_TAI_CAP2:
                moChiSoBongTai(player);
                break;
            case NANG_CAP_LINH_THU:
                moChiSolinhthu(player);
                break;
            case NANG_CAP_BONG_TAI_2_LEN_3:
                nangCapBongTaicap3(player);
                break;
            case MO_CHI_SO_BONG_TAI_CAP3:
                moChiSoBongTaicap3(player);
                break;
            case NANG_CAP_BONG_TAI_3_LEN_4:
                nangCapBongTaicap4(player);
                break;
            case MO_CHI_SO_BONG_TAI_CAP4:
                moChiSoBongTaicap4(player);
                break;
            case NANG_CAP_BONG_TAI_CAP5:
                nangCapBongTaicap5(player);
                break;
            case MO_CHI_SO_BONG_TAI_CAP5:
                moChiSoBongTaicap5(player);
            case LUYEN_HOA_CHIEN_LINH:
                //
                break;
            case MO_GIOI_HAN_CHIEN_LINH:
                //
                break;
            case DOI_DIEM:
                doidiem(player);
                break;
            case THUE_DO:
                thuedo(player);
                break;
            case NANG_CAP_CHAN_MENH:
                nangCapChanMenh(player);
        }

        player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
        player.combineNew.clearParamCombine();
        player.combineNew.lastTimeCombine = System.currentTimeMillis();

    }

    public void GetTrangBiKichHoathuydiet(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        int[][] paramNormal = {{139, 140}, {142, 144}, {136, 138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        int[][] paramVIP = {{141}, {143}, {137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(1500, 2000)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(100, 150)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(9000, 11000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(90, 150)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(15, 20)));
        }
        if (randomSkh <= 20) {//tile ra do kich hoat
            if (randomSkh <= 5) { // tile ra option vip
                item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            } else {// 
                item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            }
        }

        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void GetTrangBiKichHoatthiensu(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        int[][] paramNormal = {{139, 140}, {142, 144}, {136, 138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        int[][] paramVIP = {{141}, {143}, {137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(150, 200)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(18000, 20000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(150, 200)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(20, 25)));
        }
        if (randomSkh <= 20) {//tile ra do kich hoat
            if (randomSkh <= 5) { // tile ra option vip
                item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            } else {// 
                item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            }
        }

        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void khilv2(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(50, 20));//sd
        item.itemOptions.add(new ItemOption(77, 20));//hp
        item.itemOptions.add(new ItemOption(103, 20));//ki
        item.itemOptions.add(new ItemOption(14, 20));//cm
        item.itemOptions.add(new ItemOption(5, 20));//sd cm
        item.itemOptions.add(new ItemOption(106, 0));
        item.itemOptions.add(new ItemOption(34, 0));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void khilv3(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(50, 22));//sd
        item.itemOptions.add(new ItemOption(77, 22));//hp
        item.itemOptions.add(new ItemOption(103, 22));//ki
        item.itemOptions.add(new ItemOption(14, 22));//cm
        item.itemOptions.add(new ItemOption(5, 22));//sd cm
        item.itemOptions.add(new ItemOption(106, 0));
        item.itemOptions.add(new ItemOption(35, 0));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void khilv4(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(50, 24));//sd
        item.itemOptions.add(new ItemOption(77, 24));//hp
        item.itemOptions.add(new ItemOption(103, 24));//ki
        item.itemOptions.add(new ItemOption(14, 24));//cm
        item.itemOptions.add(new ItemOption(5, 24));//sd cm
        item.itemOptions.add(new ItemOption(106, 0));
        item.itemOptions.add(new ItemOption(36, 0));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void khilv5(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(50, 26));//sd
        item.itemOptions.add(new ItemOption(77, 26));//hp
        item.itemOptions.add(new ItemOption(103, 26));//ki
        item.itemOptions.add(new ItemOption(14, 26));//cm
        item.itemOptions.add(new ItemOption(5, 26));//sd cm
        item.itemOptions.add(new ItemOption(106, 0));
        item.itemOptions.add(new ItemOption(36, 0));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    private void doiKiemThan(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item keo = null, luoiKiem = null, chuoiKiem = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 2015) {
                    keo = it;
                } else if (it.template.id == 2016) {
                    chuoiKiem = it;
                } else if (it.template.id == 2017) {
                    luoiKiem = it;
                }
            }
            if (keo != null && keo.quantity >= 99 && luoiKiem != null && chuoiKiem != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2018);
                    item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(9, 15)));
                    item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(8, 15)));
                    item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(8, 15)));
                    if (Util.isTrue(80, 100)) {
                        item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
                    }
                    InventoryServiceNew.gI().addItemBag(player, item);

                    InventoryServiceNew.gI().subQuantityItemsBag(player, keo, 99);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, luoiKiem, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, chuoiKiem, 1);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiChuoiKiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item manhNhua = player.combineNew.itemsCombine.get(0);
            if (manhNhua.template.id == 2014 && manhNhua.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2016);
                    InventoryServiceNew.gI().addItemBag(player, item);

                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhNhua, 99);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiLuoiKiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item manhSat = player.combineNew.itemsCombine.get(0);
            if (manhSat.template.id == 2013 && manhSat.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2017);
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhSat, 99);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiManhKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 2 || player.combineNew.itemsCombine.size() == 3) {
            Item nr1s = null, doThan = null, buaBaoVe = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 14) {
                    nr1s = it;
                } else if (it.template.id == 2010) {
                    buaBaoVe = it;
                } else if (it.template.id >= 555 && it.template.id <= 567) {
                    doThan = it;
                }
            }

            if (nr1s != null && doThan != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_MANH_KICH_HOAT) {
                    player.inventory.gold -= COST_DOI_MANH_KICH_HOAT;
                    int tiLe = buaBaoVe != null ? 100 : 50;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) 2009);
                        item.itemOptions.add(new Item.ItemOption(30, 0));
                        InventoryServiceNew.gI().addItemBag(player, item);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, nr1s, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, doThan, 1);
                    if (buaBaoVe != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, buaBaoVe, 1);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");
            }
        }
    }

    public void openDTS(Player player) {
        //check sl đồ tl, đồ hd
        // new update 2 mon huy diet + 1 mon than linh(skh theo style) +  5 manh bat ki
        if (player.combineNew.itemsCombine.size() != 4) {
            Service.gI().sendThongBao(player, "Thiếu đồ");
            return;
        }
        if (player.inventory.gold < COST) {
            Service.gI().sendThongBao(player, "Ảo ít thôi con...");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            return;
        }
        Item itemTL = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).findFirst().get();
        List<Item> itemHDs = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).collect(Collectors.toList());
        Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 5).findFirst().get();

        player.inventory.gold -= COST;
        sendEffectSuccessCombine(player);
        short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

        Item itemTS = ItemService.gI().DoThienSu(itemIds[itemTL.template.gender > 2 ? player.gender : itemTL.template.gender][itemManh.typeIdManh()], itemTL.template.gender);
        InventoryServiceNew.gI().addItemBag(player, itemTS);

        InventoryServiceNew.gI().subQuantityItemsBag(player, itemTL, 1);
        InventoryServiceNew.gI().subQuantityItemsBag(player, itemManh, 5);
        itemHDs.forEach(item -> InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1));
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + itemTS.template.name);
        player.combineNew.itemsCombine.clear();
        reOpenItemCombine(player);
    }

    public void openCreateItemAngel(Player player) {
        // Công thức vip + x999 Mảnh thiên sứ + đá nâng cấp + đá may mắn
        if (player.combineNew.itemsCombine.size() < 2 || player.combineNew.itemsCombine.size() > 4) {
            Service.getInstance().sendThongBao(player, "Thiếu vật phẩm, vui lòng thêm vào");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Công thức Vip");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Mảnh thiên sứ");
            return;
        }
//        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1) {
//            Service.getInstance().sendThongBao(player, "Thiếu Đá nâng cấp");
//            return;
//        }
//        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1) {
//            Service.getInstance().sendThongBao(player, "Thiếu Đá may mắn");
//            return;
//        }
        Item mTS = null, daNC = null, daMM = null, CtVip = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.isNotNullItem()) {
                if (item.isManhTS()) {
                    mTS = item;
                } else if (item.isDaNangCap()) {
                    daNC = item;
                } else if (item.isDaMayMan()) {
                    daMM = item;
                } else if (item.isCongThucVip()) {
                    CtVip = item;
                }
            }
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {//check chỗ trống hành trang
            if (player.inventory.gold < 500000000) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            player.inventory.gold -= 500000000;

            int tilemacdinh = 35;
            int tileLucky = 20;
            if (daNC != null) {
                tilemacdinh += (daNC.template.id - 1073) * 10;
            } else {
                tilemacdinh = tilemacdinh;
            }
            if (daMM != null) {
                tileLucky += tileLucky * (daMM.template.id - 1078) * 10 / 100;
            } else {
                tileLucky = tileLucky;
            }

            if (Util.nextInt(0, 100) < tilemacdinh) {
                Item itemCtVip = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).findFirst().get();
                if (daNC != null) {
                    Item itemDaNangC = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).findFirst().get();
                }
                if (daMM != null) {
                    Item itemDaMayM = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).findFirst().get();
                }
                Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).findFirst().get();

                tilemacdinh = Util.nextInt(0, 50);
                if (tilemacdinh == 49) {
                    tilemacdinh = 20;
                } else if (tilemacdinh == 48 || tilemacdinh == 47) {
                    tilemacdinh = 19;
                } else if (tilemacdinh == 46 || tilemacdinh == 45) {
                    tilemacdinh = 18;
                } else if (tilemacdinh == 44 || tilemacdinh == 43) {
                    tilemacdinh = 17;
                } else if (tilemacdinh == 42 || tilemacdinh == 41) {
                    tilemacdinh = 16;
                } else if (tilemacdinh == 40 || tilemacdinh == 39) {
                    tilemacdinh = 15;
                } else if (tilemacdinh == 38 || tilemacdinh == 37) {
                    tilemacdinh = 14;
                } else if (tilemacdinh == 36 || tilemacdinh == 35) {
                    tilemacdinh = 13;
                } else if (tilemacdinh == 34 || tilemacdinh == 33) {
                    tilemacdinh = 12;
                } else if (tilemacdinh == 32 || tilemacdinh == 31) {
                    tilemacdinh = 11;
                } else if (tilemacdinh == 30 || tilemacdinh == 29) {
                    tilemacdinh = 10;
                } else if (tilemacdinh <= 28 || tilemacdinh >= 26) {
                    tilemacdinh = 9;
                } else if (tilemacdinh <= 25 || tilemacdinh >= 23) {
                    tilemacdinh = 8;
                } else if (tilemacdinh <= 22 || tilemacdinh >= 20) {
                    tilemacdinh = 7;
                } else if (tilemacdinh <= 19 || tilemacdinh >= 17) {
                    tilemacdinh = 6;
                } else if (tilemacdinh <= 16 || tilemacdinh >= 14) {
                    tilemacdinh = 5;
                } else if (tilemacdinh <= 13 || tilemacdinh >= 11) {
                    tilemacdinh = 4;
                } else if (tilemacdinh <= 10 || tilemacdinh >= 8) {
                    tilemacdinh = 3;
                } else if (tilemacdinh <= 7 || tilemacdinh >= 5) {
                    tilemacdinh = 2;
                } else if (tilemacdinh <= 4 || tilemacdinh >= 2) {
                    tilemacdinh = 1;
                } else if (tilemacdinh <= 1) {
                    tilemacdinh = 0;
                }
                short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

                Item itemTS = ItemService.gI().DoThienSu(itemIds[itemCtVip.template.gender > 2 ? player.gender : itemCtVip.template.gender][itemManh.typeIdManh()], itemCtVip.template.gender);

                tilemacdinh += 10;

                if (tilemacdinh > 0) {
                    for (byte i = 0; i < itemTS.itemOptions.size(); i++) {
                        if (itemTS.itemOptions.get(i).optionTemplate.id != 21 && itemTS.itemOptions.get(i).optionTemplate.id != 30) {
                            itemTS.itemOptions.get(i).param += (itemTS.itemOptions.get(i).param * tilemacdinh / 100);
                        }
                    }
                }
                tilemacdinh = Util.nextInt(0, 100);

                if (tilemacdinh <= tileLucky) {
                    if (tilemacdinh >= (tileLucky - 3)) {
                        tileLucky = 3;
                    } else if (tilemacdinh <= (tileLucky - 4) && tilemacdinh >= (tileLucky - 10)) {
                        tileLucky = 2;
                    } else {
                        tileLucky = 1;
                    }
                    itemTS.itemOptions.add(new Item.ItemOption(15, tileLucky));
                    ArrayList<Integer> listOptionBonus = new ArrayList<>();
                    listOptionBonus.add(50);
                    listOptionBonus.add(77);
                    listOptionBonus.add(103);
                    listOptionBonus.add(98);
                    listOptionBonus.add(99);
                    for (int i = 0; i < tileLucky; i++) {
                        // Ensure the upper limit for Util.nextInt is exclusive, preventing an out-of-bounds index
                        tilemacdinh = Util.nextInt(0, listOptionBonus.size());

                        // Safety check to prevent IndexOutOfBoundsException
                        if (tilemacdinh >= listOptionBonus.size()) {
                            // This should not happen if Util.nextInt is implemented correctly
                            System.err.println("Random index out of bounds: " + tilemacdinh);
                            continue; // Skip this iteration or handle the error as appropriate
                        }

                        itemTS.itemOptions.add(new ItemOption(listOptionBonus.get(tilemacdinh), Util.nextInt(1, 5)));
                        listOptionBonus.remove(tilemacdinh);
                    }
                }

                InventoryServiceNew.gI().addItemBag(player, itemTS);
                sendEffectSuccessCombine(player);
            } else {
                sendEffectFailCombine(player);
            }
            if (mTS != null && daMM != null && daNC != null && CtVip != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
            } else if (CtVip != null && mTS != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
            } else if (CtVip != null && mTS != null && daNC != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
            } else if (CtVip != null && mTS != null && daMM != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
            }
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void openSKHT(Player player) {
        // 2 đtl -- món đầu làm gốc
        if (player.combineNew.itemsCombine.size() != 2) {
            Service.gI().sendThongBao(player, "Sai nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() != 2) {
            Service.gI().sendThongBao(player, "Thiếu đồ Thần Linh");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < 1) {
                Service.gI().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            player.inventory.gold -= COST;
            Item itemTL = player.combineNew.itemsCombine.stream().filter(Item::isDTL).findFirst().get();
            List<Item> itemDiKem = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, itemTL.template.iconID, itemTL.template.iconID);
            short itemId = Manager.doSKH[player.gender][itemTL.template.type][0];
            Item item = ItemService.gI().itemSKHT(itemId, ItemService.gI().randomSKHTId(player.gender));
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTL, 1);
            itemDiKem.forEach(it -> InventoryServiceNew.gI().subQuantityItemsBag(player, it, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void openSKHVIP(Player player) {
        // 2 dhd + 1 món kích hoạt -- món đầu kh làm gốc
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() != 2) {
            Service.gI().sendThongBao(player, "Thiếu đồ huỷ diệt");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() != 1) {
            Service.gI().sendThongBao(player, "Thiếu đồ kích hoạt");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < 1) {
                Service.gI().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            player.inventory.gold -= COST;
            Item itemSKH = player.combineNew.itemsCombine.stream().filter(Item::isSKH).findAny().get();
            List<Item> itemDHD = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, itemSKH.template.iconID, itemSKH.template.iconID);
            short itemId = Manager.doSKHVip[itemSKH.template.gender < 3 ? itemSKH.template.gender : player.gender][itemSKH.template.type][Util.nextInt(0, 6)];
            Item item;
            if (new Item(itemId).isDTL()) {
                item = Util.ratiItemTL(itemId);
                item.itemOptions.add(new Item.ItemOption(ItemService.gI().randomSKHTId(player.gender), 1));
                item.itemOptions.add(new Item.ItemOption(ItemService.gI().optionIdSKH(ItemService.gI().randomSKHTId(itemSKH.template.gender < 3 ? itemSKH.template.gender : player.gender)), 1));
                item.itemOptions.remove(item.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                item.itemOptions.add(new Item.ItemOption(21, 15));
                item.itemOptions.add(new Item.ItemOption(30, 1));
            } else {
                item = ItemService.gI().itemSKHT(itemId, ItemService.gI().randomSKHTId(itemSKH.template.gender < 3 ? itemSKH.template.gender : player.gender));
            }
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemSKH, 1);
            itemDHD.forEach(it -> InventoryServiceNew.gI().subQuantityItemsBag(player, it, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void dapDoKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 1 || player.combineNew.itemsCombine.size() == 2) {
            Item dhd = null, dtl = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 650 && item.template.id <= 662) {
                        dhd = item;
                    } else if (item.template.id >= 555 && item.template.id <= 567) {
                        dtl = item;
                    }
                }
            }
            if (dhd != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 //check chỗ trống hành trang
                        && player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                    player.inventory.gold -= COST_DAP_DO_KICH_HOAT;
                    int tiLe = dtl != null ? 100 : 50;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) getTempIdItemC0(dhd.template.gender, dhd.template.type));
                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                        RewardService.gI().initActivationOption(item.template.gender < 3 ? item.template.gender : player.gender, item.template.type, item.itemOptions);
                        InventoryServiceNew.gI().addItemBag(player, item);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, dhd, 1);
                    if (dtl != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dtl, 1);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiVeHuyDiet(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item item = player.combineNew.itemsCombine.get(0);
            if (item.isNotNullItem() && item.template.id >= 555 && item.template.id <= 567) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_VE_DOI_DO_HUY_DIET) {
                    player.inventory.gold -= COST_DOI_VE_DOI_DO_HUY_DIET;
                    Item ticket = ItemService.gI().createNewItem((short) (2001 + item.template.type));
                    ticket.itemOptions.add(new Item.ItemOption(30, 0));
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                    InventoryServiceNew.gI().addItemBag(player, ticket);
                    sendEffectOpenItem(player, item.template.iconID, ticket.template.iconID);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void nangCapBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }

            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }

            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 454) {
                    bongTai = item;
                } else if (item.template.id == 933) {
                    manhVo = item;
                }
            }

            if (bongTai != null && manhVo != null && manhVo.quantity >= 9999) {
                Item findItemBag = InventoryServiceNew.gI().findItemBag(player, 921); //Khóa btc2
                if (findItemBag != null) {
                    Service.gI().sendThongBao(player, "Bạn đang sở hữu bông tai Porata cấp 2");
                    return;
                }
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(921);
                    sendEffectSuccessCombine(player);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 9999);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                } else {
                    sendEffectFailCombine(player);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 99);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }

            }
        }
    }

    private void moChiSoBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item honbongtai = null;
            Item daxanhlam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 921) {
                    bongtai = item;
                } else if (item.template.id == 934) {
                    honbongtai = item;
                } else if (item.template.id == 935) {
                    daxanhlam = item;
                }
            }
            if (bongtai != null && honbongtai != null && honbongtai.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, honbongtai, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daxanhlam, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongtai.itemOptions.clear();
                    bongtai.itemOptions.add(new Item.ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        bongtai.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 10)));
                    } else if (rdUp == 1) {
                        bongtai.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
                    } else if (rdUp == 2) {
                        bongtai.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 10)));
                    } else if (rdUp == 3) {
                        bongtai.itemOptions.add(new Item.ItemOption(108, Util.nextInt(1, 10)));
                    } else if (rdUp == 4) {
                        bongtai.itemOptions.add(new Item.ItemOption(94, Util.nextInt(1, 10)));
                    } else if (rdUp == 5) {
                        bongtai.itemOptions.add(new Item.ItemOption(14, Util.nextInt(1, 5)));
                    } else if (rdUp == 6) {
                        bongtai.itemOptions.add(new Item.ItemOption(80, Util.nextInt(5, 10)));
                    } else if (rdUp == 7) {
                        bongtai.itemOptions.add(new Item.ItemOption(81, Util.nextInt(5, 15)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapBongTaicap3(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 921) {
                    bongTai = item;
                } else if (item.template.id == 1427) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 9999) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 9999);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(1165);
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void moChiSoBongTaicap3(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item thachPhu = null;
            Item daxanhlam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1165) {
                    bongtai = item;
                } else if (item.template.id == 1429) {
                    thachPhu = item;
                } else if (item.template.id == 935) {
                    daxanhlam = item;
                }
            }
            if (bongtai != null && thachPhu != null && thachPhu.quantity >= 99 && daxanhlam != null && daxanhlam.quantity >= 1) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, thachPhu, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daxanhlam, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongtai.itemOptions.clear();
                    int rdUp = Util.nextInt(0, 7);
                    switch (rdUp) {
                        case 0:
                            bongtai.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10, 15)));
                            break;
                        case 1:
                            bongtai.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 15)));
                            break;
                        case 2:
                            bongtai.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 15)));
                            break;
                        case 3:
                            bongtai.itemOptions.add(new Item.ItemOption(108, Util.nextInt(5, 10)));
                            break;
                        case 4:
                            bongtai.itemOptions.add(new Item.ItemOption(94, Util.nextInt(5, 10)));
                            break;
                        case 5:
                            bongtai.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 7)));
                            break;
                        case 6:
                            bongtai.itemOptions.add(new Item.ItemOption(80, Util.nextInt(10, 15)));
                            break;
                        case 7:
                            bongtai.itemOptions.add(new Item.ItemOption(81, Util.nextInt(10, 15)));
                            break;
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapBongTaicap4(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1165) {
                    bongTai = item;
                } else if (item.template.id == 1428) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 999) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 999);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(1129);
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void moChiSoBongTaicap4(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item thachPhu = null;
            Item daxanhlam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1129) {
                    bongtai = item;
                } else if (item.template.id == 1429) {
                    thachPhu = item;
                } else if (item.template.id == 935) {
                    daxanhlam = item;
                }
            }
            if (bongtai != null && thachPhu != null && thachPhu.quantity >= 99 && daxanhlam != null && daxanhlam.quantity >= 5) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, thachPhu, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daxanhlam, 5);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongtai.itemOptions.clear();
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        bongtai.itemOptions.add(new Item.ItemOption(50, Util.nextInt(15, 20)));
                    } else if (rdUp == 1) {
                        bongtai.itemOptions.add(new Item.ItemOption(77, Util.nextInt(15, 20)));
                    } else if (rdUp == 2) {
                        bongtai.itemOptions.add(new Item.ItemOption(103, Util.nextInt(15, 20)));
                    } else if (rdUp == 3) {
                        bongtai.itemOptions.add(new Item.ItemOption(108, Util.nextInt(5, 15)));
                    } else if (rdUp == 4) {
                        bongtai.itemOptions.add(new Item.ItemOption(94, Util.nextInt(5, 15)));
                    } else if (rdUp == 5) {
                        bongtai.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 10)));
                    } else if (rdUp == 6) {
                        bongtai.itemOptions.add(new Item.ItemOption(80, Util.nextInt(15, 20)));
                    } else if (rdUp == 7) {
                        bongtai.itemOptions.add(new Item.ItemOption(81, Util.nextInt(15, 20)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapBongTaicap5(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1129) {
                    bongTai = item;
                } else if (item.template.id == 1424) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 999) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 999);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(1423);
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void moChiSoBongTaicap5(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item thachPhu = null;
            Item daxanhlam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1423) {
                    bongtai = item;
                } else if (item.template.id == 1425) {
                    thachPhu = item;
                } else if (item.template.id == 935) {
                    daxanhlam = item;
                }
            }
            if (bongtai != null && thachPhu != null && thachPhu.quantity >= 99 && daxanhlam != null && daxanhlam.quantity >= 10) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, thachPhu, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daxanhlam, 10);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongtai.itemOptions.clear();
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        bongtai.itemOptions.add(new Item.ItemOption(50, Util.nextInt(20, 25)));
                    } else if (rdUp == 1) {
                        bongtai.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20, 25)));
                    } else if (rdUp == 2) {
                        bongtai.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20, 25)));
                    } else if (rdUp == 3) {
                        bongtai.itemOptions.add(new Item.ItemOption(108, Util.nextInt(5, 20)));
                    } else if (rdUp == 4) {
                        bongtai.itemOptions.add(new Item.ItemOption(94, Util.nextInt(5, 20)));
                    } else if (rdUp == 5) {
                        bongtai.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 15)));
                    } else if (rdUp == 6) {
                        bongtai.itemOptions.add(new Item.ItemOption(80, Util.nextInt(20, 25)));
                    } else if (rdUp == 7) {
                        bongtai.itemOptions.add(new Item.ItemOption(81, Util.nextInt(20, 25)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void moChiSolinhthu(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item linhthu = null;
            Item damathuat = null;
            Item thucan = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id >= 2019 && item.template.id <= 2026) {
                    linhthu = item;
                } else if (item.template.id == 2030) {
                    damathuat = item;
                } else if (item.template.id >= 663 && item.template.id <= 667) {
                    thucan = item;
                }
            }
            if (linhthu != null && damathuat != null && damathuat.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, damathuat, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, thucan, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    linhthu.itemOptions.clear();
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        linhthu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 25)));
                    } else if (rdUp == 1) {
                        linhthu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 25)));
                    } else if (rdUp == 2) {
                        linhthu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 25)));
                    } else if (rdUp == 3) {
                        linhthu.itemOptions.add(new Item.ItemOption(108, Util.nextInt(5, 25)));
                    } else if (rdUp == 4) {
                        linhthu.itemOptions.add(new Item.ItemOption(94, Util.nextInt(5, 15)));
                    } else if (rdUp == 5) {
                        linhthu.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 15)));
                    } else if (rdUp == 6) {
                        linhthu.itemOptions.add(new Item.ItemOption(80, Util.nextInt(5, 25)));
                    } else if (rdUp == 7) {
                        linhthu.itemOptions.add(new Item.ItemOption(81, Util.nextInt(5, 25)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phanradoskhthanlinh(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            player.inventory.gold -= 20000000;
            Item item = player.combineNew.itemsCombine.get(0);
            if ((item.isSKHTLLVL0()) && item.isDTL()) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {

                    sendEffectSuccessCombine(player);
                    Item dangusac = ItemService.gI().createNewItem((short) 1466, 10);

                    InventoryServiceNew.gI().addItemBag(player, dangusac);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendThongBaoOK(player, "Mày Nhận Được Đá Nâng SKH Vip");
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                    player.combineNew.itemsCombine.clear();
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Cần 1 ô tromg trong hanh trang");
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);

            } else {
                Service.getInstance().sendThongBao(player, "Cần đồ skh thần linh");
            }

        }
    }

    private void phanradothanlinhx1(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            player.inventory.gold -= 20000000;
            List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
            Item item = player.combineNew.itemsCombine.get(0);
            int couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 1
                    : item.template.id == 561 ? 1 : 1;
            sendEffectSuccessCombine(player);
            Item dangusac = ItemService.gI().createNewItem((short) 1465);

            InventoryServiceNew.gI().addItemBag(player, dangusac);
            InventoryServiceNew.gI().sendItemBags(player);
            if (item.template.id == 561) {
                InventoryServiceNew.gI().addItemBag(player, dangusac);

                InventoryServiceNew.gI().sendItemBags(player);
            } else if (item.template.id == 562 || item.template.id == 564 || item.template.id == 566) {
                InventoryServiceNew.gI().addItemBag(player, dangusac);

                InventoryServiceNew.gI().sendItemBags(player);
            }
            Service.gI().sendThongBaoOK(player, "Mày Nhận Được Đá Nâng SKH");
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            player.combineNew.itemsCombine.clear();
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            reOpenItemCombine(player);
        }
    }

    private void phanradothanlinhx3(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            player.inventory.gold -= 20000000;
            List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
            Item item = player.combineNew.itemsCombine.get(0);
            Item item1 = player.combineNew.itemsCombine.get(1);
            Item item2 = player.combineNew.itemsCombine.get(2);
                        if ((item1.isSKH() && item2.isDTL() )) {
            int couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 1
                    : item.template.id == 561 ? 1 : 1;
            sendEffectSuccessCombine(player);
            Item dangusac = ItemService.gI().createNewItem((short) 1465, 3);
            InventoryServiceNew.gI().addItemBag(player, dangusac);

            InventoryServiceNew.gI().sendItemBags(player);

            Service.gI().sendThongBaoOK(player, "Mày Nhận Được 3 Đá Nâng SKH");
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);

            player.combineNew.itemsCombine.clear();
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            reOpenItemCombine(player);
        }
        }
    }

    private void phanradothanlinhx5(Player player) {
        if (player.combineNew.itemsCombine.size() == 5) {
            player.inventory.gold -= 20000000;
            List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
            Item item = player.combineNew.itemsCombine.get(0);
            Item item1 = player.combineNew.itemsCombine.get(1);
            Item item2 = player.combineNew.itemsCombine.get(2);
            Item item3 = player.combineNew.itemsCombine.get(3);
            Item item4 = player.combineNew.itemsCombine.get(4);
                      if ((item1.isSKH() && item2.isDTL() && item3.isDTL() && item4.isDTL() )) {             
            int couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 1
                    : item.template.id == 561 ? 1 : 1;
            sendEffectSuccessCombine(player);
            Item dangusac = ItemService.gI().createNewItem((short) 1465, 5);
            InventoryServiceNew.gI().addItemBag(player, dangusac);

            InventoryServiceNew.gI().sendItemBags(player);

            Service.gI().sendThongBaoOK(player, "Mày Nhận Được 5 Đá Nâng SKH");
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item4, 1);

            player.combineNew.itemsCombine.clear();
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            reOpenItemCombine(player);
        }
        }
    }

    public void nangcapskhtllvl0(Player player) {
        if (player.combineNew.itemsCombine.size() == 6) {
            Item item1 = player.combineNew.itemsCombine.get(0);
            Item item2 = player.combineNew.itemsCombine.get(1);
            Item item3 = player.combineNew.itemsCombine.get(2);
            Item item4 = player.combineNew.itemsCombine.get(3);
            Item item5 = player.combineNew.itemsCombine.get(4);
            Item item6 = player.combineNew.itemsCombine.get(5);

            if ((item1.isDHD() && item2.isDHD() && item3.isDHD() && item4.isDHD() && item5.isDHD() && item6.isDNS1())) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {

                    int tile = 100; // tỉ lệ
                    if (Util.isTrue(tile, 100)) {
                        int type = item1.template.type;
                        int[][][] items = {{{555},// áo
                        {556}, // quần
                        {562}, // găng
                        {563}, // giày
                        {561}}, // rada

                        {{557}, // nm
                        {558},
                        {564},
                        {565},
                        {561}},
                        {{559}, // xd
                        {560},
                        {566},
                        {567},
                        {561}}};
                        int[][] options = {{128, 129, 127}, {130, 131, 132}, {133, 135, 134}};
                        int skhv1 = 25;// ti le
                        int skhv2 = 35;//ti le
                        int skhc = 40;//ti le
                        int skhId = -1;
                        int rd = Util.nextInt(1, 100);
                        if (rd <= skhv1) {
                            skhId = 0;
                        } else if (rd <= skhv1 + skhv2) {
                            skhId = 1;
                        } else if (rd <= skhv1 + skhv2 + skhc) {
                            skhId = 2;
                        }
                        Item item = null;
                        switch (player.gender) {
                            case 0:
                                Random rand0 = new Random();
                                int i0 = rand0.nextInt(items.length);
                                int j0 = rand0.nextInt(items[0].length);
                                int k0 = rand0.nextInt(items[0][item1.template.type].length);
                                int randomThirdElement0 = items[0][item1.template.type][k0];
                                item = ItemService.gI().itemSKH(randomThirdElement0, options[0][skhId]);
                                break;
                            case 1:
                                Random rand1 = new Random();
                                int i1 = rand1.nextInt(items.length);
                                int j1 = rand1.nextInt(items[1].length);
                                int k1 = rand1.nextInt(items[1][item1.template.type].length);
                                int randomThirdElement1 = items[1][item1.template.type][k1];
                                item = ItemService.gI().itemSKH(randomThirdElement1, options[1][skhId]);
                                break;
                            case 2:
                                Random rand2 = new Random();
                                int i2 = rand2.nextInt(items.length);
                                int j2 = rand2.nextInt(items[2].length);
                                int k2 = rand2.nextInt(items[2][item1.template.type].length);
                                int randomThirdElement2 = items[2][item1.template.type][k2];
                                item = ItemService.gI().itemSKH(randomThirdElement2, options[2][skhId]);
                                break;
                        }
                        if (item != null) {
                            sendEffectSuccessCombine(player);
                            InventoryServiceNew.gI().addItemBag(player, item);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item4, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item5, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item6, 10);

                            InventoryServiceNew.gI().sendItemBags(player);
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
                    player.combineNew.itemsCombine.clear();
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang");
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);

            } else {
                Service.getInstance().sendThongBao(player, "Cần 5 món Hủy Diệt cùng loại");
            }
        }
    }

    public void nangcapskhtllvl1(Player player) {
        if (player.combineNew.itemsCombine.size() == 7) {
            Item item1 = player.combineNew.itemsCombine.get(0);
            Item item2 = player.combineNew.itemsCombine.get(1);
            Item item3 = player.combineNew.itemsCombine.get(2);
            Item item4 = player.combineNew.itemsCombine.get(3);
            Item item5 = player.combineNew.itemsCombine.get(4);
            Item item6 = player.combineNew.itemsCombine.get(5);
            Item item7 = player.combineNew.itemsCombine.get(6);

            Item trangbiskhtl = player.combineNew.itemsCombine.stream().filter(Item::isSKHTLLVL0).findFirst()
                    .get();
            if ((item1.isSKHTLLVL0() && item2.isDHD() && item3.isDHD() && item4.isDHD() && item5.isDHD() && item6.isDHD() && item7.isDNS1())) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {

                    int tile = 30; // tỉ lệ
                    if (Util.isTrue(tile, 100)) {
                        int type = item1.template.type;
                        int[][][] items = {{{555},// áo
                        {556}, // quần
                        {562}, // găng
                        {563}, // giày
                        {561}}, // rada

                        {{557}, // nm
                        {558},
                        {564},
                        {565},
                        {561}},
                        {{559}, // xd
                        {560},
                        {566},
                        {567},
                        {561}}};
                        int skhId = -1;
                        int[][] options = {{225, 226, 224}, {227, 228, 229}, {230, 232, 231}};
                        List<Integer> idOption = Arrays.asList(127, 128, 129, 130, 131, 132, 133, 134, 135);
                        ItemOption option_127 = new ItemOption();
                        ItemOption option_128 = new ItemOption();
                        ItemOption option_129 = new ItemOption();
                        ItemOption option_130 = new ItemOption();

                        ItemOption option_131 = new ItemOption();
                        ItemOption option_132 = new ItemOption();
                        ItemOption option_133 = new ItemOption();
                        ItemOption option_134 = new ItemOption();
                        ItemOption option_135 = new ItemOption();
                        for (ItemOption itopt : trangbiskhtl.itemOptions) {
                            if (itopt.optionTemplate.id == 127) {
                                skhId = 2;
                                option_127 = itopt;
                            }
                            if (itopt.optionTemplate.id == 128) {
                                skhId = 0;
                                option_128 = itopt;
                            }
                            if (itopt.optionTemplate.id == 129) {
                                skhId = 1;
                                option_129 = itopt;
                            }
                            if (itopt.optionTemplate.id == 130) {
                                skhId = 0;
                                option_130 = itopt;
                            }
                            if (itopt.optionTemplate.id == 131) {
                                skhId = 1;
                                option_131 = itopt;
                            }
                            if (itopt.optionTemplate.id == 132) {
                                skhId = 2;
                                option_132 = itopt;
                            }
                            if (itopt.optionTemplate.id == 133) {
                                skhId = 0;
                                option_133 = itopt;
                            }
                            if (itopt.optionTemplate.id == 134) {
                                skhId = 2;
                                option_134 = itopt;
                            }
                            if (itopt.optionTemplate.id == 135) {
                                skhId = 1;
                                option_135 = itopt;
                            }

                        }

                        Item item = null;
                        switch (player.gender) {
                            case 0:
                                Random rand0 = new Random();
                                int i0 = rand0.nextInt(items.length);
                                int j0 = rand0.nextInt(items[0].length);
                                int k0 = rand0.nextInt(items[0][item1.template.type].length);
                                int randomThirdElement0 = items[0][item1.template.type][k0];
                                item = ItemService.gI().itemSKH(randomThirdElement0, options[0][skhId]);
                                break;
                            case 1:
                                Random rand1 = new Random();
                                int i1 = rand1.nextInt(items.length);
                                int j1 = rand1.nextInt(items[1].length);
                                int k1 = rand1.nextInt(items[1][item1.template.type].length);
                                int randomThirdElement1 = items[1][item1.template.type][k1];
                                item = ItemService.gI().itemSKH(randomThirdElement1, options[1][skhId]);
                                break;
                            case 2:
                                Random rand2 = new Random();
                                int i2 = rand2.nextInt(items.length);
                                int j2 = rand2.nextInt(items[2].length);
                                int k2 = rand2.nextInt(items[2][item1.template.type].length);
                                int randomThirdElement2 = items[2][item1.template.type][k2];
                                item = ItemService.gI().itemSKH(randomThirdElement2, options[2][skhId]);
                                break;
                        }
                        if (item != null) {
                            sendEffectSuccessCombine(player);
                            InventoryServiceNew.gI().addItemBag(player, item);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item4, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item5, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item6, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item7, 20);

                            InventoryServiceNew.gI().sendItemBags(player);
                        }
                    } else {

                        InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item4, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item5, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item6, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item7, 18);

                        InventoryServiceNew.gI().sendItemBags(player);
                        sendEffectFailCombine(player);
                    }
                    player.combineNew.itemsCombine.clear();
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang");
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);

            } else {
                Service.getInstance().sendThongBao(player, "Cần 1 món skh thần [lvl0] và 5 món Hủy Diệt cùng loại");
            }
        }
    }

    public void nangcapskhtllvl2(Player player) {
        if (player.combineNew.itemsCombine.size() == 7) {
            Item item1 = player.combineNew.itemsCombine.get(0);
            Item item2 = player.combineNew.itemsCombine.get(1);
            Item item3 = player.combineNew.itemsCombine.get(2);
            Item item4 = player.combineNew.itemsCombine.get(3);
            Item item5 = player.combineNew.itemsCombine.get(4);
            Item item6 = player.combineNew.itemsCombine.get(5);
            Item item7 = player.combineNew.itemsCombine.get(6);
            Item trangbiskhtl = player.combineNew.itemsCombine.stream().filter(Item::isSKHTLLVL1).findFirst()
                    .get();

            if ((item1.isSKHTLLVL1() && item2.isDHD() && item3.isDHD() && item4.isDHD() && item5.isDHD() && item6.isDHD() && item7.isDNS1())) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {

                    int tile = 30; // tỉ lệ
                    if (Util.isTrue(tile, 100)) {
                        int type = item1.template.type;
                        int[][][] items = {{{555},// áo
                        {556}, // quần
                        {562}, // găng
                        {563}, // giày
                        {561}}, // rada

                        {{557}, // nm
                        {558},
                        {564},
                        {565},
                        {561}},
                        {{559}, // xd
                        {560},
                        {566},
                        {567},
                        {561}}};
                        int[][] options = {{198, 197, 199}, {200, 201, 202}, {203, 204, 205}};
                        List<Integer> idOption = Arrays.asList(224, 225, 226, 227, 228, 229, 230, 231, 232);
                        ItemOption option_224 = new ItemOption();
                        ItemOption option_225 = new ItemOption();

                        ItemOption option_226 = new ItemOption();

                        ItemOption option_227 = new ItemOption();

                        ItemOption option_228 = new ItemOption();
                        ItemOption option_229 = new ItemOption();
                        ItemOption option_230 = new ItemOption();
                        ItemOption option_231 = new ItemOption();
                        ItemOption option_232 = new ItemOption();
                         int skhId = -1;
                        for (ItemOption itopt : trangbiskhtl.itemOptions) {
                            if (itopt.optionTemplate.id == 224) {
                                  skhId = 1;
                                option_224 = itopt;
                            }
                            if (itopt.optionTemplate.id == 225) {
                                  skhId = 0;
                                option_225 = itopt;
                            }
                            if (itopt.optionTemplate.id == 226) {
                                 skhId = 2;
                                option_226 = itopt;
                            }
                            if (itopt.optionTemplate.id == 227) {
                                    skhId = 0;
                                option_227 = itopt;
                            }
                            if (itopt.optionTemplate.id == 228) {
                                 skhId = 1;
                                option_228 = itopt;
                            }
                            if (itopt.optionTemplate.id == 229) {
                                 skhId = 2;
                                option_229 = itopt;
                            }
                            if (itopt.optionTemplate.id == 230) {
                                skhId = 0;
                                option_230 = itopt;
                            }
                            if (itopt.optionTemplate.id == 231) {
                                 skhId = 2;
                                option_231 = itopt;
                            }
                            if (itopt.optionTemplate.id == 232) {
                                skhId = 1;
                                option_232 = itopt;
                            }

                        }

                       
                        Item item = null;
                        switch (player.gender) {
                            case 0:
                                Random rand0 = new Random();
                                int i0 = rand0.nextInt(items.length);
                                int j0 = rand0.nextInt(items[0].length);
                                int k0 = rand0.nextInt(items[0][item1.template.type].length);
                                int randomThirdElement0 = items[0][item1.template.type][k0];
                                item = ItemService.gI().itemSKH(randomThirdElement0, options[0][skhId]);
                                break;
                            case 1:
                                Random rand1 = new Random();
                                int i1 = rand1.nextInt(items.length);
                                int j1 = rand1.nextInt(items[1].length);
                                int k1 = rand1.nextInt(items[1][item1.template.type].length);
                                int randomThirdElement1 = items[1][item1.template.type][k1];
                                item = ItemService.gI().itemSKH(randomThirdElement1, options[1][skhId]);
                                break;
                            case 2:
                                Random rand2 = new Random();
                                int i2 = rand2.nextInt(items.length);
                                int j2 = rand2.nextInt(items[2].length);
                                int k2 = rand2.nextInt(items[2][item1.template.type].length);
                                int randomThirdElement2 = items[2][item1.template.type][k2];
                                item = ItemService.gI().itemSKH(randomThirdElement2, options[2][skhId]);
                                break;
                        }
                        if (item != null) {
                            sendEffectSuccessCombine(player);
                            InventoryServiceNew.gI().addItemBag(player, item);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item4, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item5, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item6, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item7, 50);

                            InventoryServiceNew.gI().sendItemBags(player);
                        }
                    } else {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item4, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item5, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item6, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item7, 45);

                        InventoryServiceNew.gI().sendItemBags(player);
                        sendEffectFailCombine(player);
                    }
                    player.combineNew.itemsCombine.clear();
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang");
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);

            } else {
                Service.getInstance().sendThongBao(player, "Cần 1 món skh thần [lvl1] và 5 món Hủy Diệt cùng loại");
            }
        }
    }

    public void nangcapskhlvl1(Player player) {
        if (player.combineNew.itemsCombine.size() == 7) {
            Item item1 = player.combineNew.itemsCombine.get(0);
            Item item2 = player.combineNew.itemsCombine.get(1);
            Item item3 = player.combineNew.itemsCombine.get(2);
            Item item4 = player.combineNew.itemsCombine.get(3);
            Item item5 = player.combineNew.itemsCombine.get(4);
            Item item6 = player.combineNew.itemsCombine.get(5);
            Item item7 = player.combineNew.itemsCombine.get(6);
            if ((item1.isSKH() && item2.isDTL() && item3.isDTL() && item4.isDTL() && item5.isDTL() && item6.isDTL() && item7.isDNS())) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {

                    Item trangbiskhtl = player.combineNew.itemsCombine.stream().filter(Item::isSKH).findFirst()
                            .get();
                    int tile = 30; // tỉ lệ
                    if (Util.isTrue(tile, 100)) {
                        int type = item1.template.type;
                        int[][][] items = {{{0},// áo
                        {6}, // quần
                        {21}, // găng
                        {27}, // giày
                        {12}}, // rada

                        {{1}, // nm
                        {7},
                        {22},
                        {28},
                        {12}},
                        {{2}, // xd
                        {8},
                        {23},
                        {29},
                        {12}}};
                        int skhId=-1;
                        int[][] options = {{225, 226, 224}, {227, 228, 229}, {230, 232, 231}};
                        List<Integer> idOption = Arrays.asList(127, 128, 129, 130, 131, 132, 133, 134, 135);
                        ItemOption option_127 = new ItemOption();
                        ItemOption option_128 = new ItemOption();
                        ItemOption option_129 = new ItemOption();
                        ItemOption option_130 = new ItemOption();

                        ItemOption option_131 = new ItemOption();
                        ItemOption option_132 = new ItemOption();
                        ItemOption option_133 = new ItemOption();
                        ItemOption option_134 = new ItemOption();
                        ItemOption option_135 = new ItemOption();
                        for (ItemOption itopt : trangbiskhtl.itemOptions) {
                              if (itopt.optionTemplate.id == 127) {
                                skhId = 2;
                                option_127 = itopt;
                            }
                            if (itopt.optionTemplate.id == 128) {
                                skhId = 0;
                                option_128 = itopt;
                            }
                            if (itopt.optionTemplate.id == 129) {
                                skhId = 1;
                                option_129 = itopt;
                            }
                            if (itopt.optionTemplate.id == 130) {
                                skhId = 0;
                                option_130 = itopt;
                            }
                            if (itopt.optionTemplate.id == 131) {
                                skhId = 1;
                                option_131 = itopt;
                            }
                            if (itopt.optionTemplate.id == 132) {
                                skhId = 2;
                                option_132 = itopt;
                            }
                            if (itopt.optionTemplate.id == 133) {
                                skhId = 0;
                                option_133 = itopt;
                            }
                            if (itopt.optionTemplate.id == 134) {
                                skhId = 2;
                                option_134 = itopt;
                            }
                            if (itopt.optionTemplate.id == 135) {
                                skhId = 1;
                                option_135 = itopt;
                            }

                        }


                        Item item = null;
                        switch (player.gender) {
                            case 0:
                                Random rand0 = new Random();
                                int i0 = rand0.nextInt(items.length);
                                int j0 = rand0.nextInt(items[0].length);
                                int k0 = rand0.nextInt(items[0][item1.template.type].length);
                                int randomThirdElement0 = items[0][item1.template.type][k0];
                                item = ItemService.gI().itemSKH(randomThirdElement0, options[0][skhId]);
                                break;
                            case 1:
                                Random rand1 = new Random();
                                int i1 = rand1.nextInt(items.length);
                                int j1 = rand1.nextInt(items[1].length);
                                int k1 = rand1.nextInt(items[1][item1.template.type].length);
                                int randomThirdElement1 = items[1][item1.template.type][k1];
                                item = ItemService.gI().itemSKH(randomThirdElement1, options[1][skhId]);
                                break;
                            case 2:
                                Random rand2 = new Random();
                                int i2 = rand2.nextInt(items.length);
                                int j2 = rand2.nextInt(items[2].length);
                                int k2 = rand2.nextInt(items[2][item1.template.type].length);
                                int randomThirdElement2 = items[2][item1.template.type][k2];
                                item = ItemService.gI().itemSKH(randomThirdElement2, options[2][skhId]);
                                break;
                        }
                        if (item != null) {
                            sendEffectSuccessCombine(player);
                            InventoryServiceNew.gI().addItemBag(player, item);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item4, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item5, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item6, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item7, 20);
                            InventoryServiceNew.gI().sendItemBags(player);
                        }
                    } else {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item4, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item5, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item6, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item7, 18);

                        InventoryServiceNew.gI().sendItemBags(player);
                        sendEffectFailCombine(player);
                    }
                    player.combineNew.itemsCombine.clear();
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang");
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);

            } else {
                Service.getInstance().sendThongBao(player, "Cần 5 món thần linh cùng loại");
            }
        }
    }

    public void nangcapskhlvl2(Player player) {
        if (player.combineNew.itemsCombine.size() == 7) {
            Item item1 = player.combineNew.itemsCombine.get(0);
            Item item2 = player.combineNew.itemsCombine.get(1);
            Item item3 = player.combineNew.itemsCombine.get(2);
            Item item4 = player.combineNew.itemsCombine.get(3);
            Item item5 = player.combineNew.itemsCombine.get(4);
            Item item6 = player.combineNew.itemsCombine.get(5);
            Item item7 = player.combineNew.itemsCombine.get(6);
            if ((item1.isSKHLVL1() && item2.isDTL() && item3.isDTL() && item4.isDTL() && item5.isDTL() && item6.isDTL() && item7.isDNS())) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    Item trangbiskhtl = player.combineNew.itemsCombine.stream().filter(Item::isSKHLVL1).findFirst()
                            .get();
                    int tile = 30; // tỉ lệ
                    if (Util.isTrue(tile, 100)) {
                        int type = item1.template.type;
                        int[][][] items = {{{0},// áo
                        {6}, // quần
                        {21}, // găng
                        {27}, // giày
                        {12}}, // rada

                        {{1}, // nm
                        {7},
                        {22},
                        {28},
                        {12}},
                        {{2}, // xd
                        {8},
                        {23},
                        {29},
                        {12}}};
                        int[][] options = {{198, 197, 199}, {200, 201, 202}, {203, 204, 205}};
                        List<Integer> idOption = Arrays.asList(224, 225, 226, 227, 228, 229, 230, 231, 232);
                        ItemOption option_224 = new ItemOption();
                        ItemOption option_225 = new ItemOption();

                        ItemOption option_226 = new ItemOption();

                        ItemOption option_227 = new ItemOption();

                        ItemOption option_228 = new ItemOption();
                        ItemOption option_229 = new ItemOption();
                        ItemOption option_230 = new ItemOption();
                        ItemOption option_231 = new ItemOption();
                        ItemOption option_232 = new ItemOption();
                       int skhId = -1;
                        for (ItemOption itopt : trangbiskhtl.itemOptions) {
                            if (itopt.optionTemplate.id == 224) {
                                  skhId = 1;
                                option_224 = itopt;
                            }
                            if (itopt.optionTemplate.id == 225) {
                                  skhId = 0;
                                option_225 = itopt;
                            }
                            if (itopt.optionTemplate.id == 226) {
                                 skhId = 2;
                                option_226 = itopt;
                            }
                            if (itopt.optionTemplate.id == 227) {
                                    skhId = 0;
                                option_227 = itopt;
                            }
                            if (itopt.optionTemplate.id == 228) {
                                 skhId = 1;
                                option_228 = itopt;
                            }
                            if (itopt.optionTemplate.id == 229) {
                                 skhId = 2;
                                option_229 = itopt;
                            }
                            if (itopt.optionTemplate.id == 230) {
                                skhId = 0;
                                option_230 = itopt;
                            }
                            if (itopt.optionTemplate.id == 231) {
                                 skhId = 2;
                                option_231 = itopt;
                            }
                            if (itopt.optionTemplate.id == 232) {
                                skhId = 1;
                                option_232 = itopt;
                            }

                        }

                        Item item = null;
                        switch (player.gender) {
                            case 0:
                                Random rand0 = new Random();
                                int i0 = rand0.nextInt(items.length);
                                int j0 = rand0.nextInt(items[0].length);
                                int k0 = rand0.nextInt(items[0][item1.template.type].length);
                                int randomThirdElement0 = items[0][item1.template.type][k0];
                                item = ItemService.gI().itemSKH(randomThirdElement0, options[0][skhId]);
                                break;
                            case 1:
                                Random rand1 = new Random();
                                int i1 = rand1.nextInt(items.length);
                                int j1 = rand1.nextInt(items[1].length);
                                int k1 = rand1.nextInt(items[1][item1.template.type].length);
                                int randomThirdElement1 = items[1][item1.template.type][k1];
                                item = ItemService.gI().itemSKH(randomThirdElement1, options[1][skhId]);
                                break;
                            case 2:
                                Random rand2 = new Random();
                                int i2 = rand2.nextInt(items.length);
                                int j2 = rand2.nextInt(items[2].length);
                                int k2 = rand2.nextInt(items[2][item1.template.type].length);
                                int randomThirdElement2 = items[2][item1.template.type][k2];
                                item = ItemService.gI().itemSKH(randomThirdElement2, options[2][skhId]);
                                break;
                        }
                        if (item != null) {
                            sendEffectSuccessCombine(player);
                            InventoryServiceNew.gI().addItemBag(player, item);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item4, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item5, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item6, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item7, 50);
                            InventoryServiceNew.gI().sendItemBags(player);
                        }
                    } else {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item4, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item5, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item6, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item7, 45);
                        InventoryServiceNew.gI().sendItemBags(player);
                        sendEffectFailCombine(player);
                    }
                    player.combineNew.itemsCombine.clear();
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang");
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);

            } else {
                Service.getInstance().sendThongBao(player, "Cần 5 món thần linh cùng loại");
            }
        }
    }

    private void epSaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item trangBi = null;
            Item daPhaLe = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiPhaLeHoa(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
            }
            int star = 0; //sao pha lê đã ép
            int starEmpty = 0; //lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                if (star < starEmpty) {
                    player.inventory.gem -= gem;
                    int optionId = getOptionDaPhaLe(daPhaLe);
                    int param = getParamDaPhaLe(daPhaLe);
                    Item.ItemOption option = null;
                    for (Item.ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(optionId, param));
                    }
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(102, 1));
                    }

                    InventoryServiceNew.gI().subQuantityItemsBag(player, daPhaLe, 1);
                    sendEffectSuccessCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phaLeHoaTrangBi(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty() && player.combineNew.itemsCombine.size() < 2) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            } else if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                int star = 0;
                int param = 0;
                Item.ItemOption optionStar = null;
                for (Item.ItemOption ts : item.itemOptions) {
                    if (ts.optionTemplate.id == 238) {
                        param = ts.param;
                    }
                }
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
                    byte ratio = (optionStar != null && optionStar.param > 4) ? (byte) 2 : 1;
                    if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
                        if (optionStar == null) {
                            item.itemOptions.add(new Item.ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        for (Item.ItemOption io : item.itemOptions) {
                            if (optionStar != null && optionStar.param == 9) {
                                if (io.optionTemplate.id == 238) {
                                    item.itemOptions.remove(io);
                                    break;
                                }
                            }
                        }
                        sendEffectSuccessCombine(player);
                        if (optionStar != null && optionStar.param >= 7) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                    + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                        }
                    } else {
                        Item.ItemOption xit = null;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 238) {
                                xit = io;
                                break;
                            }
                        }
                        if (xit == null) {
                            item.itemOptions.add(new Item.ItemOption(238, 1));
                        } else {
                            xit.param++;
                        }
                        sendEffectFailCombine(player);
                    }
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    public void phaLeHoaTrangBiVIP(Player player, int menu) {
        if (!player.combineNew.itemsCombine.isEmpty() && player.combineNew.itemsCombine.size() == 1) {
            try {
                while (menu > 0) {
                    menu--;
                    int gold = player.combineNew.goldCombine;
                    int gem = player.combineNew.gemCombine;
                    if (player.inventory.gem < gem) {
                        Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                        break;
                    }
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        int star = 0;
                        int param = 0;
                        Item.ItemOption optionStar = null;
                        for (Item.ItemOption ts : item.itemOptions) {
                            if (ts.optionTemplate.id == 238) {
                                param = ts.param;
                            }
                        }
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                optionStar = io;
                            }
                        }
                        player.combineNew.ratioCombine = CombineServiceNew.gI().getRatioPhaLeHoa(star) + CombineServiceNew.gI().Tilecongdon(param);
                        player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                        player.combineNew.gemCombine = getGemPhaLeHoa(star);
                        String npcSay = item.template.name + "\n|2|";
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id != 102) {
                                npcSay += io.getOptionString() + "\n";
                            }
                        }
                        npcSay += "|7|Tỉ lệ thành công : " + CombineServiceNew.gI().getRatioPhaLeHoa(star) + "% " + "\n";
                        npcSay += "|7|(Bonus " + CombineServiceNew.gI().Tilecongdon(param) + "%)" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\nVIP X1\n" + player.combineNew.gemCombine + " Ngọc",
                                    "Nâng cấp\nVIP X10\n" + player.combineNew.gemCombine * 10 + " Ngọc",
                                    "Nâng cấp\nVIP X100\n" + player.combineNew.gemCombine * 100 + " Ngọc");
                        } else {
                            player.sovangauto = 500000000;
                            player.autobv = true;
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay
                                    + "\n|1|HẾT VÀNG, ĐANG TIẾN HÀNH AUTO BÁN THỎI VÀNG!",
                                    "Nâng cấp X1\ncần " + player.combineNew.gemCombine + " Ngọc",
                                    "Nâng cấp X10\ncần " + player.combineNew.gemCombine * 10 + " Ngọc", "Nâng cấp X100\ncần " + player.combineNew.gemCombine * 100 + " Ngọc");
                            Thread.sleep(2500);
                            if (player.autobv == false) {
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay
                                        + "\n|1|Dừng Auto, Không tìm thấy thỏi vàng trong hành trang!", "Đóng");
                                break;
                            }
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay
                                    + "\n|1|BÁN XONG, ĐANG CHUẨN BỊ TIẾP TỤC NÂNG CẤP!",
                                    "Nâng cấp X1\ncần " + player.combineNew.gemCombine + " Ngọc",
                                    "Nâng cấp X10\ncần " + player.combineNew.gemCombine * 10 + " Ngọc", "Nâng cấp X100\ncần " + player.combineNew.gemCombine * 100 + " Ngọc");
                            Thread.sleep(1000);
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.inventory.gold -= gold;
                            player.inventory.gem -= gem;
                            byte ratio = (optionStar != null && optionStar.param > 4) ? (byte) 2 : 1;
                            if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
                                if (optionStar == null) {
                                    item.itemOptions.add(new Item.ItemOption(107, 1));
                                } else {
                                    optionStar.param++;
                                }
                                for (Item.ItemOption io : item.itemOptions) {
                                    if (optionStar != null && optionStar.param == 7) {
                                        if (io.optionTemplate.id == 238) {
                                            item.itemOptions.remove(io);
                                            break;
                                        }
                                    }
                                }
                                sendEffectSuccessCombine(player);
                                if (optionStar != null && optionStar.param >= 7) {
                                    ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                            + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                                }
                            } else {
                                Item.ItemOption xit = null;
                                for (Item.ItemOption io : item.itemOptions) {
                                    if (io.optionTemplate.id == 238) {
                                        xit = io;
                                        break;
                                    }
                                }
                                if (xit == null) {
                                    item.itemOptions.add(new Item.ItemOption(238, 1));
                                } else {
                                    xit.param++;
                                }
                                sendEffectFailCombine(player);
                            }
                        } else {
                            break;
                        }
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendMoney(player);
                        reOpenItemCombine(player);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private void phaLeHoaTrangBi8s(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty() && player.combineNew.itemsCombine.size() < 4) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1503 && item.quantity >= 1).count() != 1) {
                Service.getInstance().sendThongBao(player, "Thiếu Dùi Đục");
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1504 && item.quantity >= 3).count() != 1) {
                Service.getInstance().sendThongBao(player, "Thiếu Đá Homet");
                return;
            }

            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            } else if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            Item item1 = player.combineNew.itemsCombine.get(1);
            Item item2 = player.combineNew.itemsCombine.get(2);
            if (isTrangBiPhaLeHoa(item) && isDuiDuc(item1) && isDaHomet(item2)) {
                if (isTrangBiPhaLeHoa(item)) {
                    int star = 7;
                    int param = 0;
                    Item.ItemOption optionStar = null;
                    for (Item.ItemOption ts : item.itemOptions) {
                        if (ts.optionTemplate.id == 238) {
                            param = ts.param;
                        }
                    }
                    for (Item.ItemOption io : item.itemOptions) {
                        if (io.optionTemplate.id == 107) {
                            star = io.param;
                            optionStar = io;
                            break;
                        }
                    }
                    if (star < MAX_STAR_ITEM_VIP) {

                        player.inventory.gold -= gold;
                        player.inventory.gem -= gem;

                        if (Util.isTrue(5, 100)) {
                            if (optionStar == null) {
                                item.itemOptions.add(new Item.ItemOption(107, 1));
                            } else {
                                optionStar.param++;
                            }
                            for (Item.ItemOption io : item.itemOptions) {
                                if (optionStar != null && optionStar.param == 9) {
                                    if (io.optionTemplate.id == 238) {
                                        item.itemOptions.remove(io);
                                        break;
                                    }
                                }
                            }
                            sendEffectSuccessCombine(player);
                            if (optionStar != null && optionStar.param >= 8) {
                                ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                        + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                            }
                        } else {
                            Item.ItemOption xit = null;
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id == 238) {
                                    xit = io;
                                    break;
                                }
                            }
                            if (xit == null) {
                                item.itemOptions.add(new Item.ItemOption(238, 1));
                            } else {
                                xit.param++;
                            }
                            sendEffectFailCombine(player);
                        }
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 3);
                        InventoryServiceNew.gI().sendItemBags(player);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            } else {
                Service.gI().sendThongBao(player, "Trang bị không phù hợp");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
    }

//    public void phaLeHoaTrangBiVIP8s(Player player, int menu) {
//        if (!player.combineNew.itemsCombine.isEmpty() && player.combineNew.itemsCombine.size() == 1) {
//            try {
//                while (menu > 0) {
//                    menu--;
//                    int gold = player.combineNew.goldCombine;
//                    int gem = player.combineNew.gemCombine;
//                    if (player.inventory.gem < gem) {
//                        Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
//                        break;
//                    }
//                    Item item = player.combineNew.itemsCombine.get(0);
//                    if (isTrangBiPhaLeHoa(item)) {
//                        int star = 7;
//                        int param = 0;
//                        Item.ItemOption optionStar = null;
//                        for (Item.ItemOption ts : item.itemOptions) {
//                            if (ts.optionTemplate.id == 238) {
//                                param = ts.param;
//                            }
//                        }
//                        for (Item.ItemOption io : item.itemOptions) {
//                            if (io.optionTemplate.id == 107) {
//                                star = io.param;
//                                optionStar = io;
//                            }
//                        }
//                        player.combineNew.ratioCombine = CombineServiceNew.gI().getRatioPhaLeHoa(star) + CombineServiceNew.gI().Tilecongdon(param);
//                        player.combineNew.goldCombine = getGoldPhaLeHoa(star);
//                        player.combineNew.gemCombine = getGemPhaLeHoa(star);
//                        String npcSay = item.template.name + "\n|2|";
//                        for (Item.ItemOption io : item.itemOptions) {
//                            if (io.optionTemplate.id != 102) {
//                                npcSay += io.getOptionString() + "\n";
//                            }
//                        }
//                        npcSay += "|7|Tỉ lệ thành công : " + CombineServiceNew.gI().getRatioPhaLeHoa(star) + "% " + "\n";
//                        npcSay += "|7|(Bonus " + CombineServiceNew.gI().Tilecongdon(param) + "%)" + "\n";
//                        if (player.combineNew.goldCombine <= player.inventory.gold) {
//                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
//                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
//                                    "Nâng cấp\nVIP X1\n" + player.combineNew.gemCombine + " Ngọc",
//                                    "Nâng cấp\nVIP X10\n" + player.combineNew.gemCombine * 10 + " Ngọc",
//                                    "Nâng cấp\nVIP X100\n" + player.combineNew.gemCombine * 100 + " Ngọc");
//                        } else {
//                            player.sovangauto = 500000000;
//                            player.autobv = true;
//                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay
//                                    + "\n|1|HẾT VÀNG, ĐANG TIẾN HÀNH AUTO BÁN THỎI VÀNG!",
//                                    "Nâng cấp X1\ncần " + player.combineNew.gemCombine + " Ngọc",
//                                    "Nâng cấp X10\ncần " + player.combineNew.gemCombine * 10 + " Ngọc", "Nâng cấp X100\ncần " + player.combineNew.gemCombine * 100 + " Ngọc");
//                            Thread.sleep(2500);
//                            if (player.autobv == false) {
//                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay
//                                        + "\n|1|Dừng Auto, Không tìm thấy thỏi vàng trong hành trang!", "Đóng");
//                                break;
//                            }
//                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay
//                                    + "\n|1|BÁN XONG, ĐANG CHUẨN BỊ TIẾP TỤC NÂNG CẤP!",
//                                    "Nâng cấp X1\ncần " + player.combineNew.gemCombine + " Ngọc",
//                                    "Nâng cấp X10\ncần " + player.combineNew.gemCombine * 10 + " Ngọc", "Nâng cấp X100\ncần " + player.combineNew.gemCombine * 100 + " Ngọc");
//                            Thread.sleep(1000);
//                        }
//                        if (star < MAX_STAR_ITEM_VIP) {
//                            player.inventory.gold -= gold;
//                            player.inventory.gem -= gem;
//                            byte ratio = (optionStar != null && optionStar.param > 4) ? (byte) 2 : 1;
//                            if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
//                                if (optionStar == null) {
//                                    item.itemOptions.add(new Item.ItemOption(107, 1));
//                                } else {
//                                    optionStar.param++;
//                                }
//                                for (Item.ItemOption io : item.itemOptions) {
//                                    if (optionStar != null && optionStar.param == 9) {
//                                        if (io.optionTemplate.id == 238) {
//                                            item.itemOptions.remove(io);
//                                            break;
//                                        }
//                                    }
//                                }
//                                sendEffectSuccessCombine(player);
//                                if (optionStar != null && optionStar.param >= 8) {
//                                    ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
//                                            + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
//                                }
//                            } else {
//                                Item.ItemOption xit = null;
//                                for (Item.ItemOption io : item.itemOptions) {
//                                    if (io.optionTemplate.id == 238) {
//                                        xit = io;
//                                        break;
//                                    }
//                                }
//                                if (xit == null) {
//                                    item.itemOptions.add(new Item.ItemOption(238, 1));
//                                } else {
//                                    xit.param++;
//                                }
//                                sendEffectFailCombine(player);
//                            }
//                        } else {
//                            break;
//                        }
//                        InventoryServiceNew.gI().sendItemBags(player);
//                        Service.gI().sendMoney(player);
//                        reOpenItemCombine(player);
//                    }
//                }
//            } catch (Exception e) {
//            }
//        }
//    }
    private void nhapNgocRong(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty() && player.combineNew.itemsCombine.size() == 1) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                    sendEffectCombineDB(player, item.template.iconID);
                    Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
                    InventoryServiceNew.gI().addItemBag(player, nr);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 7);
                    InventoryServiceNew.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void nhapspl(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty() && player.combineNew.itemsCombine.size() == 1) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem() && (item.template.id == 20) && item.quantity >= 7) {
                    sendEffectCombineDB(player, item.template.iconID);
                    Item nr = ItemService.gI().createNewItem((short) (1413));
                    nr.itemOptions.add(new Item.ItemOption(30, 0));
                    InventoryServiceNew.gI().addItemBag(player, nr);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 7);
                    InventoryServiceNew.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                } else if (item != null && item.isNotNullItem() && (item.template.id == 19) && item.quantity >= 7) {
                    sendEffectCombineDB(player, item.template.iconID);
                    Item nr = ItemService.gI().createNewItem((short) (1414));
                    nr.itemOptions.add(new Item.ItemOption(30, 0));
                    InventoryServiceNew.gI().addItemBag(player, nr);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 7);
                    InventoryServiceNew.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                } else if (item != null && item.isNotNullItem() && (item.template.id == 16) && item.quantity >= 7) {
                    sendEffectCombineDB(player, item.template.iconID);
                    Item nr = ItemService.gI().createNewItem((short) (1412));
                    nr.itemOptions.add(new Item.ItemOption(30, 0));
                    InventoryServiceNew.gI().addItemBag(player, nr);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 7);
                    InventoryServiceNew.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                } else if (item != null && item.isNotNullItem() && (item.template.id == 15) && item.quantity >= 7) {
                    sendEffectCombineDB(player, item.template.iconID);
                    Item nr = ItemService.gI().createNewItem((short) (1416));
                    nr.itemOptions.add(new Item.ItemOption(30, 0));
                    InventoryServiceNew.gI().addItemBag(player, nr);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 7);
                    InventoryServiceNew.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                } else if (item != null && item.isNotNullItem() && (item.template.id == 14) && item.quantity >= 7) {
                    sendEffectCombineDB(player, item.template.iconID);
                    Item nr = ItemService.gI().createNewItem((short) (1415));
                    nr.itemOptions.add(new Item.ItemOption(30, 0));
                    InventoryServiceNew.gI().addItemBag(player, nr);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 7);
                    InventoryServiceNew.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void nhapdaskh(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty() && player.combineNew.itemsCombine.size() == 1) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem() && (item.template.id == 1465) && item.quantity >= 10) {
                    sendEffectCombineDB(player, item.template.iconID);
                    Item nr = ItemService.gI().createNewItem((short) (1466));
                    InventoryServiceNew.gI().addItemBag(player, nr);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 10);
                    InventoryServiceNew.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    public void nangCapVatPham(Player player, int menu) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            try {
                while (menu > 0) {
                    menu--;
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() != 1) {
                        return;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                        return;//admin
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                        int countDaNangCap = player.combineNew.countDaNangCap;
                        int gold = player.combineNew.goldCombine;
                        short countDaBaoVe = player.combineNew.countDaBaoVe;
                        if (itemDNC.quantity < countDaNangCap) {
                            return;
                        }
                        if (player.combineNew.itemsCombine.size() == 3) {
                            if (Objects.isNull(itemDBV)) {
                                return;
                            }
                            if (itemDBV.quantity < countDaBaoVe) {
                                return;
                            }
                        }

                        int level = 0;
                        Item.ItemOption optionLevel = null;
                        for (Item.ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                optionLevel = io;
                                break;
                            }
                        }

                        if (level < MAX_LEVEL_ITEM) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String optionit = null;
                            int param = 0;
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 47
                                        || io.optionTemplate.id == 6
                                        || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7
                                        || io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 22
                                        || io.optionTemplate.id == 23) {
                                    optionit = io.optionTemplate.name;
                                    param = io.param + (io.param * 10 / 100);
                                    break;
                                }
                            }
                            npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                    + optionit.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\n%s ĐBV", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                                break;
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                                break;
                            }
                            if (player.combineNew.goldCombine < player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng cấp\nX1\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC,
                                        "Nâng cấp\nX10\n" + Util.numberToMoney(player.combineNew.goldCombine * 10) + " vàng" + daNPC,
                                        "Nâng cấp\nX100\n" + Util.numberToMoney(player.combineNew.goldCombine * 100) + " vàng" + daNPC);
                            } else {
                                player.sovangauto = 500000000;
                                player.autobv = true;
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay
                                        + "\n|1|HẾT VÀNG, ĐANG TIẾN HÀNH AUTO BÁN THỎI VÀNG!",
                                        "Nâng cấp\nX1\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC,
                                        "Nâng cấp\nX10\n" + Util.numberToMoney(player.combineNew.goldCombine * 10) + " vàng" + daNPC,
                                        "Nâng cấp\nX100\n" + Util.numberToMoney(player.combineNew.goldCombine * 100) + " vàng" + daNPC);
                                Thread.sleep(2500);
                                if (player.autobv == false) {
                                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay
                                            + "\n|1|Dừng Auto, Không tìm thấy thỏi vàng trong hành trang!", "Đóng");
                                    break;
                                }
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay
                                        + "\n|1|BÁN XONG, ĐANG CHUẨN BỊ TIẾP TỤC NÂNG CẤP!",
                                        "Nâng cấp\nX1\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC,
                                        "Nâng cấp\nX10\n" + Util.numberToMoney(player.combineNew.goldCombine * 10) + " vàng" + daNPC,
                                        "Nâng cấp\nX100\n" + Util.numberToMoney(player.combineNew.goldCombine * 100) + " vàng" + daNPC);
                                Thread.sleep(1000);
                            }
                            player.inventory.gold -= gold;
                            Item.ItemOption option = null;
                            Item.ItemOption option2 = null;
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 47
                                        || io.optionTemplate.id == 6
                                        || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7
                                        || io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 22
                                        || io.optionTemplate.id == 23) {
                                    option = io;
                                } else if (io.optionTemplate.id == 27
                                        || io.optionTemplate.id == 28) {
                                    option2 = io;
                                }
                            }
                            if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                                option.param += (option.param * 10 / 100);
                                if (option2 != null) {
                                    option2.param += (option2.param * 10 / 100);
                                }
                                if (optionLevel == null) {
                                    itemDo.itemOptions.add(new Item.ItemOption(72, 1));
                                } else {
                                    optionLevel.param++;
                                }
//                        if (optionLevel != null && optionLevel.param >= 5) {
//                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp "
//                                    + "thành công " + trangBi.template.name + " lên +" + optionLevel.param);
//                        }
                                sendEffectSuccessCombine(player);
                            } else {
                                if ((level == 2 || level == 4 || level == 6) && (player.combineNew.itemsCombine.size() != 3)) {
                                    option.param -= (option.param * 10 / 100);
                                    if (option2 != null) {
                                        option2.param -= (option2.param * 10 / 100);
                                    }
                                    optionLevel.param--;
                                }
                                sendEffectFailCombine(player);
                            }
                            if (player.combineNew.itemsCombine.size() == 3) {
                                InventoryServiceNew.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                            }
                            InventoryServiceNew.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.gI().sendMoney(player);
                            reOpenItemCombine(player);
                        } else {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private void doidiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(663, 664, 665, 666, 667));
            int couponAdd = 0;
            Item item = player.combineNew.itemsCombine.get(0);
            if (item.isNotNullItem()) {
                if (item.template.id >= 663 && item.template.id <= 667) {
                    couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 2 : item.template.id <= 665 ? 1 : 1;
                }
            }
            if (item.quantity < 99) {
                this.bill.createOtherMenu(player, ConstNpc.IGNORE_MENU, "THỨC ĂN ĐÂUUU!!!", "Đóng");
            } else if (item.quantity >= 99) {
                Item dhd = ItemService.gI().createNewItem((short) Util.nextInt(1327, 1331), couponAdd);
                InventoryServiceNew.gI().subQuantityItemsBag(player, item, 99);
                InventoryServiceNew.gI().addItemBag(player, dhd);
                InventoryServiceNew.gI().sendItemBags(player);
                player.inventory.gold -= 500000000;
                sendEffectSuccessCombine(player);
                Service.gI().sendMoney(player);
            }
            reOpenItemCombine(player);
            Service.gI().sendThongBao(player, "Nhận Được x1 Mảnh hủy diệt ");
        }
    }

    private void thuedo(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            long gold = 500000000;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            Item dothue = null;
            Item vethuedo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isDoThue()) {
                    dothue = item;
                } else if (item.template.id == 1257) {
                    vethuedo = item;
                }
            }
            if (dothue != null && vethuedo != null && vethuedo.quantity >= 1) {
                Item.ItemOption option = null;
                for (Item.ItemOption it : dothue.itemOptions) {
                    if (it.optionTemplate.id == 93) {
                        option = it;
                        break;
                    }
                }
                if (option != null) {
                    option.param += 3;
                } else if (option != null && option.param >= 21) {
                    Service.gI().sendThongBao(player, "Quá số ngày rùi, sài hết rồi gia hạn tiếp");
                    return;
                }
                player.inventory.gold -= gold;
                InventoryServiceNew.gI().subQuantityItemsBag(player, vethuedo, 1);
                sendEffectSuccessCombine(player);
                Service.gI().sendThongBao(player, "Gia Hạn Thành Công!!!");
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapChanMenh(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int diem = player.combineNew.DiemNangcap;
            if (player.pointSb < diem) {
                Service.gI().sendThongBao(player, "Không đủ Điểm Săn Boss để thực hiện");
                return;
            }
            Item chanmenh = null;
            Item dahoangkim = null;
            int capbac = 0;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1318) {
                    dahoangkim = item;
                } else if (item.template.id >= 1300 && item.template.id < 1308) {
                    chanmenh = item;
                    capbac = item.template.id - 1299;
                }
            }
            int soluongda = player.combineNew.DaNangcap;
            if (dahoangkim != null && dahoangkim.quantity >= soluongda) {
                if (chanmenh != null && (chanmenh.template.id >= 1300 && chanmenh.template.id < 1308)) {
                    player.pointSb -= diem;
                    if (Util.isTrue(player.combineNew.TileNangcap, 100)) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dahoangkim, soluongda);
                        sendEffectSuccessCombine(player);
                        switch (capbac) {
                            case 1:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 8));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 10));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 10));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                            case 2:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 10));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 12));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 12));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                            case 3:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 12));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 15));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 15));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                            case 4:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 16));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 18));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 18));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                            case 5:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 18));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 20));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 20));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                            case 6:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 20));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 22));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 22));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                            case 7:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 25));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 25));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 25));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                            case 8:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 30));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 30));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 30));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                        }
                    } else {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dahoangkim, soluongda);
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            } else {
                Service.gI().sendThongBao(player, "Không đủ Đá Hoàng Kim để thực hiện");
            }
        } else if (player.combineNew.itemsCombine.size() == 3) {
            int diem = player.combineNew.DiemNangcap - 5;
            if (player.pointSb < diem) {
                Service.gI().sendThongBao(player, "Không đủ Điểm Săn Boss để thực hiện");
                return;
            }
            Item chanmenh = null;
            Item dahoangkim = null;
            Item tv = null;
            int capbac = 0;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1318) {
                    dahoangkim = item;
                } else if (item.template.id == 457) {
                    tv = item;
                } else if (item.template.id >= 1300 && item.template.id < 1308) {
                    chanmenh = item;
                    capbac = item.template.id - 1299;
                }
            }
            int soluongda = player.combineNew.DaNangcap - 5;
            if (dahoangkim != null && dahoangkim.quantity >= soluongda && tv != null && tv.quantity >= 1) {
                if (chanmenh != null && (chanmenh.template.id >= 1300 && chanmenh.template.id < 1308)) {
                    player.pointSb -= diem;
                    if (Util.isTrue(player.combineNew.TileNangcap, 100)) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dahoangkim, soluongda);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                        sendEffectSuccessCombine(player);
                        switch (capbac) {
                            case 1:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 8));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 10));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 10));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                            case 2:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 10));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 12));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 12));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                            case 3:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 12));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 15));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 15));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                            case 4:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 16));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 18));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 18));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                            case 5:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 18));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 20));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 20));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                            case 6:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 20));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 22));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 22));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                            case 7:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 25));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 25));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 25));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                            case 8:
                                chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                                chanmenh.itemOptions.clear();
                                chanmenh.itemOptions.add(new Item.ItemOption(50, 30));
                                chanmenh.itemOptions.add(new Item.ItemOption(77, 30));
                                chanmenh.itemOptions.add(new Item.ItemOption(103, 30));
                                chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                break;
                        }
                    } else {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dahoangkim, soluongda);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            } else {
                Service.gI().sendThongBao(player, "Không đủ Đá Hoàng Kim để thực hiện");
            }
        }
    }

    public void thuctinhDT(Player player, int menu) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item dathuctinh = null;
            Item ngocrong = null;
            Item ngocrongsp = null;
            Item tv = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item != null && item.template != null) {
                    switch (item.template.id) {
                        case 16:
                            ngocrong = item;
                            break;
                        case 14:
                            ngocrongsp = item;
                            break;
                        case 1262:
                            dathuctinh = item;
                            break;
                        case 457:
                            tv = item;
                            break;
                        default:
                            break;
                    }
                }
            }
            if (ngocrongsp != null && ngocrongsp.quantity >= (1) && dathuctinh != null && dathuctinh.quantity >= (20) && tv != null && tv.quantity >= (3)) {
                try {
                    while (menu > 0) {
                        menu--;
                        if (player.nPoint.hpg >= 680000 && player.nPoint.mpg >= 680000 && player.nPoint.dameg >= 36000) {
                            this.buhan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Bạn đã đạt cấp độ thức tỉnh tối đa tối đa", "Đóng");
                            return;
                        }
                        String npcSay = "|7|[ THỨC TỈNH BẢN THÂN ]"
                                + "\n|5|HP: " + player.nPoint.hpg
                                + "\nKI: " + player.nPoint.mpg
                                + "\nSD: " + player.nPoint.dameg
                                + "\n|7|Tỉ lệ thành công: "
                                + (player.nPoint.hpg >= 600000 && player.nPoint.hpg < 625000
                                && player.nPoint.mpg >= 600000 && player.nPoint.mpg < 625000
                                && player.nPoint.dameg >= 32000 && player.nPoint.dameg < 34000 ? "50%"
                                        : player.nPoint.hpg >= 625000 && player.nPoint.hpg < 650000
                                        && player.nPoint.mpg >= 625000 && player.nPoint.mpg < 650000
                                        && player.nPoint.dameg >= 30000 && player.nPoint.dameg < 31000 ? "30%" : "10%") + "\n";
                        this.buhan.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng X1\nCần 100\nHồng Ngọc", "Nâng X10\nCần 1.000\nHồng Ngọc", "Nâng X100\nCần 10.000\nHồng Ngọc");
                        if (Util.isTrue((player.nPoint.hpg >= 600000 && player.nPoint.hpg < 625000
                                && player.nPoint.mpg >= 600000 && player.nPoint.mpg < 625000
                                && player.nPoint.dameg >= 32000 && player.nPoint.dameg < 33000 ? 30
                                        : player.nPoint.hpg >= 625000 && player.nPoint.hpg < 650000
                                        && player.nPoint.mpg >= 625000 && player.nPoint.mpg < 650000
                                        && player.nPoint.dameg >= 33000 && player.nPoint.dameg < 34500 ? 10 : 5), 100)) {
                            int[][] randomcs = {{Util.nextInt(100, 500)}, {Util.nextInt(500, 1000)}};
                            InventoryServiceNew.gI().subQuantityItemsBag(player, ngocrongsp, (1));
                            InventoryServiceNew.gI().subQuantityItemsBag(player, dathuctinh, (20));
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, (3));
                            player.inventory.ruby -= (100);
                            sendEffectSuccessCombine(player);
                            player.nPoint.hpg += Util.nextInt(randomcs[0][0], randomcs[1][0]);
                            player.nPoint.mpg += Util.nextInt(randomcs[0][0], randomcs[1][0]);
                            player.nPoint.dameg += Util.nextInt(randomcs[0][0], randomcs[1][0]);
                            if (player.nPoint.hpg > 680000) {
                                player.nPoint.hpg = 680000;
                            }
                            if (player.nPoint.mpg > 680000) {
                                player.nPoint.mpg = 680000;
                            }
                            if (player.nPoint.dameg > 36000) {
                                player.nPoint.dameg = 36000;
                            }
                            npcSay += "|7|Đột phá thành công, chỉ số đã tăng thêm 1 bậc!\nTiếp tục sau 3s";
                            this.buhan.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng X1\nCần 100\nHồng Ngọc", "Nâng X10\nCần 1.000\nHồng Ngọc", "Nâng X100\nCần 10.000\nHồng Ngọc");
                            Thread.sleep(2500);
                        } else {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, dathuctinh, (20));
                            sendEffectFailCombine(player);
                        }
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendMoney(player);
                        reOpenItemCombine(player);
                    }
                } catch (Exception e) {
                }
            } else if (ngocrong != null && ngocrong.quantity >= (1) && dathuctinh != null && dathuctinh.quantity >= (15) && tv != null && tv.quantity >= (1)) {
                try {
                    while (menu > 0) {
                        menu--;
                        String tsSay = "|7|[ THỨC TỈNH ĐỆ TỬ ]\n" + player.pet.name.substring(1)
                                + "\n|5|Cấp bậc : " + player.pet.getNameThuctinh(player.pet.thuctinh)
                                + "\nHP: " + (player.pet.nPoint.hpg >= 680000 ? Util.format(player.pet.nPoint.hpg)
                                        : Util.format(player.pet.nPoint.hpg) + " Cấp Sau: " + Util.format(player.pet.nPoint.hpg + 200))
                                + "\nKI: " + (player.pet.nPoint.mpg >= 680000 ? Util.format(player.pet.nPoint.mpg)
                                        : Util.format(player.pet.nPoint.mpg) + " Cấp Sau: " + Util.format(player.pet.nPoint.mpg + 200))
                                + "\nSD: " + (player.pet.nPoint.dameg >= 36000 ? Util.format(player.pet.nPoint.dameg)
                                        : Util.format(player.pet.nPoint.dameg) + " Cấp Sau: " + Util.format(player.pet.nPoint.dameg + 100))
                                + "\n|7|Tỉ lệ thành công: "
                                + (player.pet.nPoint.hpg >= 600000 && player.pet.nPoint.hpg < 625000
                                && player.pet.nPoint.mpg >= 600000 && player.pet.nPoint.mpg < 625000
                                && player.pet.nPoint.dameg >= 32000 && player.pet.nPoint.dameg < 33000 ? "50%"
                                        : player.pet.nPoint.hpg >= 625000 && player.pet.nPoint.hpg < 650000
                                        && player.pet.nPoint.mpg >= 625000 && player.pet.nPoint.mpg < 650000
                                        && player.pet.nPoint.dameg >= 33000 && player.pet.nPoint.dameg < 34500 ? "30%" : "10%") + "\n";
                        this.buhan.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, tsSay,
                                "Nâng X1\nCần 100\nHồng Ngọc", "Nâng X10\nCần 1.000\nHồng Ngọc", "Nâng X100\nCần 10.000\nHồng Ngọc");
                        if (Util.isTrue((player.pet.nPoint.hpg >= 600000 && player.pet.nPoint.hpg < 625000
                                && player.pet.nPoint.mpg >= 600000 && player.pet.nPoint.mpg < 625000
                                && player.pet.nPoint.dameg >= 32000 && player.pet.nPoint.dameg < 33000 ? 50
                                        : player.pet.nPoint.hpg >= 625000 && player.pet.nPoint.hpg < 650000
                                        && player.pet.nPoint.mpg >= 625000 && player.pet.nPoint.mpg < 650000
                                        && player.pet.nPoint.dameg >= 33000 && player.pet.nPoint.dameg < 34500 ? 30 : 10), 100)) {
                            player.inventory.ruby -= 100;
                            InventoryServiceNew.gI().subQuantityItemsBag(player, ngocrong, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, dathuctinh, 15);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                            if (Util.isTrue(50, 100)) {
                                player.pet.nPoint.hpg += 200;
                                player.pet.nPoint.mpg += 200;
                                player.pet.nPoint.dameg += 100;
                                if (player.pet.nPoint.hpg > 680000) {
                                    player.pet.nPoint.hpg = 680000;
                                }
                                if (player.pet.nPoint.mpg > 680000) {
                                    player.pet.nPoint.mpg = 680000;
                                }
                                if (player.pet.nPoint.dameg > 36000) {
                                    player.pet.nPoint.dameg = 36000;
                                }
                            } else {
                                player.pet.nPoint.tiemNangUp(Util.nextInt(1000, 10000));
                            }
                            if (player.pet != null && player.pet.thuctinh < 30) {
                                if (player.pet.thuctinh >= 0 && player.pet.thuctinh < 10 && Util.isTrue(30, 100)) {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, ngocrong, 1);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, dathuctinh, 15);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                                    player.pet.thuctinh++;
                                    tsSay += "\n|7|Đột phá thành công, cấp thức tỉnh tăng 1 bậc\nTiếp tục sau 3s\n";
                                    this.buhan.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, tsSay,
                                            "Nâng X1\nCần 100\nHồng Ngọc", "Nâng X10\nCần 1.000\nHồng Ngọc", "Nâng X100\nCần 10.000\nHồng Ngọc");
                                    Thread.sleep(2500);
                                } else if (player.pet.thuctinh >= 10 && player.pet.thuctinh < 20 && Util.isTrue(20, 100)) {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, ngocrong, 1);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, dathuctinh, 15);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                                    player.pet.thuctinh++;
                                    tsSay += "\n|7|Đột phá thành công, cấp thức tỉnh tăng 1 bậc\nTiếp tục sau 3s\n";
                                    this.buhan.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, tsSay,
                                            "Nâng X1\nCần 100\nHồng Ngọc", "Nâng X10\nCần 1.000\nHồng Ngọc", "Nâng X100\nCần 10.000\nHồng Ngọc");
                                    Thread.sleep(2500);
                                } else if (player.pet.thuctinh >= 20 && player.pet.thuctinh < 30 && Util.isTrue(10, 100)) {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, ngocrong, 1);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, dathuctinh, 15);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                                    player.pet.thuctinh++;
                                    tsSay += "\n|7|Đột phá thành công, cấp thức tỉnh tăng 1 bậc\nTiếp tục sau 3s\n";
                                    this.buhan.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, tsSay,
                                            "Nâng X1\nCần 100\nHồng Ngọc", "Nâng X10\nCần 1.000\nHồng Ngọc", "Nâng X100\nCần 10.000\nHồng Ngọc");
                                    Thread.sleep(2500);
                                }
                            }
                            if (player.pet.typePet == 0 && player.pet.thuctinh == 10) {
                                InventoryServiceNew.gI().subQuantityItemsBag(player, ngocrong, 1);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, dathuctinh, 15);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                                player.pet.typePet++;
                                player.pet.name = "$" + "Mabu";
                                ChangeMapService.gI().exitMap(player.pet);
                                tsSay += "\n|7|May mắn!\nĐệ tử đã tiến hoá thành Mabu!\n";
                                this.buhan.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, tsSay,
                                        "Nâng X1\nCần 100\nHồng Ngọc", "Nâng X10\nCần 1.000\nHồng Ngọc", "Nâng X100\nCần 10.000\nHồng Ngọc");
                                Thread.sleep(2500);
                            } else if (player.pet.typePet == 1 && player.pet.thuctinh == 20) {
                                InventoryServiceNew.gI().subQuantityItemsBag(player, ngocrong, 1);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, dathuctinh, 15);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                                player.pet.typePet++;
                                player.pet.name = "$" + "Gohan Buu";
                                ChangeMapService.gI().exitMap(player.pet);
                                tsSay += "\n|7|May mắn!\nĐệ tử đã tiến hoá thành Buu Han!\n";
                                this.buhan.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, tsSay,
                                        "Nâng X1\nCần 100\nHồng Ngọc", "Nâng X10\nCần 1.000\nHồng Ngọc", "Nâng X100\nCần 10.000\nHồng Ngọc");
                                Thread.sleep(2500);
                            } else if (player.pet.typePet == 2 && player.pet.thuctinh == 30) {
                                InventoryServiceNew.gI().subQuantityItemsBag(player, ngocrong, 1);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, dathuctinh, 15);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                                player.pet.typePet++;
                                player.pet.name = "$" + "Evil Buu";
                                ChangeMapService.gI().exitMap(player.pet);
                                tsSay += "\n|7|May mắn!\nĐệ tử đã tiến hoá thành Evio Buu!\n";
                                this.buhan.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, tsSay,
                                        "Nâng X1\nCần 100\nHồng Ngọc", "Nâng X10\nCần 1.000\nHồng Ngọc", "Nâng X100\nCần 10.000\nHồng Ngọc");
                                Thread.sleep(2500);
                            }
                        } else {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, dathuctinh, 15);
                            sendEffectFailCombine(player);
                        }
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendMoney(player);
                        reOpenItemCombine(player);
                    }
                } catch (Exception e) {
                }
            } else {
                Service.gI().sendThongBao(player, "Không đủ nguyên liệu để thực hiện");
            }
        } else {
            Service.gI().sendThongBao(player, "Không đủ nguyên liệu để thực hiện");
        }
    }

    //--------------------------------------------------------------------------
    /**
     * r
     * Hiệu ứng mở item
     *
     * @param player : người chơi
     * @param icon1 : icon 1
     * @param icon2 : icon 2
     */
    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thành công
     *
     * @param player : người chơi
     */
    public void sendEffectSuccessCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_SUCCESS);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thất bại
     *
     * @param player : người chơi
     */
    private void sendEffectFailCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_FAIL);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player : người chơi
     */
    private void reOpenItemCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combineNew.itemsCombine.size());
            for (Item it : player.combineNew.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player : người chơi
     * @param icon : icon ngọc rồng
     */
    public void sendEffectCombineDB(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //--------------------------------------------------------------------------Ratio, cost combine
    private int getGoldPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 5000_000;
            case 1:
                return 10000000;
            case 2:
                return 15_000_000;
            case 3:
                return 20000000;
            case 4:
                return 40000000;
            case 5:
                return 60000000;
            case 6:
                return 80000000;
            case 7:
                return 90000000;
            case 8:
                return 200000000;
            /*case 9:
                return 50000000;
            case 10:
                return 55000000;
            case 11:
                return 60000000;
            case 12:
                return 65000000;*/
        }
        return 0;
    }

    private float Tilecongdon(int param) {
        if (param >= 20 && param < 40) {
            return 0.1f;
        }
        if (param >= 40 && param < 60) {
            return 0.2f;
        }
        if (param >= 60 && param < 80) {
            return 0.3f;
        }
        if (param >= 80 && param < 100) {
            return 0.5f;
        }
        if (param >= 100 && param < 120) {
            return 0.7f;
        }
        if (param >= 120 && param < 140) {
            return 0.9f;
        }
        if (param >= 140 && param < 160) {
            return 1.1f;
        }
        if (param >= 160 && param < 180) {
            return 1.3f;
        }
        if (param >= 180 && param < 200) {
            return 1.5f;
        }
        if (param >= 200) {
            return 2;
        }
        if (param < 20) {
            return 0;
        }
        return 0;
    }

    private float getRatioPhaLeHoa(int star) { //phần trăm pha lê hoá
        switch (star) {
            case 0:
                return (int) 100;
            case 1:
                return (int) 60;
            case 2:
                return (int) 30;
            case 3:
                return (int) 10;
            case 4:
                return (int) 5;
            case 5:
                return (int) 3;
            case 6:
                return (int) 1;
            case 7:
                return (float) 0.5;
            case 8:
                return (float) 0.3;
        }
        return 0;
    }

    private int getGemPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 100;
            case 1:
                return 110;
            case 2:
                return 120;
            case 3:
                return 130;
            case 4:
                return 140;
            case 5:
                return 150;
            case 6:
                return 160;
            case 7:
                return 170;
            case 8:
                return 180;
        }
        return 0;
    }

    private int getGemEpSao(int star) {
        switch (star) {
            case 0:
                return 100;
            case 1:
                return 200;
            case 2:
                return 500;
            case 3:
                return 1000;
            case 4:
                return 2500;
            case 5:
                return 5000;
            case 6:
                return 10000;
            case 7:
                return 15000;
            case 8:
                return 20000;
        }
        return 0;
    }

    private double getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 80;
            case 1:
                return 50;
            case 2:
                return 30;
            case 3:
                return 7;
            case 4:
                return 3;
            case 5:
                return 2;
            case 6:
                return 1;
            case 7:
                return 0.5;
            /*case 9:
                return 1;
            case 10: //
                return 0.3;
            case 11: //
                return 1; 
            case 12: //
                return 1;*/
        }
        return 0;
    }

    private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 3;
            case 1:
                return 7;
            case 2:
                return 15;
            case 3:
                return 20;
            case 4:
                return 30;
            case 5:
                return 50;
            case 6:
                return 70;
            case 7:
                return 100;
        }
        return 0;
    }

    private int getCountDaBaoVe(int level) {
        return level + 1;
    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 10000;
            case 1:
                return 70000;
            case 2:
                return 300000;
            case 3:
                return 1500000;
            case 4:
                return 7000000;
            case 5:
                return 23000000;
            case 6:
                return 100000000;
            case 7:
                return 250000000;
            case 8:
                return 250000000;
        }
        return 0;
    }

    //--------------------------------------------------Chân mệnh/////
    private int getDiemNangcapChanmenh(int star) {
        switch (star) {
            case 0:
                return 30;
            case 1:
                return 28;
            case 2:
                return 25;
            case 3:
                return 22;
            case 4:
                return 18;
            case 5:
                return 15;
            case 6:
                return 12;
            case 7:
                return 10;
        }
        return 0;
    }

    private int getDaNangcapChanmenh(int star) {
        switch (star) {
            case 0:
                return 10;
            case 1:
                return 20;
            case 2:
                return 30;
            case 3:
                return 40;
            case 4:
                return 50;
            case 5:
                return 60;
            case 6:
                return 70;
            case 7:
                return 80;
        }
        return 0;
    }

    private float getTiLeNangcapChanmenh(int star) {
        switch (star) {
            case 0:
                return 80;
            case 1:
                return 65;
            case 2:
                return 45;
            case 3:
                return 25;
            case 4:
                return 15;
            case 5:
                return 7.5f;
            case 6:
                return 3.5f;
            case 7:
                return 1.5f;
        }
        return 0;
    }

    //--------------------------------------------------------------------------check
    private boolean isCoupleItemNangCap(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type < 5) {
                trangBi = item1;
            } else if (item1.template.type == 14) {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type < 5) {
                trangBi = item2;
            } else if (item2.template.type == 14) {
                daNangCap = item2;
            }
        }
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isCoupleItemNangCapCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDaPhaLe(Item item) {
        return item != null && (item.template.type == 30 || (item.template.id >= 14 && item.template.id <= 20));
    }

    private boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type < 5 || item.template.type == 32 || item.template.type == 36) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDuiDuc(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 1503) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDaHomet(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 1504) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int getParamDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            switch (daPhaLe.template.id) {
                case 441:
                case 442:
                case 443:
                case 446:
                case 447:
                    return 5;
                case 445:
                case 444:
                case 1416:
                    return 3;
                case 1412:
                    return 4;
                case 1413:
                case 1414:
                    return 7;
                case 1415:
                    return 3;
            }
            //return daPhaLe.itemOptions.get(0).param;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 5; // +5%hp
            case 19:
                return 5; // +5%ki
            case 18:
                return 5; // +5%hp/30s
            case 17:
                return 5; // +5%ki/30s
            case 16:
                return 3; // +3%sđ
            case 15:
                return 2; // +2%giáp
            case 14:
                return 3; // +5%né đòn
            default:
                return -1;
        }
    }

    private int getOptionDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            switch (daPhaLe.template.id) {
                case 441:
                    return 95;
                case 442:
                    return 96;
                case 443:
                    return 97;
                case 444:
                    return 99;
                case 445:
                    return 98;
                case 446:
                    return 100;
                case 447:
                    return 101;
                case 1412:
                    return 50;
                case 1413:
                    return 77;
                case 1414:
                    return 103;
                case 1415:
                    return 108;
                case 1416:
                    return 94;
            }
            return daPhaLe.itemOptions.get(0).optionTemplate.id;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 77;
            case 19:
                return 103;
            case 18:
                return 80;
            case 17:
                return 81;
            case 16:
                return 50;
            case 15:
                return 94;
            case 14:
                return 108;
            default:
                return -1;
        }
    }

    /**
     * Trả về id item c0
     *
     * @param gender : giới tính
     * @param type : 0: áo, 1: quần, 2: găng, 3: giày
     * @return id item c0
     */
    private int getTempIdItemC0(int gender, int type) {
        if (type == 4) {
            return 12;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 0;
                    case 1:
                        return 6;
                    case 2:
                        return 21;
                    case 3:
                        return 27;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 1;
                    case 1:
                        return 7;
                    case 2:
                        return 22;
                    case 3:
                        return 28;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 2;
                    case 1:
                        return 8;
                    case 2:
                        return 23;
                    case 3:
                        return 29;
                }
                break;
        }
        return -1;
    }

    //Trả về tên đồ c0
    private String getNameItemC0(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo vải 3 lỗ";
                    case 1:
                        return "Quần vải đen";
                    case 2:
                        return "Găng thun đen";
                    case 3:
                        return "Giầy nhựa";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo sợi len";
                    case 1:
                        return "Quần sợi len";
                    case 2:
                        return "Găng sợi len";
                    case 3:
                        return "Giầy sợi len";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo vải thô";
                    case 1:
                        return "Quần vải thô";
                    case 2:
                        return "Găng vải thô";
                    case 3:
                        return "Giầy vải thô";
                }
                break;
        }
        return "";
    }

    //--------------------------------------------------------------------------Text tab combine
    private String getTextTopTabCombine(int type) {
        switch (type) {
            case PHAN_RA_DO_SKH_TL:
                return "Ta sẽ phân rã \n  đồ của người thành đá vip!";
            case EP_SAO_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case PHA_LE_HOA_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case PHA_LE_HOA_TRANG_BI_VIP:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case NHAP_NGOC_RONG:
                return "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao";
            case NHAP_SPL:
                return "Ta sẽ phù phép\ncho  viên Ngọc Rồng\nthành  viên sao pha lê cấp cao";
            case NHAP_DA_SKH:
                return "Ta sẽ phù phép\ncho 10 đá skh thường\nthành 1 viên đá skh vip";
            case NANG_CAP_VAT_PHAM:
                return "Ta sẽ phù phép cho trang bị của ngươi trở lên mạnh mẽ";
            case PHAN_RA_DO_THAN_LINH:
                return "Ta sẽ phân rã \n  đồ của người thành đá!";
            case PHAN_RA_DO_THAN_LINH_X3:
                return "Ta sẽ phân rã \n  đồ của người thành đá!";
            case PHAN_RA_DO_THAN_LINH_X5:
                return "Ta sẽ phân rã \n  đồ của người thành đá!";
            case NANG_CAP_DO_TS:
                return "Ta sẽ nâng cấp \n  trang bị của người thành\n đồ thiên sứ!";
            case NANG_CAP_SKH_VIP:
                return "Ngọc Rồng Kamui\nNâp Cấp Trang Bị\n [ SET KÍCH HOẠT VIP ]";
            case NANG_CAP_SKH_THUONG:
                return "Ngọc Rồng Kamui\nNâng Cấp Trang Bị\n [ SET KÍCH HOẠT ]";
            case NANG_CAP_BONG_TAI_1_LEN_2:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 2";
            case MO_CHI_SO_BONG_TAI_CAP2:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 2 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case THUE_DO:
                return "Ta sẽ giúp gia hạn\ncho trang bị của con\nhãy để ta!!";
            case DOI_DIEM:
                return "Đổi Thức Ăn\nLấy mảnh hủy diệt";
            case NANG_CAP_LINH_THU:
                return "Ta sẽ phù phép\ncho Linh Thú cùi bắp của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case CHE_TAO_TRANG_BI_TS:
                return "Chế tạo\ntrang bị thiên sứ";
            case NANG_CAP_BONG_TAI_2_LEN_3:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 2 của ngươi\nthành cấp 3";
            case MO_CHI_SO_BONG_TAI_CAP3:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 3 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case NANG_CAP_BONG_TAI_3_LEN_4:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 3 của ngươi\nthành cấp 4";
            case MO_CHI_SO_BONG_TAI_CAP4:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 4 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case NANG_CAP_BONG_TAI_CAP5:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 4 của ngươi\nthành cấp 5";
            case MO_CHI_SO_BONG_TAI_CAP5:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 5 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case LUYEN_HOA_CHIEN_LINH:
                return "Ta sẽ cùng người luyện hóa\nchiến linh";
            case MO_GIOI_HAN_CHIEN_LINH:
                return "Ta sẽ độ kiếp\ncho chiến linh của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case NANG_CAP_CHAN_MENH:
                return "Ta sẽ Nâng cấp\nChân Mệnh của ngươi\ncao hơn một bậc";
            case THUC_TINH_DT:
                return "Ta sẽ giúp Thức Tỉnh chỉ số\ncủa ngươi hoặc đệ tử\nGiúp gia tăng chỉ số cao hơn";
            case THUC_TINH_SP:
                return "Ta sẽ Thức Tỉnh \nChỉ số của ngươi\nGiúp tăng chỉ số cao hơn";
            case NANG_SKH_LVL1:
                return "Ta sẽ phù phép\n"
                        + "cho trang bị SKH [Level 0]\n"
                        + "trở thành trang bị SKH [Level 1]";
            case NANG_SKH_LVL2:
                return "Ta sẽ phù phép\n"
                        + "cho trang bị SKH [Level 1]\n"
                        + "trở thành trang bị SKH [Level 2]";
            case NANG_SKH_TL_LVL0:
                return "Ta sẽ phù phép\n"
                        + "cho trang bị HD và đá nâng cấp\n"
                        + "trở thành trang bị SKH Thần [Level 0]";
            case NANG_SKH_TL_LVL1:
                return "Ta sẽ phù phép\n"
                        + "cho trang bị SKH Thần [Level 0]\n"
                        + "trở thành trang bị SKH Thần [Level 1]";
            case NANG_SKH_TL_LVL2:
                return "Ta sẽ phù phép\n"
                        + "cho trang bị SKH Thần [Level 1]\n"
                        + "trở thành trang bị SKH Thần [Level 2]";
            default:
                return "";
        }
    }

    private String getTextInfoTabCombine(int type) {
        switch (type) {
            case PHAN_RA_DO_SKH_TL:
                return "vào hành trang\nChọn item\n(Đồ Thần)\nChọn Đồ SKH Thần Rồi Tách\n"
                        + "Sau đó chọn 'Tách'";
            case NANG_SKH_LVL1:
                return "Vào hành trang\n"
                        + "Chọn 1 trang bị skh thường [Level 0]\n"
                        + "Chọn 5 trang bị thần linh\n"
                        + "(Áo, quần, găng, giầy hoặc rada)\n"
                        + "món đầu tiên sẽ là món được nâng cấp\n"
                        + "x20 đá nâng SKH để cuối\n"
                        + "Xịt -90% đá nâng cấp\n"
                        + "sau đó chọn'Nâng cấp'";
            case NANG_SKH_LVL2:
                return "Vào hành trang\n"
                        + "Chọn 1 trang bị SKH thường [Level 1]\n"
                        + "Chọn 5 trang bị thần linh\n"
                        + "(Áo, quần, găng, giầy hoặc rada)\n"
                        + "món đầu tiên sẽ là món được nâng cấp\n"
                        + "x50 đá nâng SKH để cuối\n"
                        + "Xịt -90% đá nâng cấp\n"
                        + "sau đó chọn'Nâng cấp'";
            case NANG_SKH_TL_LVL0:
                return "Vào hành trang\n"
                        + "Chọn 5 trang bị Huỷ Diệt\n"
                        + "(Áo, quần, găng, giầy hoặc rada)\n"
                        + "món đầu tiên sẽ là món được nâng cấp\n"
                        + "x10 đá nâng SKH Vip để cuối\n"
                        + "sau đó chọn'Nâng cấp'";
            case NANG_SKH_TL_LVL1:
                return "Vào hành trang\n"
                        + "Chọn 1 trang bị SKH Thần [Level 0]\n"
                        + "Chọn 5 trang bị Huỷ Diệt\n"
                        + "(Áo, quần, găng, giầy hoặc rada)\n"
                        + "món đầu tiên sẽ là món được nâng cấp\n"
                        + "x20 đá nâng SKH để cuối\n"
                        + "Xịt -90% đá nâng cấp\n"
                        + "sau đó chọn'Nâng cấp'";
            case NANG_SKH_TL_LVL2:
                return "Vào hành trang\n"
                        + "Chọn 1 trang bị SKH Thần [Level 1]\n"
                        + "Chọn 5 trang bị Huỷ Diệt\n"
                        + "(Áo, quần, găng, giầy hoặc rada)\n"
                        + "món đầu tiên sẽ là món được nâng cấp\n"
                        + "x50 đá nâng SKH để cuối\n"
                        + "Xịt -90% đá nâng cấp\n"
                        + "sau đó chọn'Nâng cấp'";
            case EP_SAO_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) có ô đặt sao pha lê\nChọn loại sao pha lê\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI_VIP:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) 7 sao\n x1 Dùi Đục và x3 Đá Homet\nSau đó chọn 'Nâng cấp'\n Trở thành món 8 sao";
            case NHAP_NGOC_RONG:
                return "Vào hành trang\nChọn 7 viên ngọc rồng\nSau đó chọn 'Làm phép'";
            case NHAP_DA_SKH:
                return "Vào hành trang\nChọn 10 đá skh thường\nSau đó chọn 'Làm phép'\n trở thành đá SKH VIP";
            case NHAP_SPL:
                return "Vào hành trang\nSPL HP cần 7 viên 7s\nSPL KI cần 7 viên 6s\nSPL SD cần 7 viên 3s\nSPL giáp cần 7 viên 2s\nSPL né cần 7 viên 1s \nSau đó chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHAN_RA_DO_THAN_LINH:
                return "vào hành trang\nChọn item\n(Đồ Thần)\nChọn Đồ Thần Rồi Tách\n"
                        + "Sau đó chọn 'Tách'";

            case PHAN_RA_DO_THAN_LINH_X3:
                return "vào hành trang\nChọn 3 item\n(Đồ Thần)\nChọn 3 Đồ Thần Rồi Tách\n"
                        + "Sau đó chọn 'Tách'";
            case PHAN_RA_DO_THAN_LINH_X5:
                return "vào hành trang\nChọn 5 item\n(Đồ Thần)\nChọn 5 Đồ Thần Rồi Tách\n"
                        + "Sau đó chọn 'Tách'";
            case NANG_CAP_DO_TS:
                return "vào hành trang\nChọn 2 trang bị hủy diệt bất kì\nkèm 1 món đồ thần linh\n và 5 mảnh thiên sứ\n "
                        + "sẽ cho ra đồ thiên sứ từ 0-15% chỉ số"
                        + "Sau đó chọn 'Nâng Cấp'";
            case NANG_CAP_SKH_VIP:
                return "[ NÂNG CẤP TRANG BỊ KÍCH HOẠT VIP ]\n" + "\nYÊU CẦU\n+ 1 đồ Kích Hoạt\n+ 2 đồ Huỷ Diệt bất kì\n+ 500tr vàng\n\n"
                        + "Lưu Ý : Đồ Kích Hoạt Vip\nsẽ ra cùng loại và hành tinh \n với đồ kích hoạt đã chọn ban đầu!\n" + "\n"
                        + "Sau đó chỉ cần chọn 'Nâng Cấp'";
            case NANG_CAP_SKH_THUONG:
                return "[ NÂNG CẤP TRANG BỊ KÍCH HOẠT ]\n" + "\nYÊU CẦU\n+ 2 món đồ Thần Linh\n+ 500tr vàng\n\n "
                        + "Lưu Ý : Đồ Kích Hoạt\nsẽ ra cùng loại với DTL ban đầu\nvà cùng hành tinh với bạn!\n" + "\n"
                        + "Sau đó chỉ cần chọn 'Nâng Cấp'";
            case NANG_CAP_BONG_TAI_1_LEN_2:
                return "Vào hành trang\nCần chọn 1 Bông tai Porata\nVà x9999 mảnh vỡ bông tai\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI_CAP2:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh hồn bông tai số lượng 99 cái\nvà đá xanh lam để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_LINH_THU:
                return "Vào hành trang\nChọn Linh Thú\nChọn đá ma thuật  số lượng 99 cái\nvà thức ăn để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case CHE_TAO_TRANG_BI_TS:
                return "Cần 1 công thức vip\nMảnh trang bị tương ứng\n"
                        + "Số Lượng\n999"
                        + "Có thể thêm\nĐá nâng cấp (tùy chọn) để tăng tỉ lệ chế tạo\n"
                        + "Đá may mắn (tùy chọn) để tăng tỉ lệ các chỉ số cơ bản và chỉ số ẩn\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case DOI_DIEM:
                return "Cần x99 thức ăn\n"
                        + "Mỗi x99 thức ăn sẽ đổi được 1-2 mảnh hủy diệt\n"
                        + "Sau đó chọn 'Nâng Cấp'";
            case THUE_DO:
                return "Cần 1 trang bị từ shop thuê"
                        + "\nKhông thể gia hạn đồ ngoài shop"
                        + "\nVé gia hạn tương ứng\n"
                        + "Số Lượng Vé Cho Mỗi Lần Là : 1 vé"
                        + "\nSau đó chỉ cần chọn 'Gia Hạn'";
            case NANG_CAP_BONG_TAI_2_LEN_3:
                return "Vào hành trang\nCần chọn 1 Bông tai Porata cấp 2\nVà x9999 mảnh vỡ bông tai\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI_CAP3:
                return "Vào hành trang\nChọn bông tai Porata cấp 3\nNguyên liệu cần có:\nx999 Mảnh Hồn Bông Tai\nx99 Đá Xanh Lam\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI_3_LEN_4:
                return "Vào hành trang\nchọn bông tai Porata cấp 3\nchọn x999 MVBT C4\nSau đó chọn 'Nâng cấp' ";
            case MO_CHI_SO_BONG_TAI_CAP4:
                return "Vào hành trang\nChọn bông tai Porata cấp 4\nChọn thạch phù số lượng 99 \nvà đá xanh lam số lượng 5 viên\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI_CAP5:
                return "Vào hành trang\nchọn bông tai Porata cấp 4\nchọn x999 MVBT C4\nSau đó chọn 'Nâng cấp' ";
            case MO_CHI_SO_BONG_TAI_CAP5:
                return "Vào hành trang\nChọn bông tai Porata cấp 5\nChọn thạch phù số lượng 99 \nvà đá xanh lam số lượng 10 viên\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_CHAN_MENH:
                return "Vào hành trang\nChọn Chân mệnh muốn nâng cấp\nChọn Đá Hoàng Kim\n"
                        + "Sau đó chọn 'Nâng cấp'\n"
                        + "Nếu cho thêm thỏi vàng vào sẽ giúp giảm số lượng đá Hoàng Kim và tăng thêm tỉ lệ\n\n"
                        + "Lưu ý: Khi Nâng cấp Thành công sẽ tăng thêm % chỉ số của cấp trước đó";
            case THUC_TINH_DT:
                return "Nguyên liệu cần có:\n\n"
                        + "[Đối với Đệ Tử]\nx15 Đá Thức Tỉnh\nx1 Ngọc Rồng 3 Sao\nx1 Thỏi Vàng\n"
                        + "Khi thức tỉnh sẽ có khả năng Đột Phá\nGiúp tăng thêm 1 cấp bậc đệ tử\n"
                        + "[Đối với Sư Phụ]\nx20 Đá Thức Tỉnh\nx1 Ngọc Rồng 1 Sao\nx3 Thỏi Vàng\n"
                        + "Nếu thất bại sẽ chì mất đá thức tỉnh\n\n"
                        + "Sau khi đủ nguyên liệu chọn 'Nâng cấp'";
            case THUC_TINH_SP:
                return "Nguyên liệu cần có: \nĐá Thức Tỉnh x50\nNgọc Rồng 1 Sao x3\nThỏi Vàng x5\n"
                        + "Chỉ số sẽ được random từ 100-1000\n"
                        + "Sau đó chọn 'Nâng cấp'\n";
            default:
                return "";
        }
    }

}
