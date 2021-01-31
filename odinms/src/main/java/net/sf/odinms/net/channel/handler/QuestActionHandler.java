package net.sf.odinms.net.channel.handler;

import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.client.MapleClient;
import net.sf.odinms.net.AbstractMaplePacketHandler;
import net.sf.odinms.scripting.quest.QuestScriptManager;
import net.sf.odinms.server.quest.MapleQuest;
import net.sf.odinms.tools.data.input.SeekableLittleEndianAccessor;

/**
 *
 * @author Matze
 */
public class QuestActionHandler extends AbstractMaplePacketHandler {
	public QuestActionHandler() {
	}

	public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
		byte action = slea.readByte();
		short quest = slea.readShort();
		MapleCharacter player = c.getPlayer();
		//System.out.println("quest action: " + action);
		if (action == 1) { // start quest
			int npc = slea.readInt();
			slea.readInt(); // dont know *o*
			MapleQuest.getInstance(quest).start(player, npc);
		} else if (action == 2) { // complete quest
			int npc = slea.readInt();
			slea.readInt(); // dont know *o*
			if (slea.available() >= 4) {
				int selection = slea.readInt();
				MapleQuest.getInstance(quest).complete(player, npc, selection);
			} else {
				MapleQuest.getInstance(quest).complete(player, npc);
			}
			// c.getSession().write(MaplePacketCreator.completeQuest(c.getPlayer(), quest));
			//c.getSession().write(MaplePacketCreator.updateQuestInfo(c.getPlayer(), quest, npc, (byte)14));
			// 6 = start quest
			// 7 = unknown error
			// 8 = equip is full
			// 9 = not enough mesos
			// 11 = due to the equipment currently being worn wtf o.o
			// 12 = you may not posess more than one of this item
		} else if (action == 3) { // forfeit quest
			MapleQuest.getInstance(quest).forfeit(player);
		} else if (action == 4) { // scripted start quest
			int npc = slea.readInt();
			slea.readInt(); // dont know *o*
			QuestScriptManager.getInstance().start(c, npc, quest);
		} else if (action == 5) { // scripted end quests
			int npc = slea.readInt();
			slea.readInt(); // dont know *o*
			QuestScriptManager.getInstance().end(c, npc, quest);
		}
	}
}
