/** 
* 
*/
package api_demo;

import java.util.List;

import org.apache.zookeeper.ZooKeeper;

/**  
 * 描述: ZooKeeper API 测试类
 */
public class ZKUtilTest {

	// 获取zookeeper连接时所需要的服务器连接信息，格式为主机名：端口号
	private static final String ConnectString = "hadoop03:2181,hadoop04:2181,hadoop05:2181";

	// 请求了解的会话超时时长
	private static final int SessionTimeout = 5000;

	public static void main(String[] args) throws Exception {

		ZooKeeper zk = ZKUtil.getZKConnection(ConnectString, SessionTimeout);

		String path = "/aa";
		String value = "aa";

		// 创建节点
		String createZKNode = ZKUtil.createZKNode("/aa/ee/aa", value, zk);
		System.out.println(createZKNode + "\t" + createZKNode != null ? "创建节点成功" : "创建节点失败");

		// 获取节点数据
		System.out.println(ZKUtil.getZNodeData(path, zk));

		// 判断节点存在不存在
		System.out.println(ZKUtil.existsZNode(path, zk));

		// 获取子节点列表
		List<String> childrenZNodes = ZKUtil.getChildrenZNodes(path, zk);
		for (String znodePath : childrenZNodes) {
			System.out.println(znodePath);
		}

		// 修改节点数据
		System.out.println(ZKUtil.updateZNodeData(path, "abcd", zk));
	}
}
