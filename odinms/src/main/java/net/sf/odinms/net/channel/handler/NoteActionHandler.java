/*
	This file is part of the OdinMS Maple Story Server
	Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
			   Matthias Butz <matze@odinms.de>
			   Jan Christian Meyer <vimes@odinms.de>

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU Affero General Public License version 3
	as published by the Free Software Foundation. You may not use, modify
	or distribute this program under any other version of the
	GNU Affero General Public License.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU Affero General Public License for more details.

	You should have received a copy of the GNU Affero General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
	
	@author Coal
 */

package net.sf.odinms.net.channel.handler;

import java.sql.SQLException;

import net.sf.odinms.client.MapleClient;
import net.sf.odinms.tools.data.input.SeekableLittleEndianAccessor;
import net.sf.odinms.net.AbstractMaplePacketHandler;


public class NoteActionHandler extends AbstractMaplePacketHandler {

	// Create a new instance
	public NoteActionHandler() {
	}

	@Override
	public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {

		int action = slea.readByte();
		
		if (action == 1) { //delete
			int num = slea.readByte();
			slea.readByte(); //padding?
			slea.readByte(); //always 62?
			for (int i = 0; i < num; i++) {
				int id = slea.readInt();
				slea.readByte();
				
				try {
					c.getPlayer().deleteNote(id);
				} catch (SQLException e) {}
			}
		}
	}
}