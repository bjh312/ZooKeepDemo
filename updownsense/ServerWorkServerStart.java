package updownsense;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 *	用来处理服务器上下线往zookeeper写入或者删除数据用的。
 *	它就是我们抽象出来的一个台服务器，用于服务器上线
 */
public class ServerWorkServerStart {

	private static final String connectString = "hadoop02:2181,hadoop03:2181,hadoop04:2181";

	private static final int sessionTimeout = 4000;

	// 上线的服务器列表的父节点
	private static final String PARENT_NODE = "/servers";

	// 上线服务器
	private static final String server = "hadoop03";
	private static final String SUB_NODE = server;

	public static void main(String[] args) throws Exception {

		// 拿zookeeper的请求链接
		ZooKeeper zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {

			}
		});

		/**
		 * 为了在第一台服务器上线的时候找不到父节点，去先把这个父节点给创建出来
		 */
		Stat exists = zk.exists(PARENT_NODE, false);
		if (exists == null) {
			zk.create(PARENT_NODE, "servers_parent_node".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}

		// 拿到zookeeper链接之后，就往我们的父znode节点下去写入一台服务器的信息，写入
		// 的znode类型是临时节点
		zk.create(PARENT_NODE + "/" + SUB_NODE, server.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

		System.out.println(server + " is online。。。。。。。。。");

		zk.close();
	}
}
