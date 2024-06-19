package com.mrbreaknfix;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mrbreaknfix.event.EventManager;
import com.mrbreaknfix.event.Sub;
import com.mrbreaknfix.event.events.ChatMessageEvent;
import com.mrbreaknfix.event.events.ScreenEvent;
import com.mrbreaknfix.event.events.TickEvent;
import com.mrbreaknfix.gui.windows.ServerInfoWindow;
import com.mrbreaknfix.scripting.Server;
import com.mrbreaknfix.scripting.ws.MessageSender;
import com.mrbreaknfix.scripting.ws.WSServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Dev implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("DevUtils");
	public static MinecraftClient mc = MinecraftClient.getInstance();
	public static Slot currentSlot = null;
	public static Slot lastClickedSlot = null;
	public static int selectedSlot = 0;
	public static boolean showSlotIDs;
	public static boolean highlightLastClickedSlotID;
	public static boolean highlightSlotIDsOnHover;
	public static boolean showSelectedSlot;
	public static String url;
	public static int serverPort;
	public static int webSocketPort;
    public Gson gson = new Gson();
	// SlotID, center of slot (x, y)
	public static Map<Integer, int[]> slotPositions = new HashMap<>();
	// EventManager
	public static EventManager EventBus = new EventManager();
	public static MessageSender ws = new MessageSender();



	@Override
	public void onInitialize() {
		LOGGER.info("Initializing DevUtils");
		// Register events
		EventBus.addListener(this);
		// Register the ServerInfoWindow to the EventBus
		EventBus.addListener(new ServerInfoWindow());

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			EventBus.trigger(new TickEvent.Post());
			ws.send("tick");
		});

		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			EventBus.trigger(new ScreenEvent.AfterInit());
		});

		ScreenEvents.BEFORE_INIT.register((client, screen, width, height) -> {
			EventBus.trigger(new ScreenEvent.BeforeInit());
		});

		// Singleton design pattern anyone?
		Server.instance.start();
		WSServer.getInstance().startServer();
	}

	@Sub
	public void screenEvents(ScreenEvent event) {
		ws.send("screen:" + event.getClass().getSimpleName());
	}

	@Sub
	public void onChatMsgReceived(ChatMessageEvent msg) {
		ws.send("chat:" + msg);
	}


	public static String[] tailLogFile(int lastLines) {
		File logFile = new File("logs/latest.log");
		if (!logFile.exists()) {
			return new String[0];
		}
		try {
			String log = Files.readString(logFile.toPath());
			String[] lines = log.split("\n");
			int start = Math.max(0, lines.length - lastLines);
			return java.util.Arrays.copyOfRange(lines, start, lines.length);
		} catch (Exception e) {
			LOGGER.error("Error reading log file: ", e);
			return new String[0];
		}
	}

	public void saveBoolToGson(String key, boolean value) {
		// get dev.json file
		File file = new File("dev.json");
		// check if file exists
		if (!file.exists()) {
			try {
				// create file if it doesn't exist
				file.createNewFile();
			} catch (Exception e) {
				LOGGER.error("Error creating dev.json file: ", e);
			}
		}
		// read file
		try {
			String json = Files.readString(file.toPath());
			Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
			map.put(key, value);

			String newJson = gson.toJson(map);
			Files.write(file.toPath(), newJson.getBytes());
		} catch (Exception e) {
			LOGGER.error("Error reading dev.json file: ", e);
		}
	}

	public boolean getBoolFromGson(String key) {
		File file = new File("dev.json");
		if (!file.exists()) {
			return false;
		}
		try {
			String json = Files.readString(file.toPath());
			Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
			return (boolean) map.get(key);
		} catch (Exception e) {
			LOGGER.error("Error reading dev.json file: ", e);
			return false;
		}
	}
	//todo: web server api thing from mcutils
}