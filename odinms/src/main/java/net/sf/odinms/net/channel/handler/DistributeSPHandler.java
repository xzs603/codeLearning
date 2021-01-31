package net.sf.odinms.net.channel.handler;

import net.sf.odinms.client.ISkill;
import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.client.MapleClient;
import net.sf.odinms.client.MapleStat;
import net.sf.odinms.client.SkillFactory;
import net.sf.odinms.net.AbstractMaplePacketHandler;
import net.sf.odinms.tools.data.input.SeekableLittleEndianAccessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistributeSPHandler extends AbstractMaplePacketHandler {

    private static Logger log = LoggerFactory.getLogger(DistributeSPHandler.class);

    private class SP {

        private int id;
        private MapleClient c;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public MapleClient getClient() {
            return c;
        }

        public void setClient(MapleClient c) {
            this.c = c;
        }
    }

    private void addSP(SP sp) {
        int skillid = sp.getId();
        MapleCharacter player = sp.getClient().getPlayer();
        int remainingSp = player.getRemainingSp();
        boolean isBeginnerSkill = false;
        if (skillid<1000 || (skillid>1003 && skillid<2000)) {
            log.info("[h4x] Player {} illegal distribution of SP to unavailable skills", player.getName(), Integer.valueOf(skillid));
            return;
        }
        if (skillid == 1000 || skillid == 1001 || skillid == 1002) {
            int snailsLevel = player.getSkillLevel(SkillFactory.getSkill(1000));
            int recoveryLevel = player.getSkillLevel(SkillFactory.getSkill(1001));
            int nimbleFeetLevel = player.getSkillLevel(SkillFactory.getSkill(1002));
            remainingSp = Math.min((player.getLevel() - 1), 6) - snailsLevel - recoveryLevel - nimbleFeetLevel;
            isBeginnerSkill = true;
        }
        ISkill skill = SkillFactory.getSkill(skillid);
        int maxlevel = skill.isFourthJob() ? player.getMasterLevel(skill) : skill.getMaxLevel();
        int curLevel = player.getSkillLevel(skill);
        if ((remainingSp > 0 && curLevel + 1 <= maxlevel) && skill.canBeLearnedBy(player.getJob())) {
            if (!isBeginnerSkill) {
                player.setRemainingSp(player.getRemainingSp() - 1);
            }
            player.updateSingleStat(MapleStat.AVAILABLESP, player.getRemainingSp());
            player.changeSkillLevel(skill, curLevel + 1, player.getMasterLevel(skill));
        } else if (!skill.canBeLearnedBy(player.getJob())) {
            log.info("[h4x] Trying to learn a skill for a different job (" + player.getJob().name() + ":" + skillid + ")", player.getName(), Integer.valueOf(skillid));
            return;
        } else if (!(remainingSp > 0 && curLevel + 1 <= maxlevel)) {
            log.info("[h4x] Player {} is distributing SP to {} without having any", player.getName(), Integer.valueOf(skillid));
            return;
        }
    }

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        slea.readInt();
        SP sp = new SP();
        sp.setClient(c);
        sp.setId(slea.readInt());
        addSP(sp);
    }
}
