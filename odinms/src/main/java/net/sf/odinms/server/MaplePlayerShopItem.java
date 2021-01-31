// Jiongorz. 娱乐端 原端【ThePack82】
// QQ:307586193
// 囧囧技术交流论坛:http://bbs.jiongorz.com 【已开放】
// Jiongorz. 娱乐端 编译源Src 请保留信息、版权
// 此端已大部分修改 版权为：Jiongorz.©
package net.sf.odinms.server;

import net.sf.odinms.client.IItem;

// Referenced classes of package net.sf.odinms.server:
//			MaplePlayerShop

public class MaplePlayerShopItem
{

	private IItem item;
	private MaplePlayerShop shop;
	private short bundles;
	private int price;

	public MaplePlayerShopItem(MaplePlayerShop shop, IItem item, short bundles, int price)
	{
		this.shop = shop;
		this.item = item;
		this.bundles = bundles;
		this.price = price;
	}

	public IItem getItem()
	{
		return item;
	}

	public MaplePlayerShop getShop()
	{
		return shop;
	}

	public short getBundles()
	{
		return bundles;
	}

	public int getPrice()
	{
		return price;
	}

	public void setBundles(short bundles)
	{
		this.bundles = bundles;
	}
}
