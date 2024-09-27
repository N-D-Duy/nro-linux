package ServerData.Models.Shop;

import ServerData.Models.Item.Template;
import ServerData.Models.Item.Item;
import java.util.ArrayList;
import java.util.List;

public class ItemShop {
    
    public TabShop tabShop;

    public int id;
    
    public Template.ItemTemplate temp;
    
    public boolean isNew;
    
    public List<Item.ItemOption> options;
    
    public byte typeSell;
    
    public int iconSpec;
    
    public int cost;

    public ItemShop() {
        this.options = new ArrayList<>();
    }
    
    public ItemShop(ItemShop itemShop){
        this.options = new ArrayList<>();
        this.tabShop = itemShop.tabShop;
        this.id = itemShop.id;
        this.temp = itemShop.temp;
        this.isNew = itemShop.isNew;
        this.typeSell = itemShop.typeSell;
        this.iconSpec = itemShop.iconSpec;
        this.cost = itemShop.cost;
        for(Item.ItemOption io : itemShop.options){
            this.options.add(new Item.ItemOption(io));
        }
    }
    
    public void dispose(){
        this.tabShop = null;
        this.temp = null;
        if(this.options != null){
            for(Item.ItemOption io : this.options){
                io.dispose();
            }
            this.options.clear();
        }
        this.options = null;
    }
    
    
}
