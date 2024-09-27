///*
// */
//package ServerData.Services.Func.TaiXiu;
//
//import ServerData.Models.Item.Item;
//import ServerData.Models.Player.Player;
//import ServerData.Services.ChatGlobalService;
//import ServerData.Services.InventoryServiceNew;
//import ServerData.Services.ItemService;
//import ServerData.Services.Service;
//import ServerData.Utils.Util;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
///**
// *
// * @author Administrator
// */
//public class ChonAiDay implements Runnable {
//    public int goldNormar;
//    public int goldVip;
//    public long lastTimeEnd;
//    public boolean baotri = false;
//    public boolean kqchan = false;
//    public boolean kqle = false;
//    public boolean kqnull = false;
//    public List<Player> PlayersNormar = new ArrayList<>();
//    public List<Player> PlayersVIP = new ArrayList<>();
//    private static ChonAiDay instance;
//    
//    public static ChonAiDay gI() {
//        if (instance == null) {
//            instance = new ChonAiDay();
//        }
//        return instance;
//    } 
//    
//    public void addPlayerVIP(Player pl){
//        if(!PlayersVIP.equals(pl)){
//            PlayersVIP.add(pl);
//        }
//    }
//    
//    public void addPlayerNormar(Player pl){
//        if(!PlayersNormar.equals(pl)){
//            PlayersNormar.add(pl);
//        }
//    }
//    
//    public void removePlayerVIP(Player pl){
//        if(PlayersVIP.equals(pl)){
//            PlayersVIP.remove(pl);
//        }
//    }
//    
//    public void removePlayerNormar(Player pl){
//        if(PlayersNormar.equals(pl)){
//            PlayersNormar.remove(pl);
//        }
//    }
//    String[] chan = {"3 Trắng 1 Đỏ"+ " (Lẻ)","3 Đỏ 1 Trắng"+ " (Lẻ)"};
//    String[] le = {"4 Trắng"+ " (Chẵn)","4 Đỏ"+ " (Chẵn)","2 Trắng, 2 Đỏ"+ " (Chẵn)","2 Đỏ, 2 Trắng"+ " (Chẵn)"};
//
//    @Override
//    public void run() {
//        while (true) {
//            try{
//               if(((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis())/1000) <= 0) {
//                   if (Util.isTrue(60, 100)) {
//                    kqchan = true;
//                    kqle = false;
//                    kqnull = false;
//                    } else if (Util.isTrue(40, 100)) {
//                    kqle = true;
//                    kqchan = false;
//                    kqnull = false;
//                    } else {
//                    kqle = false;
//                    kqchan = false;
//                    kqnull = true;        
//                    }
//                    if (kqchan == true) {
//                    if (!ChonAiDay.gI().PlayersNormar.isEmpty()){    
//                    List<Player> listN = new ArrayList<>();
//                    ChonAiDay.gI().PlayersNormar.stream().filter(p -> p != null && p.goldNormar != 0).sorted(Comparator.comparing(p -> Math.ceil(((double)p.goldNormar/ChonAiDay.gI().goldNormar) * 100),Comparator.reverseOrder())).forEach(cl -> listN.add(cl));
//                        Player pl = listN.get(Util.nextInt(0, listN.size()));
//                        if(pl != null) {
//                            ChatGlobalService.gI().chat(pl, pl.name + " đã chiến thắng Chẵn Lẻ giải CHẴN");
//                            int goldC = ChonAiDay.gI().goldNormar * 80 / 100;
//                            Service.getInstance().sendThongBaoFromAdmin(pl, "|8|[ Số Nhà Cái Xóc Ra ]\n"
//                                                + "\n|2|"+le[Util.nextInt(le.length)]
//                                                + "\n\n|7|Bạn Đã Chiến Thắng"
//                                                + "\nBạn nhận được " + Util.format(goldC) +" Thỏi Vàng");
//                            Item it = ItemService.gI().createNewItem((short)457,goldC);
//                            it.itemOptions.add(new Item.ItemOption(30, 0));
//                            InventoryServiceNew.gI().addItemBag(pl, it);
//                            InventoryServiceNew.gI().sendItemBags(pl);
//                    }}
//                    for (int i=0; i< PlayersVIP.size();i++){
//                            Player plne = PlayersVIP.get(i);
//                                        Service.getInstance().sendThongBaoFromAdmin(plne, "|8|[ Số Nhà Cái Xóc Ra ]\n"
//                                                + "\n|2|" + le[Util.nextInt(le.length)]
//                                                + "\n\n|7|Trắng tay gòi, chơi lại đi!!!");
//                    }} else if (kqle == true) {
//                    if (!ChonAiDay.gI().PlayersVIP.isEmpty()){    
//                        List<Player> listN = new ArrayList<>();
//                        ChonAiDay.gI().PlayersVIP.stream().filter(p -> p != null && p.goldVIP != 0).sorted(Comparator.comparing(p -> Math.ceil(((double)p.goldVIP/ChonAiDay.gI().goldVip) * 100),Comparator.reverseOrder())).forEach(cl -> listN.add(cl));
//                        Player pl = listN.get(Util.nextInt(0, listN.size()));
//                        if(pl != null){
//                            ChatGlobalService.gI().chat(pl, pl.name + " đã chiến thắng Chẵn Lẻ giải LẺ");
//                            int goldC = ChonAiDay.gI().goldVip * 80 / 100;
//                            Service.getInstance().sendThongBaoFromAdmin(pl, "|8|[ Số Nhà Cái Xóc Ra ]\n"
//                                                + "\n|2|" + chan[Util.nextInt(chan.length)]
//                                                + "\n\n|7|Bạn Đã Chiến Thắng"
//                                                + "\nBạn nhận được " + Util.format(goldC) +" Thỏi Vàng");
//                            Item it = ItemService.gI().createNewItem((short)457,goldC);
//                            it.itemOptions.add(new Item.ItemOption(30, 0));
//                            InventoryServiceNew.gI().addItemBag(pl, it);
//                            InventoryServiceNew.gI().sendItemBags(pl);
//                    }} for (int i=0; i< PlayersNormar.size();i++){
//                            Player pl = PlayersNormar.get(i);
//                                        Service.getInstance().sendThongBaoFromAdmin(pl, "|8|[ Số Nhà Cái Xóc Ra ]\n"
//                                                + "\n|2|"+ chan[Util.nextInt(chan.length)]
//                                                + "\n\n|7|Trắng tay gòi, chơi lại đi!!!");
//                    }} else {
//                        for (int i=0; i< PlayersNormar.size();i++){
//                            Player plne = PlayersNormar.get(i);
//                                        Service.getInstance().sendThongBaoFromAdmin(plne, "|8|[ Số Nhà Cái Xóc Ra ]\n"
//                                                + "\n|2|"+ chan[Util.nextInt(chan.length)]
//                                                + "\n\n|7|Trắng tay gòi, chơi lại đi!!!");
//                                    }
//                        for (int i=0; i< PlayersVIP.size();i++){
//                            Player plne = PlayersVIP.get(i);
//                                        Service.getInstance().sendThongBaoFromAdmin(plne, "|8|[ Số Nhà Cái Xóc Ra ]\n"
//                                                + "\n|2|" + le[Util.nextInt(le.length)]
//                                                + "\n\n|7|Trắng tay gòi, chơi lại đi!!!");
//                                    }
//                                }
//                    for(int i = 0 ; i < ChonAiDay.gI().PlayersNormar.size();i++){
//                        Player pl = ChonAiDay.gI().PlayersNormar.get(i);
//                        if(pl != null){
//                            pl.goldVIP = 0;
//                            pl.goldNormar = 0;
//                        }
//                    }
//                    for(int i = 0 ; i < ChonAiDay.gI().PlayersVIP.size();i++){
//                        Player pl = ChonAiDay.gI().PlayersVIP.get(i);
//                        if(pl != null){
//                            pl.goldVIP = 0;
//                            pl.goldNormar = 0;
//                        }
//                    }
//                    ChonAiDay.gI().goldNormar = 0;
//                    ChonAiDay.gI().goldVip = 0;
//                    ChonAiDay.gI().PlayersNormar.clear();
//                    ChonAiDay.gI().PlayersVIP.clear();
//                    ChonAiDay.gI().lastTimeEnd = System.currentTimeMillis() + 30000;
//                } else {
//                    ChonAiDay.gI().lastTimeEnd = System.currentTimeMillis() + 30000;
//                    Thread.sleep(29000);
//               }
//            } catch (Exception e) {
//            }
//        }
//    } 
//}