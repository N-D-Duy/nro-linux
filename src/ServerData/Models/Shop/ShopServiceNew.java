package ServerData.Models.Shop;

import ServerData.Models.Item.Item;
import ServerData.Models.Player.Inventory;
import ServerData.Models.Player.Player;
import com.girlkun.network.io.Message;
import ServerData.Server.Manager;
import ServerData.Services.InventoryServiceNew;
import ServerData.Services.ItemService;
import ServerData.Services.Service;
import ServerData.Utils.Logger;
import ServerData.Utils.Util;

import java.util.List;

public class ShopServiceNew {

    private static final byte COST_GOLD = 0;
    private static final byte COST_GEM = 1;
    private static final byte COST_ITEM_SPEC = 2;
    private static final byte COST_RUBY = 3;
    private static final byte COST_COUPON = 4;


    private static final byte NORMAL_SHOP = 0;
    private static final byte SPEC_SHOP = 3;

    private static ShopServiceNew I;

    public static ShopServiceNew gI() {
        if (ShopServiceNew.I == null) {
            ShopServiceNew.I = new ShopServiceNew();
        }
        return ShopServiceNew.I;
    }

    public void opendShop(Player player, String tagName, boolean allGender) {
        if (tagName.equals("ITEMS_LUCKY_ROUND")) {
            openShopType4(player, tagName, player.inventory.itemsBoxCrackBall);
            return;
        } else if (tagName.equals("RUONG_PHU")) {
            openShopType5(player, tagName, player.inventory.itemsBoxCrackBall);
            return;
        } else if (tagName.equals("ITEMS_REWARD")) {
            player.getSession().initItemsReward();
            return;
        }
        try {
            Shop shop = this.getShop(tagName);
            shop = this.resolveShop(player, shop, allGender);
            switch (shop.typeShop) {
                case NORMAL_SHOP:
                    openShopType0(player, shop);
                    break;
                case SPEC_SHOP:
                    openShopType3(player, shop);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Service.gI().sendThongBao(player, ex.getMessage());
        }
    }

    private Shop getShop(String tagName) throws Exception {
        for (Shop s : Manager.SHOPS) {
            if (s.tagName != null && s.tagName.equals(tagName)) {
                return s;
            }
        }
        throw new Exception("Shop " + tagName + " không tồn tại!");
    }

    private void _________________Xử_lý_cửa_hàng_trước_khi_gửi_______________() {
        //**********************************************************************
    }

    private Shop resolveShop(Player player, Shop shop, boolean allGender) {
        if (shop.tagName != null && (shop.tagName.equals("BUA_1H")
                || shop.tagName.equals("BUA_8H") || shop.tagName.equals("BUA_1M"))) {
            return this.resolveShopBua(player, new Shop(shop));
        }
        return allGender ? new Shop(shop) : new Shop(shop, player.gender);
    }

    private Shop resolveShopBua(Player player, Shop s) {
        for (TabShop tabShop : s.tabShops) {
            for (ItemShop item : tabShop.itemShops) {
                long min = 0;
                switch (item.temp.id) {
                    case 213:
                        long timeTriTue = player.charms.tdTriTue;
                        long current = System.currentTimeMillis();
                        min = (timeTriTue - current) / 60000;
                        break;
                    case 214:
                        min = (player.charms.tdManhMe - System.currentTimeMillis()) / 60000;
                        break;
                    case 215:
                        min = (player.charms.tdDaTrau - System.currentTimeMillis()) / 60000;
                        break;
                    case 216:
                        min = (player.charms.tdOaiHung - System.currentTimeMillis()) / 60000;
                        break;
                    case 217:
                        min = (player.charms.tdBatTu - System.currentTimeMillis()) / 60000;
                        break;
                    case 218:
                        min = (player.charms.tdDeoDai - System.currentTimeMillis()) / 60000;
                        break;
                    case 219:
                        min = (player.charms.tdThuHut - System.currentTimeMillis()) / 60000;
                        break;
                    case 522:
                        min = (player.charms.tdDeTu - System.currentTimeMillis()) / 60000;
                        break;
                    case 671:
                        min = (player.charms.tdTriTue3 - System.currentTimeMillis()) / 60000;
                        break;
                    case 672:
                        min = (player.charms.tdTriTue4 - System.currentTimeMillis()) / 60000;
                        break;
                }
                if (min > 0) {
                    item.options.clear();
                    if (min >= 1440) {
                        item.options.add(new Item.ItemOption(63, (int) min / 1440));
                    } else if (min >= 60) {
                        item.options.add(new Item.ItemOption(64, (int) min / 60));
                    } else {
                        item.options.add(new Item.ItemOption(65, (int) min));
                    }
                }
            }
        }
        return s;
    }

    private void _________________Gửi_cửa_hàng_cho_người_chơi________________() {
        //**********************************************************************
    }

    private void openShopType0(Player player, Shop shop) {
        player.iDMark.setShopOpen(shop);
        player.iDMark.setTagNameShop(shop.tagName);
        if (shop != null) {
            Message msg;
            try {
                msg = new Message(-44);
                msg.writer().writeByte(NORMAL_SHOP);
                msg.writer().writeByte(shop.tabShops.size());
                for (TabShop tab : shop.tabShops) {
                    msg.writer().writeUTF(tab.name);
                    msg.writer().writeByte(tab.itemShops.size());
                    for (ItemShop itemShop : tab.itemShops) {
                        msg.writer().writeShort(itemShop.temp.id);
                        if (itemShop.typeSell == COST_GOLD) {
                            msg.writer().writeInt(itemShop.cost);
                            msg.writer().writeInt(0);
                        } else if (itemShop.typeSell == COST_GEM) {
                            msg.writer().writeInt(0);
                            msg.writer().writeInt(itemShop.cost);
                        } else if (itemShop.typeSell == COST_RUBY) {
                            msg.writer().writeInt(0);
                            msg.writer().writeInt(itemShop.cost);
                        } else if (itemShop.typeSell == COST_COUPON) {
                            msg.writer().writeInt(0);
                            msg.writer().writeInt(itemShop.cost);
                        }
                        msg.writer().writeByte(itemShop.options.size());
                        for (Item.ItemOption option : itemShop.options) {
                            msg.writer().writeByte(option.optionTemplate.id);
                            msg.writer().writeShort(option.param);
                        }
                        msg.writer().writeByte(itemShop.isNew ? 1 : 0);
                        if (itemShop.temp.type == 5) {
                            msg.writer().writeByte(1);
                            msg.writer().writeShort(itemShop.temp.head);
                            msg.writer().writeShort(itemShop.temp.body);
                            msg.writer().writeShort(itemShop.temp.leg);
                            msg.writer().writeShort(-1);
                        } else {
                            msg.writer().writeByte(0);
                        }
                    }
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(ShopServiceNew.class, e);
            }
        }
    }

    private void openShopType3(Player player, Shop shop) {
        player.iDMark.setShopOpen(shop);
        player.iDMark.setTagNameShop(shop.tagName);
        if (shop != null) {
            Message msg;
            try {
                msg = new Message(-44);
                msg.writer().writeByte(SPEC_SHOP);
                msg.writer().writeByte(shop.tabShops.size());
                for (TabShop tab : shop.tabShops) {
                    msg.writer().writeUTF(tab.name);
                    msg.writer().writeByte(tab.itemShops.size());
                    for (ItemShop itemShop : tab.itemShops) {
                        msg.writer().writeShort(itemShop.temp.id);
                        msg.writer().writeShort(itemShop.iconSpec);
                        msg.writer().writeInt(itemShop.cost);
                        msg.writer().writeByte(itemShop.options.size());
                        for (Item.ItemOption option : itemShop.options) {
                            msg.writer().writeByte(option.optionTemplate.id);
                            msg.writer().writeShort(option.param);
                        }
                        msg.writer().writeByte(itemShop.isNew ? 1 : 0);
                        if (itemShop.temp.type == 5) {
                            msg.writer().writeByte(1);
                            msg.writer().writeShort(itemShop.temp.head);
                            msg.writer().writeShort(itemShop.temp.body);
                            msg.writer().writeShort(itemShop.temp.leg);
                            msg.writer().writeShort(-1);
                        } else {
                            msg.writer().writeByte(0);
                        }
                    }
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(ShopServiceNew.class, e);
            }
        }
    }

    private void openShopType4(Player player, String tagName, List<Item> items) {
        if (items == null) {
            return;
        }
        player.iDMark.setTagNameShop(tagName);
        Message msg;
        try {
            msg = new Message(-44);
            msg.writer().writeByte(4);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Phần\nthưởng");
            msg.writer().writeByte(items.size());
            for (Item item : items) {
                msg.writer().writeShort(item.template.id);
                msg.writer().writeUTF("\n|7|Ngọc Rồng Kamui");
                msg.writer().writeByte(item.itemOptions.size()+1);
                for (Item.ItemOption io : item.itemOptions) {
                    msg.writer().writeByte(io.optionTemplate.id);
                    msg.writer().writeShort(io.param);
                }
                //số lượng
                msg.writer().writeByte(31);
                msg.writer().writeShort(item.quantity);
                //
                msg.writer().writeByte(1);
                if (item.template.type == 5) {
                    msg.writer().writeByte(1);
                    msg.writer().writeShort(item.template.head);
                    msg.writer().writeShort(item.template.body);
                    msg.writer().writeShort(item.template.leg);
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeByte(0);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openShopType5(Player player, String tagName, List<Item> items) {
        if (items == null) {
            return;
        }
        player.iDMark.setTagNameShop(tagName);
        Message msg;
        try {
            msg = new Message(-44);
            msg.writer().writeByte(4);
            msg.writer().writeByte(1);
            msg.writer().writeUTF(" BAG PHỤ\n ("+player.inventory.itemsBoxCrackBall.size()+"/200)");
            msg.writer().writeByte(items.size());
            for (Item item : items) {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeUTF("DRAGONBALL\n|7|KAMUI");
                    msg.writer().writeByte(item.itemOptions.size()+1);
                for (Item.ItemOption io : item.itemOptions) {
                    msg.writer().writeByte(io.optionTemplate.id);
                    msg.writer().writeShort(io.param);
                }
                //số lượng
                msg.writer().writeByte(207);
                msg.writer().writeShort(item.quantity);
                //
                msg.writer().writeByte(1);
                if (item.template.type == 5) {
                    msg.writer().writeByte(1);
                    msg.writer().writeShort(item.template.head);
                    msg.writer().writeShort(item.template.body);
                    msg.writer().writeShort(item.template.leg);
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeByte(0);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void _________________Mua_vật_phẩm______________________________() {
        //**********************************************************************
    }

    public void takeItem(Player player, byte type, int tempId) {
        String tagName = player.iDMark.getTagNameShop();
        if(tagName == null || tagName.length() <= 0){
            return;
        }
        if (tagName.equals("ITEMS_LUCKY_ROUND")) {
            getItemSideBoxLuckyRound(player, player.inventory.itemsBoxCrackBall, type, tempId);
            return;
        } else if (tagName.equals("RUONG_PHU")) {
            layruongphu(player, player.inventory.itemsBoxCrackBall, type, tempId);
            return;
        } else if (tagName.equals("ITEMS_REWARD")) {
            return;
        }

        if (player.iDMark.getShopOpen() == null) {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
            return;
        }
//        if (tagName.equals("SHOP_LUOT")) {
//            buyItem(player, tempId,"SHOP_LUOT");
//            return;  
//        }
        if (tagName.equals("BUA_1H") || tagName.equals("BUA_8H") || tagName.equals("BUA_1M")) {
            buyItemBua(player, tempId);
        } else {
            buyItem(player, tempId);
        }
        Service.gI().sendMoney(player);
    }

    private boolean subMoneyByItemShop(Player player, ItemShop is) {
        int gold = 0;
        int gem = 0;
        int ruby = 0;
        int coupon = 0;
        switch (is.typeSell) {
            case COST_GOLD:
                gold = is.cost;
                break;
            case COST_GEM:
                gem = is.cost;
                break;
            case COST_RUBY:
                ruby = is.cost;
                break;
            case COST_COUPON:
                coupon = is.cost;
                break;

        }
        if (player.inventory.gold < gold) {
            Service.gI().sendThongBao(player, "Bạn không có đủ vàng");
            return false;
        } else if (player.inventory.gem < gem) {
            Service.gI().sendThongBao(player, "Bạn không có đủ ngọc");
            return false;
        } else if (player.inventory.ruby < ruby) {
            Service.gI().sendThongBao(player, "Bạn không có đủ hồng ngọc");
            return false;
        } else if (player.inventory.coupon < coupon) {
            Service.gI().sendThongBao(player, "Bạn không có đủ điểm");
            return false;
        }
        player.inventory.gold -= is.temp.gold;
        player.inventory.gem -= is.temp.gem;
        player.inventory.ruby -= ruby;
        player.inventory.coupon -= coupon;
        return true;
    }

    /**
     * Mua bùa
     *
     * @param player     người chơi
     * @param itemTempId id template vật phẩm
     */
    private void buyItemBua(Player player, int itemTempId) {
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        if (is == null) {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
            return;
        }
        if (!subMoneyByItemShop(player, is)) {
            return;
        }
        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
        InventoryServiceNew.gI().sendItemBags(player);
        opendShop(player, shop.tagName, true);
    }

    /**
     * Mua vật phẩm trong cửa hàng
     *
     * @param player     người chơi
     * @param itemTempId id template vật phẩm
     * @param tagName // tên shop
     */
    public void buyItem(Player player, int itemTempId, String tagName) {
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        if (is == null) {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBao(player, "Hành trang đã đầy");
            return;
        }
        Item item = ItemService.gI().createItemFromItemShop(is);
        if(item == null){
            Service.gI().sendThongBao(player,"Vật phẩm đã được bán hết!");
            return;
        }
        if (player.inventory.event < is.cost) {
            Service.gI().sendThongBao(player,"Bạn không đù điểm sự kiện!");
            return;
        }
        int vt = -1;
        for(int i=0;i<item.itemOptions.size();i++){
            if(item.itemOptions.get(i).optionTemplate.id == 239){
                ShopServiceNew.gI().opendShop(player, tagName, false);
                vt = i;
                player.inventory.event -= is.cost;
                break;
            }
        }
        if(vt != -1 && vt < item.itemOptions.size()) item.itemOptions.remove(vt);
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendThongBao(player, "Mua thành công " + is.temp.name + "\nvới giá " + is.cost + " điểm");
    }
    
    public void buyItem(Player player, int itemTempId) {
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        if (is == null) {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBao(player, "Hành trang đã đầy");
            return;
        }
        if (shop.typeShop == ShopServiceNew.NORMAL_SHOP) {
            if (!subMoneyByItemShop(player, is)) {
                return;
            }
        } else if (shop.typeShop == ShopServiceNew.SPEC_SHOP) {
            if (!this.subIemByItemShop(player, is)) {
                return;
            }
        }
        Item item = ItemService.gI().createItemFromItemShop(is);
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendThongBao(player, "Mua thành công " + is.temp.name);
    }
    
    public void muanhieu(Player player, int itemTempId, int count) {
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        if (is == null) {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBao(player, "Hành trang đã đầy");
            return;
        }
        if (shop.typeShop == ShopServiceNew.NORMAL_SHOP) {
            if (!subMoneyByItemShop(player, is)) {
                return;
            }
        } else if (shop.typeShop == ShopServiceNew.SPEC_SHOP) {
            if (!this.subIemByItemShop(player, is)) {
                return;
            }
        }
        while (count > 0) {
        count--;
        Item item = ItemService.gI().createItemFromItemShop(is);
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendThongBao(player, "Mua thành công " + is.temp.name);
        }
    }

    private void _________________Bán_vật_phẩm______________________________() {
        //**********************************************************************
    }

    private boolean subIemByItemShop(Player pl, ItemShop itemShop) {
        boolean isBuy = false;
        short itSpec = ItemService.gI().getItemIdByIcon((short) itemShop.iconSpec);
        int buySpec = itemShop.cost;
        Item itS = ItemService.gI().createNewItem(itSpec);
        switch (itS.template.id) {
            case 76:
            case 188:
            case 189:
            case 190:
                if (pl.inventory.gold >= buySpec) {
                    pl.inventory.gold -= buySpec;
                    isBuy = true;
                } else {
                    Service.gI().sendThongBao(pl, "Bạn Không Đủ Vàng Để Mua Vật Phẩm");
                    isBuy = false;
                }
                break;
            case 861:
                if (pl.inventory.ruby >= buySpec) {
                    pl.inventory.ruby -= buySpec;
                    isBuy = true;
                } else {
                    Service.gI().sendThongBao(pl, "Bạn Không Đủ Hồng Ngọc Để Mua Vật Phẩm");
                    isBuy = false;
                }
                break;
            default:
                if (InventoryServiceNew.gI().findItemBag(pl, itSpec) == null || !InventoryServiceNew.gI().findItemBag(pl, itSpec).isNotNullItem()) {
                    Service.gI().sendThongBao(pl, "Bạn không có " + itS.template.name);
                    isBuy = false;
                } else if (InventoryServiceNew.gI().findItemBag(pl, itSpec).quantity < buySpec) {
                    Service.gI().sendThongBao(pl, "Bạn không có đủ " + buySpec + " " + itS.template.name);
                    isBuy = false;
                } else {
                    InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, itSpec), buySpec);
                    isBuy = true;
                }
                break;
        }
        return isBuy;
    }
    
    public void showConfirmSellItem(Player pl, int where, int index) {
        if (index == 2 || index == 1) {
            return;
        }
        Item item = null;
        if (where == 0) {
            item = pl.inventory.itemsBody.get(index);
        } else {
            if (pl.getSession().version < 220) {
                index -= (pl.inventory.itemsBody.size() - 7);
            }
            item = pl.inventory.itemsBag.get(index);
        }
        if (item != null && item.isNotNullItem()) {
            int quantity = item.quantity;
            int cost = item.template.gold;
            if (item.template.id == 457 || item.template.id == 2116) {
                quantity = 1;
            } else {
                cost /= 4;
            }
            cost *= quantity;

            String text = "Bạn có muốn bán\nx" + quantity
                    + " " + item.template.name + "\nvới giá là " + Util.numberToMoney(cost) + " vàng?";
            Message msg = new Message(7);
            try {
                msg.writer().writeByte(where);
                msg.writer().writeShort(index);
                msg.writer().writeUTF(text);
                pl.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    public void sellItem(Player pl, int where, int index) {
        Item item = null;
        if (where == 0) {
            item = pl.inventory.itemsBody.get(index);
        } else {
            item = pl.inventory.itemsBag.get(index);
        }
        if (item != null) {
            int quantity = item.quantity;
            int cost = item.template.gold;
            if (item.template.id == 2116) {
                quantity = 1;
            } if (item.template.id == 457) {
                quantity = 1;
            } else {
                cost /= 4;
            }
            cost *= quantity;
            if (pl.inventory.gold + cost > InventoryServiceNew.gI().getGodMax(pl)) {
                Service.gI().sendThongBao(pl, "Vàng sau khi bán vượt quá giới hạn, mở thêm tại Trưởng Lão Guru");
                return;
            }
            pl.inventory.gold += cost;
            if (pl.inventory.gold > InventoryServiceNew.gI().getGodMax(pl)) {
                pl.inventory.gold = InventoryServiceNew.gI().getGodMax(pl);
            }
            Service.gI().sendMoney(pl);
            Service.gI().sendThongBao(pl, "Đã bán " + item.template.name
                    + " thu được " + Util.numberToMoney(cost) + " vàng");
            if (where == 0) {
                InventoryServiceNew.gI().subQuantityItemsBody(pl, item, quantity);
                InventoryServiceNew.gI().sendItemBody(pl);
                Service.gI().Send_Caitrang(pl);
            } else {
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, quantity);
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        } else {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
        }
    }

    private void _________________Nhận_vật_phẩm_từ_rương_đặc_biệt___________() {
        //**********************************************************************
    }

    private void getItemSideBoxLuckyRound(Player player, List<Item> items, byte type, int index) {
        if (items == null) {
            return;
        }
        Item item = items.get(index);
        switch (type) {
            case 0: //nhận
                if (item.isNotNullItem()) {
                    if (InventoryServiceNew.gI().getCountEmptyBag(player) != 0) {
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.gI().sendThongBao(player,
                                "Bạn nhận được " + (item.template.id == 189
                                        ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                        InventoryServiceNew.gI().sendItemBags(player);
                        items.remove(index);
                    } else {
                        Service.gI().sendThongBao(player, "Hành trang đã đầy");
                    }
                } else {
                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                }
                break;
            case 1: //xóa
                items.remove(index);
                Service.gI().sendThongBao(player, "Xóa vật phẩm thành công");
                break;
            case 2: //nhận hết
                for (int i = items.size() - 1; i >= 0; i--) {
                    item = items.get(i);
                    if (InventoryServiceNew.gI().addItemBag(player, item)) {
                        Service.gI().sendThongBao(player,
                                "Bạn nhận được " + (item.template.id == 189
                                        ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                        items.remove(i);
                    }
                }
                InventoryServiceNew.gI().sendItemBags(player);
                break;
        }
        openShopType4(player, player.iDMark.getTagNameShop(), items);
    }
    private void layruongphu(Player player, List<Item> items, byte type, int index) {
        if (items == null) {
            return;
        }
        Item item = items.get(index);
        switch (type) {
            case 0: //nhận
                if (item.isNotNullItem()) {
                    if (InventoryServiceNew.gI().getCountEmptyBag(player) != 0) {
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.gI().sendThongBao(player,
                                "Bạn nhận được " + (item.template.id == 189
                                        ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                        InventoryServiceNew.gI().sendItemBags(player);
                        items.remove(index);
                    } else {
                        Service.gI().sendThongBao(player, "Hành trang đã đầy");
                    }
                } else {
                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                }
                break;
            case 1: //xóa
                items.remove(index);
                Service.gI().sendThongBao(player, "Xóa vật phẩm thành công");
                break;
            case 2: //nhận hết
                for (int i = items.size() - 1; i >= 0; i--) {
                    item = items.get(i);
                    if (InventoryServiceNew.gI().addItemBag(player, item)) {
                        items.remove(i);
                    }
                }
                InventoryServiceNew.gI().sendItemBags(player);
                break;
        }
        openShopType5(player, player.iDMark.getTagNameShop(), items);
    }
}



/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - Girl Béo
 */
