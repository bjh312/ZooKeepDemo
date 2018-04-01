package api_demo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * 1、级联查看某节点下所有节点及节点值
 * 2、删除一个节点，不管有有没有任何子节点
 * 3、级联创建任意节点
 * 4、清空子节点
 */
public class ZKUtil2 {
	
	/**
	 * 级联查看某节点下所有节点及节点值
	 */
	public static Map<String, String> getChildNodeAndValue(ZooKeeper zk,String path) throws Exception{
		LinkedHashMap<String, String> nodes = new LinkedHashMap<>();
		Stat exists = zk.exists(path, null);
		if(exists==null){
			return null;
		}else{
			byte[] data = zk.getData(path, false, null);
			nodes.put(path, new String(data));
			
			List<String> childrens = zk.getChildren(path, false);
			if(childrens.size()>0){
				for(String child:childrens){
					String childPath=null;
					if(path=="/"){
						childPath = path+child;
					}else{
						childPath = path+"/"+child;
					}
					byte[] chidlData = zk.getData(childPath, null, null);
					nodes.put(childPath, new String(chidlData));
				}
			}
			return nodes;
		}
		
	}

	/**
	 * 删除一个节点，不管有有没有任何子节点
	 */
	public static boolean rmr(String path, ZooKeeper zk) throws Exception {
		List<String> children = zk.getChildren(path, false);
		if (children.size() == 0) {
			// 删除节点
			zk.delete(path, -1);
		} else {
			// 要删除这个有子节点的父节点，那么就需要先删除所有子节点，
			// 然后再删除该父节点，完成对该节点的级联删除
			// 删除有子节点的父节点下的所有子节点
			for (String nodeName : children) {
				rmr(path + "/" + nodeName, zk);
			}
			// 删除该父节点
			rmr(path, zk);
		}
		return true;
	}

	/**
	 * 级联创建任意节点
	 */
	public static boolean createZNode(String znodePath, String data, ZooKeeper zk) throws Exception {
		// 首先判断该节点是否存在，如果存在，则不创建， return false
		if (zk.exists(znodePath, null) != null) {
			return false;
		} else {
			try {
				// 直接创建，如果抛异常，则捕捉异常，然后根据对应的异常如果是发现没有父节点，那么就创建父节点
				zk.create(znodePath, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} catch (KeeperException e) {
				// 截取父节点
				String parentPath = znodePath.substring(0, znodePath.lastIndexOf("/"));
				// 创建父节点
				createZNode(parentPath, parentPath, zk);
				try {
					// 父节点创建好了之后，创建该节点
					zk.create(znodePath, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				} catch (KeeperException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 清空子节点
	 */
	public static boolean clearChildNode(String znodePath, ZooKeeper zk) throws Exception {
		List<String> children = zk.getChildren(znodePath, null);
		for (String child : children) {
			String childNode = znodePath + "/" + child;
			if (zk.getChildren(childNode, null).size() != 0) {
				clearChildNode(childNode, zk);
			}
			zk.delete(childNode, -1);
		}
		return true;
	}
}
