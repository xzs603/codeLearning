package net.sf.odinms.server;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import net.sf.odinms.client.Equip;
import net.sf.odinms.client.IItem;
import net.sf.odinms.client.InventoryException;
import net.sf.odinms.client.Item;
import net.sf.odinms.client.MapleBuffStat;
import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.client.MapleClient;
import net.sf.odinms.client.MapleInventoryType;
import net.sf.odinms.tools.MaplePacketCreator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Matze
 */
public class MapleInventoryManipulator {

    private static Logger log = LoggerFactory.getLogger(MapleInventoryManipulator.class);

    public static boolean addFromDrop(MapleClient c, IItem item) {
        return addFromDrop(c, item, true);
    }

    public static boolean addFromDrop(MapleClient c, IItem item, boolean show) {
        return addFromDrop(c, item, show, null);
    }

    public static boolean addFromDrop(MapleClient c, IItem item, boolean show, String owner) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        MapleInventoryType type = ii.getInventoryType(item.getItemId());

        if (!c.getChannelServer().allowMoreThanOne() && ii.isPickupRestricted(item.getItemId()) && c.getPlayer().haveItem(item.getItemId(), 1, true, false)) {
            c.getSession().write(MaplePacketCreator.getInventoryFull());
            c.getSession().write(MaplePacketCreator.showItemUnavailable());
            return false;
        }

        short quantity = item.getQuantity();
        if (!type.equals(MapleInventoryType.EQUIP)) {
            short slotMax = ii.getSlotMax(c, item.getItemId());
            List<IItem> existing = c.getPlayer().getInventory(type).listById(item.getItemId());
            if (!ii.isThrowingStar(item.getItemId()) && !ii.isBullet(item.getItemId())) {
                if (existing.size() > 0) { // first update all existing slots to slotMax

                    Iterator<IItem> i = existing.iterator();
                    while (quantity > 0) {
                        if (i.hasNext()) {
                            Item eItem = (Item) i.next();
                            short oldQ = eItem.getQuantity();
                            if (oldQ < slotMax && item.getOwner().equals(eItem.getOwner())) {
                                short newQ = (short) Math.min(oldQ + quantity, slotMax);
                                quantity -= (newQ - oldQ);
                                eItem.setQuantity(newQ);
                                c.getSession().write(MaplePacketCreator.updateInventorySlot(type, eItem, true));
                            }
                        } else {
                            break;
                        }
                    }
                }
                // add new slots if there is still something left
                while (quantity > 0 || ii.isThrowingStar(item.getItemId()) || ii.isBullet(item.getItemId())) {
                    short newQ = (short) Math.min(quantity, slotMax);
                    quantity -= newQ;
                    Item nItem = new Item(item.getItemId(), (byte) 0, newQ);
                    nItem.setOwner(item.getOwner());
                    byte newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                    if (newSlot == -1) {
                        c.getSession().write(MaplePacketCreator.getInventoryFull());
                        c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                        item.setQuantity((short) (quantity + newQ));
                        return false;
                    }
                    c.getSession().write(MaplePacketCreator.addInventorySlot(type, nItem, true));
                }
            } else {
                // Throwing Stars and Bullets - Add all into one slot regardless of quantity.
                Item nItem = new Item(item.getItemId(), (byte) 0, quantity);
                byte newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                if (newSlot == -1) {
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                    return false;
                }
                c.getSession().write(MaplePacketCreator.addInventorySlot(type, nItem));
                c.getSession().write(MaplePacketCreator.enableActions());
            }
        } else {
            if (quantity == 1) {
                byte newSlot = c.getPlayer().getInventory(type).addItem(item);

                if (newSlot == -1) {
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                    return false;
                }
                c.getSession().write(MaplePacketCreator.addInventorySlot(type, item, true));
            } else {
                throw new RuntimeException("Trying to create equip with non-one quantity");
            }
        }
        if (owner != null) {
            item.setOwner(owner);
        }
        if (show) {
            c.getSession().write(MaplePacketCreator.getShowItemGain(item.getItemId(), item.getQuantity()));
        }
        return true;
    }


    public static void removeById(MapleClient c, MapleInventoryType type, int itemId, int quantity, boolean fromDrop, boolean consume, boolean v) {
        List<IItem> items = c.getPlayer().getInventory(type).listById(itemId);
        int remremove = quantity;
        for (IItem item : items) {
            if (remremove <= item.getQuantity()) {
                removeFromSlot(c, type, item.getPosition(), (short) remremove, fromDrop, consume);
                remremove = 0;
                break;
            } else {
                remremove -= item.getQuantity();
                removeFromSlot(c, type, item.getPosition(), item.getQuantity(), fromDrop, consume);
            }
        }
        if (remremove > 0) {
            throw new InventoryException("[h4x] Not enough cheese available (" + itemId + ", " + (quantity - remremove) +
                    "/" + quantity + ")");
        }
    }

    /** Creates a new instance of MapleInventoryManipulator */
    private MapleInventoryManipulator() {
    }

    public static boolean addRing(MapleCharacter chr, int itemId, int ringId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        MapleInventoryType type = ii.getInventoryType(itemId);
        IItem nEquip = ii.getEquipById(itemId, ringId);
        String logMsg = "Ring created by " + chr.getName();
        nEquip.log(logMsg, false);

        byte newSlot = chr.getInventory(type).addItem(nEquip);
        if (newSlot == -1) {
            return false;
        }
        chr.getClient().getSession().write(MaplePacketCreator.addInventorySlot(type, nEquip));
        return true;
    }

    public static boolean addFromDrop(MapleClient c, IItem item, String logInfo) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        MapleInventoryType type = ii.getInventoryType(item.getItemId());
        if (!c.getChannelServer().allowMoreThanOne() && ii.isPickupRestricted(item.getItemId()) && c.getPlayer().haveItem(item.getItemId(), 1, true, false)) {
            c.getSession().write(MaplePacketCreator.getInventoryFull());
            c.getSession().write(MaplePacketCreator.showItemUnavailable());
            return false;
        }
        short quantity = item.getQuantity();
        if (!type.equals(MapleInventoryType.EQUIP)) {
            short slotMax = ii.getSlotMax(c, item.getItemId());
            List<IItem> existing = c.getPlayer().getInventory(type).listById(item.getItemId());
            if (!ii.isThrowingStar(item.getItemId()) && !ii.isBullet(item.getItemId())) {
                if (existing.size() > 0) { // first update all existing slots to slotMax
                    Iterator<IItem> i = existing.iterator();
                    while (quantity > 0) {
                        if (i.hasNext()) {
                            Item eItem = (Item) i.next();
                            short oldQ = eItem.getQuantity();
                            if (oldQ < slotMax && item.getOwner().equals(eItem.getOwner())) {
                                short newQ = (short) Math.min(oldQ + quantity, slotMax);
                                quantity -= (newQ - oldQ);
                                eItem.setQuantity(newQ);
                                c.getSession().write(MaplePacketCreator.updateInventorySlot(type, eItem, true));
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
            while (quantity > 0 || ii.isThrowingStar(item.getItemId()) || ii.isBullet(item.getItemId())) {
                short newQ = (short) Math.min(quantity, slotMax);
                quantity -= newQ;
                Item nItem = new Item(item.getItemId(), (byte) 0, newQ);
                byte newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                if (newSlot == -1) {
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                    item.setQuantity((short) (quantity + newQ));
                    return false;
                }
                c.getSession().write(MaplePacketCreator.addInventorySlot(type, nItem, true));
                if ((ii.isThrowingStar(item.getItemId()) || ii.isBullet(item.getItemId())) && quantity == 0) {
                    break;
                }
            }
        } else {
            if (quantity == 1) {
                byte newSlot = c.getPlayer().getInventory(type).addItem(item);
                if (newSlot == -1) {
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                    return false;
                }
                c.getSession().write(MaplePacketCreator.addInventorySlot(type, item, true));
            } else {
                throw new RuntimeException("Trying to create equip with non-one quantity");
            }
        }
        c.getSession().write(MaplePacketCreator.getShowItemGain(item.getItemId(), item.getQuantity()));
        return true;
    }

    public static boolean addById(MapleClient c, int itemId, short quantity, String logInfo) {
        return addById(c, itemId, quantity, logInfo, null);
    }

    public static boolean addById(MapleClient c, int itemId, short quantity, String logInfo, String owner) {
        return addById(c, itemId, quantity, logInfo, owner, -1);
    }

    public static boolean addById(MapleClient c, int itemId, short quantity, String logInfo, String owner, int petid) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        MapleInventoryType type = ii.getInventoryType(itemId);
        if (!type.equals(MapleInventoryType.EQUIP)) {
            short slotMax = ii.getSlotMax(c, itemId);
            List<IItem> existing = c.getPlayer().getInventory(type).listById(itemId);
            if (!ii.isThrowingStar(itemId) && !ii.isBullet(itemId)) {
                if (existing.size() > 0) { // first update all existing slots to slotMax
                    Iterator<IItem> i = existing.iterator();
                    while (quantity > 0) {
                        if (i.hasNext()) {
                            Item eItem = (Item) i.next();
                            short oldQ = eItem.getQuantity();
                            if (oldQ < slotMax && (eItem.getOwner().equals(owner) || owner == null)) {
                                short newQ = (short) Math.min(oldQ + quantity, slotMax);
                                quantity -= (newQ - oldQ);
                                eItem.setQuantity(newQ);
                                StringBuilder logMsg = new StringBuilder("Added "+newQ+" - "+oldQ+" items to stack, new quantity is "+newQ+" ("+logInfo+" )");
                                eItem.log(logMsg.toString(), false);
                                c.getSession().write(MaplePacketCreator.updateInventorySlot(type, eItem));
                            }
                        } else {
                            break;
                        }
                    }
                }
                // add new slots if there is still something left
                while (quantity > 0 || ii.isThrowingStar(itemId) || ii.isBullet(itemId)) {
                    short newQ = (short) Math.min(quantity, slotMax);
                    if (newQ != 0) {
                        quantity -= newQ;
                        Item nItem = new Item(itemId, (byte) 0, newQ, petid);
                        StringBuilder logMsg = new StringBuilder("Created while adding by id. Quantity "+newQ+" ("+logInfo+" )");
                        nItem.log(logMsg.toString(), false);
                        byte newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                        if (newSlot == -1) {
                            c.getSession().write(MaplePacketCreator.getInventoryFull());
                            c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                            return false;
                        }
                        if (owner != null) {
                            nItem.setOwner(owner);
                        }
                        c.getSession().write(MaplePacketCreator.addInventorySlot(type, nItem));
                        if ((ii.isThrowingStar(itemId) || ii.isBullet(itemId)) && quantity == 0) {
                            break;
                        }
                    } else {
                        c.getSession().write(MaplePacketCreator.enableActions());
                        return false;
                    }
                }
            } else {
                // Throwing Stars and Bullets - Add all into one slot regardless of quantity.
                Item nItem = new Item(itemId, (byte) 0, quantity);
                StringBuilder logMsg = new StringBuilder("Created while adding by id. Quantity "+quantity+" ("+logInfo+" )");
                nItem.log(logMsg.toString(), false);
                byte newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                if (newSlot == -1) {
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                    return false;
                }
                c.getSession().write(MaplePacketCreator.addInventorySlot(type, nItem));
                c.getSession().write(MaplePacketCreator.enableActions());
            }
        } else {
            if (quantity == 1) {
                IItem nEquip = ii.getEquipById(itemId);
                StringBuilder logMsg = new StringBuilder("Created while adding by id. ("+logInfo+" )");
                nEquip.log(logMsg.toString(), false);
                if (owner != null) {
                    nEquip.setOwner(owner);
                }
                byte newSlot = c.getPlayer().getInventory(type).addItem(nEquip);
                if (newSlot == -1) {
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                    return false;
                }
                c.getSession().write(MaplePacketCreator.addInventorySlot(type, nEquip));
            } else {
                throw new InventoryException("Trying to create equip with non-one quantity");
            }
        }
        return true;
    }

    public static boolean addFromDrop(MapleClient c, IItem item, String logInfo, boolean show) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        MapleInventoryType type = ii.getInventoryType(item.getItemId());
        if (!c.getChannelServer().allowMoreThanOne() && ii.isPickupRestricted(item.getItemId()) && c.getPlayer().haveItem(item.getItemId(), 1, true, false)) {
            c.getSession().write(MaplePacketCreator.getInventoryFull());
            c.getSession().write(MaplePacketCreator.showItemUnavailable());
            return false;
        }
        short quantity = item.getQuantity();
        if (!type.equals(MapleInventoryType.EQUIP)) {
            short slotMax = ii.getSlotMax(c, item.getItemId());
            List<IItem> existing = c.getPlayer().getInventory(type).listById(item.getItemId());
            if (!ii.isThrowingStar(item.getItemId()) && !ii.isBullet(item.getItemId())) {
                if (existing.size() > 0) { // first update all existing slots to slotMax
                    Iterator<IItem> i = existing.iterator();
                    while (quantity > 0) {
                        if (i.hasNext()) {
                            Item eItem = (Item) i.next();
                            short oldQ = eItem.getQuantity();
                            if (oldQ < slotMax && item.getOwner().equals(eItem.getOwner())) {
                                short newQ = (short) Math.min(oldQ + quantity, slotMax);
                                quantity -= (newQ - oldQ);
                                eItem.setQuantity(newQ);
                                StringBuilder logMsg = new StringBuilder("Added "+newQ+" - "+oldQ+" items to stack, new quantity is "+newQ+" ("+logInfo+" )");
                                eItem.log(logMsg.toString(), false);
                                c.getSession().write(MaplePacketCreator.updateInventorySlot(type, eItem, true));
                            }
                        } else {
                            break;
                        }
                    }
                }
                // add new slots if there is still something left
                while (quantity > 0 || ii.isThrowingStar(item.getItemId()) || ii.isBullet(item.getItemId())) {
                    short newQ = (short) Math.min(quantity, slotMax);
                    quantity -= newQ;
                    Item nItem = new Item(item.getItemId(), (byte) 0, newQ);
                    StringBuilder logMsg = new StringBuilder("Created while adding from drop. Quantity "+newQ+" ("+logInfo+" )");
                    nItem.setOwner(item.getOwner());
                    nItem.log(logMsg.toString(), false);
                    byte newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                    if (newSlot == -1) {
                        c.getSession().write(MaplePacketCreator.getInventoryFull());
                        c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                        item.setQuantity((short) (quantity + newQ));
                        return false;
                    }
                    c.getSession().write(MaplePacketCreator.addInventorySlot(type, nItem, true));
                }
            } else {
                // Throwing Stars and Bullets - Add all into one slot regardless of quantity.
                Item nItem = new Item(item.getItemId(), (byte) 0, quantity);
                StringBuilder logMsg = new StringBuilder("Created while adding by id. Quantity "+quantity+" ("+logInfo+" )");
                nItem.log(logMsg.toString(), false);
                byte newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                if (newSlot == -1) {
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                    return false;
                }
                c.getSession().write(MaplePacketCreator.addInventorySlot(type, nItem));
                c.getSession().write(MaplePacketCreator.enableActions());
            }
        } else {
            if (quantity == 1) {
                byte newSlot = c.getPlayer().getInventory(type).addItem(item);
                StringBuilder logMsg = new StringBuilder("Adding from drop. ("+logInfo+" )");
                item.log(logMsg.toString(), false);
                if (newSlot == -1) {
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                    return false;
                }
                c.getSession().write(MaplePacketCreator.addInventorySlot(type, item, true));
            } else {
                throw new RuntimeException("Trying to create equip with non-one quantity");
            }
        }
        if (show) {
            c.getSession().write(MaplePacketCreator.getShowItemGain(item.getItemId(), item.getQuantity()));
        }
        return true;
    }

    public static boolean checkSpace(MapleClient c, int itemid, int quantity, String owner) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        MapleInventoryType type = ii.getInventoryType(itemid);
        if (!type.equals(MapleInventoryType.EQUIP)) {
            short slotMax = ii.getSlotMax(c, itemid);
            List<IItem> existing = c.getPlayer().getInventory(type).listById(itemid);
            if (!ii.isThrowingStar(itemid) && !ii.isBullet(itemid)) {
                if (existing.size() > 0) { // first update all existing slots to slotMax
                    for (IItem eItem : existing) {
                        short oldQ = eItem.getQuantity();
                        if (oldQ < slotMax && owner.equals(eItem.getOwner())) {
                            short newQ = (short) Math.min(oldQ + quantity, slotMax);
                            quantity -= (newQ - oldQ);
                        }
                        if (quantity <= 0) {
                            break;
                        }
                    }
                }
            }
            // add new slots if there is still something left
            final int numSlotsNeeded;
            if (slotMax > 0) {
                numSlotsNeeded = (int) (Math.ceil(((double) quantity) / slotMax));
            } else if (ii.isThrowingStar(itemid) || ii.isBullet(itemid)) {
                numSlotsNeeded = 1;
            } else {
                numSlotsNeeded = 1;
                log.error("SUCK ERROR - FIX ME! - 0 slotMax");
            }
            return !c.getPlayer().getInventory(type).isFull(numSlotsNeeded - 1);
        } else {
            return !c.getPlayer().getInventory(type).isFull();
        }
    }

    public static void removeFromSlot(MapleClient c, MapleInventoryType type, byte slot, short quantity, boolean fromDrop) {
        removeFromSlot(c, type, slot, quantity, fromDrop, false);
    }

    public static void removeFromSlot(MapleClient c, MapleInventoryType type, byte slot, short quantity, boolean fromDrop, boolean consume) {
        IItem item = c.getPlayer().getInventory(type).getItem(slot);
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        boolean allowZero = consume && (ii.isThrowingStar(item.getItemId()) || ii.isBullet(item.getItemId()));
        c.getPlayer().getInventory(type).removeItem(slot, quantity, allowZero);
        if (item.getQuantity() == 0 && !allowZero) {
            c.getSession().write(MaplePacketCreator.clearInventoryItem(type, item.getPosition(), fromDrop));
        } else {
            if (!consume) {
                StringBuilder logMsg = new StringBuilder(c.getPlayer().getName());
                logMsg.append(" removed ");
                logMsg.append(quantity);
                logMsg.append(". ");
                logMsg.append(item.getQuantity());
                logMsg.append(" left.");
                item.log(logMsg.toString(), false);
            }
            c.getSession().write(MaplePacketCreator.updateInventorySlot(type, (Item) item, fromDrop));
        }
    }

    public static void removeById(MapleClient c, MapleInventoryType type, int itemId, int quantity, boolean fromDrop, boolean consume) {
        List<IItem> items = c.getPlayer().getInventory(type).listById(itemId);
        int remremove = quantity;
        for (IItem item : items) {
            if (remremove <= item.getQuantity()) {
                removeFromSlot(c, type, item.getPosition(), (short) remremove, fromDrop, consume);
                remremove = 0;
                break;
            } else {
                remremove -= item.getQuantity();
                removeFromSlot(c, type, item.getPosition(), item.getQuantity(), fromDrop, consume);
            }
        }
        if (remremove > 0) {
            throw new InventoryException("[h4x] Not enough cheese available (" + itemId + ", " + (quantity - remremove) +
                    "/" + quantity + ")");
        }
    }

    public static void move(MapleClient c, MapleInventoryType type, byte src, byte dst) {
        if (src < 0 || dst < 0) {
            return;
        }
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        IItem source = c.getPlayer().getInventory(type).getItem(src);
        IItem initialTarget = c.getPlayer().getInventory(type).getItem(dst);
        if (source == null) {
            return;
        }
        short olddstQ = -1;
        if (initialTarget != null) {
            olddstQ = initialTarget.getQuantity();
        }
        short oldsrcQ = source.getQuantity();
        short slotMax = ii.getSlotMax(c, source.getItemId());
        c.getPlayer().getInventory(type).move(src, dst, slotMax);
        if (!type.equals(MapleInventoryType.EQUIP) && initialTarget != null &&
                initialTarget.getItemId() == source.getItemId() && !ii.isThrowingStar(source.getItemId()) &&
                !ii.isBullet(source.getItemId())) {
            if ((olddstQ + oldsrcQ) > slotMax) {
                c.getSession().write(
                        MaplePacketCreator.moveAndMergeWithRestInventoryItem(type, src, dst,
                        (short) ((olddstQ + oldsrcQ) - slotMax), slotMax));
            } else {
                c.getSession().write(
                        MaplePacketCreator.moveAndMergeInventoryItem(type, src, dst, ((Item) c.getPlayer().getInventory(type).getItem(dst)).getQuantity()));
            }
        } else {
            c.getSession().write(MaplePacketCreator.moveInventoryItem(type, src, dst));
        }
    }

    public static void equip(MapleClient c, byte src, byte dst) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Equip source = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(src);
        Equip target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(dst);
        if (source == null) {
            return;
        }
        if (dst == -6) {
            // unequip the overall
            IItem top = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -5);
            if (top != null && ii.isOverall(top.getItemId())) {
                if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).isFull()) {
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                    return;
                }
                unequip(c, (byte) -5, c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNextFreeSlot());
            }
        } else if (dst == -5) {
            // unequip the bottom and top
            IItem top = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -5);
            IItem bottom = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -6);
            if (top != null && ii.isOverall(source.getItemId())) {
                if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).isFull(bottom != null && ii.isOverall(source.getItemId()) ? 1 : 0)) {
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                    return;
                }
                unequip(c, (byte) -5, c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNextFreeSlot());
            }
            if (bottom != null && ii.isOverall(source.getItemId())) {
                if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).isFull()) {
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                    return;
                }
                unequip(c, (byte) -6, c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNextFreeSlot());
            }
        } else if (dst == -10) {
            // check if weapon is two-handed
            IItem weapon = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -11);
            if (weapon != null && ii.isTwoHanded(weapon.getItemId())) {
                if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).isFull()) {
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                    return;
                }
                unequip(c, (byte) -11, c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNextFreeSlot());
            }
        } else if (dst == -11) {
            IItem shield = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -10);
            if (shield != null && ii.isTwoHanded(source.getItemId())) {
                if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).isFull()) {
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    c.getSession().write(MaplePacketCreator.getShowInventoryFull());
                    return;
                }
                unequip(c, (byte) -10, c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNextFreeSlot());
            }
        }
        if (dst == -18) {
            if (c.getPlayer().getMount() != null) {
                c.getPlayer().getMount().setItemId(source.getItemId()); //update mount itemid
            }
        }
        source = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(src);
        target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(dst);
        c.getPlayer().getInventory(MapleInventoryType.EQUIP).removeSlot(src);
        if (target != null) {
            c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).removeSlot(dst);
        }
        source.setPosition(dst);
        c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).addFromDB(source);
        if (target != null) {
            target.setPosition(src);
            c.getPlayer().getInventory(MapleInventoryType.EQUIP).addFromDB(target);
        }
        if (c.getPlayer().getBuffedValue(MapleBuffStat.BOOSTER) != null && ii.isWeapon(source.getItemId())) {
            c.getPlayer().cancelBuffStats(MapleBuffStat.BOOSTER);
        }
        c.getSession().write(MaplePacketCreator.moveInventoryItem(MapleInventoryType.EQUIP, src, dst, (byte) 2));
        c.getPlayer().equipChanged();
    }

    public static void unequip(MapleClient c, byte src, byte dst) {
        Equip source = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(src);
        Equip target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(dst);
        if (dst < 0) {
            log.warn("Unequipping to negative slot. ({}: {}->{})", new Object[]{c.getPlayer().getName(), src, dst});
        }
        if (source == null) {
            return;
        }
        if (target != null && src <= 0) {
            // do not allow switching with equip
            c.getSession().write(MaplePacketCreator.getInventoryFull());
            return;
        }
        c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).removeSlot(src);
        if (target != null) {
            c.getPlayer().getInventory(MapleInventoryType.EQUIP).removeSlot(dst);
        }
        source.setPosition(dst);
        c.getPlayer().getInventory(MapleInventoryType.EQUIP).addFromDB(source);
        if (target != null) {
            target.setPosition(src);
            c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).addFromDB(target);
        }
        c.getSession().write(MaplePacketCreator.moveInventoryItem(MapleInventoryType.EQUIP, src, dst, (byte) 1));
        c.getPlayer().equipChanged();
    }

    public static void drop(MapleClient c, MapleInventoryType type, byte src, short quantity) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (src < 0) {
            type = MapleInventoryType.EQUIPPED;
        }
        IItem source = c.getPlayer().getInventory(type).getItem(src);
        if (quantity < 0 || source == null || quantity == 0 && !ii.isThrowingStar(source.getItemId()) && !ii.isBullet(source.getItemId())) {
            String message = "Dropping " + quantity + " " + (source == null ? "?" : source.getItemId()) + " (" +
                    type.name() + "/" + src + ")";
            //AutobanManager.getInstance().addPoints(c, 1000, 0, message);
            log.info(MapleClient.getLogMessage(c, message));
            c.getSession().close(); // disconnect the client as is inventory is inconsistent with the serverside inventory -> fuck
            return;
        }
        Point dropPos = new Point(c.getPlayer().getPosition());
        //dropPos.y -= 99;
        if (quantity < source.getQuantity() && !ii.isThrowingStar(source.getItemId()) && !ii.isBullet(source.getItemId())) {
            IItem target = source.copy();
            target.setQuantity(quantity);
            source.setQuantity((short) (source.getQuantity() - quantity));

            c.getSession().write(MaplePacketCreator.dropInventoryItemUpdate(type, source));
            boolean weddingRing = source.getItemId() == 1112803 || source.getItemId() == 1112806 || source.getItemId() == 1112807 || source.getItemId() == 1112809;
            if (weddingRing) {
                c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos);
            } else if (c.getPlayer().getMap().getEverlast()) {
                if (!c.getChannelServer().allowUndroppablesDrop() && ii.isDropRestricted(target.getItemId())) {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos);
                } else {
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, false);
                }
            } else {
                if (!c.getChannelServer().allowUndroppablesDrop() && ii.isDropRestricted(target.getItemId())) {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos);
                } else {
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, true);
                }
            }
        } else {
            c.getPlayer().getInventory(type).removeSlot(src);
            c.getSession().write(MaplePacketCreator.dropInventoryItem(
                    (src < 0 ? MapleInventoryType.EQUIP : type), src));
            if (src < 0) {
                c.getPlayer().equipChanged();
            }
            if (c.getPlayer().getMap().getEverlast()) {
                if (!c.getChannelServer().allowUndroppablesDrop() && ii.isDropRestricted(source.getItemId())) {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos);
                } else {
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, false);
                }
            } else {
                if (!c.getChannelServer().allowUndroppablesDrop() && ii.isDropRestricted(source.getItemId())) {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos);
                } else {
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, true);
                }
            }
        }
    }
}