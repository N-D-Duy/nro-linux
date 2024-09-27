package ServerData.Services;

import ServerData.Services.Giftcode.Giftcode;
import ServerData.Services.Giftcode.GiftcodeManager;
import Server.Data.Consts.ConstPlayer;
import com.girlkun.database.GirlkunDB;
import ServerData.Models.Item.Item;
import ServerData.Models.Item.Item.ItemOption;
import ServerData.Models.Map.ListMap.NgocRongSaoDen;
import ServerData.Models.NPC.BillEgg;
import ServerData.Models.Player.Inventory;
import ServerData.Models.Player.Pet;
import ServerData.Models.Player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.result.GirlkunResultSet;
import ServerData.Utils.Util;
import java.time.Instant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class InventoryServiceNew {

    private static InventoryServiceNew I;

    public static InventoryServiceNew gI() {
        if (InventoryServiceNew.I == null) {
            InventoryServiceNew.I = new InventoryServiceNew();
        }
        return InventoryServiceNew.I;
    }

    //for giftcode
    public void addItemGiftCodeToPlayer(Player p, Giftcode giftcode, String code) throws Exception {
        Set<Integer> keySet = giftcode.detail.keySet();
        String textGift = "|7|Giftcode: " + code + "\n" + "|6|Phần Thưởng Của Bạn Là :\b";
        GirlkunResultSet rs = GirlkunDB.executeQuery("SELECT * FROM `giftcode` WHERE `code` = '" + code + "';");
        if (rs != null && rs.first()) {
            for (Integer key : keySet) {
                int idItem = key;
                int quantity = giftcode.detail.get(key);
                switch (idItem) {
                    case -1:
                        p.inventory.gold = Math.min(p.inventory.gold + (long) quantity, 2000000000L);
                        textGift += quantity + " vàng\b";
                        break;
                    case -2:
                        p.inventory.gem = Math.min(p.inventory.gem + quantity, 200000000);
                        textGift += quantity + " ngọc\b";
                        break;
                    case -3:
                        p.inventory.ruby = Math.min(p.inventory.ruby + quantity, 200000000);
                        textGift += quantity + " ngọc khóa\b";
                        break;
                    case -10:
                        p.inventory.ruby = Math.min(p.inventory.ruby + quantity, 200000000);
                        textGift += quantity + " ngọc khóa\b";
                        break;
                    case -6:
                        p.titleitem = true;
                        p.dhtang1 = true;
                        p.dhtang2 = true;
                        Service.gI().point(p);
                        Service.gI().sendTitle(p, "VECHAI");
                        Service.gI().sendTitle(p, "FANCUNG");
                        textGift += "x1 Danh Hiệu Ve Chai\nx1 Danh Hiệu Fan Cứng\b";
                        break;
                    case -7:
                        p.titlett = true;
                        p.usedh1 = true;
                        p.usedh2 = true;
                        p.usedh3 = true;
                        p.timedh1 = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 1);
                        p.timedh2 = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 1);
                        p.timedh3 = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 1);
                        Service.gI().point(p);
                        Service.gI().sendTitle(p, "DH1");
                        Service.gI().sendTitle(p, "DH2");
                        Service.gI().sendTitle(p, "DH3");
                        textGift += "x1 Danh Hiệu Bất Bại\nx1 Danh Hiệu Đại Thần\nx1 Danh Hiệu Trùm Cuối\b";
                        break;
                    default:
                        Item itemGiftTemplate = ItemService.gI().createNewItem((short) idItem);
                        if (itemGiftTemplate != null) {
                            Item itemGift = new Item((short) idItem);
                            if (itemGift.template.type == 12 || itemGift.template.type >= 0 && itemGift.template.type <= 5) {
                                if (itemGift.template.id == 457) {
                                    itemGift.itemOptions.add(new ItemOption(30, 0));
                                    addItemBag(p, itemGift);
                                    sendItemBags(p);
                                } else {
                                    itemGift.itemOptions = giftcode.option;
                                    itemGift.quantity = quantity;
                                    addItemBag(p, itemGift);
                                    sendItemBags(p);
                                }
                            } else {
                                itemGift.itemOptions = giftcode.option;
                                itemGift.quantity = quantity;
                                addItemBag(p, itemGift);
                                sendItemBags(p);
                            }
                            textGift += "x" + quantity + " " + itemGift.template.name + "\b";
                            GiftcodeManager.gI().checkUseGiftCode((int) p.id, code);
                            int trucount = rs.getInt("count_left") - 1;
                            GirlkunDB.executeUpdate("UPDATE `giftcode` SET `count_left` = '" + trucount + "' WHERE `code` = '" + code + "' LIMIT 1;");
                        }
                        break;
                }
            }
            String xuatsql = "(" + p.getSession().userId + ", '" + p.id + "', '" + code + "', '" + Util.toDateString(Date.from(Instant.now())) + "');";
            GirlkunDB.executeUpdate("INSERT INTO `giftcode_save` (`id`,`player_id`,`code_da_nhap`,`tgian_nhap`) VALUES " + xuatsql);
            //sendItemBags(p);
            Service.gI().sendThongBaoFromAdmin(p, textGift);
        }
    }

    private void __________________Tìm_kiếm_item_____________________________() {
        //**********************************************************************
    }

    public Item finditemnguyenlieuKeo(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2013) && item.quantity >= 10) {
                return item;
            }
        }
        return null;
    }

    public Item finditemnguyenlieuBanh(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2014) && item.quantity >= 10) {
                return item;
            }
        }
        return null;
    }

    public Item finditemnguyenlieuBingo(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2015) && item.quantity >= 10) {
                return item;
            }
        }
        return null;
    }

    public Item finditemnguyenlieuGiokeo(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2016) && item.quantity >= 3) {
                return item;
            }
        }
        return null;
    }

    public Item finditemnguyenlieuVe(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2018) && item.quantity >= 3) {
                return item;
            }
        }
        return null;
    }

    public Item finditemnguyenlieuHopmaquy(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2017) && item.quantity >= 3) {
                return item;
            }
        }
        return null;
    }

    public Item finditemBongHoa(Player player, int soluong) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 589) && item.quantity >= soluong) {
                return item;
            }
        }
        return null;
    }

    public boolean finditemWoodChest(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 570) {
                return false;
            }
        }
        for (Item item : player.inventory.itemsBox) {
            if (item.isNotNullItem() && item.template.id == 570) {
                return false;
            }
        }
        return true;
    }

    public Item finditemKeoGiangSinh(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 2026) {
                return item;
            }
        }
        return null;
    }

    public Item findItem(List<Item> list, int tempId) {
        try {
            for (Item item : list) {
                if (item.isNotNullItem() && item.template.id == tempId) {
                    return item;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public Item findItemBody(Player player, int tempId) {
        return this.findItem(player.inventory.itemsBody, tempId);
    }

    public Item findItemBag(Player player, int tempId) {
        return this.findItem(player.inventory.itemsBag, tempId);
    }

    public Item findItemBox(Player player, int tempId) {
        return this.findItem(player.inventory.itemsBox, tempId);
    }

    public boolean isExistItem(List<Item> list, int tempId) {
        try {
            this.findItem(list, tempId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isExistItemBody(Player player, int tempId) {
        return this.isExistItem(player.inventory.itemsBody, tempId);
    }

    public boolean isExistItemBag(Player player, int tempId) {
        return this.isExistItem(player.inventory.itemsBag, tempId);
    }

    public boolean isExistItemBox(Player player, int tempId) {
        return this.isExistItem(player.inventory.itemsBox, tempId);
    }

    private void __________________Sao_chép_danh_sách_item__________________() {
        //**********************************************************************
    }

    public List<Item> copyList(List<Item> items) {
        List<Item> list = new ArrayList<>();
        for (Item item : items) {
            list.add(ItemService.gI().copyItem(item));
        }
        return list;
    }

    public List<Item> copyItemsBody(Player player) {
        return copyList(player.inventory.itemsBody);
    }

    public List<Item> copyItemsBag(Player player) {
        return copyList(player.inventory.itemsBag);
    }

    public List<Item> copyItemsBox(Player player) {
        return copyList(player.inventory.itemsBox);
    }

    private void __________________Vứt_bỏ_item______________________________() {
        //**********************************************************************
    }

    public void throwItem(Player player, int where, int index) {
        Item itemThrow = null;
        if (where == 0) {
            itemThrow = player.inventory.itemsBody.get(index);
            removeItemBody(player, index);
            sendItemBody(player);
            Service.gI().Send_Caitrang(player);
        } else if (where == 1) {
            itemThrow = player.inventory.itemsBag.get(index);
            if (itemThrow.template.id != 457) {
                removeItemBag(player, index);
                sortItems(player.inventory.itemsBag);
                sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Vật phẩm này rất có giá trị, hãy giữ lại");
            }
        }
        if (itemThrow == null) {
            return;
        }
    }

    private void __________________Xoá_bỏ_item______________________________() {
        //**********************************************************************
    }

    public void removeItem(List<Item> items, int index) {
        Item item = ItemService.gI().createItemNull();
        items.set(index, item);
    }

    public void removeItem(List<Item> items, Item item) {
        if (item == null) {
            return;
        }
        Item it = ItemService.gI().createItemNull();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(item)) {
                items.set(i, it);
                item.dispose();
                break;
            }
        }
    }

    public void removeItemBag(Player player, int index) {
        this.removeItem(player.inventory.itemsBag, index);
    }

    public void removeItemBag(Player player, Item item) {
        this.removeItem(player.inventory.itemsBag, item);
    }

    public void removeItemBody(Player player, int index) {
        this.removeItem(player.inventory.itemsBody, index);
    }

    public void removeItemPetBody(Player player, int index) {
        this.removeItemBody(player.pet, index);
    }

    public void removeItemBox(Player player, int index) {
        this.removeItem(player.inventory.itemsBox, index);
    }

    private void __________________Giảm_số_lượng_item_______________________() {
        //**********************************************************************
    }

    public void subQuantityItemsBag(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBag, item, quantity);
    }

    public void subQuantityItemsBody(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBody, item, quantity);
    }

    public void subQuantityItemsBox(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBox, item, quantity);
    }

    public void subQuantityItem(List<Item> items, Item item, int quantity) {
        if (item != null) {
            for (Item it : items) {
                if (item.equals(it)) {
                    it.quantity -= quantity;
                    if (it.quantity <= 0) {
                        this.removeItem(items, item);
                    }
                    break;
                }
            }
        }
    }

    private void __________________Tăng_số_lượng_item_______________________() {
        //**********************************************************************
    }

    public void addQuantityItemsBag(Player player, Item item, int quantity) {
        addQuantityItem(player.inventory.itemsBag, item, quantity);
    }

    public void addQuantityItemsBody(Player player, Item item, int quantity) {
        addQuantityItem(player.inventory.itemsBody, item, quantity);
    }

    public void addQuantityItemsBox(Player player, Item item, int quantity) {
        addQuantityItem(player.inventory.itemsBox, item, quantity);
    }

    public void addQuantityItem(List<Item> items, Item item, int quantity) {
        if (item != null) {
            for (Item it : items) {
                if (item.equals(it)) {
                    it.quantity += quantity;
                    if (it.quantity <= 0) {
                        this.removeItem(items, item);
                    }
                    break;
                }
            }
        }
    }

    private void __________________Sắp_xếp_danh_sách_item___________________() {
        //**********************************************************************
    }

    public void sortItems(List<Item> list) {
        int first = -1;
        int last = -1;
        Item tempFirst = null;
        Item tempLast = null;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isNotNullItem()) {
                first = i;
                tempFirst = list.get(i);
                break;
            }
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).isNotNullItem()) {
                last = i;
                tempLast = list.get(i);
                break;
            }
        }
        if (first != -1 && last != -1 && first < last) {
            list.set(first, tempLast);
            list.set(last, tempFirst);
            sortItems(list);
        }
    }

    private void __________________Thao_tác_tháo_mặc_item___________________() {
        //**********************************************************************
    }

    private Item putItemBag(Player player, Item item) {
        for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
            if (!player.inventory.itemsBag.get(i).isNotNullItem()) {
                player.inventory.itemsBag.set(i, item);
                Item sItem = ItemService.gI().createItemNull();
                return sItem;
            }
        }
        return item;
    }

    private Item putItemBox(Player player, Item item) {
        for (int i = 0; i < player.inventory.itemsBox.size(); i++) {
            if (!player.inventory.itemsBox.get(i).isNotNullItem()) {
                player.inventory.itemsBox.set(i, item);
                Item sItem = ItemService.gI().createItemNull();
                return sItem;
            }
        }
        return item;
    }

    private Item putItemBody(Player player, Item item) {
        Item sItem = item;
        if (!item.isNotNullItem()) {
            return sItem;
        }
        switch (item.template.type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 32:
            case 23:
            case 24:
            case 11:
            case 75:
            case 74:
            case 72:
            case 27:
            case 36:
                break;
            default:
                Service.gI().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Trang bị không phù hợp!");
                return sItem;
        }
        if (item.template.gender < 3 && item.template.gender != player.gender) {
            Service.gI().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Trang bị không phù hợp!");
            return sItem;
        }
        long powerRequire = item.template.strRequire;
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 21) {
                powerRequire = io.param * 1000000000L;
                break;
            }
        }
        if (player.nPoint.power < powerRequire) {
            Service.gI().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Sức mạnh không đủ yêu cầu!");
            return sItem;
        }
        int index = -1;
        switch (item.template.type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                index = item.template.type;
                break;
            case 32:
                index = 6;

                break;
            case 23:
            case 24:
                if (player.isPet) {
                    index = 9;
                }
                index = 9;
                break;
            case 11:
                index = 8;
                if (player.isPet) {
                    index = 7;
                }
                break;
            case 75:
            case 72:

                index = 10;
                if (player.isPet) {
                    index = 8;
                }
                break;
            case 27:
                index = 7;
                if (player.isPet) {
                    return sItem;
                }
                break;
            case 74:
                index = 11;
                break;
            case 36:
                if (player.isPet) {
                    index = 10;
                }
                index = 12;
                break;
        }
        sItem = player.inventory.itemsBody.get(index);
        player.inventory.itemsBody.set(index, item);
      if (player.isPl()) {
            Service.gI().player(player);
            player.zone.load_Another_To_Me(player);
           player.zone.load_Me_To_Another(player);
        } 
        return sItem;
    }

    public void itemBagToBody(Player player, int index) {
        Item item = player.inventory.itemsBag.get(index);
        Item pettt = player.inventory.itemsBody.get(7);
        if (item.isNotNullItem()) {
            player.inventory.itemsBag.set(index, putItemBody(player, item));
            if (item.template.id > 1299 && item.template.id < 1309) {
                Service.gI().removeTitle(player);
                Service.gI().sendFoot(player, item.template.id);
            }
            if (item.template.type == 36) {
                Service.gI().removeTitle(player);
                Service.gI().sendTitle(player, item.template.part);
            }
            if (player.inventory.itemsBody.get(10).isNotNullItem()) {
                Service.getInstance().sendPetFollow(player, (short) (player.inventory.itemsBody.get(10).template.iconID - 1));
            }
            if (pettt != null && player.newpet != null && item.template.type == 27) {
                ChangeMapService.gI().exitMap(player.newpet);
                player.newpet.dispose();
                player.newpet = null;
            }
            if (player.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
                if (player.inventory.itemsBody.get(5).template.id != 1378 & player.inventory.itemsBody.get(5).template.id != 1379) {
                    player.pet.unFusion2();
                }
            }
            sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().Send_Caitrang(player);
            Service.getInstance().sendFlagBag(player);
            Service.getInstance().point(player);
        }
    }

    public void itemBodyToBag(Player player, int index) {
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            if (index == 10) {
                Service.getInstance().sendPetFollow(player, (short) 0);
            }
            if (player.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA && index == 5 && item.template.id >= 1378 & item.template.id <= 1379) {
                player.pet.unFusion2();
            }
            if (index == 7) {
                if (player.newpet != null) {
                    ChangeMapService.gI().exitMap(player.newpet);
                    player.newpet.dispose();
                    player.newpet = null;
                }
            }
            if (item.template.id > 1299 && item.template.id < 1309) {
                Service.gI().removeTitle(player);
                Service.gI().sendFoot(player, item.template.id);
            }
            player.inventory.itemsBody.set(index, putItemBag(player, item));
         sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().player(player);
            player.zone.load_Me_To_Another(player);
            player.zone.load_Another_To_Me(player);
            Service.getInstance().sendFlagBag(player);
            Service.getInstance().Send_Caitrang(player);
            Service.getInstance().point(player);
    }
    }

    public void itemBagToPetBody(Player player, int index) {
        Item item = player.inventory.itemsBag.get(index);
        if (player.pet != null && player.pet.nPoint.power >= 1500000) {
//            if (item.template.id != 691 && item.template.id != 692 && item.template.id != 693 
//                && item.template.id != 860 && item.template.id != 1436 && item.template.id != 954
//                && item.template.id != 955) {
            if (item.isNotNullItem()) {
                Item itemSwap = putItemBody(player.pet, item);
                player.inventory.itemsBag.set(index, itemSwap);
//                 if (player.pet.inventory.itemsBody.get(8).isNotNullItem()) {
//                Service.getInstance().sendPetFollow(player.pet, (short) (player.pet.inventory.itemsBody.get(8).template.iconID - 1));
//            }
                sendItemBags(player);
                sendItemBody(player);
                Service.getInstance().sendFlagBagPet(player.pet);
                Service.getInstance().Send_Caitrang(player.pet);
                Service.getInstance().Send_Caitrang(player);
                if (!itemSwap.equals(item)) {
                    Service.getInstance().point(player);
                    Service.getInstance().showInfoPet(player);
                }
            }
        } else {
            Service.getInstance().sendThongBaoOK(player, "Đệ Cần Đạt 1tr5 Sức Mạnh và Trang Bị Không Phù Hợp Cho Đệ Tử");
        }
//        } else {
//            Service.getInstance().sendThongBaoOK(player, "Đệ Cần Đạt 1tr5 Sức Mạnh");
//        }
    }

    public void itemPetBodyToBag(Player player, int index) {
        Item item = player.pet.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
//            if (index == 8) {
//                Service.getInstance().sendPetFollow(player.pet, (short) 0);
//            }
            player.pet.inventory.itemsBody.set(index, putItemBag(player, item));
            sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().sendFlagBagPet(player.pet);
            Service.getInstance().Send_Caitrang(player.pet);
            Service.getInstance().Send_Caitrang(player);
            Service.getInstance().point(player);
            Service.getInstance().showInfoPet(player);
        }
    }

    public void itemBoxToBodyOrBag(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBox.size()) {
            System.out.println("Invalid index: " + index); // Or handle this case as needed
            return;
        }
        Item item = player.inventory.itemsBox.get(index);
        if (item.isNotNullItem()) {
            boolean done = false;
            if (item.template.type >= 0 && item.template.type <= 5 || item.template.type == 32) {
                Item itemBody = player.inventory.itemsBody.get(item.template.type == 32 ? 6 : item.template.type);
                if (!itemBody.isNotNullItem()) {
                    if (item.template.gender == player.gender || item.template.gender == 3) {
                        long powerRequire = item.template.strRequire;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 21) {
                                powerRequire = io.param * 1000000000L;
                                break;
                            }
                        }
                        if (powerRequire <= player.nPoint.power) {
                            player.inventory.itemsBody.set(item.template.type == 32 ? 6 : item.template.type, item);
                            player.inventory.itemsBox.set(index, itemBody);
                            done = true;

                            sendItemBody(player);
                            Service.gI().Send_Caitrang(player);
                            Service.gI().point(player);
                        }
                    }
                }
            }
            if (!done) {
                if (addItemBag(player, item)) {
                    if (item.quantity == 0) {
                        Item sItem = ItemService.gI().createItemNull();
                        player.inventory.itemsBox.set(index, sItem);
                    }
                    sendItemBags(player);
                }
            }
            sendItemBox(player);
        }
    }

    public void itemBagToBox(Player player, int index) {
        Item item = player.inventory.itemsBag.get(index);
        if (item != null) {
            if (item.template.id == 457 || item.template.id == 674 || item.template.id == 14 || item.template.id == 15 || item.template.id == 16 || item.template.id == 17 || item.template.id == 381 || item.template.id == 382 || item.template.id == 383 || item.template.id == 384 || item.template.id == 1099 || item.template.id == 1100 || item.template.id == 1101 || item.template.id == 1102 || item.template.id == 1384 || item.template.id == 1385|| item.template.id == 1412|| item.template.id == 1413|| item.template.id == 1414|| item.template.id == 1415|| item.template.id == 1416|| item.template.id == 220|| item.template.id == 221|| item.template.id == 222|| item.template.id == 223|| item.template.id == 224|| item.template.id == 1503|| item.template.id == 1504) {
                Service.gI().sendThongBao(player, "Không thể cất vào rương");
                return;
            }
            if (addItemBox(player, item)) {
                if (item.quantity == 0) {
                    Item sItem = ItemService.gI().createItemNull();
                    player.inventory.itemsBag.set(index, sItem);
                }
                sortItems(player.inventory.itemsBag);
                sendItemBags(player);
                sendItemBox(player);
            }
        }
    }

    public void itemBodyToBox(Player player, int index) {
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            player.inventory.itemsBody.set(index, putItemBox(player, item));
            sortItems(player.inventory.itemsBag);
            sendItemBody(player);
            sendItemBox(player);
            Service.gI().Send_Caitrang(player);
            sendItemBody(player);
            Service.gI().point(player);
        }
    }

    private void __________________Gửi_danh_sách_item_cho_người_chơi________() {
        //**********************************************************************
    }

    public void sendItemBags(Player player) {
        sortItems(player.inventory.itemsBag);
        Message msg;
        try {
            msg = new Message(-36);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.inventory.itemsBag.size());
            for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
                Item item = player.inventory.itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    continue;
                }
                msg.writer().writeShort(item.template.id);
                msg.writer().writeInt(item.quantity);
                msg.writer().writeUTF(item.getInfo());
                msg.writer().writeUTF(item.getContent());
                msg.writer().writeByte(item.itemOptions.size()); //options
                for (int j = 0; j < item.itemOptions.size(); j++) {
                    msg.writer().writeByte(item.itemOptions.get(j).optionTemplate.id);
                    msg.writer().writeShort(item.itemOptions.get(j).param);
                }
            }

            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendItemBody(Player player) {
        Message msg;
        try {
            msg = new Message(-37);
            msg.writer().writeByte(0);
            msg.writer().writeShort(player.getHead());
            msg.writer().writeByte(player.inventory.itemsBody.size());
            for (Item item : player.inventory.itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<Item.ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (Item.ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
        Service.gI().Send_Caitrang(player);
    }

    public void sendItemBox(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.inventory.itemsBox.size());
            for (Item it : player.inventory.itemsBox) {
                msg.writer().writeShort(it.isNotNullItem() ? it.template.id : -1);
                if (it.isNotNullItem()) {
                    msg.writer().writeInt(it.quantity);
                    msg.writer().writeUTF(it.getInfo());
                    msg.writer().writeUTF(it.getContent());
                    msg.writer().writeByte(it.itemOptions.size());
                    for (Item.ItemOption io : it.itemOptions) {
                        msg.writer().writeByte(io.optionTemplate.id);
                        msg.writer().writeShort(io.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
        this.openBox(player);
    }

    public void openBox(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void __________________Thêm_vật_phẩm_vào_danh_sách______________() {
        //**********************************************************************
    }

    private boolean addItemSpecial(Player player, Item item) {
        //bùa
        if (item.template.type == 13) {
            int min = 0;
            try {
                String tagShopBua = player.iDMark.getShopOpen().tagName;
                if (tagShopBua.equals("BUA_1H")) {
                    min = 60;
                } else if (tagShopBua.equals("BUA_8H")) {
                    min = 60 * 8;
                } else if (tagShopBua.equals("BUA_1M")) {
                    min = 60 * 24 * 30;
                }
            } catch (Exception e) {
            }
            player.charms.addTimeCharms(item.template.id, min);
            return true;
        }

        switch (item.template.id) {
//            case 569: //dưa hấu
//                if (player.duahau == null) {
//                    duahau.createduahau(player);
//                }
//                return true;
            case 2027: //Dưa hấu
                if (player.billEgg == null) {
                    BillEgg.createBillEgg(player);
                }
                return true;
            case 453: //tàu tennis
                player.haveTennisSpaceShip = true;
                return true;
            case 74: //đùi gà nướng
                player.nPoint.setFullHpMp();
                PlayerService.gI().sendInfoHpMp(player);
                return true;
        }
        return false;
    }

    public boolean addItemBag(Player player, Item item) {
        //ngọc rồng đen
        if (ItemMapService.gI().isBlackBall(item.template.id)) {
            return NgocRongSaoDen.gI().pickBlackBall(player, item);
        }
        if (addItemSpecial(player, item)) {
            return true;
        }
        //gold, gem, ruby
        switch (item.template.type) {
            case 9:
                if (player.inventory.gold + item.quantity <= this.getGodMax(player)) {
                    player.inventory.gold += item.quantity;
                    Service.gI().sendMoney(player);
                    return true;
                } else {
                    Service.gI().sendThongBao(player, "Vàng sau khi nhặt quá giới hạn cho phép");
                    return false;
                }
            case 10:
                player.inventory.gem += item.quantity;
                Service.gI().sendMoney(player);
                return true;
            case 34:
                player.inventory.ruby += item.quantity;
                Service.gI().sendMoney(player);
                return true;
        }
        //mở rộng hành trang - rương đồ
        switch (item.template.id) {
            case 517:
                if (player.inventory.itemsBag.size() < Inventory.MAX_ITEMS_BAG) {
                    player.inventory.itemsBag.add(ItemService.gI().createItemNull());
                    return true;
                } else {
                    Service.gI().sendThongBao(player, "Rương đồ của bạn đã đạt tối đa");
                    return false;
                }
            case 518:
                if (player.inventory.itemsBox.size() < Inventory.MAX_ITEMS_BOX) {
                    player.inventory.itemsBox.add(ItemService.gI().createItemNull());
                    return true;
                } else {
                    Service.gI().sendThongBao(player, "Rương đồ của bạn đã đạt tối đa");
                    break;
                }
        }
        return addItemList(player.inventory.itemsBag, item);
    }

    public boolean addItemBox(Player player, Item item) {
        return addItemList(player.inventory.itemsBox, item);
    }

    public boolean addItemList(List<Item> items, Item itemAdd) {
        //nếu item ko có option, add option rỗng vào
        if (itemAdd.itemOptions.isEmpty()) {
            itemAdd.itemOptions.add(new Item.ItemOption(73, 0));
        }
        //item cộng thêm chỉ số param: tự động luyện tập
        int[] idParam = isItemIncrementalOption(itemAdd);
        if (idParam[0] != -1) {
            for (Item it : items) {
                if (it.isNotNullItem() && it.template.id == itemAdd.template.id) {
                    for (Item.ItemOption io : it.itemOptions) {
                        if (io.optionTemplate.id == idParam[0]) {
                            io.param += idParam[1];
                        }
                    }
                    return true;
                }
            }
        }
        //item tăng số lượng
        if (itemAdd.template.isUpToUp) {
            for (Item it : items) {
                if (!it.isNotNullItem() || it.template.id != itemAdd.template.id) {
                    continue;
                }
                //457-thỏi vàng; 590-bí kiếp
                if (itemAdd.template.id == 457
                        || itemAdd.template.id == 590
                        || itemAdd.template.id == 610
                        || itemAdd.template.type == 14
                        || itemAdd.template.id == 537
                        || itemAdd.template.id == 538
                        || itemAdd.template.id == 2069) {
                    it.quantity += itemAdd.quantity;
                    itemAdd.quantity = 0;
                    return true;
                }
                if (itemAdd.template.id == 933) {
                    if (it.quantity < 150000) {
                        int sl = 150000 - it.quantity;
                        if (itemAdd.quantity <= sl) {
                            it.quantity += itemAdd.quantity;
                            itemAdd.quantity = 0;
                            return true;
                        }
                    }
                }
                if (itemAdd.template.id == 1258 || itemAdd.template.id == 964) {
                    if (it.quantity < 99999) {
                        int sll = 99999 - it.quantity;
                        if (itemAdd.quantity <= sll) {
                            it.quantity += itemAdd.quantity;
                            itemAdd.quantity = 0;
                            return true;
                        }
                    }
                }
                //số lượng item cộng dồn
                if (it.quantity < 99999) {
                    int add = 99999 - it.quantity;
                    if (itemAdd.quantity <= add) {
                        it.quantity += itemAdd.quantity;
                        itemAdd.quantity = 0;
                        return true;
                    } else {
                        it.quantity = 99999;
                        itemAdd.quantity -= add;
                    }
                }
            }
        }
        //add item vào ô mới
        if (itemAdd.quantity > 0) {
            for (int i = 0; i < items.size(); i++) {
                if (!items.get(i).isNotNullItem()) {
                    items.set(i, ItemService.gI().copyItem(itemAdd));
                    itemAdd.quantity = 0;
                    return true;
                }
            }
        }
        return false;
    }

    private void __________________Kiểm_tra_điều_kiện_vật_phẩm______________() {
        //**********************************************************************
    }

    /**
     * Kiểm tra vật phẩm có phải là vật phẩm tăng chỉ số option hay không
     *
     * @param item
     * @return id option tăng chỉ số - param
     */
    private int[] isItemIncrementalOption(Item item) {
        for (Item.ItemOption io : item.itemOptions) {
            switch (io.optionTemplate.id) {
                case 1:
                    return new int[]{io.optionTemplate.id, io.param};
            }
        }
        return new int[]{-1, -1};
    }

    private void __________________Kiểm_tra_danh_sách_còn_chỗ_trống_________() {
        //**********************************************************************
    }

    public byte getCountEmptyBag(Player player) {
        return getCountEmptyListItem(player.inventory.itemsBag);
    }

    public byte getCountEmptyListItem(List<Item> list) {
        byte count = 0;
        for (Item item : list) {
            if (!item.isNotNullItem()) {
                count++;
            }
        }
        return count;
    }

    public byte getIndexBag(Player pl, Item it) {
        for (byte i = 0; i < pl.inventory.itemsBag.size(); ++i) {
            Item item = pl.inventory.itemsBag.get(i);
            if (item != null && it.equals(item)) {
                return i;
            }
        }
        return -1;
    }

    public long getGodMax(Player player) {
        switch (player.inventory.Elon_Musk) {
            case 0:
                return 5000000000L;
            case 1:
                return 10000000000L;
            case 2:
                return 15000000000L;
            case 3:
                return 20000000000L;
            case 4:
                return 25000000000L;
            case 5:
                return 30000000000L;
            case 6:
                return 35000000000L;
            case 7:
                return 40000000000L;
            case 8:
                return 45000000000L;
            default:
                return 50000000000L;
        }
    }
}
