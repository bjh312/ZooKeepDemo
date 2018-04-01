package sync_queue;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 作用：用来记录每一台上线的服务器
 * 具体做法：往zookeeper的文件系统里面的某一个znode下面写入上线了的服务器的信息
 */
public class SyncQueueServer {

	private static final String CONNECT_STRING = "hadoop03:2181,hadoop02:2181";

	private static final int sessionTimeout = 4000;

	private static final String PARENT_NODE = "/syncQueue";

	private static final String HOSTNAME = "hadoop05";

	public static void main(String[] args) throws Exception {

		/**
		 * 1、获取zookeeper的链接
		 */
		ZooKeeper zk = new ZooKeeper(CONNECT_STRING, sessionTimeout, new Watcher() {

			/**
			 * 注意：这个监听器会监听zookeeper所有的事件
			 */
			@Override
			public void process(WatchedEvent event) {

			}
		});

		/**
		 * 2、先判断父节点是否存在
		 */
		Stat exists = zk.exists(PARENT_NODE, false);
		if (exists == null) {
			zk.create(PARENT_NODE, PARENT_NODE.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} else {
			System.out.println(PARENT_NODE + "  已存在，不用我创建");
		}

		/**
		 * 3、往父节点下记录一台刚上线的服务器的信息
		 * 节点的名字：/syncQueue/hadoop01
		 */
		String path = zk.create(PARENT_NODE + "/" + HOSTNAME, HOSTNAME.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("当前上线的服务器是：" + HOSTNAME + ", 当前服务器注册的子节点是：" + path);

		Thread.sleep(Long.MAX_VALUE);

		zk.close();
	}
}