/** 
* 
*/
package api_demo;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**  
 * 描述: ZooKeeper的简单API测试方法
 */
public class ZKUtil {

	/**
	 * 获取ZooKeeper链接
	 * @return
	 * @throws Exception
	 */
	public static ZooKeeper getZKConnection(String connectString, int sessionTimeout) throws Exception {
		return new ZooKeeper(connectString, sessionTimeout, null);
	}

	/**
	 * 创建节点
	 * @throws Exception 
	 */
	public static String createZKNode(String path, String value, ZooKeeper zk) throws KeeperException, Exception {
		String create = zk.create(path, value.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		return create;
	}

	/**
	 * 查看节点数据
	 * @throws Exception 
	 */
	public static String getZNodeData(String path, ZooKeeper zk) throws Exception {
		return new String(zk.getData(path, false, null));
	}
	
	/**
	 * 修改节点数据
	 * @throws Exception 
	 */
	public static boolean updateZNodeData(String path, String value, ZooKeeper zk) throws Exception{
		zk.setData(path, value.getBytes(), -1);
		String newData = new String(zk.getData(path, false, null));
		if(newData.equals(value)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 判断节点是否存在
	 * @throws Exception 
	 */
	public static boolean existsZNode(String path, ZooKeeper zk) throws Exception {
		return (zk.exists(path, false) != null) ? true : false;
	}

	/**
	 * 查看节点的子节点列表
	 * @throws Exception 
	 */
	public static List<String> getChildrenZNodes(String path, ZooKeeper zk) throws Exception {
		return zk.getChildren(path, false);
	}

}
