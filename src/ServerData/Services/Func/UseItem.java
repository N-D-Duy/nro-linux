package ServerData.Services.Func;

import ServerData.Services.ChangeMapService;
import ServerData.Services.TransactionService;
import ServerData.Services.InventoryServiceNew;
import ServerData.Services.TaskService;
import ServerData.Services.ItemService;
import ServerData.Services.Service;
import ServerData.Services.PlayerService;
import ServerData.Services.ItemTimeService;
import ServerData.Services.RewardService;
//import ServerData.Services.NgocRongNamecService;
import ServerData.Services.MapService;
import ServerData.Services.NpcService;
import ServerData.Services.PetService;
import Server.Data.Consts.ConstMap;
import ServerData.Models.Item.Item;
import Server.Data.Consts.ConstNpc;
import Server.Data.Consts.ConstPlayer;
import com.girlkun.database.GirlkunDB;
import ServerData.Boss.BossManager;
import ServerData.Models.Item.Item.ItemOption;
import ServerData.Models.Map.Zone;
import ServerData.Models.NPC.BillEgg;
import ServerData.Models.NPC.MabuEgg;
import ServerData.Models.Player.Inventory;
import ServerData.Models.Player.Player;
import ServerData.Models.Player.PlayerSkill.Skill;
import com.girlkun.network.io.Message;
import ServerData.Server.Manager;
import ServerData.Utils.SkillUtil;
import ServerData.Utils.TimeUtil;
import ServerData.Utils.Util;
import ServerData.Server.MySession;
import ServerData.Utils.Logger;

import java.util.Date;
import java.util.Random;
import java.util.logging.Level;

public class UseItem {

    private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
    private static final int ITEM_BAG_TO_BOX = 1;
    private static final int ITEM_BODY_TO_BOX = 3;
    private static final int ITEM_BAG_TO_BODY = 4;
    private static final int ITEM_BODY_TO_BAG = 5;
    private static final int ITEM_BAG_TO_PET_BODY = 6;
    private static final int ITEM_BODY_PET_TO_BAG = 7;

    private static final byte DO_USE_ITEM = 0;
    private static final byte DO_THROW_ITEM = 1;
    private static final byte ACCEPT_THROW_ITEM = 2;
    private static final byte ACCEPT_USE_ITEM = 3;
    public static final int[][][] LIST_ITEM_CLOTHES = {
        // áo , quần , găng ,giày,rada
        //td -> nm -> xd
        {{0, 33, 3, 34, 136, 137, 138, 139, 230, 231, 232, 233, 555}, {6, 35, 9, 36, 140, 141, 142, 143, 242, 243, 244, 245, 556}, {21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257, 562}, {27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269, 563}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}},
        {{1, 41, 4, 42, 152, 153, 154, 155, 234, 235, 236, 237, 557}, {7, 43, 10, 44, 156, 157, 158, 159, 246, 247, 248, 249, 558}, {22, 46, 25, 45, 160, 161, 162, 163, 258, 259, 260, 261, 564}, {28, 47, 31, 48, 164, 165, 166, 167, 270, 271, 272, 273, 565}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}},
        {{2, 49, 5, 50, 168, 169, 170, 171, 238, 239, 240, 241, 559}, {8, 51, 11, 52, 172, 173, 174, 175, 250, 251, 252, 253, 560}, {23, 53, 26, 54, 176, 177, 178, 179, 262, 263, 264, 265, 566}, {29, 55, 32, 56, 180, 181, 182, 183, 274, 275, 276, 277, 567}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}}
    };

    private static UseItem instance;

    private UseItem() {

    }

    public static UseItem gI() {
        if (instance == null) {
            instance = new UseItem();
        }
        return instance;
    }

    public void getItem(MySession session, Message msg) {
        Player player = session.player;

        TransactionService.gI().cancelTrade(player);
        try {
            int type = msg.reader().readByte();
            int index = msg.reader().readByte();
            if (index == -1) {
                return;
            }
            switch (type) {
                case ITEM_BOX_TO_BODY_OR_BAG:
                    InventoryServiceNew.gI().itemBoxToBodyOrBag(player, index);
                    TaskService.gI().checkDoneTaskGetItemBox(player);
                    break;
                case ITEM_BAG_TO_BOX:
                    InventoryServiceNew.gI().itemBagToBox(player, index);
                    break;
                case ITEM_BODY_TO_BOX:
                    InventoryServiceNew.gI().itemBodyToBox(player, index);
                    break;
                case ITEM_BAG_TO_BODY:
                    InventoryServiceNew.gI().itemBagToBody(player, index);
                    break;
                case ITEM_BODY_TO_BAG:
                    InventoryServiceNew.gI().itemBodyToBag(player, index);
                    break;
                case ITEM_BAG_TO_PET_BODY:
                    InventoryServiceNew.gI().itemBagToPetBody(player, index);
                    break;
                case ITEM_BODY_PET_TO_BAG:
                    InventoryServiceNew.gI().itemPetBodyToBag(player, index);
                    break;
            }
            player.setClothes.setup();
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            player.setClanMember();
            Service.gI().point(player);
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);

        }
    }

    public void testItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        try {
            byte type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            System.out.println("type: " + type);
            System.out.println("where: " + where);
            System.out.println("index: " + index);
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);
        }
    }

    public void doItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        byte type;
        try {
            type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
//            System.out.println(type + " " + where + " " + index);
            switch (type) {
                case DO_USE_ITEM:
                    if (player != null && player.inventory != null) {
                        if (index != -1) {
                            Item item = player.inventory.itemsBag.get(index);
                            if (item.isNotNullItem()) {
                                if (item.template.type == 7) {
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Bạn chắc chắn học " + player.inventory.itemsBag.get(index).template.name + "?");
                                    player.sendMessage(msg);
                                } else {
                                    UseItem.gI().useItem(player, item, index);
                                }
                            }
                        } else {
                            this.eatPea(player);
                        }
                    }
                    break;
                case DO_THROW_ITEM:
                    if (!(player.zone.map.mapId == 21 || player.zone.map.mapId == 22 || player.zone.map.mapId == 23)) {
                        Item item = null;
                        if (where == 0) {
                            item = player.inventory.itemsBody.get(index);
                        } else {
                            item = player.inventory.itemsBag.get(index);
                        }
                        msg = new Message(-43);
                        msg.writer().writeByte(type);
                        msg.writer().writeByte(where);
                        msg.writer().writeByte(index);
                        msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + item.template.name + " (Mất Luôn)?");
                        player.sendMessage(msg);
                    } else {
                        Service.gI().sendThongBao(player, "Không thể vứt ở đây");
                    }
                    break;
                case ACCEPT_THROW_ITEM:
                    InventoryServiceNew.gI().throwItem(player, where, index);
                    Service.gI().point(player);
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;
                case ACCEPT_USE_ITEM:
                    UseItem.gI().useItem(player, player.inventory.itemsBag.get(index), index);
                    break;
            }
        } catch (Exception e) {
//            Logger.logException(UseItem.class, e);
        }
    }

    private void useItem(Player pl, Item item, int indexBag) {
        if (item.template.strRequire <= pl.nPoint.power) {
            switch (item.template.type) {
                case 7: //sách học, nâng skill
                    learnSkill(pl, item);
                    break;
                case 33:
                    break;
                case 6: //đậu thần
                    this.eatPea(pl);
                    break;
                case 12: //ngọc rồng các loại
                    controllerCallRongThan(pl, item);
                    controllerCalltrb(pl, item);
                    break;
                case 23: //thú cưỡi mới
                case 24: //thú cưỡi cũ
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    break;
                case 11: //item bag
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendFlagBag(pl);
                    break;
                 case 74:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendFoot(pl, item.template.id);
                    break;
                  case 36:
                      if (pl.inventory.itemsBody.size() > 12){
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.getInstance().addEffectChar(pl, (pl.inventory.itemsBody.get(12).template.part), 0, -1, -1, 1);
                      }
                    break;
                case 75:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.getInstance().sendchienlinh(pl, (short) (item.template.iconID - 1));
                    break;
                case 72:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.getInstance().sendPetFollow(pl, (short) (item.template.iconID - 1));
                    break;
                default:
                    switch (item.template.id) {
                        case 1387:
                            NpcService.gI().createMenuConMeo(pl, ConstNpc.MAUTEN, 12639, "Fuck Đùn",
                             "Blue","Red","White","Yellow","Grey","Green","Dark");
                            break;
                        case 1236:
                            phieungoc(pl, item);
                            break;
                        case 1239:
                            phieuvang(pl, item);
                            break;
                        case 457:
                        case 2116:
                            banvang(pl,item);
                            break;
                        case 1253:
                            monqua(pl, item);
                            break;
                        case 992:
                            pl.type = 1;
                            pl.maxTime = 5;
                            Service.gI().Transport(pl);
                            break;
                        case 570:
                            openWoodChest(pl, item);
                            break;
                        case 361:
                            if (pl.idNRNM != -1) {
                                Service.gI().sendThongBao(pl, "Chức năng tạm thời đang được cập nhật !");
                                return;
                            }
//                            pl.idGo = (short) Util.nextInt(0, 6);
//                            NpcService.gI().createMenuConMeo(pl, ConstNpc.CONFIRM_TELE_NAMEC, -1,
//                            "1 Sao [" + Manager.MAPS.get(NgocRongNamecService.gI().mapNrNamec[0]).mapName + " (" + NgocRongNamecService.gI().zoneNrNamec[0] + ") Khoảng cách: ("+NgocRongNamecService.gI().getDis(pl, 0, (short) 353)+"m)"
//                            + "]\n2 Sao [" + Manager.MAPS.get(NgocRongNamecService.gI().mapNrNamec[1]).mapName+ " ("+ NgocRongNamecService.gI().zoneNrNamec[1]+ ") Khoảng cách: ("+ NgocRongNamecService.gI().getDis(pl, 0, (short) 354)+"m)"
//                            + "]\n3 Sao [" + Manager.MAPS.get(NgocRongNamecService.gI().mapNrNamec[2]).mapName+ " ("+ NgocRongNamecService.gI().zoneNrNamec[2]+ ") Khoảng cách: ("+ NgocRongNamecService.gI().getDis(pl, 0, (short) 355)+"m)"
//                            + "]\n4 Sao [" + Manager.MAPS.get(NgocRongNamecService.gI().mapNrNamec[3]).mapName+ " ("+ NgocRongNamecService.gI().zoneNrNamec[3]+ ") Khoảng cách: ("+ NgocRongNamecService.gI().getDis(pl, 0, (short) 356)+"m)"
//                            + "]\n5 Sao [" + Manager.MAPS.get(NgocRongNamecService.gI().mapNrNamec[4]).mapName+ " ("+ NgocRongNamecService.gI().zoneNrNamec[4]+ ") Khoảng cách: ("+ NgocRongNamecService.gI().getDis(pl, 0, (short) 357)+"m)"
//                            + "]\n6 Sao [" + Manager.MAPS.get(NgocRongNamecService.gI().mapNrNamec[5]).mapName+ " ("+ NgocRongNamecService.gI().zoneNrNamec[5]+ ") Khoảng cách: ("+ NgocRongNamecService.gI().getDis(pl, 0, (short) 358)+"m)"
//                            + "]\n7 Sao [" + Manager.MAPS.get(NgocRongNamecService.gI().mapNrNamec[6]).mapName+ " ("+ NgocRongNamecService.gI().zoneNrNamec[6]+ ") Khoảng cách: ("+ NgocRongNamecService.gI().getDis(pl, 0, (short) 359)+"m)"+"]"
//                            , "Đến ngay\nViên " + (pl.idGo+1) + " Sao\n50 ngọc", "Kết thức");
//                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//                            InventoryServiceNew.gI().sendItemBags(pl);
                            break;
                        /*           Pet2 Linked Player           */
                        case 1999:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1996:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 892:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 893:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1482:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1487:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1488:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 942:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 943:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 944:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 967:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1107:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1140:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1133:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1180:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1181:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1196:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1197:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1198:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1221:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1222:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1223:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1229:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        case 1230:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            break;
                        /*           Pet2            */
                        case 211: //nho tím
                        case 212: //nho xanh
                            eatGrapes(pl, item);
                            break;
                            case 1502:// hop qua skh, item 2002 xd
                            UseItem.gI().SkhTL(pl, item);
                            break;
                        case 342:
                        case 343:
                        case 344:
                        case 345:
                            if (pl.zone.items.stream().filter(it -> it != null && it.itemTemplate.type == 22).count() < 5) {
                                Service.gI().DropVeTinh(pl, item, pl.zone, pl.location.x, pl.location.y);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.gI().sendThongBao(pl, "Đặt ít thôi bạn");
                            }
                            break;
                        case 380: //cskb
                            openCSKB(pl, item);
                            break;
                        case 568:
                            if (pl.mabuEgg == null) {
                                MabuEgg.createMabuEgg(pl);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.gI().chat(pl, "Bạn đã có trứng ở nhà nhà rồi");
                            }
                            break;
                        case 1991:
                            if (pl.pet == null) {

                                int gender1 = pl.gender;
                                PetService.gI().createCadicPet(pl, gender1);
                                Service.gI().sendThongBao(pl, "Bạn đổi thành công đệ Cadic SSJ4");
                                     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            }
                            if (pl.pet != null && pl.pet.inventory.itemsBody.get(0).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(1).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(2).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(3).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(4).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(5).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(6).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(7).isNotNullItem() == false) {

                                int gender1 = pl.gender;
                                PetService.gI().changeCadicPet(pl, gender1);
                                Service.gI().sendThongBao(pl, "Bạn đổi thành công đệ Cadic SSJ4");
                                     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.gI().sendThongBao(pl, "Vui lòng tháo đồ!");
                            }
                       
                            break;
                                  case 1992:
                            if (pl.pet == null) {

                                int gender1 = pl.gender;
                                PetService.gI().createGokuPet(pl, gender1);
                                Service.gI().sendThongBao(pl, "Bạn đổi thành công đệ Goku SSJ4");
                                     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            }
                            if (pl.pet != null && pl.pet.inventory.itemsBody.get(0).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(1).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(2).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(3).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(4).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(5).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(6).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(7).isNotNullItem() == false ) {

                                int gender1 = pl.gender;
                                PetService.gI().createGokuPet(pl, gender1);
                                Service.gI().sendThongBao(pl, "Bạn đổi thành công đệ Goku SSJ4");
                                     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.gI().sendThongBao(pl, "Vui lòng tháo đồ!");
                            }
                       
                            break;
                                 case 1990:
                            if (pl.pet == null) {

                                int gender1 = pl.gender;
                                PetService.gI().createMabuPet(pl, gender1);
                                Service.gI().sendThongBao(pl, "Bạn đổi thành công đệ Bư");
                                     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            }
                            if (pl.pet != null && pl.pet.inventory.itemsBody.get(0).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(1).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(2).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(3).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(4).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(5).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(6).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(7).isNotNullItem() == false ) {

                                int gender1 = pl.gender;
                                PetService.gI().createMabuPet(pl, gender1);
                                Service.gI().sendThongBao(pl, "Bạn đổi thành công đệ Bư");
                                     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.gI().sendThongBao(pl, "Vui lòng tháo đồ!");
                            }
                       
                            break;
                                 case 1989:
                            if (pl.pet == null) {

                                int gender1 = pl.gender;
                                PetService.gI().createCumberPet(pl, gender1);
                                Service.gI().sendThongBao(pl, "Bạn đổi thành công đệ Cumber");
                                     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            }
                            if (pl.pet != null && pl.pet.inventory.itemsBody.get(0).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(1).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(2).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(3).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(4).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(5).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(6).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(7).isNotNullItem() == false ) {

                                int gender1 = pl.gender;
                                PetService.gI().createCumberPet(pl, gender1);
                                Service.gI().sendThongBao(pl, "Bạn đổi thành công đệ Cumber");
                                     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.gI().sendThongBao(pl, "Vui lòng tháo đồ!");
                            }
                       
                            break;
                        case 381: //cuồng nộ
                        case 382: //bổ huyết
                        case 383: //bổ khí
                        case 384: //giáp xên
                        case 385: //ẩn danh
                        case 379: //máy dò capsule
                       // case 2037: //máy dò cosmos
                        case 2105: //máy dò cosmos
                        case 663: //bánh pudding
                        case 664: //xúc xíc
                        case 665: //kem dâu
                        case 666: //mì ly
                        case 667: //sushi
                        case 472:
                        case 1099:
                        case 1100:
                        case 1101:
                        case 1102:
                        case 1103:
                        case 1445:
                        case 1446:
                        case 1997:
                        useItemTime(pl, item);
                        break;
                        case 1411:
                            UseItem.gI().opensplvip(pl, item);
                            break;
                        case 1296: //máy dò boss
                            BossManager.gI().showListBoss2(pl);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 521: //tdlt
                            useTDLT(pl, item);
                            break;
                        case 454: //bông tai
                            UseItem.gI().usePorata(pl);
                            break;
                        case 193: //gói 10 viên capsule
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        case 194: //capsule đặc biệt
//                            if (pl.getSession().isJail) {
//                            Service.gI().sendThongBaoFromAdmin(pl, "|7|BẠN ĐANG BỊ GIAM XIN HÃY QUAY LẠI");
//                            ChangeMapService.gI().changeMapBySpaceShip(pl, 49, 0, 760);
//                            return;
//                            }
                            openCapsuleUI(pl);
                            break;
                        case 401: //đổi đệ tử
                            changePet(pl, item);
                            break;
                        case 402: //sách nâng chiêu 1 đệ tử
                        case 403: //sách nâng chiêu 2 đệ tử
                        case 404: //sách nâng chiêu 3 đệ tử
                        case 759: //sách nâng chiêu 4 đệ tử
                            upSkillPet(pl, item);
                            break;
                        case 921://bông tai
                            UseItem.gI().usePorata2(pl);
                            break;
                        case 1165: //bông tai c3
                            UseItem.gI().usePorata3(pl);
                            break;
                        case 1129: //bông tai c4
                            UseItem.gI().usePorata4(pl);
                            break;
                        case 2117: //bông tai c5
                            UseItem.gI().usePorata5(pl);
                            break;
                        case 1447:
                            UseItem.gI().openLixi(pl, item);
                            break;
                        case 1448:
                            UseItem.gI().openLixivip(pl, item);
                            break;
//                        case 1449:
//                            UseItem.gI().openHopqua(pl, item);
//                            break;
                        case 2000://hop qua skh, item 2000 đa hành tinh
                        case 2001://hop qua skh tl, item 2001 đa hành tinh
                        case 2002://hop qua skh hd, item 2002 đa hành tinh
                        case 2003://hop qua skh ts, item 2003 đa hành tinh
                        case 2004://hop do tl đa hành tinh
                        case 2005://hop do hd tl đa hành tinh
                        case 2006://hop do ts đa hành tinh
                            UseItem.gI().ItemSKH(pl, item);
                            break;
                        case 1105://hop qua skh, item 2002 xd
                            UseItem.gI().Hopts(pl, item);
                            break;
                        case 987:
                            Service.gI().sendThongBao(pl, "Bảo vệ trang bị không bị rớt cấp"); //đá bảo vệ
                            break;
                        case 1998:
                            useItemHopQuaTanThu(pl, item);
                            break;
                        case 1498://hop qua skh
                        case 1499://hop qua skh
                        case 1500://hop qua skh
                        case 1501://hop qua skh
                            UseItem.gI().HopSkhTuChon(pl, item);
                            break;
                        case 1994: //hopquatanthu
                            hopquatanthu(pl, item);
                            break;
                        case 1342:
                            hopquatt(pl, item);
                            break;
                        case 569:
                            useItemQuaDua(pl, item);
                            break;
                        case 669:
                            if (pl.billEgg != null) {
                            Service.gI().sendThongBao(pl,"Bạn đã trồng Dưa Hấu rồi");
                                return;
                            }
                            Service.gI().sendThongBao(pl,"Bạn nhận được Dưa Hấu, được trồng trước làng");
                            BillEgg.createBillEgg(pl);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 2007:
                            Input.gI().createFormChangeNameByItem(pl);
                            break;
                        case 1131:
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                break;
                            }
                            if (pl.pet.playerSkill.skills.get(1).skillId != -1 && pl.pet.playerSkill.skills.get(2).skillId != -1) {
                                pl.pet.openSkill2();
                                pl.pet.openSkill3();
                                InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Đã đổi thành công chiêu 2 3 đệ tử");
                            } else {
                                Service.gI().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                            }
                            break;

                        case 2027:
                        case 2028: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short) Util.nextInt(2019, 2026));
                                linhThu.itemOptions.add(new Item.ItemOption(50,  Util.nextInt(1,5)));
                                linhThu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1,5)));
                                linhThu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1,5)));
                                linhThu.itemOptions.add(new Item.ItemOption(95, Util.nextInt(1,5)));
                                linhThu.itemOptions.add(new Item.ItemOption(96, Util.nextInt(1,5)));
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Chúc mừng bạn nhận được Linh thú " + linhThu.template.name);
                            }
                            break;

                        }
                    }
                    break;
            }
            InventoryServiceNew.gI().sendItemBags(pl);
        } else {
            Service.gI().sendThongBaoOK(pl, "Sức mạnh không đủ yêu cầu");
        }
    }
    
   private void banvang(Player pl, Item item) {
       try {
            Item tv = InventoryServiceNew.gI().findItem(pl.inventory.itemsBag, 457);
            if(pl.inventory.gold >= InventoryServiceNew.gI().getGodMax(pl)) {
                Service.gI().sendThongBaoFromAdmin(pl,"|1|Quá giói hạn vàng, giới hạn hiện tại là "
                +Util.powerToString(InventoryServiceNew.gI().getGodMax(pl))
                +"\n|7|Hãy mở thêm giới hạn tại Trưởng Lão Guru(Đảo Guru)");
            return;
            }/* if (tv.quantity >= 10) {
                Input.gI().banvang(pl);
            } else { */
        //if (tv.quantity <= 9) {
        pl.inventory.gold += 500000000;
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
        PlayerService.gI().sendInfoHpMpMoney(pl); 
        Service.gI().sendThongBao(pl,"Sử dụng thỏi vàng thành công nhận được\n" + Util.format(500000000) + " vàng"); 
        } catch (Exception e) {
    }
}
       
   
   private void phieungoc(Player pl, Item item) {
        if (pl.getSession().actived == 1) {
            int gold = Util.nextInt(1000, 10000);
            pl.inventory.ruby += gold;
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
        PlayerService.gI().sendInfoHpMpMoney(pl); //  " +  Util.numberToMoney(gold) + "
        Service.gI().sendThongBao(pl, "Nhận thành công " + Util.numberToMoney(gold) + " hộc ngòng");
        } else {
            Service.gI().sendThongBaoOK(pl, "Hãy mở thành viên");
        }}
   
      private void monqua(Player pl, Item item) {
        try {
            GirlkunDB.executeUpdate("update account set is_jail = 1 where id = ? and username = ?",
                    pl.getSession().userId, pl.getSession().uu);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(UseItem.class.getName()).log(Level.SEVERE, null, ex);
        }
            pl.iDMark.setLastTimeKick(System.currentTimeMillis());
            pl.iDMark.setBan(true);
        Service.gI().sendThongBaoOK(pl,
                "Bạn sẽ nhận được bất ngờ sau 5 giây, vĩnh biệt...");
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        }
      
    private void phieuvang(Player pl, Item item) {
        if (pl.getSession().actived == 1) {
            Item gold = ItemService.gI().createNewItem((short) 457,Util.nextInt(10, 100));
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, gold);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.gI().sendThongBao(pl, "Nhận thành công " + gold.quantity + " thỏi vàng");
        }else {
            Service.gI().sendThongBaoOK(pl, "Hãy mở thành viên");
        }}
    
    private void openWoodChest(Player pl, Item item) {
        int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
        if (time != 0) {
            Item itemReward = null;
            int param = item.itemOptions.size();
            int gold = 0;
            int[] listItem = {441, 442, 443, 444, 445, 446, 447, 220, 221, 222, 223, 224, 225, 16, 17, 18, 457};
            int[] listClothesReward;
            int[] listItemReward;
            String text = "Bạn nhận được\n";
            if (param < 8) {
                gold = 100000 * param;
                listClothesReward = new int[]{randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 3);
            } else if (param < 10) {
                gold = 250000 * param;
                listClothesReward = new int[]{randClothes(param), randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 4);
            } else {
                gold = 500000 * param;
                listClothesReward = new int[]{randClothes(param), randClothes(param), randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 6);
                int ruby = Util.nextInt(1, 5);
                pl.inventory.ruby += ruby;
                pl.textRuongGo.add(text + "|1| " + ruby + " Hồng Ngọc");
            }
            for (int i : listClothesReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionClothes(itemReward.template.id, itemReward.template.type, itemReward.itemOptions);
                RewardService.gI().initStarOption(itemReward, new RewardService.RatioStar[]{new RewardService.RatioStar((byte) 1, 1, 2), new RewardService.RatioStar((byte) 2, 1, 3), new RewardService.RatioStar((byte) 3, 1, 4), new RewardService.RatioStar((byte) 4, 1, 5),});
                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            for (int i : listItemReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
//         RewardService.gI().initBaseOptionSaoPhaLe(itemReward);
                itemReward.quantity = Util.nextInt(1, 5);
                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            if (param == 11) {
                itemReward = ItemService.gI().createNewItem((short) 457);
                itemReward.quantity = Util.nextInt(1, 30);
                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1, "Bạn nhận được\n|1|+" + Util.numberToMoney(gold) + " vàng", "OK [" + pl.textRuongGo.size() + "]");
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            pl.inventory.addGold(gold);
            InventoryServiceNew.gI().sendItemBags(pl);
            PlayerService.gI().sendInfoHpMpMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Vui lòng đợi 24h");
        }
    }

    private int randClothes(int level) {
        return LIST_ITEM_CLOTHES[Util.nextInt(0, 2)][Util.nextInt(0, 4)][level - 1];
    }

    

    private void useItemChangeFlagBag(Player player, Item item) {
        switch (item.template.id) {
            case 994: //vỏ ốc
                break;
            case 995: //cây kem
                break;
            case 996: //cá heo
                break;
            case 997: //con diều
                break;
            case 998: //diều rồng
                break;
            case 999: //mèo mun
                if (!player.effectFlagBag.useMeoMun) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useMeoMun = !player.effectFlagBag.useMeoMun;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1000: //xiên cá
                if (!player.effectFlagBag.useXienCa) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useXienCa = !player.effectFlagBag.useXienCa;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1001: //phóng heo
                if (!player.effectFlagBag.usePhongHeo) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.usePhongHeo = !player.effectFlagBag.usePhongHeo;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
        }
        Service.gI().point(player);
        Service.gI().sendFlagBag(player);
    }

    private void changePet(Player player, Item item) {
        if (player.pet != null&&player.pet.inventory.itemsBody.get(0).isNotNullItem()==false&&player.pet.inventory.itemsBody.get(1).isNotNullItem()==false&&player.pet.inventory.itemsBody.get(2).isNotNullItem()==false&&player.pet.inventory.itemsBody.get(3).isNotNullItem()==false&&player.pet.inventory.itemsBody.get(4).isNotNullItem()==false&&player.pet.inventory.itemsBody.get(5).isNotNullItem()==false&&player.pet.inventory.itemsBody.get(6).isNotNullItem()==false&&player.pet.inventory.itemsBody.get(7).isNotNullItem()==false) {
            int gender = player.pet.gender + 1;
            if (gender > 2) {
                gender = 0;
            }
            PetService.gI().changeNormalPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện, vui lòng tháo đồ!");
        }
    }

    private void DeCumMuahe(Player player, Item item) {
        if (player.pet != null) {
          //  int gender = player.pet.gender;
            PetService.gI().changePicPet(player);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            Service.gI().sendThongBao(player, "Nhận được đệ Kefla SSJ2");
        } else {
            Service.gI().sendThongBao(player, "Bạn làm gì đã có đệ");
        }
    }

    private void changePetPic(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changePicPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }
    private void changeMabuPet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeMabuPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBaoOK(player, "Bạn phải có đệ thường trước");
        }
    }
    
    private void openPhieuCaiTrangHaiTac(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            Item ct = ItemService.gI().createNewItem((short) Util.nextInt(618, 626));
            ct.itemOptions.add(new ItemOption(147, 3));
            ct.itemOptions.add(new ItemOption(77, 3));
            ct.itemOptions.add(new ItemOption(103, 3));
            ct.itemOptions.add(new ItemOption(149, 0));
            if (item.template.id == 2006) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            } else if (item.template.id == 2007) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 30)));
            }
            InventoryServiceNew.gI().addItemBag(pl, ct);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void eatGrapes(Player pl, Item item) {
        int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
        if (percentCurrentStatima > 50) {
            Service.gI().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
            return;
        } else if (item.template.id == 211) {
            pl.nPoint.stamina = pl.nPoint.maxStamina;
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
        } else if (item.template.id == 212) {
            pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
        PlayerService.gI().sendCurrentStamina(pl);
    }

    private void openCSKB(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {20, 190, 190, 190, 381, 382, 383, 384, 385};
            int[][] gold = {{50000, 100000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }
      private void opensplvip(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] itemne = {1412,1413,1414,1415,1416};
            byte index2 = (byte) Util.nextInt(0,itemne.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(itemne[index2]);
             it.itemOptions.add(new Item.ItemOption(30, 1));
            InventoryServiceNew.gI().addItemBag(pl, it);
           
            icon[1] = it.template.iconID;
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void hopquatt(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] rac = {1333, 1344, 1345};
            byte index2 = (byte) Util.nextInt(0,rac.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(rac[index2]);
            if (it.template.id == 1345) {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 15)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 15)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(1, 3)));
                if (Util.isTrue(10, 100)) {
                    it.itemOptions.add(new ItemOption(100, Util.nextInt(1, 20)));
                    it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 20)));
                }
            } else {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 15)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 15)));
                it.itemOptions.add(new ItemOption(95, Util.nextInt(1, 5)));
                it.itemOptions.add(new ItemOption(96, Util.nextInt(1, 5)));
            }
            if (Util.isTrue(199, 200)) {
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
            }
            it.itemOptions.add(new ItemOption(30, 0));
            InventoryServiceNew.gI().addItemBag(pl, it);
            icon[0] = it.template.iconID;
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }
        
//    private void openHopqua(Player pl, Item item) {
//        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
//            short[] itemne = {1447,16,17,18};
//            byte index2 = (byte) Util.nextInt(0,itemne.length - 1);
//            short[] icon = new short[2];
//            icon[0] = item.template.iconID;
//            Item it = ItemService.gI().createNewItem(itemne[index2]);
//            InventoryServiceNew.gI().addItemBag(pl, it);
//            icon[1] = it.template.iconID;
//            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
//            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//            InventoryServiceNew.gI().sendItemBags(pl);
//            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
//        } else {
//            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
//        }
//    }
    private void openLixi(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] itemne = {190, 190, 190, 1448, 381, 382, 383, 384, 385};
            int[][] gold = {{100000000, 200000000}};
            byte index2 = (byte) Util.nextInt(0,itemne.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index2 <= 2) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
            Item it = ItemService.gI().createNewItem(itemne[index2]);
            InventoryServiceNew.gI().addItemBag(pl, it);
            icon[1] = it.template.iconID;
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }
    private void openLixivip(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] itemne = {190, 190, 190, 1099, 1100, 1101,1102, 1103};
            int[][] gold = {{100000000, 200000000}};
            byte index2 = (byte) Util.nextInt(0,itemne.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index2 <= 2) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
            Item it = ItemService.gI().createNewItem(itemne[index2]);
            InventoryServiceNew.gI().addItemBag(pl, it);
            icon[1] = it.template.iconID;
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }
     private void useDeCadic(Player pl, Item item) {

        if (pl.pet == null) {

            int gender1 = pl.gender;
            PetService.gI().createCadicPet(pl, gender1);
            Service.gI().sendThongBao(pl, "Bạn mua thành công đệ Cadic SSJ4");
        }
        if (pl.pet != null && pl.pet.inventory.itemsBody.get(0).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(1).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(2).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(3).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(4).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(5).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(6).isNotNullItem() == false && pl.pet.inventory.itemsBody.get(7).isNotNullItem() == false ) {

            int gender1 = pl.gender;
            PetService.gI().changeCadicPet(pl, gender1);
            Service.gI().sendThongBao(pl, "Bạn mua thành công đệ Cadic SSJ4");
        } else {
            Service.gI().sendThongBao(pl, "Vui lòng tháo đồ!");
        }

    }

    private void useItemHopQuaTanThu(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {190, 190, 14, 15, 16, 17, 18, 19, 20};
            int[][] gold = {{100000000, 200000000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 1) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(30, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }
    public void hopquatanthu(Player player, Item item) {
        switch (player.gender) {
            case 0:
            {
                Item itemReward = ItemService.gI().createNewItem((short) 0);
                Item itemReward1 = ItemService.gI().createNewItem((short) 6);
                Item itemReward2 = ItemService.gI().createNewItem((short) 12);
                Item itemReward3 = ItemService.gI().createNewItem((short) 21);
                Item itemReward4 = ItemService.gI().createNewItem((short) 27);
                itemReward.quantity = 1;
                itemReward1.quantity = 1;
                itemReward2.quantity = 1;
                itemReward3.quantity = 1;
                itemReward4.quantity = 1;
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                    itemReward.itemOptions.add(new ItemOption(47, 5));
                    itemReward1.itemOptions.add(new ItemOption(7, 30));
                    itemReward2.itemOptions.add(new ItemOption(14, 1));
                    itemReward3.itemOptions.add(new ItemOption(0, 5));
                    itemReward4.itemOptions.add(new ItemOption(6, 30));
                    
                    itemReward.itemOptions.add(new ItemOption(107, 6));
                    itemReward1.itemOptions.add(new ItemOption(107, 6));
                    itemReward2.itemOptions.add(new ItemOption(107, 6));
                    itemReward3.itemOptions.add(new ItemOption(107, 6));
                    itemReward4.itemOptions.add(new ItemOption(107, 6));
                    
                    itemReward.itemOptions.add(new ItemOption(30, 1));
                    itemReward1.itemOptions.add(new ItemOption(30, 1));
                    itemReward2.itemOptions.add(new ItemOption(30, 1));
                    itemReward3.itemOptions.add(new ItemOption(30, 1));
                    itemReward4.itemOptions.add(new ItemOption(30, 1));
                    
                    InventoryServiceNew.gI().addItemBag(player, itemReward);
                    InventoryServiceNew.gI().addItemBag(player, itemReward1);
                    InventoryServiceNew.gI().addItemBag(player, itemReward2);
                    InventoryServiceNew.gI().addItemBag(player, itemReward3);
                    InventoryServiceNew.gI().addItemBag(player, itemReward4);
                    
                    Service.getInstance().sendThongBao(player, "Bạn đã nhận được set đồ 6 sao !");
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                    InventoryServiceNew.gI().sendItemBags(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
                }}
            break;
            case 1:
            {
                Item itemReward = ItemService.gI().createNewItem((short) 1);
                Item itemReward1 = ItemService.gI().createNewItem((short) 7);
                Item itemReward2 = ItemService.gI().createNewItem((short) 12);
                Item itemReward3 = ItemService.gI().createNewItem((short) 22);
                Item itemReward4 = ItemService.gI().createNewItem((short) 28);
                itemReward.quantity = 1;
                itemReward1.quantity = 1;
                itemReward2.quantity = 1;
                itemReward3.quantity = 1;
                itemReward4.quantity = 1;
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                    
                    itemReward.itemOptions.add(new ItemOption(47, 5));
                    itemReward1.itemOptions.add(new ItemOption(7, 30));
                    itemReward2.itemOptions.add(new ItemOption(14, 1));
                    itemReward3.itemOptions.add(new ItemOption(0, 5));
                    itemReward4.itemOptions.add(new ItemOption(6, 30));
                    
                    itemReward.itemOptions.add(new ItemOption(107, 6));
                    itemReward1.itemOptions.add(new ItemOption(107, 6));
                    itemReward2.itemOptions.add(new ItemOption(107, 6));
                    itemReward3.itemOptions.add(new ItemOption(107, 6));
                    itemReward4.itemOptions.add(new ItemOption(107, 6));
                    
                    itemReward.itemOptions.add(new ItemOption(30, 1));
                    itemReward1.itemOptions.add(new ItemOption(30, 1));
                    itemReward2.itemOptions.add(new ItemOption(30, 1));
                    itemReward3.itemOptions.add(new ItemOption(30, 1));
                    itemReward4.itemOptions.add(new ItemOption(30, 1));
                    
                    InventoryServiceNew.gI().addItemBag(player, itemReward);
                    InventoryServiceNew.gI().addItemBag(player, itemReward1);
                    InventoryServiceNew.gI().addItemBag(player, itemReward2);
                    InventoryServiceNew.gI().addItemBag(player, itemReward3);
                    InventoryServiceNew.gI().addItemBag(player, itemReward4);
                    
                    Service.getInstance().sendThongBao(player, "Bạn đã nhận được set đồ 6 sao !");
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                    InventoryServiceNew.gI().sendItemBags(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
                }}
            break;
            case 2:
            {
                Item itemReward = ItemService.gI().createNewItem((short) 2);
                Item itemReward1 = ItemService.gI().createNewItem((short) 8);
                Item itemReward2 = ItemService.gI().createNewItem((short) 12);
                Item itemReward3 = ItemService.gI().createNewItem((short) 23);
                Item itemReward4 = ItemService.gI().createNewItem((short) 29);
                itemReward.quantity = 1;
                itemReward1.quantity = 1;
                itemReward2.quantity = 1;
                itemReward3.quantity = 1;
                itemReward4.quantity = 1;
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                    itemReward.itemOptions.add(new ItemOption(47, 5));
                    itemReward1.itemOptions.add(new ItemOption(7, 30));
                    itemReward2.itemOptions.add(new ItemOption(14, 1));
                    itemReward3.itemOptions.add(new ItemOption(0, 5));
                    itemReward4.itemOptions.add(new ItemOption(6, 30));
                    
                    itemReward.itemOptions.add(new ItemOption(107, 6));
                    itemReward1.itemOptions.add(new ItemOption(107, 6));
                    itemReward2.itemOptions.add(new ItemOption(107, 6));
                    itemReward3.itemOptions.add(new ItemOption(107, 6));
                    itemReward4.itemOptions.add(new ItemOption(107, 6));
                    
                    itemReward.itemOptions.add(new ItemOption(30, 1));
                    itemReward1.itemOptions.add(new ItemOption(30, 1));
                    itemReward2.itemOptions.add(new ItemOption(30, 1));
                    itemReward3.itemOptions.add(new ItemOption(30, 1));
                    itemReward4.itemOptions.add(new ItemOption(30, 1));
                    
                    InventoryServiceNew.gI().addItemBag(player, itemReward);
                    InventoryServiceNew.gI().addItemBag(player, itemReward1);
                    InventoryServiceNew.gI().addItemBag(player, itemReward2);
                    InventoryServiceNew.gI().addItemBag(player, itemReward3);
                    InventoryServiceNew.gI().addItemBag(player, itemReward4);
                    
                    Service.getInstance().sendThongBao(player, "Bạn đã nhận được set đồ 6 sao !");
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                    InventoryServiceNew.gI().sendItemBags(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
                }}
                break;
        }
    }
    
    public void TOP(Player player) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 3) {
        if (player.topsm > 0) {
            Item itemReward = ItemService.gI().createNewItem((short) 1412, (player.topsm == 1 ? 30 : player.topsm == 2 ? 25 : 20)); // spl 4% sd
            Item itemReward1 = ItemService.gI().createNewItem((short) 1448,(player.topsm == 1 ? 250 : player.topsm == 2 ? 200 : 150)); // lì xì
            Item itemReward2 = ItemService.gI().createNewItem((short) 1099,(player.topsm == 1 ? 200 : player.topsm == 2 ? 150 : 100)); // cnsc
            player.usedh2 = true;
            Service.gI().sendTitle(player, "DH2");
            if (player.timedh2 == 0) {
                player.timedh2 = (player.topsm == 1 ? (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16)
                : player.topsm == 2 ? (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) : (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7)));
            } else {
                player.timedh2 += (player.topsm == 1 ? (1000 * 60 * 60 * 24 * 15) + (1000 * 60 * 60 * 24 * 16)
                : player.topsm == 2 ? (1000 * 60 * 60 * 24 * 15) : (1000 * 60 * 60 * 24 * 7));
            }
            InventoryServiceNew.gI().addItemBag(player, itemReward);
            InventoryServiceNew.gI().addItemBag(player, itemReward1);
            InventoryServiceNew.gI().addItemBag(player, itemReward2);
            Service.getInstance().sendThongBao(player, "Nhận thành công quà TOP" + player.topsm + " Sức Mạnh!");
            player.topsm = 0;
        } else if (player.topnv > 0) {
            Item itemReward = ItemService.gI().createNewItem((short) 1412, (player.topnv == 1 ? 30 : player.topnv == 2 ? 25 : 20)); // spl 4% sd
            Item itemReward1 = ItemService.gI().createNewItem((short) 1448,(player.topnv == 1 ? 250 : player.topnv == 2 ? 200 : 150)); // lì xì
            Item itemReward2 = ItemService.gI().createNewItem((short) 1099,(player.topnv == 1 ? 200 : player.topnv == 2 ? 150 : 100)); // cnsc
            player.usedh3 = true;
            Service.gI().sendTitle(player, "DH3");
            if (player.timedh3 == 0) {
                player.timedh3 = (player.topnv == 1 ? (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16)
                : player.topnv == 2 ? (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) : (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7)));
            } else {
                player.timedh3 += (player.topnv == 1 ? (1000 * 60 * 60 * 24 * 15) + (1000 * 60 * 60 * 24 * 16)
                : player.topnv == 2 ? (1000 * 60 * 60 * 24 * 15) : (1000 * 60 * 60 * 24 * 7));
            }
            InventoryServiceNew.gI().addItemBag(player, itemReward);
            InventoryServiceNew.gI().addItemBag(player, itemReward1);
            InventoryServiceNew.gI().addItemBag(player, itemReward2);
            Service.getInstance().sendThongBao(player, "Nhận thành công quà TOP" + player.topnv + " Nhiệm Vụ!");
            player.topnv = 0;
        } else if (player.topnap > 0) {
            Item itemReward = ItemService.gI().createNewItem((short) 1412, (player.topnap == 1 ? 40 : player.topnap == 2 ? 35 : 30)); // spl 4% sd
            Item itemReward1 = ItemService.gI().createNewItem((short) 1448,(player.topnap == 1 ? 300 : player.topnap == 2 ? 250 : 200)); // lì xì
            Item itemReward2 = ItemService.gI().createNewItem((short) 1099,(player.topnap == 1 ? 200 : player.topnap == 2 ? 150 : 100)); // cnsc
            Item itemReward3 = ItemService.gI().createNewItem((short) 1499,(player.topnap == 1 ? 5 : player.topnap == 2 ? 5 : 1)); // skh tl
            player.usedh1 = true;
            Service.gI().sendTitle(player, "DH1");
            if (player.timedh1 == 0) {
                player.timedh1 = (player.topnap == 1 ? (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16)
                : player.topnap == 2 ? (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) : (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7)));
            } else {
                player.timedh1 += (player.topnap == 1 ? (1000 * 60 * 60 * 24 * 15) + (1000 * 60 * 60 * 24 * 16)
                : player.topnap == 2 ? (1000 * 60 * 60 * 24 * 15) : (1000 * 60 * 60 * 24 * 7));
            }
            InventoryServiceNew.gI().addItemBag(player, itemReward);
            InventoryServiceNew.gI().addItemBag(player, itemReward1);
            InventoryServiceNew.gI().addItemBag(player, itemReward2);
            InventoryServiceNew.gI().addItemBag(player, itemReward3);
            Service.getInstance().sendThongBao(player, "Nhận thành công quà TOP" + player.topnap + " Nạp!");
            player.topnap = 0;
        } else {
        Service.getInstance().sendThongBao(player, "Bạn không nằm trong top nào!");
        }
    } else {
        Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 3 ô trống hành trang");
    }
        InventoryServiceNew.gI().sendItemBags(player);
}
    
    public void ComfirmMocNap(Player player) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 5) {
        Item itemqua;
        Item itemqua1;
        Item itemqua2;
        Item itemqua3;
        Item itemqua4;
        Item itemqua5;
          Item itemqua6;
        try {
            int time = 5;
            if (player.getSession().tongnap >= 100000 && player.mocnap == 0) {
                Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 100K\nSau " + time + " Giây!");
                while (time > 0) {
                time--;
                Thread.sleep(1000);
                Service.gI().sendThongBao(player, "|7|" + time);
                }
                player.mocnap = 100000;
                itemqua = ItemService.gI().createNewItem((short) 457,200);
                itemqua.itemOptions.add(new Item.ItemOption(30, 1));
                itemqua1 = ItemService.gI().createNewItem((short) 1296,10);
                itemqua2 = ItemService.gI().createNewItem((short) 2000,10);
                itemqua3 = ItemService.gI().createNewItem((short) 1997,5);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                InventoryServiceNew.gI().addItemBag(player, itemqua);
                InventoryServiceNew.gI().addItemBag(player, itemqua1);
                InventoryServiceNew.gI().addItemBag(player, itemqua2);
                InventoryServiceNew.gI().addItemBag(player, itemqua3);
                InventoryServiceNew.gI().sendItemBags(player);
            } else if (player.getSession().tongnap >= 200000 && player.mocnap == 100000) {
                Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 200K\nSau " + time + " Giây!");
                while (time > 0) {
                time--;
                Thread.sleep(1000);
                Service.gI().sendThongBao(player, "|7|" + time);
                }
                player.mocnap = 200000;
                itemqua = ItemService.gI().createNewItem((short) 457,300);
                itemqua.itemOptions.add(new Item.ItemOption(30, 1));
  
                itemqua2 = ItemService.gI().createNewItem((short) 1296,10);
                  itemqua3 = ItemService.gI().createNewItem((short) 1465,200);
                itemqua4 = ItemService.gI().createNewItem((short) 1997,5);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
         
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                InventoryServiceNew.gI().addItemBag(player, itemqua);
   
                InventoryServiceNew.gI().addItemBag(player, itemqua2);
                InventoryServiceNew.gI().addItemBag(player, itemqua3);
                InventoryServiceNew.gI().addItemBag(player, itemqua4);
                InventoryServiceNew.gI().sendItemBags(player);
            } else if (player.getSession().tongnap >= 300000 && player.mocnap == 200000) {
                Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 300K\nSau " + time + " Giây!");
                while (time > 0) {
                time--;
                Thread.sleep(1000);
                Service.gI().sendThongBao(player, "|7|" + time);
                }
                player.mocnap = 300000;
                itemqua = ItemService.gI().createNewItem((short) 457,400);
                itemqua.itemOptions.add(new Item.ItemOption(30, 1));

                itemqua2 = ItemService.gI().createNewItem((short) 1296,10);
                 itemqua3 = ItemService.gI().createNewItem((short) 1465,300);
                itemqua4 = ItemService.gI().createNewItem((short) 1997,5);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
       
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                InventoryServiceNew.gI().addItemBag(player, itemqua);
        
                InventoryServiceNew.gI().addItemBag(player, itemqua2);
                InventoryServiceNew.gI().addItemBag(player, itemqua3);
                InventoryServiceNew.gI().addItemBag(player, itemqua4);
                InventoryServiceNew.gI().sendItemBags(player);
            } else if (player.getSession().tongnap >= 500000 && player.mocnap == 300000) {
                Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 500K\nSau " + time + " Giây!");
                while (time > 0) {
                time--;
                Thread.sleep(1000);
                Service.gI().sendThongBao(player, "|7|" + time);
                }
                player.mocnap = 500000;
                itemqua = ItemService.gI().createNewItem((short) 457,500);
                itemqua.itemOptions.add(new Item.ItemOption(30, 1));
            
                itemqua2 = ItemService.gI().createNewItem((short) 1296,20);
                itemqua3 = ItemService.gI().createNewItem((short) 1465,500);
                itemqua4 = ItemService.gI().createNewItem((short) 1997,10);
                itemqua5 = ItemService.gI().createNewItem((short) 1411,20);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
           
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua5.template.name);
                InventoryServiceNew.gI().addItemBag(player, itemqua);
        
                InventoryServiceNew.gI().addItemBag(player, itemqua2);
                InventoryServiceNew.gI().addItemBag(player, itemqua3);
                InventoryServiceNew.gI().addItemBag(player, itemqua4);
                InventoryServiceNew.gI().addItemBag(player, itemqua5);
                InventoryServiceNew.gI().sendItemBags(player);
            } else if (player.getSession().tongnap >= 800000 && player.mocnap == 500000) {
                Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 800K\nSau " + time + " Giây!");
                while (time > 0) {
                time--;
                Thread.sleep(1000);
                Service.gI().sendThongBao(player, "|7|" + time);
                }
                player.mocnap = 800000;
                itemqua = ItemService.gI().createNewItem((short) 457,700);
                itemqua.itemOptions.add(new Item.ItemOption(30, 1));
           
                itemqua2 = ItemService.gI().createNewItem((short) 1296,20);
                itemqua3 = ItemService.gI().createNewItem((short) 1465,800);
                itemqua4 = ItemService.gI().createNewItem((short) 1997,15);
                itemqua5 = ItemService.gI().createNewItem((short) 1411,30);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
             
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua5.template.name);
                InventoryServiceNew.gI().addItemBag(player, itemqua);
             
                InventoryServiceNew.gI().addItemBag(player, itemqua2);
                InventoryServiceNew.gI().addItemBag(player, itemqua3);
                InventoryServiceNew.gI().addItemBag(player, itemqua4);
                InventoryServiceNew.gI().addItemBag(player, itemqua5);
                InventoryServiceNew.gI().sendItemBags(player);
            } else if (player.getSession().tongnap >= 1000000 && player.mocnap == 800000) {
                Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 1 Triệu\nSau " + time + " Giây!");
                while (time > 0) {
                time--;
                Thread.sleep(1000);
                Service.gI().sendThongBao(player, "|7|" + time);
                }
                player.mocnap = 1000000;
                itemqua = ItemService.gI().createNewItem((short) 457,1000);
                itemqua.itemOptions.add(new Item.ItemOption(30, 1));
                itemqua1 = ItemService.gI().createNewItem((short) 1464,1);
                itemqua1.itemOptions.add(new Item.ItemOption(50, 10));
                itemqua1.itemOptions.add(new Item.ItemOption(77, 10));
                itemqua1.itemOptions.add(new Item.ItemOption(103, 10));
   
                itemqua6 = ItemService.gI().createNewItem((short) 1504,50);
                itemqua2 = ItemService.gI().createNewItem((short) 1296,50);
                itemqua3 = ItemService.gI().createNewItem((short) 1502,1);
                itemqua4 = ItemService.gI().createNewItem((short) 1997,30);
                itemqua5 = ItemService.gI().createNewItem((short) 1411,50);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                   Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                      Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua6.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua5.template.name);
                InventoryServiceNew.gI().addItemBag(player, itemqua);
                InventoryServiceNew.gI().addItemBag(player, itemqua1);
                InventoryServiceNew.gI().addItemBag(player, itemqua2);
                InventoryServiceNew.gI().addItemBag(player, itemqua3);
                 InventoryServiceNew.gI().addItemBag(player, itemqua6);
                InventoryServiceNew.gI().addItemBag(player, itemqua4);
                InventoryServiceNew.gI().addItemBag(player, itemqua5);
                InventoryServiceNew.gI().sendItemBags(player);
            } else if (player.getSession().tongnap >= 1500000 && player.mocnap == 1000000) {
                Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 1,5 Triệu\nSau " + time + " Giây!");
                while (time > 0) {
                time--;
                Thread.sleep(1000);
                Service.gI().sendThongBao(player, "|7|" + time);
                }
                player.mocnap = 1500000;
                itemqua = ItemService.gI().createNewItem((short) 457,1500);
                itemqua.itemOptions.add(new Item.ItemOption(30, 1));
                 itemqua1 = ItemService.gI().createNewItem((short) 1504,150);
                itemqua2 = ItemService.gI().createNewItem((short) 1296,70);
                itemqua3 = ItemService.gI().createNewItem((short) 1466,300);
                itemqua4 = ItemService.gI().createNewItem((short) 1997,50);
                itemqua5 = ItemService.gI().createNewItem((short) 1411,80);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
       Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua5.template.name);
                InventoryServiceNew.gI().addItemBag(player, itemqua);
              InventoryServiceNew.gI().addItemBag(player, itemqua1);
                InventoryServiceNew.gI().addItemBag(player, itemqua2);
                InventoryServiceNew.gI().addItemBag(player, itemqua3);
                InventoryServiceNew.gI().addItemBag(player, itemqua4);
                InventoryServiceNew.gI().addItemBag(player, itemqua5);
                InventoryServiceNew.gI().sendItemBags(player);
            } else if (player.getSession().tongnap >= 2000000 && player.mocnap == 1500000) {
                Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 2 Triệu\nSau " + time + " Giây!");
                while (time > 0) {
                time--;
                Thread.sleep(1000);
                Service.gI().sendThongBao(player, "|7|" + time);
                }
                player.mocnap = 2000000;
                itemqua = ItemService.gI().createNewItem((short) 457,2000);
                itemqua.itemOptions.add(new Item.ItemOption(30, 1));
             itemqua1 = ItemService.gI().createNewItem((short) 1504,200);
                itemqua2 = ItemService.gI().createNewItem((short) 1296,100);
                itemqua3 = ItemService.gI().createNewItem((short) 1466,500);
                itemqua4 = ItemService.gI().createNewItem((short) 1997,70);
                itemqua5 = ItemService.gI().createNewItem((short) 1411,100);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                 Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua5.template.name);
                InventoryServiceNew.gI().addItemBag(player, itemqua);
              InventoryServiceNew.gI().addItemBag(player, itemqua1);
                InventoryServiceNew.gI().addItemBag(player, itemqua2);
                InventoryServiceNew.gI().addItemBag(player, itemqua3);
                InventoryServiceNew.gI().addItemBag(player, itemqua4);
                InventoryServiceNew.gI().addItemBag(player, itemqua5);
                InventoryServiceNew.gI().sendItemBags(player);
            }else {
                Service.gI().sendThongBao(player, "Bạn Chưa Đủ Điều Kiện Nhận Mốc Nạp Này!");
            }
        } catch (Exception e) {}
    } else {
        Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 6 ô trống hành trang");
    }
}
    
    public void ComfirmMocSb(Player player) {
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
        Item itemqua;
        Item itemqua1;
        Item itemqua2;
        Item itemqua3;
        try {
            int time = 5;
                if (player.pointSb >= 100 && player.mocsanboss == 0) {
                Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc 100 Điểm Boss\nSau " + time + " Giây!");
                while (time > 0) {
                time--;
                Thread.sleep(1000);
                Service.gI().sendThongBao(player, "|7|" + time);
                }
                player.mocsanboss = 100;
                itemqua = ItemService.gI().createNewItem((short) 457,20);
                itemqua.itemOptions.add(new Item.ItemOption(30, 1));
                itemqua1 = ItemService.gI().createNewItem((short) 1318,55);
                itemqua2 = ItemService.gI().createNewItem((short) 1262,111);
                itemqua3 = ItemService.gI().createNewItem((short) 1296,5);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                InventoryServiceNew.gI().addItemBag(player, itemqua);
                InventoryServiceNew.gI().addItemBag(player, itemqua1);
                InventoryServiceNew.gI().addItemBag(player, itemqua2);
                InventoryServiceNew.gI().addItemBag(player, itemqua3);
                InventoryServiceNew.gI().sendItemBags(player);
            } else if (player.pointSb >= 200 && player.mocsanboss == 100) {
                Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc 200 Điểm Boss\nSau " + time + " Giây!");
                while (time > 0) {
                time--;
                Thread.sleep(1000);
                Service.gI().sendThongBao(player, "|7|" + time);
                }
                player.mocsanboss = 200;
                itemqua = ItemService.gI().createNewItem((short) 457,30);
                itemqua.itemOptions.add(new Item.ItemOption(30, 1));
                itemqua1 = ItemService.gI().createNewItem((short) 1318,111);
                itemqua2 = ItemService.gI().createNewItem((short) 1262,222);
                itemqua3 = ItemService.gI().createNewItem((short) 1296,5);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                InventoryServiceNew.gI().addItemBag(player, itemqua);
                InventoryServiceNew.gI().addItemBag(player, itemqua1);
                InventoryServiceNew.gI().addItemBag(player, itemqua2);
                InventoryServiceNew.gI().addItemBag(player, itemqua3);
                InventoryServiceNew.gI().sendItemBags(player);
            } else if (player.pointSb >= 300 && player.mocsanboss == 200) {
                Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc 300 Điểm Boss\nSau " + time + " Giây!");
                while (time > 0) {
                time--;
                Thread.sleep(1000);
                Service.gI().sendThongBao(player, "|7|" + time);
                }
                player.mocsanboss = 300;
                itemqua = ItemService.gI().createNewItem((short) 457,50);
                itemqua.itemOptions.add(new Item.ItemOption(30, 1));
                itemqua1 = ItemService.gI().createNewItem((short) 1318,151);
                itemqua2 = ItemService.gI().createNewItem((short) 1262,333);
                itemqua3 = ItemService.gI().createNewItem((short) 1296,5);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                InventoryServiceNew.gI().addItemBag(player, itemqua);
                InventoryServiceNew.gI().addItemBag(player, itemqua1);
                InventoryServiceNew.gI().addItemBag(player, itemqua2);
                InventoryServiceNew.gI().addItemBag(player, itemqua3);
                InventoryServiceNew.gI().sendItemBags(player);
            } else if (player.pointSb >= 500 && player.mocsanboss == 300) {
                Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc 500 Điểm Boss\nSau " + time + " Giây!");
                while (time > 0) {
                time--;
                Thread.sleep(1000);
                Service.gI().sendThongBao(player, "|7|" + time);
                }
                player.mocsanboss = 500;
                itemqua = ItemService.gI().createNewItem((short) 457,150);
                itemqua.itemOptions.add(new Item.ItemOption(30, 1));
                itemqua1 = ItemService.gI().createNewItem((short) 1318,252);
                itemqua2 = ItemService.gI().createNewItem((short) 1262,555);
                itemqua3 = ItemService.gI().createNewItem((short) 1296,15);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                InventoryServiceNew.gI().addItemBag(player, itemqua);
                InventoryServiceNew.gI().addItemBag(player, itemqua1);
                InventoryServiceNew.gI().addItemBag(player, itemqua2);
                InventoryServiceNew.gI().addItemBag(player, itemqua3);
                InventoryServiceNew.gI().sendItemBags(player);
            } else if (player.pointSb >= 800 && player.mocsanboss == 500) {
                Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc 800 Điểm Boss\nSau " + time + " Giây!");
                while (time > 0) {
                time--;
                Thread.sleep(1000);
                Service.gI().sendThongBao(player, "|7|" + time);
                }
                player.mocsanboss = 800;
                itemqua = ItemService.gI().createNewItem((short) 457,300);
                itemqua.itemOptions.add(new Item.ItemOption(30, 1));
                itemqua1 = ItemService.gI().createNewItem((short) 1318,444);
                itemqua2 = ItemService.gI().createNewItem((short) 1262,888);
                itemqua3 = ItemService.gI().createNewItem((short) 1296,20);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                InventoryServiceNew.gI().addItemBag(player, itemqua);
                InventoryServiceNew.gI().addItemBag(player, itemqua1);
                InventoryServiceNew.gI().addItemBag(player, itemqua2);
                InventoryServiceNew.gI().addItemBag(player, itemqua3);
                InventoryServiceNew.gI().sendItemBags(player);
            } else if (player.pointSb >= 1000 && player.mocsanboss == 800) {
                Service.gI().sendThongBao(player, "Tiến Hành Nhận\nMốc 1.000 Điểm Boss\nSau " + time + " Giây!");
                while (time > 0) {
                time--;
                Thread.sleep(1000);
                Service.gI().sendThongBao(player, "|7|" + time);
                }
                player.mocsanboss = 1000;
                itemqua = ItemService.gI().createNewItem((short) 457,500);
                itemqua.itemOptions.add(new Item.ItemOption(30, 1));
                itemqua1 = ItemService.gI().createNewItem((short) 1318,499);
                itemqua2 = ItemService.gI().createNewItem((short) 1262,999);
                itemqua3 = ItemService.gI().createNewItem((short) 1296,30);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                InventoryServiceNew.gI().addItemBag(player, itemqua);
                InventoryServiceNew.gI().addItemBag(player, itemqua1);
                InventoryServiceNew.gI().addItemBag(player, itemqua2);
                InventoryServiceNew.gI().addItemBag(player, itemqua3);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn Chưa Đủ Điều Kiện Nhận Mốc Điểm Săn Boss Này!");
            }
        } catch (Exception e) {}
    } else {
        Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
    }
}
    
    public void hopthanlinh(Player player, Item item) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length);
        Item thanlinh = Util.randomthanlinh(Manager.itemIds_TL[randomDo]);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
            InventoryServiceNew.gI().addItemBag(player, thanlinh);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + thanlinh.template.name);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            icon[1] = thanlinh.template.iconID;
            InventoryServiceNew.gI().sendItemBags(player);
            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }
    private void useItemQuaDua(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {2069, 2070, 2071, 2072, 2073};
            int[][] gold = {{10000000, 20000000}};
            int[][] ruby = {{10, 20}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useItemTime(Player pl, Item item) {
        switch (item.template.id) {
            case 1445:
                pl.itemTime.lastTimeUseBanhChung = System.currentTimeMillis();
                pl.itemTime.isUseBanhChung = true;
                break;
            case 1446:
                pl.itemTime.lastTimeUseBanhTet = System.currentTimeMillis();
                pl.itemTime.isUseBanhTet = true;
                break;
            case 382: //bổ huyết
                if (pl.itemTime.isUseBoHuyetSC == true) {
                    pl.itemTime.isUseBoHuyetSC = false;
                    pl.itemTime.lastTimeBoHuyetSC = 0;
                    ItemTimeService.gI().removeItemTime(pl, 10714);
                }
                pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet = true;
                break;
            case 383: //bổ khí
                if (pl.itemTime.isUseBoKhiSC == true) {
                    pl.itemTime.isUseBoKhiSC = false;
                    pl.itemTime.lastTimeBoKhiSC = 0;
                    ItemTimeService.gI().removeItemTime(pl, 10715);
                }
                pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi = true;
                break;
            case 384: //giáp xên
                if (pl.itemTime.isUseGiapXenSC == true) {
                    pl.itemTime.isUseGiapXenSC = false;
                    pl.itemTime.lastTimeGiapXenSC = 0;
                    ItemTimeService.gI().removeItemTime(pl, 10712);
                }
                pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen = true;
                break;
            case 381: //cuồng nộ
                if (pl.itemTime.isUseCuongNoSC == true) {
                    pl.itemTime.isUseCuongNoSC = false;
                    pl.itemTime.lastTimeCuongNoSC = 0;
                    ItemTimeService.gI().removeItemTime(pl, 10716);
                }
                pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo = true;
                Service.getInstance().point(pl);
                break;
            case 385: //ẩn danh
                if (pl.itemTime.isUseAnDanhSC == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng ẩn danh đặc biệt");
                    return;
                }
                pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh = true;
                break;
            case 379: //máy dò capsule
                pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
                pl.itemTime.isUseMayDo = true;
                break;
            case 1100: //bổ huyết
                if (pl.itemTime.isUseBoHuyet == true) {
                    pl.itemTime.isUseBoHuyet = false;
                    pl.itemTime.lastTimeBoHuyet = 0;
                    ItemTimeService.gI().removeItemTime(pl, 2755);
                }
                pl.itemTime.lastTimeBoHuyetSC = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyetSC = true;
                break;
            case 1101: //giáp xên
                if (pl.itemTime.isUseGiapXen == true) {
                    pl.itemTime.isUseGiapXen = false;
                    pl.itemTime.lastTimeGiapXen = 0;
                    ItemTimeService.gI().removeItemTime(pl, 2757);
                }
                pl.itemTime.lastTimeGiapXenSC = System.currentTimeMillis();
                pl.itemTime.isUseGiapXenSC = true;
                break;
            case 1102: //bổ khí
                if (pl.itemTime.isUseBoKhi == true) {
                    pl.itemTime.isUseBoKhi = false;
                    pl.itemTime.lastTimeBoKhi = 0;
                    ItemTimeService.gI().removeItemTime(pl, 2756);
                }
                pl.itemTime.lastTimeBoKhiSC = System.currentTimeMillis();
                pl.itemTime.isUseBoKhiSC = true;
                break;
            case 1099: //cuồng nộ
                if (pl.itemTime.isUseCuongNo == true) {
                    pl.itemTime.isUseCuongNo = false;
                    pl.itemTime.lastTimeCuongNo = 0;
                    ItemTimeService.gI().removeItemTime(pl, 2754);
                }
                pl.itemTime.lastTimeCuongNoSC = System.currentTimeMillis();
                pl.itemTime.isUseCuongNoSC = true;
                Service.getInstance().point(pl);
                break;
            case 1103: //ẩn danh
                if (pl.itemTime.isUseAnDanh == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng ẩn danh");
                    return;
                }
                pl.itemTime.lastTimeAnDanhSC = System.currentTimeMillis();
                pl.itemTime.isUseAnDanhSC = true;
                break;
            case 663: //bánh pudding
            case 664: //xúc xíc
            case 665: //kem dâu
            case 666: //mì ly
            case 667: //sushi
                pl.itemTime.lastTimeEatMeal = System.currentTimeMillis();
                pl.itemTime.isEatMeal = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal);
                pl.itemTime.iconMeal = item.template.iconID;
                break;
            case 472: //banhtrungthu
                pl.itemTime.LTBANH = System.currentTimeMillis();
                pl.itemTime.ISBANH = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.ICONBANH);
                pl.itemTime.ICONBANH = item.template.iconID;
                break;
            case 1997: //
//                if (pl.getSession() != null && pl.isJail()) {
//                    Service.gI().sendThongBaoFromAdmin(pl, "|7|BẠN ĐANG BỊ TẠM GIAM");
//                    return;
//                }
                pl.itemTime.LTNGOC = System.currentTimeMillis();
                pl.itemTime.VENGOC = true;
                ChangeMapService.gI().changeMapBySpaceShip(pl, 205, Util.nextInt(3), 115);
                Service.getInstance().sendThongBao(pl, "Bạn đã được tàu vận chuyển đưa đến map up");
                break;
            case 2105: //máy dò đồ
                pl.itemTime.lastTimeUseMayDo3 = System.currentTimeMillis();
                pl.itemTime.isUseMayDo3 = true;
                break;
        }
        Service.gI().point(pl);
        ItemTimeService.gI().sendAllItemTime(pl);
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
    }

    private void controllerCallRongThan(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
            switch (tempId) {
                case SummonDragon.NGOC_RONG_1_SAO:
                case SummonDragon.NGOC_RONG_2_SAO:
                case SummonDragon.NGOC_RONG_3_SAO:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13));
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGON,
                            -1, "Bạn chỉ có thể gọi rồng từ ngọc 3 sao, 2 sao, 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        }
    }

    private void controllerCalltrb(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONGTRB1 && tempId <= SummonDragon.NGOC_RONGTRB3) {
            switch (tempId) {
                case SummonDragon.NGOC_RONGTRB1:
                    SummonDragon.gI().openMenuSummonShenronTRB(pl, (byte) (tempId - 2090));
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGONTRB,
                            -1, "Bạn chỉ có thể gọi rồng từ ngọc 1 sao TRB ", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        }
    }

    private void learnSkill(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.gender == pl.gender || item.template.gender == 3) {
                String[] subName = item.template.name.split("");
                byte level = Byte.parseByte(subName[subName.length - 1]);
                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                if (curSkill.point >= 7) {
                    Service.getInstance().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                } else {
                    if (curSkill.point == 0) {
                        if (level == 1) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.getInstance().messageSubCommand((byte) 23);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Skill skillNeed = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            Service.getInstance().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " cấp " + skillNeed.point + " trước!");
                        }
                    } else {
                        if (curSkill.point + 1 == level) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.getInstance().messageSubCommand((byte) 62);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Service.getInstance().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp " + (curSkill.point + 1) + " trước!");
                        }
                    }
                    InventoryServiceNew.gI().sendItemBags(pl);
                }
            } else {
                Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
            }
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);
        }
    }

    private void useTDLT(Player pl, Item item) {
        if (pl.itemTime.isUseTDLT) {
            ItemTimeService.gI().turnOffTDLT(pl, item);
        } else {
            ItemTimeService.gI().turnOnTDLT(pl, item);
        }
    }

    public void usePorata(Player pl) {
    if (pl.pet == null || pl.fusion.typeFusion==ConstPlayer.LUONG_LONG_NHAT_THE)return;
    Item get5 = InventoryServiceNew.gI().findItem(pl.inventory.itemsBody, 1378);
    Item get6 = InventoryServiceNew.gI().findItem(pl.inventory.itemsBody, 1379);
      Item ct = pl.inventory.itemsBody.get(5);
          Item.ItemOption option = null;
      if (ct.isNotNullItem()) {
                    for (Item.ItemOption io : ct.itemOptions) {
                        if (io.optionTemplate.id == 206) {
                             option = io;
                        }
                    }
                }
    if (pl.pet.typePet >4&&pl.fusion.typeFusion == ConstPlayer.NON_FUSION&&get5!=null&&Util.canDoWithTime(pl.pet.lastTimeUnfusion2, pl.ltggtv4)||get6!=null&&pl.pet.typePet >4&&pl.fusion.typeFusion == ConstPlayer.NON_FUSION&&Util.canDoWithTime(pl.pet.lastTimeUnfusion2, pl.ltggtv4)||pl.pet.typePet >4&&pl.fusion.typeFusion == ConstPlayer.NON_FUSION&&option!=null&&Util.canDoWithTime(pl.pet.lastTimeUnfusion2, pl.ltggtv4)) {
        pl.pet.ggtv4(true);
    } else {
    if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
         pl.pet.fusion(true);   
    } else {
    if (pl.fusion.typeFusion != ConstPlayer.HOP_THE_GOGETA) {
        pl.pet.unFusion();
    } else if (pl.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
        pl.pet.unFusion2();
    }}}
}

    public void usePorata2(Player pl) {
     if (pl.pet == null || pl.fusion.typeFusion==ConstPlayer.LUONG_LONG_NHAT_THE)return;
    Item get5 = InventoryServiceNew.gI().findItem(pl.inventory.itemsBody, 1378);
    Item get6 = InventoryServiceNew.gI().findItem(pl.inventory.itemsBody, 1379);
      Item ct = pl.inventory.itemsBody.get(5);
          Item.ItemOption option = null;
      if (ct.isNotNullItem()) {
                    for (Item.ItemOption io : ct.itemOptions) {
                        if (io.optionTemplate.id == 206) {
                             option = io;
                        }
                    }
                }
    if (pl.pet.typePet >4&&pl.fusion.typeFusion == ConstPlayer.NON_FUSION&&get5!=null&&Util.canDoWithTime(pl.pet.lastTimeUnfusion2, pl.ltggtv4)||get6!=null&&pl.pet.typePet >4&&pl.fusion.typeFusion == ConstPlayer.NON_FUSION&&Util.canDoWithTime(pl.pet.lastTimeUnfusion2, pl.ltggtv4)||pl.pet.typePet >4&&pl.fusion.typeFusion == ConstPlayer.NON_FUSION&&option!=null&&Util.canDoWithTime(pl.pet.lastTimeUnfusion2, pl.ltggtv4)) {
        pl.pet.ggtv4(true);
    } else {
    if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
         pl.pet.fusion2(true);   
    } else {
    if (pl.fusion.typeFusion != ConstPlayer.HOP_THE_GOGETA) {
        pl.pet.unFusion();
    } else if (pl.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
        pl.pet.unFusion2();
    }}}
}

    public void usePorata3(Player pl) {
     if (pl.pet == null || pl.fusion.typeFusion==ConstPlayer.LUONG_LONG_NHAT_THE)return;
    Item get5 = InventoryServiceNew.gI().findItem(pl.inventory.itemsBody, 1378);
    Item get6 = InventoryServiceNew.gI().findItem(pl.inventory.itemsBody, 1379);
      Item ct = pl.inventory.itemsBody.get(5);
          Item.ItemOption option = null;
      if (ct.isNotNullItem()) {
                    for (Item.ItemOption io : ct.itemOptions) {
                        if (io.optionTemplate.id == 206) {
                             option = io;
                        }
                    }
                }
    if (pl.pet.typePet >4&&pl.fusion.typeFusion == ConstPlayer.NON_FUSION&&get5!=null&&Util.canDoWithTime(pl.pet.lastTimeUnfusion2, pl.ltggtv4)||get6!=null&&pl.pet.typePet >4&&pl.fusion.typeFusion == ConstPlayer.NON_FUSION&&Util.canDoWithTime(pl.pet.lastTimeUnfusion2, pl.ltggtv4)||pl.pet.typePet >4&&pl.fusion.typeFusion == ConstPlayer.NON_FUSION&&option!=null&&Util.canDoWithTime(pl.pet.lastTimeUnfusion2, pl.ltggtv4)) {
        pl.pet.ggtv4(true);
    }else {
    if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
         pl.pet.fusion3(true);   
    } else {
    if (pl.fusion.typeFusion != ConstPlayer.HOP_THE_GOGETA) {
        pl.pet.unFusion();
    } else if (pl.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
        pl.pet.unFusion2();
    }}}
}

    private void usePorata4(Player pl) {
     if (pl.pet == null || pl.fusion.typeFusion==ConstPlayer.LUONG_LONG_NHAT_THE)return;
    Item get5 = InventoryServiceNew.gI().findItem(pl.inventory.itemsBody, 1378);
    Item get6 = InventoryServiceNew.gI().findItem(pl.inventory.itemsBody, 1379);
      Item ct = pl.inventory.itemsBody.get(5);
          Item.ItemOption option = null;
      if (ct.isNotNullItem()) {
                    for (Item.ItemOption io : ct.itemOptions) {
                        if (io.optionTemplate.id == 206) {
                             option = io;
                        }
                    }
                }
    if (pl.pet.typePet >4&&pl.fusion.typeFusion == ConstPlayer.NON_FUSION&&get5!=null&&Util.canDoWithTime(pl.pet.lastTimeUnfusion2, pl.ltggtv4)||get6!=null&&pl.pet.typePet >4&&pl.fusion.typeFusion == ConstPlayer.NON_FUSION&&Util.canDoWithTime(pl.pet.lastTimeUnfusion2, pl.ltggtv4)||pl.pet.typePet >4&&pl.fusion.typeFusion == ConstPlayer.NON_FUSION&&option!=null&&Util.canDoWithTime(pl.pet.lastTimeUnfusion2, pl.ltggtv4)) {
        pl.pet.ggtv4(true);
    }else {
    if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
         pl.pet.fusion4(true);   
    } else {
    if (pl.fusion.typeFusion != ConstPlayer.HOP_THE_GOGETA) {
        pl.pet.unFusion();
    } else if (pl.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
        pl.pet.unFusion2();
    }}}
}

    private void usePorata5(Player pl) {
     if (pl.pet == null || pl.fusion.typeFusion==ConstPlayer.LUONG_LONG_NHAT_THE)return;
    Item get5 = InventoryServiceNew.gI().findItem(pl.inventory.itemsBody, 1378);
    Item get6 = InventoryServiceNew.gI().findItem(pl.inventory.itemsBody, 1379);
      Item ct = pl.inventory.itemsBody.get(5);
          Item.ItemOption option = null;
      if (ct.isNotNullItem()) {
                    for (Item.ItemOption io : ct.itemOptions) {
                        if (io.optionTemplate.id == 206) {
                             option = io;
                        }
                    }
                }
    if (pl.pet.typePet >4&&pl.fusion.typeFusion == ConstPlayer.NON_FUSION&&get5!=null&&Util.canDoWithTime(pl.pet.lastTimeUnfusion2, pl.ltggtv4)||get6!=null&&pl.pet.typePet >4&&pl.fusion.typeFusion == ConstPlayer.NON_FUSION&&Util.canDoWithTime(pl.pet.lastTimeUnfusion2, pl.ltggtv4)||pl.pet.typePet >4&&pl.fusion.typeFusion == ConstPlayer.NON_FUSION&&option!=null&&Util.canDoWithTime(pl.pet.lastTimeUnfusion2, pl.ltggtv4)) {
        pl.pet.ggtv4(true);
    } else {
    if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
         pl.pet.fusion5(true);   
    } else {
    if (pl.fusion.typeFusion != ConstPlayer.HOP_THE_GOGETA) {
        pl.pet.unFusion();
    } else if (pl.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
        pl.pet.unFusion2();
    }}}
}

    private void openCapsuleUI(Player pl) {
        pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
        ChangeMapService.gI().openChangeMapTab(pl);
    }
public boolean maydoboss1(Player pl) {
        try {
            BossManager.gI().showListBoss(pl);
            return true;
        } catch (Exception e) {
        }
        return false;
    }
     public boolean maydoboss2(Player pl) {
        try {
            BossManager.gI().showListBoss2(pl);
            return true;
        } catch (Exception e) {
        }
        return false;
    }
     public void choseMapCapsule(Player pl, int index) {
            if (index < 0 || index >= pl.mapCapsule.size()) {
        System.out.println("Invalid index: " + index); // Or handle this case as needed
        return;
    }
        int zoneId = -1;
        Zone zoneChose = pl.mapCapsule.get(index);
        // Kiểm tra số lượng người trong khu

        if (zoneChose.getNumOfPlayers() > 9
                || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
                || MapService.gI().isMapConDuongRanDoc(zoneChose.map.mapId)
            
                || MapService.gI().isMapMaBu(zoneChose.map.mapId)
              
                || MapService.gI().isMapBanDoKhoBau(zoneChose.map.mapId)
                || MapService.gI().isMapHuyDiet(zoneChose.map.mapId)) {
            Service.gI().sendThongBao(pl, "Hiện tại không thể vào được khu!");
            return;
        }
        if (index != 0 || zoneChose.map.mapId == 21
                || zoneChose.map.mapId == 22
                || zoneChose.map.mapId == 23) {
            pl.mapBeforeCapsule = pl.zone;
        } else {
            zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
            pl.mapBeforeCapsule = null;
        }
        ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);
    }
    public void eatPea(Player player) {
        Item pea = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == 6) {
                pea = item;
                break;
            }
        }
        if (pea != null) {
            int hpKiHoiPhuc = 0;
            int lvPea = Integer.parseInt(pea.template.name.substring(13));
            for (Item.ItemOption io : pea.itemOptions) {
                if (io.optionTemplate.id == 2) {
                    hpKiHoiPhuc = io.param * 1000;
                    break;
                }
                if (io.optionTemplate.id == 48) {
                    hpKiHoiPhuc = io.param;
                    break;
                }
            }
            player.nPoint.setHp(player.nPoint.hp + hpKiHoiPhuc);
            player.nPoint.setMp(player.nPoint.mp + hpKiHoiPhuc);
            PlayerService.gI().sendInfoHpMp(player);
            Service.gI().sendInfoPlayerEatPea(player);
            if (player.pet != null && player.zone.equals(player.pet.zone) && !player.pet.isDie()) {
                int statima = 100 * lvPea;
                player.pet.nPoint.stamina += statima;
                if (player.pet.nPoint.stamina > player.pet.nPoint.maxStamina) {
                    player.pet.nPoint.stamina = player.pet.nPoint.maxStamina;
                }
                player.pet.nPoint.setHp(player.pet.nPoint.hp + hpKiHoiPhuc);
                player.pet.nPoint.setMp(player.pet.nPoint.mp + hpKiHoiPhuc);
                Service.gI().sendInfoPlayerEatPea(player.pet);
                Service.gI().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã cho con đậu thần");
            }

            InventoryServiceNew.gI().subQuantityItemsBag(player, pea, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        }
    }

    private void upSkillPet(Player pl, Item item) {
        if (pl.pet == null) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        try {
            switch (item.template.id) {
                case 402: //skill 1
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 403: //skill 2
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 404: //skill 3
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 759: //skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;

            }

        } catch (Exception e) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
        }
    }

    private void ItemSKH(Player pl, Item item) {//hop qua skh
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }
    private void HopSkhTuChon(Player pl, Item item) {//hop qua do ts
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn set của bạn đi"
                , (pl.gender == 0 ? "Set\nTaiyoken" : pl.gender == 1 ? "Set\nKi" : "Set\nKakarot")
                , (pl.gender == 0 ? "Set\nGenki" : pl.gender == 1 ? "Set\nDame" : "Set\nVegeta")
                , (pl.gender == 0 ? "Set\nKamejoko" : pl.gender == 1 ? "Set\nPikkoro" : "Set\nNappa"), "Từ chổi");
    }
    private void Hopts(Player pl, Item item) {//hop qua do ts
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }
  private void SkhTL(Player pl, Item item) {// hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất",
                "Set namec", "Set xayda", "Từ chổi");
    }
}
