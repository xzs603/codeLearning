package net.sf.odinms.net.channel.handler;



import net.sf.odinms.client.*;
import net.sf.odinms.net.AbstractMaplePacketHandler;

import net.sf.odinms.scripting.npc.NPCScriptManager;
import net.sf.odinms.tools.MaplePacketCreator;
import net.sf.odinms.tools.data.input.SeekableLittleEndianAccessor;

public class EnterMTSHandler extends AbstractMaplePacketHandler {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DistributeSPHandler.class);

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
            NPCScriptManager.getInstance().start(c, 9310008, null, null);
            c.getSession().write(MaplePacketCreator.enableActions());
        }
}
