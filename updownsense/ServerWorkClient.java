package updownsense;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

/**
 * 这就是用户的客户端程序，用来监控服务器集群到底有哪些服务器在工作
 */
public class ServerWorkClient {

	private static final String connectString = "hadoop02:2181,hadoop03:2181,hadoop04:2181";

	private static final int sessionTimeout = 4000;

	private static final String PARENT_NODE = "/servers";
	
	static ZooKeeper zk = null;

	public static void main(String[] args) throws Exception {

		/**
		 * 1、拿zookeeper的服务器链接
		 */
		zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
	
				@Override
				public void process(WatchedEvent event) {
	
					String path = event.getPath();
					EventType et = event.getType();
					/**
					 * 收到监听通知的时候，就去查询servers节点下有哪些服务器在线
					 */
					if(path.equals(PARENT_NODE) && et == EventType.NodeChildrenChanged){
						
						/**
						 * 2、获取父节点servers下有哪些子节点，并且把子节点的数据给拿出来
						 */
						List<String> hostList = getServerList();
						
						// [hadoop01,hadoop02]
						System.out.println(hostList);
					}else{
						System.out.println("当前事件跟我服务器动态上下线没有任何关系");
					}
					
				}
		});
		
		zk.getChildren(PARENT_NODE, true);

		Thread.sleep(Long.MAX_VALUE);
		
		zk.close();
	}
	
	/**
	 * 用来获取当前在线服务器列表
	 */
	public static List<String> getServerList(){
		List<String> hostList = new ArrayList<String>();
		
		try {
			// 这句话是为了获取servers节点下的子节点
			List<String> children = zk.getChildren(PARENT_NODE, true);
			
			for(String znode: children){
				byte[] data = zk.getData(PARENT_NODE+"/"+znode,false, null);
				hostList.add(new String(data));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return hostList;
 	}
}
