package base.fly.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class FlybaseConfig {
	private static final Path CONFIG_PATH = Paths.get("config/flybase.json");
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	// 飞行空间大小（立方体：以飞行基站为中心的 ±value 格）
	public static int spaceX = 128;
	public static int spaceY = 128;
	public static int spaceZ = 128;

	// 缓降效果时长（秒）
	public static int slowFallingTime = 5;

	public static void load() {
		try {
			if (Files.exists(CONFIG_PATH)) {
				String content = Files.readString(CONFIG_PATH);
				JsonObject json = GSON.fromJson(content, JsonObject.class);
				
				if (json.has("space_x")) {
					spaceX = json.get("space_x").getAsInt();
				}
				if (json.has("space_y")) {
					spaceY = json.get("space_y").getAsInt();
				}
				if (json.has("space_z")) {
					spaceZ = json.get("space_z").getAsInt();
				}
				if (json.has("slow_falling_time")) {
					slowFallingTime = json.get("slow_falling_time").getAsInt();
				}
			} else {
				save();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			
			JsonObject json = new JsonObject();
			json.addProperty("space_x", spaceX);
			json.addProperty("space_y", spaceY);
			json.addProperty("space_z", spaceZ);
			json.addProperty("slow_falling_time", slowFallingTime);
			
			Files.writeString(CONFIG_PATH, GSON.toJson(json));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
