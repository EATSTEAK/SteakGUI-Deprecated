package tk.itstake.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Blacklist {
	private static HashMap<String, BlackData> map;
	
	public static void init() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월dd일_HH시mm분ss초");
		
		map = new HashMap<String, BlackData>();
		
		for (String type : new String[]{
				"http://list.nickname.mc-blacklist.kr/", 
				"http://list.uuid.mc-blacklist.kr/", 
				"http://list.ip.mc-blacklist.kr/" }) {
			
			try {
				URL url = new URL(type);
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setDoOutput(true);
				
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
				br.readLine();
				
				String line;
				while ((line = br.readLine()) != null) {
					for (String value : line.split("</br>")) {
						if (value.isEmpty() || value.equals(" ") || value.startsWith("ban_date | ")) continue;
						
						String[] data = value.split(" \\| ");
						String date = data[0];
						String ban = data[1];
						String reason = data[2].replaceAll("_", " ");
						String punisher = data[3];
						map.put(ban, new BlackData(format.parse(date), reason, punisher));
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		try {
			URL url = new URL("http://ip.mc-blacklist.kr/");
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setDoOutput(true);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
			String ip = br.readLine().substring(1);
			
			if (map.containsKey(ip)) {
				Bukkit.getConsoleSender().sendMessage("§c#============================#");
				Bukkit.getConsoleSender().sendMessage("귀하의 서버는 블랙리스트에 등록되어");
				Bukkit.getConsoleSender().sendMessage("플러그인에 의해 구동이 제한됩니다.");
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage("§4사유: ");
				Bukkit.getConsoleSender().sendMessage("  \""+map.get(ip).getReason()+"\"");
				Bukkit.getConsoleSender().sendMessage("§c#============================#");
				
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				Bukkit.shutdown();
				
				return;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static boolean contains(OfflinePlayer player) {
		if (Material.getMaterial("DOUBLE_PLANT") != null && map.containsKey(player.getUniqueId().toString())) return true;
		if (map.containsKey(player.getName())) return true;
		
		if (player.isOnline()) {
			Player p = player.getPlayer();
			
			if (map.containsKey(p.getAddress().getAddress().getHostAddress())) {
				return true;
			}
		}
		
		return false;
	}
	
	public static BlackData getBlackData(OfflinePlayer player) {
		if (Material.getMaterial("DOUBLE_PLANT") != null && map.containsKey(player.getUniqueId().toString())) return map.get(player.getUniqueId().toString());
		if (map.containsKey(player.getName())) return map.get(player.getName());
		
		if (player.isOnline()) {
			String ip = player.getPlayer().getAddress().getAddress().getHostAddress();
			
			if (map.containsKey(ip)) {
				return map.get(ip);
			}
		}
		
		return null;
	}
	
	public static String kick(Player player) {
		log(player.getName(), player.getUniqueId().toString(), player.getAddress().getHostName(), getBlackData(player).getReason());
		return "§7§l[Blacklist]§r\n§8블랙 리스트에 등록된 사용자입니다!§r\n\n§c§l\"§4"+getBlackData(player).getReason()+"§c§l\"§r";
	}
	
	public static class BlackData {
		private final Date date;
		private final String reason;
		private final String punisher;
		
		public BlackData(Date date, String reason, String punisher) {
			this.date = date;
			this.reason = reason;
			this.punisher = punisher;
		}
		
		public Date getDate() {
			return this.date;
		}
		
		public String getReason() {
			return this.reason;
		}
		
		public String getPunisher() {
			return this.punisher;
		}
	}

	public static void log(String nickname, String uuid, String ip, String reason) {
		String link = "http://horyu.cafe24.com/Minecraft/Plugin/Blacklist/log.php?nickname="+nickname+"&ip="+ip+"&reason="+reason;
		if (uuid != null) link += "&uuid="+uuid;

		try {
			URL url = new URL(link);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setDoOutput(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}