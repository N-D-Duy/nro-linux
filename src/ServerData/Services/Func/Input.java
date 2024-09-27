package ServerData.Services.Func;

import ServerData.Services.ChangeMapService;
//import ServerData.Services.Func.TaiXiu.TaiXiu_Old;
import com.girlkun.database.GirlkunDB;
import Server.Data.Consts.ConstNpc;
import Server.Connect.PlayerDAO;
import Server.Data.Consts.ConstEvent;
import ServerData.Models.Item.Item;
import ServerData.Models.Item.Item.ItemOption;
import ServerData.Models.Map.Zone;
import ServerData.Models.NPC.NauBanh;
import ServerData.Models.NPC.Npc;
import static ServerData.Models.NPC.NpcFactory.PLAYERID_OBJECT;
import ServerData.Models.NPC.NpcManager;
import ServerData.Models.Player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;
import com.girlkun.result.GirlkunResultSet;
import ServerData.Server.Client;
import ServerData.Services.Service;
import ServerData.Services.Giftcode.GiftcodeService;
import ServerData.Services.InventoryServiceNew;
import ServerData.Services.ItemService;
import ServerData.Server.Manager;
import ServerData.Server.ServerNotify;
import ServerData.Services.ItemTimeService;
import ServerData.Services.NpcService;
import ServerData.Services.TaskService;
import ServerData.Services.TransactionService;
import ServerData.Utils.TimeUtil;
import ServerData.Utils.Util;

import java.util.HashMap;
import java.util.Map;

public class Input {

    public static String LOAI_THE;
    public static String MENH_GIA;
    private static final Map<Integer, Object> PLAYER_ID_OBJECT = new HashMap<>();

    public static final int CHANGE_PASSWORD = 500;
    public static final int GIFT_CODE = 501;
    public static final int FIND_PLAYER = 502;
    public static final int CHANGE_NAME = 503;
    public static final int CHOOSE_LEVEL_BDKB = 504;
    public static final int CHOOSE_LEVEL_KGHD = 514;
    public static final int NAP_THE = 505;
    public static final int CHANGE_NAME_BY_ITEM = 506;
    public static final int GIVE_IT = 507;
    public static final int GIVE_IT2 = 520;
    public static final int NEXTNV = 522;
      public static final int CHOOSE_LEVEL_CDRD = 51522;
    public static final int CHATALL = 521;
    public static final int GIVE_VND = 515;
    public static final int ACTIVE = 516;
    public static final int QUANLY = 519;
    public static final int THAY_EXP = 517;
    public static final int BUFF_VND = 518;
    public static final int SEND_ITEM_OP = 512;
    public static final int SEND_ITEM_SKH = 524;
    public static final int GIVE_CS = 625;
    public static final int SUB_CS = 626;
    public static final int THAY_EVENT = 627;
    public static final int BANVANG = 628;
    public static final int QUY_DOI_THOI_VANG = 508;
    public static final int QUY_DOI_HONG_NGOC = 509;
      public static final int QUY_DOI_XU_BAC = 1312;
       public static final int QUY_DOI_THANG_TINH_THACH = 1314;
        public static final int QUY_DOI_XU_VANG = 1313;
    public static final int DONATE_CS = 523;
    public static final int TAI = 510;
    public static final int XIU = 511;
    public static final int XIU_TX = 629;
    public static final int TAI_TX = 630;
    public static final int CSDE = 631;
    public static final int THUITEM = 632;
    public static final int SLVANG = 633;
    public static final int TILE_NAP = 634;
    private static final int NAU_BANH_TET = 635;
    public static final int NAU_BANH_CHUNG = 636;
    public static final int DUA_HAU = 637;
    public static final int BONG_HOA = 638;
    public static final int HOC_SKILL_TD1 = 512;
    public static final int HOC_SKILL_NM1 = 513;
    public static final int HOC_SKILL_XD1 = 514;

    public static final byte NUMERIC = 0;
    public static final byte ANY = 1;
    public static final byte PASSWORD = 2;

    private static Input intance;

    private Input() {

    }

    public static Input gI() {
        if (intance == null) {
            intance = new Input();
        }
        return intance;
    }

    public void doInput(Player player, Message msg) {
        try {
            String[] text = new String[msg.reader().readByte()];
            for (int i = 0; i < text.length; i++) {
                text[i] = msg.reader().readUTF();
            }
            switch (player.iDMark.getTypeInput()) {
                case THUITEM:
                    Player nameT = (Player) PLAYERID_OBJECT.get(player.id);
                    int idT = Integer.parseInt(text[0]);
                    int qT = Integer.parseInt(text[1]);
                    Item itembag = InventoryServiceNew.gI().findItemBag(nameT, idT);
                    Item itembody = InventoryServiceNew.gI().findItemBody(nameT, idT);
                    Item itembox = InventoryServiceNew.gI().findItemBox(nameT, idT);
                    Item idCheck = ItemService.gI().createNewItem((short)idT);
                    if (nameT != null) {
                        if (itembag != null) {
                        Service.gI().sendThongBaoOK(player, "Thu x" + qT + " (" + itembag.template.name + ") từ player : " + nameT.name);
                        InventoryServiceNew.gI().subQuantityItemsBag(nameT, itembag,qT);
                        InventoryServiceNew.gI().sendItemBags(nameT);
                    } else if (itembody != null) {
                        Service.gI().sendThongBaoOK(player, "Thu x" + qT + " (" +  itembody.template.name + ") từ player : " + nameT.name);
                        InventoryServiceNew.gI().subQuantityItemsBody(nameT, itembody,qT);
                        InventoryServiceNew.gI().sendItemBags(nameT);
                    } else if (itembox != null) {
                        Service.gI().sendThongBaoOK(player, "Thu x" + qT + " (" + itembox.template.name + ") từ player : " + nameT.name);
                        InventoryServiceNew.gI().subQuantityItemsBox(nameT, itembox,qT);
                        InventoryServiceNew.gI().sendItemBags(nameT);
                    } else {
                        Service.gI().sendThongBaoOK(player, nameT.name + "\n không sở hữu x" + qT + " (" + idCheck.template.name + ")");
                    }} else {
                        Service.gI().sendThongBao(player, "Player không online");
                    }
                    break;
                case GIVE_IT:
                    String name = text[0];
                    int id = Integer.valueOf(text[1]);
                    int q = Integer.valueOf(text[2]);
                    if (Client.gI().getPlayer(name) != null) {
                        Item item = ItemService.gI().createNewItem((short) id,q);
                        InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name), item);
                        InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name));
                        Service.gI().sendThongBaoOK(player, "Buff " + item.template.name + " to player : " + name);
                        Service.gI().sendThongBao(Client.gI().getPlayer(name), "Nhận được " + item.template.name + " từ Admin : " + player.name);
                    } else {
                        Service.gI().sendThongBao(player, "Không online");
                    }
                    break;
                case GIVE_IT2:
                    String name2 = text[0];
                    String id2 = text[1];
                    String q2 = text[2];
                    String[] iadd1 = id2.split("-");
                    String[] qadd1 = q2.split("-");
                    int chuoi1 = iadd1.length;
                    int chuoi2 = qadd1.length;
                    if (chuoi1 == chuoi2){
                   if(Client.gI().getPlayer(name2) != null){
                    for (int i = 0; i < chuoi1; i++) {
                        String iadd2 = iadd1[i];
                        String qadd2 = qadd1[i];
                        int ik;
                        int qk;
                        try {
                            ik = Integer.parseInt(iadd2);
                            qk = Integer.parseInt(qadd2);
                            Item item2 = ItemService.gI().createNewItem(((short)ik));
                            item2.quantity = qk;
                            Service.getInstance().sendThongBaoOK(Client.gI().getPlayer(name2), " NHẬN CHUỖI ITEM TỪ ADMIN : " + player.name);
                            Service.getInstance().sendThongBaoFromAdmin(player, "|7|BUFF CHUỖI ITEM CHO " + name2 + " THÀNH CÔNG!");
                            InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name2), item2);
                            InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name2));
                        } catch (NumberFormatException e) {
                            break;
                        }}} else {Service.getInstance().sendThongBao(player, "Không online");}} else {Service.getInstance().sendThongBao(player, "Nhập dữ liệu không đúng");}
                    break;

                    case NEXTNV:
                    Player plid = (Player) PLAYERID_OBJECT.get(player.id);
                    int nv = Integer.parseInt(text[0]);
                       if (plid.playerTask.taskMain.id > 33) {
                        Service.gI().sendThongBao(player, "PLAYER ĐÃ MAX NHIỆM VỤ");
                        break;
                       }
                       if (nv <= 33) {
                        TaskService.gI().getTaskMainById(plid, nv);
                        plid.playerTask.taskMain.id = nv;
                        plid.iDMark.setLastTimeBan(System.currentTimeMillis());
                        plid.iDMark.setBan(true);
                        Service.gI().sendThongBaoOK(plid,"Bạn vừa được next nhiệm vụ bởi ADMIN\nVui lòng đăng nhập lại\nHOẶC HỆ THỐNG SẼ TỰ KICK BẠN SAU 10s");
                        Service.gI().sendThongBao(player,"Next NV player [" + plid.name +"] đến "+nv+ " thành công");
                    } else {
                        Service.gI().sendThongBao(player, "Player không online");
                    }
                    break;
                    
                case GIVE_VND:
                    if (player.isAdmin2()) {
                        String name1 = text[0];
                        int vnd = Integer.parseInt(text[1]);
                        int tvnd = Integer.parseInt(text[2]);
                        if (Client.gI().getPlayer(name1) != null) {
                        if(tvnd == 0){
                            PlayerDAO.addvnd(Client.gI().getPlayer(name1), vnd);
                            Service.gI().sendThongBaoFromAdmin(player,"|7|[ - PLAYER RECHARGE - ]\n" 
                                    +"|2|Người Thực Hiện : " + player.name 
                                    +"\nNạp Tiền Đến : " + name1 
                                    +"\nSố tiền nạp : " + Util.format(vnd) + " VNĐ"
                                    +"\nThời gian thực hiện : ("+TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss")+")"+"\n"
                                    +"|7|Giao Dịch Thành Công!");
                            Service.gI().sendThongBaoFromAdmin(Client.gI().getPlayer(name1),"|7|[ - Recharge Bill - ]\n" 
                                    + "|2|Nhân viên thực hiện : " + player.name 
                                    + "\nSố tiền nạp đến bạn : " + Util.format(vnd) + " VNĐ"
                                    +"\nVào lúc : ("+TimeUtil.getTimeNow("HH:mm:ss dd/MM/yyyy")+")"
                                    +"\n(Vui lòng kiểm tra kĩ lại số tiền trước khi dùng!)\n"
                                    +"|7|Nạp Tiền Thành Công!");
                            break;
                        } if(vnd == 0){
                            PlayerDAO.subvnd(Client.gI().getPlayer(name1), tvnd);
                            Service.gI().sendThongBaoFromAdmin(player,"|7|[ - PLAYER RECHARGE - ]\n" 
                                    +"|2|Người Thực Hiện : " + player.name 
                                    +"\nTrừ Tiền Player : " + name1 
                                    +"\nSố tiền trừ : "+ Util.format(tvnd) + " VNĐ"
                                    +"\nThời gian thực hiện (: "+TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss")+")"+"\n"
                                    +"|7|Trừ Tiền Thành Công!");
                            Service.gI().sendThongBaoFromAdmin(Client.gI().getPlayer(name1),"|7|[ Trừ Tiền Tài Khoản ]\n(Do sự cố, nhầm lẫn từ phía server)\n" 
                                    + "|2|Từ Nhân Viên : " + player.name 
                                    + "\nSố tiền đã trừ : " + Util.format(tvnd) + " VNĐ"
                                    +"\nThời gian thực hiện (: "+TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss")+")"
                                    +"\n"
                                    +"|7|Chúc bạn online vui vẻ!");
                            break;
                        }} else {
                            Service.gI().sendThongBao(player, "Không online");
                            break;
                        }}
                        break;
                    

                case GIVE_CS:
                    Player phucml = ((Player) PLAYERID_OBJECT.get(player.id));
                    if (phucml != null) {
                        long SM = Long.parseLong(text[0]);
                        long TN = Long.parseLong(text[1]);
                        int SD = Integer.parseInt(text[2]);
                        int HPKI = Integer.parseInt(text[3]);
                        int CRIT = Integer.parseInt(text[4]);
                        phucml.pet.nPoint.tiemNangUp(+TN);
                        phucml.pet.nPoint.powerUp(+SM);
                        phucml.pet.nPoint.dameg += SD;
                        phucml.pet.nPoint.mpg += HPKI;
                        phucml.pet.nPoint.hpg += HPKI;
                        phucml.pet.nPoint.critg += CRIT;
                        if(phucml.pet.nPoint.dameg >= 32000) phucml.pet.nPoint.dameg = 32000;
                        if(phucml.pet.nPoint.hpg >= 600000 && phucml.pet.nPoint.mpg >= 450000) phucml.pet.nPoint.hpg = 600000;phucml.pet.nPoint.mpg = 600000;
                        if(phucml.pet.nPoint.critg >= 100) phucml.pet.nPoint.critg = 10;
                        if(phucml.pet.nPoint.power >= 180000000000L) phucml.pet.nPoint.power = 180000000000L;
                        
                            Service.gI().sendThongBaoFromAdmin(player,"|7|[ - BUFF CHỈ SỐ ĐỆ TỬ - ]\n" + "|2|Đến Player : " + ((Player) PLAYERID_OBJECT.get(player.id)).name
                                    +"\nPlayer Pet : " + phucml.pet.name
                                    +"\nSỨC MẠNH ĐÃ BUFF : " + SM + " SM" + " | SAU KHI BUFF : " + phucml.pet.nPoint.power
                                    +"\nTIỀM NĂNG ĐÃ BUFF : " + TN + " TN" + " | SAU KHI BUFF : " + phucml.pet.nPoint.tiemNang
                                    +"\nSỨC ĐÁNH ĐÃ BUFF : " + SD + " SDG" + " | SAU KHI BUFF : " + phucml.pet.nPoint.dame
                                    +"\nHP, KI ĐÃ BUFF : " + HPKI + " HPKIG" + " | SAU KHI BUFF : " + phucml.pet.nPoint.hp +", " + phucml.pet.nPoint.mp
                                    +"\nCHÍ MẠNG ĐÃ BUFF : " + CRIT + " CM" + " | SAU KHI BUFF : " + phucml.pet.nPoint.crit
                                    +"\n(Thời gian thực hiện : "+TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss")+")"+"\n"
                                    +"|7|SUCCESS!");
                            break;
                        } else {
                            Service.gI().sendThongBao(player, "Không online");
                    }
                            break;
                case SUB_CS:
                    if(((Player) PLAYERID_OBJECT.get(player.id)).pet != null) {
                        long SM = Long.parseLong(text[0]);
                        long TN = Long.parseLong(text[1]);
                        int SD = Integer.parseInt(text[2]);
                        int HPKI = Integer.parseInt(text[3]);
                        int CRIT = Integer.parseInt(text[4]);
                        ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.tiemNangUp(-TN);
                        ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.powerUp(-SM);
                        ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.dameg -= SD;
                        ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.mpg -= HPKI;
                        ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.hpg -= HPKI;
                        ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.critg -= CRIT;
                            Service.gI().sendThongBaoFromAdmin(player,"|7|[ - BUFF CHỈ SỐ ĐỆ TỬ - ]\n" + "|2|Đến Player : " + ((Player) PLAYERID_OBJECT.get(player.id)).name
                                    +"\nPlayer Pet : " + ((Player) PLAYERID_OBJECT.get(player.id)).pet.name
                                    +"\nSỨC MẠNH ĐÃ GIẢM : " + SM + " SM" + " | SAU KHI GIẢM : " + ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.power
                                    +"\nTIỀM NĂNG ĐÃ GIẢM : " + TN + " TN" + " | SAU KHI GIẢM : " + ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.tiemNang
                                    +"\nSỨC ĐÁNH ĐÃ GIẢM : " + SD + " SDG" + " | SAU KHI GIẢM : " + ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.dame
                                    +"\nHP, KI ĐÃ GIẢM : " + HPKI + " HPKIG" + " | SAU KHI GIẢM : " + ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.hp +", " + ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.mp
                                    +"\nCHÍ MẠNG ĐÃ GIẢM : " + CRIT + " CM" + " | SAU KHI GIẢM : " + ((Player) PLAYERID_OBJECT.get(player.id)).pet.nPoint.crit
                                    +"\n(Thời gian thực hiện : "+TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss")+")"+"\n"
                                    +"|7|SUCCESS!");
                            break;
                    } else {
                            Service.gI().sendThongBao(player, "Không online");
                    }
                            break;
                case CHATALL:
                            String chat = text[0];
                            Service.gI().ChatAll(22713,"|7|[ - •⊹٭NGỌC RỒNG Kamui THÔNG BÁO٭⊹• - ]"+"\n" 
                                    +"|1|"+(player.isAdmin() ? "KEY : " : player.isAdmin2() ? "ADMIN : ":"")+chat + "\n");
                        break;

//                case SEND_ITEM_OP:
//                   String name1 = text[0];
//                   String it = text[1];
//                   String[] itqt = it.split("-");
//                   String option = text[2];
//                   String param = text[3];
//                   String[] option1 = option.split("-");
//                   String[] param1 = param.split("-");
//                   int length1 = option1.length;
//                   int length2 = param1.length;
//                   if (length1 == length2){
//                   if(Client.gI().getPlayer(name1) != null){
//                    Item item = ItemService.gI().createNewItem((short)Short.parseShort(itqt[0]));
//                    item.quantity = itqt.length <= 1 ? 1 : Integer.parseInt(itqt[1]);
//                    for (int i = 0; i < length1; i++) {
//                        String option2 = option1[i];
//                        String param2 = param1[i];
//                        int opt;
//                        int par;
//                        try {
//                            opt = Integer.parseInt(option2);
//                            par = Integer.parseInt(param2);
//                            item.itemOptions.add(new Item.ItemOption(opt, par));
//                        } catch (NumberFormatException e) {
//                            break;
//                        }
//                    }
//                    String npcSay ="|7|Send x" + (itqt.length <= 1 ? 1 : Integer.parseInt(itqt[1])) + " " + item.template.name +"\n|2|";
//                    for (Item.ItemOption io : item.itemOptions) {
//                        npcSay += io.getOptionString() + "\n";
//                    }
//                    Service.getInstance().sendThongBaoFromAdmin(player, npcSay + "|7|Đến Player ("+name1+") Thành Công!");
//                    InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name1), item);
//                    InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name1));
//                    Service.getInstance().sendThongBaoOK(Client.gI().getPlayer(name1), "Send item từ admin : " + player.name + "\nBạn nhận được\nx" + (itqt.length <= 1 ? 1 : Integer.parseInt(itqt[1])) + " " + item.template.name);
//                    } else{
//                       Service.getInstance().sendThongBao(player, "Không online");
//                    }} else {
//                            Service.getInstance().sendThongBao(player, "Nhập dữ liệu không đúng");
//                            }
//                    break;
                      case SEND_ITEM_OP:
                    if ( player.isAdmin2()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        String idOptionBuff = text[2];
                        int slItemBuff = Integer.parseInt(text[3]);
                        Player pBuffItem = Client.gI().getPlayer(text[0]);
                        if (pBuffItem != null) {
                            String txtBuff = "Buff to player: " + pBuffItem.name + "\b";
                            if (idItemBuff == -1) {
                                // pBuffItem.inventory.gold = Math.min(pBuffItem.inventory.gold +
                                // (long)slItemBuff, (Inventory.LIMIT_GOLD + pBuffItem.limitgold));
                                txtBuff += slItemBuff + " vàng\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -2) {
                                pBuffItem.inventory.gem = Math.min(pBuffItem.inventory.gem + slItemBuff, 2000000000);
                                txtBuff += slItemBuff + " ngọc\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -3) {
                                pBuffItem.inventory.ruby = Math.min(pBuffItem.inventory.ruby + slItemBuff, 2000000000);
                                txtBuff += slItemBuff + " ngọc khóa\b";
                                Service.getInstance().sendMoney(player);
                            } else {
                                // Item itemBuffTemplate = ItemBuff.getItem(idItemBuff);
                                Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                                // itemBuffTemplate.itemOptions.add(new Item.ItemOption(idOptionBuff,
                                // slOptionBuff));
                                String[] OptionArr = idOptionBuff.split("v");
                                for (String OptionItem : OptionArr) {
                                    String[] OptionList = OptionItem.split("i");
                                    Integer Option = Integer.parseInt(OptionList[0]);
                                    Integer Param = Integer.parseInt(OptionList[1]);
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(Option, Param));
                                }
                                itemBuffTemplate.quantity = slItemBuff;
                                txtBuff += "x" + slItemBuff + " " + itemBuffTemplate.template.name + "\b";
                                InventoryServiceNew.gI().addItemBag(pBuffItem, itemBuffTemplate);
                                InventoryServiceNew.gI().sendItemBags(pBuffItem);
                            }
                            NpcService.gI().createTutorial(player, 24, txtBuff);
                            if (player.id != pBuffItem.id) {
                                NpcService.gI().createTutorial(player, 24, txtBuff);
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Player không online");
                        }
                        break;
                    }
                    // cái này làm trong 5p by lucyonfire
                    break;
                case ACTIVE:
                    String username = text[0];
                    try {
                        GirlkunResultSet rs = GirlkunDB.executeQuery("SELECT * FROM `account` WHERE username = ?", username);
                        if (rs.first()) {
                            GirlkunDB.executeUpdate("update account set active = 1 where username = ?", username);
                            Service.gI().sendThongBao(player, "Mở thành viên thành công");
                            rs.dispose();
                        } else {
                            Service.gI().sendThongBaoOK(player, "Không tìm thấy tên tài khoản");
                            rs.dispose();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case THAY_EXP:
                    short exp = Short.parseShort(text[0]);
                    if (exp >= 1 && exp <= 300) {
                    Manager.RATE_EXP_SERVER = exp;
                    Service.gI().sendThongBaoAllPlayer("|7|EXP SERVER HIỆN TẠI LÀ : X"+Manager.RATE_EXP_SERVER);
                    ServerNotify.gI().notify("EXP SERVER HIỆN TẠI : X"+Manager.RATE_EXP_SERVER);
                    } else {
                    Service.gI().sendThongBaoFromAdmin(player,"|2|EXP TỐI THIỂU LÀ X1 VÀ TỐI ĐA LÀ X300");    
                    }
                    break;
//                case THAY_EVENT:
//                    byte ev = Byte.parseByte(text[0]);
//                    if(ev > 14) {
//                    ev = 8;
//                    }
//                    ConstEvent.EVENT = ev;
//                    ServerNotify.gI().notify("Sự kiện hiện tại : " + ConstEvent.gI().getNameEv());
//                    Service.gI().sendThongBaoAllPlayer("|7|Sự Kiện Hiện Tại: " + ConstEvent.gI().getNameEv());
//                    break;
                case SLVANG:
                    byte slv = Byte.parseByte(text[0]);
                    if(slv>=1&&slv<=20) {
                       player.sovangauto = slv*1000000000;
                       player.autobv = true;
                       Service.gI().sendThongBao(player, "|7|Auto bán thỏi vàng khi vàng dưới\n"+Util.format(slv*1000000000)+" vàng");
                    } else {
                       Service.gI().sendThongBaoFromAdmin(player, "|7|Số lượng phải từ 200Tr và không lớn hơn giới hạn vàng của bản thân");
                    }
                    break;
                
                case BANVANG:
                    int v=Integer.parseInt(text[0]);try{int time=v;int quantity=0;long goldc=0;
                        Item tv=InventoryServiceNew.gI().findItem
                        (player.inventory.itemsBag, 457);
                    if(v>=1&&v<=tv.quantity){while(time>0){Thread.sleep(10);goldc+=500000000;time--;quantity+=1;
                    if(tv.quantity>=v--){player.inventory.gold += 500000000;
                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player,"|7|Bán Thành Công!\n|1|Nhận: "+ Util.numberToMoney(goldc) + " Vàng"); 
                    if(player.inventory.gold >= InventoryServiceNew.gI().getGodMax(player)) {
                        Service.gI().sendThongBaoFromAdmin(player,"|7|Đã quá giới hạn vàng, giới hạn vàng hiện tại là "
                        +Util.powerToString(InventoryServiceNew.gI().getGodMax(player))+" vàng"
                        +"\n|1|ĐÃ BÁN "+quantity +" THỎI VÀNG\nNHẬN ĐƯỢC : "+Util.powerToString(goldc)+" VÀNG"
                        +"\n|7|Hãy mở thêm giới hạn trữ vàng tại Trưởng Lão Guru(Đảo Guru)");
                    break;}}}}else{
                        Service.gI().sendThongBaoFromAdmin(player,"|7|Số đã nhập tối thiểu là 1"
                        + "\nVà không vượt quá số thỏi vàng của bản thân!");
                    break;}}catch(Exception e){}
                break;
                case CHANGE_PASSWORD:
                    Service.gI().changePassword(player, text[0], text[1], text[2]);
                    break;
                case GIFT_CODE:
                    GiftcodeService.gI().giftCode(player, text[0]);
                    break;
                case FIND_PLAYER:
                    Player pl = Client.gI().getPlayer(text[0]);
                    if (pl != null) {
                        int sl = InventoryServiceNew.gI().findItemBag(pl, (short)457) == null ? 0 : InventoryServiceNew.gI().findItemBag(pl, (short)457).quantity;
                        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_FIND_PLAYER, 12639, "|7|[ QUẢN LÝ ACCOUNT ]\n"
                                +"|1|Player Name : " + pl.name +"\n"
                                +"Account ID : " + pl.id + " | "+ "IP Connected : " + pl.getSession().ipAddress +" | "  + "Version : " + pl.getSession().version
                                +"\nHồng Ngọc Inventory : " + pl.inventory.ruby
                                +"\nCoin Vnđ Inventory : " + pl.getSession().vnd
                                +"\nThỏi Vàng Inventory : " + sl
                                +"\nActive Status : " + (pl.getSession().actived == 1 ? "isActived" : "isNonActive")
                                +"\nPrison Status : " + (pl.isJail() ? "Jail" : "unJail") 
                                +"\nAccount Status : " + (pl.isAdmin() ? "Key Controller" : "PlayerOnline ") + (pl.isAdmin2() ? "[ADMINISTRATOR]" : "")
                                +"\n|7|[KhanhDTK]",
                                new String[]{"Đi tới\n" + pl.name, "Gọi " + pl.name + "\ntới đây", "Đổi tên", "Ban", "Kick"},
                                pl);
                    } else {
                        Service.gI().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;
                case QUANLY:
                    Player ql = Client.gI().getPlayer(text[0]);
                    if (ql != null) {
                        int sl = InventoryServiceNew.gI().findItemBag(ql, (short)457) == null ? 0 : InventoryServiceNew.gI().findItemBag(ql, (short)457).quantity;
                        NpcService.gI().createMenuConMeo(player, ConstNpc.QUANLYTK, 12639, "|7|[ QUẢN LÝ ACCOUNT BẬC 2 ]\n"
                                +"|1|Player Name : " + ql.name +"\n"
                                +"Account ID : " + ql.id + " | "+ "IP Connected : " + ql.getSession().ipAddress +" | "  + "Version : " + ql.getSession().version
                                +"\nHồng Ngọc Inventory : " + ql.inventory.ruby
                                +"\nCoin Vnđ Inventory : " + ql.getSession().vnd
                                +"\nThỏi Vàng Inventory : " + sl
                                +"\nActive Status : " + (ql.getSession().actived == 1 ? "isActived" : "isNonActive")
                                +"\nPrison Status : " + (ql.isJail() ? "isJail" : "unJail") 
                                +"\nAccount Status : " + (ql.isAdmin() ? "Key Controller" : "PlayerOnline ") + (ql.isAdmin2() ? "[ADMINISTRATOR]" : "")
                                +"\n"
                                +"|7|[KhanhDTK]",
                                new String[]{"ĐỔI TÊN\n"+ql.name,"BAN ID\n"+ql.name, "KICK ID\n"+ql.name, "ACTIVE ID\n"+ql.name, "PET MODE\n"+ql.name,"NEXT TASK\n"+ql.name,"GIAM GIỮ\n"+ql.name,"CONTROLLER\nADMIN"},
                                ql);
                    } else {
                        Service.gI().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;

                case CHANGE_NAME: {
                    Player plChanged = (Player) PLAYER_ID_OBJECT.get((int) player.id);
                    if (plChanged != null) {
                        if (GirlkunDB.executeQuery("select * from player where name = ?", text[0]).next()) {
                            Service.gI().sendThongBao(player, "Tên nhân vật đã tồn tại");
                        } else {
                            plChanged.name = text[0];
                            GirlkunDB.executeUpdate("update player set name = ? where id = ?", plChanged.name, plChanged.id);
                            Service.gI().player(plChanged);
                            Service.gI().Send_Caitrang(plChanged);
                            Service.gI().sendFlagBag(plChanged);
                            Zone zone = plChanged.zone;
                            ChangeMapService.gI().changeMap(plChanged, zone, plChanged.location.x, plChanged.location.y);
                            Service.gI().sendThongBao(plChanged, "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
                            Service.gI().sendThongBao(player, "Đổi tên người chơi thành công");
                        }
                    }
                }
                break;
                case CHANGE_NAME_BY_ITEM: {
                    if (player.getSession() != null) {
                        if (GirlkunDB.executeQuery("select * from player where name = ?", text[0]).next()) {
                            Service.gI().sendThongBao(player, "Tên nhân vật đã tồn tại");
                            createFormChangeNameByItem(player);
                        } else {
                            Item theDoiTen = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2007);
                            if (theDoiTen == null) {
                                Service.gI().sendThongBao(player, "Không tìm thấy thẻ đổi tên");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(player, theDoiTen, 1);
                                player.name = text[0];
                                GirlkunDB.executeUpdate("update player set name = ? where id = ?", player.name, player.id);
                                Service.gI().player(player);
                                Service.gI().Send_Caitrang(player);
                                Service.gI().sendFlagBag(player);
                                Zone zone = player.zone;
                                ChangeMapService.gI().changeMap(player, zone, player.location.x, player.location.y);
                                Service.gI().sendThongBao(player, "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
                            }
                        }
                    }
                }
                break;
      case TAI:
                    if(player != null){
                    int sohntai = Integer.valueOf(text[0]);
                    if (sohntai > 50000){
                        Service.getInstance().sendThongBao(player, "Tối đa 50000 Hồng Ngọc!!");
                        return;
                    }
                    if (sohntai <= 0){
                        Service.getInstance().sendThongBao(player, "Nu Nu ai cho mày bug!!");
                        return;
                    }
                    if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1){
                        Service.getInstance().sendThongBao(player, "Ít nhất 2 ô trống trong hành trang!!");
                        return;
                    }
//                    Item tv1 = null;
//                    for (Item item : player.inventory.itemsBag) {
//                        if (item.isNotNullItem() && item.template.id == 457) {
//                            tv1 = item;
//                            break;
//                        }
//                    }
                    try {
                        if (player.inventory.ruby >= sohntai) {
//                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv1, sotvtai);
                            player.inventory.ruby -= sohntai;
                            Service.gI().sendMoney(player);
                            int TimeSeconds = 10;
                            Service.getInstance().sendThongBao(player, "Chờ 10 giây để biết ăn cơm hay ăn cứt");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x+y+z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
                                    Service.getInstance().sendThongBaoOK(player, "Kết quả"+ "\nSố hệ thống quay ra là :" +
                                    " " + x + " " + y + " " + z + "\nTổng là : " +tong+ "\nBạn đã cược : "
                                    + sohntai + " Hồng Ngọc vào Tài"  + "\nKết quả : Xỉu" + "\nCòn cái nịt.");
                                    return;
                                }
                             } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.getInstance().sendThongBaoOK(player, "Kết quả" + "Số hệ thống quay ra : " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + sohntai + " Hồng Ngọc vào Xỉu" + "\nKết quả : Tam hoa" + "\nCòn cái nịt.");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                
                                if (player != null) {
//                                    Item tvthang = ItemService.gI().createNewItem((short) 457);
//                                    tvthang.quantity = (int) Math.round(sotvtai * 1.8);
//                                    InventoryServiceNew.gI().addItemBag(player, tvthang);
                                    player.inventory.ruby += sohntai*1.8;
                                    Service.gI().sendMoney(player);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.getInstance().sendThongBaoOK(player, "Kết quả"+ "\nSố hệ thống quay ra : " + x + " " +
                                    y + " " + z+ "\nTổng là : " + tong + "\nBạn đã cược : " + sohntai +
                                    " Hồng Ngọc vào Tài"+ "\nKết quả : Tài"+ "\n\nVề bờ");
                                    return;
                                }
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Bạn không đủ Hồng Ngọc để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.getInstance().sendThongBao(player, "Lỗi.");
                    }
                    }
                case XIU:
                    if (player != null){
                    int sohnxiu = Integer.valueOf(text[0]);
                    if (sohnxiu > 50000){
                        Service.getInstance().sendThongBao(player, "Tối đa 50000 Hồng Ngọc!!");
                        return;
                    }
                    if (sohnxiu <= 0){
                        Service.getInstance().sendThongBao(player, "Nu Nu ai cho mày bug!!");
                        return;
                    }
                    if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1){
                        Service.getInstance().sendThongBao(player, "Ít nhất 2 ô trống trong hành trang!!");
                        return;
                    }
//                    Item tv2 = null;
//                    for (Item item : player.inventory.itemsBag) {
//                        if (item.isNotNullItem() && item.template.id == 457) {
//                            tv2 = item;
//                            break;
//                        }
//                    }
                    try {
                        if (player.inventory.ruby >= sohnxiu) {
//                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv2, sotvxiu);
                            player.inventory.ruby -= sohnxiu;
                            Service.gI().sendMoney(player);
                            int TimeSeconds = 10;
                            Service.getInstance().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x+y+z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
//                                    Item tvthang = ItemService.gI().createNewItem((short) 457);
//                                    tvthang.quantity = (int) Math.round(sotvxiu * 1.8);
//                                    InventoryServiceNew.gI().addItemBag(player, tvthang);
                                    player.inventory.ruby += sohnxiu*1.8;
                                    Service.gI().sendMoney(player);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.getInstance().sendThongBaoOK(player, "Kết quả"+ "\nSố hệ thống quay ra : " + x + " " +
                                    y + " " + z+ "\nTổng là : " + tong + "\nBạn đã cược : " + sohnxiu +
                                    " Hồng Ngọc vào Xỉu"+ "\nKết quả : Xỉu"+ "\n\nVề bờ");
                                    return;
                                }
                             } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.getInstance().sendThongBaoOK(player, "Kết quả" + "Số hệ thống quay ra : " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + sohnxiu + " Hồng Ngọc vào Xỉu" + "\nKết quả : Tam hoa" + "\nCòn cái nịt.");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                if (player != null) {
                                    Service.getInstance().sendThongBaoOK(player, "Kết quả"+ "\nSố hệ thống quay ra là :" +
                                    " " + x + " " + y + " " + z + "\nTổng là : " +tong+ "\nBạn đã cược : "
                                    + sohnxiu + " Hồng Ngọc vào Xỉu"  + "\nKết quả : Tài" + "\nCòn cái nịt.");
                                    return;
                                }
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Bạn không đủ Hồng Ngọc để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.getInstance().sendThongBao(player, "Lỗi.");
                    }}
//                 case TAI_TX:
//                    int sotvxiu1 = Integer.valueOf(text[0]);
//                    try {
//                        if (sotvxiu1 >= 1000 && sotvxiu1 <= 1000000000) {
//                            if (player.inventory.ruby >= sotvxiu1) {
//                                player.inventory.ruby -= sotvxiu1;
//                                player.goldTai += sotvxiu1;
//                                TaiXiu_Old.gI().goldTai += sotvxiu1;
//                                Service.gI().sendThongBao(player, "Bạn đã đặt " + Util.format(sotvxiu1) + " Hồng ngọc vào TÀI");
//                                TaiXiu_Old.gI().addPlayerTai(player);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendMoney(player);
//                                PlayerDAO.updatePlayer(player);
//                            } else {
//                                Service.gI().sendThongBao(player, "Bạn không đủ Hồng ngọc để chơi.");
//                            }
//                        } else {
//                            Service.gI().sendThongBao(player, "Cược ít nhất 1.000 - nhiều nhất 1.000.000.000 Hồng ngọc");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Service.gI().sendThongBao(player, "Lỗi.");
//                    }
//                    break;
//                     case XIU_TX:
//                    int sotvxiu2 = Integer.valueOf(text[0]);
//                    try {
//                        if (sotvxiu2 >= 1000 && sotvxiu2 <= 1000000000) {
//                            if (player.inventory.ruby >= sotvxiu2) {
//                                player.inventory.ruby -= sotvxiu2;
//                                player.goldXiu += sotvxiu2;
//                                TaiXiu_Old.gI().goldXiu += sotvxiu2;
//                                Service.gI().sendThongBao(player, "Bạn đã đặt " + Util.format(sotvxiu2) + " Hồng ngọc vào XỈU");
//                                TaiXiu_Old.gI().addPlayerXiu(player);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendMoney(player);
//                                PlayerDAO.updatePlayer(player);
//                            } else {
//                                Service.gI().sendThongBao(player, "Bạn không đủ Hồng ngọc để chơi.");
//                            }
//                        } else {
//                            Service.gI().sendThongBao(player, "Cược ít nhất 1.000 - nhiều nhất 1.000.000.000 Hồng ngọc ");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Service.gI().sendThongBao(player, "Lỗi.");
//                        System.out.println("nnnnn2  ");
//                    }
//                    break;

                case NAU_BANH_TET:
                    nauBanhTet(player, text[0]);
                    break;
                case NAU_BANH_CHUNG:
                    nauBanhChung(player, text[0]);
                    break;
                case DUA_HAU:
                    duahau(player, text[0]);
                    break;
                case BONG_HOA:
                    bonghoa(player, text[0]);
                    break;
                     case CHOOSE_LEVEL_CDRD:
                    int level3 = Integer.parseInt(text[0]);
                    if (level3 >= 1 && level3 <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.THAN_VU_TRU, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_CDRD,
                                    "Con có chắc chắn muốn tới con đường rắn độc cấp độ " + level3 + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level3);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                case CSDE:
                    byte CS = Byte.parseByte(text[0]);
                    try {
                        if (CS >= 1 && CS <= 100) {
                                Manager.TNPET = CS;
                                Service.gI().sendThongBaoFromAdmin(player,"|7|Bội Số Tăng Tiềm Năng Mỗi Giây Là : "+CS+"\nTổng Là : " + Util.format(CS*20) + " Điểm Tiềm Năng/Giây");
                        } else {
                            Service.gI().sendThongBao(player, "Bội số ít nhất là 1, cao nhất là 100");
                        }
                    } catch (Exception e) {
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;
                case CHOOSE_LEVEL_BDKB:
                    int level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.QUY_LAO_KAME, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_BDKB,
                                    "Con có chắc chắn muốn tới bản đồ kho báu cấp độ " + level + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case CHOOSE_LEVEL_KGHD:
                    int level1 = Integer.parseInt(text[0]);
                    if (level1 >= 1 && level1 <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.POPO, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_KGHD,
                                    "Con có chắc chắn muốn tới bản đồ kho báu cấp độ " + level1 + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level1);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case QUY_DOI_THOI_VANG:
                    short sotvdoi = Short.parseShort(text[0]);
                    int tinhtientv = sotvdoi * 1000;
                    if (sotvdoi > 1000) {
                        Service.gI().sendThongBao(player, "Quá giới hạn, mỗi lần đổi số lượng không được quá 1000");
                    }else if (sotvdoi < 5) {
                        Service.gI().sendThongBao(player, "Quá nghèo, mỗi lần đổi số lượng ít nhất phải là 5");
                    }
                     else if (player.getSession().vnd >= tinhtientv) {
                        PlayerDAO.subvndmua(player, tinhtientv);
                        Item thoiVang = ItemService.gI().createNewItem((short) 457, sotvdoi * Manager.TLDOITV);                                                      
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được\n" + Util.format(sotvdoi * Manager.TLDOITV)
                                + " Thỏi Vàng");
                    } else {
//                        Service.gI().sendThongBao(player, "Số tiền của bạn là " +player.getSession().vnd+ " không đủ để quy "
//                                + " đổi " + goldTrade + " thỏi vàng " + " " + "bạn cần thêm" +(player.getSession().vnd-goldTrade));
                        Service.gI().sendThongBao(player, "Số tiền không đủ thực hiện");
                    }
                    break;

                case QUY_DOI_HONG_NGOC:
                    short sohndoi = Short.parseShort(text[0]);
                    int tinhtienhn = sohndoi * 1000;
                    if (sohndoi > 1000) {
                        Service.gI().sendThongBao(player, "Quá giới hạn, mỗi lần đổi không được quá 1000");
                    }else if (sohndoi < 5) {
                        Service.gI().sendThongBao(player, "Quá nghèo, mỗi lần đổi số lượng ít nhất phải là 5");
                    }else if (player.session.vnd >= tinhtienhn) {
                        PlayerDAO.subvndmua(player, tinhtienhn);
                        player.inventory.ruby += tinhtienhn * Manager.TLDOIHN;// 1k = 1k5 hn
                        Service.gI().sendMoney(player);
                        Service.gI().sendThongBao(player, "bạn nhận được\n" + Util.format(tinhtienhn * Manager.TLDOIHN)
                                + " Hồng Ngọc");
                    } else {
                        Service.gI().sendThongBao(player, "Số tiền không đủ thực hiện");
                    }
                    break;
                      case QUY_DOI_XU_BAC:
                   short soxubacdoi = Short.parseShort(text[0]);
                    int tinhtienxubac = soxubacdoi * 1000;
                    if (soxubacdoi > 1000) {
                        Service.gI().sendThongBao(player, "Quá giới hạn, mỗi lần đổi số lượng không được quá 1000");
                    }else if (soxubacdoi < 5) {
                        Service.gI().sendThongBao(player, "Quá nghèo, mỗi lần đổi số lượng ít nhất phải là 5");
                    }
                     else if (player.getSession().vnd >= tinhtienxubac) {
                        PlayerDAO.subvndmua(player, tinhtienxubac);
                        Item xu = ItemService.gI().createNewItem((short) 1385, soxubacdoi * Manager.TLDOIXUBAC);                                                      
                        InventoryServiceNew.gI().addItemBag(player, xu);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được\n" + Util.format(soxubacdoi * Manager.TLDOIXUBAC)
                                + " XU BẠC");
                    } else {
//                        Service.gI().sendThongBao(player, "Số tiền của bạn là " +player.getSession().vnd+ " không đủ để quy "
//                                + " đổi " + goldTrade + " thỏi vàng " + " " + "bạn cần thêm" +(player.getSession().vnd-goldTrade));
                        Service.gI().sendThongBao(player, "Số tiền không đủ thực hiện");
                    }
                    break;
                      case QUY_DOI_THANG_TINH_THACH:
                   short sothangtinhthachdoi = Short.parseShort(text[0]);
                    int tinhtienthangtinhthach = sothangtinhthachdoi * 1000;
                    if (sothangtinhthachdoi > 1000) {
                        Service.gI().sendThongBao(player, "Quá giới hạn, mỗi lần đổi số lượng không được quá 1000");
                    }else if (sothangtinhthachdoi < 5) {
                        Service.gI().sendThongBao(player, "Quá nghèo, mỗi lần đổi số lượng ít nhất phải là 5");
                    }
                     else if (player.getSession().vnd >= tinhtienthangtinhthach) {
                        PlayerDAO.subvndmua(player, tinhtienthangtinhthach);
                        Item xu = ItemService.gI().createNewItem((short) 2031, sothangtinhthachdoi * Manager.TLDOITHANGTINHTHACH);                                                      
                        InventoryServiceNew.gI().addItemBag(player, xu);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được\n" + Util.format(sothangtinhthachdoi * Manager.TLDOITHANGTINHTHACH)
                                + " Thăng tinh thạch");
                    } else {
//                        Service.gI().sendThongBao(player, "Số tiền của bạn là " +player.getSession().vnd+ " không đủ để quy "
//                                + " đổi " + goldTrade + " thỏi vàng " + " " + "bạn cần thêm" +(player.getSession().vnd-goldTrade));
                        Service.gI().sendThongBao(player, "Số tiền không đủ thực hiện");
                    }
                    break;
                        case QUY_DOI_XU_VANG:
                   short soxuvangdoi = Short.parseShort(text[0]);
                    int tinhtienxuvang = soxuvangdoi * 1000;
                    if (soxuvangdoi > 1000) {
                        Service.gI().sendThongBao(player, "Quá giới hạn, mỗi lần đổi số lượng không được quá 1000");
                    }else if (soxuvangdoi < 5) {
                        Service.gI().sendThongBao(player, "Quá nghèo, mỗi lần đổi số lượng ít nhất phải là 5");
                    }
                     else if (player.getSession().vnd >= tinhtienxuvang) {
                        PlayerDAO.subvndmua(player, tinhtienxuvang);
                        Item xu = ItemService.gI().createNewItem((short) 1384, soxuvangdoi * Manager.TLDOIXUVANG);                                                      
                        InventoryServiceNew.gI().addItemBag(player, xu);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được\n" + Util.format(soxuvangdoi * Manager.TLDOIXUVANG)
                                + " XU Vàng");
                    } else {
//                        Service.gI().sendThongBao(player, "Số tiền của bạn là " +player.getSession().vnd+ " không đủ để quy "
//                                + " đổi " + goldTrade + " thỏi vàng " + " " + "bạn cần thêm" +(player.getSession().vnd-goldTrade));
                        Service.gI().sendThongBao(player, "Số tiền không đủ thực hiện");
                    }
                    break;
                    case TILE_NAP:
                    short tv = Short.parseShort(text[0]);
                    float hn = Float.parseFloat(text[1]);
                    Manager.TLDOIHN = hn;
                    Manager.TLDOITV = tv;
                    if (tv > 10) {
                        Manager.TLDOITV = 10;
                    }
                    if (tv < 1) {
                        Manager.TLDOITV = 1;
                    }
                    if (hn > 10) {
                        Manager.TLDOIHN = 10;
                    }
                    if (hn < 1) {
                        Manager.TLDOIHN = 1;
                    }
                    Service.gI().sendThongBaoFromAdmin(player, "|7|Tỉ lệ quy đổi hiện tại là : "
                            + "\n|2|X" + Manager.TLDOITV + " Thỏi Vàng"
                            + "\nX" + Util.format(Manager.TLDOIHN) + " Hồng Ngọc");
                    break;
                    
                case DONATE_CS:
                    int csbang = Integer.parseInt(text[0]);
                    Item cscanhan = InventoryServiceNew.gI().findItemBag(player, 2046);
                    if (cscanhan == null && player.clanMember.memberPoint < 1) {
                        Service.gI().sendThongBao(player, "Số điểm capsule bản thân không đủ để thực hiện");
                        break;
                    }  
                        InventoryServiceNew.gI().subQuantityItemsBag(player, cscanhan, csbang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendMoney(player);
                        player.clanMember.memberPoint -= csbang;
                        player.clan.capsuleClan += csbang;
                        player.clanMember.clanPoint += csbang;
                        Service.gI().sendThongBao(player, "bạn đã quyên góp " + csbang + " điểm bang");
                        break;
                    }
        } catch (Exception e) {
        }
    }

    public void createForm(Player pl, int typeInput, String title, SubInput... subInputs) {
        pl.iDMark.setTypeInput(typeInput);
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createForm(ISession session, int typeInput, String title, SubInput... subInputs) {
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createFormChangePassword(Player pl) {
        createForm(pl, CHANGE_PASSWORD, "Quên Mật Khẩu", new SubInput("Nhập mật khẩu đã quên", PASSWORD),
                new SubInput("Mật khẩu mới", PASSWORD),
                new SubInput("Nhập lại mật khẩu mới", PASSWORD));
    }

    public void createFormGiveItem(Player pl) {
        createForm(pl, GIVE_IT, "Gửi vật phẩm", new SubInput("Tên Người Chơi", ANY), new SubInput("Id Item", ANY), new SubInput("Số lượng", ANY));
    }
    public void thuitem(Player pl) {
        Player nameT = (Player) PLAYERID_OBJECT.get(pl.id);
        createForm(pl, THUITEM, "Thu vật phẩm Player : "+ nameT.name, new SubInput("Id Item", ANY), new SubInput("Số lượng", ANY));
    }
    public void createFormGiveIte2m(Player pl) {
        createForm(pl, GIVE_IT2, "BUFF LIST ITEM, LIST QUANTITY",
                new SubInput("Name Player", ANY),
                new SubInput("Id Item (LIST '-')", ANY),
                new SubInput("Quantity (LIST '-')", ANY));
    }

    public void nextnhiemvu(Player pl) {
        createForm(pl, NEXTNV, "NEXT NHIỆM VỤ TRỰC TIẾP",  new SubInput("ID NV (không quá 31)", ANY));
    }
     public void createFormChooseLevelCDRD(Player pl) {
        createForm(pl, CHOOSE_LEVEL_CDRD, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }
    public void ChatAll(Player pl) {
        createForm(pl, CHATALL, "CHAT ALL PLAYER", new SubInput("Chat Sex", ANY));
    }
    
    public void createFormGiftCode(Player pl) {
        createForm(pl, GIFT_CODE, "Nhập Gifcode ", new SubInput("Nhập Giftcode", ANY));
    }
    public void createFormActiveAccount(Player pl) {
        createForm(pl, ACTIVE, "Active account", new SubInput("Nhập tài khoản cần mở thành viên", ANY));
    }
    public void createFormEXP(Player pl) {
        createForm(pl, THAY_EXP, "EXP Hiện Tại Là "+Manager.RATE_EXP_SERVER, new SubInput("Không Dưới 1 Và Không Lớn Hơn 150", ANY));
    }
//    public void thayevent(Player pl) {
//        createForm(pl, THAY_EVENT, "Sự Kiện Hiện Tại : (" + ConstEvent.EVENT + ") "+ ConstEvent.gI().getNameEv(), new SubInput("NHẬP ID SỰ KIỆN MUỐN ĐỔI", ANY));
//    }
    public void banvang(Player pl) {
        createForm(pl, BANVANG, "Bán Vàng SLL", new SubInput("Nhập số thỏi vàng muốn bán", NUMERIC));
    }
    public void slvang(Player pl) {
        createForm(pl, SLVANG, "Auto Bán Thỏi Vàng\n"
                + "(Ví dụ : Bạn nhập 1 tức = 1.000.000.000 vàng)\nKhi vàng trong hành trang thấp hơn số vàng bạn nhập\nThỏi vàng sẽ được tự động bán đến giới hạn",
                new SubInput("Nhập số vàng muốn bán", ANY));
    }
    public void createFormNauBanhChung(Player player) {
        createForm(player, NAU_BANH_CHUNG, "Nấu bánh chưng", new SubInput("Nhập số lượng bánh chưng cần nấu", NUMERIC));
    }
    public void duahau(Player player) {
        createForm(player, DUA_HAU, "Dâng Dưa Hấu\n(1 dưa hấu = 1 điểm)", new SubInput("Nhập số lượng dưa muốn dâng", NUMERIC));
    }
    public void bonghoa(Player player) {
        createForm(player, BONG_HOA, "Đổi Capsule\n(100 điểm = 1 capsule)", new SubInput("Nhập số lượng capsule muốn đổi", NUMERIC));
    }
    public void createFormNauBanhTet(Player player) {
        createForm(player, NAU_BANH_TET, "Nấu bánh tết", new SubInput("Nhập số lượng bánh tết cần nấu", NUMERIC));
    }
    public void createFormFindPlayer(Player pl) {
        createForm(pl, FIND_PLAYER, "Tìm kiếm người chơi", new SubInput("Tên người chơi", ANY));
    }
    public void QuanLyTK(Player pl) {
        createForm(pl, QUANLY, "Quản Lý Account", new SubInput("Tên người chơi", ANY));
    }
    // tài xỉu
    public void TAI(Player pl) {
        createForm(pl, TAI, "Nhập Số Thỏi Vàng Đặt Tài", new SubInput("Chỉ nhập số (Dưới 1.000 thỏi vàng/1 lần cược)", ANY));
    }
    public void XIU(Player pl) {
        createForm(pl, XIU, "Nhập Số Thỏi Vàng Đặt Xỉu", new SubInput("Chỉ nhập số (Dưới 1,000 thỏi vàng/1 lần cược)", ANY));
    }
    public void TAI_taixiu(Player pl) {
        createForm(pl, TAI_TX, "Nhập Số Hồng Ngọc Đặt Tài", new SubInput("Chỉ nhập số (Tối Thiểu 10.000 Hồng Ngọc Và Tối Đa 500.000 Hồng Ngọc)", ANY));
    }
    public void XIU_taixiu(Player pl) {
        createForm(pl, XIU_TX, "Nhập Số Hồng Ngọc Đặt Xỉu", new SubInput("Chỉ nhập số (Tối Thiểu 10.000 Hồng Ngọc Và Tối Đa 500.000 Hồng Ngọc)", ANY));
    }
    public void TNPET(Player pl) {
        createForm(pl, CSDE, "Nhập Bội Số Tăng TN", new SubInput("Chỉ nhập số (Tối Thiểu Là 1 Và Tối Đa Là 100)", ANY));
    }
    // tài xỉu
    public void createFormNapThe(Player pl, String loaiThe, String menhGia) {
        LOAI_THE = loaiThe;
        MENH_GIA = menhGia;
        createForm(pl, NAP_THE, "Nạp thẻ", new SubInput("Số Seri", ANY), new SubInput("Mã thẻ", ANY));
    }

     public void createFormSenditem1(Player pl) {
        createForm(pl, SEND_ITEM_OP, "SEND Vật Phẩm Option",
                new SubInput("Tên người chơi", ANY),
                new SubInput("ID Trang Bị", NUMERIC),
                new SubInput("String Option1+i+Param1+v+Option2+i+Param2", ANY),
                new SubInput("Số lượng", NUMERIC));
    }
    
    public void createFormQDTV(Player pl) {
        createForm(pl, QUY_DOI_THOI_VANG, "Quy Đổi TV, giới hạn đổi không quá 1000 và không nhỏ hơn 5",
                new SubInput("(VD : NHẬP VÀO 1 = 1K VND = " + Manager.TLDOITV + " THỎI VÀNG)", NUMERIC));
    }
     public void createFormQDXUBAC(Player pl) {
        createForm(pl, QUY_DOI_XU_BAC, "Quy Đổi XU, giới hạn đổi không quá 1000 và không nhỏ hơn 5",
                new SubInput("(VD : NHẬP VÀO 1 = 1K VND = " + Manager.TLDOIXUBAC + " XU ITEM)", NUMERIC));
    }
       public void createFormQDTHANGTINHTHACH(Player pl) {
        createForm(pl, QUY_DOI_THANG_TINH_THACH, "Quy Đổi Thăng Tinh Thạch, giới hạn đổi không quá 1000 và không nhỏ hơn 5",
                new SubInput("(VD : NHẬP VÀO 1 = 1K VND = " + Manager.TLDOITHANGTINHTHACH + " Thăng tinh thạch)", NUMERIC));
    }
      public void createFormQDXUVANG(Player pl) {
        createForm(pl, QUY_DOI_XU_VANG, "Quy Đổi XU, giới hạn đổi không quá 1000 và không nhỏ hơn 5",
                new SubInput("(VD : NHẬP VÀO 1 = 1K VND = " + Manager.TLDOIXUVANG + " XU ITEM)", NUMERIC));
    }
    public void createFormQDHN(Player pl) {
        createForm(pl, QUY_DOI_HONG_NGOC, "Quy Đổi HN, giới hạn đổi không quá 1000 và không nhỏ hơn 5",
                new SubInput("(VD : NHẬP VÀO 1 = 1K VND = " + Util.format(Manager.TLDOIHN*1000) + " HỒNG NGỌC)", NUMERIC));
    }
    public void thayTileNap(Player pl) {
        createForm(pl, TILE_NAP, "Thay đổi tỉ lệ nạp",
                new SubInput("Nhập tỉ lệ thỏi vàng muốn đổi", NUMERIC),
                new SubInput("Nhập tỉ lệ hồng ngọc muốn đổi", ANY));
    }


    public void DonateCsbang(Player pl) {
        createForm(pl, DONATE_CS, "Donate (Điểm Capsule Cá Nhân của bạn sẽ donate vào bang)", new SubInput("Nhập số lượng capsule muốn quyên góp", NUMERIC));
    }
    
    public void createFormHOC_SKILL_TD1(Player pl) {

        createForm(pl, HOC_SKILL_TD1, "Điều kiện để học chiêu đặc biệt"
                + "\nĐủ 60 tỷ tiềm năng"
                + "\nĐủ 50k Hồng ngọc "
                + "\nĐủ 1 tỷ vàng ", new SubInput("Có đồng ý không", ANY));
    }

    public void createFormChangeName(Player pl, Player plChanged) {
        PLAYER_ID_OBJECT.put((int) pl.id, plChanged);
        createForm(pl, CHANGE_NAME, "Đổi tên " + plChanged.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChangeNameByItem(Player pl) {
        createForm(pl, CHANGE_NAME_BY_ITEM, "Đổi tên " + pl.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChooseLevelBDKB(Player pl) {
        createForm(pl, CHOOSE_LEVEL_BDKB, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormChooseLevelKGHD(Player pl) {
        createForm(pl, CHOOSE_LEVEL_KGHD, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void buffvnd(Player pl) {
        createForm(pl, GIVE_VND, "NẠP VND [ BUFF VND TRỰC TIẾP ]", new SubInput("PLAYER NAME", ANY), new SubInput("SỐ VNĐ NẠP [ TỔNG NẠP SẼ ĐƯỢC += SỐ VNĐ NẠP ]",ANY), new SubInput("SỐ VNĐ TRỪ [ TỔNG NẠP SẼ ĐƯỢC -= SỐ VNĐ TRỪ ]",ANY));
    }
    public void buffcspet(Player pl) {
        createForm(pl, GIVE_CS, "BUFF CHỈ SỐ ĐỆ TỬ [ TĂNG CS GỐC ]",
                new SubInput("SỨC MẠNH",ANY),
                new SubInput("TIỀM NĂNG",ANY),
                new SubInput("SỨC ĐÁNH",ANY),
                new SubInput("HP, KI",ANY),
                new SubInput("CRIT",ANY));
    }
    public void subcspet(Player pl) {
        createForm(pl, SUB_CS, "GIẢM CHỈ SỐ ĐỆ TỬ [ GIẢM CS GỐC ]",
                new SubInput("GIẢM SỨC MẠNH",ANY),
                new SubInput("GIẢM TIỀM NĂNG",ANY),
                new SubInput("GIẢM SỨC ĐÁNH",ANY),
                new SubInput("GIẢM HP, KI",ANY),
                new SubInput("GIẢM CRIT",ANY));
    }

    public static class SubInput {
        private String name;
        private byte typeInput;
        public SubInput(String name, byte typeInput) {
            this.name = name;
            this.typeInput = typeInput;
        }
    }
        private void duahau(Player player, String s) {
            int sl = Integer.parseInt(s);
            if (sl < 0 || sl > 999) {
                Service.gI().sendThongBao(player, "Số liệu không hợp lệ");
                return;
            }
            Item ngocboi = InventoryServiceNew.gI().findItemBag(player, 669);
            if (ngocboi == null || ngocboi.quantity < sl) {
               Service.gI().chat(player, "Thiếu dưa hấu");
            } else if (player.inventory.gold < 50_000_000) {
                Service.gI().chat(player, "Bạn không đủ 50Tr vàng");
            } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                Service.gI().chat(player, "Hành trang của bạn không đủ chỗ trống");
            } else {
                player.inventory.gold -= 50000000;
                InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi, sl);
                Service.gI().sendMoney(player);
                player.inventory.event += sl;
                Service.gI().sendMoney(player);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().chat(player, "Bạn nhận được " + sl + " điểm sự kiện");
            }
        }
        private void bonghoa(Player player, String s) {
            int sl = Integer.parseInt(s);
            if (sl < 0 || sl > 999) {
                Service.gI().sendThongBao(player, "Số liệu không hợp lệ");
                return;
            }
            if (player.inventory.event >= 100*sl) {
                Item capsule = ItemService.gI().createNewItem((short) 722, sl);
                player.inventory.event -= 100*sl;
                capsule.itemOptions.add(new ItemOption(207, 0));
                InventoryServiceNew.gI().addItemBag(player, capsule);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn nhận được x" + sl + " Capsule Hồng");
            } else {
                Service.getInstance().sendThongBao(player, "Cần " + (100*sl) + " điểm tích lũy để đổi");
            }
        }
        private void nauBanhChung(Player player, String s) {
        try {
            int slBanhChung = Math.abs(Integer.parseInt(s));
            if (slBanhChung < 0 || slBanhChung > 999) {
                Service.gI().sendThongBao(player, "Số lượng không hợp lệ");
                return;
            }
            Item laGiong = InventoryServiceNew.gI().findItemBag(player, 1439);
            Item gaoNep = InventoryServiceNew.gI().findItemBag(player, 1438);
            Item dauXanh = InventoryServiceNew.gI().findItemBag(player, 1437);
            Item giongTre = InventoryServiceNew.gI().findItemBag(player, 1441);
            Item thitLon = InventoryServiceNew.gI().findItemBag(player, 1442);
            if (laGiong == null) {
                Service.gI().sendThongBao(player, "Thiếu lá giông");
                return;
            }
            if (gaoNep == null) {
                Service.gI().sendThongBao(player, "Thiếu gạo nếp");
                return;
            }
            if (dauXanh == null) {
                Service.gI().sendThongBao(player, "Thiếu đậu xanh");
                return;
            }
            if (giongTre == null) {
                Service.gI().sendThongBao(player, "Thiếu giống tre");
                return;
            }
            if (thitLon == null) {
                Service.gI().sendThongBao(player, "Thiếu thịt lợn");
                return;
            }
            if (laGiong.quantity < (12 * slBanhChung)) {
                Service.gI().sendThongBao(player, "Không đủ lá giông");
                return;
            }
            if (gaoNep.quantity < (2 * slBanhChung)) {
                Service.gI().sendThongBao(player, "Không đủ gạo nếp");
                return;
            }
            if (dauXanh.quantity < (1 * slBanhChung)) {
                Service.gI().sendThongBao(player, "Không đủ đậu xanh");
                return;
            }
            if (giongTre.quantity < (12 * slBanhChung)) {
                Service.gI().sendThongBao(player, "Không đủ giống tre");
                return;
            }
            if (thitLon.quantity < (3 * slBanhChung)) {
                Service.gI().sendThongBao(player, "Không đủ thịt lợn");
                return;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(player, laGiong, (99 * slBanhChung));
            InventoryServiceNew.gI().subQuantityItemsBag(player, gaoNep, (50 * slBanhChung));
            InventoryServiceNew.gI().subQuantityItemsBag(player, dauXanh, (50 * slBanhChung));
            InventoryServiceNew.gI().subQuantityItemsBag(player, giongTre, (99 * slBanhChung));
            InventoryServiceNew.gI().subQuantityItemsBag(player, thitLon, (99 * slBanhChung));
            NauBanh.SoLuongBanhDangNau += slBanhChung;
            player.BanhChungDaNau += slBanhChung;
            Service.gI().sendThongBao(player, "Đã Nấu Bánh Chân");
            if (!NauBanh.gI().ListPlNauBanh.contains(player)) {
            NauBanh.gI().addListPlNauBanh(player);
            }
        } catch (NumberFormatException e) {
            Service.gI().sendThongBao(player, "Số lượng nhập không hợp lệ");
        }
    }

    private void nauBanhTet(Player player, String s) {
        try {
            int slBanhTet = Math.abs(Integer.parseInt(s));
            Item laChuoi = InventoryServiceNew.gI().findItemBag(player, 1440);
            Item gaoNep = InventoryServiceNew.gI().findItemBag(player, 1438);
            Item dauXanh = InventoryServiceNew.gI().findItemBag(player, 1437);
            Item giongTre = InventoryServiceNew.gI().findItemBag(player, 1441);
            Item thitLon = InventoryServiceNew.gI().findItemBag(player, 1442);
            if (laChuoi == null) {
                Service.gI().sendThongBao(player, "Thiếu lá chuối");
                return;
            }
            if (gaoNep == null) {
                Service.gI().sendThongBao(player, "Thiếu gạo nếp");
                return;
            }
            if (dauXanh == null) {
                Service.gI().sendThongBao(player, "Thiếu đậu xanh");
                return;
            }
            if (giongTre == null) {
                Service.gI().sendThongBao(player, "Thiếu giống tre");
                return;
            }
            if (thitLon == null) {
                Service.gI().sendThongBao(player, "Thiếu thịt lợn");
                return;
            }
            if (laChuoi.quantity < (12 * slBanhTet)) {
                Service.gI().sendThongBao(player, "Không đủ lá chuối");
                return;
            }
            if (gaoNep.quantity < (2 * slBanhTet)) {
                Service.gI().sendThongBao(player, "Không đủ gạo nếp");
                return;
            }
            if (dauXanh.quantity < (1 * slBanhTet)) {
                Service.gI().sendThongBao(player, "Không đủ đậu xanh");
                return;
            }
            if (giongTre.quantity < (12 * slBanhTet)) {
                Service.gI().sendThongBao(player, "Không đủ giống tre");
                return;
            }
            if (thitLon.quantity < (3 * slBanhTet)) {
                Service.gI().sendThongBao(player, "Không đủ thịt lợn");
                return;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(player, laChuoi, (99 * slBanhTet));
            InventoryServiceNew.gI().subQuantityItemsBag(player, gaoNep, (50 * slBanhTet));
            InventoryServiceNew.gI().subQuantityItemsBag(player, dauXanh, (50 * slBanhTet));
            InventoryServiceNew.gI().subQuantityItemsBag(player, giongTre, (99 * slBanhTet));
            InventoryServiceNew.gI().subQuantityItemsBag(player, thitLon, (99 * slBanhTet));
            NauBanh.SoLuongBanhDangNau += slBanhTet;
            player.BanhTetDaNau += slBanhTet;
            Service.gI().sendThongBao(player, "Đã Nấu Bánh Toét");
            if (!NauBanh.gI().ListPlNauBanh.contains(player)) {
            NauBanh.gI().addListPlNauBanh(player);
            }
        } catch (NumberFormatException e) {
            Service.gI().sendThongBao(player, "Số lượng nhập không hợp lệ");
        }
    }
}