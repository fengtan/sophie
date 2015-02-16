package obsolete;

import com.github.fengtan.solrgui.SolrGUIConfig;


public class Main {

	public static void main(String[] args) {
		Dashboard dashboard = new Dashboard();
		for (Server server:SolrGUIConfig.getServers()) {
			dashboard.addServer(server);
		}
		dashboard.display();
	}
	
}
