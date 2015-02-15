package obsolete;

import com.github.fengtan.solrgui.Config;


public class Main {

	public static void main(String[] args) {
		Dashboard dashboard = new Dashboard();
		for (Server server:Config.getServers()) {
			dashboard.addServer(server);
		}
		dashboard.display();
	}
	
}
