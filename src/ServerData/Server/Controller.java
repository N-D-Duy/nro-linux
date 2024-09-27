package ServerData.Server;

import Server.Connect.NhiemVuThanhTich.ThanhTichPlayer;
import ServerData.Services.SubMenuService;
import ServerData.Services.FlagBagService;
import ServerData.Services.NpcService;
import ServerData.Services.ChatGlobalService;
import ServerData.Models.Player.PlayerSkill.SkillService;
import ServerData.Services.InventoryServiceNew;
import ServerData.Services.TaskService;
import ServerData.Services.ClanService;
import ServerData.Services.Service;
import ServerData.Services.PlayerService;
import ServerData.Services.EffectSkillService;
import ServerData.Services.ItemMapService;
import ServerData.Services.ItemTimeService;
import ServerData.Services.IntrinsicService;
import ServerData.Services.MapService;
import ServerData.Services.FriendAndEnemyService;
import ServerData.Models.Player.Card;
import ServerData.Models.Player.RadarCard;
import ServerData.Models.Player.RadarService;
import com.girlkun.database.GirlkunDB;
import com.girlkun.result.GirlkunResultSet;
import Server.Data.Consts.ConstIgnoreName;
import Server.Data.Consts.ConstMap;
import ServerData.Utils.Util;
import Server.Data.DataGame;
import java.io.IOException;

import ServerData.Services.ChangeMapService;
import ServerData.Services.Func.UseItem;
import ServerData.Services.Func.Input;
import Server.Data.Consts.ConstNpc;
import Server.Data.Consts.ConstTask;
import Server.Data.ItemData;
import Server.Connect.PlayerDAO;
import Server.Data.Consts.ConstEvent;
import ServerData.Boss.Boss;
import ServerData.Boss.BossManager;
import ServerData.Models.Map.ListMap.NgocRongSaoDen;
import ServerData.Models.Map.ListMap.SieuHangService;
import ServerData.Models.Map.Zone;
import ServerData.Models.NPC.NpcManager;
import ServerData.Models.Player.Player;
import ServerData.Models.PVP.PVPService;
import ServerData.Models.Shop.ShopServiceNew;
import com.girlkun.network.handler.IMessageHandler;
import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;
import ServerData.Services.Func.CombineServiceNew;
import ServerData.Services.Func.LuckyRound;
import ServerData.Models.NPC.NauBanh;

import ServerData.Services.TransactionService;
import ServerData.Utils.Logger;
import ServerData.Models.Shop.ShopKyGuiService;
import static ServerData.Server.Manager.MAPS;
import java.util.List;

public class Controller implements IMessageHandler {

    private static Controller instance;

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    @Override
    public void onMessage(ISession s, Message _msg) {
        MySession _session = (MySession) s;
        //System.out.println(_session.ipAddress);
        long st = System.currentTimeMillis();
        Player player = null;
        try {
            player = _session.player;
            byte cmd = _msg.command;
//            if (cmd != -29 && cmd != -107 && cmd != 29 && cmd != -30 && cmd != 74 && cmd != -16 && cmd != -101 && cmd != 126 && cmd != -74 && cmd != 21 && cmd != -15 && cmd != -103) {
//                System.out.println(cmd);
//            }
           // System.out.println("***************************CMD receive: " + cmd);
            switch (cmd) {
                case 119:
                  /*  int idp = _msg.reader().readInt();
                    int tv =  _msg.reader().readInt();
                    int typez =  _msg.reader().readInt();
                    System.out.println(idp+" "+tv+" "+typez);
                    List<Player> pls = Client.gI().getPlayers();
                    for(Player pl : pls){
                        if(pl.id == idp){
                            
                            if(typez == 1){
                                pl.cuocT += tv;
                                if(pl.cuocT != 0) TaiXiuService.gI().addPlayerEven(pl,tv);
                            }else{
                                pl.cuocX += tv;
                                if(pl.cuocX != 0) TaiXiuService.gI().addPlayerOdd(pl,tv);
                            }
                            Service.gI().sendThongBao(pl, "Bạn đã đặt cược thành công "+tv+" thỏi vàng");
                            break;
                        }
                    } */
                    break;
                case -100:
                    byte action = _msg.reader().readByte();
                    switch (action) {
                        case 0:
                            // ký gửi
                            short idItem = _msg.reader().readShort();
                            byte moneyType = _msg.reader().readByte();
                            int money = _msg.reader().readInt();
                            int quantity;
                            if (player.getSession().version >= 214) {
                                quantity = _msg.reader().readInt();
                            } else {
                                quantity = _msg.reader().readByte();
                            }
                            if (quantity > 0) {
                                ShopKyGuiService.gI().KiGui(player, idItem, money, moneyType, quantity);
                            }
                            break;
                        case 1:
                        case 2: 
                            // nhận tiền
                            idItem = _msg.reader().readShort();
                            ShopKyGuiService.gI().claimOrDel(player, action, idItem);
                            break;
                        case 3:
                            // buy item
                            idItem = _msg.reader().readShort();
                            _msg.reader().readByte();
                            _msg.reader().readInt();
                            ShopKyGuiService.gI().buyItem(player, idItem);
                            break;
                        case 4:
                            // next page
                            moneyType = _msg.reader().readByte();
                            money = _msg.reader().readByte();
                            ShopKyGuiService.gI().openShopKyGui(player, moneyType, money);
                            break;
                        case 5:
                            // up top
                            idItem = _msg.reader().readShort();
                            ShopKyGuiService.gI().upItemToTop(player, idItem);
                            break;
//                        default:
//                            Service.gI().sendThongBao(player, "Không thể thực hiện");
//                         hủy ký gửi
                    }
                    break;
                     case -118: 
              
                  
                          BossManager.gI().teleBoss(player, _msg);
                    break;
                case 127:
                    if (player != null) {
                        byte actionRadar = _msg.reader().readByte();
                        switch (actionRadar) {
                            case 0:
                                RadarService.gI().sendRadar(player, player.Cards);
                                break;
                            case 1:
                                short idC = _msg.reader().readShort();
                                Card card = player.Cards.stream().filter(r -> r != null && r.Id == idC).findFirst().orElse(null);
                                if (card != null) {
                                    if (card.Level == 0) {
                                        return;
                                    }
                                    if (card.Used == 0) {
                                        if (player.Cards.stream().anyMatch(c -> c != null && c.Used == 1)) {
                                            Service.gI().sendThongBao(player, "Số thẻ sử dụng đã đạt tối đa");
                                            return;
                                        }
                                        card.Used = 1;
                                        RadarCard radarTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(r -> r.Id == idC).findFirst().orElse(null);
                                        if (radarTemplate != null && card.Level >= 2) {
                                            player.idAura = radarTemplate.AuraId;
                                        }
                                    } else {
                                        card.Used = 0;
                                        player.idAura = -1;
                                    }
                                    RadarService.gI().Radar1(player, idC, card.Used);
                                    Service.gI().point(player);
                                }
                                break;
                        }
                    }
                    break;
             
                case -105:
                    if (player.type == 0 && player.maxTime == 30) {
                        ChangeMapService.gI().changeMap(player, 102, 0, 100, 336);
                    } else if (player.type == 1 && player.maxTime == 5) {
                        ChangeMapService.gI().changeMap(player, 160, 0, -1, 5);
                    }
                    break;
                case 42:
                    Service.gI().regisAccount(_session, _msg);
                    break;
                case -127:
                    if (player != null) {
                        LuckyRound.gI().readOpenBall(player, _msg);
                    }
                    break;
                case -125:
                    if (player != null) {
                        Input.gI().doInput(player, _msg);
                    }
                    break;
                case 112:
                    if (player != null) {
                        IntrinsicService.gI().showMenu(player);
                    }
                    break;
                case -34:
                    if (player != null) {
                        switch (_msg.reader().readByte()) {
                            case 1:
                                player.magicTree.openMenuTree();
                                break;
                            case 2:
                                player.magicTree.loadMagicTree();
                                break;
                        }
                    }
                    break;
                case -99:
                    if (player != null) {
                        FriendAndEnemyService.gI().controllerEnemy(player, _msg);
                    }
                    break;
                case 18:
                    if (player != null) {
                        FriendAndEnemyService.gI().goToPlayerWithYardrat(player, _msg);
                    }
                    break;
                case -72:
                    if (player != null) {
                        FriendAndEnemyService.gI().chatPrivate(player, _msg);
                    }
                    break;
                case -80:
                    if (player != null) {
                        FriendAndEnemyService.gI().controllerFriend(player, _msg);
                    }
                    break;
                case -59:
                    if (player != null) {
                        PVPService.gI().controllerThachDau(player, _msg);
                    }
                    break;
                case -86:
                    if (player != null) {
                        TransactionService.gI().controller(player, _msg);
                    }
                    break;
                case -107:
                    if (player != null) {
                        Service.gI().showInfoPet(player);
                    }
                    break;
                case -108:
                    if (player != null && player.pet != null) {
                        player.pet.changeStatus(_msg.reader().readByte());
                    }
                    break;
                case 6: //buy item
                    if (player != null && !Maintenance.isRuning) {
                        byte typeBuy = _msg.reader().readByte();
                        int tempId = _msg.reader().readShort();
                        int quantity = 0;
                        try {
                            quantity = _msg.reader().readShort();
                        } catch (Exception e) {
                        }
                        ShopServiceNew.gI().takeItem(player, typeBuy, tempId);
                    }
                    break;
                case 7: //sell item
                    if (player != null && !Maintenance.isRuning) {
                        int action1 = _msg.reader().readByte();
                        if (action1 == 0) {
                            ShopServiceNew.gI().showConfirmSellItem(player, _msg.reader().readByte(),
                                    _msg.reader().readShort());
                        } else {
                            ShopServiceNew.gI().sellItem(player, _msg.reader().readByte(),
                                    _msg.reader().readShort());
                        }
                    }
                    break;
                case 29:
                    if (player != null) {
                        ChangeMapService.gI().openZoneUI(player);
                    }
                    break;
                case 21:
                    if (player != null) {
                        int zoneId = _msg.reader().readByte();
                        ChangeMapService.gI().changeZone(player, zoneId);
                    }
                    break;
                case -71:
                    if (player != null) {
                        ChatGlobalService.gI().chat(player, _msg.reader().readUTF());
                    }
                    break;
                case -79:
                    if (player != null) {
                        Service.gI().getPlayerMenu(player, _msg.reader().readInt());
                    }
                    break;
                case -113:
                    if (player != null) {
                        for (int i = 0; i < 10; i++) {
                            player.playerSkill.skillShortCut[i] = _msg.reader().readByte();
                        }
                        player.playerSkill.sendSkillShortCut();
                    }
                    break;
                case -101:
                    login2(_session, _msg);
                    break;
                case -103:
                    if (player != null) {
                        byte act = _msg.reader().readByte();
                        if (act == 0) {
                            Service.gI().openFlagUI(player);
                        } else if (act == 1) {
                            if(player.isAdmin2()) {
                                Service.gI().changeFlag(player, _msg.reader().readByte());
                            } else {
                                Service.gI().chooseFlag(player, _msg.reader().readByte());
                            }
                        } else {
//                        Util.log("id map" + player.map.id);
                        }
                    }
                    break;
                case -7:
                    if (player != null) {
                        int toX = 0;
                        int toY = 0;
                        if (player.location != null) {
                            toX = player.location.x;
                            toY = player.location.y;
                        }
                        try {
                            _msg.reader().readByte();
                            toX = _msg.reader().readShort();
                            toY = _msg.reader().readShort();
                        } catch (Exception e) {
                        }
                        if (!player.isDie() && player.zone != null) {
                            player.location.lastTimeplayerMove = System.currentTimeMillis();
                            if (player.effectSkill.isCharging) {
                                EffectSkillService.gI().stopCharge(player);
                            }
                            if (player.effectSkill.useTroi) {
                                EffectSkillService.gI().removeUseTroi(player);
                            }
                            if ((player.effectSkill.isStun
                                    || player.effectSkill.isThoiMien || player.effectSkill.isBlindDCTT
                                    || player.effectSkill.anTroi || player.effectSkill.useTroi) && player.isPl()) {
                                Service.gI().resetPoint(player, player.location.x, 250);
                                MapService.gI().sendPlayerMove(player);
                            } else {
                                PlayerService.gI().playerMove(player, toX, toY);
                            }
                        }
                    }
                    break;
                case -74:
                    byte type = _msg.reader().readByte();
//                    System.out.println("cmd -74 type: "+type);
                    if (type == 1) {
                        DataGame.sendSizeRes(_session);
                    } else if (type == 2) {
                        DataGame.sendRes(_session);
                    }
                    break;
                case -81:
                    if (player != null) {
                        _msg.reader().readByte();
                        int[] indexItem = new int[_msg.reader().readByte()];
                        for (int i = 0; i < indexItem.length; i++) {
                            indexItem[i] = _msg.reader().readByte();
                        }
                        CombineServiceNew.gI().showInfoCombine(player, indexItem);
                    }
                    break;
                case -87:
                    DataGame.updateData(_session);
                    break;
                case -67:
                    int id = _msg.reader().readInt();
                    DataGame.sendIcon(_session, id);
                    break;
                case 66:
                    DataGame.sendImageByName(_session, _msg.reader().readUTF());
                    break;
                case -66:
                    int effId = _msg.reader().readShort();
                    int idT = effId;
                    if (effId == 25) {
                        idT = 51; // id eff rong muon thay doi ( hien tai la rong xuong) 
                    }
                    DataGame.effData(_session, effId, idT);
                    break;
                case -62:
                    if (player != null) {
                        FlagBagService.gI().sendIconFlagChoose(player, _msg.reader().readByte());
                    }
                    break;
                case -63:
                    if (player != null) {
                        byte fbid = _msg.reader().readByte();
                        int fbidz = fbid & 0xFF;
                        FlagBagService.gI().sendIconEffectFlag(player, fbidz);
                    }
                    break;
                case -76:
                    if (player != null) {
                        int index = _msg.reader().readByte();
                        ThanhTichPlayer.GetArchivemnt(player,index);
                    }
                    break;
                case -32:
                    int bgId = _msg.reader().readShort();
                    DataGame.sendItemBGTemplate(_session, bgId);
                    break;
                case 22:
                    if (player != null) {
                        _msg.reader().readByte();
                        NpcManager.getNpc(ConstNpc.DAU_THAN).confirmMenu(player, _msg.reader().readByte());
                    }
                    break;
                case -33:
                case -23:
                    if (player != null) {
                        ChangeMapService.gI().changeMapWaypoint(player);
                        Service.gI().hideWaitDialog(player);
                    }
                    break;
                case -45:
                    if (player != null) {
                        if (player.skillSpecial != null && player.skillSpecial.isStartSkillSpecial) {                                                  
                            return;
                        }
                        SkillService.gI().useSkill(player, null, null, _msg);
                    }
                    break;
                case -46:
                    if (player != null) {
                        ClanService.gI().getClan(player, _msg);
                    }
                    break;
                case -51:
                    if (player != null) {
                        ClanService.gI().clanMessage(player, _msg);
                    }
                    break;
                case -54://cho đậu
                    if (player != null) {
                        ClanService.gI().clanDonate(player, _msg);
                        //Service.gI().sendThongBao(player, "Can not invoke clan donate");
                    }
                    break;
                case -49:
                    if (player != null) {
                        ClanService.gI().joinClan(player, _msg);
                    }
                    break;
                case -50:
                    if (player != null) {
                        ClanService.gI().sendListMemberClan(player, _msg.reader().readInt());
                    }
                    break;
                case -56:
                    if (player != null) {
                        ClanService.gI().clanRemote(player, _msg);
                    }
                    break;
                case -47:
                    if (player != null) {
                        ClanService.gI().sendListClan(player, _msg.reader().readUTF());
                    }
                    break;
                case -55:
                    if (player != null) {
                        ClanService.gI().showMenuLeaveClan(player);
                    }
                    break;
                case -57:
                    if (player != null) {
                        ClanService.gI().clanInvite(player, _msg);
                    }
                    break;
                case -40:
                    if (_session != null) {
                        UseItem.gI().getItem(_session, _msg);
                    }
                    break;
                case -41:
                    Service.gI().sendCaption(_session, _msg.reader().readByte());
                    break;
                case -43:
                    if (player != null) {
                        UseItem.gI().doItem(player, _msg);
                    }
                    break;
                case -91:
                    if (player != null) {
                        switch (player.iDMark.getTypeChangeMap()) {
                            case ConstMap.CHANGE_CAPSULE:
                                UseItem.gI().choseMapCapsule(player, _msg.reader().readByte());
                                break;
                            case ConstMap.CHANGE_BLACK_BALL:
//                                Service.gI().sendThongBao(player, "Đang bảo trì...");
                                NgocRongSaoDen.gI().changeMap(player, _msg.reader().readByte());
                                break;
                        }
                    }
                    break;
                case -39:
                    if (player != null) {
                        //finishLoadMap
                        ChangeMapService.gI().finishLoadMap(player);
                        if (player.zone.map.mapId == (21 + player.gender)) {
                            if (player.mabuEgg != null) {
                                player.mabuEgg.sendMabuEgg();
                            }
                        }
                        if (player.zone.map.mapId == (7 * player.gender)) {
                            if (player.billEgg != null) {
                                player.billEgg.sendBillEgg();
                            }
                        }
                    }
                    break;
                case -98:
                    //Message msg = new Message(-98);
                    //Client.gI().createBot(_session);
                    break;
                case -97:
                    Client.gI().clear();
                    break;
                case 11:
                    byte modId = _msg.reader().readByte();
                    DataGame.requestMobTemplate(_session, modId);
                    break;
                case 44:
                    if (player != null) {
                        Service.gI().chat(player, _msg.reader().readUTF());
                    }
                    break;
                case 32:
                    if (player != null) {
                        int npcId = _msg.reader().readShort();
                        int select = _msg.reader().readByte();
                        MenuController.getInstance().doSelectMenu(player, npcId, select);
                    }
                    break;
                case 33:
                    if (player != null) {
                        int npcId = _msg.reader().readShort();
                        MenuController.getInstance().openMenuNPC(_session, npcId, player);
                    }
                    break;
                case 34:
                    if (player != null) {
                        if (player.skillSpecial != null && player.skillSpecial.isStartSkillSpecial) {
                           break;
                        }
                         int selectSkill = _msg.reader().readShort();
                         //System.out.println("34  id: "+selectSkill);
                            SkillService.gI().selectSkill(player, selectSkill);
                    }
                    break;
                case 54:
                    if (player != null) {
                        Service.gI().attackMob(player, (int) (_msg.reader().readByte()));
                    }
                    break;
                case -60:
                    if (player != null) {
                        int playerId = _msg.reader().readInt();
                        Service.gI().attackPlayer(player, playerId);
                    }
                    break;
                case -27:
                    _session.sendKey();
                    DataGame.sendVersionRes(_session);
                    break;
                case -111:
                    DataGame.sendDataImageVersion(_session);
                    break;
                case -20:
                    if (player != null && !player.isDie()) {
                        int itemMapId = _msg.reader().readShort();
                        ItemMapService.gI().pickItem(player, itemMapId, false);
                    }
                    break;
                case -28:
                    messageNotMap(_session, _msg);
                    break;
             
                case -120:
                    BossManager.gI().teleBoss(player, _msg);
                    break;
                case -121:
                    BossManager.gI().summonBoss(player, _msg);
                    break;
                case -122:
                    Service.gI().managePlayer(player, _msg);
                    break;
                case -29:
                    messageNotLogin(_session, _msg);
                    break;
                case -30:
                    messageSubCommand(_session, _msg);
                    break;
                case -15: // về nhà
//                    if (player.getSession().isJail&&player.zone.map.mapId != 49) {
//                    Service.gI().sendThongBaoFromAdmin(player, "|7|BẠN ĐANG BỊ GIAM XIN HÃY QUAY LẠI");
//                    ChangeMapService.gI().changeMap(player, 49, 0, 760, 470);
//                    return;
//                    }
                    if (player != null) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                    }
                    break;
                case -16: // hồi sinh
                    if (player != null) {
                        PlayerService.gI().hoiSinh(player);
                    }
                default:
//                    Util.log("CMD: " + cmd);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            _msg.cleanup();
            _msg.dispose();
        }
    }

    public void messageNotLogin(MySession session, Message msg) {
//        Service.gI().sendThongBaoOK(session,"Anh dang that tinh khong muon lamf server nua");
//                    //System.out.println("doc key sai "+key);
//                    return;
        if (msg != null) {
           try{
               
           }catch(Exception e){
                Service.gI().sendThongBaoOK(session,"Vui lòng tải phiên bản chính thức tại https://www.nrokamui.online/");
               System.out.println("Loi doc key");
              }
            try {
                
                byte cmd = msg.reader().readByte();
                switch (cmd) {
                    case 0:
                        session.login(msg.reader().readUTF(), msg.reader().readUTF());
                        if (Manager.LOCAL) {
                            break;
                        }
                        System.out.println("█ Version Mod: " + msg.readUTF()+"\n█ [CONNECTING SUCSESS!]"+ "\n████████████████████\n");
                        break;
                    case 2:
                        Service.gI().setClientType(session, msg);
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                Logger.logException(Controller.class, e);
            }
        }
    }

    public void messageNotMap(MySession _session, Message _msg) {
        if (_msg != null) {
            Player player = null;
            try {
                player = _session.player;
                byte cmd = _msg.reader().readByte();
                switch (cmd) {
                    case 2:
                        createChar(_session, _msg);
                        break;
                    case 6:
                        DataGame.updateMap(_session);
                        break;
                    case 7:
                        DataGame.updateSkill(_session);
                        break;
                    case 8:
                        ItemData.updateItem(_session);
                        break;
                    case 10:
                        DataGame.sendMapTemp(_session, _msg.reader().readUnsignedByte());
                        break;
                    case 13:
                        //client ok
                        if (player != null) {
                            Service.gI().player(player);
                            Service.gI().Send_Caitrang(player);
//                            if(player.zone != null){
                                player.zone.load_Another_To_Me(player);
//                            }
                            // -64 my flag bag
                            Service.gI().sendFlagBag(player);

                            // -113 skill shortcut
                            player.playerSkill.sendSkillShortCut();
                            // item time
                            ItemTimeService.gI().sendAllItemTime(player);
                            ItemTimeService.gI().sendTextConDuongRanDoc(player);
                            ItemTimeService.gI().sendTextGas(player);
                            // send current task
                            TaskService.gI().sendInfoCurrentTask(player);
                        }
                        //System.out.println("done");
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                Logger.logException(Controller.class, e);
            }
        }
    }

    public void messageSubCommand(MySession _session, Message _msg) {
        if (_msg != null) {
            Player player = null;
            try {
                player = _session.player;
                byte command = _msg.reader().readByte();
                switch (command) {
                    case 16:
                        byte type = _msg.reader().readByte();
                        short point = _msg.reader().readShort();
                        if (player != null && player.nPoint != null) {
                            player.nPoint.increasePoint(type, point);
                        }
                        break;
                    case 17:
                        byte typeP = _msg.reader().readByte();
                        short pointP = _msg.reader().readShort();
                        if (player != null && player.pet != null && player.pet.nPoint != null && typeP < 3) {
                            player.pet.nPoint.increasePoint(typeP, pointP);
                        } else {
                            Service.gI().sendThongBaoOK(player," Đây là chỉ số gốc, không thể nâng chỉ số này!");
                        }
                        break;
                    case 19:
                        byte select = _msg.reader().readByte();
                        short cost = _msg.reader().readShort();
                        if (player != null && player.pet != null && player.pet.nPoint != null) {
                            player.pet.upSkillPet(select, cost);
                        }
                        break;
                    case 64:
                        int playerId = _msg.reader().readInt();
                        int menuId = _msg.reader().readShort();
                        SubMenuService.gI().controller(player, playerId, menuId);
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                Logger.logException(Controller.class, e);
            }
        }
    }

    public void createChar(MySession session, Message msg) {
        if (!Maintenance.isRuning) {
            GirlkunResultSet rs = null;
            boolean created = false;
            try {
                String name = msg.reader().readUTF();
                int gender = msg.reader().readByte();
                int hair = msg.reader().readByte();
                if (name.length() <= 10) {
                    rs = GirlkunDB.executeQuery("select * from player where name = ?", name);
                    if (rs.first()) {
                        Service.gI().sendThongBaoOK(session, "Tên nhân vật đã tồn tại");
                    } else {
                        if (Util.haveSpecialCharacter(name)) {
                            Service.gI().sendThongBaoOK(session, "Tên nhân vật không được chứa ký tự đặc biệt");
                        } else {
                            boolean isNotIgnoreName = true;
                            for (String n : ConstIgnoreName.IGNORE_NAME) {
                                if (name.equals(n)) {
                                    Service.gI().sendThongBaoOK(session, "Tên nhân vật đã tồn tại");
                                    isNotIgnoreName = false;
                                    break;
                                }
                            }
                            if (isNotIgnoreName) {
                                created = PlayerDAO.createNewPlayer(session.userId, name.toLowerCase(), (byte) gender, hair);
                            }
                        }
                    }
                } else {
                    Service.gI().sendThongBaoOK(session, "Tên nhân vật tối đa 10 ký tự");
                }
            } catch (Exception e) {
                Logger.logException(Controller.class, e);
            } finally {
                if (rs != null) {
                    rs.dispose();
                }
            }
            if (created) {
                session.login(session.uu, session.pp);
            } 
        } 
    }

    public void login2(MySession session, Message msg) {
       Service.gI().switchToRegisterScr(session);
      // Service.gI().sendThongBaoOK(session, "Vui lòng đăng ký tài khoản tại trang chủ https://www.nroblus.online/");
    }

    public void sendInfo(MySession session) {
        Player player = session.player;

        // -82 set tile map
        DataGame.sendTileSetInfo(session);

        // 112 my info intrinsic
        IntrinsicService.gI().sendInfoIntrinsic(player);

        // -42 my point
        Service.gI().point(player);

        // 40 task
        TaskService.gI().sendTaskMain(player);

        // -22 reset all
        Service.gI().clearMap(player);

        // -53 my clan
        ClanService.gI().sendMyClan(player);
        // -69 max statima
        PlayerService.gI().sendMaxStamina(player);

        // -68 cur statima
        PlayerService.gI().sendCurrentStamina(player);

        // -97 năng động
        // -107 have pet
        Service.gI().sendHavePet(player);

        // -119 top rank
        Service.gI().sendMessage(session, -119, "1630679754740_-119_r");

        // -50 thông tin bảng thông báo
        ServerNotify.gI().sendNotifyTab(player);
        // -24 join map - map info
        player.zone.load_Me_To_Another(player);
        player.zone.mapInfo(player);

        // -70 thông báo bigmessage
        sendThongBaoServer(player);

        //check activation set
        player.setClothes.setup();
        if (player.pet != null) {
            player.pet.setClothes.setup();
        }

        //last time use skill
        Service.getInstance().sendTimeSkill(player);

        //clear vt sk
        clearVTSK(player);

        if (TaskService.gI().getIdTask(player) == ConstTask.TASK_0_0) {
            NpcService.gI().createTutorial(player, 12639,
                    "Chào mừng " + player.name + " đến với máy chủ DragonBall Kamui\n"
                    + "Nhiệm vụ đầu tiên của bạn là di chuyển\n"
                    + "Bạn hãy di chuyển nhân vật theo mũi tên chỉ hướng");
        }
//        if (player.getSession().isJail && player.zone.map.mapId != 49) {
//                            Service.gI().sendThongBaoFromAdmin(player, "|7|BẠN ĐANG BỊ GIAM XIN HÃY QUAY LẠI");
//            ChangeMapService.gI().changeMap(player, 49, 0, 760, 470);
//        }
         if (player.inventory.itemsBody.get(10).isNotNullItem()) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Service.gI().sendPetFollow(player,
                            (short) (player.inventory.itemsBody.get(10).template.iconID - 1));
                } catch (Exception e) {
                }
            }).start();
        }
    }

    private void sendThongBaoServer(Player player) {
        Service.gI().sendThongBaoFromAdmin(player, "|7|Chào Mừng Cư Dân Đã Đến Với NroKamui!\n\n"
                      + "|4|(HƯỚNG DẪN : Chat (thongtin) hoặc (info) để xem thông tin nhân vật)\n\n"
                      + "|7|Chúc Các Cư Dân Chơi Game Vui Vẻ <3"+ "\nSự kiện : " + ConstEvent.gI().getNameEv() + " đang diễn ra, hãy mau chóng tham gia!");
    }

    private void clearVTSK(Player player) {
        player.inventory.itemsBag.stream().filter(item -> item.isNotNullItem() && item.template.id == 610).forEach(item -> {
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, item.quantity);
        });
        player.inventory.itemsBox.stream().filter(item -> item.isNotNullItem() && item.template.id == 610).forEach(item -> {
            InventoryServiceNew.gI().subQuantityItemsBox(player, item, item.quantity);
        });
        InventoryServiceNew.gI().sendItemBags(player);
    }
}
