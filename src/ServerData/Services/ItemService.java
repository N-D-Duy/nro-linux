package ServerData.Services;

import com.girlkun.database.GirlkunDB;
import Server.Connect.ShopData;
import ServerData.Models.Item.Template;
import ServerData.Models.Item.Template.ItemOptionTemplate;
import ServerData.Models.Item.Item;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Models.Shop.ItemShop;
import ServerData.Server.Manager;
import ServerData.Services.Func.CombineServiceNew;
import ServerData.Utils.TimeUtil;
import ServerData.Utils.Util;
import ServerData.Models.Item.Item.ItemOption;
import ServerData.Models.Shop.Shop;
import ServerData.Models.Shop.ShopServiceNew;
import java.sql.Connection;

import java.util.*;
import java.util.stream.Collectors;


public class ItemService {

    private static ItemService i;

    public static ItemService gI() {
        if (i == null) {
            i = new ItemService();
        }
        return i;
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

    public Item createItemFromItemShop(ItemShop itemShop) {
        Item item = new Item();
        item.template = itemShop.temp;
        item.quantity = 1;
        item.content = item.getContent();
        item.info = item.getInfo();
        for (Item.ItemOption io : itemShop.options) {
//            if(io.optionTemplate.id == 239){
//                if(io.param <= 0) return null;
//                try {
//                    GirlkunDB.executeUpdate("UPDATE item_shop_option SET param = param - 1 WHERE item_shop_option.item_shop_id IN (SELECT id FROM item_shop WHERE temp_id = ? ) AND item_shop_option.option_id = 239 AND item_shop_option.param > 0;", itemShop.temp.id);
//                    System.out.println("thành  công");
//                }catch(Exception e){}
//                    Manager.gI().loadShops();
//            }
            item.itemOptions.add(new Item.ItemOption(io));
        }
        switch (item.template.id) {
            case 656:
                item.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(3) + 17)); //chí mạng 17-19%
                item.itemOptions.add(new Item.ItemOption(21, 40)); 
                item.itemOptions.add(new Item.ItemOption(30, 0)); 
                break;
            case 654:
            case 650:    
            case 652: 
                item.itemOptions.add(new Item.ItemOption(47, new Random().nextInt(1001) + 1500)); // áo từ 1800-2800 giáp
                item.itemOptions.add(new Item.ItemOption(21, 40)); 
                item.itemOptions.add(new Item.ItemOption(30, 0)); 
                break;
            case 657:
            case 659:    
            case 661:
                item.itemOptions.add(new Item.ItemOption(0, new Random().nextInt(500) + 7500)); // găng
                item.itemOptions.add(new Item.ItemOption(21, 40)); 
                item.itemOptions.add(new Item.ItemOption(30, 0)); 
                break;
            case 651:   
            case 655:   
            case 653: 
                item.itemOptions.add(new Item.ItemOption(22, new Random().nextInt(10) + 80)); // ki 80-90k
                item.itemOptions.add(new Item.ItemOption(21, 40)); 
                item.itemOptions.add(new Item.ItemOption(30, 0)); 
                break;
            case 658:
            case 660:
            case 662:
                item.itemOptions.add(new Item.ItemOption(23, new Random().nextInt(10) + 80)); // ki 80-90k
                item.itemOptions.add(new Item.ItemOption(21, 40)); 
                item.itemOptions.add(new Item.ItemOption(30, 0)); 
                break;
            default:
                break;
        }
        return item;
    }   
    
    public Item copyItem(Item item) {
        Item it = new Item();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.info = item.info;
        it.content = item.content;
        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (Item.ItemOption io : item.itemOptions) {
            it.itemOptions.add(new Item.ItemOption(io));
        }
        return it;
    }

    public Item createNewItem(short tempId) {
        return createNewItem(tempId, 1);
    }
    
    public Item otpts(short tempId) {
        return otpts(tempId, 1);
    }

    public Item createNewItem(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
        
    }
    
        public Item otpts(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        if (item.template.type== 0){
            item.itemOptions.add(new ItemOption(21,80));
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000,2500)));
        }
        if (item.template.type== 1){
            item.itemOptions.add(new ItemOption(21,80));
            item.itemOptions.add(new ItemOption(22, Util.nextInt(150,200)));
        }
        if (item.template.type== 2){
            item.itemOptions.add(new ItemOption(21,80));
            item.itemOptions.add(new ItemOption(0, Util.nextInt(18000,20000)));
        }
        if (item.template.type== 3){
            item.itemOptions.add(new ItemOption(21,80));
            item.itemOptions.add(new ItemOption(23, Util.nextInt(150,200)));
        }
        if (item.template.type== 4){
            item.itemOptions.add(new ItemOption(21,80));
            item.itemOptions.add(new ItemOption(14, Util.nextInt(20,25)));
        }
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item createItemSetKichHoat(int tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.itemOptions = createItemNull().itemOptions;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }
        public Item createItemSetKichHoatV(int tempId, int quantity) {
        Item itemV = new Item();
        itemV.template = getTemplate(tempId);
        itemV.quantity = quantity;
        itemV.itemOptions = createItemNull().itemOptions;
        itemV.createTime = System.currentTimeMillis();
        itemV.content = itemV.getContent();
        itemV.info = itemV.getInfo();
        return itemV;
    }

    public Item createItemDoHuyDiet(int tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.itemOptions = createItemNull().itemOptions;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item createItemFromItemMap(ItemMap itemMap) {
        Item item = createNewItem(itemMap.itemTemplate.id, itemMap.quantity);
        item.itemOptions = itemMap.options;
        return item;
    }

    public ItemOptionTemplate getItemOptionTemplate(int id) {
        return Manager.ITEM_OPTION_TEMPLATES.get(id);
    }

    public Template.ItemTemplate getTemplate(int id) {
        return Manager.ITEM_TEMPLATES.get(id);
    }

    public boolean isItemActivation(Item item) {
        return false;
    }

    public int getPercentTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                    return 10;
                case 530:
                case 535:
                    return 20;
                case 531:
                case 536:
                    return 30;
                default:
                    return 0;
            }
        } else {
            return 0;
        }
    }

    public boolean isTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                case 530:
                case 535:
                case 531:
                case 536:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean isOutOfDateTime(Item item) {
        if (item != null) {
            for (Item.ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 93) {
                    int dayPass = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
                    if (dayPass != 0) {
                        io.param -= dayPass;
                        if (io.param <= 0) {
                            return true;
                        } else {
                            item.createTime = System.currentTimeMillis();
                        }
                    }
                }
            }
        }
        return false;
    }

    public void OpenSetTongHop(Player player, int itemUseId, int select) throws Exception {
        if (select < 0 || select > 4) return;
        Item itemUse = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, itemUseId);
        int[][] itemthuong = {{0, 6, 21, 27, 12}, {1, 7, 22, 28, 12}, {2, 8, 23, 29, 12}};
        int[][] itemdtl = {{555, 556, 562, 563, 561}, {557, 558, 564, 565, 561}, {559, 560, 566, 567, 561}};
        int[][] itemdhd = {{650, 651, 657, 658, 656}, {652, 653, 659, 660, 656}, {654, 655, 661, 662, 656}}; //td, namec,xd
        int[][] itemdts = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2
        int[][] optionskh = {{128, 127, 129}, {130, 131, 132}, {133, 134, 135}};
        int[][] optionskhvip = {{224, 225, 226}, {227, 228, 229}, {230, 231, 232}};
        int skh1 = 25;// ti le %
        int skh2 = 35;//ti le %
        int skhc = 40;//ti le %
        int skhId = -1;
        int rd = Util.nextInt(1, 100);
        if (rd <= skh1) {
            skhId = 0;
        } else if (rd <= skh1 + skh2) {
            skhId = 1;
        } else if (rd <= skh1 + skh2 + skhc) {
            skhId = 2;
        }
        Item item = null;
        switch (itemUseId) {
            case 2000:
                item = itemSKHT(itemthuong[player.gender][select], optionskh[player.gender][skhId]);//set kích hoạt
                break;
            case 2001:
                item = randomCS_DTLKH(itemdtl[player.gender][select], optionskh[player.gender][skhId]);// set kích hoạt tl
                item.itemOptions.add(new Item.ItemOption(30, 1));// ko the gd
                break;
            case 2002:
                item = randomCS_DHDKH(itemdhd[player.gender][select], optionskh[player.gender][skhId]);// set kích hoạt hd
                item.itemOptions.add(new Item.ItemOption(30, 1));// ko the gd
                break;
            case 2003:
                item = randomCS_DTSKH(itemdts[player.gender][select], optionskh[player.gender][skhId]);// set kích hoạt hd
                item.itemOptions.add(new Item.ItemOption(30, 1));// ko the gd
                break;
            case 2004: 
                item = randomCS_DTL(itemdtl[player.gender][select], player.gender); // hộp đồ tl
                break;
            case 2005: 
                item = randomCS_DHD(itemdhd[player.gender][select], player.gender); // hộp đồ hd
                break;
            case 2006: 
                item = randomCS_DTS(itemdts[player.gender][select], player.gender); // hộp đồ ts
                break;
        }
        if (item != null && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }
     
    public Item itemSKH(int itemId, int skhId) {
        Item item = createItemSetKichHoat(itemId, 1);
        if (item != null) {
            switch (itemId) {
//                 case 0:
//                case 1:
//                case 2:
//                    item.itemOptions.add(new Item.ItemOption(47, Util.nextInt(2, 5)));
//                  break;
                case 555:
                case 557:
                case 559:
                    item.itemOptions.add(new Item.ItemOption(47, Util.nextInt(200, 500)));
                    item.itemOptions.add(new Item.ItemOption(21, 16));
                    break;
                case 650:
                case 652:
                case 654:
                    item.itemOptions.add(new Item.ItemOption(47, Util.nextInt(1000, 1800)));
                    item.itemOptions.add(new Item.ItemOption(21, 80));
                    break;
                case 1048:
                case 1049:
                case 1050:
                    item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
                    item.itemOptions.add(new ItemOption(21, 200));
                    break;
//                    case 6:
//                case 7:
//                case 8:
//                    item.itemOptions.add(new Item.ItemOption(6, Util.nextInt(20, 25)));
//                    
//               
//                    break;
                case 556:
                case 558:
                case 560:
                    item.itemOptions.add(new Item.ItemOption(22, Util.nextInt(50, 70)));
                    item.itemOptions.add(new Item.ItemOption(27, Util.nextInt(10000)));
                    item.itemOptions.add(new Item.ItemOption(21, 16));
                    break;
                case 651:
                case 653:
                case 655:
                    item.itemOptions.add(new Item.ItemOption(22, Util.nextInt(85, 110)));
                    item.itemOptions.add(new Item.ItemOption(27, Util.nextInt(20000)));
                    item.itemOptions.add(new Item.ItemOption(21, 80));
                    break;
                case 1051:
                case 1052:
                case 1053:
                    item.itemOptions.add(new ItemOption(22, Util.nextInt(150, 200)));
                    item.itemOptions.add(new Item.ItemOption(21, 200));
                    break;
//                    case 21:
//                case 22:
//                case 23:
//                    item.itemOptions.add(new Item.ItemOption(0, Util.nextInt(1, 10)));
//                    
//                    break;
                case 562:
                case 564:
                case 566:
                    item.itemOptions.add(new Item.ItemOption(0, Util.nextInt(3000, 5000)));
                    item.itemOptions.add(new Item.ItemOption(21, 16));
                    break;
                case 657:
                case 659:
                case 661:
                    item.itemOptions.add(new Item.ItemOption(0, Util.nextInt(6000, 8000)));
                    item.itemOptions.add(new Item.ItemOption(21, 80));
                    break;
                case 1054:
                case 1055:
                case 1056:
                    item.itemOptions.add(new ItemOption(0, Util.nextInt(10000, 13000)));
                    item.itemOptions.add(new Item.ItemOption(21, 200));
                    break;
//                    case 27:
//                case 28:
//                case 29:
//                    item.itemOptions.add(new Item.ItemOption(7, Util.nextInt(20, 25)));
//               
//                    break;
                case 565:
                case 563:
                case 567:
                    item.itemOptions.add(new Item.ItemOption(7, Util.nextInt(50000, 70000)));
                    item.itemOptions.add(new Item.ItemOption(28, Util.nextInt(10000)));
                    item.itemOptions.add(new Item.ItemOption(21, 16));
                    break;
                case 658:
                case 660:
                case 662:
                    item.itemOptions.add(new Item.ItemOption(7, Util.nextInt(80000, 110000)));
                    item.itemOptions.add(new Item.ItemOption(28, Util.nextInt(20000)));
                    item.itemOptions.add(new Item.ItemOption(21, 80));
                    break;
                case 1057:
                case 1058:
                case 1059:
                    item.itemOptions.add(new ItemOption(23, Util.nextInt(200, 250)));
                    item.itemOptions.add(new Item.ItemOption(21, 200));
                    break;
                case 561:
                    item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(15, 18)));
                    item.itemOptions.add(new Item.ItemOption(21, 16));
                    break;
                case 656:
                    item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(17, 20)));
                    item.itemOptions.add(new Item.ItemOption(21, 80));
                    break;
                case 1060:
                case 1061:
                case 1062:
                    item.itemOptions.add(new ItemOption(14, Util.nextInt(20, 24)));
                    item.itemOptions.add(new Item.ItemOption(21, 200));
                    break;
                default:
                    break;
            }
            item.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) itemId));
            item.itemOptions.add(new Item.ItemOption(skhId, 1));
            item.itemOptions.add(new Item.ItemOption(195, 1));
            item.itemOptions.add(new Item.ItemOption(optionIdSKH(skhId), 1));
            item.itemOptions.add(new Item.ItemOption(30, 0));
          
        }
        return item;
    }

    public void OpenSetTuyChon(Player player, int itemUseId, int select) throws Exception {
        if (select < 0 || select > 4) return;
        Item itemUse = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, (itemUseId >= 0 && itemUseId <= 2 ? 1498 : itemUseId >= 3 && itemUseId <= 5 ? 1499 : itemUseId >= 6 && itemUseId <= 8 ? 1500 : itemUseId >= 9 && itemUseId <= 11 ? 1501 : itemUseId));
        int[][] itemthuong = {{0, 6, 21, 27, 12}, {1, 7, 22, 28, 12}, {2, 8, 23, 29, 12}};
        int[][] itemdtl = {{555, 556, 562, 563, 561}, {557, 558, 564, 565, 561}, {559, 560, 566, 567, 561}};
        int[][] itemdhd = {{650, 651, 657, 658, 656}, {652, 653, 659, 660, 656}, {654, 655, 661, 662, 656}}; //td, namec,xd
        int[][] itemdts = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2
        int[][] optionskh = {{128, 127, 129}, {130, 131, 132}, {133, 134, 135}};
        int[][] optionskhvip = {{224, 225, 226}, {227, 228, 229}, {230, 231, 232}};
        Item item = null;
        switch (itemUseId) {
            case 0:
                item = itemSKHTN(itemthuong[player.gender][select], optionskh[player.gender][0]);//set kích hoạt
                item.itemOptions.add(new Item.ItemOption(Util.nextInt(86,87), 1));// ký gửi
                item.itemOptions.add(new Item.ItemOption(237, 0));// co the gd
                break;
            case 1:
                item = itemSKHTN(itemthuong[player.gender][select], optionskh[player.gender][1]);//set kích hoạt
                item.itemOptions.add(new Item.ItemOption(Util.nextInt(86,87), 1));// ký gửi
                item.itemOptions.add(new Item.ItemOption(237, 0));// co the gd
                break;
            case 2:
                item = itemSKHTN(itemthuong[player.gender][select], optionskh[player.gender][2]);//set kích hoạt
                item.itemOptions.add(new Item.ItemOption(Util.nextInt(86,87), 1));// ký gửi
                item.itemOptions.add(new Item.ItemOption(237, 0));// co the gd
                break;
            case 3: // select 1 skh tl
                item = randomCS_DTLKH(itemdtl[player.gender][select], optionskh[player.gender][0]);// set kích hoạt tl
                item.itemOptions.add(new Item.ItemOption(Util.nextInt(86,87), 1));// ký gửi
                item.itemOptions.add(new Item.ItemOption(237, 0));// co the gd
                break;
            case 4://  select 2 sih tl
                item = randomCS_DTLKH(itemdtl[player.gender][select], optionskh[player.gender][1]);// set kích hoạt tl
                item.itemOptions.add(new Item.ItemOption(Util.nextInt(86,87), 1));// ký gửi
                item.itemOptions.add(new Item.ItemOption(237, 0));// co the gd
                break;
            case 5: // select 3 skh tl
                item = randomCS_DTLKH(itemdtl[player.gender][select], optionskh[player.gender][2]);// set kích hoạt tl
                item.itemOptions.add(new Item.ItemOption(Util.nextInt(86,87), 1));// ký gửi
                item.itemOptions.add(new Item.ItemOption(237, 0));// co the gd
                break;
            case 6: // select 1 skh hd
                item = randomCS_DHDKH(itemdhd[player.gender][select], optionskh[player.gender][0]);// set kích hoạt tl
                item.itemOptions.add(new Item.ItemOption(Util.nextInt(86,87), 1));// ký gửi
                item.itemOptions.add(new Item.ItemOption(237, 0));// co the gd
                break;
            case 7://  select 2 sih hd
                item = randomCS_DHDKH(itemdhd[player.gender][select], optionskh[player.gender][1]);// set kích hoạt tl
                item.itemOptions.add(new Item.ItemOption(Util.nextInt(86,87), 1));// ký gửi
                item.itemOptions.add(new Item.ItemOption(237, 0));// co the gd
                break;
            case 8: // select 3 skh hd
                item = randomCS_DHDKH(itemdhd[player.gender][select], optionskh[player.gender][2]);// set kích hoạt tl
                item.itemOptions.add(new Item.ItemOption(Util.nextInt(86,87), 1));// ký gửi
                item.itemOptions.add(new Item.ItemOption(237, 0));// co the gd
                break;
            case 9: // select 1 skh ts
                item = randomCS_DTSKH(itemdts[player.gender][select], optionskh[player.gender][0]);// set kích hoạt tl
                item.itemOptions.add(new Item.ItemOption(Util.nextInt(86,87), 1));// ký gửi
                item.itemOptions.add(new Item.ItemOption(237, 0));// co the gd
                break;
            case 10://  select 2 sih ts
                item = randomCS_DTSKH(itemdts[player.gender][select], optionskh[player.gender][1]);// set kích hoạt tl
                item.itemOptions.add(new Item.ItemOption(Util.nextInt(86,87), 1));// ký gửi
                item.itemOptions.add(new Item.ItemOption(237, 0));// co the gd
                break;
            case 11: // select 3 skh ts
                item = randomCS_DTSKH(itemdts[player.gender][select], optionskh[player.gender][2]);// set kích hoạt tl
                item.itemOptions.add(new Item.ItemOption(Util.nextInt(86,87), 1));// ký gửi
                item.itemOptions.add(new Item.ItemOption(237, 0));// co the gd
                break;
        }
        if (item != null && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public int randomSKHTId(byte gender) {
        if (gender == 3) gender = 2;
        int[][] options = {{127, 128, 129}, {130, 131, 132}, {133, 134, 135}};
        int skht1 = 25;
        int skht2 = 35;
        int skhtc = 40;
        int skhtId = -1;
        int rd = Util.nextInt(1, 100);
        if (rd <= skht1) {
            skhtId = 0;
        } else if (rd <= skht1 + skht2) {
            skhtId = 1;
        } else if (rd <= skht1 + skht2 + skhtc) {
            skhtId = 2;
        }
        return options[gender][skhtId];
    }
    
    public int randomSKHVId(byte gender) {
        if (gender == 3) gender = 2;
        int[][] options = {{224, 225, 226}, {227, 228, 229}, {230, 231, 232}};
        int skhv1 = 25;
        int skhv2 = 35;
        int skhc = 40;
        int skhId = -1;
        int rd = Util.nextInt(1, 100);
        if (rd <= skhv1) {
            skhId = 0;
        } else if (rd <= skhv1 + skhv2) {
            skhId = 1;
        } else if (rd <= skhv1 + skhv2 + skhc) {
            skhId = 2;
        }
        return options[gender][skhId];
    }
    
    public Item itemSKHT(int itemId, int skhtId) {
        Item item = createItemSetKichHoat(itemId, 1);
        if (item != null) {
            item.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) itemId));
            item.itemOptions.add(new Item.ItemOption(skhtId, 1));
            item.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
            item.itemOptions.add(new Item.ItemOption(30, 1));
        }
        return item;
    }
    public Item itemSKHTN(int itemId, int skhtId) {
        Item item = createItemSetKichHoat(itemId, 1);
        if (item != null) {
            item.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) itemId));
            item.itemOptions.add(new Item.ItemOption(skhtId, 1));
            item.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        return item;
    }
    public Item itemSKHV(int itemId, int skhvId) {
        Item itemV = createItemSetKichHoatV(itemId, 1);
        if (itemV != null) {
            itemV.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) itemId));
            itemV.itemOptions.add(new Item.ItemOption(skhvId, 1));
            itemV.itemOptions.add(new Item.ItemOption(optionIdSKHVIP(skhvId), 1));
            itemV.itemOptions.add(new Item.ItemOption(30, 1));
        }
        return itemV;
    }
    public int optionItemSKH(int typeItem) {
        switch (typeItem) {
            case 0:
                return 47;
            case 1:
                return 6;
            case 2:
                return 0;
            case 3:
                return 7;
            default:
                return 14;
        }
    }

    public int pagramItemSKH(int typeItem) {
        switch (typeItem) {
            case 0:
            case 2:
                return Util.nextInt(5);
            case 1:
            case 3:
                return Util.nextInt(20, 30);
            default:
                return Util.nextInt(3);
        }
    }

    public int optionIdSKH(int skhtId) {
        switch (skhtId) {
            case 127: //Set Arriety Taiyoken
                return 139;
            case 128: //Set Arriety Genki
                return 140;
            case 129: //Set Arriety Kamejoko
                return 141;
            case 130: //Set Arriety KI
                return 142;
            case 131: //Set Arriety Dame
                return 143;
            case 132: //Set Arriety Summon
                return 144;
            case 133: //Set Arriety Galick
                return 136;
            case 134: //Set Arriety Monkey
                return 137;
            case 135: //Set Arriety HP
                return 138;
            case 224: //Set Arriety Taiyoken
                return 218;
            case 225: //Set Arriety Genki
                return 219;
            case 226: //Set Arriety Kamejoko
                return 220;
            case 227: //Set Arriety KI
                return 221;
            case 228: //Set Arriety Dame
                return 222;
            case 229: //Set Arriety Summon
                return 223;
            case 230: //Set Arriety Galick
                return 215;
            case 231: //Set Arriety Monkey
                return 216;
            case 232: //Set Arriety HP
                return 217;
            case 197: //Set Arriety Taiyoken
                return 44;
            case 198: //Set Arriety Genki
                return 45;
            case 199: //Set Arriety Kamejoko
                return 46;
            case 200: //Set Arriety KI
                return 189;
            case 201: //Set Arriety Dame
                return 190;
            case 202: //Set Arriety Summon
                return 191;
            case 203: //Set Arriety Galick
                return 41;
            case 204: //Set Arriety Monkey
                return 42;
            case 205: //Set Arriety HP
                return 43;
                
        }
        return 0;
    }
    public int optionIdSKHVIP(int skhvId) {
        switch (skhvId) {
            case 230: //
                return 215;
            case 231: //
                return 216;
            case 232: //
                return 217;
            case 224: //
                return 218;
            case 225: //
                return 219;
            case 226: //
                return 220;
            case 227: //
                return 221;
            case 228: //
                return 222;
            case 229: //
                return 223;
        }
        return 0;
    }
    public Item itemDHD(int itemId, int dhdId) {
        Item item = createItemSetKichHoat(itemId, 1);
        if (item != null) {
            item.itemOptions.add(new Item.ItemOption(dhdId, 1));
            item.itemOptions.add(new Item.ItemOption(optionIdDHD(dhdId), 1));
            item.itemOptions.add(new Item.ItemOption(30, 1));
        }
        return item;
    }

    public int optionIdDHD(int skhId) {
        switch (skhId) {
            case 127: //Set Arriety Taiyoken
                return 139;
            case 128: //Set Arriety Genki
                return 140;
            case 129: //Set Arriety Kamejoko
                return 141;
            case 130: //Set Arriety KI
                return 142;
            case 131: //Set Arriety Dame
                return 143;
            case 132: //Set Arriety Summon
                return 144;
            case 133: //Set Arriety Galick
                return 136;
            case 134: //Set Arriety Monkey
                return 137;
            case 135: //Set Arriety HP
                return 138;
        }
        return 0;
    }
    public Item randomCS_DTS(int itemId, int gender) {
        Item it = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        List<Integer> nhan = Arrays.asList(1060, 1061, 1062);
        if (ao.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(gender == 2, new Random().nextInt(500) + 2000))); // áo từ 1800-2800 giáp
        }
        if (quan.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(50) + 150))); // hp 85-100k
        }
        if (gang.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(2000) + 18000))); // 8500-10000
        }
        if (giay.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(50) + 150))); // ki 80-90k
        }
        if (nhan.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(5) + 21)); //chí mạng 17-19%
        }
        it.itemOptions.add(new Item.ItemOption(21, 80));// yêu cầu sm 80 tỉ
        it.itemOptions.add(new Item.ItemOption(30, 1));// ko the gd
        return it;
    }
    public Item randomCS_DTSKH(int itemId, int skhtId) {
        Item it = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        List<Integer> nhan = Arrays.asList(1060, 1061, 1062);
        if (ao.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(it.template.gender == 2, new Random().nextInt(500) + 2000))); // áo từ 1800-2800 giáp
            it.itemOptions.add(new Item.ItemOption(skhtId, 1));
            it.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        if (quan.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(it.template.gender == 0, new Random().nextInt(50) + 150))); // hp 85-100k
            it.itemOptions.add(new Item.ItemOption(skhtId, 1));
            it.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        if (gang.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(it.template.gender == 2, new Random().nextInt(2000) + 18000))); // 8500-10000
            it.itemOptions.add(new Item.ItemOption(skhtId, 1));
            it.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        if (giay.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(it.template.gender == 1, new Random().nextInt(50) + 150))); // ki 80-90k
            it.itemOptions.add(new Item.ItemOption(skhtId, 1));
            it.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        if (nhan.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(14,Util.highlightsItem(it.template.gender == 2, new Random().nextInt(5) + 21))); //chí mạng 17-19%
            it.itemOptions.add(new Item.ItemOption(skhtId, 1));
            it.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        it.itemOptions.add(new Item.ItemOption(21, 80));// yêu cầu sm 80 tỉ
        return it;
    }
    public Item randomCS_DHD(int itemId, int gender) {
        Item it = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(650, 652, 654);
        List<Integer> quan = Arrays.asList(651, 653, 655);
        List<Integer> gang = Arrays.asList(657, 659, 661);
        List<Integer> giay = Arrays.asList(658, 660, 662);
        int nhd = 656;
        if (ao.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(gender == 2, new Random().nextInt(1001) + 1500))); // áo từ 1800-2800 giáp
        }
        if (quan.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(16) + 70))); // hp 85-100k
        }
        if (gang.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(150) + 7500))); // 8500-10000
        }
        if (giay.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(11) + 80))); // ki 80-90k
        }
        if (nhd == itemId) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(3) + 17)); //chí mạng 17-19%
        }
        it.itemOptions.add(new Item.ItemOption(21, 40));// yêu cầu sm 80 tỉ
        it.itemOptions.add(new Item.ItemOption(30, 1));// ko the gd
        return it;
    }
    public Item randomCS_DTL(int itemId, int gender) {
        Item it = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains((int) itemId)) {
            it.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(it.template.gender == 2, new Random().nextInt(501) + 1000)));
        }
        if (quan.contains((int) itemId)) {
            it.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(it.template.gender == 0, new Random().nextInt(11) + 45)));
        }
        if (gang.contains((int) itemId)) {
            it.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(it.template.gender == 2, new Random().nextInt(1001) + 3500)));
        }
        if (giay.contains((int) itemId)) {
            it.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(it.template.gender == 1, new Random().nextInt(11) + 35)));
        }
        if (ntl == itemId) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(3) + 15));
        }
        it.itemOptions.add(new Item.ItemOption(21, 18));
        it.itemOptions.add(new Item.ItemOption(30, 1));
        return it;
    }
    public Item randomCS_DTLKH(int itemId, int skhtId) {
        Item it = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains((int) itemId)) {
            it.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(it.template.gender == 0, new Random().nextInt(501) + 1000)));
            it.itemOptions.add(new Item.ItemOption(skhtId, 1));
            it.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        if (quan.contains((int) itemId)) {
            it.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(it.template.gender == 0, new Random().nextInt(11) + 45)));
            it.itemOptions.add(new Item.ItemOption(skhtId, 1));
            it.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        if (gang.contains((int) itemId)) {
            it.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(it.template.gender == 2, new Random().nextInt(1001) + 3500)));
            it.itemOptions.add(new Item.ItemOption(skhtId, 1));
            it.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        if (giay.contains((int) itemId)) {
            it.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(it.template.gender == 1, new Random().nextInt(11) + 35)));
            it.itemOptions.add(new Item.ItemOption(skhtId, 1));
            it.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        if (ntl == itemId) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(3) + 15));
            it.itemOptions.add(new Item.ItemOption(skhtId, 1));
            it.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        it.itemOptions.add(new Item.ItemOption(21, 18));
        return it;
    }
    public Item randomCS_DHDKH(int itemId, int skhtId) {
        Item it = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(650, 652, 654);
        List<Integer> quan = Arrays.asList(651, 653, 655);
        List<Integer> gang = Arrays.asList(657, 659, 661);
        List<Integer> giay = Arrays.asList(658, 660, 662);
        int nhd = 656;
        if (ao.contains((int) itemId)) {
            it.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(it.template.gender == 2, new Random().nextInt(1001) + 1500))); // áo từ 1800-2800 giáp
            it.itemOptions.add(new Item.ItemOption(skhtId, 1));
            it.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        if (quan.contains((int) itemId)) {
            it.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(it.template.gender == 0, new Random().nextInt(16) + 70))); // hp 85-100k
            it.itemOptions.add(new Item.ItemOption(skhtId, 1));
            it.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        if (gang.contains((int) itemId)) {
            it.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(it.template.gender == 2, new Random().nextInt(150) + 7500))); // 8500-10000
            it.itemOptions.add(new Item.ItemOption(skhtId, 1));
            it.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        if (giay.contains((int) itemId)) {
            it.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(it.template.gender == 1, new Random().nextInt(11) + 80))); // ki 80-90k
            it.itemOptions.add(new Item.ItemOption(skhtId, 1));
            it.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        if (nhd == itemId) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(3) + 17)); //chí mạng 17-19%
            it.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) itemId));
            it.itemOptions.add(new Item.ItemOption(skhtId, 1));
            it.itemOptions.add(new Item.ItemOption(optionIdSKH(skhtId), 1));
        }
        it.itemOptions.add(new Item.ItemOption(21, 45));// yêu cầu sm 45 tỉ
        return it;
    }
    //Cải trang sự kiện 20/11
    public Item caitrang2011(boolean rating) {
        Item item = createItemSetKichHoat(680, 1);
        item.itemOptions.add(new Item.ItemOption(76, 1));//VIP
        item.itemOptions.add(new Item.ItemOption(77, 28));//hp 28%
        item.itemOptions.add(new Item.ItemOption(103, 25));//ki 25%
        item.itemOptions.add(new Item.ItemOption(147, 24));//sd 26%
        item.itemOptions.add(new Item.ItemOption(117, 18));//Đẹp + 18% sd
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }
 
    //skh tl
    public void settaiyokentl(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1502 + i);
            Item ao = Util.ratiItemTL((short) 555);
            Item quan = Util.ratiItemTL((short) 556);
            Item gang = Util.ratiItemTL((short) 562);
            Item giay = Util.ratiItemTL((short) 563);
            Item nhan = Util.ratiItemTL((short) 561);
            ao.itemOptions.add(new Item.ItemOption(127, 0));
            quan.itemOptions.add(new Item.ItemOption(127, 0));
            gang.itemOptions.add(new Item.ItemOption(127, 0));
            giay.itemOptions.add(new Item.ItemOption(127, 0));
            nhan.itemOptions.add(new Item.ItemOption(127, 0));
            ao.itemOptions.add(new Item.ItemOption(139, 0));
            quan.itemOptions.add(new Item.ItemOption(139, 0));
            gang.itemOptions.add(new Item.ItemOption(139, 0));
            giay.itemOptions.add(new Item.ItemOption(139, 0));
            nhan.itemOptions.add(new Item.ItemOption(139, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thần linh ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    public void setgenkitl(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1502 + i);
            Item ao = Util.ratiItemTL((short) 555);
            Item quan = Util.ratiItemTL((short) 556);
            Item gang = Util.ratiItemTL((short) 562);
            Item giay = Util.ratiItemTL((short) 563);
            Item nhan = Util.ratiItemTL((short) 561);
            ao.itemOptions.add(new Item.ItemOption(128, 0));
            quan.itemOptions.add(new Item.ItemOption(128, 0));
            gang.itemOptions.add(new Item.ItemOption(128, 0));
            giay.itemOptions.add(new Item.ItemOption(128, 0));
            nhan.itemOptions.add(new Item.ItemOption(128, 0));
            ao.itemOptions.add(new Item.ItemOption(140, 0));
            quan.itemOptions.add(new Item.ItemOption(140, 0));
            gang.itemOptions.add(new Item.ItemOption(140, 0));
            giay.itemOptions.add(new Item.ItemOption(140, 0));
            nhan.itemOptions.add(new Item.ItemOption(140, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thần linh ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    public void setkamejokotl(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1502 + i);
            Item ao = Util.ratiItemTL((short) 555);
            Item quan = Util.ratiItemTL((short) 556);
            Item gang = Util.ratiItemTL((short) 562);
            Item giay = Util.ratiItemTL((short) 563);
            Item nhan = Util.ratiItemTL((short) 561);
            ao.itemOptions.add(new Item.ItemOption(129, 0));
            quan.itemOptions.add(new Item.ItemOption(129, 0));
            gang.itemOptions.add(new Item.ItemOption(129, 0));
            giay.itemOptions.add(new Item.ItemOption(129, 0));
            nhan.itemOptions.add(new Item.ItemOption(129, 0));
            ao.itemOptions.add(new Item.ItemOption(141, 0));
            quan.itemOptions.add(new Item.ItemOption(141, 0));
            gang.itemOptions.add(new Item.ItemOption(141, 0));
            giay.itemOptions.add(new Item.ItemOption(141, 0));
            nhan.itemOptions.add(new Item.ItemOption(141, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thần linh ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    public void setgodkitl(Player player) throws Exception {//nm
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1502 + i);
            Item ao = Util.ratiItemTL((short) 557);
            Item quan = Util.ratiItemTL((short) 558);
            Item gang = Util.ratiItemTL((short) 564);
            Item giay = Util.ratiItemTL((short) 565);
            Item nhan = Util.ratiItemTL((short) 561);
            ao.itemOptions.add(new Item.ItemOption(130, 0));
            quan.itemOptions.add(new Item.ItemOption(130, 0));
            gang.itemOptions.add(new Item.ItemOption(130, 0));
            giay.itemOptions.add(new Item.ItemOption(130, 0));
            nhan.itemOptions.add(new Item.ItemOption(130, 0));
            ao.itemOptions.add(new Item.ItemOption(142, 0));
            quan.itemOptions.add(new Item.ItemOption(142, 0));
            gang.itemOptions.add(new Item.ItemOption(142, 0));
            giay.itemOptions.add(new Item.ItemOption(142, 0));
            nhan.itemOptions.add(new Item.ItemOption(142, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thần namec ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }
    public void setgoddamtl(Player player) throws Exception {//nm
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1502 + i);
            Item ao = Util.ratiItemTL((short) 557);
            Item quan = Util.ratiItemTL((short) 558);
            Item gang = Util.ratiItemTL((short) 564);
            Item giay = Util.ratiItemTL((short) 565);
            Item nhan = Util.ratiItemTL((short) 561);
            ao.itemOptions.add(new Item.ItemOption(131, 0));
            quan.itemOptions.add(new Item.ItemOption(131, 0));
            gang.itemOptions.add(new Item.ItemOption(131, 0));
            giay.itemOptions.add(new Item.ItemOption(131, 0));
            nhan.itemOptions.add(new Item.ItemOption(131, 0));
            ao.itemOptions.add(new Item.ItemOption(143, 0));
            quan.itemOptions.add(new Item.ItemOption(143, 0));
            gang.itemOptions.add(new Item.ItemOption(143, 0));
            giay.itemOptions.add(new Item.ItemOption(143, 0));
            nhan.itemOptions.add(new Item.ItemOption(143, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thần namec ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    public void setsummontl(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1502 + i);
            Item ao = Util.ratiItemTL((short) 557);
            Item quan = Util.ratiItemTL((short) 558);
            Item gang = Util.ratiItemTL((short) 564);
            Item giay = Util.ratiItemTL((short) 565);
            Item nhan = Util.ratiItemTL((short) 561);
            ao.itemOptions.add(new Item.ItemOption(132, 0));
            quan.itemOptions.add(new Item.ItemOption(132, 0));
            gang.itemOptions.add(new Item.ItemOption(132, 0));
            giay.itemOptions.add(new Item.ItemOption(132, 0));
            nhan.itemOptions.add(new Item.ItemOption(132, 0));
            ao.itemOptions.add(new Item.ItemOption(144, 0));
            quan.itemOptions.add(new Item.ItemOption(144, 0));
            gang.itemOptions.add(new Item.ItemOption(144, 0));
            giay.itemOptions.add(new Item.ItemOption(144, 0));
            nhan.itemOptions.add(new Item.ItemOption(144, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thần namec ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    public void setgodgalicktl(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1502 + i);
            Item ao = Util.ratiItemTL((short) 559);
            Item quan = Util.ratiItemTL((short) 560);
            Item gang = Util.ratiItemTL((short) 566);
            Item giay = Util.ratiItemTL((short) 567);
            Item nhan = Util.ratiItemTL((short) 561);
            ao.itemOptions.add(new Item.ItemOption(133, 0));
            quan.itemOptions.add(new Item.ItemOption(133, 0));
            gang.itemOptions.add(new Item.ItemOption(133, 0));
            giay.itemOptions.add(new Item.ItemOption(133, 0));
            nhan.itemOptions.add(new Item.ItemOption(133, 0));
            ao.itemOptions.add(new Item.ItemOption(136, 0));
            quan.itemOptions.add(new Item.ItemOption(136, 0));
            gang.itemOptions.add(new Item.ItemOption(136, 0));
            giay.itemOptions.add(new Item.ItemOption(136, 0));
            nhan.itemOptions.add(new Item.ItemOption(136, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thần xayda ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }
    
    public void setmonkeytl(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1502 + i);
            Item ao = Util.ratiItemTL((short) 559);
            Item quan = Util.ratiItemTL((short) 560);
            Item gang = Util.ratiItemTL((short) 566);
            Item giay = Util.ratiItemTL((short) 567);
            Item nhan = Util.ratiItemTL((short) 561);
            ao.itemOptions.add(new Item.ItemOption(134, 0));
            quan.itemOptions.add(new Item.ItemOption(134, 0));
            gang.itemOptions.add(new Item.ItemOption(134, 0));
            giay.itemOptions.add(new Item.ItemOption(134, 0));
            nhan.itemOptions.add(new Item.ItemOption(134, 0));
            ao.itemOptions.add(new Item.ItemOption(137, 0));
            quan.itemOptions.add(new Item.ItemOption(137, 0));
            gang.itemOptions.add(new Item.ItemOption(137, 0));
            giay.itemOptions.add(new Item.ItemOption(137, 0));
            nhan.itemOptions.add(new Item.ItemOption(137, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thần xayda ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    public void setgodhptl(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1502 + i);
            Item ao = Util.ratiItemTL((short) 559);
            Item quan = Util.ratiItemTL((short) 560);
            Item gang = Util.ratiItemTL((short) 566);
            Item giay = Util.ratiItemTL((short) 567);
            Item nhan = Util.ratiItemTL((short) 561);
            ao.itemOptions.add(new Item.ItemOption(135, 0));
            quan.itemOptions.add(new Item.ItemOption(135, 0));
            gang.itemOptions.add(new Item.ItemOption(135, 0));
            giay.itemOptions.add(new Item.ItemOption(135, 0));
            nhan.itemOptions.add(new Item.ItemOption(135, 0));
            ao.itemOptions.add(new Item.ItemOption(138, 0));
            quan.itemOptions.add(new Item.ItemOption(138, 0));
            gang.itemOptions.add(new Item.ItemOption(138, 0));
            giay.itemOptions.add(new Item.ItemOption(138, 0));
            nhan.itemOptions.add(new Item.ItemOption(138, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thần xayda ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }
    
    //610 - bong hoa
    //Phụ kiện bó hoa 20/11
    public Item phuKien2011(boolean rating) {
        Item item = createItemSetKichHoat(954, 1);
        item.itemOptions.add(new Item.ItemOption(77, new Random().nextInt(5) + 5));
        item.itemOptions.add(new Item.ItemOption(103, new Random().nextInt(5) + 5));
        item.itemOptions.add(new Item.ItemOption(147, new Random().nextInt(5) + 5));
        if (Util.isTrue(1, 100)) {
            item.itemOptions.get(Util.nextInt(item.itemOptions.size() - 1)).param = 10;
        }
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    public Item vanBay2011(boolean rating) {
        Item item = createItemSetKichHoat(795, 1);
        item.itemOptions.add(new Item.ItemOption(89, 1));
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        if (Util.isTrue(950, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    public Item daBaoVe() {
        Item item = createItemSetKichHoat(987, 1);
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        return item;
    }

    public Item randomRac() {
        short[] racs = {20, 19, 18, 17};
        Item item = createItemSetKichHoat(racs[Util.nextInt(racs.length - 1)], 1);
        if (optionRac(item.template.id) != 0) {
            item.itemOptions.add(new Item.ItemOption(optionRac(item.template.id), 1));
        }
        return item;
    }

    public byte optionRac(short itemId) {
        switch (itemId) {
            case 220:
                return 71;
            case 221:
                return 70;
            case 222:
                return 69;
            case 224:
                return 67;
            case 223:
                return 68;
            default:
                return 0;
        }
    }
    public void settaiyoken(Player player) throws Exception {
        for (int i = 0 ; i < 1;i++){
        Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105+i);
        Item ao = ItemService.gI().otpts((short)1048);
        Item quan = ItemService.gI().otpts((short)1051);
        Item gang = ItemService.gI().otpts((short)1054);
        Item giay = ItemService.gI().otpts((short)1057);
        Item nhan = ItemService.gI().otpts((short)1060);
        ao.itemOptions.add(new Item.ItemOption(127,0));
        quan.itemOptions.add(new Item.ItemOption(127,0));
        gang.itemOptions.add(new Item.ItemOption(127,0));
        giay.itemOptions.add(new Item.ItemOption(127,0));
        nhan.itemOptions.add(new Item.ItemOption(127,0));
        ao.itemOptions.add(new Item.ItemOption(139,0));
        quan.itemOptions.add(new Item.ItemOption(139,0));
        gang.itemOptions.add(new Item.ItemOption(139,0));
        giay.itemOptions.add(new Item.ItemOption(139,0));
        nhan.itemOptions.add(new Item.ItemOption(139,0));
        ao.itemOptions.add(new Item.ItemOption(30,0));
        quan.itemOptions.add(new Item.ItemOption(30,0));
        gang.itemOptions.add(new Item.ItemOption(30,0));
        giay.itemOptions.add(new Item.ItemOption(30,0));
        nhan.itemOptions.add(new Item.ItemOption(30,0));
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
            InventoryServiceNew.gI().addItemBag(player, ao);
            InventoryServiceNew.gI().addItemBag(player, quan);
            InventoryServiceNew.gI().addItemBag(player, gang);
            InventoryServiceNew.gI().addItemBag(player, giay);
            InventoryServiceNew.gI().addItemBag(player, nhan);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }
    }
    
    public void setgenki(Player player) throws Exception {
        for (int i = 0 ; i < 1;i++){
        Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105+i);
        Item ao = ItemService.gI().otpts((short)1048);
        Item quan = ItemService.gI().otpts((short)1051);
        Item gang = ItemService.gI().otpts((short)1054);
        Item giay = ItemService.gI().otpts((short)1057);
        Item nhan = ItemService.gI().otpts((short)1060);
        ao.itemOptions.add(new Item.ItemOption(128,0));
        quan.itemOptions.add(new Item.ItemOption(128,0));
        gang.itemOptions.add(new Item.ItemOption(128,0));
        giay.itemOptions.add(new Item.ItemOption(128,0));
        nhan.itemOptions.add(new Item.ItemOption(128,0));
        ao.itemOptions.add(new Item.ItemOption(140,0));
        quan.itemOptions.add(new Item.ItemOption(140,0));
        gang.itemOptions.add(new Item.ItemOption(140,0));
        giay.itemOptions.add(new Item.ItemOption(140,0));
        nhan.itemOptions.add(new Item.ItemOption(140,0));
        ao.itemOptions.add(new Item.ItemOption(30,0));
        quan.itemOptions.add(new Item.ItemOption(30,0));
        gang.itemOptions.add(new Item.ItemOption(30,0));
        giay.itemOptions.add(new Item.ItemOption(30,0));
        nhan.itemOptions.add(new Item.ItemOption(30,0));
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
            InventoryServiceNew.gI().addItemBag(player, ao);
            InventoryServiceNew.gI().addItemBag(player, quan);
            InventoryServiceNew.gI().addItemBag(player, gang);
            InventoryServiceNew.gI().addItemBag(player, giay);
            InventoryServiceNew.gI().addItemBag(player, nhan);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }
    }
    
        public void setkamejoko(Player player) throws Exception {
        for (int i = 0 ; i < 1;i++){
        Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105+i);
        Item ao = ItemService.gI().otpts((short)1048);
        Item quan = ItemService.gI().otpts((short)1051);
        Item gang = ItemService.gI().otpts((short)1054);
        Item giay = ItemService.gI().otpts((short)1057);
        Item nhan = ItemService.gI().otpts((short)1060);
        ao.itemOptions.add(new Item.ItemOption(129,0));
        quan.itemOptions.add(new Item.ItemOption(129,0));
        gang.itemOptions.add(new Item.ItemOption(129,0));
        giay.itemOptions.add(new Item.ItemOption(129,0));
        nhan.itemOptions.add(new Item.ItemOption(129,0));
        ao.itemOptions.add(new Item.ItemOption(141,0));
        quan.itemOptions.add(new Item.ItemOption(141,0));
        gang.itemOptions.add(new Item.ItemOption(141,0));
        giay.itemOptions.add(new Item.ItemOption(141,0));
        nhan.itemOptions.add(new Item.ItemOption(141,0));
        ao.itemOptions.add(new Item.ItemOption(30,0));
        quan.itemOptions.add(new Item.ItemOption(30,0));
        gang.itemOptions.add(new Item.ItemOption(30,0));
        giay.itemOptions.add(new Item.ItemOption(30,0));
        nhan.itemOptions.add(new Item.ItemOption(30,0));
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
            InventoryServiceNew.gI().addItemBag(player, ao);
            InventoryServiceNew.gI().addItemBag(player, quan);
            InventoryServiceNew.gI().addItemBag(player, gang);
            InventoryServiceNew.gI().addItemBag(player, giay);
            InventoryServiceNew.gI().addItemBag(player, nhan);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }
    }
        
    public void setgodki(Player player) throws Exception {
        for (int i = 0 ; i < 1;i++){
        Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105+i);
        Item ao = ItemService.gI().otpts((short)1049);
        Item quan = ItemService.gI().otpts((short)1052);
        Item gang = ItemService.gI().otpts((short)1055);
        Item giay = ItemService.gI().otpts((short)1058);
        Item nhan = ItemService.gI().otpts((short)1061);
        ao.itemOptions.add(new Item.ItemOption(130,0));
        quan.itemOptions.add(new Item.ItemOption(130,0));
        gang.itemOptions.add(new Item.ItemOption(130,0));
        giay.itemOptions.add(new Item.ItemOption(130,0));
        nhan.itemOptions.add(new Item.ItemOption(130,0));
        ao.itemOptions.add(new Item.ItemOption(142,0));
        quan.itemOptions.add(new Item.ItemOption(142,0));
        gang.itemOptions.add(new Item.ItemOption(142,0));
        giay.itemOptions.add(new Item.ItemOption(142,0));
        nhan.itemOptions.add(new Item.ItemOption(142,0));
        ao.itemOptions.add(new Item.ItemOption(30,0));
        quan.itemOptions.add(new Item.ItemOption(30,0));
        gang.itemOptions.add(new Item.ItemOption(30,0));
        giay.itemOptions.add(new Item.ItemOption(30,0));
        nhan.itemOptions.add(new Item.ItemOption(30,0));
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
            InventoryServiceNew.gI().addItemBag(player, ao);
            InventoryServiceNew.gI().addItemBag(player, quan);
            InventoryServiceNew.gI().addItemBag(player, gang);
            InventoryServiceNew.gI().addItemBag(player, giay);
            InventoryServiceNew.gI().addItemBag(player, nhan);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }
    }
    
    public void setgoddam(Player player) throws Exception {
        for (int i = 0 ; i < 1;i++){
        Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105+i);
        Item ao = ItemService.gI().otpts((short)1049);
        Item quan = ItemService.gI().otpts((short)1052);
        Item gang = ItemService.gI().otpts((short)1055);
        Item giay = ItemService.gI().otpts((short)1058);
        Item nhan = ItemService.gI().otpts((short)1061);
        ao.itemOptions.add(new Item.ItemOption(131,0));
        quan.itemOptions.add(new Item.ItemOption(131,0));
        gang.itemOptions.add(new Item.ItemOption(131,0));
        giay.itemOptions.add(new Item.ItemOption(131,0));
        nhan.itemOptions.add(new Item.ItemOption(131,0));
        ao.itemOptions.add(new Item.ItemOption(143,0));
        quan.itemOptions.add(new Item.ItemOption(143,0));
        gang.itemOptions.add(new Item.ItemOption(143,0));
        giay.itemOptions.add(new Item.ItemOption(143,0));
        nhan.itemOptions.add(new Item.ItemOption(143,0));
        ao.itemOptions.add(new Item.ItemOption(30,0));
        quan.itemOptions.add(new Item.ItemOption(30,0));
        gang.itemOptions.add(new Item.ItemOption(30,0));
        giay.itemOptions.add(new Item.ItemOption(30,0));
        nhan.itemOptions.add(new Item.ItemOption(30,0));
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
            InventoryServiceNew.gI().addItemBag(player, ao);
            InventoryServiceNew.gI().addItemBag(player, quan);
            InventoryServiceNew.gI().addItemBag(player, gang);
            InventoryServiceNew.gI().addItemBag(player, giay);
            InventoryServiceNew.gI().addItemBag(player, nhan);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }
    }
    
    public void setsummon(Player player) throws Exception {
        for (int i = 0 ; i < 1;i++){
        Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105+i);
        Item ao = ItemService.gI().otpts((short)1049);
        Item quan = ItemService.gI().otpts((short)1052);
        Item gang = ItemService.gI().otpts((short)1055);
        Item giay = ItemService.gI().otpts((short)1058);
        Item nhan = ItemService.gI().otpts((short)1061);
        ao.itemOptions.add(new Item.ItemOption(132,0));
        quan.itemOptions.add(new Item.ItemOption(132,0));
        gang.itemOptions.add(new Item.ItemOption(132,0));
        giay.itemOptions.add(new Item.ItemOption(132,0));
        nhan.itemOptions.add(new Item.ItemOption(132,0));
        ao.itemOptions.add(new Item.ItemOption(144,0));
        quan.itemOptions.add(new Item.ItemOption(144,0));
        gang.itemOptions.add(new Item.ItemOption(144,0));
        giay.itemOptions.add(new Item.ItemOption(144,0));
        nhan.itemOptions.add(new Item.ItemOption(144,0));
        ao.itemOptions.add(new Item.ItemOption(30,0));
        quan.itemOptions.add(new Item.ItemOption(30,0));
        gang.itemOptions.add(new Item.ItemOption(30,0));
        giay.itemOptions.add(new Item.ItemOption(30,0));
        nhan.itemOptions.add(new Item.ItemOption(30,0));
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
            InventoryServiceNew.gI().addItemBag(player, ao);
            InventoryServiceNew.gI().addItemBag(player, quan);
            InventoryServiceNew.gI().addItemBag(player, gang);
            InventoryServiceNew.gI().addItemBag(player, giay);
            InventoryServiceNew.gI().addItemBag(player, nhan);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }
    }
    
        public void setgodgalick(Player player) throws Exception {
        for (int i = 0 ; i < 1;i++){
        Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105+i);
        Item ao = ItemService.gI().otpts((short)1050);
        Item quan = ItemService.gI().otpts((short)1053);
        Item gang = ItemService.gI().otpts((short)1056);
        Item giay = ItemService.gI().otpts((short)1059);
        Item nhan = ItemService.gI().otpts((short)1062);
        ao.itemOptions.add(new Item.ItemOption(133,0));
        quan.itemOptions.add(new Item.ItemOption(133,0));
        gang.itemOptions.add(new Item.ItemOption(133,0));
        giay.itemOptions.add(new Item.ItemOption(133,0));
        nhan.itemOptions.add(new Item.ItemOption(133,0));
        ao.itemOptions.add(new Item.ItemOption(136,0));
        quan.itemOptions.add(new Item.ItemOption(136,0));
        gang.itemOptions.add(new Item.ItemOption(136,0));
        giay.itemOptions.add(new Item.ItemOption(136,0));
        nhan.itemOptions.add(new Item.ItemOption(136,0));
        ao.itemOptions.add(new Item.ItemOption(30,0));
        quan.itemOptions.add(new Item.ItemOption(30,0));
        gang.itemOptions.add(new Item.ItemOption(30,0));
        giay.itemOptions.add(new Item.ItemOption(30,0));
        nhan.itemOptions.add(new Item.ItemOption(30,0));
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
            InventoryServiceNew.gI().addItemBag(player, ao);
            InventoryServiceNew.gI().addItemBag(player, quan);
            InventoryServiceNew.gI().addItemBag(player, gang);
            InventoryServiceNew.gI().addItemBag(player, giay);
            InventoryServiceNew.gI().addItemBag(player, nhan);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }
    }
        
    public void setmonkey(Player player) throws Exception {
        for (int i = 0 ; i < 1;i++){
        Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105+i);
        Item ao = ItemService.gI().otpts((short)1050);
        Item quan = ItemService.gI().otpts((short)1053);
        Item gang = ItemService.gI().otpts((short)1056);
        Item giay = ItemService.gI().otpts((short)1059);
        Item nhan = ItemService.gI().otpts((short)1062);
        ao.itemOptions.add(new Item.ItemOption(134,0));
        quan.itemOptions.add(new Item.ItemOption(134,0));
        gang.itemOptions.add(new Item.ItemOption(134,0));
        giay.itemOptions.add(new Item.ItemOption(134,0));
        nhan.itemOptions.add(new Item.ItemOption(134,0));
        ao.itemOptions.add(new Item.ItemOption(137,0));
        quan.itemOptions.add(new Item.ItemOption(137,0));
        gang.itemOptions.add(new Item.ItemOption(137,0));
        giay.itemOptions.add(new Item.ItemOption(137,0));
        nhan.itemOptions.add(new Item.ItemOption(137,0));
        ao.itemOptions.add(new Item.ItemOption(30,0));
        quan.itemOptions.add(new Item.ItemOption(30,0));
        gang.itemOptions.add(new Item.ItemOption(30,0));
        giay.itemOptions.add(new Item.ItemOption(30,0));
        nhan.itemOptions.add(new Item.ItemOption(30,0));
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
            InventoryServiceNew.gI().addItemBag(player, ao);
            InventoryServiceNew.gI().addItemBag(player, quan);
            InventoryServiceNew.gI().addItemBag(player, gang);
            InventoryServiceNew.gI().addItemBag(player, giay);
            InventoryServiceNew.gI().addItemBag(player, nhan);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }
    }
            
    public void setgodhp(Player player) throws Exception {
        for (int i = 0 ; i < 1;i++){
        Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1105+i);
        Item ao = ItemService.gI().otpts((short)1050);
        Item quan = ItemService.gI().otpts((short)1053);
        Item gang = ItemService.gI().otpts((short)1056);
        Item giay = ItemService.gI().otpts((short)1059);
        Item nhan = ItemService.gI().otpts((short)1062);
        ao.itemOptions.add(new Item.ItemOption(135,0));
        quan.itemOptions.add(new Item.ItemOption(135,0));
        gang.itemOptions.add(new Item.ItemOption(135,0));
        giay.itemOptions.add(new Item.ItemOption(135,0));
        nhan.itemOptions.add(new Item.ItemOption(135,0));
        ao.itemOptions.add(new Item.ItemOption(138,0));
        quan.itemOptions.add(new Item.ItemOption(138,0));
        gang.itemOptions.add(new Item.ItemOption(138,0));
        giay.itemOptions.add(new Item.ItemOption(138,0));
        nhan.itemOptions.add(new Item.ItemOption(138,0));
        ao.itemOptions.add(new Item.ItemOption(30,0));
        quan.itemOptions.add(new Item.ItemOption(30,0));
        gang.itemOptions.add(new Item.ItemOption(30,0));
        giay.itemOptions.add(new Item.ItemOption(30,0));
        nhan.itemOptions.add(new Item.ItemOption(30,0));
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
            InventoryServiceNew.gI().addItemBag(player, ao);
            InventoryServiceNew.gI().addItemBag(player, quan);
            InventoryServiceNew.gI().addItemBag(player, gang);
            InventoryServiceNew.gI().addItemBag(player, giay);
            InventoryServiceNew.gI().addItemBag(player, nhan);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }
    }
    public void openBoxCongThuc(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1) {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            return;
        }
        if (player.inventory.event < 200) {
            Service.gI().sendThongBao(player, "Bạn không đủ điểm");
            return;
        }
        Item item;
        if (player.gender == 0) {
          item = createItemSetKichHoat(1084, 1);
        }
        else if (player.gender == 1) {
          item = createItemSetKichHoat(1085, 1);
        }
        else {
          item = createItemSetKichHoat(1086, 1);
        }
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        player.inventory.event -= 200;
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
    }
public void openBoxdua(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1) {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            return;
        }
        Item item;
        item = createNewItem((short)569);
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
    }
    public void openBoxCt(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1) {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            return;
        }
        if (player.inventory.event < 999) {
            Service.gI().sendThongBao(player, "Bạn không đủ điểm");
            return;
        }
        Item item;
        if (Util.isTrue(10, 100)) {
          item = createItemSetKichHoat(Util.nextInt(1378,1380), 1);
          item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20,38)));
          item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20,28)));
          item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(20,25)));
          item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3,7)));
        }else{
          item = createItemSetKichHoat(1104, 1);
          item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20,38)));
          item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20,38)));
          item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(20,35)));
          item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3,7)));
        }
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        player.inventory.event -= 999;
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
    }
    public void openBoxitemc2(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1) {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            return;
        }
        if (player.inventory.event < 100) {
            Service.gI().sendThongBao(player, "Bạn không đủ điểm");
            return;
        }
        Item item;
        if (Util.isTrue(10, 100)) {
          item = createItemSetKichHoat(1099, 1);
        }else if(Util.isTrue(20, 100)){
          item = createItemSetKichHoat(1100, 1);
        }else if(Util.isTrue(30, 100)){
          item = createItemSetKichHoat(1101, 1);
        }else if(Util.isTrue(40, 100)){
          item = createItemSetKichHoat(1102, 1);
        }else{
          item = createItemSetKichHoat(1103, Util.nextInt(1,5));
        }
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        player.inventory.event -= 100;
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
    }
      public void openBoxitemnr(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1) {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            return;
        }
        if (player.inventory.event < 200) {
            Service.gI().sendThongBao(player, "Bạn không đủ điểm");
            return;
        }
        Item item;
        if (Util.isTrue(5, 100)) {
        item = createItemSetKichHoat(Util.nextInt(14, 15), 1);
        } else {
        item = createItemSetKichHoat(Util.nextInt(16, 18), 1);
        }
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        player.inventory.event -= 200;
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
    }
    public void openBoxVip(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1) {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang");
            return;
        }
        if (player.inventory.event < 3000) {
            Service.gI().sendThongBao(player, "Bạn không đủ bông...");
            return;
        }
        Item item;
        if (Util.isTrue(45, 100)) {
            item = caitrang2011(false);
        } else {
            item = phuKien2011(false);
        }
        short[] icon = new short[2];
        icon[0] = 6983;
        icon[1] = item.template.iconID;
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        player.inventory.event -= 3000;
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    }

    public void giaobong(Player player, int quantity) {
        if (quantity > 100) return;
        try {
            Item itemUse = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 610);
            if (itemUse.quantity < quantity) {
                Service.gI().sendThongBao(player, "Bạn không đủ dưa hấu...");
                return;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, quantity);
            Item item = createItemSetKichHoat(736, (quantity / 100));
            item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được x" + (quantity / 100) + " " + item.template.name);
        } catch (Exception e) {
            Service.gI().sendThongBao(player, "Bạn không đủ dưa hấu...");
        }
    }

    public Item PK_WC(int itemId) {
        Item phukien = createItemSetKichHoat(itemId, 1);
        int co = 983;
        int cup = 982;
        int bong = 966;
        if (cup == itemId) {
            phukien.itemOptions.add(new Item.ItemOption(77, new Random().nextInt(6) + 5)); // hp 5-10%
        }
        if (co == itemId) {
            phukien.itemOptions.add(new Item.ItemOption(103, new Random().nextInt(6) + 5)); // ki 5-10%
        }
        if (bong == itemId) {
            phukien.itemOptions.add(new Item.ItemOption(50, new Random().nextInt(6) + 5)); // sd 5- 10%
        }
//        phukien.itemOptions.add(new Item.ItemOption(192, 1));//WORLDCUP
//        phukien.itemOptions.add(new Item.ItemOption(193, 1));//(2 món kích hoạt ....)
        if (Util.isTrue(99, 100)) {// tỉ lệ ra hsd
            phukien.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(2) + 1));//hsd
        }
        return phukien;
    }

    //Cải trang Gohan WC
    public Item CT_WC(boolean rating) {
        Item caitrang = createItemSetKichHoat(883, 1);
        caitrang.itemOptions.add(new Item.ItemOption(77, 30));// hp 30%
        caitrang.itemOptions.add(new Item.ItemOption(103, 15));// ki 15%
        caitrang.itemOptions.add(new Item.ItemOption(50, 20));// sd 20%
//        caitrang.itemOptions.add(new Item.ItemOption(192, 1));//WORLDCUP
//        caitrang.itemOptions.add(new Item.ItemOption(193, 1));//(2 món kích hoạt ....)
        if (Util.isTrue(99, 100) && rating) {// tỉ lệ ra hsd
            caitrang.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(2) + 1));//hsd
        }
        return caitrang;
    }

    public void openDTS(Player player) {
        //check sl đồ tl, đồ hd
        if (player.combineNew.itemsCombine.stream().filter(item -> item.template.id >= 555 && item.template.id <= 567).count() < 1) {
            Service.gI().sendThongBao(player, "Thiếu đồ thần linh");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.template.id >= 650 && item.template.id <= 662).count() < 2) {
            Service.gI().sendThongBao(player, "Thiếu đồ hủy diệt");
            return;
        }
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.gI().sendThongBao(player, "Thiếu đồ");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            Item itemTL = player.combineNew.itemsCombine.stream().filter(item -> item.template.id >= 555 && item.template.id <= 567).findFirst().get();
            List<Item> itemHDs = player.combineNew.itemsCombine.stream().filter(item -> item.template.id >= 650 && item.template.id <= 662).collect(Collectors.toList());
            short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

            Item itemTS = DoThienSu(itemIds[player.gender][itemTL.template.type], player.gender);
            InventoryServiceNew.gI().addItemBag(player, itemTS);

            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTL, 1);
            itemHDs.forEach(item -> InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1));

            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + itemTS.template.name);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public Item DoThienSu(int itemId, int gender) {
        Item dots = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        List<Integer> nhan = Arrays.asList(1060, 1061, 1062);
        //áo
        if (ao.contains(itemId)) {
            dots.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(gender == 2, new Random().nextInt(1201) + 2800))); // áo từ 2800-4000 giáp
        }
        //quần
        if (Util.isTrue(80, 100)) {
            if (quan.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(11) + 120))); // hp 120k-130k
            }
        } else {
            if (quan.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(21) + 130))); // hp 130-150k 15%
            }
        }
        //găng
        if (Util.isTrue(80, 100)) {
            if (gang.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(651) + 11000))); // 9350-10000
            }
        } else {
            if (gang.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(1001) + 11200))); // gang 15% 10-11k -xayda 12k1
            }
        }
        //giày
        if (Util.isTrue(80, 100)) {
            if (giay.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(21) + 90))); // ki 90-110k
            }
        } else {
            if (giay.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(21) + 110))); // ki 110-130k
            }
        }

        if (nhan.contains(itemId)) {
            dots.itemOptions.add(new Item.ItemOption(14, Util.highlightsItem(gender == 1, new Random().nextInt(3) + 18))); // nhẫn 18-20%
        }
        dots.itemOptions.add(new Item.ItemOption(21, 120));
        dots.itemOptions.add(new Item.ItemOption(30, 1));
        return dots;
    }

    public List<Item.ItemOption> getListOptionItemShop(short id) {
        List<Item.ItemOption> list = new ArrayList<>();
        Manager.SHOPS.forEach(shop -> shop.tabShops.forEach(tabShop -> tabShop.itemShops.forEach(itemShop -> {
            if (itemShop.temp.id == id && list.size() == 0) {
                list.addAll(itemShop.options);
            }
        })));
        return list;
    }
}
