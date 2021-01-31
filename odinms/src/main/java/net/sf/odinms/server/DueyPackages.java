
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
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package net.sf.odinms.server;

import java.util.Calendar;
import net.sf.odinms.client.IItem;

public class DueyPackages {

private String sender = null;
private IItem item = null;
private int mesos = 0;
private int day;
private int quantity = 1;
private int month;
private int year;
private int packageId = 0;

public DueyPackages(int pId, IItem item) {
this.item = item;
this.quantity = item.getQuantity();
packageId = pId;
}

public DueyPackages(int pId) {
this.packageId = pId;
}

public String getSender() {
return sender;
}

public void setSender(String name) {
sender = name;
}

public IItem getItem() {
return item;
}

public int getMesos() {
return mesos;
}

public void setMesos(int set) {
mesos = set;
}

public int getQuantity() {
return quantity;
}

public int getPackageId() {
return packageId;
}

public boolean isExpired() {
Calendar cal1 = Calendar.getInstance();
cal1.set(year, month - 1, day);
long diff = System.currentTimeMillis() - cal1.getTimeInMillis();
int diffDays = (int) Math.abs(diff / (24 * 60 * 60 * 1000));
return diffDays > 30;
}

public long sentTimeInMilliseconds() {
Calendar cal = Calendar.getInstance();
cal.set(year, month, day);
return cal.getTimeInMillis();
}

public void setSentTime(String sentTime) {
day = Integer.parseInt(sentTime.substring(0, 2));
month = Integer.parseInt(sentTime.substring(3, 5));
year = Integer.parseInt(sentTime.substring(6, 10));
}
}
