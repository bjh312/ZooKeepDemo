package configserver;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 描述：
 * 		作用： 模拟一个真正用来对配置信息进行 添加 修改 删除的操作的 客户端
 */
public class Client {
	
	private static final String Connect_String = "hadoop02:2181,hadoop03:2181,hadoop04:2181,hadoop05:2181";

	private static final int Session_Timeout = 4000;
	
	private static final String PARENT_NODE = "/config";
	
	private static final String key = "name";
	private static final String value = "huangbo";
	private static final String value_new = "huangbo_copy";
	

	public static void main(String[] args) throws Exception {
		
		
		/**
		 * 第一步：  获取 zookeeper 连接
		 */
		ZooKeeper zk = new ZooKeeper(Connect_String, Session_Timeout, null);
		
		
		/**
		 * 第二步： 先判断 /config 节点存在与否
		 */
		Stat exists = zk.exists(PARENT_NODE, null);
		if(exists == null){
			zk.create(PARENT_NODE, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		
		
		/**
		 * 第三部： 模拟实现 添加 修改 删除
		 */
		String path = PARENT_NODE + "/" + key;
		
		// 增加一项配置
//		zk.create(path , value.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		// 删除一项配置
//		zk.delete(path, -1);
		
		// 修改一项配置
		zk.setData(path, value_new.getBytes(), -1);
		
		zk.close();
	}
}
